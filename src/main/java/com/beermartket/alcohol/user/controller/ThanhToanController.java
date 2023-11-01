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
import com.beermartket.alcohol.model.ChiTietHoaDon;
import com.beermartket.alcohol.model.GioHang;
import com.beermartket.alcohol.model.HoaDon;
import com.beermartket.alcohol.model.SanPham;
import com.beermartket.alcohol.repository.ChiTietGioHangReponsitory;
import com.beermartket.alcohol.repository.ChiTietHoaDonReponsitory;
import com.beermartket.alcohol.repository.GioHangRepository;
import com.beermartket.alcohol.repository.HoaDonReponsitory;
import com.beermartket.alcohol.service.gioHangService;

@Controller
@RequestMapping("/user")
public class ThanhToanController {
	@Autowired
	HoaDonReponsitory hdDao;
	
	@Autowired
	ChiTietHoaDonReponsitory cthdDAO;
	
	@Autowired
	GioHangRepository giohangDao;
	
	@Autowired
	ChiTietGioHangReponsitory ctgiohangDao;

	@RequestMapping("/checkout/4")
	public String checkout() {
		return "customer/view/cart/checkout";

	}

	@PostMapping("/gotocheckout/{maGioHang}")
	public String createHoaDon(@PathVariable Integer maGioHang) {
		HoaDon hd = new HoaDon();
		hdDao.save(hd);
		
		// Lấy thông tin giỏ hàng từ mã giỏ hàng
		GioHang gioHang = giohangDao.findById(maGioHang).orElse(null);
		if (gioHang != null) {
			// Sử dụng phương thức findByGioHang để lấy danh sách chi tiết giỏ hàng
			List<ChiTietGioHang> chiTietGioHangList = ctgiohangDao.findByGioHang(gioHang);
			for (ChiTietGioHang chiTietGioHang : chiTietGioHangList) {
				ChiTietHoaDon cthd = new ChiTietHoaDon();
				cthd.setHoaDon(hd);
				cthd.setSanPham(chiTietGioHang.getSanPham());
				cthd.setSoLuong(chiTietGioHang.getSoLuong());
				int giaBan = (int) (chiTietGioHang.getSanPham().getGiaGoc() - (chiTietGioHang.getSanPham().getGiaGoc()* chiTietGioHang.getSanPham().getChietKhauKH() /100));
				cthd.setGiaBan(giaBan);
				cthd.setGhiChu("abc");
				cthdDAO.save(cthd);
			}
		}
		return "thành công";		
	}
	
	
}
