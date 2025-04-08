package com.example.thuedientu.util;

import com.example.thuedientu.model.HashFile;
import com.example.thuedientu.service.DatabaseServiceNew;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.File;

public class ImportTask implements Runnable {
    private final File file;
    private final HashFile hashFile;
    private final DatabaseServiceNew service;

    public ImportTask(File file, HashFile hashFile, DatabaseServiceNew service) {
        this.file = file;
        this.hashFile = hashFile;
        this.service = service;
    }

    @Override
    public void run() {
        service.importInternal(file, hashFile);
    }

    @Override
    public String toString() {
        return hashFile.getFileHash(); // để hiển thị rõ hơn trong getWaitingQueue()
    }
}
