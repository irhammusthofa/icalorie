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
 * Created by Irham on 12/26/2017.
 */

public class NutritionRAdapter extends ArrayAdapter<NutritionR> {

    public NutritionRAdapter(@NonNull Context context, @NonNull List<NutritionR> objects) {
        super(context, R.layout.item_list_kalori, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        final LayoutInflater layoutInflater = (LayoutInflater)
                getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View view = layoutInflater.inflate(R.layout.item_list_kalori,parent,false);
        TextView txtParentName = (TextView) view.findViewById(R.id.txtParentName);
        TextView txtParentBerat = (TextView) view.findViewById(R.id.txtParentBerat);
        TextView txtChildName = (TextView) view.findViewById(R.id.txtChildName);
        TextView txtChildBerat = (TextView) view.findViewById(R.id.txtChildBerat);


        final NutritionR nutrisi = getItem(position);
        final String parentBerat = nutrisi.getParentBerat();
        final String childBerat = nutrisi.getChildBerat();

        txtParentName.setText(nutrisi.getParentName());
        txtChildName.setText(nutrisi.getChildName());
        if (parentBerat.isEmpty()){
            txtParentBerat.setText("");
        }else{
            txtParentBerat.setText(String.format("%s gr",nutrisi.getParentBerat()));
        }
        if (txtChildName.toString().isEmpty()){
            txtChildBerat.setVisibility(View.GONE);
            txtChildName.setVisibility(View.GONE);
        }else{
            txtChildBerat.setVisibility(View.VISIBLE);
            txtChildName.setVisibility(View.VISIBLE);
        }
        if (childBerat.isEmpty()){
            txtChildBerat.setText("");
        }else{
            txtChildBerat.setText(String.format("%s gr",nutrisi.getChildBerat()));
        }
        return view;
    }
}
