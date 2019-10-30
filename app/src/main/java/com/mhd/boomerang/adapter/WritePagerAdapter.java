package com.mhd.boomerang.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.mhd.boomerang.fragment.BaseFragment;
import com.mhd.boomerang.fragment.PostFragment;
import com.mhd.boomerang.fragment.GalleryFragment;
import com.mhd.boomerang.fragment.CameraFragment;


public class WritePagerAdapter extends FragmentPagerAdapter{


    public WritePagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public BaseFragment getItem(int position) {
        switch(position) {
            case 0:
                return GalleryFragment.create();
            case 1:
                return PostFragment.create();
            case 2:
                return CameraFragment.create();
        }

        return null;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch(position) {
            case 0:
                return "Gallery";
            case 1:
                return "Post";
            case 2:
                return "Camera";
        }

        return super.getPageTitle(position);
    }

    @Override
    public int getCount() {
        return 3;
    }
}
