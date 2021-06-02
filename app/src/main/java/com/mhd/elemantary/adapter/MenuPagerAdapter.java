package com.mhd.elemantary.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.mhd.elemantary.fragment.ScheduleFragment;
import com.mhd.elemantary.fragment.SettingFragment;
import com.mhd.elemantary.fragment.SumFragment;
import com.mhd.elemantary.fragment.TodoFragment;
import com.mhd.elemantary.fragment.SelfFragment;


public class MenuPagerAdapter extends FragmentPagerAdapter{


    public MenuPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch(position) {
            case 0:
                return TodoFragment.create();
            case 1:
                return ScheduleFragment.create();
            case 2:
                return SelfFragment.create();
            case 3:
                return SumFragment.create();
            case 4:
                return SettingFragment.create();
        }

        return null;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch(position) {
            case 0:
                return "할일";
            case 1:
                return "스케쥴";
            case 2:
                return "스스로해요";
            case 3:
                return "통계";
            case 4:
                return "설정";
        }

        return super.getPageTitle(position);
    }

    @Override
    public int getCount() {
        return 5;
    }
}
