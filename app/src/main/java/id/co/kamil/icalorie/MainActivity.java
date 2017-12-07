package id.co.kamil.icalorie;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private Menu menu=null;
    private TabLayout tabLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

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
        menu.findItem(R.id.menu_logout).setVisible(true);
        menu.findItem(R.id.menu_edit).setVisible(false);
        menu.findItem(R.id.menu_insert).setVisible(false);
        menu.findItem(R.id.menu_delete).setVisible(false);
        if (position==0){
            menu.findItem(R.id.menu_edit).setVisible(true);
        }else if (position==4) {
            menu.findItem(R.id.menu_insert).setVisible(true);
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
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_insert:
                break;
            case R.id.menu_edit:
                if (tabLayout.getSelectedTabPosition()==0){
                    startActivity(new Intent(this,EditProfileActivity.class));
                }
                break;
            case R.id.menu_logout:
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
