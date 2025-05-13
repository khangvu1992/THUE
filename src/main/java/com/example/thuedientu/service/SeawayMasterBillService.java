package com.example.thuedientu.service;

import com.example.thuedientu.model.HashFile;
import com.example.thuedientu.model.SeawayHouseBillEntity;
import com.example.thuedientu.model.SeawayMasterBillEntity;
import com.example.thuedientu.repository.FileRepository;
import com.example.thuedientu.util.*;
import com.monitorjbl.xlsx.StreamingReader;
import com.opencsv.CSVReader;
import org.apache.commons.csv.CSVRecord;
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
public class SeawayMasterBillService extends csvService<SeawayMasterBillEntity> {

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
    private ProgressWebSocketSender progressWebSocketSender;

    public SeawayMasterBillService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }



    @Override
    public SeawayMasterBillEntity createEntity() {
        return new  SeawayMasterBillEntity();
    }

    @Override
    public <T> void insertDataBatch(List<T> batch) {
        // Kiểm tra nếu batch là danh sách SeawayHouseBillEntity
        if (batch != null && !batch.isEmpty() && batch.get(0) instanceof SeawayMasterBillEntity) {
            // Chuyển kiểu về List<SeawayHouseBillEntity> an toàn
            List<SeawayMasterBillEntity> castedBatch = (List<SeawayMasterBillEntity>) batch;
            insertDataBatch1(castedBatch);  // Gọi phương thức đã triển khai
        } else {
            throw new IllegalArgumentException("Invalid batch type. Expected List<SeawayMasterBillEntity>");
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
        String sql = "IF OBJECT_ID('dbo.seaway_house_bill', 'U') IS NULL " +
                "BEGIN " +
                "CREATE TABLE seaway_house_bill (" +
                "id BIGINT IDENTITY PRIMARY KEY, " +
                "SoKhaiBao NVARCHAR(4000), " +
                "SoHoSo NVARCHAR(4000), " +
                "LoaiHoSo NVARCHAR(4000), " +
                "ArrivalDate SMALLDATETIME, " +
                "CangTiepNhan NVARCHAR(500), " +
                "NgayGui SMALLDATETIME, " +
                "TenTau NVARCHAR(500), " +
                "SoIMO NVARCHAR(4000), " +
                "HangTau NVARCHAR(4000), " +
                "NgayTauDenRoi SMALLDATETIME, " +
                "NgayDenRoi SMALLDATETIME, " +
                "CangRoiCuoiCungCangDich NVARCHAR(255), " +
                "Consignee NVARCHAR(4000), " +
                "Consigner NVARCHAR(4000), " +
                "NotificatedParty NVARCHAR(4000), " +
                "NotificatedParty2 NVARCHAR(4000), " +
                "MasterBillNo NVARCHAR(4000), " +
                "ContNumber NVARCHAR(4000), " +
                "ContSealNumber NVARCHAR(225), " +
                "GoodDescription NVARCHAR(4000), " +
                "CangXepHangGoc NVARCHAR(4000), " +
                "CangXepHang NVARCHAR(4000), " +
                "CangDoHang NVARCHAR(4000), " +
                "CangDich NVARCHAR(4000), " +
                "TenCangDich NVARCHAR(4000), " +
                "DiaDiemDoHang NVARCHAR(4000), " +
                "NetWeight DECIMAL(20,3), " +
                "GrossWeight DECIMAL(20,3),  " +
                "Demension DECIMAL(20,3), " +
                ") " +
                "END";

        jdbcTemplate.execute(sql);
    }

    @Override
    public <T> void fileRepositorySave(T entity) {      // Kiểm tra nếu entity là một đối tượng hợp lệ để lưu
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
        if (record instanceof CSVRecord && entity instanceof SeawayMasterBillEntity) {
            CSVRecord csvRecord = (CSVRecord) record;
            SeawayMasterBillEntity seawayMasterBillEntity = (SeawayMasterBillEntity) entity;

            // Gọi phương thức mapCsvRowToEntity từ mapEntitySeawayHouseContext để chuyển đổi dữ liệu
            mapEntitySeawayMasterContext.mapCsvRowToEntity(csvRecord, seawayMasterBillEntity);
        } else {
            throw new IllegalArgumentException("record phải là CSVRecord và entity phải là SeawayHouseBillEntity.");
        }

    }

    //    @Transactional
    public void insertDataBatch1(List<SeawayMasterBillEntity> batchList) {
        String insertSQL = "INSERT INTO seaway_house_bill( " +
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


}