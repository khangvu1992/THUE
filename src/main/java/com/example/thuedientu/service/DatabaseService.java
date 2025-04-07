package com.example.thuedientu.service;

import com.example.thuedientu.model.EnityExcelJDBC;
import com.example.thuedientu.util.mapEntityJDBC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class DatabaseService {



    private final JdbcTemplate jdbcTemplate;

    public DatabaseService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void createTable() {
        String createTableSQL = "CREATE TABLE IF NOT EXISTS khang_hehe (" +
                "id INT AUTO_INCREMENT PRIMARY KEY, " +
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
                ");";

        jdbcTemplate.execute(createTableSQL);
    }


    // Phương thức chèn dữ liệu



    public void insertData(EnityExcelJDBC enityExcel) {
        String insertSQL = "INSERT INTO khang_hehe( bpkthsdt, bptq, ptvc, malh, ngayDk, hourDk, ngayThaydoiDk, hourThaydoiDk, masothueKbhq, tenDoanhnghiep,"+
                " sodienthoai, tenDoanhnghiepUythac, tenDoitacnuocngoai, maquocgiaDoitacnuocngoai, vandon01, vandon02,"+
               " vandon03, vandon04, vandon05, soluongkienhang, maDvtKienhang, grossweight, maDvtGw, soluongContainer, maDiadiemdohang, maDiadiemxephang,"+
                "tenPhuongtienvanchuyen, ngayHangDen, phuongThucThanhToan, tongTriGiaHoaDon, tongTriGiaTinhThue, tongTienThue, tongSoDonghang, ngayCapPhep,"+
                " gioCapPhep, ngayHoanthanhKiemtra, gioHoanthanhKiemtra, ngayHuyTk, gioHuyTk, tenNguoiphutrachKiemtrahoso, tenNguoiphutrachKiemhoa,"+
                "hsCode, moTaHangHoa, soLuongHanghoa, maDvtHanghoa, triGiaHoaDon, dongiaHoadon, maTienteHoadon, donviDongiaTiente, triGiaTinhThueS,"+
              "  triGiaTinhThueM, dongiaTinhthue, thuesuatNhapkhau, tienThueNhapkhau, xuatxu, maVanbanphapquy, phanloaiGiayphepNk, maBieuthueNk, maMiengiamThue,"+
               " tkid, sotk, mahq, trangthaitk)"+
        "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

        jdbcTemplate.update(insertSQL,
                enityExcel.getTrangthaitk(), enityExcel.getBpkthsdt(), enityExcel.getBptq(), enityExcel.getPtvc(),
                enityExcel.getMalh(), enityExcel.getNgayDk(), enityExcel.getHourDk(), enityExcel.getNgayThaydoiDk(),
                enityExcel.getHourThaydoiDk(), enityExcel.getMasothueKbhq(), enityExcel.getTenDoanhnghiep(),
                enityExcel.getSodienthoai(), enityExcel.getTenDoanhnghiepUythac(), enityExcel.getTenDoitacnuocngoai(),
                enityExcel.getMaquocgiaDoitacnuocngoai(), enityExcel.getVandon01(), enityExcel.getVandon02(),
                enityExcel.getVandon03(), enityExcel.getVandon04(), enityExcel.getVandon05(),
                enityExcel.getSoluongkienhang(), enityExcel.getMaDvtKienhang(), enityExcel.getGrossweight(),
                enityExcel.getMaDvtGw(), enityExcel.getSoluongContainer(), enityExcel.getMaDiadiemdohang(),
                enityExcel.getMaDiadiemxephang(), enityExcel.getTenPhuongtienvanchuyen(), enityExcel.getNgayHangDen(),
                enityExcel.getPhuongThucThanhToan(), enityExcel.getTongTriGiaHoaDon(), enityExcel.getTongTriGiaTinhThue(),
                enityExcel.getTongTienThue(), enityExcel.getTongSoDonghang(), enityExcel.getNgayCapPhep(),
                enityExcel.getGioCapPhep(), enityExcel.getNgayHoanthanhKiemtra(), enityExcel.getGioHoanthanhKiemtra(),
                enityExcel.getNgayHuyTk(), enityExcel.getGioHuyTk(), enityExcel.getTenNguoiphutrachKiemtrahoso(),
                enityExcel.getTenNguoiphutrachKiemhoa(), enityExcel.getHsCode(), enityExcel.getMoTaHangHoa(),
                enityExcel.getSoLuongHanghoa(), enityExcel.getMaDvtHanghoa(), enityExcel.getTriGiaHoaDon(),
                enityExcel.getDongiaHoadon(), enityExcel.getMaTienteHoadon(), enityExcel.getDonviDongiaTiente(),
                enityExcel.getTriGiaTinhThueS(), enityExcel.getTriGiaTinhThueM(), enityExcel.getDongiaTinhthue(),
                enityExcel.getThuesuatNhapkhau(), enityExcel.getTienThueNhapkhau(), enityExcel.getXuatxu(),
                enityExcel.getMaVanbanphapquy(), enityExcel.getPhanloaiGiayphepNk(), enityExcel.getMaBieuthueNk(),
                enityExcel.getMaMiengiamThue(), enityExcel.getTkid(), enityExcel.getSotk(), enityExcel.getMahq());
    }
}
