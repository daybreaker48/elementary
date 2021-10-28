package com.mhd.elemantary.fragment;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.preference.Preference;
import androidx.preference.PreferenceManager;

import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mhd.elemantary.BuildConfig;
import com.mhd.elemantary.MainActivity;
import com.mhd.elemantary.R;
import com.mhd.elemantary.activity.KidsListActivity;
import com.mhd.elemantary.activity.ModifyKidsActivity;
import com.mhd.elemantary.adapter.ReCyclerKidsAdapter;
import com.mhd.elemantary.common.MHDApplication;
import com.mhd.elemantary.common.vo.KidsVo;
import com.mhd.elemantary.util.MHDDialogUtil;
import com.mhd.elemantary.util.MHDLog;

public class SettingFragment extends BaseFragment {
    LinearLayout ll_kids_regist_title, ll_logout_title, ll_kids_cell_list_1, ll_kids_cell_list_2;
    TextView tv_kids_regist_summary, tv_user_info, tv_version;
    ImageView iv_kids_extension;
    SharedPreferences pref;
    TextView ll_kids_cell_1, ll_kids_cell_2, ll_kids_cell_3, ll_kids_cell_4, ll_kids_cell_5, ll_kids_cell_6;
    int kidsCount = 0;

    public static SettingFragment create() {
        return new SettingFragment();
    }

    @Override
    public int getLayoutResId() {
        return R.layout.fragment_setting;
    }

    @Override
    public void inOnCreateView(View root, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //Statusbar 아래로 내리기
//        final TextView mTitle = (TextView) root.findViewById(R.id.vst_top_title);
//        RelativeLayout.LayoutParams mLayoutParams = (RelativeLayout.LayoutParams) mTitle.getLayoutParams();
//        mLayoutParams.topMargin = Util.getInstance().getStatusBarHeight(root.getContext());
//        mTitle.setLayoutParams(mLayoutParams);
//        init();

        ll_kids_regist_title = (LinearLayout) root.findViewById(R.id.ll_kids_regist_title);
        ll_logout_title = (LinearLayout) root.findViewById(R.id.ll_logout_title);
        tv_kids_regist_summary = (TextView) root.findViewById(R.id.tv_kids_regist_summary);
        tv_version = (TextView) root.findViewById(R.id.tv_version);
        tv_version.setText(BuildConfig.VERSION_NAME);
        tv_user_info = (TextView) root.findViewById(R.id.tv_user_info);
        tv_user_info.setText(MHDApplication.getInstance().getMHDSvcManager().getUserVo().getUuMail());
        iv_kids_extension = (ImageView) root.findViewById(R.id.iv_kids_extension);
        ll_kids_cell_list_1 = (LinearLayout) root.findViewById(R.id.ll_kids_cell_list_1);
        ll_kids_cell_list_2 = (LinearLayout) root.findViewById(R.id.ll_kids_cell_list_2);
        ll_kids_cell_1 = (TextView) root.findViewById(R.id.ll_kids_cell_1);
        ll_kids_cell_2 = (TextView) root.findViewById(R.id.ll_kids_cell_2);
        ll_kids_cell_3 = (TextView) root.findViewById(R.id.ll_kids_cell_3);
        ll_kids_cell_4 = (TextView) root.findViewById(R.id.ll_kids_cell_4);
        ll_kids_cell_5 = (TextView) root.findViewById(R.id.ll_kids_cell_5);
        ll_kids_cell_6 = (TextView) root.findViewById(R.id.ll_kids_cell_6);

        if(MHDApplication.getInstance().getMHDSvcManager().getKidsVo() != null) {
            KidsVo kidsVo = MHDApplication.getInstance().getMHDSvcManager().getKidsVo();
            kidsCount = kidsVo == null ? 0 : kidsVo.getCnt();
        }
        ll_kids_regist_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(kidsCount >= 6){
                    MHDDialogUtil.sAlert(mContext, R.string.alert_kids_limit, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            return;
                        }
                    });
                }else {
                    ((MainActivity) MainActivity.context_main).startKidsRegist();
                }
            }
        });
        ll_logout_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity)MainActivity.context_main).logoutProcess();
            }
        });
        iv_kids_extension.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity)MainActivity.context_main).startKidsList();
            }
        });

        //SharedPreference객체를 참조하여 설정상태에 대한 제어 가능..
//        pref = PreferenceManager.getDefaultSharedPreferences(getActivity());
    }

    @Override
    public void onResume() {
        super.onResume();

        batchFunction("");
    }

    @Override
    public void batchFunction(String api) {
        ll_kids_cell_list_1.setVisibility(View.GONE);
        ll_kids_cell_list_2.setVisibility(View.GONE);
        ll_kids_cell_1.setVisibility(View.GONE);
        ll_kids_cell_2.setVisibility(View.GONE);
        ll_kids_cell_3.setVisibility(View.GONE);
        ll_kids_cell_4.setVisibility(View.GONE);
        ll_kids_cell_5.setVisibility(View.GONE);
        ll_kids_cell_6.setVisibility(View.GONE);

        if(MHDApplication.getInstance().getMHDSvcManager().getKidsVo() != null){
            KidsVo kidsVo = MHDApplication.getInstance().getMHDSvcManager().getKidsVo();
            kidsCount = kidsVo == null ? 0 : kidsVo.getCnt();
            String kidsInfo = "";
            if(kidsCount>0){
                ll_kids_cell_list_1.setVisibility(View.VISIBLE);
            }
            if(kidsCount>3){
                ll_kids_cell_list_2.setVisibility(View.VISIBLE);
            }

            for(int i=0;i<kidsCount;i++){
//                if(i==0) kidsInfo = kidsVo.getMsg().get(i).getName() + " / " + kidsVo.getMsg().get(i).getAge() + "세";
//                else kidsInfo = kidsInfo + "\n" + kidsVo.getMsg().get(i).getName() + " / " + kidsVo.getMsg().get(i).getAge() + "세";
                kidsInfo = kidsVo.getMsg().get(i).getName() + "/" + kidsVo.getMsg().get(i).getAge() + "세";

                if(i == 0){
                    ll_kids_cell_1.setVisibility(View.VISIBLE);
                    ll_kids_cell_1.setText(kidsInfo);
                    ll_kids_cell_1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ((MainActivity)getActivity()).startKidsModify(0);
                        }
                    });
                }
                if(i == 1){
                    ll_kids_cell_2.setVisibility(View.VISIBLE);
                    ll_kids_cell_2.setText(kidsInfo);
                    ll_kids_cell_2.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ((MainActivity)getActivity()).startKidsModify(1);
                        }
                    });
                }
                if(i == 2){
                    ll_kids_cell_3.setVisibility(View.VISIBLE);
                    ll_kids_cell_3.setText(kidsInfo);
                    ll_kids_cell_3.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ((MainActivity)getActivity()).startKidsModify(2);
                        }
                    });
                }
                if(i == 3){
                    ll_kids_cell_4.setVisibility(View.VISIBLE);
                    ll_kids_cell_4.setText(kidsInfo);
                    ll_kids_cell_4.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ((MainActivity)getActivity()).startKidsModify(3);
                        }
                    });
                }
                if(i == 4){
                    ll_kids_cell_5.setVisibility(View.VISIBLE);
                    ll_kids_cell_5.setText(kidsInfo);
                    ll_kids_cell_5.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ((MainActivity)getActivity()).startKidsModify(4);
                        }
                    });
                }
                if(i == 5){
                    ll_kids_cell_6.setVisibility(View.VISIBLE);
                    ll_kids_cell_6.setText(kidsInfo);
                    ll_kids_cell_6.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ((MainActivity)getActivity()).startKidsModify(5);
                        }
                    });
                }
            }
        }else{
            kidsCount = 0;
        }

        tv_kids_regist_summary.setText("총 "+ kidsCount +" 명");

        if(kidsCount == 0){
            iv_kids_extension.setVisibility(View.GONE);
        }else{
            iv_kids_extension.setVisibility(View.VISIBLE);
        }
    }

}

