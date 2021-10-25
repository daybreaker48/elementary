package com.mhd.elemantary.fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.preference.Preference;
import androidx.preference.PreferenceManager;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mhd.elemantary.MainActivity;
import com.mhd.elemantary.R;
import com.mhd.elemantary.common.MHDApplication;
import com.mhd.elemantary.common.vo.KidsVo;
import com.mhd.elemantary.util.MHDLog;

public class SettingFragment extends BaseFragment {
    LinearLayout ll_kids_regist_title, ll_logout_title;
    TextView tv_kids_regist_summary;
    ImageView iv_kids_extension;
    SharedPreferences pref;

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
        iv_kids_extension = (ImageView) root.findViewById(R.id.iv_kids_extension);

        if(MHDApplication.getInstance().getMHDSvcManager().getKidsVo() != null){
            KidsVo kidsVo = MHDApplication.getInstance().getMHDSvcManager().getKidsVo();
            int kidsCount = kidsVo.getCnt();
            String kidsInfo = "";
            for(int i=0;i<kidsCount;i++){
                if(i==0) kidsInfo = kidsVo.getMsg().get(i).getName() + " / " + kidsVo.getMsg().get(i).getAge() + "세";
                else kidsInfo = kidsInfo + "\n" + kidsVo.getMsg().get(i).getName() + " / " + kidsVo.getMsg().get(i).getAge() + "세";
            }
            tv_kids_regist_summary.setText("총 "+ kidsCount +" 명");
            if(kidsCount == 0){
                iv_kids_extension.setVisibility(View.GONE);
            }else{
                iv_kids_extension.setVisibility(View.VISIBLE);
            }
        }
        ll_kids_regist_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity)MainActivity.context_main).startKidsRegist();
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
    public void batchFunction(String api) {
        KidsVo kidsVo = MHDApplication.getInstance().getMHDSvcManager().getKidsVo();
        int kidsCount = kidsVo.getCnt();
        String kidsInfo = "";
        for(int i=0;i<kidsCount;i++){
            if(i==0) kidsInfo = kidsVo.getMsg().get(i).getName() + " / " + kidsVo.getMsg().get(i).getAge() + "세";
            else kidsInfo = kidsInfo + "\n" + kidsVo.getMsg().get(i).getName() + " / " + kidsVo.getMsg().get(i).getAge() + "세";
        }
        tv_kids_regist_summary.setText("총 "+ kidsCount +" 명");
    }

//    private void init(){
//        getChildFragmentManager().beginTransaction().replace(R.id.preferencef_frame, new PreferenceCustomFragment()).commit();
//
//        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
//        sharedPreferences.registerOnSharedPreferenceChangeListener(new SharedPreferences.OnSharedPreferenceChangeListener() {
//            @Override
//            public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
//                MHDLog.d("SettingFragment", key);
//            }
//        });
//    }
}

