package com.beermartket.alcohol.service;

import com.beermartket.alcohol.model.TinTuc;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface TinTucService {
    List<TinTuc> getAllTinTuc();
    Page<TinTuc> getAllTinTucPaged(Pageable pageable);
    TinTuc getTinTucById(int maTinTuc);
    void addTinTuc(TinTuc tinTuc);
    void editTinTuc(TinTuc tinTuc);
    void deleteTinTuc(int maTinTuc);
	List<TinTuc> searchByTieuDe(String tieuDe);
}
