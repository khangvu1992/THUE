package com.example.thuedientu.dto;
import com.fasterxml.jackson.annotation.JsonProperty;


import lombok.Data;

@Data
public class ExcelDataDTO {

    @JsonProperty("TKID")
    private String tkid;

    @JsonProperty("SOTK")
    private String sotk;

    @JsonProperty("MAHQ")
    private String mahq;

    @JsonProperty("TRANGTHAITK")
    private String trangthaitk;

    @JsonProperty("BPKTHSDT")
    private String bpkthsdt;

    @JsonProperty("BPTQ")
    private String bptq;

    @JsonProperty("PTVC")
    private String ptvc;

    @JsonProperty("MALH")
    private String malh;

    @JsonProperty("NGAY_DK")
    private String ngayDk;

    @JsonProperty("HOUR_DK")
    private String hourDk;

    @JsonProperty("NGAY_THAYDOI_DK")
    private String ngayThaydoiDk;

    @JsonProperty("HOUR_THAYDOI_DK")
    private String hourThaydoiDk;

    @JsonProperty("MASOTHUE_KBHQ")
    private String masothueKbhq;

    @JsonProperty("TEN_DOANHNGHIEP")
    private String tenDoanhnghiep;

    @JsonProperty("SODIENTHOAI")
    private String sodienthoai;

    @JsonProperty("TEN_DOANHNGHIEP_UYTHAC")
    private String tenDoanhnghiepUythac;

    @JsonProperty("TEN_DOITACNUOCNGOAI")
    private String tenDoitacnuocngoai;

    @JsonProperty("MAQUOCGIA_DOITACNUOCNGOAI")
    private String maquocgiaDoitacnuocngoai;

    @JsonProperty("VANDON_01")
    private String vandon01;

    @JsonProperty("VANDON_02")
    private String vandon02;

    @JsonProperty("VANDON_03")
    private String vandon03;

    @JsonProperty("VANDON_04")
    private String vandon04;

    @JsonProperty("VANDON_05")
    private String vandon05;

    @JsonProperty("SOLUONGKIENHANG")
    private Integer soluongkienhang;

    @JsonProperty("MA_DVT_KIENHANG")
    private String maDvtKienhang;

    @JsonProperty("GROSSWEIGHT")
    private Double grossweight;

    @JsonProperty("MA_DVT_GW")
    private String maDvtGw;

    @JsonProperty("SOLUONG_CONTAINER")
    private Integer soluongContainer;

    @JsonProperty("MA_DIADIEMDOHANG")
    private String maDiadiemdohang;

    @JsonProperty("MA_DIADIEMXEPHANG")
    private String maDiadiemxephang;

    @JsonProperty("TEN_PHUONGTIENVANCHUYEN")
    private String tenPhuongtienvanchuyen;

    @JsonProperty("NGAY_HANG_DEN")
    private String ngayHangDen;

    @JsonProperty("PHUONG_THUC_THANH_TOAN")
    private String phuongThucThanhToan;

    @JsonProperty("TONG_TRI_GIA_HOA_DON")
    private Double tongTriGiaHoaDon;

    @JsonProperty("TONG_TRI_GIA_TINH_THUE")
    private Double tongTriGiaTinhThue;

    @JsonProperty("TONG_TIEN_THUE")
    private Double tongTienThue;

    @JsonProperty("TONG_SO_DONGHANG")
    private Integer tongSoDonghang;

    @JsonProperty("NGAY_CAP_PHEP")
    private String ngayCapPhep;

    @JsonProperty("GIO_CAP_PHEP")
    private String gioCapPhep;

    @JsonProperty("NGAY_HOANTHANH_KIEMTRA")
    private String ngayHoanthanhKiemtra;

    @JsonProperty("GIO_HOANTHANH_KIEMTRA")
    private String gioHoanthanhKiemtra;

    @JsonProperty("NGAY_HUY_TK")
    private String ngayHuyTk;

    @JsonProperty("GIO_HUY_TK")
    private String gioHuyTk;

    @JsonProperty("TEN_NGUOIPHUTRACH_KIEMTRAHOSO")
    private String tenNguoiphutrachKiemtrahoso;

    @JsonProperty("TEN_NGUOIPHUTRACH_KIEMHOA")
    private String tenNguoiphutrachKiemhoa;

    @JsonProperty("HS_CODE")
    private String hsCode;

    @JsonProperty("MO_TA_HANG_HOA")
    private String moTaHangHoa;

    @JsonProperty("SO_LUONG_HANGHOA")
    private Integer soLuongHanghoa;

    @JsonProperty("MA_DVT_HANGHOA")
    private String maDvtHanghoa;

    @JsonProperty("TRI_GIA_HOA_DON")
    private Double triGiaHoaDon;

    @JsonProperty("DONGIA_HOADON")
    private Double dongiaHoadon;

    @JsonProperty("MA_TIENTE_HOADON")
    private String maTienteHoadon;

    @JsonProperty("DONVI_DONGIA_TIENTE")
    private String donviDongiaTiente;

    @JsonProperty("TRI_GIA_TINH_THUE_S")
    private Double triGiaTinhThueS;

    @JsonProperty("TRI_GIA_TINH_THUE_M")
    private Double triGiaTinhThueM;

    @JsonProperty("DONGIA_TINHTHUE")
    private Double dongiaTinhthue;

    @JsonProperty("THUESUAT_NHAPKHAU")
    private Double thuesuatNhapkhau;

    @JsonProperty("TIEN_THUE_NHAPKHAU")
    private Double tienThueNhapkhau;

    @JsonProperty("XUATXU")
    private String xuatxu;

    @JsonProperty("MA_VANBANPHAPQUY")
    private String maVanbanphapquy;

    @JsonProperty("PHANLOAI_GIAYPHEP_NK")
    private String phanloaiGiayphepNk;

    @JsonProperty("MA_BIEUTHUE_NK")
    private String maBieuthueNk;

    @JsonProperty("MA_MIENGIAM_THUE")
    private String maMiengiamThue;
}
