package com.beermartket.alcohol.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.beermartket.alcohol.model.SanPham;

@Repository
public interface SanPhamRepository extends JpaRepository<SanPham, Integer>{

}
