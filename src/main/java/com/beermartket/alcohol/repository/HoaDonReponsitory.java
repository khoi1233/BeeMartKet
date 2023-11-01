package com.beermartket.alcohol.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.beermartket.alcohol.model.HoaDon;


@Repository
public interface HoaDonReponsitory extends JpaRepository<HoaDon, Integer>{

}
