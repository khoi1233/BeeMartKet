package com.beermartket.alcohol.service;

import com.beermartket.alcohol.model.TinTuc;
import com.beermartket.alcohol.repository.TinTucRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TinTucService {

    private final TinTucRepository tinTucRepository;

    @Autowired
    public TinTucService(TinTucRepository tinTucRepository) {
        this.tinTucRepository = tinTucRepository;
    }

    public List<TinTuc> getAllTinTuc() {
        return tinTucRepository.findAll();
    }

    public Optional<TinTuc> getTinTucById(int id) {
        return tinTucRepository.findById(id);
    }

    public TinTuc saveTinTuc(TinTuc tinTuc) {
        return tinTucRepository.save(tinTuc);
    }

    public void deleteTinTucById(int id) {
        tinTucRepository.deleteById(id);
    }
    public List<TinTuc> searchTinTucByTitle(String title) {
        return tinTucRepository.findByTieuDeContaining(title);
    }

}
