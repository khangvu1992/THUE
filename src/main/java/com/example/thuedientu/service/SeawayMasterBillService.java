package com.example.thuedientu.service;

import com.example.thuedientu.model.HashFile;
import com.example.thuedientu.model.SeawayMasterBillEntity;
import com.example.thuedientu.repository.FileRepository;
import com.example.thuedientu.util.*;
import com.monitorjbl.xlsx.StreamingReader;
import com.opencsv.CSVReader;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.io.*;
import java.sql.Timestamp;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

@Service
public class SeawayMasterBillService {

    @Autowired
    private ExcelDataFormatterService formatterService;

    private final JdbcTemplate jdbcTemplate;
    private final int BATCH_SIZE = 5000;
    private final int WORKER_COUNT = 4;

    //    @Autowired private insertDataBatchService insertDataBatchService1;
    @Autowired
    private FileRepository fileRepository;
    @Autowired
    private ExcelProcessingService excelProcessingService;

    @Autowired
    private mapEntitySeawayMasterContext mapEntitySeawayMasterContext;
    @Autowired
    private SeawayMasterQueueManager fileQueueManager;
    @Autowired
    private ProgressWebSocketSender progressWebSocketSender;

    public SeawayMasterBillService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void import1Datbase1JDBC1(File file, HashFile hashFile) {
        String fileId = hashFile.getFileHash();
        String filename = hashFile.getFilename();
        fileQueueManager.removePendingFile(filename);
        createTable();
        fileQueueManager.createContext(fileId, 50, filename);

        for (int i = 0; i < WORKER_COUNT; i++) {
            new Thread(() -> workerWriteToDb(fileId, filename), "worker-" + i + "-" + filename).start();
            System.out.println("worker-" + i + "-" + fileId);
        }

        readCsvAndEnqueue(file, fileId, filename);

        fileQueueManager.markReadingDone(fileId);

        file.delete();
        fileRepository.save(hashFile);
        System.out.println("🧹 Xoá file tạm: " + file.getAbsolutePath());
    }

    private void workerWriteToDb(String fileId, String filename) {
        BlockingQueue<List<SeawayMasterBillEntity>> queue = fileQueueManager.getQueue(fileId);

        while (true) {
            try {
                List<SeawayMasterBillEntity> batch = queue.poll(5, TimeUnit.SECONDS);
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
                fileQueueManager.setErrorMessage("❌ Lỗi khi import file: " + filename + " - " + e.getMessage());

            }
        }

        System.out.println(Thread.currentThread().getName() + " done!");
        progressWebSocketSender.sendProgress1(fileId, filename, 100, true);

        fileQueueManager.removeContext(fileId);


    }

    public void createTableIfNotExists() {
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
        String sql = "IF OBJECT_ID('dbo.seaway_master_bill', 'U') IS NULL " +
                "BEGIN " +
                "CREATE TABLE seaway_master_bill (" +
                "id BIGINT IDENTITY PRIMARY KEY, " +
                "SoKhaiBao NVARCHAR(255), " +
                "SoHoSo NVARCHAR(255), " +
                "LoaiHoSo NVARCHAR(255), " +
                "ArrivalDate SMALLDATETIME, " +
                "CangTiepNhan NVARCHAR(500), " +
                "NgayGui SMALLDATETIME, " +
                "TenTau NVARCHAR(500), " +
                "SoIMO NVARCHAR(255), " +
                "HangTau NVARCHAR(255), " +
                "NgayTauDenRoi SMALLDATETIME, " +
                "NgayDenRoi SMALLDATETIME, " +
                "CangRoiCuoiCungCangDich NVARCHAR(255), " +
                "Consignee NVARCHAR(4000), " +
                "Consigner NVARCHAR(4000), " +
                "NotificatedParty NVARCHAR(4000), " +
                "NotificatedParty2 NVARCHAR(4000), " +
                "MasterBillNo NVARCHAR(225), " +
                "ContNumber NVARCHAR(255), " +
                "ContSealNumber NVARCHAR(225), " +
                "GoodDescription NVARCHAR(4000), " +
                "CangXepHangGoc NVARCHAR(500), " +
                "CangXepHang NVARCHAR(500), " +
                "CangDoHang NVARCHAR(500), " +
                "CangDich NVARCHAR(500), " +
                "TenCangDich NVARCHAR(500), " +
                "DiaDiemDoHang NVARCHAR(500), " +
                "NetWeight DECIMAL(20,3), " +
                "GrossWeight DECIMAL(20,3),  " +
                "Demension DECIMAL(20,3), " +
                ") " +
                "END";

        jdbcTemplate.execute(sql);
    }

    //    @Transactional
    public void insertDataBatch(List<SeawayMasterBillEntity> batchList) {
        String insertSQL = "INSERT INTO seaway_master_bill( " +
                "SoKhaiBao, " +
                "SoHoSo, " +
                "LoaiHoSo, " +
                "ArrivalDate, " +
                "CangTiepNhan, " +
                "NgayGui, " +
                "TenTau, " +
                "SoIMO, " +
                "HangTau, " +
                "NgayTauDenRoi, " +
                "NgayDenRoi, " +
                "CangRoiCuoiCungCangDich, " +
                "Consignee, " +
                "Consigner, " +
                "NotificatedParty, " +
                "NotificatedParty2, " +
                "MasterBillNo, " +
                "ContNumber, " +
                "ContSealNumber, " +
                "GoodDescription, " +
                "CangXepHangGoc, " +
                "CangXepHang, " +
                "CangDoHang, " +
                "CangDich, " +
                "TenCangDich, " +
                "DiaDiemDoHang, " +
                "NetWeight, " +
                "GrossWeight, " +
                "Demension ) " +
                "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";


        jdbcTemplate.batchUpdate(insertSQL, batchList, batchList.size(), (ps, e) -> {

            ps.setString(1, e.getSoKhaiBao());
            ps.setString(2, e.getSoHoSo());
            ps.setString(3, e.getLoaiHoSo());
            ps.setTimestamp(4, e.getArrivalDate());
            ps.setString(5, e.getCangTiepNhan());
            ps.setTimestamp(6, e.getNgayGui());
            ps.setString(7, e.getTenTau());
            ps.setString(8, e.getSoIMO());
            ps.setString(9, e.getHangTau());
            ps.setTimestamp(10, e.getNgayTauDenRoi());
            ps.setTimestamp(11, e.getNgayDenRoi());
            ps.setString(12, e.getCangRoiCuoiCungCangDich());
            ps.setString(13, e.getConsignee());
            ps.setString(14, e.getConsigner());
            ps.setString(15, e.getNotificatedParty());
            ps.setString(16, e.getNotificatedParty2());
            ps.setString(17, e.getMasterBillNo());
            ps.setString(18, e.getContNumber());
            ps.setString(19, e.getContSealNumber());
            ps.setString(20, e.getGoodDescription());
            ps.setString(21, e.getCangXepHangGoc());
            ps.setString(22, e.getCangXepHang());
            ps.setString(23, e.getCangDoHang());
            ps.setString(24, e.getCangDich());
            ps.setString(25, e.getTenCangDich());
            ps.setString(26, e.getDiaDiemDoHang());
            ps.setBigDecimal(27, e.getNetWeight());
            ps.setBigDecimal(28, e.getGrossWeight());
            ps.setBigDecimal(29, e.getDemension());

        });
    }

//    private void readExcelAndEnqueue(File file, String fileId,String filename) {
//
//        // 1. Đếm tổng số dòng (trừ header nếu cần)
////        int totalRows = excelProcessingService.countTotalRows(file);
//
//
//        try (InputStream is = new FileInputStream(file);
//             Workbook workbook = StreamingReader.builder()
//                     .rowCacheSize(100)
//                     .bufferSize(4096)
//                     .open(is)) {
//
//            Sheet sheet = workbook.getSheetAt(0);
//            Iterator<Row> rows = sheet.iterator();
//            if (rows.hasNext()) rows.next(); // skip header
//
//            List<SeawayMasterBillEntity> batch = new ArrayList<>();
//            int count = 0;
//
//            while (rows.hasNext()) {
//                Row row = rows.next();
//                SeawayMasterBillEntity entity = new SeawayMasterBillEntity();
//                mapEntitySeawayMasterContext.mapRowToEntity(row, entity);
//                batch.add(entity);
//                count++;
//
//                if (batch.size() >= BATCH_SIZE) {
//                    fileQueueManager.getQueue(fileId).put(new ArrayList<>(batch));
//                    batch.clear();
//                }
//
//                if (count % 10000 == 0) {
////                    fileQueueManager.logWaitingFiles();
//                    int progress = (int) count*100 / 1048576 ;
//
//                    progressWebSocketSender.sendProgress1(fileId,filename, progress,false);
//

    /// /                    System.out.println("Progress: " + count);
//                }
//            }
//
//            if (!batch.isEmpty()) {
//                fileQueueManager.getQueue(fileId).put(batch);
//            }
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

//
//    private void readCsvAndEnqueue(File file, String fileId, String filename) {
//        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
//            String line;
//            boolean skipHeader = true;
//            List<SeawayMasterBillEntity> batch = new ArrayList<>();
//            int count = 0;
//
//            while ((line = reader.readLine()) != null) {
//                if (skipHeader) {
//                    skipHeader = false;
//                    continue; // Bỏ qua header
//                }
//
//                String[] tokens = line.split(",", -1); // tách cột, giữ giá trị rỗng nếu có
//
//                SeawayMasterBillEntity entity = new SeawayMasterBillEntity();
//                mapEntitySeawayMasterContext.mapCsvRowToEntity(tokens, entity); // bạn phải viết hàm này tương tự mapRowToEntity
//                batch.add(entity);
//                count++;
//
//                if (batch.size() >= BATCH_SIZE) {
//                    fileQueueManager.getQueue(fileId).put(new ArrayList<>(batch));
//                    batch.clear();
//                }
//
//                if (count % 10000 == 0) {
//                    int progress = (int) (count * 100L / 1048576); // giả sử tổng 1,048,576 dòng
//                    progressWebSocketSender.sendProgress1(fileId, filename, progress, false);
//                }
//            }
//
//            if (!batch.isEmpty()) {
//                fileQueueManager.getQueue(fileId).put(batch);
//            }
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
    private void readCsvAndEnqueue(File file, String fileId, String filename) {
        try (CSVReader csvReader = new CSVReader(new FileReader(file))) {
            String[] tokens;
            boolean skipHeader = true;
            List<SeawayMasterBillEntity> batch = new ArrayList<>();
            int count = 0;

            while ((tokens = csvReader.readNext()) != null) {
                if (skipHeader) {
                    skipHeader = false;
                    continue; // Bỏ qua header
                }

                SeawayMasterBillEntity entity = new SeawayMasterBillEntity();
                mapEntitySeawayMasterContext.mapCsvRowToEntity(tokens, entity);
                batch.add(entity);
                count++;

                if (batch.size() >= BATCH_SIZE) {
                    fileQueueManager.getQueue(fileId).put(new ArrayList<>(batch));
                    batch.clear();
                }

                if (count % 10000 == 0) {
                    int progress = (int) (count * 100L / 1048576); // giả sử 1,048,576 dòng
                    progressWebSocketSender.sendProgress1(fileId, filename, progress, false);
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