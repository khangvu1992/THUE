package com.example.thuedientu.controller;

import com.example.thuedientu.model.HashFile;
import com.example.thuedientu.repository.FileRepository;
import com.example.thuedientu.service.DatabaseServiceNew;
import com.example.thuedientu.service.FileUploadService;
import com.example.thuedientu.util.DownloadManager;
import com.example.thuedientu.util.ImportTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@RestController
@RequestMapping("/api/excel1_jdbc_new")
@CrossOrigin(origins = "*")
public class DatabaseControllerNew {

    private static final String TEMP_DIR_PATH = "D:\\excel-import-temp\\";

    @Autowired
    private FileRepository fileRepository;


    @Autowired
    private DatabaseServiceNew databaseServiceNew;

    @Autowired
    private FileUploadService fileUploadService;

    @Autowired
    private DownloadManager downloadManager; // ✅ sử dụng hàng đợi

    @PostMapping("/import")
    public ResponseEntity<String> importExcel(@RequestParam("file") MultipartFile file) {
        try {
            // 1. Tính hash của file
            String fileHash = fileUploadService.generateFileHash(file);



            // 2. Kiểm tra file đã upload chưa
            if (fileUploadService.checkForDuplicateByContent(file)) {
                return ResponseEntity.badRequest().body("❌ File đã tồn tại trước đó.");
            }

            // 3. Tạo thư mục tạm nếu chưa có
            File tempDir = new File(TEMP_DIR_PATH);
            if (!tempDir.exists()) tempDir.mkdirs();

            // 4. Tạo file mới và lưu dữ liệu từ MultipartFile
            String fileName = "import_" + System.currentTimeMillis() + ".xlsx";
            File savedFile = new File(tempDir, fileName);
            file.transferTo(savedFile);

            System.out.println("📁 File saved at: " + savedFile.getAbsolutePath());

            // 5. Tạo đối tượng HashFile
            HashFile hashFile = new HashFile();
            hashFile.setFilename(file.getOriginalFilename());
            hashFile.setFileHash(fileHash);

            // 6. Đưa vào hàng đợi xử lý nền
            ImportTask importTask = new ImportTask(savedFile, hashFile, databaseServiceNew);
            String fileId = hashFile.getFileHash();
            downloadManager.enqueue(fileId, importTask);


            return ResponseEntity.ok("✅ File đã được đưa vào hàng đợi xử lý.");
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Lỗi khi lưu file: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Lỗi hệ thống: " + e.getMessage());
        }
    }
}
