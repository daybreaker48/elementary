package com.mhd.elemantary.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

import com.mhd.elemantary.R;
import com.mhd.elemantary.adapter.WritePagerAdapter;
import com.mhd.elemantary.common.MHDApplication;
import com.mhd.elemantary.util.MHDLog;
import com.mhd.elemantary.view.CustomViewPager;


public class WriteFragment extends BaseFragment {

    public CustomViewPager viewPagerTop;
    public WritePagerAdapter adapterTop;

    public static WriteFragment create() {
        return new WriteFragment();
    }

    @Override
    public int getLayoutResId() {
        return R.layout.fragment_write;
    }

    @Override
    public void inOnCreateView(View root, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // 배경. 컬러 애니메이션 등의 효과를 주기 위해
        final View background = root.findViewById(R.id.am_background_view_horizontal);

        // viewpager
        viewPagerTop = (CustomViewPager) root.findViewById(R.id.am_write_view_pager_horizontal);
        adapterTop = new WritePagerAdapter(this.getChildFragmentManager());
        viewPagerTop.setAdapter(adapterTop);
        MHDLog.d("dagian", "test >>>>>>>>>>>>>> " + adapterTop.getCount());

        // read 좌우 메뉴 제어
        MHDApplication.getInstance().getMHDSvcManager().getGlobalTabsView().setUpWithWriteViewPager(viewPagerTop);

        // UI 에 필요한 컬러코드 값
        final int colorBlue = ContextCompat.getColor(root.getContext(), R.color.light_blue);
        final int colorPurple = ContextCompat.getColor(root.getContext(), R.color.light_purple);

        // viewpager 에서 특정위치 view 초기 지정.
        viewPagerTop.setCurrentItem(1);

        // viewpager 이동에 따른 컬러, 투명도 변경 애니메이션 처리.
        viewPagerTop.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if(position == 0) {
                    background.setBackgroundColor(colorBlue);
                    background.setAlpha(1 - positionOffset);
                }
                else if(position == 1) {
                    background.setBackgroundColor(colorPurple);
                    background.setAlpha(positionOffset);
                }
            }

            @Override
            public void onPageSelected(int position) {
                MHDApplication.getInstance().getMHDSvcManager().setCurrentWriteIndex(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    @Override
    public void batchFunction(String api) {
//        if(api.equals(getString(R.string.api_editor_clear))) {
//            // editor 내용 초기화.
//            editor.clearAllContents();
//        }
    }
}