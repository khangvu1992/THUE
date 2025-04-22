package com.example.thuedientu.dto;

import lombok.Data;

import java.util.List;

@Data
public class TableDefinition {
    private String tableName;
    private List<ColumnDefinition> columns;
}

