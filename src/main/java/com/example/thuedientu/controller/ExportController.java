package com.example.thuedientu.controller;

import com.example.thuedientu.service.ExportImportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;


@RestController
@RequestMapping("/api/export")
@CrossOrigin(origins = "*")
public class ExportController {


    @Autowired
    ExportImportService exportImportService;

    @PostMapping("/import-export")
    public ResponseEntity<?> importExportData(@RequestParam("file") MultipartFile file) {
        try {
            File temp = File.createTempFile("import-", ".xlsx");
            file.transferTo(temp);

            exportImportService.importExcelFile(temp);
            return ResponseEntity.ok("Import thành công!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Import thất bại: " + e.getMessage());
        }
    }
}
