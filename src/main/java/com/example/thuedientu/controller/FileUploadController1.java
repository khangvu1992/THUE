package com.example.thuedientu.controller;


import com.example.thuedientu.model.HashFile;
import com.example.thuedientu.service.FileUploadService;
import com.example.thuedientu.util.FileImportQueueService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

@RestController
@RequestMapping("/files")
@RequiredArgsConstructor
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
            String tempDirPath = "C:\\excel-import-temp\\";
            File tempDir = new File(tempDirPath);
            if (!tempDir.exists()) {
                tempDir.mkdirs();
            }


            // 2. Lưu file tạm
            String filename = multipartFile.getOriginalFilename();
            File tempFile = new File(tempDirPath, filename);
            multipartFile.transferTo(tempFile);

            //them hash file

            HashFile hashFile = new HashFile();
            hashFile.setFilename(multipartFile.getOriginalFilename());
            hashFile.setFileHash(fileUploadService.generateFileHash(multipartFile));


            // 3. Ghi vào hàng đợi
            importQueueService.enqueueFile(tempFile);

            return ResponseEntity.ok("Đã tải và xếp hàng: " + filename);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Lỗi khi lưu file: " + e.getMessage());
        }
    }
}
