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
import com.mhd.elemantary.adapter.ReCyclerAdapter;
import com.mhd.elemantary.common.MHDApplication;
import com.mhd.elemantary.common.vo.TodoData;
import com.mhd.elemantary.common.vo.TodoVo;
import com.mhd.elemantary.network.MHDNetworkInvoker;
import com.mhd.elemantary.util.MHDLog;

import java.util.HashMap;
import java.util.Map;


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
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));

        // query todo
        queryTodo();
    }

    @Override
    public void batchFunction(String api) {
//        if(api.equals(getString(R.string.api_editor_clear))) {
//            // editor 내용 초기화.
//            editor.clearAllContents();
//        }
    }

    /**
     * query todo
     */
    public void queryTodo(){
        try {
            Map<String, String> params = new HashMap<String, String>();
            //params.put("UUID", MHDApplication.getInstance().getMHDSvcManager().getDeviceNewUuid());
            params.put("UUMAIL", MHDApplication.getInstance().getMHDSvcManager().getUserVo().getUuMail());

            MHDNetworkInvoker.getInstance().sendVolleyRequest(((MainActivity)getActivity()), R.string.url_restapi_query_todo, params, ((MainActivity)getActivity()).responseListener);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            MHDLog.printException(e);
        }
    }
    /**
     * BaseActivity에서 상속받지 못하기 때문에 parent Activity에서 받아서 현재 fragment의 function을 호출하도록 처리
     */
    public boolean networkResponseProcess(String nvMsg, int nvCnt, String result) {
        TodoVo todoVo = null;
        adapter.deleteAll();

        if (nvCnt == 0) {
            // 정보가 없으면 비정상
            Toast.makeText(mContext, nvMsg, Toast.LENGTH_SHORT).show();
        } else {
            // 할일정보를 받아옴.
            Gson gson = new Gson();
            todoVo = gson.fromJson(result, TodoVo.class);
            MHDApplication.getInstance().getMHDSvcManager().setTodoVo(null);
            MHDApplication.getInstance().getMHDSvcManager().setTodoVo(todoVo);
        }
        for(int i=0; i<nvCnt; i++){
            // 각 List의 값들을 data 객체에 set 해줍니다.
            TodoData data = new TodoData();
            data.setSubject(todoVo.getMsg().get(i).getSubject());
            data.setDetail(todoVo.getMsg().get(i).getDetail());
            data.setDaily(todoVo.getMsg().get(i).getOneday());
            data.setTotal(todoVo.getMsg().get(i).getTotal());
            data.setRest(todoVo.getMsg().get(i).getRest());
            data.setGoal(todoVo.getMsg().get(i).getGoal());
            data.setSun(todoVo.getMsg().get(i).getSun());
            data.setMon(todoVo.getMsg().get(i).getMon());
            data.setTue(todoVo.getMsg().get(i).getTue());
            data.setWed(todoVo.getMsg().get(i).getWed());
            data.setThu(todoVo.getMsg().get(i).getThu());
            data.setFri(todoVo.getMsg().get(i).getFri());
            data.setSat(todoVo.getMsg().get(i).getSat());
            data.setPublisher(todoVo.getMsg().get(i).getPublish());
            data.setTitle(todoVo.getMsg().get(i).getTitle());
            data.setOption(todoVo.getMsg().get(i).getOption());

            // 각 값이 들어간 data를 adapter에 추가합니다.
            adapter.addItem(data);
        }

        // adapter의 값이 변경되었다는 것을 알려줍니다.
        adapter.notifyDataSetChanged();

        return true;
    }
}
