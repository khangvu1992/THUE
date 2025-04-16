package com.example.thuedientu.util;

import com.example.thuedientu.model.HashFile;
import com.example.thuedientu.service.DatabaseService;
import com.example.thuedientu.service.FileUploadService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;






@Service
public class FileImportQueueService {


    @Autowired
    private DatabaseService excelImportService1;


    @Autowired
    private FileUploadService fileUploadService;

    private final BlockingQueue<File> importQueue = new LinkedBlockingQueue<>();
    private final ExecutorService executor = Executors.newSingleThreadExecutor(); // Đảm bảo tuần tự

    @PostConstruct
    public void startImportWorker() {
        executor.submit(() -> {
            while (true) {
                File file = importQueue.take(); // Chờ lấy file tiếp theo


                try {
                    System.out.println("📥 Importing file: " + file.getName());
                    // Gọi xử lý Excel tại đây
                    excelImportService1.import1Datbase1JDBC1(file, null);


                    // Xóa file sau khi import xong
                    file.delete();
                    System.out.println("✅ Done: " + file.getName());
                } catch (Exception e) {
                    System.err.println("❌ Lỗi khi import file " + file.getName() + ": " + e.getMessage());
                }
            }
        });
    }



    public void enqueueFile(File file) {
        importQueue.add(file);
        System.out.println("📦 File added to queue: " + file.getName());
    }

    private void importFile(File file) throws Exception {
        // TODO: logic import Excel vào DB tại đây
        Thread.sleep(3000); // Giả lập xử lý lâu
    }
}

