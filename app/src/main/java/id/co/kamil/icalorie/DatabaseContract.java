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
    public class Exercise implements BaseColumns{
        public static final String TABLE_EXERCISE = "tb_exercise";
        public static final String EXERCISE_COL_TYPE = "e_type";
        public static final String EXERCISE_COL_STEP = "e_step";
        public static final String EXERCISE_COL_DISTANCE = "e_distance";
        public static final String EXERCISE_COL_CALORIES = "e_calories";
        public static final String EXERCISE_COL_LENGTH_TIME = "e_length_time";
        public static final String EXERCISE_COL_START_DATE = "e_startdate";
    }
    public class RangeKalori implements BaseColumns{
        public static final String TABLE_RANGE_KALORI = "tb_range_kalori";
        public static final String RK_COL_KODE = "rk_kode";
        public static final String RK_COL_NAMA = "rk_nama";
        public static final String RK_COL_BERAT_SAJI = "rk_berat";
        public static final String RK_COL_KALORI = "rk_kalori";
        public static final String RK_COL_KALORI_AWAL = "rk_kalori_1";
        public static final String RK_COL_KALORI_AKHIR = "rk_kalori_2";
        public static final String RK_COL_ID_PARENT = "rk_id_parent";

    }
}
