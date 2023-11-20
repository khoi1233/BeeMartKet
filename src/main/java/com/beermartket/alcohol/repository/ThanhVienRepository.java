package com.beermartket.alcohol.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.beermartket.alcohol.model.ThanhVien;

public interface ThanhVienRepository extends JpaRepository<ThanhVien, Long> {
    // Các phương thức tùy chọn nếu cần
}
