package com.example.thuedientu.utilExcel;

import com.example.thuedientu.model.EnityExcelJDBC;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.stereotype.Service;


@Service
public class mapEntityJDBC {

    EnityExcelJDBC entity = new EnityExcelJDBC();


    public void mapRowToEntity(Row row, EnityExcelJDBC entity) {



//        entity.setTkid(getString(row, 0));
        entity.setSotk(getString(row, 0));
        entity.setMahq(getString(row, 1));
        entity.setTrangthaitk(getString(row, 2));
        entity.setBpkthsdt(getString(row, 3));
        entity.setBptq(getString(row, 4));
        entity.setPtvc(getString(row, 5));
        entity.setMalh(getString(row, 6));
        entity.setNgayDk(getString(row, 7));
        entity.setHourDk(getString(row, 8));
        entity.setNgayThaydoiDk(getString(row, 9));
        entity.setHourThaydoiDk(getString(row, 10));
        entity.setMasothueKbhq(getString(row, 11));
        entity.setTenDoanhnghiep(getString(row, 12));
        entity.setSodienthoai(getString(row, 13));
        entity.setTenDoanhnghiepUythac(getString(row, 14));
        entity.setTenDoitacnuocngoai(getString(row, 15));
        entity.setMaquocgiaDoitacnuocngoai(getString(row, 16));
        entity.setVandon01(getString(row, 17));
        entity.setVandon02(getString(row, 18));
        entity.setVandon03(getString(row, 19));
        entity.setVandon04(getString(row, 20));
        entity.setVandon05(getString(row, 21));
        entity.setSoluongkienhang(getString(row, 22));
        entity.setMaDvtKienhang(getString(row, 23));
        entity.setGrossweight(getString(row, 24));
        entity.setMaDvtGw(getString(row, 25));
        entity.setSoluongContainer(getString(row, 26));
        entity.setMaDiadiemdohang(getString(row, 27));
        entity.setMaDiadiemxephang(getString(row, 28));
        entity.setTenPhuongtienvanchuyen(getString(row, 29));
        entity.setNgayHangDen(getString(row, 30));
        entity.setPhuongThucThanhToan(getString(row, 31));
        entity.setTongTriGiaHoaDon(getString(row, 32));
        entity.setTongTriGiaTinhThue(getString(row, 33));
        entity.setTongTienThue(getString(row, 34));
        entity.setTongSoDonghang(getString(row, 35));
        entity.setNgayCapPhep(getString(row, 36));
        entity.setGioCapPhep(getString(row, 37));
        entity.setNgayHoanthanhKiemtra(getString(row, 38));
        entity.setGioHoanthanhKiemtra(getString(row, 39));
        entity.setNgayHuyTk(getString(row, 40));
        entity.setGioHuyTk(getString(row, 41));
        entity.setTenNguoiphutrachKiemtrahoso(getString(row, 42));
        entity.setTenNguoiphutrachKiemhoa(getString(row, 43));
        entity.setHsCode(getString(row, 44));
        entity.setMoTaHangHoa(getString(row, 45));
        entity.setSoLuongHanghoa(getString(row, 46));
        entity.setMaDvtHanghoa(getString(row, 47));
        entity.setTriGiaHoaDon(getString(row, 48));
        entity.setDongiaHoadon(getString(row, 49));
        entity.setMaTienteHoadon(getString(row, 50));
        entity.setDonviDongiaTiente(getString(row, 51));
        entity.setTriGiaTinhThueS(getString(row, 52));
        entity.setTriGiaTinhThueM(getString(row, 53));
        entity.setDongiaTinhthue(getString(row, 54));
        entity.setThuesuatNhapkhau(getString(row, 55));
        entity.setTienThueNhapkhau(getString(row, 56));
        entity.setXuatxu(getString(row, 57));
        entity.setMaVanbanphapquy(getString(row, 58));
        entity.setPhanloaiGiayphepNk(getString(row, 59));
        entity.setMaBieuthueNk(getString(row, 60));
        entity.setMaMiengiamThue(getString(row, 61));



    }

    // Helper method để lấy giá trị String từ cột trong row
    private String getString(Row row, int index) {
        Cell cell = row.getCell(index);
        if (cell == null) return "";
        return cell.getStringCellValue();
    }
}

