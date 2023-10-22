package com.beermartket.alcohol.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

import org.hibernate.annotations.NamedQuery;

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

	@Column(name="SoDienThoai")
	private String soDienThoai;

	@Column(name="TenDangNhap")
	private String tenDangNhap;


	@OneToMany(mappedBy="taiKhoanAdmin")
	private List<PhieuNhapHang> phieuNhapHangs;


	@OneToMany(mappedBy="taiKhoanAdmin")
	private List<SanPham> sanPhams;


	@OneToMany(mappedBy="taiKhoanAdmin")
	private List<TinTuc> tinTucs;


	@OneToMany(mappedBy="taiKhoanAdmin")
	private List<Trang> trangs;

}