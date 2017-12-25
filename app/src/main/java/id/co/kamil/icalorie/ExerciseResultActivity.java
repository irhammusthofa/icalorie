package id.co.kamil.icalorie;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise_result);

        mSettings = PreferenceManager.getDefaultSharedPreferences(this);
        mPedometerSettings = new PedometerSettings(mSettings);


        typeExercise = findViewById(R.id.txtExercise);
        iconExercise = findViewById(R.id.iconExercise);
        lengthTime = findViewById(R.id.txtWaktu);
        startDate = findViewById(R.id.txtTgl);
        startTime = findViewById(R.id.txtJam);
        mDistance = findViewById(R.id.txtDistance);
        mSteps = findViewById(R.id.txtStep);
        mCalories = findViewById(R.id.txtCalories);

        exercise = getIntent().getIntExtra("type",0);
        calories = getIntent().getFloatExtra("calories",0);
        distance = getIntent().getFloatExtra("distance",0);
        steps = getIntent().getIntExtra("steps",0);
        time = getIntent().getLongExtra("lengthTime",0);
        date = getIntent().getLongExtra("startTime",0);

        mPedometerSettings.clearServiceRunning();

        mIsMetric = mPedometerSettings.isMetric();

        mCalories.setText(calories + " Kcal");
        mDistance.setText(distance + " " + getString(
                mIsMetric
                        ? R.string.kilometers_per_hour
                        : R.string.miles_per_hour
        ));
        mSteps.setText(steps);
        typeExercise.setText((exercise == 1) ? getString(R.string.exercise_type_running) :
                (exercise == 2) ? getString(R.string.exercise_type_walking) :
                        getString(R.string.exercise_type_bicycling));

        DateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy");
        DateFormat timeFormat = new SimpleDateFormat("hh:mm:ss");

        startDate.setText(dateFormat.format(date));
        startTime.setText(timeFormat.format(date));
        lengthTime.setText(terbilang.convert(time));
    }

}
