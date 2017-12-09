package id.co.kamil.icalorie;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Irham on 12/7/2017.
 */

public class NutrisiAdapter extends ArrayAdapter<Nutrisi> {

    public NutrisiAdapter(@NonNull Context context, @NonNull List<Nutrisi> objects) {
        super(context, R.layout.item_list_db, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        final LayoutInflater layoutInflater = (LayoutInflater)
                getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View view = layoutInflater.inflate(R.layout.item_list_db,parent,false);
        TextView txtNama = (TextView) view.findViewById(R.id.txtNama);
        TextView txtBerat = (TextView) view.findViewById(R.id.txtBerat);
        TextView txtKalori = (TextView) view.findViewById(R.id.txtKalori);
        TextView txtProtein = (TextView) view.findViewById(R.id.txtProtein);
        TextView txtKarbohidrat = (TextView) view.findViewById(R.id.txtKarbohidrat);
        TextView txtVitA = (TextView) view.findViewById(R.id.txtVitA);
        TextView txtVitB = (TextView) view.findViewById(R.id.txtVitB);
        TextView txtVitC = (TextView) view.findViewById(R.id.txtVitC);

        final Nutrisi nutrisi = getItem(position);

        txtNama.setText(nutrisi.getNama());
        txtBerat.setText(String.format("%.2f gr",nutrisi.getBerat()));
        txtKalori.setText(String.format("%.2f KKal",nutrisi.getKalori()));
        txtKarbohidrat.setText(Double.valueOf(nutrisi.getKarbohidrat()).toString());
        txtProtein.setText(Double.valueOf(nutrisi.getProtein()).toString());
        txtVitA.setText(Double.valueOf(nutrisi.getVita()).toString());
        txtVitB.setText(Double.valueOf(nutrisi.getVitb()).toString());
        txtVitC.setText(Double.valueOf(nutrisi.getVitc()).toString());

        return view;
    }
}
