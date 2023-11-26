package com.beermartket.alcohol.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.beermartket.alcohol.model.TaiKhoan;
import com.beermartket.alcohol.model.TaiKhoanAdmin;

@Repository
public interface TaiKhoanAdminRepository extends JpaRepository<TaiKhoanAdmin, Integer>{
	 @Query("SELECT t FROM TaiKhoanAdmin t WHERE t.tenDangNhap = :tenDangNhap")
	 	TaiKhoanAdmin findByTenDangNhap(@Param("tenDangNhap") String tenDangNhap);
}
