package com.beermartket.alcohol.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.beermartket.alcohol.model.HoaDon;


public interface HoaDonReponsitory extends JpaRepository<HoaDon, Integer>{
	@Query("SELECT h FROM HoaDon h WHERE h.trangThaiHoaDon = '1'")
	List<HoaDon> findByTrangThaiHoaDon();
	
	@Query("SELECT h FROM HoaDon h WHERE h.trangThaiHoaDon = '2'")
	List<HoaDon> findByTrangThaiHoaDon2();
	
	@Query("SELECT h FROM HoaDon h WHERE h.trangThaiHoaDon = '3'")
	List<HoaDon> findByTrangThaiHoaDon3();
	
	@Query("SELECT DATEDIFF(MINUTE, h.ngayMua, CURRENT_TIMESTAMP) AS ThoiGianTuNgayMuaDenHienTai FROM HoaDon h WHERE h.maHoaDon = :maHoaDon")
    Integer findTimeDifferenceByMaHoaDon(@Param("maHoaDon") Integer maHoaDon);

}
