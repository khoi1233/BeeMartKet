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
import org.springframework.web.bind.annotation.RestController;

import com.beermartket.alcohol.model.Hinh;
import com.beermartket.alcohol.model.SanPham;
import com.beermartket.alcohol.repository.HinhReponsitory;
import com.beermartket.alcohol.repository.SanPhamRepository;

@RestController
public class SanPham_restController {

	@Autowired
	SanPhamRepository sanphamDao;

	@Autowired
	HinhReponsitory hinhDao;

	@GetMapping("/rest/sanpham")
	public ResponseEntity<List<SanPham>> findAll() {
		return ResponseEntity.ok(sanphamDao.findAll());
	}

	@GetMapping("/rest/{maSanPham}")
	public ResponseEntity<Optional<SanPham>> getSanPhamByMaSanPham(@PathVariable Integer maSanPham) {
		Optional<SanPham> sanPham = sanphamDao.findById(maSanPham);

		if (sanPham != null) {
			return ResponseEntity.ok(sanPham);
		} else {
			return ResponseEntity.notFound().build();
		}

	}

	@GetMapping("/rest/hinh/{maSanPham}")
	public ResponseEntity<List<Hinh>> getHinhMaSanPham(@PathVariable Integer maSanPham) {
		List<Hinh> hinhs = hinhDao.findHinhByMaSanPham(maSanPham);

		if (hinhs != null) {
			return ResponseEntity.ok(hinhs);
		} else {
			return ResponseEntity.notFound().build();
		}

	}
	@GetMapping("/rest/sanpham/{maNhaCungCap}")
	public ResponseEntity<List<SanPham>> getSanPhamNCC(@PathVariable Integer maNhaCungCap) {
		List<SanPham> sanPhams = sanphamDao.findByNhaCungCap(maNhaCungCap);

		if (sanPhams != null) {
			return ResponseEntity.ok(sanPhams);
		} else {
			return ResponseEntity.notFound().build();
		}

	}

	@PutMapping("/rest/update/product/{maSanPham}")
	public ResponseEntity<SanPham> updateSanPham(@PathVariable int maSanPham, @RequestBody SanPham sanPham) {

		// Kiểm tra xem sản phẩm có tồn tại không
		SanPham existingSanPham = sanphamDao.findById(maSanPham).orElse(null);
		if (existingSanPham != null) {
			existingSanPham.setTenSanPham(sanPham.getTenSanPham());
			existingSanPham.setLoaiSanPham(sanPham.getLoaiSanPham());
			existingSanPham.setDonViTinh(sanPham.getDonViTinh());
			existingSanPham.setGiaGoc(sanPham.getGiaGoc());
			existingSanPham.setChietKhauKH(sanPham.getChietKhauKH());
			existingSanPham.setTrangThai(sanPham.getTrangThai() == true);
			existingSanPham.setNoiBat(sanPham.getNoiBat() == true);
			existingSanPham.setThongTin(sanPham.getThongTin());
			existingSanPham.setNhaCungCap(sanPham.getNhaCungCap());
			existingSanPham.setHinhAnh(sanPham.getHinhAnh());
			sanphamDao.save(existingSanPham);
			return ResponseEntity.ok(existingSanPham);
		} else {
			return ResponseEntity.badRequest().build();
		}

	}
	
	@PutMapping("/rest/update/gianhap/product/{maSanPham}")
	public ResponseEntity<SanPham> updateGiaNhapSanPham(@PathVariable int maSanPham, @RequestBody SanPham sanPham) {
	    // Kiểm tra xem sản phẩm có tồn tại không
	    SanPham existingSanPham = sanphamDao.findById(maSanPham).orElse(null);
	    
	    if (existingSanPham != null) {
	        // Cập nhật giaNhap thay vì soLuong
	        existingSanPham.setGiaNhap(sanPham.getGiaNhap());
	        sanphamDao.save(existingSanPham);
	        return ResponseEntity.ok(existingSanPham);
	    } else {
	        return ResponseEntity.badRequest().build();
	    }
	}

	
	@PostMapping("/add/product")
	public ResponseEntity<SanPham> addSanPham(@RequestBody SanPham sanpham) {
	    try {
	    	// Lấy thời gian hiện tại
            LocalDateTime currentTime = LocalDateTime.now();
            
            // Chuyển đổi thành kiểu Timestamp
            Timestamp currentTimestamp = Timestamp.valueOf(currentTime);

            // Gán thời gian hiện tại cho trường NgayTao
            sanpham.setNgayTao(currentTimestamp);
	    	sanpham.setSoLuong(0);
SanPham newSanPham = sanphamDao.save(sanpham); // Lưu item vào cơ sở dữ liệu
	        return ResponseEntity.status(HttpStatus.CREATED).body(newSanPham);
	    } catch (Exception e) {
	        // Debug: In ra lỗi nếu có
	        e.printStackTrace();
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
	    }
	}
	
	 @DeleteMapping("/delete/product/{masanpham}")
	    public ResponseEntity<Void> deleteProduct(@PathVariable int masanpham) {
	        try {
	            // Kiểm tra xem sản phẩm có tồn tại không
	            if (!sanphamDao.existsById(masanpham)) {
	                return ResponseEntity.notFound().build();
	            }

	            // Xóa sản phẩm từ cơ sở dữ liệu
	            sanphamDao.deleteById(masanpham);

	            return ResponseEntity.noContent().build();
	        } catch (Exception e) {
	            // In ra lỗi nếu có
	            e.printStackTrace();
	            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
	        }
	    }



}
