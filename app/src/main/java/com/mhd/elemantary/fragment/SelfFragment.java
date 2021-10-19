package com.mhd.elemantary.fragment;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.gson.Gson;
import com.mhd.elemantary.MainActivity;
import com.mhd.elemantary.R;
import com.mhd.elemantary.adapter.ReCyclerSelfAdapter;
import com.mhd.elemantary.common.MHDApplication;
import com.mhd.elemantary.common.vo.SelfData;
import com.mhd.elemantary.common.vo.SelfVo;
import com.mhd.elemantary.network.MHDNetworkInvoker;
import com.mhd.elemantary.util.MHDLog;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


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
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));

//        getData();
        // query self
        querySelf();
    }

    @Override
    public void batchFunction(String api) {
//        if(api.equals(getString(R.string.api_editor_clear))) {
//            // editor 내용 초기화.
//            editor.clearAllContents();
//        }
    }

    /**
     * query self
     */
    public void querySelf(){
        try {
            Map<String, String> params = new HashMap<String, String>();
            //params.put("UUID", MHDApplication.getInstance().getMHDSvcManager().getDeviceNewUuid());
            params.put("UUMAIL", MHDApplication.getInstance().getMHDSvcManager().getUserVo().getUuMail());

            MHDNetworkInvoker.getInstance().sendVolleyRequest(((MainActivity)getActivity()), R.string.url_restapi_query_self, params, ((MainActivity)getActivity()).responseListener);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            MHDLog.printException(e);
        }
    }
    /**
     * BaseActivity에서 상속받지 못하기 때문에 parent Activity에서 받아서 현재 fragment의 function을 호출하도록 처리
     */
    public boolean networkResponseProcess(String nvMsg, int nvCnt, String result) {
        SelfVo selfVo = null;
        adapter.deleteAll();

        if (nvCnt == 0) {
            // 정보가 없으면 비정상
            Toast.makeText(mContext, nvMsg, Toast.LENGTH_SHORT).show();
        } else {
            // self 정보를 받아옴.
            Gson gson = new Gson();
            selfVo = gson.fromJson(result, SelfVo.class);
            MHDApplication.getInstance().getMHDSvcManager().setSelfVo(null);
            MHDApplication.getInstance().getMHDSvcManager().setSelfVo(selfVo);
        }
        for(int i=0; i<nvCnt; i++){
            // 각 List의 값들을 data 객체에 set 해줍니다.
            SelfData data = new SelfData();
            data.setSelfIdx(selfVo.getMsg().get(i).getIdx());
            data.setSelfItem(selfVo.getMsg().get(i).getTbtitle());
            data.setSelfComplete(selfVo.getMsg().get(i).getSfcomplete()
            );

            // 각 값이 들어간 data를 adapter에 추가합니다.
            adapter.addItem(data);
        }

        // adapter의 값이 변경되었다는 것을 알려줍니다.
        adapter.notifyDataSetChanged();

        return true;
    }
    public boolean networkResponseProcess_update(String nvMsg, int nvCnt, String result) {
        if (nvCnt == 0) {
            // 정보가 없으면 비정상. update 실패.
            Toast.makeText(mContext, "fail", Toast.LENGTH_SHORT).show();
        } else {// update 성공.
            Toast.makeText(mContext, "success", Toast.LENGTH_SHORT).show();
        }

        return true;
    }
}
