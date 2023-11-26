package com.beermartket.alcohol.model;

import java.io.Serializable;
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
@Table(name = "TaiKhoan")
@NoArgsConstructor
@AllArgsConstructor
public class TaiKhoan implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "MaTaiKhoan")
	private int maTaiKhoan;

<<<<<<< HEAD
	@Column(name = "Email")
=======

	@Column(name="Email")
>>>>>>> thanh02
	private String email;

	@Column(name = "HinhAnh")
	private String hinhAnh;

	@Column(name = "HoTen")
	private String hoTen;

	@Column(name = "MatKhau")
	private String matKhau;

	@Column(name = "MaHoaMatKhau")
	private String maHoaMatKhau;

<<<<<<< HEAD
	@Column(name = "TenDangNhap")
=======


	@Column(name="TenDangNhap")
>>>>>>> thanh02
	private String tenDangNhap;

	@Column(name = "NgayTao")
	private String ngayTao;

	// bi-directional many-to-one association to DanhGia
	@JsonIgnore
	@OneToMany(mappedBy = "taiKhoan")
	private List<DanhGia> danhGias;

	// bi-directional many-to-one association to GioHang
	@JsonIgnore
	@OneToMany(mappedBy = "taiKhoan")
	private List<GioHang> gioHangs;

	// bi-directional many-to-one association to HoaDon
	@JsonIgnore
	@OneToMany(mappedBy = "taiKhoan")
	private List<HoaDon> hoaDons;

	// bi-directional many-to-one association to ThanhVien
	@JsonIgnore
	@OneToMany(mappedBy = "taiKhoan")
	private List<ThanhVien> thanhViens;

	// bi-directional many-to-one association to ViKhuyenMai
	@JsonIgnore
	@OneToMany(mappedBy = "taiKhoan")
	private List<ThanhVien> viKhuyenMais;

	// bi-directional many-to-one association to DiaChi
	@JsonIgnore
	@OneToMany(mappedBy = "taiKhoan")
	private List<DiaChi> diaChis;

	// bi-directional many-to-one association to SoDienThoai
	@JsonIgnore
	@OneToMany(mappedBy = "taiKhoan")
	private List<SoDienThoai> soDienThoais;
}