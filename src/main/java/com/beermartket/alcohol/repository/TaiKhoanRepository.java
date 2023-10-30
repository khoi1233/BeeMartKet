package com.beermartket.alcohol.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.beermartket.alcohol.model.TaiKhoan;

@Repository
public interface TaiKhoanRepository extends JpaRepository<TaiKhoan, Integer>{
	 @Query("SELECT t FROM TaiKhoan t WHERE t.tenDangNhap = :tenDangNhap")
	 	TaiKhoan findByTenDangNhap(@Param("tenDangNhap") String tenDangNhap);
}
