package com.beermartket.alcohol.model;

import java.io.Serializable;
import java.util.List;

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
	@Column(name="MaTaiKhoan")
	private int maTaiKhoan;

	@Column(name="DiaChi")
	private String diaChi;

	@Column(name="Email")
	private String email;

	@Column(name="HinhAnh")
	private String hinhAnh;

	@Column(name="HoTen")
	private String hoTen;

	@Column(name="MatKhau")
	private String matKhau;
	
	@Column(name="MaHoaMatKhau")
	private String maHoaMatKhau;
	
	@Column(name="SoDienThoai")
	private String soDienThoai;

	@Column(name="TenDangNhap")
	private String tenDangNhap;
	
	@Column(name="NgayTao")
	private String ngayTao;


	//bi-directional many-to-one association to DanhGia
	@OneToMany(mappedBy="taiKhoan")
	private List<DanhGia> danhGias;

	//bi-directional many-to-one association to GioHang
	@OneToMany(mappedBy="taiKhoan")
	private List<GioHang> gioHangs;

	//bi-directional many-to-one association to HoaDon
	@OneToMany(mappedBy="taiKhoan")
	private List<HoaDon> hoaDons;

	//bi-directional many-to-one association to ThanhVien
	@OneToMany(mappedBy="taiKhoan")
	private List<ThanhVien> thanhViens;

}