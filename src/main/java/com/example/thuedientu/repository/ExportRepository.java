package com.example.thuedientu.repository;

import com.example.thuedientu.model.ExportEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExportRepository extends JpaRepository<ExportEntity, Long> {
    // Bạn có thể thêm các phương thức tìm kiếm tùy chỉnh nếu cần
}
