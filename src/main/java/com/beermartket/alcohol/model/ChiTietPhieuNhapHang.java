package com.beermartket.alcohol.model;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "ChiTietPhieuNhapHang")
@NoArgsConstructor
@AllArgsConstructor
public class ChiTietPhieuNhapHang implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "MaChiTietNH")
	private int maChiTietNH;

	@Column(name = "GhiChu")
	private String ghiChu;

	@Column(name = "GiaNhap")
	private double giaNhap;

	@Column(name = "MaSanPham")
	private int maSanPham;

	@Column(name = "SoLuongNhap")
	private int soLuongNhap;

	@ManyToOne
	@JoinColumn(name = "MaPhieuNhap")
	private PhieuNhapHang phieuNhapHang;

}