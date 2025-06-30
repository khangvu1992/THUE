package com.example.thuedientu.dto;

import lombok.Data;
import java.util.Date;

@Data
public class BillDTO {
    private String vandon01;
    private String sotk;
    private String moTaHangHoaTokhai;
    private String tkid;
    private Date ngayDk;

    private String masterBillNo;
    private String contNumber;
    private String goodDescription;
    private int houseId;

    private String billNumber;
    private Date arrivalDate;
    private String moTaHangHoaMaster;
    private int masterId;


}
