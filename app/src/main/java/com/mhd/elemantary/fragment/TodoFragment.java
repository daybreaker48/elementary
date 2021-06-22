package com.mhd.elemantary.fragment;

import android.os.Bundle;
import androidx.annotation.Nullable;

import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.mhd.elemantary.R;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.T;


public class TodoFragment extends BaseFragment {
    private TableLayout tl_todo;
    private TableRow tablerow;

    public static TodoFragment create() {
        return new TodoFragment();
    }

    @Override
    public int getLayoutResId() {
        return R.layout.fragment_todo;
    }

    @Override
    public void inOnCreateView(View root, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        //Statusbar 아래로 내리기
//        final TextView mTitle = (TextView) root.findViewById(R.id.vst_top_title);
//        RelativeLayout.LayoutParams mLayoutParams = (RelativeLayout.LayoutParams) mTitle.getLayoutParams();
//        mLayoutParams.topMargin = Util.getInstance().getStatusBarHeight(root.getContext());
//        mTitle.setLayoutParams(mLayoutParams);

        tl_todo = (TableLayout) root.findViewById(R.id.tl_todo);
        tablerow = new TableRow(getActivity());

        root.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                tablerow.setLayoutParams(new TableRow.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT));

                for(int i=0;i<5;i++){
                    TextView textView = new TextView(getActivity());
                    textView.setText(String.valueOf(i));
                    textView.setGravity(Gravity.CENTER);
                    textView.setTextSize(18);
                    tablerow.addView(textView);
                }
                tl_todo.addView(tablerow);
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
