package com.example.thuedientu.util;

public class ImportProgress {
    private int totalBytes; // Tổng số bytes
    private int processedBytes; // Số bytes đã xử lý
    private boolean completed; // Đánh dấu hoàn thành
    private boolean failed; // Đánh dấu thất bại

    public ImportProgress(int totalBytes, int processedBytes, boolean completed, boolean failed) {
        this.totalBytes = totalBytes;
        this.processedBytes = processedBytes;
        this.completed = completed;
        this.failed = failed;
    }

    // Getters and Setters
    public int getTotalBytes() {
        return totalBytes;
    }

    public void setTotalBytes(int totalBytes) {
        this.totalBytes = totalBytes;
    }

    public int getProcessedBytes() {
        return processedBytes;
    }

    public void setProcessedBytes(int processedBytes) {
        this.processedBytes = processedBytes;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public boolean isFailed() {
        return failed;
    }

    public void setFailed(boolean failed) {
        this.failed = failed;
    }
}
