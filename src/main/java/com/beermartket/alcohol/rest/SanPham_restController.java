package com.beermartket.alcohol.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.beermartket.alcohol.model.LoaiSanPham;
import com.beermartket.alcohol.model.SanPham;
import com.beermartket.alcohol.repository.LoaiSanPhamRepository;
import com.beermartket.alcohol.repository.SanPhamRepository;

@RestController
public class SanPham_restController {
	@Autowired
	LoaiSanPhamRepository loaisanphamDao;
	
	@Autowired
	SanPhamRepository sanphamDao;

	@GetMapping("/rest/loaisanpham")
	public ResponseEntity<List<LoaiSanPham>> findAll1() {
		return ResponseEntity.ok(loaisanphamDao.findAll());
	}
	@GetMapping("/rest/sanpham")
	public ResponseEntity<List<SanPham>> findAll() {
		return ResponseEntity.ok(sanphamDao.findAll());
	}

}
