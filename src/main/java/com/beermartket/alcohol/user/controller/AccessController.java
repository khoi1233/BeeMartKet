package com.beermartket.alcohol.user.controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.regex.Pattern;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
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
	public String loginPage(Model model ,HttpServletRequest request,HttpSession session) {
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
	public String loginAccess(Model model, 
			@RequestParam("username") String username, 
			@RequestParam("password") String password,
			@RequestParam(value = "rememberMe", required = false) String rememberMe,
			HttpServletResponse response) {
		
		boolean remember = "on".equals(rememberMe);
		System.out.println(remember);
	TaiKhoan tk = taikhoanrp.findByTenDangNhap(username);
	username = username.trim();
    password = password.trim();

    	if (username.isEmpty() && password.isEmpty()) {
            model.addAttribute("error", "Vui lòng nhập tên tài khoản và mật khẩu!");
        }
    	
    	else if (username.isEmpty()){
    		model.addAttribute("error", "Vui lòng nhập tên tài khoản!");
    	}
    	else if (password.isEmpty()){
    		model.addAttribute("error", "Vui lòng nhập mật khẩu!");
    	}
    	else {
    		if (tk != null) {
    	        
    	        // Mã hóa mật khẩu người dùng với salt lấy từ cơ sở dữ liệu
    	        String hashedUserInputPassword = BCrypt.hashpw(password, tk.getMaHoaMatKhau());
    	        System.out.println(hashedUserInputPassword);
    	        System.out.println(tk.getMatKhau());
    	        // So sánh hashed version của mật khẩu người dùng với mật khẩu lấy từ cơ sở dữ liệu
    	        
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
                        System.out.println("Setting username cookie: " + usernameCookie.getName() + "=" + usernameCookie.getValue());
                        System.out.println("Setting password cookie: " + passwordCookie.getName() + "=" + passwordCookie.getValue());
                    }
    	            return "redirect:/user/home";

    	        } else {
    	            System.out.println("Mật khẩu không đúng. Đăng nhập thất bại.");
    	            model.addAttribute("error", "Đăng nhập thất bại, mật khẩu không chính xác!");
    	        }
    	    	}else {
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

		return "customer/home/homex";
	}
	
	public static String generateRandomString(Integer length) {
		if (length == null) {
	        throw new IllegalArgumentException("Length must not be null");
	    }
        String characters = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        Random random = new Random();
        StringBuilder sb = new StringBuilder(length);

        for (int i = 0; i < length; i++) {
            int index = random.nextInt(characters.length());
            char randomChar = characters.charAt(index);
            sb.append(randomChar);
        }

        return sb.toString();
    }
	
	
	 @RequestMapping("/access/register")
		public String pageregisteraccess(Model model) {
			
	        return "access/register";
		    
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
	 
    public String generateAlphanumericCode(int length) {
        String alphanumericCharacters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder randomCode = new StringBuilder();

        while (randomCode.length() < length) {
            char randomChar = UUID.randomUUID().toString().charAt(0);
            if (Pattern.matches("[A-Za-z0-9]", String.valueOf(randomChar))) {
                randomCode.append(randomChar);
            }
        }

        return randomCode.toString();
    }

}
