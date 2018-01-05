package id.co.kamil.icalorie;

import android.os.SystemClock;
import android.util.Log;

/**
 * Created by Irham on 12/22/2017.
 */

public class CaloriesCalculator {
    private static final String TAG = "CaloriesCalculator";
    private static final float[] METS_WALKING = {(float) 2.5,3, (float) 3.5,4,4, (float) 4.5};
    private static final float[] METS_WALKING_SPEED = {2, (float) 2.5,3, (float) 3.5,4, (float) 4.5};
    private static final float[] METS_BICYCLING = {4,6,6,8,8,10,10,12,12,16};
    private static final float[] METS_BICYCLING_SPEED = {9,10, (float) 11.9,12, (float) 13.9,14, (float) 15.9,16,19,20};
    private static final float[] METS_RUNNING = {8,9,10,11, (float) 11.5,(float)12.5,(float)13.5,14,15,16,18};
    private static final float[] METS_RUNNING_SPEED = {5,(float)5.2,6,(float)6.7,7,(float)7.5,8,(float)8.6,9,10,(float)10.9};

    private float Mets = 0;

    long MillisecondTime, StartTime = 0, TimeBuff, UpdateTime = 0L ;
    float Seconds,  Hours ;
    float calories = 0;
    float Weight;
    float Height;
    int TypeActivity;

    public  CaloriesCalculator(float Weight, float Height, int TypeActivity){
        this.Weight = Weight;
        this.Height = Height;
        this.TypeActivity = TypeActivity;
    }

    public float Calculate(float Speed){
        float mTable = 0;
        if (Speed > 0){
            mTable = MetsTable(Speed);
        }
        if (mTable <= 0){
            mTable = 1;
        }
        if (StartTime == 0 && Speed > 0) {
            calories = 0;
            StartTime = SystemClock.uptimeMillis();
            Mets = mTable;
            Log.i(TAG,"Start Time. Mets : " + Mets + ". Calories " + calories);

        }else if(Speed < 0) {
            long Now = SystemClock.uptimeMillis();
            MillisecondTime = Now - StartTime;
            StartTime = Now;
            UpdateTime = TimeBuff + MillisecondTime;
            Seconds = (float) (UpdateTime / 1000);

            double DurationInHour = Seconds / 360;

            calories += (Mets/24) * Weight * Height * DurationInHour;
            //Mets = MetsTable(speed);
            Log.i(TAG, "Finish/Refresh  : " + Mets
                    + ". Calories " + calories
                    + ". Duration In Hour : " + DurationInHour
            );
        }else if (Speed == 0){
            long Now = SystemClock.uptimeMillis();
            MillisecondTime = Now - StartTime;
            StartTime = Now;
            UpdateTime = TimeBuff + MillisecondTime;
            Seconds = (float) (UpdateTime / 1000);

            double DurationInHour = Seconds / 360;

            calories += (Mets/24) * Weight * Height * DurationInHour;
            Mets = mTable;
            Log.i(TAG,"[Diam] Mets Change : " + Mets
                    + ". Calories " + calories
                    + ". Duration In Hour : " + DurationInHour
            );
        }else if (Mets != mTable){
            long Now = SystemClock.uptimeMillis();
            MillisecondTime = Now - StartTime;
            StartTime = Now;
            UpdateTime = TimeBuff + MillisecondTime;
            Seconds = (float) (UpdateTime / 1000);

            double DurationInHour = Seconds / 360;

            calories += (Mets/24) * Weight * Height * DurationInHour;
            if (Mets == 0) Mets = 1;
            Mets = mTable;
            Log.i(TAG,"Mets Change : " + Mets
                    + ". Calories " + calories
                    + ". Duration In Hour : " + DurationInHour
            );
        }

        return calories;
    }

    private float MetsTable(float Speed){
        int i;
        float mets = 0;
        float MetsSpeed,Mets,MetsSpeedBefore = 0;
        int lengthArray;
        if (TypeActivity == 1) {
            lengthArray = METS_RUNNING.length;
        }else if (TypeActivity==2){
            lengthArray = METS_WALKING.length;
        }else{
            lengthArray = METS_BICYCLING.length;
        }
        for(i=0;i<lengthArray;i++){
            if (TypeActivity==1){ // Mets Running
                Mets = METS_RUNNING[i];
                MetsSpeed = METS_RUNNING_SPEED[i];
                if (i>0){
                    MetsSpeedBefore = METS_RUNNING_SPEED[i-1];
                }
            }else if(TypeActivity==2){ // Mets Walking
                Mets = METS_WALKING[i];
                MetsSpeed = METS_WALKING_SPEED[i];
                if (i>0) {
                    MetsSpeedBefore = METS_WALKING_SPEED[i - 1];
                }
            }else{ // Mets Bicycling
                Mets = METS_BICYCLING[i];
                MetsSpeed = METS_BICYCLING_SPEED[i];
                if (i>0) {
                    MetsSpeedBefore = METS_BICYCLING_SPEED[i - 1];
                }
            }
            if (i == 0) {
                if (Speed > 0 && Speed<=MetsSpeed){
                    if(mets==0){
                        mets = Mets;
                    }
                }
            }else if (i==10){
                if (Speed>=MetsSpeed){
                    if(mets==0){
                        mets = Mets;
                    }
                }
            }else{
                if (Speed>MetsSpeedBefore && Speed<=MetsSpeed){
                    if(mets==0){
                        mets = Mets;
                    }
                }
            }
        }
        return mets;
    }

}
