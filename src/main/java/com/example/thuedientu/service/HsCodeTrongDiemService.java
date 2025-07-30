package com.example.thuedientu.service;


import com.example.thuedientu.model.HsCodeTrongDiemEntity;
import com.example.thuedientu.repository.HsCodeTrongDiemRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class HsCodeTrongDiemService {

    private final HsCodeTrongDiemRepository repository;

    public HsCodeTrongDiemService(HsCodeTrongDiemRepository repository) {
        this.repository = repository;
    }

    public List<HsCodeTrongDiemEntity> saveAll(List<HsCodeTrongDiemEntity> list) {
        return repository.saveAll(list);
    }

    public List<HsCodeTrongDiemEntity> findAll() {
        return repository.findAll();
    }

    public Optional<HsCodeTrongDiemEntity> findById(String maHs) {
        return repository.findById(maHs);
    }

    public HsCodeTrongDiemEntity save(HsCodeTrongDiemEntity hsCode) {
        return repository.save(hsCode);
    }

    public void deleteById(String maHs) {
        repository.deleteById(maHs);
    }
}
