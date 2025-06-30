//package com.example.thuedientu.controller;
//
//import com.example.thuedientu.dto.BillDTO;
//import com.example.thuedientu.model.EnityExcel;
//import com.example.thuedientu.service.BillService;
//import com.example.thuedientu.service.ExcellSearchService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.PageRequest;
//import org.springframework.data.domain.Pageable;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//import java.util.Map;
//
//@RestController
//@RequestMapping("/api/bill_search")
//@CrossOrigin(origins = "*")
//public class SearchBillController {
//
//    @Autowired
//    private BillService billService;
//
//    @GetMapping("/search")
//    public List<BillDTO> searchBill(
//            @RequestParam(defaultValue = "") String keyword,
//            @RequestParam(defaultValue = "1") int page,
//            @RequestParam(defaultValue = "10") int size
//    ) {
//        return billService.searchBill(keyword, page, size);
//    }
//}
