package com.example.thuedientu.util;

import com.example.thuedientu.model.ExportEntity;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class mapEntityJDBCClone {

    @Autowired
    private ExcelDataFormatterService formatterService;

    ExportEntity entity = new ExportEntity();


    public void mapRowToEntity(Row row, ExportEntity entity) {

        entity.setSoToKhai(getString(row, 0));
        entity.setMaChiCucHaiQuanTaoMoi(getString(row, 1));
        entity.setMaPhanLoaiTrangThaiSauCung(formatterService.trangThaiTK(getString(row, 2)));
        entity.setBoPhanKiemTraHoSoDauTien(getString(row, 3));
        entity.setBoPhanKiemTraHoSoSauCung(getString(row, 4));
        entity.setPhuongThucVanChuyen(getString(row, 5));
        entity.setMaLoaiHinh(getString(row, 6));

        entity.setNgayDangKy( formatterService.parseSqlDate(getString(row, 7)) );
        entity.setGioDangKy( formatterService.parseSqlTime(getString(row, 8)) );
        entity.setNgayThayDoiDangKy(formatterService.parseSqlDate(getString(row, 9)) );
        entity.setGioThayDoiDangKy( formatterService.parseSqlTime(getString(row, 10)));

        entity.setMaNguoiXuatKhau(getString(row, 11));
        entity.setTenNguoiXuatKhau(getString(row, 12));
        entity.setTenNguoiNhapKhau(getString(row, 13));
        entity.setMaNuoc(getString(row, 14));
        entity.setSoVanDon(getString(row, 15));

        entity.setSoLuong(formatterService.parseBigDecimal( getString(row, 16),1) );
        entity.setMaDonViTinh(getString(row, 17));
        entity.setTongTrongLuongHangGross(formatterService.parseBigDecimal(getString(row, 18),2));
        entity.setMaDonViTinhTrongLuongGross(getString(row, 19));
        entity.setMaDiaDiemNhanHangCuoiCung(getString(row, 20));
        entity.setMaDiaDiemXepHang(getString(row, 21));

        entity.setTongTriGiaHoaDon(formatterService.parseBigDecimal(getString(row, 22),3));
        entity.setTongTriGiaTinhThue(formatterService.parseBigDecimal(getString(row, 23),4));
        entity.setTongSoTienThueXuatKhau(formatterService.parseBigDecimal(getString(row, 24),8));
        entity.setTongSoDongHangCuaToKhai(formatterService.parseBigDecimal(getString(row, 25),6));
        entity.setPhanGhiChu(getString(row, 26));

        entity.setNgayHoanThanhKiemTra(formatterService.parseSqlDate(getString(row, 27)));
        entity.setGioHoanThanhKiemTra( formatterService.parseSqlTime(getString(row, 28)));
        entity.setNgayHuyKhaiBaoHaiQuan(formatterService.parseSqlDate(getString(row, 29)));
        entity.setGioHuyKhaiBaoHaiQuan( formatterService.parseSqlTime(getString(row, 30)));

        entity.setTenNguoiPhuTrachKiemTraHoSo(getString(row, 31));
        entity.setTenNguoiPhuTrachKiemHoa(getString(row, 32));

        entity.setMaSoHangHoa(getString(row, 33));
        entity.setMoTaHangHoa(getString(row, 34));
        entity.setSoLuong1(formatterService.parseBigDecimal(getString(row, 35),9));

        entity.setTriGiaHoaDon(formatterService.parseBigDecimal(getString(row, 36),89));
        entity.setDonGiaHoaDon(formatterService.parseBigDecimal(getString(row, 37),99));
        entity.setMaDongTienDonGiaHoaDon(getString(row, 38));

        entity.setTriGiaTinhThueS(formatterService.parseBigDecimal(getString(row, 39),8));
        entity.setTriGiaTinhThueM(formatterService.parseBigDecimal(getString(row, 40),99));
        entity.setDonGiaTinhThue(formatterService.parseBigDecimal(getString(row, 41),9));

        entity.setThueSuatThueXuatKhau(getString(row, 42));
        entity.setPhanLoaiNhapThueSuatThueXuatKhau(getString(row, 43));
        entity.setSoTienThueXuatKhau(formatterService.parseBigDecimal(getString(row, 44),7));

        entity.setMaVanBanPhapQuyKhac1(getString(row, 45));
        entity.setMaMienGiamKhongChiuThueXuatKhau(getString(row, 46));



    }

    // Helper method để lấy giá trị String từ cột trong row
    private String getString(Row row, int index) {
        Cell cell = row.getCell(index);
        if (cell == null) return "";
        return cell.getStringCellValue();
    }
}

