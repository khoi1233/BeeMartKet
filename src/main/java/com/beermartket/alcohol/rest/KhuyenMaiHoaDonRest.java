package com.beermartket.alcohol.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.beermartket.alcohol.model.KhuyenMaiHoaDon;
import com.beermartket.alcohol.repository.KhuyenMaiHoaDonReponsitory;

@RestController
public class KhuyenMaiHoaDonRest {

	@Autowired
	KhuyenMaiHoaDonReponsitory kmhdDAO;
	@GetMapping("/rest/voucher")
	public ResponseEntity<List<KhuyenMaiHoaDon>> findAll() {
		return ResponseEntity.ok(kmhdDAO.findAll());
	}
}
