package com.example.thuedientu.model;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Entity
@Table(name = "air_master_bill")
@Data
public class AirMasterBillEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "IDCHUYENBAY")
    private String idChuyenBay;

    @Column(name = "FLIGHTDATE")
    private Timestamp flightDate;

    @Column(name = "CARRIER")
    private String carrier;

    @Column(name = "FLIGHTNO")
    private String flightNo;

    @Column(name = "ORIGIN")
    private String origin;

    @Column(name = "DESTINATION")
    private String destination;

    @Column(name = "MAWB_NO")
    private String mawbNo;

    @Column(name = "M_TENNGUOIGUI")
    private String mTenNguoiGui;

    @Column(name = "M_TENNGUOINHAN")
    private String mTenNguoiNhan;

    @Column(name = "M_MOTAHANGHOA")
    private String mMoTaHangHoa;

    @Column(name = "M_SOKIEN")
    private Integer mSoKien;

    @Column(name = "M_GROSSWEIGHT")
    private BigDecimal mGrossWeight;

    @Column(name = "M_SANBAYDI")
    private String mSanBayDi;

    @Column(name = "M_SANBAYDEN")
    private String mSanBayDen;

    @Column(name = "M_VERSION")
    private String mVersion;

    @Column(name = "H_SOMAWB")
    private String hSoMawb;

    @Column(name = "H_SOHWB")
    private String hSoHwb;

    @Column(name = "H_TENNGUOIGUI")
    private String hTenNguoiGui;

    @Column(name = "H_TENNGUOINHAN")
    private String hTenNguoiNhan;

    @Column(name = "H_SOKIEN")
    private Integer hSoKien;

    @Column(name = "H_TRONGLUONG")
    private BigDecimal hTrongLuong;

    @Column(name = "H_NOIDI")
    private String hNoiDi;

    @Column(name = "H_NOIDEN")
    private String hNoiDen;

    @Column(name = "H_MOTAHANGHOA")
    private String hMoTaHangHoa;
}
