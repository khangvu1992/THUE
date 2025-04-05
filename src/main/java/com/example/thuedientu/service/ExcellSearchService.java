package com.example.thuedientu.service;

import com.example.thuedientu.model.EnityExcel;
import com.example.thuedientu.repository.ExcelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;


@Service
public class ExcellSearchService {

    @Autowired
    private ExcelRepository excelRepository;

    public Page<EnityExcel> getProducts(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return excelRepository.findAll(pageable);
    }
}
