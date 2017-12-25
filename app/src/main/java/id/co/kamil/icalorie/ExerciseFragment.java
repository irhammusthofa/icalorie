package id.co.kamil.icalorie;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;


public class ExerciseFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private FloatingActionButton btnTambah;
    private List<Exercise> list = new ArrayList<>();
    private TextView txtInfo;
    private ListView listExercise;

    private DatabaseHelper dbHelper;

    public ExerciseFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dbHelper = new DatabaseHelper(getContext());
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_exercise, container, false);
        txtInfo = (TextView) view.findViewById(R.id.txtInfo);
        listExercise = (ListView) view.findViewById(R.id.listExercise);

        btnTambah = (FloatingActionButton) view.findViewById(R.id.btnTambah);
        btnTambah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(),ExerciseActivity.class));
            }
        });

        loadExercise();
        displayExercise();

        return view;
    }
    private void displayExercise(){
        final ExerciseAdapter exerciseAdapter = new ExerciseAdapter(getContext(),list);
        listExercise.setAdapter(exerciseAdapter);
    }
    private void loadExercise(){
        list.clear();
        txtInfo.setVisibility(View.GONE);
        listExercise.setVisibility(View.GONE);
        final SQLiteDatabase db = dbHelper.getWritableDatabase();
        final Cursor cursor = db.rawQuery(
                String.format("SELECT * FROM %s ORDER BY %s DESC",DatabaseContract.Exercise.TABLE_EXERCISE,DatabaseContract.Exercise._ID),null
        );
        if (cursor!=null){
            try {
                if(cursor.moveToFirst()){
                    listExercise.setVisibility(View.VISIBLE);
                    do {
                        final int type = cursor.getInt(cursor.getColumnIndex(DatabaseContract.Exercise.EXERCISE_COL_TYPE));
                        final long length = cursor.getLong(cursor.getColumnIndex(DatabaseContract.Exercise.EXERCISE_COL_LENGTH_TIME));
                        final long time = cursor.getLong(cursor.getColumnIndex(DatabaseContract.Exercise.EXERCISE_COL_START_DATE));

                        final Exercise exercise = new Exercise();
                        if (type == 1){
                            exercise.setAktivitas(getString(R.string.exercise_type_running));
                            exercise.setIcon(R.drawable.ic_directions_run_black_24dp);
                        }else if(type==2){
                            exercise.setAktivitas(getString(R.string.exercise_type_walking));
                            exercise.setIcon(R.drawable.ic_directions_walk_black_24dp);
                        }else{
                            exercise.setAktivitas(getString(R.string.exercise_type_bicycling));
                            exercise.setIcon(R.drawable.ic_directions_bike_black_24dp);
                        }
                        exercise.setW_length(length);
                        exercise.setW_awal(time);

                        list.add(exercise);
                    }while (cursor.moveToNext());
                }else{
                    Log.i(TAG,"Data Exercise tidak tersedia");
                    txtInfo.setVisibility(View.VISIBLE);
                }
            }finally {
                Log.i(TAG,"Cursor Close.");
                cursor.close();
            }
        }
    }

}
