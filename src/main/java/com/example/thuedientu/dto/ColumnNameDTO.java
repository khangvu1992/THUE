package com.example.thuedientu.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data // tạo getter, setter, toString, equals, hashCode
@AllArgsConstructor // constructor có đủ tham số
@NoArgsConstructor  // constructor không tham
public class ColumnNameDTO {

    private String columnName;
    private String dataType;
    private Integer characterMaximumLength;
    private String isNullable;



}
