package com.example.tugas1.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Many;

import com.example.tugas1.model.KecamatanModel;
import com.example.tugas1.model.KeluargaModel;
import com.example.tugas1.model.KelurahanModel;
import com.example.tugas1.model.KotaModel;
import com.example.tugas1.model.LokasiModel;
import com.example.tugas1.model.PendudukModel;

@Mapper
public interface PendudukMapper {
	
	@Select("select p.* , k.alamat, k.RT, k.RW, kl.nama_kelurahan as kelurahan, kc.nama_kecamatan as kecamatan from penduduk p, keluarga k, kelurahan kl, kecamatan kc where "
			+ "p.id_keluarga = k.id and k.id_kelurahan = kl.id and kl.id_kecamatan = kc.id and nik = #{nik}")
	PendudukModel selectPenduduk(@Param("nik") String nik);
	
	@Select("select * from keluarga where nomor_kk = #{nkk}")
	@Results(value = {
	@Result(property="id", column = "id"),
	@Result(property="nomor_kk", column = "nomor_kk"),
	@Result(property="alamat", column = "alamat"),
	@Result(property="RT", column = "RT"),
	@Result(property="RW", column = "RW"),
	@Result(property="id_kelurahan", column = "id_kelurahan"),
	@Result(property="is_tidak_berlaku", column = "is_tidak_berlaku"),
	@Result(property="penduduk", column="id",
	javaType = List.class,
	many=@Many(select="selectAnggota"))
	
	})
	KeluargaModel selectKeluarga(@Param("nkk") String nkk);
	
	@Select("select DISTINCT kl.nama_kelurahan, kc.nama_kecamatan,"
			+ "kt.nama_kota from keluarga kg, kelurahan kl, kecamatan kc, kota kt where "
			+ "kt.id = kc.id_kota and kc.id = kl.id_kecamatan and kl.id = kg.id_kelurahan and kg.id_kelurahan = #{id_kelurahan}")
	LokasiModel selectLokasi(@Param("id_kelurahan") String id_kelurahan);
	
	@Select("select * from penduduk where id_keluarga = #{id}")
	List<PendudukModel> selectAnggota (@Param("id") String id);
	
	@Insert("insert into penduduk (id, nik, nama, tempat_lahir, "
			+ "tanggal_lahir, jenis_kelamin, is_wni, id_keluarga, agama, "
			+ "pekerjaan, status_perkawinan, status_dalam_keluarga, golongan_darah, "
			+ "is_wafat)"
			+ " VALUES (#{id}, #{nik}, #{nama}, #{tempat_lahir}, #{tanggal_lahir}, #{jenis_kelamin}, "
			+ "#{is_wni}, #{id_keluarga}, #{agama}, #{pekerjaan}, #{status_perkawinan}, "
			+ "#{status_dalam_keluarga}, #{golongan_darah}, #{is_wafat})")
	void addPenduduk (PendudukModel penduduk);	
	
	@Select("select nik from penduduk where nik like '%" + "${nik_lama}" + "%'")
	List<String> selectNikMirip(@Param("nik_lama") String nik_lama);
	
	@Select("select DISTINCT kc.kode_kecamatan from kecamatan kc, "
			+ "keluarga kg, kelurahan kl, penduduk p "
			+ "WHERE kc.id =  kl.id_kecamatan and kl.id = kg.id_kelurahan and "
			+ "p.id_keluarga=kg.id and p.id_keluarga = #{id_keluarga}")
	String getKodeKecamatan(@Param("id_keluarga") String id_keluarga);
	
	
	@Select("select distinct nik from penduduk where nik = #{nik} order by nik desc" )
	String getJumlahNik(@Param("nik") String nik);
	
	@Select("SELECT count(*) FROM penduduk")
	int countAllPenduduk();
	
	@Insert("insert into keluarga (id, nomor_kk, alamat, RT, RW, id_kelurahan, is_tidak_berlaku) VALUES (#{id}, #{nomor_kk},"
			+ "#{alamat}, #{RT}, #{RW}, #{id_kelurahan}, #{is_tidak_berlaku})")
	void addKeluarga (KeluargaModel keluarga);
	
	@Select("select nomor_kk from keluarga where nomor_kk like '%" + "${nkk_same}" + "%'")
	List<String> selectNkkMirip(@Param("nkk_same") String nkk_same);
	
	@Select("SELECT COUNT(nomor_kk) from keluarga WHERE nomor_kk LIKE #{nkk_same}")
	String countNkkSama(@Param("nkk_same") String nkk_same);
	
	@Select("select kode_kecamatan from kecamatan where nama_kecamatan = #{nama_kecamatan}")
	String getNomorKK(@Param("nama_kecamatan") String nama_kecamatan);
	
	@Select("Select count(*) from keluarga")
	int countAllKeluarga();
	
	@Select("select id from kelurahan where nama_kelurahan = #{nama_kelurahan}")
	String getIdKelurahan(@Param("nama_kelurahan") String nama_kelurahan);
	
	@Update("update penduduk set nik = #{nik}, nama = #{nama}, tempat_lahir = #{tempat_lahir},"
			+ "tanggal_lahir = #{tanggal_lahir}, jenis_kelamin = #{jenis_kelamin}, is_wni = #{is_wni},"
			+ "id_keluarga = #{id_keluarga}, agama = #{agama}, pekerjaan = #{pekerjaan},"
			+ "status_perkawinan = #{status_perkawinan}, status_dalam_keluarga = #{status_dalam_keluarga},"
			+ "golongan_darah = #{golongan_darah}, is_wafat = #{is_wafat} where id = #{id}")
	void updatePenduduk(PendudukModel penduduk);
	
	@Update("update keluarga set nomor_kk = #{nomor_kk}, alamat = #{alamat}, RT = #{RT}, "
			+ "RW = #{RW}, id_kelurahan = #{id_kelurahan},"
			+ " is_tidak_berlaku = #{is_tidak_berlaku} where id = #{id}")
	void updateKeluarga(KeluargaModel keluarga);
	
	@Select("select DISTINCT kl.nama_kelurahan FROM kelurahan kl, keluarga ke WHERE "
			+ "kl.id = ke.id_kelurahan and ke.nomor_kk = #{nomor_kk}")
	String viewKelurahanKeluarga(@Param("nomor_kk")String nomor_kk);
	
	@Select("select DISTINCT kc.nama_kecamatan FROM kecamatan kc, keluarga ke, "
			+ "kelurahan kl where kc.id = kl.id_kecamatan and kl.id = ke.id_kelurahan and ke.nomor_kk = #{nomor_kk}")
	String viewKecamatanKeluarga(@Param("nomor_kk")String nomor_kk);
	
	@Select("select DISTINCT k.nama_kota from kelurahan kl, kecamatan kc, "
			+ "kota k, keluarga ke where k.id = kc.id_kota and kc.id = kl.id_kecamatan and "
			+ "kl.id = ke.id_kelurahan and ke.nomor_kk = #{nomor_kk}")
	String viewKotaKeluarga(@Param("nomor_kk")String nomor_kk);
	
	@Select("select * from kota")
	List<KotaModel> selectAllKota();
	
	@Select("select * from kota where id = #{id}")
	KotaModel selectKota(String id);
	
	@Select("select * from kecamatan where id_kota = #{kota}")
	List<KecamatanModel> selectAllKecamatan(String kota);
	
	@Select("select * from kecamatan where id = #{id}")
	KecamatanModel selectKecamatan(String id);
	
	@Select("select * from kelurahan where id_kecamatan = #{kecamatan}")
	List<KelurahanModel> selectAllKelurahan(String kecamatan);
	
	@Select("select * from kelurahan where id = #{id}")
	KelurahanModel selectKelurahan(String id);
	
	@Select("select * from penduduk p, keluarga k where p.id_keluarga = k.id and id_kelurahan = #{id_kelurahan}")
	List<PendudukModel> selectPendudukByKelurahan(String id_kelurahan);
	
	@Select("select is_wafat from penduduk where id_keluarga = #{id_keluarga}")
	List<String> selectWafatKeluarga(@Param("id_keluarga")String id_keluarga);
	
	@Select("select * from keluarga where id = #{id_keluarga}")
	KeluargaModel selectKeluargaById(@Param("id_keluarga") String id_keluarga);
	
}
