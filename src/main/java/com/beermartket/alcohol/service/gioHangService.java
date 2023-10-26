package com.beermartket.alcohol.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.beermartket.alcohol.model.ChiTietGioHang;
import com.beermartket.alcohol.repository.ChiTietGioHangReponsitory;

@Service
public class gioHangService {
    @Autowired
    private ChiTietGioHangReponsitory CTgiohangDAO;
    
    public ChiTietGioHang addToCart(ChiTietGioHang ctgh) {
        return CTgiohangDAO.save(ctgh);
    }
    
    public void remove(int maSanPham) {
    	CTgiohangDAO.deleteById(maSanPham);
    }
    
    public void removeAll() {
    	CTgiohangDAO.deleteAll();
    }
}