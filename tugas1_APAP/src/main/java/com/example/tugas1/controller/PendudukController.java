package com.example.tugas1.controller;


import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.tugas1.model.KeluargaModel;
import com.example.tugas1.model.LokasiModel;
import com.example.tugas1.model.PendudukModel;
import com.example.tugas1.service.PendudukService;


@Controller
public class PendudukController {
	@Autowired
	PendudukService pendudukDAO;
	
	@RequestMapping("/")
    public String index ()
    {
        return "view-penduduk";
    }
	
	@RequestMapping("/penduduk")
	public String viewPenduduk(Model model, 
			@RequestParam(value = "nik", required = false) String nik)
	{
		PendudukModel penduduk = pendudukDAO.selectPenduduk(nik);
		
		if (penduduk != null) {			
			penduduk.setIs_wni(penduduk.generateWNI());
			penduduk.setStatus(pendudukDAO.setStatus(penduduk));
//			System.out.println(penduduk.getIs_wafat());
//			System.out.println(penduduk.getStatus());
			model.addAttribute("penduduk", penduduk);
			return "viewPenduduk";
		} else {
			model.addAttribute("nik", nik);
			return "not-found";
		}
	}
	
	@RequestMapping(value = "/penduduk/mati/", method = RequestMethod.POST)
	public String setStatusKematian(@Valid @ModelAttribute("penduduk") PendudukModel penduduk) {
		penduduk.setIs_wafat("1");
		penduduk.setStatus(pendudukDAO.setStatus(penduduk));
		int wni = pendudukDAO.nikToInteger(penduduk);
		penduduk.setIs_wni(""+wni);
		
		
		KeluargaModel keluarga = pendudukDAO.selectKeluargaById(penduduk.getId_keluarga());
		List<String> cekWafat = pendudukDAO.selectWafatKeluarga(penduduk.getId_keluarga());
		boolean wafat = false;
		
		for (int i = 0; i < cekWafat.size(); i++) {
			if (cekWafat.get(i).equalsIgnoreCase("1")) {
				wafat = true;
			}
		}
		
		if (wafat) {
			keluarga.setIs_tidak_berlaku("1");
			pendudukDAO.updateKeluarga(keluarga);
		}
		pendudukDAO.updatePenduduk(penduduk);
		return "penduduk-mati";
		
	}
	
	@RequestMapping("/keluarga")
	public String viewKeluarga(Model model,
			@RequestParam(value = "nkk", required = false) String nkk)
	{
		KeluargaModel keluarga = pendudukDAO.selectKeluarga(nkk);
		LokasiModel lokasi = pendudukDAO.selectLokasi(keluarga.getId_kelurahan());
		
		if (keluarga != null) {
			model.addAttribute("keluarga", keluarga);
			model.addAttribute("lokasi", lokasi);
			return "viewKeluarga";
		} else {
			model.addAttribute("nkk", nkk);
			return "keluarga-not-found";
		}
	}
	
	@RequestMapping(value = "/penduduk/tambah")
	public String addPenduduk () {
		return "form-tambah-penduduk";
	}
	
	@RequestMapping(value = "/penduduk/tambah", method = RequestMethod.POST)
    public String addPendudukSubmit(@Valid @ModelAttribute("penduduk") PendudukModel penduduk) {
		String jenisKelamin = penduduk.getJenis_kelamin();
		String kodeKecamatan = pendudukDAO.getKodeKecamatan(penduduk.getId_keluarga()).substring(0,6);
		String [] tanggalLahir = penduduk.getTanggal_lahir().split("-");
		
		
		
		int tanggal = Integer.parseInt(tanggalLahir[2]);
		String tanggalLahir2 = "";
		if (jenisKelamin.equalsIgnoreCase("1")) {
			tanggal = Integer.parseInt(tanggalLahir[2]) + 40;
			tanggalLahir2= tanggal + tanggalLahir[1]+ tanggalLahir[0].substring(2);
		} else {
			if (tanggal > 9) {
				tanggalLahir2 = tanggal + tanggalLahir[1] + tanggalLahir[0].substring(2);
			} else {
				tanggalLahir2 = "0" + tanggal + tanggalLahir[1] + tanggalLahir[0].substring(2);
			}
		}
		
		penduduk.setNik_mirip(kodeKecamatan + tanggalLahir2);
		int urutan = pendudukDAO.selectNikMirip(penduduk.getNik_mirip()).size() + 1;

		NumberFormat nf = new DecimalFormat("0000");
		String nik = kodeKecamatan + tanggalLahir2 + nf.format(urutan);
		penduduk.setNik(nik);
		
		if (penduduk.getNik().equalsIgnoreCase(pendudukDAO.getJumlahNik(penduduk.getNik()))) {
			Long nikLong = Long.parseLong(nik)+1;
			String nikFix = ""+nikLong;
			penduduk.setNik(nikFix);
		}
		int id = pendudukDAO.countAllPenduduk() + 1;
		penduduk.setId(id);
		penduduk.setIs_wafat("0");
		
    	pendudukDAO.addPenduduk(penduduk);
		return "tambah-sukses";
    }
	
	@RequestMapping(value = "/keluarga/tambah")
	public String addKeluarga() {
		return "form-tambah-keluarga";
	}
	
	@RequestMapping(value = "/keluarga/tambah", method = RequestMethod.POST)
	public String addKeluargaSubmit(@Valid @ModelAttribute("keluarga") KeluargaModel keluarga) {
		//set nomor kartu keluarga
		String kec = keluarga.getKecamatan();
		String kec2 = pendudukDAO.getNomorKK(kec).substring(0,6);
		DateFormat df = new SimpleDateFormat("dd/MM/yy");
		Date dateobj = new Date();
		String [] currentDate = df.format(dateobj).split("/");
		String date = currentDate[0] + currentDate[1] + currentDate[2];
		String nkk_same = kec2+date;
		keluarga.setNkk_same(nkk_same);

	//	int urutan = Integer.parseInt(pendudukDAO.countNkkSama(nkk_same)) + 1;
		int urutan = pendudukDAO.selectNkkMirip(keluarga.getNkk_same()).size() + 1; 
		System.out.println(pendudukDAO.selectNkkMirip(keluarga.getNkk_same()).size());
		NumberFormat nf = new DecimalFormat("0000");
	
		keluarga.setNomor_kk(kec2+date+nf.format(urutan));
		
		//set id
		int id = pendudukDAO.countAllKeluarga() + 1;
		keluarga.setId(id);
		
		//set id kelurahan
		String kel = keluarga.getNama_kelurahan();
		String kel2 = pendudukDAO.getIdKelurahan(kel);
		keluarga.setId_kelurahan(kel2);
		keluarga.setIs_tidak_berlaku("0");
		
		pendudukDAO.addKeluarga(keluarga);
		return "sukses-tambah-keluarga";
	}
	
	@RequestMapping(value = "/penduduk/ubah/{nik}")
	public String updatePenduduk(Model model, @PathVariable(value = "nik") String nik) {
		PendudukModel penduduk = pendudukDAO.selectPenduduk(nik);
		
		if(penduduk != null) {
			model.addAttribute("penduduk", penduduk);
			return "update-penduduk";
		} else {
			model.addAttribute("nik", nik);
			return "not-found";
		}
	}
	
	@RequestMapping(value = "/penduduk/ubah/{nik}", method = RequestMethod.POST)
	public String updatePendudukSubmit(@Valid @ModelAttribute("penduduk") PendudukModel penduduk)
	{
		String oldNIK = penduduk.getNik();
		penduduk.setNik_lama(oldNIK);
		String jenisKelamin = penduduk.getJenis_kelamin();
		String kodeKecamatan = pendudukDAO.getKodeKecamatan(penduduk.getId_keluarga()).substring(0,6);
		String [] tanggalLahir = penduduk.getTanggal_lahir().split("-");
		
		int tanggal = Integer.parseInt(tanggalLahir[2]);
		String tanggalLahir2 = "";
		if (jenisKelamin.equalsIgnoreCase("1")) {
			tanggal = Integer.parseInt(tanggalLahir[2]) + 40;
			tanggalLahir2= tanggal + tanggalLahir[1]+ tanggalLahir[0].substring(2);
		} else {
			if (tanggal > 9) {
				tanggalLahir2 = tanggal + tanggalLahir[1] + tanggalLahir[0].substring(2);
			} else {
				tanggalLahir2 = "0" + tanggal + tanggalLahir[1] + tanggalLahir[0].substring(2);
			}
		}
		
		
		String nik = kodeKecamatan + tanggalLahir2 + penduduk.getNik().substring(12);
		penduduk.setNik(nik);

		System.out.println(penduduk.getId());
		System.out.println(penduduk.getNik().toString());
		pendudukDAO.updatePenduduk(penduduk);	
		return "sukses-update-penduduk";
	}
	
	@RequestMapping(value = "keluarga/ubah/{nkk}")
	public String updateKeluarga(Model model, @PathVariable(value = "nkk") String nkk)
	{
		KeluargaModel keluarga = pendudukDAO.selectKeluarga(nkk);
		
		if (keluarga != null) {
			String namaKelurahan = pendudukDAO.viewKelurahanKeluarga(nkk);
			String namaKecamatan = pendudukDAO.viewKecamatanKeluarga(nkk);
			String kota = pendudukDAO.viewKotaKeluarga(nkk);
			keluarga.setNama_kelurahan(namaKelurahan);
			keluarga.setKecamatan(namaKecamatan);
			keluarga.setKota(kota);
			model.addAttribute("keluarga", keluarga);
			return "update-keluarga";
		} else {
			model.addAttribute("nkk", nkk);
			return "keluarga-not-found";
		}
	}
	
	@RequestMapping(value = "keluarga/ubah/{nkk}", method = RequestMethod.POST)
	public String updateKeluargaSubmit(@Valid @ModelAttribute("keluarga") KeluargaModel keluarga)
	{
		String old = keluarga.getNomor_kk();
		System.out.println(old);
		keluarga.setLama(old);
		
		String kec = keluarga.getKecamatan();
		System.out.println(keluarga.getKecamatan());
		String kec2 = pendudukDAO.getNomorKK(kec).substring(0,6);
		DateFormat df = new SimpleDateFormat("dd/MM/yy");
		Date dateobj = new Date();
		String [] currentDate = df.format(dateobj).split("/");
		String date = currentDate[0] + currentDate[1] + currentDate[2];
		String nkk_same = kec2+date;
		keluarga.setNkk_same(nkk_same);

		int urutan = pendudukDAO.selectNkkMirip(keluarga.getNkk_same()).size() + 1;
		
		NumberFormat nf = new DecimalFormat("0000");
	
		keluarga.setNomor_kk(kec2+date+nf.format(urutan));
		
		//set id kelurahan
		String kel = keluarga.getNama_kelurahan();
		String kel2 = pendudukDAO.getIdKelurahan(kel);
		keluarga.setId_kelurahan(kel2);
		keluarga.setIs_tidak_berlaku("0");

				
		pendudukDAO.updateKeluarga(keluarga);
		return "sukses-update-keluarga";
	}
	
	@RequestMapping("/penduduk/cari")
	public String cariPenduduk(@RequestParam(value="kota", required = false) String kota,
			@RequestParam(value="kecamatan", required = false) String kecamatan,
			@RequestParam(value="kelurahan", required = false) String kelurahan,
			Model model) {
		
		if (kota == null) {
			model.addAttribute("kota", pendudukDAO.selectAllKota());
			return "penduduk-cari-kota";
		} else {
			if (kecamatan == null) {
				model.addAttribute("kota", pendudukDAO.selectKota(kota));
				model.addAttribute("kecamatan", pendudukDAO.selectAllKecamatan(kota));
				return "penduduk-cari-kecamatan";
			} else {
				if(kelurahan == null) {
				model.addAttribute("kota", pendudukDAO.selectKota(kota));
				model.addAttribute("kecamatan", pendudukDAO.selectKecamatan(kecamatan));
				model.addAttribute("kelurahan", pendudukDAO.selectAllKelurahan(kecamatan));
				return "penduduk-cari-kelurahan";
				}
			}
		}
		
		List<PendudukModel> hasil = pendudukDAO.selectPendudukByKelurahan(kelurahan);
		model.addAttribute("penduduk", hasil);
		model.addAttribute("kota", pendudukDAO.selectKota(kota));
		model.addAttribute("kecamatan", pendudukDAO.selectKecamatan(kecamatan));
		model.addAttribute("kelurahan", pendudukDAO.selectKelurahan(kelurahan));
		return "hasil-pencarian";
		
		
	}
	
}


	
	