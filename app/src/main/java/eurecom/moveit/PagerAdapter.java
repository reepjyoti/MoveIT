package eurecom.moveit;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;

/**
 * Created by test on 21/01/2018.
 */

public class PagerAdapter extends FragmentStatePagerAdapter
{
    int mNumOfTabs;

    public PagerAdapter(FragmentManager fragmentManager, int NumOfTabs) {
        super(fragmentManager);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 1:
                return new Sending_Destination();

            case 2:
                return new Sending_Price();

            default:
                return new Sending_PackageDesc();
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}