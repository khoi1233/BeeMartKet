package com.beermartket.alcohol.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.beermartket.alcohol.model.TaiKhoan;
import com.beermartket.alcohol.model.TaiKhoanAdmin;

@Repository
public interface TaiKhoanRepository extends JpaRepository<TaiKhoan, Integer>{
	TaiKhoan findByEmail(String email);
	@Query("SELECT tk FROM TaiKhoan tk WHERE tk.tenDangNhap = :tenDangNhap AND tk.trangThai = :trangThai")
    List<TaiKhoan> findByTenDangNhapAndTrangThai(
        @Param("tenDangNhap") String tenDangNhap,
        @Param("trangThai") String trangThai
    );
	@Query("SELECT tk FROM TaiKhoan tk WHERE tk.trangThai = :trangThai")
    List<TaiKhoan> findByTrangThai(@Param("trangThai") String trangThai);

	 @Query("SELECT tk FROM TaiKhoan tk WHERE tk.tenDangNhap = :tenDangNhap AND tk.trangThai = :trangThai")
	    TaiKhoan findByTenDangNhap(@Param("tenDangNhap") String tenDangNhap, @Param("trangThai") String trangThai);
	 @Query("SELECT tk FROM TaiKhoan tk WHERE tk.tenDangNhap = :tenDangNhap")
	    TaiKhoan findByTenDangNhapAll(@Param("tenDangNhap") String tenDangNhap);
}
