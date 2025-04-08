package com.example.thuedientu.util;

import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class ImportProgressTracker {

    public static class FileProgress {
        private final String fileId;
        private final String fileName;
        private final int totalRows;
        private int processedRows;
        private boolean done;
        private boolean failed;
        private String errorMessage;

        public FileProgress(String fileId, String fileName, int totalRows) {
            this.fileId = fileId;
            this.fileName = fileName;
            this.totalRows = totalRows;
            this.processedRows = 0;
            this.done = false;
            this.failed = false;
        }

        public synchronized void update(int rows) {
            this.processedRows = rows;
        }

        public synchronized void increment(int rows) {
            this.processedRows += rows;
        }

        public synchronized void finish() {
            this.done = true;
        }

        public synchronized void fail(String message) {
            this.failed = true;
            this.done = true;
            this.errorMessage = message;
        }

        public String getFileId() {
            return fileId;
        }

        public String getFileName() {
            return fileName;
        }

        public int getTotalRows() {
            return totalRows;
        }

        public int getProcessedRows() {
            return processedRows;
        }

        public boolean isDone() {
            return done;
        }

        public boolean isFailed() {
            return failed;
        }

        public String getErrorMessage() {
            return errorMessage;
        }

        public double getProgressPercent() {
            return totalRows == 0 ? 0 : (processedRows * 100.0 / totalRows);
        }
    }

    private final Map<String, FileProgress> progressMap = new ConcurrentHashMap<>();

    public void startTracking(String fileId, String fileName) {
        int totalRows = 1_048_576;
        progressMap.put(fileId, new FileProgress(fileId, fileName, totalRows));
    }

    public void updateProgress(String fileId, int processedRows) {
        FileProgress progress = progressMap.get(fileId);
        if (progress != null) {
            progress.update(processedRows);
        }
    }

    public void incrementWritten(String fileId, int rowsWritten) {
        FileProgress progress = progressMap.get(fileId);
        if (progress != null) {
            progress.increment(rowsWritten);
        }
    }

    public void finish(String fileId) {
        FileProgress progress = progressMap.get(fileId);
        if (progress != null) {
            progress.finish();
        }
    }

    public void markFailed(String fileId, String errorMessage) {
        FileProgress progress = progressMap.get(fileId);
        if (progress != null) {
            progress.fail(errorMessage);
        }
    }

    public FileProgress getProgress(String fileId) {
        return progressMap.get(fileId);
    }

    public Map<String, FileProgress> getAllProgress() {
        return progressMap;
    }
}
