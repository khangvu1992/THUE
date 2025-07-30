package com.example.thuedientu.service;

import com.example.thuedientu.model.DoanhNghiepTrongDiemEntity;
import com.example.thuedientu.repository.DoanhNghiepTrongDiemRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DoanhNghiepTrongDiemService {

    private final DoanhNghiepTrongDiemRepository repository;

    public DoanhNghiepTrongDiemService(DoanhNghiepTrongDiemRepository repository) {
        this.repository = repository;
    }

    // ✅ Lấy toàn bộ danh sách
    public List<DoanhNghiepTrongDiemEntity> getAll() {
        return repository.findAll();
    }

    // ✅ Lưu 1 doanh nghiệp
    public DoanhNghiepTrongDiemEntity save(DoanhNghiepTrongDiemEntity dn) {
        return repository.save(dn);
    }

    // ✅ Lưu danh sách doanh nghiệp
    public List<DoanhNghiepTrongDiemEntity> saveAll(List<DoanhNghiepTrongDiemEntity> list) {
        return repository.saveAll(list);
    }

    // ✅ Xoá theo mã số thuế
    public void deleteById(String maSoThue) {
        repository.deleteById(maSoThue);
    }

    // ✅ Cập nhật cờ isTrongDiem
    public DoanhNghiepTrongDiemEntity updateIsTrongDiem(String maSoThue, boolean isTrongDiem) {
        Optional<DoanhNghiepTrongDiemEntity> optional = repository.findById(maSoThue);
        if (optional.isPresent()) {
            DoanhNghiepTrongDiemEntity entity = optional.get();
            entity.setIsTrongDiem(isTrongDiem);
            return repository.save(entity);
        } else {
            throw new RuntimeException("Không tìm thấy doanh nghiệp với mã số thuế: " + maSoThue);
        }
    }
}
