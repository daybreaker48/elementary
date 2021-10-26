package com.mhd.elemantary.activity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.Gson;
import com.mhd.elemantary.MainActivity;
import com.mhd.elemantary.R;
import com.mhd.elemantary.adapter.ReCyclerSubjectAdapter;
import com.mhd.elemantary.common.ClickCallbackListener;
import com.mhd.elemantary.common.MHDApplication;
import com.mhd.elemantary.common.vo.MenuVo;
import com.mhd.elemantary.common.vo.SubjectListData;
import com.mhd.elemantary.common.vo.SubjectVo;
import com.mhd.elemantary.common.vo.TodoVo;
import com.mhd.elemantary.constant.MHDConstants;
import com.mhd.elemantary.network.MHDNetworkInvoker;
import com.mhd.elemantary.util.MHDDialogUtil;
import com.mhd.elemantary.util.MHDLog;
import com.mhd.elemantary.util.Util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class ModifyTodoActivity extends BaseActivity implements TextView.OnEditorActionListener  {
    TextView tv_selectday, vst_top_title, tv_todo_subject, tv_todo_subject_detail, tv_todo_subject_2, tv_todo_subject_detail_2;
    TextView tv_rb_daily_progress_2_finishday, tv_startday;
    ImageView vst_right_image;
    private String[] day_array = new String[7];
    String Ptotal, Poneday = "";

    EditText et_daily_radio_2, et_daily_radio_3, et_textbook_publish, et_textbook_subject;
    String displayStrings, innerStrings = "";
    String pGoal = "";
    String pStart = "";
    String selectedSubject = "";
    String selectedDetail = "";
    /* 수정 처리 */
    String dataIndex = "";
    /* 수정 처리 */

    public ReCyclerSubjectAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    ArrayList subjectArrayList, detailArrayList;
    BottomSheetDialog bottomSheetSubject;
    BottomSheetDialog bottomSheetDetail;

    AppCompatButton btn_sun, btn_mon, btn_tues, btn_wed, btn_thur, btn_fri, btn_sat, btn_move_stat_left;

    public ClickCallbackListener callbackListener = new ClickCallbackListener() {
        @Override
        public void callBack(int pos) {
            if(bottomSheetSubject.isShowing()) {
                selectedSubject = subjectArrayList.get(pos).toString();
                tv_todo_subject_2.setText(selectedSubject);
                bottomSheetSubject.dismiss();
                showBottomSheetSubjectDetail(selectedSubject);
                return;
            }
            if(bottomSheetDetail.isShowing()) {
                selectedDetail = detailArrayList.get(pos).toString();
                tv_todo_subject_detail_2.setText(selectedDetail);
                bottomSheetDetail.dismiss();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initialize(R.layout.activity_todo_regist);
        mContext = ModifyTodoActivity.this;

        /* 수정 처리 */
        Intent intent = getIntent();
        int itemPosition = intent.getIntExtra("position", -1);
        TodoVo mTodoVo = MHDApplication.getInstance().getMHDSvcManager().getTodoVo();
        dataIndex = mTodoVo.getMsg().get(itemPosition).getIdx();

        if(itemPosition == -1){
            //비정상적인 접근
            Toast.makeText(mContext, R.string.text_never, Toast.LENGTH_SHORT).show();
            finish();
        }
        /* 수정 처리 */

        btn_move_stat_left = (AppCompatButton) findViewById(R.id.btn_move_stat_left);
        btn_sun = (AppCompatButton) findViewById(R.id.btn_sun);
        btn_mon = (AppCompatButton) findViewById(R.id.btn_mon);
        btn_tues = (AppCompatButton) findViewById(R.id.btn_tues);
        btn_wed = (AppCompatButton) findViewById(R.id.btn_wed);
        btn_thur = (AppCompatButton) findViewById(R.id.btn_thur);
        btn_fri = (AppCompatButton) findViewById(R.id.btn_fri);
        btn_sat = (AppCompatButton) findViewById(R.id.btn_sat);

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

        bottomSheetDetail = new BottomSheetDialog(this);

        /* 수정 처리 */
        selectedSubject = mTodoVo.getMsg().get(itemPosition).getSubject();
        selectedDetail = mTodoVo.getMsg().get(itemPosition).getDetail();
        /* 수정 처리 */

        tv_todo_subject = (TextView) findViewById(R.id.tv_todo_subject);
        tv_todo_subject_detail = (TextView) findViewById(R.id.tv_todo_subject_detail);
        tv_todo_subject_2 = (TextView) findViewById(R.id.tv_todo_subject_2);
        tv_todo_subject_detail_2 = (TextView) findViewById(R.id.tv_todo_subject_detail_2);
        /* 수정 처리 */
        tv_todo_subject_2.setText(selectedSubject);
        tv_todo_subject_detail_2.setText(selectedDetail);
        /* 수정 처리 */

        tv_todo_subject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { showBottomSheetSubject(); }
        });
        tv_todo_subject_detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { showBottomSheetSubjectDetail(selectedSubject); }
        });
        tv_todo_subject_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { showBottomSheetSubject(); }
        });
        tv_todo_subject_detail_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { showBottomSheetSubjectDetail(selectedSubject); }
        });

        tv_startday = (TextView) findViewById(R.id.tv_startday);
        tv_rb_daily_progress_2_finishday = (TextView) findViewById(R.id.tv_rb_daily_progress_2_finishday);
        vst_top_title = (TextView) findViewById(R.id.vst_top_title);

        MenuVo menuVo = MHDApplication.getInstance().getMHDSvcManager().getMenuVo();
        String displayKid = "";
        for(int k=0; k<menuVo.getMsg().size(); k++){
            if("TO".equals(menuVo.getMsg().get(k).getMenuname())){
                // 해당메뉴에 설정된 아이정보
                displayKid = menuVo.getMsg().get(k).getKidname();
            }
        }
        vst_top_title.setText("["+displayKid+"] "+ getString(R.string.title_todo_modify));
        tv_selectday = (TextView) findViewById(R.id.tv_selectday);
        tv_selectday.setText(getString(R.string.content_dailyprogress));

        et_daily_radio_2 = (EditText) findViewById(R.id.et_daily_radio_2);
        et_daily_radio_3 = (EditText) findViewById(R.id.et_daily_radio_3);
        et_textbook_publish = (EditText) findViewById(R.id.et_textbook_publish);
        et_textbook_subject = (EditText) findViewById(R.id.et_textbook_subject);
        /* 수정 처리 */
        et_textbook_publish.setText(mTodoVo.getMsg().get(itemPosition).getPublish());
        et_textbook_subject.setText(mTodoVo.getMsg().get(itemPosition).getTitle());
        /* 수정 처리 */
        AppCompatButton btn_todo_cancel = (AppCompatButton) findViewById(R.id.btn_todo_cancel);
        et_daily_radio_2.setOnEditorActionListener(this);
        et_daily_radio_3.setOnEditorActionListener(this);
        btn_todo_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { finish(); }
        });
        btn_move_stat_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { finish(); }
        });
        AppCompatButton btn_todo_save = (AppCompatButton) findViewById(R.id.btn_todo_save);
        btn_todo_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveProcess();
            }
        });

        /* 수정 처리 */
        if("Y".equals(mTodoVo.getMsg().get(itemPosition).getSun()))
            selectDay(0);
        if("Y".equals(mTodoVo.getMsg().get(itemPosition).getMon()))
            selectDay(1);
        if("Y".equals(mTodoVo.getMsg().get(itemPosition).getTue()))
            selectDay(2);
        if("Y".equals(mTodoVo.getMsg().get(itemPosition).getWed()))
            selectDay(3);
        if("Y".equals(mTodoVo.getMsg().get(itemPosition).getThu()))
            selectDay(4);
        if("Y".equals(mTodoVo.getMsg().get(itemPosition).getFri()))
            selectDay(5);
        if("Y".equals(mTodoVo.getMsg().get(itemPosition).getSat()))
            selectDay(6);
        /* 수정 처리 */

        // 만약 목표일을 입력하게 한다면 그 달력 창을 띄우는 부분. 관련 부분들
//        btn_todo_goal = (AppCompatButton) findViewById(R.id.btn_todo_goal);
//        btn_todo_goal.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                final Calendar c = Calendar.getInstance();
//                int year = c.get(Calendar.YEAR);
//                int month = c.get(Calendar.MONTH);
//                int day = c.get(Calendar.DAY_OF_MONTH);
//                baseDate = new GregorianCalendar(year, month, day);
//                DatePickerDialog dialog = new DatePickerDialog(mContext, callbackMethod, year, month, day);
//                dialog.getDatePicker().setMinDate(c.getTimeInMillis());
//                dialog.show();
//            }
//        });
//        callbackMethod = new DatePickerDialog.OnDateSetListener()
//        {
//            @Override
//            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth)
//            {
//                // 선택한 날짜가 오늘보다 이전인지 체크. 이전이면 오류
//                btn_todo_goal.setText(year + "년 " + monthOfYear + "월 " + dayOfMonth + "일");
//                targetDate =  new GregorianCalendar(year, monthOfYear, dayOfMonth);
//
//                et_daily_radio_4.setFocusableInTouchMode(true);
//                et_daily_radio_4.requestFocus();
//                InputMethodManager manager = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
//                manager.showSoftInput(et_daily_radio_4, InputMethodManager.SHOW_IMPLICIT);
//            }
//        };

        checkSubject();

        /* 수정 처리 */
        vst_right_image = (ImageView) findViewById(R.id.vst_right_image);
        vst_right_image.setVisibility(View.VISIBLE);
        vst_right_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MHDDialogUtil.sAlert(ModifyTodoActivity.this, R.string.confirm_delete, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteTodo(mTodoVo.getMsg().get(itemPosition).getIdx());
                    }
                }, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
            }
        });

        int rest = Integer.parseInt(mTodoVo.getMsg().get(itemPosition).getRest());
        int toto = Integer.parseInt(mTodoVo.getMsg().get(itemPosition).getTotal());
        int oned = Integer.parseInt(mTodoVo.getMsg().get(itemPosition).getOneday());
        Poneday = mTodoVo.getMsg().get(itemPosition).getOneday();
        Ptotal = String.valueOf(toto);
        et_daily_radio_2.setText(String.valueOf(toto));
        et_daily_radio_3.setText(Poneday);
        et_daily_radio_2.setClickable(false);
        et_daily_radio_2.setFocusable(false);
        tv_startday.setText(getString(R.string.content_base_start_todo) + " " + mTodoVo.getMsg().get(itemPosition).getTdstart().substring(0,10));

        if(toto > 0)
            calcGoal();
        /* 수정 처리 */
    }

    public void selectDay(int day) {
        switch (day) {
            case 0:
                if(Arrays.asList(day_array).contains(getString(R.string.content_sun))) {
                    day_array[0] = "";
                    btn_sun.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.circle_transparent, null));
                }else {
                    day_array[0] = getString(R.string.content_sun);
                    btn_sun.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.circle_toggle_transparent, null));
                }
                break;
            case 1:
                if(Arrays.asList(day_array).contains(getString(R.string.content_mon))) {
                    day_array[1] = "";
                    btn_mon.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.circle_transparent, null));
                }else {
                    day_array[1] = getString(R.string.content_mon);
                    btn_mon.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.circle_toggle_transparent, null));
                }
                break;
            case 2:
                if(Arrays.asList(day_array).contains(getString(R.string.content_tues))) {
                    day_array[2] = "";
                    btn_tues.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.circle_transparent, null));
                }else {
                    day_array[2] = getString(R.string.content_tues);
                    btn_tues.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.circle_toggle_transparent, null));
                }
                break;
            case 3:
                if(Arrays.asList(day_array).contains(getString(R.string.content_wed))) {
                    day_array[3] = "";
                    btn_wed.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.circle_transparent, null));
                }else {
                    day_array[3] = getString(R.string.content_wed);
                    btn_wed.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.circle_toggle_transparent, null));
                }
                break;
            case 4:
                if(Arrays.asList(day_array).contains(getString(R.string.content_thur))) {
                    day_array[4] = "";
                    btn_thur.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.circle_transparent, null));
                }else {
                    day_array[4] = getString(R.string.content_thur);
                    btn_thur.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.circle_toggle_transparent, null));
                }
                break;
            case 5:
                if(Arrays.asList(day_array).contains(getString(R.string.content_fri))) {
                    day_array[5] = "";
                    btn_fri.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.circle_transparent, null));
                }else {
                    day_array[5] = getString(R.string.content_fri);
                    btn_fri.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.circle_toggle_transparent, null));
                }
                break;
            case 6:
                if(Arrays.asList(day_array).contains(getString(R.string.content_sat))) {
                    day_array[6] = "";
                    btn_sat.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.circle_transparent, null));
                }else {
                    day_array[6] = getString(R.string.content_sat);
                    btn_sat.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.circle_toggle_transparent, null));
                }
                break;
            default:

                break;
        }

        displayStrings = "";
        innerStrings = "";
        for (String days : day_array) {
            if(days != null && !days.isEmpty()) {
                displayStrings = (displayStrings == null || displayStrings.isEmpty()) ? days : displayStrings + ", " + days;
                if("일".equals(days)) innerStrings = (innerStrings == null || innerStrings.isEmpty()) ? "1" : innerStrings + "1";
                if("월".equals(days)) innerStrings = (innerStrings == null || innerStrings.isEmpty()) ? "2" : innerStrings + "2";
                if("화".equals(days)) innerStrings = (innerStrings == null || innerStrings.isEmpty()) ? "3" : innerStrings + "3";
                if("수".equals(days)) innerStrings = (innerStrings == null || innerStrings.isEmpty()) ? "4" : innerStrings + "4";
                if("목".equals(days)) innerStrings = (innerStrings == null || innerStrings.isEmpty()) ? "5" : innerStrings + "5";
                if("금".equals(days)) innerStrings = (innerStrings == null || innerStrings.isEmpty()) ? "6" : innerStrings + "6";
                if("토".equals(days)) innerStrings = (innerStrings == null || innerStrings.isEmpty()) ? "7" : innerStrings + "7";
            }
        }
        if(innerStrings.isEmpty()) {
            tv_selectday.setText(getString(R.string.content_dailyprogress));
            /* 수정 처리 */
//            tv_startday.setText(getString(R.string.content_start_todo));
            /* 수정 처리 */
            tv_rb_daily_progress_2_finishday.setText(getString(R.string.content_finish_todo));
        }else{
            tv_selectday.setText("매주 " + displayStrings);

            Calendar cal = Calendar.getInstance();
            int weekd = cal.get(Calendar.DAY_OF_WEEK); // 오늘 요일
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            if(weekd == 7){//
                String startday = innerStrings.substring(0,1);
                cal.add(Calendar.DATE, Integer.parseInt(startday));
                pStart = df.format(cal.getTime());
                /* 수정 처리 */
//                tv_startday.setText(getString(R.string.content_start_todo) + " " + pStart);
                /* 수정 처리 */
            }else{
                String[] strArray = innerStrings.split("");
                int curWeekCount = 0;
                for(String s : strArray) {
                    if(Integer.parseInt(s) > weekd) {
                        curWeekCount++;
                        cal.add(Calendar.DATE, Integer.parseInt(s) - weekd);
                        pStart = df.format(cal.getTime());
                        /* 수정 처리 */
//                        tv_startday.setText(getString(R.string.content_start_todo) + " " + pStart);
                        /* 수정 처리 */
                        break;
                    }
                }
                // curWeekCount == 0 이면 오늘 요일보다 뒤에 학습일이 없다는 것. 다음 주로 넘어가야 한다.
                // curWeekCount > 0 이면 오늘 요일보다 뒤에 학습일이 있다는 것. 그 요일이 첫 학습일이다.
                if(curWeekCount == 0){
                    String startday = innerStrings.substring(0,1);
                    cal.add(Calendar.DATE, 7 - weekd + Integer.parseInt(startday));
                    pStart = df.format(cal.getTime());
                    /* 수정 처리 */
//                    tv_startday.setText(getString(R.string.content_start_todo) + " " + pStart);
                    /* 수정 처리 */
                }
            }
            Poneday = (et_daily_radio_3.getText() == null) ? "" : et_daily_radio_3.getText().toString();
            Ptotal = (et_daily_radio_2.getText() == null) ? "" : et_daily_radio_2.getText().toString();

            if(Poneday!=null && !"".equals(Poneday) && Ptotal!=null && !"".equals(Ptotal)){
                calcGoal();
            }
        }
    }

    /**
     * check subject
     */
    public void checkSubject(){
        try {
            Map<String, String> params = new HashMap<String, String>();
            params.put("UUMAIL", MHDApplication.getInstance().getMHDSvcManager().getUserVo().getUuMail());

            MHDNetworkInvoker.getInstance().sendVolleyRequest(ModifyTodoActivity.this, R.string.url_restapi_check_subject, params, responseListener);
        } catch (Exception e) {
            MHDLog.printException(e);
        }
    }

    @Override
    protected boolean networkResponseProcess(String result) {
        boolean resultFlag = super.networkResponseProcess(result);
        MHDLog.d(TAG, "networkResponseProcess resultFlag >>> " + resultFlag);
        SubjectVo subjectVo = null;

        if(!resultFlag) return resultFlag;

        if("M".equals(nvResultCode)){
            // Just show nvMsg
            MHDDialogUtil.sAlert(mContext, nvMsg);
            return true;
        }else if("S".equals(nvResultCode)){
            if(nvApi.equals(getString(R.string.restapi_check_subject))) {
                if (nvCnt == 0) {
                    // 정보가 없으면 비정상
                    // 우선 toast를 띄울 것.
                    Toast.makeText(mContext, nvMsg, Toast.LENGTH_SHORT).show();
                } else {
                    // 과목정보를 받아옴.
                    // 현재는 DB 에 있는것만. 나중에는 사용자가 입력한 것도 가져오도록.
                    Gson gson = new Gson();
                    subjectVo = gson.fromJson(nvJsonDataString, SubjectVo.class);
                    MHDApplication.getInstance().getMHDSvcManager().setSubjectVo(null);
                    MHDApplication.getInstance().getMHDSvcManager().setSubjectVo(subjectVo);
                }

                String prevSubject = "";
                ArrayList subjectArrayList = new ArrayList<>();
                ArrayList detailArrayList = new ArrayList<>();
                // 과목명만 group by로 추출할 때 사용
                for(int i=0; i<nvCnt; i++){
                    if(i>0 && prevSubject.equals(subjectVo.getMsg().get(i).getSubject()))
                        continue;
                    else {
                        subjectArrayList.add(subjectVo.getMsg().get(i).getSubject());
                        prevSubject = subjectVo.getMsg().get(i).getSubject();
                    }
                }

                // 과목 - 상세 형식으로 추출할 때 사용
                for(int i=0; i<nvCnt; i++){
                    subjectArrayList.add(subjectVo.getMsg().get(i).getSubject() + " - " + subjectVo.getMsg().get(i).getDetail());
                }

                return true;
            }else if(nvApi.equals(getString(R.string.restapi_modify_todo))){
                if (nvCnt == 0) {
                    // 정보가 없으면 비정상
                    // 우선 toast를 띄울 것.
                    Toast.makeText(mContext, nvMsg, Toast.LENGTH_SHORT).show();
                } else {
                    // 학습 정상 수정 여부를 알림
                    MHDDialogUtil.sAlert(mContext, R.string.alert_networkRequestSuccess, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            setResult(Activity.RESULT_OK);
                            finish();
                            return;
                        }
                    });
                }
                return true;
            }else if(nvApi.equals(getString(R.string.restapi_delete_todo))){
                if (nvCnt == 0) {
                    // 정보가 없으면 비정상
                    // 우선 toast를 띄울 것.
                    Toast.makeText(mContext, nvMsg, Toast.LENGTH_SHORT).show();
                } else {
                    // 학습 정상 삭제 여부를 알림
                    MHDDialogUtil.sAlert(mContext, R.string.alert_networkRequestSuccess, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            setResult(Activity.RESULT_OK);
                            finish();
                            return;
                        }
                    });
                }
                return true;
            }
        }
        return true;
    }

    @Override
    public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
        if (i == EditorInfo.IME_ACTION_DONE || i == EditorInfo.IME_ACTION_NEXT) {
            switch (textView.getId()) {
                case R.id.et_daily_radio_2: // 총페이지
                    Poneday = (et_daily_radio_3.getText() == null) ? "" : et_daily_radio_3.getText().toString();
                    if ("".equals(Poneday) || Poneday == null) {
                        // 총페이지 입력 여부는 옵션. 하루분량을 입력 안했다면 되돌린다.
                        Toast.makeText(mContext, "하루분량을 입력해주세요.", Toast.LENGTH_SHORT).show();
                        et_daily_radio_3.setFocusableInTouchMode(true);
                        et_daily_radio_3.requestFocus();
                        return true;
                    }else{
                        // 하루분량 입력했다면.
                        Ptotal = (et_daily_radio_2.getText() == null) ? "" : et_daily_radio_2.getText().toString();
                        if ("".equals(Ptotal) || Ptotal == null) {
                            // 총페이지 입력 안했다면 그냥 패스. 종료일 계산 안하기 때문에. 바로 저정해도 된다.
                        }else{
                            // 총페이지 입력했다면. 둘다 입력한 것.
                            // 하루분량이 총페이지보다 더 큰지 체크.
                            if (Integer.parseInt(Ptotal) < Integer.parseInt(Poneday)) {
                                Toast.makeText(mContext, "총페이지보다 하루분량이 작아야 합니다.", Toast.LENGTH_SHORT).show();
                                et_daily_radio_2.setFocusableInTouchMode(true);
                                et_daily_radio_2.requestFocus();
                                break;
                            }
                            // 요일 선택했는가?
                            if ("".equals(innerStrings)) {
                                Toast.makeText(mContext, getString(R.string.content_dailyprogress), Toast.LENGTH_SHORT).show();
                                break;
                            }
                            // 문제없으면 종료일 계산.
                            calcGoal();
                        }
                    }
                    break;
                case R.id.et_daily_radio_3: // 하루분량
                    // 하루분량 입력안했으면 되돌린다. 했으면 총페이지로 이동시키면 된다.
                    // 혹 이미 총페이지 입력된 상태면 크기 비교를 하고 문제없으면 종료일 계산.
                    Poneday = (et_daily_radio_3.getText() == null) ? "" : et_daily_radio_3.getText().toString();
                    if ("".equals(Poneday) || Poneday == null) {
                        Toast.makeText(mContext, "하루분량을 입력해주세요.", Toast.LENGTH_SHORT).show();
                        et_daily_radio_3.setFocusableInTouchMode(true);
                        et_daily_radio_3.requestFocus();
                        break;
                    }
                    Ptotal = (et_daily_radio_2.getText() == null) ? "" : et_daily_radio_2.getText().toString();
                    if ("".equals(Ptotal) || Ptotal == null) { // 총페이지 입력 안된 상태면 그리(아래로) 보낸다.
                        et_daily_radio_2.setFocusableInTouchMode(true);
                        et_daily_radio_2.requestFocus();
                    }else{ // 총페이지 입력된 상태면 크기비교.
                        if (Integer.parseInt(Ptotal) < Integer.parseInt(Poneday)) {
                            Toast.makeText(mContext, "총페이지보다 하루분량이 작아야 합니다.", Toast.LENGTH_SHORT).show();
                            et_daily_radio_3.setFocusableInTouchMode(true);
                            et_daily_radio_3.requestFocus();
                            break;
                        }
                        // 요일 선택했는가?
                        if ("".equals(innerStrings)) {
                            Toast.makeText(mContext, getString(R.string.content_dailyprogress), Toast.LENGTH_SHORT).show();
                            break;
                        }
                        // 문제없으면 종료일 계산.
                        calcGoal();
                    }
                    break;
                // 만약 목표일을 입력하게 한다면 그 달력 창을 띄우는 부분. 관련 부분들
//                case R.id.et_daily_radio_4:
//                    // 이 항목은 업데이트 버전에서 다룬다.(update version)
//                    // 하루 할당량 계산. 목표일 설정 체크
//                    if (targetDate == null) {
//                        Toast.makeText(mContext, "목표일을 설정해주세요.", Toast.LENGTH_SHORT).show();
//                        break;
//                    }
//                    Gtotal = (et_daily_radio_4.getText() == null) ? "" : et_daily_radio_4.getText().toString();
//                    if ("".equals(Gtotal) || Gtotal == null) {
//                        Toast.makeText(mContext, "총페이지를 입력해주세요.", Toast.LENGTH_SHORT).show();
//                        et_daily_radio_4.setFocusableInTouchMode(true);
//                        et_daily_radio_4.requestFocus();
//                        break;
//                    }
//                    // 오늘부터 목표일 까지의 날 수 계산(나중에는 시작일, 주말제외 옵셥을 넣어야 함)
//                    long diffSec = (targetDate.getTimeInMillis() - baseDate.getTimeInMillis()) / 1000;
//                    long diffDay = diffSec / (24 * 60 * 60);
//                    int diffDayInt = (int) diffDay + 1;
//                    int totoal_1_int = Integer.parseInt(Gtotal);
//                    // 날수와 총페이지수로 하루분량을 계산해서 edittext에 표시.
//                    MHDLog.d(TAG, " diffDay: " + diffDayInt); // 날수
//                    MHDLog.d(TAG, " totoal_1_int: " + totoal_1_int); // 총페이지
//                    if (diffDayInt > totoal_1_int) {
//                        Toast.makeText(mContext, "목표일까지 날짜 수가 총페이지 수보다 큽니다", Toast.LENGTH_SHORT).show();
//                        et_daily_radio_4.setFocusableInTouchMode(true);
//                        et_daily_radio_4.requestFocus();
//                        break;
//                    }
//                    // 몫과 나머지를 구한다. 몫이 하루 고정 페이지수이고, 나머지가 마지막 날 고정페이지수에 더해야 하는 것이다.
//                    int plusPage = totoal_1_int % diffDayInt;
//                    int dayPage = totoal_1_int / diffDayInt;
//                    MHDLog.d(TAG, " plusPage: " + plusPage); // 남는페이지
//                    MHDLog.d(TAG, " dayPage: " + dayPage); // 하루고정페이지
//                    break;
            }
        }
        return false;
    }

    private void calcGoal(){
        // 여기까지 문제없으면 종료예정일을 계산해서 아래 표시할 것.
        // 총 몇일의 학습일이 필요한지 계산. 나머지는 분배보다는 마지막 하루로 계산.
        // 시작일을 설정하는 기능도 필요할거 같긴하지만 이건 나중에 구현. 등록 당일은 제외
        // 종료일을 DB에 넣고 종료일에 오기 전에는 정해진 요일에 표시하는 방식.
        // 중간에 그냥 건너뛰는 날, 건너뛰어야 하는 날을 어떻게 처리하지.
        long dayCount = Integer.parseInt(Ptotal) / Integer.parseInt(Poneday);
        long dayRest = Integer.parseInt(Ptotal) % Integer.parseInt(Poneday);
        dayRest = dayRest > 0 ? 1 : 0;
        int dayTotal = (int) dayCount + (int) dayRest; // 소요 day 수, 나머지(dayRest)가 0이면 그냥 0인거니...
        Calendar cal = Calendar.getInstance();
        int weekd = cal.get(Calendar.DAY_OF_WEEK); // 오늘 요일
        weekd = weekd == 7 ? 1 : weekd + 1; // 시작요일(오늘 제외한 다음 날. 이번주 계산에만 사용).
        int lengthWeek = innerStrings.length(); // 한주 동안의 학습일 수
        int spendWeek, extraDays = 0;
        if (weekd == 1) {
            // 이 값이 1이면 한주의 끝. 토요일에 등록한다는 것.
            // weekd = 1이면 이번주는 한주의 처음부터 시작하면 되는 것.
            spendWeek = dayTotal / lengthWeek;
            extraDays = dayTotal % lengthWeek;

            if (extraDays == 0) {
                // extraDays = 0 이면 주 단위로 딱 떨어지는 것.
                String lastDays = innerStrings.substring(lengthWeek - 1); // 한 주의 마지막 학습요일. spendWeek주 후의 이 요일이 종료일이다.
                // 다음주(등록일이 토요일이니) 마지막 학습요일의 날짜를 구한다. 등록하는 날짜에서 요일코드만큼 더한 날짜와 같다.
                cal.setTime(new Date());
                cal.add(Calendar.DATE, 1); // 다음주의 시작이(일요일) 된다.
                DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
//                            System.out.println("current: " + df.format(cal.getTime()));
//                            cal.add(Calendar.MONTH, 2);
                cal.add(Calendar.DATE, Integer.parseInt(lastDays) - 1);
                // 여기서 spendWeek주 후 날짜를 구한다.  spendWeek*7
                cal.add(Calendar.DATE, (spendWeek - 1) * 7);
                // 이 날짜가 종료일이다.
                pGoal = df.format(cal.getTime());
                tv_rb_daily_progress_2_finishday.setText(getString(R.string.content_finish_todo) + " " + pGoal);
            } else {
                // extraDays > 0 이면 그 다음 주 추가학습일이 필요한 것.
                String lastDays = innerStrings.substring(lengthWeek - 1); // 한 주의 마지막 학습요일. spendWeek주 후, 그 다음주의 extraDays 값에 해당하는 인덱스의 요일코드만큼 더한 날짜가 종료일이다.
                // 다음주(등록일이 토요일이니) 마지막 학습요일의 날짜를 구한다. 등록하는 날짜에서 요일코드만큼 더한 날짜와 같다.
                cal.setTime(new Date());
                cal.add(Calendar.DATE, 1); // 다음주의 시작이(일요일) 된다.
                DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
//                            System.out.println("current: " + df.format(cal.getTime()));
//                            cal.add(Calendar.MONTH, 2);
                cal.add(Calendar.DATE, Integer.parseInt(lastDays) - 1);
                // 여기서 spendWeek주 후 날짜를 구한다.  spendWeek*7
                cal.add(Calendar.DATE, (spendWeek - 1) * 7);
                // 여기서 extraDays 값의 index-1에 해당하는 요일 코드를 갸져온다.
                String lastDaysCode = innerStrings.substring(extraDays - 1, extraDays);
                // 다음 주에 위에서 구한 요일코드에 해당하는 날짜를 가져오면 된다.
                // 그러기 위해서 우선 이번 주의 해당 요일코드에 해당하는 날짜를 구하고
                cal.set(Calendar.DAY_OF_WEEK, Integer.parseInt(lastDaysCode));
                // 거기에 1주일을 더 하고
                cal.add(Calendar.DATE, 7);
                // 이 날짜가 종료일이다.
                pGoal = df.format(cal.getTime());
                tv_rb_daily_progress_2_finishday.setText(getString(R.string.content_finish_todo) + " " + pGoal);
            }
        } else {
            // weekd > 1이면 weekd = 1 일때와 비슷한 방식이지만. 그 전에 먼저 해야할 것이
            // 이번 주 남은 학습일(currentWeekRestDays)을 구해서 그 학습일 수 만큼을 뺀 상태에서 weekd = 1 일때의 프로세스를 타는 것.
            // 이 얘기는 곧. dayTotal 의 값이 dayTotal - currentWeekRestDays 가 된다는 것.
            String[] strArray = innerStrings.split("");
            int curWeekCount = 0;
            for (String s : strArray) {
                if (Integer.parseInt(s) >= weekd) curWeekCount++;
            }
            // curWeekCount > 0 이라면 이번주에 학습일 요일이 있다는 것.
            // 그런데 이번주 그 남은 학습일이 전체 소요일보다 작거나 같을수도 있겠구나.
//                        if(curWeekCount >= dayTotal)
            dayTotal = dayTotal - curWeekCount;
            //========================================================================================
            // 여기부터는 weekd = 1인 것과 동일한 프로세스
            spendWeek = dayTotal / lengthWeek;
            extraDays = dayTotal % lengthWeek;

            if (extraDays == 0) {
                // extraDays = 0 이면 주 단위로 딱 떨어지는 것.
                String lastDays = innerStrings.substring(lengthWeek - 1); // 한 주의 마지막 학습요일. spendWeek주 후의 이 요일이 종료일이다.
                // 이번주 마지막 학습요일의 날짜를 구한다. 등록하는 날짜에서 요일코드만큼 더한 날짜와 같다.
                cal.setTime(new Date());
                cal.add(Calendar.DATE, 9 - weekd); // 다음주의 시작이(일요일) 된다.
                DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
//                            System.out.println("current: " + df.format(cal.getTime()));
//                            cal.add(Calendar.MONTH, 2);
                cal.add(Calendar.DATE, Integer.parseInt(lastDays) - 1);
                // 여기서 spendWeek주 후 날짜를 구한다.  spendWeek*7
                cal.add(Calendar.DATE, (spendWeek - 1) * 7);
                // 이 날짜가 종료일이다.
                pGoal = df.format(cal.getTime());
                tv_rb_daily_progress_2_finishday.setText(getString(R.string.content_finish_todo) + " " + pGoal);
            } else {
                // extraDays > 0 이면 그 다음 주 추가학습일이 필요한 것.
                String lastDays = innerStrings.substring(lengthWeek - 1); // 한 주의 마지막 학습요일. spendWeek주 후, 그 다음주의 extraDays 값에 해당하는 인덱스의 요일코드만큼 더한 날짜가 종료일이다.
                // 이번주 마지막 학습요일의 날짜를 구한다. 등록하는 날짜에서 요일코드만큼 더한 날짜와 같다.
                cal.setTime(new Date());
                cal.add(Calendar.DATE, 9 - weekd); // 다음주의 시작이(일요일) 된다.
                DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
//                            System.out.println("current: " + df.format(cal.getTime()));
//                            cal.add(Calendar.MONTH, 2);
                cal.add(Calendar.DATE, Integer.parseInt(lastDays) - 1);
                // 여기서 spendWeek주 후 날짜를 구한다.  spendWeek*7
                cal.add(Calendar.DATE, (spendWeek - 1) * 7);
                // 여기서 extraDays 값의 index-1에 해당하는 요일 코드를 갸져온다.
                String lastDaysCode = innerStrings.substring(extraDays - 1, extraDays);
                // 다음 주에 위에서 구한 요일코드에 해당하는 날짜를 가져오면 된다.
                // 그러기 위해서 우선 이번 주의 해당 요일코드에 해당하는 날짜를 구하고
                cal.set(Calendar.DAY_OF_WEEK, Integer.parseInt(lastDaysCode));
                // 거기에 1주일을 더 하고
                cal.add(Calendar.DATE, 7);
                // 이 날짜가 종료일이다.
                pGoal = df.format(cal.getTime());
                tv_rb_daily_progress_2_finishday.setText(getString(R.string.content_finish_todo) + " " + pGoal);
            }
        }
    }

    private void saveProcess() {
        String tmpSubject = selectedSubject;
        String tmpDetail = selectedDetail;
        String tmpPublisher = et_textbook_publish.getText().toString();
        String tmpTitle = et_textbook_subject.getText().toString();

        if (tmpSubject == null || "".equals(tmpSubject)) {
            // 과목 선택 안했다.
            Toast.makeText(mContext, getString(R.string.content_todo_subject), Toast.LENGTH_SHORT).show();
        } else if (tmpDetail == null || "".equals(tmpDetail)) {
            // 세부항목 선택 안했다.
            Toast.makeText(mContext, getString(R.string.content_todo_detail), Toast.LENGTH_SHORT).show();
        } else if (tmpPublisher == null || "".equals(tmpPublisher)) {
            // 출판사가 없다.
            Toast.makeText(mContext, getString(R.string.content_todo_publisher), Toast.LENGTH_SHORT).show();
        } else if (tmpTitle == null || "".equals(tmpTitle)) {
            // 교재명이 없다.
            Toast.makeText(mContext, getString(R.string.content_todo_title), Toast.LENGTH_SHORT).show();
        } else if ("".equals(innerStrings)) {
            // 학습 날짜 정보가 없다.
            Toast.makeText(mContext, getString(R.string.content_dailyprogress), Toast.LENGTH_SHORT).show();
        } else {
            Poneday = (et_daily_radio_3.getText() == null) ? "" : et_daily_radio_3.getText().toString();
            Ptotal = (et_daily_radio_2.getText() == null) ? "" : et_daily_radio_2.getText().toString();

            // 필수는 Poneday
            if ("".equals(Poneday) || Poneday == null) {
                Toast.makeText(mContext, "하루분량을 입력해주세요.", Toast.LENGTH_SHORT).show();
                et_daily_radio_3.setFocusableInTouchMode(true);
                et_daily_radio_3.requestFocus();
            } else {
                if (!"".equals(Ptotal) && Ptotal != null) {
                    // 총페이지가 입력되어 있다면 크기 비교
                    if (Integer.parseInt(Ptotal) < Integer.parseInt(Poneday)) {
                        Toast.makeText(mContext, "총페이지보다 하루분량이 작아야 합니다.", Toast.LENGTH_SHORT).show();
                        et_daily_radio_3.setFocusableInTouchMode(true);
                        et_daily_radio_3.requestFocus();
                    }else{
                        // 크기 문제 없다면. 둘 다 입력되어 있는 것. 종료일 계산
                        calcGoal();
                        sendTodoData(Poneday, Ptotal, pGoal, tmpSubject, tmpDetail, tmpPublisher, tmpTitle, "P");
                    }
                }else{
                    // 총페이지가 입력되어 있지 않다면 하루분량만으로 저장.
                    sendTodoData(Poneday, "0", "2100-12-31", tmpSubject, tmpDetail, tmpPublisher, tmpTitle, "F");
                }
            }
        }
    }

    private void sendTodoData(String oneday, String total, String goal, String subject, String detail, String publisher, String title, String option){
        try {
            MenuVo menuVo = MHDApplication.getInstance().getMHDSvcManager().getMenuVo();
            String dKid = "";
            for(int k=0; k<menuVo.getMsg().size(); k++){
                if("TO".equals(menuVo.getMsg().get(k).getMenuname())){
                    // 해당메뉴에 설정된 아이정보
                    dKid = menuVo.getMsg().get(k).getKidname();
                }
            }

            // Map 방식 0
            Map<String, String> params = new HashMap<String, String>();
            params.put("UUMAIL", MHDApplication.getInstance().getMHDSvcManager().getUserVo().getUuMail());
            params.put("TKNAME", dKid);
            params.put("TBSUBJECT", subject);
            params.put("TBDETAIL", detail);
            params.put("TBPUBLISHER", publisher);
            params.put("TBTITLE", title);
            params.put("TDOPTION", option);
            params.put("TDSUN", innerStrings.contains("1") ? "Y" : "N");
            params.put("TDMON", innerStrings.contains("2") ? "Y" : "N");
            params.put("TDTUE", innerStrings.contains("3") ? "Y" : "N");
            params.put("TDWED", innerStrings.contains("4") ? "Y" : "N");
            params.put("TDTHU", innerStrings.contains("5") ? "Y" : "N");
            params.put("TDFRI", innerStrings.contains("6") ? "Y" : "N");
            params.put("TDSAT", innerStrings.contains("7") ? "Y" : "N");
            params.put("TDONEDAY", oneday);
            params.put("TDTOTAL", total);
            params.put("TDGOAL", goal);
            params.put("TDSTART", pStart);
            params.put("IDX", dataIndex);
            MHDNetworkInvoker.getInstance().sendVolleyRequest(mContext, R.string.url_restapi_modify_todo, params, responseListener);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            MHDLog.printException(e);
        }
    }

    private void showBottomSheetSubject() {
        bottomSheetSubject = new BottomSheetDialog(this);
        bottomSheetSubject.setContentView(R.layout.bottom_sheet_dialog);

        RecyclerView recyclerView = (RecyclerView) bottomSheetSubject.findViewById(R.id.recv_subject);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(mContext);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new ReCyclerSubjectAdapter();
        recyclerView.setAdapter(adapter);

        SubjectVo subjectVo = MHDApplication.getInstance().getMHDSvcManager().getSubjectVo();
        String prevSubject = "";
        subjectArrayList = new ArrayList<>();
        // 과목명만 group by로 추출할 때 사용
        for(int i=0; i<nvCnt; i++){
            if(i>0 && prevSubject.equals(subjectVo.getMsg().get(i).getSubject()))
                continue;
            else {
                subjectArrayList.add(subjectVo.getMsg().get(i).getSubject());
                prevSubject = subjectVo.getMsg().get(i).getSubject();
            }
        }

        for(int i=0; i<subjectArrayList.size(); i++){
            // 각 List의 값들을 data 객체에 set 해줍니다.
            SubjectListData data = new SubjectListData();
            data.setSubject(subjectArrayList.get(i).toString());

            // 각 값이 들어간 data를 adapter에 추가합니다.
            adapter.addItem(data);
        }

        adapter.setCallbackListener(callbackListener);
        // adapter의 값이 변경되었다는 것을 알려줍니다.
        adapter.notifyDataSetChanged();

        bottomSheetSubject.show();
    }
    private void showBottomSheetSubjectDetail(String subejct) {
        // subject가 선택되지 않았다면 subject 선택 창 띄우기
        if("".equals(subejct)){
            showBottomSheetSubject();
            return;
        }
        bottomSheetDetail = new BottomSheetDialog(this);
        bottomSheetDetail.setContentView(R.layout.bottom_sheet_dialog);

        RecyclerView recyclerView = (RecyclerView) bottomSheetDetail.findViewById(R.id.recv_subject);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(mContext);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new ReCyclerSubjectAdapter();
        recyclerView.setAdapter(adapter);

        SubjectVo subjectVo = MHDApplication.getInstance().getMHDSvcManager().getSubjectVo();
        String prevDetail = "";
        detailArrayList = new ArrayList<>();
        // 과목명만 group by로 추출할 때 사용
        for(int i=0; i<nvCnt; i++){
            if(subejct.equals(subjectVo.getMsg().get(i).getSubject())) {
                if (i > 0 && prevDetail.equals(subjectVo.getMsg().get(i).getDetail()))
                    continue;
                else {
                    detailArrayList.add(subjectVo.getMsg().get(i).getDetail());
                    prevDetail = subjectVo.getMsg().get(i).getDetail();
                }
            }
        }

        for(int i=0; i<detailArrayList.size(); i++){
            // 각 List의 값들을 data 객체에 set 해줍니다.
            SubjectListData data = new SubjectListData();
            data.setSubject(detailArrayList.get(i).toString());

            // 각 값이 들어간 data를 adapter에 추가합니다.
            adapter.addItem(data);
        }

        adapter.setCallbackListener(callbackListener);
        // adapter의 값이 변경되었다는 것을 알려줍니다.
        adapter.notifyDataSetChanged();

        bottomSheetDetail.show();
    }

    private void deleteTodo(String sIndex){
        // db index 값 받아서 넘기면서 바로 삭제 처리
        try {
            Map<String, String> params = new HashMap<String, String>();
            //params.put("UUID", MHDApplication.getInstance().getMHDSvcManager().getDeviceNewUuid());
            params.put("UUMAIL", MHDApplication.getInstance().getMHDSvcManager().getUserVo().getUuMail());
            params.put("IDX", sIndex);

            MHDNetworkInvoker.getInstance().sendVolleyRequest(mContext, R.string.url_restapi_delete_todo, params, responseListener);
        } catch (Exception e) {
            MHDLog.printException(e);
        }
    }
}
