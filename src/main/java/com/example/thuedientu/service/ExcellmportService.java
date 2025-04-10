//package com.example.thuedientu.service;
//
//import com.example.thuedientu.model.EnityExcel;
//import com.example.thuedientu.repository.EnityExcelRepository;
//import com.example.thuedientu.repository.EnityExcelRepository;
//import org.apache.poi.ss.usermodel.*;
////import org.apache.poi.xssf.usermodel.XSSFWorkbook;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.scheduling.annotation.Async;
//import org.springframework.scheduling.annotation.EnableAsync;
//import org.springframework.stereotype.Service;
//import org.springframework.web.multipart.MultipartFile;
//import com.monitorjbl.xlsx.StreamingReader;
//import org.apache.poi.ss.usermodel.*;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//import org.apache.poi.ss.usermodel.Row;
//import org.apache.poi.ss.usermodel.Cell;
//import org.apache.poi.ss.usermodel.DateUtil;
//
//
//import java.io.File;
//import java.io.FileInputStream;
//import java.io.IOException;
//import java.io.InputStream;
//import java.util.ArrayList;
//import java.util.Iterator;
//import java.util.List;
//
//@Service
//public class ExcellmportService {
//
//
//    @Autowired
//    private EnityExcelRepository excelRepository;
//
//    @Async
//    public void importAsync(File file) {
//        List<EnityExcel> dataList = new ArrayList<>();
//        int cout=0;
//
//        try (InputStream is = new FileInputStream(file);
//             Workbook workbook = StreamingReader.builder()
//                     .rowCacheSize(100)  // Đọc nhiều rows hơn để tối ưu bộ nhớ
//                     .bufferSize(4096)   // Tăng buffer size để xử lý nhanh hơn
//                     .open(is)) {
//
//            Sheet sheet = workbook.getSheetAt(0); // Đọc sheet đầu tiên
//            Iterator<Row> rows = sheet.iterator();
//
//            // Bỏ qua dòng tiêu đề
//            if (rows.hasNext()) {
//                rows.next(); // Bỏ qua dòng tiêu đề
//            }
//
//            while (rows.hasNext()) {
//                Row row = rows.next();
//                EnityExcel entity = new EnityExcel();
//
//                // Ánh xạ dữ liệu từ các cột của row vào entity
//                mapRowToEntity(row, entity);
//
//                // Thêm entity vào danh sách
//                dataList.add(entity);
//                cout++;
//
//                if (cout % 10000 == 0) {
//                    int progress = (cout / 10000);  // Every 10,000 iterations, you want the progress
//                    System.out.println("Progress: " + progress + "%");
//                }
//            }
//
//            // Lưu tất cả dữ liệu vào DB
//            excelRepository.saveAll(dataList);
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        } finally {
//            // Xoá file tạm sau khi xử lý xong
//            cout=0;
//            file.delete();
//        }
//    }
//
//    // Hàm ánh xạ các cột từ row vào entity
//    private void mapRowToEntity(Row row, EnityExcel entity) {
//        entity.setTkid(getString(row, 0));
//        entity.setSotk(getString(row, 1));
//        entity.setMahq(getString(row, 2));
//        entity.setTrangthaitk(getString(row, 3));
//        entity.setBpkthsdt(getString(row, 4));
//        entity.setBptq(getString(row, 5));
//        entity.setPtvc(getString(row, 6));
//        entity.setMalh(getString(row, 7));
//        entity.setNgayDk(getString(row, 8));
//        entity.setHourDk(getString(row, 10));
//        entity.setNgayThaydoiDk(getString(row, 11));
//        entity.setHourThaydoiDk(getString(row, 12));
//        entity.setMasothueKbhq(getString(row, 13));
//        entity.setTenDoanhnghiep(getString(row, 14));
//        entity.setSodienthoai(getString(row, 15));
//        entity.setTenDoanhnghiepUythac(getString(row, 16));
//        entity.setTenDoitacnuocngoai(getString(row, 17));
//        entity.setMaquocgiaDoitacnuocngoai(getString(row, 18));
//        entity.setVandon01(getString(row, 19));
//        entity.setVandon02(getString(row, 20));
//        entity.setVandon03(getString(row, 21));
//        entity.setVandon04(getString(row, 22));
//        entity.setVandon05(getString(row, 23));
//        entity.setSoluongkienhang(getString(row, 24));
//        entity.setMaDvtKienhang(getString(row, 25));
//        entity.setGrossweight(getString(row, 26));
//        entity.setMaDvtGw(getString(row, 27));
//        entity.setSoluongContainer(getString(row, 28));
//        entity.setMaDiadiemdohang(getString(row, 29));
//        entity.setMaDiadiemxephang(getString(row, 30));
//        entity.setTenPhuongtienvanchuyen(getString(row, 31));
//        entity.setNgayHangDen(getString(row, 32));
//        entity.setPhuongThucThanhToan(getString(row, 33));
//        entity.setTongTriGiaHoaDon(getString(row, 34));
//        entity.setTongTriGiaTinhThue(getString(row, 35));
//        entity.setTongTienThue(getString(row, 36));
//        entity.setTongSoDonghang(getString(row, 37));
//        entity.setNgayCapPhep(getString(row, 38));
//        entity.setGioCapPhep(getString(row, 39));
//        entity.setNgayHoanthanhKiemtra(getString(row, 40));
//        entity.setGioHoanthanhKiemtra(getString(row, 41));
//        entity.setNgayHuyTk(getString(row, 42));
//        entity.setGioHuyTk(getString(row, 43));
//        entity.setTenNguoiphutrachKiemtrahoso(getString(row, 44));
//        entity.setTenNguoiphutrachKiemhoa(getString(row, 45));
//        entity.setHsCode(getString(row, 46));
//        entity.setMoTaHangHoa(getString(row, 47));
//        entity.setSoLuongHanghoa(getString(row, 48));
//        entity.setMaDvtHanghoa(getString(row, 49));
//        entity.setTriGiaHoaDon(getString(row, 50));
//        entity.setDongiaHoadon(getString(row, 51));
//        entity.setMaTienteHoadon(getString(row, 52));
//        entity.setDonviDongiaTiente(getString(row, 53));
//        entity.setTriGiaTinhThueS(getString(row, 54));
//        entity.setTriGiaTinhThueM(getString(row, 55));
//        entity.setDongiaTinhthue(getString(row, 56));
//        entity.setThuesuatNhapkhau(getString(row, 57));
//        entity.setTienThueNhapkhau(getString(row, 58));
//        entity.setXuatxu(getString(row, 59));
//        entity.setMaVanbanphapquy(getString(row, 60));
//        entity.setPhanloaiGiayphepNk(getString(row, 61));
//        entity.setMaBieuthueNk(getString(row, 62));
//        entity.setMaMiengiamThue(getString(row, 63));
//    }
//
//    // Helper method để lấy giá trị String từ cột trong row
//    private String getString(Row row, int index) {
//        Cell cell = row.getCell(index);
//        if (cell == null) return "";
//        return cell.getStringCellValue();
//    }
//}
//
//
