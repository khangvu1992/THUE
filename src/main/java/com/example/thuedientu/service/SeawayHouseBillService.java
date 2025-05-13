package com.example.thuedientu.service;

import com.example.thuedientu.model.HashFile;
import com.example.thuedientu.model.SeawayHouseBillEntity;
import com.example.thuedientu.repository.FileRepository;
import com.example.thuedientu.util.*;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;



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
    private ProgressWebSocketSender progressWebSocketSender;

    public SeawayHouseBillService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }



    public void import1Datbase1JDBC1(File file, HashFile hashFile) {
        String fileId = hashFile.getFileHash();
        String filename = hashFile.getFilename();
        createTable(); // Tạo bảng nếu chưa có

        BlockingQueue<List<SeawayHouseBillEntity>> queue = new LinkedBlockingQueue<>(100);
        AtomicBoolean readingDone = new AtomicBoolean(false);
        AtomicInteger totalProcessed = new AtomicInteger(0);

        // Khởi tạo các worker
        for (int i = 0; i < WORKER_COUNT; i++) {
            new Thread(() -> {
                while (true) {
                    try {
                        List<SeawayHouseBillEntity> batch = queue.poll(5, TimeUnit.SECONDS);
                        if (batch == null) {
                            if (readingDone.get()) break;
                            continue;
                        }

                        insertDataBatch(batch);
                        long processed = totalProcessed.addAndGet(batch.size());

                        if (processed % 10000 == 0) {
                            int  percent = (int)( processed * 100L / 1048576);
                            System.out.println("Progress: " + (percent * 10) + "%");
                            progressWebSocketSender.sendProgress1(fileId, filename, percent, false);
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                System.out.println(Thread.currentThread().getName() + " done!");
                progressWebSocketSender.sendProgress1(fileId, filename, 100, false);

            }, "worker-" + i + "-" + filename).start();
        }

        // Đọc CSV bằng Apache Commons CSV
        try (
                Reader reader = new FileReader(file);
                CSVParser parser = new CSVParser(reader, CSVFormat.DEFAULT.withFirstRecordAsHeader())
        ) {
            int count = 0;
            int nullCount = 0;
            List<SeawayHouseBillEntity> batch = new ArrayList<>();
            CSVRecord lastRecord = null;

            for (CSVRecord record : parser) {
                if (record == null || record.size() == 0) {
                    nullCount++;
                    if (nullCount >= 2) {
                        System.out.println("🛑 Gặp 2 dòng null liên tiếp. Dừng đọc.");
                        break;
                    }
                    continue;
                } else {
                    nullCount = 0;
                }

                count++;
//                lastRecord = record;

//                SeawayHouseBillEntity entity = new SeawayHouseBillEntity();
//                mapEntitySeawayHouseContext.mapCsvRowToEntity(line, entity);
//                batch.add(entity);

                SeawayHouseBillEntity entity = new SeawayHouseBillEntity();
                mapEntitySeawayHouseContext.mapCsvRowToEntity(record, entity); // cần sửa hàm map để nhận CSVRecord
                batch.add(entity);

                // ✅ In số dòng và nội dung dòng cuối cùng
//                System.out.println("📦 Tổng số dòng đã đọc: " + count);
//                System.out.println("🧾 Dòng cuối cùng:");
//                for (int i = 0; i < record.size(); i++) {
//                    System.out.println("Cột " + (i + 1) + ": " + record.get(i));
//                }

                if (batch.size() >= 10000) {
                    queue.put(new ArrayList<>(batch));
                    batch.clear();
                }
            }

            if (!batch.isEmpty()) {
                queue.put(batch);
            }

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        } finally {
            readingDone.set(true);
            file.delete();
            fileRepository.save(hashFile);
            System.out.println("🧹 Đã xoá file tạm: " + file.getAbsolutePath());

        }
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
                "ArrivalDate DATETIME, " +
                "CangTiepNhan NVARCHAR(4000), " +
                "NgayGui DATETIME, " +
                "TenTau NVARCHAR(4000), " +
                "SoIMO NVARCHAR(4000), " +
                "HangTau NVARCHAR(4000), " +
                "NgayTauDenRoi DATETIME, " +
                "NgayDenRoi DATETIME, " +
                "CangRoiCuoiCungCangDich NVARCHAR(4000), " +
                "BillNumber NVARCHAR(255), " +
                "HB_Consigner NVARCHAR(4000), " +
                "HB_Consignee NVARCHAR(4000), " +
                "HB_NotificatedParty NVARCHAR(4000), " +
                "HB_NotificatedParty2 NVARCHAR(4000), " +
                "DateOfBill DATETIME, " +
                "DepartureDate DATETIME, " +
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
        String insertSQL = "INSERT INTO seaway_master_bill ( " +
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




}