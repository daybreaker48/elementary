package com.mhd.elemantary.activity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.Gson;
import com.mhd.elemantary.MainActivity;
import com.mhd.elemantary.R;
import com.mhd.elemantary.adapter.ReCyclerAdapter;
import com.mhd.elemantary.adapter.ReCyclerSubjectAdapter;
import com.mhd.elemantary.common.ClickCallbackListener;
import com.mhd.elemantary.common.MHDApplication;
import com.mhd.elemantary.common.vo.SubjectListData;
import com.mhd.elemantary.common.vo.SubjectVo;
import com.mhd.elemantary.constant.MHDConstants;
import com.mhd.elemantary.fragment.TodoFragment;
import com.mhd.elemantary.network.MHDNetworkInvoker;
import com.mhd.elemantary.util.MHDDialogUtil;
import com.mhd.elemantary.util.MHDLog;

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

public class RegistTodoActivity extends BaseActivity implements TextView.OnEditorActionListener  {

    TextView tv_selectday, vst_top_title, tv_todo_subject, tv_todo_subject_detail;
    TextView tv_rb_daily_progress_2_finishday, tv_startday;
    LinearLayout ll_daily_progress, ll_daily_textbook;
    private String[] day_array = new String[7];
    String sendDay = "";
    String Ptotal, Gtotal, Foneday, Poneday, Goneday = "";

    String currentRadio = "F";
    RadioGroup rg_daily_progress;
    RadioButton rb_daily_progress_1, rb_daily_progress_2, rb_daily_progress_3;
    LinearLayout ll_daily_radio_1, ll_daily_radio_2, ll_daily_radio_3, ll_rg_daily_progress;
    EditText et_daily_radio_1, et_daily_radio_2, et_daily_radio_3, et_daily_radio_4, et_daily_radio_5, et_textbook_publish, et_textbook_subject;
    AppCompatButton btn_todo_goal;
    private DatePickerDialog.OnDateSetListener callbackMethod;
    Calendar baseDate, targetDate = null;
    String displayStrings, innerStrings = "";
    String pGoal = "";
    String selectedSubject = "";
    String selectedDetail = "";

    public ReCyclerSubjectAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    ArrayList subjectArrayList, detailArrayList;
    BottomSheetDialog bottomSheetSubject;
    BottomSheetDialog bottomSheetDetail;

    public ClickCallbackListener callbackListener = new ClickCallbackListener() {
        @Override
        public void callBack(int pos) {
            if(bottomSheetSubject.isShowing()) {
                selectedSubject = subjectArrayList.get(pos).toString();
                tv_todo_subject.setText(selectedSubject);
                bottomSheetSubject.dismiss();
                showBottomSheetSubjectDetail(selectedSubject);
                return;
            }
            if(bottomSheetDetail.isShowing()) {
                selectedDetail = detailArrayList.get(pos).toString();
                tv_todo_subject_detail.setText(selectedDetail);
                bottomSheetDetail.dismiss();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initialize(R.layout.activity_todo_regist);
        mContext = RegistTodoActivity.this;


        bottomSheetDetail = new BottomSheetDialog(this);

        tv_todo_subject = (TextView) findViewById(R.id.tv_todo_subject);
        tv_todo_subject_detail = (TextView) findViewById(R.id.tv_todo_subject_detail);
        tv_todo_subject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { showBottomSheetSubject(); }
        });
        tv_todo_subject_detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { showBottomSheetSubjectDetail(selectedSubject); }
        });
        tv_startday = (TextView) findViewById(R.id.tv_startday);
        tv_rb_daily_progress_2_finishday = (TextView) findViewById(R.id.tv_rb_daily_progress_2_finishday);
        vst_top_title = (TextView) findViewById(R.id.vst_top_title);
        vst_top_title.setText(R.string.title_todo_regist);
        tv_selectday = (TextView) findViewById(R.id.tv_selectday);
        tv_selectday.setText(getString(R.string.content_dailyprogress));
        ll_daily_progress = (LinearLayout) findViewById(R.id.ll_daily_progress);
        ll_daily_textbook = (LinearLayout) findViewById(R.id.ll_daily_textbook);
        ll_rg_daily_progress = (LinearLayout) findViewById(R.id.ll_rg_daily_progress);

        rg_daily_progress = (RadioGroup) findViewById(R.id.rg_daily_progress);
        rb_daily_progress_1 = (RadioButton) findViewById(R.id.rb_daily_progress_1);
        rb_daily_progress_2 = (RadioButton) findViewById(R.id.rb_daily_progress_2);
        rb_daily_progress_3 = (RadioButton) findViewById(R.id.rb_daily_progress_3);
        ll_daily_radio_1 = (LinearLayout) findViewById(R.id.ll_daily_radio_1);
        ll_daily_radio_2 = (LinearLayout) findViewById(R.id.ll_daily_radio_2);
        ll_daily_radio_3 = (LinearLayout) findViewById(R.id.ll_daily_radio_3);
        et_daily_radio_1 = (EditText) findViewById(R.id.et_daily_radio_1);
        et_daily_radio_2 = (EditText) findViewById(R.id.et_daily_radio_2);
        et_daily_radio_3 = (EditText) findViewById(R.id.et_daily_radio_3);
        et_daily_radio_4 = (EditText) findViewById(R.id.et_daily_radio_4);
        et_daily_radio_5 = (EditText) findViewById(R.id.et_daily_radio_5);
        et_textbook_publish = (EditText) findViewById(R.id.et_textbook_publish);
        et_textbook_subject = (EditText) findViewById(R.id.et_textbook_subject);
        AppCompatButton btn_todo_cancel = (AppCompatButton) findViewById(R.id.btn_todo_cancel);
        et_daily_radio_1.setOnEditorActionListener(this);
        et_daily_radio_2.setOnEditorActionListener(this);
        et_daily_radio_3.setOnEditorActionListener(this);
        et_daily_radio_4.setOnEditorActionListener(this);
        et_daily_radio_5.setOnEditorActionListener(this);
        btn_todo_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { finish(); }
        });
        AppCompatButton btn_todo_save = (AppCompatButton) findViewById(R.id.btn_todo_save);
        btn_todo_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // currentRadio(F, P, G)에 따라 필수입력값 체크, 문제가 없다면 서버 전송.
                saveProcess();
            }
        });
        btn_todo_goal= (AppCompatButton) findViewById(R.id.btn_todo_goal);
        btn_todo_goal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);
                baseDate = new GregorianCalendar(year, month, day);
                DatePickerDialog dialog = new DatePickerDialog(mContext, callbackMethod, year, month, day);
                dialog.getDatePicker().setMinDate(c.getTimeInMillis());
                dialog.show();
            }
        });
        callbackMethod = new DatePickerDialog.OnDateSetListener()
        {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth)
            {
                // 선택한 날짜가 오늘보다 이전인지 체크. 이전이면 오류
                btn_todo_goal.setText(year + "년 " + monthOfYear + "월 " + dayOfMonth + "일");
                targetDate =  new GregorianCalendar(year, monthOfYear, dayOfMonth);

                et_daily_radio_4.setFocusableInTouchMode(true);
                et_daily_radio_4.requestFocus();
                InputMethodManager manager = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
                manager.showSoftInput(et_daily_radio_4, InputMethodManager.SHOW_IMPLICIT);
            }
        };

//        ll_daily_radio_1.post(new Runnable() {
//            @Override
//            public void run() {
//                et_daily_radio_1.setFocusableInTouchMode(true);
//                et_daily_radio_1.requestFocus();
//                InputMethodManager imm = (InputMethodManager)getSystemService(mContext.INPUT_METHOD_SERVICE);
//                imm.showSoftInput(et_daily_radio_1,0);
//            }
//        });
        rg_daily_progress.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.rb_daily_progress_1:
                        currentRadio = "F";
                        ll_daily_radio_1.setVisibility(View.VISIBLE);
                        ll_daily_radio_2.setVisibility(View.GONE);
                        ll_daily_radio_3.setVisibility(View.GONE);
                        et_daily_radio_1.post(new Runnable() {
                            @Override
                            public void run() {
                                et_daily_radio_1.setFocusableInTouchMode(true);
                                et_daily_radio_1.requestFocus();
                                InputMethodManager imm = (InputMethodManager)getSystemService(mContext.INPUT_METHOD_SERVICE);
                                imm.showSoftInput(et_daily_radio_1,0);
                            }
                        });
                        break;
                    case R.id.rb_daily_progress_2:
                        currentRadio = "P";
                        ll_daily_radio_1.setVisibility(View.GONE);
                        ll_daily_radio_2.setVisibility(View.VISIBLE);
                        ll_daily_radio_3.setVisibility(View.GONE);
                        et_daily_radio_2.post(new Runnable() {
                            @Override
                            public void run() {
                                et_daily_radio_2.setFocusableInTouchMode(true);
                                et_daily_radio_2.requestFocus();
                                InputMethodManager imm = (InputMethodManager)getSystemService(mContext.INPUT_METHOD_SERVICE);
                                imm.showSoftInput(et_daily_radio_2,0);
                            }
                        });
                        break;
                    case R.id.rb_daily_progress_3:
                        currentRadio = "G";
                        InputMethodManager manager = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
                        manager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                        ll_daily_radio_1.setVisibility(View.GONE);
                        ll_daily_radio_2.setVisibility(View.GONE);
                        ll_daily_radio_3.setVisibility(View.VISIBLE);
//                        et_daily_radio_3.post(new Runnable() {
//                            @Override
//                            public void run() {
//                                et_daily_radio_3.setFocusableInTouchMode(true);
//                                et_daily_radio_3.requestFocus();
//                                InputMethodManager imm = (InputMethodManager)getSystemService(mContext.INPUT_METHOD_SERVICE);
//                                imm.showSoftInput(et_daily_radio_3,0);
//                            }
//                        });
                        break;
                }
            }
        });

        et_daily_radio_3.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(b){

                }else{

                }
            }
        });

        checkSubject();
    }
    public void startDailyPregressActivity() {
        // 할일 요일 정보를 보내야 한다. 오늘/매주 어떤 요일
        MHDLog.d(TAG, "pre sendDay: " + sendDay);
        Intent intent = new Intent(mContext, OptionDailyProgressActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.putExtra("sendDay", sendDay);
        startActivityResult.launch(intent);
    }
    ActivityResultLauncher<Intent> startActivityResult = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if(result.getResultCode() == MHDConstants.StartActivityForResult.REQUEST_TODO_OPTION){

                    }
                }
            }
        );


    public void startDailyTextbookActivity() {
        // 교재 선택 창을 띄운다.
        Intent i = new Intent(mContext, OptionDailyTextbookActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(i);
    }


    public void selectDay(int day) {
        switch (day) {
            case 0:
                if(Arrays.asList(day_array).contains(getString(R.string.content_sun)))
                    day_array[0] = "";
                else
                    day_array[0] = getString(R.string.content_sun);
                break;
            case 1:
                if(Arrays.asList(day_array).contains(getString(R.string.content_mon)))
                    day_array[1] = "";
                else
                    day_array[1] = getString(R.string.content_mon);
                break;
            case 2:
                if(Arrays.asList(day_array).contains(getString(R.string.content_tues)))
                    day_array[2] = "";
                else
                    day_array[2] = getString(R.string.content_tues);
                break;
            case 3:
                if(Arrays.asList(day_array).contains(getString(R.string.content_wed)))
                    day_array[3] = "";
                else
                    day_array[3] = getString(R.string.content_wed);
                break;
            case 4:
                if(Arrays.asList(day_array).contains(getString(R.string.content_thur)))
                    day_array[4] = "";
                else
                    day_array[4] = getString(R.string.content_thur);
                break;
            case 5:
                if(Arrays.asList(day_array).contains(getString(R.string.content_fri)))
                    day_array[5] = "";
                else
                    day_array[5] = getString(R.string.content_fri);
                break;
            case 6:
                if(Arrays.asList(day_array).contains(getString(R.string.content_sat)))
                    day_array[6] = "";
                else
                    day_array[6] = getString(R.string.content_sat);
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
            sendDay = "";
            tv_startday.setText("");
            ll_rg_daily_progress.setVisibility(View.GONE);
        }else{
            tv_selectday.setText("매주 " + displayStrings);

            Calendar cal = Calendar.getInstance();
            int weekd = cal.get(Calendar.DAY_OF_WEEK); // 오늘 요일
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            if(weekd == 7){//
                String startday = innerStrings.substring(0,1);
                cal.add(Calendar.DATE, Integer.parseInt(startday));
                tv_startday.setText("학습 시작일 : " + df.format(cal.getTime()));
            }else{
                String[] strArray = innerStrings.split("");
                int curWeekCount = 0;
                for(String s : strArray) {
                    if(Integer.parseInt(s) > weekd) {
                        curWeekCount++;
                        cal.add(Calendar.DATE, Integer.parseInt(s) - weekd);
                        tv_startday.setText("학습 시작일 : " + df.format(cal.getTime()));
                        break;
                    }
                }
                // curWeekCount == 0 이면 오늘 요일보다 뒤에 학습일이 없다는 것. 다음 주로 넘어가야 한다.
                // curWeekCount > 0 이면 오늘 요일보다 뒤에 학습일이 있다는 것. 그 요일이 첫 학습일이다.
                if(curWeekCount == 0){
                    String startday = innerStrings.substring(0,1);
                    cal.add(Calendar.DATE, 7 - weekd + Integer.parseInt(startday));
                    tv_startday.setText("학습 시작일 : " + df.format(cal.getTime()));
                }
            }

            ll_rg_daily_progress.setVisibility(View.VISIBLE);
        }
    }

    /**
     * check subject
     */
    public void checkSubject(){
        try {
            // 획득한 uuid 로 서버조회 후 처리.

            // call service intro check
//                // String 방식
//                StringBuilder fullParams = new StringBuilder("{");
//                fullParams.append("\"UUID\":\""+userVo.getUuID()+"\"")
//                        .append(",\"UUPN\":\""+userVo.getUuPN()+"\"")
//                        .append(",\"UUOS\":\""+userVo.getUuOs()+"\"")
//                        .append(",\"UUDEVICE\":\""+userVo.getUuDevice()+"\"")
//                        .append(",\"UUTOKEN\":\""+userVo.getUuToken()+"\"")
//                        .append(",\"UUAPP\":\""+userVo.getUuAppVer()+"\"")
//                      v  .append("}");
            // Map 방식 0
            Map<String, String> params = new HashMap<String, String>();
            //params.put("UUID", MHDApplication.getInstance().getMHDSvcManager().getDeviceNewUuid());
            params.put("UUMAIL", MHDApplication.getInstance().getMHDSvcManager().getUserVo().getUuMail());

            MHDNetworkInvoker.getInstance().sendVolleyRequest(RegistTodoActivity.this, R.string.url_restapi_check_subject, params, responseListener);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            MHDLog.printException(e);
        }
    }

    @Override
    protected boolean networkResponseProcess(String result) {
        boolean resultFlag = super.networkResponseProcess(result);
        MHDLog.d(TAG, "networkResponseProcess resultFlag >>> " + resultFlag);
        SubjectVo subjectVo = null;

        if(!resultFlag) return resultFlag;

        // resultFlag 이 true 라면 현재 여기에 필요한 data 들이 전역에 들어가 있는 상태.

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

                AppCompatButton btn_sun = (AppCompatButton) findViewById(R.id.btn_sun);
                AppCompatButton btn_mon = (AppCompatButton) findViewById(R.id.btn_mon);
                AppCompatButton btn_tues = (AppCompatButton) findViewById(R.id.btn_tues);
                AppCompatButton btn_wed = (AppCompatButton) findViewById(R.id.btn_wed);
                AppCompatButton btn_thur = (AppCompatButton) findViewById(R.id.btn_thur);
                AppCompatButton btn_fri = (AppCompatButton) findViewById(R.id.btn_fri);
                AppCompatButton btn_sat = (AppCompatButton) findViewById(R.id.btn_sat);

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
//        ll_daily_progress.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) { startDailyPregressActivity(); }
//        });
//        ll_daily_textbook.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) { startDailyTextbookActivity(); }
//        });

//        try {
//            if("S".equals(resultCode)){  // Success
//                // 기존 사용자라면.
//                // 서버 저장. 단말 기본 정보 : VO 방식
//                // 이것은 로그인이 되었을때만 넣는게 맞다.
//                Gson gson = new Gson();
//                UserVo userVo;
//                userVo = gson.fromJson(jsonDataObject.toString(), UserVo.class);
//                MHDApplication.getInstance().getMHDSvcManager().setUserVo(null);
//                MHDApplication.getInstance().getMHDSvcManager().setUserVo(userVo);
//
//                if(MHDApplication.getInstance().getMHDSvcManager().getUserVo() != null){
//                    // MainActivity 로 이동
//                    goMain();
//                }
//            }else{  // maybe -1
//                // 신규 사용자라면.
//
//                // 튜토리얼 화면을 띄운다.
//                // 슬라이드 형식 말고 자연스럽게 나타나게.
//                Intent i = new Intent(mContext, TutorialActivity.class);
//                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
//                startActivity(i);
//                overridePendingTransition(0, 0);
//                finish();
//            }
//        } catch (Exception e) {
//            MHDLog.printException(e);
//        }
                return true;
            }else if(nvApi.equals(getString(R.string.restapi_regist_todo))){
                if (nvCnt == 0) {
                    // 정보가 없으면 비정상
                    // 우선 toast를 띄울 것.
                    Toast.makeText(mContext, nvMsg, Toast.LENGTH_SHORT).show();
                } else {
                    // 할일 정상등록 여부를 알림
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
        if(i == EditorInfo.IME_ACTION_DONE || i == EditorInfo.IME_ACTION_NEXT) {
            switch (textView.getId()) {
                case R.id.et_daily_radio_1:
                    // 하루 분량. 총페이지를 모르기 때문에 여기서는 이동할 곳도, 계산할 것도 없다.
                    // 정해진 요일마다 기한없이 해당 내용을 표시해주면 되는 것.
                    // currentRadio ="F"라면 edittext 1 값을 저장.
                    Foneday = (et_daily_radio_1.getText() == null) ? "" : et_daily_radio_1.getText().toString();
                    if("".equals(Foneday)){
                        Toast.makeText(mContext, "하루분량을 입력해주세요.", Toast.LENGTH_SHORT).show();
                        et_daily_radio_1.setFocusableInTouchMode(true);
                        et_daily_radio_1.requestFocus();
                        return true;
                    }
                    break;
                case R.id.et_daily_radio_2:
                    // 총페이지 다음 edittext로 이동
                    Ptotal = (et_daily_radio_2.getText() == null) ? "" : et_daily_radio_2.getText().toString();
                    if("".equals(Ptotal) || Ptotal == null){
                        Toast.makeText(mContext, "총페이지를 입력해주세요.", Toast.LENGTH_SHORT).show();
                        et_daily_radio_2.setFocusableInTouchMode(true);
                        et_daily_radio_2.requestFocus();
                        return true;
                    }
                    et_daily_radio_3.setFocusableInTouchMode(true);
                    et_daily_radio_3.requestFocus();
                    break;
                case R.id.et_daily_radio_3:
                    Ptotal = (et_daily_radio_2.getText() == null) ? "" : et_daily_radio_2.getText().toString();
                    if("".equals(Ptotal) || Ptotal == null){
                        Toast.makeText(mContext, "총페이지를 입력해주세요.", Toast.LENGTH_SHORT).show();
                        et_daily_radio_2.setFocusableInTouchMode(true);
                        et_daily_radio_2.requestFocus();
                        return true;
                    }
                    // 하루분량, 값이 비어있는지, 총페이지보다 크지 않은지 체크. 종료예정일 계산
                    // currentRadio ="P"라면 edittext 2,3 값을 저장.
                    Poneday = (et_daily_radio_3.getText() == null) ? "" : et_daily_radio_3.getText().toString();
                    if("".equals(Poneday) || Poneday == null){
                        Toast.makeText(mContext, "하루분량을 입력해주세요.", Toast.LENGTH_SHORT).show();
                        et_daily_radio_3.setFocusableInTouchMode(true);
                        et_daily_radio_3.requestFocus();
                        break;
                    }
                    if(!"".equals(Ptotal) && Ptotal != null && Integer.parseInt(Ptotal) < Integer.parseInt(Poneday)) {
                        Toast.makeText(mContext, "총페이지보다 하루분량이 작아야 합니다.", Toast.LENGTH_SHORT).show();
                        et_daily_radio_3.setFocusableInTouchMode(true);
                        et_daily_radio_3.requestFocus();
                        break;
                    }
                    // 여기까지 문제없으면 종료예정일을 계산해서 아래 표시할 것.
                    // 총 몇일의 학습일이 필요한지 계산. 나머지는 분배보다는 마지막 하루로 계산.
                    // 시작일을 설정하는 기능도 필요할거 같긴하지만 이건 나중에 구현. 등록 당일은 제외
                    // 종료일을 DB에 넣고 종료일에 오기 전에는 정해진 요일에 표시하는 방식.
                    // 중간에 그냥 건너뛰는 날, 건너뛰어야 하는 날을 어떻게 처리하지.
                    long dayCount = Integer.parseInt(Ptotal) / Integer.parseInt(Poneday);
                    long dayRest = Integer.parseInt(Ptotal) % Integer.parseInt(Poneday);
                    dayRest = dayRest > 0 ? 1 : 0;
                    int dayTotal = (int)dayCount + (int)dayRest; // 소요 day 수, 나머지(dayRest)가 0이면 그냥 0인거니...
                    Calendar cal = Calendar.getInstance();
                    int weekd = cal.get(Calendar.DAY_OF_WEEK); // 오늘 요일
                    weekd = weekd == 7 ? 1 : weekd+1; // 시작요일(오늘 제외한 다음 날. 이번주 계산에만 사용).
                    int lengthWeek = innerStrings.length(); // 한주 동안의 학습일 수
                    int spendWeek, extraDays = 0;
                    if(weekd == 1){
                        // 이 값이 1이면 한주의 끝. 토요일에 등록한다는 것.
                        // weekd = 1이면 이번주는 한주의 처음부터 시작하면 되는 것.
                        spendWeek = dayTotal / lengthWeek;
                        extraDays = dayTotal % lengthWeek;

                        if(extraDays == 0){
                            // extraDays = 0 이면 주 단위로 딱 떨어지는 것.
                            String lastDays = innerStrings.substring(lengthWeek-1); // 한 주의 마지막 학습요일. spendWeek주 후의 이 요일이 종료일이다.
                            // 다음주(등록일이 토요일이니) 마지막 학습요일의 날짜를 구한다. 등록하는 날짜에서 요일코드만큼 더한 날짜와 같다.
                            cal.setTime(new Date());
                            cal.add(Calendar.DATE, 1); // 다음주의 시작이(일요일) 된다.
                            DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
//                            System.out.println("current: " + df.format(cal.getTime()));
//                            cal.add(Calendar.MONTH, 2);
                            cal.add(Calendar.DATE, Integer.parseInt(lastDays)-1);
                            // 여기서 spendWeek주 후 날짜를 구한다.  spendWeek*7
                            cal.add(Calendar.DATE, (spendWeek-1)*7);
                            // 이 날짜가 종료일이다.
                            tv_rb_daily_progress_2_finishday.setVisibility(View.VISIBLE);
                            pGoal = df.format(cal.getTime());
                            tv_rb_daily_progress_2_finishday.setText("학습 종료일 : " + pGoal);
                        }else{
                            // extraDays > 0 이면 그 다음 주 추가학습일이 필요한 것.
                            String lastDays = innerStrings.substring(lengthWeek-1); // 한 주의 마지막 학습요일. spendWeek주 후, 그 다음주의 extraDays 값에 해당하는 인덱스의 요일코드만큼 더한 날짜가 종료일이다.
                            // 다음주(등록일이 토요일이니) 마지막 학습요일의 날짜를 구한다. 등록하는 날짜에서 요일코드만큼 더한 날짜와 같다.
                            cal.setTime(new Date());
                            cal.add(Calendar.DATE, 1); // 다음주의 시작이(일요일) 된다.
                            DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
//                            System.out.println("current: " + df.format(cal.getTime()));
//                            cal.add(Calendar.MONTH, 2);
                            cal.add(Calendar.DATE, Integer.parseInt(lastDays)-1);
                            // 여기서 spendWeek주 후 날짜를 구한다.  spendWeek*7
                            cal.add(Calendar.DATE, (spendWeek-1)*7);
                            // 여기서 extraDays 값의 index-1에 해당하는 요일 코드를 갸져온다.
                            String lastDaysCode = innerStrings.substring(extraDays-1,extraDays);
                            // 다음 주에 위에서 구한 요일코드에 해당하는 날짜를 가져오면 된다.
                            // 그러기 위해서 우선 이번 주의 해당 요일코드에 해당하는 날짜를 구하고
                            cal.set(Calendar.DAY_OF_WEEK, Integer.parseInt(lastDaysCode));
                            // 거기에 1주일을 더 하고
                            cal.add(Calendar.DATE, 7);
                            // 이 날짜가 종료일이다.
                            tv_rb_daily_progress_2_finishday.setVisibility(View.VISIBLE);
                            pGoal = df.format(cal.getTime());
                            tv_rb_daily_progress_2_finishday.setText("학습 종료일 : " + pGoal);
                        }
                    }else{
                        // weekd > 1이면 weekd = 1 일때와 비슷한 방식이지만. 그 전에 먼저 해야할 것이
                        // 이번 주 남은 학습일(currentWeekRestDays)을 구해서 그 학습일 수 만큼을 뺀 상태에서 weekd = 1 일때의 프로세스를 타는 것.
                        // 이 얘기는 곧. dayTotal 의 값이 dayTotal - currentWeekRestDays 가 된다는 것.
                        String[] strArray = innerStrings.split("");
                        int curWeekCount = 0;
                        for(String s : strArray) {
                            if(Integer.parseInt(s) >= weekd) curWeekCount++;
                        }
                        // curWeekCount > 0 이라면 이번주에 학습일 요일이 있다는 것.
                        // 그런데 이번주 그 남은 학습일이 전체 소요일보다 작거나 같을수도 있겠구나.
//                        if(curWeekCount >= dayTotal)
                        dayTotal = dayTotal - curWeekCount;
                        //========================================================================================
                        // 여기부터는 weekd = 1인 것과 동일한 프로세스
                        spendWeek = dayTotal / lengthWeek;
                        extraDays = dayTotal % lengthWeek;

                        if(extraDays == 0){
                            // extraDays = 0 이면 주 단위로 딱 떨어지는 것.
                            String lastDays = innerStrings.substring(lengthWeek-1); // 한 주의 마지막 학습요일. spendWeek주 후의 이 요일이 종료일이다.
                            // 이번주 마지막 학습요일의 날짜를 구한다. 등록하는 날짜에서 요일코드만큼 더한 날짜와 같다.
                            cal.setTime(new Date());
                            cal.add(Calendar.DATE, 9-weekd); // 다음주의 시작이(일요일) 된다.
                            DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
//                            System.out.println("current: " + df.format(cal.getTime()));
//                            cal.add(Calendar.MONTH, 2);
                            cal.add(Calendar.DATE, Integer.parseInt(lastDays)-1);
                            // 여기서 spendWeek주 후 날짜를 구한다.  spendWeek*7
                            cal.add(Calendar.DATE, (spendWeek-1)*7);
                            // 이 날짜가 종료일이다.
                            tv_rb_daily_progress_2_finishday.setVisibility(View.VISIBLE);
                            pGoal = df.format(cal.getTime());
                            tv_rb_daily_progress_2_finishday.setText("학습 종료일 : " + pGoal);
                        }else{
                            // extraDays > 0 이면 그 다음 주 추가학습일이 필요한 것.
                            String lastDays = innerStrings.substring(lengthWeek-1); // 한 주의 마지막 학습요일. spendWeek주 후, 그 다음주의 extraDays 값에 해당하는 인덱스의 요일코드만큼 더한 날짜가 종료일이다.
                            // 이번주 마지막 학습요일의 날짜를 구한다. 등록하는 날짜에서 요일코드만큼 더한 날짜와 같다.
                            cal.setTime(new Date());
                            cal.add(Calendar.DATE, 9-weekd); // 다음주의 시작이(일요일) 된다.
                            DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
//                            System.out.println("current: " + df.format(cal.getTime()));
//                            cal.add(Calendar.MONTH, 2);
                            cal.add(Calendar.DATE, Integer.parseInt(lastDays)-1);
                            // 여기서 spendWeek주 후 날짜를 구한다.  spendWeek*7
                            cal.add(Calendar.DATE, (spendWeek-1)*7);
                            // 여기서 extraDays 값의 index-1에 해당하는 요일 코드를 갸져온다.
                            String lastDaysCode = innerStrings.substring(extraDays-1,extraDays);
                            // 다음 주에 위에서 구한 요일코드에 해당하는 날짜를 가져오면 된다.
                            // 그러기 위해서 우선 이번 주의 해당 요일코드에 해당하는 날짜를 구하고
                            cal.set(Calendar.DAY_OF_WEEK, Integer.parseInt(lastDaysCode));
                            // 거기에 1주일을 더 하고
                            cal.add(Calendar.DATE, 7);
                            // 이 날짜가 종료일이다.
                            tv_rb_daily_progress_2_finishday.setVisibility(View.VISIBLE);
                            pGoal = df.format(cal.getTime());
                            tv_rb_daily_progress_2_finishday.setText("학습 종료일 : " + pGoal);
                        }
                    }

                    break;
                case R.id.et_daily_radio_4:
                    // 이 항목은 업데이트 버전에서 다룬다.(update version)
                    // 하루 할당량 계산. 목표일 설정 체크
                    if(targetDate == null){
                        Toast.makeText(mContext, "목표일을 설정해주세요.", Toast.LENGTH_SHORT).show();
                        break;
                    }
                    Gtotal = (et_daily_radio_4.getText() == null) ? "" : et_daily_radio_4.getText().toString();
                    if("".equals(Gtotal) || Gtotal == null){
                        Toast.makeText(mContext, "총페이지를 입력해주세요.", Toast.LENGTH_SHORT).show();
                        et_daily_radio_4.setFocusableInTouchMode(true);
                        et_daily_radio_4.requestFocus();
                        break;
                    }
                    // 오늘부터 목표일 까지의 날 수 계산(나중에는 시작일, 주말제외 옵셥을 넣어야 함)
                    long diffSec = (targetDate.getTimeInMillis() - baseDate.getTimeInMillis()) / 1000;
                    long diffDay = diffSec / (24*60*60);
                    int diffDayInt = (int)diffDay + 1;
                    int totoal_1_int = Integer.parseInt(Gtotal);
                    // 날수와 총페이지수로 하루분량을 계산해서 edittext에 표시.
                    MHDLog.d(TAG, " diffDay: " + diffDayInt); // 날수
                    MHDLog.d(TAG, " totoal_1_int: " + totoal_1_int); // 총페이지
                    if(diffDayInt > totoal_1_int){
                        Toast.makeText(mContext, "목표일까지 날짜 수가 총페이지 수보다 큽니다", Toast.LENGTH_SHORT).show();
                        et_daily_radio_4.setFocusableInTouchMode(true);
                        et_daily_radio_4.requestFocus();
                        break;
                    }
                    // 몫과 나머지를 구한다. 몫이 하루 고정 페이지수이고, 나머지가 마지막 날 고정페이지수에 더해야 하는 것이다.
                    int plusPage = totoal_1_int % diffDayInt;
                    int dayPage = totoal_1_int / diffDayInt;
                    MHDLog.d(TAG, " plusPage: " + plusPage); // 남는페이지
                    MHDLog.d(TAG, " dayPage: " + dayPage); // 하루고정페이지
                    break;
            }
        }
        return false;
    }

    private void saveProcess() {
//        Intent tent = getPackageManager().getLaunchIntentForPackage("com.multicampus.player");
//        if (tent == null) {
//            Toast.makeText(mContext, "not install", Toast.LENGTH_SHORT).show();
//        } else {
//            try {
//                String url = "kplayer://com.multicampus.player?p_grcode=CUS0001&p_subj=A39183&p_year=2020&p_subjseq=10020&p_resno=C595271540558";
//                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
//                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                startActivity(intent);
//            } catch (ActivityNotFoundException e) {
//                Toast.makeText(mContext, "not app", Toast.LENGTH_SHORT).show();
//            }
//        }
        if(checkP()) {
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
            } else if ("F".equals(currentRadio)) {
                // 하루분량 값만 있으면 된다.
                Foneday = (et_daily_radio_1.getText() == null) ? "" : et_daily_radio_1.getText().toString();
                if ("".equals(Foneday)) {
                    Toast.makeText(mContext, "하루분량을 입력해주세요.", Toast.LENGTH_SHORT).show();
                    et_daily_radio_1.setFocusableInTouchMode(true);
                    et_daily_radio_1.requestFocus();
                } else {
                    sendTodoData(Foneday, "0", "2100-12-31", tmpSubject, tmpDetail, tmpPublisher, tmpTitle, "F");
                }
            } else if ("P".equals(currentRadio)) {
                // 총페이지, 하루분량, 종료일 을 넘겨야 한다.
                Poneday = (et_daily_radio_3.getText() == null) ? "" : et_daily_radio_3.getText().toString();
                if ("".equals(Poneday) || Poneday == null) {
                    Toast.makeText(mContext, "하루분량을 입력해주세요.", Toast.LENGTH_SHORT).show();
                    et_daily_radio_3.setFocusableInTouchMode(true);
                    et_daily_radio_3.requestFocus();
                } else if (Integer.parseInt(Ptotal) < Integer.parseInt(Poneday)) {
                    Toast.makeText(mContext, "총페이지보다 하루분량이 작아야 합니다.", Toast.LENGTH_SHORT).show();
                    et_daily_radio_3.setFocusableInTouchMode(true);
                    et_daily_radio_3.requestFocus();
                } else {
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
                            tv_rb_daily_progress_2_finishday.setVisibility(View.VISIBLE);
                            pGoal = df.format(cal.getTime());
                            tv_rb_daily_progress_2_finishday.setText("학습 종료일 : " + pGoal);
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
                            tv_rb_daily_progress_2_finishday.setVisibility(View.VISIBLE);
                            pGoal = df.format(cal.getTime());
                            tv_rb_daily_progress_2_finishday.setText("학습 종료일 : " + pGoal);
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
                            tv_rb_daily_progress_2_finishday.setVisibility(View.VISIBLE);
                            pGoal = df.format(cal.getTime());
                            tv_rb_daily_progress_2_finishday.setText("학습 종료일 : " + pGoal);
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
                            tv_rb_daily_progress_2_finishday.setVisibility(View.VISIBLE);
                            pGoal = df.format(cal.getTime());
                            tv_rb_daily_progress_2_finishday.setText("학습 종료일 : " + pGoal);
                        }
                    }

                    sendTodoData(Poneday, Ptotal, pGoal, tmpSubject, tmpDetail, tmpPublisher, tmpTitle, "P");
                }
            } else if ("G".equals(currentRadio)) {
                // (update version)

            }
        }
    }

    private boolean checkP(){
        String tmpSubject = selectedSubject;
        String tmpDetail = selectedDetail;
        String tmpPublisher = et_textbook_publish.getText().toString();
        String tmpTitle = et_textbook_subject.getText().toString();

        if (tmpSubject == null || "".equals(tmpSubject)) {
            // 과목 선택 안했다.
            Toast.makeText(mContext, getString(R.string.content_todo_subject), Toast.LENGTH_SHORT).show();
            return false;
        } else if (tmpDetail == null || "".equals(tmpDetail)) {
            // 세부항목 선택 안했다.
            Toast.makeText(mContext, getString(R.string.content_todo_detail), Toast.LENGTH_SHORT).show();
            return false;
        } else if (tmpPublisher == null || "".equals(tmpPublisher)) {
            // 출판사가 없다.
            Toast.makeText(mContext, getString(R.string.content_todo_publisher), Toast.LENGTH_SHORT).show();
            return false;
        } else if (tmpTitle == null || "".equals(tmpTitle)) {
            // 교재명이 없다.
            Toast.makeText(mContext, getString(R.string.content_todo_title), Toast.LENGTH_SHORT).show();
            return false;
        } else if ("".equals(innerStrings)) {
            // 학습 날짜 정보가 없다.
            Toast.makeText(mContext, getString(R.string.content_dailyprogress), Toast.LENGTH_SHORT).show();
            return false;
        }

        if(!"F".equals(currentRadio)) {
            // 총페이지 다음 edittext로 이동
            Ptotal = (et_daily_radio_2.getText() == null) ? "" : et_daily_radio_2.getText().toString();
            if ("".equals(Ptotal) || Ptotal == null) {
                // Fix 옵션이 아닌데 총페이지가 비어있다면.
                Toast.makeText(mContext, "총페이지를 입력해주세요.", Toast.LENGTH_SHORT).show();
                et_daily_radio_2.setFocusableInTouchMode(true);
                et_daily_radio_2.requestFocus();
                return false;
            }
            // 하루분량, 값이 비어있는지, 총페이지보다 크지 않은지 체크. 종료예정일 계산
            // currentRadio ="P"라면 edittext 2,3 값을 저장.
            Poneday = (et_daily_radio_3.getText() == null) ? "" : et_daily_radio_3.getText().toString();
            if ("".equals(Poneday) || Poneday == null) {
                Toast.makeText(mContext, "하루분량을 입력해주세요.", Toast.LENGTH_SHORT).show();
                et_daily_radio_3.setFocusableInTouchMode(true);
                et_daily_radio_3.requestFocus();
                return false;
            }
            if (!"".equals(Ptotal) && Ptotal != null && Integer.parseInt(Ptotal) < Integer.parseInt(Poneday)) {
                Toast.makeText(mContext, "총페이지보다 하루분량이 작아야 합니다.", Toast.LENGTH_SHORT).show();
                et_daily_radio_3.setFocusableInTouchMode(true);
                et_daily_radio_3.requestFocus();
                return false;
            }
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
                    tv_rb_daily_progress_2_finishday.setVisibility(View.VISIBLE);
                    pGoal = df.format(cal.getTime());
                    tv_rb_daily_progress_2_finishday.setText("학습 종료일 : " + pGoal);
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
                    tv_rb_daily_progress_2_finishday.setVisibility(View.VISIBLE);
                    pGoal = df.format(cal.getTime());
                    tv_rb_daily_progress_2_finishday.setText("학습 종료일 : " + pGoal);
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
                    tv_rb_daily_progress_2_finishday.setVisibility(View.VISIBLE);
                    pGoal = df.format(cal.getTime());
                    tv_rb_daily_progress_2_finishday.setText("학습 종료일 : " + pGoal);
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
                    tv_rb_daily_progress_2_finishday.setVisibility(View.VISIBLE);
                    pGoal = df.format(cal.getTime());
                    tv_rb_daily_progress_2_finishday.setText("학습 종료일 : " + pGoal);
                }
            }
        }
        return true;
    }

    private void sendTodoData(String oneday, String total, String goal, String subject, String detail, String publisher, String title, String option){
        try {
            // Map 방식 0
            Map<String, String> params = new HashMap<String, String>();
            params.put("UUMAIL", MHDApplication.getInstance().getMHDSvcManager().getUserVo().getUuMail());
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
            MHDNetworkInvoker.getInstance().sendVolleyRequest(mContext, R.string.url_restapi_regist_todo, params, responseListener);
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
}
