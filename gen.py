import os
import re

with open("src/parentpoint/master/MasterKelas.java", "r", encoding="utf-8") as f:
    content = f.read()

# Mapel
mapel = content.replace("MasterKelas", "MasterMapel")
mapel = mapel.replace("Master Data Kelas", "Master Data Mata Pelajaran")
# Variable names
mapel = mapel.replace("tfNamaKelas", "tfNamaMapel")
mapel = mapel.replace("jLabelNamaKelas", "jLabelNamaMapel")
mapel = mapel.replace("tfWaliKelas", "tfKeterangan")
mapel = mapel.replace("jLabelWaliKelas", "jLabelKeterangan")
# UI text
mapel = mapel.replace("Nama Kelas :", "Nama Mapel :")
mapel = mapel.replace("Wali Kelas :", "Keterangan :")
mapel = mapel.replace("Nama Kelas", "Nama Mapel")
mapel = mapel.replace("Wali Kelas", "Keterangan")

# SQL & Data
mapel = mapel.replace(
    '''SELECT k.id, k.nama_kelas, k.wali_kelas, COUNT(s.id) AS jml " +
                    "FROM kelas k LEFT JOIN siswa s ON k.id = s.kelas_id " +
                    "GROUP BY k.id, k.nama_kelas, k.wali_kelas ORDER BY k.nama_kelas''',
    '''SELECT id, nama_mapel, '' AS keterangan, 0 AS jml FROM mata_pelajaran ORDER BY nama_mapel'''
)
mapel = mapel.replace("nama_kelas", "nama_mapel")
mapel = mapel.replace("wali_kelas", "keterangan")
mapel = mapel.replace("INSERT INTO kelas (nama_mapel, keterangan) VALUES (?,?)", "INSERT INTO mata_pelajaran (nama_mapel) VALUES (?)")
mapel = mapel.replace("UPDATE kelas SET nama_mapel=?, keterangan=? WHERE id=?", "UPDATE mata_pelajaran SET nama_mapel=? WHERE id=?")
mapel = mapel.replace("DELETE FROM kelas WHERE id=?", "DELETE FROM mata_pelajaran WHERE id=?")
mapel = mapel.replace("ps.setString(2, tfKeterangan.getText().trim());\n                ps.executeUpdate();", "ps.executeUpdate();")
mapel = mapel.replace("ps.setString(2, tfKeterangan.getText().trim());\n                ps.setInt(3, selectedId);", "ps.setInt(2, selectedId);")

with open("src/parentpoint/master/MasterMapel.java", "w", encoding="utf-8") as f:
    f.write(mapel)

# Guru
guru = content.replace("MasterKelas", "MasterGuru")
guru = guru.replace("Master Data Kelas", "Master Data Guru")
# Variable names
guru = guru.replace("tfNamaKelas", "tfNamaGuru")
guru = guru.replace("jLabelNamaKelas", "jLabelNamaGuru")
guru = guru.replace("tfWaliKelas", "tfNip")
guru = guru.replace("jLabelWaliKelas", "jLabelNip")
# UI text
guru = guru.replace("Nama Kelas :", "Nama Guru :")
guru = guru.replace("Wali Kelas :", "NIP :")
guru = guru.replace("Nama Kelas", "Nama Guru")
guru = guru.replace("Wali Kelas", "NIP")

# SQL & Data
guru = guru.replace(
    '''SELECT k.id, k.nama_kelas, k.wali_kelas, COUNT(s.id) AS jml " +
                    "FROM kelas k LEFT JOIN siswa s ON k.id = s.kelas_id " +
                    "GROUP BY k.id, k.nama_kelas, k.wali_kelas ORDER BY k.nama_kelas''',
    '''SELECT id, nama_guru, nip, 0 AS jml FROM guru ORDER BY nama_guru'''
)
guru = guru.replace("nama_kelas", "nama_guru")
guru = guru.replace("wali_kelas", "nip")
guru = guru.replace("INSERT INTO kelas", "INSERT INTO guru")
guru = guru.replace("UPDATE kelas", "UPDATE guru")
guru = guru.replace("DELETE FROM kelas", "DELETE FROM guru")

with open("src/parentpoint/master/MasterGuru.java", "w", encoding="utf-8") as f:
    f.write(guru)

print("Generated Mapel and Guru via Python")
