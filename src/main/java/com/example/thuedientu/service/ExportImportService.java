package com.example.thuedientu.service;

import com.example.thuedientu.model.ExportEntity;
import com.example.thuedientu.repository.ExportRepository;
import com.monitorjbl.xlsx.StreamingReader;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
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

    private final ExportRepository exportRepository;

    @Transactional(rollbackFor = Exception.class)
    public void importExcelFile(File file) {
        List<ExportEntity> entities = new ArrayList<>();

        try (InputStream is = new FileInputStream(file);
             Workbook workbook = StreamingReader.builder()
                     .rowCacheSize(100)      // tùy chỉnh
                     .bufferSize(4096)       // tùy chỉnh
                     .open(is)) {

            Sheet sheet = workbook.getSheetAt(0);
            int rowIndex = 0;

            for (Row row : sheet) {
                if (rowIndex++ == 0) continue; // Bỏ qua header

                try {
                    ExportEntity entity = mapRowToEntity(row);
                    entities.add(entity);
                } catch (Exception e) {
                    throw new RuntimeException("Lỗi tại dòng " + (rowIndex) + ": " + e.getMessage(), e);
                }
            }

            // Save toàn bộ - rollback nếu có lỗi
            exportRepository.saveAll(entities);

        } catch (IOException e) {
            throw new RuntimeException("Không thể đọc file Excel: " + e.getMessage(), e);
        }
    }

    private ExportEntity mapRowToEntity(Row row) {
        ExportEntity entity = new ExportEntity();

        entity.setSoToKhai(getCellValue(row.getCell(0)));
        entity.setMaChiCucHaiQuanTaoMoi(getCellValue(row.getCell(1)));
        entity.setMaPhanLoaiTrangThaiSauCung(getCellValue(row.getCell(2)));
        entity.setBoPhanKiemTraHoSoDauTien(getCellValue(row.getCell(3)));
        entity.setBoPhanKiemTraHoSoSauCung(getCellValue(row.getCell(4)));
        entity.setPhuongThucVanChuyen(getCellValue(row.getCell(5)));
        entity.setMaLoaiHinh(getCellValue(row.getCell(6)));
        entity.setNgayDangKy(getCellValue(row.getCell(7)));
        entity.setGioDangKy(getCellValue(row.getCell(8)));
        entity.setNgayThayDoiDangKy(getCellValue(row.getCell(9)));
        entity.setGioThayDoiDangKy(getCellValue(row.getCell(10)));
        entity.setMaNguoiXuatKhau(getCellValue(row.getCell(11)));
        entity.setTenNguoiXuatKhau(getCellValue(row.getCell(12)));
        entity.setTenNguoiNhapKhau(getCellValue(row.getCell(13)));
        entity.setMaNuoc(getCellValue(row.getCell(14)));
        entity.setSoVanDon(getCellValue(row.getCell(15)));
        entity.setSoLuong(getCellValue(row.getCell(16)));
        entity.setMaDonViTinh(getCellValue(row.getCell(17)));
        entity.setTongTrongLuongHangGross(getCellValue(row.getCell(18)));
        entity.setMaDonViTinhTrongLuongGross(getCellValue(row.getCell(19)));
        entity.setMaDiaDiemNhanHangCuoiCung(getCellValue(row.getCell(20)));
        entity.setMaDiaDiemXepHang(getCellValue(row.getCell(21)));
        entity.setTongTriGiaHoaDon(getCellValue(row.getCell(22)));
        entity.setTongTriGiaTinhThue(getCellValue(row.getCell(23)));
        entity.setTongSoTienThueXuatKhau(getCellValue(row.getCell(24)));
        entity.setTongSoDongHangCuaToKhai(getCellValue(row.getCell(25)));
        entity.setPhanGhiChu(getCellValue(row.getCell(26)));
        entity.setNgayHoanThanhKiemTra(getCellValue(row.getCell(27)));
        entity.setGioHoanThanhKiemTra(getCellValue(row.getCell(28)));
        entity.setNgayHuyKhaiBaoHaiQuan(getCellValue(row.getCell(29)));
        entity.setGioHuyKhaiBaoHaiQuan(getCellValue(row.getCell(30)));
        entity.setTenNguoiPhuTrachKiemTraHoSo(getCellValue(row.getCell(31)));
        entity.setTenNguoiPhuTrachKiemHoa(getCellValue(row.getCell(32)));
        entity.setMaSoHangHoa(getCellValue(row.getCell(33)));
        entity.setMoTaHangHoa(getCellValue(row.getCell(34)));
        entity.setSoLuong1(getCellValue(row.getCell(35)));
        entity.setTriGiaHoaDon(getCellValue(row.getCell(36)));
        entity.setDonGiaHoaDon(getCellValue(row.getCell(37)));
        entity.setMaDongTienDonGiaHoaDon(getCellValue(row.getCell(38)));
        entity.setTriGiaTinhThueS(getCellValue(row.getCell(39)));
        entity.setTriGiaTinhThueM(getCellValue(row.getCell(40)));
        entity.setDonGiaTinhThue(getCellValue(row.getCell(41)));
        entity.setThueSuatThueXuatKhau(getCellValue(row.getCell(42)));
        entity.setPhanLoaiNhapThueSuatThueXuatKhau(getCellValue(row.getCell(43)));
        entity.setSoTienThueXuatKhau(getCellValue(row.getCell(44)));
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
