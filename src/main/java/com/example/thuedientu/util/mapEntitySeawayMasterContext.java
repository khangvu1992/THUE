package com.example.thuedientu.util;

import com.example.thuedientu.model.ExportEntity;
import com.example.thuedientu.model.SeawayMasterBillEntity;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class mapEntitySeawayMasterContext {

    @Autowired
    private ExcelDataFormatterService formatterService;

    ExportEntity entity = new ExportEntity();


//    public void mapRowToEntity(Row row, SeawayMasterBillEntity entity) {
//
//        entity.setSoKhaiBao(getString(row, 0));
//        entity.setSoHoSo(getString(row, 1));
//        entity.setLoaiHoSo(getString(row, 2));
//        entity.setArrivalDate(formatterService.parseSqlTimestamp(getString(row, 3)));
//        entity.setCangTiepNhan(getString(row, 4));
//        entity.setNgayGui(formatterService.parseSqlTimestamp(getString(row, 5)));
//        entity.setTenTau(getString(row, 6));
//        entity.setSoIMO(getString(row, 7));
//        entity.setHangTau(getString(row, 8));
//        entity.setNgayTauDenRoi(formatterService.parseSqlTimestamp(getString(row, 9)));
//        entity.setNgayDenRoi(formatterService.parseSqlTimestamp(getString(row, 10)));
//        entity.setCangRoiCuoiCungCangDich(getString(row, 11));
//        entity.setConsignee(getString(row, 12));
//        entity.setConsigner(getString(row, 13));
//        entity.setNotificatedParty(getString(row, 14));
//        entity.setNotificatedParty2(getString(row, 15));
//        entity.setMasterBillNo(getString(row, 16));
//        entity.setContNumber(getString(row, 17));
//        entity.setContSealNumber(getString(row, 18));
//        entity.setGoodDescription(getString(row, 19));
//        entity.setCangXepHangGoc(getString(row, 20));
//        entity.setCangXepHang(getString(row, 21));
//        entity.setCangDoHang(getString(row, 22));
//        entity.setCangDich(getString(row, 23));
//        entity.setTenCangDich(getString(row, 24));
//        entity.setDiaDiemDoHang(getString(row, 25));
//        entity.setNetWeight(formatterService.parseBigDecimal(getString(row, 26), 2));
//        entity.setGrossWeight(formatterService.parseBigDecimal(getString(row, 27), 2));
//        entity.setDemension(formatterService.parseBigDecimal(getString(row, 28), 2));
//
//
//    }
//
//    // Helper method để lấy giá trị String từ cột trong row
//    private String getString(Row row, int index) {
//        Cell cell = row.getCell(index);
//        if (cell == null) return "";
//        return cell.getStringCellValue();
//    }

    public void mapCsvRowToEntity(String[] tokens, SeawayMasterBillEntity entity) {

        if (tokens.length < 29) {
            throw new Error("Insufficient tokens. The length must be at least 29.");
        }

        entity.setSoKhaiBao(getString(tokens, 0));
        entity.setSoHoSo(getString(tokens, 1));
        entity.setLoaiHoSo(getString(tokens, 2));
        entity.setArrivalDate(formatterService.parseSqlTimestamp(getString(tokens, 3),tokens));
        entity.setCangTiepNhan(getString(tokens, 4));
        entity.setNgayGui(formatterService.parseSqlTimestamp(getString(tokens, 5),tokens));
        entity.setTenTau(getString(tokens, 6));
        entity.setSoIMO(getString(tokens, 7));
        entity.setHangTau(getString(tokens, 8));
        entity.setNgayTauDenRoi(formatterService.parseSqlTimestamp(getString(tokens, 9),tokens));
        entity.setNgayDenRoi(formatterService.parseSqlTimestamp(getString(tokens, 10),tokens));
        entity.setCangRoiCuoiCungCangDich(getString(tokens, 11));
        entity.setConsignee(getString(tokens, 12));
        entity.setConsigner(getString(tokens, 13));
        entity.setNotificatedParty(getString(tokens, 14));
        entity.setNotificatedParty2(getString(tokens, 15));
        entity.setMasterBillNo(getString(tokens, 16));
        entity.setContNumber(getString(tokens, 17));
        entity.setContSealNumber(getString(tokens, 18));
        entity.setGoodDescription(getString(tokens, 19));
        entity.setCangXepHangGoc(getString(tokens, 20));
        entity.setCangXepHang(getString(tokens, 21));
        entity.setCangDoHang(getString(tokens, 22));
        entity.setCangDich(getString(tokens, 23));
        entity.setTenCangDich(getString(tokens, 24));
        entity.setDiaDiemDoHang(getString(tokens, 25));
        entity.setNetWeight(formatterService.parseBigDecimal(getString(tokens, 26), 2));
        entity.setGrossWeight(formatterService.parseBigDecimal(getString(tokens, 27), 2));
        entity.setDemension(formatterService.parseBigDecimal(getString(tokens, 28), 2));
    }

    private String getString(String[] tokens, int index) {
        if (tokens.length <= index || tokens[index] == null) {
            return "";
        }
        return tokens[index].trim();
    }
}

