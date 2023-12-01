package com.beermartket.alcohol.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.beermartket.alcohol.model.ChiTietGioHang;
import com.beermartket.alcohol.model.ChiTietPhieuNhapHang;
import com.beermartket.alcohol.model.GioHang;
import com.beermartket.alcohol.model.PhieuNhapHang;
import com.beermartket.alcohol.model.SanPham;

public interface CTPhieuNhapHangReponsitory  extends JpaRepository<ChiTietPhieuNhapHang, Integer> {

	@Query("SELECT c FROM ChiTietPhieuNhapHang c WHERE c.phieuNhapHang.maPhieuNhap = :maPhieuNhap")
    List<ChiTietPhieuNhapHang> findByMaPhieuNhap(@Param("maPhieuNhap") int maPhieuNhap);
	//	List<ChiTietPhieuNhapHang> findByPhieu(PhieuNhapHang phieunhaphang);
	//    List<ChiTietPhieuNhapHang> findByPhieuSanPham(SanPham sanPham);
}
