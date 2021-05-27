package com.mhd.elemantary.view;

import android.content.Context;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewConfiguration;

import com.mhd.elemantary.common.MHDApplication;
import com.mhd.elemantary.transforms.DefaultTransformer;


public class VerticalViewPager extends ViewPager {

    private float initialX;
    private float initialY;
    private int mActivePointerId;
    private float initialXPress;
    private float initialYPress;

    private final ViewConfiguration configuration;
    private int mTouchSlop;


    public VerticalViewPager(Context context) {
        this(context, null);
    }

    public VerticalViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        configuration = ViewConfiguration.get(context);
        mTouchSlop = configuration.getScaledPagingTouchSlop();

        setPageTransformer(false, new DefaultTransformer(DefaultTransformer.TransformType.DEFAULT));
    }

    private MotionEvent swapTouchEvent(MotionEvent event) {
        float width = getWidth();
        float height = getHeight();

        float swappedX = (event.getY() / height) * width;
        float swappedY = (event.getX() / width) * height;

        event.setLocation(swappedX, swappedY);

        return event;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        boolean intercept = false;
        final int action = event.getAction() & MotionEventCompat.ACTION_MASK;

        if(this.getCurrentItem() == 0) {
            if (MHDApplication.getInstance().getMHDSvcManager().getCurrentWriteIndex() == 1) {
                // 작성화면(좌:카메라, 우:갤러리, 하:Main)
                switch (action) {
                    case MotionEvent.ACTION_DOWN: {
                        mActivePointerId = event.getPointerId(0);
                        initialXPress = event.getX();
                        initialYPress = event.getY();

                        break;
                    }
                    case MotionEvent.ACTION_MOVE: {
                        final int pointerIndex = event.findPointerIndex(mActivePointerId);
                        if (pointerIndex < 0 || pointerIndex > 0) {
                            break;
                        }

                        final float xMove = event.getX(pointerIndex);
                        final float yMove = event.getY(pointerIndex);

                                    //calculate distance moved
                        final float dx = xMove - initialXPress;
                        final float dy = yMove - initialYPress;
                        final float yDiff = Math.abs(dy);

                        if (yDiff > mTouchSlop) {
                            // vertical drag confirm
                            intercept = true;
                        }
                        break;
                    }
//                    case MotionEvent.ACTION_UP: {
//                        intercept = false;
//                        break;
//                    }
//                    case MotionEvent.ACTION_CANCEL:
//                        intercept = false;
//                        break;
                }

                super.onInterceptTouchEvent(swapTouchEvent(event));
                swapTouchEvent(event);

                return intercept;
            } else {
                // sub pager 작동.
                return false;
            }
        }else if(this.getCurrentItem() == 1){
            if(MHDApplication.getInstance().getMHDSvcManager().getCurrentStatIndex() == 1) {
                // 통계화면(좌:수신완료, 우:수신중, 상:Write, 하:Read)
                switch (action) {
                    case MotionEvent.ACTION_DOWN: {
                        mActivePointerId = event.getPointerId(0);
                        initialXPress = event.getX();
                        initialYPress = event.getY();

                        break;
                    }
                    case MotionEvent.ACTION_MOVE: {
                        final int pointerIndex = event.findPointerIndex(mActivePointerId);
                        if (pointerIndex < 0 || pointerIndex > 0) {
                            break;
                        }

                        final float xMove = event.getX(pointerIndex);
                        final float yMove = event.getY(pointerIndex);

                        //calculate distance moved
                        final float dx = xMove - initialXPress;
                        final float dy = yMove - initialYPress;
                        final float yDiff = Math.abs(dy);

                        if (yDiff > mTouchSlop) {
                            // vertical drag confirm
                            intercept = true;
                        }
                        break;
                    }
//                    case MotionEvent.ACTION_UP: {
//                        intercept = false;
//                        break;
//                    }
//                    case MotionEvent.ACTION_CANCEL:
//                        intercept = false;
//                        break;
                }

                super.onInterceptTouchEvent(swapTouchEvent(event));
                swapTouchEvent(event);

                return intercept;
            }else{
                // sub pager 작동.
                return false;
            }
        }else{
            if (MHDApplication.getInstance().getMHDSvcManager().getCurrentReadIndex() == 1) {
                // 조회화면(좌:패스,다음글, 우:LIKE,다음글, 상:Main)
                switch (action) {
                    case MotionEvent.ACTION_DOWN: {
                        mActivePointerId = event.getPointerId(0);
                        initialXPress = event.getX();
                        initialYPress = event.getY();

                        break;
                    }
                    case MotionEvent.ACTION_MOVE: {
                        final int pointerIndex = event.findPointerIndex(mActivePointerId);
                        if (pointerIndex < 0 || pointerIndex > 0) {
                            break;
                        }

                        final float xMove = event.getX(pointerIndex);
                        final float yMove = event.getY(pointerIndex);

                        //calculate distance moved
                        final float dx = xMove - initialXPress;
                        final float dy = yMove - initialYPress;
                        final float yDiff = Math.abs(dy);

                        if (yDiff > mTouchSlop) {
                            // vertical drag confirm
                            intercept = true;
                        }
                        break;
                    }
//                    case MotionEvent.ACTION_UP: {
//                        intercept = false;
//                        break;
//                    }
//                    case MotionEvent.ACTION_CANCEL:
//                        intercept = false;
//                        break;
                }

                // parent pager 작동
                super.onInterceptTouchEvent(swapTouchEvent(event));
                //If not intercept, touch event should not be swapped.
                swapTouchEvent(event);

                return intercept;
            } else {
                // sub pager 작동.
                return false;
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return super.onTouchEvent(swapTouchEvent(ev));
    }
}

