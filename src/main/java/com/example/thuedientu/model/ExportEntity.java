package com.example.thuedientu.model;

import jakarta.persistence.*;
import lombok.Data;
import org.apache.poi.hpsf.Date;

import java.math.BigDecimal;

@Entity
@Table(name = "thue_xuat_khau")
@Data
public class ExportEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "so_to_khai")
    private String soToKhai;

    @Column(name = "ma_chi_cuc_hai_quan_tao_moi")
    private String maChiCucHaiQuanTaoMoi;

    @Column(name = "ma_phan_loai_trang_thai_sau_cung")
    private String maPhanLoaiTrangThaiSauCung;

    @Column(name = "bo_phan_kiem_tra_ho_so_dau_tien")
    private String boPhanKiemTraHoSoDauTien;

    @Column(name = "bo_phan_kiem_tra_ho_so_sau_cung")
    private String boPhanKiemTraHoSoSauCung;

    @Column(name = "phuong_thuc_van_chuyen")
    private String phuongThucVanChuyen;

    @Column(name = "ma_loai_hinh")
    private String maLoaiHinh;

    @Column(name = "ngay_dang_ky", columnDefinition = "DATE")
    private java.sql.Date ngayDangKy;

    @Column(name = "gio_dang_ky")
    private java.sql.Time gioDangKy;

    @Column(name = "ngay_thay_doi_dang_ky")
    private java.sql.Date ngayThayDoiDangKy;

    @Column(name = "gio_thay_doi_dang_ky")
    private java.sql.Time gioThayDoiDangKy;

    @Column(name = "ma_nguoi_xuat_khau")
    private String maNguoiXuatKhau;

    @Column(name = "ten_nguoi_xuat_khau")
    private String tenNguoiXuatKhau;

    @Column(name = "ten_nguoi_nhap_khau")
    private String tenNguoiNhapKhau;

    @Column(name = "ma_nuoc")
    private String maNuoc;

    @Column(name = "so_van_don")
    private String soVanDon;

    @Column(name = "so_luong")
    private BigDecimal soLuong;

    @Column(name = "ma_don_vi_tinh")
    private String maDonViTinh;

    @Column(name = "tong_trong_luong_hang_gross")
    private BigDecimal  tongTrongLuongHangGross;

    @Column(name = "ma_don_vi_tinh_trong_luong_gross")
    private String maDonViTinhTrongLuongGross;

    @Column(name = "ma_dia_diem_nhan_hang_cuoi_cung")
    private String maDiaDiemNhanHangCuoiCung;

    @Column(name = "ma_dia_diem_xep_hang")
    private String maDiaDiemXepHang;

    @Column(name = "tong_tri_gia_hoa_don")
    private BigDecimal  tongTriGiaHoaDon;

    @Column(name = "tong_tri_gia_tinh_thue")
    private BigDecimal  tongTriGiaTinhThue;

    @Column(name = "tong_so_tien_thue_xuat_khau")
    private BigDecimal  tongSoTienThueXuatKhau;

    @Column(name = "tong_so_dong_hang_cua_to_khai")
    private BigDecimal  tongSoDongHangCuaToKhai;

    @Column(name = "phan_ghi_chu")
    private String phanGhiChu;

    @Column(name = "ngay_hoan_thanh_kiem_tra")
    private  java.sql.Date ngayHoanThanhKiemTra;

    @Column(name = "gio_hoan_thanh_kiem_tra")
    private  java.sql.Time gioHoanThanhKiemTra;

    @Column(name = "ngay_huy_khai_bao_hai_quan")
    private java.sql.Date ngayHuyKhaiBaoHaiQuan;

    @Column(name = "gio_huy_khai_bao_hai_quan")
    private java.sql.Time gioHuyKhaiBaoHaiQuan;

    @Column(name = "ten_nguoi_phu_trach_kiem_tra_ho_so")
    private String tenNguoiPhuTrachKiemTraHoSo;

    @Column(name = "ten_nguoi_phu_trach_kiem_hoa")
    private String tenNguoiPhuTrachKiemHoa;

    @Column(name = "ma_so_hang_hoa")
    private String maSoHangHoa;

    @Column(name = "mo_ta_hang_hoa")
    private String moTaHangHoa;

    @Column(name = "so_luong1")
    private BigDecimal soLuong1;

    @Column(name = "tri_gia_hoa_don")
    private BigDecimal triGiaHoaDon;

    @Column(name = "don_gia_hoa_don")
    private BigDecimal donGiaHoaDon;

    @Column(name = "ma_dong_tien_don_gia_hoa_don")
    private String maDongTienDonGiaHoaDon;

    @Column(name = "tri_gia_tinh_thue_s")
    private BigDecimal triGiaTinhThueS;

    @Column(name = "tri_gia_tinh_thue_m")
    private BigDecimal triGiaTinhThueM;

    @Column(name = "don_gia_tinh_thue")
    private BigDecimal donGiaTinhThue;

    @Column(name = "thue_suat_thue_xuat_khau")
    private String thueSuatThueXuatKhau;

    @Column(name = "phan_loai_nhap_thue_suat_thue_xuat_khau")
    private String phanLoaiNhapThueSuatThueXuatKhau;

    @Column(name = "so_tien_thue_xuat_khau")
    private BigDecimal soTienThueXuatKhau;

    @Column(name = "ma_van_ban_phap_quy_khac1")
    private String maVanBanPhapQuyKhac1;

    @Column(name = "ma_mien_giam_khong_chiu_thue_xuat_khau")
    private String maMienGiamKhongChiuThueXuatKhau;
}
