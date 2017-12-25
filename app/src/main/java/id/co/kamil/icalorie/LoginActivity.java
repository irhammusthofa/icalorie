package id.co.kamil.icalorie;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";
    private Button btnLogin,btnDaftar;
    private String email,password;
    private EditText edtEmail,edtPassword;
    private DatabaseHelper dbHelper;
    private SessionManager session;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().setTitle("Login");

        session = new SessionManager(getApplicationContext());
        dbHelper = new DatabaseHelper(this);

        edtEmail = (EditText) findViewById(R.id.edtEmail);
        edtPassword = (EditText) findViewById(R.id.edtPassword);


        btnLogin = (Button) findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = edtEmail.getText().toString();
                password = edtPassword.getText().toString();
                if (email.isEmpty() && password.isEmpty()){
                    Toast.makeText(getApplicationContext(),"Email atau Password masih kosong",Toast.LENGTH_SHORT).show();
                }else{
                    /*startActivity(new Intent(getApplicationContext(),MainActivity.class));
                    finish();*/
                    doLogin();
                }
            }
        });

        btnDaftar = (Button) findViewById(R.id.btnDaftar);
        btnDaftar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),DaftarActivity.class));
            }
        });
    }


    private void doLogin(){
        final SQLiteDatabase db = dbHelper.getWritableDatabase();
        final Cursor cursor = db.rawQuery(String.format(
                "SELECT * FROM %s WHERE %s=? AND %s=?",DatabaseContract.Pengguna.TABLE_PENGGUNA,
                DatabaseContract.Pengguna.PENGGUNA_COL_EMAIL,DatabaseContract.Pengguna.PENGGUNA_COL_PASSWORD
        ),new String[]{email,password});
        if (cursor != null){
            try {
                if (cursor.moveToFirst()){
                    final Integer id = cursor.getInt(cursor.getColumnIndex(DatabaseContract.Pengguna._ID));

                    session.createLoginSession(Integer.valueOf(id).toString());

                    Log.i(TAG,"Success Login : " + email);
                    startActivity(new Intent(getApplicationContext(),MainActivity.class));
                    finish();
                }else{
                    Log.i(TAG,"Akun tidak dapat ditemukan");
                    Toast.makeText(this,"Maaf, Email / Password masih salah.",Toast.LENGTH_SHORT).show();
                }
            }finally {
                Log.i(TAG,"Cursor close");
                cursor.close();
            }
        }
    }
}
