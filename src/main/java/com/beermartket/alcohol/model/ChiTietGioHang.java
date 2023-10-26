package com.beermartket.alcohol.model;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Entity
@Table(name = "ChiTietGioHang")
@NoArgsConstructor
@AllArgsConstructor
public class ChiTietGioHang implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="MaChiTietGH")
	private int maChiTietGH;

	@Column(name="SoLuong")
	private int soLuong;

	//bi-directional many-to-one association to GioHang
	@ManyToOne
	@JoinColumn(name="MaGioHang")
	private GioHang gioHang;

	//bi-directional many-to-one association to SanPham
	@ManyToOne
	@JoinColumn(name="MaSanPham")
	private SanPham sanPham;


}