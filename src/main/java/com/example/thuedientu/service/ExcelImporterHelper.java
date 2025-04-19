package com.example.thuedientu.service;


import com.monitorjbl.xlsx.StreamingReader;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class ExcelImporterHelper {

    private final JdbcTemplate jdbcTemplate;

    public ExcelImporterHelper(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * Import file Excel (toàn bộ file là 1 transaction, lỗi là rollback hết).
     *
     * @param file Excel file
     * @throws Exception nếu bất kỳ lỗi nào
     */
    @Transactional(rollbackFor = Exception.class)
    public void importExcel(File file) throws Exception {
        try (InputStream is = new FileInputStream(file);
             Workbook workbook = StreamingReader.builder()
                     .rowCacheSize(100)      // Đọc 100 dòng một lúc
                     .bufferSize(4096)       // 4KB buffer
                     .open(is)) {

            Sheet sheet = workbook.getSheetAt(0);
            List<Object[]> batch = new ArrayList<>();
            int batchSize = 5000;
            int rowIndex = 0;

            for (Row row : sheet) {
                rowIndex++;

                if (rowIndex == 1) {
                    continue; // Bỏ qua dòng header
                }

                try {
                    // Đọc dữ liệu từng cell
                    Cell nameCell = row.getCell(0);
                    Cell ageCell = row.getCell(1);

                    if (nameCell == null || ageCell == null) {
                        throw new RuntimeException("Thiếu dữ liệu ở dòng " + rowIndex);
                    }

                    String name = nameCell.getStringCellValue().trim();
                    int age = (int) ageCell.getNumericCellValue();

                    batch.add(new Object[]{name, age});
                } catch (Exception e) {
                    throw new RuntimeException("Lỗi ở dòng " + rowIndex + ": " + e.getMessage(), e);
                }

                if (batch.size() >= batchSize) {
                    insertBatch(batch);
                    batch.clear();
                }
            }

            if (!batch.isEmpty()) {
                insertBatch(batch);
            }

            log.info("✅ Import hoàn tất. Tổng dòng: {}", rowIndex - 1);

        } catch (Exception e) {
            log.error("❌ Import thất bại. Rollback toàn bộ file", e);
            throw e; // Kích hoạt rollback
        }
    }

    private void insertBatch(List<Object[]> batch) {
        jdbcTemplate.batchUpdate("INSERT INTO users(name, age) VALUES (?, ?)", batch);
    }
}
