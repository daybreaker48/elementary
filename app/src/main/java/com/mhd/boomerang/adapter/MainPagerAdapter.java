package com.mhd.boomerang.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.mhd.boomerang.fragment.MainFragment;
import com.mhd.boomerang.fragment.ReadFragment;
import com.mhd.boomerang.fragment.WriteFragment;


public class MainPagerAdapter extends FragmentPagerAdapter{


    public MainPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch(position) {
            case 0:
                return WriteFragment.create();
            case 1:
                return MainFragment.create();
            case 2:
                return ReadFragment.create();
        }

        return null;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch(position) {
            case 0:
                return "Write";
            case 1:
                return "Stat";
            case 2:
                return "Read";
        }

        return super.getPageTitle(position);
    }

    @Override
    public int getCount() {
        return 3;
    }
}
