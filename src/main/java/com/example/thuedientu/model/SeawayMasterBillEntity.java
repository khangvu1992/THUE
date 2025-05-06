package com.example.thuedientu.model;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;

@Entity
@Table(name = "seaway_master_bill")
@Data
public class SeawayMasterBillEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "SoKhaiBao")
    private String soKhaiBao;

    @Column(name = "SoHoSo")
    private String soHoSo;

    @Column(name = "LoaiHoSo")
    private String loaiHoSo;

    @Column(name = "ArrivalDate")
    private java.sql.Timestamp arrivalDate;

    @Column(name = "CangTiepNhan")
    private String cangTiepNhan;

    @Column(name = "NgayGui")
    private java.sql.Timestamp ngayGui;

    @Column(name = "TenTau")
    private String tenTau;

    @Column(name = "SoIMO")
    private String soIMO;

    @Column(name = "HangTau")
    private String hangTau;

    @Column(name = "NgayTauDenRoi")
    private java.sql.Timestamp ngayTauDenRoi;

    @Column(name = "NgayDenRoi")
    private java.sql.Timestamp ngayDenRoi;

    @Column(name = "CangRoiCuoiCungCangDich")
    private String cangRoiCuoiCungCangDich;

    @Column(name = "Consignee")
    private String consignee;

    @Column(name = "Consigner")
    private String consigner;

    @Column(name = "NotificatedParty")
    private String notificatedParty;

    @Column(name = "NotificatedParty2")
    private String notificatedParty2;

    @Column(name = "MasterBillNo")
    private String masterBillNo;

    @Column(name = "ContNumber")
    private String contNumber;

    @Column(name = "ContSealNumber")
    private String contSealNumber;

    @Column(name = "GoodDescription")
    private String goodDescription;

    @Column(name = "CangXepHangGoc")
    private String cangXepHangGoc;

    @Column(name = "CangXepHang")
    private String cangXepHang;

    @Column(name = "CangDoHang")
    private String cangDoHang;

    @Column(name = "CangDich")
    private String cangDich;

    @Column(name = "TenCangDich")
    private String tenCangDich;

    @Column(name = "DiaDiemDoHang")
    private String diaDiemDoHang;

    @Column(name = "NetWeight")
    private BigDecimal netWeight;

    @Column(name = "GrossWeight")
    private BigDecimal grossWeight;

    @Column(name = "Demension")
    private BigDecimal demension;
}
