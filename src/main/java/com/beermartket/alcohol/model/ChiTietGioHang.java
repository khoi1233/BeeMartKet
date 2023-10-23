package com.beermartket.alcohol.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQuery;
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