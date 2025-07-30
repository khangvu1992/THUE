package com.example.thuedientu.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DynamicQueryResponse {
    private List<Map<String, Object>> data;
    private long total;
    private long totalUniqueSoToKhaiSql;
    private List<Map<String, Object>>   distinctList;
    private long trihoadon;
    private List<Map<String, Object>>   top5codethue;
    private List<Map<String, Object>>   top5totalMaLoaiHinh;
    private List<Map<String, Object>>   top5totalmaHScode;
    private List<Map<String, Object>>   top5totalmaSotk;
    private List<Map<String, Object>>   listHScode;
    private List<Map<String, Object>>   taxCodeListcongty;
    private List<Map<String, Object>>   hSCodeListDuong;










    ;

}
