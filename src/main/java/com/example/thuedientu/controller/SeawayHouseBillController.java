package com.example.thuedientu.controller;

import com.example.thuedientu.model.HashFile;
import com.example.thuedientu.repository.FileRepository;
import com.example.thuedientu.service.DatabaseService;
import com.example.thuedientu.service.FileUploadService;
import com.example.thuedientu.utilExcel.FileQueueManager;
import com.example.thuedientu.queueService.SeawayHouseQueueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/api/SeawayHouseBill")
@CrossOrigin(origins = "*")
public class SeawayHouseBillController {

    @Autowired
    private FileRepository fileRepository;

    @Autowired
    private SeawayHouseQueueService importQueueService;

    @Autowired
    private FileUploadService fileUploadService;

    @Autowired
    private DatabaseService excelImportService;

    @Autowired
    private FileQueueManager fileQueueManager;

    @PostMapping("/import-SeawayHouseBill")
    public ResponseEntity<?> importExcel(@RequestParam("file") MultipartFile file) {
        fileQueueManager.addPendingFile(file.getOriginalFilename());

        File tempFile = null;
        excelImportService.createTableIfNotExists();

        try {
            // Kiểm tra trùng lặp bằng hash
//            if (fileUploadService.checkForDuplicateByContent(file)) {
//                System.out.println("🔁 Duplicate file detected");
//                return ResponseEntity.badRequest().body(Map.of(
//                        "status", "error",
//                        "message", "Duplicate file detected. Upload canceled."
//                ));
//            }

            // Tạo bản ghi hash mới
            HashFile hashFile = new HashFile();
            hashFile.setFilename(file.getOriginalFilename());
            hashFile.setFileHash(fileUploadService.generateFileHash(file));

            try {
                // Tạo thư mục tạm
                String tempDirPath = "C:\\excel-SeawayHouseBill-temp\\";
                File tempDir = new File(tempDirPath);
                if (!tempDir.exists()) {
                    tempDir.mkdirs();
                }

                // Tạo file tạm an toàn
                String fileName = "import_" + System.currentTimeMillis() + ".xlsx";
                File safeTempFile = new File(tempDir, fileName);
                file.transferTo(safeTempFile);

                System.out.println("📁 File saved at: " + safeTempFile.getAbsolutePath());

                // Đưa vào hàng đợi xử lý
                importQueueService.enqueueFile(safeTempFile, hashFile);
                importQueueService.printQueueStatus();

                System.out.println("✅ Import finished successfully.");
                return ResponseEntity.ok(Map.of(
                        "status", "success",
                        "message", "File Excel đã được nhập thành công."
                ));
            } catch (IOException e) {
                e.printStackTrace();
                return ResponseEntity.status(500).body(Map.of(
                        "status", "error",
                        "message", "Lỗi khi xử lý file: " + e.getMessage()
                ));
            } finally {
                if (tempFile != null && tempFile.exists()) {
                    // tempFile.delete(); // Tùy chọn xóa file tạm
                }
            }
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of(
                    "status", "error",
                    "message", "Error uploading file: " + e.getMessage()
            ));
        }
    }
}
