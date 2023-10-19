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
@Table(name = "Hinh")
@NoArgsConstructor
@AllArgsConstructor
public class Hinh implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="MaHinh")
	private int maHinh;

	@Column(name="TenHinh")
	private String tenHinh;

	//bi-directional many-to-one association to SanPham
	@ManyToOne
	@JoinColumn(name="MaSanPham")
	private SanPham sanPham;
}