package id.co.kamil.icalorie;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by Irham on 12/17/2017.
 */

public class ExerciseAdapter extends ArrayAdapter<Exercise> {

    private Date date;
    private WaktuTerbilang terbilang = new WaktuTerbilang();

    public ExerciseAdapter(@NonNull Context context, @NonNull List<Exercise> objects) {
        super(context, R.layout.item_list_exercise, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater)
                getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.item_list_exercise,parent,false);
        TextView txtExercise = (TextView) view.findViewById(R.id.txtExercise);
        TextView txtWaktu = (TextView) view.findViewById(R.id.txtWaktu);
        TextView txtWaktuMulai = (TextView) view.findViewById(R.id.txtWaktuMulai);
        TextView txtTgl = (TextView) view.findViewById(R.id.txtTgl);
        ImageView iconExercise = (ImageView) view.findViewById(R.id.iconExercise);

        final Exercise exercise = getItem(position);
        date = new Date(exercise.getW_awal());
        SimpleDateFormat df1 = new SimpleDateFormat("hh:mm:ss");
        SimpleDateFormat df2 = new SimpleDateFormat("dd MMM yyyy");

        final String tgl = df2.format(date);
        final String w_mulai = df1.format(date);
        final String waktu = terbilang.convert(exercise.getW_length());

        txtExercise.setText(exercise.getAktivitas());
        iconExercise.setImageResource(exercise.getIcon());
        txtTgl.setText(tgl);
        txtWaktuMulai.setText(w_mulai);
        txtWaktu.setText(waktu);

        return view;
    }
}
