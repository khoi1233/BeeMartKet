package com.beermartket.alcohol.admin.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.beermartket.alcohol.model.TaiKhoan;
import com.beermartket.alcohol.repository.SanPhamRepository;
import com.beermartket.alcohol.repository.TaiKhoanRepository;

@Controller
@RequestMapping("/admin")
public class HomeAdminController {
	@Autowired
	SanPhamRepository spDaoPhamRepository;
	
	@Autowired
	private TaiKhoanRepository taiKhoanRepository;
	
	@RequestMapping("/home")
	public String Home() {

		return "admin/view/index";
	}
	@RequestMapping("/listAccount")
	public String listAccount(Model model) {
		List<TaiKhoan> taiKhoanList = taiKhoanRepository.findAll();
        model.addAttribute("taiKhoanList", taiKhoanList);
		return "admin/view/listAccount";
	}
	@RequestMapping("/addAccount")
	public String addAccount() {
		return "admin/view/addAccount";
	}
	@RequestMapping("/listProduct")
	public String listProduct(Model model) {
		/*
		 * List<SanPham> sanPhams = spDaoPhamRepository.findAll();
		 * 
		 * model.addAttribute("sanpham", sanPhams);
		 */
		return "admin/view/listProduct";
	}

	@RequestMapping("/addProduct")
	public String addProduct() {

		return "admin/view/addProduct";
	}

	@RequestMapping("/product_detail/{maSanPham}")
	public String detailProduct() {

		return "admin/view/product_detail";
	}

	@RequestMapping("/listCoupon")
	public String listCoupon() {

		return "admin/view/listCoupon";
	}
	
	@RequestMapping("/coupon_detail/{maPhieuNhap}")
	public String detailCoupon() {

		return "admin/view/coupon_detail";
	}
}
