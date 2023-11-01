package com.beermartket.alcohol.user.controller;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.beermartket.alcohol.constant.SessionAttr;
import com.beermartket.alcohol.model.TaiKhoan;
import com.beermartket.alcohol.repository.TaiKhoanRepository;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Controller
public class AccessController {
	@Autowired
	HttpSession session;
	@Autowired
	private HttpServletResponse response;
	@Autowired
	TaiKhoanRepository taikhoanrp;

	@RequestMapping("/access/login")
	public String loginPage(Model model, HttpServletRequest request, HttpSession session) {
		Cookie[] cookies = request.getCookies();
		if (cookies != null) {
			String savedUsername = null;
			String savedPassword = null;
			for (Cookie cookie : cookies) {
				if ("username".equals(cookie.getName())) {
					savedUsername = cookie.getValue();
				} else if ("password".equals(cookie.getName())) {
					savedPassword = cookie.getValue();
				}
			}

			if (savedUsername != null && savedPassword != null) {
				model.addAttribute("username", savedUsername);
				model.addAttribute("password", savedPassword);
			}
		}
		return "access/login";

	}

	@RequestMapping(value = "/access/login", method = RequestMethod.POST)
	public String loginAccess(Model model, @RequestParam("username") String username,
			@RequestParam("password") String password,
			@RequestParam(value = "rememberMe", required = false) String rememberMe, HttpServletResponse response) {

		boolean remember = "on".equals(rememberMe);
		System.out.println(remember);
		TaiKhoan tk = taikhoanrp.findByTenDangNhap(username);
		username = username.trim();
		password = password.trim();

		if (username.isEmpty() && password.isEmpty()) {
			model.addAttribute("error", "Vui lòng nhập tên tài khoản và mật khẩu!");
		}

		else if (username.isEmpty()) {
			model.addAttribute("error", "Vui lòng nhập tên tài khoản!");
		} else if (password.isEmpty()) {
			model.addAttribute("error", "Vui lòng nhập mật khẩu!");
		} else {
			if (tk != null) {

				// Mã hóa mật khẩu người dùng với salt lấy từ cơ sở dữ liệu
				String hashedUserInputPassword = BCrypt.hashpw(password, tk.getMaHoaMatKhau());
				System.out.println(hashedUserInputPassword);
				System.out.println(tk.getMatKhau());
				// So sánh hashed version của mật khẩu người dùng với mật khẩu lấy từ cơ sở dữ
				// liệu

				if (hashedUserInputPassword.equals(tk.getMatKhau())) {
					model.addAttribute("error", "Đăng nhập thành công");
					session.setAttribute(SessionAttr.CURRENT_USER, tk.getTenDangNhap());
					session.setAttribute(SessionAttr.User, tk.getTenDangNhap());

					if (remember) {
						// Lưu thông tin tài khoản vào cookie
						Cookie usernameCookie = new Cookie("username", username);
						Cookie passwordCookie = new Cookie("password", password);
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
					return "redirect:/user/home";
				} else {
					System.out.println("Mật khẩu không đúng. Đăng nhập thất bại.");
					model.addAttribute("error", "Đăng nhập thất bại, mật khẩu không chính xác!");
				}
			} else {
				// Tên người dùng không tồn tại, báo lỗi
				System.out.println("Tên người dùng không tồn tại");
				model.addAttribute("error", "Tên đăng nhập không tồn tại!");

			}
		}
		model.addAttribute("username", username);
		model.addAttribute("password", password);
		return "access/login";
	}

	@RequestMapping("/home/log-out")
	public String logout(Model model, HttpServletRequest req) {
		session.removeAttribute(SessionAttr.CURRENT_USER);
		session.removeAttribute(SessionAttr.Admin);
		session.removeAttribute(SessionAttr.User);

		return "customer/view/home/home";
	}

	@RequestMapping("/access/register/check")
	public String registeraccess(Model model, @RequestParam("usernameRT") String usernameRT,
			@RequestParam("emailRT") String emailRT, @RequestParam("passwordRT") String passwordRT,
			@RequestParam("passwordRT2") String passwordRT2) {
		System.out.println(usernameRT + "," + passwordRT);
		TaiKhoan themtaikhoan = new TaiKhoan();

		// Mật khẩu người dùng nhập khi đăng ký
		String userPassword = "user";

		// Tạo salt ngẫu nhiên
		String salt = BCrypt.gensalt(12);

		// Mã hóa mật khẩu với salt
		String hashedPassword = BCrypt.hashpw("123", salt);

		// Lưu hashedPassword và salt vào cơ sở dữ liệu (điều này phải được thực hiện
		// trong ứng dụng thực tế)
//        System.out.println("Hashed Password: " + hashedPassword);
//        System.out.println("Salt: " + salt);

//        String userInputPassword = "password123"; // Mật khẩu người dùng nhập
//        String saltFromDatabase = "salt_from_database"; // Salt lấy từ cơ sở dữ liệu
		String hashedPasswordFromDatabase = hashedPassword; // Hashed password lấy từ cơ sở dữ liệu

		// Mã hóa mật khẩu người dùng với salt lấy từ cơ sở dữ liệu
		String hashedUserInputPassword = BCrypt.hashpw(passwordRT, salt);

		// So sánh hashed version của mật khẩu người dùng với hashed password lấy từ cơ
		// sở dữ liệu
		if (hashedUserInputPassword.equals(hashedPasswordFromDatabase)) {
			System.out.println("Mật khẩu đúng. Đăng nhập thành công.");
		} else {
			System.out.println("Mật khẩu không đúng. Đăng nhập thất bại.");
		}
		return "access/register";
	}

	@RequestMapping("/access/register")
	public String pageregisteraccess(Model model) {
		return "access/register";
	}

}
