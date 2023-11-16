package com.beermartket.alcohol.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.beermartket.alcohol.model.KhuyenMaiHoaDon;

@Repository
public interface KhuyenMaiHoaDonReponsitory extends JpaRepository<KhuyenMaiHoaDon, String>{
	@Query("SELECT t FROM KhuyenMaiHoaDon t WHERE t.trangThai = true")
    List<KhuyenMaiHoaDon> findAllActivated();
	@Query("SELECT t FROM KhuyenMaiHoaDon t WHERE t.maKhuyenMai = :maKhuyenMai AND t.trangThai = true")
    KhuyenMaiHoaDon findByMaKhuyenMai(@Param("maKhuyenMai") String maKhuyenMai);
}
