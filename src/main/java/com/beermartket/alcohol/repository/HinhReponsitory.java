package com.beermartket.alcohol.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.beermartket.alcohol.model.Hinh;

public interface HinhReponsitory extends JpaRepository<Hinh, Integer>{

	 @Query("SELECT h FROM Hinh h WHERE h.sanPham.maSanPham = :maSanPham")
	    List<Hinh> findHinhByMaSanPham(@Param("maSanPham") int maSanPham);
}


