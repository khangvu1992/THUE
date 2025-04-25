package com.example.thuedientu.controller;


import com.example.thuedientu.dto.TableDefinition;
import com.example.thuedientu.service.ExcelImportService1;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@RestController
@RequestMapping("/api/excel")
@CrossOrigin(origins = "*")
public class DynamicControler {

    private final ExcelImportService1 excelImportService;

    @Autowired
    public DynamicControler(ExcelImportService1 excelImportService) {
        this.excelImportService = excelImportService;
    }

    @PostMapping("/import-dynamic3333")
    public ResponseEntity<?> importDynamicExcel(
            @RequestParam("file") MultipartFile file,
            @RequestParam("tableInfo") String tableInfoJson
    ) {
        try {
            // Parse thông tin cấu trúc bảng từ JSON
            ObjectMapper mapper = new ObjectMapper();
            TableDefinition tableDef = mapper.readValue(tableInfoJson, TableDefinition.class);


            String tempDirPath = "C:\\excel-import-tempxxx\\";
            File tempDir = new File(tempDirPath);
            if (!tempDir.exists()) {
                tempDir.mkdirs();
            }





            // 2. Tạo file tạm an toàn
            String fileName = "import_" + System.currentTimeMillis() + ".xlsx";
            File safeTempFile = new File(tempDir, fileName);
            file.transferTo(safeTempFile);

//            // Lưu file Excel vào file tạm
//            File tempFile = File.createTempFile("upload_", ".xlsx");
//            file.transferTo(tempFile);

            // Gọi service xử lý import
            excelImportService.importToDynamicTable(safeTempFile, tableDef);

            return ResponseEntity.ok("Import started.");
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to process file: " + e.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Import failed: " + ex.getMessage());
        }
    }
}