package com.mhd.elemantary.fragment;

import android.content.DialogInterface;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.gson.Gson;
import com.mhd.elemantary.MainActivity;
import com.mhd.elemantary.R;
import com.mhd.elemantary.activity.BaseActivity;
import com.mhd.elemantary.activity.RegistTodoActivity;
import com.mhd.elemantary.adapter.ReCyclerAdapter;
import com.mhd.elemantary.common.MHDApplication;
import com.mhd.elemantary.common.vo.SubjectVo;
import com.mhd.elemantary.common.vo.TodoData;
import com.mhd.elemantary.common.vo.TodoVo;
import com.mhd.elemantary.network.MHDNetworkInvoker;
import com.mhd.elemantary.util.MHDDialogUtil;
import com.mhd.elemantary.util.MHDLog;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
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

        //getData();
        queryTodo();
    }

    @Override
    public void batchFunction(String api) {
//        if(api.equals(getString(R.string.api_editor_clear))) {
//            // editor 내용 초기화.
//            editor.clearAllContents();
//        }
    }

    public void getData() {
        // 임의의 데이터입니다. 서버조회 구현할 것.
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

    /**
     * query todo
     */
    public void queryTodo(){
        try {
            // 획득한 uuid 로 서버조회 후 처리.

            // call service intro check
//                // String 방식
//                StringBuilder fullParams = new StringBuilder("{");
//                fullParams.append("\"UUID\":\""+userVo.getUuID()+"\"")
//                        .append(",\"UUPN\":\""+userVo.getUuPN()+"\"")
//                        .append(",\"UUOS\":\""+userVo.getUuOs()+"\"")
//                        .append(",\"UUDEVICE\":\""+userVo.getUuDevice()+"\"")
//                        .append(",\"UUTOKEN\":\""+userVo.getUuToken()+"\"")
//                        .append(",\"UUAPP\":\""+userVo.getUuAppVer()+"\"")
//                      v  .append("}");
            // Map 방식 0
            Map<String, String> params = new HashMap<String, String>();
            //params.put("UUID", MHDApplication.getInstance().getMHDSvcManager().getDeviceNewUuid());
            params.put("UUMAIL", MHDApplication.getInstance().getMHDSvcManager().getUserVo().getUuMail());
            MHDLog.d(TAG, "queryTodo >>> ");

            MHDNetworkInvoker.getInstance().sendVolleyRequest(((MainActivity)getActivity()), R.string.url_restapi_query_todo, params, ((MainActivity)getActivity()).responseListener);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            MHDLog.printException(e);
        }
    }

    public boolean networkResponseProcess(String nvResultCode, String nvJsonDataString, String nvApi, String nvMsg, int nvCnt) {
        TodoVo todoVo = null;

        if (nvCnt == 0) {
            // 정보가 없으면 비정상
            // 우선 toast를 띄울 것.
            Toast.makeText(mContext, nvMsg, Toast.LENGTH_SHORT).show();
        } else {
            // 할일정보를 받아옴.
            Gson gson = new Gson();
            todoVo = gson.fromJson(nvJsonDataString, TodoVo.class);
            MHDApplication.getInstance().getMHDSvcManager().setTodoVo(null);
            MHDApplication.getInstance().getMHDSvcManager().setTodoVo(todoVo);
        }

        String prevSubject = "";
        ArrayList subjectArrayList = new ArrayList<>();
        ArrayList detailArrayList = new ArrayList<>();

        // 과목 - 상세 형식으로 추출할 때 사용
        for(int i=0; i<nvCnt; i++){
            subjectArrayList.add(subjectVo.getMsg().get(i).getSubject() + " - " + subjectVo.getMsg().get(i).getDetail());
        }

        spi_todo_subject = (Spinner) findViewById(R.id.spi_todo_subject);
        ArrayAdapter<CharSequence> adapter = new ArrayAdapter<CharSequence>(this, R.layout.default_spinner_item, subjectArrayList);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spi_todo_subject.setAdapter(adapter);

        AppCompatButton btn_sun = (AppCompatButton) findViewById(R.id.btn_sun);
        AppCompatButton btn_mon = (AppCompatButton) findViewById(R.id.btn_mon);
        AppCompatButton btn_tues = (AppCompatButton) findViewById(R.id.btn_tues);
        AppCompatButton btn_wed = (AppCompatButton) findViewById(R.id.btn_wed);
        AppCompatButton btn_thur = (AppCompatButton) findViewById(R.id.btn_thur);
        AppCompatButton btn_fri = (AppCompatButton) findViewById(R.id.btn_fri);
        AppCompatButton btn_sat = (AppCompatButton) findViewById(R.id.btn_sat);

        btn_sun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { selectDay(0); }
        });
        btn_mon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { selectDay(1); }
        });
        btn_tues.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { selectDay(2); }
        });
        btn_wed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { selectDay(3); }
        });
        btn_thur.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { selectDay(4); }
        });
        btn_fri.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { selectDay(5); }
        });
        btn_sat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { selectDay(6); }
        });

        return true;
    }
}
