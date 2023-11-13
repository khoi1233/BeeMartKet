package com.beermartket.alcohol.rest;

import com.beermartket.alcohol.model.SanPham;
import com.beermartket.alcohol.model.TinTuc;
import com.beermartket.alcohol.repository.TinTucRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
public class TinTucRestController {

	@Autowired
	private TinTucRepository tinTucRepository;

	@GetMapping("rest/tintuc")
	public ResponseEntity<List<TinTuc>> findAll() {
		List<TinTuc> tinTucs = tinTucRepository.findAll();
		return ResponseEntity.ok(tinTucs);
	}

	@GetMapping("rest/{maTinTuc}")
	public ResponseEntity<TinTuc> getTinTucByMaTinTuc(@PathVariable Integer maTinTuc) {
		Optional<TinTuc> tinTuc = tinTucRepository.findById(maTinTuc);
        return tinTuc.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());

	}
}
