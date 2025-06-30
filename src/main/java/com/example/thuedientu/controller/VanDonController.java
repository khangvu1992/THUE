package com.example.thuedientu.controller;

import com.example.thuedientu.model.HashFile;
import com.example.thuedientu.queueService.VanDonQueueService;
import com.example.thuedientu.repository.FileRepository;
import com.example.thuedientu.service.DatabaseService;
import com.example.thuedientu.service.FileUploadService;
import com.example.thuedientu.queueService.FileImportQueueService;
import com.example.thuedientu.service.VanDonService;
import com.example.thuedientu.utilExcel.FileQueueManager;
import com.example.thuedientu.utilExcel.VanDonQueueManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/api/van_don_jdbc")
@CrossOrigin(origins = "*")
public class VanDonController {


    @Autowired
    private FileRepository fileRepository;

    @Autowired
    private VanDonQueueService importQueueService;

    @Autowired
    private FileUploadService fileUploadService;

    @Autowired
    private VanDonService excelImportService;
    @Autowired
    private VanDonQueueManager fileQueueManager;




    @PostMapping("/import")
    public ResponseEntity<?> importExcel(@RequestParam("file") MultipartFile file) {

        System.out.println("fffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff");
        fileQueueManager.addPendingFile(file.getOriginalFilename());

        File tempFile = null;
        excelImportService.createTableIfNotExists();

        try {
//             Kiểm tra xem file có trùng lặp hay không dựa trên hash
            if (fileUploadService.checkForDuplicateByContent(file)) {
                System.out.println("🔁 Duplicate file detected");
                return ResponseEntity.badRequest().body(Map.of(
                        "status", "error",
                        "message", "Duplicate file detected. Upload canceled."
                ));
            }

            // Nếu không trùng lặp, lưu tên file vào cơ sở dữ liệu
            HashFile hashFile = new HashFile();
            hashFile.setFilename(file.getOriginalFilename());
            hashFile.setFileHash(fileUploadService.generateFileHash(file));

            try {
                // 1. Tạo thư mục tạm nếu chưa tồn tại
                String tempDirPath = "C:\\VanDon-import-temp\\";
                File tempDir = new File(tempDirPath);
                if (!tempDir.exists()) {
                    tempDir.mkdirs();
                }





                // 2. Tạo file tạm an toàn
                String fileName = "import_" + System.currentTimeMillis() + ".xlsx";
                File safeTempFile = new File(tempDir, fileName);
                file.transferTo(safeTempFile);

                System.out.println("📁 File saved at: " + safeTempFile.getAbsolutePath());


                importQueueService.enqueueFile(safeTempFile, hashFile);
                importQueueService.printQueueStatus();


                // 3. Import dữ liệu từ file Excel
//                excelImportService.import1Datbase1JDBC1(safeTempFile, hashFile);

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
                    // tempFile.delete(); // Chưa dùng
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
