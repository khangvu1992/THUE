package com.example.thuedientu.specification;

import com.example.thuedientu.model.EnityExcel;
import org.springframework.data.jpa.domain.Specification;

import jakarta.persistence.criteria.*;

import java.util.Map;

public class EnityExcelSpecification {

    public static Specification<EnityExcel> buildSpecification(Map<String, String> filters) {
        return (Root<EnityExcel> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) -> {
            Predicate predicate = criteriaBuilder.conjunction();

            // Duyệt qua tất cả các bộ lọc và xây dựng các điều kiện
            for (Map.Entry<String, String> entry : filters.entrySet()) {
                String field = entry.getKey();
                String value = entry.getValue();

                if (value != null && !value.isEmpty()) {
                    switch (field) {
                        case "Tkid":
                            predicate = criteriaBuilder.and(
                                    predicate,
                                    criteriaBuilder.like(root.get("Tkid"), "%" + value + "%")
                            );
                            break;
                        case "Sotk":
                            predicate = criteriaBuilder.and(
                                    predicate,
                                    criteriaBuilder.like(root.get("Sotk"), "%" + value + "%")
                            );
                            break;
                        case "Mahq":
                            predicate = criteriaBuilder.and(
                                    predicate,
                                    criteriaBuilder.like(root.get("Mahq"), "%" + value + "%")
                            );
                            break;
                        case "trangthaitk":
                            predicate = criteriaBuilder.and(
                                    predicate,
                                    criteriaBuilder.like(root.get("trangthaitk"), "%" + value + "%")
                            );
                            break;
                        case "bpkthsdt":
                            predicate = criteriaBuilder.and(
                                    predicate,
                                    criteriaBuilder.like(root.get("bpkthsdt"), "%" + value + "%")
                            );
                            break;
                        case "bptq":
                            predicate = criteriaBuilder.and(
                                    predicate,
                                    criteriaBuilder.like(root.get("bptq"), "%" + value + "%")
                            );
                            break;
                        case "ptvc":
                            predicate = criteriaBuilder.and(
                                    predicate,
                                    criteriaBuilder.like(root.get("ptvc"), "%" + value + "%")
                            );
                            break;
                        case "malh":
                            predicate = criteriaBuilder.and(
                                    predicate,
                                    criteriaBuilder.like(root.get("malh"), "%" + value + "%")
                            );
                            break;
                        case "ngayDk":
                            predicate = criteriaBuilder.and(
                                    predicate,
                                    criteriaBuilder.like(root.get("ngayDk"), "%" + value + "%")
                            );
                            break;
                        case "hourDk":
                            predicate = criteriaBuilder.and(
                                    predicate,
                                    criteriaBuilder.like(root.get("hourDk"), "%" + value + "%")
                            );
                            break;
                        case "ngayThaydoiDk":
                            predicate = criteriaBuilder.and(
                                    predicate,
                                    criteriaBuilder.like(root.get("ngayThaydoiDk"), "%" + value + "%")
                            );
                            break;
                        case "hourThaydoiDk":
                            predicate = criteriaBuilder.and(
                                    predicate,
                                    criteriaBuilder.like(root.get("hourThaydoiDk"), "%" + value + "%")
                            );
                            break;
                        case "masothueKbhq":
                            predicate = criteriaBuilder.and(
                                    predicate,
                                    criteriaBuilder.like(root.get("masothueKbhq"), "%" + value + "%")
                            );
                            break;
                        case "tenDoanhnghiep":
                            predicate = criteriaBuilder.and(
                                    predicate,
                                    criteriaBuilder.like(root.get("tenDoanhnghiep"), "%" + value + "%")
                            );
                            break;
                        case "sodienthoai":
                            predicate = criteriaBuilder.and(
                                    predicate,
                                    criteriaBuilder.like(root.get("sodienthoai"), "%" + value + "%")
                            );
                            break;
                        case "tenDoanhnghiepUythac":
                            predicate = criteriaBuilder.and(
                                    predicate,
                                    criteriaBuilder.like(root.get("tenDoanhnghiepUythac"), "%" + value + "%")
                            );
                            break;
                        case "tenDoitacnuocngoai":
                            predicate = criteriaBuilder.and(
                                    predicate,
                                    criteriaBuilder.like(root.get("tenDoitacnuocngoai"), "%" + value + "%")
                            );
                            break;
                        case "maquocgiaDoitacnuocngoai":
                            predicate = criteriaBuilder.and(
                                    predicate,
                                    criteriaBuilder.like(root.get("maquocgiaDoitacnuocngoai"), "%" + value + "%")
                            );
                            break;
                        case "vandon01":
                            predicate = criteriaBuilder.and(
                                    predicate,
                                    criteriaBuilder.like(root.get("vandon01"), "%" + value + "%")
                            );
                            break;
                        case "vandon02":
                            predicate = criteriaBuilder.and(
                                    predicate,
                                    criteriaBuilder.like(root.get("vandon02"), "%" + value + "%")
                            );
                            break;
                        case "vandon03":
                            predicate = criteriaBuilder.and(
                                    predicate,
                                    criteriaBuilder.like(root.get("vandon03"), "%" + value + "%")
                            );
                            break;
                        case "vandon04":
                            predicate = criteriaBuilder.and(
                                    predicate,
                                    criteriaBuilder.like(root.get("vandon04"), "%" + value + "%")
                            );
                            break;
                        case "vandon05":
                            predicate = criteriaBuilder.and(
                                    predicate,
                                    criteriaBuilder.like(root.get("vandon05"), "%" + value + "%")
                            );
                            break;
                        case "soluongkienhang":
                            predicate = criteriaBuilder.and(
                                    predicate,
                                    criteriaBuilder.like(root.get("soluongkienhang"), "%" + value + "%")
                            );
                            break;
                        case "maDvtKienhang":
                            predicate = criteriaBuilder.and(
                                    predicate,
                                    criteriaBuilder.like(root.get("maDvtKienhang"), "%" + value + "%")
                            );
                            break;
                        case "grossweight":
                            predicate = criteriaBuilder.and(
                                    predicate,
                                    criteriaBuilder.like(root.get("grossweight"), "%" + value + "%")
                            );
                            break;
                        case "maDvtGw":
                            predicate = criteriaBuilder.and(
                                    predicate,
                                    criteriaBuilder.like(root.get("maDvtGw"), "%" + value + "%")
                            );
                            break;
                        case "soluongContainer":
                            predicate = criteriaBuilder.and(
                                    predicate,
                                    criteriaBuilder.like(root.get("soluongContainer"), "%" + value + "%")
                            );
                            break;
                        case "maDiadiemdohang":
                            predicate = criteriaBuilder.and(
                                    predicate,
                                    criteriaBuilder.like(root.get("maDiadiemdohang"), "%" + value + "%")
                            );
                            break;
                        // ... Các trường khác tiếp tục theo cách tương tự
                        default:
                            break;
                    }
                }
            }

            return predicate;
        };
    }
}
