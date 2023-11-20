package com.beermartket.alcohol.admin.controller;

import com.beermartket.alcohol.model.TinTuc;
import com.beermartket.alcohol.service.TinTucService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin")
public class ThanhVienAdminController {

   

	@RequestMapping("/ListThanhVien")
	public String listProduct() {
		
		return "admin/view/ListThanhVien";
	}
	 
}
