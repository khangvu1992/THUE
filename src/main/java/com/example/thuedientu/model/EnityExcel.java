package com.example.thuedientu.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "thue_nhap_khau")
@Data
public class EnityExcel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long tkid;

//    @Column(name = "tkid", columnDefinition = "BIGINT")
//    private String tkid;

    @Column(name = "sotk", columnDefinition = "NVARCHAR(255)")// số tờ khai
    private String sotk;

    @Column(name = "mahq", columnDefinition = "NVARCHAR(255)")// mã chi cục hải quan
    private String mahq;

    @Column(name = "trangthaitk", columnDefinition = "NVARCHAR(255)") // mã phân loại trạng thái sau cùng
    private String trangthaitk;

    @Column(name = "bpkthsdt", columnDefinition = "BIGINT")// bộ phận kiểm tra hồ sơ đầu tiên
    private String bpkthsdt;

    @Column(name = "bptq", columnDefinition = "NVARCHAR(255)")// bo phận kiểm tra hồ sơ sau cùng
    private String bptq;

    @Column(name = "ptvc", columnDefinition = "NVARCHAR(255)")//phương thức vận chuyển
    private String ptvc;

    @Column(name = "malh", columnDefinition = "NVARCHAR(255)")//mã loại hinh
    private String malh;

    @Column(name = "ngay_Dk", columnDefinition = "DATE")// ngày đăng ký
    private String ngayDk;

    @Column(name = "hour_Dk", columnDefinition = "TIME(0)")// gờ đăng ký
    private String hourDk;

    @Column(name = "ngay_Thaydoi_Dk", columnDefinition = "DATE")// ngày thay đổi đăng ký
    private String ngayThaydoiDk;

    @Column(name = "hour_Thaydoi_Dk", columnDefinition = "TIME(0)")//giờ thay đổi đăng ký
    private String hourThaydoiDk;

    @Column(name = "masothue_Kbhq", columnDefinition = "NVARCHAR(255)")
    private String masothueKbhq;

    @Column(name = "ten_Doanhnghiep", columnDefinition = "NVARCHAR(255)")
    private String tenDoanhnghiep;

    @Column(name = "sodienthoai", columnDefinition = "NVARCHAR(255)")
    private String sodienthoai;

    @Column(name = "ten_Doanhnghiep_Uythac", columnDefinition = "NVARCHAR(255)")
    private String tenDoanhnghiepUythac;

    @Column(name = "ten_Doitacnuocngoai", columnDefinition = "NVARCHAR(255)")
    private String tenDoitacnuocngoai;

    @Column(name = "maquocgia_Doitacnuocngoai", columnDefinition = "NCHAR(10)")
    private String maquocgiaDoitacnuocngoai;

    @Column(name = "vandon_01", columnDefinition = "NVARCHAR(255)")
    private String vandon01;

    @Column(name = "vandon_02", columnDefinition = "NVARCHAR(255)")
    private String vandon02;

    @Column(name = "vandon_03", columnDefinition = "NVARCHAR(255)")
    private String vandon03;

    @Column(name = "vandon_04", columnDefinition = "NVARCHAR(255)")
    private String vandon04;

    @Column(name = "vandon_05", columnDefinition = "NVARCHAR(255)")
    private String vandon05;

    @Column(name = "soluongkienhang", columnDefinition = "BIGINT")
    private String soluongkienhang;

    @Column(name = "ma_Dvt_Kienhang", columnDefinition = "NVARCHAR(255)")
    private String maDvtKienhang;

    @Column(name = "grossweight", columnDefinition = "DECIMAL(15,3)")
    private String grossweight;

    @Column(name = "ma_Dvt_Gw", columnDefinition = "NVARCHAR(255)")
    private String maDvtGw;

    @Column(name = "soluong_Container", columnDefinition = "BIGINT")
    private String soluongContainer;

    @Column(name = "ma_Diadiemdohang", columnDefinition = "NVARCHAR(255)")
    private String maDiadiemdohang;

    @Column(name = "ma_Diadiemxephang", columnDefinition = "NVARCHAR(255)")
    private String maDiadiemxephang;

    @Column(name = "ten_Phuongtienvanchuyen", columnDefinition = "NVARCHAR(255)")
    private String tenPhuongtienvanchuyen;

    @Column(name = "ngay_Hang_Den", columnDefinition = "DATE")
    private String ngayHangDen;

    @Column(name = "phuong_Thuc_Thanh_Toan", columnDefinition = "NVARCHAR(255)")
    private String phuongThucThanhToan;

    @Column(name = "tong_Tri_Gia_Hoa_Don", columnDefinition = "DECIMAL(20,3)")
    private String tongTriGiaHoaDon;

    @Column(name = "tong_Tri_Gia_Tinh_Thue", columnDefinition = "DECIMAL(20,3)")
    private String tongTriGiaTinhThue;

    @Column(name = "tong_Tien_Thue", columnDefinition = "NVARCHAR(255)")
    private String tongTienThue;

    @Column(name = "tong_So_Donghang", columnDefinition = "BIGINT")
    private String tongSoDonghang;

    @Column(name = "ngay_Cap_Phep", columnDefinition = "DATE")
    private String ngayCapPhep;

    @Column(name = "gio_Cap_Phep", columnDefinition = "TIME(0)")
    private String gioCapPhep;

    @Column(name = "ngay_Hoanthanh_Kiemtra", columnDefinition = "DATE")
    private String ngayHoanthanhKiemtra;

    @Column(name = "gio_Hoanthanh_Kiemtra", columnDefinition = "TIME(0)")
    private String gioHoanthanhKiemtra;

    @Column(name = "ngay_Huy_Tk", columnDefinition = "DATE")
    private String ngayHuyTk;

    @Column(name = "gio_Huy_Tk", columnDefinition = "TIME(0)")
    private String gioHuyTk;

    @Column(name = "ten_Nguoiphutrach_Kiemtrahoso", columnDefinition = "NVARCHAR(255)")
    private String tenNguoiphutrachKiemtrahoso;

    @Column(name = "ten_Nguoiphutrach_Kiemhoa", columnDefinition = "NVARCHAR(255)")
    private String tenNguoiphutrachKiemhoa;

    @Column(name = "hs_Code", columnDefinition = "NVARCHAR(255)")
    private String hsCode;

    @Column(name = "mo_Ta_Hang_Hoa", columnDefinition = "NVARCHAR(4000)")
    private String moTaHangHoa;

    @Column(name = "so_Luong_Hanghoa", columnDefinition = "DECIMAL(20,3)")
    private String soLuongHanghoa;

    @Column(name = "ma_Dvt_Hanghoa", columnDefinition = "NVARCHAR(255)")
    private String maDvtHanghoa;

    @Column(name = "tri_Gia_Hoa_Don", columnDefinition = "DECIMAL(20,3)")
    private String triGiaHoaDon;

    @Column(name = "dongia_Hoadon", columnDefinition = "DECIMAL(20,3)")
    private String dongiaHoadon;

    @Column(name = "ma_Tiente_Hoadon", columnDefinition = "NVARCHAR(255)")
    private String maTienteHoadon;

    @Column(name = "donvi_Dongia_Tiente", columnDefinition = "NVARCHAR(255)")
    private String donviDongiaTiente;

    @Column(name = "tri_Gia_Tinh_Thue_S", columnDefinition = "DECIMAL(20,3)")
    private String triGiaTinhThueS;

    @Column(name = "tri_Gia_Tinh_Thue_M", columnDefinition = "DECIMAL(20,3)")
    private String triGiaTinhThueM;

    @Column(name = "dongia_Tinhthue", columnDefinition = "DECIMAL(20,3)")
    private String dongiaTinhthue;

    @Column(name = "thuesuat_Nhapkhau", columnDefinition = "NVARCHAR(255)")
    private String thuesuatNhapkhau;

    @Column(name = "tien_Thue_Nhapkhau", columnDefinition = "DECIMAL(20,3)")
    private String tienThueNhapkhau;

    @Column(name = "xuatxu", columnDefinition = "NVARCHAR(255)")
    private String xuatxu;

    @Column(name = "ma_Vanbanphapquy", columnDefinition = "NVARCHAR(255)")
    private String maVanbanphapquy;

    @Column(name = "phanloai_Giayphep_Nk", columnDefinition = "NVARCHAR(255)")
    private String phanloaiGiayphepNk;

    @Column(name = "ma_Bieuthue_Nk", columnDefinition = "NVARCHAR(255)")
    private String maBieuthueNk;

    @Column(name = "ma_Miengiam_Thue", columnDefinition = "NVARCHAR(255)")
    private String maMiengiamThue;



}
