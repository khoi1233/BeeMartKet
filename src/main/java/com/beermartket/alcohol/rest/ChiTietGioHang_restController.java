package com.beermartket.alcohol.rest;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.beermartket.alcohol.model.ChiTietGioHang;
import com.beermartket.alcohol.model.GioHang;
import com.beermartket.alcohol.model.SanPham;
import com.beermartket.alcohol.repository.ChiTietGioHangReponsitory;
import com.beermartket.alcohol.repository.GioHangRepository;
import com.beermartket.alcohol.repository.SanPhamRepository;
import com.beermartket.alcohol.service.gioHangService;

@RestController
public class ChiTietGioHang_restController {

	@Autowired
	ChiTietGioHangReponsitory ctgiohangDao;

	@GetMapping("/rest/ctgiohang")
	public ResponseEntity<List<ChiTietGioHang>> findAll() {
		return ResponseEntity.ok(ctgiohangDao.findAll());
	}


}
