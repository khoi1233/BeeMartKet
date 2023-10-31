package com.beermartket.alcohol.user.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.beermartket.alcohol.model.Hinh;
import com.beermartket.alcohol.repository.HinhReponsitory;

@Controller
@RequestMapping("/user")
public class HomeController {
	@Autowired
	HinhReponsitory hinhDao;

	@RequestMapping("/home")
	public String Home() {

		return "customer/view/home/home";
	}

	@GetMapping("/product_detail/{maSanPham}")
	public String Product_detail(@PathVariable("maSanPham") int maSanPham, Model model) {

		List<Hinh> hinhSanPhamHinh = hinhDao.findHinhByMaSanPham(maSanPham);
		for (Hinh hinh : hinhSanPhamHinh) {

		}
		model.addAttribute("hinh", hinhSanPhamHinh);
		return "customer/view/home/product-video";
	}

	@RequestMapping("/faq")
	public String Faq() {

		return "customer/view/home/faq";
	}

	@RequestMapping("/offerl")
	public String Offerls() {

		return "customer/view/home/offerl";
	}

	@RequestMapping("/profile")
	public String Profile() {

		return "customer/view/home/profile";
	}
	
	@RequestMapping("/change-password")
	public String ChangePassword() {

		return "customer/view/home/change-password";
	}
	
	@RequestMapping("/about")
	public String About() {

		return "customer/view/home/about";
	}

	@RequestMapping("/contact")
	public String Contact() {

		return "customer/view/home/contact";
	}
	
	@RequestMapping("/privacy")
	public String Privacy() {

		return "customer/view/home/privacy";
	}
	
	@RequestMapping("/error")
	public String Email_en() {

		return "customer/view/home/error";
	}
}
