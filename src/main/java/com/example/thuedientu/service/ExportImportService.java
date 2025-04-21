package com.example.thuedientu.service;

import com.example.thuedientu.model.ExportEntity;
import com.example.thuedientu.model.HashFile;
import com.example.thuedientu.repository.ExportRepository;
import com.example.thuedientu.repository.FileRepository;
import com.example.thuedientu.util.ExcelDataFormatterService;
import com.example.thuedientu.util.ProgressWebSocketSender;
import com.monitorjbl.xlsx.StreamingReader;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ExportImportService {

    @Autowired private FileRepository fileRepository;


    @Autowired
    ProgressWebSocketSender progressWebSocketSender;

    @Autowired
    private ExcelDataFormatterService formatterService;

    private final ExportRepository exportRepository;

    @Transactional(rollbackFor = Exception.class)
    public void importExcelFile(File file, HashFile hashFile) {
        List<ExportEntity> entities = new ArrayList<>();

        try (InputStream is = new FileInputStream(file);
             Workbook workbook = StreamingReader.builder()
                     .rowCacheSize(100)      // tùy chỉnh
                     .bufferSize(4096)       // tùy chỉnh
                     .open(is)) {

            Sheet sheet = workbook.getSheetAt(0);
            int rowIndex = 0;
            int count=0;
            for (Row row : sheet) {
                if (rowIndex++ == 0) continue; // Bỏ qua header
                count++;
                if (count % 10000 == 0) {
//                    fileQueueManager.logWaitingFiles();
                    int progress = (int) count*100 / 1048576 ;

                    progressWebSocketSender.sendProgress1(hashFile.getFileHash(),hashFile.getFilename(), progress,false);

//                    System.out.println("Progress: " + count);
                }

                try {
                    ExportEntity entity = mapRowToEntity(row);
                    entities.add(entity);
                } catch (Exception e) {
                    throw new RuntimeException("Lỗi tại dòng " + (rowIndex) + ": " + e.getMessage(), e);
                }
            }

            // Save toàn bộ - rollback nếu có lỗi
            exportRepository.saveAll(entities);
            file.delete();
            fileRepository.save(hashFile);
            entities.clear();
            System.gc();
            progressWebSocketSender.sendProgress1(hashFile.getFileHash(),hashFile.getFilename(), 100,true);
            System.out.println("✅ ✅ ✅ ✅ ✅  Đã import 1 file xuất khẩu xong.");




        } catch (IOException e) {
            throw new RuntimeException("Không thể đọc file Excel: " + e.getMessage(), e);
        }
    }

    private ExportEntity mapRowToEntity(Row row) {
        ExportEntity entity = new ExportEntity();

        entity.setSoToKhai(getCellValue(row.getCell(0)));
        entity.setMaChiCucHaiQuanTaoMoi(getCellValue(row.getCell(1)));
        entity.setMaPhanLoaiTrangThaiSauCung(formatterService.trangThaiTK(getCellValue(row.getCell(2))));
        entity.setBoPhanKiemTraHoSoDauTien(getCellValue(row.getCell(3)));
        entity.setBoPhanKiemTraHoSoSauCung(getCellValue(row.getCell(4)));
        entity.setPhuongThucVanChuyen(getCellValue(row.getCell(5)));
        entity.setMaLoaiHinh(getCellValue(row.getCell(6)));
        entity.setNgayDangKy( formatterService.parseSqlDate( getCellValue(row.getCell(7))) );
        entity.setGioDangKy(  formatterService.parseSqlTime(getCellValue(row.getCell(8))) );
        entity.setNgayThayDoiDangKy(formatterService.parseSqlDate(getCellValue(row.getCell(9))));
        entity.setGioThayDoiDangKy(formatterService.parseSqlTime(getCellValue(row.getCell(10))));
        entity.setMaNguoiXuatKhau(getCellValue(row.getCell(11)));
        entity.setTenNguoiXuatKhau(getCellValue(row.getCell(12)));
        entity.setTenNguoiNhapKhau(getCellValue(row.getCell(13)));
        entity.setMaNuoc(getCellValue(row.getCell(14)));
        entity.setSoVanDon(getCellValue(row.getCell(15)));
        entity.setSoLuong(formatterService.parseBigDecimal(getCellValue(row.getCell(16)),1));
        entity.setMaDonViTinh(getCellValue(row.getCell(17)));
        entity.setTongTrongLuongHangGross(formatterService.parseBigDecimal(getCellValue(row.getCell(18)),2));
        entity.setMaDonViTinhTrongLuongGross(getCellValue(row.getCell(19)));
        entity.setMaDiaDiemNhanHangCuoiCung(getCellValue(row.getCell(20)));
        entity.setMaDiaDiemXepHang(getCellValue(row.getCell(21)));
        entity.setTongTriGiaHoaDon(formatterService.parseBigDecimal(getCellValue(row.getCell(22)),3));
        entity.setTongTriGiaTinhThue(formatterService.parseBigDecimal(getCellValue(row.getCell(23)),4));
        entity.setTongSoTienThueXuatKhau(formatterService.parseBigDecimal(getCellValue(row.getCell(24)),5));
        entity.setTongSoDongHangCuaToKhai(formatterService.parseBigDecimal(getCellValue(row.getCell(25)),6));
        entity.setPhanGhiChu(getCellValue(row.getCell(26)));
        entity.setNgayHoanThanhKiemTra( formatterService.parseSqlDate( getCellValue(row.getCell(27))) );
        entity.setGioHoanThanhKiemTra(formatterService.parseSqlTime(getCellValue(row.getCell(28))));
        entity.setNgayHuyKhaiBaoHaiQuan( formatterService.parseSqlDate( getCellValue(row.getCell(29))));
        entity.setGioHuyKhaiBaoHaiQuan(formatterService.parseSqlTime(getCellValue(row.getCell(30))));
        entity.setTenNguoiPhuTrachKiemTraHoSo(getCellValue(row.getCell(31)));
        entity.setTenNguoiPhuTrachKiemHoa(getCellValue(row.getCell(32)));
        entity.setMaSoHangHoa(getCellValue(row.getCell(33)));
        entity.setMoTaHangHoa(getCellValue(row.getCell(34)));
        entity.setSoLuong1(formatterService.parseBigDecimal(getCellValue(row.getCell(35)),7));
        entity.setTriGiaHoaDon(formatterService.parseBigDecimal(getCellValue(row.getCell(25)),8));
        entity.setDonGiaHoaDon(formatterService.parseBigDecimal(getCellValue(row.getCell(37)),9));
        entity.setMaDongTienDonGiaHoaDon(getCellValue(row.getCell(38)));
        entity.setTriGiaTinhThueS(formatterService.parseBigDecimal(getCellValue(row.getCell(39)),10));
        entity.setTriGiaTinhThueM(formatterService.parseBigDecimal(getCellValue(row.getCell(40)),11));
        entity.setDonGiaTinhThue(formatterService.parseBigDecimal(getCellValue(row.getCell(41)),12));
        entity.setThueSuatThueXuatKhau(getCellValue(row.getCell(42)));
        entity.setPhanLoaiNhapThueSuatThueXuatKhau(getCellValue(row.getCell(43)));
        entity.setSoTienThueXuatKhau(formatterService.parseBigDecimal(getCellValue(row.getCell(44)),13));
        entity.setMaVanBanPhapQuyKhac1(getCellValue(row.getCell(45)));
        entity.setMaMienGiamKhongChiuThueXuatKhau(getCellValue(row.getCell(46)));

        return entity;
    }

    private String getCellValue(Cell cell) {
        if (cell == null) return null;
        return switch (cell.getCellType()) {
            case STRING -> cell.getStringCellValue().trim();
            case NUMERIC -> String.valueOf(cell.getNumericCellValue());
            case BOOLEAN -> String.valueOf(cell.getBooleanCellValue());
            case FORMULA -> cell.getCellFormula();
            default -> null;
        };
    }
}
