package com.example.thuedientu.model;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table( name="khang_hehe")
@Data
public class EnityExcel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "TKID",columnDefinition = "NVARCHAR(255)")
    private String Tkid;

    @Column(name = "SOTK",columnDefinition = "NVARCHAR(255)")
    private String Sotk;

    @Column(name = "MAHQ",columnDefinition = "NVARCHAR(255)")
    private String Mahq;

    @Column(name = "TRANGTHAITK",columnDefinition = "NVARCHAR(255)")
    private String trangthaitk;

    @Column(name = "BPKTHSDT",columnDefinition = "NVARCHAR(255)")
    private String bpkthsdt;

    @Column(name = "BPTQ",columnDefinition = "NVARCHAR(255)")
    private String bptq;

    @Column(name = "PTVC",columnDefinition = "NVARCHAR(255)")
    private String ptvc;

    @Column(name = "MALH",columnDefinition = "NVARCHAR(255)")
    private String malh;

    @Column(name = "NGAY_DK",columnDefinition = "NVARCHAR(255)")
    private String ngayDk;

    @Column(name = "HOUR_DK",columnDefinition = "NVARCHAR(255)")
    private String hourDk;

    @Column(name = "NGAY_THAYDOI_DK",columnDefinition = "NVARCHAR(255)")
    private String ngayThaydoiDk;

    @Column(name = "HOUR_THAYDOI_DK",columnDefinition = "NVARCHAR(255)")
    private String hourThaydoiDk;

    @Column(name = "MASOTHUE_KBHQ",columnDefinition = "NVARCHAR(255)")
    private String masothueKbhq;

    @Column(name = "TEN_DOANHNGIHEP",columnDefinition = "NVARCHAR(255)")
    private String tenDoanhnghiep;

    @Column(name = "SODIENTHOAI",columnDefinition = "NVARCHAR(255)")
    private String sodienthoai;

    @Column(name = "TEN_DOANHNGHIEP_UYTHAC",columnDefinition = "NVARCHAR(255)")
    private String tenDoanhnghiepUythac;

    @Column(name = "TEN_DOITACNUOCNGOAI",columnDefinition = "NVARCHAR(255)")
    private String tenDoitacnuocngoai;

    @Column(name = "MAQUOCGIA_DOITACNUOCNGOAI",columnDefinition = "NVARCHAR(255)")
    private String maquocgiaDoitacnuocngoai;

    @Column(name = "VANDON_01",columnDefinition = "NVARCHAR(255)")
    private String vandon01;

    @Column(name = "VANDON_02",columnDefinition = "NVARCHAR(255)")
    private String vandon02;

    @Column(name = "VANDON_03",columnDefinition = "NVARCHAR(255)")
    private String vandon03;

    @Column(name = "VANDON_04",columnDefinition = "NVARCHAR(255)")
    private String vandon04;

    @Column(name = "VANDON_05",columnDefinition = "NVARCHAR(255)")
    private String vandon05;

    @Column(name = "SOLUONGKIENHANG",columnDefinition = "NVARCHAR(255)")
    private String soluongkienhang;

    @Column(name = "MA_DVT_KIENHANG",columnDefinition = "NVARCHAR(255)")
    private String maDvtKienhang;

    @Column(name = "GROSSWEIGHT",columnDefinition = "NVARCHAR(255)")
    private String grossweight;

    @Column(name = "MA_DVT_GW",columnDefinition = "NVARCHAR(255)")
    private String maDvtGw;

    @Column(name = "SOLUONG_CONTAINER",columnDefinition = "NVARCHAR(255)")
    private String soluongContainer;

    @Column(name = "MA_DIADIEMDOHANG",columnDefinition = "NVARCHAR(255)")
    private String maDiadiemdohang;

    @Column(name = "MA_DIADIEMXEPHANG",columnDefinition = "NVARCHAR(255)")
    private String maDiadiemxephang;

    @Column(name = "TEN_PHUONGTIENVANCHUYEN",columnDefinition = "NVARCHAR(255)")
    private String tenPhuongtienvanchuyen;

    @Column(name = "NGAY_HANG_DEN",columnDefinition = "NVARCHAR(255)")
    private String ngayHangDen;

    @Column(name = "PHUONG_THUC_THANH_TOAN",columnDefinition = "NVARCHAR(255)")
    private String phuongThucThanhToan;

    @Column(name = "TONG_TRI_GIA_HOA_DON",columnDefinition = "NVARCHAR(255)")
    private String tongTriGiaHoaDon;

    @Column(name = "TONG_TRI_GIA_TINH_THUE",columnDefinition = "NVARCHAR(255)")
    private String tongTriGiaTinhThue;

    @Column(name = "TONG_TIEN_THUE",columnDefinition = "NVARCHAR(255)")
    private String tongTienThue;

    @Column(name = "TONG_SO_DONGHANG",columnDefinition = "NVARCHAR(255)")
    private String tongSoDonghang;

    @Column(name = "NGAY_CAP_PHEP",columnDefinition = "NVARCHAR(255)")
    private String ngayCapPhep;

    @Column(name = "GIO_CAP_PHEP",columnDefinition = "NVARCHAR(255)")
    private String gioCapPhep;

    @Column(name = "NGAY_HOANTHANH_KIEMTRA",columnDefinition = "NVARCHAR(255)")
    private String ngayHoanthanhKiemtra;

    @Column(name = "GIO_HOANTHANH_KIEMTRA",columnDefinition = "NVARCHAR(255)")
    private String gioHoanthanhKiemtra;

    @Column(name = "NGAY_HUY_TK",columnDefinition = "NVARCHAR(255)")
    private String ngayHuyTk;

    @Column(name = "GIO_HUY_TK",columnDefinition = "NVARCHAR(255)")
    private String gioHuyTk;

    @Column(name = "TEN_NGUOIPHUTRACH_KIEMTRAHOSO",columnDefinition = "NVARCHAR(255)")
    private String tenNguoiphutrachKiemtrahoso;

    @Column(name = "TEN_NGUOIPHUTRACH_KIEMHOA",columnDefinition = "NVARCHAR(255)")
    private String tenNguoiphutrachKiemhoa;

    @Column(name = "HS_CODE",columnDefinition = "NVARCHAR(255)")
    private String hsCode;

    @Column(name = "MO_TA_HANG_HOA",columnDefinition = "NVARCHAR(255)")
    private String moTaHangHoa;

    @Column(name = "SO_LUONG_HANGHOA",columnDefinition = "NVARCHAR(255)")
    private String soLuongHanghoa;

    @Column(name = "MA_DVT_HANGHOA",columnDefinition = "NVARCHAR(255)")
    private String maDvtHanghoa;

    @Column(name = "TRI_GIA_HOA_DON",columnDefinition = "NVARCHAR(255)")
    private String triGiaHoaDon;

    @Column(name = "DONGIA_HOADON",columnDefinition = "NVARCHAR(255)")
    private String dongiaHoadon;

    @Column(name = "MA_TIENTE_HOADON",columnDefinition = "NVARCHAR(255)")
    private String maTienteHoadon;

    @Column(name = "DONVI_DONGIA_TIENTE",columnDefinition = "NVARCHAR(255)")
    private String donviDongiaTiente;

    @Column(name = "TRI_GIA_TINH_THUE_S",columnDefinition = "NVARCHAR(255)")
    private String triGiaTinhThueS;

    @Column(name = "TRI_GIA_TINH_THUE_M",columnDefinition = "NVARCHAR(255)")
    private String triGiaTinhThueM;

    @Column(name = "DONGIA_TINHTHUE",columnDefinition = "NVARCHAR(255)")
    private String dongiaTinhthue;

    @Column(name = "THUESUAT_NHAPKHAU",columnDefinition = "NVARCHAR(255)")
    private String thuesuatNhapkhau;

    @Column(name = "TIEN_THUE_NHAPKHAU",columnDefinition = "NVARCHAR(255)")
    private String tienThueNhapkhau;

    @Column(name = "XUATXU",columnDefinition = "NVARCHAR(255)")
    private String xuatxu;

    @Column(name = "MA_VANBANPHAPQUY",columnDefinition = "NVARCHAR(255)")
    private String maVanbanphapquy;

    @Column(name = "PHANLOAI_GIAYPHEP_NK",columnDefinition = "NVARCHAR(255)")
    private String phanloaiGiayphepNk;

    @Column(name = "MA_BIEUTHUE_NK",columnDefinition = "NVARCHAR(255)")
    private String maBieuthueNk;

    @Column(name = "MA_MIENGIAM_THUE",columnDefinition = "NVARCHAR(255)")
    private String maMiengiamThue;


}
