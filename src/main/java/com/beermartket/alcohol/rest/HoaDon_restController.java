package com.beermartket.alcohol.rest;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
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

	@GetMapping("/rest/xacnhanhoadon")
	public ResponseEntity<List<HoaDon>> findByTrangThaiHD() {
		return ResponseEntity.ok(hoaDonDao.findByTrangThaiHoaDon());
	}

	@GetMapping("/rest/hoadonxacnhan")
	public ResponseEntity<List<HoaDon>> findByTrangThaiHD2() {
		return ResponseEntity.ok(hoaDonDao.findByTrangThaiHoaDon2());
	}
	
	@GetMapping("/rest/hoadondahuy")
	public ResponseEntity<List<HoaDon>> findByTrangThaiHD3() {
		return ResponseEntity.ok(hoaDonDao.findByTrangThaiHoaDon3());
	}
	
	@GetMapping("/hoadon/{maHoaDon}")
	public HoaDon getHoaDonByMaHoaDon(@PathVariable int maHoaDon) {
		Optional<HoaDon> optionalHoaDon = hoaDonDao.findById(maHoaDon);
		if (optionalHoaDon.isPresent()) {
			return optionalHoaDon.get();
		} else {
			// Nếu không tìm thấy hóa đơn, có thể trả về một response HTTP 404 Not Found.
			// Hoặc bạn có thể trả về một giá trị mặc định hoặc làm bất kỳ xử lý nào phù hợp
			// với ứng dụng của bạn.
			return null;
		}
	}

	@GetMapping("/rest/hoadon/thoigian/{maHoaDon}")
	public ResponseEntity<Integer> tinhThoiGian(@PathVariable Integer maHoaDon) {
		Integer thoigian = hoaDonDao.findTimeDifferenceByMaHoaDon(maHoaDon);
		return ResponseEntity.ok(thoigian);
	}

	@GetMapping("/rest/chitiet/{maHoaDon}")
	public ResponseEntity<List<ChiTietHoaDon>> findByID(@PathVariable Integer maHoaDon) {
		List<ChiTietHoaDon> chiTietHoaDons = hoaDonCTDao.findByMaHoaDon(maHoaDon);
		return ResponseEntity.ok(chiTietHoaDons);
	}

	@PutMapping("/rest/{maHoaDon}/cap-nhat-trang-thai/{trangThaiHoaDon}")
	public ResponseEntity<HoaDon> capNhatTrangThaiHoaDon(@PathVariable int maHoaDon, @PathVariable String trangThaiHoaDon) {
		try {
			Optional<HoaDon> hoaDonOptional = hoaDonDao.findById(maHoaDon);

			if (!hoaDonOptional.isPresent()) {
				return ResponseEntity.notFound().build();
			}

			HoaDon hoaDon = hoaDonOptional.get();
			hoaDon.setTrangThaiHoaDon(trangThaiHoaDon);
			hoaDonDao.save(hoaDon);

			return ResponseEntity.ok(hoaDon);
		} catch (Exception e) {
			return ResponseEntity.badRequest().build();
		}
	}

}
