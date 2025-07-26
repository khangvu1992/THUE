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

    ;

}
