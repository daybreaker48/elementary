package com.mhd.elemantary.fragment;

import android.os.Bundle;
import androidx.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mhd.elemantary.R;


public class ManualThreeFragment extends BaseFragment {

    public static ManualThreeFragment create() {
        return new ManualThreeFragment();
    }

    @Override
    public int getLayoutResId() {
        return R.layout.fragment_manual;
    }

    @Override
    public void inOnCreateView(View root, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        TextView mSubject = (TextView) root.findViewById(R.id.tv_tutorial_subject);
        TextView mContent = (TextView) root.findViewById(R.id.tv_tutorial_content);
        mSubject.setText(R.string.text_tutorial_three_subject);
        mContent.setText(R.string.text_tutorial_three_content);
    }

    @Override
    public void batchFunction(String api) {
//        if(api.equals(getString(R.string.api_editor_clear))) {
//            // editor 내용 초기화.
//            editor.clearAllContents();
//        }
    }
}
