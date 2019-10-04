package com.mhd.boomerang.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mhd.boomerang.R;
import com.mhd.boomerang.util.Util;


public class ReceivedFragment extends BaseFragment {

    public static ReceivedFragment create() {
        return new ReceivedFragment();
    }

    @Override
    public int getLayoutResId() {
        return R.layout.fragment_received;
    }

    @Override
    public void inOnCreateView(View root, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //Statusbar 아래로 내리기
        final TextView mTitle = (TextView) root.findViewById(R.id.vst_top_title);
        RelativeLayout.LayoutParams mLayoutParams = (RelativeLayout.LayoutParams) mTitle.getLayoutParams();
        mLayoutParams.topMargin = Util.getInstance().getStatusBarHeight(root.getContext());
        mTitle.setLayoutParams(mLayoutParams);
    }
}
