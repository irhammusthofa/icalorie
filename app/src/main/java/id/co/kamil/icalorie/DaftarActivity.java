package id.co.kamil.icalorie;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

public class DaftarActivity extends AppCompatActivity {
    private static final String TAG = "DaftarActivity";
    private DatabaseHelper dbHelper;
    private EditText edtNama,edtBerat,edtTinggi,edtEmail,edtTglLahir,edtTelepon,edtPassword;
    private RadioButton optLaki,optPerempuan;
    private Button btnSimpan,btnCamera,btnGallery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daftar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        dbHelper = new DatabaseHelper(this);

        edtNama = (EditText) findViewById(R.id.edtNama);
        edtEmail = (EditText) findViewById(R.id.edtEmail);
        edtBerat = (EditText) findViewById(R.id.edtBerat);
        edtTinggi = (EditText) findViewById(R.id.edtTinggi);
        edtTglLahir = (EditText) findViewById(R.id.edtTglLahir);
        edtTelepon = (EditText) findViewById(R.id.edtTelepon);
        edtPassword = (EditText) findViewById(R.id.edtPassword);
        optLaki = (RadioButton) findViewById(R.id.optLaki);
        optPerempuan = (RadioButton) findViewById(R.id.optPerempuan);
        btnSimpan = (Button) findViewById(R.id.btnSimpan);
        btnCamera = (Button) findViewById(R.id.btnCamera);
        btnGallery = (Button) findViewById(R.id.btnGallery);

        btnSimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final SQLiteDatabase db = dbHelper.getWritableDatabase();
                final ContentValues values = new ContentValues();
                values.put(DatabaseContract.Pengguna.PENGGUNA_COL_NAMA,edtNama.getText().toString());
                values.put(DatabaseContract.Pengguna.PENGGUNA_COL_EMAIL,edtEmail.getText().toString());
                values.put(DatabaseContract.Pengguna.PENGGUNA_COL_BERAT,edtBerat.getText().toString());
                values.put(DatabaseContract.Pengguna.PENGGUNA_COL_TINGGI,edtTinggi.getText().toString());
                values.put(DatabaseContract.Pengguna.PENGGUNA_COL_TGL_LAHIR,edtTglLahir.getText().toString());
                values.put(DatabaseContract.Pengguna.PENGGUNA_COL_TELP,edtTelepon.getText().toString());
                values.put(DatabaseContract.Pengguna.PENGGUNA_COL_PASSWORD,edtPassword.getText().toString());
                values.put(DatabaseContract.Pengguna.PENGGUNA_COL_FOTO,"");

                String tempGender = "";
                if (optLaki.isChecked()){
                    tempGender = "L";
                }else{
                    tempGender = "P";
                }
                values.put(DatabaseContract.Pengguna.PENGGUNA_COL_KELAMIN,tempGender);
                if (!checkEmail(edtEmail.getText().toString())){
                    Log.i(TAG,"Inserting data " + values);
                    db.insert(DatabaseContract.Pengguna.TABLE_PENGGUNA,null,values);
                    Toast.makeText(getApplicationContext(), "Pendaftaran berhasil, silahkan Login menggunakan email dan password yang telah anda daftarkan", Toast.LENGTH_SHORT).show();
                    finish();
                }else{
                    Toast.makeText(getApplicationContext(), "Email yang anda masukan sudah terdaftar", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    public boolean checkEmail(String email){
        final SQLiteDatabase db = dbHelper.getWritableDatabase();
        final Cursor cursor = db.rawQuery(String.format(
                "SELECT * FROM %s WHERE %s=?",DatabaseContract.Pengguna.TABLE_PENGGUNA,DatabaseContract.Pengguna.PENGGUNA_COL_EMAIL
        ),new String[]{email});
        boolean status = false;
        if (cursor != null){
            try {
                if (cursor.getCount()>0){
                    status = true;
                }
            }finally {
                Log.i(TAG,"cursor close");
                cursor.close();
            }
        }
        return status;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
