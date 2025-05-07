package com.example.thuedientu.model;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Entity
@Table(name = "Air_house_bill")
@Data
public class AirHouseBillEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "ID_CHUYENBAY")
    private String idChuyenBay;

    @Column(name = "SOHIEU")
    private String soHieu;

    @Column(name = "FLIGHTDATE")
    private Timestamp flightDate;

    @Column(name = "CARRIERCODE")
    private String carrierCode;

    @Column(name = "CARRIERNAME")
    private String carrierName;

    @Column(name = "MANOIDI")
    private String maNoiDi;

    @Column(name = "TENNOIDI")
    private String tenNoiDi;

    @Column(name = "MANOIDEN")
    private String maNoiDen;

    @Column(name = "TENNOIDEN")
    private String tenNoiDen;

    @Column(name = "FI_MAQUACANH")
    private String fiMaQuaCanh;

    @Column(name = "IDHANGHOA")
    private String idHangHoa;

    @Column(name = "ID_HHCT")
    private String idHangHoaChiTiet;

    @Column(name = "FI_SOAWB")
    private String fiSoAwb;

    @Column(name = "FI_SOKIEN")
    private Integer fiSoKien;

    @Column(name = "FI_MOTA")
    private String fiMoTa;

    @Column(name = "FI_SHC")
    private String fiShc;

    @Column(name = "TRONGLUONG")
    private BigDecimal trongLuong;

    @Column(name = "NOIDIHH")
    private String noiDiHangHoa;

    @Column(name = "NOIDENHH")
    private String noiDenHangHoa;

    @Column(name = "FI_LOAIHANG")
    private String fiLoaiHang;
}
