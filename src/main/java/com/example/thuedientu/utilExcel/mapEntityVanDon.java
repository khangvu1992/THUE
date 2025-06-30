package com.example.thuedientu.utilExcel;


import com.example.thuedientu.model.VanDonEntity;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;


@Service
public class mapEntityVanDon {
    private final DataFormatter dataFormatter = new DataFormatter();


    VanDonEntity entity = new VanDonEntity();


    public void mapRowToEntity(Row row, VanDonEntity dto) {
        System.out.println(getString1(row, 0));





//        entity.setTkid(getString(row, 0));
        dto.setB319aDecno(getString1(row, 0));
        dto.setB319aOlcdt(getString(row, 1));
        dto.setB319aInextp(getString(row, 2));
        dto.setB319aPurcd(getString(row, 3));
        dto.setB319aTypcd(getString(row, 4));
        dto.setB319aCtmcd(getString(row, 5));
        dto.setB319aArvctmcd(getString(row, 6));
        dto.setB319aPortercd(getString(row, 7));
        dto.setB319aPorternm(getString(row, 8));
        dto.setB319aScdepdt(getString(row, 9));
        dto.setB319aScarvdt(getString(row, 10));
        dto.setB319aDeploc1(getString(row, 11));
        dto.setB319aArvloc1(getString(row, 12));
        dto.setB319aRoute(getString(row, 13));
        dto.setB319aAppdt(getString(row, 14));
        dto.setB319aInvlddt(getString(row, 15));
        dto.setB319aAcdepdt(getString(row, 16));
        dto.setB319aBoasysdt(getString(row, 17));
        dto.setB319aDepboausr(getString(row, 18));
        dto.setB319aAcarvdt(getString(row, 19));
        dto.setB319aUpddt(getString(row, 20));
        dto.setB319aArvbiausr(getString(row, 21));

        dto.setB319bSerno(getString(row, 22));
        dto.setB319bBlno(getString(row, 23));
        dto.setB319bGdsnm(getString(row, 24));
        dto.setB319bHscd(getString(row, 25));
        dto.setB319bOrgncd(getString(row, 26));
        dto.setB319bQuano(getString(row, 27));
        dto.setB319bQuaunt(getString(row, 28));
        dto.setB319bGrsno(getString(row, 29));
        dto.setB319bGrsunt(getString(row, 30));
        dto.setB319bImpcd(getString(row, 31));
        dto.setB319bImpnm(getString(row, 32));
        dto.setB319bImpad(getString(row, 33));
        dto.setB319bExpcd(getString(row, 34));
        dto.setB319bExpnm(getString(row, 35));
        dto.setB319bExpad(getString(row, 36));
        dto.setB319bMrkno(getString(row, 37));

        dto.setB319cCntno(getString(row, 38));


    }

    // Helper method để lấy giá trị String từ cột trong row
    private String getString1 (Row row, int index) {
        Cell cell = row.getCell(index);
        if (cell == null) return "";

        switch (cell.getCellType()) {

            case NUMERIC:
                // Format để không mất số, đặc biệt với số lớn như mã vận đơn
                return BigDecimal.valueOf(cell.getNumericCellValue())
                        .toPlainString();

            default:
                return cell.getStringCellValue();
        }
    }

    private String getString(Row row, int index) {
        Cell cell = row.getCell(index);
        if (cell == null) return "";
        return cell.getStringCellValue();
    }

}

