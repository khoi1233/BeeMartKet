package com.beermartket.alcohol.rest;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.beermartket.alcohol.model.ChiTietGioHang;
import com.beermartket.alcohol.model.GioHang;
import com.beermartket.alcohol.model.SanPham;
import com.beermartket.alcohol.repository.ChiTietGioHangReponsitory;
import com.beermartket.alcohol.repository.GioHangRepository;

@RestController
public class ChiTietGioHang_restController {
	
	@Autowired
	ChiTietGioHangReponsitory ctgiohangDao;
	
	@Autowired
	GioHangRepository giohangDao;


	@GetMapping("/rest/ctgiohang")
	public ResponseEntity<List<ChiTietGioHang>> findAll() {
		return ResponseEntity.ok(ctgiohangDao.findAll());
	}
	
	@GetMapping("/rest/{maGioHang}/chitietgiohang")
	public ResponseEntity<List<ChiTietGioHang>> getChiTietGioHang(@PathVariable Integer maGioHang) {
	    // Lấy thông tin giỏ hàng từ mã giỏ hàng
	    GioHang gioHang = giohangDao.findById(maGioHang).orElse(null);

	    if (gioHang != null) {
	        // Sử dụng phương thức findByGioHang để lấy danh sách chi tiết giỏ hàng
	        List<ChiTietGioHang> chiTietGioHangList = ctgiohangDao.findByGioHang(gioHang);

	        if (!chiTietGioHangList.isEmpty()) {
	            return ResponseEntity.ok(chiTietGioHangList);
	        } else {
	            return ResponseEntity.notFound().build();
	        }
	    } else {
	        return ResponseEntity.notFound().build();
	    }
	}

}
