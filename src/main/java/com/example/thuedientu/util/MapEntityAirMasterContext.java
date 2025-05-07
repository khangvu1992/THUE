package com.example.thuedientu.util;

import com.example.thuedientu.model.AirMasterBillEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MapEntityAirMasterContext {

    @Autowired
    private ExcelDataFormatterService formatterService;

    public void mapCsvRowToEntity(String[] tokens, AirMasterBillEntity entity) {
        if (tokens.length < 24) {
            throw new IllegalArgumentException("Insufficient tokens. Expected at least 25 fields, got: " + tokens.length);
        }

        entity.setIdChuyenBay(getSafe(tokens, 0));
        entity.setFlightDate(formatterService.parseSqlTimestamp(getSafe(tokens, 1)));
        entity.setCarrier(getSafe(tokens, 2));
        entity.setFlightNo(getSafe(tokens, 3));
        entity.setOrigin(getSafe(tokens, 4));
        entity.setDestination(getSafe(tokens, 5));
        entity.setMawbNo(getSafe(tokens, 6));
        entity.setMTenNguoiGui(getSafe(tokens, 7));
        entity.setMTenNguoiNhan(getSafe(tokens, 8));
        entity.setMMoTaHangHoa(getSafe(tokens, 9));
        entity.setMSoKien(formatterService.parseInteger(getSafe(tokens, 10), 0));
        entity.setMGrossWeight(formatterService.parseBigDecimal(getSafe(tokens, 11), 1));
        entity.setMSanBayDi(getSafe(tokens, 12));
        entity.setMSanBayDen(getSafe(tokens, 13));
        entity.setMVersion(getSafe(tokens, 14));
        entity.setHSoMawb(getSafe(tokens, 15));
        entity.setHSoHwb(getSafe(tokens, 16));
        entity.setHTenNguoiGui(getSafe(tokens, 17));
        entity.setHTenNguoiNhan(getSafe(tokens, 18));
        entity.setHSoKien(formatterService.parseInteger(getSafe(tokens, 19), 0));
        entity.setHTrongLuong(formatterService.parseBigDecimal(getSafe(tokens, 20), 2));
        entity.setHNoiDi(getSafe(tokens, 21));
        entity.setHNoiDen(getSafe(tokens, 22));
        entity.setHMoTaHangHoa(getSafe(tokens, 23));
    }

    private String getSafe(String[] tokens, int index) {
        if (index >= tokens.length || tokens[index] == null) {
            return "";
        }
        return tokens[index].trim();
    }
}
