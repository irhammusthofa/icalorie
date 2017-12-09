package id.co.kamil.icalorie;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class EditNutrisiActivity extends AppCompatActivity {
    private static final String TAG = "EditNutrisiActivity";
    private int TypeForm;
    private String Id;
    private TextView txtTitle;
    private EditText txtNama,txtBerat, txtKalori,txtKarbohidrat,txtProtein,txtVitA,txtVitB,txtVitC;
    private Button btnSimpan;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_nutrisi);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        dbHelper = new DatabaseHelper(this);

        TypeForm =  getIntent().getIntExtra("Type",0);
        Id = getIntent().getStringExtra("Id");
        txtTitle = (TextView) findViewById(R.id.txtTitle);
        txtNama = (EditText) findViewById(R.id.edtNutrisi);
        txtBerat = (EditText) findViewById(R.id.edtBeratSaji);
        txtKalori= (EditText) findViewById(R.id.edtKalori);
        txtKarbohidrat= (EditText) findViewById(R.id.edtKarbohidrat);
        txtProtein= (EditText) findViewById(R.id.edtProtein);
        txtVitA= (EditText) findViewById(R.id.edtVitA);
        txtVitB= (EditText) findViewById(R.id.edtVitB);
        txtVitC= (EditText) findViewById(R.id.edtVitC);

        btnSimpan = (Button) findViewById(R.id.btnSimpan);
        if (TypeForm==0){
            getSupportActionBar().setTitle("Tambah Nutrisi");
            txtTitle.setText("Tambah data nutrisi");
        }else{
            getSupportActionBar().setTitle("Edit Nutrisi");
            final SQLiteDatabase db = dbHelper.getWritableDatabase();
            final Cursor cursor = db.rawQuery(String.format("SELECT * FROM %s WHERE %s=%s",DatabaseContract.Nutrisi.TABLE_NUTRISI,DatabaseContract.Nutrisi._ID,Id),null);
            if (cursor!=null){
                try {
                    if (cursor.moveToFirst()) {
                        do {
                            final String nama = cursor.getString(cursor.getColumnIndex(DatabaseContract.Nutrisi.NUTRISI_COL_NAMA));
                            final Double berat = cursor.getDouble(cursor.getColumnIndex(DatabaseContract.Nutrisi.NUTRISI_COL_BERAT));
                            final Double kalori = cursor.getDouble(cursor.getColumnIndex(DatabaseContract.Nutrisi.NUTRISI_COL_KALORI));
                            final Double karbohidrat = cursor.getDouble(cursor.getColumnIndex(DatabaseContract.Nutrisi.NUTRISI_COL_KARBOHIDRAT));
                            final Double protein = cursor.getDouble(cursor.getColumnIndex(DatabaseContract.Nutrisi.NUTRISI_COL_PROTEIN));
                            final Double vit_a = cursor.getDouble(cursor.getColumnIndex(DatabaseContract.Nutrisi.NUTRISI_COL_VIT_A));
                            final Double vit_b = cursor.getDouble(cursor.getColumnIndex(DatabaseContract.Nutrisi.NUTRISI_COL_VIT_B));
                            final Double vit_c = cursor.getDouble(cursor.getColumnIndex(DatabaseContract.Nutrisi.NUTRISI_COL_VIT_C));
                            txtNama.setText(nama);
                            txtBerat.setText(Double.valueOf(berat).toString());
                            txtKalori.setText(Double.valueOf(kalori).toString());
                            txtKarbohidrat.setText(Double.valueOf(karbohidrat).toString());
                            txtProtein.setText(Double.valueOf(protein).toString());
                            txtVitA.setText(Double.valueOf(vit_a).toString());
                            txtVitB.setText(Double.valueOf(vit_b).toString());
                            txtVitC.setText(Double.valueOf(vit_c).toString());
                        } while (cursor.moveToNext());
                    }else{
                        Log.i(TAG,"Tidak ada data yang dapat ditampilkan");
                    }
                }finally {
                    Log.i(TAG,"Cursor state : Close");
                    cursor.close();
                }
            }
            txtTitle.setText("Edit data nutrisi");
        }

        btnSimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nama, berat,karbohidrat,protein,kalori,vita,vitb,vitc;
                nama = txtNama.getText().toString();
                berat = txtBerat.getText().toString();
                karbohidrat = txtKarbohidrat.getText().toString();
                kalori = txtKalori.getText().toString();
                protein = txtProtein.getText().toString();
                vita = txtVitA.getText().toString();
                vitb = txtVitB.getText().toString();
                vitc = txtVitC.getText().toString();
                if (nama.isEmpty()){
                    Toast.makeText(getApplicationContext(),"Nama nutrisi tidak boleh kosong.",Toast.LENGTH_SHORT).show();
                }else{
                    final SQLiteDatabase db = dbHelper.getWritableDatabase();
                    final ContentValues values = new ContentValues();
                    values.put(DatabaseContract.Nutrisi.NUTRISI_COL_NAMA,nama);
                    values.put(DatabaseContract.Nutrisi.NUTRISI_COL_BERAT,berat);
                    values.put(DatabaseContract.Nutrisi.NUTRISI_COL_KARBOHIDRAT,karbohidrat);
                    values.put(DatabaseContract.Nutrisi.NUTRISI_COL_KALORI,kalori);
                    values.put(DatabaseContract.Nutrisi.NUTRISI_COL_PROTEIN,protein);
                    values.put(DatabaseContract.Nutrisi.NUTRISI_COL_VIT_A,vita);
                    values.put(DatabaseContract.Nutrisi.NUTRISI_COL_VIT_B,vitb);
                    values.put(DatabaseContract.Nutrisi.NUTRISI_COL_VIT_C,vitc);
                    if (TypeForm==0){
                        Log.i(TAG,"Insert data : " + values);
                        db.insert(DatabaseContract.Nutrisi.TABLE_NUTRISI,null,values);

                        Toast.makeText(getApplicationContext(), "Data berhasil disimpan", Toast.LENGTH_SHORT).show();
                    }else if (TypeForm==1){
                        Log.i(TAG,"Update data Id : " + Id + ". " + values);
                        db.update(DatabaseContract.Nutrisi.TABLE_NUTRISI,values,String.format("%s = ?",DatabaseContract.Nutrisi._ID), new String[]{Id});
                        Toast.makeText(getApplicationContext(), "Data berhasil diupdate", Toast.LENGTH_SHORT).show();
                    }

                    Intent intent = new Intent();
                    intent.putExtra("stateSave", 1);
                    setResult(RESULT_OK, intent);
                    finish();
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu,menu);
        menu.findItem(R.id.menu_insert).setVisible(false);
        menu.findItem(R.id.menu_delete).setVisible(false);
        menu.findItem(R.id.menu_edit).setVisible(false);
        menu.findItem(R.id.menu_logout).setVisible(false);
        if (TypeForm==1){
            menu.findItem(R.id.menu_delete).setVisible(true);
        }
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
            case R.id.menu_delete:
                AlertDialog.Builder alBuilder = new AlertDialog.Builder(this);
                alBuilder.setTitle("Hapus");
                alBuilder.setMessage("Apakah anda yakin akan menghapus data berikut?");
                alBuilder.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.i(TAG,"Sedang menghapus data nutrisi");
                        // TODO: Hapus data
                        final SQLiteDatabase db = dbHelper.getWritableDatabase();
                        db.delete(DatabaseContract.Nutrisi.TABLE_NUTRISI,String.format("%s = ?",DatabaseContract.Nutrisi._ID),new String[]{Id});
                        Intent intent = new Intent();
                        intent.putExtra("stateSave", 1);
                        setResult(RESULT_OK, intent);
                        finish();
                    }
                });
                alBuilder.setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                alBuilder.show();
                break;

            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

}
