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
    @RequestMapping("/access/login/check")
	public String loginaccess(Model model, @RequestParam("username")String username, @RequestParam("password")String password) {
    	TaiKhoan tk = taikhoanrp.findByTenDangNhap(username);
    	System.out.println(username + "," + password);
    	if (tk != null) {
        // Mật khẩu người dùng nhập khi đăng ký
//    		String salt = BCrypt.gensalt(12);
//        System.out.println(salt + "," + password);
//        String hashedPassword = BCrypt.hashpw("123", salt);
        // Mã hóa mật khẩu với salt
//        String hashedPassword = BCrypt.hashpw(password, salt);
//        System.out.println(hashedPassword + "," + password);
        // Lưu hashedPassword và salt vào cơ sở dữ liệu (điều này phải được thực hiện trong ứng dụng thực tế)
//        System.out.println("Hashed Password: " + hashedPassword);
//        System.out.println("Salt: " + salt);
        
//        String userInputPassword = "password123"; // Mật khẩu người dùng nhập
//        String saltFromDatabase = "salt_from_database"; // Salt lấy từ cơ sở dữ liệu
//        String hashedPasswordFromDatabase = hashedPassword; // Hashed password lấy từ cơ sở dữ liệu
        
        // Mã hóa mật khẩu người dùng với salt lấy từ cơ sở dữ liệu
        String hashedUserInputPassword = BCrypt.hashpw(password, tk.getMaHoaMatKhau());
        System.out.println(hashedUserInputPassword);
        System.out.println(tk.getMatKhau());
        // So sánh hashed version của mật khẩu người dùng với mật khẩu lấy từ cơ sở dữ liệu
        if (hashedUserInputPassword.equals(tk.getMatKhau())) {
            System.out.println("Mật khẩu đúng. Đăng nhập thành công.");
            return "redirect:/access/login";
        } else {
            System.out.println("Mật khẩu không đúng. Đăng nhập thất bại.");
            return "redirect:/access/login";
        }
    	}else {
            // Tên người dùng không tồn tại, báo lỗi
            System.out.println("Tên người dùng không tồn tại");
            return "redirect:/access/login";
          	
    	}
        
        
//        return "access/login";
    }
    @RequestMapping("/access/register/check")
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
