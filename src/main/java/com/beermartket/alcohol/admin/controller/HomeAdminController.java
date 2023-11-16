package com.beermartket.alcohol.admin.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.beermartket.alcohol.model.Hinh;
import com.beermartket.alcohol.model.SanPham;
import com.beermartket.alcohol.repository.HinhReponsitory;
import com.beermartket.alcohol.repository.SanPhamRepository;

@Controller
@RequestMapping("/admin")
public class HomeAdminController {
	@Autowired
	SanPhamRepository spDaoPhamRepository;
	
	@Autowired HinhReponsitory hinhDao;

	@RequestMapping("/home")
	public String Home() {

		return "admin/view/index";
	}

	@RequestMapping("/listProduct")
	public String listProduct(Model model) {

		return "admin/view/listProduct";
	}

	@RequestMapping("/addProduct")
	public String addProduct() {

		return "admin/view/addProduct";
	}

	@RequestMapping("/product_detail/{maSanPham}")
	public String detailProduct(@PathVariable("maSanPham") int maSanPham, Model model) {
		List<Hinh> hinhSanPhamHinh = hinhDao.findHinhByMaSanPham(maSanPham);
		for (Hinh hinh : hinhSanPhamHinh) {

		}
		model.addAttribute("hinh", hinhSanPhamHinh);
		return "admin/view/product_detail";
	}
	
	@GetMapping("/get/{maSanPham}")
	public List<Hinh> getHinh(@PathVariable("maSanPham") int maSanPham, Model model) {
		List<Hinh> hinhSanPhamHinh = hinhDao.findHinhByMaSanPham(maSanPham);
		return hinhSanPhamHinh;
	}

	@RequestMapping("/listCoupon")
	public String listCoupon() {

		return "admin/view/listCoupon";
	}
	
	@RequestMapping("/coupon_detail/{maPhieuNhap}")
	public String detailCoupon() {

		return "admin/view/coupon_detail";
	}
	@RequestMapping("/hoaDon")
	public String HoaDon() {

		return "admin/view/listOrder";
	}
	
	@RequestMapping("/hoaDon/{maHoaDon}")
	public String HoaDon2() {
        
		return "admin/view/listOrderDetail";
	}
	
}
