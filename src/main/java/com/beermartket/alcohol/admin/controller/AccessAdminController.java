package com.beermartket.alcohol.admin.controller;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.beermartket.alcohol.constant.SessionAttr;
import com.beermartket.alcohol.model.TaiKhoanAdmin;
import com.beermartket.alcohol.repository.TaiKhoanAdminRepository;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Controller
public class AccessAdminController {
	@Autowired
	HttpSession session;
	
	@Autowired
	TaiKhoanAdminRepository taikhoanadminrp;
	
	@RequestMapping("/admin/access/login")
	public String loginPage(Model model, HttpServletRequest request, HttpSession session) {
		Cookie[] cookies = request.getCookies();
		if (cookies != null) {	
			String savedUsername = null;
			String savedPassword = null;
			for (Cookie cookie : cookies) {
				if ("usernameadmin".equals(cookie.getName())) {
					savedUsername = cookie.getValue();
				} else if ("passwordadmin".equals(cookie.getName())) {
					savedPassword = cookie.getValue();
				}
			}

			if (savedUsername != null && savedPassword != null) {
				model.addAttribute("usernameadmin", savedUsername);
				model.addAttribute("passwordadmin", savedPassword);
			}
		}
		return "admin/access/login";

	}
	
	@RequestMapping(value = "/admin/access/login", method = RequestMethod.POST)
	public String loginAccess(Model model, @RequestParam("username") String username,
			@RequestParam("password") String password,
			@RequestParam(value = "rememberMe", required = false) String rememberMe, HttpServletResponse response) {

		boolean remember = "on".equals(rememberMe);
		System.out.println(remember);
		TaiKhoanAdmin tk = taikhoanadminrp.findByTenDangNhap(username);
		username = username.trim();
		password = password.trim();

		if (username.isEmpty() && password.isEmpty()) {
			model.addAttribute("erroradmin", "Vui lòng nhập tên tài khoản và mật khẩu!");
		}

		else if (username.isEmpty()) {
			model.addAttribute("erroradmin", "Vui lòng nhập tên tài khoản!");
		} else if (password.isEmpty()) {
			model.addAttribute("erroradmin", "Vui lòng nhập mật khẩu!");
		} else {
			if (tk != null) {				                
				// Mã hóa mật khẩu người dùng với salt lấy từ cơ sở dữ liệu
				String hashedUserInputPassword = BCrypt.hashpw(password, tk.getMaHoaMatKhau());
				System.out.println(hashedUserInputPassword);
				System.out.println(tk.getMatKhau());
				
				if (hashedUserInputPassword.equals(tk.getMatKhau())) {
					model.addAttribute("erroradmin", "Đăng nhập thành công");
					session.setAttribute(SessionAttr.CURRENT_Admin, tk.getTenDangNhap());
					session.setAttribute(SessionAttr.IMAGE, tk.getHinhAnh());
					if (tk.getChucVu() == 1) {
		                // If ChucVu is 1, set the user as an admin
		                session.setAttribute(SessionAttr.Admin, true);
		                session.setAttribute(SessionAttr.Admin, tk.getTenDangNhap());
		                session.removeAttribute(SessionAttr.SUPER_ADMIN); // Ensure superadmin flag is not set
		            } else {
		                // If ChucVu is 0, set the user as a superadmin
		                session.setAttribute(SessionAttr.SUPER_ADMIN, true);
		                session.setAttribute(SessionAttr.SUPER_ADMIN, tk.getTenDangNhap());
		                session.removeAttribute(SessionAttr.Admin); // Ensure admin flag is not set
		            }
					 
					if (remember) {
						// Lưu thông tin tài khoản vào cookie
						Cookie usernameCookie = new Cookie("usernameadmin", username);
						Cookie passwordCookie = new Cookie("passwordadmin", password);
						// Đặt thời hạn cho cookie là: 30 ngày
						int maxAge = 30 * 24 * 60 * 60; // 30 days in seconds
						usernameCookie.setMaxAge(maxAge);
						passwordCookie.setMaxAge(maxAge);
						// Đặt path cho cookie (thay đổi theo yêu cầu của ứng dụng)
						usernameCookie.setPath("/");
						passwordCookie.setPath("/");
						// Thêm cookie vào phản hồi
						response.addCookie(usernameCookie);
						response.addCookie(passwordCookie);
						System.out.println("Setting username cookie: " + usernameCookie.getName() + "="
								+ usernameCookie.getValue());
						System.out.println("Setting password cookie: " + passwordCookie.getName() + "="
								+ passwordCookie.getValue());
					}
					return "redirect:/admin/home";
				} else {
					System.out.println("Mật khẩu không đúng. Đăng nhập thất bại.");
					model.addAttribute("erroradmin", "Đăng nhập thất bại, mật khẩu không chính xác!");
				}
			} else {
				// Tên người dùng không tồn tại, báo lỗi
				System.out.println("Tên người dùng không tồn tại");
				model.addAttribute("erroradmin", "Tên đăng nhập không tồn tại!");

			}
		}
		model.addAttribute("usernameadmin", username);
		model.addAttribute("passwordadmin", password);
		return "admin/access/login";
	}
	@RequestMapping("/admin/log-out")
	public String logout(Model model, HttpServletRequest req) {
		session.removeAttribute(SessionAttr.CURRENT_Admin);
		session.removeAttribute(SessionAttr.Admin);
		session.removeAttribute(SessionAttr.SUPER_ADMIN);
		session.removeAttribute(SessionAttr.IMAGE);
		return "redirect:/admin/access/login";
	}
}
