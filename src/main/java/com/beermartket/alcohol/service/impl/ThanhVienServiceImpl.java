package com.beermartket.alcohol.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.beermartket.alcohol.model.ThanhVien;
import com.beermartket.alcohol.repository.ThanhVienRepository;
import com.beermartket.alcohol.service.ThanhVienService;

import java.util.List;

@Service
public class ThanhVienServiceImpl implements ThanhVienService {

    @Autowired
    private ThanhVienRepository thanhVienRepository;

    @Override
    public List<ThanhVien> getAllThanhViens() {
        return thanhVienRepository.findAll();
    }

    @Override
    public ThanhVien getThanhVienById(Long id) {
        return thanhVienRepository.findById(id).orElse(null);
    }

    @Override
    public ThanhVien saveOrUpdateThanhVien(ThanhVien thanhVien) {
        return thanhVienRepository.save(thanhVien);
    }

    @Override
    public void deleteThanhVien(Long id) {
        thanhVienRepository.deleteById(id);
    }
}
