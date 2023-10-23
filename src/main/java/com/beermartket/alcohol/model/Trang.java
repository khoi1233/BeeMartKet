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
@Table(name = "Trang")
@NoArgsConstructor
@AllArgsConstructor
public class Trang implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="MaTrang")
	private int maTrang;

	@Column(name="MaLoaiTrang")
	private int maLoaiTrang;

	@Column(name="NgayTao")
	private LocalDateTime ngayTao;

	@Column(name="NoiDung")
	private String noiDung;

	@Column(name="TenTrang")
	private String tenTrang;

	@Column(name="TieuDe")
	private String tieuDe;

	//bi-directional many-to-one association to HinhAnh
	@OneToMany(mappedBy="trang")
	private List<HinhAnh> hinhAnhs;

	//bi-directional many-to-one association to LoaiTrang
	@OneToMany(mappedBy="trang")
	private List<LoaiTrang> loaiTrangs;

	//bi-directional many-to-one association to TaiKhoanAdmin
	@ManyToOne
	@JoinColumn(name="MaTaiKhoan")
	private TaiKhoanAdmin taiKhoanAdmin;

}