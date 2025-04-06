//package com.example.thuedientu.specification;
//
//import com.example.thuedientu.model.EnityExcel;
//import com.example.thuedientu.model.EnityExcelSearchRequest;
//import org.springframework.data.jpa.domain.Specification;
//
//import jakarta.persistence.criteria.Predicate;
//
//import java.lang.reflect.Field;
//import java.util.ArrayList;
//import java.util.List;
//
//public class EnityExcelSpecification {
//
//    public static Specification<EnityExcel> withFilters(EnityExcelSearchRequest request) {
//        return (root, query, cb) -> {
//            List<Predicate> predicates = new ArrayList<>();
//
//            Field[] fields = EnityExcelSearchRequest.class.getDeclaredFields();
//            for (Field field : fields) {
//                try {
//                    field.setAccessible(true);
//                    Object value = field.get(request);
//                    if (value != null && value instanceof String str && !str.isBlank()) {
//                        predicates.add(cb.like(cb.lower(root.get(field.getName())), "%" + str.toLowerCase() + "%"));
//                    }
//                } catch (IllegalAccessException e) {
//                    throw new RuntimeException(e);
//                }
//            }
//
//            return cb.and(predicates.toArray(new Predicate[0]));
//        };
//    }
//}
