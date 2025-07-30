package com.example.thuedientu.model;

import jakarta.persistence.*;

@Entity
@Table(name = "doanh_nghiep_trong_diem")
public class DoanhNghiepTrongDiemEntity {

    @Id
    @Column(name = "ma_so_thue", nullable = false)
    private String maSoThue;

    @Column(name = "ten_cong_ty")
    private String tenCongTy;

    @Column(name = "mo_ta")
    private String moTa;

    @Column(name = "is_trong_diem")
    private boolean isTrongDiem;

    // --- Constructors ---
    public DoanhNghiepTrongDiemEntity() {
    }

    public DoanhNghiepTrongDiemEntity(String maSoThue, String tenCongTy, String moTa, boolean isTrongDiem) {
        this.maSoThue = maSoThue;
        this.tenCongTy = tenCongTy;
        this.moTa = moTa;
        this.isTrongDiem = isTrongDiem;
    }

    // --- Getters and Setters ---
    public String getMaSoThue() {
        return maSoThue;
    }

    public void setMaSoThue(String maSoThue) {
        this.maSoThue = maSoThue;
    }

    public String getTenCongTy() {
        return tenCongTy;
    }

    public void setTenCongTy(String tenCongTy) {
        this.tenCongTy = tenCongTy;
    }

    public String getMoTa() {
        return moTa;
    }

    public void setMoTa(String moTa) {
        this.moTa = moTa;
    }

    public boolean getIsTrongDiem() {
        return isTrongDiem;
    }

    public void setIsTrongDiem(boolean isTrongDiem) {
        this.isTrongDiem = isTrongDiem;
    }

    // --- Optional: toString, equals, hashCode ---
    @Override
    public String toString() {
        return "DoanhNghiepTrongDiemEntity{" +
                "maSoThue='" + maSoThue + '\'' +
                ", tenCongTy='" + tenCongTy + '\'' +
                ", moTa='" + moTa + '\'' +
                ", isTrongDiem=" + isTrongDiem +
                '}';
    }
}
