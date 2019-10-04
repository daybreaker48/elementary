package com.mhd.boomerang.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.mhd.boomerang.fragment.ManualThreeFragment;
import com.mhd.boomerang.fragment.ManualTwoFragment;
import com.mhd.boomerang.fragment.ManualOneFragment;
import com.mhd.boomerang.fragment.MemberInputFragment;


public class TutorialPagerAdapter extends FragmentPagerAdapter{

    private final int ITEM_COUNT = 4;

    public TutorialPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch(position) {
            case 0:
                return ManualOneFragment.create();
            case 1:
                return ManualTwoFragment.create();
            case 2:
                return ManualThreeFragment.create();
            case 3:
                return MemberInputFragment.create();
        }

        return null;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch(position) {
            case 0:
                return "Manual";
            case 1:
                return "Manual";
            case 2:
                return "Manual";
            case 3:
                return "Info";
        }

        return super.getPageTitle(position);
    }

    @Override
    public int getCount() {
        return ITEM_COUNT;
    }
}
