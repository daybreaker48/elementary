package com.mhd.elemantary.adapter;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.mhd.elemantary.fragment.BaseFragment;
import com.mhd.elemantary.fragment.MainFragment;
import com.mhd.elemantary.fragment.ReadFragment;
import com.mhd.elemantary.fragment.WriteFragment;


public class MainPagerAdapter extends FragmentStatePagerAdapter {


    public MainPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public BaseFragment getItem(int position) {
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
                return "할일";
            case 1:
                return "스케쥴";
            case 2:
                return "스스로해요";
        }

        return super.getPageTitle(position);
    }

    @Override
    public int getCount() {
        return 3;
    }
}
