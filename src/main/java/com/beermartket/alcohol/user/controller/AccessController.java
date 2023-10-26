package com.beermartket.alcohol.user.controller;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.beermartket.alcohol.model.TaiKhoan;
import com.beermartket.alcohol.repository.TaiKhoanRepository;

import jakarta.websocket.server.PathParam;

@Controller
public class AccessController {
	@Autowired
	TaiKhoanRepository taikhoanrp;
	
    @RequestMapping("/access/{page}")
	public String pageaccess(Model model, @PathVariable("page")String page) {
    	model.addAttribute("mix", page);
        return "access/"+page;
    }
    @RequestMapping("/access/login/t")
	public String loginaccess(Model model, @RequestParam("username")String username, @RequestParam("password")String password) {
    	System.out.println(username + "," + password);
    	TaiKhoan themtaikhoan = new TaiKhoan();
        
        // Mật khẩu người dùng nhập khi đăng ký
        String userPassword = "user";

        // Tạo salt ngẫu nhiên
        String salt = BCrypt.gensalt(12);
        
        // Mã hóa mật khẩu với salt
        String hashedPassword = BCrypt.hashpw(password, salt);

        // Lưu hashedPassword và salt vào cơ sở dữ liệu (điều này phải được thực hiện trong ứng dụng thực tế)
//        System.out.println("Hashed Password: " + hashedPassword);
//        System.out.println("Salt: " + salt);
        
//        String userInputPassword = "password123"; // Mật khẩu người dùng nhập
//        String saltFromDatabase = "salt_from_database"; // Salt lấy từ cơ sở dữ liệu
        String hashedPasswordFromDatabase = hashedPassword; // Hashed password lấy từ cơ sở dữ liệu

        // Mã hóa mật khẩu người dùng với salt lấy từ cơ sở dữ liệu
        String hashedUserInputPassword = BCrypt.hashpw("123", salt);

        // So sánh hashed version của mật khẩu người dùng với hashed password lấy từ cơ sở dữ liệu
        if (hashedUserInputPassword.equals(hashedPasswordFromDatabase)) {
            System.out.println("Mật khẩu đúng. Đăng nhập thành công.");
        } else {
            System.out.println("Mật khẩu không đúng. Đăng nhập thất bại.");
        }
        return "access/login";
    }
    @RequestMapping("/access/register/t")
	public String registeraccess(Model model, @RequestParam("usernameRT")String usernameRT, @RequestParam("emailRT")String emailRT, @RequestParam("passwordRT")String passwordRT, @RequestParam("passwordRT2")String passwordRT2) {
    	System.out.println(usernameRT + "," + passwordRT);
    	TaiKhoan themtaikhoan = new TaiKhoan();
        
        // Mật khẩu người dùng nhập khi đăng ký
        String userPassword = "user";

        // Tạo salt ngẫu nhiên
        String salt = BCrypt.gensalt(12);
        
        // Mã hóa mật khẩu với salt
        String hashedPassword = BCrypt.hashpw("123", salt);

        // Lưu hashedPassword và salt vào cơ sở dữ liệu (điều này phải được thực hiện trong ứng dụng thực tế)
//        System.out.println("Hashed Password: " + hashedPassword);
//        System.out.println("Salt: " + salt);
        
//        String userInputPassword = "password123"; // Mật khẩu người dùng nhập
//        String saltFromDatabase = "salt_from_database"; // Salt lấy từ cơ sở dữ liệu
        String hashedPasswordFromDatabase = hashedPassword; // Hashed password lấy từ cơ sở dữ liệu

        // Mã hóa mật khẩu người dùng với salt lấy từ cơ sở dữ liệu
        String hashedUserInputPassword = BCrypt.hashpw(passwordRT, salt);

        // So sánh hashed version của mật khẩu người dùng với hashed password lấy từ cơ sở dữ liệu
        if (hashedUserInputPassword.equals(hashedPasswordFromDatabase)) {
            System.out.println("Mật khẩu đúng. Đăng nhập thành công.");
        } else {
            System.out.println("Mật khẩu không đúng. Đăng nhập thất bại.");
        }
        return "access/register";
    }
    
    
}
