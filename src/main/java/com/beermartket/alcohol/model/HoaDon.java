package com.beermartket.alcohol.model;

import java.io.Serializable;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

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
@Table(name = "HoaDon")
@NoArgsConstructor
@AllArgsConstructor
public class HoaDon implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="MaHoaDon")
	private int maHoaDon;

	@Column(name="DiaChi")
	private String diaChi;

	@Column(name="GhiChu")
	private String ghiChu;

	@Column(name="NgayMua")
	private LocalDateTime ngayMua;

	@Column(name="TongTien")
	private double tongTien;

	@Column(name="TrangThaiHoaDon")
	private String trangThaiHoaDon;

	@Column(name="TrangThaiThanhToan")
	private boolean trangThaiThanhToan;

	//bi-directional many-to-one association to ChiTietHoaDon
	@OneToMany(mappedBy="hoaDon")
	private List<ChiTietHoaDon> chiTietHoaDons;

	//bi-directional many-to-one association to TaiKhoan
	@ManyToOne
	@JoinColumn(name="MaTaiKhoan")
	private TaiKhoan taiKhoan;

	//bi-directional many-to-one association to LienKetKhuyenMai
	@OneToMany(mappedBy="hoaDon")
	private List<LienKetKhuyenMai> lienKetKhuyenMais;

}