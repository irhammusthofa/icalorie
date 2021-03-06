package id.co.kamil.icalorie;

import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import static android.content.ContentValues.TAG;

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
    private long currentDate;
    private static final int TIME_INTERVAL = 2000; // # milliseconds, desired time passed between two back presses.
    private long mBackPressed;

    private DatabaseHelper dbHelper;

    private ListView uiListNutrisi;
    private List<NutritionR> listNutrisi= new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise);
        getSupportActionBar().setTitle("Exercise Mode");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        dbHelper = new DatabaseHelper(this);

        handler = new Handler() ;
        btnMulai = (Button) findViewById(R.id.btnStart);
        btnStop = (Button) findViewById(R.id.btnStop);
        btnStop.setEnabled(false);
        btnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handler.removeCallbacks(runnable);
                berhentiSejenak();
                mCaloriesValueView.setText(String.format("%.2f",caloriesCalculator.Calculate(-1)));
                showAlert();

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

        btnMulai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (btnMulai.getText().equals("Start")){
                    Calendar calendar = Calendar.getInstance();
                    currentDate = calendar.getTimeInMillis();
                    Log.i(TAG, String.valueOf(currentDate));
                    StartTime = SystemClock.uptimeMillis();

                    handler.postDelayed(runnable, 0);
                    btnMulai.setText("Pause");
                    btnStop.setEnabled(true);
                    mulai();
                    if (spAktivitas.isEnabled()){
                        spAktivitas.setEnabled(false);
                        int pos = spAktivitas.getSelectedItemPosition() + 1;
                        TypeExercise = pos;
                        caloriesCalculator = new CaloriesCalculator(mPedometerSettings.getBodyWeight(), pos);
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

        mUtils.setSpeak(mSettings.getBoolean("speak", false));
        // Read from preferences if the service was running on the last onPause
        mIsRunning = mPedometerSettings.isServiceRunning();
        resetValues(true);
    }
    private void showAlert(){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        LayoutInflater layoutInflater = getLayoutInflater();
        View view = (View) layoutInflater.inflate(R.layout.list,null);
        alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                showResult();
            }
        });
        alertDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
                showResult();
            }
        });
        alertDialog.setView(view);
        uiListNutrisi = (ListView) view.findViewById(R.id.lv);

        searchRangeKalori(mCaloriesValue);
        displayRangeKalori();
        alertDialog.setTitle("Rekomendasi Nutrisi");
        alertDialog.show();

    }
    private void showResult(){
        Intent intent = new Intent(getApplicationContext(),ExerciseResultActivity.class);
        intent.putExtra("distance",mDistanceValue);
        intent.putExtra("calories",mCaloriesValue);
        intent.putExtra("steps",mStepValue);
        intent.putExtra("type",TypeExercise);
        intent.putExtra("lengthTime",lengthTime);
        intent.putExtra("startTime",currentDate);
        intent.addFlags(Intent.FLAG_ACTIVITY_FORWARD_RESULT);
        startActivity(intent);
        finish();
    }
    private void displayRangeKalori(){
        final NutritionRAdapter nutritionRAdapter = new NutritionRAdapter(this,listNutrisi);
        uiListNutrisi.setAdapter(nutritionRAdapter);
    }
    private void searchRangeKalori(float kalori){
        List<String> parentId = new ArrayList<String>(),
                parentName = new ArrayList<String>(),
                parentBerat = new ArrayList<String>();
        final SQLiteDatabase db = dbHelper.getWritableDatabase();
        final String SQLQuery = String.format("SELECT * FROM %s WHERE %s="+0+" AND %s<="+ kalori +" AND %s>=" + kalori,
                DatabaseContract.RangeKalori.TABLE_RANGE_KALORI,
                DatabaseContract.RangeKalori.RK_COL_ID_PARENT,
                DatabaseContract.RangeKalori.RK_COL_KALORI_AWAL,
                DatabaseContract.RangeKalori.RK_COL_KALORI_AKHIR
        );
        Log.i(TAG,SQLQuery);
        final Cursor cursor = db.rawQuery(SQLQuery,null);

        if (cursor != null){
            try{
                if (cursor.moveToFirst()){
                    do {
                        final String kaloriId = cursor.getString(cursor.getColumnIndex(DatabaseContract.RangeKalori._ID));
                        final String kaloriName = cursor.getString(cursor.getColumnIndex(DatabaseContract.RangeKalori.RK_COL_NAMA));
                        final String kaloriBerat = cursor.getString(cursor.getColumnIndex(DatabaseContract.RangeKalori.RK_COL_BERAT_SAJI));
                        parentName.add(kaloriName);
                        parentBerat.add(kaloriBerat);
                        parentId.add(kaloriId);
                    }while (cursor.moveToNext());
                }else{
                    Log.i(TAG,"Data Range Kalori tidak tersedia");
                }
            }finally {
                Log.i(TAG,"Cursor Close");
                cursor.close();
            }
        }
        Log.i(TAG,"Count RKalori ; " + parentId.size());
        if(parentId.size()>0){
            int i;

            for (i=0;i<parentId.size();i++){
                Log.i(TAG,"Parent Name : " + parentName.get(i));
                String childName = "",
                        childBerat = "";
                final Cursor c = db.rawQuery(
                        String.format("SELECT * FROM %s WHERE %s=?",
                                DatabaseContract.RangeKalori.TABLE_RANGE_KALORI,
                                DatabaseContract.RangeKalori.RK_COL_ID_PARENT),
                        new String[]{String.valueOf(parentId.get(i))}
                );
                if (c != null){
                    try{
                        if (c.moveToFirst()){
                            do {
                                final String kaloriName = c.getString(c.getColumnIndex(DatabaseContract.RangeKalori.RK_COL_NAMA));
                                final String kaloriBerat = c.getString(c.getColumnIndex(DatabaseContract.RangeKalori.RK_COL_BERAT_SAJI));
                                if (!childName.isEmpty()){
                                    childName +="\n";
                                    childBerat +="\n";
                                }
                                childName += kaloriName;
                                childBerat += kaloriBerat;
                            }while (c.moveToNext());
                            Log.i(TAG,"Child Name : " + childName);
                        }else{
                            Log.i(TAG,"Data Range Kalori tidak tersedia");
                        }
                    }finally {
                        Log.i(TAG,"Cursor Close");
                        c.close();
                    }
                }
                final NutritionR nutritionR = new NutritionR();
                nutritionR.setParentName(parentName.get(i));
                nutritionR.setParentBerat(parentBerat.get(i));
                nutritionR.setChildName(childName);
                nutritionR.setChildBerat(childBerat);
                listNutrisi.add(nutritionR);
            }
        }
    }
    @Override
    public void onBackPressed()
    {
        if (mBackPressed + TIME_INTERVAL > System.currentTimeMillis())
        {
            super.onBackPressed();
            return;
        }
        else { Toast.makeText(getBaseContext(), "Tap back button in order to exit", Toast.LENGTH_SHORT).show(); }

        mBackPressed = System.currentTimeMillis();
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

            lengthTime = MillisecondTime;

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
