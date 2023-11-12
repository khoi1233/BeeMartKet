package com.beermartket.alcohol.user.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.beermartket.alcohol.model.TinTuc;

import com.beermartket.alcohol.service.TinTucService;

@Controller
@RequestMapping("/user")
public class TinTucController {
	@Autowired
	TinTucService tinTucService;

	@GetMapping("/blog-grid")
	public String blog(Model model) {
		List<TinTuc> listOfTinTuc = tinTucService.getAllTinTuc();
		model.addAttribute("listOfTinTuc", listOfTinTuc);
		return "customer/view/home/blog-grid";
	}
	@GetMapping("/blog-grid")
	public String blog1(Model model) {
		List<TinTuc> listOfTinTuc = tinTucService.getAllTinTuc();
		model.addAttribute("listOfTinTuc", listOfTinTuc);
		return "customer/view/home/blog-grid";
	}

	@GetMapping("/search")
	public String searchByTieuDe(@RequestParam("tieuDe") String tieuDe, Model model) {
		List<TinTuc> searchResult = tinTucService.searchTinTucByTitle(tieuDe);
		model.addAttribute("listOfTinTuc", searchResult);
		return "customer/view/home/blog-grid";
	}
}
