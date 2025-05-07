package com.example.thuedientu.model;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Entity
@Table(name = "seaway_house_bill")
@Data
public class SeawayHouseBillEntity {

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
    private Timestamp arrivalDate;

    @Column(name = "CangTiepNhan")
    private String cangTiepNhan;

    @Column(name = "NgayGui")
    private Timestamp ngayGui;

    @Column(name = "TenTau")
    private String tenTau;

    @Column(name = "SoIMO")
    private String soIMO;

    @Column(name = "HangTau")
    private String hangTau;

    @Column(name = "NgayTauDenRoi")
    private Timestamp ngayTauDenRoi;

    @Column(name = "NgayDenRoi")
    private Timestamp ngayDenRoi;

    @Column(name = "CangRoiCuoiCungCangDich")
    private String cangRoiCuoiCungCangDich;

    @Column(name = "BillNumber")
    private String billNumber;

    @Column(name = "HB_Consigner")
    private String hbConsigner;

    @Column(name = "HB_Consignee")
    private String hbConsignee;

    @Column(name = "HB_NotificatedParty")
    private String hbNotificatedParty;

    @Column(name = "HB_NotificatedParty2")
    private String hbNotificatedParty2;

    @Column(name = "DateOfBill")
    private Timestamp dateOfBill;

    @Column(name = "DepartureDate")
    private Timestamp departureDate;

    @Column(name = "PortNameOfTranship")
    private String portNameOfTranship;

    @Column(name = "PortNameOfDestination")
    private String portNameOfDestination;

    @Column(name = "PortNameOfLoad")
    private String portNameOfLoad;

    @Column(name = "PortNameOfUnload")
    private String portNameOfUnload;

    @Column(name = "PlaceOfDelivery")
    private String placeOfDelivery;

    @Column(name = "MoTaHangHoa")
    private String moTaHangHoa;

    @Column(name = "ContNumber")
    private String contNumber;

    @Column(name = "ContSealNumber")
    private String contSealNumber;

    @Column(name = "NumberOfPackage")
    private Integer numberOfPackage;

    @Column(name = "CargoType")
    private String cargoType;
}