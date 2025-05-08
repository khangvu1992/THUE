package com.example.thuedientu.service;

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
public class SeawayHouseBillService {

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
    private MapEntitySeawayHouseContext mapEntitySeawayHouseContext;
    @Autowired
    private SeawayHouseQueueManager fileQueueManager;
    @Autowired
    private ProgressWebSocketSender progressWebSocketSender;

    public SeawayHouseBillService(JdbcTemplate jdbcTemplate) {
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
        BlockingQueue<List<SeawayHouseBillEntity>> queue = fileQueueManager.getQueue(fileId);

        while (true) {
            try {
                List<SeawayHouseBillEntity> batch = queue.poll(5, TimeUnit.SECONDS);
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
        String sql = "IF OBJECT_ID('dbo.seaway_house_bill', 'U') IS NULL " +
                "BEGIN " +
                "CREATE TABLE seaway_house_bill (" +
                "id BIGINT IDENTITY PRIMARY KEY, " +
                "SoKhaiBao NVARCHAR(255), " +
                "SoHoSo NVARCHAR(255), " +
                "LoaiHoSo NVARCHAR(255), " +
                "ArrivalDate SMALLDATETIME, " +
                "CangTiepNhan NVARCHAR(4000), " +
                "NgayGui SMALLDATETIME, " +
                "TenTau NVARCHAR(4000), " +
                "SoIMO NVARCHAR(4000), " +
                "HangTau NVARCHAR(4000), " +
                "NgayTauDenRoi SMALLDATETIME, " +
                "NgayDenRoi SMALLDATETIME, " +
                "CangRoiCuoiCungCangDich NVARCHAR(4000), " +
                "BillNumber NVARCHAR(255), " +
                "HB_Consigner NVARCHAR(4000), " +
                "HB_Consignee NVARCHAR(4000), " +
                "HB_NotificatedParty NVARCHAR(4000), " +
                "HB_NotificatedParty2 NVARCHAR(4000), " +
                "DateOfBill SMALLDATETIME, " +
                "DepartureDate SMALLDATETIME, " +
                "PortNameOfTranship NVARCHAR(4000), " +
                "PortNameOfDestination NVARCHAR(4000), " +
                "PortNameOfLoad NVARCHAR(4000), " +
                "PortNameOfUnload NVARCHAR(4000), " +
                "PlaceOfDelivery NVARCHAR(4000), " +
                "MoTaHangHoa NVARCHAR(4000), " +
                "ContNumber NVARCHAR(4000), " +
                "ContSealNumber NVARCHAR(4000), " +
                "NumberOfPackage BIGINT, " +
                "CargoType NVARCHAR(4000) " +
                ") " +
                "END";

        jdbcTemplate.execute(sql);
    }



    //    @Transactional
    public void insertDataBatch(List<SeawayHouseBillEntity> batchList) {
        String insertSQL = "INSERT INTO seaway_house_bill ( " +
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
                "BillNumber, " +
                "HB_Consigner, " +
                "HB_Consignee, " +
                "HB_NotificatedParty, " +
                "HB_NotificatedParty2, " +
                "DateOfBill, " +
                "DepartureDate, " +
                "PortNameOfTranship, " +
                "PortNameOfDestination, " +
                "PortNameOfLoad, " +
                "PortNameOfUnload, " +
                "PlaceOfDelivery, " +
                "MoTaHangHoa, " +
                "ContNumber, " +
                "ContSealNumber, " +
                "NumberOfPackage, " +
                "CargoType " +
                ") VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

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
            ps.setString(13, e.getBillNumber());
            ps.setString(14, e.getHbConsigner());
            ps.setString(15, e.getHbConsignee());
            ps.setString(16, e.getHbNotificatedParty());
            ps.setString(17, e.getHbNotificatedParty2());
            ps.setTimestamp(18, e.getDateOfBill());
            ps.setTimestamp(19, e.getDepartureDate());
            ps.setString(20, e.getPortNameOfTranship());
            ps.setString(21, e.getPortNameOfDestination());
            ps.setString(22, e.getPortNameOfLoad());
            ps.setString(23, e.getPortNameOfUnload());
            ps.setString(24, e.getPlaceOfDelivery());
            ps.setString(25, e.getMoTaHangHoa());
            ps.setString(26, e.getContNumber());
            ps.setString(27, e.getContSealNumber());
            ps.setObject(28, e.getNumberOfPackage(), java.sql.Types.INTEGER);
            ps.setString(29, e.getCargoType());
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
            List<SeawayHouseBillEntity> batch = new ArrayList<>();
            int count = 0;

            while ((tokens = csvReader.readNext()) != null) {
                if (skipHeader) {
                    skipHeader = false;
                    continue; // Bỏ qua header
                }

                SeawayHouseBillEntity entity = new SeawayHouseBillEntity();
                mapEntitySeawayHouseContext.mapCsvRowToEntity(tokens, entity);
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