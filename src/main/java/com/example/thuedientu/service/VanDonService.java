package com.example.thuedientu.service;

import com.example.thuedientu.model.HashFile;
import com.example.thuedientu.model.VanDonEntity;
import com.example.thuedientu.repository.FileRepository;
import com.example.thuedientu.util.*;
import com.example.thuedientu.utilExcel.VanDonQueueManager;
import com.example.thuedientu.utilExcel.mapEntityVanDon;
import com.monitorjbl.xlsx.StreamingReader;
import org.apache.poi.ss.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

@Service
public class VanDonService {

    @Autowired
    private ExcelDataFormatterService formatterService;

    private final JdbcTemplate jdbcTemplate;
    private final int BATCH_SIZE = 5000;
    private final int WORKER_COUNT = 4;

    //    @Autowired private insertDataBatchService insertDataBatchService1;
    @Autowired private FileRepository fileRepository;

    @Autowired private mapEntityVanDon map1EntityJDBC;
    @Autowired private VanDonQueueManager fileQueueManager;
    @Autowired
    private ProgressWebSocketSender progressWebSocketSender;

    public VanDonService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void import1Datbase1JDBC1(File file, HashFile hashFile) {
        createTable();
        String fileId = hashFile.getFileHash();
        String filename = hashFile.getFilename();
        fileQueueManager.removePendingFile(filename);

        fileQueueManager.createContext(fileId, 50, filename);

        for (int i = 0; i < WORKER_COUNT; i++) {
            new Thread(() -> workerWriteToDb(fileId,filename), "worker-" + i + "-" + filename).start();
            System.out.println("worker-" + i + "-" + fileId);
        }

        readExcelAndEnqueue(file, fileId,filename);

        fileQueueManager.markReadingDone(fileId);

        file.delete();
        fileRepository.save(hashFile);
        System.out.println("🧹 Xoá file tạm: " + file.getAbsolutePath());
    }

    private void workerWriteToDb(String fileId,String filename) {
        BlockingQueue<List<VanDonEntity>> queue = fileQueueManager.getQueue(fileId);

        while (true) {
            try {
                List<VanDonEntity> batch = queue.poll(5, TimeUnit.SECONDS);
                if (batch == null) {
                    if (fileQueueManager.isReadingDone(fileId)) break;
                    else continue;
                }

                insertDataBatch(batch);
                fileQueueManager.incrementProcessed(fileId, batch.size());



//                FileContext ctx = fileQueueManager.getContext(fileId);
//                progressWebSocketSender.sendProgress1("fileId, ctx.getFileName(), ctx.getProcessedCount(), ctx.getQueue().size(), ctx.isReadingDone(), ctx.getErrorMessage()");

            } catch (Exception e) {
                e.printStackTrace();
                fileQueueManager.setErrorMessage( "❌ Lỗi khi import file: " + filename + " - " + e.getMessage());

            }
        }

        System.out.println(Thread.currentThread().getName() + " done!");
        progressWebSocketSender.sendProgress1(fileId,filename, 100,true);

        fileQueueManager.removeContext(fileId);


    }

    public void createTableIfNotExists() {

        createTable();

        String sql = """
            IF NOT EXISTS (SELECT * FROM sysobjects WHERE name='hash_files' AND xtype='U')
            CREATE TABLE hash_files (
                id BIGINT IDENTITY(1,1) PRIMARY KEY,
                filename NVARCHAR(255) NOT NULL,
                file_hash NVARCHAR(255) NOT NULL
            );
        """;

        jdbcTemplate.execute(sql);
    }

    public void createTable() {




        String createTableSQL = "IF OBJECT_ID('dbo.van_don', 'U') IS NULL " +
                "BEGIN " +
                "CREATE TABLE dbo.van_don (" +
                "id BIGINT IDENTITY(1,1) PRIMARY KEY, " +

                "B319A_DECNO NVARCHAR(255), B319A_OLCDT DATE, B319A_INEXTP NVARCHAR(255), " +
                "B319A_PURCD NVARCHAR(255), B319A_TYPCD NVARCHAR(255), B319A_CTMCD NVARCHAR(255), " +
                "B319A_ARVCTMCD NVARCHAR(255), B319A_PORTERCD NVARCHAR(255), B319A_PORTERNM NVARCHAR(2000), " +
                "B319A_SCDEPDT DATE, B319A_SCARVDT DATE, B319A_DEPLOC1 NVARCHAR(255), " +
                "B319A_ARVLOC1 NVARCHAR(255), B319A_ROUTE NVARCHAR(2000), B319A_APPDT DATE, " +
                "B319A_INVLDDT DATE , B319A_ACDEPDT DATE, B319A_BOASYSDT DATE, " +
                "B319A_DEPBOAUSR NVARCHAR(255), B319A_ACARVDT DATE, B319A_UPDDT DATE, " +
                "B319A_ARVBIAUSR NVARCHAR(255), " +

                "B319B_SERNO BIGINT, B319B_BLNO NVARCHAR(255), B319B_GDSNM NVARCHAR(4000), " +
                "B319B_HSCD NVARCHAR(255), B319B_ORGNCD NVARCHAR(255), B319B_QUANO BIGINT, " +
                "B319B_QUAUNT NVARCHAR(255), B319B_GRSNO DECIMAL(20,3), B319B_GRSUNT NVARCHAR(255), " +
                "B319B_IMPCD NVARCHAR(255), B319B_IMPNM NVARCHAR(1000), B319B_IMPAD NVARCHAR(4000), " +
                "B319B_EXPCD NVARCHAR(255), B319B_EXPNM NVARCHAR(1000), B319B_EXPAD NVARCHAR(4000), " +
                "B319B_MRKNO NVARCHAR(1000), " +

                "B319C_CNTNO NVARCHAR(255)" +
                "); END";


        jdbcTemplate.execute(createTableSQL);
    }




    public void insertDataBatch(List<VanDonEntity> batchList) {
        String insertSQL = "INSERT INTO van_don (" +
                "B319A_DECNO, B319A_OLCDT, B319A_INEXTP, B319A_PURCD, B319A_TYPCD, B319A_CTMCD, B319A_ARVCTMCD, " +
                "B319A_PORTERCD, B319A_PORTERNM, B319A_SCDEPDT, B319A_SCARVDT, B319A_DEPLOC1, B319A_ARVLOC1, B319A_ROUTE, " +
                "B319A_APPDT, B319A_INVLDDT, B319A_ACDEPDT, B319A_BOASYSDT, B319A_DEPBOAUSR, B319A_ACARVDT, B319A_UPDDT, B319A_ARVBIAUSR, " +
                "B319B_SERNO, B319B_BLNO, B319B_GDSNM, B319B_HSCD, B319B_ORGNCD, B319B_QUANO, B319B_QUAUNT, B319B_GRSNO, B319B_GRSUNT, " +
                "B319B_IMPCD, B319B_IMPNM, B319B_IMPAD, B319B_EXPCD, B319B_EXPNM, B319B_EXPAD, B319B_MRKNO, B319C_CNTNO) " +
                "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

        jdbcTemplate.batchUpdate(insertSQL, batchList, batchList.size(), (ps, dto) -> {
            ps.setString(1, dto.getB319aDecno());
            ps.setDate(2, formatterService.parseSqlDate(dto.getB319aOlcdt()) );
            ps.setString(3, dto.getB319aInextp());
            ps.setString(4, dto.getB319aPurcd());
            ps.setString(5, dto.getB319aTypcd());
            ps.setString(6, dto.getB319aCtmcd());
            ps.setString(7, dto.getB319aArvctmcd());
            ps.setString(8, dto.getB319aPortercd());
            ps.setString(9, dto.getB319aPorternm());
            ps.setDate(10, formatterService.parseSqlDate(dto.getB319aScdepdt()));
            ps.setDate(11, formatterService.parseSqlDate(dto.getB319aScarvdt()));
            ps.setString(12, dto.getB319aDeploc1());
            ps.setString(13, dto.getB319aArvloc1());
            ps.setString(14, dto.getB319aRoute());
            ps.setDate(15, formatterService.parseSqlDate(dto.getB319aAppdt()));
            ps.setDate(16, formatterService.parseSqlDate(dto.getB319aInvlddt()));
            ps.setDate(17,formatterService.parseSqlDate( dto.getB319aAcdepdt()));
            ps.setDate(18,formatterService.parseSqlDate(dto.getB319aBoasysdt()) );
            ps.setString(19, dto.getB319aDepboausr());
            ps.setDate(20, formatterService.parseSqlDate(dto.getB319aAcarvdt()));
            ps.setDate(21,formatterService.parseSqlDate( dto.getB319aUpddt()));
            ps.setString(22, dto.getB319aArvbiausr());

            ps.setInt(23, formatterService.parseInteger(dto.getB319bSerno(),345656456) );
            ps.setString(24, dto.getB319bBlno());
            ps.setString(25, dto.getB319bGdsnm());
            ps.setString(26, dto.getB319bHscd());
            ps.setString(27, dto.getB319bOrgncd());
            ps.setInt(28, formatterService.parseInteger(dto.getB319bQuano(),446546456));
            ps.setString(29, dto.getB319bQuaunt());
            ps.setBigDecimal(30, formatterService.parseBigDecimal(dto.getB319bGrsno(),865656568) );
            ps.setString(31, dto.getB319bGrsunt());
            ps.setString(32, dto.getB319bImpcd());
            ps.setString(33, dto.getB319bImpnm());
            ps.setString(34, dto.getB319bImpad());
            ps.setString(35, dto.getB319bExpcd());
            ps.setString(36, dto.getB319bExpnm());
            ps.setString(37, dto.getB319bExpad());
            ps.setString(38, dto.getB319bMrkno());

            ps.setString(39, dto.getB319cCntno());
        });



    }






    private void readExcelAndEnqueue(File file, String fileId,String filename) {

        // 1. Đếm tổng số dòng (trừ header nếu cần)
//        int totalRows = excelProcessingService.countTotalRows(file);


        try (InputStream is = new FileInputStream(file);
             Workbook workbook = StreamingReader.builder()
                     .rowCacheSize(100)
                     .bufferSize(4096)
                     .open(is)) {

            Sheet sheet = workbook.getSheetAt(0);
            Iterator<Row> rows = sheet.iterator();
            if (rows.hasNext()) rows.next(); // skip header

            List<VanDonEntity> batch = new ArrayList<>();
            int count = 0;

            while (rows.hasNext()) {
                Row row = rows.next();
                VanDonEntity entity = new VanDonEntity();
                map1EntityJDBC.mapRowToEntity(row, entity);
                batch.add(entity);
                count++;

                if (batch.size() >= BATCH_SIZE) {
                    fileQueueManager.getQueue(fileId).put(new ArrayList<>(batch));
                    batch.clear();
                }

                if (count % 10000 == 0) {
//                    fileQueueManager.logWaitingFiles();
                    int progress = (int) count*100 / 1048576 ;

                    progressWebSocketSender.sendProgress1(fileId,filename, progress,false);

//                    System.out.println("Progress: " + count);
                }
            }

            if (!batch.isEmpty()) {
                fileQueueManager.getQueue(fileId).put(batch);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}