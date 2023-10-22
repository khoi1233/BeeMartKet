package com.beermartket.alcohol.user.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.beermartket.alcohol.model.LoaiSanPham;
import com.beermartket.alcohol.repository.LoaiSanPhamRepository;

@Controller
@RequestMapping("user")
public class HomeController {
	@Autowired
	LoaiSanPhamRepository lspDao;
	
	@RequestMapping("/home")
	public String Home() {
			
		 return "customer/view/home/home";
	}
	
	@RequestMapping("/product_detail")
	public String Product_detail() {
			
		 return "customer/view/home/product-video";
	}
	
//	@GetMapping("/all/category")
//	public ResponseEntity<List<LoaiSanPham>> findAll() {
//		return ResponseEntity.ok(lspDao.findAll());
//	}
}
