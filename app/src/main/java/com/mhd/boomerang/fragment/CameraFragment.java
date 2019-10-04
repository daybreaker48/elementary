package com.mhd.boomerang.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;

import com.mhd.boomerang.R;


public class CameraFragment extends BaseFragment {

    public static CameraFragment create() {
        return new CameraFragment();
    }

    @Override
    public int getLayoutResId() {
        return R.layout.fragment_camera;
    }

    @Override
    public void inOnCreateView(View root, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

    }
}
