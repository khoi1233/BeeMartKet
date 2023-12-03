package com.beermartket.alcohol.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.beermartket.alcohol.model.TaiKhoanAdmin;

@Repository
public interface TaiKhoanAdminRepository extends JpaRepository<TaiKhoanAdmin, Integer>{
	 @Query("SELECT t FROM TaiKhoanAdmin t WHERE t.tenDangNhap = :tenDangNhap")
	 	TaiKhoanAdmin findBytdn(@Param("tenDangNhap") String tenDangNhap);
	 @Query("SELECT tk FROM TaiKhoanAdmin tk WHERE tk.tenDangNhap = :tenDangNhap AND tk.trangThai = :trangThai")
	    TaiKhoanAdmin findByTenDangNhap(@Param("tenDangNhap") String tenDangNhap, @Param("trangThai") String trangThai);
	 @Query("SELECT tk FROM TaiKhoanAdmin tk WHERE  tk.trangThai = :trangThai AND tk.chucVu = :chucVu")
	 List<TaiKhoanAdmin> findByTenDangNhapAndTrangThaiAndChucVu(
	        @Param("trangThai") String trangThai,
	        @Param("chucVu") String chucVu
	    );
	 
	 @Query("SELECT COUNT(t) > 0 FROM TaiKhoanAdmin t WHERE t.tenDangNhap = :tenDangNhap")
	    boolean existsByTenDangNhap(@Param("tenDangNhap") String tenDangNhap);

	    @Query("SELECT COUNT(t) > 0 FROM TaiKhoanAdmin t WHERE t.email = :email")
	    boolean existsByEmail(@Param("email") String email);
	    
	    @Query("SELECT COUNT(t) > 0 FROM TaiKhoanAdmin t WHERE t.soDienThoai = :soDienThoai")
	    boolean existsByPhoneNumber(@Param("soDienThoai") String soDienThoai);
}
