package com.mhd.elemantary.fragment;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.mhd.elemantary.R;
import com.mhd.elemantary.adapter.ReCyclerSelfAdapter;
import com.mhd.elemantary.common.vo.SelfData;

import java.util.Arrays;
import java.util.List;


public class SelfFragment extends BaseFragment {
    private RecyclerView recyclerView;
    private ReCyclerSelfAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    public static SelfFragment create() {
        return new SelfFragment();
    }

    @Override
    public int getLayoutResId() {
        return R.layout.fragment_self;
    }

    @Override
    public void inOnCreateView(View root, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //Statusbar 아래로 내리기
//        final TextView mTitle = (TextView) root.findViewById(R.id.vst_top_title);
//        LinearLayout.LayoutParams mLayoutParams = (LinearLayout.LayoutParams) mTitle.getLayoutParams();
//        mLayoutParams.topMargin = Util.getInstance().getStatusBarHeight(root.getContext());
//        mTitle.setLayoutParams(mLayoutParams);

        RecyclerView recyclerView = (RecyclerView) root.findViewById(R.id.recv_receiving);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(mContext);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new ReCyclerSelfAdapter();
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
        List<String> listSelfItem = Arrays.asList("집안일 돕기", "스스로 씻기", "치카", "화분물주기");

        for (int i = 0; i < listSelfItem.size(); i++) {
            // 각 List의 값들을 data 객체에 set 해줍니다.
            SelfData data = new SelfData();
            data.setSelfItem(listSelfItem.get(i));

            // 각 값이 들어간 data를 adapter에 추가합니다.
            adapter.addItem(data);
        }

        // adapter의 값이 변경되었다는 것을 알려줍니다.
        adapter.notifyDataSetChanged();
    }
}
