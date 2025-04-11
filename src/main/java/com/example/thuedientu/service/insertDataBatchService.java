//package com.example.thuedientu.service;
//
//
//import com.example.thuedientu.model.EnityExcelJDBC;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.jdbc.core.JdbcTemplate;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Propagation;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.util.List;
//
//@Service
//public class insertDataBatchService {
//
//
//    @Autowired
//    private JdbcTemplate jdbcTemplate;
//
//    @Autowired
//    private ExcelDataFormatterService formatterService;
//
//
//
//    //    @Transactional
//    public void insertDataBatch(List<EnityExcelJDBC> batchList) {
//        String insertSQL = "INSERT INTO khang_heheJDBC (tkid, sotk, mahq, trangthaitk, bpkthsdt, bptq, ptvc, malh, " +
//                "ngay_Dk, hour_Dk, ngay_Thay_doiDk, hour_Thay_doi_Dk, masothue_Kbhq, ten_Doanhnghiep, " +
//                "sodienthoai, ten_Doanhnghiep_Uythac, ten_Doitacnuocngoai, maquocgia_Doitacnuocngoai, vandon_01, vandon_02, vandon_03, vandon_04, vandon_05, " +
//                "soluongkienhang, ma_Dvt_Kienhang, grossweight, ma_Dvt_Gw, soluong_Container, ma_Diadiemdohang, " +
//                "ma_Diadiemxephang, ten_Phuongtienvanchuyen, ngay_Hang_Den, phuong_Thuc_Thanh_Toan, tong_Tri_Gia_Hoa_Don, tong_Tri_Gia_Tinh_Thue, " +
//                "tong_Tien_Thue, tong_So_Donghang, ngay_Cap_Phep, gio_Cap_Phep, ngay_Hoanthanh_Kiemtra, " +
//                "gio_Hoanthanh_Kiemtra, ngay_Huy_Tk, gio_Huy_Tk, ten_Nguoiphutrach_Kiemtrahoso, ten_Nguoiphutrach_Kiemhoa, hs_Code, mo_Ta_Hang_Hoa, so_Luong_Hanghoa, " +
//                "ma_Dvt_Hanghoa, tri_Gia_Hoa_Don, dongia_Hoadon, ma_Tiente_Hoadon, donvi_Dongia_Tiente, tri_Gia_Tinh_Thue_S, " +
//                "tri_Gia_Tinh_Thue_M, dongia_Tinhthue, thuesuat_Nhapkhau, tien_Thue_Nhapkhau, xuatXu, ma_Vanbanphapquy, phanloai_Giayphep_Nk, ma_Bieuthue_Nk, ma_Miengiam_Thue ) " +
//                "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
//
//        jdbcTemplate.batchUpdate(insertSQL, batchList, batchList.size(), (ps, e) -> {
////            ps.setString(1,e.getTkid() );
//            ps.setString(2, e.getSotk());
//            ps.setString(3, e.getMahq());
//            ps.setString(4, e.getTrangthaitk());
//            ps.setString(5, e.getBpkthsdt());
//            ps.setString(6, e.getBptq());
//            ps.setString(7, e.getPtvc());
//            ps.setString(8, e.getMalh());
//            ps.setDate(9, formatterService.parseSqlDate(e.getNgayDk()));
//            ps.setTime(10, formatterService.parseSqlTime(e.getHourDk()) );
//            ps.setDate(11, formatterService.parseSqlDate(e.getNgayThaydoiDk())  );
//            ps.setTime(12, formatterService.parseSqlTime( e.getHourThaydoiDk()) );
//            ps.setString(13, e.getMasothueKbhq());
//            ps.setString(14, e.getTenDoanhnghiep());
//            ps.setString(15, e.getSodienthoai());
//            ps.setString(16, e.getTenDoanhnghiepUythac());
//            ps.setString(17, e.getTenDoitacnuocngoai());
//            ps.setString(18, e.getMaquocgiaDoitacnuocngoai());
//            ps.setString(19, e.getVandon01());
//            ps.setString(20, e.getVandon02());
//            ps.setString(21, e.getVandon03());
//            ps.setString(22, e.getVandon04());
//            ps.setString(23, e.getVandon05());
//            ps.setString(24, e.getSoluongkienhang());
//            ps.setString(25, e.getMaDvtKienhang());
//            ps.setString(26, e.getGrossweight());
//            ps.setString(27, e.getMaDvtGw());
//            ps.setString(28, e.getSoluongContainer());
//            ps.setString(29, e.getMaDiadiemdohang());
//            ps.setString(30, e.getMaDiadiemxephang());
//            ps.setString(31, e.getTenPhuongtienvanchuyen());
//            ps.setString(32, e.getNgayHangDen());
//            ps.setString(33, e.getPhuongThucThanhToan());
//            ps.setString(34, e.getTongTriGiaHoaDon());
//            ps.setString(35, e.getTongTriGiaTinhThue());
//            ps.setString(36, e.getTongTienThue());
//            ps.setString(37, e.getTongSoDonghang());
//            ps.setString(38, e.getNgayCapPhep());
//            ps.setString(39, e.getGioCapPhep());
//            ps.setString(40, e.getNgayHoanthanhKiemtra());
//            ps.setString(41, e.getGioHoanthanhKiemtra());
//            ps.setString(42, e.getNgayHuyTk());
//            ps.setString(43, e.getGioHuyTk());
//            ps.setString(44, e.getTenNguoiphutrachKiemtrahoso());
//            ps.setString(45, e.getTenNguoiphutrachKiemhoa());
//            ps.setString(46, e.getHsCode());
//            ps.setString(47, e.getMoTaHangHoa());
//            ps.setString(48, e.getSoLuongHanghoa());
//            ps.setString(49, e.getMaDvtHanghoa());
//            ps.setString(50, e.getTriGiaHoaDon());
//            ps.setString(51, e.getDongiaHoadon());
//            ps.setString(52, e.getMaTienteHoadon());
//            ps.setString(53, e.getDonviDongiaTiente());
//            ps.setString(54, e.getTriGiaTinhThueS());
//            ps.setString(55, e.getTriGiaTinhThueM());
//            ps.setString(56, e.getDongiaTinhthue());
//            ps.setString(57, e.getThuesuatNhapkhau());
//            ps.setString(58, e.getTienThueNhapkhau());
//            ps.setString(59, e.getXuatxu());
//            ps.setString(60, e.getMaVanbanphapquy());
//            ps.setString(61, e.getPhanloaiGiayphepNk());
//            ps.setString(62, e.getMaBieuthueNk());
//            ps.setString(63, e.getMaMiengiamThue());
//
//        });
//    }
//
//
//}
