package com.example.thuedientu.service;

import com.example.thuedientu.model.AirHouseBillEntity;
import com.example.thuedientu.model.AirMasterBillEntity;
import com.example.thuedientu.model.HashFile;
import com.example.thuedientu.model.SeawayHouseBillEntity;
import com.example.thuedientu.repository.FileRepository;
import com.example.thuedientu.util.*;
import com.opencsv.CSVReader;
import org.apache.commons.csv.CSVRecord;
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
public class AirHouseBillService extends csvService<AirHouseBillEntity> {

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
    private ProgressWebSocketSender progressWebSocketSender;

    public AirHouseBillService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
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


    @Override
    public AirHouseBillEntity createEntity() {
        return new AirHouseBillEntity();
    }

    @Override
    public <T> void insertDataBatch(List<T> batch) {
        if (batch != null && !batch.isEmpty() && batch.get(0) instanceof AirHouseBillEntity) {
            // Chuyển kiểu về List<SeawayHouseBillEntity> an toàn
            List<AirHouseBillEntity> castedBatch = (List<AirHouseBillEntity>) batch;
            insertDataBatch1(castedBatch);  // Gọi phương thức đã triển khai
        } else {
            throw new IllegalArgumentException("Invalid batch type. Expected List<AirHouseBillEntity>");
        }

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

    @Override
    public <T> void fileRepositorySave(T entity) {
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

        if (record instanceof CSVRecord && entity instanceof AirHouseBillEntity) {
            CSVRecord csvRecord = (CSVRecord) record;
            AirHouseBillEntity airHouseBillEntity = (AirHouseBillEntity) entity;

            // Gọi phương thức mapCsvRowToEntity từ mapEntitySeawayHouseContext để chuyển đổi dữ liệu
            mapCsvRowToEntity(csvRecord, airHouseBillEntity);
        } else {
            throw new IllegalArgumentException("record phải là CSVRecord và entity phải là SeawayHouseBillEntity.");
        }

    }


    //    @Transactional
    public void insertDataBatch1(List<AirHouseBillEntity> batchList) {
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


    public void mapCsvRowToEntity(String[] tokens, AirHouseBillEntity entity) {
        if (tokens.length < 20) {
            throw new IllegalArgumentException("Insufficient tokens. Expected at least 20 fields, got: " + tokens.length);
        }

        try {

            entity.setIdChuyenBay(getSafe(tokens, 0));
            entity.setSoHieu(getSafe(tokens, 1));
            entity.setFlightDate(formatterService.parseSqlTimestamp(getSafe(tokens, 2),tokens));
            entity.setCarrierCode(getSafe(tokens, 3));
            entity.setCarrierName(getSafe(tokens, 4));
            entity.setMaNoiDi(getSafe(tokens, 5));
            entity.setTenNoiDi(getSafe(tokens, 6));
            entity.setMaNoiDen(getSafe(tokens, 7));
            entity.setTenNoiDen(getSafe(tokens, 8));
            entity.setFiMaQuaCanh(getSafe(tokens, 9));
            entity.setIdHangHoa(getSafe(tokens, 10));
            entity.setIdHangHoaChiTiet(getSafe(tokens, 11));
            entity.setFiSoAwb(getSafe(tokens, 12));
            entity.setFiSoKien(formatterService.parseInteger(getSafe(tokens, 13), 0));
            entity.setFiMoTa(getSafe(tokens, 14));
            entity.setFiShc(getSafe(tokens, 15));
            entity.setTrongLuong(formatterService.parseBigDecimal(getSafe(tokens, 16), 2));
            entity.setNoiDiHangHoa(getSafe(tokens, 17));
            entity.setNoiDenHangHoa(getSafe(tokens, 18));
            entity.setFiLoaiHang(getSafe(tokens, 19));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private String getSafe(String[] tokens, int index) {
        if (index >= tokens.length || tokens[index] == null) {
            return "";
        }
        return tokens[index].trim();
    }

}