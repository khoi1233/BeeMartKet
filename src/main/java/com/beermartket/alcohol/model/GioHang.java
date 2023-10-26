package com.beermartket.alcohol.model;

import java.io.Serializable;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

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
@Table(name = "GioHang")
@NoArgsConstructor
@AllArgsConstructor
public class GioHang implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="MaGioHang")
	private int maGioHang;

	@Column(name="NgayTao")
	private LocalDateTime ngayTao;
    
	@JsonIgnore
	@OneToMany(mappedBy="gioHang")
	private List<ChiTietGioHang> chiTietGioHangs;

	
	@ManyToOne
	@JoinColumn(name="MaTaiKhoan")
	private TaiKhoan taiKhoan;

}