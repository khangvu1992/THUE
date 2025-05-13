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
public class AirMasterBillService extends csvService<AirMasterBillEntity> {

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
    public AirMasterBillEntity createEntity() {
        return new AirMasterBillEntity();
    }

    @Override
    public <T> void insertDataBatch(List<T> batch) {

        if (batch != null && !batch.isEmpty() && batch.get(0) instanceof AirMasterBillEntity) {
            // Chuyển kiểu về List<SeawayHouseBillEntity> an toàn
            List<AirMasterBillEntity> castedBatch = (List<AirMasterBillEntity>) batch;
            insertDataBatch1(castedBatch);  // Gọi phương thức đã triển khai
        } else {
            throw new IllegalArgumentException("Invalid batch type. Expected List<AirMasterBillEntity>");
        }
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
        if (record instanceof CSVRecord && entity instanceof AirMasterBillEntity) {
            CSVRecord csvRecord = (CSVRecord) record;
            AirMasterBillEntity airMasterBillEntity = (AirMasterBillEntity) entity;

            // Gọi phương thức mapCsvRowToEntity từ mapEntitySeawayHouseContext để chuyển đổi dữ liệu
            mapCsvRowToEntity1(csvRecord, airMasterBillEntity);
        } else {
            throw new IllegalArgumentException("record phải là CSVRecord và entity phải là SeawayHouseBillEntity.");
        }


    }


    //    @Transactional
    public void insertDataBatch1(List<AirMasterBillEntity> batchList) {
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

    public void mapCsvRowToEntity1(CSVRecord tokens, AirMasterBillEntity entity) {

        entity.setIdChuyenBay(getSafe(tokens, 0));
        entity.setFlightDate(formatterService.parseSqlTimestamp(getSafe(tokens, 1),tokens.toList().toArray(new String[0])));
        entity.setCarrier(getSafe(tokens, 2));
        entity.setFlightNo(getSafe(tokens, 3));
        entity.setOrigin(getSafe(tokens, 4));
        entity.setDestination(getSafe(tokens, 5));
        entity.setMawbNo(getSafe(tokens, 6));
        entity.setMTenNguoiGui(getSafe(tokens, 7));
        entity.setMTenNguoiNhan(getSafe(tokens, 8));
        entity.setMMoTaHangHoa(getSafe(tokens, 9));
        entity.setMSoKien(formatterService.parseInteger(getSafe(tokens, 10), 0));
        entity.setMGrossWeight(formatterService.parseBigDecimal(getSafe(tokens, 11), 1));
        entity.setMSanBayDi(getSafe(tokens, 12));
        entity.setMSanBayDen(getSafe(tokens, 13));
        entity.setMVersion(getSafe(tokens, 14));
        entity.setHSoMawb(getSafe(tokens, 15));
        entity.setHSoHwb(getSafe(tokens, 16));
        entity.setHTenNguoiGui(getSafe(tokens, 17));
        entity.setHTenNguoiNhan(getSafe(tokens, 18));
        entity.setHSoKien(formatterService.parseInteger(getSafe(tokens, 19), 0));
        entity.setHTrongLuong(formatterService.parseBigDecimal(getSafe(tokens, 20), 2));
        entity.setHNoiDi(getSafe(tokens, 21));
        entity.setHNoiDen(getSafe(tokens, 22));
        entity.setHMoTaHangHoa(getSafe(tokens, 23));
    }

    private String getSafe(CSVRecord tokens, int index) {
        if (index >= tokens.size()) {
            System.err.println("⚠️ Thiếu cột ở index " + index + " trong dòng: " + tokens.toString());
            return "";
        }
        String value = tokens.get(index);
        return value != null ? value.trim() : "";
    }


}