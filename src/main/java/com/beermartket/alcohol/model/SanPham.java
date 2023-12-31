package com.beermartket.alcohol.model;

import java.io.Serializable;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
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
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="MaSanPham")
	private int maSanPham;

	@Column(name="ChietKhauKH")
	private Double chietKhauKH;

	@Column(name="GiaGoc")
	private Double giaGoc;
	
	@Column(name="GiaNhap")
	private Double giaNhap;

	@Column(name="MoTa")
	private String moTa;

	@Column(name="NgayTao")
	private Timestamp ngayTao;

	@Column(name="SoLuong")
	private Integer soLuong;

	@Column(name="TenSanPham")
	private String tenSanPham;

	@Column(name="ThongTin")
	private String thongTin;

	@Column(name="TrangThai")
	private Boolean  trangThai;

	@Column(name="HinhAnh")
	private String hinhAnh;
	
	@Column(name="NoiBat")
	private Boolean noiBat;
	

	@JsonIgnore
	@OneToMany(mappedBy="sanPham")
	private List<ChiTietHoaDon> chiTietHoaDons;
	
	@JsonIgnore
	@OneToMany(mappedBy="sanPham")
	private List<ChiTietPhieuNhapHang> chiTietPhieuNhapHangs;

	@JsonIgnore
	@OneToMany(mappedBy="sanPham")
	private List<DanhGia> danhGias;

	@JsonIgnore
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

	@ManyToOne
	@JoinColumn(name="MaTaiKhoanAdmin")
	private TaiKhoanAdmin taiKhoanAdmin;
	
	@ManyToOne
	@JoinColumn(name="MaDonViTinh")
	private DonViTinh donViTinh;

}