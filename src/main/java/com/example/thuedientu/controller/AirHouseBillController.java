package com.example.thuedientu.controller;

import com.example.thuedientu.model.HashFile;
import com.example.thuedientu.repository.FileRepository;
import com.example.thuedientu.service.DatabaseService;
import com.example.thuedientu.service.FileUploadService;
import com.example.thuedientu.util.FileQueueManager;
import com.example.thuedientu.util.AirHouseQueueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/api/AirHouseBill")
@CrossOrigin(origins = "*")
public class AirHouseBillController {

    @Autowired
    private FileRepository fileRepository;

    @Autowired
    private AirHouseQueueService importQueueService;

    @Autowired
    private FileUploadService fileUploadService;

    @Autowired
    private DatabaseService excelImportService;

    @Autowired
    private FileQueueManager fileQueueManager;

    @PostMapping("/import-AirHouseBill")
    public ResponseEntity<?> importExcel(@RequestParam("file") MultipartFile file) {
        fileQueueManager.addPendingFile(file.getOriginalFilename());

        File tempFile = null;
        excelImportService.createTableIfNotExists(); // đảm bảo bảng tồn tại trước

        try {
            // Kiểm tra trùng lặp file bằng hash
            if (fileUploadService.checkForDuplicateByContent(file)) {
                System.out.println("🔁 Duplicate file detected");
                return ResponseEntity.badRequest().body(Map.of(
                        "status", "error",
                        "message", "Tệp tin đã tồn tại. Hủy tải lên."
                ));
            }

            // Tạo đối tượng HashFile
            HashFile hashFile = new HashFile();
            hashFile.setFilename(file.getOriginalFilename());
            hashFile.setFileHash(fileUploadService.generateFileHash(file));

            try {
                // Tạo thư mục tạm nếu chưa có
                String tempDirPath = "C:\\excel-AirHouseBill-temp\\";
                File tempDir = new File(tempDirPath);
                if (!tempDir.exists()) {
                    tempDir.mkdirs();
                }

                // Lưu file tạm thời
                String fileName = "air_house_" + System.currentTimeMillis() + ".xlsx";
                File safeTempFile = new File(tempDir, fileName);
                file.transferTo(safeTempFile);

                System.out.println("📁 File saved at: " + safeTempFile.getAbsolutePath());

                // Đưa vào hàng đợi
                importQueueService.enqueueFile(safeTempFile, hashFile);
                importQueueService.printQueueStatus();

                System.out.println("✅ Air House Bill import task enqueued successfully.");
                return ResponseEntity.ok(Map.of(
                        "status", "success",
                        "message", "Tệp Excel đã được thêm vào hàng đợi xử lý thành công."
                ));
            } catch (IOException e) {
                e.printStackTrace();
                return ResponseEntity.status(500).body(Map.of(
                        "status", "error",
                        "message", "Lỗi khi xử lý file: " + e.getMessage()
                ));
            } finally {
                if (tempFile != null && tempFile.exists()) {
                    // Optional: tempFile.delete();
                }
            }
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of(
                    "status", "error",
                    "message", "Lỗi trong quá trình tải lên: " + e.getMessage()
            ));
        }
    }
}
