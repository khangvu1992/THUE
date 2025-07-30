package com.example.thuedientu.controller;

import com.example.thuedientu.model.TuyenDuongTrongDiemEntity;
import com.example.thuedientu.service.TuyenDuongService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import jakarta.annotation.PostConstruct;
import java.util.List;

@RestController
@RequestMapping("/api/tuyenduong/trongdiem")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class TuyenDuongController {

    private final TuyenDuongService service;
    private final JdbcTemplate jdbcTemplate;

    // ✅ Tạo bảng nếu chưa tồn tại
    @PostConstruct
    public void init() {
        String sql = "IF OBJECT_ID('dbo.tuyen_duong_trong_diem', 'U') IS NULL " +
                "BEGIN " +
                "CREATE TABLE dbo.tuyen_duong_trong_diem (" +
                "id BIGINT IDENTITY PRIMARY KEY, " +
                "cua_khau_di NVARCHAR(255), " +
                "cua_khau_den NVARCHAR(255), " +
                "mo_ta NVARCHAR(MAX)" +
                ") " +
                "END";
        jdbcTemplate.execute(sql);
    }

    @GetMapping
    public ResponseEntity<List<TuyenDuongTrongDiemEntity>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @PostMapping("/import")
    public ResponseEntity<String> importData(@RequestBody List<TuyenDuongTrongDiemEntity> list) {
        service.importList(list);
        return ResponseEntity.ok("Imported");
    }

    @DeleteMapping
    public ResponseEntity<String> delete(@RequestBody TuyenDuongTrongDiemEntity td) {
        service.delete(td);
        return ResponseEntity.ok("Deleted");
    }
}
