package com.mhd.elemantary.fragment;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentTransaction;
import androidx.gridlayout.widget.GridLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
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

import org.w3c.dom.Text;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ScheduleFragment extends BaseFragment {
    private RecyclerView recyclerView;
    private ReCyclerScheduleAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private LinearLayout linearLayoutTime, linearLayoutMon, linearLayoutTue, linearLayoutWed, linearLayoutThu, linearLayoutFri, linearLayoutSat, linearLayoutSun;
    private TextView titleText;


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

        linearLayoutTime= (LinearLayout) root.findViewById(R.id.ll_schedule_time);
        linearLayoutMon = (LinearLayout) root.findViewById(R.id.ll_schedule_mon);
        linearLayoutTue = (LinearLayout) root.findViewById(R.id.ll_schedule_tue);
        linearLayoutWed = (LinearLayout) root.findViewById(R.id.ll_schedule_wed);
        linearLayoutThu = (LinearLayout) root.findViewById(R.id.ll_schedule_thu);
        linearLayoutFri = (LinearLayout) root.findViewById(R.id.ll_schedule_fri);
        linearLayoutSat = (LinearLayout) root.findViewById(R.id.ll_schedule_sat);
        linearLayoutSun = (LinearLayout) root.findViewById(R.id.ll_schedule_sun);

        querySchedule();
    }

    protected LayerDrawable getBorders(int bgColor, int borderColor,
                                       int left, int top, int right, int bottom){
        // Initialize new color drawables
        ColorDrawable borderColorDrawable = new ColorDrawable(borderColor);
        ColorDrawable backgroundColorDrawable = new ColorDrawable(bgColor);

        // Initialize a new array of drawable objects
        Drawable[] drawables = new Drawable[]{
                borderColorDrawable,
                backgroundColorDrawable
        };

        // Initialize a new layer drawable instance from drawables array
        LayerDrawable layerDrawable = new LayerDrawable(drawables);

        // Set padding for background color layer
        layerDrawable.setLayerInset(
                1, // Index of the drawable to adjust [background color layer]
                left, // Number of pixels to add to the left bound [left border]
                top, // Number of pixels to add to the top bound [top border]
                right, // Number of pixels to add to the right bound [right border]
                bottom // Number of pixels to add to the bottom bound [bottom border]
        );

        // Finally, return the one or more sided bordered background drawable
        return layerDrawable;
    }

    @Override
    public void batchFunction(String api) {
//        if(api.equals(getString(R.string.api_editor_clear))) {
//            // editor 내용 초기화.
//            editor.clearAllContents();
//        }
    }

    /**
     * query schedule
     */
    public void querySchedule(){
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

        if (nvCnt == 0) {
            // 정보가 없으면 비정상
            Toast.makeText(mContext, nvMsg, Toast.LENGTH_SHORT).show();
        } else {
            // 스케쥴 정보를 받아옴.
            Gson gson = new Gson();
            scheduleVo = gson.fromJson(result, ScheduleVo.class);
            MHDApplication.getInstance().getMHDSvcManager().setScheduleVo(null);
            MHDApplication.getInstance().getMHDSvcManager().setScheduleVo(scheduleVo);
        }

        linearLayoutTime.removeAllViews();
        linearLayoutMon.removeAllViews();
        linearLayoutTue.removeAllViews();
        linearLayoutWed.removeAllViews();
        linearLayoutThu.removeAllViews();
        linearLayoutFri.removeAllViews();
        linearLayoutSat.removeAllViews();
        linearLayoutSun.removeAllViews();

        // 시간표 cell 생성
        // data가 몇개든, 모든 요일/모든 시간을 루프를 돌며 표시해야 한다.
        int startTime = 8;
        int endTime = 22;
        String[] arrDay = {};
        // 8 ~ 22, 각 시간 루프를 돌린다.
        for (int i = startTime; i < endTime; i++) {
            drawTimeCell(linearLayoutTime, i);
        }
        for (int i = startTime; i < endTime; i++) {
            // 데이터 중 각 요일을 체크한다.
            boolean dMon = false;
            boolean dTue = false;
            boolean dWed = false;
            boolean dThu = false;
            boolean dFri = false;
            boolean dSat = false;
            boolean dSun = false;
            for (int k=0; k < scheduleVo.getCnt(); k++){
                if("Y".equals(scheduleVo.getMsg().get(k).getMon())){
                    if(!dMon) {
                        if (i == scheduleVo.getMsg().get(k).getStart()) {
                            drawCell(scheduleVo, k, linearLayoutMon);
                            dMon = true;
                        } else if (i > scheduleVo.getMsg().get(k).getStart() && i < scheduleVo.getMsg().get(k).getEnd()) {
                            // 이미 그렸을테니 패스.
                            dMon = true;
                        } else if (i < scheduleVo.getMsg().get(k).getStart()) {
                            // 데이터가 있어서 이미 다른 일정이 그려진 경우가 있고, 없어서 공백을 그려야할 때가 있다.
                            // 이미 그려진 경우를 구분해내야 한다.
                            boolean cuTimeDraw = checkTIme(scheduleVo, "mon", i);
                            if (!cuTimeDraw) {
                                drawBlankCell(linearLayoutMon);
                                dMon = true;
                            }
                        }
                    }
                }
                if("Y".equals(scheduleVo.getMsg().get(k).getTue())){
                    if(!dTue) {
                        if (i == scheduleVo.getMsg().get(k).getStart()) {
                            drawCell(scheduleVo, k, linearLayoutTue);
                            dTue = true;
                        } else if (i > scheduleVo.getMsg().get(k).getStart() && i < scheduleVo.getMsg().get(k).getEnd()) {
                            // 이미 그렸을테니 패스.
                            dTue = true;
                        } else if (i < scheduleVo.getMsg().get(k).getStart()) {
                            // 데이터가 있어서 이미 그려진 경우가 있고, 없어서 공백을 그려야할 때가 있다.
                            // 이미 그려진 경우를 구분해내야 한다.
                            boolean cuTimeDraw = checkTIme(scheduleVo, "tue", i);
                            if (!cuTimeDraw) {
                                drawBlankCell(linearLayoutTue);
                                dTue = true;
                            }
                        }
                    }
                }
                if("Y".equals(scheduleVo.getMsg().get(k).getWed())){
                    if(!dWed) {
                        if (i == scheduleVo.getMsg().get(k).getStart()) {
                            drawCell(scheduleVo, k, linearLayoutWed);
                            dWed = true;
                        } else if (i > scheduleVo.getMsg().get(k).getStart() && i < scheduleVo.getMsg().get(k).getEnd()) {
                            // 이미 그렸을테니 패스.
                            dWed = true;
                        } else if (i < scheduleVo.getMsg().get(k).getStart()) {
                            // 데이터가 있어서 이미 그려진 경우가 있고, 없어서 공백을 그려야할 때가 있다.
                            // 이미 그려진 경우를 구분해내야 한다.
                            boolean cuTimeDraw = checkTIme(scheduleVo, "wed", i);
                            if (!cuTimeDraw) {
                                drawBlankCell(linearLayoutWed);
                                dWed = true;
                            }
                        }
                    }
                }
                if("Y".equals(scheduleVo.getMsg().get(k).getThu())){
                    if(!dThu) {
                        if (i == scheduleVo.getMsg().get(k).getStart()) {
                            drawCell(scheduleVo, k, linearLayoutThu);
                            dThu = true;
                        } else if (i > scheduleVo.getMsg().get(k).getStart() && i < scheduleVo.getMsg().get(k).getEnd()) {
                            // 이미 그렸을테니 패스.
                            dThu = true;
                        } else if (i < scheduleVo.getMsg().get(k).getStart()) {
                            // 데이터가 있어서 이미 그려진 경우가 있고, 없어서 공백을 그려야할 때가 있다.
                            // 이미 그려진 경우를 구분해내야 한다.
                            boolean cuTimeDraw = checkTIme(scheduleVo, "thu", i);
                            if (!cuTimeDraw) {
                                drawBlankCell(linearLayoutThu);
                                dThu = true;
                            }
                        }
                    }
                }
                if("Y".equals(scheduleVo.getMsg().get(k).getFri())){
                    if(!dFri) {
                        if (i == scheduleVo.getMsg().get(k).getStart()) {
                            drawCell(scheduleVo, k, linearLayoutFri);
                            dFri = true;
                        } else if (i > scheduleVo.getMsg().get(k).getStart() && i < scheduleVo.getMsg().get(k).getEnd()) {
                            // 이미 그렸을테니 패스.
                            dFri = true;
                        } else if (i < scheduleVo.getMsg().get(k).getStart()) {
                            // 데이터가 있어서 이미 그려진 경우가 있고, 없어서 공백을 그려야할 때가 있다.
                            // 이미 그려진 경우를 구분해내야 한다.
                            boolean cuTimeDraw = checkTIme(scheduleVo, "fri", i);
                            if (!cuTimeDraw) {
                                drawBlankCell(linearLayoutFri);
                                dFri = true;
                            }
                        }
                    }
                }
                if("Y".equals(scheduleVo.getMsg().get(k).getSat())){
                    if(!dSat) {
                        if (i == scheduleVo.getMsg().get(k).getStart()) {
                            drawCell(scheduleVo, k, linearLayoutSat);
                            dSat = true;
                        } else if (i > scheduleVo.getMsg().get(k).getStart() && i < scheduleVo.getMsg().get(k).getEnd()) {
                            // 이미 그렸을테니 패스.
                            dSat = true;
                        } else if (i < scheduleVo.getMsg().get(k).getStart()) {
                            // 데이터가 있어서 이미 그려진 경우가 있고, 없어서 공백을 그려야할 때가 있다.
                            // 이미 그려진 경우를 구분해내야 한다.
                            boolean cuTimeDraw = checkTIme(scheduleVo, "sat", i);
                            if (!cuTimeDraw) {
                                drawBlankCell(linearLayoutSat);
                                dSat = true;
                            }
                        }
                    }
                }
                if("Y".equals(scheduleVo.getMsg().get(k).getSun())){
                    if(!dSun) {
                        if (i == scheduleVo.getMsg().get(k).getStart()) {
                            drawCell(scheduleVo, k, linearLayoutSun);
                            dSun = true;
                        } else if (i > scheduleVo.getMsg().get(k).getStart() && i < scheduleVo.getMsg().get(k).getEnd()) {
                            // 이미 그렸을테니 패스.
                            dSun = true;
                        } else if (i < scheduleVo.getMsg().get(k).getStart()) {
                            // 데이터가 있어서 이미 그려진 경우가 있고, 없어서 공백을 그려야할 때가 있다.
                            // 이미 그려진 경우를 구분해내야 한다.
                            boolean cuTimeDraw = checkTIme(scheduleVo, "sun", i);
                            if (!cuTimeDraw) {
                                drawBlankCell(linearLayoutSun);
                                dSun = true;
                            }
                        }
                    }
                }
                // 각 요일에 해당하고 시작시간과 동일한 데이터가 있으면 그리게 되고, 그 후에..
                // 각 요일에 해당하고 시작시간과 종료시간 사이에 있는 데이터가 있으면 안그려야 하고(아무것도 안하는), 그 후에..
                // 각 요일에 해당도 안되거나, 해당이 되지만 시간이 사이에 끼지 못하는 것은 그려야 한다.
            }

            if(!dMon)
                drawBlankCell(linearLayoutMon);
            if(!dTue)
                drawBlankCell(linearLayoutTue);
            if(!dWed)
                drawBlankCell(linearLayoutWed);
            if(!dThu)
                drawBlankCell(linearLayoutThu);
            if(!dFri)
                drawBlankCell(linearLayoutFri);
            if(!dSat)
                drawBlankCell(linearLayoutSat);
            if(!dSun)
                drawBlankCell(linearLayoutSun);
        }

        return true;
    }

     private boolean drawCell(ScheduleVo scheduleVo, int k, LinearLayout mLinearLayout) {
         // 각 요일, 현재 index 시간과 vo 시간이 동일하다면 일단 그리는 것.
         // 몇칸일지를 보기 위해 종료시간 계산. gap 만큼의 weight를 줄 것.
         int gap = scheduleVo.getMsg().get(k).getEnd() - scheduleVo.getMsg().get(k).getStart();
         titleText = new TextView(getContext());
         titleText.setText(scheduleVo.getMsg().get(k).getSubject());
         titleText.setTextSize(15);
         titleText.setTextColor(Color.BLACK);
         titleText.setGravity(Gravity.CENTER);
         LayerDrawable bottomBorder = getBorders(Color.parseColor(scheduleVo.getMsg().get(k).getColor()), Color.BLACK, 1, 1, 1, 1);
         titleText.setBackground(bottomBorder);
         titleText.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);

         mLinearLayout.addView(titleText);

         LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
         param.height = 0;
         param.width = LinearLayout.LayoutParams.MATCH_PARENT;
         param.weight = gap;
         param.gravity = Gravity.CENTER;

         titleText.setLayoutParams (param);

         return true;
     }
    private boolean drawBlankCell(LinearLayout mLinearLayout) {
        // 각 요일, 현재 index 시간과 vo 시간이 동일하다면 일단 그리는 것.
        // 몇칸일지를 보기 위해 종료시간 계산. gap 만큼의 weight를 줄 것.
        int gap = 1;
        titleText = new TextView(getContext());
        titleText.setText("");
        titleText.setTextSize(15);
        titleText.setTextColor(Color.BLACK);
        titleText.setGravity(Gravity.CENTER);
        LayerDrawable bottomBorder = getBorders(Color.WHITE, Color.BLACK, 1, 1, 1, 1);
        titleText.setBackground(bottomBorder);
        titleText.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);

        mLinearLayout.addView(titleText);

        LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        param.height = 0;
        param.width = LinearLayout.LayoutParams.MATCH_PARENT;
        param.weight = gap;
        param.gravity = Gravity.CENTER;

        titleText.setLayoutParams (param);

        return true;
    }
    private boolean drawTimeCell(LinearLayout mLinearLayout, int time) {
        // 각 요일, 현재 index 시간과 vo 시간이 동일하다면 일단 그리는 것.
        // 몇칸일지를 보기 위해 종료시간 계산. gap 만큼의 weight를 줄 것.
        int gap = 1;
        titleText = new TextView(getContext());
        titleText.setText(String.valueOf(time) + "~" + String.valueOf(time+1));
        titleText.setTextSize(14);
        titleText.setTextColor(Color.BLACK);
        titleText.setGravity(Gravity.CENTER);
        LayerDrawable bottomBorder = getBorders(Color.WHITE, Color.BLACK, 1, 1, 1, 1);
        titleText.setBackground(bottomBorder);
        titleText.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);

        mLinearLayout.addView(titleText);

        LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        param.height = 0;
        param.width = LinearLayout.LayoutParams.MATCH_PARENT;
        param.weight = gap;
        param.gravity = Gravity.CENTER;

        titleText.setLayoutParams (param);

        return true;
    }

     private boolean checkTIme(ScheduleVo scheduleVo, String day, int cutime){
        if("mon".equals(day)){
            for (int k=0; k < scheduleVo.getCnt(); k++) {
                if ("Y".equals(scheduleVo.getMsg().get(k).getMon())) {
                    if(cutime >= scheduleVo.getMsg().get(k).getStart() && cutime <= scheduleVo.getMsg().get(k).getEnd())
                        return true;
                }
            }
        }
         if("tue".equals(day)){
             for (int k=0; k < scheduleVo.getCnt(); k++) {
                 if ("Y".equals(scheduleVo.getMsg().get(k).getTue())) {
                     if(cutime >= scheduleVo.getMsg().get(k).getStart() && cutime <= scheduleVo.getMsg().get(k).getEnd())
                         return true;
                 }
             }
         }
         if("wed".equals(day)){
             for (int k=0; k < scheduleVo.getCnt(); k++) {
                 if ("Y".equals(scheduleVo.getMsg().get(k).getWed())) {
                     if(cutime >= scheduleVo.getMsg().get(k).getStart() && cutime <= scheduleVo.getMsg().get(k).getEnd())
                         return true;
                 }
             }
         }
         if("thu".equals(day)){
             for (int k=0; k < scheduleVo.getCnt(); k++) {
                 if ("Y".equals(scheduleVo.getMsg().get(k).getThu())) {
                     if(cutime >= scheduleVo.getMsg().get(k).getStart() && cutime <= scheduleVo.getMsg().get(k).getEnd())
                         return true;
                 }
             }
         }
         if("fri".equals(day)){
             for (int k=0; k < scheduleVo.getCnt(); k++) {
                 if ("Y".equals(scheduleVo.getMsg().get(k).getFri())) {
                     if(cutime >= scheduleVo.getMsg().get(k).getStart() && cutime <= scheduleVo.getMsg().get(k).getEnd())
                         return true;
                 }
             }
         }
         if("sat".equals(day)){
             for (int k=0; k < scheduleVo.getCnt(); k++) {
                 if ("Y".equals(scheduleVo.getMsg().get(k).getSat())) {
                     if(cutime >= scheduleVo.getMsg().get(k).getStart() && cutime <= scheduleVo.getMsg().get(k).getEnd())
                         return true;
                 }
             }
         }
         if("sun".equals(day)){
             for (int k=0; k < scheduleVo.getCnt(); k++) {
                 if ("Y".equals(scheduleVo.getMsg().get(k).getSun())) {
                     if(cutime >= scheduleVo.getMsg().get(k).getStart() && cutime <= scheduleVo.getMsg().get(k).getEnd())
                         return true;
                 }
             }
         }
        return false;
     }
}
