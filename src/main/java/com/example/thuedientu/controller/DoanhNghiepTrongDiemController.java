package com.example.thuedientu.controller;

import com.example.thuedientu.model.DoanhNghiepTrongDiemEntity;
import com.example.thuedientu.service.DoanhNghiepTrongDiemService;
import jakarta.annotation.PostConstruct;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/doanhnghiep/trongdiem")
@CrossOrigin(origins = "*")
public class DoanhNghiepTrongDiemController {

    private final DoanhNghiepTrongDiemService service;
    private final JdbcTemplate jdbcTemplate;

    public DoanhNghiepTrongDiemController(DoanhNghiepTrongDiemService service, JdbcTemplate jdbcTemplate) {
        this.service = service;
        this.jdbcTemplate = jdbcTemplate;
    }

    // ✅ Tạo bảng nếu chưa có khi khởi động controller
    @PostConstruct
    public void init() {
        String sql = "IF OBJECT_ID('dbo.doanh_nghiep_trong_diem', 'U') IS NULL " +
                "BEGIN " +
                "CREATE TABLE dbo.doanh_nghiep_trong_diem (" +
                "ma_so_thue NVARCHAR(50) PRIMARY KEY, " +
                "ten_cong_ty NVARCHAR(255), " +
                "mo_ta NVARCHAR(500), " +
                "is_trong_diem BIT" +
                ") " +
                "END";
        jdbcTemplate.execute(sql);
    }

    // ✅ Import từ Excel hoặc nhập tay (nhiều hoặc 1 record)
    @PostMapping("/import")
    public ResponseEntity<List<DoanhNghiepTrongDiemEntity>> importDoanhNghiep(@RequestBody List<DoanhNghiepTrongDiemEntity> list) {
        List<DoanhNghiepTrongDiemEntity> savedList = service.saveAll(list);
        return ResponseEntity.ok(savedList);
    }

    // ✅ Lấy toàn bộ danh sách
    @GetMapping
    public List<DoanhNghiepTrongDiemEntity> getAll() {
        return service.getAll();
    }

    // ✅ Xóa theo mã số thuế
    @DeleteMapping("/{maSoThue}")
    public ResponseEntity<Void> delete(@PathVariable String maSoThue) {
        service.deleteById(maSoThue);
        return ResponseEntity.noContent().build();
    }
}
