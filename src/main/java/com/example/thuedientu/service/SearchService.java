package com.example.thuedientu.service;

import com.example.thuedientu.model.EnityExcelJDBC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

@Service
public class SearchService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<EnityExcelJDBC> searchEntities(String tkid, String sotk, String mahq, String trangthaitk,
                                               Long bpkthsdt, String bptq, String ptvc, String malh,
                                               Date ngayDk, Time hourDk, Date ngayThayDoiDk, Time hourThayDoiDk,
                                               String masothueKbhq, String tenDoanhnghiep, String sodienthoai,
                                               String tenDoanhnghiepUythac, String tenDoitacnuocngoai,
                                               String maquocgiaDoitacnuocngoai, String vandon01, String vandon02,
                                               String vandon03, String vandon04, String vandon05, Long soluongkienhang,
                                               String maDvtKienhang, Double grossweight, String maDvtGw, Long soluongContainer,
                                               String maDiadiemdohang, String maDiadiemxephang, String tenPhuongtienvanchuyen,
                                               Date ngayHangDen, String phuongThucThanhToan, Double tongTriGiaHoaDon,
                                               Double tongTriGiaTinhThue, Double tongTienThue, Long tongSoDonghang,
                                               Date ngayCapPhep, Time gioCapPhep, Date ngayHoanthanhKiemtra, Time gioHoanthanhKiemtra,
                                               Date ngayHuyTk, Time gioHuyTk, String tenNguoiphutrachKiemtrahoso,
                                               String tenNguoiphutrachKiemhoa, String hsCode, String moTaHangHoa,
                                               Double soLuongHanghoa, String maDvtHanghoa, Double triGiaHoaDon, Double dongiaHoadon,
                                               String maTienteHoadon, String donviDongiaTiente, Double triGiaTinhThueS,
                                               Double triGiaTinhThueM, Double dongiaTinhthue, String thuesuatNhapkhau,
                                               Double tienThueNhapkhau, String xuatXu, String maVanbanphapquy,
                                               String phanloaiGiayphepNk, String maBieuthueNk, String maMiengiamThue) {

        // Tạo danh sách tham số truy vấn
        List<Object> params = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT * FROM khang_hehe WHERE 1=1");

        // Kiểm tra từng trường và thêm điều kiện vào câu truy vấn
        if (tkid != null && !tkid.isEmpty()) {
            sql.append(" AND TKID LIKE ?");
            params.add("%" + tkid + "%");
        }
        if (sotk != null && !sotk.isEmpty()) {
            sql.append(" AND SOTK LIKE ?");
            params.add("%" + sotk + "%");
        }
        if (mahq != null && !mahq.isEmpty()) {
            sql.append(" AND MAHQ LIKE ?");
            params.add("%" + mahq + "%");
        }
        if (trangthaitk != null && !trangthaitk.isEmpty()) {
            sql.append(" AND TRANGTHAITK LIKE ?");
            params.add("%" + trangthaitk + "%");
        }
        if (bpkthsdt != null) {
            sql.append(" AND BPKTHSDT = ?");
            params.add(bpkthsdt);
        }
        if (bptq != null && !bptq.isEmpty()) {
            sql.append(" AND BPTQ LIKE ?");
            params.add("%" + bptq + "%");
        }
        if (ptvc != null && !ptvc.isEmpty()) {
            sql.append(" AND PTVC LIKE ?");
            params.add("%" + ptvc + "%");
        }
        if (malh != null && !malh.isEmpty()) {
            sql.append(" AND MALH LIKE ?");
            params.add("%" + malh + "%");
        }
        if (ngayDk != null) {
            sql.append(" AND NGAY_DK = ?");
            params.add(ngayDk);
        }
        if (hourDk != null) {
            sql.append(" AND HOUR_DK = ?");
            params.add(hourDk);
        }
        if (ngayThayDoiDk != null) {
            sql.append(" AND NGAY_THAY_DOI_DK = ?");
            params.add(ngayThayDoiDk);
        }
        if (hourThayDoiDk != null) {
            sql.append(" AND HOUR_THAY_DOI_DK = ?");
            params.add(hourThayDoiDk);
        }
        if (masothueKbhq != null && !masothueKbhq.isEmpty()) {
            sql.append(" AND MASOTHUE_KBHQ LIKE ?");
            params.add("%" + masothueKbhq + "%");
        }
        if (tenDoanhnghiep != null && !tenDoanhnghiep.isEmpty()) {
            sql.append(" AND TEN_DOANHNGHIEP LIKE ?");
            params.add("%" + tenDoanhnghiep + "%");
        }
        if (sodienthoai != null && !sodienthoai.isEmpty()) {
            sql.append(" AND SODIENTHOAI LIKE ?");
            params.add("%" + sodienthoai + "%");
        }
        if (tenDoanhnghiepUythac != null && !tenDoanhnghiepUythac.isEmpty()) {
            sql.append(" AND TEN_DOANHNGHIEP_UYTHAC LIKE ?");
            params.add("%" + tenDoanhnghiepUythac + "%");
        }
        if (tenDoitacnuocngoai != null && !tenDoitacnuocngoai.isEmpty()) {
            sql.append(" AND TEN_DOITACNUOCNGOAI LIKE ?");
            params.add("%" + tenDoitacnuocngoai + "%");
        }
        if (maquocgiaDoitacnuocngoai != null && !maquocgiaDoitacnuocngoai.isEmpty()) {
            sql.append(" AND MAQUOCGIA_DOITACNUOCNGOAI LIKE ?");
            params.add("%" + maquocgiaDoitacnuocngoai + "%");
        }
        if (vandon01 != null && !vandon01.isEmpty()) {
            sql.append(" AND VANDON_01 LIKE ?");
            params.add("%" + vandon01 + "%");
        }
        if (vandon02 != null && !vandon02.isEmpty()) {
            sql.append(" AND VANDON_02 LIKE ?");
            params.add("%" + vandon02 + "%");
        }
        if (vandon03 != null && !vandon03.isEmpty()) {
            sql.append(" AND VANDON_03 LIKE ?");
            params.add("%" + vandon03 + "%");
        }
        if (vandon04 != null && !vandon04.isEmpty()) {
            sql.append(" AND VANDON_04 LIKE ?");
            params.add("%" + vandon04 + "%");
        }
        if (vandon05 != null && !vandon05.isEmpty()) {
            sql.append(" AND VANDON_05 LIKE ?");
            params.add("%" + vandon05 + "%");
        }
        if (soluongkienhang != null) {
            sql.append(" AND SOLUONGKIENHANG = ?");
            params.add(soluongkienhang);
        }
        if (maDvtKienhang != null && !maDvtKienhang.isEmpty()) {
            sql.append(" AND MA_DVT_KIENHANG LIKE ?");
            params.add("%" + maDvtKienhang + "%");
        }
        if (grossweight != null) {
            sql.append(" AND GROSSWEIGHT = ?");
            params.add(grossweight);
        }
        if (maDvtGw != null && !maDvtGw.isEmpty()) {
            sql.append(" AND MA_DVT_GW LIKE ?");
            params.add("%" + maDvtGw + "%");
        }
        if (soluongContainer != null) {
            sql.append(" AND SOLUONG_CONTAINER = ?");
            params.add(soluongContainer);
        }
        if (maDiadiemdohang != null && !maDiadiemdohang.isEmpty()) {
            sql.append(" AND MA_DIADIEMDOHANG LIKE ?");
            params.add("%" + maDiadiemdohang + "%");
        }
        if (maDiadiemxephang != null && !maDiadiemxephang.isEmpty()) {
            sql.append(" AND MA_DIADIEMXEPHANG LIKE ?");
            params.add("%" + maDiadiemxephang + "%");
        }
        if (tenPhuongtienvanchuyen != null && !tenPhuongtienvanchuyen.isEmpty()) {
            sql.append(" AND TEN_PHUONGTIENVANCHUYEN LIKE ?");
            params.add("%" + tenPhuongtienvanchuyen + "%");
        }
        if (ngayHangDen != null) {
            sql.append(" AND NGAY_HANG_DEN = ?");
            params.add(ngayHangDen);
        }
        if (phuongThucThanhToan != null && !phuongThucThanhToan.isEmpty()) {
            sql.append(" AND PHUONG_THUC_THANH_TOAN LIKE ?");
            params.add("%" + phuongThucThanhToan + "%");
        }
        if (tongTriGiaHoaDon != null) {
            sql.append(" AND TONG_TRI_GIA_HOA_DON = ?");
            params.add(tongTriGiaHoaDon);
        }
        if (tongTriGiaTinhThue != null) {
            sql.append(" AND TONG_TRI_GIA_TINH_THUE = ?");
            params.add(tongTriGiaTinhThue);
        }
        if (tongTienThue != null) {
            sql.append(" AND TONG_TIEN_THUE = ?");
            params.add(tongTienThue);
        }
        if (tongSoDonghang != null) {
            sql.append(" AND TONG_SO_DONGHANG = ?");
            params.add(tongSoDonghang);
        }
        if (ngayCapPhep != null) {
            sql.append(" AND NGAY_CAPPHEP = ?");
            params.add(ngayCapPhep);
        }
        if (gioCapPhep != null) {
            sql.append(" AND HOUR_CAPPHEP = ?");
            params.add(gioCapPhep);
        }
        if (ngayHoanthanhKiemtra != null) {
            sql.append(" AND NGAY_HOANTHANH_KIEMTRA = ?");
            params.add(ngayHoanthanhKiemtra);
        }
        if (gioHoanthanhKiemtra != null) {
            sql.append(" AND HOUR_HOANTHANH_KIEMTRA = ?");
            params.add(gioHoanthanhKiemtra);
        }
        if (ngayHuyTk != null) {
            sql.append(" AND NGAY_HUY_TK = ?");
            params.add(ngayHuyTk);
        }
        if (gioHuyTk != null) {
            sql.append(" AND HOUR_HUY_TK = ?");
            params.add(gioHuyTk);
        }
        if (tenNguoiphutrachKiemtrahoso != null && !tenNguoiphutrachKiemtrahoso.isEmpty()) {
            sql.append(" AND TEN_NGUOIPHUTRACH_KIEMTRAHOSO LIKE ?");
            params.add("%" + tenNguoiphutrachKiemtrahoso + "%");
        }
        if (tenNguoiphutrachKiemhoa != null && !tenNguoiphutrachKiemhoa.isEmpty()) {
            sql.append(" AND TEN_NGUOIPHUTRACH_KIEMHOA LIKE ?");
            params.add("%" + tenNguoiphutrachKiemhoa + "%");
        }
        if (hsCode != null && !hsCode.isEmpty()) {
            sql.append(" AND HSCODE LIKE ?");
            params.add("%" + hsCode + "%");
        }
        if (moTaHangHoa != null && !moTaHangHoa.isEmpty()) {
            sql.append(" AND MO_TA_HANGHOA LIKE ?");
            params.add("%" + moTaHangHoa + "%");
        }
        if (soLuongHanghoa != null) {
            sql.append(" AND SO_LUONG_HANGHOA = ?");
            params.add(soLuongHanghoa);
        }
        if (maDvtHanghoa != null && !maDvtHanghoa.isEmpty()) {
            sql.append(" AND MA_DVT_HANGHOA LIKE ?");
            params.add("%" + maDvtHanghoa + "%");
        }
        if (triGiaHoaDon != null) {
            sql.append(" AND TRI_GIA_HOA_DON = ?");
            params.add(triGiaHoaDon);
        }
        if (dongiaHoadon != null) {
            sql.append(" AND DONGIA_HOADON = ?");
            params.add(dongiaHoadon);
        }
        if (maTienteHoadon != null && !maTienteHoadon.isEmpty()) {
            sql.append(" AND MA_TIENTE_HOADON LIKE ?");
            params.add("%" + maTienteHoadon + "%");
        }
        if (donviDongiaTiente != null && !donviDongiaTiente.isEmpty()) {
            sql.append(" AND DONVI_DONGIA_TIENTE LIKE ?");
            params.add("%" + donviDongiaTiente + "%");
        }
        if (triGiaTinhThueS != null) {
            sql.append(" AND TRI_GIA_TINH_THUE_S = ?");
            params.add(triGiaTinhThueS);
        }
        if (triGiaTinhThueM != null) {
            sql.append(" AND TRI_GIA_TINH_THUE_M = ?");
            params.add(triGiaTinhThueM);
        }
        if (dongiaTinhthue != null) {
            sql.append(" AND DONGIA_TINHTHUE = ?");
            params.add(dongiaTinhthue);
        }
        if (thuesuatNhapkhau != null && !thuesuatNhapkhau.isEmpty()) {
            sql.append(" AND THUESUAT_NHAPKHAU LIKE ?");
            params.add("%" + thuesuatNhapkhau + "%");
        }
        if (tienThueNhapkhau != null) {
            sql.append(" AND TIEN_THUE_NHAPKHAU = ?");
            params.add(tienThueNhapkhau);
        }
        if (xuatXu != null && !xuatXu.isEmpty()) {
            sql.append(" AND XUAT_XU LIKE ?");
            params.add("%" + xuatXu + "%");
        }
        if (maVanbanphapquy != null && !maVanbanphapquy.isEmpty()) {
            sql.append(" AND MA_VANBANPHAPQUY LIKE ?");
            params.add("%" + maVanbanphapquy + "%");
        }
        if (phanloaiGiayphepNk != null && !phanloaiGiayphepNk.isEmpty()) {
            sql.append(" AND PHANLOAI_GIAYPHEPNK LIKE ?");
            params.add("%" + phanloaiGiayphepNk + "%");
        }
        if (maBieuthueNk != null && !maBieuthueNk.isEmpty()) {
            sql.append(" AND MA_BIEUTHUENK LIKE ?");
            params.add("%" + maBieuthueNk + "%");
        }
        if (maMiengiamThue != null && !maMiengiamThue.isEmpty()) {
            sql.append(" AND MA_MIENGIAMTHUE LIKE ?");
            params.add("%" + maMiengiamThue + "%");
        }

        // Truy vấn dữ liệu
        return jdbcTemplate.query(sql.toString(), params.toArray(), new RowMapper<EnityExcelJDBC>() {
            @Override
            public EnityExcelJDBC mapRow(ResultSet rs, int rowNum) throws SQLException {
                EnityExcelJDBC entity = new EnityExcelJDBC();
                entity.setTkid(rs.getString("TKID"));
                entity.setSotk(rs.getString("SOTK"));
                entity.setMahq(rs.getString("MAHQ"));
                entity.setTrangthaitk(rs.getString("TRANGTHAITK"));
                entity.setBpkthsdt(rs.getString("BPKTHSDT"));
                entity.setBptq(rs.getString("BPTQ"));
                entity.setPtvc(rs.getString("PTVC"));
                entity.setMalh(rs.getString("MALH"));
                entity.setNgayDk(String.valueOf(rs.getDate("NGAY_DK")));
                entity.setHourDk(String.valueOf(rs.getTime("HOUR_DK")));
                entity.setNgayThaydoiDk(String.valueOf(rs.getDate("NGAY_THAY_DOI_DK")));
                entity.setHourDk(String.valueOf(rs.getTime("HOUR_THAY_DOI_DK")));
                entity.setMasothueKbhq(rs.getString("MASOTHUE_KBHQ"));
                entity.setTenDoanhnghiep(rs.getString("TEN_DOANHNGHIEP"));
                entity.setSodienthoai(rs.getString("SODIENTHOAI"));
                entity.setTenDoanhnghiepUythac(rs.getString("TEN_DOANHNGHIEP_UYTHAC"));
                entity.setTenDoitacnuocngoai(rs.getString("TEN_DOITACNUOCNGOAI"));
                entity.setMaquocgiaDoitacnuocngoai(rs.getString("MAQUOCGIA_DOITACNUOCNGOAI"));
                entity.setVandon01(rs.getString("VANDON_01"));
                entity.setVandon02(rs.getString("VANDON_02"));
                entity.setVandon03(rs.getString("VANDON_03"));
                entity.setVandon04(rs.getString("VANDON_04"));
                entity.setVandon05(rs.getString("VANDON_05"));
                entity.setSoluongkienhang(String.valueOf(rs.getLong("SOLUONGKIENHANG")));
                entity.setMaDvtKienhang(rs.getString("MA_DVT_KIENHANG"));
                entity.setGrossweight(String.valueOf(rs.getDouble("GROSSWEIGHT")));
                entity.setMaDvtGw(rs.getString("MA_DVT_GW"));
                entity.setSoluongContainer(String.valueOf(rs.getLong("SOLUONG_CONTAINER")));
                entity.setMaDiadiemdohang(rs.getString("MA_DIADIEMDOHANG"));
                entity.setMaDiadiemxephang(rs.getString("MA_DIADIEMXEPHANG"));
                entity.setTenPhuongtienvanchuyen(rs.getString("TEN_PHUONGTIENVANCHUYEN"));
                entity.setNgayHangDen(String.valueOf(rs.getDate("NGAY_HANG_DEN")));
                entity.setPhuongThucThanhToan(rs.getString("PHUONG_THUC_THANH_TOAN"));
                entity.setTongTriGiaHoaDon(String.valueOf(rs.getDouble("TONG_TRI_GIA_HOA_DON")));
                entity.setTongTriGiaTinhThue(String.valueOf(rs.getDouble("TONG_TRI_GIA_TINH_THUE")));
                entity.setTongTienThue(String.valueOf(rs.getDouble("TONG_TIEN_THUE")));
                entity.setTongSoDonghang(String.valueOf(rs.getLong("TONG_SO_DONGHANG")));
                entity.setNgayCapPhep(String.valueOf(rs.getDate("NGAY_CAPPHEP")));
                entity.setGioCapPhep(String.valueOf(rs.getTime("HOUR_CAPPHEP")));
                entity.setNgayHoanthanhKiemtra(String.valueOf(rs.getDate("NGAY_HOANTHANH_KIEMTRA")));
                entity.setGioHoanthanhKiemtra(String.valueOf(rs.getTime("HOUR_HOANTHANH_KIEMTRA")));
                entity.setNgayHuyTk(String.valueOf(rs.getDate("NGAY_HUY_TK")));
                entity.setGioHuyTk(String.valueOf(rs.getTime("HOUR_HUY_TK")));
                entity.setTenNguoiphutrachKiemtrahoso(rs.getString("TEN_NGUOIPHUTRACH_KIEMTRAHOSO"));
                entity.setTenNguoiphutrachKiemhoa(rs.getString("TEN_NGUOIPHUTRACH_KIEMHOA"));
                entity.setHsCode(rs.getString("HSCODE"));
                entity.setMoTaHangHoa(rs.getString("MO_TA_HANGHOA"));
                entity.setSoLuongHanghoa(String.valueOf(rs.getDouble("SO_LUONG_HANGHOA")));
                entity.setMaDvtHanghoa(rs.getString("MA_DVT_HANGHOA"));
                entity.setTriGiaHoaDon(String.valueOf(rs.getDouble("TRI_GIA_HOA_DON")));
                entity.setDongiaHoadon(String.valueOf(rs.getDouble("DONGIA_HOADON")));
                entity.setMaTienteHoadon(rs.getString("MA_TIENTE_HOADON"));
                entity.setDonviDongiaTiente(rs.getString("DONVI_DONGIA_TIENTE"));
                entity.setTriGiaTinhThueS(String.valueOf(rs.getDouble("TRI_GIA_TINH_THUE_S")));
                entity.setTriGiaTinhThueM(String.valueOf(rs.getDouble("TRI_GIA_TINH_THUE_M")));
                entity.setDongiaTinhthue(String.valueOf(rs.getDouble("DONGIA_TINHTHUE")));
                entity.setThuesuatNhapkhau(rs.getString("THUESUAT_NHAPKHAU"));
                entity.setTienThueNhapkhau(String.valueOf(rs.getDouble("TIEN_THUE_NHAPKHAU")));
                entity.setXuatxu(rs.getString("XUAT_XU"));
                entity.setMaVanbanphapquy(rs.getString("MA_VANBANPHAPQUY"));
                entity.setPhanloaiGiayphepNk(rs.getString("PHANLOAI_GIAYPHEPNK"));
                entity.setMaBieuthueNk(rs.getString("MA_BIEUTHUENK"));
                entity.setMaMiengiamThue(rs.getString("MA_MIENGIAMTHUE"));
                return entity;
            }
        });
    }
}

