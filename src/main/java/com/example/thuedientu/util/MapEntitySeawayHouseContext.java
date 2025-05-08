package com.example.thuedientu.util;

import com.example.thuedientu.model.SeawayHouseBillEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class MapEntitySeawayHouseContext {

    @Autowired
    private ExcelDataFormatterService formatterService;

    public void mapCsvRowToEntity(String[] tokens, SeawayHouseBillEntity entity) {
        if (tokens.length < 29) {
            throw new Error("Insufficient tokens. The length must be at least 29.");
        }

        // Mapping CSV tokens to the entity fields
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
        entity.setBillNumber(getString(tokens, 12));
        entity.setHbConsigner(getString(tokens, 13));
        entity.setHbConsignee(getString(tokens, 14));
        entity.setHbNotificatedParty(getString(tokens, 15));
        entity.setHbNotificatedParty2(getString(tokens, 16));
        entity.setDateOfBill(formatterService.parseSqlTimestamp(getString(tokens, 17),tokens));
        entity.setDepartureDate(formatterService.parseSqlTimestamp(getString(tokens, 18),tokens));
        entity.setPortNameOfTranship(getString(tokens, 19));
        entity.setPortNameOfDestination(getString(tokens, 20));
        entity.setPortNameOfLoad(getString(tokens, 21));
        entity.setPortNameOfUnload(getString(tokens, 22));
        entity.setPlaceOfDelivery(getString(tokens, 23));
        entity.setMoTaHangHoa(getString(tokens, 24));
        entity.setContNumber(getString(tokens, 25));
        entity.setContSealNumber(getString(tokens, 26));
        entity.setNumberOfPackage(formatterService.parseInteger(getString(tokens, 27),3));
        entity.setCargoType(getString(tokens, 28));
    }

    private String getString(String[] tokens, int index) {
        if (tokens.length <= index || tokens[index] == null) {
            return "";
        }
        return tokens[index].trim();
    }
}


