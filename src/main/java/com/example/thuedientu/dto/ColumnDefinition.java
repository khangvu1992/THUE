package com.example.thuedientu.dto;

import lombok.Data;

@Data
public class ColumnDefinition {
    private String name;
    private String type; // ví dụ: VARCHAR(255), INT, FLOAT
}
