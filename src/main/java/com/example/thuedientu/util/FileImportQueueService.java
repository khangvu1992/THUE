package com.example.thuedientu.util;

import com.example.thuedientu.dto.FileWithHash;
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

    private final BlockingQueue<FileWithHash> importQueue = new LinkedBlockingQueue<>();
    private final ExecutorService executor = Executors.newSingleThreadExecutor(); // Đảm bảo tuần tự



    @PostConstruct
    public void startImportWorker() {
        executor.submit(() -> {
            while (true) {
                FileWithHash file = importQueue.take(); // Chờ lấy file tiếp theo


                try {
                    System.out.println("📥 Importing file: " + file.getFile());
                    // Gọi xử lý Excel tại đây
                    excelImportService1.import1Datbase1JDBC1(file.getFile(), file.getHashFile());


                    // Xóa file sau khi import xong
                    System.out.println("✅ Done: " + file.getFile());

                    //xoa file khoi hang doi

                    importQueue.remove(file);

                } catch (Exception e) {
                    System.err.println("❌ Lỗi khi import file " +  ": " + e.getMessage());
                }
            }
        });
    }



    public void enqueueFile(File file, HashFile hashFile) {
        FileWithHash fileNew= new FileWithHash(file,hashFile);
        importQueue.add(fileNew);
        System.out.println("📦 File added to queue: " + file.getName());
    }


}

