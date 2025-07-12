package com.example.thuedientu.controller;

import com.example.thuedientu.dto.DynamicQueryRequest;
import com.example.thuedientu.dto.DynamicQueryResponse;
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
    public ResponseEntity<DynamicQueryResponse> search(@RequestBody DynamicQueryRequest request) {
        // Truy vấn phân trang (data)
        List<Object> params = new ArrayList<>();
        String dataSql = buildDataSql(request, params);
        List<Map<String, Object>> data = jdbcTemplate.queryForList(dataSql, params.toArray());

        // Truy vấn đếm total
        List<Object> countParams = new ArrayList<>();
        String countSql = buildCountSql(request, countParams);
        Long total = jdbcTemplate.queryForObject(countSql, countParams.toArray(), Long.class);

        // Trả về kết quả
        DynamicQueryResponse response = new DynamicQueryResponse(data, total);
        return ResponseEntity.ok(response);
    }

    private String buildDataSql(DynamicQueryRequest req, List<Object> params) {
        StringBuilder sql = new StringBuilder();

        // SELECT
        String selectFields = (req.getSelectedFields() == null || req.getSelectedFields().isEmpty())
                ? "*"
                : String.join(", ", req.getSelectedFields());

        sql.append("SELECT ").append(selectFields)
                .append(" FROM ").append(req.getNameTable());

        // WHERE
        appendWhereClause(sql, req.getFiltered(), params);


        // ORDER BY
        if (req.getOrder() != null && !req.getOrder().isEmpty()) {
            sql.append(" ORDER BY ").append(String.join(", ", req.getOrder()));
        }

        // Pagination
        int pageSize = req.getPagination().getPageSize();
        int offset = req.getPagination().getPageIndex() * pageSize;
        sql.append(" OFFSET ").append(offset)
                .append(" ROWS FETCH NEXT ").append(pageSize)
                .append(" ROWS ONLY");

        return sql.toString();
    }

    private String buildCountSql(DynamicQueryRequest req, List<Object> params) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT COUNT(*) FROM ").append(req.getNameTable());

        appendWhereClause(sql, req.getFiltered(), params);
        return sql.toString();
    }

    private void appendWhereClause(StringBuilder sql, Map<String, Object> filters, List<Object> params) {
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
    }
}
