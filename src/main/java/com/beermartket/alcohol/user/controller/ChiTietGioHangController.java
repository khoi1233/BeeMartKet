package com.beermartket.alcohol.user.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.beermartket.alcohol.constant.SessionAttr;
import com.beermartket.alcohol.model.ChiTietGioHang;
import com.beermartket.alcohol.model.GioHang;
import com.beermartket.alcohol.model.SanPham;
import com.beermartket.alcohol.model.TaiKhoan;
import com.beermartket.alcohol.repository.ChiTietGioHangReponsitory;
import com.beermartket.alcohol.repository.GioHangRepository;
import com.beermartket.alcohol.repository.SanPhamRepository;
import com.beermartket.alcohol.repository.TaiKhoanRepository;
import com.beermartket.alcohol.service.gioHangService;

import jakarta.servlet.http.HttpSession;

@RequestMapping("/cart")
@RestController
public class ChiTietGioHangController {

	@Autowired
	ChiTietGioHangReponsitory ctgiohangDao;

	@Autowired
	GioHangRepository giohangDao;

	@Autowired
	SanPhamRepository sanPhamDAO;

	@Autowired
	gioHangService gioHang;
	
	@Autowired
	HttpSession session;
	
	@Autowired
	TaiKhoanRepository taikhoanrp;

	
	
	@GetMapping("/getUsername")
    public int getUsername() {
		int maGioHang = 0;
        // Lấy tên đăng nhập từ session
        String username = (String) session.getAttribute(SessionAttr.User);
        
        if (username != null) {
        	TaiKhoan tk = taikhoanrp.findByTenDangNhap(username);
        	List<GioHang> gh;
        	gh = tk.getGioHangs();
        	for (GioHang gioHang : gh) {
        		maGioHang = gioHang.getMaGioHang();
			}
            return maGioHang;
        }else {
        	return maGioHang;
        }
       
    }

	@GetMapping("/rest/{maGioHang}/chitietgiohang")
	public ResponseEntity<List<ChiTietGioHang>> getChiTietGioHang(@PathVariable Integer maGioHang) {
		// Lấy thông tin giỏ hàng từ mã giỏ hàng
		GioHang gioHang = giohangDao.findById(maGioHang).orElse(null);

		if (gioHang != null) {
			// Sử dụng phương thức findByGioHang để lấy danh sách chi tiết giỏ hàng
			List<ChiTietGioHang> chiTietGioHangList = ctgiohangDao.findByGioHang(gioHang);

			if (!chiTietGioHangList.isEmpty()) {
				return ResponseEntity.ok(chiTietGioHangList);
			} else {
				return ResponseEntity.notFound().build();
			}
		} else {
			return ResponseEntity.notFound().build();
		}
	}

	// lưu giở hàng vào cơ sở dữ liệu

	@PostMapping("/add/{maGioHang}/{maSanPham}")
	public ChiTietGioHang addToCart(@PathVariable Integer maGioHang, @PathVariable Integer maSanPham) {
		ChiTietGioHang ctgh = new ChiTietGioHang(); // Tạo một thể hiện mới
		ctgh.setSoLuong(1);
//	    ctgh.setSoLuong(maSanPham);

		Optional<SanPham> sanPhamOptional = sanPhamDAO.findById(maSanPham);

		if (sanPhamOptional.isPresent()) {
			ctgh.setSanPham(sanPhamOptional.get());

		} else {
			// Xử lý trường hợp không tìm thấy sản phẩm
		}

		Optional<GioHang> gioHangOptional = giohangDao.findById(maGioHang);
		if (gioHangOptional.isPresent()) {
			ctgh.setGioHang(gioHangOptional.get());
		} else {
			// Xử lý trường hợp không tìm thấy giỏ hàng
		}

		return ctgiohangDao.save(ctgh);
	}
	
	@PutMapping("/update/{id}")
    public ResponseEntity<ChiTietGioHang> updateQuantity(@PathVariable Integer id, @RequestBody ChiTietGioHang updatedChiTietGioHang) {
        ChiTietGioHang existingChiTietGioHang = ctgiohangDao.findById(id).orElse(null);

        if (existingChiTietGioHang == null) {
            return ResponseEntity.notFound().build();
        }

        // Kiểm tra và xử lý số lượng không âm
        if (updatedChiTietGioHang.getSoLuong() >= 0) {
            existingChiTietGioHang.setSoLuong(updatedChiTietGioHang.getSoLuong());

            // Lưu lại thông tin sản phẩm vào cơ sở dữ liệu
            ctgiohangDao.save(existingChiTietGioHang);

            return ResponseEntity.ok(existingChiTietGioHang);
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

	@DeleteMapping("/delete/{maChiTietGH}")
	public void remove(@PathVariable Integer maChiTietGH) {
		ctgiohangDao.deleteById(maChiTietGH);
	}
}
