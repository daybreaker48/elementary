package com.mhd.elemantary.fragment;

import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.mhd.elemantary.MainActivity;
import com.mhd.elemantary.R;
import com.mhd.elemantary.adapter.ReCyclerAdapter;
import com.mhd.elemantary.common.MHDApplication;
import com.mhd.elemantary.common.MHDSvcManager;
import com.mhd.elemantary.common.vo.KidsVo;
import com.mhd.elemantary.common.vo.MenuVo;
import com.mhd.elemantary.common.vo.TodoData;
import com.mhd.elemantary.common.vo.TodoVo;
import com.mhd.elemantary.network.MHDNetworkInvoker;
import com.mhd.elemantary.util.MHDLog;
import com.skydoves.powermenu.MenuAnimation;
import com.skydoves.powermenu.OnMenuItemClickListener;
import com.skydoves.powermenu.PowerMenu;
import com.skydoves.powermenu.PowerMenuItem;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class TodoFragment extends BaseFragment {
    private RecyclerView recyclerView;
    private ReCyclerAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    PowerMenu powerMenu;
    TextView vst_top_title;
    String displayKid = "";

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

        LinearLayout ll_top_todo = (LinearLayout) root.findViewById(R.id.ll_top_todo);

        // vo에 있는 아이 정보를 메뉴item 으로 삽입.
        KidsVo kidsVo = MHDApplication.getInstance().getMHDSvcManager().getKidsVo();
        MenuVo menuVo = MHDApplication.getInstance().getMHDSvcManager().getMenuVo();
        List<PowerMenuItem> kidsList = new ArrayList();
        for(int k=0; k<kidsVo.getCnt(); k++){
            kidsList.add(new PowerMenuItem(kidsVo.getMsg().get(k).getName(), k == 0 ? true : false));
        }
        vst_top_title = (TextView) root.findViewById(R.id.vst_top_title);
        for(int k=0; k<menuVo.getMsg().size(); k++){
            if("TO".equals(menuVo.getMsg().get(k).getMenuname())){
                // 해당메뉴에 설정된 아이정보
                displayKid = menuVo.getMsg().get(k).getKidname();
            }
        }
        vst_top_title.setText("["+displayKid+"] 학습");

        ll_top_todo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                powerMenu = new PowerMenu.Builder(getActivity())
                        .addItemList(kidsList) //
//                .addItem(new PowerMenuItem("한다인", false)) // add an item.
//                .addItem(new PowerMenuItem("한지인", false)) // aad an item list.
                        .setTextSize(14)
                        .setAnimation(MenuAnimation.SHOWUP_TOP_LEFT) // Animation start point (TOP | LEFT).
                        .setMenuRadius(10f) // sets the corner radius.
                        .setMenuShadow(10f) // sets the shadow.
                        .setTextColor(ContextCompat.getColor(getActivity(), R.color.black))
                        .setTextGravity(Gravity.CENTER)
                        .setTextTypeface(Typeface.createFromAsset(getActivity().getAssets(), "notoregular.otf"))
                        .setSelectedTextColor(Color.WHITE)
                        .setMenuColor(Color.WHITE)
                        .setSelectedMenuColor(ContextCompat.getColor(getActivity(), R.color.colorPrimary))
                        .setOnMenuItemClickListener(onMenuItemClickListener)
                        .setDivider(new ColorDrawable(ContextCompat.getColor(getActivity(), R.color.gray))) // sets a divider.
                        .setDividerHeight(1)
                        .build();

                powerMenu.showAsDropDown(v);
            }
        });

        RecyclerView recyclerView = (RecyclerView) root.findViewById(R.id.recv_receiving);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(mContext);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new ReCyclerAdapter();
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));

        queryTodo();
    }

    private OnMenuItemClickListener<PowerMenuItem> onMenuItemClickListener = new OnMenuItemClickListener<PowerMenuItem>() {
        @Override
        public void onItemClick(int position, PowerMenuItem item) {
            displayKid = item.getTitle().toString();
            vst_top_title.setText("["+displayKid+"] 학습");
            powerMenu.setSelectedPosition(position); // change selected item
            // MenuVo 정보를 갱신
            MenuVo menuVo = MHDApplication.getInstance().getMHDSvcManager().getMenuVo();
            for(int k=0; k<menuVo.getMsg().size(); k++){
                if("TO".equals(menuVo.getMsg().get(k).getMenuname())){
                    // 해당메뉴에 설정된 아이정보
                    menuVo.getMsg().get(k).setKidname(displayKid);
                    queryTodo();
                    break;
                }
            }

            powerMenu.dismiss();
        }
    };

    @Override
    public void batchFunction(String api) {
//        if(api.equals(getString(R.string.api_editor_clear))) {
//            // editor 내용 초기화.
//            editor.clearAllContents();
//        }
    }

    /**
     * query tododata
     */
    public void queryTodo(){
        try {
            Map<String, String> params = new HashMap<String, String>();
            //params.put("UUID", MHDApplication.getInstance().getMHDSvcManager().getDeviceNewUuid());
            params.put("UUMAIL", MHDApplication.getInstance().getMHDSvcManager().getUserVo().getUuMail());
            params.put("TKNAME", displayKid);

            MHDNetworkInvoker.getInstance().sendVolleyRequest(((MainActivity)getActivity()), R.string.url_restapi_query_todo, params, ((MainActivity)getActivity()).responseListener);
        } catch (Exception e) {
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
            // 정보가 없으면
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

    /**
     * BaseActivity에서 상속받지 못하기 때문에 parent Activity에서 받아서 현재 fragment의 function을 호출하도록 처리
     */
    public boolean noData(String nvApiParam) {
        TodoVo todoVo = null;
        adapter.deleteAll();

        if (nvCnt == 0) {
            // 정보가 없으면
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
