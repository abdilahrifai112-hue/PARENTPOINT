package parentpoint.util;

public class Session {
    private static String username;
    private static String role;
    private static int siswaId;
    private static String namaSiswa;
    private static String namaOrtu;
    private static int kelasId;
    private static String namaKelas;
    private static String guruNama;

    public static void setSession(String u, String r, int sId, String nSiswa, String nOrtu, int kId, String nKelas, String gNama) {
        username = u;
        role = r;
        siswaId = sId;
        namaSiswa = nSiswa;
        namaOrtu = nOrtu;
        kelasId = kId;
        namaKelas = nKelas;
        guruNama = gNama;
    }

    public static void clearSession() {
        username = null;
        role = null;
        siswaId = 0;
        namaSiswa = null;
        namaOrtu = null;
        kelasId = 0;
        namaKelas = null;
        guruNama = null;
    }

    public static String getUsername() { return username; }
    public static String getRole() { return role; }
    public static int getSiswaId() { return siswaId; }
    public static String getNamaSiswa() { return namaSiswa; }
    public static String getNamaOrtu() { return namaOrtu; }
    public static int getKelasId() { return kelasId; }
    public static String getNamaKelas() { return namaKelas; }
    public static String getGuruNama() { return guruNama; }
}
