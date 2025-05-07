package com.example.thuedientu.util;

import com.example.thuedientu.model.ExportEntity;
import com.example.thuedientu.model.SeawayMasterBillEntity;
import lombok.Getter;

import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class SeawayMasterContext {
    @Getter
    private final BlockingQueue<List<SeawayMasterBillEntity>> queue;
    private final AtomicInteger processedCount = new AtomicInteger(0);
    @Getter
    private volatile boolean readingDone = false;
    @Getter
    private final String fileName;

    private volatile String errorMessage = null;


    public SeawayMasterContext(BlockingQueue<List<SeawayMasterBillEntity>> queue, String fileName) {
        this.queue = queue;
        this.fileName = fileName;
    }

    public int getProcessedCount() {
        return processedCount.get();
    }

    public void incrementProcessed(int count) {
        processedCount.addAndGet(count);
    }

    public void markReadingDone() {
        this.readingDone = true;
    }

    // ✅ Thêm method này để dùng trong logWaitingFiles()
    public boolean isStillProcessing() {
        return !readingDone || !queue.isEmpty();
    }




    public void setErrorMessage(String message) {
        this.errorMessage = message;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public boolean hasError() {
        return errorMessage != null && !errorMessage.isEmpty();
    }
}
