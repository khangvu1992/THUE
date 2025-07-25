package com.example.thuedientu.controller;

import com.example.thuedientu.dto.DynamicQueryRequest;
import com.example.thuedientu.dto.DynamicQueryResponse;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayOutputStream;
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
        List<Object> params = new ArrayList<>();
        String dataSql = buildDataSql(request, params);
        List<Map<String, Object>> data = jdbcTemplate.queryForList(dataSql, params.toArray());

        List<Object> countParams = new ArrayList<>();
        String countSql = buildCountSql(request, countParams);
        Long total = jdbcTemplate.queryForObject(countSql, countParams.toArray(), Long.class);

        return ResponseEntity.ok(new DynamicQueryResponse(data, total));
    }

    private String buildDataSql(DynamicQueryRequest req, List<Object> params) {
        StringBuilder sql = new StringBuilder();

        String selectFields = (req.getSelectedFields() == null || req.getSelectedFields().isEmpty())
                ? "*"
                : String.join(", ", req.getSelectedFields());

        sql.append("SELECT ").append(selectFields)
                .append(" FROM ").append(req.getNameTable())
                .append(" WHERE 1=1");

        appendWhereClause(sql, req.getFiltered(), params);

        if (req.isRemoveDuplicate() && req.getDuplicateColumn() != null && !req.getDuplicateColumn().isBlank()) {
            String col = req.getDuplicateColumn();
            sql.append(" AND ").append(req.getNameTable()).append(".").append(col).append(" IN (")
                    .append("SELECT MAX(").append(col).append(") FROM ")
                    .append(req.getNameTable())
                    .append(" GROUP BY LEFT(").append(col).append(", 11))");
        }

        if (req.getOrder() != null && !req.getOrder().isEmpty()) {
            sql.append(" ORDER BY ").append(String.join(", ", req.getOrder()));
        }

        int pageSize = req.getPagination().getPageSize();
        int offset = req.getPagination().getPageIndex() * pageSize;
        sql.append(" OFFSET ").append(offset)
                .append(" ROWS FETCH NEXT ").append(pageSize)
                .append(" ROWS ONLY");

        return sql.toString();
    }

    private String buildCountSql(DynamicQueryRequest req, List<Object> params) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT COUNT(*) FROM ").append(req.getNameTable())
                .append(" WHERE 1=1");

        appendWhereClause(sql, req.getFiltered(), params);

        if (req.isRemoveDuplicate() && req.getDuplicateColumn() != null && !req.getDuplicateColumn().isBlank()) {
            String col = req.getDuplicateColumn();
            sql.append(" AND ").append(req.getNameTable()).append(".").append(col).append(" IN (")
                    .append("SELECT MAX(").append(col).append(") FROM ")
                    .append(req.getNameTable())
                    .append(" GROUP BY LEFT(").append(col).append(", 11))");
        }

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
                } else if (val != null) {
                    String trimmedVal = val.toString().trim();
                    if (!trimmedVal.isEmpty()) {
                        long dollarCount = trimmedVal.chars().filter(ch -> ch == '$').count();
                        String keyword = trimmedVal.replace("$", "").toLowerCase();

                        if (dollarCount == 1) {
                            where.add("LOWER(" + field + ") LIKE ?");
                            params.add("%" + keyword + "%");
                        } else if (dollarCount >= 2) {
                            where.add("LOWER(" + field + ") LIKE ?");
                            params.add(keyword + "%");
                        } else {
                            where.add(field + " = ?");
                            params.add(trimmedVal);
                        }
                    }
                }
            }
        }

        if (!where.isEmpty()) {
            sql.append(" AND ").append(String.join(" AND ", where));
        }
    }

    @PostMapping("/export")
    public ResponseEntity<byte[]> exportExcel(@RequestBody DynamicQueryRequest request) {
        List<Object> params = new ArrayList<>();
        StringBuilder sql = new StringBuilder();

        String selectFields = (request.getSelectedFields() == null || request.getSelectedFields().isEmpty())
                ? "*"
                : String.join(", ", request.getSelectedFields());

        sql.append("SELECT ").append(selectFields)
                .append(" FROM ").append(request.getNameTable())
                .append(" WHERE 1=1");

        appendWhereClause(sql, request.getFiltered(), params);

        if (request.isRemoveDuplicate() && request.getDuplicateColumn() != null && !request.getDuplicateColumn().isBlank()) {
            String col = request.getDuplicateColumn();
            sql.append(" AND ").append(request.getNameTable()).append(".").append(col).append(" IN (")
                    .append("SELECT MAX(").append(col).append(") FROM ")
                    .append(request.getNameTable())
                    .append(" GROUP BY LEFT(").append(col).append(", 11))");
        }

        try (
                SXSSFWorkbook workbook = new SXSSFWorkbook(100);
                ByteArrayOutputStream out = new ByteArrayOutputStream()
        ) {
            final int MAX_ROWS_PER_SHEET = 1_000_000;
            final List<String> headerNames = new ArrayList<>();
            final int[] rowIndex = {0};
            final int[] sheetIndex = {1};
            Sheet[] sheet = {workbook.createSheet("Export_" + sheetIndex[0])};

            jdbcTemplate.query(sql.toString(), params.toArray(), rs -> {
                int colCount = rs.getMetaData().getColumnCount();

                if (rowIndex[0] == 0) {
                    Row headerRow = sheet[0].createRow(rowIndex[0]++);
                    headerNames.clear();
                    for (int i = 1; i <= colCount; i++) {
                        String colName = rs.getMetaData().getColumnLabel(i);
                        headerNames.add(colName);
                        headerRow.createCell(i - 1).setCellValue(colName);
                    }
                }

                if (rowIndex[0] >= MAX_ROWS_PER_SHEET) {
                    sheetIndex[0]++;
                    sheet[0] = workbook.createSheet("Export_" + sheetIndex[0]);
                    rowIndex[0] = 0;
                    Row newHeader = sheet[0].createRow(rowIndex[0]++);
                    for (int i = 0; i < headerNames.size(); i++) {
                        newHeader.createCell(i).setCellValue(headerNames.get(i));
                    }
                }

                Row row = sheet[0].createRow(rowIndex[0]++);
                for (int i = 0; i < headerNames.size(); i++) {
                    Object value = rs.getObject(headerNames.get(i));
                    row.createCell(i).setCellValue(value != null ? value.toString() : "");
                }
            });

            workbook.write(out);
            workbook.dispose();

            HttpHeaders headers = new HttpHeaders();
            headers.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=export.xlsx");
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);

            return ResponseEntity.ok().headers(headers).body(out.toByteArray());

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }
}
