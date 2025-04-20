package com.example.thuedientu.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "thue_xuat_khau")
@Data
public class ExportEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String soToKhai;
    private String maChiCucHaiQuanTaoMoi;
    private String maPhanLoaiTrangThaiSauCung;
    private String boPhanKiemTraHoSoDauTien;
    private String boPhanKiemTraHoSoSauCung;
    private String phuongThucVanChuyen;
    private String maLoaiHinh;
    private String ngayDangKy;
    private String gioDangKy;
    private String ngayThayDoiDangKy;
    private String gioThayDoiDangKy;
    private String maNguoiXuatKhau;
    private String tenNguoiXuatKhau;
    private String tenNguoiNhapKhau;
    private String maNuoc;
    private String soVanDon;
    private String soLuong;
    private String maDonViTinh;
    private String tongTrongLuongHangGross;
    private String maDonViTinhTrongLuongGross;
    private String maDiaDiemNhanHangCuoiCung;
    private String maDiaDiemXepHang;
    private String tongTriGiaHoaDon;
    private String tongTriGiaTinhThue;
    private String tongSoTienThueXuatKhau;
    private String tongSoDongHangCuaToKhai;
    private String phanGhiChu;
    private String ngayHoanThanhKiemTra;
    private String gioHoanThanhKiemTra;
    private String ngayHuyKhaiBaoHaiQuan;
    private String gioHuyKhaiBaoHaiQuan;
    private String tenNguoiPhuTrachKiemTraHoSo;
    private String tenNguoiPhuTrachKiemHoa;
    private String maSoHangHoa;
    private String moTaHangHoa;
    private String soLuong1;
    private String triGiaHoaDon;
    private String donGiaHoaDon;
    private String maDongTienDonGiaHoaDon;
    private String triGiaTinhThueS;
    private String triGiaTinhThueM;
    private String donGiaTinhThue;
    private String thueSuatThueXuatKhau;
    private String phanLoaiNhapThueSuatThueXuatKhau;
    private String soTienThueXuatKhau;
    private String maVanBanPhapQuyKhac1;
    private String maMienGiamKhongChiuThueXuatKhau;

    // Getters and Setters
}
