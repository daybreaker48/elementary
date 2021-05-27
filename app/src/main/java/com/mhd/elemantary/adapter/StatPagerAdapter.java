package com.mhd.elemantary.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.mhd.elemantary.fragment.StatsFragment;
import com.mhd.elemantary.fragment.ReceivedFragment;
import com.mhd.elemantary.fragment.ReceivingFragment;


public class StatPagerAdapter extends FragmentPagerAdapter{


    public StatPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch(position) {
            case 0:
                return ReceivedFragment.create();
            case 1:
                return StatsFragment.create();
            case 2:
                return ReceivingFragment.create();
        }

        return null;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch(position) {
            case 0:
                return "Past";
            case 1:
                return "View";
            case 2:
                return "Next";
        }

        return super.getPageTitle(position);
    }

    @Override
    public int getCount() {
        return 3;
    }
}
