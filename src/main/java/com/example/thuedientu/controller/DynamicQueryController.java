package com.example.thuedientu.controller;

import com.example.thuedientu.dto.DynamicQueryRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/bill_search1")
@CrossOrigin(origins = "*")
public class DynamicQueryController {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @PostMapping("/find")
    public ResponseEntity<List<Map<String, Object>>> search(@RequestBody DynamicQueryRequest request) {
        List<Object> params = new ArrayList<>();
        String sql = buildSql(request, params);
        List<Map<String, Object>> result = jdbcTemplate.queryForList(sql, params.toArray());
        return ResponseEntity.ok(result);
    }

    private String buildSql(DynamicQueryRequest req, List<Object> params) {
        StringBuilder sql = new StringBuilder();

        // 1. SELECT fields
        String selectFields = req.getSelectedFields() == null || req.getSelectedFields().isEmpty()
                ? "*"
                : String.join(", ", req.getSelectedFields());
        sql.append("SELECT ").append(selectFields)
                .append(" FROM ").append(req.getNameTable());

        // 2. WHERE clause
        Map<String, Object> filters = req.getFiltered();
        List<String> where = new ArrayList<>();

        if (filters != null) {
            for (Map.Entry<String, Object> entry : filters.entrySet()) {
                String field = entry.getKey();
                Object val = entry.getValue();

                if (val instanceof Map<?, ?> mapVal) {
                    Object from = mapVal.get("from");
                    Object to = mapVal.get("to");

                    if (from != null && !from.toString().isBlank()) {
                        where.add(field + " >= ?");
                        params.add(from);
                    }

                    if (to != null && !to.toString().isBlank()) {
                        where.add(field + " <= ?");
                        params.add(to);
                    }
                } else {
                    if (val != null && !val.toString().isBlank()) {
                        where.add(field + " = ?");
                        params.add(val);
                    }
                }
            }
        }

        if (!where.isEmpty()) {
            sql.append(" WHERE ").append(String.join(" AND ", where));
        }

        // 3. ORDER BY
        if (req.getOrder() != null && !req.getOrder().isEmpty()) {
            sql.append(" ORDER BY ").append(String.join(", ", req.getOrder()));
        }

        // 4. Pagination
        int pageSize = req.getPagination().getPageSize();
        int offset = req.getPagination().getPageIndex() * pageSize;
        sql.append(" OFFSET ").append(offset)
                .append(" ROWS FETCH NEXT ").append(pageSize)
                .append(" ROWS ONLY");

        return sql.toString();
    }
}
