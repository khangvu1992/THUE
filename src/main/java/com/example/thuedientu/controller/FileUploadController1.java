package com.example.thuedientu.controller;


import com.example.thuedientu.model.HashFile;
import com.example.thuedientu.service.FileUploadService;
import com.example.thuedientu.util.FileImportQueueService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@RestController
@RequestMapping("/files")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class FileUploadController1 {

    private final FileImportQueueService importQueueService;
    @Autowired
    private FileUploadService fileUploadService;



    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile multipartFile) {
        if (multipartFile.isEmpty()) {
            return ResponseEntity.badRequest().body("File is empty");
        }

        try {
            // 1. Tạo thư mục nếu chưa có
            String tempDirPath = "C:\\excel-import-temp2\\";
            File tempDir = new File(tempDirPath);
            if (!tempDir.exists()) {
                tempDir.mkdirs();
            }

            // 2. Lưu file tạm
            String filename = multipartFile.getOriginalFilename();
            if (filename == null || filename.isEmpty()) {
                return ResponseEntity.badRequest().body("Invalid file name");
            }
            File tempFile = new File(tempDirPath, filename);

            // Kiểm tra nếu file đã tồn tại (có thể thay đổi logic này tùy nhu cầu)
            if (tempFile.exists()) {
                return ResponseEntity.badRequest().body("File already exists");
            }

            // Lưu file vào thư mục tạm
            multipartFile.transferTo(tempFile);

            // Tạo hash cho file
            HashFile hashFile = new HashFile();
            hashFile.setFilename(filename);
            hashFile.setFileHash(fileUploadService.generateFileHash(multipartFile));

            // 3. Ghi vào hàng đợi
            importQueueService.enqueueFile(tempFile, hashFile);

            return ResponseEntity.ok("Đã tải và xếp hàng: " + filename);
        } catch (IOException e) {
            // Xử lý lỗi IO (ví dụ khi không thể lưu file)
            e.printStackTrace();
            return ResponseEntity.status(500).body("Lỗi khi lưu file: " + e.getMessage());
        } catch (Exception e) {
            // Xử lý lỗi chung
            e.printStackTrace();
            return ResponseEntity.status(500).body("Đã xảy ra lỗi không xác định: " + e.getMessage());
        }
    }

}
