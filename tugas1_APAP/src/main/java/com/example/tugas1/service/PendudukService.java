package com.example.tugas1.service;

import java.util.List;

import com.example.tugas1.model.KecamatanModel;
import com.example.tugas1.model.KeluargaModel;
import com.example.tugas1.model.KelurahanModel;
import com.example.tugas1.model.KotaModel;
import com.example.tugas1.model.LokasiModel;
import com.example.tugas1.model.PendudukModel;

public interface PendudukService {
	PendudukModel selectPenduduk(String nik);
	KeluargaModel selectKeluarga(String nkk);
	void addPenduduk(PendudukModel penduduk);
	void addKeluarga(KeluargaModel keluarga);
	String getKodeKecamatan(String id_keluarga);
	String getJumlahNik(String nik);
	int countAllPenduduk();
	String getNomorKK(String nama_kecamatan);
	int countAllKeluarga();
	String getIdKelurahan(String nama_kelurahan);
	//String statusKematian(String nik);
	void updatePenduduk(PendudukModel penduduk);
	void updateKeluarga(KeluargaModel keluarga);
	String viewKelurahanKeluarga(String nomor_kk);
	String viewKecamatanKeluarga(String nomor_kk);
	String viewKotaKeluarga(String nomor_kk);
	String countNkkSama(String nkk_same);
	List<String> selectNikMirip(String nik_lama);
	List<String> selectNkkMirip(String nkk_same);
	List<KotaModel> selectAllKota();
	KotaModel selectKota(String id);
	List<KecamatanModel> selectAllKecamatan(String kota);
	KecamatanModel selectKecamatan(String id);
	List<KelurahanModel> selectAllKelurahan(String kecamatan);
	KelurahanModel selectKelurahan(String id);
	List<PendudukModel> selectPendudukByKelurahan(String id_kelurahan);
	LokasiModel selectLokasi(String id_kelurahan);
	String setStatus(PendudukModel penduduk);
	int nikToInteger(PendudukModel penduduk);
	List<String> selectWafatKeluarga(String id_keluarga);
	KeluargaModel selectKeluargaById(String id_keluarga);

}

