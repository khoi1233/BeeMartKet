package com.beermartket.alcohol.rest;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.beermartket.alcohol.model.ChiTietPhieuNhapHang;
import com.beermartket.alcohol.model.GioHang;
import com.beermartket.alcohol.model.Hinh;
import com.beermartket.alcohol.model.HoaDon;
import com.beermartket.alcohol.model.PhieuNhapHang;
import com.beermartket.alcohol.model.SanPham;
import com.beermartket.alcohol.repository.CTPhieuNhapHangReponsitory;
import com.beermartket.alcohol.repository.NhaCungCapReponsitory;
import com.beermartket.alcohol.repository.PhieuNhapHangReponsitory;

@RestController
public class PhieuNhapHang_restController {

	@Autowired
	PhieuNhapHangReponsitory phieuDao;
	
	@Autowired
	CTPhieuNhapHangReponsitory ctphieuDao;
	
	@Autowired
	NhaCungCapReponsitory nccDAO;
	
	@GetMapping("/rest/phieuhang")
	public ResponseEntity<List<PhieuNhapHang>> findAll() {
		return ResponseEntity.ok(phieuDao.findAll());
	}
	
	@GetMapping("rest/phieuhang/{maPhieuNhap}")
	public ResponseEntity<List<ChiTietPhieuNhapHang>> getCTPhieuNhap(@PathVariable Integer maPhieuNhap) {
		List<ChiTietPhieuNhapHang> phieus = ctphieuDao.findByMaPhieuNhap(maPhieuNhap);

		if (phieus != null) {
			return ResponseEntity.ok(phieus);
		} else {
			return ResponseEntity.notFound().build();
		}

	}
	
	@PostMapping("/add/phieunhaphang/{maNhaCungCap}")
	public ResponseEntity<PhieuNhapHang> createPhieuNhapHang(
	  
	    @RequestBody List<SanPham> sanphamdachon,
	    @PathVariable int maNhaCungCap
	) {
	    PhieuNhapHang phieu = new PhieuNhapHang();
	    
	    // Lấy thời gian hiện tại
	    LocalDateTime currentTime = LocalDateTime.now();        
	    // Chuyển đổi thành kiểu Timestamp
	    Timestamp currentTimestamp = Timestamp.valueOf(currentTime);

	    // Gán thời gian hiện tại cho trường NgayTao
	    phieu.setNgayNhap(currentTimestamp);
	    phieu.setNhaCungCap(nccDAO.findById(maNhaCungCap).orElse(null));
//	    phieu.setGhiChu(phieunhaphang.getGhiChu());
//	    phieu.setTongTien(phieunhaphang.getTongTien());
	    phieuDao.save(phieu);

	    // Lưu thông tin chi tiết phiếu nhập hàng từ danh sách sản phẩm đã chọn
	    for (SanPham sp : sanphamdachon) {
	        ChiTietPhieuNhapHang ctphieu = new ChiTietPhieuNhapHang();
	        ctphieu.setSanPham(sp);
	        ctphieu.setGiaNhap(sp.getGiaNhap());
	        ctphieu.setPhieuNhapHang(phieu);
	        ctphieuDao.save(ctphieu);
	    }

	    return ResponseEntity.ok(phieu);
	}

}
