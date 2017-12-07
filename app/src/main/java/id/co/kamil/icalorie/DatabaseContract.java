package id.co.kamil.icalorie;

import android.provider.BaseColumns;

/**
 * Created by Irham on 12/5/2017.
 */

public class DatabaseContract {
    public class Nutrisi implements BaseColumns{
        public static final String TABLE_NUTRISI = "tb_nutrisi";
        public static final String NUTRISI_COL_NAMA = "n_nama";
        public static final String NUTRISI_COL_BERAT = "n_berat";
        public static final String NUTRISI_COL_KALORI = "n_kalori";
        public static final String NUTRISI_COL_KARBOHIDRAT = "n_karbohidrat";
        public static final String NUTRISI_COL_PROTEIN = "n_protein";
        public static final String NUTRISI_COL_VIT_A = "n_vit_a";
        public static final String NUTRISI_COL_VIT_B = "n_vit_b";
        public static final String NUTRISI_COL_VIT_C = "n_vit_c";
    }
    public class Pengguna implements BaseColumns{
        public static final String TABLE_PENGGUNA  = "tb_pengguna";
        public static final String PENGGUNA_COL_NAMA  = "p_nama";
        public static final String PENGGUNA_COL_PASSWORD  = "p_pass";
        public static final String PENGGUNA_COL_EMAIL  = "p_email";
        public static final String PENGGUNA_COL_TGL_LAHIR  = "p_tgl_lahir";
        public static final String PENGGUNA_COL_FOTO  = "p_foto";
        public static final String PENGGUNA_COL_BERAT  = "p_berat";
        public static final String PENGGUNA_COL_TINGGI = "p_tinggi";
        public static final String PENGGUNA_COL_KELAMIN = "p_kelamin";
        public static final String PENGGUNA_COL_TELP = "p_telp";
    }
}
