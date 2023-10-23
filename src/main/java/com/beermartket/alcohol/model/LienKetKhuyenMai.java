package com.beermartket.alcohol.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

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
@Table(name = "LienKetKhuyenMai")
@NoArgsConstructor
@AllArgsConstructor
public class LienKetKhuyenMai implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="MaLienKet")
	private int maLienKet;

	//bi-directional many-to-one association to HoaDon
	@ManyToOne
	@JoinColumn(name="MaHoaDon")
	private HoaDon hoaDon;

	//bi-directional many-to-one association to KhuyenMaiHoaDon
	@ManyToOne
	@JoinColumn(name="MaKhuyenMai")
	private KhuyenMaiHoaDon khuyenMaiHoaDon;
}