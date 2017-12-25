package id.co.kamil.icalorie;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
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
    private Menu menu=null;
    private TabLayout tabLayout;
    private SessionManager session;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

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
                    startActivity(new Intent(this,EditProfileActivity.class));
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
