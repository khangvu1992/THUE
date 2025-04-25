package com.example.thuedientu.service;

import com.example.thuedientu.model.ExportEntity;
import com.example.thuedientu.model.HashFile;
import com.example.thuedientu.repository.FileRepository;
import com.example.thuedientu.util.*;
import com.monitorjbl.xlsx.StreamingReader;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

@Service
public class DatabaseServiceClone {

    @Autowired
    private ExcelDataFormatterService formatterService;

    private final JdbcTemplate jdbcTemplate;
    private final int BATCH_SIZE = 5000;
    private final int WORKER_COUNT = 4;

    //    @Autowired private insertDataBatchService insertDataBatchService1;
    @Autowired private FileRepository fileRepository;
    @Autowired private ExcelProcessingService excelProcessingService;

    @Autowired private mapEntityJDBCClone map1EntityJDBC;
    @Autowired private FileQueueManagerClone fileQueueManager;
    @Autowired
    private ProgressWebSocketSender progressWebSocketSender;

    public DatabaseServiceClone(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void import1Datbase1JDBC1(File file, HashFile hashFile) {
        String fileId = hashFile.getFileHash();
        String filename = hashFile.getFilename();
        fileQueueManager.removePendingFile(filename);
        createTable();
        fileQueueManager.createContext(fileId, 50, filename);

        for (int i = 0; i < WORKER_COUNT; i++) {
            new Thread(() -> workerWriteToDb(fileId,filename), "worker-" + i + "-" + filename).start();
            System.out.println("worker-" + i + "-" + fileId);
        }

        readExcelAndEnqueue(file, fileId,filename);

        fileQueueManager.markReadingDone(fileId);

        file.delete();
        fileRepository.save(hashFile);
        System.out.println("🧹 Xoá file tạm: " + file.getAbsolutePath());
    }

    private void workerWriteToDb(String fileId,String filename) {
        BlockingQueue<List<ExportEntity>> queue = fileQueueManager.getQueue(fileId);

        while (true) {
            try {
                List<ExportEntity> batch = queue.poll(5, TimeUnit.SECONDS);
                if (batch == null) {
                    if (fileQueueManager.isReadingDone(fileId)) break;
                    else continue;
                }

                insertDataBatch(batch);
                fileQueueManager.incrementProcessed(fileId, batch.size());



//                FileContext ctx = fileQueueManager.getContext(fileId);
//                progressWebSocketSender.sendProgress1("fileId, ctx.getFileName(), ctx.getProcessedCount(), ctx.getQueue().size(), ctx.isReadingDone(), ctx.getErrorMessage()");

            } catch (Exception e) {
                e.printStackTrace();
                fileQueueManager.setErrorMessage( "❌ Lỗi khi import file: " + filename + " - " + e.getMessage());

            }
        }

        System.out.println(Thread.currentThread().getName() + " done!");
        progressWebSocketSender.sendProgress1(fileId,filename, 100,true);

        fileQueueManager.removeContext(fileId);


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

    //    @Transactional
    public void insertDataBatch(List<ExportEntity> batchList) {
        String insertSQL = "INSERT INTO thue_xuat_khau( " +
                "so_to_khai, " +
                "ma_chi_cuc_hai_quan_tao_moi," +
                " ma_phan_loai_trang_thai_sau_cung," +
                " bo_phan_kiem_tra_ho_so_dau_tien," +
                " bo_phan_kiem_tra_ho_so_sau_cung," +
                " phuong_thuc_van_chuyen," +
                " ma_loai_hinh, " +
                "ngay_dang_ky," +
                " gio_dang_ky," +
                " ngay_thay_doi_dang_ky," +
                " gio_thay_doi_dang_ky," +
                " ma_nguoi_xuat_khau," +
                " ten_nguoi_xuat_khau, " +
                "ten_nguoi_nhap_khau," +
                " ma_nuoc," +
                " so_van_don," +
                " so_luong," +
                " ma_don_vi_tinh, " +
                "tong_trong_luong_hang_gross," +
                " ma_don_vi_tinh_trong_luong_gross," +
                " ma_dia_diem_nhan_hang_cuoi_cung," +
                " ma_dia_diem_xep_hang, " +
                "tong_tri_gia_hoa_don, " +
                "tong_tri_gia_tinh_thue, " +
                "tong_so_tien_thue_xuat_khau," +
                " tong_so_dong_hang_cua_to_khai," +
                " phan_ghi_chu," +
                " ngay_hoan_thanh_kiem_tra, " +
                "gio_hoan_thanh_kiem_tra," +
                " ngay_huy_khai_bao_hai_quan," +
                " gio_huy_khai_bao_hai_quan," +
                " ten_nguoi_phu_trach_kiem_tra_ho_so," +
                " ten_nguoi_phu_trach_kiem_hoa," +
                " ma_so_hang_hoa, " +
                "mo_ta_hang_hoa," +
                " so_luong1," +
                " tri_gia_hoa_don," +
                " don_gia_hoa_don," +
                " ma_dong_tien_don_gia_hoa_don, " +
                "tri_gia_tinh_thue_s," +
                " tri_gia_tinh_thue_m, " +
                "don_gia_tinh_thue, " +
                "thue_suat_thue_xuat_khau," +
                " phan_loai_nhap_thue_suat_thue_xuat_khau, " +
                "so_tien_thue_xuat_khau," +
                " ma_van_ban_phap_quy_khac1," +
                " ma_mien_giam_khong_chiu_thue_xuat_khau ) " +
                "VALUES (?,?,?,?,?,?,?,?,?,?," +
                "?,?,?,?,?,?,?,?,?,?," +
                "?,?,?,?,?,?,?,?,?,?," +
                "?,?,?,?,?,?,?,?,?,?," +
                "?,?,?,?,?,?,?)";




        jdbcTemplate.batchUpdate(insertSQL, batchList, batchList.size(), (ps, e) -> {

            ps.setString(1, e.getSoToKhai());
            ps.setString(2, e.getMaChiCucHaiQuanTaoMoi());
            ps.setString(3, e.getMaPhanLoaiTrangThaiSauCung());
            ps.setString(4, e.getBoPhanKiemTraHoSoDauTien());
            ps.setString(5, e.getBoPhanKiemTraHoSoSauCung());
            ps.setString(6, e.getPhuongThucVanChuyen());
            ps.setString(7, e.getMaLoaiHinh());
            ps.setDate(8, e.getNgayDangKy());
            ps.setTime(9, e.getGioDangKy());
            ps.setDate(10, e.getNgayThayDoiDangKy());
            ps.setTime(11, e.getGioThayDoiDangKy());
            ps.setString(12, e.getMaNguoiXuatKhau());
            ps.setString(13, e.getTenNguoiXuatKhau());
            ps.setString(14, e.getTenNguoiNhapKhau());
            ps.setString(15, e.getMaNuoc());
            ps.setString(16, e.getSoVanDon());
            ps.setBigDecimal(17, e.getSoLuong());
            ps.setString(18, e.getMaDonViTinh());
            ps.setBigDecimal(19, e.getTongTrongLuongHangGross());
            ps.setString(20, e.getMaDonViTinhTrongLuongGross());
            ps.setString(21, e.getMaDiaDiemNhanHangCuoiCung());
            ps.setString(22, e.getMaDiaDiemXepHang());
            ps.setBigDecimal(23, e.getTongTriGiaHoaDon());
            ps.setBigDecimal(24, e.getTongTriGiaTinhThue());
            ps.setBigDecimal(25, e.getTongSoTienThueXuatKhau());
            ps.setBigDecimal(26, e.getTongSoDongHangCuaToKhai());
            ps.setString(27, e.getPhanGhiChu());
            ps.setDate(28, e.getNgayHoanThanhKiemTra());
            ps.setTime(29, e.getGioHoanThanhKiemTra());
            ps.setDate(30, e.getNgayHuyKhaiBaoHaiQuan());
            ps.setTime(31, e.getGioHuyKhaiBaoHaiQuan());
            ps.setString(32, e.getTenNguoiPhuTrachKiemTraHoSo());
            ps.setString(33, e.getTenNguoiPhuTrachKiemHoa());
            ps.setString(34, e.getMaSoHangHoa());
            ps.setString(35, e.getMoTaHangHoa());
            ps.setBigDecimal(36, e.getSoLuong1());
            ps.setBigDecimal(37, e.getTriGiaHoaDon());
            ps.setBigDecimal(38, e.getDonGiaHoaDon());
            ps.setString(39, e.getMaDongTienDonGiaHoaDon());
            ps.setBigDecimal(40, e.getTriGiaTinhThueS());
            ps.setBigDecimal(41, e.getTriGiaTinhThueM());
            ps.setBigDecimal(42, e.getDonGiaTinhThue());
            ps.setString(43, e.getThueSuatThueXuatKhau());
            ps.setString(44, e.getPhanLoaiNhapThueSuatThueXuatKhau());
            ps.setBigDecimal(45, e.getSoTienThueXuatKhau());
            ps.setString(46, e.getMaVanBanPhapQuyKhac1());
            ps.setString(47, e.getMaMienGiamKhongChiuThueXuatKhau());


        });
    }

    private void readExcelAndEnqueue(File file, String fileId,String filename) {

        // 1. Đếm tổng số dòng (trừ header nếu cần)
//        int totalRows = excelProcessingService.countTotalRows(file);


        try (InputStream is = new FileInputStream(file);
             Workbook workbook = StreamingReader.builder()
                     .rowCacheSize(100)
                     .bufferSize(4096)
                     .open(is)) {

            Sheet sheet = workbook.getSheetAt(0);
            Iterator<Row> rows = sheet.iterator();
            if (rows.hasNext()) rows.next(); // skip header

            List<ExportEntity> batch = new ArrayList<>();
            int count = 0;

            while (rows.hasNext()) {
                Row row = rows.next();
                ExportEntity entity = new ExportEntity();
                map1EntityJDBC.mapRowToEntity(row, entity);
                batch.add(entity);
                count++;

                if (batch.size() >= BATCH_SIZE) {
                    fileQueueManager.getQueue(fileId).put(new ArrayList<>(batch));
                    batch.clear();
                }

                if (count % 10000 == 0) {
//                    fileQueueManager.logWaitingFiles();
                    int progress = (int) count*100 / 1048576 ;

                    progressWebSocketSender.sendProgress1(fileId,filename, progress,false);

//                    System.out.println("Progress: " + count);
                }
            }

            if (!batch.isEmpty()) {
                fileQueueManager.getQueue(fileId).put(batch);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}