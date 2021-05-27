package com.mhd.elemantary.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.mhd.elemantary.fragment.LikeFragment;
import com.mhd.elemantary.fragment.PassFragment;
import com.mhd.elemantary.fragment.TaskFragment;


public class ReadPagerAdapter extends FragmentPagerAdapter{


    public ReadPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch(position) {
            case 0:
                return PassFragment.create();
            case 1:
                return TaskFragment.create();
            case 2:
                return LikeFragment.create();
        }

        return null;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch(position) {
            case 0:
                return "Pass";
            case 1:
                return "Task";
            case 2:
                return "Like";
        }

        return super.getPageTitle(position);
    }

    @Override
    public int getCount() {
        return 3;
    }
}
