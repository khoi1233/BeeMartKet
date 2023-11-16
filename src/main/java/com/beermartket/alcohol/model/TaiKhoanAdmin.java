package com.beermartket.alcohol.model;

import java.io.Serializable;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

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
@Table(name = "TaiKhoanAdmin")
@NoArgsConstructor
@AllArgsConstructor
public class TaiKhoanAdmin implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "MaTaiKhoan")
	private int maTaiKhoan;

	@Column(name = "DiaChi")
	private String diaChi;

	@Column(name = "Email")
	private String email;

	@Column(name = "HinhAnh")
	private String hinhAnh;

	@Column(name = "HoTen")
	private String hoTen;

	@Column(name = "MatKhau")
	private String matKhau;

	@Column(name = "NgayTao")
	private LocalDateTime ngayTao;

	@Column(name = "MaHoaMatKhau")
	private String maHoaMatKhau;

	@Column(name = "SoDienThoai")
	private String soDienThoai;

	@Column(name = "TenDangNhap")
	private String tenDangNhap;
	
	@Column(name = "ChucVu")
	private String chucVu;

	@JsonIgnore
	@OneToMany(mappedBy = "taiKhoanAdmin")
	private List<PhieuNhapHang> phieuNhapHangs;

	@JsonIgnore
	@OneToMany(mappedBy = "taiKhoanAdmin")
	private List<SanPham> sanPham;

	@JsonIgnore
	@OneToMany(mappedBy = "taiKhoanAdmin")
	private List<TinTuc> tinTucs;

	@JsonIgnore
	@OneToMany(mappedBy = "taiKhoanAdmin")
	private List<Trang> trangs;

}