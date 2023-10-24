package com.beermartket.alcohol.user.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import com.beermartket.alcohol.repository.LoaiSanPhamRepository;

@Controller
@RequestMapping("khachhang")
public class TrangChuController {
	@Autowired
	LoaiSanPhamRepository lspDao;
	
	@RequestMapping("/trangchu")
	public String Trangchu() {
			
		 return "customer/view/home/TrangChu";
	}
	
	@RequestMapping("/chitietsanpham/{maSanPham}")
	public String Chitietsanpham(@PathVariable("maSanPham") int maSanPham, Model model) {
			
		 return "customer/view/home/ChiTietSanPham";
	}
	
	@RequestMapping("/sanpham")
	public String Sanpham() {
			
		 return "customer/view/home/sanpham";
	}

}
