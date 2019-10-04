package com.mhd.boomerang.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;

import com.mhd.boomerang.R;


public class GalleryFragment extends BaseFragment {

    public static GalleryFragment create() {
        return new GalleryFragment();
    }

    @Override
    public int getLayoutResId() {
        return R.layout.fragment_gallery;
    }

    @Override
    public void inOnCreateView(View root, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

    }
}
