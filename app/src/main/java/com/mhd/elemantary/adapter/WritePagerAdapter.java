package com.mhd.elemantary.adapter;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.mhd.elemantary.fragment.BaseFragment;
import com.mhd.elemantary.fragment.PostFragment;
import com.mhd.elemantary.fragment.GalleryFragment;
import com.mhd.elemantary.fragment.CameraFragment;


public class WritePagerAdapter extends FragmentStatePagerAdapter {


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
