package com.example.thuedientu.service;

import com.example.thuedientu.dto.ColumnDefinition;
import com.example.thuedientu.dto.TableDefinition;
import org.apache.poi.openxml4j.util.ZipSecureFile;
import org.apache.poi.ss.usermodel.*;
import com.monitorjbl.xlsx.StreamingReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.io.*;
import java.math.BigDecimal;
import java.sql.*;
import java.sql.Date;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ExcelImportService1 {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    // Method to configure zip security settings
    public void configureZipSecurity() {
        // Set the max entry size to 5GB (adjust this as needed)
        ZipSecureFile.setMaxEntrySize(5000000000L);  // 5GB limit
    }



    public void importToDynamicTable(File file, TableDefinition tableDef) {
        configureZipSecurity();
        String createSql = generateCreateTableSQL(tableDef);
        jdbcTemplate.execute(createSql);





        try (InputStream is = new FileInputStream(file)) {



            Workbook workbook = StreamingReader.builder()
                    .rowCacheSize(100)
                    .bufferSize(4096)
                    .open(is);

            Sheet sheet = workbook.getSheetAt(0);
            Iterator<Row> rowIterator = sheet.iterator();
            List<List<String>> rows = new ArrayList<>();
            List<String> columnNames = tableDef.getColumns().stream()
                    .map(ColumnDefinition::getName)
                    .toList();

            int batchSize = 10000;
            int count = 0;

            // Lặp qua các dòng sử dụng iterator
            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();

                // Bỏ qua 2 dòng đầu tiên
                if (row.getRowNum() < 3) continue;

//                count++;
//
//                if (count % 10000 == 0) {
//                    int progress = (int) count * 100 / 1048576;
//                    System.out.println("Tiến độ: " + progress);
//                }

                List<String> values = new ArrayList<>();

                for (int i = 0; i < columnNames.size(); i++) {
                    Cell cell = row.getCell(i);

                    try {
                        values.add(cell != null ? getStringCellValue(cell) : null);
                    } catch (Exception ex) {
                        System.out.println("Lỗi đọc ô tại cột " + i + ", dòng " + row.getRowNum());
                        ex.printStackTrace();
                    }
                }

                rows.add(values);
                if (rows.size() >= batchSize) {
                    insertBatch(tableDef.getTableName(), tableDef.getColumns(), rows);
                    rows.clear();
                }
            }

            if (!rows.isEmpty()) {
                insertBatch(tableDef.getTableName(), tableDef.getColumns(), rows);
            }

            workbook.close();
            file.delete();

        } catch (Exception e) {
            System.out.println("that báiddd");
            System.out.println(e);
        }
    }



    private String generateCreateTableSQL(TableDefinition tableDef) {
        StringBuilder sb = new StringBuilder();

        sb.append("IF OBJECT_ID('").append(tableDef.getTableName()).append("', 'U') IS NULL\n");
        sb.append("BEGIN\n");
        sb.append("    CREATE TABLE ").append(tableDef.getTableName()).append(" (");

        List<String> columns = new ArrayList<>();
        for (ColumnDefinition col : tableDef.getColumns()) {
            columns.add(col.getName() + " " + col.getType());
        }

        sb.append(String.join(", ", columns)).append(");\n");
        sb.append("END");

        return sb.toString();
    }


    private void insertBatch(String tableName, List<ColumnDefinition> columns, List<List<String>> rows) {
        String colPart = columns.stream().map(ColumnDefinition::getName).collect(Collectors.joining(", "));
        String placeholders = columns.stream().map(c -> "?").collect(Collectors.joining(", "));
        String sql = String.format("INSERT INTO %s (%s) VALUES (%s)", tableName, colPart, placeholders);

        jdbcTemplate.batchUpdate(sql, rows, 10000, (ps, row) -> {
            for (int i = 0; i < columns.size(); i++) {
                String value = row.get(i);
                String type = columns.get(i).getType().toUpperCase();

                if (value == null || value.trim().isEmpty()) {
                    ps.setObject(i + 1, null);
                    continue;
                }

                try {
                    switch (normalizeType(type)) {
                        case "INT", "INTEGER" -> ps.setInt(i + 1, Integer.parseInt(value));
                        case "BIGINT" -> ps.setLong(i + 1, Long.parseLong(value));
//                        case "FLOAT", "REAL" -> ps.setFloat(i + 1, Float.parseFloat(value));
//                        case "DOUBLE" -> ps.setDouble(i + 1, Double.parseDouble(value));
                        case "DECIMAL", "NUMERIC" -> ps.setBigDecimal(i + 1, new BigDecimal(value));
//                        case "BOOLEAN", "BIT" -> ps.setBoolean(i + 1, Boolean.parseBoolean(value));
                        case "DATE" -> ps.setDate(i + 1, Date.valueOf(value)); // yyyy-[m]m-[d]d
                        case "TIME" -> ps.setTime(i + 1, Time.valueOf(value)); // HH:mm:ss
//                        case "DATETIME", "TIMESTAMP" -> {
//                            try {
//                                ps.setTimestamp(i + 1, Timestamp.valueOf(value)); // yyyy-MM-dd HH:mm:ss
//                            } catch (IllegalArgumentException e) {
//                                ps.setObject(i + 1, null);
//                            }
//                        }
                        case "VARCHAR", "NVARCHAR", "TEXT" -> ps.setString(i + 1, value);
                        default -> ps.setObject(i + 1, value); // fallback
                    }
                } catch (Exception ex) {
                    ps.setObject(i + 1, null);
                }
            }
        });
    }

    private String normalizeType(String rawType) {
        String type = rawType.toUpperCase();
        if (type.startsWith("NVARCHAR")) return "NVARCHAR";
        if (type.startsWith("DATE")) return "DATE";
        if (type.startsWith("DECIMAL")) return "DECIMAL";
        if (type.startsWith("TIME")) return "TIME";
        if (type.startsWith("VARCHAR")) return "VARCHAR";
        return type;
    }

    private String getStringCellValue(Cell cell) {
        if (cell == null) return null;

        try {
            return switch (cell.getCellType()) {
                case STRING -> cell.getStringCellValue();
                case NUMERIC -> {
                    if (DateUtil.isCellDateFormatted(cell)) {
                        yield cell.getDateCellValue().toString(); // Hoặc format theo ý muốn
                    } else {
                        yield String.valueOf(cell.getNumericCellValue());
                    }
                }
                case BOOLEAN -> String.valueOf(cell.getBooleanCellValue());
                case FORMULA -> {
                    try {
                        yield cell.getStringCellValue(); // fallback
                    } catch (IllegalStateException e) {
                        yield String.valueOf(cell.getNumericCellValue());
                    }
                }
                case BLANK -> null;
                default -> cell.toString(); // fallback nếu là lỗi khác
            };
        } catch (Exception e) {
            System.out.println(e);
            return null;
        }
    }

}
