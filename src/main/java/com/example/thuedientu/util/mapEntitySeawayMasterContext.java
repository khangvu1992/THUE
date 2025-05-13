package com.example.thuedientu.util;

import com.example.thuedientu.model.ExportEntity;
import com.example.thuedientu.model.SeawayMasterBillEntity;
import org.apache.commons.csv.CSVRecord;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class mapEntitySeawayMasterContext {

    @Autowired
    private ExcelDataFormatterService formatterService;

    ExportEntity entity = new ExportEntity();



    public void mapCsvRowToEntity(CSVRecord tokens, SeawayMasterBillEntity entity) {


        entity.setSoKhaiBao(getString(tokens, 0));
        entity.setSoHoSo(getString(tokens, 1));
        entity.setLoaiHoSo(getString(tokens, 2));
        entity.setArrivalDate(formatterService.parseSqlTimestamp(getString(tokens, 3),tokens.toList().toArray(new String[0])));
        entity.setCangTiepNhan(getString(tokens, 4));
        entity.setNgayGui(formatterService.parseSqlTimestamp(getString(tokens, 5),tokens.toList().toArray(new String[0])));
        entity.setTenTau(getString(tokens, 6));
        entity.setSoIMO(getString(tokens, 7));
        entity.setHangTau(getString(tokens, 8));
        entity.setNgayTauDenRoi(formatterService.parseSqlTimestamp(getString(tokens, 9),tokens.toList().toArray(new String[0])));
        entity.setNgayDenRoi(formatterService.parseSqlTimestamp(getString(tokens, 10),tokens.toList().toArray(new String[0])));
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

    private String getString(CSVRecord tokens, int index) {
        if (index >= tokens.size()) {
            System.err.println("⚠️ Thiếu cột ở index " + index + " trong dòng: " + tokens.toString());
            return "";
        }
        String value = tokens.get(index);
        return value != null ? value.trim() : "";
    }
}

