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
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.gson.Gson;
import com.mhd.stard.MainActivity;
import com.mhd.stard.R;
import com.mhd.stard.common.MHDApplication;
import com.mhd.stard.common.XYMarkerView;
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

            // 차트를 표시한다.
            getChartData();
        }

        return true;
    }

    public void initChart() {
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
        //// 차트에 표시할 X, Y축 정보를 생성, 저장.
        ArrayList<BarEntry> entries = new ArrayList<BarEntry>();
        final ArrayList<String> xLabels = new ArrayList<String>();

        int dCount = sumVo.getCnt();
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
        initChart();

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
        barChart.getLegend().setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);

        //// bar 클릭 시 상단에 marker 표시.
        XYMarkerView mv = new XYMarkerView(mContext, indexAxisValueFormatter);
        mv.setChartView(barChart); // For bounds control
        barChart.setMarker(mv); // Set the marker to the chart

        //// bar data 구성
        BarDataSet dataSet = new BarDataSet(entries, "주간 학습 성취율");
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
