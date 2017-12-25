package id.co.kamil.icalorie;

import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

public class DaftarActivity extends AppCompatActivity {
    private static final String TAG = "DaftarActivity";
    private EditText edtNama,edtTglLahir,edtBerat,edtTinggi,edtEmail,edtTelepon,edtPassword;
    private RadioButton optLaki,optPerempuan;
    private DatePickerDialog datePickerDialog;
    private Button btnSimpan;
    private DatabaseHelper dbHelper;
    private EmailExisting emailExisting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daftar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Daftar");
        dbHelper = new DatabaseHelper(this);
        emailExisting = new EmailExisting(this);

        edtNama = (EditText) findViewById(R.id.edtNama);
        edtTglLahir = (EditText) findViewById(R.id.edtTglLahir);
        edtBerat = (EditText) findViewById(R.id.edtBerat);
        edtTinggi = (EditText) findViewById(R.id.edtTinggi);
        edtEmail = (EditText) findViewById(R.id.edtEmail);
        edtTelepon = (EditText) findViewById(R.id.edtTelepon);
        edtPassword = (EditText) findViewById(R.id.edtPassword);
        optLaki = (RadioButton) findViewById(R.id.optLaki);
        optPerempuan = (RadioButton) findViewById(R.id.optPerempuan);
        btnSimpan = (Button) findViewById(R.id.btnSimpan);
        btnSimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nama, jenisKelamin,tglLahir,berat,tinggi,email,telepon,password;
                nama = edtNama.getText().toString();
                tglLahir = edtTglLahir.getText().toString();
                berat = edtBerat.getText().toString();
                tinggi = edtTinggi.getText().toString();
                email = edtEmail.getText().toString();
                telepon = edtTelepon.getText().toString();
                password = edtPassword.getText().toString();

                if (nama.isEmpty()){
                    Toast.makeText(getApplicationContext(),"Nama Lengkap tidak boleh kosong",Toast.LENGTH_SHORT).show();
                }else if(!optLaki.isChecked() && !optPerempuan.isChecked()){
                    Toast.makeText(getApplicationContext(),"Jenis kelamin belum dipilih",Toast.LENGTH_SHORT).show();
                }else if(tglLahir.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Tgl Lahir tidak boleh kosong", Toast.LENGTH_SHORT).show();
                }else if(berat.isEmpty()){
                    Toast.makeText(getApplicationContext(), "Berat badan tidak boleh kosong", Toast.LENGTH_SHORT).show();
                }else if(tinggi.isEmpty()){
                    Toast.makeText(getApplicationContext(), "Tinggi badan tidak boleh kosong", Toast.LENGTH_SHORT).show();
                }else if(email.isEmpty()){
                    Toast.makeText(getApplicationContext(), "Email tidak boleh kosong", Toast.LENGTH_SHORT).show();
                }else if(telepon.isEmpty()){
                    Toast.makeText(getApplicationContext(), "Telepon tidak boleh kosong", Toast.LENGTH_SHORT).show();
                }else if(password.isEmpty()){
                    Toast.makeText(getApplicationContext(), "Password tidak boleh kosong", Toast.LENGTH_SHORT).show();
                }else if (emailExisting.checkEmail(email)) {
                    Toast.makeText(getApplicationContext(), "Email yang anda masukan sudah terdaftar", Toast.LENGTH_SHORT).show();
                }else{
                    final SQLiteDatabase db = dbHelper.getWritableDatabase();
                    final ContentValues values = new ContentValues();
                    jenisKelamin = "L";
                    if (optPerempuan.isChecked()) jenisKelamin = "P";

                    values.put(DatabaseContract.Pengguna.PENGGUNA_COL_NAMA,nama);
                    values.put(DatabaseContract.Pengguna.PENGGUNA_COL_TGL_LAHIR,tglLahir);
                    values.put(DatabaseContract.Pengguna.PENGGUNA_COL_KELAMIN,jenisKelamin);
                    values.put(DatabaseContract.Pengguna.PENGGUNA_COL_BERAT,berat);
                    values.put(DatabaseContract.Pengguna.PENGGUNA_COL_TINGGI,tinggi);
                    values.put(DatabaseContract.Pengguna.PENGGUNA_COL_EMAIL,email);
                    values.put(DatabaseContract.Pengguna.PENGGUNA_COL_TELP,telepon);
                    values.put(DatabaseContract.Pengguna.PENGGUNA_COL_PASSWORD,password);

                    Log.i(TAG,"Insert data : " + values);
                    db.insert(DatabaseContract.Pengguna.TABLE_PENGGUNA,null,values);
                    Toast.makeText(getApplicationContext(), "Pendaftaran berhasil. Silahkan Login menggunakan Email dan Password yang telah anda daftarkan", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        });
        edtTglLahir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR); // current year
                int mMonth = c.get(Calendar.MONTH); // current month
                int mDay = c.get(Calendar.DAY_OF_MONTH); // current day
                // date picker dialog
                datePickerDialog = new DatePickerDialog(DaftarActivity.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                // set day of month , month and year value in the edit text
                                edtTglLahir.setText(dayOfMonth + "/"
                                        + (monthOfYear + 1) + "/" + year);
                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });
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
