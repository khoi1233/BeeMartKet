package com.beermartket.alcohol.user.controller;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.beermartket.alcohol.model.ChiTietGioHang;
import com.beermartket.alcohol.model.GioHang;
import com.beermartket.alcohol.model.HoaDon;
import com.beermartket.alcohol.model.SanPham;
import com.beermartket.alcohol.repository.ChiTietGioHangReponsitory;
import com.beermartket.alcohol.repository.GioHangRepository;
import com.beermartket.alcohol.repository.HoaDonReponsitory;
import com.beermartket.alcohol.service.gioHangService;

@Controller
@RequestMapping("/user")
public class ThanhToanController {
	@Autowired
	HoaDonReponsitory hdDao;
	
	@Autowired
	gioHangService gioHang;
	
	@Autowired
	GioHangRepository giohangDao;
	
	@Autowired
	ChiTietGioHangReponsitory ctgiohangDao;

	@RequestMapping("/checkout/3")
	public String checkout() {
		return "customer/view/cart/checkout";

	}

	@PostMapping("/gotocheckout/{maGioHang}")
	public HoaDon createHoaDon(@PathVariable Integer maGioHang) {
//		// Lấy thông tin giỏ hàng từ mã giỏ hàng
//		GioHang gioHang = giohangDao.findById(maGioHang).orElse(null);
//
//		if (gioHang != null) {
//			// Sử dụng phương thức findByGioHang để lấy danh sách chi tiết giỏ hàng
//			List<ChiTietGioHang> chiTietGioHangList = ctgiohangDao.findByGioHang(gioHang);
//
//		}
		
		HoaDon hd = new HoaDon();
		Date currentDate = new Date();
//		hd.setNgayMua(currentDate);
//		hd.setTongTien(0);
//		hd.setDiaChi("Ninh Kiều");
//		hd.setTrangThaiThanhToan(false);
//		hd.setTrangThaiHoaDon("Đang xử lý");
//		hd.setGhiChu("abc");
		return hdDao.save(hd);
	}
}
