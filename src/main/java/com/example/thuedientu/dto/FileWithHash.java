package com.example.thuedientu.dto;

import com.example.thuedientu.model.HashFile;

import java.io.File;

public class FileWithHash {
    private File file;
    private HashFile hashFile;

    public FileWithHash(File file, HashFile hashFile) {
        this.file = file;
        this.hashFile = hashFile;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public HashFile getHashFile() {
        return hashFile;
    }

    public void setHashFile(HashFile hashFile) {
        this.hashFile = hashFile;
    }
}
