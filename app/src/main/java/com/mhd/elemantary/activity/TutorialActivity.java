package com.mhd.elemantary.activity;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Px;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;
import android.view.View;

import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.google.gson.Gson;
import com.mhd.elemantary.R;
import com.mhd.elemantary.adapter.TutorialPagerAdapter;
import com.mhd.elemantary.common.CircleIndicator;
import com.mhd.elemantary.common.MHDApplication;
import com.mhd.elemantary.common.vo.UserVo;
import com.mhd.elemantary.network.MHDNetworkInvoker;
import com.mhd.elemantary.util.MHDDialogUtil;
import com.mhd.elemantary.util.MHDLog;

public class TutorialActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initialize(R.layout.activity_tutorial);

        // 상단 status bar 를 감추고 그 영역까지 확대시킨다.
//        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_FULLSCREEN);

        // 배경. 컬러 애니메이션 등의 효과를 주기 위해
        final View background = findViewById(R.id.am_tutorial_background_view);

        // viewpager
        ViewPager2 viewPager2 = findViewById(R.id.am_tutorial_view_pager);
        TutorialPagerAdapter adapter = new TutorialPagerAdapter(this);
        viewPager2.setAdapter(adapter);

//        LineIndicator lineIndicator = (LineIndicator) findViewById(R.id.line_indicator);
//        lineIndicator.setupWithViewPager(viewPager);

        CircleIndicator circleIndicator = (CircleIndicator) findViewById(R.id.circle_indicator);
        circleIndicator.setupWithViewPager(viewPager2);

        // viewpager 위에서 돌아가는 메뉴.
//        MHDApplication.getInstance().getMHDSvcManager().setGlobalTabsView((GlobalTabsView) findViewById(R.id.am_snap_tabs));
//        MHDApplication.getInstance().getMHDSvcManager().getGlobalTabsView().setUpWithVerticalViewPager(viewPager);

        // viewpager 에서 특정위치 view 초기 지정.
//        viewPager.setCurrentItem(1);

        // UI 에 필요한 컬러코드 값
        final int colorBlue = ContextCompat.getColor(this, R.color.light_blue);
        final int colorPurple = ContextCompat.getColor(this, R.color.light_purple);

        // viewpager 이동에 따른 컬러, 투명도 변경 애니메이션 처리.
        ViewPager2.OnPageChangeCallback callback = new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position,
                                       float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);

                if(position == 0) {
                    background.setBackgroundColor(colorBlue);
//                    background.setAlpha(1 - positionOffset);
//                    MHDLog.e("dagian = 0", 1 - positionOffset);
                }
                else if(position == 1) {
                    background.setBackgroundColor(colorPurple);
//                    background.setAlpha(positionOffset);
//                    MHDLog.e("dagian = 1", positionOffset);
                }
            }
        };
        viewPager2.registerOnPageChangeCallback(callback);
    }

    @Override
    protected boolean networkResponseProcess(String result) {
        boolean resultFlag = super.networkResponseProcess(result);
        MHDLog.d(TAG, "networkResponseProcess resultFlag >>> " + resultFlag);

        if(!resultFlag) return resultFlag;

        // resultFlag 이 true 라면 현재 여기에 필요한 data 들이 전역에 들어가 있는 상태.

        if("M".equals(nvResultCode)){
            // Just show nvMsg
            MHDDialogUtil.sAlert(mContext, nvMsg);
            return true;
        }else if("S".equals(nvResultCode)){
            if(nvCnt == 0){
                // 신규 사용자인 경우.
                // 튜토리얼 화면을 띄운다.
                // 슬라이드 형식 말고 자연스럽게 나타나게.
                Intent i = new Intent(mContext, TutorialActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(i);
                overridePendingTransition(0, 0);
                finish();
            }else{
                MHDLog.d(TAG, "networkResponseProcess nvMsg >>> " + nvMsg);

                // 기존 사용자인 경우.
                // vo 및 각종 변수에 저장하고 메인으로 넘긴다. 레코드가 하나여도 jsonarray 로 보내니 gson에서 에러가 나드라.
                Gson gson = new Gson();
                UserVo userVo;
                userVo = gson.fromJson(nvMsg, UserVo.class);
                MHDApplication.getInstance().getMHDSvcManager().setUserVo(null);
                MHDApplication.getInstance().getMHDSvcManager().setUserVo(userVo);

                if(MHDApplication.getInstance().getMHDSvcManager().getUserVo() != null){
                    // MainActivity 로 이동
                    goMain();
                }
            }
        }

//        try {
//            if("S".equals(resultCode)){  // Success
//                // 기존 사용자라면.
//                // 서버 저장. 단말 기본 정보 : VO 방식
//                // 이것은 로그인이 되었을때만 넣는게 맞다.
//                Gson gson = new Gson();
//                UserVo userVo;
//                userVo = gson.fromJson(jsonDataObject.toString(), UserVo.class);
//                MHDApplication.getInstance().getMHDSvcManager().setUserVo(null);
//                MHDApplication.getInstance().getMHDSvcManager().setUserVo(userVo);
//
//                if(MHDApplication.getInstance().getMHDSvcManager().getUserVo() != null){
//                    // MainActivity 로 이동
//                    goMain();
//                }
//            }else{  // maybe -1
//                // 신규 사용자라면.
//
//                // 튜토리얼 화면을 띄운다.
//                // 슬라이드 형식 말고 자연스럽게 나타나게.
//                Intent i = new Intent(mContext, TutorialActivity.class);
//                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
//                startActivity(i);
//                overridePendingTransition(0, 0);
//                finish();
//            }
//        } catch (Exception e) {
//            MHDLog.printException(e);
//        }
        return true;
    }
}
