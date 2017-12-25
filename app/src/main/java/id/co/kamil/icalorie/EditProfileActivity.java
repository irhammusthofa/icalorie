package id.co.kamil.icalorie;

import android.content.ContentValues;
import android.content.Intent;
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
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Toast;

public class EditProfileActivity extends AppCompatActivity {
    private static final String TAG = "EditProfileActivity";
    private DatabaseHelper dbHelper;
    private EditText edtNama,edtBerat,edtTinggi,edtTglLahir,edtEmail,edtTelepon;
    private RadioButton laki,perempuan;
    private ImageView imgProfile;
    private Button btnSimpan,btnCamera,btnGallery;
    private String Id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        dbHelper = new DatabaseHelper(this);
        Id = getIntent().getStringExtra("Id");
        imgProfile = (ImageView) findViewById(R.id.imgProfile);
        edtNama = (EditText) findViewById(R.id.edtNama);
        edtBerat = (EditText) findViewById(R.id.edtNama);
        edtTinggi = (EditText) findViewById(R.id.edtNama);
        edtTglLahir = (EditText) findViewById(R.id.edtNama);
        edtEmail = (EditText) findViewById(R.id.edtNama);
        edtTelepon = (EditText) findViewById(R.id.edtNama);
        laki = (RadioButton) findViewById(R.id.optLaki);
        perempuan = (RadioButton) findViewById(R.id.optPerempuan);

        btnSimpan = (Button) findViewById(R.id.btnSimpan);
        btnCamera = (Button) findViewById(R.id.btnCamera);
        btnGallery = (Button) findViewById(R.id.btnGallery);

        btnSimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edtNama.getText().equals("")){
                    Toast.makeText(getApplicationContext(),"Maaf, Nama lengkap tidak boleh kosong.",Toast.LENGTH_SHORT).show();
                }else if (edtBerat.getText().equals("")){
                    Toast.makeText(getApplicationContext(),"Maaf, Berat badan tidak boleh kosong.",Toast.LENGTH_SHORT).show();
                }else if (edtTinggi.getText().equals("")){
                    Toast.makeText(getApplicationContext(),"Maaf, Tinggi badan tidak boleh kosong.",Toast.LENGTH_SHORT).show();
                }else{
                    final SQLiteDatabase db = dbHelper.getWritableDatabase();
                    String gender = "";
                    if (laki.isChecked()){
                        gender = "L";
                    }else if(perempuan.isChecked()){
                        gender = "P";
                    }
                    final ContentValues values = new ContentValues();
                    values.put(DatabaseContract.Pengguna.PENGGUNA_COL_NAMA,edtNama.getText().toString());
                    values.put(DatabaseContract.Pengguna.PENGGUNA_COL_BERAT,edtBerat.getText().toString());
                    values.put(DatabaseContract.Pengguna.PENGGUNA_COL_TINGGI,edtTinggi.getText().toString());
                    values.put(DatabaseContract.Pengguna.PENGGUNA_COL_EMAIL,edtEmail.getText().toString());
                    values.put(DatabaseContract.Pengguna.PENGGUNA_COL_TGL_LAHIR,edtTglLahir.getText().toString());
                    values.put(DatabaseContract.Pengguna.PENGGUNA_COL_TELP,edtTelepon.getText().toString());
                    values.put(DatabaseContract.Pengguna.PENGGUNA_COL_KELAMIN,gender);
                    Log.i(TAG,"Inserting data");
                    db.update(DatabaseContract.Pengguna.TABLE_PENGGUNA,values,String.format("%s = ?",DatabaseContract.Pengguna._ID),new String[]{Id});
                    Toast.makeText(getApplicationContext(),"Profile berhasil diupdate.",Toast.LENGTH_SHORT).show();

                }
            }
        });
        loadData();
    }
    private void loadData(){
        final String Id = getIntent().getStringExtra("Id");


        final SQLiteDatabase db = dbHelper.getWritableDatabase();
        final Cursor cursor = db.rawQuery(String.format(
                "SELECT * FROM %s WHERE %s=%s",DatabaseContract.Pengguna.TABLE_PENGGUNA,DatabaseContract.Pengguna._ID, Id
        ),null);
        if (cursor != null){
            try {
                if (cursor.moveToFirst()){
                    do {
                        edtNama.setText(cursor.getString(cursor.getColumnIndex(DatabaseContract.Pengguna.PENGGUNA_COL_NAMA)));
                        edtBerat.setText(cursor.getString(cursor.getColumnIndex(DatabaseContract.Pengguna.PENGGUNA_COL_BERAT)));
                        edtTinggi.setText(cursor.getString(cursor.getColumnIndex(DatabaseContract.Pengguna.PENGGUNA_COL_TINGGI)));
                        edtTglLahir.setText(cursor.getString(cursor.getColumnIndex(DatabaseContract.Pengguna.PENGGUNA_COL_TGL_LAHIR)));
                        edtEmail.setText(cursor.getString(cursor.getColumnIndex(DatabaseContract.Pengguna.PENGGUNA_COL_EMAIL)));
                        edtTelepon.setText(cursor.getString(cursor.getColumnIndex(DatabaseContract.Pengguna.PENGGUNA_COL_TELP)));
                        final String tmpGender = cursor.getString(cursor.getColumnIndex(DatabaseContract.Pengguna.PENGGUNA_COL_KELAMIN));
                        if (tmpGender.equals("L")){
                            laki.setChecked(true);
                        }else if (tmpGender.equals("P")){
                            perempuan.setChecked(true);
                        }
                    }while(cursor.moveToNext());
                }else{
                    Toast.makeText(this,"Maaf, Data dengan Id : " + Id + " tidak dapat ditemukan.",Toast.LENGTH_SHORT).show();
                    finish();
                }
            }finally {
                Log.i(TAG,"Close cursor");
                cursor.close();
            }
        }
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
