package com.beermartket.alcohol.model;

import java.io.Serializable;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Entity
@Table(name = "LoaiSanPham")
@NoArgsConstructor
@AllArgsConstructor
public class LoaiSanPham implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="MaLoaiSanPham")
	private int maLoaiSanPham;

	@Column(name="GhiChu")
	private String ghiChu;

	@Column(name="MaLoaiCha")
	private int maLoaiCha;

	@Column(name="TenLoaiSanPham")
	private String tenLoaiSanPham;

	//bi-directional many-to-one association to SanPham
	@OneToMany(mappedBy="loaiSanPham")
	private List<SanPham> sanPhams;

}