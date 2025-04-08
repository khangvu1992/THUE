package com.example.thuedientu.service;


import com.example.thuedientu.model.EnityExcelJDBC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class insertDataBatchService {


    @Autowired
    private JdbcTemplate jdbcTemplate;




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
