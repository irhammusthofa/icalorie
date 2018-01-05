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
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;
import static android.content.ContentValues.TAG;


public class ExerciseFragment extends Fragment {
    private static final String TAG = "ExerciseFragment";

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
        listExercise.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                final String aktivitas = list.get(i).getAktivitas();

                Intent intent = new Intent(getContext(),ExerciseResultActivity.class);
                intent.putExtra("id",list.get(i).getId());
                intent.putExtra("calories",list.get(i).getKalori());
                intent.putExtra("type",(aktivitas == getString(R.string.exercise_type_walking)) ? 2
                        : (aktivitas == getString(R.string.exercise_type_running)) ? 1 : 3);
                intent.putExtra("distance",list.get(i).getJarak());
                intent.putExtra("steps",list.get(i).getLangkah());
                intent.putExtra("lengthTime",list.get(i).getW_length());
                intent.putExtra("startTime",list.get(i).getW_awal());
                intent.putExtra("typeForm",1);
                startActivityForResult(intent,2);
            }
        });
        btnTambah = (FloatingActionButton) view.findViewById(R.id.btnTambah);
        btnTambah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(getContext(),ExerciseActivity.class),2);
            }
        });

        loadExercise();
        displayExercise();

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 2) {
            if(resultCode == RESULT_OK) {
                int stateSave = data.getIntExtra("stateSave",0);
                Log.i(TAG,"Result Index : " + stateSave);
                if (stateSave==1){
                    loadExercise();
                    displayExercise();
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    public void displayExercise(){
        final ExerciseAdapter exerciseAdapter = new ExerciseAdapter(getContext(),list);
        listExercise.setAdapter(exerciseAdapter);
    }
    public void loadExercise(){
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
                        final int id = cursor.getInt(cursor.getColumnIndex(DatabaseContract.Exercise._ID));
                        final int type = cursor.getInt(cursor.getColumnIndex(DatabaseContract.Exercise.EXERCISE_COL_TYPE));
                        final float calories = cursor.getFloat(cursor.getColumnIndex(DatabaseContract.Exercise.EXERCISE_COL_CALORIES));
                        final int steps = cursor.getInt(cursor.getColumnIndex(DatabaseContract.Exercise.EXERCISE_COL_STEP));
                        final float distance = cursor.getFloat(cursor.getColumnIndex(DatabaseContract.Exercise.EXERCISE_COL_DISTANCE));
                        final long length = cursor.getLong(cursor.getColumnIndex(DatabaseContract.Exercise.EXERCISE_COL_LENGTH_TIME));
                        final long time = cursor.getLong(cursor.getColumnIndex(DatabaseContract.Exercise.EXERCISE_COL_START_DATE));

                        final Exercise exercise = new Exercise();
                        exercise.setId(id);
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
                        exercise.setKalori(calories);
                        exercise.setLangkah(steps);
                        exercise.setJarak(distance);
                        exercise.setW_length(length);
                        exercise.setW_awal(time);
                        Log.i(TAG,"Load data : " + exercise.toString());
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
