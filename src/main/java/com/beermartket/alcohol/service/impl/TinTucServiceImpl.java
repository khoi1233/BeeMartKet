package com.beermartket.alcohol.service.impl;
import com.beermartket.alcohol.model.TinTuc;
import com.beermartket.alcohol.repository.TinTucRepository;
import com.beermartket.alcohol.service.TinTucService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TinTucServiceImpl implements TinTucService {

    private final TinTucRepository tinTucRepository;

    @Autowired
    public TinTucServiceImpl(TinTucRepository tinTucRepository) {
        this.tinTucRepository = tinTucRepository;
    }

    @Override
    public List<TinTuc> getAllTinTuc() {
        return tinTucRepository.findAll();
    }

    @Override
    public Page<TinTuc> getAllTinTucPaged(Pageable pageable) {
        return tinTucRepository.findAll(pageable);
    }

    @Override
    public TinTuc getTinTucById(int maTinTuc) {
        return tinTucRepository.findById(maTinTuc).orElse(null);
    }

    @Override
    public void addTinTuc(TinTuc tinTuc) {
        tinTucRepository.save(tinTuc);
    }

    @Override
    public void editTinTuc(TinTuc tinTuc) {
        tinTucRepository.save(tinTuc);
    }

    @Override
    public void deleteTinTuc(int maTinTuc) {
        tinTucRepository.deleteById(maTinTuc);
    }
    public List<TinTuc> searchByTieuDe(String tieuDe) {
        return tinTucRepository.findByTieuDeContaining(tieuDe);
    }
}
