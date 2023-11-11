package com.beermartket.alcohol.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.beermartket.alcohol.model.LoaiSanPham;
import com.beermartket.alcohol.model.SanPham;
import com.beermartket.alcohol.repository.LoaiSanPhamRepository;
import com.beermartket.alcohol.repository.SanPhamRepository;

@RestController
public class LoaiSanPham_restController {
	
	@Autowired
	LoaiSanPhamRepository lsanphamDao;

	@GetMapping("/rest/loaisanpham")
	public ResponseEntity<List<LoaiSanPham>> findAll() {
		return ResponseEntity.ok(lsanphamDao.findAll());
	}
}
