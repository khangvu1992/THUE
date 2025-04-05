package com.example.thuedientu.controller;


import com.example.thuedientu.model.EnityExcel;
import com.example.thuedientu.service.ExcellSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/excel_search")
@CrossOrigin(origins = "*")
public class ExcelSearchController {

    @Autowired
    private ExcellSearchService excellSearchService;

    @GetMapping
    public ResponseEntity<Page<EnityExcel>> getAllExcels(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ){
        Page<EnityExcel> excels = excellSearchService.getProducts(page,size);
        return ResponseEntity.ok(excels);
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
