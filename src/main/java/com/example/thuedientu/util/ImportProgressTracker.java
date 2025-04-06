package com.example.thuedientu.util;

import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Component
public class ImportProgressTracker {

    private final ConcurrentMap<String, ImportProgress> progressMap = new ConcurrentHashMap<>();

    // Bắt đầu tiến độ import
    public void start(String importId, int totalBytes) {
        progressMap.put(importId, new ImportProgress(totalBytes, 0, false, false));
    }

    // Cập nhật tiến độ sau mỗi lần tải lên hoặc xử lý
    public void update(String importId, int bytesProcessed) {
        ImportProgress progress = progressMap.get(importId);
        if (progress != null) {
            progress.setProcessedBytes(bytesProcessed);
        }
    }

    // Đánh dấu hoàn thành
    public void complete(String importId) {
        ImportProgress progress = progressMap.get(importId);
        if (progress != null) {
            progress.setCompleted(true);
        }
    }

    // Đánh dấu thất bại
    public void fail(String importId) {
        ImportProgress progress = progressMap.get(importId);
        if (progress != null) {
            progress.setFailed(true);
        }
    }

    // Lấy tiến độ theo importId
    public ImportProgress getProgress(String importId) {
        return progressMap.get(importId);
    }
}
