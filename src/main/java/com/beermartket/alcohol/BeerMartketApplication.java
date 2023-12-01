package com.beermartket.alcohol;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
@Controller
@SpringBootApplication
public class BeerMartketApplication {

	public static void main(String[] args) {
		SpringApplication.run(BeerMartketApplication.class, args);
	}
	
	@RequestMapping("/404")
    public String handle404Error() {
        return "customer/view/home/error"; 
    }

}
