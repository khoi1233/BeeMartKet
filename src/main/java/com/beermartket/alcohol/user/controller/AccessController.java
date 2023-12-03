package com.beermartket.alcohol.user.controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
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
		String trangthai = "Đang hoạt động"; 
		TaiKhoan tk = taikhoanrp.findByTenDangNhap(username,trangthai);
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
	@RequestMapping("/access/checkconfirm-mail")
	 public ResponseEntity<String> checkconfirmmail(@RequestParam(name = "code", required = false) String code, Model model, HttpServletRequest request, @RequestBody Map<String, Object> requestBody) {
		 HttpSession sessionSEmail = request.getSession();
        String rdCode = (String) sessionSEmail.getAttribute("randomCode");
        String emCode = (String) sessionSEmail.getAttribute("emailcode");
        Map<String, Object> userInfo = (Map<String, Object>) requestBody.get("userInfo");
        
        System.out.println("Code: " + code);
		 if (emCode != null) {
			 
	         if (code != null && code.equalsIgnoreCase(rdCode)) {
	        
	             // Mã người dùng nhập vào khớp với mã lưu trong session
	        	 if (userInfo != null) {
	        		 	System.out.println("Chính xác");
	        		 	LocalDateTime currentDateTime = LocalDateTime.now();
	        		 	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
	        		 	String formattedDateTime = currentDateTime.format(formatter);
	        		    String username = (String) userInfo.get("username");
	        		    String email = (String) userInfo.get("email");
	        		    String password = (String) userInfo.get("password");
	        		    String fullName = (String) userInfo.get("fullName");
	        		    String salt = BCrypt.gensalt(12);
	        		    String hashedUserInputPassword = BCrypt.hashpw(password, salt);
	        		    TaiKhoan dangkytaikhoan = new TaiKhoan();
	        		    dangkytaikhoan.setHoTen(fullName);
	        		    dangkytaikhoan.setEmail(email);
	        		    dangkytaikhoan.setTenDangNhap(username);
	        		    dangkytaikhoan.setMatKhau(hashedUserInputPassword);
	        		    dangkytaikhoan.setMaHoaMatKhau(salt);
	        		    dangkytaikhoan.setNgayTao(formattedDateTime);
	        		    taikhoanrp.save(dangkytaikhoan);
	        		    System.out.println("Username: " + username);
	        		    System.out.println("Email: " + email);
	        		    System.out.println("Password: " + password);
	        		    System.out.println("Full Name: " + fullName);
	        		    return ResponseEntity.ok("Xác minh thành công");
	        		} else {
	        		    System.out.println("userInfo không có dữ liệu");
	        		}
	        	    
	        	 
	         }
	         else {
	        	 
	        	 System.out.println("sai");
	        	 return ResponseEntity.badRequest().body("Xác minh không thành công");
			}
	             if (emCode != null) {
	                 // Email cũng đã được lưu trong session, bạn có thể sử dụng email để xác minh
	                 sendVerificationCodeByEmail(emCode, rdCode);
	                 
	            }
	             
	         }
	         
	         // Lưu giá trị randomCode vào session
	         
	         
		 return ResponseEntity.badRequest().body("Xác minh không thành công");
	 }
	 @RequestMapping("/access/send-mail")
	 public String sendmail(@RequestParam(name = "email", required = false) String email, Model model, HttpServletRequest request) {
	     if (email != null) {
	         System.out.println("Email received from client: " + email);
	         String randomCode = generateRandomCode(6);
	         System.out.println("Email received from client: " + randomCode);

	         // Lưu giá trị randomCode vào session
	         HttpSession sessionSEmail = request.getSession();
	         sessionSEmail.setAttribute("randomCode", randomCode);
	         sessionSEmail.setAttribute("emailcode", email);
	         sessionSEmail.setMaxInactiveInterval(60);
	         sendVerificationCodeByEmail(email, randomCode);
	     } else {
	         System.out.println("No email was provided by the client.");
	     }

	     return "redirect:/access/confirm-mail";
	 }
	 @RequestMapping("/access/confirm-mail")
	    public String pageConfirmMail(Model model, HttpServletRequest request) {
		 	

	        return "access/confirm-mail";
	    }
	 private void sendVerificationCodeByEmail(String toEmail, String verificationCode) {
//	        SimpleMailMessage message = new SimpleMailMessage();
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
	
	
	
	@RequestMapping(value = "/access/reset-password", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<String> resetpassword(Model model, @RequestParam("username") String username,
            @RequestParam("email") String email) {
		String trangthai = "Đang hoạt động"; 
		TaiKhoan tk = taikhoanrp.findByTenDangNhap(username,trangthai);

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
	
	
	@RequestMapping(value = "/api/check-user", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Boolean> checkUser(@RequestParam("usernameRT") String usernameRT, @RequestParam("emailRT") String emailRT) {
	    Map<String, Boolean> result = new HashMap<>();

	    // Check username
	    TaiKhoan tkUsername = taikhoanrp.findByTenDangNhapAll(usernameRT);
	    result.put("usernameExists", tkUsername != null);

	    // Check email
	    TaiKhoan tkEmail = taikhoanrp.findByEmail(emailRT);
	    result.put("emailExists", tkEmail != null);

	    return result;
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
		 String trangthai = "Đang hoạt động"; 
			TaiKhoan tk = taikhoanrp.findByTenDangNhap(username,trangthai);
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
	 
	 @RequestMapping("/access/change-password2")
		public String changepassword2(Model model) {
		 
			return "access/change-password2";
		}
	 
	 @RequestMapping(value = "/access/change-password2", method = RequestMethod.POST)
	 @ResponseBody
	 public String changePassword2(@RequestParam("password") String password,
	                               @RequestParam("newpassword") String newpassword) {
	     String currentUser = (String) session.getAttribute(SessionAttr.CURRENT_USER);

	     if (currentUser != null) {
	    	 String trangthai = "Đang hoạt động"; 
	         TaiKhoan tk = taikhoanrp.findByTenDangNhap(currentUser,trangthai);

	         String hashedUserInputPassword2 = BCrypt.hashpw(password, tk.getMaHoaMatKhau());

	         if (hashedUserInputPassword2.equalsIgnoreCase(tk.getMatKhau())) {
	             String salt = BCrypt.gensalt(12);
	             String hashedUserInputPassword = BCrypt.hashpw(newpassword, salt);
	             tk.setMatKhau(hashedUserInputPassword);
	             tk.setMaHoaMatKhau(salt);
	             taikhoanrp.save(tk);
	             session.removeAttribute(SessionAttr.CURRENT_USER);
	     		session.removeAttribute(SessionAttr.Admin);
	     		session.removeAttribute(SessionAttr.User);
	     		session.removeAttribute(SessionAttr.SUPER_ADMIN);
	     		session.removeAttribute(SessionAttr.IMAGE);
	             return "";
	         } else {
	             return "Mật khẩu cũ không chính xác";
	         }
	     } else {
	         return "access/change-password2";
	     }
	 }
}
