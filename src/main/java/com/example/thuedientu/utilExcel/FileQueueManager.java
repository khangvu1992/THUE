package com.example.thuedientu.utilExcel;

import com.example.thuedientu.model.EnityExcelJDBC;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
public class FileQueueManager {

    private final AtomicInteger processedCount = new AtomicInteger();
    private volatile boolean readingDone = false;
    @Setter
    @Getter
    private volatile String errorMessage = null; // ❗ lỗi xảy ra trong import
    private final ConcurrentLinkedQueue<String> pendingFileNames = new ConcurrentLinkedQueue<>();

    private final Map<String, FileContext> fileContexts = new ConcurrentHashMap<>();

    public void createContext(String fileId, int queueCapacity, String fileName) {
        BlockingQueue<List<EnityExcelJDBC>> queue = new ArrayBlockingQueue<>(queueCapacity);
        fileContexts.put(fileId, new FileContext(queue, fileName));
    }

    public BlockingQueue<List<EnityExcelJDBC>> getQueue(String fileId) {
        return fileContexts.get(fileId).getQueue();
    }

    public void incrementProcessed(String fileId, int count) {
        FileContext context = fileContexts.get(fileId);
        if (context != null) {
            context.incrementProcessed(count);
        }
    }

    public int getProcessed(String fileId) {
        FileContext context = fileContexts.get(fileId);
        return context != null ? context.getProcessedCount() : 0;
    }

    public void markReadingDone(String fileId) {
        FileContext context = fileContexts.get(fileId);
        if (context != null) {
            context.markReadingDone();
        }
    }

    public boolean isReadingDone(String fileId) {
        FileContext context = fileContexts.get(fileId);
        return context != null && context.isReadingDone();
    }

    public void removeContext(String fileId) {
        fileContexts.remove(fileId);
    }

    public void addPendingFile(String fileName) {
        pendingFileNames.add(fileName);
    }

    public void removePendingFile(String fileName) {
        pendingFileNames.remove(fileName);
    }

    public List<String> getPendingFiles() {
        return new ArrayList<>(pendingFileNames);
    }

    public void logWaitingFiles() {
        System.out.println("\n📦 [HÀNG ĐỢI FILE] Tổng số file đang xử lý: " + fileContexts.size());

        if (!pendingFileNames.isEmpty()) {
            System.out.println("📥 Các file đang đợi tới lượt xử lý:");
            for (String name : pendingFileNames) {
                System.out.println("🕓 " + name);
            }
        }

        List<FileContext> processingFiles = fileContexts.values().stream()
                .filter(FileContext::isStillProcessing)
                .collect(Collectors.toList());

        List<FileContext> errorFiles = fileContexts.values().stream()
                .filter(FileContext::hasError)
                .collect(Collectors.toList());

        if (!processingFiles.isEmpty()) {
            System.out.println("⏳ Các file đang xử lý:");
            for (FileContext ctx : processingFiles) {
                System.out.println("🕒 " + ctx.getFileName());
            }
        }

        if (!errorFiles.isEmpty()) {
            System.out.println("❌ Các file bị lỗi:");
            for (FileContext ctx : errorFiles) {
                System.out.println("🚨 " + ctx.getFileName() + " - " );
            }
        }

        if (pendingFileNames.isEmpty() && processingFiles.isEmpty() && errorFiles.isEmpty()) {
            System.out.println("✅ Không còn file nào đang chờ.");
        }
    }

    public boolean hasError() {
        return errorMessage != null;
    }


}
