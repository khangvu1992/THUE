package com.example.thuedientu.service;

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
}
