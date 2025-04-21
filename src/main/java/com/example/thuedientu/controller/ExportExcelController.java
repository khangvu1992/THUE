package com.example.thuedientu.controller;

import com.example.thuedientu.model.HashFile;
import com.example.thuedientu.service.ExportImportService;
import com.example.thuedientu.service.FileUploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.Map;


@RestController
@RequestMapping("/api/export")
@CrossOrigin(origins = "*")
public class ExportExcelController {
    @Autowired
    private JdbcTemplate jdbcTemplate;


    @Autowired
    FileUploadService fileUploadService;

    @Autowired
    ExportImportService exportImportService;

    @PostMapping("/import-export")
    public ResponseEntity<?> importExportData(@RequestParam("file") MultipartFile file) {
        try {


            // Check if the table exists
            createTableIfNotExists();

                createTable();

//            Kiểm tra xem file có trùng lặp hay không dựa trên hash
            if (fileUploadService.checkForDuplicateByContent(file)) {
                System.out.println("🔁 Duplicate file detected");
                return ResponseEntity.badRequest().body(Map.of(
                        "status", "error",
                        "message", "Duplicate file detected. Upload canceled."
                ));
            }

            // Nếu không trùng lặp, lưu tên file vào cơ sở dữ liệu
            HashFile hashFile = new HashFile();
            hashFile.setFilename(file.getOriginalFilename());
            hashFile.setFileHash(fileUploadService.generateFileHash(file));


            // 1. Tạo thư mục tạm nếu chưa tồn tại
            String tempDirPath = "C:\\excel-export-temp\\";
            File tempDir = new File(tempDirPath);
            if (!tempDir.exists()) {
                tempDir.mkdirs();
            }

            // 2. Tạo file tạm an toàn
            String fileName = "export_" + System.currentTimeMillis() + ".xlsx";
            File safeTempFile = new File(tempDir, fileName);
            file.transferTo(safeTempFile);

            //  excelImportService.import1Datbase1JDBC1(safeTempFile, hashFile);





//            File temp = File.createTempFile("import-", ".xlsx");
//            file.transferTo(temp);

            exportImportService.importExcelFile(safeTempFile, hashFile);
            return ResponseEntity.ok("Import thành công!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Import thất bại: " + e.getMessage());
        }
    }

    public void createTable() {
        String sql =   "IF OBJECT_ID('dbo.thue_xuat_khau', 'U') IS NULL " +
                "BEGIN " +
                "CREATE TABLE thue_xuat_khau (" +
                "id BIGINT IDENTITY PRIMARY KEY, " +
                "so_to_khai NVARCHAR(255), " +
                "ma_chi_cuc_hai_quan_tao_moi NVARCHAR(255), " +
                "ma_phan_loai_trang_thai_sau_cung NVARCHAR(255), " +
                "bo_phan_kiem_tra_ho_so_dau_tien NVARCHAR(255), " +
                "bo_phan_kiem_tra_ho_so_sau_cung NVARCHAR(255), " +
                "phuong_thuc_van_chuyen NVARCHAR(255), " +
                "ma_loai_hinh NVARCHAR(255), " +
                "ngay_dang_ky DATE, " +
                "gio_dang_ky TIME(0), " +
                "ngay_thay_doi_dang_ky DATE, " +
                "gio_thay_doi_dang_ky TIME(0), " +
                "ma_nguoi_xuat_khau NVARCHAR(255), " +
                "ten_nguoi_xuat_khau NVARCHAR(255), " +
                "ten_nguoi_nhap_khau NVARCHAR(255), " +
                "ma_nuoc NVARCHAR(255), " +
                "so_van_don NVARCHAR(255), " +
                "so_luong DECIMAL(20,3), " +
                "ma_don_vi_tinh NVARCHAR(255), " +
                "tong_trong_luong_hang_gross DECIMAL(20,3), " +
                "ma_don_vi_tinh_trong_luong_gross NVARCHAR(255), " +
                "ma_dia_diem_nhan_hang_cuoi_cung NVARCHAR(255), " +
                "ma_dia_diem_xep_hang NVARCHAR(255), " +
                "tong_tri_gia_hoa_don DECIMAL(20,3), " +
                "tong_tri_gia_tinh_thue DECIMAL(20,3), " +
                "tong_so_tien_thue_xuat_khau DECIMAL(20,3), " +
                "tong_so_dong_hang_cua_to_khai DECIMAL(20,3), " +
                "phan_ghi_chu NVARCHAR(4000), " +
                "ngay_hoan_thanh_kiem_tra DATE, " +
                "gio_hoan_thanh_kiem_tra TIME(0), " +
                "ngay_huy_khai_bao_hai_quan DATE, " +
                "gio_huy_khai_bao_hai_quan TIME(0), " +
                "ten_nguoi_phu_trach_kiem_tra_ho_so NVARCHAR(255), " +
                "ten_nguoi_phu_trach_kiem_hoa NVARCHAR(255), " +
                "ma_so_hang_hoa NVARCHAR(255), " +
                "mo_ta_hang_hoa NVARCHAR(4000), " +
                "so_luong1 DECIMAL(20,3), " +
                "tri_gia_hoa_don DECIMAL(20,3), " +
                "don_gia_hoa_don DECIMAL(20,3), " +
                "ma_dong_tien_don_gia_hoa_don NVARCHAR(255), " +
                "tri_gia_tinh_thue_s DECIMAL(20,3), " +
                "tri_gia_tinh_thue_m DECIMAL(20,3), " +
                "don_gia_tinh_thue DECIMAL(20,3), " +
                "thue_suat_thue_xuat_khau NVARCHAR(255), " +
                "phan_loai_nhap_thue_suat_thue_xuat_khau NVARCHAR(255), " +
                "so_tien_thue_xuat_khau DECIMAL(20,3), " +
                "ma_van_ban_phap_quy_khac1 NVARCHAR(255), " +
                "ma_mien_giam_khong_chiu_thue_xuat_khau NVARCHAR(255)" +
                ") " +
                "END";

        jdbcTemplate.execute(sql);
    }

    public void createTableIfNotExists() {
        String sql = """
            IF NOT EXISTS (SELECT * FROM sysobjects WHERE name='hash_files' AND xtype='U')
            CREATE TABLE hash_files (
                id BIGINT IDENTITY(1,1) PRIMARY KEY,
                filename NVARCHAR(255) NOT NULL,
                file_hash NVARCHAR(255) NOT NULL
            );
        """;

        jdbcTemplate.execute(sql);
    }


}
