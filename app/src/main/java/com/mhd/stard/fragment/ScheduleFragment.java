package com.mhd.stard.fragment;

import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
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
import com.mhd.stard.adapter.ReCyclerScheduleAdapter;
import com.mhd.stard.common.MHDApplication;
import com.mhd.stard.common.vo.KidsVo;
import com.mhd.stard.common.vo.MenuVo;
import com.mhd.stard.common.vo.ScheduleVo;
import com.mhd.stard.network.MHDNetworkInvoker;
import com.mhd.stard.util.MHDLog;
import com.skydoves.powermenu.MenuAnimation;
import com.skydoves.powermenu.OnMenuItemClickListener;
import com.skydoves.powermenu.PowerMenu;
import com.skydoves.powermenu.PowerMenuItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;


public class ScheduleFragment extends BaseFragment {
    private RecyclerView recyclerView;
    private ReCyclerScheduleAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private LinearLayout tv_no_data, linearLayoutTime, linearLayoutLine, linearLayoutMon, linearLayoutTue, linearLayoutWed, linearLayoutThu, linearLayoutFri, linearLayoutSat, linearLayoutSun;
    private TextView titleText;
    PowerMenu powerMenu = null;
    TextView vst_top_title;
    String displayKid = "";
    int displayKidPosition = 0;
    LinearLayout ll_area_data;

    public static ScheduleFragment create() {
        return new ScheduleFragment();
    }

    @Override
    public int getLayoutResId() {
        return R.layout.fragment_schedule;
    }

    @Override
    public void inOnCreateView(View root, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //Statusbar ????????? ?????????
//        final TextView mTitle = (TextView) root.findViewById(R.id.vst_top_title);
//        RelativeLayout.LayoutParams mLayoutParams = (RelativeLayout.LayoutParams) mTitle.getLayoutParams();
//        mLayoutParams.topMargin = Util.getInstance().getStatusBarHeight(root.getContext());
//        mTitle.setLayoutParams(mLayoutParams);

        LinearLayout ll_top_schedule = (LinearLayout) root.findViewById(R.id.ll_top_schedule);
        ll_area_data = (LinearLayout) root.findViewById(R.id.ll_area_data);
        tv_no_data = (LinearLayout) root.findViewById(R.id.tv_no_data);

        // vo??? ?????? ?????? ????????? ??????item ?????? ??????.
        KidsVo kidsVo = MHDApplication.getInstance().getMHDSvcManager().getKidsVo();
        MenuVo menuVo = MHDApplication.getInstance().getMHDSvcManager().getMenuVo();
        List<PowerMenuItem> kidsList = new ArrayList();
        for(int k=0; k<kidsVo.getCnt(); k++){
            kidsList.add(new PowerMenuItem(kidsVo.getMsg().get(k).getName(), k == 0 ? true : false));
        }
        vst_top_title = (TextView) root.findViewById(R.id.vst_top_title);
        for(int k=0; k<menuVo.getMsg().size(); k++){
            if("SC".equals(menuVo.getMsg().get(k).getMenuname())){
                // ??????????????? ????????? ????????????
                displayKid = menuVo.getMsg().get(k).getKidname();
                displayKidPosition = 0;
            }
        }
        vst_top_title.setText("[ "+displayKid+" ] ?????????");

        powerMenu = new PowerMenu.Builder(getActivity())
                .addItemList(kidsList) //
                //                .addItem(new PowerMenuItem("?????????", false)) // add an item.
                //                .addItem(new PowerMenuItem("?????????", false)) // aad an item list.
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

//        ll_top_schedule.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                KidsVo kidsVo = MHDApplication.getInstance().getMHDSvcManager().getKidsVo();
//                List<PowerMenuItem> kidsList = new ArrayList();
//                for(int k=0; k<kidsVo.getCnt(); k++){
//                    kidsList.add(new PowerMenuItem(kidsVo.getMsg().get(k).getName(), k == 0 ? true : false));
//                }
//                powerMenu = new PowerMenu.Builder(getActivity())
//                        .addItemList(kidsList) //
////                .addItem(new PowerMenuItem("?????????", false)) // add an item.
////                .addItem(new PowerMenuItem("?????????", false)) // aad an item list.
//                        .setTextSize(14)
//                        .setAnimation(MenuAnimation.SHOWUP_TOP_LEFT) // Animation start point (TOP | LEFT).
//                        .setMenuRadius(10f) // sets the corner radius.
//                        .setMenuShadow(10f) // sets the shadow.
//                        .setTextColor(ContextCompat.getColor(getActivity(), R.color.black))
//                        .setTextGravity(Gravity.CENTER)
//                        .setTextTypeface(Typeface.createFromAsset(getActivity().getAssets(), "notoregular.otf"))
//                        .setSelectedTextColor(Color.WHITE)
//                        .setMenuColor(Color.WHITE)
//                        .setSelectedMenuColor(ContextCompat.getColor(getActivity(), R.color.colorPrimary))
//                        .setOnMenuItemClickListener(onMenuItemClickListener)
//                        .setDivider(new ColorDrawable(ContextCompat.getColor(getActivity(), R.color.gray))) // sets a divider.
//                        .setDividerHeight(1)
//                        .build();
//
//                powerMenu.showAsDropDown(v);
//            }
//        });

        linearLayoutTime= (LinearLayout) root.findViewById(R.id.ll_schedule_time);
        linearLayoutLine= (LinearLayout) root.findViewById(R.id.ll_schedule_line);
        linearLayoutMon = (LinearLayout) root.findViewById(R.id.ll_schedule_mon);
        linearLayoutTue = (LinearLayout) root.findViewById(R.id.ll_schedule_tue);
        linearLayoutWed = (LinearLayout) root.findViewById(R.id.ll_schedule_wed);
        linearLayoutThu = (LinearLayout) root.findViewById(R.id.ll_schedule_thu);
        linearLayoutFri = (LinearLayout) root.findViewById(R.id.ll_schedule_fri);
        linearLayoutSat = (LinearLayout) root.findViewById(R.id.ll_schedule_sat);
        linearLayoutSun = (LinearLayout) root.findViewById(R.id.ll_schedule_sun);

        querySchedule();
    }

    @Override
    public void onResume() {
        super.onResume();

        // ????????? ???????????? item
        if(powerMenu != null) {
            int sp = powerMenu.getSelectedPosition() == -1 ? 0 : powerMenu.getSelectedPosition();
            KidsVo kidsVo = MHDApplication.getInstance().getMHDSvcManager().getKidsVo();
            List<PowerMenuItem> kidsList = new ArrayList();
            for (int k = 0; k < kidsVo.getCnt(); k++) {
                kidsList.add(new PowerMenuItem(kidsVo.getMsg().get(k).getName(), k == sp ? true : false));
            }

            powerMenu = new PowerMenu.Builder(getActivity())
                    .addItemList(kidsList) //
                    //                .addItem(new PowerMenuItem("?????????", false)) // add an item.
                    //                .addItem(new PowerMenuItem("?????????", false)) // aad an item list.
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

    private OnMenuItemClickListener<PowerMenuItem> onMenuItemClickListener = new OnMenuItemClickListener<PowerMenuItem>() {
        @Override
        public void onItemClick(int position, PowerMenuItem item) {
            displayKid = item.getTitle().toString();
            vst_top_title.setText("[ "+displayKid+" ] ?????????");
            powerMenu.setSelectedPosition(position); // change selected item
            displayKidPosition = position;
            // MenuVo ????????? ??????
            MenuVo menuVo = MHDApplication.getInstance().getMHDSvcManager().getMenuVo();
            for(int k=0; k<menuVo.getMsg().size(); k++){
                if("SC".equals(menuVo.getMsg().get(k).getMenuname())){
                    // ??????????????? ????????? ????????????
                    menuVo.getMsg().get(k).setKidname(displayKid);
                    querySchedule();
                    break;
                }
            }

            powerMenu.dismiss();
        }
    };

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
//            // editor ?????? ?????????.
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
            params.put("TKNAME", displayKid);

            MHDNetworkInvoker.getInstance().sendVolleyRequest(((MainActivity)getActivity()), R.string.url_restapi_query_schedule, params, ((MainActivity)getActivity()).responseListener);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            MHDLog.printException(e);
        }
    }
    /**
     * BaseActivity?????? ???????????? ????????? ????????? parent Activity?????? ????????? ?????? fragment??? function??? ??????????????? ??????
     */
    public boolean networkResponseProcess(String nvMsg, int nvCnt, String result) {
        ScheduleVo scheduleVo = null;

        tv_no_data.setVisibility(View.GONE);
        ll_area_data.setVisibility(View.VISIBLE);

        if (nvCnt == 0) {
            // ????????? ????????? ?????????
            Toast.makeText(mContext, nvMsg, Toast.LENGTH_SHORT).show();
        } else {
            // ????????? ????????? ?????????.
            Gson gson = new Gson();
            scheduleVo = gson.fromJson(result, ScheduleVo.class);
            MHDApplication.getInstance().getMHDSvcManager().setScheduleVo(null);
            MHDApplication.getInstance().getMHDSvcManager().setScheduleVo(scheduleVo);
        }

        linearLayoutTime.removeAllViews();
        linearLayoutLine.removeAllViews();
        linearLayoutMon.removeAllViews();
        linearLayoutTue.removeAllViews();
        linearLayoutWed.removeAllViews();
        linearLayoutThu.removeAllViews();
        linearLayoutFri.removeAllViews();
        linearLayoutSat.removeAllViews();
        linearLayoutSun.removeAllViews();

        // ????????? cell ??????
        // data??? ?????????, ?????? ??????/?????? ????????? ????????? ?????? ???????????? ??????.
        int startTime = 8;
        int endTime = 22;
        String[] arrDay = {};
        // 8 ~ 22, ??? ?????? ????????? ?????????.
        for (int i = startTime; i < endTime; i++) {
            drawTimeCell(linearLayoutTime, i);
            drawBlankCell(linearLayoutLine, i, 99);
        }
        for (int i = startTime*100; i < endTime*100; i+=100) {
            // ??? ???????????? ????????? ??????
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

                    // ??????????????? ?????? ?????? ????????? ?????? ??? ??????????????? ????????? ?????? ????????? ??????.
                    // ??? ?????????(30??? ??????)??? ????????? ???????????? ????????? ?????? ????????????.
                    if ("Y".equals(scheduleVo.getMsg().get(k).getMon())) {
                        if (!dMon) {
                            if (i+d == sTime) {
                                // ?????? ????????? ??????????????? gap ???????????? ?????????.
                                drawCell(scheduleVo, k, linearLayoutMon, i, d);
                                dMon = true;
                            } else if (i+d > sTime && i+d < eTime) {
                                // ?????? ??????????????? ??????.
                                dMon = true;
                            } else if (i+d < sTime) {
                                // ???????????? ????????? ?????? ?????? ????????? ????????? ????????? ??????, ????????? ????????? ???????????? ?????? ??????.
                                // ?????? ????????? ????????? ??????????????? ??????.
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
                                // ?????? ??????????????? ??????.
                                dTue = true;
                            } else if (i+d < sTime) {
                                // ???????????? ????????? ?????? ????????? ????????? ??????, ????????? ????????? ???????????? ?????? ??????.
                                // ?????? ????????? ????????? ??????????????? ??????.
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
                                // ?????? ??????????????? ??????.
                                dWed = true;
                            } else if (i+d < sTime) {
                                // ???????????? ????????? ?????? ????????? ????????? ??????, ????????? ????????? ???????????? ?????? ??????.
                                // ?????? ????????? ????????? ??????????????? ??????.
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
                                // ?????? ??????????????? ??????.
                                dThu = true;
                            } else if (i+d < sTime) {
                                // ???????????? ????????? ?????? ????????? ????????? ??????, ????????? ????????? ???????????? ?????? ??????.
                                // ?????? ????????? ????????? ??????????????? ??????.
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
                                // ?????? ??????????????? ??????.
                                dFri = true;
                            } else if (i+d < sTime) {
                                // ???????????? ????????? ?????? ????????? ????????? ??????, ????????? ????????? ???????????? ?????? ??????.
                                // ?????? ????????? ????????? ??????????????? ??????.
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
                                // ?????? ??????????????? ??????.
                                dSat = true;
                            } else if (i+d < sTime) {
                                // ???????????? ????????? ?????? ????????? ????????? ??????, ????????? ????????? ???????????? ?????? ??????.
                                // ?????? ????????? ????????? ??????????????? ??????.
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
                                // ?????? ??????????????? ??????.
                                dSun = true;
                            } else if (i+d < sTime) {
                                // ???????????? ????????? ?????? ????????? ????????? ??????, ????????? ????????? ???????????? ?????? ??????.
                                // ?????? ????????? ????????? ??????????????? ??????.
                                boolean cuTimeDraw = checkTIme(scheduleVo, "sun", i+d);
                                if (!cuTimeDraw) {
                                    drawBlankCell(linearLayoutSun, i, d);
                                    dSun = true;
                                }
                            }
                        }
                    }
                    // ??? ????????? ???????????? ??????????????? ????????? ???????????? ????????? ????????? ??????, ??? ??????..
                    // ??? ????????? ???????????? ??????????????? ???????????? ????????? ?????? ???????????? ????????? ???????????? ??????(???????????? ?????????), ??? ??????..
                    // ??? ????????? ????????? ????????????, ????????? ????????? ????????? ????????? ?????? ????????? ?????? ????????? ??????.
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

    public static boolean getType(String word){
        return Pattern.matches("^[???-???]*$", word);
    }
    public static boolean getTypeEng(String word){
        return Pattern.matches("^[a-zA-Z]*$", word);
    }

     private boolean drawCell(ScheduleVo scheduleVo, int k, LinearLayout mLinearLayout, int ii, int dd) {
         // ??? ??????, ?????? index ????????? vo ????????? ??????????????? ?????? ????????? ???.
         // ??????????????? ?????? ?????? ???????????? ??????. gap ????????? weight??? ??? ???.
         int sHour = scheduleVo.getMsg().get(k).getStart();
         int sMin = scheduleVo.getMsg().get(k).getStartMin();
         int eHour = scheduleVo.getMsg().get(k).getEnd();
         int eMin = scheduleVo.getMsg().get(k).getEndMin();
         String sIdx = scheduleVo.getMsg().get(k).getIdx();
         int sCalc = sMin == 30 ? -1 : 0;
         int eCalc= eMin == 30 ? 1 : 0;
         int gap = ((eHour - sHour)*2) + sCalc + eCalc;
         int bottomline = 0;
         if((dd==0 && gap%2==0) || (dd==30 && gap%2==1)){
             bottomline = 1;
         }

         LinearLayout myLinearLayout = new LinearLayout(getContext());
         myLinearLayout.setOrientation(LinearLayout.HORIZONTAL);
         LinearLayout.LayoutParams mParams = new LinearLayout.LayoutParams(
                 LinearLayout.LayoutParams.MATCH_PARENT,
                 LinearLayout.LayoutParams.MATCH_PARENT);
         mParams.height = 0;
         mParams.width = LinearLayout.LayoutParams.MATCH_PARENT;
         mParams.weight = gap;
         mParams.gravity = Gravity.CENTER;
         myLinearLayout.setPadding(6,9,6,7);
         myLinearLayout.setLayoutParams(mParams);
//         LayerDrawable shape = getBorders(Color.WHITE, Color.GRAY, 0, 0, 0, bottomline);
//         myLinearLayout.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.bottom_border));

         titleText = new TextView(getContext());
         String cellString = scheduleVo.getMsg().get(k).getSubject();
         if(getType(cellString) && cellString.length() == 4) // ??????????????? ???????????? ????????????.
             titleText.setText(cellString.substring(0,2) + "\n" + cellString.substring(2,4));
         else
             titleText.setText(scheduleVo.getMsg().get(k).getSubject());

         titleText.setTextSize(12);
         if(getType(cellString) && cellString.length() >= 4) {
             titleText.setTextSize(11);
         }
         titleText.setLineSpacing(0.2f, 0.8f);

         //// cell??? ???????????? ??????. ??????????????? ??????????????? ??????.
         int lrPadding = 1;
         if(getTypeEng(cellString)) lrPadding = 4;
         if(gap <= 2)
             titleText.setPadding(lrPadding, 6, lrPadding, 0);
         else
             titleText.setPadding(lrPadding, 10, lrPadding, 0);


         if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
             Typeface typeface = getResources().getFont(R.font.main_font);
             titleText.setTypeface(typeface);
         }else{
             Typeface typeface = Typeface.createFromAsset(getActivity().getAssets(), "notoregular.otf"); // font ???????????? ?????? jua.ttf ????????? typeface??? ??????
             titleText.setTypeface(typeface);
         }
         titleText.setIncludeFontPadding(false);
         titleText.setTextColor(Color.WHITE);
         titleText.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL);
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

         myLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity)getActivity()).startScheduleModify(k);
            }
        });

         return true;
     }
    private boolean drawBlankCell(LinearLayout mLinearLayout, int ii, int dd) {
        // ??? ??????, ?????? index ????????? vo ????????? ??????????????? ?????? ????????? ???.
        // ??????????????? ?????? ?????? ???????????? ??????. gap ????????? weight??? ??? ???.
        int gap = 1;
        LinearLayout myLinearLayout = new LinearLayout(getContext());
        myLinearLayout.setOrientation(LinearLayout.HORIZONTAL);
        LinearLayout.LayoutParams mParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        mParams.height = 0;
        mParams.width = LinearLayout.LayoutParams.MATCH_PARENT;
        mParams.weight = gap;
        mParams.gravity = Gravity.CENTER;
        if(dd == 99) {
            LayerDrawable bottomBorder = getBorders(Color.WHITE, Color.GRAY, 0, 0, 0, 1);
            myLinearLayout.setBackground(bottomBorder);
        }
        myLinearLayout.setLayoutParams(mParams);
        mLinearLayout.addView(myLinearLayout);

//        if(dd == 99)
//            myLinearLayout.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.bottom_border));

        return true;
    }
    private boolean drawTimeCell(LinearLayout mLinearLayout, int time) {
        // ??? ??????, ?????? index ????????? vo ????????? ??????????????? ?????? ????????? ???.
        // ??????????????? ?????? ?????? ???????????? ??????. gap ????????? weight??? ??? ???.
        // 30??? ???????????? ??????????????? ??????_2021.10.14
        int gap = 1;
        titleText = new TextView(getContext());
//        titleText.setText(String.valueOf(time) + "~" + String.valueOf(time+1));
        titleText.setText(String.valueOf(time) + ":00");
        titleText.setTextSize(12);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Typeface typeface = getResources().getFont(R.font.main_font);
            titleText.setTypeface(typeface);
        }else{
            Typeface typeface = Typeface.createFromAsset(getActivity().getAssets(), "notoregular.otf"); // font ???????????? ?????? jua.ttf ????????? typeface??? ??????
            titleText.setTypeface(typeface);
        }
        titleText.setTextColor(getResources().getColor(R.color.dark_gray));
        titleText.setGravity(Gravity.CENTER);
        LayerDrawable bottomBorder = getBorders(Color.WHITE, Color.GRAY, 0, 0, 1, 1);
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

    public void noData(String nvApiParam) {
        tv_no_data.setVisibility(View.VISIBLE);
        ll_area_data.setVisibility(View.GONE);
    }

    public void showPMenu(){
        KidsVo kidsVo = MHDApplication.getInstance().getMHDSvcManager().getKidsVo();
        List<PowerMenuItem> kidsList = new ArrayList();
        for(int k=0; k<kidsVo.getCnt(); k++){
            kidsList.add(new PowerMenuItem(kidsVo.getMsg().get(k).getName(), k == 0 ? true : false));
        }
        powerMenu = new PowerMenu.Builder(getActivity())
                .addItemList(kidsList) //
//                .addItem(new PowerMenuItem("?????????", false)) // add an item.
//                .addItem(new PowerMenuItem("?????????", false)) // aad an item list.
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
}
