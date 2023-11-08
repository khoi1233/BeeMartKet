package com.beermartket.alcohol.model;

import java.io.Serializable;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Entity
@Table(name = "HoaDon")
@NoArgsConstructor
@AllArgsConstructor
public class HoaDon implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="MaHoaDon")
	private int maHoaDon;

	@Column(name="GhiChu")
	private String ghiChu;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="NgayMua")
	private Date ngayMua;

	@Column(name="TrangThaiHoaDon")
	private String trangThaiHoaDon;

	@Column(name="TrangThaiThanhToan")
	private boolean trangThaiThanhToan;

	//bi-directional many-to-one association to ChiTietHoaDon
	@JsonIgnore
	@OneToMany(mappedBy="hoaDon")
	private List<ChiTietHoaDon> chiTietHoaDons;

	//bi-directional many-to-one association to TaiKhoan
	@ManyToOne
	@JoinColumn(name="MaTaiKhoan")
	private TaiKhoan taiKhoan;

	//bi-directional many-to-one association to LienKetKhuyenMai
	@JsonIgnore
	@OneToMany(mappedBy="hoaDon")
	private List<LienKetKhuyenMai> lienKetKhuyenMais;

}