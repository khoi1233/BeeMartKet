package com.beermartket.alcohol.rest;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.beermartket.alcohol.model.KhuyenMaiHoaDon;
import com.beermartket.alcohol.model.LoaiSanPham;
import com.beermartket.alcohol.repository.KhuyenMaiHoaDonReponsitory;

@RestController
public class KhuyenMaiHoaDonRest {

	@Autowired
	KhuyenMaiHoaDonReponsitory kmhdDAO;
	@GetMapping("/rest/voucher")
	public ResponseEntity<List<KhuyenMaiHoaDon>> findAll() {
		return ResponseEntity.ok(kmhdDAO.findAll());
	}
	
	@GetMapping("/voucher/{maKhuyenMai}")
    public ResponseEntity<KhuyenMaiHoaDon> getKhuyenMaiByMa(@PathVariable String maKhuyenMai) {
        KhuyenMaiHoaDon khuyenMai = kmhdDAO.findByMaKhuyenMai(maKhuyenMai);

        if (khuyenMai != null) {
        	return ResponseEntity.ok(khuyenMai);
        } else {
        	return ResponseEntity.notFound().build();
        }
    }
	
	@PutMapping("/rest/update/promotion/{maKhuyenMai}")
	public ResponseEntity<KhuyenMaiHoaDon> updateKhuyenMai(@PathVariable String maKhuyenMai,
	        @RequestBody KhuyenMaiHoaDon khuyenmai) {

	    // Kiểm tra xem khuyến mãi có tồn tại không
	    KhuyenMaiHoaDon existingKhuyenMai = kmhdDAO.findByMaKhuyenMai(maKhuyenMai);
	    if (existingKhuyenMai != null) {
	        existingKhuyenMai.setTenKhuyenMai(khuyenmai.getTenKhuyenMai());
	        existingKhuyenMai.setHinh(khuyenmai.getHinh());
	        existingKhuyenMai.setSoLuong(khuyenmai.getSoLuong());
	        existingKhuyenMai.setChiecKhau(khuyenmai.getChiecKhau());
	        existingKhuyenMai.setGiaTriKhuyenMai(khuyenmai.getGiaTriKhuyenMai());
	        existingKhuyenMai.setGiaTriToiThieu(khuyenmai.getGiaTriToiThieu());
	        existingKhuyenMai.setNgayTao(khuyenmai.getNgayTao());
	        existingKhuyenMai.setNgayKetThuc(khuyenmai.getNgayKetThuc());
	        existingKhuyenMai.setTrangThai(khuyenmai.getTrangThai() == true);
	        existingKhuyenMai.setLoai(khuyenmai.getLoai() == true);
	        kmhdDAO.save(existingKhuyenMai);
	        return ResponseEntity.ok(existingKhuyenMai);
	    } else {
	        return ResponseEntity.badRequest().build();
	    }
	}
	@PostMapping("/add/promotion")
	public ResponseEntity<KhuyenMaiHoaDon> addKhuyenMai(@RequestBody KhuyenMaiHoaDon khuyenmai) {
		try {
			
			KhuyenMaiHoaDon khuyenmaiDon = kmhdDAO.save(khuyenmai);
			return ResponseEntity.status(HttpStatus.CREATED).body(khuyenmaiDon);
		} catch (Exception e) {
			// Debug: In ra lỗi nếu có
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

}
