package com.example.thuedientu.controller;

import com.example.thuedientu.model.HashFile;
import com.example.thuedientu.repository.FileRepository;
import com.example.thuedientu.service.DatabaseService;
import com.example.thuedientu.service.ExcellmportService;
import com.example.thuedientu.service.FileUploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.cache.CacheManager;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.io.IOException;

@RestController
@RequestMapping("/api/excel1_jdbc")
@CrossOrigin(origins = "*")
public class DatabaseController {


    @Autowired
    private FileRepository fileRepository;

    @Autowired
    private FileUploadService fileUploadService;

    @Autowired
    private DatabaseService excelImportService;

    // API để tải lên file Excel
    @PostMapping("/import")
    public ResponseEntity<String> importExcel(@RequestParam("file") MultipartFile file) {
        File tempFile = null;

        try {
            // Kiểm tra xem file có trùng lặp hay không dựa trên hash
//            if (fileUploadService.checkForDuplicateByContent(file)) {
//                return ResponseEntity.badRequest().body("Duplicate file detected. Upload canceled.");  // Nếu trùng lặp, không tải lên
//            }

            // Nếu không trùng lặp, lưu tên file vào cơ sở dữ liệu
            HashFile hashFile = new HashFile();
            System.out.println("dfffffffffffffffffff");
            hashFile.setFilename(file.getOriginalFilename());
            hashFile.setFileHash(fileUploadService.generateFileHash(file));  // Set the hash

            System.out.println("den day la luu");



            try {
                // Tạo file tạm để xử lý
                tempFile = File.createTempFile("myTemp", ".xlsx"); // or .txt depending on your use
                System.out.println("Temp file created at: " + tempFile.getAbsolutePath());
                file.transferTo(tempFile);  // Chuyển dữ liệu từ MultipartFile vào temp file

                // Gọi service để import dữ liệu từ file Excel
                excelImportService.import1Datbase1JDBC1(tempFile);
// Now accessible here
                System.out.println("den day la eeeeeeeeeeeeeeeeeeeeeluu");

//                fileRepository.save(hashFile);


                return ResponseEntity.ok("File Excel đã được nhjjjjjjjjjjjhập thành công.");
            } catch (IOException e) {
                e.printStackTrace();
                return ResponseEntity.status(500).body("Lỗi khi xử lý file: " + e.getMessage());
            } finally {

                if (tempFile != null && tempFile.exists()) {
                    tempFile.delete(); // Xóa file tạm sau khi xử lý
                }
            }

        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error uploading file: " + e.getMessage());  // Trả về lỗi nếu có ngoại lệ
        }
    }


}
