package com.beermartket.alcohol.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.beermartket.alcohol.model.SanPham;


public interface SanPhamRepository extends JpaRepository<SanPham, Integer>{
	@Query("SELECT sp FROM SanPham sp WHERE sp.nhaCungCap.maNhaCungCap = :maNhaCungCap")
    List<SanPham> findByNhaCungCap(@Param("maNhaCungCap") int maNhaCungCap);
}
