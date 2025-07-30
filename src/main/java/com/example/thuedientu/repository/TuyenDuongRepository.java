package com.example.thuedientu.repository;


import com.example.thuedientu.model.TuyenDuongTrongDiemEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TuyenDuongRepository extends JpaRepository<TuyenDuongTrongDiemEntity, Long> {
    Optional<TuyenDuongTrongDiemEntity> findByCuaKhauDiAndCuaKhauDen(String cuaKhauDi, String cuaKhauDen);
    void deleteByCuaKhauDiAndCuaKhauDen(String cuaKhauDi, String cuaKhauDen);
}
