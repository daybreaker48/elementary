package com.mhd.stard.fragment;

import android.os.Bundle;
import androidx.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;

import com.mhd.stard.R;


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

    @Override
    public void batchFunction(String api) {
//        if(api.equals(getString(R.string.api_editor_clear))) {
//            // editor 내용 초기화.
//            editor.clearAllContents();
//        }
    }
}
