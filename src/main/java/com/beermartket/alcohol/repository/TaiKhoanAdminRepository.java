package com.beermartket.alcohol.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.beermartket.alcohol.model.TaiKhoanAdmin;

public interface TaiKhoanAdminRepository extends JpaRepository<TaiKhoanAdmin, Integer>{
	 @Query("SELECT t FROM TaiKhoanAdmin t WHERE t.tenDangNhap = :tenDangNhap")
	 	TaiKhoanAdmin findByTenDangNhap(@Param("tenDangNhap") String tenDangNhap);
}
