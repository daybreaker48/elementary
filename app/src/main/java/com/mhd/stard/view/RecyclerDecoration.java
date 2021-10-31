package com.mhd.stard.view;

import android.graphics.Paint;
import android.graphics.Rect;
import android.view.View;

import com.mhd.stard.adapter.ReCyclerAdapter;
import com.mhd.stard.util.MHDLog;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class RecyclerDecoration extends RecyclerView.ItemDecoration {
    private final int divHeight;
    private Paint mPaint = new Paint();

    public RecyclerDecoration(int divHeight, int a_color){
        this.divHeight = divHeight;
        mPaint.setColor(a_color);
    }

//    @Override
//    public void onDrawOver(@NonNull Canvas a_canvas, @NonNull RecyclerView parent, @NonNull RecyclerView.State a_state) {
//        super.onDrawOver(a_canvas, parent, a_state);
//
//        final int left = parent.getPaddingLeft();
//        final int right = parent.getWidth() - parent.getPaddingRight();
//        final int childCount = parent.getChildCount();
//        for (int i = 0; i < childCount; i++) {
//            ReCyclerAdapter todoAdapter = (ReCyclerAdapter) parent.getAdapter();
//            int[] arr_delete_divider = todoAdapter.getSectionPosition();
//
//            View child = parent.getChildAt(i);
//            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
//            int top = child.getBottom() + params.bottomMargin;
//            if(arr_delete_divider.length > 0) {
//                    int eheight = this.divHeight;
//                    for (int k : arr_delete_divider) {
//                        if (k == i) {
//                            eheight = 0;
//                            break;
//                        }
//                    }
//                    float bottom = top + eheight;
//                    a_canvas.drawRect(left, top, right, bottom, mPaint);
//            }else{
//                float bottom = top + divHeight;
//                a_canvas.drawRect(left, top, right, bottom, mPaint);
//            }
//        }
//    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);

        MHDLog.d("dagian", parent.getChildAdapterPosition(view) + "/" + parent.getAdapter().getItemCount());
        MHDLog.d("dagian", parent.getChildAdapterPosition(view) + "/" + parent.getAdapter().getItemCount());
        ReCyclerAdapter todoAdapter = (ReCyclerAdapter) parent.getAdapter();
        int[] arr_delete_divider = todoAdapter.getSectionPosition();
        if(arr_delete_divider.length > 0) {
            if (parent.getChildAdapterPosition(view) != parent.getAdapter().getItemCount() - 1) {
                int eheight = this.divHeight;
                for (int i : arr_delete_divider) {
                    if (i == parent.getChildAdapterPosition(view)) {
                        eheight = 0;
                        break;
                    }
                }
                outRect.bottom = eheight;
            }
        }else{
            outRect.bottom = divHeight;
        }
    }
}
