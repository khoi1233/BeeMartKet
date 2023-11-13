package com.beermartket.alcohol.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.beermartket.alcohol.model.DonViTinh;
import com.beermartket.alcohol.repository.DonViTinhReponsitory;

@RestController
public class DonViTinh_restController {
	@Autowired
	DonViTinhReponsitory donviTinhDao;

	@GetMapping("/rest/donvitinh")
	public ResponseEntity<List<DonViTinh>> findAll() {
		return ResponseEntity.ok(donviTinhDao.findAll());
	}
}
