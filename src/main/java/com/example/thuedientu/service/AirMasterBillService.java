package com.example.thuedientu.service;

import com.example.thuedientu.model.AirMasterBillEntity;
import com.example.thuedientu.model.HashFile;
import com.example.thuedientu.model.SeawayHouseBillEntity;
import com.example.thuedientu.repository.FileRepository;
import com.example.thuedientu.util.*;
import com.opencsv.CSVReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

@Service
public class AirMasterBillService {

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
    private MapEntityAirMasterContext mapEntityAirMasterContext;
    @Autowired
    private AirMasterQueueManager fileQueueManager;
    @Autowired
    private ProgressWebSocketSender progressWebSocketSender;

    public AirMasterBillService(JdbcTemplate jdbcTemplate) {
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
        BlockingQueue<List<AirMasterBillEntity>> queue = fileQueueManager.getQueue(fileId);

        while (true) {
            try {
                List<AirMasterBillEntity> batch = queue.poll(5, TimeUnit.SECONDS);
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
        String sql = """
        IF OBJECT_ID('dbo.air_master_bill', 'U') IS NULL
        BEGIN
            CREATE TABLE air_master_bill (
                id BIGINT IDENTITY PRIMARY KEY,
                IDCHUYENBAY NVARCHAR(255),
                FLIGHTDATE SMALLDATETIME,
                CARRIER NVARCHAR(4000),
                FLIGHTNO NVARCHAR(4000),
                ORIGIN NVARCHAR(4000),
                DESTINATION NVARCHAR(4000),
                MAWB_NO NVARCHAR(4000),
                M_TENNGUOIGUI NVARCHAR(4000),
                M_TENNGUOINHAN NVARCHAR(4000),
                M_MOTAHANGHOA NVARCHAR(4000),
                M_SOKIEN BIGINT,
                M_GROSSWEIGHT DECIMAL(20,3),
                M_SANBAYDI NVARCHAR(4000),
                M_SANBAYDEN NVARCHAR(4000),
                M_VERSION NVARCHAR(4000),
                H_SOMAWB NVARCHAR(4000),
                H_SOHWB NVARCHAR(4000),
                H_TENNGUOIGUI NVARCHAR(4000),
                H_TENNGUOINHAN NVARCHAR(4000),
                H_SOKIEN INT,
                H_TRONGLUONG DECIMAL(20,3),
                H_NOIDI NVARCHAR(4000),
                H_NOIDEN NVARCHAR(4000),
                H_MOTAHANGHOA NVARCHAR(4000)
            )
        END
    """;

        jdbcTemplate.execute(sql);
    }



    //    @Transactional
    public void insertDataBatch(List<AirMasterBillEntity> batchList) {
        String insertSQL = "INSERT INTO air_master_bill ( " +
                "IDCHUYENBAY, " +
                "FLIGHTDATE, " +
                "CARRIER, " +
                "FLIGHTNO, " +
                "ORIGIN, " +
                "DESTINATION, " +
                "MAWB_NO, " +
                "M_TENNGUOIGUI, " +
                "M_TENNGUOINHAN, " +
                "M_MOTAHANGHOA, " +
                "M_SOKIEN, " +
                "M_GROSSWEIGHT, " +
                "M_SANBAYDI, " +
                "M_SANBAYDEN, " +
                "M_VERSION, " +
                "H_SOMAWB, " +
                "H_SOHWB, " +
                "H_TENNGUOIGUI, " +
                "H_TENNGUOINHAN, " +
                "H_SOKIEN, " +
                "H_TRONGLUONG, " +
                "H_NOIDI, " +
                "H_NOIDEN, " +
                "H_MOTAHANGHOA" +
                ") VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

        jdbcTemplate.batchUpdate(insertSQL, batchList, batchList.size(), (ps, e) -> {
            ps.setString(1, e.getIdChuyenBay());
            ps.setTimestamp(2, e.getFlightDate());
            ps.setString(3, e.getCarrier());
            ps.setString(4, e.getFlightNo());
            ps.setString(5, e.getOrigin());
            ps.setString(6, e.getDestination());
            ps.setString(7, e.getMawbNo());
            ps.setString(8, e.getMTenNguoiGui());
            ps.setString(9, e.getMTenNguoiNhan());
            ps.setString(10, e.getMMoTaHangHoa());
            ps.setObject(11, e.getMSoKien(), java.sql.Types.INTEGER);
            ps.setObject(12, e.getMGrossWeight(), java.sql.Types.DOUBLE);
            ps.setString(13, e.getMSanBayDi());
            ps.setString(14, e.getMSanBayDen());
            ps.setString(15, e.getMVersion());
            ps.setString(16, e.getHSoMawb());
            ps.setString(17, e.getHSoHwb());
            ps.setString(18, e.getHTenNguoiGui());
            ps.setString(19, e.getHTenNguoiNhan());
            ps.setObject(20, e.getHSoKien(), java.sql.Types.INTEGER);
            ps.setObject(21, e.getHTrongLuong(), java.sql.Types.DOUBLE);
            ps.setString(22, e.getHNoiDi());
            ps.setString(23, e.getHNoiDen());
            ps.setString(24, e.getHMoTaHangHoa());
        });
    }


    private void readCsvAndEnqueue(File file, String fileId, String filename) {
        try (CSVReader csvReader = new CSVReader(new FileReader(file))) {
            String[] tokens;
            boolean skipHeader = true;
            List<AirMasterBillEntity> batch = new ArrayList<>();
            int count = 0;

            while ((tokens = csvReader.readNext()) != null) {
                if (skipHeader) {
                    skipHeader = false;
                    continue; // Bỏ qua header
                }

                AirMasterBillEntity entity = new AirMasterBillEntity();
                mapEntityAirMasterContext.mapCsvRowToEntity(tokens, entity);
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