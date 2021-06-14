package com.mhd.elemantary;

import android.content.Intent;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;
import android.view.View;

import com.mhd.elemantary.activity.BaseActivity;
import com.mhd.elemantary.adapter.MenuPagerAdapter;
import com.mhd.elemantary.common.MHDApplication;
import com.mhd.elemantary.constant.MHDConstants;
import com.mhd.elemantary.util.MHDDialogUtil;
import com.mhd.elemantary.util.MHDLog;
import com.mhd.elemantary.view.GlobalTabsView;

public class MainActivity extends BaseActivity {

    public ViewPager2 viewPager2;
    public MenuPagerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initialize(R.layout.activity_main);
//        setContentView(R.layout.activity_main);

        // 상단 status bar 를 감추고 그 영역까지 확대시킨다.
//        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_FULLSCREEN);

        // 배경. 컬러 애니메이션 등의 효과를 주기 위해
        final View background = findViewById(R.id.am_background_view);

        // viewpager
        viewPager2 = findViewById(R.id.am_view_pager);
        adapter = new MenuPagerAdapter(this);
        viewPager2.setAdapter(adapter);

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
//                    background.setBackgroundColor(colorBlue);
//                    background.setAlpha(1 - positionOffset);
                    MHDLog.e("dagian = 0", 1 - positionOffset);
                }
                else if(position == 1) {
//                    background.setBackgroundColor(colorPurple);
//                    background.setAlpha(positionOffset);
                    MHDLog.e("dagian = 1", positionOffset);
                }
            }
        };
        viewPager2.registerOnPageChangeCallback(callback);

        // viewpager 위에서 돌아가는 메뉴.
        MHDApplication.getInstance().getMHDSvcManager().setGlobalTabsView((GlobalTabsView) findViewById(R.id.am_snap_tabs));
        MHDApplication.getInstance().getMHDSvcManager().getGlobalTabsView().setUpWithMenuViewPager(viewPager2, MainActivity.this);

        // viewpager 에서 특정위치 view 초기 지정.
        viewPager2.setCurrentItem(0);
    }
    /**
     * onNewIntent
     * @Override
     */
    @Override
    protected void onNewIntent(Intent intent) {
        if(intent != null) {
            boolean isRefreshNeeded = intent.getBooleanExtra(MHDConstants.KEY_IS_MAIN_ACTIVITY_REPRESH_NEEDED, false);

            if(isRefreshNeeded) {
                // 서비스 메인화면 초기화 프로세스
            }
        }
        super.onNewIntent(intent);
    }

    /**
     * volley listner 의 success response 처리.
     * 필요한 곳에서 override 해서 구현.
     * 대부분의 Fragment 처리를 여기서?
     */
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
            if(nvApi.equals(getApplicationContext().getString(R.string.restapi_regist_post))){
                // regist post
                MHDLog.d(TAG, "networkResponseProcess nvMsg >>> " + nvMsg);

            }else if(nvApi.equals(getApplicationContext().getString(R.string.restapi_regist_post))){

            }
        }

        return true;
    }
}
