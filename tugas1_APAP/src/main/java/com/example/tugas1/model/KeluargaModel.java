package com.example.tugas1.model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class KeluargaModel {
	private int id;
	private String nomor_kk;
	private String alamat;
	private String RT;
	private String RW;
	private String nama_kelurahan;
	private String id_kelurahan;
	private String is_tidak_berlaku;
	private String kecamatan;
	private String kota;
	private String lama;
	private String nkk_same;
	private List<PendudukModel> penduduk;
}
