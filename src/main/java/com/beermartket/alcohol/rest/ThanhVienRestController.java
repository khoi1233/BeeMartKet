package com.beermartket.alcohol.rest;

import com.beermartket.alcohol.model.ThanhVien;
import com.beermartket.alcohol.repository.ThanhVienRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
public class ThanhVienRestController {

    @Autowired
    private ThanhVienRepository thanhVienRepository;

    @GetMapping("/rest/thanhviens")
    public ResponseEntity<List<ThanhVien>> findAll() {
        List<ThanhVien> thanhViens = thanhVienRepository.findAll();
        return ResponseEntity.ok(thanhViens);
    }

    @GetMapping("rest/{maThanhVien}")
    public ResponseEntity<ThanhVien> getThanhVienByMaThanhVien(@PathVariable Long maThanhVien) {
        Optional<ThanhVien> thanhVien = thanhVienRepository.findById(maThanhVien);
        return thanhVien.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }
}
