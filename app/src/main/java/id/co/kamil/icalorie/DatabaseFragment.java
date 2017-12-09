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
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;


public class DatabaseFragment extends Fragment {
    private static final String TAG = "DatabaseFragment";

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private List<Nutrisi> list = new ArrayList<>();
    private ListView listDb;
    private Nutrisi nutrisi;
    private DatabaseHelper dbHelper;
    private TextView txtNotif;

    public DatabaseFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DatabaseFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DatabaseFragment newInstance(String param1, String param2) {
        DatabaseFragment fragment = new DatabaseFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_database, container, false);
        dbHelper = new DatabaseHelper(getContext());

        listDb = (ListView) view.findViewById(R.id.List1);
        txtNotif = (TextView) view.findViewById(R.id.txtNotif);
        listDb.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getContext(),EditNutrisiActivity.class);

                intent.putExtra("Id",String.valueOf(list.get(position).getId()));
                intent.putExtra("Type",1);
                startActivityForResult(intent,1);
            }
        });
        loadData();
        displayData();

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if(resultCode == RESULT_OK) {
                int stateSave = data.getIntExtra("stateSave",0);
                Log.i(TAG,"Result Index : " + stateSave);
                if (stateSave==1){
                    loadData();
                    displayData();
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void displayData(){
        final NutrisiAdapter nutrisiAdapter = new NutrisiAdapter(getContext(),list);
        listDb.setAdapter(nutrisiAdapter);
    }
    private void loadData(){
        list.clear();
        listDb.setVisibility(View.GONE);
        txtNotif.setVisibility(View.VISIBLE);
        final SQLiteDatabase db = dbHelper.getWritableDatabase();
        final Cursor cursor = db.rawQuery(String.format(
                "SELECT * FROM %s ORDER BY %s",DatabaseContract.Nutrisi.TABLE_NUTRISI,DatabaseContract.Nutrisi._ID
        ),null);
        if (cursor != null){
            try {
                if (cursor.moveToFirst()){
                    listDb.setVisibility(View.VISIBLE);
                    txtNotif.setVisibility(View.GONE);
                    do {
                        final Integer id = cursor.getInt(cursor.getColumnIndex(DatabaseContract.Nutrisi._ID));
                        final String nama = cursor.getString(cursor.getColumnIndex(DatabaseContract.Nutrisi.NUTRISI_COL_NAMA));
                        final Double berat = cursor.getDouble(cursor.getColumnIndex(DatabaseContract.Nutrisi.NUTRISI_COL_BERAT));
                        final Double kalori = cursor.getDouble(cursor.getColumnIndex(DatabaseContract.Nutrisi.NUTRISI_COL_KALORI));
                        final Double karbohidrat = cursor.getDouble(cursor.getColumnIndex(DatabaseContract.Nutrisi.NUTRISI_COL_KARBOHIDRAT));
                        final Double protein = cursor.getDouble(cursor.getColumnIndex(DatabaseContract.Nutrisi.NUTRISI_COL_PROTEIN));
                        final Double vit_a = cursor.getDouble(cursor.getColumnIndex(DatabaseContract.Nutrisi.NUTRISI_COL_VIT_A));
                        final Double vit_b = cursor.getDouble(cursor.getColumnIndex(DatabaseContract.Nutrisi.NUTRISI_COL_VIT_B));
                        final Double vit_c = cursor.getDouble(cursor.getColumnIndex(DatabaseContract.Nutrisi.NUTRISI_COL_VIT_C));

                        final Nutrisi nutrisi = new Nutrisi();
                        nutrisi.setId(id);
                        nutrisi.setNama(nama);
                        nutrisi.setBerat(berat);
                        nutrisi.setKalori(kalori);
                        nutrisi.setKarbohidrat(karbohidrat);
                        nutrisi.setProtein(protein);
                        nutrisi.setVita(vit_a);
                        nutrisi.setVitb(vit_b);
                        nutrisi.setVitc(vit_c);

                        Log.i(TAG,"Get data : "+nutrisi.toString());
                        list.add(nutrisi);
                    }while (cursor.moveToNext());

                }else{
                    Log.i(TAG,"Tidak ada data yang dapat ditampilkan");

                }
            }finally {
                Log.i(TAG,"Cursor state : Close");
                cursor.close();
            }
        }
    }
}
