package com.example.thuedientu.controller;

import com.example.thuedientu.model.EnityExcel;
import com.example.thuedientu.service.ExcellSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/excel_search")
@CrossOrigin(origins = "*")
public class ExcelSearchController {

    @Autowired
    private ExcellSearchService excellSearchService;

    /**
     * Search entities with dynamic filters and pagination.
     * Example request:
     * POST /api/excel_search?page=0&size=10
     * Body:
     * {
     *   "tkid": "8787",
     *   "sotk": "56",
     *   "mahq": "565"
     * }
     */
    @PostMapping
    public ResponseEntity<Page<EnityExcel>> searchEntities(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestBody(required = false) Map<String, String> filters
    ) {
        // Xử lý đầu vào, nếu filters có thì phân tích
        if (filters != null && !filters.isEmpty()) {
            System.out.println("Received filters: " + filters);
        } else {
            System.out.println("No filters provided.");
        }

        // Tạo Pageable cho phân trang
        Pageable pageable = PageRequest.of(page, size);

        // Nếu có filters, sử dụng dịch vụ để phân tích
        Map<String, String> parsedFilters = (filters != null) ? excellSearchService.parseFilters(filters) : Map.of();

        // Thực hiện tìm kiếm với filters và phân trang
        Page<EnityExcel> result = excellSearchService.searchEntities(parsedFilters, pageable);

        // Trả về kết quả dưới dạng phản hồi phân trang
        return ResponseEntity.ok(result);
    }
}
