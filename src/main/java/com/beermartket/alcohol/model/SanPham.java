package com.beermartket.alcohol.model;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Entity
@Table(name = "SanPham")
@NoArgsConstructor
@AllArgsConstructor
public class SanPham implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="MaSanPham")
	private int maSanPham;

	@Column(name="ChietKhauKH")
	private double chietKhauKH;

	@Column(name="DonViTinh")
	private String donViTinh;

	@Column(name="GiaGoc")
	private double giaGoc;

	@Column(name="MoTa")
	private String moTa;

	@Column(name="NgayTao")
	private Timestamp ngayTao;

	@Column(name="SoLuong")
	private int soLuong;

	@Column(name="TenSanPham")
	private String tenSanPham;

	@Column(name="ThongTin")
	private String thongTin;

	@Column(name="TrangThai")
	private boolean trangThai;

	//bi-directional many-to-one association to ChiTietGioHang
	@OneToMany(mappedBy="sanPham")
	private List<ChiTietGioHang> chiTietGioHangs;

	//bi-directional many-to-one association to ChiTietHoaDon
	@OneToMany(mappedBy="sanPham")
	private List<ChiTietHoaDon> chiTietHoaDons;

	//bi-directional many-to-one association to DanhGia
	@OneToMany(mappedBy="sanPham")
	private List<DanhGia> danhGias;

	//bi-directional many-to-one association to Hinh
	@OneToMany(mappedBy="sanPham")
	private List<Hinh> hinhs;

	//bi-directional many-to-one association to LoaiSanPham
	@ManyToOne
	@JoinColumn(name="MaLoaiSanPham")
	private LoaiSanPham loaiSanPham;
//
	//bi-directional many-to-one association to NhaCungCap
	@ManyToOne
	@JoinColumn(name="MaNhaCungCap")
	private NhaCungCap nhaCungCap;
//
	//bi-directional many-to-one association to TaiKhoanAdmin
	@ManyToOne
	@JoinColumn(name="MaTaiKhoanAdmin")
	private TaiKhoanAdmin taiKhoanAdmin;


}