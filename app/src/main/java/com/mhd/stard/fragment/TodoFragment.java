package com.mhd.stard.fragment;

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
import com.mhd.stard.MainActivity;
import com.mhd.stard.R;
import com.mhd.stard.adapter.ReCyclerAdapter;
import com.mhd.stard.common.MHDApplication;
import com.mhd.stard.common.vo.KidsVo;
import com.mhd.stard.common.vo.MenuVo;
import com.mhd.stard.common.vo.TodoData;
import com.mhd.stard.common.vo.TodoVo;
import com.mhd.stard.network.MHDNetworkInvoker;
import com.mhd.stard.util.MHDLog;
import com.mhd.stard.view.RecyclerDecoration;
import com.skydoves.powermenu.MenuAnimation;
import com.skydoves.powermenu.OnMenuItemClickListener;
import com.skydoves.powermenu.PowerMenu;
import com.skydoves.powermenu.PowerMenuItem;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class TodoFragment extends BaseFragment {
    private RecyclerView recyclerView;
    private ReCyclerAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    PowerMenu powerMenu = null;
    TextView vst_top_title, tv_todo_subject_section, tv_area_month, tv_area_month_move, tv_area_month_past, tv_area_month_next;
    String displayKid = "";
    int displayKidPosition = 0;
    int weekd = 1;
    String displayDays = "";

    LinearLayout btn_past_3, btn_past_2, btn_past_1, btn_todoy, btn_next_1, btn_next_2, btn_next_3, ll_area_days, tv_no_data;
    TextView tv_past_3_day, tv_past_2_day, tv_past_1_day, tv_today_day, tv_next_1_day, tv_next_2_day, tv_next_3_day;
    TextView tv_past_3_week, tv_past_2_week, tv_past_1_week, tv_today_week, tv_next_1_week, tv_next_2_week, tv_next_3_week;
    TextView tv_area_today;

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
        tv_no_data = (LinearLayout) root.findViewById(R.id.tv_no_data);
        tv_area_month = (TextView) root.findViewById(R.id.tv_area_month);
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
                displayKidPosition = 0;
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

        tv_area_month_move = (TextView) root.findViewById(R.id.tv_area_month_move);
        tv_area_month_past = (TextView) root.findViewById(R.id.tv_area_month_past);
        tv_area_month_past.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //cal 에 특정 날짜를 지정해서 그거 기준으로 아래를 계산해야 한다.
                Calendar cal = Calendar.getInstance();
                cal.set(Calendar.YEAR, Integer.parseInt(displayDays.substring(0, 4)));
                cal.set(Calendar.MONTH, Integer.parseInt(displayDays.substring(5, 7))-1);
                cal.set(Calendar.DATE, Integer.parseInt(displayDays.substring(8, 10)));
                cal.set(Calendar.HOUR, 0);
                cal.set(Calendar.MINUTE, 0);
                cal.set(Calendar.SECOND, 0);
                weekd = cal.get(Calendar.DAY_OF_WEEK); // 현재 기준 날짜, 요일, displayDays 가 기준날짜.
                DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                cal.add(Calendar.MONTH, -1); // -1 month 의 표시될 날짜
                tv_past_3_day.setText(df.format(cal.getTime()).substring(8)); // 표시되어질 날짜 일수
                tv_past_3_week.setText(selectWeek(cal.get(Calendar.DAY_OF_WEEK))); // 표시되어질 요일
                // 이동할 날짜
                final String tmpDays = df.format(cal.getTime());
                final int tmpWeek = cal.get(Calendar.DAY_OF_WEEK);
                // 서버 조회
                changeDays(tmpDays, tmpWeek);
            }
        });
        tv_area_month_next = (TextView) root.findViewById(R.id.tv_area_month_next);
        tv_area_month_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //cal 에 특정 날짜를 지정해서 그거 기준으로 아래를 계산해야 한다.
                Calendar cal = Calendar.getInstance();
                cal.set(Calendar.YEAR, Integer.parseInt(displayDays.substring(0, 4)));
                cal.set(Calendar.MONTH, Integer.parseInt(displayDays.substring(5, 7))-1);
                cal.set(Calendar.DATE, Integer.parseInt(displayDays.substring(8, 10)));
                cal.set(Calendar.HOUR, 0);
                cal.set(Calendar.MINUTE, 0);
                cal.set(Calendar.SECOND, 0);
                weekd = cal.get(Calendar.DAY_OF_WEEK); // 현재 기준 날짜, 요일, displayDays 가 기준날짜.
                DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                cal.add(Calendar.MONTH, 1); // -1 month 의 표시될 날짜
                tv_past_3_day.setText(df.format(cal.getTime()).substring(8)); // 표시되어질 날짜 일수
                tv_past_3_week.setText(selectWeek(cal.get(Calendar.DAY_OF_WEEK))); // 표시되어질 요일
                // 이동할 날짜
                final String tmpDays = df.format(cal.getTime());
                final int tmpWeek = cal.get(Calendar.DAY_OF_WEEK);
                // 서버 조회
                changeDays(tmpDays, tmpWeek);
            }
        });
        tv_area_today = (TextView) root.findViewById(R.id.tv_area_today);
        tv_area_today.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                displayDays = df.format(cal.getTime());
                setTopWeek(); // weekd 저장됨
                changeDays(displayDays, weekd);
            }
        });
        Calendar cal = Calendar.getInstance();
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        displayDays = df.format(cal.getTime());
        MHDLog.d("calendar 시작", df.format(cal.getTime()));
        setTopWeek();

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

        recyclerView = (RecyclerView) root.findViewById(R.id.recv_receiving);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(mContext);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new ReCyclerAdapter();
        recyclerView.setAdapter(adapter);
        RecyclerDecoration recyclerDecoration = new RecyclerDecoration(1, R.color.gray);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);

        adapter.setOnItemClickListener(new ReCyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                // position을 가지고 라인을 알아낸 다음에
                // 해당 라인의 메뉴를 띄우려 했는데 이건 일단 보류. 안할 수도 있다.
                // 클릭하면 바로 수정화면으로 넘기고, 거기에 삭제버튼을 만든다.
                // 지난 데이타는 수정하지 못하게 한다.
                if(checkToday(displayDays))
                    ((MainActivity)getActivity()).startTodoModify(position);
                else
                    Toast.makeText(mContext, "지난 내역은 수정할 수 없습니다.", Toast.LENGTH_SHORT).show();
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
            displayKidPosition = position;
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
            Map<String, String> params = new HashMap<String, String>();
            //params.put("UUID", MHDApplication.getInstance().getMHDSvcManager().getDeviceNewUuid());
            params.put("UUMAIL", MHDApplication.getInstance().getMHDSvcManager().getUserVo().getUuMail());
            params.put("TKNAME", displayKid);
            params.put("TDDATE", displayDays);
            params.put("TDWEEKD", String.valueOf(weekd));
            //과거냐 아니냐를 보내는 것이 낫다. 과거라면 완료된 것도 가져온다.checkToday
            params.put("TDPAST", checkToday(displayDays) ? "N" : "Y");
            MHDLog.d("queryTodo", checkToday(displayDays) ? "N" : "Y");

            MHDNetworkInvoker.getInstance().sendVolleyRequest(((MainActivity)getActivity()), R.string.url_restapi_query_todo, params, ((MainActivity)getActivity()).responseListener);
        } catch (Exception e) {
            MHDLog.printException(e);
        }
    }
    public void changeDays(String tddate, int weekdd){
        // 여기서 기준날짜를 변경했는데, 데이터를 가져오다가 에러가 난다면..
        // 기준날짜와 보여지는 것에서 어긋남이 발생할 수도 있다.
        displayDays = tddate;
        weekd = weekdd;

        queryTodo();
    }
    /**
     * BaseActivity에서 상속받지 못하기 때문에 parent Activity에서 받아서 현재 fragment의 function을 호출하도록 처리
     */
    public boolean networkResponseProcess(String nvMsg, int nvCnt, String result) {
        TodoVo todoVo = null;
        adapter.deleteAll();
        adapter.setQueryDays(displayDays);

        tv_no_data.setVisibility(View.GONE);
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
            data.setComplete(todoVo.getMsg().get(i).getTdcomplete());
            data.setIdx(todoVo.getMsg().get(i).getIdx());
            data.setStart(todoVo.getMsg().get(i).getTdstart());
            data.setEnd(todoVo.getMsg().get(i).getTdend());
            data.setUse(todoVo.getMsg().get(i).getTduse());
            data.setTdpc(todoVo.getMsg().get(i).getTdpc());
            data.setKid(displayKid);

            // 각 값이 들어간 data를 adapter에 추가합니다.
            adapter.addItem(data);
        }

        // adapter의 값이 변경되었다는 것을 알려줍니다.
        adapter.notifyDataSetChanged();

        // 기준 날짜가 변경된대로 날짜버튼 새로 설정.
        setTopWeek();

        return true;
    }

    public void noData(String nvApiParam) {
        tv_no_data.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);

        setTopWeek();
    }

    public String selectWeek(int day) {
        switch (day) {
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
    /*
    //
    */
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

        powerMenu.setSelectedPosition(displayKidPosition);
        powerMenu.showAsDropDown(vst_top_title, 600, 0);
    }

    public void setTopWeek(){
        //cal 에 특정 날짜를 지정해서 그거 기준으로 아래를 계산해야 한다.
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, Integer.parseInt(displayDays.substring(0, 4)));
        cal.set(Calendar.MONTH, Integer.parseInt(displayDays.substring(5, 7))-1);
        cal.set(Calendar.DATE, Integer.parseInt(displayDays.substring(8, 10)));
        cal.set(Calendar.HOUR, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        tv_area_month.setText(displayDays.substring(5, 7));
        tv_area_month_move.setText(displayDays.substring(0, 4)+"."+displayDays.substring(5, 7));

        weekd = cal.get(Calendar.DAY_OF_WEEK); // 기준 날짜 요일, displayDays 가 기준날짜.
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");

        MHDLog.d("calendar", df.format(cal.getTime()));

        tv_today_day.setText(df.format(cal.getTime()).substring(8));
        tv_today_week.setText(selectWeek(weekd));
        cal.add(Calendar.DATE, -3);
        tv_past_3_day.setText(df.format(cal.getTime()).substring(8));
        tv_past_3_week.setText(selectWeek(cal.get(Calendar.DAY_OF_WEEK)));
        final String tmpDays = df.format(cal.getTime());
        final int tmpWeek = cal.get(Calendar.DAY_OF_WEEK);
        btn_past_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { changeDays(tmpDays, tmpWeek); }
        });
        cal.add(Calendar.DATE, 1);
        tv_past_2_day.setText(df.format(cal.getTime()).substring(8));
        tv_past_2_week.setText(selectWeek(cal.get(Calendar.DAY_OF_WEEK)));
        final String tmpDays2 = df.format(cal.getTime());
        final int tmpWeek2 = cal.get(Calendar.DAY_OF_WEEK);
        btn_past_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { changeDays(tmpDays2, tmpWeek2); }
        });
        cal.add(Calendar.DATE, 1);
        tv_past_1_day.setText(df.format(cal.getTime()).substring(8));
        tv_past_1_week.setText(selectWeek(cal.get(Calendar.DAY_OF_WEEK)));
        final String tmpDays3 = df.format(cal.getTime());
        final int tmpWeek3 = cal.get(Calendar.DAY_OF_WEEK);
        btn_past_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { changeDays(tmpDays3, tmpWeek3); }
        });
        cal.add(Calendar.DATE, 2);
        tv_next_1_day.setText(df.format(cal.getTime()).substring(8));
        tv_next_1_week.setText(selectWeek(cal.get(Calendar.DAY_OF_WEEK)));
        final String tmpDays4 = df.format(cal.getTime());
        final int tmpWeek4 = cal.get(Calendar.DAY_OF_WEEK);
        btn_next_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { changeDays(tmpDays4, tmpWeek4); }
        });
        cal.add(Calendar.DATE, 1);
        tv_next_2_day.setText(df.format(cal.getTime()).substring(8));
        tv_next_2_week.setText(selectWeek(cal.get(Calendar.DAY_OF_WEEK)));
        final String tmpDays5 = df.format(cal.getTime());
        final int tmpWeek5 = cal.get(Calendar.DAY_OF_WEEK);
        btn_next_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { changeDays(tmpDays5, tmpWeek5); }
        });
        cal.add(Calendar.DATE, 1);
        tv_next_3_day.setText(df.format(cal.getTime()).substring(8));
        tv_next_3_week.setText(selectWeek(cal.get(Calendar.DAY_OF_WEEK)));
        final String tmpDays6 = df.format(cal.getTime());
        final int tmpWeek6 = cal.get(Calendar.DAY_OF_WEEK);
        btn_next_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { changeDays(tmpDays6, tmpWeek6); }
        });
    }

    public boolean checkToday(String rdate) {
        Calendar cal = Calendar.getInstance();
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String tdate = df.format(cal.getTime());
        Date rdate_d = null;
        Date tdate_d = null;
        try {
            rdate_d = df.parse(rdate);
            tdate_d = df.parse(tdate);
        } catch(ParseException e) {
            e.printStackTrace();
        }

        int compare = rdate_d.compareTo(tdate_d);
        if (compare > 0) { // 미래
            return true;
        } else if (compare < 0) { // 과거
            return false;
        } else {
            return true;
        }
    }
}
