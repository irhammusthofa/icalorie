package id.co.kamil.icalorie;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import java.text.DateFormat;
import java.util.Date;

public class ExerciseActivity extends AppCompatActivity {

    private static final String TAG = "ExerciseActivity";
    private SharedPreferences mSettings;
    private PedometerSettings mPedometerSettings;
    public CaloriesCalculator caloriesCalculator;
    private Utils mUtils;

    private TextView mStepValueView;
    private TextView mPaceValueView;
    private TextView mDistanceValueView;
    private TextView mSpeedValueView;
    private TextView mCaloriesValueView;
    TextView mDesiredPaceView;
    private int mStepValue;
    private int mPaceValue;
    private float mDistanceValue;
    private float mSpeedValue;
    private float mCaloriesValue;
    private boolean mIsMetric;
    private boolean mQuitting = false; // Set when user selected Quit from menu, can be used by onPause, onStop, onDestroy
    /**
     * True, when service is running.
     */
    private boolean mIsRunning;
    private Button btnMulai;
    private Spinner spAktivitas;
    private String[] aktivitas ={"Lari","Jalan Kaki","Bersepeda"};
    private TextView txtAktivitas;
    private ImageView iconExercise;
    private TextView txtTime;
    private int TypeExercise;
    private boolean SpeedChange = false;
    private boolean CaloriesChange = false;

    long MillisecondTime, StartTime, TimeBuff, UpdateTime = 0L ;
    Handler handler;
    int Seconds, Minutes, MilliSeconds,Hours ;
    int secondsLimit = 30,caloriesLimit = 60;
    long lengthTime;
    private Button btnStop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise);
        getSupportActionBar().setTitle("Exercise Mode");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        handler = new Handler() ;
        btnMulai = (Button) findViewById(R.id.btnStart);
        btnStop = (Button) findViewById(R.id.btnStop);
        btnStop.setEnabled(false);
        btnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimeBuff += MillisecondTime;
                handler.removeCallbacks(runnable);
                berhentiSejenak();
                mCaloriesValueView.setText(String.format("%.2f",caloriesCalculator.Calculate(-1)));
                Intent intent = new Intent(getApplicationContext(),ExerciseResultActivity.class);
                intent.putExtra("distance",mDistanceValue);
                intent.putExtra("calories",mCaloriesValue);
                intent.putExtra("steps",mStepValue);
                intent.putExtra("type",TypeExercise);
                intent.putExtra("lengthTime",lengthTime);
                intent.putExtra("startTime",StartTime);
                startActivity(intent);
                finish();
            }
        });
        btnMulai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (btnMulai.getText().equals("Start")){
                    StartTime = SystemClock.uptimeMillis();
                    handler.postDelayed(runnable, 0);
                    btnMulai.setText("Pause");
                    btnStop.setEnabled(true);
                    mulai();
                    if (spAktivitas.isEnabled()){
                        spAktivitas.setEnabled(false);
                        int pos = spAktivitas.getSelectedItemPosition() + 1;
                        TypeExercise = pos;
                        caloriesCalculator = new CaloriesCalculator(46,pos);
                    }
                }else{
                    TimeBuff += MillisecondTime;
                    handler.removeCallbacks(runnable);
                    berhentiSejenak();
                    mCaloriesValueView.setText(String.format("%.2f",caloriesCalculator.Calculate(-1)));
                    btnMulai.setText("Start");
                    btnStop.setEnabled(true);
                }
            }
        });
        spAktivitas = (Spinner) findViewById(R.id.spAktivitas);
        mStepValueView     = (TextView) findViewById(R.id.step_value);
        mPaceValueView     = (TextView) findViewById(R.id.pace_value);
        mDistanceValueView = (TextView) findViewById(R.id.distance_value);
        mSpeedValueView    = (TextView) findViewById(R.id.speed_value);
        mCaloriesValueView = (TextView) findViewById(R.id.calories_value);
        mDesiredPaceView   = (TextView) findViewById(R.id.desired_pace_value);
        txtAktivitas       = (TextView) findViewById(R.id.txtExercise);
        iconExercise       = (ImageView) findViewById(R.id.iconExercise);
        txtTime            = (TextView) findViewById(R.id.txtTime);

        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,aktivitas);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spAktivitas.setAdapter(adapter);

        spAktivitas.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i==0){
                    txtAktivitas.setText("Lari");
                    iconExercise.setImageResource(R.drawable.ic_directions_run_white_24dp);
                }else if(i==1){
                    txtAktivitas.setText("Jalan Kaki");
                    iconExercise.setImageResource(R.drawable.ic_directions_walk_white_24dp);
                }else{
                    txtAktivitas.setText("Bersepeda");
                    iconExercise.setImageResource(R.drawable.ic_directions_bike_white_24dp);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        mStepValue = 0;
        mPaceValue = 0;


        mUtils = Utils.getInstance();

        mSettings = PreferenceManager.getDefaultSharedPreferences(this);
        mPedometerSettings = new PedometerSettings(mSettings);


        mUtils.setSpeak(mSettings.getBoolean("speak", false));
        // Read from preferences if the service was running on the last onPause
        mIsRunning = mPedometerSettings.isServiceRunning();
        resetValues(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);

    }
    public Runnable runnable = new Runnable() {

        public void run() {
            boolean ChangeDiam = false;
            MillisecondTime = SystemClock.uptimeMillis() - StartTime;
            UpdateTime = TimeBuff + MillisecondTime;
            Seconds = (int) (UpdateTime / 1000);
            Hours = Minutes / 60;
            Minutes = Seconds / 60;
            Seconds = Seconds % 60;
            MilliSeconds = (int) (UpdateTime % 1000);

            lengthTime = UpdateTime;

            txtTime.setText("" + String.format("%02d",Hours ) + ":"
                    + String.format("%02d",Minutes ) + ":"
                    + String.format("%02d", Seconds));
            handler.postDelayed(this, 0);
            if (SpeedChange==true){
                SpeedChange = false;
                ChangeDiam = false;
                secondsLimit = 30;
            }else{
                if (secondsLimit == 0){
                    secondsLimit = 30;
                    mSpeedValue = 0;
                    mSpeedValueView.setText(
                            ("" + (mSpeedValue + 0.01f)).substring(0, 4)
                    );
                    SpeedChange = true;
                    CaloriesChange = true;
                    mCaloriesValue = caloriesCalculator.Calculate(Float.valueOf(mSpeedValue));
                    mCaloriesValueView.setText(String.format("%.2f",mCaloriesValue));
                    ChangeDiam = true;
                }else{
                    ChangeDiam = false;
                    secondsLimit--;
                }
            }
            if (CaloriesChange == false ){
                if (caloriesLimit==0 && ChangeDiam == false){
                    caloriesLimit = 60;
                    mCaloriesValue = caloriesCalculator.Calculate(Float.valueOf(-1));
                    mCaloriesValueView.setText(String.format("%.2f",mCaloriesValue));
                    CaloriesChange=true;
                }
                caloriesLimit--;
            }else{
                caloriesLimit = 60;
                CaloriesChange = false;
            }
        }

    };
    private void mulai(){
        // Start the service if this is considered to be an application start (last onPause was long ago)

        if (!mIsRunning && mPedometerSettings.isNewStart()) {
            startStepService();
            bindStepService();
        } else if (mIsRunning) {
            bindStepService();
        }

        mPedometerSettings.clearServiceRunning();

        mIsMetric = mPedometerSettings.isMetric();
        ((TextView) findViewById(R.id.distance_units)).setText(getString(
                mIsMetric
                        ? R.string.kilometers
                        : R.string.miles
        ));
        ((TextView) findViewById(R.id.speed_units)).setText(getString(
                mIsMetric
                        ? R.string.kilometers_per_hour
                        : R.string.miles_per_hour
        ));


    }

    private void berhentiSejenak(){
        if (mIsRunning) {
            unbindStepService();
        }
        if (mQuitting) {
            mPedometerSettings.saveServiceRunningWithNullTimestamp(mIsRunning);
        } else {
            mPedometerSettings.saveServiceRunningWithTimestamp(mIsRunning);
        }

    }

    private StepService mService;

    private ServiceConnection mConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName className, IBinder service) {
            mService = ((StepService.StepBinder)service).getService();

            mService.registerCallback(mCallback);
            mService.reloadSettings();

        }

        public void onServiceDisconnected(ComponentName className) {
            mService = null;
        }
    };


    private void startStepService() {
        if (! mIsRunning) {
            Log.i(TAG, "[SERVICE] Start");
            mIsRunning = true;
            startService(new Intent(this,
                    StepService.class));
        }
    }

    private void bindStepService() {
        Log.i(TAG, "[SERVICE] Bind");
        bindService(new Intent(this,
                StepService.class), mConnection, Context.BIND_AUTO_CREATE + Context.BIND_DEBUG_UNBIND);
    }

    private void unbindStepService() {
        Log.i(TAG, "[SERVICE] Unbind");
        unbindService(mConnection);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopStepService();
    }

    private void stopStepService() {
        Log.i(TAG, "[SERVICE] Stop");
        if (mService != null) {
            Log.i(TAG, "[SERVICE] stopService");
            stopService(new Intent(this,
                    StepService.class));
        }
        mIsRunning = false;
    }

    private void resetValues(boolean updateDisplay) {
        if (mService != null && mIsRunning) {
            mService.resetValues();
        } else {
            mStepValueView.setText("0");
            mPaceValueView.setText("0");
            mDistanceValueView.setText("0");
            mSpeedValueView.setText("0");
            mCaloriesValueView.setText("0");
            SharedPreferences state = getSharedPreferences("state", 0);
            SharedPreferences.Editor stateEditor = state.edit();
            if (updateDisplay) {
                stateEditor.putInt("steps", 0);
                stateEditor.putInt("pace", 0);
                stateEditor.putFloat("distance", 0);
                stateEditor.putFloat("speed", 0);
                stateEditor.putFloat("calories", 0);
                stateEditor.commit();
            }
        }
    }

    // TODO: unite all into 1 type of message
    private StepService.ICallback mCallback = new StepService.ICallback() {
        public void stepsChanged(int value) {
            mHandler.sendMessage(mHandler.obtainMessage(STEPS_MSG, value, 0));
        }
        public void paceChanged(int value) {
            mHandler.sendMessage(mHandler.obtainMessage(PACE_MSG, value, 0));
        }
        public void distanceChanged(float value) {
            mHandler.sendMessage(mHandler.obtainMessage(DISTANCE_MSG, (int)(value*1000), 0));
        }
        public void speedChanged(float value) {
            mHandler.sendMessage(mHandler.obtainMessage(SPEED_MSG, (int)(value*1000), 0));
        }
    };

    private static final int STEPS_MSG = 1;
    private static final int PACE_MSG = 2;
    private static final int DISTANCE_MSG = 3;
    private static final int SPEED_MSG = 4;
    private static final int CALORIES_MSG = 5;

    private Handler mHandler = new Handler() {
        @Override public void handleMessage(Message msg) {
            switch (msg.what) {
                case STEPS_MSG:
                    mStepValue = (int)msg.arg1;
                    mStepValueView.setText("" + mStepValue);
                    break;
                case PACE_MSG:
                    mPaceValue = msg.arg1;
                    if (mPaceValue <= 0) {
                        mPaceValueView.setText("0");
                    }
                    else {
                        mPaceValueView.setText("" + (int)mPaceValue);
                    }
                    break;
                case DISTANCE_MSG:
                    mDistanceValue = ((int)msg.arg1)/1000f;
                    if (mDistanceValue <= 0) {
                        mDistanceValueView.setText("0");
                    }
                    else {
                        mDistanceValueView.setText(
                                ("" + (mDistanceValue + 0.000001f)).substring(0, 5)
                        );
                    }
                    break;
                case SPEED_MSG:
                    mSpeedValue = ((int)msg.arg1)/1000f;
                    if (mSpeedValue <= 0) {
                        mSpeedValueView.setText("0");
                    }else{
                        mSpeedValueView.setText(
                                ("" + (mSpeedValue + 0.000001f)).substring(0, 4)
                        );
                        SpeedChange = true;

                        float tmpCalories = caloriesCalculator.Calculate(Float.valueOf(mSpeedValue + 0.01f));
                        if (mCaloriesValue != tmpCalories){
                            mCaloriesValue = tmpCalories;
                            CaloriesChange = true;
                        }else{
                            CaloriesChange = false;
                        }
                        mCaloriesValueView.setText(String.format("%.2f",mCaloriesValue));
                    }
                    break;
                default:
                    super.handleMessage(msg);
            }
        }

    };
}
