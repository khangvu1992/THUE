package com.example.thuedientu.service;

import com.example.thuedientu.model.HashFile;
import com.example.thuedientu.repository.FileRepository;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import org.apache.commons.codec.binary.Hex;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.security.MessageDigest;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Service
public class FileUploadService {

    // Khai báo cache Guava với thời gian hết hạn sau 1 giờ
    private Cache<String, Boolean> uploadedFileHashesCache = CacheBuilder.newBuilder()
            .expireAfterWrite(1, TimeUnit.HOURS) // Mỗi mục trong cache sẽ hết hạn sau 1 giờ
            .build();


    @Autowired
    private FileRepository fileRepository;

    // In-memory cache for file hashes (used to track duplicates within the current session)
    private Set<String> uploadedFileHashes = new HashSet<>();

    // Method to check for duplicate files based on content hash (checks both in-memory and database)
    public boolean checkForDuplicateByContent(MultipartFile file) throws Exception {
        String fileHash = generateFileHash(file);  // Generate hash for the file

//         First check in-memory cache
//        if (uploadedFileHashes.contains(fileHash)) {
//            return true;  // File is duplicate (in-memory check)
//        }

        System.out.println("cghhay den day khonrrrrrrrrrrrrrrrrrrrg ban oui");
        // If not found in cache, check the database for duplicates
        List<HashFile> existingFiles = fileRepository.findByFileHash(fileHash);
        if (!existingFiles.isEmpty()) {
            return true;  // File is duplicate (database check)
        }

        // If the file is not found in either, add the hash to cache
        uploadedFileHashes.add(fileHash);
        return false;  // File is not duplicate
    }

    // Method to generate hash for the file using SHA-256
    public String generateFileHash(MultipartFile file) throws Exception {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        try (InputStream inputStream = file.getInputStream()) {
            byte[] byteArray = new byte[1024];
            int bytesRead;

            // Read the file and update the digest with the data
            while ((bytesRead = inputStream.read(byteArray)) != -1) {
                digest.update(byteArray, 0, bytesRead);
            }
        }

        // Generate the file's hash and return it as a string
        byte[] hashBytes = digest.digest();
        return Hex.encodeHexString(hashBytes);
    }

    // Method to save the file's information into the database (if it's not a duplicate)
    public void saveFile(MultipartFile file) throws Exception {
        if (!checkForDuplicateByContent(file)) {
            HashFile hashFile = new HashFile();
            hashFile.setFilename(file.getOriginalFilename());
            hashFile.setFileHash(generateFileHash(file));  // Set the hash

            System.out.println("den day la luu");

            fileRepository.save(hashFile);  // Save the file record in the database
        } else {
            // Handle duplicate file (e.g., notify the user or skip saving)
            System.out.println("File is a duplicate and won't be saved.");
        }
    }
}
