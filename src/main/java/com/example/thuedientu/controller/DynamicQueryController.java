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
        String sql = buildSql(request);
        List<Object> params = buildParams(request);

        List<Map<String, Object>> result = jdbcTemplate.queryForList(sql, params.toArray());
        return ResponseEntity.ok(result);
    }

    private String buildSql(DynamicQueryRequest req) {
        StringBuilder sql = new StringBuilder();

        // 1. SELECT
        String selectFields = req.getSelectedFields().isEmpty() ? "*" : String.join(", ", req.getSelectedFields());
        sql.append("SELECT ").append(selectFields).append(" FROM ").append(req.getNameTable());

        // 2. WHERE
        Map<String, Object> filters = req.getFiltered();
        List<String> where = new ArrayList<>();
        if (filters != null) {
            for (Map.Entry<String, Object> entry : filters.entrySet()) {
                Object val = entry.getValue();
                if (val instanceof Map) {
                    where.add(entry.getKey() + " BETWEEN ? AND ?");
                } else {
                    where.add(entry.getKey() + " = ?");
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

        // 4. OFFSET
        int offset = req.getPagination().getPageIndex() * req.getPagination().getPageSize();
        sql.append(" OFFSET ").append(offset)
                .append(" ROWS FETCH NEXT ").append(req.getPagination().getPageSize()).append(" ROWS ONLY");

        return sql.toString();
    }

    private List<Object> buildParams(DynamicQueryRequest req) {
        List<Object> params = new ArrayList<>();
        Map<String, Object> filters = req.getFiltered();
        if (filters != null) {
            for (Object value : filters.values()) {
                if (value instanceof Map<?, ?> mapVal) {
                    params.add(mapVal.get("from"));
                    params.add(mapVal.get("to"));
                } else {
                    params.add(value);
                }
            }
        }
        return params;
    }
}
