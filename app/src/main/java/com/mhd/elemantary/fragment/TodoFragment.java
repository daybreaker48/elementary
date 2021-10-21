package com.mhd.elemantary.fragment;

import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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
import com.mhd.elemantary.view.RecyclerDecoration;
import com.skydoves.powermenu.MenuAnimation;
import com.skydoves.powermenu.MenuBaseAdapter;
import com.skydoves.powermenu.OnMenuItemClickListener;
import com.skydoves.powermenu.PowerMenu;
import com.skydoves.powermenu.PowerMenuItem;

import org.w3c.dom.Text;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class TodoFragment extends BaseFragment {
    private RecyclerView recyclerView;
    private ReCyclerAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    PowerMenu powerMenu = null;
    TextView vst_top_title, tv_no_data, tv_todo_subject_section;
    String displayKid = "";

    LinearLayout btn_past_3, btn_past_2, btn_past_1, btn_todoy, btn_next_1, btn_next_2, btn_next_3, ll_area_days;
    TextView tv_past_3_day, tv_past_2_day, tv_past_1_day, tv_today_day, tv_next_1_day, tv_next_2_day, tv_next_3_day;
    TextView tv_past_3_week, tv_past_2_week, tv_past_1_week, tv_today_week, tv_next_1_week, tv_next_2_week, tv_next_3_week;

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
        tv_no_data = (TextView) root.findViewById(R.id.tv_no_data);
        tv_todo_subject_section = (TextView) root.findViewById(R.id.tv_todo_subject_section);

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
        vst_top_title.setText("[ "+displayKid+" ] 오늘의 학습");

        ll_area_days = (LinearLayout) root.findViewById(R.id.ll_area_days);

        btn_past_3 = (LinearLayout) root.findViewById(R.id.btn_past_3);
        btn_past_2 = (LinearLayout) root.findViewById(R.id.btn_past_2);
        btn_past_1 = (LinearLayout) root.findViewById(R.id.btn_past_1);
        btn_todoy = (LinearLayout) root.findViewById(R.id.btn_today);
        btn_next_1 = (LinearLayout) root.findViewById(R.id.btn_next_1);
        btn_next_2 = (LinearLayout) root.findViewById(R.id.btn_next_2);
        btn_next_3 = (LinearLayout) root.findViewById(R.id.btn_next_3);

        tv_today_day = (TextView) root.findViewById(R.id.tv_today_day);
        tv_past_3_day = (TextView) root.findViewById(R.id.tv_past_3_day);
        tv_past_2_day = (TextView) root.findViewById(R.id.tv_past_2_day);
        tv_past_1_day = (TextView) root.findViewById(R.id.tv_past_1_day);
        tv_next_1_day = (TextView) root.findViewById(R.id.tv_next_1_day);
        tv_next_2_day = (TextView) root.findViewById(R.id.tv_next_2_day);
        tv_next_3_day = (TextView) root.findViewById(R.id.tv_next_3_day);

        tv_today_week = (TextView) root.findViewById(R.id.tv_today_week);
        tv_past_3_week = (TextView) root.findViewById(R.id.tv_past_3_week);
        tv_past_2_week = (TextView) root.findViewById(R.id.tv_past_2_week);
        tv_past_1_week = (TextView) root.findViewById(R.id.tv_past_1_week);
        tv_next_1_week = (TextView) root.findViewById(R.id.tv_next_1_week);
        tv_next_2_week = (TextView) root.findViewById(R.id.tv_next_2_week);
        tv_next_3_week = (TextView) root.findViewById(R.id.tv_next_3_week);

        Calendar cal = Calendar.getInstance();
        int weekd = cal.get(Calendar.DAY_OF_WEEK); // 오늘 요일
        DateFormat df = new SimpleDateFormat("yyyyMMdd");
        tv_today_day.setText(df.format(cal.getTime()).substring(6));
        tv_today_week.setText(selectWeek(weekd));
        cal.add(Calendar.DATE, -3);
        weekd = cal.get(Calendar.DAY_OF_WEEK);
        tv_past_3_day.setText(df.format(cal.getTime()).substring(6));
        tv_past_3_week.setText(selectWeek(weekd));
        cal.add(Calendar.DATE, 1);
        weekd = cal.get(Calendar.DAY_OF_WEEK);
        tv_past_2_day.setText(df.format(cal.getTime()).substring(6));
        tv_past_2_week.setText(selectWeek(weekd));
        cal.add(Calendar.DATE, 1);
        weekd = cal.get(Calendar.DAY_OF_WEEK);
        tv_past_1_day.setText(df.format(cal.getTime()).substring(6));
        tv_past_1_week.setText(selectWeek(weekd));
        cal.add(Calendar.DATE, 2);
        weekd = cal.get(Calendar.DAY_OF_WEEK);
        tv_next_1_day.setText(df.format(cal.getTime()).substring(6));
        tv_next_1_week.setText(selectWeek(weekd));
        cal.add(Calendar.DATE, 1);
        weekd = cal.get(Calendar.DAY_OF_WEEK);
        tv_next_2_day.setText(df.format(cal.getTime()).substring(6));
        tv_next_2_week.setText(selectWeek(weekd));
        cal.add(Calendar.DATE, 1);
        weekd = cal.get(Calendar.DAY_OF_WEEK);
        tv_next_3_day.setText(df.format(cal.getTime()).substring(6));
        tv_next_3_week.setText(selectWeek(weekd));

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
                .setSelectedMenuColor(ContextCompat.getColor(getActivity(), R.color.powermenu_select))
                .setOnMenuItemClickListener(onMenuItemClickListener)
                .setDivider(new ColorDrawable(ContextCompat.getColor(getActivity(), R.color.gray))) // sets a divider.
                .setDividerHeight(1)
                .setHeaderView(null)
                .setFooterView(null)
                .build();

//        vst_right_2_image.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                KidsVo kidsVo = MHDApplication.getInstance().getMHDSvcManager().getKidsVo();
//                List<PowerMenuItem> kidsList = new ArrayList();
//                for(int k=0; k<kidsVo.getCnt(); k++){
//                    kidsList.add(new PowerMenuItem(kidsVo.getMsg().get(k).getName(), k == 0 ? true : false));
//                }
//                powerMenu = new PowerMenu.Builder(getActivity())
//                        .addItemList(kidsList) //
////                .addItem(new PowerMenuItem("한다인", false)) // add an item.
////                .addItem(new PowerMenuItem("한지인", false)) // aad an item list.
//                        .setTextSize(14)
//                        .setAnimation(MenuAnimation.SHOWUP_TOP_LEFT) // Animation start point (TOP | LEFT).
//                        .setMenuRadius(10f) // sets the corner radius.
//                        .setMenuShadow(10f) // sets the shadow.
//                        .setTextColor(ContextCompat.getColor(getActivity(), R.color.black))
//                        .setTextGravity(Gravity.CENTER)
//                        .setTextTypeface(Typeface.createFromAsset(getActivity().getAssets(), "notoregular.otf"))
//                        .setSelectedTextColor(Color.WHITE)
//                        .setMenuColor(Color.WHITE)
//                        .setSelectedMenuColor(ContextCompat.getColor(getActivity(), R.color.v))
//                        .setOnMenuItemClickListener(onMenuItemClickListener)
//                        .setDivider(new ColorDrawable(ContextCompat.getColor(getActivity(), R.color.gray))) // sets a divider.
//                        .setDividerHeight(1)
//                        .setHeaderView(null)
//                        .setFooterView(null)
//                        .build();
//
//                powerMenu.showAsDropDown(v);
//            }
//        });

        recyclerView = (RecyclerView) root.findViewById(R.id.recv_receiving);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(mContext);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new ReCyclerAdapter();
        recyclerView.setAdapter(adapter);
        RecyclerDecoration recyclerDecoration = new RecyclerDecoration(1);
        recyclerView.addItemDecoration(recyclerDecoration);
//        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));

        adapter.setOnItemClickListener(new ReCyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                // position을 가지고 라인을 알아낸 다음에
                // 해당 라인의 메뉴를 띄우려 했는데 이건 일단 보류. 안할 수도 있다.
                // 클릭하면 바로 수정화면으로 넘기고, 거기에 삭제버튼을 만든다.
                ((MainActivity)getActivity()).startTodoModify(position);
                Toast.makeText(mContext, "test" + position, Toast.LENGTH_SHORT).show();
            }
        }) ;

        queryTodo();
    }

    private OnMenuItemClickListener<PowerMenuItem> onMenuItemClickListener = new OnMenuItemClickListener<PowerMenuItem>() {
        @Override
        public void onItemClick(int position, PowerMenuItem item) {
            displayKid = item.getTitle().toString();
            vst_top_title.setText("[ "+displayKid+" ] 학습");
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
    public void onResume() {
        super.onResume();

        // 기존에 선택됐던 item
        if(powerMenu != null) {
            int sp = powerMenu.getSelectedPosition() == -1 ? 0 : powerMenu.getSelectedPosition();
            KidsVo kidsVo = MHDApplication.getInstance().getMHDSvcManager().getKidsVo();
            List<PowerMenuItem> kidsList = new ArrayList();
            for (int k = 0; k < kidsVo.getCnt(); k++) {
                kidsList.add(new PowerMenuItem(kidsVo.getMsg().get(k).getName(), k == sp ? true : false));
            }

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
                    .setSelectedMenuColor(ContextCompat.getColor(getActivity(), R.color.powermenu_select))
                    .setOnMenuItemClickListener(onMenuItemClickListener)
                    .setDivider(new ColorDrawable(ContextCompat.getColor(getActivity(), R.color.gray))) // sets a divider.
                    .setDividerHeight(1)
                    .setHeaderView(null)
                    .setFooterView(null)
                    .build();
        }
    }

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
            Calendar cal = Calendar.getInstance();
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            String tdDate = df.format(cal.getTime()).substring(6);

            Map<String, String> params = new HashMap<String, String>();
            //params.put("UUID", MHDApplication.getInstance().getMHDSvcManager().getDeviceNewUuid());
            params.put("UUMAIL", MHDApplication.getInstance().getMHDSvcManager().getUserVo().getUuMail());
            params.put("TKNAME", displayKid);
            params.put("TDDATE", tdDate);

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

        tv_no_data.setVisibility(View.GONE);
        ll_area_days.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.VISIBLE);

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
            data.setSection(todoVo.getMsg().get(i).getSection());

            // 각 값이 들어간 data를 adapter에 추가합니다.
            adapter.addItem(data);
        }

        // adapter의 값이 변경되었다는 것을 알려줍니다.
        adapter.notifyDataSetChanged();

        return true;
    }

    public void noData(String nvApiParam) {
        tv_no_data.setVisibility(View.VISIBLE);
        ll_area_days.setVisibility(View.GONE);
        recyclerView.setVisibility(View.GONE);
    }

    public String selectWeek(int day) {
        switch (day) {
            case 1:
                return "Sun";
            case 2:
                return "Mon";
            case 3:
                return "Tue";
            case 4:
                return "Wed";
            case 5:
                return "Thu";
            case 6:
                return "Fri";
            case 7:
                return "Sat";
            default:
                return "Sun";
        }
    }

    public void showPMenu(){
        KidsVo kidsVo = MHDApplication.getInstance().getMHDSvcManager().getKidsVo();
        List<PowerMenuItem> kidsList = new ArrayList();
        for(int k=0; k<kidsVo.getCnt(); k++){
            kidsList.add(new PowerMenuItem(kidsVo.getMsg().get(k).getName(), k == 0 ? true : false));
        }
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
                .setSelectedMenuColor(ContextCompat.getColor(getActivity(), R.color.powermenu_select))
                .setOnMenuItemClickListener(onMenuItemClickListener)
                .setDivider(new ColorDrawable(ContextCompat.getColor(getActivity(), R.color.gray))) // sets a divider.
                .setDividerHeight(1)
                .setHeaderView(null)
                .setFooterView(null)
                .build();

        powerMenu.showAsDropDown(vst_top_title);
    }
}
