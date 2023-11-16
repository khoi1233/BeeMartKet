package com.beermartket.alcohol.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.beermartket.alcohol.model.ChiTietHoaDon;
import com.beermartket.alcohol.model.HoaDon;
import com.beermartket.alcohol.repository.ChiTietHoaDonReponsitory;
import com.beermartket.alcohol.repository.HoaDonReponsitory;

@RestController
public class HoaDon_restController {

	@Autowired
	HoaDonReponsitory hoaDonDao;
	
	@Autowired
	ChiTietHoaDonReponsitory hoaDonCTDao;
	
	@GetMapping("/rest/hoadon")
	public ResponseEntity<List<HoaDon>> findAll() {
		return ResponseEntity.ok(hoaDonDao.findAll());
	}
	
	@GetMapping("/rest/hoadon/{maHoaDon}")
	public ResponseEntity<List<ChiTietHoaDon>> findByID(@PathVariable Integer maHoaDon) {
	    // Chú ý: Sử dụng @PathVariable để lấy giá trị của biến mã hóa đơn từ đường dẫn.
	    
	    // Thực hiện việc tìm kiếm chi tiết hóa đơn dựa trên mã hóa đơn
	    List<ChiTietHoaDon> chiTietHoaDons = hoaDonCTDao.findByMaHoaDon(maHoaDon);

	    // Kiểm tra nếu không tìm thấy chi tiết hóa đơn
	    if (chiTietHoaDons.isEmpty()) {
	        // Trả về HTTP status code 404 Not Found nếu không tìm thấy
	        return ResponseEntity.notFound().build();
	    }

	    // Trả về danh sách chi tiết hóa đơn và HTTP status code 200 OK
	    return ResponseEntity.ok(chiTietHoaDons);
	}

}
