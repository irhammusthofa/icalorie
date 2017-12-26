package id.co.kamil.icalorie;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ExerciseResultActivity extends AppCompatActivity {
    private static final String TAG = "ExerciseResultActivity";
    private SharedPreferences mSettings;
    private PedometerSettings mPedometerSettings;
    private boolean mIsMetric;

    private TextView typeExercise,lengthTime,startDate,startTime,mDistance,mCalories,mSteps;
    private ImageView iconExercise;
    private int exercise,steps;
    private long time,date;
    private float calories,distance;
    private WaktuTerbilang terbilang = new WaktuTerbilang();
    private Button btnSimpan;

    private DatabaseHelper dbHelper;
    private int idExercise;
    private int typeForm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise_result);
        getSupportActionBar().setTitle("Result Exercise");

        mSettings = PreferenceManager.getDefaultSharedPreferences(this);
        mPedometerSettings = new PedometerSettings(mSettings);
        dbHelper  = new DatabaseHelper(this);

        typeExercise = (TextView) findViewById(R.id.txtExercise);
        iconExercise = (ImageView) findViewById(R.id.iconExercise);
        lengthTime = (TextView) findViewById(R.id.txtWaktu);
        startDate = (TextView) findViewById(R.id.txtTgl);
        startTime = (TextView) findViewById(R.id.txtJam);
        mDistance = (TextView) findViewById(R.id.txtDistance);
        mSteps = (TextView) findViewById(R.id.mSteps);
        mCalories = (TextView) findViewById(R.id.txtCalories);
        btnSimpan = (Button) findViewById(R.id.btnSimpan);
        typeForm = getIntent().getIntExtra("typeForm",0);
        if(typeForm==0){
            btnSimpan.setVisibility(View.VISIBLE);
        }else{
            btnSimpan.setVisibility(View.GONE);
        }
        idExercise = getIntent().getIntExtra("id",0);
        exercise = getIntent().getIntExtra("type",0);
        calories = getIntent().getFloatExtra("calories",0);
        distance = getIntent().getFloatExtra("distance",0);
        steps = getIntent().getIntExtra("steps",0);
        time = getIntent().getLongExtra("lengthTime",0);
        date = getIntent().getLongExtra("startTime",0);
        if (exercise==1){
            iconExercise.setImageResource(R.drawable.ic_directions_run_black_24dp);
        }else if(exercise==2){
            iconExercise.setImageResource(R.drawable.ic_directions_walk_black_24dp);
        }else{
            iconExercise.setImageResource(R.drawable.ic_directions_bike_black_24dp);
        }
        btnSimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final SQLiteDatabase db = dbHelper.getWritableDatabase();
                final ContentValues values = new ContentValues();
                values.put(DatabaseContract.Exercise.EXERCISE_COL_TYPE,exercise);
                values.put(DatabaseContract.Exercise.EXERCISE_COL_CALORIES,calories);
                values.put(DatabaseContract.Exercise.EXERCISE_COL_DISTANCE,distance);
                values.put(DatabaseContract.Exercise.EXERCISE_COL_LENGTH_TIME,time);
                values.put(DatabaseContract.Exercise.EXERCISE_COL_START_DATE,date);
                values.put(DatabaseContract.Exercise.EXERCISE_COL_STEP,steps);

                db.insert(DatabaseContract.Exercise.TABLE_EXERCISE,null,values);
                Log.i(TAG,"Inserting data " + values);
                Toast.makeText(getApplicationContext(), "Data Exercise berhasil disimpan", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent();
                intent.putExtra("stateSave", 1);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
        mPedometerSettings.clearServiceRunning();

        mIsMetric = mPedometerSettings.isMetric();

        mCalories.setText(String.format("%.2f",calories) + " Kcal");
        mDistance.setText(String.format("%.2f",distance) + " " + getString(
                mIsMetric
                        ? R.string.kilometers
                        : R.string.miles
        ));
        mSteps.setText(String.valueOf(steps));
        typeExercise.setText((exercise == 1) ? getString(R.string.exercise_type_running) :
                (exercise == 2) ? getString(R.string.exercise_type_walking) :
                        getString(R.string.exercise_type_bicycling));

        DateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy");
        DateFormat timeFormat = new SimpleDateFormat("hh:mm:ss");

        startDate.setText(dateFormat.format(date));
        startTime.setText(timeFormat.format(date));
        lengthTime.setText(terbilang.convert(time));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu,menu);
        menu.findItem(R.id.menu_insert).setVisible(false);
        menu.findItem(R.id.menu_delete).setVisible(false);
        menu.findItem(R.id.menu_edit).setVisible(false);
        menu.findItem(R.id.menu_logout).setVisible(false);
        if (typeForm==1){
            menu.findItem(R.id.menu_delete).setVisible(true);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_delete:
                AlertDialog.Builder alBuilder = new AlertDialog.Builder(this);
                alBuilder.setTitle("Hapus");
                alBuilder.setMessage("Apakah anda yakin akan menghapus data berikut?");
                alBuilder.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.i(TAG,"Sedang menghapus data exericse");
                        // TODO: Hapus data
                        final SQLiteDatabase db = dbHelper.getWritableDatabase();
                        db.delete(DatabaseContract.Exercise.TABLE_EXERCISE,String.format("%s = ?",DatabaseContract.Exercise._ID),new String[]{String.valueOf(idExercise)});
                        Intent intent = new Intent();
                        intent.putExtra("stateSave", 1);
                        setResult(RESULT_OK, intent);
                        finish();
                    }
                });
                alBuilder.setNegativeButton("T*idak", new DialogInterface.OnClickListener() {
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
