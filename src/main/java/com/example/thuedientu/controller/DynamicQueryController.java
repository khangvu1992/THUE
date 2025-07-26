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
        List<Object> dataParams = new ArrayList<>();
        String dataSql = buildDataSql(request, dataParams);
        List<Map<String, Object>> data = jdbcTemplate.queryForList(dataSql, dataParams.toArray());

        List<Object> countParams = new ArrayList<>();
        String countSql = buildCountSql(request, countParams);
        Long total = jdbcTemplate.queryForObject(countSql, countParams.toArray(), Long.class);

        // Xử lý loại bỏ trùng
        String dupCol = request.getDuplicateColumn();
        if (dupCol != null && !dupCol.isBlank() && (dupCol.equals("sotk") || dupCol.equals("so_to_khai"))) {
            // Dùng params RIÊNG biệt
            List<Object> uniqueCountParams = new ArrayList<>();
            String countUniqueSql = buildCountUniqueSoToKhaiSql(request, uniqueCountParams);
            Long totalUnique = jdbcTemplate.queryForObject(countUniqueSql, uniqueCountParams.toArray(), Long.class);

            // Lấy danh sách mã số thuế không trùng
            String taxCodeField = dupCol.equals("sotk") ? "masothue_Kbhq" : "ma_nguoi_xuat_khau";
            List<Object> listFirmParams = new ArrayList<>();
            String listFirmSql = buildDistinctListSqlFirm(request, listFirmParams, taxCodeField);
            List<Map<String, Object>> taxCodeList = jdbcTemplate.queryForList(listFirmSql, listFirmParams.toArray());

            List<Object> uniqueCountParams2 = new ArrayList<>();
            String codeVolumTax = dupCol.equals("sotk") ? "tong_Tri_Gia_Tinh_Thue" : "tong_tri_gia_tinh_thue";
            String countVolumTax = buildSumColumnSql(request, uniqueCountParams2,codeVolumTax);
            Long totalVolumTax = jdbcTemplate.queryForObject(countVolumTax, uniqueCountParams2.toArray(), Long.class);

            String totalTaxCodeField = dupCol.equals("sotk") ? "masothue_Kbhq" : "ma_nguoi_xuat_khau";
            String totalTaxCodeField2 = dupCol.equals("sotk") ? "tong_Tri_Gia_Tinh_Thue" : "tong_tri_gia_tinh_thue";

            List<Object> totallistFirmParams = new ArrayList<>();
            String totallistFirmSql = buildTopCodeThueByGroupSumColumnSql(request, totallistFirmParams, totalTaxCodeField,totalTaxCodeField2,10,true);
            List<Map<String, Object>> top5codethuetotaltaxCodeList = jdbcTemplate.queryForList(totallistFirmSql, totallistFirmParams.toArray());

            String maLoaiHinh = dupCol.equals("sotk") ? "malh" : "ma_loai_hinh";
            List<Object> totalmaLoaiHinh = new ArrayList<>();
            String totalMaLoaiHinhSql = buildTopCodeThueByGroupSumColumnSql(request, totalmaLoaiHinh, maLoaiHinh,totalTaxCodeField2,10,false);
            List<Map<String, Object>> top5totalMaLoaiHinh = jdbcTemplate.queryForList(totalMaLoaiHinhSql, totalmaLoaiHinh.toArray());

            String maHScode = dupCol.equals("sotk") ? "hs_Code" : "ma_so_hang_hoa";
            List<Object> totalmaHScode = new ArrayList<>();
            String totalmaHScodeSql  = buildTopCodeThueByGroupSumColumnSql(request, totalmaHScode, maHScode,totalTaxCodeField2,10,false);
            System.out.println(totalmaHScodeSql);
            List<Map<String, Object>> top5totalmaHScode  = jdbcTemplate.queryForList(totalmaHScodeSql, totalmaHScode.toArray());

            String maSotk = dupCol.equals("sotk") ? "sotk" : "so_to_khai";
            List<Object> totamaSotk = new ArrayList<>();
            String totalmaSotkSql  = buildTopCodeThueByGroupSumColumnSql(request, totamaSotk, maSotk,totalTaxCodeField2,10,false);
            List<Map<String, Object>> top5totalmaSotk  = jdbcTemplate.queryForList(totalmaSotkSql, totamaSotk.toArray());


            return ResponseEntity.ok(new DynamicQueryResponse(data, total, totalUnique, taxCodeList,totalVolumTax,top5codethuetotaltaxCodeList,top5totalMaLoaiHinh,top5totalmaHScode,top5totalmaSotk));
        }

        // Trường hợp không trùng
        return ResponseEntity.ok(new DynamicQueryResponse(data, total, 0, null,0,null,null,null,null));
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

    private String buildCountUniqueSoToKhaiSql(DynamicQueryRequest req, List<Object> params) {
        StringBuilder sql = new StringBuilder();

        sql.append("SELECT COUNT(DISTINCT ").append(req.getDuplicateColumn()).append(") FROM (")
                .append("SELECT * FROM ").append(req.getNameTable()).append(" WHERE 1=1");

        appendWhereClause(sql, req.getFiltered(), params);

        if (req.isRemoveDuplicate() && req.getDuplicateColumn() != null && !req.getDuplicateColumn().isBlank()) {
            String col = req.getDuplicateColumn();
            sql.append(" AND ").append(col).append(" IN (")
                    .append("SELECT MAX(").append(col).append(") FROM ")
                    .append(req.getNameTable())
                    .append(" GROUP BY LEFT(").append(col).append(", 11))");
        }

        sql.append(") AS filtered_data");

        return sql.toString();
    }

    private String buildDistinctListSqlFirm(DynamicQueryRequest req, List<Object> params, String name) {
        StringBuilder sql = new StringBuilder();

        sql.append("SELECT DISTINCT ").append(name)
                .append(" FROM ").append(req.getNameTable())
                .append(" WHERE 1=1");

        // Thêm điều kiện lọc nếu có
        appendWhereClause(sql, req.getFiltered(), params);

        // Xử lý loại bỏ trùng lặp theo dupCol nếu được bật
        if (req.isRemoveDuplicate() && req.getDuplicateColumn() != null && !req.getDuplicateColumn().isBlank()) {
            String col = req.getDuplicateColumn();
            sql.append(" AND ").append(col).append(" IN (")
                    .append("SELECT MAX(").append(col).append(") FROM ")
                    .append(req.getNameTable())
                    .append(" GROUP BY LEFT(").append(col).append(", 11))");
        }

        return sql.toString();
    }

    private String buildSumColumnSql(DynamicQueryRequest req, List<Object> params, String name) {
        StringBuilder sql = new StringBuilder();

        sql.append("SELECT SUM(").append(name).append(") FROM ").append(req.getNameTable())
                .append(" WHERE 1=1");

        // Thêm điều kiện lọc nếu có
        appendWhereClause(sql, req.getFiltered(), params);

        // Xử lý loại bỏ trùng lặp theo dupCol nếu được bật
        if (req.isRemoveDuplicate() && req.getDuplicateColumn() != null && !req.getDuplicateColumn().isBlank()) {
            String col = req.getDuplicateColumn();
            sql.append(" AND ").append(col).append(" IN (")
                    .append("SELECT MAX(").append(col).append(") FROM ")
                    .append(req.getNameTable())
                    .append(" GROUP BY LEFT(").append(col).append(", 11))");
        }

        return sql.toString();
    }


    private String buildTopCodeThueByGroupSumColumnSql(
            DynamicQueryRequest req,
            List<Object> params,
            String name,
            String name2,
            int top,
            boolean asc
    ) {
        StringBuilder sql = new StringBuilder();

        sql.append("SELECT TOP ").append(top).append(" ").append(name)
                .append(", SUM(").append(name2).append(") AS tong_gia_tri ")
                .append("FROM ").append(req.getNameTable())
                .append(" WHERE 1=1");

        // Thêm điều kiện lọc nếu có
        appendWhereClause(sql, req.getFiltered(), params);

        // Xử lý loại bỏ trùng lặp
        if (req.isRemoveDuplicate() && req.getDuplicateColumn() != null && !req.getDuplicateColumn().isBlank()) {
            String col = req.getDuplicateColumn();
            sql.append(" AND ").append(col).append(" IN (")
                    .append("SELECT MAX(").append(col).append(") FROM ")
                    .append(req.getNameTable())
                    .append(" GROUP BY LEFT(").append(col).append(", 11))");
        }

        sql.append(" GROUP BY ").append(name)
                .append(" ORDER BY tong_gia_tri ").append(asc ? "ASC" : "DESC");

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
