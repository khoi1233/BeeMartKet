package com.beermartket.alcohol.user.controller;

import java.util.Random;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.beermartket.alcohol.constant.SessionAttr;
import com.beermartket.alcohol.model.TaiKhoan;
import com.beermartket.alcohol.repository.TaiKhoanRepository;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
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
	@Autowired
	private JavaMailSender javaMailSender;

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
				
				if (hashedUserInputPassword.equals(tk.getMatKhau())) {
					System.out.println("Phong ăn cức");
					model.addAttribute("error", "Đăng nhập thành công");
					session.setAttribute(SessionAttr.CURRENT_USER, tk.getTenDangNhap());
					session.setAttribute(SessionAttr.User, tk.getTenDangNhap());
					session.setAttribute(SessionAttr.IMAGE, tk.getHinhAnh());
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
		session.removeAttribute(SessionAttr.SUPER_ADMIN);
		session.removeAttribute(SessionAttr.IMAGE);
		return "redirect:/user/home";
	}

	

	@RequestMapping("/access/register")
	public String pageregisteraccess(Model model) {

		return "access/register";
	}
	@RequestMapping(value = "/access/reset-password", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<String> resetpassword(Model model, @RequestParam("username") String username,
            @RequestParam("email") String email) {
		
		TaiKhoan tk = taikhoanrp.findByTenDangNhap(username);

		 if (tk == null) {
	            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"usernameError\": \"Tên đăng nhập không tồn tại\"}");
	        }

	        if (!tk.getEmail().equals(email)) {
	            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"emailError\": \"Email không đúng với tên đăng nhập\"}");
	        }

	        return ResponseEntity.ok("{\"success\": \"Reset password successful\"}");
	    }
		
	@RequestMapping("/access/reset-password")
	public String pageresetpassword(Model model) {

		return "access/reset-password";
	}
	@RequestMapping("/access/confirm-mail-reset-password")
	public String pageconfirmmailresetpassword(Model model) {
		
		return "access/confirm-mail-reset-password";
	}
	
	
	 @RequestMapping(value = "/api/check-username", method = RequestMethod.GET)
	 @ResponseBody
	 public boolean checkUsername(@RequestParam("usernameRT") String usernameRT) {
		 System.out.println(usernameRT);
	     TaiKhoan tk = taikhoanrp.findByTenDangNhap(usernameRT);
	     return tk != null;
	 }
	 
	 public String generateRandomCode(int length) {
	        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
	        StringBuilder result = new StringBuilder();
	        Random rnd = new Random();
	        
	        while (result.length() < length) {
	            int index = (int) (rnd.nextFloat() * characters.length());
	            result.append(characters.charAt(index));
	        }
	        
	        return result.toString();
	    }
	
	 @RequestMapping("/access/change-password")
		public String pagechangepassword(Model model, HttpServletRequest request) {
		 HttpSession sessionSEmail = request.getSession();

		 String okcheckcode = (String) sessionSEmail.getAttribute("okcheckcode");
			if (okcheckcode!=null) {
				return "access/change-password";
			}
			else {
				return "redirect:/access/reset-password";
			}
		}
	 @RequestMapping(value = "/access/change-password", method = RequestMethod.POST)
		@ResponseBody
		public String changepassword(Model model, @RequestParam("username") String username,
	            @RequestParam("email") String email, @RequestParam("newpassword") String newpassword, HttpServletRequest request) {
		 HttpSession sessionSEmail = request.getSession();

		 String okcheckcode = (String) sessionSEmail.getAttribute("okcheckcode");
			TaiKhoan tk = taikhoanrp.findByTenDangNhap(username);
			if (okcheckcode!=null) {
				if (tk != null && tk.getEmail().equals(email)) {		
					String salt = BCrypt.gensalt(12);
				    String hashedUserInputPassword = BCrypt.hashpw(newpassword, salt);
					tk.setMatKhau(hashedUserInputPassword);
					tk.setMaHoaMatKhau(salt);
					taikhoanrp.save(tk);
					System.out.println(email+","+username+","+newpassword);
					session.removeAttribute("okcheckcode");
					return "redirect:/access/login";
					}
			}
			else {
				return "redirect:/access/reset-password";
			}
			return "access/change-password";
		}
	 
	 
	@RequestMapping(value = "/access/checkmail-change-password", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<String>  checkmailchangepassword(Model model, @RequestParam(name = "code", required = false) String code,
             HttpServletRequest request) {
		HttpSession sessionSEmail = request.getSession();
        String rdCode = (String) sessionSEmail.getAttribute("randomCode");
        
        	if (code != null && code.equalsIgnoreCase(rdCode)) {
        		sessionSEmail.setAttribute("okcheckcode", code);
        		return ResponseEntity.ok("Xác minh thành công");	 
    		} 
        	else {
    
        		System.out.println("sai");
        		return ResponseEntity.badRequest().body("Xác minh không thành công");
        	}
                       
	}
	
	 private void sendByEmailResetPassword(String toEmail, String verificationCode) {

	        MimeMessage message = javaMailSender.createMimeMessage();
	        try {
	            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
	            String Subject = "BEE MARKET XIN CHÀO, MÃ XÁC NHẬN EMAIL CỦA BẠN";
	            String icon1 = "<h3 style =\" style= color: black;\">Mã xác nhận email của bạn là:</h3>";
	            String styledIcon1 = icon1+"<h3 style=\"color: blue; text-decoration: underline;\">" + verificationCode + "</h3>";
	            String content = "<html><body>" + styledIcon1 + "</body></html>";
	            helper.setTo(toEmail);
	            helper.setSubject(Subject);
	            helper.setText(content, true);  // Sử dụng true để cho phép email hiển thị HTML

	            javaMailSender.send(message);
	        } catch (MessagingException e) {
	            e.printStackTrace();
	        }
	        
	        javaMailSender.send(message);

	        
	    }
	 @RequestMapping("/access/send-mail-resetpassword")
	 public String sendmailresetpassword(@RequestParam(name = "email", required = false) String email, Model model, HttpServletRequest request) {
	     if (email != null) {
	         System.out.println("Email received from client: " + email);
	         String randomCode = generateRandomCode(6);
	         System.out.println("Email received from client: " + randomCode);

	         // Lưu giá trị randomCode vào session
	         HttpSession sessionSEmail = request.getSession();
	         sessionSEmail.setAttribute("randomCode", randomCode);
	         sessionSEmail.setAttribute("emailcode", email);
	         sessionSEmail.setMaxInactiveInterval(60);
	         sendByEmailResetPassword(email, randomCode);
	     } else {
	         System.out.println("No email was provided by the client.");
	     }

	     return "redirect:/access/confirm-mail-reset-password";
	 }
}
