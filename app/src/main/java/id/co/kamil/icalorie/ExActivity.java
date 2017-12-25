package id.co.kamil.icalorie;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class ExActivity extends AppCompatActivity implements SensorEventListener {
    private static final String TAG = "ExActivity";

    private float   mLimit = 50;
    private float   mLastValues[] = new float[3*2];
    private float   mScale[] = new float[2];
    private float   mYOffset;

    private float   mLastDirections[] = new float[3*2];
    private float   mLastExtremes[][] = { new float[3*2], new float[3*2] };
    private float   mLastDiff[] = new float[3*2];
    private int     mLastMatch = -1;

    private long    mSteps = 0 ;

    private SensorManager sensorManager;
    private Sensor sensor;

    private TextView txtStep;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ex);

        txtStep = (TextView) findViewById(R.id.txtSteps);
        int h = 480; // TODO: remove this constant
        mYOffset = h * 0.5f;
        mScale[0] = - (h * 0.5f * (1.0f / (SensorManager.STANDARD_GRAVITY * 2)));
        mScale[1] = - (h * 0.5f * (1.0f / (SensorManager.MAGNETIC_FIELD_EARTH_MAX)));

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        Log.i(TAG,"mYOffset : " + mYOffset);
        Log.i(TAG,"mScale[0] : " + mScale[0]);
        Log.i(TAG,"mScale[1] : " + mScale[1]);
    }

    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(this,sensor,sensorManager.SENSOR_DELAY_UI);
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        Sensor sensor = event.sensor;
        synchronized (this) {
            if (sensor.getType() == Sensor.TYPE_ORIENTATION) {
            }
            else {
                int j = (sensor.getType() == Sensor.TYPE_ACCELEROMETER) ? 1 : 0;
                if (j == 1) {
                    // TODO : Deteksi Gerak
                    float vSum = 0;
                    for (int i=0 ; i<3 ; i++) {
                        final float v = mYOffset + event.values[i] * mScale[j];
                        vSum += v;
                    }
                    Log.i(TAG,"vSum : " + vSum);
                    int k = 0;
                    float v = vSum / 3;
                    Log.i(TAG,"v " + " : " + v );
                    Log.i(TAG,"LastValues " + k + " : " + mLastValues[k] );
                    float direction = (v > mLastValues[k] ? 1 : (v < mLastValues[k] ? -1 : 0));
                    Log.i(TAG,"direction : " + direction + " | " + -mLastDirections[k]);

                    if (direction == - mLastDirections[k]) {
                        // Direction changed
                        int extType = (direction > 0 ? 0 : 1); // minumum or maximum?
                        mLastExtremes[extType][k] = mLastValues[k];
                        float diff = Math.abs(mLastExtremes[extType][k] - mLastExtremes[1 - extType][k]);
                        Log.i(TAG,"LastExtremes[" + extType + "]["+k+"]" + mLastExtremes[extType][k]);
                        Log.i(TAG,"LastExtremes[" + (1- extType) + "]["+k+"]" + mLastExtremes[1 - extType][k]);

                        if (diff > mLimit) {

                            boolean isAlmostAsLargeAsPrevious = diff > (mLastDiff[k]*2/3);
                            boolean isPreviousLargeEnough = mLastDiff[k] > (diff/3);
                            boolean isNotContra = (mLastMatch != 1 - extType);

                            if (isAlmostAsLargeAsPrevious && isPreviousLargeEnough && isNotContra) {
                                Log.i(TAG, "step");
                                onStep();
                                mLastMatch = extType;
                            }
                            else {
                                mLastMatch = -1;
                            }
                        }
                        mLastDiff[k] = diff;
                    }
                    mLastDirections[k] = direction;
                    mLastValues[k] = v;
                }
            }
        }
    }
    private void onStep(){
        this.mSteps++;
        txtStep.setText(String.valueOf(mSteps));
    }
    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}
