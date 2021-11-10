package com.mhd.stard.fragment;

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
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.gson.Gson;
import com.mhd.stard.MainActivity;
import com.mhd.stard.R;
import com.mhd.stard.adapter.ReCyclerAdapter;
import com.mhd.stard.adapter.ReCyclerEndAdapter;
import com.mhd.stard.common.MHDApplication;
import com.mhd.stard.common.XYMarkerView;
import com.mhd.stard.common.vo.KidsVo;
import com.mhd.stard.common.vo.MenuVo;
import com.mhd.stard.common.vo.SumVo;
import com.mhd.stard.common.vo.TodoData;
import com.mhd.stard.common.vo.TodoEndData;
import com.mhd.stard.common.vo.TodoEndVo;
import com.mhd.stard.common.vo.TodoVo;
import com.mhd.stard.network.MHDNetworkInvoker;
import com.mhd.stard.util.MHDLog;
import com.mhd.stard.view.RecyclerDecoration;
import com.skydoves.powermenu.MenuAnimation;
import com.skydoves.powermenu.OnMenuItemClickListener;
import com.skydoves.powermenu.PowerMenu;
import com.skydoves.powermenu.PowerMenuItem;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SumFragment extends BaseFragment {
    private RecyclerView recyclerView;
    private ReCyclerEndAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    PowerMenu powerMenu = null;
    TextView vst_top_title, pieChartBlank;
    LinearLayout tv_no_data, ll_area_sum, ll_area_end;
    BarChart barChart;
    PieChart pieChart;
    String displayKid = "";
    int displayKidPosition = 0;
    SumVo sumVo = null;
    String startDate = "";
    String endDate = "";
    String twoDate = "";
    String threeDate = "";
    String fourDate = "";
    String fiveDate = "";
    String sixDate = "";

    AppCompatButton btn_report, btn_todo_end;


    public static SumFragment create() {
        return new SumFragment();
    }

    @Override
    public int getLayoutResId() {
        return R.layout.fragment_sum;
    }

    @Override
    public void inOnCreateView(View root, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        tv_no_data = (LinearLayout) root.findViewById(R.id.tv_no_data);
        ll_area_sum = (LinearLayout) root.findViewById(R.id.ll_area_sum);
        ll_area_end = (LinearLayout) root.findViewById(R.id.ll_area_end);
        pieChartBlank = (TextView) root.findViewById(R.id.pieChartBlank);
        vst_top_title = (TextView) root.findViewById(R.id.vst_top_title);
        barChart = (BarChart) root.findViewById(R.id.barChart);
        pieChart = (PieChart) root.findViewById(R.id.pieChart);

        btn_report = (AppCompatButton) root.findViewById(R.id.btn_report);
        btn_report.setOnClickListener(new View.OnClickListener() { // 학습리포트 처리
            @Override
            public void onClick(View v) {
                //// UI 셋팅
                btn_report.setBackgroundResource(R.drawable.bottom_border_padding_selected);
                btn_todo_end.setBackgroundResource(R.drawable.bottom_border_padding);
                ll_area_sum.setVisibility(View.VISIBLE);
                ll_area_end.setVisibility(View.GONE);
                //// 기본화면이 차트이기 때문에 이것을 클릭하기 전에 이미 차트는 그려져있다.
                //// 다시 조회를 안해도 될 수 있다. 그렇다면 querySum을 주석처리 하기만 하면 된다.
                querySum(startDate, endDate);

                vst_top_title.setText("[ "+displayKid+" ] 리포트");
            }
        });
        btn_todo_end = (AppCompatButton) root.findViewById(R.id.btn_todo_end);
        btn_todo_end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_report.setBackgroundResource(R.drawable.bottom_border_padding);
                btn_todo_end.setBackgroundResource(R.drawable.bottom_border_padding_selected);
                ll_area_sum.setVisibility(View.GONE);
                ll_area_end.setVisibility(View.VISIBLE);

                queryEnd();

                vst_top_title.setText("[ "+displayKid+" ] 완료학습");
            }
        });

        KidsVo kidsVo = MHDApplication.getInstance().getMHDSvcManager().getKidsVo();
        MenuVo menuVo = MHDApplication.getInstance().getMHDSvcManager().getMenuVo();
        List<PowerMenuItem> kidsList = new ArrayList();
        for(int k=0; k<kidsVo.getCnt(); k++){
            kidsList.add(new PowerMenuItem(kidsVo.getMsg().get(k).getName(), k == 0 ? true : false));
        }
        for(int k=0; k<menuVo.getMsg().size(); k++){
            if("TO".equals(menuVo.getMsg().get(k).getMenuname())){
                // 해당메뉴에 설정된 아이정보
                displayKid = menuVo.getMsg().get(k).getKidname();
                displayKidPosition = 0;
            }
        }
        vst_top_title.setText("[ "+displayKid+" ] 리포트");

        //// 완료학습 관련
        recyclerView = (RecyclerView) root.findViewById(R.id.recv_endtodo);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(mContext);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new ReCyclerEndAdapter();
        recyclerView.setAdapter(adapter);
        RecyclerDecoration recyclerDecoration = new RecyclerDecoration(1, R.color.gray);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);

        adapter.setOnItemClickListener(new ReCyclerEndAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                // position을 가지고 라인을 알아낸 다음에
                // 아이템 클릭 이벤트를 건다. 현재로서는 기능 걸지 않는다.
//                if(checkFuture(displayDays))
//                    ((MainActivity)getActivity()).startTodoModify(position);
//                else
//                    Toast.makeText(mContext, "지난 내역은 수정할 수 없습니다.", Toast.LENGTH_SHORT).show();
            }
        }) ;

//        initChart();
        Calendar cal = Calendar.getInstance();
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        cal.set(Calendar.DAY_OF_WEEK, 1); // 현재 주 일요일로 셋팅. 한 주의 시작.
        startDate = df.format(cal.getTime());
        cal.add(Calendar.DATE, 1); // 현재 주 월요일로 셋팅.
        twoDate = df.format(cal.getTime());
        cal.add(Calendar.DATE, 1); // 현재 주 화요일로 셋팅.
        threeDate = df.format(cal.getTime());
        cal.add(Calendar.DATE, 1); // 현재 주 수요일로 셋팅.
        fourDate = df.format(cal.getTime());
        cal.add(Calendar.DATE, 1); // 현재 주 목요일로 셋팅.
        fiveDate = df.format(cal.getTime());
        cal.add(Calendar.DATE, 1); // 현재 주 금요일로 셋팅.
        sixDate = df.format(cal.getTime());
        cal.add(Calendar.DATE, 1); // 현재 주 토요일로 셋팅. 한 주의 끝.
        endDate = df.format(cal.getTime());

        querySum(startDate, endDate);
    }

    @Override
    public void batchFunction(String api) {
//        if(api.equals(getString(R.string.api_editor_clear))) {
//            // editor 내용 초기화.
//            editor.clearAllContents();
//        }
    }
    public void noData(String nvApiParam) {
        tv_no_data.setVisibility(View.VISIBLE);
        ll_area_sum.setVisibility(View.GONE);
        ll_area_end.setVisibility(View.GONE);
    }

    /**
     * query end todo
     * 리스트 초기화도 포함
     */
    public void queryEnd() {
        try {
            Map<String, String> params = new HashMap<String, String>();
            //params.put("UUID", MHDApplication.getInstance().getMHDSvcManager().getDeviceNewUuid());
            params.put("UUMAIL", MHDApplication.getInstance().getMHDSvcManager().getUserVo().getUuMail());
            params.put("TKNAME", displayKid);
            //완료된 학습에서는 필요없는 파라미터들
            params.put("TDDATE", "");
            params.put("TDWEEKD", "");
            params.put("TDPAST", "");

            MHDNetworkInvoker.getInstance().sendVolleyRequest(((MainActivity)getActivity()), R.string.url_restapi_query_end, params, ((MainActivity)getActivity()).responseListener);
        } catch (Exception e) {
            MHDLog.printException(e);
        }
    }
    /**
     * query sum
     */
    public void querySum(String sdate, String edate){
        try {
            Map<String, String> params = new HashMap<String, String>();
            //params.put("UUID", MHDApplication.getInstance().getMHDSvcManager().getDeviceNewUuid());
            params.put("UUMAIL", MHDApplication.getInstance().getMHDSvcManager().getUserVo().getUuMail());
            params.put("TDKID", displayKid);
            params.put("TDSDATE", sdate);
            params.put("TDEDATE", edate);

            MHDNetworkInvoker.getInstance().sendVolleyRequest(((MainActivity)getActivity()), R.string.url_restapi_query_sum, params, ((MainActivity)getActivity()).responseListener);
        } catch (Exception e) {
            MHDLog.printException(e);
        }
    }
    /**
     * BaseActivity에서 상속받지 못하기 때문에 parent Activity에서 받아서 현재 fragment의 function을 호출하도록 처리
     */
    public boolean networkResponseProcess(String nvMsg, int nvCnt, String nvMsg2, int nvCnt2, String result) {
        tv_no_data.setVisibility(View.GONE);
        ll_area_sum.setVisibility(View.VISIBLE);
        ll_area_end.setVisibility(View.GONE);

//        if (nvCnt == 0) {
//            // Bar 차트 정보가 없으면
//            Toast.makeText(mContext, nvMsg, Toast.LENGTH_SHORT).show();
//        } else {
            //// 통계정보를 받아옴.
            //// 데이터가 실제로는 없다고 해도 빈 값이라도 들어오니 나중에 출력하는 부분에서 체크한다.
            Gson gson = new Gson();
            sumVo = gson.fromJson(result, SumVo.class);
            MHDApplication.getInstance().getMHDSvcManager().setSumVo(null);
            MHDApplication.getInstance().getMHDSvcManager().setSumVo(sumVo);

            // bar 차트를 표시한다.
            getChartData();

            // pie 차트를 표시한다.
            setupPieChart();
            loadPieChartData();
//        }

        return true;
    }
    public boolean networkResponseProcess_end(String nvMsg, int nvCnt, String result) {
        tv_no_data.setVisibility(View.GONE);
        ll_area_sum.setVisibility(View.GONE);
        ll_area_end.setVisibility(View.VISIBLE);

        TodoEndVo todoEndVo = null;
        adapter.deleteAll();

        if (nvCnt == 0) {
            // 정보가 없으면
            Toast.makeText(mContext, nvMsg, Toast.LENGTH_SHORT).show();
        } else {
            // 할일정보를 받아옴.
            Gson gson = new Gson();
            todoEndVo = gson.fromJson(result, TodoEndVo.class);
            MHDApplication.getInstance().getMHDSvcManager().setTodoEndVo(null);
            MHDApplication.getInstance().getMHDSvcManager().setTodoEndVo(todoEndVo);
        }

        for(int i=0; i<nvCnt; i++){
            // 각 List의 값들을 data 객체에 set 해줍니다.
            TodoEndData data = new TodoEndData();
            data.setSubject(todoEndVo.getMsg().get(i).getSubject());
            data.setDetail(todoEndVo.getMsg().get(i).getDetail());
            data.setDaily(todoEndVo.getMsg().get(i).getOneday());
            data.setTotal(todoEndVo.getMsg().get(i).getTotal());
            data.setRest(todoEndVo.getMsg().get(i).getRest());
            data.setTitle(todoEndVo.getMsg().get(i).getTitle());
            data.setSection(todoEndVo.getMsg().get(i).getSection());
            data.setIdx(todoEndVo.getMsg().get(i).getIdx());
            data.setStart(todoEndVo.getMsg().get(i).getTdstart());
            data.setTddate(todoEndVo.getMsg().get(i).getTddate());
            data.setTdenddate(todoEndVo.getMsg().get(i).getTdenddate());
            data.setKid(displayKid);

            // 각 값이 들어간 data를 adapter에 추가합니다.
            adapter.addItem(data);
        }

        // adapter의 값이 변경되었다는 것을 알려줍니다.
        adapter.notifyDataSetChanged();

        return true;
    }

    public void initBarChart() {
        barChart.clear();

        barChart.setMaxVisibleValueCount(7);
        barChart.setPinchZoom(false);
        barChart.setDrawBarShadow(false);
        barChart.setDrawGridBackground(false);
        barChart.setTouchEnabled(true);
        barChart.setDoubleTapToZoomEnabled(false);
        barChart.getLegend().setEnabled(true);
        barChart.getDescription().setEnabled(false);

        barChart.getAxisLeft().setDrawGridLines(false);
        barChart.getAxisLeft().setDrawLabels(true);
        barChart.getAxisLeft().setEnabled(true);
        barChart.getAxisLeft().mAxisMaximum = 100f;
        barChart.getAxisLeft().mAxisMinimum = 0f;
        barChart.getAxisLeft().setAxisLineColor(R.color.red);
        barChart.getAxisLeft().setTextColor(R.color.black_overlay);
        barChart.getAxisLeft().setSpaceBottom(0);
        barChart.getAxisLeft().setDrawTopYLabelEntry(true);

        barChart.getAxisRight().setDrawGridLines(false);
        barChart.getAxisRight().setDrawLabels(false);
        barChart.getAxisRight().setEnabled(false);

        barChart.getXAxis().setEnabled(true);
        barChart.getXAxis().setDrawGridLines(false);
        barChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        barChart.getXAxis().setTextColor(R.color.black_overlay);

        barChart.setDrawValueAboveBar(true);

    }

    public void getChartData(){
        //// Bar 차트에 표시할 X, Y축 정보를 생성, 저장.
        ArrayList<BarEntry> entries = new ArrayList<BarEntry>();
        final ArrayList<String> xLabels = new ArrayList<String>();

        int dCount = sumVo.getCnt();
        ////성취율 정보가 없다면 모두 기본값 0으로 셋팅된다. 그렇게 Bar는 축이라도 그려지기 때문에 빈 차트로 표시한다.
        float[] yValue = {0, 0, 0, 0, 0, 0, 0};
        String[] xLabel = {"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};
        for (int i=0; i<dCount; i++) { // 받아온 값 중에서...
            if (startDate.equals(sumVo.getMsg().get(i).getThdate())) { // 일요일, 해당 날짜 데이타가 있다면
                yValue[0] = Float.parseFloat(sumVo.getMsg().get(i).getAver());
            }
            if (twoDate.equals(sumVo.getMsg().get(i).getThdate())) { // 월요일, 해당 날짜 데이타가 있다면
                yValue[1] = Float.parseFloat(sumVo.getMsg().get(i).getAver());
            }
            if (threeDate.equals(sumVo.getMsg().get(i).getThdate())) { // 화요일, 해당 날짜 데이타가 있다면
                yValue[2] = Float.parseFloat(sumVo.getMsg().get(i).getAver());
            }
            if (fourDate.equals(sumVo.getMsg().get(i).getThdate())) { // 수요일, 해당 날짜 데이타가 있다면
                yValue[3] = Float.parseFloat(sumVo.getMsg().get(i).getAver());
            }
            if (fiveDate.equals(sumVo.getMsg().get(i).getThdate())) { // 목요일, 해당 날짜 데이타가 있다면
                yValue[4] = Float.parseFloat(sumVo.getMsg().get(i).getAver());
            }
            if (sixDate.equals(sumVo.getMsg().get(i).getThdate())) { // 금요일, 해당 날짜 데이타가 있다면
                yValue[5] = Float.parseFloat(sumVo.getMsg().get(i).getAver());
            }
            if (endDate.equals(sumVo.getMsg().get(i).getThdate())) { // 토요일, 해당 날짜 데이타가 있다면
                yValue[6] = Float.parseFloat(sumVo.getMsg().get(i).getAver());
            }
        }
        entries.add(new BarEntry(0, yValue[0], xLabel[0]));
        entries.add(new BarEntry(1, yValue[1], xLabel[1]));
        entries.add(new BarEntry(2, yValue[2], xLabel[2]));
        entries.add(new BarEntry(3, yValue[3], xLabel[3]));
        entries.add(new BarEntry(4, yValue[4], xLabel[4]));
        entries.add(new BarEntry(5, yValue[5], xLabel[5]));
        entries.add(new BarEntry(6, yValue[6], xLabel[6]));

        //// 차트 초기화.
        initBarChart();

        ////////////////////
        //// 차트 custom 셋팅.
        ////////////////////
        //// X축 Label 셋팅
        IndexAxisValueFormatter indexAxisValueFormatter = new IndexAxisValueFormatter(){
            @Override
            public String getFormattedValue(float value) {
                return xLabel[(int) value];
            }
        };
        barChart.getXAxis().setValueFormatter(indexAxisValueFormatter);
        barChart.getXAxis().setTextSize(14);
        //// Y축 Label 셋팅
        IndexAxisValueFormatter indexAxisValueFormatterY = new IndexAxisValueFormatter(){
            @Override
            public String getFormattedValue(float value) {
                return String.valueOf(Math.round(value)) + "%";
            }
        };
        barChart.getAxisLeft().setValueFormatter(indexAxisValueFormatterY);
        barChart.getAxisLeft().setTextSize(14);
        //// 범례
        barChart.getLegend().setTextSize(15);
        barChart.getLegend().setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        //// 100 % 맞추기
        float max = yValue[0];
        for(int i=0; i<yValue.length; i++){
            if(yValue[i] > max){
                max = yValue[i];
            }
        }
        barChart.getAxisLeft().setSpaceTop((100-max)/max*100); // 최대치 100% 를 만들기 위해.
        barChart.getLegend().setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        barChart.setExtraBottomOffset(10);

        //// bar 클릭 시 상단에 marker 표시.
        XYMarkerView mv = new XYMarkerView(mContext, indexAxisValueFormatter);
        mv.setChartView(barChart); // For bounds control
        barChart.setMarker(mv); // Set the marker to the chart

        //// bar data 구성
        BarDataSet dataSet = new BarDataSet(entries, "주간 학습 성취율");
        dataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        BarData data = new BarData(dataSet);
        data.setBarWidth(0.7f);
        barChart.setData(data);
        barChart.animateY(800, Easing.EaseInOutQuad);
        barChart.invalidate();
    }

    private void setupPieChart() {
        pieChart.setDrawHoleEnabled(true);
        pieChart.setUsePercentValues(true);
        pieChart.setEntryLabelTextSize(15);
        pieChart.setEntryLabelColor(Color.BLACK);
        pieChart.setCenterText("주간 학습 비중");
        pieChart.setCenterTextSize(16);
        pieChart.getDescription().setEnabled(false);

        Legend legend = pieChart.getLegend();
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        legend.setOrientation(Legend.LegendOrientation.VERTICAL);
        legend.setTextSize(13);
        legend.setDrawInside(true);
        legend.setEnabled(true);
    }

    private void loadPieChartData() {
        int pieCount = sumVo.getCn2();
        if(pieCount == 0){
            //// 차트를 표시하지 않는다.
            //// 주간 학습정보가 존재하지 않습니다.
            pieChartBlank.setVisibility(View.VISIBLE);
            pieChart.setVisibility(View.GONE);
        }else{
            pieChartBlank.setVisibility(View.GONE);
            pieChart.setVisibility(View.VISIBLE);

            ArrayList<PieEntry> pieEntries = new ArrayList<>();
            float[] yValue = new float[pieCount];
            for(int j=0; j<pieCount; j++){
                yValue[j] = Float.parseFloat(sumVo.getSub().get(j).getScount());
            }
            String[] xLabel = new String[pieCount];
            for(int k=0; k<pieCount; k++){
                xLabel[k] = sumVo.getSub().get(k).getTbsubject();
            }
            for(int m=0; m<pieCount; m++){
                pieEntries.add(new PieEntry(yValue[m], xLabel[m]));
            }

            ArrayList<Integer> colors = new ArrayList<>();
            for(int color: ColorTemplate.MATERIAL_COLORS){
                colors.add(color);
            }
            for(int color: ColorTemplate.VORDIPLOM_COLORS){
                colors.add(color);
            }

            PieDataSet pieDataSet = new PieDataSet(pieEntries, "과목별");
            pieDataSet.setColors(colors);

            PieData pieData = new PieData(pieDataSet);
            pieData.setDrawValues(true);
            PercentFormatter percentFormatter = new PercentFormatter(pieChart){
                @Override
                public String getFormattedValue(float value) {
                    return String.valueOf(Math.round(value)) + "%";
                }
            };
            pieData.setValueFormatter(percentFormatter);
            pieData.setValueTextSize(14);
            pieData.setValueTextColor(R.color.black_overlay);

            pieChart.setData(pieData);
            pieChart.animateY(800, Easing.EaseInOutQuad);
            pieChart.invalidate();
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

        powerMenu.setSelectedPosition(displayKidPosition);
        powerMenu.showAsDropDown(vst_top_title, 600, 0);
    }
    private OnMenuItemClickListener<PowerMenuItem> onMenuItemClickListener = new OnMenuItemClickListener<PowerMenuItem>() {
        @Override
        public void onItemClick(int position, PowerMenuItem item) {
            displayKid = item.getTitle().toString();
            if(ll_area_sum.getVisibility() == View.VISIBLE)
                vst_top_title.setText("[ "+displayKid+" ] 리포트");
            else
                vst_top_title.setText("[ "+displayKid+" ] 완료학습");
            powerMenu.setSelectedPosition(position); // change selected item
            displayKidPosition = position;
            // MenuVo 정보를 갱신
            MenuVo menuVo = MHDApplication.getInstance().getMHDSvcManager().getMenuVo();
            for(int k=0; k<menuVo.getMsg().size(); k++){
                if("SE".equals(menuVo.getMsg().get(k).getMenuname())){
                    // 해당메뉴에 설정된 아이정보
                    menuVo.getMsg().get(k).setKidname(displayKid);
                    if(ll_area_sum.getVisibility() == View.VISIBLE)
                        querySum(startDate, endDate);
                    else
                        queryEnd();

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
                    .setSelectedMenuColor(ContextCompat.getColor(getActivity(), R.color.colorPrimary))
                    .setOnMenuItemClickListener(onMenuItemClickListener)
                    .setDivider(new ColorDrawable(ContextCompat.getColor(getActivity(), R.color.gray))) // sets a divider.
                    .setDividerHeight(1)
                    .setHeaderView(null)
                    .setFooterView(null)
                    .build();
        }
    }
}
