package com.mhd.elemantary.fragment;

import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Build;
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
//        GradientDrawable backgroundCornerDrawable =  new GradientDrawable();
//        backgroundCornerDrawable.setCornerRadius( 20 );

        // Initialize a new array of drawable objects
        Drawable[] drawables = new Drawable[]{
                borderColorDrawable,
                backgroundColorDrawable
//                backgroundCornerDrawable
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
        for (int i = startTime*100; i < endTime*100; i+=100) {
            // 각 시간별로 루프를 돌며
            for (int d = 0; d<31; d+=30) {
                boolean dMon = false;
                boolean dTue = false;
                boolean dWed = false;
                boolean dThu = false;
                boolean dFri = false;
                boolean dSat = false;
                boolean dSun = false;

                for (int k = 0; k < scheduleVo.getCnt(); k++) {
                    int sTime = scheduleVo.getMsg().get(k).getStart()*100 + scheduleVo.getMsg().get(k).getStartMin();
                    int eTime = scheduleVo.getMsg().get(k).getEnd()*100 + scheduleVo.getMsg().get(k).getEndMin();

                    // 시간루프에 따른 해당 시간에 대해 각 데이타별로 루프를 돌며 그리기 시작.
                    // 각 시간대(30분 단위)에 한해서 시간표의 좌에서 우로 그려진다.
                    if ("Y".equals(scheduleVo.getMsg().get(k).getMon())) {
                        if (!dMon) {
                            if (i+d == sTime) {
                                // 시작 시간이 일치한다면 gap 계산해서 그린다.
                                drawCell(scheduleVo, k, linearLayoutMon, i, d);
                                dMon = true;
                            } else if (i+d > sTime && i+d < eTime) {
                                // 이미 그렸을테니 패스.
                                dMon = true;
                            } else if (i+d < sTime) {
                                // 데이터가 있어서 이미 다른 일정이 그려진 경우가 있고, 없어서 공백을 그려야할 때가 있다.
                                // 이미 그려진 경우를 구분해내야 한다.
                                boolean cuTimeDraw = checkTIme(scheduleVo, "mon", i+d);
                                if (!cuTimeDraw) {
                                    drawBlankCell(linearLayoutMon, i, d);
                                    dMon = true;
                                }
                            }
                        }
                    }
                    if ("Y".equals(scheduleVo.getMsg().get(k).getTue())) {
                        if (!dTue) {
                            if (i+d == sTime) {
                                drawCell(scheduleVo, k, linearLayoutTue, i, d);
                                dTue = true;
                            } else if (i+d > sTime && i+d < eTime) {
                                // 이미 그렸을테니 패스.
                                dTue = true;
                            } else if (i+d < sTime) {
                                // 데이터가 있어서 이미 그려진 경우가 있고, 없어서 공백을 그려야할 때가 있다.
                                // 이미 그려진 경우를 구분해내야 한다.
                                boolean cuTimeDraw = checkTIme(scheduleVo, "tue", i+d);
                                if (!cuTimeDraw) {
                                    drawBlankCell(linearLayoutTue, i, d);
                                    dTue = true;
                                }
                            }
                        }
                    }
                    if ("Y".equals(scheduleVo.getMsg().get(k).getWed())) {
                        if (!dWed) {
                            if (i+d == sTime) {
                                drawCell(scheduleVo, k, linearLayoutWed, i, d);
                                dWed = true;
                            } else if (i+d > sTime && i+d < eTime) {
                                // 이미 그렸을테니 패스.
                                dWed = true;
                            } else if (i+d < sTime) {
                                // 데이터가 있어서 이미 그려진 경우가 있고, 없어서 공백을 그려야할 때가 있다.
                                // 이미 그려진 경우를 구분해내야 한다.
                                boolean cuTimeDraw = checkTIme(scheduleVo, "wed", i+d);
                                if (!cuTimeDraw) {
                                    drawBlankCell(linearLayoutWed, i, d);
                                    dWed = true;
                                }
                            }
                        }
                    }
                    if ("Y".equals(scheduleVo.getMsg().get(k).getThu())) {
                        if (!dThu) {
                            if (i+d == sTime) {
                                drawCell(scheduleVo, k, linearLayoutThu, i, d);
                                dThu = true;
                            } else if (i+d > sTime && i+d < eTime) {
                                // 이미 그렸을테니 패스.
                                dThu = true;
                            } else if (i+d < sTime) {
                                // 데이터가 있어서 이미 그려진 경우가 있고, 없어서 공백을 그려야할 때가 있다.
                                // 이미 그려진 경우를 구분해내야 한다.
                                boolean cuTimeDraw = checkTIme(scheduleVo, "thu", i+d);
                                if (!cuTimeDraw) {
                                    drawBlankCell(linearLayoutThu, i, d);
                                    dThu = true;
                                }
                            }
                        }
                    }
                    if ("Y".equals(scheduleVo.getMsg().get(k).getFri())) {
                        if (!dFri) {
                            if (i+d == sTime) {
                                drawCell(scheduleVo, k, linearLayoutFri, i, d);
                                dFri = true;
                            } else if (i+d > sTime && i+d < eTime) {
                                // 이미 그렸을테니 패스.
                                dFri = true;
                            } else if (i+d < sTime) {
                                // 데이터가 있어서 이미 그려진 경우가 있고, 없어서 공백을 그려야할 때가 있다.
                                // 이미 그려진 경우를 구분해내야 한다.
                                boolean cuTimeDraw = checkTIme(scheduleVo, "fri", i+d);
                                if (!cuTimeDraw) {
                                    drawBlankCell(linearLayoutFri, i, d);
                                    dFri = true;
                                }
                            }
                        }
                    }
                    if ("Y".equals(scheduleVo.getMsg().get(k).getSat())) {
                        if (!dSat) {
                            if (i+d == sTime) {
                                drawCell(scheduleVo, k, linearLayoutSat, i, d);
                                dSat = true;
                            } else if (i+d > sTime && i+d < eTime) {
                                // 이미 그렸을테니 패스.
                                dSat = true;
                            } else if (i+d < sTime) {
                                // 데이터가 있어서 이미 그려진 경우가 있고, 없어서 공백을 그려야할 때가 있다.
                                // 이미 그려진 경우를 구분해내야 한다.
                                boolean cuTimeDraw = checkTIme(scheduleVo, "sat", i+d);
                                if (!cuTimeDraw) {
                                    drawBlankCell(linearLayoutSat, i, d);
                                    dSat = true;
                                }
                            }
                        }
                    }
                    if ("Y".equals(scheduleVo.getMsg().get(k).getSun())) {
                        if (!dSun) {
                            if (i+d == sTime) {
                                drawCell(scheduleVo, k, linearLayoutSun, i, d);
                                dSun = true;
                            } else if (i+d > sTime && i+d < eTime) {
                                // 이미 그렸을테니 패스.
                                dSun = true;
                            } else if (i+d < sTime) {
                                // 데이터가 있어서 이미 그려진 경우가 있고, 없어서 공백을 그려야할 때가 있다.
                                // 이미 그려진 경우를 구분해내야 한다.
                                boolean cuTimeDraw = checkTIme(scheduleVo, "sun", i+d);
                                if (!cuTimeDraw) {
                                    drawBlankCell(linearLayoutSun, i, d);
                                    dSun = true;
                                }
                            }
                        }
                    }
                    // 각 요일에 해당하고 시작시간과 동일한 데이터가 있으면 그리게 되고, 그 후에..
                    // 각 요일에 해당하고 시작시간과 종료시간 사이에 있는 데이터가 있으면 안그려야 하고(아무것도 안하는), 그 후에..
                    // 각 요일에 해당도 안되거나, 해당이 되지만 시간이 사이에 끼지 못하는 것은 그려야 한다.
                }

                if (!dMon)
                    drawBlankCell(linearLayoutMon, i, d);
                if (!dTue)
                    drawBlankCell(linearLayoutTue, i, d);
                if (!dWed)
                    drawBlankCell(linearLayoutWed, i, d);
                if (!dThu)
                    drawBlankCell(linearLayoutThu, i, d);
                if (!dFri)
                    drawBlankCell(linearLayoutFri, i, d);
                if (!dSat)
                    drawBlankCell(linearLayoutSat, i, d);
                if (!dSun)
                    drawBlankCell(linearLayoutSun, i, d);
            }
        }

        return true;
    }

     private boolean drawCell(ScheduleVo scheduleVo, int k, LinearLayout mLinearLayout, int ii, int dd) {
         // 각 요일, 현재 index 시간과 vo 시간이 동일하다면 일단 그리는 것.
         // 몇칸일지를 보기 위해 종료시간 계산. gap 만큼의 weight를 줄 것.
         int sHour = scheduleVo.getMsg().get(k).getStart();
         int sMin = scheduleVo.getMsg().get(k).getStartMin();
         int eHour = scheduleVo.getMsg().get(k).getEnd();
         int eMin = scheduleVo.getMsg().get(k).getEndMin();
         int sCalc = sMin == 30 ? -1 : 0;
         int eCalc= eMin == 30 ? 1 : 0;
         int gap = ((eHour - sHour)*2) + sCalc + eCalc;
         int bottomline = 0;
         if((dd==0 && gap%2==0) || (dd==30 && gap%2==1)){
             bottomline = 1;
         }

         LinearLayout myLinearLayout = new LinearLayout(getContext());
         myLinearLayout.setOrientation(LinearLayout.VERTICAL);
         LinearLayout.LayoutParams mParams = new LinearLayout.LayoutParams(
                 LinearLayout.LayoutParams.MATCH_PARENT,
                 LinearLayout.LayoutParams.MATCH_PARENT);
         mParams.height = 0;
         mParams.width = LinearLayout.LayoutParams.MATCH_PARENT;
         mParams.weight = gap;
         mParams.gravity = Gravity.CENTER;
         myLinearLayout.setPadding(6,6,6,6);
         myLinearLayout.setLayoutParams(mParams);
         LayerDrawable shape = getBorders(Color.WHITE, Color.GRAY, 0, 0, 0, bottomline);
         myLinearLayout.setBackground(shape);

         titleText = new TextView(getContext());
         titleText.setText(scheduleVo.getMsg().get(k).getSubject());
         titleText.setTextSize(13);
         if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
             Typeface typeface = getResources().getFont(R.font.main_font);
             titleText.setTypeface(typeface);
         }else{
             Typeface typeface = Typeface.createFromAsset(getActivity().getAssets(), "notoregular.otf"); // font 폴더내에 있는 jua.ttf 파일을 typeface로 설정
             titleText.setTypeface(typeface);
         }
         titleText.setTextColor(Color.WHITE);
         titleText.setGravity(Gravity.CENTER);
//         LayerDrawable bottomBorder = getBorders(Color.parseColor(scheduleVo.getMsg().get(k).getColor()), Color.GRAY, 0, 0, 0, 0);
//         titleText.setBackground(bottomBorder);
         GradientDrawable shape2 =  new GradientDrawable();
         shape2.setCornerRadius( 20 );
         shape2.setColor(Color.parseColor(scheduleVo.getMsg().get(k).getColor()));
         titleText.setBackground(shape2);
//         GradientDrawable gd = new GradientDrawable();
//         gd.setColor(Color.RED);
//         gd.setCornerRadius(10);
//         gd.setStroke(2, Color.WHITE);
//
//         view.setBackground(gd);

         myLinearLayout.addView(titleText);
         mLinearLayout.addView(myLinearLayout);

         LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
         param.height = LinearLayout.LayoutParams.MATCH_PARENT;
         param.width = LinearLayout.LayoutParams.MATCH_PARENT;
         param.gravity = Gravity.CENTER;

         titleText.setLayoutParams(param);

         return true;
     }
    private boolean drawBlankCell(LinearLayout mLinearLayout, int ii, int dd) {
        // 각 요일, 현재 index 시간과 vo 시간이 동일하다면 일단 그리는 것.
        // 몇칸일지를 보기 위해 종료시간 계산. gap 만큼의 weight를 줄 것.
        int gap = 1;
        LinearLayout myLinearLayout = new LinearLayout(getContext());
        myLinearLayout.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams mParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        mParams.height = 0;
        mParams.width = LinearLayout.LayoutParams.MATCH_PARENT;
        mParams.weight = gap;
        mParams.gravity = Gravity.CENTER;
        myLinearLayout.setPadding(6,6,6,6);
        myLinearLayout.setLayoutParams(mParams);
        LayerDrawable bottomBorder = getBorders(Color.WHITE, Color.GRAY, 0, 0, 0, dd == 30 ? 1 : 0);
        myLinearLayout.setBackground(bottomBorder);

        mLinearLayout.addView(myLinearLayout);


        return true;
    }
    private boolean drawTimeCell(LinearLayout mLinearLayout, int time) {
        // 각 요일, 현재 index 시간과 vo 시간이 동일하다면 일단 그리는 것.
        // 몇칸일지를 보기 위해 종료시간 계산. gap 만큼의 weight를 줄 것.
        // 30분 단위까지 표시하도록 수정_2021.10.14
        int gap = 1;
        titleText = new TextView(getContext());
//        titleText.setText(String.valueOf(time) + "~" + String.valueOf(time+1));
        titleText.setText(String.valueOf(time) + ":00");
        titleText.setTextSize(12);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Typeface typeface = getResources().getFont(R.font.main_font);
            titleText.setTypeface(typeface);
        }else{
            Typeface typeface = Typeface.createFromAsset(getActivity().getAssets(), "notoregular.otf"); // font 폴더내에 있는 jua.ttf 파일을 typeface로 설정
            titleText.setTypeface(typeface);
        }
        titleText.setTextColor(Color.BLACK);
        titleText.setGravity(Gravity.CENTER);
        LayerDrawable bottomBorder = getBorders(Color.WHITE, Color.GRAY, 1, 1, 1, 1);
        titleText.setBackground(bottomBorder);

        mLinearLayout.addView(titleText);

        LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        param.height = 0;
        param.width = LinearLayout.LayoutParams.MATCH_PARENT;
        param.weight = gap;
        param.gravity = Gravity.CENTER;

        titleText.setLayoutParams (param);

//        titleText = new TextView(getContext());
////        titleText.setText(String.valueOf(time) + "~" + String.valueOf(time+1));
//        titleText.setText(String.valueOf(time) + ":30");
//        titleText.setTextSize(14);
//        titleText.setTextColor(Color.BLACK);
//        titleText.setGravity(Gravity.CENTER);
//        titleText.setBackground(bottomBorder);
//        titleText.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
//
//        mLinearLayout.addView(titleText);
//
//        titleText.setLayoutParams (param);

        return true;
    }

     private boolean checkTIme(ScheduleVo scheduleVo, String day, int cutime){
        if("mon".equals(day)){
            for (int k=0; k < scheduleVo.getCnt(); k++) {
                if ("Y".equals(scheduleVo.getMsg().get(k).getMon())) {
                    if(cutime >= (scheduleVo.getMsg().get(k).getStart()*100)+scheduleVo.getMsg().get(k).getStartMin() && cutime <= (scheduleVo.getMsg().get(k).getEnd()*100)+scheduleVo.getMsg().get(k).getEndMin())
                        return true;
                }
            }
        }
         if("tue".equals(day)){
             for (int k=0; k < scheduleVo.getCnt(); k++) {
                 if ("Y".equals(scheduleVo.getMsg().get(k).getTue())) {
                     if(cutime >= (scheduleVo.getMsg().get(k).getStart()*100)+scheduleVo.getMsg().get(k).getStartMin() && cutime <= (scheduleVo.getMsg().get(k).getEnd()*100)+scheduleVo.getMsg().get(k).getEndMin())
                         return true;
                 }
             }
         }
         if("wed".equals(day)){
             for (int k=0; k < scheduleVo.getCnt(); k++) {
                 if ("Y".equals(scheduleVo.getMsg().get(k).getWed())) {
                     if(cutime >= (scheduleVo.getMsg().get(k).getStart()*100)+scheduleVo.getMsg().get(k).getStartMin() && cutime <= (scheduleVo.getMsg().get(k).getEnd()*100)+scheduleVo.getMsg().get(k).getEndMin())
                         return true;
                 }
             }
         }
         if("thu".equals(day)){
             for (int k=0; k < scheduleVo.getCnt(); k++) {
                 if ("Y".equals(scheduleVo.getMsg().get(k).getThu())) {
                     if(cutime >= (scheduleVo.getMsg().get(k).getStart()*100)+scheduleVo.getMsg().get(k).getStartMin() && cutime <= (scheduleVo.getMsg().get(k).getEnd()*100)+scheduleVo.getMsg().get(k).getEndMin())
                         return true;
                 }
             }
         }
         if("fri".equals(day)){
             for (int k=0; k < scheduleVo.getCnt(); k++) {
                 if ("Y".equals(scheduleVo.getMsg().get(k).getFri())) {
                     if(cutime >= (scheduleVo.getMsg().get(k).getStart()*100)+scheduleVo.getMsg().get(k).getStartMin() && cutime <= (scheduleVo.getMsg().get(k).getEnd()*100)+scheduleVo.getMsg().get(k).getEndMin())
                         return true;
                 }
             }
         }
         if("sat".equals(day)){
             for (int k=0; k < scheduleVo.getCnt(); k++) {
                 if ("Y".equals(scheduleVo.getMsg().get(k).getSat())) {
                     if(cutime >= (scheduleVo.getMsg().get(k).getStart()*100)+scheduleVo.getMsg().get(k).getStartMin() && cutime <= (scheduleVo.getMsg().get(k).getEnd()*100)+scheduleVo.getMsg().get(k).getEndMin())
                         return true;
                 }
             }
         }
         if("sun".equals(day)){
             for (int k=0; k < scheduleVo.getCnt(); k++) {
                 if ("Y".equals(scheduleVo.getMsg().get(k).getSun())) {
                     if(cutime >= (scheduleVo.getMsg().get(k).getStart()*100)+scheduleVo.getMsg().get(k).getStartMin() && cutime <= (scheduleVo.getMsg().get(k).getEnd()*100)+scheduleVo.getMsg().get(k).getEndMin())
                         return true;
                 }
             }
         }
        return false;
     }
}
