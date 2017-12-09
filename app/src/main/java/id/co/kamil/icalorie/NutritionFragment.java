package id.co.kamil.icalorie;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;


public class NutritionFragment extends Fragment {

    private static final String TAG = "NutritionFragment";
    private Spinner spNutrisi;
    private ArrayList<String> nutrisi = new ArrayList<String>();
    private List<Nutrisi> listNutrisi = new ArrayList<>();
    private DatabaseHelper dbHelper;
    private Button btnCalculation;
    private EditText edtBerat;
    private TextView txtKalori,txtKarbohidrat,txtProtein,txtVitA,txtVitB,txtVitC;

    public NutritionFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        dbHelper = new DatabaseHelper(getContext());
        View view = inflater.inflate(R.layout.fragment_nutrition, container, false);
        spNutrisi = (Spinner) view.findViewById(R.id.spNutrisi);
        btnCalculation = (Button) view.findViewById(R.id.btnCalculation);
        edtBerat = (EditText) view.findViewById(R.id.edtBerat);
        txtKalori = (TextView) view.findViewById(R.id.txtKalori);
        txtKarbohidrat = (TextView) view.findViewById(R.id.txtKarbohidrat);
        txtProtein = (TextView) view.findViewById(R.id.txtProtein);
        txtVitA = (TextView) view.findViewById(R.id.txtVitA);
        txtVitB = (TextView) view.findViewById(R.id.txtVitB);
        txtVitC = (TextView) view.findViewById(R.id.txtVitC);
        btnCalculation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(spNutrisi.getSelectedItemPosition()>=0){
                    try {
                        final double berat = listNutrisi.get(spNutrisi.getSelectedItemPosition()).getBerat();
                        final double kalori = listNutrisi.get(spNutrisi.getSelectedItemPosition()).getKalori();
                        final double karbohidrat = listNutrisi.get(spNutrisi.getSelectedItemPosition()).getKarbohidrat();
                        final double protein = listNutrisi.get(spNutrisi.getSelectedItemPosition()).getProtein();
                        final double vita = listNutrisi.get(spNutrisi.getSelectedItemPosition()).getVita();
                        final double vitb = listNutrisi.get(spNutrisi.getSelectedItemPosition()).getVitb();
                        final double vitc = listNutrisi.get(spNutrisi.getSelectedItemPosition()).getVitc();

                        final double berat_saji = Double.parseDouble(edtBerat.getText().toString());
                        final double percent = berat_saji/berat;

                        final String rKalori = String.format("%.2f KKal",kalori*percent);
                        final String rKarbohidrat = String.format("%.2f",karbohidrat*percent);
                        final String rProtein = String.format("%.2f",protein*percent);
                        final String rVitA = String.format("%.2f",vita*percent);
                        final String rVitB = String.format("%.2f",vitb*percent);
                        final String rVitC = String.format("%.2f",vitc*percent);

                        txtKalori.setText(rKalori);
                        txtKarbohidrat.setText(rKarbohidrat);
                        txtProtein.setText(rProtein);
                        txtVitA.setText(rVitA);
                        txtVitB.setText(rVitB);
                        txtVitC.setText(rVitC);
                    }catch (NumberFormatException e){
                        Log.i(TAG,e.getMessage());
                        Toast.makeText(getContext(),"Berat saji tidak valid",Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(getContext(),"Nutrisi belum dipilih atau Berat saji masih kosong.",Toast.LENGTH_SHORT).show();
                }
            }
        });
        loadNutrisi();
        displayNutrisi();
        return view;
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if(resultCode == RESULT_OK) {
                int stateSave = data.getIntExtra("stateSave",0);
                Log.i(TAG,"Result Index : " + stateSave);
                if (stateSave==1){
                    loadNutrisi();
                    displayNutrisi();
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    private void displayNutrisi(){
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),android.R.layout.simple_spinner_item,nutrisi);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spNutrisi.setAdapter(adapter);
    }
    private void loadNutrisi(){
        nutrisi.clear();
        listNutrisi.clear();
        final SQLiteDatabase db = dbHelper.getWritableDatabase();
        final Cursor cursor = db.rawQuery(String.format(
                "SELECT * FROM %s ORDER BY %s",DatabaseContract.Nutrisi.TABLE_NUTRISI,DatabaseContract.Nutrisi._ID
        ),null);
        if (cursor != null){
            try {
                if (cursor.moveToFirst()){
                    do {
                        final int id = cursor.getInt(cursor.getColumnIndex(DatabaseContract.Nutrisi._ID));
                        final String nama = cursor.getString(cursor.getColumnIndex(DatabaseContract.Nutrisi.NUTRISI_COL_NAMA));
                        final double berat = cursor.getDouble(cursor.getColumnIndex(DatabaseContract.Nutrisi.NUTRISI_COL_BERAT));
                        final double kalori = cursor.getDouble(cursor.getColumnIndex(DatabaseContract.Nutrisi.NUTRISI_COL_KALORI));
                        final double karbohidrat = cursor.getDouble(cursor.getColumnIndex(DatabaseContract.Nutrisi.NUTRISI_COL_KARBOHIDRAT));
                        final double protein = cursor.getDouble(cursor.getColumnIndex(DatabaseContract.Nutrisi.NUTRISI_COL_PROTEIN));
                        final double vita = cursor.getDouble(cursor.getColumnIndex(DatabaseContract.Nutrisi.NUTRISI_COL_VIT_A));
                        final double vitb = cursor.getDouble(cursor.getColumnIndex(DatabaseContract.Nutrisi.NUTRISI_COL_VIT_B));
                        final double vitc = cursor.getDouble(cursor.getColumnIndex(DatabaseContract.Nutrisi.NUTRISI_COL_VIT_C));

                        final Nutrisi obNutrisi = new Nutrisi();
                        obNutrisi.setId(id);
                        obNutrisi.setNama(nama);
                        obNutrisi.setBerat(berat);
                        obNutrisi.setKalori(kalori);
                        obNutrisi.setKarbohidrat(karbohidrat);
                        obNutrisi.setProtein(protein);
                        obNutrisi.setVita(vita);
                        obNutrisi.setVitb(vitb);
                        obNutrisi.setVitc(vitc);
                        nutrisi.add(nama);
                        listNutrisi.add(obNutrisi);

                        Log.i(TAG,"Get Data : " + obNutrisi.toString());
                    }while(cursor.moveToNext());
                }else{
                    Log.i(TAG,"Tidak ada data yang dapat ditampilkan");
                }
            }finally {
                Log.i(TAG,"Cursor close");
                cursor.close();
            }
        }
    }
}
