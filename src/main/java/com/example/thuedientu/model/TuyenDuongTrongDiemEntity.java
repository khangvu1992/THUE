package com.example.thuedientu.model;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "tuyen_duong_trong_diem")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TuyenDuongTrongDiemEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String cuaKhauDi;
    private String cuaKhauDen;
    private String moTa;
}
