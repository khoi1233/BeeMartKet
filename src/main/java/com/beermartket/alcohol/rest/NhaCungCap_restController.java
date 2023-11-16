package com.beermartket.alcohol.rest;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.beermartket.alcohol.model.NhaCungCap;
import com.beermartket.alcohol.model.SanPham;
import com.beermartket.alcohol.repository.NhaCungCapReponsitory;

@RestController
public class NhaCungCap_restController {
	@Autowired
	NhaCungCapReponsitory nhaccDao;

	@GetMapping("/rest/nhacungcap")
	public ResponseEntity<List<NhaCungCap>> findAll() {
		return ResponseEntity.ok(nhaccDao.findAll());
	}
	
	 @GetMapping("/rest/nhacungcap/{maNhaCungCap}")
	    public ResponseEntity<Optional<NhaCungCap>> getNhaCungCapByMaNhaCungCap(@PathVariable int maNhaCungCap) {
	        Optional<NhaCungCap> nhaCungCap = nhaccDao.findById(maNhaCungCap);

	        if (nhaCungCap != null) {
	        	  return ResponseEntity.ok(nhaCungCap);
	        } else {
	            return ResponseEntity.notFound().build();
	        }
	    }
	 
}
