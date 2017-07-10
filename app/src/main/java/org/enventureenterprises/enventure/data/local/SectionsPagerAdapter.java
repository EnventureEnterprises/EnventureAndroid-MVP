package org.enventureenterprises.enventure.data.local;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import org.enventureenterprises.enventure.ui.reports.DailyReportFragment;
import org.enventureenterprises.enventure.ui.reports.MonthlyReportFragment;
import org.enventureenterprises.enventure.ui.reports.WeeklyReportFragment;

import java.util.ArrayList;


public class SectionsPagerAdapter extends FragmentStatePagerAdapter {

    private int NUM_ITEMS = 0;
    private ArrayList<String> frags = new ArrayList<>();

    private String TAG = SectionsPagerAdapter.class.getSimpleName();

    public SectionsPagerAdapter(FragmentManager fm, ArrayList<String> frags) {
        super(fm);
        this.frags = frags;
        this.NUM_ITEMS = frags.size();
    }



    // Returns the fragment to display for that page
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return  DailyReportFragment.newInstance();

            case 1:
                return  WeeklyReportFragment.newInstance ();
            case 2:
                return  MonthlyReportFragment.newInstance();



            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return NUM_ITEMS;
    }

    @Override
    public CharSequence getPageTitle(int position) {

        return frags.get (position);
    }



}