package com.beermartket.alcohol.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.beermartket.alcohol.model.GioHang;

@Repository
public interface GioHangRepository extends JpaRepository<GioHang, Integer>{
	GioHang findByTaiKhoan_MaTaiKhoan(int maTaiKhoan);
}
