package com.example.thuedientu.controller;

import com.example.thuedientu.model.HsCodeTrongDiemEntity;
import com.example.thuedientu.service.HsCodeTrongDiemService;
import jakarta.annotation.PostConstruct;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/hs-code-trongdiem")
@CrossOrigin(origins = "*")
public class HsCodeTrongDiemController {

    private final HsCodeTrongDiemService service;
    private final JdbcTemplate jdbcTemplate;

    public HsCodeTrongDiemController(HsCodeTrongDiemService service, JdbcTemplate jdbcTemplate) {
        this.service = service;
        this.jdbcTemplate = jdbcTemplate;
    }

    // ✅ Tạo bảng hs_code_trong_diem nếu chưa tồn tại
    @PostConstruct
    public void init() {
        String sql = "IF OBJECT_ID('dbo.hs_code_trong_diem', 'U') IS NULL " +
                "BEGIN " +
                "CREATE TABLE dbo.hs_code_trong_diem (" +
                "ma_hs NVARCHAR(50) PRIMARY KEY, " +
                "mo_ta NVARCHAR(500)" +
                ") " +
                "END";
        jdbcTemplate.execute(sql);
    }

    // ✅ Lấy danh sách HS code
    @GetMapping
    public List<HsCodeTrongDiemEntity> getAll() {
        return service.findAll();
    }

    // ✅ Thêm mới hoặc ghi đè nhiều mã HS
    @PostMapping("/import")
    public List<HsCodeTrongDiemEntity> importHsCodes(@RequestBody List<HsCodeTrongDiemEntity> hsCodes) {
        return service.saveAll(hsCodes);
    }

    // ✅ Thêm hoặc cập nhật 1 mã HS
    @PostMapping
    public HsCodeTrongDiemEntity createOrUpdate(@RequestBody HsCodeTrongDiemEntity hsCode) {
        return service.save(hsCode);
    }

    // ✅ Xoá mã HS theo ID
    @DeleteMapping("/{maHs}")
    public ResponseEntity<Void> delete(@PathVariable String maHs) {
        if (service.findById(maHs).isPresent()) {
            service.deleteById(maHs);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
