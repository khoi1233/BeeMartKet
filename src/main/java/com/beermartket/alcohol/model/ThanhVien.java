package com.beermartket.alcohol.model;

import java.io.Serializable;
import java.util.List;

import org.hibernate.annotations.NamedQuery;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Entity
@Table(name = "ThanhVien")
@NoArgsConstructor
@AllArgsConstructor
public class ThanhVien implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="MaThanhVien")
	private int maThanhVien;

	@Column(name="SoDiemTich")
	private int soDiemTich;

	//bi-directional many-to-one association to TaiKhoan
	@ManyToOne
	@JoinColumn(name="MaTaiKhoan")
	private TaiKhoan taiKhoan;

}