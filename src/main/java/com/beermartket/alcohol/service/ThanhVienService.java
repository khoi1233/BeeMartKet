package com.beermartket.alcohol.service;

import java.util.List;

import com.beermartket.alcohol.model.ThanhVien;

public interface ThanhVienService {
    List<ThanhVien> getAllThanhViens();
    ThanhVien getThanhVienById(Long id);
    ThanhVien saveOrUpdateThanhVien(ThanhVien thanhVien);
    void deleteThanhVien(Long id);
}
