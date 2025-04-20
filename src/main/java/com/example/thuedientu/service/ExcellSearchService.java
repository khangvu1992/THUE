package com.example.thuedientu.service;

import com.example.thuedientu.model.EnityExcel;
import com.example.thuedientu.repository.EnityExcelRepository;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class ExcellSearchService {

    private final EnityExcelRepository enityExcelRepository;

    /**
     * Parse chuỗi JSON filters (dạng: {"Tkid":"abc","Sotk":"xyz"}) thành Map
     */
    public Map<String, String> parseFilters(Map<String, String> filters) {
        return filters != null ? filters : Collections.emptyMap();
    }

    /**
     * Tìm kiếm với nhiều điều kiện, có phân trang
     */
    public Page<EnityExcel> searchEntities(Map<String, String> filterMap, Pageable pageable) {
        Specification<EnityExcel> spec = buildSpecification(filterMap);
        return enityExcelRepository.findAll(spec, pageable);
    }

    /**
     * Build Specification từ Map filters
     */
    private Specification<EnityExcel> buildSpecification(Map<String, String> filters) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            filters.forEach((key, value) -> {
                if (value != null && !value.isBlank()) {
                    // Check if the field is a String
                    if (isStringField(root, key)) {
                        predicates.add(cb.like(cb.lower(root.get(key)), value.toLowerCase() + "%"));
                    } else if (isLongField(root, key)) {
                        try {
                            Long longValue = Long.parseLong(value);
                            predicates.add(cb.equal(root.get(key), longValue));
                        } catch (NumberFormatException e) {
                            // Log error for invalid number format
                            System.err.println("Invalid Long value for field: " + key + " with value: " + value);
                        }
                    } else if (isIntegerField(root, key)) {
                        try {
                            Integer intValue = Integer.parseInt(value);
                            predicates.add(cb.equal(root.get(key), intValue));
                        } catch (NumberFormatException e) {
                            // Log error for invalid number format
                            System.err.println("Invalid Integer value for field: " + key + " with value: " + value);
                        }
                    } else if (isDoubleField(root, key)) {
                        try {
                            Double doubleValue = Double.parseDouble(value);
                            predicates.add(cb.equal(root.get(key), doubleValue));
                        } catch (NumberFormatException e) {
                            // Log error for invalid number format
                            System.err.println("Invalid Double value for field: " + key + " with value: " + value);
                        }
                    } else if (isBooleanField(root, key)) {
                        Boolean booleanValue = Boolean.valueOf(value);
                        predicates.add(cb.equal(root.get(key), booleanValue));
                    } else {
                        // Handle unknown field types
                        System.err.println("Unknown field type for field: " + key);
                    }
                }
            });

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }

    // Helper method to check if a field is of type String
    private boolean isStringField(jakarta.persistence.criteria.Root<EnityExcel> root, String fieldName) {
        return root.get(fieldName).getJavaType() == String.class;
    }

    // Helper method to check if a field is of type Long
    private boolean isLongField(jakarta.persistence.criteria.Root<EnityExcel> root, String fieldName) {
        return root.get(fieldName).getJavaType() == Long.class;
    }

    // Helper method to check if a field is of type Integer
    private boolean isIntegerField(jakarta.persistence.criteria.Root<EnityExcel> root, String fieldName) {
        return root.get(fieldName).getJavaType() == Integer.class;
    }

    // Helper method to check if a field is of type Double
    private boolean isDoubleField(jakarta.persistence.criteria.Root<EnityExcel> root, String fieldName) {
        return root.get(fieldName).getJavaType() == Double.class;
    }

    // Helper method to check if a field is of type Boolean
    private boolean isBooleanField(jakarta.persistence.criteria.Root<EnityExcel> root, String fieldName) {
        return root.get(fieldName).getJavaType() == Boolean.class;
    }


}
