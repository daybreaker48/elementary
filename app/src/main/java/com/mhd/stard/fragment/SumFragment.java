package com.mhd.stard.fragment;

import android.os.Bundle;
import androidx.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.gson.Gson;
import com.mhd.stard.MainActivity;
import com.mhd.stard.R;
import com.mhd.stard.common.MHDApplication;
import com.mhd.stard.common.vo.KidsVo;
import com.mhd.stard.common.vo.MenuVo;
import com.mhd.stard.common.vo.SumVo;
import com.mhd.stard.common.vo.TodoData;
import com.mhd.stard.common.vo.TodoVo;
import com.mhd.stard.network.MHDNetworkInvoker;
import com.mhd.stard.util.MHDLog;
import com.skydoves.powermenu.PowerMenuItem;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SumFragment extends BaseFragment {
    TextView vst_top_title;
    LinearLayout tv_no_data, ll_area_sum;
    BarChart barChart;
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


    public static SumFragment create() {
        return new SumFragment();
    }

    @Override
    public int getLayoutResId() {
        return R.layout.fragment_sum;
    }

    @Override
    public void inOnCreateView(View root, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //Statusbar 아래로 내리기
//        final TextView mTitle = (TextView) root.findViewById(R.id.vst_top_title);
//        RelativeLayout.LayoutParams mLayoutParams = (RelativeLayout.LayoutParams) mTitle.getLayoutParams();
//        mLayoutParams.topMargin = Util.getInstance().getStatusBarHeight(root.getContext());
//        mTitle.setLayoutParams(mLayoutParams);

        tv_no_data = (LinearLayout) root.findViewById(R.id.tv_no_data);
        ll_area_sum = (LinearLayout) root.findViewById(R.id.ll_area_sum);
        vst_top_title = (TextView) root.findViewById(R.id.vst_top_title);
        barChart = (BarChart) root.findViewById(R.id.barChart);

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

//        initChart();
        Calendar cal = Calendar.getInstance();
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        cal.add(Calendar.DATE, -7); // 현재 주 월요일로 셋팅.
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
    public boolean networkResponseProcess(String nvMsg, int nvCnt, String result) {
        tv_no_data.setVisibility(View.GONE);
        ll_area_sum.setVisibility(View.VISIBLE);

        if (nvCnt == 0) {
            // 정보가 없으면
            Toast.makeText(mContext, nvMsg, Toast.LENGTH_SHORT).show();
        } else {
            // 할일정보를 받아옴.
            Gson gson = new Gson();
            sumVo = gson.fromJson(result, SumVo.class);
            MHDApplication.getInstance().getMHDSvcManager().setSumVo(null);
            MHDApplication.getInstance().getMHDSvcManager().setSumVo(sumVo);
        }

        getChartData();
//        for(int i=0; i<nvCnt; i++){
//            // 각 List의 값들을 data 객체에 set 해줍니다.
//            TodoData data = new TodoData();
//            data.setSubject(todoVo.getMsg().get(i).getSubject());
//            data.setDetail(todoVo.getMsg().get(i).getDetail());
//            data.setDaily(todoVo.getMsg().get(i).getOneday());
//            data.setTotal(todoVo.getMsg().get(i).getTotal());
//            data.setRest(todoVo.getMsg().get(i).getRest());
//            data.setGoal(todoVo.getMsg().get(i).getGoal());
//            data.setSun(todoVo.getMsg().get(i).getSun());
//            data.setMon(todoVo.getMsg().get(i).getMon());
//            data.setTue(todoVo.getMsg().get(i).getTue());
//            data.setWed(todoVo.getMsg().get(i).getWed());
//            data.setThu(todoVo.getMsg().get(i).getThu());
//            data.setFri(todoVo.getMsg().get(i).getFri());
//            data.setSat(todoVo.getMsg().get(i).getSat());
//            data.setPublisher(todoVo.getMsg().get(i).getPublish());
//            data.setTitle(todoVo.getMsg().get(i).getTitle());
//            data.setOption(todoVo.getMsg().get(i).getOption());
//            data.setSection(todoVo.getMsg().get(i).getSection());
//            data.setComplete(todoVo.getMsg().get(i).getTdcomplete());
//            data.setIdx(todoVo.getMsg().get(i).getIdx());
//            data.setStart(todoVo.getMsg().get(i).getTdstart());
//            data.setEnd(todoVo.getMsg().get(i).getTdend());
//            data.setUse(todoVo.getMsg().get(i).getTduse());
//            data.setTdpc(todoVo.getMsg().get(i).getTdpc());
//            data.setKid(displayKid);
//
//            // 각 값이 들어간 data를 adapter에 추가합니다.
//            adapter.addItem(data);
//        }
//
//        // adapter의 값이 변경되었다는 것을 알려줍니다.
//        adapter.notifyDataSetChanged();
//
//        // 기준 날짜가 변경된대로 날짜버튼 새로 설정.
//        setTopWeek();

        return true;
    }

    public void initChart() {
        barChart.clear();

        barChart.setMaxVisibleValueCount(7);
        barChart.setPinchZoom(false);
        barChart.setDrawBarShadow(false);
        barChart.setDrawGridBackground(false);
        barChart.setTouchEnabled(false);
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

        barChart.getAxisRight().setDrawGridLines(false);
        barChart.getAxisRight().setDrawLabels(false);

        barChart.getXAxis().setEnabled(true);
        barChart.getXAxis().setDrawGridLines(false);
        barChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        barChart.getXAxis().setTextColor(R.color.black_overlay);
//
//        ArrayList NoOfEmp = new ArrayList();
//        NoOfEmp.add(new BarEntry(945f, 0));
//        NoOfEmp.add(new BarEntry(1040f, 1));
//        NoOfEmp.add(new BarEntry(1133f, 2));
//        NoOfEmp.add(new BarEntry(1240f, 3));
//        NoOfEmp.add(new BarEntry(1369f, 4));
//        NoOfEmp.add(new BarEntry(1487f, 5));
//        NoOfEmp.add(new BarEntry(1501f, 6));
//        NoOfEmp.add(new BarEntry(1645f, 7));
//        NoOfEmp.add(new BarEntry(1578f, 8));
//        NoOfEmp.add(new BarEntry(1695f, 9));
//        ArrayList year = new ArrayList();
//        year.add("2008");
//        year.add("2009");
//        year.add("2010");
//        year.add("2011");
//        year.add("2012");
//        year.add("2013");
//        year.add("2014");
//        year.add("2015");
//        year.add("2016");
//        year.add("2017");
//        BarDataSet bardataset = new BarDataSet(NoOfEmp, "No Of Employee");
//
//        BarData data = new BarData(bardataset); // MPAndroidChart v3.X 오류 발생
//        bardataset.setColors(ColorTemplate.COLORFUL_COLORS);
//        data.setBarWidth(50f);
//        barChart.setData(data);
    }

    public void getChartData(){
        ArrayList<BarEntry> entries = new ArrayList<BarEntry>();
        final ArrayList<String> xLabels = new ArrayList<String>();

        int dCount = sumVo.getCnt();
        float[] yValue = {0, 0, 0, 0, 0, 0, 0};
        String[] xLabel = {"SUN", "MON", "TUE", "WED", "THU", "FRI", "SAT"};
        for (int i=0; i<sumVo.getCnt(); i++) { // 받아온 값 중에서...
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

        initChart();

        entries.add(new BarEntry(0, yValue[0], xLabel[0]));
        entries.add(new BarEntry(1, yValue[1], xLabel[1]));
        entries.add(new BarEntry(2, yValue[2], xLabel[2]));
        entries.add(new BarEntry(3, yValue[3], xLabel[3]));
        entries.add(new BarEntry(4, yValue[4], xLabel[4]));
        entries.add(new BarEntry(5, yValue[5], xLabel[5]));
        entries.add(new BarEntry(6, yValue[6], xLabel[6]));

        barChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(){
            @Override
            public String getFormattedValue(float value) {
                return xLabel[(int) value];
            }
        });

        BarDataSet dataSet = new BarDataSet(entries, "Todo Report");
        dataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        BarData data = new BarData(dataSet);
        data.setBarWidth(0.7f);

        barChart.invalidate();
        barChart.setData(data);
        barChart.animateY(1000);
    }

    public void showReport(View v){

    }

    public void getReportData(){

    }

}
