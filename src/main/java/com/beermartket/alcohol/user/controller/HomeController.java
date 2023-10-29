package com.beermartket.alcohol.user.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import com.beermartket.alcohol.repository.LoaiSanPhamRepository;

@Controller
@RequestMapping("/user")
public class HomeController {
	@Autowired
	
	@RequestMapping("/home")
	public String Home() {
			
		 return "customer/view/home/home";
	}
	
	@RequestMapping("/product_detail/{maSanPham}")
	public String Product_detail(@PathVariable("maSanPham") int maSanPham, Model model) {
			
		 return "customer/view/home/product-video";
	}
	

}
