package com.beermartket.alcohol.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

import org.hibernate.annotations.NamedQuery;

import com.fasterxml.jackson.annotation.JsonIgnore;

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
	
	@Column(name="ThongTin")
	private String ThongTin;

	@Column(name="SoLuong")
	private int soLuong;

	@Column(name="TenSanPham")
	private String tenSanPham;

	@Column(name="TrangThai")
	private boolean trangThai;
	
	@Column(name="NgayTao")
	private LocalDateTime ngayTao;

	@JsonIgnore
	@OneToMany(mappedBy="sanPham")
	private List<ChiTietGioHang> chiTietGioHangs;

	@JsonIgnore
	@OneToMany(mappedBy="sanPham")
	private List<ChiTietHoaDon> chiTietHoaDons;

	@JsonIgnore
	@OneToMany(mappedBy="sanPham")
	private List<DanhGia> danhGias;

	@JsonIgnore
	@OneToMany(mappedBy="sanPham")
	private List<Hinh> hinhs;


	@ManyToOne
	@JoinColumn(name="MaLoaiSanPham")
	private LoaiSanPham loaiSanPham;


	@ManyToOne
	@JoinColumn(name="MaNhaCungCap")
	private NhaCungCap nhaCungCap;


	@ManyToOne
	@JoinColumn(name="MaTaiKhoanAdmin")
	private TaiKhoanAdmin taiKhoanAdmin;

}