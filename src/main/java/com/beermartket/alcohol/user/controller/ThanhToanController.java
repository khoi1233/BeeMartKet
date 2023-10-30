package com.beermartket.alcohol.user.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/user")
public class ThanhToanController {
	@RequestMapping("/checkout/3")
    public String checkout() {
       
        return "customer/view/cart/checkout";
  
    }
}
