package com.beermartket.alcohol.model;

import java.io.Serializable;

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
@Table(name = "HinhAnh")
@NoArgsConstructor
@AllArgsConstructor
public class HinhAnh implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="MaAnh")
	private int maAnh;

	@Column(name="TenAnh")
	private String tenAnh;

	//bi-directional many-to-one association to Trang
	@ManyToOne
	@JoinColumn(name="MaTrang")
	private Trang trang;

}