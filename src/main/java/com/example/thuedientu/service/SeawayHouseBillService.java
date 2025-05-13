package com.example.thuedientu.service;

import com.example.thuedientu.model.HashFile;
import com.example.thuedientu.model.SeawayHouseBillEntity;
import com.example.thuedientu.repository.FileRepository;
import com.example.thuedientu.util.*;
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
public class SeawayHouseBillService extends  csvService<SeawayHouseBillEntity> {

    @Autowired
    private ExcelDataFormatterService formatterService;

    private final JdbcTemplate jdbcTemplate;
    private final int BATCH_SIZE = 5000;
    private final int WORKER_COUNT = 4;

    //    @Autowired private insertDataBatchService insertDataBatchService1;
    @Autowired
    private FileRepository fileRepository;





    @Autowired
    private ProgressWebSocketSender progressWebSocketSender;

    public SeawayHouseBillService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }



    @Override
    public SeawayHouseBillEntity createEntity() {
        return  new SeawayHouseBillEntity();
    }



    @Override
    public <T> void insertDataBatch(List<T> batch) {
        // Kiểm tra nếu batch là danh sách SeawayHouseBillEntity
        if (batch != null && !batch.isEmpty() && batch.get(0) instanceof SeawayHouseBillEntity) {
            // Chuyển kiểu về List<SeawayHouseBillEntity> an toàn
            List<SeawayHouseBillEntity> castedBatch = (List<SeawayHouseBillEntity>) batch;
            insertDataBatch1(castedBatch);  // Gọi phương thức đã triển khai
        } else {
            throw new IllegalArgumentException("Invalid batch type. Expected List<SeawayHouseBillEntity>");
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

    @Override
    public <T> void fileRepositorySave(T entity) {
        // Kiểm tra nếu entity là một đối tượng hợp lệ để lưu
        if (entity instanceof HashFile) {
            HashFile hashFile = (HashFile) entity;
            fileRepository.save(hashFile);  // Lưu hashFile vào repository
        } else {
            throw new IllegalArgumentException("Entity phải là đối tượng HashFile.");
        }
    }

    @Override
    public <T, U, V, L> void progressWebSocketSenderSendProgress1(T entity, U e2, V e3, L e4) {

        String fileId= (String) entity;
        String filename= (String) e2;
        int percent= (int) e3;
        progressWebSocketSender.sendProgress1(fileId, filename, percent, false);

    }

    @Override
    public <T, V> void mapCsvRowToEntity(T record, V entity) {

        if (record instanceof CSVRecord && entity instanceof SeawayHouseBillEntity) {
            CSVRecord csvRecord = (CSVRecord) record;
            SeawayHouseBillEntity seawayHouseBillEntity = (SeawayHouseBillEntity) entity;

            // Gọi phương thức mapCsvRowToEntity từ mapEntitySeawayHouseContext để chuyển đổi dữ liệu
            mapCsvRowToEntity(csvRecord, seawayHouseBillEntity);
        } else {
            throw new IllegalArgumentException("record phải là CSVRecord và entity phải là SeawayHouseBillEntity.");
        }


    }


    //    @Transactional
    public void insertDataBatch1(List<SeawayHouseBillEntity> batchList) {
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


    public void mapCsvRowToEntity(CSVRecord record, SeawayHouseBillEntity entity) {
        // if (record.size() < 29) {
        //     throw new IllegalArgumentException("Insufficient columns: expected at least 29, but got " + record.size());
        // }

        entity.setSoKhaiBao(getString(record, 0));
        entity.setSoHoSo(getString(record, 1));
        entity.setLoaiHoSo(getString(record, 2));
        entity.setArrivalDate(formatterService.parseSqlTimestamp(getString(record, 3), record.toList().toArray(new String[0])));
        entity.setCangTiepNhan(getString(record, 4));
        entity.setNgayGui(formatterService.parseSqlTimestamp(getString(record, 5), record.toList().toArray(new String[0])));
        entity.setTenTau(getString(record, 6));
        entity.setSoIMO(getString(record, 7));
        entity.setHangTau(getString(record, 8));
        entity.setNgayTauDenRoi(formatterService.parseSqlTimestamp(getString(record, 9), record.toList().toArray(new String[0])));
        entity.setNgayDenRoi(formatterService.parseSqlTimestamp(getString(record, 10), record.toList().toArray(new String[0])));
        entity.setCangRoiCuoiCungCangDich(getString(record, 11));
        entity.setBillNumber(getString(record, 12));
        entity.setHbConsigner(getString(record, 13));
        entity.setHbConsignee(getString(record, 14));
        entity.setHbNotificatedParty(getString(record, 15));
        entity.setHbNotificatedParty2(getString(record, 16));
        entity.setDateOfBill(formatterService.parseSqlTimestamp(getString(record, 17), record.toList().toArray(new String[0])));
        entity.setDepartureDate(formatterService.parseSqlTimestamp(getString(record, 18), record.toList().toArray(new String[0])));
        entity.setPortNameOfTranship(getString(record, 19));
        entity.setPortNameOfDestination(getString(record, 20));
        entity.setPortNameOfLoad(getString(record, 21));
        entity.setPortNameOfUnload(getString(record, 22));
        entity.setPlaceOfDelivery(getString(record, 23));
        entity.setMoTaHangHoa(getString(record, 24));
        entity.setContNumber(getString(record, 25));
        entity.setContSealNumber(getString(record, 26));
        entity.setNumberOfPackage(formatterService.parseInteger(getString(record, 27), 3));
        entity.setCargoType(getString(record, 28));
    }


    private String getString(CSVRecord record, int index) {
        if (index >= record.size()) {
            System.err.println("⚠️ Thiếu cột ở index " + index + " trong dòng: " + record.toString());
            return "";
        }
        String value = record.get(index);
        return value != null ? value.trim() : "";
    }




}