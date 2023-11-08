package com.beermartket.alcohol.user.controller;

import java.time.LocalDateTime;
import java.util.List;
import java.sql.Timestamp;


import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.beermartket.alcohol.constant.SessionAttr;
import com.beermartket.alcohol.model.ChiTietGioHang;
import com.beermartket.alcohol.model.ChiTietHoaDon;
import com.beermartket.alcohol.model.GioHang;
import com.beermartket.alcohol.model.HoaDon;

import com.beermartket.alcohol.model.TaiKhoan;
import com.beermartket.alcohol.repository.ChiTietGioHangReponsitory;
import com.beermartket.alcohol.repository.ChiTietHoaDonReponsitory;
import com.beermartket.alcohol.repository.GioHangRepository;
import com.beermartket.alcohol.repository.HoaDonReponsitory;
import com.beermartket.alcohol.repository.TaiKhoanRepository;


import jakarta.servlet.http.HttpSession;

@Controller

public class ThanhToanController {
	@Autowired
	HoaDonReponsitory hdDao;

	@Autowired
	ChiTietHoaDonReponsitory cthdDAO;

	@Autowired
	GioHangRepository giohangDao;

	@Autowired
	ChiTietGioHangReponsitory ctgiohangDao;
	
	@Autowired
	HttpSession session;
	
	@Autowired
	TaiKhoanRepository taikhoanrp;

	@RequestMapping("/checkout/{maGioHang}")
	public String checkout() {
		return "customer/view/cart/checkout";

	}
	
	@RequestMapping("/invoice/{maGioHang}")
	public String s() {
		
		return "customer/view/cart/invoice";

	}

	@PostMapping("/createcheckout/{maGioHang}")
	public String createHoaDon(@PathVariable Integer maGioHang, Model model) {
		HoaDon hd = new HoaDon();
		
		TaiKhoan tk = new TaiKhoan() ;
		
		String username = (String) session.getAttribute(SessionAttr.User);
		if (username != null) {
			tk = taikhoanrp.findByTenDangNhap(username);
			
		}else {
			
		}
		hd.setTaiKhoan(tk);
	
		
		LocalDateTime gioHienTai = LocalDateTime.now();
		Timestamp timestamp = Timestamp.valueOf(gioHienTai);
		hd.setNgayMua(timestamp);
		
		hdDao.save(hd);
		model.addAttribute("hoaDon", hd);
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
				int giaBan = (int) (chiTietGioHang.getSanPham().getGiaGoc() - (chiTietGioHang.getSanPham().getGiaGoc()
						* chiTietGioHang.getSanPham().getChietKhauKH() / 100));
				cthd.setGiaBan(giaBan);
				cthdDAO.save(cthd);
			}
			ctgiohangDao.deleteAll();
		}
		return "customer/view/cart/invoice";
	}

}
