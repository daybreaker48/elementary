package com.mhd.elemantary.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.mhd.elemantary.R;
import com.mhd.elemantary.adapter.ReCyclerAdapter;


public class SelfFragment extends BaseFragment {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
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

        String[] textSet = {"111", "222", "333", "444", "111", "222", "333", "444", "111", "222", "333", "444"};
        int[] imgSet = {R.drawable.icon_link, R.drawable.ico_ofw_logout, R.drawable.icon_bullet_list, R.drawable.icon_numbered_list, R.drawable.icon_link, R.drawable.ico_ofw_logout, R.drawable.icon_bullet_list, R.drawable.icon_numbered_list, R.drawable.icon_link, R.drawable.ico_ofw_logout, R.drawable.icon_bullet_list, R.drawable.icon_numbered_list};

        adapter = new ReCyclerAdapter(textSet, imgSet);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void batchFunction(String api) {
//        if(api.equals(getString(R.string.api_editor_clear))) {
//            // editor 내용 초기화.
//            editor.clearAllContents();
//        }
    }
}
