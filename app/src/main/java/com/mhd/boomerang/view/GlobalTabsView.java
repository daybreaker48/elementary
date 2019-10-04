package com.mhd.boomerang.view;

import android.animation.ArgbEvaluator;
import android.content.Context;
import android.content.res.Configuration;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.mhd.boomerang.R;
import com.mhd.boomerang.adapter.StatPagerAdapter;
import com.mhd.boomerang.adapter.WritePagerAdapter;
import com.mhd.boomerang.common.MHDApplication;
import com.mhd.boomerang.util.MHDLog;
import com.mhd.boomerang.util.Util;


public class GlobalTabsView extends FrameLayout {

    private final String TAG = getClass().getName();

    private ImageView mCenterImage;
    private ImageView mStartImage;
    private ImageView mEndImage;
    private ImageView mBottomImage;

    private View mIndicator;

    private ArgbEvaluator mArgEvaluator;
    private int mCenterColor;
    private int mSideColor;

    private int mEndViewTranslationX;
    private int mIndicatorTranslationX;
    private int mCenterTranslationY;

    private int firstOffset = 0;
    private int secondOffset = 0;
    private int checkDirection = 0;
    private int vvpItem = 0;

    private ViewPager verticalViewPager;
    private ViewPager writeViewPager, statViewPager, readViewPager;


    public GlobalTabsView(@NonNull Context context) {
        this(context, null);
    }

    public GlobalTabsView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GlobalTabsView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        if(MHDApplication.getInstance().getMHDSvcManager().getIsFirstStart() == true)
            init();
    }

    public void setUpWithVerticalViewPager(final ViewPager viewPager) {
        viewPager.addOnPageChangeListener(mVerticalPageChangeListener);
        verticalViewPager = viewPager;


//        mStartImage.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(viewPager.getCurrentItem() != 0)
//                    viewPager.setCurrentItem(0);
//            }
//        });
//        mEndImage.setOnClickListener(new OnClickListener() {00//            @Override
//            public void onClick(View v) {
//                if(viewPager.getCurrentItem() != 2)
//                    viewPager.setCurrentItem(2);
//            }
//        });
        mCenterImage.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(vvpItem == 0){
                    viewPager.setCurrentItem(1);
                }
                else {
                    viewPager.setCurrentItem(0);
                }
            }
        });
        mBottomImage.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(viewPager.getCurrentItem() != 2)
                    viewPager.setCurrentItem(2);
            }
        });
    }

    public void setUpWithWriteViewPager(final ViewPager viewPager) {
        viewPager.addOnPageChangeListener(mWritePageChangeListener);
        writeViewPager = viewPager;

//        mCenterImage.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(viewPager.getCurrentItem() == 1)
//                    viewPager.setCurrentItem(0);
//                else if(viewPager.getCurrentItem() != 1)
//                    viewPager.setCurrentItem(1);
//            }
//        });
    }

    public void setUpWithStatViewPager(final ViewPager viewPager) {
        viewPager.addOnPageChangeListener(mStatPageChangeListener);
        statViewPager = viewPager;
        sideMenuSetOnClickListener(statViewPager);

//        mCenterImage.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(viewPager.getCurrentItem() == 1)
//                    viewPager.setCurrentItem(0);
//                else if(viewPager.getCurrentItem() != 1)
//                    viewPager.setCurrentItem(1);
//            }
//        });
    }

    public void setUpWithReadViewPager(final ViewPager viewPager) {
        viewPager.addOnPageChangeListener(mReadPageChangeListener);
        readViewPager = viewPager;

//        mCenterImage.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(viewPager.getCurrentItem() == 1)
//                    viewPager.setCurrentItem(0);
//                else if(viewPager.getCurrentItem() != 1)
//                    viewPager.setCurrentItem(1);
//            }
//        });
    }

    private void sideMenuSetOnClickListener(final ViewPager viewPager) {
        mStartImage.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (viewPager.getCurrentItem() != 0)
                    viewPager.setCurrentItem(0);
            }
        });
        mEndImage.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (viewPager.getCurrentItem() != 2)
                    viewPager.setCurrentItem(2);
            }
        });
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_global_tabs, this, true);

        mCenterImage = (ImageView) findViewById(R.id.vst_center_image);
        mStartImage = (ImageView) findViewById(R.id.vst_start_image);
        mEndImage = (ImageView) findViewById(R.id.vst_end_image);
        mBottomImage = (ImageView) findViewById(R.id.vst_bottom_image);

        mIndicator = findViewById(R.id.vst_indicator);
        mIndicator.setAlpha(0);

        mCenterColor = ContextCompat.getColor(getContext(), R.color.white);
        mSideColor = ContextCompat.getColor(getContext(), R.color.dark_grey);

        mArgEvaluator = new ArgbEvaluator();

        mIndicatorTranslationX = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 80, getResources().getDisplayMetrics());

        mBottomImage.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                mEndViewTranslationX = (int) ((mBottomImage.getX() - mStartImage.getX()) - mIndicatorTranslationX);
                mBottomImage.getViewTreeObserver().removeOnGlobalLayoutListener(this);

                mCenterTranslationY = (int) mBottomImage.getY() - 200;
            }
        });

        FrameLayout.LayoutParams mLayoutParams = (FrameLayout.LayoutParams) mCenterImage.getLayoutParams();
        mLayoutParams.topMargin = Util.getInstance().getStatusBarHeight(getContext());
        mCenterImage.setLayoutParams(mLayoutParams);
    }

    private void moveAndScaleCenter(float fractionFromCenter) {
        float scale = 1.3f + ((fractionFromCenter - 1) * .3f);
//        float scale = .7f + ((1 - fractionFromCenter) * .3f);
//        float scale = 1.0f;

        mCenterImage.setScaleX(scale);
        mCenterImage.setScaleY(scale);

        int tralation = (int) (fractionFromCenter * mCenterTranslationY);

        mCenterImage.setTranslationY(tralation);
        mBottomImage.setTranslationY(tralation);

//        mCenterImage.setAlpha(Math.abs(1 - (fractionFromCenter*2)));
        //mBottomImage.setAlpha(1 - fractionFromCenter);
    }

    private void moveViews(float fractionFromCenter) {
        mStartImage.setTranslationX(fractionFromCenter * mEndViewTranslationX);
        mEndImage.setTranslationX(-fractionFromCenter * mEndViewTranslationX);

        mIndicator.setAlpha(fractionFromCenter);
        mIndicator.setScaleX(fractionFromCenter);
    }
    /**
     * Center Menu control
     */
    private void setColor(float fractionFromCenter) {
        int color = (int) mArgEvaluator.evaluate(fractionFromCenter, mCenterColor, mSideColor);

//        mCenterImage.setColorFilter(color);
        mBottomImage.setColorFilter(color);
//        mStartImage.setColorFilter(color);
//        mEndImage.setColorFilter(color);

        // 작성이미지는 작성화면이 아닐때만 보이고, 작성화면에서는 보이지 않는다.
        // 이미지의 background 는 setImageAlpha 의 영향을 받지 않는다.
        // 이미지의 background 는 setAlpha 의 영향은 받는다.
        mCenterImage.setImageAlpha((int)((1-fractionFromCenter)*255));
//        mCenterImage.setAlpha(1 - fractionFromCenter);
    }
    /**
     * Left/Right Menu control
     */
    private void setLRColor(float fractionFromCenter) {
        int color = (int) mArgEvaluator.evaluate(fractionFromCenter, mCenterColor, mSideColor);

//        mCenterImage.setColorFilter(color);
//        mBottomImage.setColorFilter(color);
        mStartImage.setColorFilter(color);
        mEndImage.setColorFilter(color);
        mBottomImage.setAlpha(1 - fractionFromCenter);

        // 작성이미지는 작성화면이 아닐때만 보이고, 작성화면에서는 보이지 않는다.
        // 이미지의 background 는 setImageAlpha 의 영향을 받지 않는다.
        // 이미지의 background 는 setAlpha 의 영향은 받는다.
//        mCenterImage.setImageAlpha((int)((1-fractionFromCenter)*255));
//        mCenterImage.setAlpha(1 - fractionFromCenter);
    }
    /**
     * Main vertical pager listener
     */
    private ViewPager.OnPageChangeListener mVerticalPageChangeListener = new ViewPager.OnPageChangeListener(){
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//            MHDLog.d("dagian", "position >>>>>>>>>>>>>> " + position);
//            MHDLog.d("dagian", "verticalViewPager.getCurrentItem() >>>>>>>>>>>>>> " + verticalViewPager.getCurrentItem());

            if(position == 0) {
                setColor(1 - positionOffset);
                //moveViews(1 - positionOffset);

                moveAndScaleCenter(1 - positionOffset);

                //mIndicator.setTranslationX((positionOffset - 1) * mIndicatorTranslationX);

                //write 이미지를 투명처리한다.
                //mCenterImage.setImageAlpha((int) (1 - positionOffset));
            }
            else if(position == 1) {
                // positionOffsetPixels 으로 방향 판단.
                // 작아지고 있는 중에는 이전 position 으로 이동.
                // 커지고 있는 중에는 다음 position 으로 이동.
                // 이동이 멈추면 최종적으로 0.
                // 여기서는 최초 시도를 어느쪽으로 했는지 판단해서 애니메이션을 줄건지 안줄건지를 판단해야 함.
                if(checkDirection == 0) {
                    if (firstOffset == 0) {
                        firstOffset = positionOffsetPixels;
                    } else {
                        secondOffset = positionOffsetPixels;
                        if(vvpItem == 1) {
                            if (secondOffset < firstOffset) { // 이전 position 으로 이동
                                checkDirection = 1;
                            } else { // 다음 position 으로 이동
                                checkDirection = 2;
                            }
                        }
                    }
                }

                if(checkDirection == 1){
                    setColor(positionOffset);
                    //moveViews(positionOffset);

                    moveAndScaleCenter(positionOffset);
                    //mIndicator.setTranslationX(positionOffset * mIndicatorTranslationX);
                }
            }

            if(positionOffsetPixels == 0){
                firstOffset = 0;
                secondOffset = 0;
                checkDirection = 0;
                //최종적으로 paging 이 멈추면 선택된 Item을 저장.
                //onPageScrollStateChanged 에 이것을 넣어봤더니 paging이 멈추기 전에 state 값이 변해서
                //제대로 작동을 안했다.
                vvpItem = verticalViewPager.getCurrentItem();
            }
        }

        @Override
        public void onPageSelected(int position) {

        }

        @Override
        public void onPageScrollStateChanged(int state) {
            //verticalPager Item에 따라 좌우메뉴의 형태와 기능을 다르게 처리.
            if(verticalViewPager.getCurrentItem() == 0){
                //Write
                mStartImage.setImageResource(R.drawable.gallery);
                mEndImage.setImageResource(R.drawable.camera);
                mBottomImage.setImageResource(R.drawable.read);

                sideMenuSetOnClickListener(writeViewPager);
            }
            else if(verticalViewPager.getCurrentItem() == 1){
                //Stat
                mStartImage.setImageResource(R.drawable.returned);
                mEndImage.setImageResource(R.drawable.returning);
                mBottomImage.setImageResource(R.drawable.read);

                sideMenuSetOnClickListener(statViewPager);
            }
            else{
                //Read
                mStartImage.setImageResource(R.drawable.pass);
                mEndImage.setImageResource(R.drawable.good);
                mBottomImage.setImageResource(R.drawable.bad);

                sideMenuSetOnClickListener(readViewPager);
            }
        }
    };
    /**
     * Write horizontal pager listener
     */
    private ViewPager.OnPageChangeListener mWritePageChangeListener = new ViewPager.OnPageChangeListener(){
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//            MHDLog.d("dagian", "position >>>>>>>>>>>>>> " + position);
//            MHDLog.d("dagian", "verticalViewPager.getCurrentItem() >>>>>>>>>>>>>> " + verticalViewPager.getCurrentItem());

            if(position == 0) {
                setLRColor(1 - positionOffset);
                moveViews(1 - positionOffset);

                //moveAndScaleCenter(1 - positionOffset);

                mIndicator.setTranslationX((positionOffset - 1) * mIndicatorTranslationX);

                //write 이미지를 투명처리한다.
                //mCenterImage.setImageAlpha((int) (1 - positionOffset));
            }
            else if(position == 1) {
                setLRColor(positionOffset);
                moveViews(positionOffset);

                //moveAndScaleCenter(positionOffset);
                mIndicator.setTranslationX(positionOffset * mIndicatorTranslationX);
            }
        }

        @Override
        public void onPageSelected(int position) {

        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };
    /**
     * Stat horizontal pager listener
     */
    private ViewPager.OnPageChangeListener mStatPageChangeListener = new ViewPager.OnPageChangeListener(){
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//            MHDLog.d("dagian", "position >>>>>>>>>>>>>> " + position);
//            MHDLog.d("dagian", "verticalViewPager.getCurrentItem() >>>>>>>>>>>>>> " + verticalViewPager.getCurrentItem());

            if(position == 0) {
                setLRColor(1 - positionOffset);
                moveViews(1 - positionOffset);

                //moveAndScaleCenter(1 - positionOffset);

                mIndicator.setTranslationX((positionOffset - 1) * mIndicatorTranslationX);

                //write 이미지를 투명처리한다.
                //mCenterImage.setImageAlpha((int) (1 - positionOffset));
            }
            else if(position == 1) {
                setLRColor(positionOffset);
                moveViews(positionOffset);

                //moveAndScaleCenter(positionOffset);
                mIndicator.setTranslationX(positionOffset * mIndicatorTranslationX);
            }
        }

        @Override
        public void onPageSelected(int position) {

        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };
    /**
     * Read horizontal pager listener
     */
    private ViewPager.OnPageChangeListener mReadPageChangeListener = new ViewPager.OnPageChangeListener(){
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//            MHDLog.d("dagian", "position >>>>>>>>>>>>>> " + position);
//            MHDLog.d("dagian", "verticalViewPager.getCurrentItem() >>>>>>>>>>>>>> " + verticalViewPager.getCurrentItem());

            if(position == 0) {
                setLRColor(1 - positionOffset);
                moveViews(1 - positionOffset);

                //moveAndScaleCenter(1 - positionOffset);

                mIndicator.setTranslationX((positionOffset - 1) * mIndicatorTranslationX);

                //write 이미지를 투명처리한다.
                //mCenterImage.setImageAlpha((int) (1 - positionOffset));
            }
            else if(position == 1) {
                setLRColor(positionOffset);
                moveViews(positionOffset);

                //moveAndScaleCenter(positionOffset);
                mIndicator.setTranslationX(positionOffset * mIndicatorTranslationX);
            }
        }

        @Override
        public void onPageSelected(int position) {

        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };
}