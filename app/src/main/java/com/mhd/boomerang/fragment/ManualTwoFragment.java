package com.mhd.boomerang.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mhd.boomerang.R;


public class ManualTwoFragment extends BaseFragment {

    public static ManualTwoFragment create() {
        return new ManualTwoFragment();
    }

    @Override
    public int getLayoutResId() {
        return R.layout.fragment_manual;
    }

    @Override
    public void inOnCreateView(View root, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        TextView mSubject = (TextView) root.findViewById(R.id.tv_tutorial_subject);
        TextView mContent = (TextView) root.findViewById(R.id.tv_tutorial_content);
        mSubject.setText(R.string.text_tutorial_two_subject);
        mContent.setText(R.string.text_tutorial_two_content);
    }
}
