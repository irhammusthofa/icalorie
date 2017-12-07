package id.co.kamil.icalorie;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * Created by Irham on 12/5/2017.
 */

public class PagerAdapter extends FragmentStatePagerAdapter {
    int numOfTabs;

    public PagerAdapter(FragmentManager fm, int numOfTabs){
        super(fm);
        this.numOfTabs = numOfTabs;
    }

    public Fragment getItem(int position){
        switch (position){
            case 0:
                ProfileFragment profileFragment = new ProfileFragment();
                return profileFragment;
            case 1:
                BmrFragment bmrFragment = new BmrFragment();
                return bmrFragment;
            case 2:
                ExerciseFragment exerciseFragment = new ExerciseFragment();
                return exerciseFragment;
            case 3:
                NutritionFragment nutritionFragment = new NutritionFragment();
                return nutritionFragment;
            case 4:
                DatabaseFragment databaseFragment = new DatabaseFragment();
                return databaseFragment;
            default:
                return null;
        }
    }

    public int getCount(){
        return numOfTabs;
    }
}
