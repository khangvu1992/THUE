package com.example.thuedientu.util;

import com.example.thuedientu.model.EnityExcelJDBC;
import lombok.Getter;

import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class FileContext {
    @Getter
    private final BlockingQueue<List<EnityExcelJDBC>> queue;
    private final AtomicInteger processedCount = new AtomicInteger(0);
    @Getter
    private volatile boolean readingDone = false;
    @Getter
    private final String fileName;

    public FileContext(BlockingQueue<List<EnityExcelJDBC>> queue, String fileName) {
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

}
