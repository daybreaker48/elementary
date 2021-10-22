package com.mhd.elemantary.view;

import android.graphics.Rect;
import android.view.View;

import com.mhd.elemantary.adapter.ReCyclerAdapter;
import com.mhd.elemantary.util.MHDLog;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class RecyclerDecoration extends RecyclerView.ItemDecoration {
    private final int divHeight;

    public RecyclerDecoration(int divHeight){
        this.divHeight = divHeight;
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);

        MHDLog.d("ttt", parent.getChildAdapterPosition(view) + "/" + parent.getAdapter().getItemCount());
        MHDLog.d("ttt", parent.getChildAdapterPosition(view) + "/" + parent.getAdapter().getItemCount());
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
