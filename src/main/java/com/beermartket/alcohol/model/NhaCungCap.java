package com.beermartket.alcohol.model;

import java.io.Serializable;
import java.util.List;

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
@Table(name = "NhaCungCap")
@NoArgsConstructor
@AllArgsConstructor
public class NhaCungCap implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="MaNhaCungCap")
	private int maNhaCungCap;

	@Column(name="DiaChi")
	private String diaChi;

	@Column(name="GhiChu")
	private String ghiChu;

	@Column(name="SoDienThoai")
	private String soDienThoai;

	@Column(name="TenNhaCungCap")
	private String tenNhaCungCap;

	@Column(name="Website")
	private String website;

	//bi-directional many-to-one association to PhieuNhapHang
	@OneToMany(mappedBy="nhaCungCap")
	private List<PhieuNhapHang> phieuNhapHangs;

	//bi-directional many-to-one association to SanPham
	@OneToMany(mappedBy="nhaCungCap")
	private List<SanPham> sanPhams;

}