package com.beermartket.alcohol.model;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

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
@Table(name = "LoaiTrang")
@NoArgsConstructor
@AllArgsConstructor
public class LoaiTrang implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="MaLoaiTrang")
	private int maLoaiTrang;

	@Column(name="TenLoaiTrang")
	private String tenLoaiTrang;

	//bi-directional many-to-one association to Trang
	@ManyToOne
	@JoinColumn(name="MaTrang")
	private Trang trang;

}