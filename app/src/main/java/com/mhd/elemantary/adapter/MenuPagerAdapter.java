package com.mhd.elemantary.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.mhd.elemantary.fragment.ScheduleFragment;
import com.mhd.elemantary.fragment.SettingFragment;
import com.mhd.elemantary.fragment.SumFragment;
import com.mhd.elemantary.fragment.TodoFragment;
import com.mhd.elemantary.fragment.SelfFragment;

import org.jetbrains.annotations.NotNull;


public class MenuPagerAdapter extends FragmentStateAdapter{

    public MenuPagerAdapter(FragmentActivity fa) {
        super(fa);
    }

    @NonNull
    @NotNull
    @Override
    public Fragment createFragment(int position) {
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
    public int getItemCount() {
        return 5;
    }
}
