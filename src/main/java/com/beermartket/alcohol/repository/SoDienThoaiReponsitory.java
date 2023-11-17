package com.beermartket.alcohol.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.beermartket.alcohol.model.SoDienThoai;

@Repository
public interface SoDienThoaiReponsitory extends JpaRepository<SoDienThoai, Integer>{

}
