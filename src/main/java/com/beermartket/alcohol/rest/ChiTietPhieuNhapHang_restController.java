package com.beermartket.alcohol.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.beermartket.alcohol.model.ChiTietPhieuNhapHang;
import com.beermartket.alcohol.repository.CTPhieuNhapHangReponsitory;

@RestController
public class ChiTietPhieuNhapHang_restController {

	@Autowired
	CTPhieuNhapHangReponsitory phieuDao;
	
	@GetMapping("/rest/ct-phieuhang")
	public ResponseEntity<List<ChiTietPhieuNhapHang>> findAll() {
		return ResponseEntity.ok(phieuDao.findAll());
	}
}
