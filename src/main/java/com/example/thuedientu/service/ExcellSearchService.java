package com.example.thuedientu.service;

import com.example.thuedientu.model.EnityExcel;
import com.example.thuedientu.repository.ExcelRepository;
import com.example.thuedientu.specification.EnityExcelSpecification;
import com.example.thuedientu.util.FilterUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.Map;


@Service
public class ExcellSearchService {


    @Autowired
    private ExcelRepository excelRepository;

    public Page<EnityExcel> searchEntities(Map<String, String> filterMap, Pageable pageable) {
        Specification<EnityExcel> specification = EnityExcelSpecification.buildSpecification(filterMap);
        return excelRepository.findAll(specification, pageable);
    }

    public Map<String, String> parseFilters(String filtersJson) {
        // Xử lý phân tích JSON từ frontend
        // Sử dụng ObjectMapper để chuyển thành Map
        return FilterUtil.parseFilters(filtersJson);
    }
}
