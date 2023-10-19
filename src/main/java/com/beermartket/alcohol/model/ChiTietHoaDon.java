package com.beermartket.alcohol.model;

import java.io.Serializable;

import org.hibernate.annotations.NamedQuery;

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
@Table(name = "ChiTietHoaDon")
@NoArgsConstructor
@AllArgsConstructor
public class ChiTietHoaDon implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="MaChiTietHD")
	private int maChiTietHD;

	@Column(name="GhiChu")
	private String ghiChu;

	@Column(name="GiaBan")
	private double giaBan;

	@Column(name="SoLuong")
	private int soLuong;

	//bi-directional many-to-one association to HoaDon
	@ManyToOne
	@JoinColumn(name="MaHoaDon")
	private HoaDon hoaDon;

	//bi-directional many-to-one association to SanPham
	@ManyToOne
	@JoinColumn(name="MaSanPham")
	private SanPham sanPham;
}