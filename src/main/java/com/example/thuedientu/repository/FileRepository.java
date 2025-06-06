package com.example.thuedientu.repository;

import com.example.thuedientu.model.HashFile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FileRepository extends JpaRepository<HashFile, Long> {

    // Find HashFile by file hash
    List<HashFile> findByFileHash(String fileHash);

    // Find HashFile by filename
    List<HashFile> findByFilename(String filename);
}
