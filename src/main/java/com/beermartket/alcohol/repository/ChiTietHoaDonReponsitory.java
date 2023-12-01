package com.beermartket.alcohol.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.beermartket.alcohol.model.ChiTietHoaDon;

public interface ChiTietHoaDonReponsitory extends JpaRepository<ChiTietHoaDon, Integer>{
	@Query("SELECT c FROM ChiTietHoaDon c WHERE c.hoaDon.maHoaDon = :maHoaDon")
    List<ChiTietHoaDon> findByMaHoaDon(@Param("maHoaDon") int maHoaDon);
}
