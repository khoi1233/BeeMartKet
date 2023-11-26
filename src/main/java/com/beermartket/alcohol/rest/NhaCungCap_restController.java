package com.beermartket.alcohol.rest;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.beermartket.alcohol.model.LoaiSanPham;
import com.beermartket.alcohol.model.NhaCungCap;
import com.beermartket.alcohol.model.SanPham;
import com.beermartket.alcohol.repository.NhaCungCapReponsitory;

@RestController
public class NhaCungCap_restController {
	@Autowired
	NhaCungCapReponsitory nhaccDao;

	@GetMapping("/rest/nhacungcap")
	public ResponseEntity<List<NhaCungCap>> findAll() {
		return ResponseEntity.ok(nhaccDao.findAll());
	}
	
	 @GetMapping("/rest/nhacungcap/{maNhaCungCap}")
	    public ResponseEntity<Optional<NhaCungCap>> getNhaCungCapByMaNhaCungCap(@PathVariable int maNhaCungCap) {
	        Optional<NhaCungCap> nhaCungCap = nhaccDao.findById(maNhaCungCap);

	        if (nhaCungCap != null) {
	        	  return ResponseEntity.ok(nhaCungCap);
	        } else {
	            return ResponseEntity.notFound().build();
	        }
	    }
	 @PutMapping("/rest/update/nhacungcap/{maNhaCungCap}")
		public ResponseEntity<NhaCungCap> updateNhaCungCap(@PathVariable int maNhaCungCap,
				@RequestBody NhaCungCap nhacungcap1) {

			// Kiểm tra xem sản phẩm có tồn tại không
			NhaCungCap existingNhaCungCap = nhaccDao.findById(maNhaCungCap).orElse(null);
			if (existingNhaCungCap != null) {
				existingNhaCungCap.setTenNhaCungCap(nhacungcap1.getTenNhaCungCap());
				existingNhaCungCap.setDiaChi(nhacungcap1.getDiaChi());
				existingNhaCungCap.setSoDienThoai(nhacungcap1.getSoDienThoai());
				existingNhaCungCap.setHinhAnh(nhacungcap1.getHinhAnh());
				existingNhaCungCap.setWebsite(nhacungcap1.getWebsite());
				existingNhaCungCap.setGhiChu(nhacungcap1.getGhiChu());
				nhaccDao.save(existingNhaCungCap);
				return ResponseEntity.ok(existingNhaCungCap);
			} else {
				return ResponseEntity.badRequest().build();
			}

		}
	 @PostMapping("/add/nhacungcap")
		public ResponseEntity<NhaCungCap> addNhaCungCap(@RequestBody NhaCungCap nhacungcap1) {
			try {
				NhaCungCap nhacungcap = nhaccDao.save(nhacungcap1);
				return ResponseEntity.status(HttpStatus.CREATED).body(nhacungcap);
			} catch (Exception e) {
				// Debug: In ra lỗi nếu có
				e.printStackTrace();
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
			}
		}
	 
}
