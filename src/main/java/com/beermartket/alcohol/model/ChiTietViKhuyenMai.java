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
@Table(name = "ChiTietViKhuyenMai")
@NoArgsConstructor
@AllArgsConstructor
public class ChiTietViKhuyenMai implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "MaChiTietViKhuyenMai")
	private int maChiTietViKhuyenMai;

	// bi-directional many-to-one association to ViKhuyenMai
	@ManyToOne
	@JoinColumn(name = "MaViKhuyenMai")
	private ViKhuyenMai viKhuyenMai;

	// bi-directional many-to-one association to KhuyenMaiHoaDon
	@ManyToOne
	@JoinColumn(name = "MaKhuyenMai")
	private KhuyenMaiHoaDon khuyenMaiHoaDon;
}
