package com.example.thuedientu.util;

import com.example.thuedientu.model.HashFile;
import com.example.thuedientu.service.FileUploadService;
import com.example.thuedientu.service.AirMasterBillService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

@Service
public class AirMasterQueueService {

    @Autowired
    private AirMasterBillService airMasterBillService;

    @Autowired
    private FileUploadService fileUploadService;

    private final BlockingQueue<FileWithHash> importQueue = new LinkedBlockingQueue<>();
    private final ExecutorService executor = Executors.newSingleThreadExecutor(); // Chạy tuần tự từng file

    @PostConstruct
    public void startImportWorker() {
        executor.submit(() -> {
            while (true) {
                try {
                    FileWithHash fileWithHash = importQueue.take(); // Đợi file tiếp theo trong hàng đợi
                    File file = fileWithHash.getFile();
                    HashFile hashFile = fileWithHash.getHashFile();

                    System.out.println("📥 Đang import file: " + file.getAbsolutePath());

                    // Gọi hàm xử lý import Excel
                    airMasterBillService.importToDatabase(file, hashFile);

                    // Sau khi xử lý xong, có thể xoá file tạm (nếu cần)
                    System.out.println("✅ Hoàn tất import: " + file.getName());

                } catch (Exception e) {
                    System.err.println("❌ Lỗi khi import file: " + e.getMessage());
                }
            }
        });
    }

    public void enqueueFile(File file, HashFile hashFile) {
        FileWithHash wrappedFile = new FileWithHash(file, hashFile);
        importQueue.add(wrappedFile);
        System.out.println("📦 File đã thêm vào hàng đợi: " + file.getName());
    }

    public void printQueueStatus() {
        System.out.println("📂 Trạng thái hàng đợi:");
        if (importQueue.isEmpty()) {
            System.out.println("🚫 Hàng đợi đang trống.");
        } else {
            for (FileWithHash file : importQueue) {
                System.out.printf("🕒 Chờ xử lý: %s | Hash: %s%n",
                        file.getHashFile().getFilename(),
                        file.getHashFile().getFileHash());
            }
        }
    }

    public boolean removeFileFromQueue(String hash) {
        for (FileWithHash file : importQueue) {
            if (file.getHashFile().getFileHash().equals(hash)) {
                boolean removed = importQueue.remove(file);
                if (removed) {
                    System.out.println("🗑️ Đã xoá file khỏi hàng đợi: " + file.getFile().getName());
                }
                return removed;
            }
        }
        System.out.println("⚠️ Không tìm thấy file với hash: " + hash);
        return false;
    }
}
