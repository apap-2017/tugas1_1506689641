package com.example.tugas1.model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LokasiModel {
	private String nama_kelurahan;
	private String nama_kecamatan;
	private String nama_kota;
}
