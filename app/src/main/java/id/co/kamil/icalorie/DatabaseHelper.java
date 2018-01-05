package id.co.kamil.icalorie;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.sql.SQLException;

/**
 * Created by Irham on 12/5/2017.
 */

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String TAG = "DatabaseHelper";
    public static final String DATABASE_NAME = "icalori.db";
    public static final int DATABASE_VERSION = 3;
    public static final String CREATE_TABLE_PENGGUNA =
            String.format("CREATE TABLE %s " +
                    "(%s INTEGER PRIMARY KEY," +
                    "%s VARCHAR(30)," +
                    "%s VARCHAR(35)," +
                    "%s VARCHAR(35)," +
                    "%s DATE," +
                    "%s VARCHAR(50)," +
                    "%s DECIMAL(10,2)," +
                    "%s DECIMAL(10,2)," +
                    "%s VARCHAR(1)," +
                    "%s VARCHAR(15))",
                    DatabaseContract.Pengguna.TABLE_PENGGUNA,
                    DatabaseContract.Pengguna._ID,
                    DatabaseContract.Pengguna.PENGGUNA_COL_NAMA,
                    DatabaseContract.Pengguna.PENGGUNA_COL_PASSWORD,
                    DatabaseContract.Pengguna.PENGGUNA_COL_EMAIL,
                    DatabaseContract.Pengguna.PENGGUNA_COL_TGL_LAHIR,
                    DatabaseContract.Pengguna.PENGGUNA_COL_FOTO,
                    DatabaseContract.Pengguna.PENGGUNA_COL_BERAT,
                    DatabaseContract.Pengguna.PENGGUNA_COL_TINGGI,
                    DatabaseContract.Pengguna.PENGGUNA_COL_KELAMIN,
                    DatabaseContract.Pengguna.PENGGUNA_COL_TELP
            );
    public static final String CREATE_TABLE_NUTRISI =
            String.format("CREATE TABLE %s " +
                            "(%s INTEGER PRIMARY KEY," +
                            "%s VARCHAR(30)," +
                            "%s DECIMAL(10,2)," +
                            "%s DECIMAL(10,2)," +
                            "%s DECIMAL(10,2)," +
                            "%s DECIMAL(10,2)," +
                            "%s DECIMAL(10,2)," +
                            "%s DECIMAL(10,2)," +
                            "%s DECIMAL(10,2))",
                    DatabaseContract.Nutrisi.TABLE_NUTRISI,
                    DatabaseContract.Nutrisi._ID,
                    DatabaseContract.Nutrisi.NUTRISI_COL_NAMA,
                    DatabaseContract.Nutrisi.NUTRISI_COL_BERAT,
                    DatabaseContract.Nutrisi.NUTRISI_COL_KALORI,
                    DatabaseContract.Nutrisi.NUTRISI_COL_KARBOHIDRAT,
                    DatabaseContract.Nutrisi.NUTRISI_COL_PROTEIN,
                    DatabaseContract.Nutrisi.NUTRISI_COL_VIT_A,
                    DatabaseContract.Nutrisi.NUTRISI_COL_VIT_B,
                    DatabaseContract.Nutrisi.NUTRISI_COL_VIT_C
            );
    public static final String CREATE_TABLE_EXERCISE =
            String.format("CREATE TABLE %s " +
                            "(%s INTEGER PRIMARY KEY," +
                            "%s INTEGER(1)," +
                            "%s INTEGER(5)," +
                            "%s INTEGER(5)," +
                            "%s DECIMAL(10,2)," +
                            "%s LONG(50), " +
                            "%s LONG(50))",
                    DatabaseContract.Exercise.TABLE_EXERCISE,
                    DatabaseContract.Exercise._ID,
                    DatabaseContract.Exercise.EXERCISE_COL_TYPE,
                    DatabaseContract.Exercise.EXERCISE_COL_STEP,
                    DatabaseContract.Exercise.EXERCISE_COL_DISTANCE,
                    DatabaseContract.Exercise.EXERCISE_COL_CALORIES,
                    DatabaseContract.Exercise.EXERCISE_COL_LENGTH_TIME,
                    DatabaseContract.Exercise.EXERCISE_COL_START_DATE
            );
    public static final String CREATE_TABLE_KALORI =
            String.format("CREATE TABLE %s " +
                    "(%s INTEGER PRIMARY KEY," +
                    "%s VARCHAR(5)," +
                    "%s VARCHAR(30)," +
                    "%s DECIMAL(10,2)," +
                    "%s DECIMAL(10,2)," +
                    "%s DECIMAL(10,2)," +
                    "%s DECIMAL(10,2)," +
                    "%s INTEGER," +
                    "%s INTEGER)",
                    DatabaseContract.RangeKalori.TABLE_RANGE_KALORI,
                    DatabaseContract.RangeKalori._ID,
                    DatabaseContract.RangeKalori.RK_COL_KODE,
                    DatabaseContract.RangeKalori.RK_COL_NAMA,
                    DatabaseContract.RangeKalori.RK_COL_BERAT_SAJI,
                    DatabaseContract.RangeKalori.RK_COL_KALORI,
                    DatabaseContract.RangeKalori.RK_COL_KALORI_AWAL,
                    DatabaseContract.RangeKalori.RK_COL_KALORI_AKHIR,
                    DatabaseContract.RangeKalori.RK_COL_ID_PARENT,
                    DatabaseContract.RangeKalori.RK_COL_PARENT_TYPE
            );


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.i(TAG,"Cerating database");
        db.execSQL(CREATE_TABLE_PENGGUNA);
        db.execSQL(CREATE_TABLE_NUTRISI);
        db.execSQL(CREATE_TABLE_KALORI);
        db.execSQL(CREATE_TABLE_EXERCISE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.i(TAG,"Deleting Table");
        db.execSQL("DROP TABLE IF EXISTS " + DatabaseContract.Pengguna.TABLE_PENGGUNA);
        db.execSQL("DROP TABLE IF EXISTS " + DatabaseContract.Nutrisi.TABLE_NUTRISI);
        db.execSQL("DROP TABLE IF EXISTS " + DatabaseContract.RangeKalori.TABLE_RANGE_KALORI);
        db.execSQL("DROP TABLE IF EXISTS " + DatabaseContract.Exercise.TABLE_EXERCISE);
        onCreate(db);
    }
}
