package com.mhd.stard.adapter;

import androidx.annotation.NonNull;

import androidx.fragment.app.FragmentActivity;

import com.mhd.stard.fragment.ManualFourFragment;
import com.mhd.stard.fragment.ManualThreeFragment;
import com.mhd.stard.fragment.ManualTwoFragment;
import com.mhd.stard.fragment.ManualOneFragment;

import androidx.viewpager2.adapter.FragmentStateAdapter;


public class TutorialPagerAdapter extends FragmentStateAdapter {

    private final int ITEM_COUNT = 4;

    public TutorialPagerAdapter(@NonNull FragmentActivity fa) {
        super(fa);
    }

    @Override
    public androidx.fragment.app.Fragment createFragment(int i) {
        switch(i) {
            case 0:
                return ManualOneFragment.create();
            case 1:
                return ManualTwoFragment.create();
            case 2:
                return ManualThreeFragment.create();
            case 3:
                return ManualFourFragment.create();
            default:
                return ManualOneFragment.create();
        }
    }

    @Override
    public int getItemCount() {
        return ITEM_COUNT;
    }
}
