package com.beermartket.alcohol.model;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
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
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="MaLoaiSanPham")
	private int maLoaiSanPham;

	@Column(name="GhiChu")
	private String ghiChu;

	@Column(name="MaLoaiCha")
	private int maLoaiCha;

	@Column(name="TenLoaiSanPham")
	private String tenLoaiSanPham;
	
	@Column(name="NgayTao")
	private Timestamp ngayTao;

	@JsonIgnore
	@OneToMany(mappedBy="loaiSanPham")
	private List<SanPham> sanPhams;

	
	
}