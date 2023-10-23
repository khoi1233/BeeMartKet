package com.beermartket.alcohol.model;

import java.io.Serializable;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Entity
@Table(name = "PhieuNhapHang")
@NoArgsConstructor
@AllArgsConstructor
public class PhieuNhapHang implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="MaPhieuNhap")
	private int maPhieuNhap;

	@Column(name="GhiChu")
	private String ghiChu;

	@Column(name="NgayNhap")
	private LocalDateTime ngayNhap;

	@Column(name="TongTien")
	private double tongTien;

	@Column(name="TrangThai")
	private String trangThai;

	//bi-directional many-to-one association to ChiTietPhieuNhapHang
	@OneToMany(mappedBy="phieuNhapHang")
	private List<ChiTietPhieuNhapHang> chiTietPhieuNhapHangs;

	//bi-directional many-to-one association to NhaCungCap
	@ManyToOne
	@JoinColumn(name="MaNhaCungCap")
	private NhaCungCap nhaCungCap;

	//bi-directional many-to-one association to TaiKhoanAdmin
	@ManyToOne
	@JoinColumn(name="MaTaiKhoan")
	private TaiKhoanAdmin taiKhoanAdmin;

}