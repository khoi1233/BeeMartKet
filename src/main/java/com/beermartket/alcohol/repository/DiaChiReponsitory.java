package com.beermartket.alcohol.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.beermartket.alcohol.model.DiaChi;

@Repository
public interface DiaChiReponsitory extends JpaRepository<DiaChi, Integer>{

}
