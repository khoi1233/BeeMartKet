package com.beermartket.alcohol.admin.controller;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.apache.commons.io.FilenameUtils;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.beermartket.alcohol.model.TaiKhoan;
import com.beermartket.alcohol.model.TaiKhoanAdmin;
import com.beermartket.alcohol.repository.TaiKhoanAdminRepository;
import com.beermartket.alcohol.repository.TaiKhoanRepository;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpSession;

@Controller
public class AccountAdminController {
	@Autowired
	HttpSession session;
	
	@Autowired
	private JavaMailSender javaMailSender;
	
    @Autowired
    TaiKhoanAdminRepository taiKhoanAdmin;
    @Autowired
    TaiKhoanRepository taiKhoan;

    @GetMapping("/admin/listAccountAdmin")
    public String showAdminAccounts(Model model) {
    	String trangthai = "Đang hoạt động"; 
    	String chucvu = "1"; 
        List<TaiKhoanAdmin> taiKhoanAdminList = taiKhoanAdmin.findByTenDangNhapAndTrangThaiAndChucVu(trangthai,chucvu);
        model.addAttribute("taiKhoanAdminList", taiKhoanAdminList);
        return "admin/view/listAccountAdmin";
    }
    
    @GetMapping("/admin/listAccountAdminLock")
    public String showAccountsAdminLock(Model model) {
    	String trangthai = "Đã khóa"; 
    	String chucvu = "1"; 
        List<TaiKhoanAdmin> taiKhoanAdminListLock = taiKhoanAdmin.findByTenDangNhapAndTrangThaiAndChucVu(trangthai,chucvu);
        model.addAttribute("taiKhoanAdminListLock", taiKhoanAdminListLock);
        return "admin/view/listAccountLockAdmin";
    }
    @PostMapping("/admin/lockAccountAdmin")
    @ResponseBody
    public String lockAccountAdmin(@RequestParam String accountId) {
    	String trangthai = "Đang hoạt động"; 
        TaiKhoanAdmin tkupdate = taiKhoanAdmin.findByTenDangNhap(accountId,trangthai);
        tkupdate.setTrangThai("Đã khóa");
        taiKhoanAdmin.save(tkupdate);
        return "success"; 
    }
    @PostMapping("/admin/listAccountLockAdmin")
    @ResponseBody
    public String openAccountLockAdmin(@RequestParam String accountId) {
    	String trangthai = "Đã khóa";
        TaiKhoanAdmin tkupdate = taiKhoanAdmin.findByTenDangNhap(accountId,trangthai);
        tkupdate.setTrangThai("Đang hoạt động");
        taiKhoanAdmin.save(tkupdate);
        return "success"; 
    }
    
    
    
    
    
    
    @GetMapping("/admin/listAccount")
    public String showAccounts(Model model) {
    	String trangthai = "Đang hoạt động";      
        List<TaiKhoan> taiKhoanList = taiKhoan.findByTrangThai(trangthai);
        model.addAttribute("taiKhoanList", taiKhoanList);
        return "admin/view/listAccount";
    }
    @GetMapping("/admin/listAccountLock")
    public String showAccountsLock(Model model) {
    	String trangthaiLock = "Đã khóa";      
        List<TaiKhoan> taiKhoanListLock = taiKhoan.findByTrangThai(trangthaiLock);
        model.addAttribute("taiKhoanListLock", taiKhoanListLock);
        return "admin/view/listAccountLock";
    }
    
    @PostMapping("/admin/lockAccount")
    @ResponseBody
    public String lockAccount(@RequestParam String accountId) {
    	String trangthai = "Đang hoạt động"; 
        TaiKhoan tkupdate = taiKhoan.findByTenDangNhap(accountId,trangthai);
        tkupdate.setTrangThai("Đã khóa");
        taiKhoan.save(tkupdate);
        return "success"; 
    }
    @PostMapping("/admin/listAccountLock")
    @ResponseBody
    public String openAccountLock(@RequestParam String accountId) {
    	String trangthai = "Đang hoạt động"; 
        TaiKhoan tkupdate = taiKhoan.findByTenDangNhap(accountId,trangthai);
        tkupdate.setTrangThai("Đang hoạt động");
        taiKhoan.save(tkupdate);
        return "success"; 
    }
    
    
    
    @GetMapping("/check-username")
    public ResponseEntity<?> checkUsernameExistence(@RequestParam String username) {
        boolean isUsernameExists = taiKhoanAdmin.existsByTenDangNhap(username);
        Map<String, Boolean> response = new HashMap<>();
        response.put("isUsernameExists", isUsernameExists);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/check-email")
    public ResponseEntity<?> checkEmailExistence(@RequestParam String email) {
        boolean isEmailExists = taiKhoanAdmin.existsByEmail(email);

        Map<String, Boolean> response = new HashMap<>();
        response.put("isEmailExists", isEmailExists);

        return ResponseEntity.ok(response);
    }
    @GetMapping("/check-phone-number")
    public ResponseEntity<?> checkPhoneNumberExistence(@RequestParam String phoneNumber) {
        boolean isPhoneNumberExists = taiKhoanAdmin.existsByPhoneNumber(phoneNumber);

        Map<String, Boolean> response = new HashMap<>();
        response.put("isPhoneNumberExists", isPhoneNumberExists);

        return ResponseEntity.ok(response);
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
    private String generateUniqueFilename(String filename) {
	    // Implement a logic to generate a unique filename (e.g., append timestamp)
	    return FilenameUtils.getBaseName(filename) + "_" + System.currentTimeMillis() + "." + FilenameUtils.getExtension(filename);
	}
    
    private void sendByEmailResetPassword(String toEmail, String tenDangNhap, String matKhau) {

        MimeMessage message = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            String Subject = "BEE MARKET XIN CHÀO, TÀI KHOẢN CỦA BẠN";
            String icon1 = "<h3 style =\" style= color: black;\">Tài khoản đăng nhập của bạn là::</h3>";
            String styledIcon1 = icon1+"<br><h3> Tên Đăng nhập: <p style=\"text-decoration: underline;\" >" + tenDangNhap + "<p></h3>"+ "<br><h3> Mật khẩu: <p style=\"text-decoration: underline;\" >" + matKhau + "<p></h3>";
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
    
    @PostMapping("/addAdmin-form")
    public ResponseEntity<String> submitForm(
            @RequestParam("fullName") String fullName,
            @RequestParam("phoneNumber") String phoneNumber,
            @RequestParam("username") String username,
            @RequestParam("email") String email,
            @RequestParam("address") String address,
            @RequestParam(value = "file", required = false) MultipartFile img
    ) {
        // Your logic to process the form data and handle the file
        try {
        	LocalDateTime currentDateTime = LocalDateTime.now();
		 	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		 	String formattedDateTime = currentDateTime.format(formatter);
		 	String salt = BCrypt.gensalt(12);
		 	String randompass = generateRandomCode(9);
		    String hashedUserInputPassword = BCrypt.hashpw(randompass, salt);
        	 

            TaiKhoanAdmin tkadminnew = new TaiKhoanAdmin();
            
            if (img != null) {
            	String filename = img.getOriginalFilename();
            	Path uploadPath = Paths.get("src/main/resources/static/images/avatar");

    	        if (!Files.exists(uploadPath)) {
    	            Files.createDirectories(uploadPath);
    	        }
    	        if (tkadminnew.getHinhAnh() != null && !tkadminnew.getHinhAnh().isEmpty()) {
    	            Path oldFilePath = uploadPath.resolve(tkadminnew.getHinhAnh());
    	            Files.deleteIfExists(oldFilePath);
    	        }
    	        Path filePath = uploadPath.resolve(filename);

    	        if (Files.isDirectory(filePath)) {
    	            filePath = filePath.resolve(filename);
    	        }


    	        if (Files.exists(filePath)) {

    	            filename = generateUniqueFilename(filename);
    	            filePath = uploadPath.resolve(filename);
    	        }

    	        Files.copy(img.getInputStream(), filePath);
            	tkadminnew.setChucVu(1);
            	tkadminnew.setDiaChi(address);
            	tkadminnew.setEmail(email);
            	tkadminnew.setHinhAnh(filename);
            	tkadminnew.setHoTen(fullName);
            	tkadminnew.setMaHoaMatKhau(salt);
            	tkadminnew.setMatKhau(hashedUserInputPassword+".dmk");
            	tkadminnew.setNgayTao(formattedDateTime);
            	tkadminnew.setSoDienThoai(phoneNumber);
            	tkadminnew.setTrangThai("Đang hoạt động");
            	tkadminnew.setTenDangNhap(username);
            	taiKhoanAdmin.save(tkadminnew);
                System.out.println("File Name: " + img.getOriginalFilename());
                System.out.println("File Size: " + img.getSize());
            }
            else {
            	tkadminnew.setChucVu(1);
            	tkadminnew.setDiaChi(address);
            	tkadminnew.setEmail(email);
            	tkadminnew.setHoTen(fullName);
            	tkadminnew.setMaHoaMatKhau(salt);
            	tkadminnew.setMatKhau(hashedUserInputPassword+".dmk");
            	tkadminnew.setNgayTao(formattedDateTime);
            	tkadminnew.setSoDienThoai(phoneNumber);
            	tkadminnew.setTrangThai("Đang hoạt động");
            	tkadminnew.setTenDangNhap(username);
            	taiKhoanAdmin.save(tkadminnew);
            }
            sendByEmailResetPassword(email,username,randompass);
            
            return new ResponseEntity<>("Form submitted successfully", HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();

            return new ResponseEntity<>("Error processing the form", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    
    @RequestMapping("/admin/EditedAccount")
    public String editAccount(Model model, @RequestParam("accountId") String accountId) {
        // Fetch account details by accountId from the service
    	String trangthai = "Đang hoạt động"; 
        TaiKhoanAdmin tkedit = taiKhoanAdmin.findByTenDangNhap(accountId,trangthai);
       
        // Add the account details to the model
        if (tkedit!=null) {
        	model.addAttribute("tkedit", tkedit);
            
            return "/admin/view/updateAccount";
		}
        return "redirect:/admin/listAccountAdmin";
    }

    @PostMapping("/editAdmin-form")
    public ResponseEntity<String> editForm(
            @RequestParam("fullName") String fullName,
            @RequestParam("phoneNumber") String phoneNumber,
            @RequestParam("username") String username,
            @RequestParam("email") String email,
            @RequestParam("address") String address,
            @RequestParam(value = "file", required = false) MultipartFile img
    ) {
        // Your logic to process the form data and handle the file
        try {
        	LocalDateTime currentDateTime = LocalDateTime.now();
		 	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		 	String formattedDateTime = currentDateTime.format(formatter);
		 	String salt = BCrypt.gensalt(12);
		 	String randompass = generateRandomCode(9);
		    String hashedUserInputPassword = BCrypt.hashpw(randompass, salt);
		    String trangthai = "Đang hoạt động"; 
            TaiKhoanAdmin tkedit = taiKhoanAdmin.findByTenDangNhap(username,trangthai);

            TaiKhoanAdmin tkadminnew = new TaiKhoanAdmin();
            
            if (img != null) {
            	String filename = img.getOriginalFilename();
            	Path uploadPath = Paths.get("src/main/resources/static/images/avatar");

    	        if (!Files.exists(uploadPath)) {
    	            Files.createDirectories(uploadPath);
    	        }
    	        if (tkadminnew.getHinhAnh() != null && !tkadminnew.getHinhAnh().isEmpty()) {
    	            Path oldFilePath = uploadPath.resolve(tkadminnew.getHinhAnh());
    	            Files.deleteIfExists(oldFilePath);
    	        }
    	        Path filePath = uploadPath.resolve(filename);

    	        if (Files.isDirectory(filePath)) {
    	            filePath = filePath.resolve(filename);
    	        }


    	        if (Files.exists(filePath)) {

    	            filename = generateUniqueFilename(filename);
    	            filePath = uploadPath.resolve(filename);
    	        }

    	        Files.copy(img.getInputStream(), filePath);
            	tkedit.setDiaChi(address);
            	tkedit.setEmail(email);
            	tkedit.setHinhAnh(filename);
            	tkedit.setHoTen(fullName);
            	tkedit.setSoDienThoai(phoneNumber);
            	taiKhoanAdmin.save(tkedit);
                System.out.println("File Name: " + img.getOriginalFilename());
                System.out.println("File Size: " + img.getSize());
            }
            else {
            	
               
            	tkedit.setDiaChi(address);
            	tkedit.setEmail(email);
            	tkedit.setHoTen(fullName);
            	tkedit.setSoDienThoai(phoneNumber);
            	tkedit.setHinhAnh(tkedit.getHinhAnh());
            	taiKhoanAdmin.save(tkedit);
            }

            
            return new ResponseEntity<>("Form submitted successfully", HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();

            return new ResponseEntity<>("Error processing the form", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @GetMapping("/check-emailedit")
    public ResponseEntity<?> editcheckEmailExistence(@RequestParam String email,@RequestParam String username) {
    	String trangthai = "Đang hoạt động"; 
    	Map<String, Boolean> response = new HashMap<>();
        TaiKhoanAdmin tkedit = taiKhoanAdmin.findByTenDangNhap(username,trangthai);
        if (tkedit.getEmail().equals(email)) {
        	response.put("isEmailExists", null);
		}
        else {
        	boolean isEmailExists = taiKhoanAdmin.existsByEmail(email);
            response.put("isEmailExists", isEmailExists);
		}
        

        return ResponseEntity.ok(response);
    }
    @GetMapping("/check-phone-numberedit")
    public ResponseEntity<?> editcheckPhoneNumberExistence(@RequestParam String phoneNumber, @RequestParam String username) {
        System.out.println(phoneNumber);
        System.out.println(username);
        String trangthai = "Đang hoạt động"; 
        TaiKhoanAdmin tkedit = taiKhoanAdmin.findByTenDangNhap(username,trangthai);
        Map<String, Boolean> response = new HashMap<>();
        if (tkedit.getSoDienThoai().equals(phoneNumber)) {
        	response.put("isPhoneNumberExists", null);
		}
        else {
        	boolean isPhoneNumberExists = taiKhoanAdmin.existsByPhoneNumber(phoneNumber);
            response.put("isPhoneNumberExists", isPhoneNumberExists);
		}
        

        return ResponseEntity.ok(response);
    }
    
}
