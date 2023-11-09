package com.beermartket.alcohol.admin.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class HomeAdminController {

	@RequestMapping("/home")
	public String Home() {

		return "admin/view/index";
	}
	
	@RequestMapping("/listProduct")
	public String listProduct() {

		return "admin/view/listProduct";
	}
	@RequestMapping("/addProduct")
	public String adđProduct() {

		return "admin/view/addProduct";
	}
}
