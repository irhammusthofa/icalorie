package id.co.kamil.icalorie;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.preference.PreferenceManager;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private static final int REQUEST_CODE_EDIT_PROFILE = 5;

    private Menu menu=null;
    private TabLayout tabLayout;
    private SessionManager session;
    private DatabaseHelper dbHelper;
    private PedometerSettings mPedometerSettings;

    private SharedPreferences mSettings;
    private String Id;
    private static final String INSERT_DATA_RANGE[] = {
            "INSERT INTO tb_range_kalori VALUES('1','1','pisang ','25','','0','25','0','0');",
            "INSERT INTO tb_range_kalori VALUES('2','2','Apel','40','','0','25','0','0');",
            "INSERT INTO tb_range_kalori VALUES('3','3','jeruk','50','','0','25','0','0');",
            "INSERT INTO tb_range_kalori VALUES('4','4','ayam','40','','26','50','0','0');",
            "INSERT INTO tb_range_kalori VALUES('5','5','Apel','80','','26','50','0','0');",
            "INSERT INTO tb_range_kalori VALUES('6','6','kacang hijau','20','','26','50','0','0');",
            "INSERT INTO tb_range_kalori VALUES('7','7','telur','60','','51','75','0','0');",
            "INSERT INTO tb_range_kalori VALUES('8','8','tempe','50','','51','75','0','0');",
            "INSERT INTO tb_range_kalori VALUES('9','9','daging sapi ','35','','51','75','0','0');",
            "INSERT INTO tb_range_kalori VALUES('10','10','roti','40','','76','100','0','0');",
            "INSERT INTO tb_range_kalori VALUES('11','11','nasi','50','','76','100','0','0');",
            "INSERT INTO tb_range_kalori VALUES('12','12','biskuit','30','','76','100','0','0');",
            "INSERT INTO tb_range_kalori VALUES('13','13','susu','200','125','101','125','0','0');",
            "INSERT INTO tb_range_kalori VALUES('14','14','roti','30','60','101','125','13','1');",
            "INSERT INTO tb_range_kalori VALUES('15','15','selai coklat','20','60','101','125','0','0');",
            "INSERT INTO tb_range_kalori VALUES('16','16','nasi','50','90','126','150','15','1');",
            "INSERT INTO tb_range_kalori VALUES('17','17','ayam','40','50','126','150','0','0');",
            "INSERT INTO tb_range_kalori VALUES('18','18','jus (jambu biji)','100','50','126','150','17','1');",
            "INSERT INTO tb_range_kalori VALUES('19','19','gula','26','100','151','175','0','0');",
            "INSERT INTO tb_range_kalori VALUES('20','20','nasi','100','175','151','175','0','0');",
            "INSERT INTO tb_range_kalori VALUES('21','21','roti','70','175','151','175','0','0');",
            "INSERT INTO tb_range_kalori VALUES('22','22','biskuit','50','175','151','175','0','0');",
            "INSERT INTO tb_range_kalori VALUES('23','23','nasi ','50','90','151','175','22','1');",
            "INSERT INTO tb_range_kalori VALUES('24','24','telur','60','75','176','200','0','0');",
            "INSERT INTO tb_range_kalori VALUES('25','25','nasi','50','90','176','200','24','1');",
            "INSERT INTO tb_range_kalori VALUES('26','26','ayam','40','50','176','200','25','1');",
            "INSERT INTO tb_range_kalori VALUES('27','27','minyak goreng','5','50','176','200','0','0');",
            "INSERT INTO tb_range_kalori VALUES('28','28','roti','50','120','201','225','27','1');",
            "INSERT INTO tb_range_kalori VALUES('29','29','telur','60','75','201','225','28','1');",
            "INSERT INTO tb_range_kalori VALUES('30','30','minyak goreng','3','30','201','225','0','0');",
            "INSERT INTO tb_range_kalori VALUES('31','31','roti','50','120','226','250','30','1');",
            "INSERT INTO tb_range_kalori VALUES('32','32','telur','60','75','226','250','31','1');",
            "INSERT INTO tb_range_kalori VALUES('33','33','minyak goreng','3','30','226','250','32','1');",
            "INSERT INTO tb_range_kalori VALUES('34','34','tomat','30','','251','275','0','0');",
            "INSERT INTO tb_range_kalori VALUES('35','35','nasi','70','120','251','275','34','1');",
            "INSERT INTO tb_range_kalori VALUES('36','36','ayam','40','50','251','275','35','1');",
            "INSERT INTO tb_range_kalori VALUES('37','37','minyak goreng','5','50','251','275','36','1');",
            "INSERT INTO tb_range_kalori VALUES('38','38','pisang','50','50','251','275','0','0');",
            "INSERT INTO tb_range_kalori VALUES('39','39','roti','50','120','276','300','38','1');",
            "INSERT INTO tb_range_kalori VALUES('40','40','telur','60','75','276','300','39','1');",
            "INSERT INTO tb_range_kalori VALUES('41','41','minyak goreng','3','30','276','300','40','1');",
            "INSERT INTO tb_range_kalori VALUES('42','42','tomat','30','30','276','300','41','1');",
            "INSERT INTO tb_range_kalori VALUES('43','43','mayonaise','10','30','276','300','0','0');"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        dbHelper = new DatabaseHelper(this);

        final SQLiteDatabase db = dbHelper.getWritableDatabase();
        final Cursor cursor = db.rawQuery(
                String.format("SELECT * FROM %s",DatabaseContract.RangeKalori.TABLE_RANGE_KALORI),null
        );
        if (cursor != null){
            try{
                if (cursor.moveToFirst()){
                    Log.i(TAG, "Count: " + String.valueOf(cursor.getCount()));
                }else{
                    db.execSQL("DELETE FROM " + DatabaseContract.RangeKalori.TABLE_RANGE_KALORI);
                    int i;
                    for(i=0;i<INSERT_DATA_RANGE.length;i++){
                        db.execSQL(INSERT_DATA_RANGE[i]);
                    }
                }
            }finally {
                Log.i(TAG,"cursor close");
                cursor.close();
            }
        }

        session = new SessionManager(getApplicationContext());
        if (!session.checkLogin()){
            session.login();
            finish();
        }

        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.ic_account_circle_black_24dp));
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.ic_queue_play_next_black_24dp));
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.ic_fitness_center_black_24dp));
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.ic_battery_charging_80_black_24dp));
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.ic_storage_black_24dp));
        tabLayout.getTabAt(0).setContentDescription("Profile");
        tabLayout.getTabAt(1).setContentDescription("BMR");
        tabLayout.getTabAt(2).setContentDescription("Exercise");
        tabLayout.getTabAt(3).setContentDescription("Nutrition");
        tabLayout.getTabAt(4).setContentDescription("Database");
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        final ViewPager pager = (ViewPager) findViewById(R.id.pager);
        final PagerAdapter pagerAdapter = new PagerAdapter(getSupportFragmentManager(),tabLayout.getTabCount());
        pager.setAdapter(pagerAdapter);
        pager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                pager.setCurrentItem(tab.getPosition());
                changeMenu(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        try{
            Pengguna user;
            user = session.getUserDetails();
            float berat = Integer.parseInt(user.getBerat());
            float tinggi = Integer.parseInt(user.getTinggi());
            if (berat<=0) berat = 50;
            if (tinggi<=0) tinggi = 150;
            mSettings = PreferenceManager.getDefaultSharedPreferences(this);
            mPedometerSettings = new PedometerSettings(mSettings);
            SharedPreferences.Editor editor = mSettings.edit();
            editor.putString("body_weight", String.valueOf(berat));
            editor.putString("body_height", String.valueOf(tinggi));
            editor.commit();
        }catch (Exception e){
            Log.e(TAG,e.getMessage());
        }
    }
    private void changeMenu(int position){
        try {
            menu.findItem(R.id.menu_logout).setVisible(true);
            menu.findItem(R.id.menu_edit).setVisible(false);
            menu.findItem(R.id.menu_insert).setVisible(false);
            menu.findItem(R.id.menu_delete).setVisible(false);
            menu.findItem(R.id.menu_setting).setVisible(false);
            if (position==0){
                menu.findItem(R.id.menu_edit).setVisible(true);
            }else if (position==2) {
                menu.findItem(R.id.menu_setting).setVisible(true);
            }else if (position==4) {
                menu.findItem(R.id.menu_insert).setVisible(true);
            }
        }catch (Exception e){
            Log.i(TAG,e.getMessage());
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu,menu);
        this.menu = menu;
        changeMenu(tabLayout.getSelectedTabPosition());
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        for (Fragment fragment : getSupportFragmentManager().getFragments()) {
            fragment.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_insert:
                if (tabLayout.getSelectedTabPosition()==4){
                    Intent intent = new Intent(getBaseContext(),EditNutrisiActivity.class);
                    intent.putExtra("Type",0);
                    startActivityForResult(intent,1);
                }
                break;
            case R.id.menu_edit:
                if (tabLayout.getSelectedTabPosition()==0){
                    try{
                        Id = session.getId();
                        Intent intent = new Intent(this,EditProfileActivity.class);
                        intent.putExtra("id",Id);
                        startActivityForResult(intent,REQUEST_CODE_EDIT_PROFILE);
                    }catch (Exception e){
                        Log.e(TAG,e.getMessage());
                    }

                }
                break;
            case R.id.menu_setting:
                startActivity(new Intent(this, Settings.class));
                break;
            case R.id.menu_logout:
                AlertDialog.Builder alBuilder = new AlertDialog.Builder(this);
                alBuilder.setTitle("Logout");
                alBuilder.setMessage("Apakah anda yakin akan keluar dari aplikasi ?");
                alBuilder.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.i(TAG,"Sedang proses Logout");
                        // TODO: Clear Session
                        session.logoutUser();
                        finish();
                    }
                });
                alBuilder.setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
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
