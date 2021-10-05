package com.mhd.elemantary.fragment;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.gson.Gson;
import com.mhd.elemantary.MainActivity;
import com.mhd.elemantary.R;
import com.mhd.elemantary.adapter.ReCyclerScheduleAdapter;
import com.mhd.elemantary.common.MHDApplication;
import com.mhd.elemantary.common.vo.ScheduleData;
import com.mhd.elemantary.common.vo.ScheduleVo;
import com.mhd.elemantary.network.MHDNetworkInvoker;
import com.mhd.elemantary.util.MHDLog;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ScheduleFragment extends BaseFragment {
    private RecyclerView recyclerView;
    private ReCyclerScheduleAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;


    public static ScheduleFragment create() {
        return new ScheduleFragment();
    }

    @Override
    public int getLayoutResId() {
        return R.layout.fragment_schedule;
    }

    @Override
    public void inOnCreateView(View root, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //Statusbar 아래로 내리기
//        final TextView mTitle = (TextView) root.findViewById(R.id.vst_top_title);
//        RelativeLayout.LayoutParams mLayoutParams = (RelativeLayout.LayoutParams) mTitle.getLayoutParams();
//        mLayoutParams.topMargin = Util.getInstance().getStatusBarHeight(root.getContext());
//        mTitle.setLayoutParams(mLayoutParams);

        RecyclerView recyclerView = (RecyclerView) root.findViewById(R.id.recv_receiving);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(mContext);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new ReCyclerScheduleAdapter();
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
        List<String> listTime = Arrays.asList("8~9", "9~10", "10~11", "11~12", "12~13", "13~14", "14~15", "15~16", "16~17", "17~18", "18~19", "19~20", "20~21", "21~22");
        List<String> listMon = Arrays.asList(
                "학교",
                "학교",
                "학교",
                "학교",
                "학교",
                "학교",
                "학교",
                "피아노",
                "태권도",
                "휴식",
                "영어",
                "수학",
                "국어",
                "취침"
        );
        List<String> listTue = Arrays.asList(
                "학교",
                "학교",
                "학교",
                "학교",
                "학교",
                "학교",
                "학교",
                "피아노",
                "태권도",
                "휴식",
                "영어",
                "수학",
                "국어",
                "취침"
        );
        List<String> listWed = Arrays.asList(
                "학교",
                "학교",
                "학교",
                "학교",
                "학교",
                "학교",
                "학교",
                "피아노",
                "태권도",
                "휴식",
                "영어",
                "수학",
                "국어",
                "취침"
        );
        List<String> listThu = Arrays.asList(
                "학교",
                "학교",
                "학교",
                "학교",
                "학교",
                "학교",
                "학교",
                "피아노",
                "태권도",
                "휴식",
                "영어",
                "수학",
                "국어",
                "취침"
        );
        List<String> listFri = Arrays.asList(
                "학교",
                "학교",
                "학교",
                "학교",
                "학교",
                "학교",
                "학교",
                "체조",
                "태권도",
                "휴식",
                "영어",
                "수학",
                "국어",
                "취침"
        );
        List<String> listSat = Arrays.asList(
                "자유",
                "자유",
                "자유",
                "자유",
                "자유",
                "자유",
                "자유",
                "발레",
                "태권도",
                "휴식",
                "영어",
                "수학",
                "국어",
                "취침"
        );
        List<String> listSun = Arrays.asList(
                "자유",
                "자유",
                "자유",
                "자유",
                "자유",
                "자유",
                "자유",
                "수영",
                "태권도",
                "휴식",
                "영어",
                "수학",
                "국어",
                "취침"
        );
        for (int i = 0; i < listTime.size(); i++) {
            // 각 List의 값들을 data 객체에 set 해줍니다.
            ScheduleData data = new ScheduleData();
            data.setTime(listTime.get(i));
            data.setMon(listMon.get(i));
            data.setTue(listTue.get(i));
            data.setWed(listWed.get(i));
            data.setThu(listThu.get(i));
            data.setFri(listFri.get(i));
            data.setSat(listSat.get(i));
            data.setSun(listSun.get(i));

            // 각 값이 들어간 data를 adapter에 추가합니다.
            adapter.addItem(data);
        }

        // adapter의 값이 변경되었다는 것을 알려줍니다.
        adapter.notifyDataSetChanged();
    }

    /**
     * query todo
     */
    public void queryTodo(){
        try {
            Map<String, String> params = new HashMap<String, String>();
            //params.put("UUID", MHDApplication.getInstance().getMHDSvcManager().getDeviceNewUuid());
            params.put("UUMAIL", MHDApplication.getInstance().getMHDSvcManager().getUserVo().getUuMail());

            MHDNetworkInvoker.getInstance().sendVolleyRequest(((MainActivity)getActivity()), R.string.url_restapi_query_schedule, params, ((MainActivity)getActivity()).responseListener);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            MHDLog.printException(e);
        }
    }
    /**
     * BaseActivity에서 상속받지 못하기 때문에 parent Activity에서 받아서 현재 fragment의 function을 호출하도록 처리
     */
    public boolean networkResponseProcess(String nvMsg, int nvCnt, String result) {
        ScheduleVo scheduleVo = null;
        adapter.deleteAll();

        if (nvCnt == 0) {
            // 정보가 없으면 비정상
            Toast.makeText(mContext, nvMsg, Toast.LENGTH_SHORT).show();
        } else {
            // 할일정보를 받아옴.
            Gson gson = new Gson();
            scheduleVo = gson.fromJson(result, ScheduleVo.class);
            MHDApplication.getInstance().getMHDSvcManager().setTodoVo(null);
            MHDApplication.getInstance().getMHDSvcManager().setTodoVo(scheduleVo);
        }
        for(int i=0; i<nvCnt; i++){
            // 각 List의 값들을 data 객체에 set 해줍니다.
            TodoData data = new TodoData();
            data.setSubject(scheduleVo.getMsg().get(i).getSubject());
            data.setTextbook(scheduleVo.getMsg().get(i).getDetail());
            data.setDailyProgress(scheduleVo.getMsg().get(i).getOneday());

            // 각 값이 들어간 data를 adapter에 추가합니다.
            adapter.addItem(data);
        }

        // adapter의 값이 변경되었다는 것을 알려줍니다.
        adapter.notifyDataSetChanged();

        return true;
    }
}
