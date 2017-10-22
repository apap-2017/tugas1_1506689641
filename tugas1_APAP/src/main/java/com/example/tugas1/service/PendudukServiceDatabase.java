package com.example.tugas1.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.tugas1.dao.PendudukMapper;
import com.example.tugas1.model.KecamatanModel;
import com.example.tugas1.model.KeluargaModel;
import com.example.tugas1.model.KelurahanModel;
import com.example.tugas1.model.KotaModel;
import com.example.tugas1.model.LokasiModel;
import com.example.tugas1.model.PendudukModel;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class PendudukServiceDatabase implements PendudukService{
	
	@Autowired
	private PendudukMapper pendudukMapper;
	
	@Override
	public PendudukModel selectPenduduk(String nik) {
		log.info("select penduduk with nik {}", nik);
		return pendudukMapper.selectPenduduk(nik);
	}

	@Override
	public KeluargaModel selectKeluarga(String nkk) {
		log.info("select keluarga with nkk {}", nkk);
		return pendudukMapper.selectKeluarga(nkk);
	}

	@Override
	public void addPenduduk(PendudukModel penduduk) {
		log.info("add penduduk with nik{}", penduduk.getNik());
		pendudukMapper.addPenduduk(penduduk);
		
	}

	@Override
	public void addKeluarga(KeluargaModel keluarga) {
		pendudukMapper.addKeluarga(keluarga);
	}

	@Override
	public String getKodeKecamatan(String id_keluarga) {
		log.info("get kode kecamatan with id keluarga {}", id_keluarga);
		return pendudukMapper.getKodeKecamatan(id_keluarga);
	}

	@Override
	public String getJumlahNik(String nik) {
		return pendudukMapper.getJumlahNik(nik);
	}

	@Override
	public int countAllPenduduk() {
		return pendudukMapper.countAllPenduduk();
	}

	@Override
	public String getNomorKK(String nama_kecamatan) {
		return pendudukMapper.getNomorKK(nama_kecamatan);
	}

	@Override
	public int countAllKeluarga() {
		return pendudukMapper.countAllKeluarga();
	}

	@Override
	public String getIdKelurahan(String nama_kelurahan) {
		return pendudukMapper.getIdKelurahan(nama_kelurahan);
	}

	@Override
	public void updatePenduduk(PendudukModel penduduk) {
		log.info("update penduduk with nik{} ", penduduk.getNik());
		pendudukMapper.updatePenduduk(penduduk);
	}

	@Override
	public void updateKeluarga(KeluargaModel keluarga) {
		pendudukMapper.updateKeluarga(keluarga);
	}

	@Override
	public String viewKelurahanKeluarga(String nomor_kk) {
		//System.out.println(pendudukMapper.viewKelurahanKeluarga(nomor_kk));
		return pendudukMapper.viewKelurahanKeluarga(nomor_kk);
		
	}

	@Override
	public String viewKecamatanKeluarga(String nomor_kk) {
		return pendudukMapper.viewKecamatanKeluarga(nomor_kk);
	}

	@Override
	public String viewKotaKeluarga(String nomor_kk) {
		return pendudukMapper.viewKotaKeluarga(nomor_kk);
	}

	@Override
	public String countNkkSama(String nkk_same) {
		return pendudukMapper.countNkkSama(nkk_same);
	}

	@Override
	public List<String> selectNikMirip(String nik_lama) {
		return pendudukMapper.selectNikMirip(nik_lama);
	}

	@Override
	public List<String> selectNkkMirip(String nkk_same) {
		return pendudukMapper.selectNkkMirip(nkk_same);
	}

	@Override
	public List<KotaModel> selectAllKota() {
		return pendudukMapper.selectAllKota();
	}

	@Override
	public List<KecamatanModel> selectAllKecamatan(String kota) {
		return pendudukMapper.selectAllKecamatan(kota);
	}

	@Override
	public KotaModel selectKota(String id) {
		return pendudukMapper.selectKota(id);
	}

	@Override
	public KecamatanModel selectKecamatan(String id) {
		return pendudukMapper.selectKecamatan(id);
	}

	@Override
	public List<KelurahanModel> selectAllKelurahan(String kecamatan) {
		return pendudukMapper.selectAllKelurahan(kecamatan);
	}

	@Override
	public List<PendudukModel> selectPendudukByKelurahan(String id_kelurahan) {
		return pendudukMapper.selectPendudukByKelurahan(id_kelurahan);
	}

	@Override
	public KelurahanModel selectKelurahan(String id) {
		return pendudukMapper.selectKelurahan(id);
	}

	@Override
	public LokasiModel selectLokasi(String id_kelurahan) {
		return pendudukMapper.selectLokasi(id_kelurahan);
	}
	
	public String setStatus(PendudukModel penduduk) {
		if (penduduk.getIs_wafat().equalsIgnoreCase("1")) {
			return "Mati";
		} else {
			return "Hidup";
		}
	}
	
	public int nikToInteger(PendudukModel penduduk) {
		if (penduduk.getIs_wni() == "WNI") {
			return 1;
		} else {
			return 0;
		}
	}

	@Override
	public List<String> selectWafatKeluarga(String id_keluarga) {
		return pendudukMapper.selectWafatKeluarga(id_keluarga);
	}

	@Override
	public KeluargaModel selectKeluargaById(String id_keluarga) {
		return pendudukMapper.selectKeluargaById(id_keluarga);
	}
	
}
