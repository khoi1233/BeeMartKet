package com.beermartket.alcohol.model;

import java.io.Serializable;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
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
@Table(name = "KhuyenMaiHoaDon")
@NoArgsConstructor
@AllArgsConstructor
public class KhuyenMaiHoaDon implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "MaKhuyenMai")
	private String maKhuyenMai;

	@Column(name = "GiaTriKhuyenMai")
	private int giaTriKhuyenMai;

	@Column(name = "GiaTriToiThieu")
	private int giaTriToiThieu;

	@Column(name = "NgayTao")
	private Timestamp ngayTao;

	@Column(name = "TenKhuyenMai")
	private String tenKhuyenMai;

	@Column(name = "Loai")
	private Boolean loai;

	@Column(name = "TrangThai")
	private Boolean trangThai;

	@Column(name = "SoLuong")
	private int soLuong;

	@Column(name = "ChietKhau")
	private Integer chiecKhau;
	
	@Column(name = "NgayKetThuc")
	private LocalDateTime ngayKetThuc;
	
	@Column(name = "Hinh")
	private String hinh;

	// bi-directional many-to-one association to HoaDon
	@JsonIgnore
	@OneToMany(mappedBy = "khuyenMaiHoaDon")
	private List<HoaDon> hoaDons;

	// bi-directional many-to-one association to ChiTietKhuyenMai
	@JsonIgnore
	@OneToMany(mappedBy = "khuyenMaiHoaDon")
	private List<ChiTietViKhuyenMai> chiTietViKhuyenMais;

}