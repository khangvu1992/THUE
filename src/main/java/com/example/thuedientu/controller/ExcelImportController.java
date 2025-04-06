package com.example.thuedientu.controller;

import com.example.thuedientu.service.ExcellmportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@RestController
@RequestMapping("/api/excel1")
@CrossOrigin(origins = "*")
public class ExcelImportController {

    @Autowired
    private ExcellmportService excelImportService;

    // API để tải lên file Excel
    @PostMapping("/import")
    public ResponseEntity<String> importExcel(@RequestParam("file") MultipartFile file) {
        File tempFile = null;

        try {
            tempFile = File.createTempFile("myTemp", ".xlsx"); // or .txt depending on your use
            System.out.println("Temp file created at: " + tempFile.getAbsolutePath());
            file.transferTo(tempFile);

            excelImportService.importAsync(tempFile);  // Now accessible here
            return ResponseEntity.ok("File Excel đã được nhập thành công.");
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Lỗi khi xử lý file: " + e.getMessage());
        } finally {
            if (tempFile != null && tempFile.exists()) {
                tempFile.delete(); // Clean up the temporary file
            }
        }
    }
}
