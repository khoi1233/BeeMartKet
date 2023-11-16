package com.beermartket.alcohol.model;

import java.io.Serializable;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Entity
@Table(name = "ViKhuyenMai")
@NoArgsConstructor
@AllArgsConstructor
public class ViKhuyenMai implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="MaViKhuyenMai")
	private int maViKhuyenMai;

	//bi-directional many-to-one association to TaiKhoan
		@ManyToOne
		@JoinColumn(name="MaTaiKhoan")
		private TaiKhoan taiKhoan;
		
		//bi-directional many-to-one association to ChiTietViKhuyenMai
		@JsonIgnore
		@OneToMany(mappedBy="viKhuyenMai")
		private List<ChiTietViKhuyenMai> chiTietViKhuyenMais;
}