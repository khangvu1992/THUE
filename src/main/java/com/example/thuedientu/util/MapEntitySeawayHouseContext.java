package com.example.thuedientu.util;

import com.example.thuedientu.model.SeawayHouseBillEntity;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class MapEntitySeawayHouseContext {

    @Autowired
    private ExcelDataFormatterService formatterService;

    public void mapCsvRowToEntity(CSVRecord record, SeawayHouseBillEntity entity) {
        // if (record.size() < 29) {
        //     throw new IllegalArgumentException("Insufficient columns: expected at least 29, but got " + record.size());
        // }

        entity.setSoKhaiBao(getString(record, 0));
        entity.setSoHoSo(getString(record, 1));
        entity.setLoaiHoSo(getString(record, 2));
        entity.setArrivalDate(formatterService.parseSqlTimestamp(getString(record, 3), record.toList().toArray(new String[0])));
        entity.setCangTiepNhan(getString(record, 4));
        entity.setNgayGui(formatterService.parseSqlTimestamp(getString(record, 5), record.toList().toArray(new String[0])));
        entity.setTenTau(getString(record, 6));
        entity.setSoIMO(getString(record, 7));
        entity.setHangTau(getString(record, 8));
        entity.setNgayTauDenRoi(formatterService.parseSqlTimestamp(getString(record, 9), record.toList().toArray(new String[0])));
        entity.setNgayDenRoi(formatterService.parseSqlTimestamp(getString(record, 10), record.toList().toArray(new String[0])));
        entity.setCangRoiCuoiCungCangDich(getString(record, 11));
        entity.setBillNumber(getString(record, 12));
        entity.setHbConsigner(getString(record, 13));
        entity.setHbConsignee(getString(record, 14));
        entity.setHbNotificatedParty(getString(record, 15));
        entity.setHbNotificatedParty2(getString(record, 16));
        entity.setDateOfBill(formatterService.parseSqlTimestamp(getString(record, 17), record.toList().toArray(new String[0])));
        entity.setDepartureDate(formatterService.parseSqlTimestamp(getString(record, 18), record.toList().toArray(new String[0])));
        entity.setPortNameOfTranship(getString(record, 19));
        entity.setPortNameOfDestination(getString(record, 20));
        entity.setPortNameOfLoad(getString(record, 21));
        entity.setPortNameOfUnload(getString(record, 22));
        entity.setPlaceOfDelivery(getString(record, 23));
        entity.setMoTaHangHoa(getString(record, 24));
        entity.setContNumber(getString(record, 25));
        entity.setContSealNumber(getString(record, 26));
        entity.setNumberOfPackage(formatterService.parseInteger(getString(record, 27), 3));
        entity.setCargoType(getString(record, 28));
    }


    private String getString(CSVRecord record, int index) {
        if (index >= record.size()) {
            System.err.println("⚠️ Thiếu cột ở index " + index + " trong dòng: " + record.toString());
            return "";
        }
        String value = record.get(index);
        return value != null ? value.trim() : "";
    }
}


