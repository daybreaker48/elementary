package com.mhd.stard.view;

import android.animation.ArgbEvaluator;
import android.content.Context;

import androidx.annotation.AttrRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.AlphaAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.mhd.stard.MainActivity;
import com.mhd.stard.R;
import com.mhd.stard.common.MHDApplication;
import com.mhd.stard.util.MHDLog;
import com.mhd.stard.util.Util;


public class GlobalTabsView extends FrameLayout {

    private final String TAG = getClass().getName();

    private ImageView mCenterImage;
    private ImageView mFirstImage;
    private ImageView mSecondImage;
    private ImageView mThirdImage;
    private ImageView mFourthImage;
    private ImageView mFifthImage;
    private ImageView mTopLeftImage, mTopRightImage, mTopRightImage_2;

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
    private int currentposition = 99;

    boolean isStart = true;
    boolean wDirection = false; // left

    private ViewPager verticalViewPager;
    private ViewPager writeViewPager, readViewPager;
    private ViewPager2 menuViewPager;
    private FragmentStatePagerAdapter writeFragmentPagerAdapter, statFragmentPagerAdapter, readFragmentPagerAdapter;


    public GlobalTabsView(@NonNull Context context) {
        this(context, null);
    }

    public GlobalTabsView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GlobalTabsView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);

//        if(MHDApplication.getInstance().getMHDSvcManager().getIsFirstStart() == true)
            init();
    }

    public void setUpWithVerticalViewPager(final ViewPager viewPager) {
        viewPager.addOnPageChangeListener(mVerticalPageChangeListener);
        verticalViewPager = viewPager;


//        mFirstImage.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(viewPager.getCurrentItem() != 0)
//                    viewPager.setCurrentItem(0);
//            }
//        });
//        mSecondImage.setOnClickListener(new OnClickListener() {00//            @Override
//            public void onClick(View v) {
//                if(viewPager.getCurrentItem() != 2)
//                    viewPager.setCurrentItem(2);
//            }
//        });

        // vertical viewpager??? ????????? ????????? ?????? ????????????
//        mCenterImage.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(vvpItem == 0){
//                    viewPager.setCurrentItem(1);
//                }
//                else {
//                    viewPager.setCurrentItem(0);
//                }
//            }
//        });
//        mThirdImage.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(viewPager.getCurrentItem() != 2)
//                    viewPager.setCurrentItem(2);
//            }
//        });
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

    public void setUpWithMenuViewPager(final ViewPager2 viewPager, final Context mContext) {
        ViewPager2.OnPageChangeCallback callback = new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position,
                                       float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);

                MHDLog.i("dagian", "position >>>>>>>>>>>>>> " + position);
                MHDLog.i("dagian", "positionOffset >>>>>>>>>>>>>> " + positionOffset);
                MHDLog.i("dagian", "positionOffsetPixels >>>>>>>>>>>>>> " + positionOffsetPixels);
                MHDLog.i("dagian", "MenuViewPager.getCurrentItem() >>>>>>>>>>>>>> " + menuViewPager.getCurrentItem());

                if (position == menuViewPager.getCurrentItem() && isStart) {
                    wDirection = true; // right
                    isStart = false;
                }
                setLRColorOne(positionOffset, position, menuViewPager.getCurrentItem());
                moveViewsFiveMenu(positionOffset, position, menuViewPager.getCurrentItem());
                if(position == 0) {
//                setLRColor(1 - positionOffset);
//                moveViews(1 - positionOffset);

                    //moveAndScaleCenter(1 - positionOffset);

//                mIndicator.setTranslationX((positionOffset - 1) * mIndicatorTranslationX);

                    //write ???????????? ??????????????????.
                    //mCenterImage.setImageAlpha((int) (1 - positionOffset));
                }
                else if(position == 1) {
//                setLRColor(positionOffset);
//                moveViews(positionOffset);

                    //moveAndScaleCenter(positionOffset);
//                mIndicator.setTranslationX(positionOffset * mIndicatorTranslationX);
                }
            }
        };

        menuViewPager = viewPager;
        menuViewPager.registerOnPageChangeCallback(callback);
        sideMenuSetOnClickListener(menuViewPager);

//        mCenterImage.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(viewPager.getCurrentItem() == 1)
//                    viewPager.setCurrentItem(0);
//                else if(viewPager.getCurrentItem() != 1)
//                    viewPager.setCurrentItem(1);
//            }
//        });

        mTopLeftImage.setOnClickListener(new OnClickListener() {//            @Override
            public void onClick(View v) {
//                if(menuViewPager.getCurrentItem() == 0) { // ??????
//                    Intent intent = new Intent(mContext, TutorialActivity.class);
//                    mContext.startActivity(intent);
//                } else if(menuViewPager.getCurrentItem() == 1) {// ?????????
//                    viewPager.setCurrentItem(2);
//                } else if(menuViewPager.getCurrentItem() == 2) {// ??????
//                    viewPager.setCurrentItem(2);
//                }
            }
        });
        mTopRightImage.setOnClickListener(new OnClickListener() {//            @Override
            public void onClick(View v) {
                // ??????, ?????????, ?????? ????????????.
                if(menuViewPager.getCurrentItem() == 0) { // ??????
                    // MainActivity ?????? ?????? function ??????.
                    ((MainActivity)MainActivity.context_main).startTodoRegist();
                } else if(menuViewPager.getCurrentItem() == 1) {// ?????????
                    ((MainActivity)MainActivity.context_main).startScheduleRegist();
                } else if(menuViewPager.getCurrentItem() == 2) {// ??????
                    ((MainActivity)MainActivity.context_main).startSelfRegist();
                }
            }
        });
        mTopRightImage_2.setOnClickListener(new OnClickListener() {//            @Override
            public void onClick(View v) {
                // show powermenu
                // MainActivity ?????? ?????? function ??????.
                ((MainActivity)MainActivity.context_main).showPMenu(menuViewPager.getCurrentItem());
            }
        });
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

    private void sideMenuSetOnClickListener(final ViewPager2 viewPager) {
        viewPager.setPageTransformer(new ViewPager2.PageTransformer() {
            @Override
            public void transformPage(@NonNull View page, float position) {
                return;
            }
        });
        mFirstImage.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (viewPager.getCurrentItem() != 0)
                    viewPager.setCurrentItem(0, false);
            }
        });
        mSecondImage.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (viewPager.getCurrentItem() != 1)
                    viewPager.setCurrentItem(1, false);
            }
        });
        mThirdImage.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (viewPager.getCurrentItem() != 2)
                    viewPager.setCurrentItem(2, false);
            }
        });
        mFourthImage.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (viewPager.getCurrentItem() != 3)
                    viewPager.setCurrentItem(3, false);
            }
        });
        mFifthImage.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (viewPager.getCurrentItem() != 4)
                    viewPager.setCurrentItem(4, false);
            }
        });
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_global_tabs, this, true);

        mCenterImage = (ImageView) findViewById(R.id.vst_center_image);
        mFirstImage = (ImageView) findViewById(R.id.vst_main_first_image);
        mSecondImage = (ImageView) findViewById(R.id.vst_main_second_image);
        mThirdImage = (ImageView) findViewById(R.id.vst_main_third_image);
        mFourthImage = (ImageView) findViewById(R.id.vst_main_fourth_image);
        mFifthImage = (ImageView) findViewById(R.id.vst_main_fifth_image);
        mTopLeftImage = (ImageView) findViewById(R.id.vst_left_image);
        mTopRightImage = (ImageView) findViewById(R.id.vst_right_image);
        mTopRightImage_2 = (ImageView) findViewById(R.id.vst_right_2_image);

        mIndicator = findViewById(R.id.vst_indicator);
        mIndicator.setAlpha(1);
        mIndicator.setTranslationX((-2) * mIndicatorTranslationX);
//        AlphaAnimation alpha = new AlphaAnimation(0.3f, 0.3f);
//        alpha.setDuration(0);
//        alpha.setFillAfter(true);
//        mSecondImage.startAnimation(alpha);
//        mThirdImage.startAnimation(alpha);
//        mFourthImage.startAnimation(alpha);
//        mFifthImage.startAnimation(alpha);
//        mSecondImage.getDrawable().setAlpha(120);
//        mSecondImage.getDrawable().setAlpha(120);
//        mThirdImage.getDrawable().setAlpha(120);
//        mFourthImage.getDrawable().setAlpha(120);
//        mFifthImage.getDrawable().setAlpha(120);

        mCenterColor = ContextCompat.getColor(getContext(), R.color.white);
        mSideColor = ContextCompat.getColor(getContext(), R.color.dark_gray);

        mArgEvaluator = new ArgbEvaluator();

        // ?????? ????????? 82
        mIndicatorTranslationX = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 82, getResources().getDisplayMetrics());

        mCenterImage.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                mEndViewTranslationX = (int) ((mCenterImage.getX() - mFirstImage.getX()) - mIndicatorTranslationX);
                mCenterImage.getViewTreeObserver().removeOnGlobalLayoutListener(this);

                mCenterTranslationY = (int) mCenterImage.getY() - 200;
            }
        });

        FrameLayout.LayoutParams mLayoutParams = (FrameLayout.LayoutParams) mCenterImage.getLayoutParams();
        mLayoutParams.topMargin = Util.getInstance().getStatusBarHeight(getContext());
        
        mCenterImage.setLayoutParams(mLayoutParams);

        mCenterImage.setVisibility(View.GONE);

        MHDApplication.getInstance().getMHDSvcManager().setIsFirstStart(false);
    }

    private void moveAndScaleCenter(float fractionFromCenter) {
        float scale = 1.3f + ((fractionFromCenter - 1) * .3f);
//        float scale = .7f + ((1 - fractionFromCenter) * .3f);
//        float scale = 1.0f;

        mCenterImage.setScaleX(scale);
        mCenterImage.setScaleY(scale);

        int tralation = (int) (fractionFromCenter * mCenterTranslationY);

        mCenterImage.setTranslationY(tralation);
        mThirdImage.setTranslationY(tralation);

//        mCenterImage.setAlpha(Math.abs(1 - (fractionFromCenter*2)));
        //mThirdImage.setAlpha(1 - fractionFromCenter);
    }

    private void moveViews(float fractionFromCenter) {
        mFirstImage.setTranslationX(fractionFromCenter * mEndViewTranslationX);
        mSecondImage.setTranslationX(-fractionFromCenter * mEndViewTranslationX);

        mIndicator.setAlpha(fractionFromCenter);
        mIndicator.setScaleX(fractionFromCenter);
    }
    private void moveViewsFiveMenu(float fractionFromCenter, int position, int startPosition) {
//        mIndicator.setAlpha(fractionFromCenter);
//        mIndicator.setScaleX(fractionFromCenter);
//        MHDLog.d("dagian", "currentposition >>>>>>>>>>>>>> " + fractionFromCenter + "/"+ position+"/"+ startPosition);
        if(currentposition == 99) currentposition = position;
        if(currentposition == position) {
            mIndicator.setTranslationX((fractionFromCenter + position - 2) * mIndicatorTranslationX);
//            MHDLog.d("dagian", "currentposition >>>>>>>>>>>>>> " + position);
        } else if (fractionFromCenter == 0) {
            mIndicator.setTranslationX((fractionFromCenter + position - 2) * mIndicatorTranslationX);
//            MHDLog.d("dagian", "currentposition >>>>>>>>>>>>>> " + position);
        } else {
            currentposition = position;
        }
    }
    /**
     * Center Menu control
     */
    private void setColor(float fractionFromCenter) {
        int color = (int) mArgEvaluator.evaluate(fractionFromCenter, mCenterColor, mSideColor);

//        mCenterImage.setColorFilter(color);
        mThirdImage.setColorFilter(color);
//        mFirstImage.setColorFilter(color);
//        mSecondImage.setColorFilter(color);

        // ?????????????????? ??????????????? ???????????? ?????????, ????????????????????? ????????? ?????????.
        // ???????????? background ??? setImageAlpha ??? ????????? ?????? ?????????.
        // ???????????? background ??? setAlpha ??? ????????? ?????????.
        mCenterImage.setImageAlpha((int)((1-fractionFromCenter)*255));
//        mCenterImage.setAlpha(1 - fractionFromCenter);
    }
    /**
     * Left/Right Menu control
     */
    private void setLRColor(float fractionFromCenter) {
        int color = (int) mArgEvaluator.evaluate(fractionFromCenter, mCenterColor, mSideColor);

//        mCenterImage.setColorFilter(color);
//        mThirdImage.setColorFilter(color);
//        mFirstImage.setColorFilter(color);
//        mSecondImage.setColorFilter(color);
        mFirstImage.setAlpha(1 - fractionFromCenter);
        mSecondImage.setAlpha(1 - fractionFromCenter);
        mThirdImage.setAlpha(1 - fractionFromCenter);
        mFourthImage.setAlpha(1 - fractionFromCenter);
        mFifthImage.setAlpha(1 - fractionFromCenter);

        // ?????????????????? ??????????????? ???????????? ?????????, ????????????????????? ????????? ?????????.
        // ???????????? background ??? setImageAlpha ??? ????????? ?????? ?????????.
        // ???????????? background ??? setAlpha ??? ????????? ?????????.
//        mCenterImage.setImageAlpha((int)((1-fractionFromCenter)*255));
//        mCenterImage.setAlpha(1 - fractionFromCenter);
    }
    /**
     * Left/Right Menu control 2(one of menu)
     */
    private void setLRColorOne(float fractionFromCenter, int position, int startPosition) {
        int color = (int) mArgEvaluator.evaluate(1-fractionFromCenter, mCenterColor, mSideColor);

//        mCenterImage.setColorFilter(color);
//        mFirstImage.setColorFilter(color);
//        mSecondImage.setColorFilter(color);
//        mThirdImage.setColorFilter(color);
//        mFourthImage.setColorFilter(color);
//        mFifthImage.setColorFilter(color);

        // menuViewPager.getCurrentItem() ??? ????????? position, ?????? position ??? ????????????.
        // positionOffset ??? ?????? ???????????? ????????? ??????, ???????????? ?????? ??????.
        // ?????????, ???????????? ???????????? ???????????? position + positionOffset = MenuViewPager.getCurrentItem()  ??????.
        // position + fractionFromCenter = 1 ===> position 0?????? 1???
        // setAlpha = 1 ?????? ??????. 0 ?????? ??????.
//        float selectedMenu = 1/3 + (1 - fractionFromCenter);
//        float previousMenu = 1/3 + (fractionFromCenter*3)/3;
        float selectedMenu = (1 - fractionFromCenter) + 0.3f;
        float previousMenu = 0.3f + fractionFromCenter;
//        if (wDirection) {
//            selectedMenu = 1 - fractionFromCenter;
//            previousMenu = fractionFromCenter/3;
//        }
//        MHDLog.d("dagian", "selectedMenu >>>>>>>>>>>>>> " + selectedMenu + "/" + previousMenu);
//        MHDLog.d("dagian", "isStart >>>>>>>>>>>>>> " + isStart);
//        MHDLog.d("dagian", "wDirection >>>>>>>>>>>>>> " + wDirection);
        if(fractionFromCenter > 0) {
            switch (position) {
                case 0: // 0???1
                    mFirstImage.setAlpha(selectedMenu);
                    mSecondImage.setAlpha(previousMenu);
                    break;
                case 1: // 1???2, 0???1 ??? ???????????? ???.
                    mSecondImage.setAlpha(selectedMenu);
                    mThirdImage.setAlpha(previousMenu);
//                    mFourthImage.setAlpha(1 - fractionFromCenter);
//                    mFifthImage.setAlpha(1 - fractionFromCenter);
                    break;
                case 2: // 2???3, 1???2 ??? ???????????? ???.
                    mThirdImage.setAlpha(selectedMenu);
                    mFourthImage.setAlpha(previousMenu);
                    break;
                case 3: // 3???4, 2???3 ??? ???????????? ???.
                    mFourthImage.setAlpha(selectedMenu);
                    mFifthImage.setAlpha(previousMenu);
                    break;
                case 4: // 4???5, 3???4 ??? ???????????? ???.
                    mFifthImage.setAlpha(selectedMenu);
                    break;
            }
        } else {
            switch (position) {
                case 0: // 0???1
                    mTopRightImage.setVisibility(View.VISIBLE);
                    mTopRightImage_2.setVisibility(View.VISIBLE);
                    mFirstImage.setAlpha(selectedMenu);
                    mSecondImage.setAlpha(previousMenu);
                    mThirdImage.setAlpha(previousMenu);
                    mFourthImage.setAlpha(previousMenu);
                    mFifthImage.setAlpha(previousMenu);
                    break;
                case 1: // 1???2, 0???1 ??? ???????????? ???.
                    mTopRightImage.setVisibility(View.VISIBLE);
                    mTopRightImage_2.setVisibility(View.VISIBLE);
                    mFirstImage.setAlpha(previousMenu);
                    mSecondImage.setAlpha(selectedMenu);
                    mThirdImage.setAlpha(previousMenu);
                    mFourthImage.setAlpha(previousMenu);
                    mFifthImage.setAlpha(previousMenu);
//                    mFourthImage.setAlpha(1 - fractionFromCenter);
//                    mFifthImage.setAlpha(1 - fractionFromCenter);
                    break;
                case 2: // 2???3, 1???2 ??? ???????????? ???.
                    mTopRightImage_2.setVisibility(View.VISIBLE);
                    mTopRightImage.setVisibility(View.VISIBLE);
                    mFirstImage.setAlpha(previousMenu);
                    mSecondImage.setAlpha(previousMenu);
                    mThirdImage.setAlpha(selectedMenu);
                    mFourthImage.setAlpha(previousMenu);
                    mFifthImage.setAlpha(previousMenu);
                    break;
                case 3: // 3???4, 2???3 ??? ???????????? ???.
                    mTopRightImage.setVisibility(View.GONE);
                    mTopRightImage_2.setVisibility(View.VISIBLE);
                    mFirstImage.setAlpha(previousMenu);
                    mSecondImage.setAlpha(previousMenu);
                    mThirdImage.setAlpha(previousMenu);
                    mFourthImage.setAlpha(selectedMenu);
                    mFifthImage.setAlpha(previousMenu);
                    break;
                case 4: // 4???5, 3???4 ??? ???????????? ???.
                    mTopRightImage.setVisibility(View.INVISIBLE);
                    mTopRightImage_2.setVisibility(View.INVISIBLE);
                    mFirstImage.setAlpha(previousMenu);
                    mSecondImage.setAlpha(previousMenu);
                    mThirdImage.setAlpha(previousMenu);
                    mFourthImage.setAlpha(previousMenu);
                    mFifthImage.setAlpha(selectedMenu);
                    break;
            }
        }
//        mFirstImage.setAlpha(1 - fractionFromCenter);
//        mSecondImage.setAlpha(1 - fractionFromCenter);
//        mThirdImage.setAlpha(1 - fractionFromCenter);
//        mFourthImage.setAlpha(1 - fractionFromCenter);
//        mFifthImage.setAlpha(1 - fractionFromCenter);

        // ?????????????????? ??????????????? ???????????? ?????????, ????????????????????? ????????? ?????????.
        // ???????????? background ??? setImageAlpha ??? ????????? ?????? ?????????.
        // ???????????? background ??? setAlpha ??? ????????? ?????????.
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

                //write ???????????? ??????????????????.
                //mCenterImage.setImageAlpha((int) (1 - positionOffset));
            }
            else if(position == 1) {
                // positionOffsetPixels ?????? ?????? ??????.
                // ???????????? ?????? ????????? ?????? position ?????? ??????.
                // ????????? ?????? ????????? ?????? position ?????? ??????.
                // ????????? ????????? ??????????????? 0.
                // ???????????? ?????? ????????? ??????????????? ????????? ???????????? ?????????????????? ????????? ??????????????? ???????????? ???.
                if(checkDirection == 0) {
                    if (firstOffset == 0) {
                        firstOffset = positionOffsetPixels;
                    } else {
                        secondOffset = positionOffsetPixels;
                        if(vvpItem == 1) {
                            if (secondOffset < firstOffset) { // ?????? position ?????? ??????
                                checkDirection = 1;
                            } else { // ?????? position ?????? ??????
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
                //??????????????? paging ??? ????????? ????????? Item??? ??????.
                //onPageScrollStateChanged ??? ????????? ??????????????? paging??? ????????? ?????? state ?????? ?????????
                //????????? ????????? ?????????.
                vvpItem = verticalViewPager.getCurrentItem();
            }
        }

        @Override
        public void onPageSelected(int position) {

        }

        @Override
        public void onPageScrollStateChanged(int state) {
            //verticalPager Item??? ?????? ??????????????? ????????? ????????? ????????? ??????.
            if(verticalViewPager.getCurrentItem() == 0){
                //Write
                mFirstImage.setImageResource(R.drawable.gallery);
                mSecondImage.setImageResource(R.drawable.camera);
                mThirdImage.setImageResource(R.drawable.read);

                // viewpager2??? ????????? ????????? ???????????? ??????????????? ?????? ??????
//                sideMenuSetOnClickListener(writeViewPager);
            }
            else if(verticalViewPager.getCurrentItem() == 1){
                //Stat
                mFirstImage.setImageResource(R.drawable.todo);
                mSecondImage.setImageResource(R.drawable.schedule);
                mThirdImage.setImageResource(R.drawable.self);
                mFourthImage.setImageResource(R.drawable.summary);
                mFifthImage.setImageResource(R.drawable.setting);

                sideMenuSetOnClickListener(menuViewPager);
            }
            else{
                //Read
                mFirstImage.setImageResource(R.drawable.pass);
                mSecondImage.setImageResource(R.drawable.good);
                mThirdImage.setImageResource(R.drawable.bad);

                // viewpager2??? ????????? ????????? ???????????? ??????????????? ?????? ??????
//                sideMenuSetOnClickListener(readViewPager);
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

//            if(position == 0) {
//                setLRColor(1 - positionOffset);
//                moveViews(1 - positionOffset);
//
//                //moveAndScaleCenter(1 - positionOffset);
//
//                mIndicator.setTranslationX((positionOffset - 1) * mIndicatorTranslationX);
//
//                //write ???????????? ??????????????????.
//                //mCenterImage.setImageAlpha((int) (1 - positionOffset));
//            }
//            else if(position == 1) {
//                setLRColor(positionOffset);
//                moveViews(positionOffset);
//
//                //moveAndScaleCenter(positionOffset);
//                mIndicator.setTranslationX(positionOffset * mIndicatorTranslationX);
//            }
            setLRColorOne(positionOffset, position, writeViewPager.getCurrentItem());
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
    private ViewPager.OnPageChangeListener mMenuPageChangeListener = new ViewPager.OnPageChangeListener(){
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//            MHDLog.i("dagian", "position >>>>>>>>>>>>>> " + position);
//            MHDLog.i("dagian", "positionOffset >>>>>>>>>>>>>> " + positionOffset);
//            MHDLog.i("dagian", "positionOffsetPixels >>>>>>>>>>>>>> " + positionOffsetPixels);
//            MHDLog.i("dagian", "MenuViewPager.getCurrentItem() >>>>>>>>>>>>>> " + menuViewPager.getCurrentItem());

            if (position == menuViewPager.getCurrentItem() && isStart) {
                wDirection = true; // right
                isStart = false;
            }
            setLRColorOne(positionOffset, position, menuViewPager.getCurrentItem());
            moveViewsFiveMenu(positionOffset, position, menuViewPager.getCurrentItem());
            if(position == 0) {//                setLRColor(1 - positionOffset);
//                moveViews(1 - positionOffset);

                //
            moveAndScaleCenter(1 - positionOffset);

//                mIndicator.setTranslationX((positionOffset - 1) * mIndicatorTranslationX);

                //write ???????????? ??????????????????.
                //mCenterImage.setImageAlpha((int) (1 - positionOffset));
            }
            else if(position == 1) {
//                setLRColor(positionOffset);
//                moveViews(positionOffset);

                //moveAndScaleCenter(positionOffset);
//                mIndicator.setTranslationX(positionOffset * mIndicatorTranslationX);
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

//            if(position == 0) {
//                setLRColor(1 - positionOffset);
//                moveViews(1 - positionOffset);
//
//                //moveAndScaleCenter(1 - positionOffset);
//
//                mIndicator.setTranslationX((positionOffset - 1) * mIndicatorTranslationX);
//
//                //write ???????????? ??????????????????.
//                //mCenterImage.setImageAlpha((int) (1 - positionOffset));
//            }
//            else if(position == 1) {
//                setLRColor(positionOffset);
//                moveViews(positionOffset);
//
//                //moveAndScaleCenter(positionOffset);
//                mIndicator.setTranslationX(positionOffset * mIndicatorTranslationX);
//            }
            setLRColorOne(positionOffset, position, readViewPager.getCurrentItem());
        }

        @Override
        public void onPageSelected(int position) {

        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

}