package id.co.kamil.icalorie;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;


public class BmrFragment extends Fragment {
    private static final String TAG = "BmrFragment";
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private EditText usia,berat,tinggi;
    private TextView hasil;
    private RadioButton laki,perempuan;
    private Button btnHitung;

    public BmrFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BmrFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static BmrFragment newInstance(String param1, String param2) {
        BmrFragment fragment = new BmrFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("kalori",hasil.getText().toString());
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null){
            hasil.setText(savedInstanceState.getString("kalori"));
        }
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

        View view = inflater.inflate(R.layout.fragment_bmr, container, false);
        usia = (EditText) view.findViewById(R.id.edtUsia);
        berat = (EditText) view.findViewById(R.id.edtBerat);
        tinggi = (EditText) view.findViewById(R.id.edtTinggi);
        hasil = (TextView) view.findViewById(R.id.txtHasil);
        laki = (RadioButton) view.findViewById(R.id.optLaki);
        perempuan = (RadioButton) view.findViewById(R.id.optPerempuan);
        btnHitung = (Button) view.findViewById(R.id.btnHitung);

        btnHitung.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                double tmpUsia,tmpBerat,tmpTinggi,tmpHasil = 0;
                String tmpGender="";
                try {
                    tmpUsia = Double.parseDouble(usia.getText().toString());
                    tmpBerat = Double.parseDouble(berat.getText().toString());
                    tmpTinggi = Double.parseDouble(tinggi.getText().toString());
                    if (laki.isChecked()) {
                        tmpGender = "Laki-laki";
                    } else if (perempuan.isChecked()) {
                        tmpGender = "Perempuan";
                    } else {
                        tmpGender = "";
                    }

                    if (tmpUsia <= 0) {
                        Toast.makeText(getContext(), "Usia tidak valid atau belum diisi. Usia harus lebih dari 0.", Toast.LENGTH_SHORT).show();
                    } else if (tmpBerat <= 0) {
                        Toast.makeText(getContext(), "Berat badan tidak valid atau belum diisi. Berat badan harus lebih dari 0.", Toast.LENGTH_SHORT).show();
                    } else if (tmpTinggi <= 0) {
                        Toast.makeText(getContext(), "Tinggi badan tidak valid atau belum diisi. Tinggi badan harus lebih dari 0.", Toast.LENGTH_SHORT).show();
                    } else if (tmpGender.isEmpty()) {
                        Toast.makeText(getContext(), "Jenis kelamin belum diisi.", Toast.LENGTH_SHORT).show();
                    } else if (tmpGender.equals("Laki-laki")) {
                        tmpHasil = 66 + (13.7 * tmpBerat) + (5 * tmpTinggi) - (6.8 * tmpUsia);
                    } else if (tmpGender.equals("Perempuan")) {
                        tmpHasil = 655 + (9.6 * tmpBerat) + (1.8 * tmpTinggi) - (4.7 * tmpUsia);
                    }
                    String hasilAkhir = String.format("%.2f KKal", tmpHasil);
                    hasil.setText(hasilAkhir);
                }catch (NumberFormatException e){
                    Log.e(TAG,e.getMessage());
                    Toast.makeText(getContext(),"Format Number tidak valid.",Toast.LENGTH_SHORT).show();
                }catch (Exception e){
                    Log.e(TAG, e.getMessage());
                    Toast.makeText(getContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
                }
            }
        });
        return view;
    }



}
