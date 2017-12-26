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
    private EditText edtNama,edtBerat,edtTinggi,edtTglLahir,edtEmail,edtTelepon,edtPassword;
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
        Id = getIntent().getStringExtra("id");
        imgProfile = (ImageView) findViewById(R.id.imgProfile);
        edtNama = (EditText) findViewById(R.id.edtNama);
        edtTglLahir = (EditText) findViewById(R.id.edtTglLahir);
        edtBerat = (EditText) findViewById(R.id.edtBerat);
        edtTinggi = (EditText) findViewById(R.id.edtTinggi);
        edtEmail = (EditText) findViewById(R.id.edtEmail);
        edtTelepon = (EditText) findViewById(R.id.edtTelepon);
        edtPassword = (EditText) findViewById(R.id.edtPassword);
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
                    if (!edtPassword.getText().toString().isEmpty()){
                        values.put(DatabaseContract.Pengguna.PENGGUNA_COL_PASSWORD,edtPassword.getText().toString());
                    }
                    Log.i(TAG,"Inserting data");
                    db.update(DatabaseContract.Pengguna.TABLE_PENGGUNA,values,String.format("%s = ?",DatabaseContract.Pengguna._ID),new String[]{Id});
                    Toast.makeText(getApplicationContext(),"Profile berhasil diupdate.",Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent();
                    intent.putExtra("update",1);
                    setResult(RESULT_OK,intent);
                    finish();
                }
            }
        });
        loadData();
    }

    private void loadData(){

        final SQLiteDatabase db = dbHelper.getWritableDatabase();
        final Cursor cursor = db.rawQuery(String.format(
                "SELECT * FROM %s WHERE %s=?",DatabaseContract.Pengguna.TABLE_PENGGUNA,
                DatabaseContract.Pengguna._ID
        ),new String[]{Id});
        final Pengguna pengguna = new Pengguna();
        if (cursor != null) {
            try {
                if (cursor.moveToFirst()) {
                    do {
                        final String nama = cursor.getString(cursor.getColumnIndex(DatabaseContract.Pengguna.PENGGUNA_COL_NAMA));
                        final String email = cursor.getString(cursor.getColumnIndex(DatabaseContract.Pengguna.PENGGUNA_COL_EMAIL));
                        final String telepon = cursor.getString(cursor.getColumnIndex(DatabaseContract.Pengguna.PENGGUNA_COL_TELP));
                        final String kelamin = cursor.getString(cursor.getColumnIndex(DatabaseContract.Pengguna.PENGGUNA_COL_KELAMIN));
                        final String tgllahir = cursor.getString(cursor.getColumnIndex(DatabaseContract.Pengguna.PENGGUNA_COL_TGL_LAHIR));
                        final String berat = cursor.getString(cursor.getColumnIndex(DatabaseContract.Pengguna.PENGGUNA_COL_BERAT));
                        final String tinggi = cursor.getString(cursor.getColumnIndex(DatabaseContract.Pengguna.PENGGUNA_COL_TINGGI));
                        final String foto = cursor.getString(cursor.getColumnIndex(DatabaseContract.Pengguna.PENGGUNA_COL_FOTO));
                        Log.i(TAG,kelamin);
                        edtNama.setText(nama);
                        edtBerat.setText(berat);
                        edtTinggi.setText(tinggi);
                        edtEmail.setText(email);
                        edtTelepon.setText(telepon);
                        edtTglLahir.setText(tgllahir);
                        if (kelamin.equals("L")){
                            laki.setChecked(true);
                        }else{
                            perempuan.setChecked(true);
                        }

                    } while (cursor.moveToNext());

                } else {
                    Log.i(TAG, "User dengan Id '" + Id + "' tidak ditemukan");
                }
            } finally {
                Log.i(TAG, "Cursor close");
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
