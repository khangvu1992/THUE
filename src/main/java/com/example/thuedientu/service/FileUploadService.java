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
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class FileUploadService {

    // Guava cache với TTL là 1 giờ cho mỗi hash
    private final Cache<String, Boolean> uploadedFileHashesCache = CacheBuilder.newBuilder()
            .expireAfterWrite(1, TimeUnit.HOURS)
            .build();

    @Autowired
    private FileRepository fileRepository;

    /**
     * Kiểm tra trùng file dựa trên hash nội dung.
     * Trả về true nếu file đã tồn tại trong cache hoặc trong database.
     */
    public boolean checkForDuplicateByContent(MultipartFile file) throws Exception {
        String fileHash = generateFileHash(file);  // Tạo hash cho file

        // Kiểm tra trong cache
        if (uploadedFileHashesCache.getIfPresent(fileHash) != null) {
            return true;
        }

        // Kiểm tra trong database
        List<HashFile> existingFiles = fileRepository.findByFileHash(fileHash);
        if (!existingFiles.isEmpty()) {
            // Nếu tồn tại trong DB, lưu vào cache để các lần sau khỏi truy vấn
            uploadedFileHashesCache.put(fileHash, true);
            return true;
        }

        // Nếu không tồn tại ở đâu cả, thêm vào cache
        uploadedFileHashesCache.put(fileHash, true);
        return false;
    }

    /**
     * Tạo hash SHA-256 từ nội dung file.
     */
    public String generateFileHash(MultipartFile file) throws Exception {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        try (InputStream inputStream = file.getInputStream()) {
            byte[] byteArray = new byte[1024];
            int bytesRead;
            while ((bytesRead = inputStream.read(byteArray)) != -1) {
                digest.update(byteArray, 0, bytesRead);
            }
        }
        byte[] hashBytes = digest.digest();
        return Hex.encodeHexString(hashBytes);
    }

    /**
     * Lưu file vào database nếu không bị trùng.
     */
    public void saveFile(MultipartFile file) throws Exception {
        if (!checkForDuplicateByContent(file)) {
            HashFile hashFile = new HashFile();
            hashFile.setFilename(file.getOriginalFilename());
            hashFile.setFileHash(generateFileHash(file));  // Gán hash

            System.out.println("File is new. Saving to database.");
            fileRepository.save(hashFile);
        } else {
            System.out.println("File is a duplicate and won't be saved.");
        }
    }
}
