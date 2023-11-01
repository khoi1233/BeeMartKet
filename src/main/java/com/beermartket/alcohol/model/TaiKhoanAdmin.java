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
	@Column(name="MaTaiKhoan")
	private int maTaiKhoan;

	@Column(name="ChucVu")
	private String chucVu;

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


	@Column(name="NgayTao")
<<<<<<< HEAD
	private LocalDateTime ngayTao;
	
	@Column(name="MaHoaMatKhau")
	private String maHoaMatKhau;
	
=======
	private LocalDateTime ngayTao;	

	private String maHoaMatKhau;

>>>>>>> a52fef4fd03b58834c1361801eadc0144fe5f842
	@Column(name="SoDienThoai")
	private String soDienThoai;

	@Column(name="TenDangNhap")
	private String tenDangNhap;

	@JsonIgnore
	@OneToMany(mappedBy="taiKhoanAdmin")
	private List<PhieuNhapHang> phieuNhapHangs;

	@JsonIgnore
	@OneToMany(mappedBy="taiKhoanAdmin")
	private List<SanPham> sanPhams;

	@JsonIgnore
	@OneToMany(mappedBy="taiKhoanAdmin")
	private List<TinTuc> tinTucs;

	@JsonIgnore
	@OneToMany(mappedBy="taiKhoanAdmin")
	private List<Trang> trangs;

}