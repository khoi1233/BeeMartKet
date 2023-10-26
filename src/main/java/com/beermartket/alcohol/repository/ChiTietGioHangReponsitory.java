package com.beermartket.alcohol.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.beermartket.alcohol.model.ChiTietGioHang;
import com.beermartket.alcohol.model.GioHang;
import com.beermartket.alcohol.model.SanPham;

@Repository
public interface ChiTietGioHangReponsitory extends JpaRepository<ChiTietGioHang, Integer> {
    List<ChiTietGioHang> findByGioHang(GioHang gioHang);
    List<ChiTietGioHang> findBySanPham(SanPham sanPham);

}
