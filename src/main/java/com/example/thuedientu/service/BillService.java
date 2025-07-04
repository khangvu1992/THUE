package com.example.thuedientu.service;

import com.example.thuedientu.dto.ColumnNameDTO;
import com.example.thuedientu.dto.TableNameDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BillService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<TableNameDTO> getAllTableNames() {
        String sql = """
            SELECT TABLE_NAME
            FROM INFORMATION_SCHEMA.TABLES
            WHERE TABLE_TYPE = 'BASE TABLE'
        """;

        return jdbcTemplate.query(sql, (rs, rowNum) ->
                new TableNameDTO(rs.getString("TABLE_NAME"))
        );
    }

    public List<ColumnNameDTO> getAllColumnNames(String tableName) {
        String sql = """
        SELECT
                       COLUMN_NAME,
                        DATA_TYPE,
                        CHARACTER_MAXIMUM_LENGTH,
                        IS_NULLABLE
        FROM INFORMATION_SCHEMA.COLUMNS
        WHERE TABLE_NAME = ?
    """;

        return jdbcTemplate.query(sql, new Object[]{tableName}, (rs, rowNum) ->
                new ColumnNameDTO(
                        rs.getString("COLUMN_NAME"),
                        rs.getString("DATA_TYPE"),
                        rs.getObject("CHARACTER_MAXIMUM_LENGTH", Integer.class),
                        rs.getString("IS_NULLABLE")
                )
        );
    }

}
