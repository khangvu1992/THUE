package com.example.thuedientu.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data // tạo getter, setter, toString, equals, hashCode
@AllArgsConstructor // constructor có đủ tham số
@NoArgsConstructor  // constructor không tham
public class DynamicQueryRequest {
    private String nameTable;
    private List<String> selectedFields;
    private Map<String, Object> filtered;
    private List<String> order;
    private Pagination pagination;

    // getters and setters

    public static class Pagination {
        private int pageIndex;
        private int pageSize;

        public int getPageIndex() {
            return  pageIndex;
        }

        public int getPageSize() {
            return pageSize;
        }
        // getters and setters
    }
}
