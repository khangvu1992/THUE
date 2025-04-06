//package com.example.thuedientu.controller;
//
//
//import com.example.thuedientu.service.FileUploadService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.web.multipart.MultipartFile;
//
//import jakarta.servlet.http.HttpServletResponse;
//
//@RestController
//@RequestMapping("/files")
//public class FileUploadController {
//
//    @Autowired
//    private FileUploadService fileUploadService;
//
//    // Endpoint để kiểm tra và upload file
//    @PostMapping("/upload")
//    public String uploadFile(@RequestParam("file") MultipartFile file, HttpServletResponse response) {
//        try {
//            // Kiểm tra xem file có trùng lặp hay không dựa trên hash
//            if (fileUploadService.checkForDuplicateByContent(file)) {
//                return "Duplicate file detected. Upload canceled.";  // Nếu trùng lặp, không tải lên
//            }
//
//            // Nếu không trùng lặp, lưu tên file vào cơ sở dữ liệu
//            fileUploadService.saveFile(file);
//
//            // Chuyển hướng đến một API khác sau khi upload thành công
//            response.sendRedirect("/import" ,file);  // Thay đổi URL chuyển hướng nếu cần
//            return null;  // Trả về null vì đã thực hiện chuyển hướng
//        } catch (Exception e) {
//            return "Error uploading file: " + e.getMessage();  // Nếu có lỗi, trả về thông báo lỗi
//        }
//    }
//
//    // Endpoint để xử lý khi upload thành công
//    @GetMapping("/success")
//    public String successPage() {
//        return "File uploaded successfully and redirected to another page!";
//    }
//}
