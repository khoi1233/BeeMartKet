package com.beermartket.alcohol.admin.controller;

import com.beermartket.alcohol.model.TinTuc;
import com.beermartket.alcohol.service.TinTucService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin")
public class TinTucAdminController {

    private final TinTucService tinTucService;

    @Autowired
    public TinTucAdminController(TinTucService tinTucService) {
        this.tinTucService = tinTucService;
    }

    @RequestMapping("/ListTinTuc")
	public String listProduct(Model model) {
		/*
		 * List<SanPham> sanPhams = spDaoPhamRepository.findAll();
		 * 
		 * model.addAttribute("sanpham", sanPhams);
		 */
		return "admin/view/ListTinTuc";
	}
	
	/*
	 * @RequestMapping("/news_detail/${maTinTuc}}") public String detailProduct() {
	 * 
	 * return "admin/view/news_detail"; }
	 */
	 
}
