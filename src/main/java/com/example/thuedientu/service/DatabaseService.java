package com.example.thuedientu.service;

import com.example.thuedientu.model.EnityExcel;
import com.example.thuedientu.model.EnityExcelJDBC;
import com.example.thuedientu.util.mapEntityJDBC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.example.thuedientu.model.EnityExcel;
import com.example.thuedientu.repository.EnityExcelRepository;
import org.apache.poi.ss.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import com.monitorjbl.xlsx.StreamingReader;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Cell;


import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


@Service
public class DatabaseService {





    private final JdbcTemplate jdbcTemplate;

    public DatabaseService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    @Autowired
    private FileUploadService fileUploadService;
    @Autowired
    private mapEntityJDBC map1EntityJDBC;

    @Async
    public void import1Datbase1JDBC1(File file) {
        System.out.println("den cho nay ko");
        createTable();
        List<EnityExcelJDBC> dataList = new ArrayList<>();
        int cout=0;
        System.out.println("den cho nay ko");

        try (InputStream is = new FileInputStream(file);
             Workbook workbook = StreamingReader.builder()
                     .rowCacheSize(100)  // Đọc nhiều rows hơn để tối ưu bộ nhớ
                     .bufferSize(4096)   // Tăng buffer size để xử lý nhanh hơn
                     .open(is)) {

            Sheet sheet = workbook.getSheetAt(0); // Đọc sheet đầu tiên
            Iterator<Row> rows = sheet.iterator();

            // Bỏ qua dòng tiêu đề
            if (rows.hasNext()) {
                rows.next(); // Bỏ qua dòng tiêu đề
            }

            while (rows.hasNext()) {
                Row row = rows.next();
                EnityExcelJDBC entity = new EnityExcelJDBC();

                // Ánh xạ dữ liệu từ các cột của row vào entity
                map1EntityJDBC.mapRowToEntity(row, entity);

                // Thêm entity vào danh sách
                dataList.add(entity);
                cout++;

                if (cout % 10000 == 0) {
                    int progress = (cout / 10000);  // Every 10,000 iterations, you want the progress
                    System.out.println("Progress: " + progress + "%");
                }
            }

            // Lưu tất cả dữ liệu vào DB
            insertData(dataList);



        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // Xoá file tạm sau khi xử lý xong
            cout=0;
            file.delete();
        }
    }


    public void createTable() {
        System.out.println("Đang tạo bảng khang_heheJDBC...");

        String createTableSQL =
                "IF OBJECT_ID('dbo.khang_heheJDBC', 'U') IS NULL " +
                        "BEGIN " +
                        "CREATE TABLE dbo.khang_heheJDBC (" +
                        "trangthaitk NVARCHAR(255), " +
                        "bpkthsdt NVARCHAR(255), " +
                        "bptq NVARCHAR(255), " +
                        "ptvc NVARCHAR(255), " +
                        "malh NVARCHAR(255), " +
                        "ngayDk NVARCHAR(255), " +
                        "hourDk NVARCHAR(255), " +
                        "ngayThaydoiDk NVARCHAR(255), " +
                        "hourThaydoiDk NVARCHAR(255), " +
                        "masothueKbhq NVARCHAR(255), " +
                        "tenDoanhnghiep NVARCHAR(255), " +
                        "sodienthoai NVARCHAR(255), " +
                        "tenDoanhnghiepUythac NVARCHAR(255), " +
                        "tenDoitacnuocngoai NVARCHAR(255), " +
                        "maquocgiaDoitacnuocngoai NVARCHAR(255), " +
                        "vandon01 NVARCHAR(255), " +
                        "vandon02 NVARCHAR(255), " +
                        "vandon03 NVARCHAR(255), " +
                        "vandon04 NVARCHAR(255), " +
                        "vandon05 NVARCHAR(255), " +
                        "soluongkienhang NVARCHAR(255), " +
                        "maDvtKienhang NVARCHAR(255), " +
                        "grossweight NVARCHAR(255), " +
                        "maDvtGw NVARCHAR(255), " +
                        "soluongContainer NVARCHAR(255), " +
                        "maDiadiemdohang NVARCHAR(255), " +
                        "maDiadiemxephang NVARCHAR(255), " +
                        "tenPhuongtienvanchuyen NVARCHAR(255), " +
                        "ngayHangDen NVARCHAR(255), " +
                        "phuongThucThanhToan NVARCHAR(255), " +
                        "tongTriGiaHoaDon NVARCHAR(255), " +
                        "tongTriGiaTinhThue NVARCHAR(255), " +
                        "tongTienThue NVARCHAR(255), " +
                        "tongSoDonghang NVARCHAR(255), " +
                        "ngayCapPhep NVARCHAR(255), " +
                        "gioCapPhep NVARCHAR(255), " +
                        "ngayHoanthanhKiemtra NVARCHAR(255), " +
                        "gioHoanthanhKiemtra NVARCHAR(255), " +
                        "ngayHuyTk NVARCHAR(255), " +
                        "gioHuyTk NVARCHAR(255), " +
                        "tenNguoiphutrachKiemtrahoso NVARCHAR(255), " +
                        "tenNguoiphutrachKiemhoa NVARCHAR(255), " +
                        "hsCode NVARCHAR(255), " +
                        "moTaHangHoa NVARCHAR(255), " +
                        "soLuongHanghoa NVARCHAR(255), " +
                        "maDvtHanghoa NVARCHAR(255), " +
                        "triGiaHoaDon NVARCHAR(255), " +
                        "dongiaHoadon NVARCHAR(255), " +
                        "maTienteHoadon NVARCHAR(255), " +
                        "donviDongiaTiente NVARCHAR(255), " +
                        "triGiaTinhThueS NVARCHAR(255), " +
                        "triGiaTinhThueM NVARCHAR(255), " +
                        "dongiaTinhthue NVARCHAR(255), " +
                        "thuesuatNhapkhau NVARCHAR(255), " +
                        "tienThueNhapkhau NVARCHAR(255), " +
                        "xuatxu NVARCHAR(255), " +
                        "maVanbanphapquy NVARCHAR(255), " +
                        "phanloaiGiayphepNk NVARCHAR(255), " +
                        "maBieuthueNk NVARCHAR(255), " +
                        "maMiengiamThue NVARCHAR(255), " +
                        "tkid NVARCHAR(255), " +
                        "sotk NVARCHAR(255), " +
                        "mahq NVARCHAR(255)" +
                        "); " +
                        "END";

        jdbcTemplate.execute(createTableSQL);
    }


    // Phương thức chèn dữ liệu



    public void insertData(List<EnityExcelJDBC> dataList) {
        System.out.println("chay den day chua");
        String insertSQL = "INSERT INTO khang_heheJDBC (bpkthsdt, bptq, ptvc, malh, ngayDk, hourDk, ngayThaydoiDk, hourThaydoiDk," +
                " masothueKbhq, tenDoanhnghiep, sodienthoai, tenDoanhnghiepUythac, tenDoitacnuocngoai, maquocgiaDoitacnuocngoai," +
                " vandon01, vandon02, vandon03, vandon04, vandon05, soluongkienhang, maDvtKienhang, grossweight, maDvtGw," +
                " soluongContainer, maDiadiemdohang, maDiadiemxephang, tenPhuongtienvanchuyen, ngayHangDen, phuongThucThanhToan," +
                " tongTriGiaHoaDon, tongTriGiaTinhThue, tongTienThue, tongSoDonghang, ngayCapPhep, gioCapPhep," +
                " ngayHoanthanhKiemtra, gioHoanthanhKiemtra, ngayHuyTk, gioHuyTk, tenNguoiphutrachKiemtrahoso," +
                " tenNguoiphutrachKiemhoa, hsCode, moTaHangHoa, soLuongHanghoa, maDvtHanghoa, triGiaHoaDon, dongiaHoadon," +
                " maTienteHoadon, donviDongiaTiente, triGiaTinhThueS, triGiaTinhThueM, dongiaTinhthue, thuesuatNhapkhau," +
                " tienThueNhapkhau, xuatxu, maVanbanphapquy, phanloaiGiayphepNk, maBieuthueNk, maMiengiamThue, tkid, sotk," +
                " mahq, trangthaitk) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

        jdbcTemplate.batchUpdate(insertSQL, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                EnityExcelJDBC e = dataList.get(i);
                ps.setString(1,  e.getBpkthsdt());
                ps.setString(2,  e.getBptq());
                ps.setString(3,  e.getPtvc());
                ps.setString(4,  e.getMalh());
                ps.setString(5,  e.getNgayDk());
                ps.setString(6,  e.getHourDk());
                ps.setString(7,  e.getNgayThaydoiDk());
                ps.setString(8,  e.getHourThaydoiDk());
                ps.setString(9,  e.getMasothueKbhq());
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
            }

            @Override
            public int getBatchSize() {
                return dataList.size();
            }
        });
    }

}
