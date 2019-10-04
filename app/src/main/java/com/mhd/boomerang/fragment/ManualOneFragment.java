package com.mhd.boomerang.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;

import com.mhd.boomerang.R;


public class ManualOneFragment extends BaseFragment {

    public static ManualOneFragment create() {
        return new ManualOneFragment();
    }

    @Override
    public int getLayoutResId() {
        return R.layout.fragment_manual;
    }

    @Override
    public void inOnCreateView(View root, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

    }
}
