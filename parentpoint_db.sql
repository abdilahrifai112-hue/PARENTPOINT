-- ============================================
-- DATABASE PARENTPOINT - Aplikasi Monitoring Siswa
-- Import file ini di phpMyAdmin
-- ============================================

-- Buat database
CREATE DATABASE IF NOT EXISTS parentpoint_db;
USE parentpoint_db;

-- ============================================
-- Tabel Users (untuk LOGIN)
-- ============================================
CREATE TABLE IF NOT EXISTS users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL,
    role VARCHAR(20) NOT NULL DEFAULT 'admin',
    siswa_id INT DEFAULT NULL,
    guru_nama VARCHAR(100) DEFAULT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (siswa_id) REFERENCES siswa(id) ON DELETE CASCADE
) ENGINE=InnoDB;

-- ============================================
-- Tabel Kelas
-- ============================================
CREATE TABLE IF NOT EXISTS kelas (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nama_kelas VARCHAR(20) NOT NULL,
    wali_kelas VARCHAR(100),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB;

-- ============================================
-- Tabel Siswa
-- ============================================
CREATE TABLE IF NOT EXISTS siswa (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nis VARCHAR(20) NOT NULL UNIQUE,
    nama VARCHAR(100) NOT NULL,
    kelas_id INT,
    jenis_kelamin ENUM('L','P') NOT NULL DEFAULT 'L',
    alamat TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (kelas_id) REFERENCES kelas(id) ON DELETE SET NULL
) ENGINE=InnoDB;

-- ============================================
-- Tabel Orang Tua
-- ============================================
CREATE TABLE IF NOT EXISTS orang_tua (
    id INT AUTO_INCREMENT PRIMARY KEY,
    siswa_id INT NOT NULL,
    nama_ortu VARCHAR(100) NOT NULL,
    no_telp VARCHAR(20),
    email VARCHAR(100),
    alamat TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (siswa_id) REFERENCES siswa(id) ON DELETE CASCADE
) ENGINE=InnoDB;

-- ============================================
-- Tabel Jadwal Kelas
-- ============================================
CREATE TABLE IF NOT EXISTS jadwal_kelas (
    id INT AUTO_INCREMENT PRIMARY KEY,
    kelas_id INT NOT NULL,
    hari VARCHAR(20) NOT NULL,
    jam_mulai TIME NOT NULL,
    jam_selesai TIME NOT NULL,
    mata_pelajaran VARCHAR(100) NOT NULL,
    guru VARCHAR(100),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (kelas_id) REFERENCES kelas(id) ON DELETE CASCADE
) ENGINE=InnoDB;

-- ============================================
-- Tabel Kehadiran
-- ============================================
CREATE TABLE IF NOT EXISTS kehadiran (
    id INT AUTO_INCREMENT PRIMARY KEY,
    siswa_id INT NOT NULL,
    tanggal DATE NOT NULL,
    status ENUM('Hadir','Sakit','Izin','Alpha') NOT NULL,
    keterangan TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (siswa_id) REFERENCES siswa(id) ON DELETE CASCADE
) ENGINE=InnoDB;

-- ============================================
-- DATA AWAL
-- ============================================

-- Admin default (username: admin, password: admin123)
INSERT INTO users (username, password, role, siswa_id, guru_nama) VALUES
('admin', 'admin123', 'admin', NULL, NULL),
('ortu1', 'ortu123', 'orang_tua', 1, NULL),
('guru1', 'guru123', 'guru', NULL, 'Ibu Sari');

-- Data Kelas
INSERT INTO kelas (nama_kelas, wali_kelas) VALUES
('X-A', 'Ibu Sari'),
('X-B', 'Bapak Budi'),
('XI-A', 'Ibu Dewi'),
('XI-B', 'Bapak Ahmad'),
('XII-A', 'Ibu Rina'),
('XII-B', 'Bapak Joko');

-- Data Siswa
INSERT INTO siswa (nis, nama, kelas_id, jenis_kelamin, alamat) VALUES
('2024001', 'Ahmad Fadillah', 1, 'L', 'Jl. Merdeka No. 10'),
('2024002', 'Siti Nurhaliza', 1, 'P', 'Jl. Sudirman No. 15'),
('2024003', 'Budi Santoso', 2, 'L', 'Jl. Pahlawan No. 20'),
('2024004', 'Dewi Lestari', 2, 'P', 'Jl. Diponegoro No. 25'),
('2024005', 'Rudi Hermawan', 3, 'L', 'Jl. Gatot Subroto No. 30'),
('2024006', 'Ani Wijaya', 3, 'P', 'Jl. Ahmad Yani No. 35'),
('2024007', 'Doni Prasetyo', 4, 'L', 'Jl. Veteran No. 40'),
('2024008', 'Rina Marlina', 4, 'P', 'Jl. Kartini No. 45'),
('2024009', 'Eko Saputra', 5, 'L', 'Jl. Imam Bonjol No. 50'),
('2024010', 'Maya Sari', 5, 'P', 'Jl. Teuku Umar No. 55');

-- Data Orang Tua
INSERT INTO orang_tua (siswa_id, nama_ortu, no_telp, email) VALUES
(1, 'H. Fadillah', '081234567890', 'fadillah@email.com'),
(2, 'Hj. Nurhaliza', '081234567891', 'nurhaliza@email.com'),
(3, 'Santoso', '081234567892', 'santoso@email.com'),
(4, 'Lestari', '081234567893', 'lestari@email.com'),
(5, 'Hermawan', '081234567894', 'hermawan@email.com'),
(6, 'Wijaya', '081234567895', 'wijaya@email.com'),
(7, 'Prasetyo', '081234567896', 'prasetyo@email.com'),
(8, 'Marlina', '081234567897', 'marlina@email.com'),
(9, 'Saputra', '081234567898', 'saputra@email.com'),
(10, 'Sari', '081234567899', 'sari@email.com');

-- Data Kehadiran (contoh beberapa hari)
INSERT INTO kehadiran (siswa_id, tanggal, status, keterangan) VALUES
(1, '2026-06-16', 'Hadir', NULL),
(2, '2026-06-16', 'Hadir', NULL),
(3, '2026-06-16', 'Sakit', 'Demam'),
(4, '2026-06-16', 'Hadir', NULL),
(5, '2026-06-16', 'Izin', 'Acara keluarga'),
(6, '2026-06-16', 'Hadir', NULL),
(7, '2026-06-16', 'Alpha', NULL),
(8, '2026-06-16', 'Hadir', NULL),
(9, '2026-06-16', 'Hadir', NULL),
(10, '2026-06-16', 'Hadir', NULL),
(1, '2026-06-17', 'Hadir', NULL),
(2, '2026-06-17', 'Sakit', 'Flu'),
(3, '2026-06-17', 'Hadir', NULL),
(4, '2026-06-17', 'Hadir', NULL),
(5, '2026-06-17', 'Hadir', NULL),
(6, '2026-06-17', 'Izin', 'Keperluan mendadak'),
(7, '2026-06-17', 'Hadir', NULL),
(8, '2026-06-17', 'Alpha', NULL),
(9, '2026-06-17', 'Hadir', NULL),
(10, '2026-06-17', 'Hadir', NULL),
(1, '2026-06-18', 'Hadir', NULL),
(2, '2026-06-18', 'Hadir', NULL),
(3, '2026-06-18', 'Hadir', NULL),
(4, '2026-06-18', 'Sakit', 'Batuk'),
(5, '2026-06-18', 'Hadir', NULL),
(6, '2026-06-18', 'Hadir', NULL),
(7, '2026-06-18', 'Hadir', NULL),
(8, '2026-06-18', 'Hadir', NULL),
(9, '2026-06-18', 'Alpha', NULL),
(10, '2026-06-18', 'Izin', 'Urusan keluarga');
