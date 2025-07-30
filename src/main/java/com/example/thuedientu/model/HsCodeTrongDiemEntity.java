package com.example.thuedientu.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "hs_code_trong_diem")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HsCodeTrongDiemEntity {

    @Id
    @Column(name = "ma_hs", nullable = false)
    private String maHs;

    @Column(name = "mo_ta")
    private String moTa;
}
