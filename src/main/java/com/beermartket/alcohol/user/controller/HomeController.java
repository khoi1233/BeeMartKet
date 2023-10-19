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
public class HomeController {
	@Autowired
	LoaiSanPhamRepository lspDao;
	
	 @RequestMapping("/home/{page}")
		public String mota(Model model, @PathVariable("page")String page) {
	    	model.addAttribute("mix", page);
	        return "customer/home/index";
	    }
//	@RequestMapping("/home")
//	public String Home() {
//		
//		 return "customer/home/index";
//	}
	
	@GetMapping("/all/category")
	public ResponseEntity<List<LoaiSanPham>> findAll() {
		return ResponseEntity.ok(lspDao.findAll());
	}
}
