package com.example.thuedientu.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "khang_hehe")
@Data
public class EnityExcel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "trangthaitk", columnDefinition = "NVARCHAR(255)")
    private String trangthaitk;

    @Column(name = "bpkthsdt", columnDefinition = "NVARCHAR(255)")
    private String bpkthsdt;

    @Column(name = "bptq", columnDefinition = "NVARCHAR(255)")
    private String bptq;

    @Column(name = "ptvc", columnDefinition = "NVARCHAR(255)")
    private String ptvc;

    @Column(name = "malh", columnDefinition = "NVARCHAR(255)")
    private String malh;

    @Column(name = "ngayDk", columnDefinition = "NVARCHAR(255)")
    private String ngayDk;

    @Column(name = "hourDk", columnDefinition = "NVARCHAR(255)")
    private String hourDk;

    @Column(name = "ngayThaydoiDk", columnDefinition = "NVARCHAR(255)")
    private String ngayThaydoiDk;

    @Column(name = "hourThaydoiDk", columnDefinition = "NVARCHAR(255)")
    private String hourThaydoiDk;

    @Column(name = "masothueKbhq", columnDefinition = "NVARCHAR(255)")
    private String masothueKbhq;

    @Column(name = "tenDoanhnghiep", columnDefinition = "NVARCHAR(255)")
    private String tenDoanhnghiep;

    @Column(name = "sodienthoai", columnDefinition = "NVARCHAR(255)")
    private String sodienthoai;

    @Column(name = "tenDoanhnghiepUythac", columnDefinition = "NVARCHAR(255)")
    private String tenDoanhnghiepUythac;

    @Column(name = "tenDoitacnuocngoai", columnDefinition = "NVARCHAR(255)")
    private String tenDoitacnuocngoai;

    @Column(name = "maquocgiaDoitacnuocngoai", columnDefinition = "NVARCHAR(255)")
    private String maquocgiaDoitacnuocngoai;

    @Column(name = "vandon01", columnDefinition = "NVARCHAR(255)")
    private String vandon01;

    @Column(name = "vandon02", columnDefinition = "NVARCHAR(255)")
    private String vandon02;

    @Column(name = "vandon03", columnDefinition = "NVARCHAR(255)")
    private String vandon03;

    @Column(name = "vandon04", columnDefinition = "NVARCHAR(255)")
    private String vandon04;

    @Column(name = "vandon05", columnDefinition = "NVARCHAR(255)")
    private String vandon05;

    @Column(name = "soluongkienhang", columnDefinition = "NVARCHAR(255)")
    private String soluongkienhang;

    @Column(name = "maDvtKienhang", columnDefinition = "NVARCHAR(255)")
    private String maDvtKienhang;

    @Column(name = "grossweight", columnDefinition = "NVARCHAR(255)")
    private String grossweight;

    @Column(name = "maDvtGw", columnDefinition = "NVARCHAR(255)")
    private String maDvtGw;

    @Column(name = "soluongContainer", columnDefinition = "NVARCHAR(255)")
    private String soluongContainer;

    @Column(name = "maDiadiemdohang", columnDefinition = "NVARCHAR(255)")
    private String maDiadiemdohang;

    @Column(name = "maDiadiemxephang", columnDefinition = "NVARCHAR(255)")
    private String maDiadiemxephang;

    @Column(name = "tenPhuongtienvanchuyen", columnDefinition = "NVARCHAR(255)")
    private String tenPhuongtienvanchuyen;

    @Column(name = "ngayHangDen", columnDefinition = "NVARCHAR(255)")
    private String ngayHangDen;

    @Column(name = "phuongThucThanhToan", columnDefinition = "NVARCHAR(255)")
    private String phuongThucThanhToan;

    @Column(name = "tongTriGiaHoaDon", columnDefinition = "NVARCHAR(255)")
    private String tongTriGiaHoaDon;

    @Column(name = "tongTriGiaTinhThue", columnDefinition = "NVARCHAR(255)")
    private String tongTriGiaTinhThue;

    @Column(name = "tongTienThue", columnDefinition = "NVARCHAR(255)")
    private String tongTienThue;

    @Column(name = "tongSoDonghang", columnDefinition = "NVARCHAR(255)")
    private String tongSoDonghang;

    @Column(name = "ngayCapPhep", columnDefinition = "NVARCHAR(255)")
    private String ngayCapPhep;

    @Column(name = "gioCapPhep", columnDefinition = "NVARCHAR(255)")
    private String gioCapPhep;

    @Column(name = "ngayHoanthanhKiemtra", columnDefinition = "NVARCHAR(255)")
    private String ngayHoanthanhKiemtra;

    @Column(name = "gioHoanthanhKiemtra", columnDefinition = "NVARCHAR(255)")
    private String gioHoanthanhKiemtra;

    @Column(name = "ngayHuyTk", columnDefinition = "NVARCHAR(255)")
    private String ngayHuyTk;

    @Column(name = "gioHuyTk", columnDefinition = "NVARCHAR(255)")
    private String gioHuyTk;

    @Column(name = "tenNguoiphutrachKiemtrahoso", columnDefinition = "NVARCHAR(255)")
    private String tenNguoiphutrachKiemtrahoso;

    @Column(name = "tenNguoiphutrachKiemhoa", columnDefinition = "NVARCHAR(255)")
    private String tenNguoiphutrachKiemhoa;

    @Column(name = "hsCode", columnDefinition = "NVARCHAR(255)")
    private String hsCode;

    @Column(name = "moTaHangHoa", columnDefinition = "NVARCHAR(255)")
    private String moTaHangHoa;

    @Column(name = "soLuongHanghoa", columnDefinition = "NVARCHAR(255)")
    private String soLuongHanghoa;

    @Column(name = "maDvtHanghoa", columnDefinition = "NVARCHAR(255)")
    private String maDvtHanghoa;

    @Column(name = "triGiaHoaDon", columnDefinition = "NVARCHAR(255)")
    private String triGiaHoaDon;

    @Column(name = "dongiaHoadon", columnDefinition = "NVARCHAR(255)")
    private String dongiaHoadon;

    @Column(name = "maTienteHoadon", columnDefinition = "NVARCHAR(255)")
    private String maTienteHoadon;

    @Column(name = "donviDongiaTiente", columnDefinition = "NVARCHAR(255)")
    private String donviDongiaTiente;

    @Column(name = "triGiaTinhThueS", columnDefinition = "NVARCHAR(255)")
    private String triGiaTinhThueS;

    @Column(name = "triGiaTinhThueM", columnDefinition = "NVARCHAR(255)")
    private String triGiaTinhThueM;

    @Column(name = "dongiaTinhthue", columnDefinition = "NVARCHAR(255)")
    private String dongiaTinhthue;

    @Column(name = "thuesuatNhapkhau", columnDefinition = "NVARCHAR(255)")
    private String thuesuatNhapkhau;

    @Column(name = "tienThueNhapkhau", columnDefinition = "NVARCHAR(255)")
    private String tienThueNhapkhau;

    @Column(name = "xuatxu", columnDefinition = "NVARCHAR(255)")
    private String xuatxu;

    @Column(name = "maVanbanphapquy", columnDefinition = "NVARCHAR(255)")
    private String maVanbanphapquy;

    @Column(name = "phanloaiGiayphepNk", columnDefinition = "NVARCHAR(255)")
    private String phanloaiGiayphepNk;

    @Column(name = "maBieuthueNk", columnDefinition = "NVARCHAR(255)")
    private String maBieuthueNk;

    @Column(name = "maMiengiamThue", columnDefinition = "NVARCHAR(255)")
    private String maMiengiamThue;

    @Column(name = "tkid", columnDefinition = "NVARCHAR(255)")
    private String tkid;

    @Column(name = "sotk", columnDefinition = "NVARCHAR(255)")
    private String sotk;

    @Column(name = "mahq", columnDefinition = "NVARCHAR(255)")
    private String mahq;

}
