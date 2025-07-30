package com.example.thuedientu.service;


import com.example.thuedientu.model.TuyenDuongTrongDiemEntity;
import com.example.thuedientu.repository.TuyenDuongRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TuyenDuongService {

    private final TuyenDuongRepository repo;

    public List<TuyenDuongTrongDiemEntity> getAll() {
        return repo.findAll();
    }

    public void importList(List<TuyenDuongTrongDiemEntity> list) {
        for (TuyenDuongTrongDiemEntity td : list) {
            var existing = repo.findByCuaKhauDiAndCuaKhauDen(td.getCuaKhauDi(), td.getCuaKhauDen());
            existing.ifPresentOrElse(
                    e -> {
                        e.setMoTa(td.getMoTa());
                        repo.save(e);
                    },
                    () -> repo.save(td)
            );
        }
    }

    public void delete(TuyenDuongTrongDiemEntity td) {
        repo.deleteByCuaKhauDiAndCuaKhauDen(td.getCuaKhauDi(), td.getCuaKhauDen());
    }
}
