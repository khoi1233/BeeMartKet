package com.beermartket.alcohol.rest;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.beermartket.alcohol.model.SanPham;
import com.beermartket.alcohol.repository.SanPhamRepository;

@RestController
public class SanPham_restController {
	
	@Autowired
	SanPhamRepository sanphamDao;

	@GetMapping("/rest/sanpham")
	public ResponseEntity<List<SanPham>> findAll() {
		return ResponseEntity.ok(sanphamDao.findAll());
	}
	 @GetMapping("rest/{maSanPham}")
	    public ResponseEntity<Optional<SanPham>> getSanPhamByMaSanPham(@PathVariable Integer maSanPham) {
	        Optional<SanPham> sanPham = sanphamDao.findById(maSanPham);
        
	        if (sanPham != null) {
	            return ResponseEntity.ok(sanPham);
	        } else {
	            return ResponseEntity.notFound().build();
	        }
	        	        
	    }
}
