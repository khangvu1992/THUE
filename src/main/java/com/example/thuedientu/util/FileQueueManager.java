package com.example.thuedientu.util;

import com.example.thuedientu.model.EnityExcelJDBC;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class FileQueueManager {

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

    public void logProgress() {
        System.out.println("\n📦 [HÀNG ĐỢI FILE] Tổng số file đang xử lý: " + fileContexts.size());

        if (fileContexts.isEmpty()) {
            System.out.println("📭 Không có file nào trong hàng đợi.");
            return;
        }

        for (Map.Entry<String, FileContext> entry : fileContexts.entrySet()) {
            String fileId = entry.getKey();
            FileContext context = entry.getValue();

            int processed = context.getProcessedCount();
            boolean done = context.isReadingDone();
            int queueSize = context.getQueue().size();
            String fileName = context.getFileName();

            System.out.println("📄 File: " + fileName +
                    " | ID: " + fileId +
                    " | Đã xử lý: " + processed +
                    " dòng | Trong hàng đợi: " + queueSize +
                    " batch | Đọc xong: " + (done ? "✅" : "⏳"));
        }
    }

}
