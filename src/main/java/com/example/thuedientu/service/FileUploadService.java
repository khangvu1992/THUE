package com.example.thuedientu.service;

import com.example.thuedientu.model.HashFile;
import com.example.thuedientu.repository.FileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.security.MessageDigest;
import java.util.List;
import org.apache.commons.codec.binary.Hex;

@Service
public class FileUploadService {

    @Autowired
    private FileRepository fileRepository;

    // Method to check for duplicate files based on hash
    public boolean checkForDuplicateByContent(MultipartFile file) throws Exception {
        String fileHash = generateFileHash(file);  // Generate hash for the file

        // Check if a file with the same hash already exists
        List<HashFile> existingFiles = fileRepository.findByFileHash(fileHash);
        return !existingFiles.isEmpty();
    }

    // Method to generate hash for the file
    private String generateFileHash(MultipartFile file) throws Exception {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        InputStream inputStream = file.getInputStream();
        byte[] byteArray = new byte[1024];
        int bytesRead;

        // Read the file and update the digest with the data
        while ((bytesRead = inputStream.read(byteArray)) != -1) {
            digest.update(byteArray, 0, bytesRead);
        }

        // Generate the file's hash and return it as a string
        byte[] hashBytes = digest.digest();
        return Hex.encodeHexString(hashBytes);
    }

    // Method to save the file's information into the database
    public void saveFile(MultipartFile file) throws Exception {
        HashFile hashFile = new HashFile();
        hashFile.setFilename(file.getOriginalFilename());
        hashFile.setFileHash(generateFileHash(file));  // Set the hash
        fileRepository.save(hashFile);  // Save the file record in the database
    }
}
