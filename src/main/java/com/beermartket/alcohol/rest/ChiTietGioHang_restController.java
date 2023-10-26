package com.beermartket.alcohol.rest;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.beermartket.alcohol.model.ChiTietGioHang;
import com.beermartket.alcohol.model.GioHang;
import com.beermartket.alcohol.model.SanPham;
import com.beermartket.alcohol.repository.ChiTietGioHangReponsitory;
import com.beermartket.alcohol.repository.GioHangRepository;
import com.beermartket.alcohol.repository.SanPhamRepository;
import com.beermartket.alcohol.service.gioHangService;

@RestController
public class ChiTietGioHang_restController {

	@Autowired
	ChiTietGioHangReponsitory ctgiohangDao;

	@Autowired
	GioHangRepository giohangDao;

	@Autowired
	SanPhamRepository sanPhamDAO;

	@Autowired
	gioHangService gioHang;

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

	// lưu giở hàng vào cơ sở dữ liệu

	@PostMapping("/add/{maGioHang}/{maSanPham}")
	public ChiTietGioHang addToCart(@PathVariable Integer maSanPham, @PathVariable Integer maGioHang) {
		ChiTietGioHang ctgh = new ChiTietGioHang(); // Tạo một thể hiện mới
		ctgh.setSoLuong(1);
//	    ctgh.setSoLuong(maSanPham);

		Optional<SanPham> sanPhamOptional = sanPhamDAO.findById(maSanPham);

		if (sanPhamOptional.isPresent()) {
			ctgh.setSanPham(sanPhamOptional.get());

		} else {
			// Xử lý trường hợp không tìm thấy sản phẩm
		}

		Optional<GioHang> gioHangOptional = giohangDao.findById(maGioHang);
		if (gioHangOptional.isPresent()) {
			ctgh.setGioHang(gioHangOptional.get());
			System.out.println(maGioHang);
		} else {
			// Xử lý trường hợp không tìm thấy giỏ hàng
		}

		return ctgiohangDao.save(ctgh);
	}

	@DeleteMapping("/delete/{maChiTietGH}")
	public void remove(@PathVariable Integer maChiTietGH) {
		ctgiohangDao.deleteById(maChiTietGH);
	}
}
