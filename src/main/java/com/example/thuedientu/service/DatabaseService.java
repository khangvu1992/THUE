package com.example.thuedientu.service;

import com.example.thuedientu.model.EnityExcelJDBC;
import com.example.thuedientu.util.mapEntityJDBC;
import com.monitorjbl.xlsx.StreamingReader;
import org.apache.poi.ss.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.scheduling.annotation.Async;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Service
public class DatabaseService {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    private mapEntityJDBC map1EntityJDBC;

    public DatabaseService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Async
    public void import1Datbase1JDBC1(File file) {
        createTable();
        int count = 0;

        try (InputStream is = new FileInputStream(file);
             Workbook workbook = StreamingReader.builder()
                     .rowCacheSize(100)
                     .bufferSize(4096)
                     .open(is)) {

            Sheet sheet = workbook.getSheetAt(0);
            Iterator<Row> rows = sheet.iterator();

            if (rows.hasNext()) rows.next(); // skip header

            List<EnityExcelJDBC> batchList = new ArrayList<>();
            int batchSize = 20000;

            while (rows.hasNext()) {
                Row row = rows.next();
                EnityExcelJDBC entity = new EnityExcelJDBC();
                map1EntityJDBC.mapRowToEntity(row, entity);
                batchList.add(entity);
                count++;

                if (batchList.size() >= batchSize) {
                    insertDataBatch(batchList);
                    batchList.clear();
                }

                if (count % 10000 == 0) {
                    System.out.println("Progress: " + (count / 10000) + "%");
                }
            }

            if (!batchList.isEmpty()) {
                insertDataBatch(batchList);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            file.delete();
        }
    }

    public void createTable() {
        String createTableSQL = "IF OBJECT_ID('dbo.khang_heheJDBC', 'U') IS NULL " +
                "BEGIN " +
                "CREATE TABLE dbo.khang_heheJDBC (" +
                "trangthaitk NVARCHAR(255), bpkthsdt NVARCHAR(255), bptq NVARCHAR(255), ptvc NVARCHAR(255), " +
                "malh NVARCHAR(255), ngayDk NVARCHAR(255), hourDk NVARCHAR(255), ngayThaydoiDk NVARCHAR(255), " +
                "hourThaydoiDk NVARCHAR(255), masothueKbhq NVARCHAR(255), tenDoanhnghiep NVARCHAR(255), " +
                "sodienthoai NVARCHAR(255), tenDoanhnghiepUythac NVARCHAR(255), tenDoitacnuocngoai NVARCHAR(255), " +
                "maquocgiaDoitacnuocngoai NVARCHAR(255), vandon01 NVARCHAR(255), vandon02 NVARCHAR(255), " +
                "vandon03 NVARCHAR(255), vandon04 NVARCHAR(255), vandon05 NVARCHAR(255), soluongkienhang NVARCHAR(255), " +
                "maDvtKienhang NVARCHAR(255), grossweight NVARCHAR(255), maDvtGw NVARCHAR(255), " +
                "soluongContainer NVARCHAR(255), maDiadiemdohang NVARCHAR(255), maDiadiemxephang NVARCHAR(255), " +
                "tenPhuongtienvanchuyen NVARCHAR(255), ngayHangDen NVARCHAR(255), phuongThucThanhToan NVARCHAR(255), " +
                "tongTriGiaHoaDon NVARCHAR(255), tongTriGiaTinhThue NVARCHAR(255), tongTienThue NVARCHAR(255), " +
                "tongSoDonghang NVARCHAR(255), ngayCapPhep NVARCHAR(255), gioCapPhep NVARCHAR(255), " +
                "ngayHoanthanhKiemtra NVARCHAR(255), gioHoanthanhKiemtra NVARCHAR(255), ngayHuyTk NVARCHAR(255), " +
                "gioHuyTk NVARCHAR(255), tenNguoiphutrachKiemtrahoso NVARCHAR(255), tenNguoiphutrachKiemhoa NVARCHAR(255), " +
                "hsCode NVARCHAR(255), moTaHangHoa NVARCHAR(255), soLuongHanghoa NVARCHAR(255), " +
                "maDvtHanghoa NVARCHAR(255), triGiaHoaDon NVARCHAR(255), dongiaHoadon NVARCHAR(255), " +
                "maTienteHoadon NVARCHAR(255), donviDongiaTiente NVARCHAR(255), triGiaTinhThueS NVARCHAR(255), " +
                "triGiaTinhThueM NVARCHAR(255), dongiaTinhthue NVARCHAR(255), thuesuatNhapkhau NVARCHAR(255), " +
                "tienThueNhapkhau NVARCHAR(255), xuatxu NVARCHAR(255), maVanbanphapquy NVARCHAR(255), " +
                "phanloaiGiayphepNk NVARCHAR(255), maBieuthueNk NVARCHAR(255), maMiengiamThue NVARCHAR(255), " +
                "tkid NVARCHAR(255), sotk NVARCHAR(255), mahq NVARCHAR(255)" +
                "); END";

        jdbcTemplate.execute(createTableSQL);
    }

    @Transactional
    public void insertDataBatch(List<EnityExcelJDBC> batchList) {
        String insertSQL = "INSERT INTO khang_heheJDBC (bpkthsdt, bptq, ptvc, malh, ngayDk, hourDk, ngayThaydoiDk, hourThaydoiDk, " +
                "masothueKbhq, tenDoanhnghiep, sodienthoai, tenDoanhnghiepUythac, tenDoitacnuocngoai, maquocgiaDoitacnuocngoai, " +
                "vandon01, vandon02, vandon03, vandon04, vandon05, soluongkienhang, maDvtKienhang, grossweight, maDvtGw, " +
                "soluongContainer, maDiadiemdohang, maDiadiemxephang, tenPhuongtienvanchuyen, ngayHangDen, phuongThucThanhToan, " +
                "tongTriGiaHoaDon, tongTriGiaTinhThue, tongTienThue, tongSoDonghang, ngayCapPhep, gioCapPhep, " +
                "ngayHoanthanhKiemtra, gioHoanthanhKiemtra, ngayHuyTk, gioHuyTk, tenNguoiphutrachKiemtrahoso, " +
                "tenNguoiphutrachKiemhoa, hsCode, moTaHangHoa, soLuongHanghoa, maDvtHanghoa, triGiaHoaDon, dongiaHoadon, " +
                "maTienteHoadon, donviDongiaTiente, triGiaTinhThueS, triGiaTinhThueM, dongiaTinhthue, thuesuatNhapkhau, " +
                "tienThueNhapkhau, xuatxu, maVanbanphapquy, phanloaiGiayphepNk, maBieuthueNk, maMiengiamThue, tkid, sotk, mahq, trangthaitk) " +
                "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

        jdbcTemplate.batchUpdate(insertSQL, batchList, batchList.size(), (ps, e) -> {
            ps.setString(1, e.getBpkthsdt());
            ps.setString(2, e.getBptq());
            ps.setString(3, e.getPtvc());
            ps.setString(4, e.getMalh());
            ps.setString(5, e.getNgayDk());
            ps.setString(6, e.getHourDk());
            ps.setString(7, e.getNgayThaydoiDk());
            ps.setString(8, e.getHourThaydoiDk());
            ps.setString(9, e.getMasothueKbhq());
            ps.setString(10, e.getTenDoanhnghiep());
            ps.setString(11, e.getSodienthoai());
            ps.setString(12, e.getTenDoanhnghiepUythac());
            ps.setString(13, e.getTenDoitacnuocngoai());
            ps.setString(14, e.getMaquocgiaDoitacnuocngoai());
            ps.setString(15, e.getVandon01());
            ps.setString(16, e.getVandon02());
            ps.setString(17, e.getVandon03());
            ps.setString(18, e.getVandon04());
            ps.setString(19, e.getVandon05());
            ps.setString(20, e.getSoluongkienhang());
            ps.setString(21, e.getMaDvtKienhang());
            ps.setString(22, e.getGrossweight());
            ps.setString(23, e.getMaDvtGw());
            ps.setString(24, e.getSoluongContainer());
            ps.setString(25, e.getMaDiadiemdohang());
            ps.setString(26, e.getMaDiadiemxephang());
            ps.setString(27, e.getTenPhuongtienvanchuyen());
            ps.setString(28, e.getNgayHangDen());
            ps.setString(29, e.getPhuongThucThanhToan());
            ps.setString(30, e.getTongTriGiaHoaDon());
            ps.setString(31, e.getTongTriGiaTinhThue());
            ps.setString(32, e.getTongTienThue());
            ps.setString(33, e.getTongSoDonghang());
            ps.setString(34, e.getNgayCapPhep());
            ps.setString(35, e.getGioCapPhep());
            ps.setString(36, e.getNgayHoanthanhKiemtra());
            ps.setString(37, e.getGioHoanthanhKiemtra());
            ps.setString(38, e.getNgayHuyTk());
            ps.setString(39, e.getGioHuyTk());
            ps.setString(40, e.getTenNguoiphutrachKiemtrahoso());
            ps.setString(41, e.getTenNguoiphutrachKiemhoa());
            ps.setString(42, e.getHsCode());
            ps.setString(43, e.getMoTaHangHoa());
            ps.setString(44, e.getSoLuongHanghoa());
            ps.setString(45, e.getMaDvtHanghoa());
            ps.setString(46, e.getTriGiaHoaDon());
            ps.setString(47, e.getDongiaHoadon());
            ps.setString(48, e.getMaTienteHoadon());
            ps.setString(49, e.getDonviDongiaTiente());
            ps.setString(50, e.getTriGiaTinhThueS());
            ps.setString(51, e.getTriGiaTinhThueM());
            ps.setString(52, e.getDongiaTinhthue());
            ps.setString(53, e.getThuesuatNhapkhau());
            ps.setString(54, e.getTienThueNhapkhau());
            ps.setString(55, e.getXuatxu());
            ps.setString(56, e.getMaVanbanphapquy());
            ps.setString(57, e.getPhanloaiGiayphepNk());
            ps.setString(58, e.getMaBieuthueNk());
            ps.setString(59, e.getMaMiengiamThue());
            ps.setString(60, e.getTkid());
            ps.setString(61, e.getSotk());
            ps.setString(62, e.getMahq());
            ps.setString(63, e.getTrangthaitk());
        });
    }
}