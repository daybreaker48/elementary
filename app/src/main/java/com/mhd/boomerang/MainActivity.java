package com.mhd.boomerang;

import android.content.Intent;
import android.os.PersistableBundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.View;

import com.mhd.boomerang.activity.BaseActivity;
import com.mhd.boomerang.adapter.MainPagerAdapter;
import com.mhd.boomerang.common.MHDApplication;
import com.mhd.boomerang.constant.MHDConstants;
import com.mhd.boomerang.view.GlobalTabsView;
import com.mhd.boomerang.view.VerticalViewPager;

public class MainActivity extends BaseActivity {

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
        VerticalViewPager viewPager = (VerticalViewPager) findViewById(R.id.am_view_pager);
        MainPagerAdapter adapter = new MainPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);

        // viewpager 위에서 돌아가는 메뉴.
        MHDApplication.getInstance().getMHDSvcManager().setGlobalTabsView((GlobalTabsView) findViewById(R.id.am_snap_tabs));
        MHDApplication.getInstance().getMHDSvcManager().getGlobalTabsView().setUpWithVerticalViewPager(viewPager);

        // viewpager 에서 특정위치 view 초기 지정.
        viewPager.setCurrentItem(1);

        // UI 에 필요한 컬러코드 값
        final int colorBlue = ContextCompat.getColor(this, R.color.light_blue);
        final int colorPurple = ContextCompat.getColor(this, R.color.light_purple);

        // viewpager 이동에 따른 컬러, 투명도 변경 애니메이션 처리.
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if(position == 0) {
                    background.setBackgroundColor(colorBlue);
                    background.setAlpha(1 - positionOffset);
//                    MHDLog.e("dagian = 0", 1 - positionOffset);
                }
                else if(position == 1) {
                    background.setBackgroundColor(colorPurple);
                    background.setAlpha(positionOffset);
//                    MHDLog.e("dagian = 1", positionOffset);
                }
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
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
}
