package com.beermartket.alcohol.model;

import java.io.Serializable;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
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
	@Column(name="MaKhuyenMai")
	private int maKhuyenMai;

	@Column(name="GiaTriKhuyenMai")
	private double giaTriKhuyenMai;

	@Column(name="GiaTriToiThieu")
	private double giaTriToiThieu;

	@Column(name="NgayTao")
	private LocalDateTime ngayTao;

	@Column(name="TenKhuyenMai")
	private String tenKhuyenMai;

	@Column(name="TrangThai")
	private boolean trangThai;

	//bi-directional many-to-one association to LienKetKhuyenMai
	@OneToMany(mappedBy="khuyenMaiHoaDon")
	private List<LienKetKhuyenMai> lienKetKhuyenMais;

}