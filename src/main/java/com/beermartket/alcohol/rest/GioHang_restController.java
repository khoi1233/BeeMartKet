package com.beermartket.alcohol.rest;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.beermartket.alcohol.model.GioHang;
import com.beermartket.alcohol.repository.GioHangRepository;


@RestController
public class GioHang_restController {
	
	@Autowired
	GioHangRepository giohangDao;

	@GetMapping("/rest/giohang")
	public ResponseEntity<List<GioHang>> findAll() {
		return ResponseEntity.ok(giohangDao.findAll());
	}
}
