package com.beermartket.alcohol.model;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

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
@Table(name = "TaiKhoan")
@NoArgsConstructor
@AllArgsConstructor
public class DiaChi implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "MaDiaChi")
	private int maDiaChi;

	@Column(name = "DiaChi")
	private String diaChi;

	@Column(name = "ChiTietDiaChi")
	private String chiTietDiaChi;

	// bi-directional many-to-one association to TaiKhoan
	@ManyToOne
	@JoinColumn(name = "MaTaiKhoan")
	private TaiKhoan taiKhoan;
}
