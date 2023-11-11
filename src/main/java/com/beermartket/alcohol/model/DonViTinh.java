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
@Table(name = "DonViTinh")
@NoArgsConstructor
@AllArgsConstructor
public class DonViTinh implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name="MaDonViTinh")
	private int maDonViTinh;

	@Column(name="TenDonViTinh")
	private String tenDonViTinh;
	
	@Column(name="GhiChu")
	private String ghiChu;

	@JsonIgnore
	@OneToMany(mappedBy="donViTinh")
	private List<SanPham> sanPhams;
}
