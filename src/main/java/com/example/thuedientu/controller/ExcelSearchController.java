package com.example.thuedientu.controller;


import com.example.thuedientu.model.EnityExcel;
import com.example.thuedientu.service.ExcellSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/excel_search")
@CrossOrigin(origins = "*")
public class ExcelSearchController {

//    @Autowired
//    private ExcellSearchService excellSearchService;
//
//    @GetMapping
//    public ResponseEntity<Page<EnityExcel>> getAllExcels(
//            @RequestParam(defaultValue = "0") int page,
//            @RequestParam(defaultValue = "10") int size
//    ){
//        Page<EnityExcel> excels = excellSearchService.getProducts(page,size);
//        return ResponseEntity.ok(excels);
//    }


    @Autowired
    private ExcellSearchService excellSearchService;




    @GetMapping
    public Page<EnityExcel> searchEntities(
            @RequestParam int page,
            @RequestParam int size,
            @RequestParam(required = false) String filters) {

        Pageable pageable = PageRequest.of(page, size);

        Map<String, String> filterMap = new HashMap<>();



        if (filters != null && !filters.isEmpty()) {
            // Parse filters (Assuming you have a method in your service to handle this)
            filterMap = excellSearchService.parseFilters(filters);
        }
        // Giải mã JSON bộ lọc từ frontend (thêm xử lý nếu cần)
//        Map<String, String> filterMap = excellSearchService.parseFilters(filters);

        return excellSearchService.searchEntities(filterMap, pageable);
    }


}


//@RestController
//@RequestMapping("/api/products")
//public class ProductController {
//    @Autowired
//    private ProductService productService;
//
//    @GetMapping
//    public ResponseEntity<Page<Product>> getAllProducts(
//            @RequestParam(defaultValue = "0") int page,
//            @RequestParam(defaultValue = "10") int size) {
//        Page<Product> products = productService.getProducts(page, size);
//        return ResponseEntity.ok(products);
//    }
//}
