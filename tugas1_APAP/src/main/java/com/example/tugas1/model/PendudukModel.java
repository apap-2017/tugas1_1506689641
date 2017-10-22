package com.example.tugas1.model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PendudukModel {
	private int id;
	private String nik;
	private String nama;
	private String tanggal_lahir;
	private String tempat_lahir;
	private String alamat;
	private String RT;
	private String RW;
	private String kelurahan;
	private String kecamatan;
	private String jenis_kelamin;
	private String is_wni;
	private String id_keluarga;
	private String agama;
	private String pekerjaan;
	private String status_perkawinan;
	private String status_dalam_keluarga;
	private String golongan_darah;
	private String is_wafat;
	private String nik_lama;
	private String nik_mirip;
	private String status;
	
	public String generateWNI() {
		if(this.is_wni.equalsIgnoreCase("1")) {
			return this.is_wni = "WNI";
		}else {
			return this.is_wni= "WNA";
		}
	}
	
}
