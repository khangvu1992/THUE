package com.example.thuedientu.service;

import com.example.thuedientu.model.EnityExcel;
import com.example.thuedientu.repository.ExcelRepository;
import org.apache.poi.ss.usermodel.*;
//import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.monitorjbl.xlsx.StreamingReader;
import org.apache.poi.ss.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Service
public class ExcelImportService {

    @Autowired
    private ExcelRepository excelRepository;  // ✅ đây là biến

    @Async // Phương thức này sẽ chạy bất đồng bộ
    public void importExcel(MultipartFile file) throws IOException {
        List<EnityExcel> dataList = new ArrayList<>();
//        long rowCount=0;
//
//
//        try (Workbook workbook = StreamingReader.builder()
//                .rowCacheSize(100)
//                .bufferSize(4096)
//                .open(file.getInputStream())) {
//
//            Sheet sheet = workbook.getSheetAt(0);
//             rowCount = 0;
//            Iterator<Row> rows = sheet.iterator();
//
//            while (rows.hasNext()) {
//                rows.next();
//                rowCount++;
//            }
//
//
//        }

        try(Workbook workbook = StreamingReader.builder()
                .rowCacheSize(500)
                .bufferSize(16384)
                .open(file.getInputStream())

                ){




//        Workbook workbook = new XSSFWorkbook(file.getInputStream());



        Sheet sheet = workbook.getSheetAt(0);
        Iterator<Row> rows = sheet.iterator();

        if (rows.hasNext()) rows.next(); // bỏ dòng tiêu đề

        while (rows.hasNext()) {
            Row row = rows.next();
            EnityExcel entity = new EnityExcel();



            entity.setTkid(getString(row, 0));
            entity.setSotk(getString(row, 1));
            entity.setMahq(getString(row, 2));
            entity.setTrangthaitk(getString(row, 3));
            entity.setBpkthsdt(getString(row, 4));
            entity.setBptq(getString(row, 5));
            entity.setPtvc(getString(row, 6));
            entity.setMalh(getString(row, 7));
            entity.setNgayDk(getString(row, 8));
            entity.setHourDk(getString(row, 10));
            entity.setNgayThaydoiDk(getString(row, 11));
            entity.setHourThaydoiDk(getString(row, 12));
            entity.setMasothueKbhq(getString(row, 13));
            entity.setTenDoanhnghiep(getString(row, 14));
            entity.setSodienthoai(getString(row, 15));
            entity.setTenDoanhnghiepUythac(getString(row, 16));
            entity.setTenDoitacnuocngoai(getString(row, 17));
            entity.setMaquocgiaDoitacnuocngoai(getString(row, 18));
            entity.setVandon01(getString(row, 19));
            entity.setVandon02(getString(row, 20));
            entity.setVandon03(getString(row, 21));
            entity.setVandon04(getString(row, 22));
            entity.setVandon05(getString(row, 23));
            entity.setSoluongkienhang(getString(row, 24));
            entity.setMaDvtKienhang(getString(row, 25));
            entity.setGrossweight(getString(row, 26));
            entity.setMaDvtGw(getString(row, 27));
            entity.setSoluongContainer(getString(row, 28));
            entity.setMaDiadiemdohang(getString(row, 29));
            entity.setMaDiadiemxephang(getString(row, 30));
            entity.setTenPhuongtienvanchuyen(getString(row, 31));
            entity.setNgayHangDen(getString(row, 32));
            entity.setPhuongThucThanhToan(getString(row, 33));
            entity.setTongTriGiaHoaDon(getString(row, 34));
            entity.setTongTriGiaTinhThue(getString(row, 35));
            entity.setTongTienThue(getString(row, 36));
            entity.setTongSoDonghang(getString(row, 37));
            entity.setNgayCapPhep(getString(row, 38));
            entity.setGioCapPhep(getString(row, 39));
            entity.setNgayHoanthanhKiemtra(getString(row, 40));
            entity.setGioHoanthanhKiemtra(getString(row, 41));
            entity.setNgayHuyTk(getString(row, 42));
            entity.setGioHuyTk(getString(row, 43));
            entity.setTenNguoiphutrachKiemtrahoso(getString(row, 44));
            entity.setTenNguoiphutrachKiemhoa(getString(row, 45));
            entity.setHsCode(getString(row, 46));
            entity.setMoTaHangHoa(getString(row, 47));
            entity.setSoLuongHanghoa(getString(row, 48));
            entity.setMaDvtHanghoa(getString(row, 49));
            entity.setTriGiaHoaDon(getString(row, 50));
            entity.setDongiaHoadon(getString(row, 51));
            entity.setMaTienteHoadon(getString(row, 52));
            entity.setDonviDongiaTiente(getString(row, 53));
            entity.setTriGiaTinhThueS(getString(row, 54));
            entity.setTriGiaTinhThueM(getString(row, 55));
            entity.setDongiaTinhthue(getString(row, 56));
            entity.setThuesuatNhapkhau(getString(row, 57));
            entity.setTienThueNhapkhau(getString(row, 58));
            entity.setXuatxu(getString(row, 59));
            entity.setMaVanbanphapquy(getString(row, 60));
            entity.setPhanloaiGiayphepNk(getString(row, 61));
            entity.setMaBieuthueNk(getString(row, 62));
            entity.setMaMiengiamThue(getString(row, 63));



            dataList.add(entity);
//
//            if (dataList.size() % 30000 == 0) {
//                int progress = (int) (((double) dataList.size() / rowCount) * 100);
//                System.out.println("Upload Progress: " + progress + "%");        }

        }



        excelRepository.saveAll(dataList); }
        // ✅ sử dụng instance
    }

    private String getString(Row row, int index) {
        Cell cell = row.getCell(index, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
        if (cell == null) return "";


        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                return DateUtil.isCellDateFormatted(cell)
                        ? cell.getDateCellValue().toString()
                        : new java.text.DecimalFormat("#.######").format(cell.getNumericCellValue()); // tránh số thập phân dư
            case BOOLEAN:
                return Boolean.toString(cell.getBooleanCellValue());
            case FORMULA:
                return cell.getCellFormula();
            default:
                return "";
        }
    }
}
