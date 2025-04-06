package com.example.thuedientu.controller;

import com.example.thuedientu.service.ExcellmportService;
import com.example.thuedientu.util.ImportProgressTracker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

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
            // Tạo file tạm lưu trên ổ đĩa
            tempFile = File.createTempFile("excel_", ".xlsx");

            // Lấy tổng số byte của file để tính toán tiến trình
            long totalBytes = file.getSize();
            long bytesRead = 0;

            // Đọc dữ liệu từ file và theo dõi tiến độ
            try (InputStream inputStream = file.getInputStream();
                 OutputStream outputStream = new FileOutputStream(tempFile)) {

                byte[] buffer = new byte[1024]; // Bộ đệm để đọc tệp
                int length;

                while ((length = inputStream.read(buffer)) != -1) {
                    // Ghi từng phần vào tệp tạm thời
                    outputStream.write(buffer, 0, length);
                    bytesRead += length;

                    // Tính toán phần trăm tiến trình

                    if(bytesRead % 10000==0){
                        double progress = (double) bytesRead / totalBytes * 100;
                        System.out.println("Progress: " + progress + "%");

                    }


                }
            }

            file.transferTo(tempFile);

            System.out.println(tempFile.length());

            // Gọi service để xử lý file (bất đồng bộ)
            excelImportService.importAsync(tempFile);

            // Trả về phản hồi thành công
            return ResponseEntity.accepted().body("File import thành công!");

        } catch (IOException e) {
            // Xử lý ngoại lệ khi xảy ra lỗi trong quá trình đọc hoặc ghi file
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Đã xảy ra lỗi trong quá trình import file.");

        } finally {
            // Xoá file tạm nếu nó tồn tại
            if (tempFile != null && tempFile.exists()) {
                boolean deleted = tempFile.delete();
                if (!deleted) {
                    System.out.println("Không thể xoá tệp tạm.");
                }
            }
        }
    }}


