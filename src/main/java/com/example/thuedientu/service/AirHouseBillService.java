package com.example.thuedientu.service;

import com.example.thuedientu.model.AirHouseBillEntity;
import com.example.thuedientu.model.AirMasterBillEntity;
import com.example.thuedientu.model.HashFile;
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
public class AirHouseBillService {

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
    private MapEntityAirHouseContext mapEntityAirHouseContext;
    @Autowired
    private AirHouseQueueManager fileQueueManager;
    @Autowired
    private ProgressWebSocketSender progressWebSocketSender;

    public AirHouseBillService(JdbcTemplate jdbcTemplate) {
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
        BlockingQueue<List<AirHouseBillEntity>> queue = fileQueueManager.getQueue(fileId);

        while (true) {
            try {
                List<AirHouseBillEntity> batch = queue.poll(5, TimeUnit.SECONDS);
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
    IF OBJECT_ID('dbo.air_house_bill', 'U') IS NULL
    BEGIN
        CREATE TABLE air_house_bill (
            id BIGINT IDENTITY PRIMARY KEY,
            ID_CHUYENBAY NVARCHAR(255),
            SOHIEU NVARCHAR(255),
            FLIGHTDATE SMALLDATETIME,
            CARRIERCODE NVARCHAR(255),
            CARRIERNAME NVARCHAR(4000),
            MANOIDI NVARCHAR(255),
            TENNOIDI NVARCHAR(4000),
            MANOIDEN NVARCHAR(4000),
            TENNOIDEN NVARCHAR(4000),
            FI_MAQUACANH NVARCHAR(4000),
            IDHANGHOA NVARCHAR(4000),
            ID_HHCT NVARCHAR(4000),
            FI_SOAWB NVARCHAR(4000),
            FI_SOKIEN BIGINT,
            FI_MOTA NVARCHAR(4000),
            FI_SHC NVARCHAR(4000),
            TRONGLUONG DECIMAL(20,3),
            NOIDIHH NVARCHAR(4000),
            NOIDENHH NVARCHAR(4000),
            FI_LOAIHANG NVARCHAR(4000)
        )
    END
    """;

        jdbcTemplate.execute(sql);
    }



    //    @Transactional
    public void insertDataBatch(List<AirHouseBillEntity> batchList) {
        String insertSQL = "INSERT INTO air_house_bill ( " +
                "ID_CHUYENBAY, " +
                "SOHIEU, " +
                "FLIGHTDATE, " +
                "CARRIERCODE, " +
                "CARRIERNAME, " +
                "MANOIDI, " +
                "TENNOIDI, " +
                "MANOIDEN, " +
                "TENNOIDEN, " +
                "FI_MAQUACANH, " +
                "IDHANGHOA, " +
                "ID_HHCT, " +
                "FI_SOAWB, " +
                "FI_SOKIEN, " +
                "FI_MOTA, " +
                "FI_SHC, " +
                "TRONGLUONG, " +
                "NOIDIHH, " +
                "NOIDENHH, " +
                "FI_LOAIHANG" +
                ") VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

        jdbcTemplate.batchUpdate(insertSQL, batchList, batchList.size(), (ps, e) -> {
            ps.setString(1, e.getIdChuyenBay());
            ps.setString(2, e.getSoHieu());
            ps.setTimestamp(3, e.getFlightDate());
            ps.setString(4, e.getCarrierCode());
            ps.setString(5, e.getCarrierName());
            ps.setString(6, e.getMaNoiDi());
            ps.setString(7, e.getTenNoiDi());
            ps.setString(8, e.getMaNoiDen());
            ps.setString(9, e.getTenNoiDen());
            ps.setString(10, e.getFiMaQuaCanh());
            ps.setString(11, e.getIdHangHoa());
            ps.setString(12, e.getIdHangHoaChiTiet());
            ps.setString(13, e.getFiSoAwb());
            ps.setObject(14, e.getFiSoKien(), java.sql.Types.INTEGER);
            ps.setString(15, e.getFiMoTa());
            ps.setString(16, e.getFiShc());
            ps.setObject(17, e.getTrongLuong(), java.sql.Types.DECIMAL);
            ps.setString(18, e.getNoiDiHangHoa());
            ps.setString(19, e.getNoiDenHangHoa());
            ps.setString(20, e.getFiLoaiHang());
        });
    }

    private void readCsvAndEnqueue(File file, String fileId, String filename) {
        try (CSVReader csvReader = new CSVReader(new FileReader(file))) {
            String[] tokens;
            boolean skipHeader = true;
            List<AirHouseBillEntity> batch = new ArrayList<>();
            int count = 0;

            while ((tokens = csvReader.readNext()) != null) {
                if (skipHeader) {
                    skipHeader = false;
                    continue; // Bỏ qua header
                }


                AirHouseBillEntity entity = new AirHouseBillEntity();
                mapEntityAirHouseContext.mapCsvRowToEntity(tokens, entity);
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