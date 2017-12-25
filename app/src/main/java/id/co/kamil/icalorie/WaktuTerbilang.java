package id.co.kamil.icalorie;

import java.util.Date;

/**
 * Created by Irham on 12/17/2017.
 */

public class WaktuTerbilang {

    public String convert(long time){
        Date date = new Date(time);
        final int jam = date.getHours();
        final int menit = date.getMinutes();
        final int detik = date.getSeconds();

        String narasi = "";
        if (jam>0 && menit > 0){
            narasi = String.valueOf(jam) + " jam " + String.valueOf(menit) + " menit ";
        }else if (jam > 0 && menit ==0){
            narasi = String.valueOf(jam) + " jam ";
        }else if(menit>0 && detik >0){
            narasi = String.valueOf(menit) + " menit " + String.valueOf(detik) + " detik";
        }else if(menit>0 && detik == 0){
            narasi = String.valueOf(menit) + " menit ";
        }else{
            narasi = String.valueOf(detik) + " detik ";
        }

        return narasi;
    }
}
