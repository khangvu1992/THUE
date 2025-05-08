package com.example.thuedientu.util;

import com.example.thuedientu.model.AirHouseBillEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MapEntityAirHouseContext {

    @Autowired
    private ExcelDataFormatterService formatterService;

    public void mapCsvRowToEntity(String[] tokens, AirHouseBillEntity entity) {
        if (tokens.length < 20) {
            throw new IllegalArgumentException("Insufficient tokens. Expected at least 20 fields, got: " + tokens.length);
        }

        try {

            entity.setIdChuyenBay(getSafe(tokens, 0));
            entity.setSoHieu(getSafe(tokens, 1));
            entity.setFlightDate(formatterService.parseSqlTimestamp(getSafe(tokens, 2),tokens));
            entity.setCarrierCode(getSafe(tokens, 3));
            entity.setCarrierName(getSafe(tokens, 4));
            entity.setMaNoiDi(getSafe(tokens, 5));
            entity.setTenNoiDi(getSafe(tokens, 6));
            entity.setMaNoiDen(getSafe(tokens, 7));
            entity.setTenNoiDen(getSafe(tokens, 8));
            entity.setFiMaQuaCanh(getSafe(tokens, 9));
            entity.setIdHangHoa(getSafe(tokens, 10));
            entity.setIdHangHoaChiTiet(getSafe(tokens, 11));
            entity.setFiSoAwb(getSafe(tokens, 12));
            entity.setFiSoKien(formatterService.parseInteger(getSafe(tokens, 13), 0));
            entity.setFiMoTa(getSafe(tokens, 14));
            entity.setFiShc(getSafe(tokens, 15));
            entity.setTrongLuong(formatterService.parseBigDecimal(getSafe(tokens, 16), 2));
            entity.setNoiDiHangHoa(getSafe(tokens, 17));
            entity.setNoiDenHangHoa(getSafe(tokens, 18));
            entity.setFiLoaiHang(getSafe(tokens, 19));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private String getSafe(String[] tokens, int index) {
        if (index >= tokens.length || tokens[index] == null) {
            return "";
        }
        return tokens[index].trim();
    }
}
