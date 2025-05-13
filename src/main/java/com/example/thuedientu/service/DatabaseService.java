package com.example.thuedientu.service;

import com.example.thuedientu.model.EnityExcelJDBC;
import com.example.thuedientu.model.HashFile;
import com.example.thuedientu.repository.FileRepository;
import com.example.thuedientu.util.*;
import com.monitorjbl.xlsx.StreamingReader;
import org.apache.poi.ss.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.scheduling.annotation.Async;

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
public class DatabaseService {

    @Autowired
    private ExcelDataFormatterService formatterService;

    private final JdbcTemplate jdbcTemplate;
    private final int BATCH_SIZE = 5000;
    private final int WORKER_COUNT = 4;

    //    @Autowired private insertDataBatchService insertDataBatchService1;
    @Autowired private FileRepository fileRepository;

    @Autowired private mapEntityJDBC map1EntityJDBC;
    @Autowired private FileQueueManager fileQueueManager;
    @Autowired
    private ProgressWebSocketSender progressWebSocketSender;

    public DatabaseService(JdbcTemplate jdbcTemplate) {
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
        BlockingQueue<List<EnityExcelJDBC>> queue = fileQueueManager.getQueue(fileId);

        while (true) {
            try {
                List<EnityExcelJDBC> batch = queue.poll(5, TimeUnit.SECONDS);
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


        String createTableSQL = "IF OBJECT_ID('dbo.thue_nhap_khau', 'U') IS NULL " +
                "BEGIN " +
                "CREATE TABLE dbo.thue_nhap_khau (" +
                "tkid BIGINT IDENTITY(1,1) PRIMARY KEY, sotk NVARCHAR(255), mahq NVARCHAR(255), trangthaitk NVARCHAR(255), " +
                "bpkthsdt BIGINT,  bptq NVARCHAR(255), ptvc NVARCHAR(255), malh NVARCHAR(255), " +
                "ngay_Dk DATE, hour_Dk TIME(0), ngay_Thaydoi_Dk DATE, " +
                "hour_Thaydoi_Dk TIME(0), masothue_Kbhq NVARCHAR(255), ten_Doanhnghiep NVARCHAR(255), " +
                "sodienthoai NVARCHAR(255), ten_Doanhnghiep_Uythac NVARCHAR(255), ten_Doitacnuocngoai NVARCHAR(255), " +
                "maquocgia_Doitacnuocngoai NCHAR(10), vandon_01 NVARCHAR(255), vandon_02 NVARCHAR(255), vandon_03 NVARCHAR(255), " +
                "vandon_04 NVARCHAR(255), vandon_05 NVARCHAR(255), soluongkienhang BIGINT, " +
                "ma_Dvt_Kienhang NVARCHAR(255), grossweight DECIMAL(15,3), ma_Dvt_Gw NVARCHAR(255), " +
                "soluong_Container BIGINT, ma_Diadiemdohang NVARCHAR(255), ma_Diadiemxephang NVARCHAR(255), " +
                "ten_Phuongtienvanchuyen NVARCHAR(255), ngay_Hang_Den DATE, phuong_Thuc_Thanh_Toan NVARCHAR(255), " +
                "tong_Tri_Gia_Hoa_Don DECIMAL(20,3), tong_Tri_Gia_Tinh_Thue DECIMAL(20,3), tong_Tien_Thue DECIMAL(20,3), " +
                "tong_So_Donghang BIGINT, ngay_Cap_Phep DATE, gio_Cap_Phep TIME(0), " +
                "ngay_Hoanthanh_Kiemtra DATE, gio_Hoanthanh_Kiemtra TIME(0), ngay_Huy_Tk DATE, " +
                "gio_Huy_Tk TIME(0), ten_Nguoiphutrach_Kiemtrahoso NVARCHAR(255), ten_Nguoiphutrach_Kiemhoa NVARCHAR(255), " +
                "hs_Code NVARCHAR(255), mo_Ta_Hang_Hoa NVARCHAR(4000), so_Luong_Hanghoa DECIMAL(20,3), " +
                "ma_Dvt_Hanghoa NVARCHAR(255), tri_Gia_Hoa_Don DECIMAL(20,3), dongia_Hoadon DECIMAL(20,3), " +

                "ma_Tiente_Hoadon NVARCHAR(255), donvi_Dongia_Tiente NVARCHAR(255), tri_Gia_Tinh_Thue_S DECIMAL(20,3), " +
                "tri_Gia_Tinh_Thue_M DECIMAL(20,3), dongia_Tinhthue DECIMAL(20,3), thuesuat_Nhapkhau NVARCHAR(255), " +

                "tien_Thue_Nhapkhau DECIMAL(20,3), xuatxu NVARCHAR(255), ma_Vanbanphapquy NVARCHAR(255), " +
                "phanloai_Giayphep_Nk NVARCHAR(255), ma_Bieuthue_Nk NVARCHAR(255), ma_Miengiam_Thue NVARCHAR(255)" +
                "); END";


        jdbcTemplate.execute(createTableSQL);
    }

    //    @Transactional
    public void insertDataBatch(List<EnityExcelJDBC> batchList) {
        String insertSQL = "INSERT INTO thue_nhap_khau ( sotk, mahq, trangthaitk, bpkthsdt, bptq, ptvc, malh, " +
                "ngay_Dk, hour_Dk, ngay_Thaydoi_Dk, hour_Thaydoi_Dk, masothue_Kbhq, ten_Doanhnghiep, " +
                "sodienthoai, ten_Doanhnghiep_Uythac, ten_Doitacnuocngoai, maquocgia_Doitacnuocngoai, vandon_01, vandon_02, vandon_03, vandon_04, vandon_05, " +
                "soluongkienhang, ma_Dvt_Kienhang, grossweight, ma_Dvt_Gw, soluong_Container, ma_Diadiemdohang, " +
                "ma_Diadiemxephang, ten_Phuongtienvanchuyen, ngay_Hang_Den, phuong_Thuc_Thanh_Toan, tong_Tri_Gia_Hoa_Don, tong_Tri_Gia_Tinh_Thue, " +
                "tong_Tien_Thue, tong_So_Donghang, ngay_Cap_Phep, gio_Cap_Phep, ngay_Hoanthanh_Kiemtra, " +
                "gio_Hoanthanh_Kiemtra, ngay_Huy_Tk, gio_Huy_Tk, ten_Nguoiphutrach_Kiemtrahoso, ten_Nguoiphutrach_Kiemhoa, hs_Code, mo_Ta_Hang_Hoa, so_Luong_Hanghoa, " +
                "ma_Dvt_Hanghoa, tri_Gia_Hoa_Don, dongia_Hoadon, ma_Tiente_Hoadon, donvi_Dongia_Tiente, tri_Gia_Tinh_Thue_S, " +
                "tri_Gia_Tinh_Thue_M, dongia_Tinhthue, thuesuat_Nhapkhau, tien_Thue_Nhapkhau, xuatxu, ma_Vanbanphapquy, phanloai_Giayphep_Nk, ma_Bieuthue_Nk, ma_Miengiam_Thue ) " +
                "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";




        jdbcTemplate.batchUpdate(insertSQL, batchList, batchList.size(), (ps, e) -> {
//            if (formatterService.parseLong(e.getTkid()) != null) {
//                ps.setLong(1, formatterService.parseLong(e.getTkid()));
//            } else {
//                ps.setNull(1, Types.INTEGER);
//            }


//            ps.setLong(1,formatterService.parseLong(e.getTkid()));
            ps.setString(1, e.getSotk());
            ps.setString(2, e.getMahq());
            ps.setString(3, formatterService.trangThaiTK(e.getTrangthaitk()));

            if (formatterService.parseInteger(e.getBpkthsdt(),1) != null) {
                ps.setInt(4, formatterService.parseInteger(e.getBpkthsdt(),2) );
            } else {
                ps.setNull(4, Types.INTEGER);
            }

            ps.setString(5, e.getBptq());


            ps.setString(6, e.getPtvc());
            ps.setString(7, e.getMalh());
            ps.setDate(8, formatterService.parseSqlDate(e.getNgayDk()));
            ps.setTime(9, formatterService.parseSqlTime(e.getHourDk()) );
            ps.setDate(10, formatterService.parseSqlDate(e.getNgayThaydoiDk())  );
            ps.setTime(11, formatterService.parseSqlTime( e.getHourThaydoiDk()) );
            ps.setString(12, e.getMasothueKbhq());
            ps.setString(13, e.getTenDoanhnghiep());
            ps.setString(14, e.getSodienthoai());
            ps.setString(15, e.getTenDoanhnghiepUythac());
            ps.setString(16, e.getTenDoitacnuocngoai());
            ps.setString(17, e.getMaquocgiaDoitacnuocngoai());
            ps.setString(18, e.getVandon01());
            ps.setString(19, e.getVandon02());
            ps.setString(20, e.getVandon03());
            ps.setString(21, e.getVandon04());
            ps.setString(22, e.getVandon05());

            if (formatterService.parseInteger( e.getSoluongkienhang(),5) != null) {
                ps.setInt(23,formatterService.parseInteger( e.getSoluongkienhang(),6));
            } else {
                ps.setNull(23, Types.INTEGER);
            }



            ps.setString(24, e.getMaDvtKienhang());
            if (formatterService.parseBigDecimal( e.getGrossweight(),26) != null) {
                ps.setBigDecimal(25, formatterService.parseBigDecimal( e.getGrossweight(),26));
            } else {
                ps.setNull(25, Types.INTEGER);
            }




            ps.setString(26, e.getMaDvtGw());

            if ( formatterService.parseInteger(e.getSoluongContainer(),7) != null) {
                ps.setInt(27, formatterService.parseInteger(e.getSoluongContainer(),8));
            } else {
                ps.setNull(27, Types.INTEGER);
            }



            ps.setString(28, e.getMaDiadiemdohang());
            ps.setString(29, e.getMaDiadiemxephang());
            ps.setString(30, e.getTenPhuongtienvanchuyen());
            ps.setDate(31, formatterService.parseSqlDate( e.getNgayHangDen()));
            ps.setString(32, e.getPhuongThucThanhToan());

            if (  formatterService.parseBigDecimal(e.getTongTriGiaHoaDon(),34) != null) {
                ps.setBigDecimal(33, formatterService.parseBigDecimal(e.getTongTriGiaHoaDon(),34));
            } else {
                ps.setNull(33, Types.INTEGER);
            }


            if (  formatterService.parseBigDecimal(e.getTongTriGiaTinhThue(),35) != null) {
                ps.setBigDecimal(34, formatterService.parseBigDecimal(e.getTongTriGiaTinhThue(),35));
            } else {
                ps.setNull(34, Types.INTEGER);
            }


            if (  formatterService.parseBigDecimal(e.getTongTienThue(),36) != null) {
                ps.setBigDecimal(35, formatterService.parseBigDecimal(e.getTongTienThue(),36));

            } else {
                ps.setNull(35, Types.INTEGER);
            }




            if ( formatterService.parseInteger(e.getTongSoDonghang(),9) != null) {
                ps.setInt(36, formatterService.parseInteger(e.getTongSoDonghang(),10));
            } else {
                ps.setNull(36, Types.INTEGER);
            }

            ps.setDate(37, formatterService.parseSqlDate(e.getNgayCapPhep()));
            ps.setTime(38, formatterService.parseSqlTime(e.getGioCapPhep()));
            ps.setDate(39, formatterService.parseSqlDate(e.getNgayHoanthanhKiemtra()));
            ps.setTime(40, formatterService.parseSqlTime(e.getGioHoanthanhKiemtra()));
            ps.setDate(41, formatterService.parseSqlDate(e.getNgayHuyTk()));
            ps.setTime(42, formatterService.parseSqlTime(e.getGioHuyTk()));
            ps.setString(43, e.getTenNguoiphutrachKiemtrahoso());
            ps.setString(44, e.getTenNguoiphutrachKiemhoa());
            ps.setString(45, e.getHsCode());
            ps.setString(46, e.getMoTaHangHoa());


            if ( formatterService.parseBigDecimal(e.getSoLuongHanghoa(),12) != null) {
                ps.setBigDecimal(47, formatterService.parseBigDecimal(e.getSoLuongHanghoa(),12));
            } else {
                ps.setNull(47, Types.INTEGER);
            }





            ps.setString(48, e.getMaDvtHanghoa());


            if (  formatterService.parseBigDecimal(e.getTriGiaHoaDon(),50) != null) {
                ps.setBigDecimal(49, formatterService.parseBigDecimal(e.getTriGiaHoaDon(),50));
            } else {
                ps.setNull(49, Types.INTEGER);
            }

            if (  formatterService.parseBigDecimal(e.getDongiaHoadon(),55) != null) {
                ps.setBigDecimal(50, formatterService.parseBigDecimal(e.getDongiaHoadon(),55));
            } else {
                ps.setNull(50, Types.INTEGER);
            }




            ps.setString(51, e.getMaTienteHoadon());
            ps.setString(52, e.getDonviDongiaTiente());

            if (  formatterService.parseBigDecimal(e.getTriGiaTinhThueS(),51) != null) {
                ps.setBigDecimal(53, formatterService.parseBigDecimal(e.getTriGiaTinhThueS(),51));
            } else {
                ps.setNull(53, Types.INTEGER);
            }

            if (  formatterService.parseBigDecimal(e.getTriGiaTinhThueM(),54) != null) {
                ps.setBigDecimal(54, formatterService.parseBigDecimal(e.getTriGiaTinhThueM(),54));
            } else {
                ps.setNull(54, Types.INTEGER);
            }


            if (  formatterService.parseBigDecimal( e.getDongiaTinhthue(),53) != null) {
                ps.setBigDecimal(55, formatterService.parseBigDecimal( e.getDongiaTinhthue(),53));
            } else {
                ps.setNull(55, Types.INTEGER);
            }



            ps.setString(56, e.getThuesuatNhapkhau());


            if (  formatterService.parseBigDecimal( e.getTienThueNhapkhau(),54) != null) {
                ps.setBigDecimal(57, formatterService.parseBigDecimal( e.getTienThueNhapkhau(),54));
            } else {
                ps.setNull(57, Types.INTEGER);
            }




            ps.setString(58, e.getXuatxu());
            ps.setString(59, e.getMaVanbanphapquy());
            ps.setString(60, e.getPhanloaiGiayphepNk());
            ps.setString(61, e.getMaBieuthueNk());
            ps.setString(62, e.getMaMiengiamThue());

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