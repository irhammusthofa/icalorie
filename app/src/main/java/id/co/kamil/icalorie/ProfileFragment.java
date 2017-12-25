package id.co.kamil.icalorie;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


public class ProfileFragment extends Fragment {
    private static final String TAG = "ProfileFragment";
    private DatabaseHelper dbHelper;
    private TextView txtNama, txtBerat, txtTinggi, txtTgllahir, txtEmail, txtGender, txtTelepon;
    private String nama, berat,tinggi,tgllahir,email,gender,telepon;
    private ImageView imgProfile;
    private String Id;
    private SessionManager session;
    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        if (savedInstanceState != null){
            txtNama.setText(savedInstanceState.getString("nama"));
            txtBerat.setText(savedInstanceState.getString("berat"));
            txtTinggi.setText(savedInstanceState.getString("tinggi"));
            txtTgllahir.setText(savedInstanceState.getString("tgllahir"));
            txtGender.setText(savedInstanceState.getString("gender"));
            txtEmail.setText(savedInstanceState.getString("email"));
            txtTelepon.setText(savedInstanceState.getString("telepon"));
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("nama",txtNama.getText().toString());
        outState.putString("berat",txtBerat.getText().toString());
        outState.putString("tinggi",txtTinggi.getText().toString());
        outState.putString("tgllahir",txtTgllahir.getText().toString());
        outState.putString("gender",txtGender.getText().toString());
        outState.putString("email",txtEmail.getText().toString());
        outState.putString("telepon",txtTelepon.getText().toString());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        session = new SessionManager(getContext());

        txtNama = (TextView) view.findViewById(R.id.txtNama);
        txtBerat = (TextView) view.findViewById(R.id.txtBerat);
        txtTinggi = (TextView) view.findViewById(R.id.txtTinggi);
        txtTgllahir = (TextView) view.findViewById(R.id.txtTglLahir);
        txtGender = (TextView) view.findViewById(R.id.txtGender);
        txtEmail = (TextView) view.findViewById(R.id.txtEmail);
        txtTelepon = (TextView) view.findViewById(R.id.txtTelepon);
        try {
            Pengguna user;
            user = session.getUserDetails();

            txtNama.setText(user.getNama());
            txtBerat.setText(user.getBerat() + " kg");
            txtTinggi.setText(user.getTinggi() + " cm");
            txtTgllahir.setText(user.getTgllahir());
            if (user.getKelamin().equals("L")){
                txtGender.setText("Laki-laki");
            }else{
                txtGender.setText("Laki-laki");
            }
            txtEmail.setText(user.getEmail());
            txtTelepon.setText(user.getTelepon());
        }catch (Exception e){
            Log.e(TAG,e.getMessage());
        }
        return view;
    }
}
