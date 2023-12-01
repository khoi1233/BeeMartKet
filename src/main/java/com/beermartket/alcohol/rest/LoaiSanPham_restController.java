package com.beermartket.alcohol.rest;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
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

	@GetMapping("rest/loaisanpham/{maLoaiSanPham}")
	public ResponseEntity<Optional<LoaiSanPham>> getLoaiSanPham(@PathVariable Integer maLoaiSanPham) {
		Optional<LoaiSanPham> sanPham = lsanphamDao.findById(maLoaiSanPham);

		if (sanPham != null) {
			return ResponseEntity.ok(sanPham);
		} else {
			return ResponseEntity.notFound().build();
		}

	}

	@PutMapping("/rest/update/categoty/{maLoaiSanPham}")
	public ResponseEntity<LoaiSanPham> updateLoaiSanPham(@PathVariable int maLoaiSanPham,
			@RequestBody LoaiSanPham loaiSanPham) {

		// Kiểm tra xem sản phẩm có tồn tại không
		LoaiSanPham existingLoaiSanPham = lsanphamDao.findById(maLoaiSanPham).orElse(null);
		if (existingLoaiSanPham != null) {
			existingLoaiSanPham.setTenLoaiSanPham(loaiSanPham.getTenLoaiSanPham());
			existingLoaiSanPham.setGhiChu(loaiSanPham.getGhiChu());
			lsanphamDao.save(existingLoaiSanPham);
			return ResponseEntity.ok(existingLoaiSanPham);
		} else {
			return ResponseEntity.badRequest().build();
		}

	}

	@PostMapping("/add/category")
	public ResponseEntity<LoaiSanPham> addSanPham(@RequestBody LoaiSanPham loaiSanPham) {
		try {
			// Lấy thời gian hiện tại
			LocalDateTime currentTime = LocalDateTime.now();

			// Chuyển đổi thành kiểu Timestamp
			Timestamp currentTimestamp = Timestamp.valueOf(currentTime);

			// Gán thời gian hiện tại cho trường NgayTao
			loaiSanPham.setNgayTao(currentTimestamp);
			LoaiSanPham loaiSanPham2 = lsanphamDao.save(loaiSanPham);
			return ResponseEntity.status(HttpStatus.CREATED).body(loaiSanPham2);
		} catch (Exception e) {
			// Debug: In ra lỗi nếu có
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}
	
	 @DeleteMapping("/delete/category/{maLoaiSanPham}")
	    public ResponseEntity<Void> deleteProduct(@PathVariable int maLoaiSanPham) {
	        try {
	            // Kiểm tra xem sản phẩm có tồn tại không
	            if (!lsanphamDao.existsById(maLoaiSanPham)) {
	                return ResponseEntity.notFound().build();
	            }
	            // Xóa sản phẩm từ cơ sở dữ liệu
	            lsanphamDao.deleteById(maLoaiSanPham);
	            return ResponseEntity.noContent().build();
	        } catch (Exception e) {
	            // In ra lỗi nếu có
	            e.printStackTrace();
	            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
	        }
	    }
}
