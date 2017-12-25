package id.co.kamil.icalorie;


import java.util.Date;

/**
 * Created by Irham on 12/17/2017.
 */

public class WaktuTerbilang {
    long MillisecondTime, TimeBuff, UpdateTime = 0L ;
    int Seconds, Minutes, MilliSeconds,Hours ;

    public String convert(long time){

        UpdateTime = TimeBuff + time;
        Seconds = (int) (UpdateTime / 1000);
        Hours = Minutes / 60;
        Minutes = Seconds / 60;
        Seconds = Seconds % 60;
        MilliSeconds = (int) (UpdateTime % 1000);


        String narasi = "";
        if (Hours>0 && Minutes > 0){
            narasi = String.valueOf(Hours) + " jam " + String.valueOf(Minutes) + " menit ";
        }else if (Hours > 0 && Minutes ==0){
            narasi = String.valueOf(Hours) + " jam ";
        }else if(Minutes>0 && Seconds >0){
            narasi = String.valueOf(Minutes) + " menit " + String.valueOf(Seconds) + " detik";
        }else if(Minutes>0 && Seconds == 0){
            narasi = String.valueOf(Minutes) + " menit ";
        }else{
            narasi = String.valueOf(Seconds) + " detik ";
        }

        return narasi;
    }
}
