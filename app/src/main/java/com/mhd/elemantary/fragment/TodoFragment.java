package com.mhd.elemantary.fragment;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.view.ViewGroup;

import com.mhd.elemantary.R;
import com.mhd.elemantary.adapter.ReCyclerAdapter;
import com.mhd.elemantary.common.vo.TodoData;

import java.util.Arrays;
import java.util.List;


public class TodoFragment extends BaseFragment {
    private RecyclerView recyclerView;
    private ReCyclerAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;

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

        RecyclerView recyclerView = (RecyclerView) root.findViewById(R.id.recv_receiving);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(mContext);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new ReCyclerAdapter();
        recyclerView.setAdapter(adapter);

        getData();
    }

    @Override
    public void batchFunction(String api) {
//        if(api.equals(getString(R.string.api_editor_clear))) {
//            // editor 내용 초기화.
//            editor.clearAllContents();
//        }
    }

    private void getData() {
        // 임의의 데이터입니다.
        List<String> listSubject = Arrays.asList("국어", "국어", "국어", "수학", "수학", "수학", "수학", "영어",
                "영어", "영어", "영어");
        List<String> listTextbook = Arrays.asList(
                "받아쓰기",
                "받아쓰기",
                "받아쓰기",
                "덧셈",
                "뺄셈",
                "곱셈",
                "나눗셈",
                "english",
                "english",
                "english",
                "english"
        );
        List<String> listDailyProgress = Arrays.asList(
                "1 page",
                "2 page",
                "3 page",
                "4 page",
                "5 page",
                "6 page",
                "7 page",
                "8 page",
                "9 page",
                "10 page",
                "11 page"
        );
        for (int i = 0; i < listSubject.size(); i++) {
            // 각 List의 값들을 data 객체에 set 해줍니다.
            TodoData data = new TodoData();
            data.setSubject(listSubject.get(i));
            data.setTextbook(listTextbook.get(i));
            data.setDailyProgress(listDailyProgress.get(i));

            // 각 값이 들어간 data를 adapter에 추가합니다.
            adapter.addItem(data);
        }

        // adapter의 값이 변경되었다는 것을 알려줍니다.
        adapter.notifyDataSetChanged();
    }
}
