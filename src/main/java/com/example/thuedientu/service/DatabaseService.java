package com.example.thuedientu.service;

import com.example.thuedientu.model.EnityExcelJDBC;
import com.example.thuedientu.model.HashFile;
import com.example.thuedientu.repository.FileRepository;
import com.example.thuedientu.util.FileContext;
import com.example.thuedientu.util.FileQueueManager;
import com.example.thuedientu.util.ProgressWebSocketSender;
import com.example.thuedientu.util.mapEntityJDBC;
import com.monitorjbl.xlsx.StreamingReader;
import org.apache.poi.ss.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.scheduling.annotation.Async;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

@Service
public class DatabaseService {

    private final JdbcTemplate jdbcTemplate;
    private final int BATCH_SIZE = 20000;
    private final int WORKER_COUNT = 16;

    @Autowired private insertDataBatchService insertDataBatchService1;
    @Autowired private FileRepository fileRepository;
    @Autowired private mapEntityJDBC map1EntityJDBC;
    @Autowired private FileQueueManager fileQueueManager;
    @Autowired
    private ProgressWebSocketSender progressWebSocketSender;

    public DatabaseService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Async
    public void import1Datbase1JDBC1(File file, HashFile hashFile) {
        String fileId = hashFile.getFileHash();
        String filename = hashFile.getFilename();
        fileQueueManager.removePendingFile(filename);

        createTable();
        fileQueueManager.createContext(fileId, 50, filename);

        for (int i = 0; i < WORKER_COUNT; i++) {
            new Thread(() -> workerWriteToDb(fileId,filename), "worker-" + i + "-" + fileId).start();
        }

        readExcelAndEnqueue(file, fileId);

        fileQueueManager.markReadingDone(fileId);

        file.delete();
        fileRepository.save(hashFile);
        System.out.println("🧹 Xoá file tạm: " + file.getAbsolutePath());
    }

    private void workerWriteToDb(String fileId,String filename) {
        BlockingQueue<List<EnityExcelJDBC>> queue = fileQueueManager.getQueue(fileId);

        while (true) {
            try {
                List<EnityExcelJDBC> batch = queue.poll(3, TimeUnit.SECONDS);
                if (batch == null) {
                    if (fileQueueManager.isReadingDone(fileId)) break;
                    else continue;
                }

                insertDataBatchService1.insertDataBatch(batch);
                fileQueueManager.incrementProcessed(fileId, batch.size());

//                FileContext ctx = fileQueueManager.getContext(fileId);
                progressWebSocketSender.sendProgress1("fileId, ctx.getFileName(), ctx.getProcessedCount(), ctx.getQueue().size(), ctx.isReadingDone(), ctx.getErrorMessage()");

            } catch (Exception e) {
                e.printStackTrace();
                fileQueueManager.setErrorMessage( "❌ Lỗi khi import file: " + filename + " - " + e.getMessage());

            }
        }

        System.out.println(Thread.currentThread().getName() + " done!");
        fileQueueManager.removeContext(fileId);
    }

    public void createTable() {
        String createTableSQL = "IF OBJECT_ID('dbo.khang_heheJDBC', 'U') IS NULL " +
                "BEGIN " +
                "CREATE TABLE dbo.khang_heheJDBC (" +
                "tkid BIGINT, sotk NVARCHAR(255), mahq NVARCHAR(255), trangthaitk NVARCHAR(255), " +
                "bpkthsdt INT,  bptq INT, ptvc NVARCHAR(255), malh NVARCHAR(255), " +
                "ngay_Dk DATE, hour_Dk TIME, ngay_Thay_doiDk DATE, " +
                "hour_Thay_doi_Dk TIME, masothue_Kbhq NVARCHAR(255), ten_Doanhnghiep NVARCHAR(255), " +
                "sodienthoai NVARCHAR(255), ten_Doanhnghiep_Uythac NVARCHAR(255), ten_Doitacnuocngoai NVARCHAR(255), " +
                "maquocgia_Doitacnuocngoai NCHAR(10), vandon_01 NVARCHAR(255), vandon_02 NVARCHAR(255), vandon_03 NVARCHAR(255), " +
                "vandon_04 NVARCHAR(255), vandon_05 NVARCHAR(255), soluongkienhang BIGINT, " +
                "ma_Dvt_Kienhang NVARCHAR(255), grossweight DECIMAL(18,5), ma_Dvt_Gw NVARCHAR(255), " +
                "soluong_Container INT, ma_Diadiemdohang NVARCHAR(255), ma_Diadiemxephang NVARCHAR(255), " +
                "ten_Phuongtienvanchuyen NVARCHAR(255), ngay_Hang_Den DATE, phuong_Thuc_Thanh_Toan NVARCHAR(255), " +
                "tong_Tri_Gia_Hoa_Don DECIMAL(18,5), tong_Tri_Gia_Tinh_Thue DECIMAL(18,5), tong_Tien_Thue DECIMAL(18,5), " +
                "tong_So_Donghang BIGINT, ngay_Cap_Phep DATE, gio_Cap_Phep TIME, " +
                "ngay_Hoanthanh_Kiemtra DATE, gio_Hoanthanh_Kiemtra TIME, ngay_Huy_Tk DATE, " +
                "gio_Huy_Tk TIME, ten_Nguoiphutrach_Kiemtrahoso NVARCHAR(255), ten_Nguoiphutrach_Kiemhoa NVARCHAR(255), " +
                "hs_Code NVARCHAR(255), mo_Ta_Hang_Hoa NVARCHAR(4000), so_Luong_Hanghoa INT, " +
                "ma_Dvt_Hanghoa NVARCHAR(255), tri_Gia_Hoa_Don DECIMAL(18,5), dongia_Hoadon DECIMAL(18,5), " +
                "ma_Tiente_Hoadon NVARCHAR(255), donvi_Dongia_Tiente NVARCHAR(255), tri_Gia_Tinh_Thue_S DECIMAL(18,5), " +
                "tri_Gia_Tinh_Thue_M DECIMAL(18,5), dongia_Tinhthue DECIMAL(18,7), thuesuat_Nhapkhau NVARCHAR(255), " +
                "tien_Thue_Nhapkhau DECIMAL(18,5), xuatXu NVARCHAR(255), ma_Vanbanphapquy NVARCHAR(255), " +
                "phanloai_Giayphep_Nk NVARCHAR(255), ma_Bieuthue_Nk NVARCHAR(255), ma_Miengiam_Thue NVARCHAR(255)" +
                "); END";


        jdbcTemplate.execute(createTableSQL);
    }

//    @Transactional
    public void insertDataBatch(List<EnityExcelJDBC> batchList) {
        String insertSQL = "INSERT INTO khang_heheJDBC (tkid, sotk, mahq, trangthaitk, bpkthsdt, bptq, ptvc, malh, " +
                "ngay_Dk, hour_Dk, ngay_Thay_doiDk, hour_Thay_doi_Dk, masothue_Kbhq, ten_Doanhnghiep, " +
                "sodienthoai, ten_Doanhnghiep_Uythac, ten_Doitacnuocngoai, maquocgia_Doitacnuocngoai, vandon_01, vandon_02, vandon_03, vandon_04, vandon_05, " +
                "soluongkienhang, ma_Dvt_Kienhang, grossweight, ma_Dvt_Gw, soluong_Container, ma_Diadiemdohang, " +
                "ma_Diadiemxephang, ten_Phuongtienvanchuyen, ngay_Hang_Den, phuong_Thuc_Thanh_Toan, tong_Tri_Gia_Hoa_Don, tong_Tri_Gia_Tinh_Thue, " +
                "tong_Tien_Thue, tong_So_Donghang, ngay_Cap_Phep, gio_Cap_Phep, ngay_Hoanthanh_Kiemtra, " +
                "gio_Hoanthanh_Kiemtra, ngay_Huy_Tk, gio_Huy_Tk, ten_Nguoiphutrach_Kiemtrahoso, ten_Nguoiphutrach_Kiemhoa, hs_Code, mo_Ta_Hang_Hoa, so_Luong_Hanghoa, " +
                "ma_Dvt_Hanghoa, tri_Gia_Hoa_Don, dongia_Hoadon, ma_Tiente_Hoadon, donvi_Dongia_Tiente, tri_Gia_Tinh_Thue_S, " +
                "tri_Gia_Tinh_Thue_M, dongia_Tinhthue, thuesuat_Nhapkhau, tien_Thue_Nhapkhau, xuatXu, ma_Vanbanphapquy, phanloai_Giayphep_Nk, ma_Bieuthue_Nk, ma_Miengiam_Thue ) " +
                "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

        jdbcTemplate.batchUpdate(insertSQL, batchList, batchList.size(), (ps, e) -> {
            ps.setString(1, e.getBpkthsdt());
            ps.setString(2, e.getBptq());
            ps.setString(3, e.getPtvc());
            ps.setString(4, e.getMalh());
            ps.setString(5, e.getNgayDk());
            ps.setString(6, e.getHourDk());
            ps.setString(7, e.getNgayThaydoiDk());
            ps.setString(8, e.getHourThaydoiDk());
            ps.setString(9, e.getMasothueKbhq());
            ps.setString(10, e.getTenDoanhnghiep());
            ps.setString(11, e.getSodienthoai());
            ps.setString(12, e.getTenDoanhnghiepUythac());
            ps.setString(13, e.getTenDoitacnuocngoai());
            ps.setString(14, e.getMaquocgiaDoitacnuocngoai());
            ps.setString(15, e.getVandon01());
            ps.setString(16, e.getVandon02());
            ps.setString(17, e.getVandon03());
            ps.setString(18, e.getVandon04());
            ps.setString(19, e.getVandon05());
            ps.setString(20, e.getSoluongkienhang());
            ps.setString(21, e.getMaDvtKienhang());
            ps.setString(22, e.getGrossweight());
            ps.setString(23, e.getMaDvtGw());
            ps.setString(24, e.getSoluongContainer());
            ps.setString(25, e.getMaDiadiemdohang());
            ps.setString(26, e.getMaDiadiemxephang());
            ps.setString(27, e.getTenPhuongtienvanchuyen());
            ps.setString(28, e.getNgayHangDen());
            ps.setString(29, e.getPhuongThucThanhToan());
            ps.setString(30, e.getTongTriGiaHoaDon());
            ps.setString(31, e.getTongTriGiaTinhThue());
            ps.setString(32, e.getTongTienThue());
            ps.setString(33, e.getTongSoDonghang());
            ps.setString(34, e.getNgayCapPhep());
            ps.setString(35, e.getGioCapPhep());
            ps.setString(36, e.getNgayHoanthanhKiemtra());
            ps.setString(37, e.getGioHoanthanhKiemtra());
            ps.setString(38, e.getNgayHuyTk());
            ps.setString(39, e.getGioHuyTk());
            ps.setString(40, e.getTenNguoiphutrachKiemtrahoso());
            ps.setString(41, e.getTenNguoiphutrachKiemhoa());
            ps.setString(42, e.getHsCode());
            ps.setString(43, e.getMoTaHangHoa());
            ps.setString(44, e.getSoLuongHanghoa());
            ps.setString(45, e.getMaDvtHanghoa());
            ps.setString(46, e.getTriGiaHoaDon());
            ps.setString(47, e.getDongiaHoadon());
            ps.setString(48, e.getMaTienteHoadon());
            ps.setString(49, e.getDonviDongiaTiente());
            ps.setString(50, e.getTriGiaTinhThueS());
            ps.setString(51, e.getTriGiaTinhThueM());
            ps.setString(52, e.getDongiaTinhthue());
            ps.setString(53, e.getThuesuatNhapkhau());
            ps.setString(54, e.getTienThueNhapkhau());
            ps.setString(55, e.getXuatxu());
            ps.setString(56, e.getMaVanbanphapquy());
            ps.setString(57, e.getPhanloaiGiayphepNk());
            ps.setString(58, e.getMaBieuthueNk());
            ps.setString(59, e.getMaMiengiamThue());
            ps.setString(60, e.getTkid());
            ps.setString(61, e.getSotk());
            ps.setString(62, e.getMahq());
            ps.setString(63, e.getTrangthaitk());
        });
    }

    private void readExcelAndEnqueue(File file, String fileId) {
        try (InputStream is = new FileInputStream(file);
             Workbook workbook = StreamingReader.builder()
                     .rowCacheSize(100)
                     .bufferSize(4096)
                     .open(is)) {

            Sheet sheet = workbook.getSheetAt(0);
            Iterator<Row> rows = sheet.iterator();
            if (rows.hasNext()) rows.next(); // skip header

            List<EnityExcelJDBC> batch = new ArrayList<>();
            int count = 0;

            while (rows.hasNext()) {
                Row row = rows.next();
                EnityExcelJDBC entity = new EnityExcelJDBC();
                map1EntityJDBC.mapRowToEntity(row, entity);
                batch.add(entity);
                count++;

                if (batch.size() >= BATCH_SIZE) {
                    fileQueueManager.getQueue(fileId).put(new ArrayList<>(batch));
                    batch.clear();
                }

                if (count % 10000 == 0) {
                    fileQueueManager.logWaitingFiles();
                    progressWebSocketSender.sendProgress1("fileId, ctx.getFileName(), ctx.getProcessedCount(), ctx.getQueue().size(), ctx.isReadingDone(), ctx.getErrorMessage()");


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