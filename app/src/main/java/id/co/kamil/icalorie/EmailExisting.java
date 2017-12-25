package id.co.kamil.icalorie;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * Created by Irham on 12/25/2017.
 */

public class EmailExisting {
    private static final String TAG = "EmailExisting";
    private DatabaseHelper dbHelper;
    private Context context;
    public EmailExisting(Context context){
        this.context = context;
    }
    public boolean checkEmail(String email){
        boolean exist = false;
        dbHelper = new DatabaseHelper(this.context);
        final SQLiteDatabase db = dbHelper.getWritableDatabase();
        final Cursor cursor = db.rawQuery(
                String.format("SELECT * FROM %s WHERE %s=?",
                        DatabaseContract.Pengguna.TABLE_PENGGUNA,
                        DatabaseContract.Pengguna.PENGGUNA_COL_EMAIL),new String[]{email});
        if (cursor!=null){
            try {
                if (cursor.getCount()>0){
                    exist = true;
                }
            }finally {
                Log.i(TAG,"Cursor close");
                cursor.close();
            }
        }
        return exist;
    }
}
