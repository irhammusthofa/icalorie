package id.co.kamil.icalorie;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * Created by Irham on 12/11/2017.
 */

public class SessionManager {
    private static final String TAG = "SessionManager";
    private SharedPreferences pref;
    private Editor editor;
    private Context _context;
    private DatabaseHelper dbHelper;

    int PRIVATE_MODE = 0;

    private static final String PREF_NAME = "iCalorie";
    private static final String IS_LOGIN = "IsLoggedIn";
    private static final String KEY_ID = "id";

    public SessionManager(Context context){
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME,PRIVATE_MODE);
        editor = pref.edit();
        dbHelper = new DatabaseHelper(_context);
    }
    public void createLoginSession(String id){
        editor.putBoolean(IS_LOGIN, true);
        editor.putString(KEY_ID, id);
        editor.commit();
    }
    public boolean checkLogin(){
        return this.isLoggedIn();
    }
    public void logoutUser(){
        editor.clear();
        editor.commit();

        Intent i = new Intent(_context, LoginActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        _context.startActivity(i);
    }
    public void login(){
        Intent i = new Intent(_context, LoginActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        _context.startActivity(i);
    }
    public boolean isLoggedIn(){
        return pref.getBoolean(IS_LOGIN, false);
    }
    public String getId(){
        return pref.getString(KEY_ID,"");
    }
    public Pengguna getUserDetails(){
        final SQLiteDatabase db = dbHelper.getWritableDatabase();
        final Cursor cursor = db.rawQuery(String.format(
                "SELECT * FROM %s WHERE %s=?",DatabaseContract.Pengguna.TABLE_PENGGUNA,
                DatabaseContract.Pengguna._ID
        ),new String[]{getId()});
        final Pengguna pengguna = new Pengguna();
        if (cursor != null){
            try {
                if (cursor.moveToFirst()){
                    do {
                        final String nama = cursor.getString(cursor.getColumnIndex(DatabaseContract.Pengguna.PENGGUNA_COL_NAMA));
                        final String email = cursor.getString(cursor.getColumnIndex(DatabaseContract.Pengguna.PENGGUNA_COL_EMAIL));
                        final String telepon = cursor.getString(cursor.getColumnIndex(DatabaseContract.Pengguna.PENGGUNA_COL_TELP));
                        final String kelamin= cursor.getString(cursor.getColumnIndex(DatabaseContract.Pengguna.PENGGUNA_COL_KELAMIN));
                        final String tgllahir = cursor.getString(cursor.getColumnIndex(DatabaseContract.Pengguna.PENGGUNA_COL_TGL_LAHIR));
                        final String berat = cursor.getString(cursor.getColumnIndex(DatabaseContract.Pengguna.PENGGUNA_COL_BERAT));
                        final String tinggi = cursor.getString(cursor.getColumnIndex(DatabaseContract.Pengguna.PENGGUNA_COL_TINGGI));
                        final String foto = cursor.getString(cursor.getColumnIndex(DatabaseContract.Pengguna.PENGGUNA_COL_FOTO));

                        pengguna.setId(Integer.parseInt(getId()));
                        pengguna.setNama(nama);
                        pengguna.setBerat(berat);
                        pengguna.setTinggi(tinggi);
                        pengguna.setEmail(email);
                        pengguna.setTelepon(telepon);
                        pengguna.setKelamin(kelamin);
                        pengguna.setTgllahir(tgllahir);
                        pengguna.setFoto(foto);
                    }while (cursor.moveToNext());

                }else{
                    Log.i(TAG,"User dengan Id '" + getId() + "' tidak ditemukan");
                }
            }finally {
                Log.i(TAG,"Cursor close");
                cursor.close();
            }
        }
        return pengguna;
    }
}
