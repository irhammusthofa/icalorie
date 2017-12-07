package id.co.kamil.icalorie;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Irham on 12/5/2017.
 */

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String TAG = "DatabaseHelper";
    public static final String DATABASE_NAME = "icalori.db";
    public static final int DATABASE_VERSION = 1;
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

    public DatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
