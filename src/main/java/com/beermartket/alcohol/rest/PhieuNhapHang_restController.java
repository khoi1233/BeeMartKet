package com.beermartket.alcohol.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.beermartket.alcohol.model.GioHang;
import com.beermartket.alcohol.model.PhieuNhapHang;
import com.beermartket.alcohol.repository.PhieuNhapHangReponsitory;

@RestController
public class PhieuNhapHang_restController {

	@Autowired
	PhieuNhapHangReponsitory phieuDao;
	
	@GetMapping("/rest/phieuhang")
	public ResponseEntity<List<PhieuNhapHang>> findAll() {
		return ResponseEntity.ok(phieuDao.findAll());
	}
}
