package com.example.thuedientu.repository;

import com.example.thuedientu.model.EnityExcel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface EnityExcelRepository extends JpaRepository<EnityExcel, Long>, JpaSpecificationExecutor<EnityExcel> {
    // Đã hỗ trợ tìm kiếm động bằng Specification
}
