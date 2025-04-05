package com.example.thuedientu.repository;

import com.example.thuedientu.model.EnityExcel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import java.util.List;

@Repository
public interface ExcelRepository extends JpaRepository<EnityExcel, Long>, JpaSpecificationExecutor<EnityExcel> {}

