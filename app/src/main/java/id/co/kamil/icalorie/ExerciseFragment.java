package id.co.kamil.icalorie;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import static android.content.ContentValues.TAG;


public class ExerciseFragment extends Fragment implements SensorEventListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private SensorManager sensorManager;
    private Sensor stepSensor;
    private long steps = 0;
    private TextView txtLangkah,txtJarak;

    public ExerciseFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sensorManager = (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);
        stepSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR);
        if (stepSensor!=null){
            Log.i(TAG,"Berhasil, Sensor ditemukan.");
        }else{
            Log.i(TAG,"Gagal, Sensor tidak ditemukan");
        }
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        sensorManager.registerListener(this,stepSensor,sensorManager.SENSOR_DELAY_FASTEST);

    }

    @Override
    public void onStop() {
        super.onStop();
        sensorManager.unregisterListener(this,stepSensor);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_exercise, container, false);
        txtLangkah = (TextView) view.findViewById(R.id.txtLangkah);
        txtJarak = (TextView) view.findViewById(R.id.txtJarak);

        return view;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        Sensor sensor = event.sensor;
        float[] values = event.values;
        int value = -1;

        if (values.length > 0) {
            value = (int) values[0];
        }
        Log.i(TAG,"Values :"+value);
        if (sensor.getType() == Sensor.TYPE_STEP_DETECTOR) {
            steps++;
        }
        Log.i(TAG,"Steps :"+steps);

        txtLangkah.setText(String.valueOf(steps).toString());
        txtJarak.setText(String.format("%.2f km",getDistanceRun(steps)));
    }
    public float getDistanceRun(long steps){
        float distance = (float)(steps*78)/(float)100000;
        return distance;
    }
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
