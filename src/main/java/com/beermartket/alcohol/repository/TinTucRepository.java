package com.beermartket.alcohol.repository;

import com.beermartket.alcohol.model.TinTuc;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TinTucRepository extends JpaRepository<TinTuc, Integer> {
    List<TinTuc> findByTieuDeContaining(String tieuDe);

}
