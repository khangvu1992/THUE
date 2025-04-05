package com.example.thuedientu.controller;

import com.example.thuedientu.service.ExcelImportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/excel1")
@CrossOrigin(origins = "*")
public class ExcelImportController {

    @Autowired
    private ExcelImportService excelImportService;

    // API để tải lên file Excel
    @PostMapping("/import")
    public ResponseEntity<String> importExcel(@RequestParam("file") MultipartFile file) {
        try {
            excelImportService.importExcel(file);  // Gọi dịch vụ để xử lý file
            return ResponseEntity.ok("File Excel đã được nhập thành công.");
        } catch (IOException e) {
            return ResponseEntity.status(500).body("Lỗi khi xử lý file: " + e.getMessage());
        }
    }
}
