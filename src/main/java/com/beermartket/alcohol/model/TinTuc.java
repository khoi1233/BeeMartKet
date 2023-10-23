package com.beermartket.alcohol.model;

import java.io.Serializable;
import java.sql.Timestamp;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Entity
@Table(name = "TinTuc")
@NoArgsConstructor
@AllArgsConstructor
public class TinTuc implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="MaTinTuc")
	private int maTinTuc;

	@Column(name="HinhAnh")
	private String hinhAnh;

	@Column(name="NgayDang")
	private LocalDateTime ngayDang;

	@Column(name="NoiDung")
	private String noiDung;

	@Column(name="TieuDe")
	private String tieuDe;

	//bi-directional many-to-one association to TaiKhoanAdmin
	@ManyToOne
	@JoinColumn(name="MaTaiKhoan")
	private TaiKhoanAdmin taiKhoanAdmin;

}