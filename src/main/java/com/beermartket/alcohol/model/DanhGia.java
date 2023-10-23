package com.beermartket.alcohol.model;

import java.io.Serializable;
import java.sql.Timestamp;
import java.time.LocalDateTime;

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
@Table(name = "DanhGia")
@NoArgsConstructor
@AllArgsConstructor
public class DanhGia implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="MaDanhGia")
	private int maDanhGia;

	@Column(name="Commet")
	private String commet;

	@Column(name="DiemDanhGia")
	private int diemDanhGia;

	@Column(name="NgayDanhGia")
	private LocalDateTime ngayDanhGia;

	//bi-directional many-to-one association to SanPham
	@ManyToOne
	@JoinColumn(name="MaSanPham")
	private SanPham sanPham;

	//bi-directional many-to-one association to TaiKhoan
	@ManyToOne
	@JoinColumn(name="MaTaiKhoan")
	private TaiKhoan taiKhoan;
}