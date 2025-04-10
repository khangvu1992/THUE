package com.example.thuedientu.controller;

import com.example.thuedientu.model.HashFile;
import com.example.thuedientu.repository.FileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/files")
@CrossOrigin(origins = "*")
public class FileController {

    @Autowired
    private FileRepository fileRepository;

    // Get all HashFile records
    @GetMapping("/")
    public ResponseEntity<List<HashFile>> getAllFiles() {
        List<HashFile> hashFiles = fileRepository.findAll();
        if (hashFiles.isEmpty()) {
            return ResponseEntity.noContent().build(); // Returns 204 No Content if no records found
        }
        return ResponseEntity.ok(hashFiles); // Returns 200 OK with the list of files
    }

    // Get HashFile by file hash
    @GetMapping("/hash/{fileHash}")
    public ResponseEntity<List<HashFile>> getFilesByHash(@PathVariable String fileHash) {
        List<HashFile> hashFiles = fileRepository.findByFileHash(fileHash);
        if (hashFiles.isEmpty()) {
            return ResponseEntity.notFound().build(); // Returns 404 Not Found if no records found
        }
        return ResponseEntity.ok(hashFiles); // Returns 200 OK with the list of files
    }

    // Get HashFile by filename
    @GetMapping("/filename/{filename}")
    public ResponseEntity<List<HashFile>> getFilesByFilename(@PathVariable String filename) {
        List<HashFile> hashFiles = fileRepository.findByFilename(filename);
        if (hashFiles.isEmpty()) {
            return ResponseEntity.notFound().build(); // Returns 404 Not Found if no records found
        }
        return ResponseEntity.ok(hashFiles); // Returns 200 OK with the list of files
    }

    // Get a single HashFile by ID (optional)
    @GetMapping("/{id}")
    public ResponseEntity<HashFile> getFileById(@PathVariable Long id) {
        Optional<HashFile> hashFile = fileRepository.findById(id);
        return hashFile.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build()); // Returns 404 if file not found
    }
}
