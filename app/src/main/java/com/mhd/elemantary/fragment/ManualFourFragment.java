package com.mhd.elemantary.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.mhd.elemantary.R;
import com.mhd.elemantary.activity.LoginActivity;

import androidx.annotation.Nullable;


public class ManualFourFragment extends BaseFragment {

    public static ManualFourFragment create() {
        return new ManualFourFragment();
    }

    @Override
    public int getLayoutResId() {
        return R.layout.fragment_manual;
    }

    @Override
    public void inOnCreateView(View root, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        TextView btn_start = (Button) root.findViewById(R.id.btn_start);
        btn_start.setVisibility(View.VISIBLE);
        btn_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startService();
            }
        });
        TextView mSubject = (TextView) root.findViewById(R.id.tv_tutorial_subject);
        TextView mContent = (TextView) root.findViewById(R.id.tv_tutorial_content);
        mSubject.setText(R.string.text_tutorial_four_subject);
        mContent.setText(R.string.text_tutorial_four_content);
    }

    private void startService(){
        // 로그인 화면을 띄운다.
        Intent intent = new Intent(mContext, LoginActivity.class);
        mContext.startActivity(intent);
        getActivity().finish();
    }

    @Override
    public void batchFunction(String api) {
//        if(api.equals(getString(R.string.api_editor_clear))) {
//            // editor 내용 초기화.
//            editor.clearAllContents();
//        }
    }
}
