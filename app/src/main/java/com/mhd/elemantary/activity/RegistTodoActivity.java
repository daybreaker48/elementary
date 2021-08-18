package com.mhd.elemantary.activity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.mhd.elemantary.R;
import com.mhd.elemantary.common.MHDApplication;
import com.mhd.elemantary.common.vo.SubjectVo;
import com.mhd.elemantary.common.vo.UserVo;
import com.mhd.elemantary.constant.MHDConstants;
import com.mhd.elemantary.network.MHDNetworkInvoker;
import com.mhd.elemantary.util.MHDDialogUtil;
import com.mhd.elemantary.util.MHDLog;
import com.mhd.elemantary.util.Util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.widget.AppCompatButton;

public class RegistTodoActivity extends BaseActivity implements TextView.OnEditorActionListener  {

    TextView tv_selectday, vst_top_title;
    LinearLayout ll_daily_progress, ll_daily_textbook;
    private String[] day_array = new String[7];
    String sendDay = "";

    int currentRadio = 1;
    RadioGroup rg_daily_progress;
    RadioButton rb_daily_progress_1, rb_daily_progress_2, rb_daily_progress_3;
    LinearLayout ll_daily_radio_1, ll_daily_radio_2, ll_daily_radio_3;
    LinearLayout ll_rb_daily_progress_1, ll_rb_daily_progress_2, ll_rb_daily_progress_3;
    EditText et_daily_radio_1, et_daily_radio_2, et_daily_radio_3, et_daily_radio_4, et_daily_radio_5;
    AppCompatButton btn_todo_goal;
    private DatePickerDialog.OnDateSetListener callbackMethod;
    Calendar baseDate, targetDate = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initialize(R.layout.activity_todo_regist);
        mContext = RegistTodoActivity.this;
        sendDay = getString(R.string.content_dailyprogress);
        vst_top_title = (TextView) findViewById(R.id.vst_top_title);
        vst_top_title.setText(R.string.title_todo_regist);
        tv_selectday = (TextView) findViewById(R.id.tv_selectday);
        tv_selectday.setText(getString(R.string.content_dailyprogress));
        ll_daily_progress = (LinearLayout) findViewById(R.id.ll_daily_progress);
        ll_daily_textbook = (LinearLayout) findViewById(R.id.ll_daily_textbook);

        rg_daily_progress = (RadioGroup) findViewById(R.id.rg_daily_progress);
        rb_daily_progress_1 = (RadioButton) findViewById(R.id.rb_daily_progress_1);
        rb_daily_progress_2 = (RadioButton) findViewById(R.id.rb_daily_progress_2);
        rb_daily_progress_3 = (RadioButton) findViewById(R.id.rb_daily_progress_3);
        ll_daily_radio_1 = (LinearLayout) findViewById(R.id.ll_daily_radio_1);
        ll_daily_radio_2 = (LinearLayout) findViewById(R.id.ll_daily_radio_2);
        ll_daily_radio_3 = (LinearLayout) findViewById(R.id.ll_daily_radio_3);
        ll_rb_daily_progress_1 = (LinearLayout) findViewById(R.id.ll_rb_daily_progress_1);
        ll_rb_daily_progress_2 = (LinearLayout) findViewById(R.id.ll_rb_daily_progress_2);
        ll_rb_daily_progress_3 = (LinearLayout) findViewById(R.id.ll_rb_daily_progress_3);
        et_daily_radio_1 = (EditText) findViewById(R.id.et_daily_radio_1);
        et_daily_radio_2 = (EditText) findViewById(R.id.et_daily_radio_2);
        et_daily_radio_3 = (EditText) findViewById(R.id.et_daily_radio_3);
        et_daily_radio_4 = (EditText) findViewById(R.id.et_daily_radio_4);
        et_daily_radio_5 = (EditText) findViewById(R.id.et_daily_radio_5);
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
                // 입력 값 아래 화면으로 전달하고 요일에 따른 페이지 등을 계산해서 입력.
                // 그 후에 저장하면 모두 동시에 서버로 전송.
                finish();
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
                        currentRadio = 1;
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
                        currentRadio = 2;
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
                        currentRadio = 3;
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

        String displayStrings = "";
        for (String days : day_array) {
            if(days != null && !days.isEmpty()) {
                displayStrings = (displayStrings == null || displayStrings.isEmpty()) ? days : displayStrings + ", " + days;
            }
        }
        if(displayStrings.isEmpty()) {
            tv_selectday.setText(getString(R.string.content_dailyprogress));
            sendDay = getString(R.string.content_dailyprogress);
            rb_daily_progress_1.setChecked(true);
            ll_rb_daily_progress_1.setVisibility(View.GONE);
            ll_rb_daily_progress_2.setVisibility(View.GONE);
            ll_rb_daily_progress_3.setVisibility(View.GONE);
        }else {
            tv_selectday.setText("매주 " + displayStrings + " 진행합니다.");
            sendDay = displayStrings;
            ll_rb_daily_progress_1.setVisibility(View.VISIBLE);
            ll_rb_daily_progress_2.setVisibility(View.VISIBLE);
            ll_rb_daily_progress_3.setVisibility(View.VISIBLE);
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
            if(nvCnt == 0){
                // 정보가 없으면 비정상
                // 우선 toast를 띄울 것.
                Toast.makeText(mContext, nvMsg, Toast.LENGTH_SHORT).show();
            }else{
                // 과목정보를 받아옴.
                // 현재는 DB 에 있는것만. 나중에는 사용자가 입력한 것도 가져오도록.
                Gson gson = new Gson();
                subjectVo = gson.fromJson(nvJsonDataString, SubjectVo.class);
                MHDApplication.getInstance().getMHDSvcManager().setSubjectVo(null);
                MHDApplication.getInstance().getMHDSvcManager().setSubjectVo(subjectVo);
            }
        }

        String prevSubject = "";
        ArrayList subjectArrayList = new ArrayList<>();
        ArrayList detailArrayList = new ArrayList<>();
        // 과목명만 group by로 추출할 때 사용
//        for(int i=0; i<nvCnt; i++){
//            if(i>0 && prevSubject.equals(subjectVo.getMsg().get(i).getSubject()))
//                continue;
//            else {
//                subjectArrayList.add(subjectVo.getMsg().get(i).getSubject());
//                prevSubject = subjectVo.getMsg().get(i).getSubject();
//            }
//        }
        // 과목 - 상세 형식으로 추출할 때 사용
        for(int i=0; i<nvCnt; i++){
            subjectArrayList.add(subjectVo.getMsg().get(i).getSubject() + " - " + subjectVo.getMsg().get(i).getDetail());
        }

        Spinner spi_todo_subject = (Spinner) findViewById(R.id.spi_todo_subject);
        ArrayAdapter<CharSequence> adapter = new ArrayAdapter<CharSequence>(this, R.layout.default_spinner_item, subjectArrayList);

//        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.todo_subject_array, R.layout.default_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spi_todo_subject.setAdapter(adapter);
        // 일단 세부항목을 과목과 통합시켰다. 그래서 hidden.
//        final Spinner spi_todo_subject_detail = (Spinner) findViewById(R.id.spi_todo_subject_detail);
//        ArrayAdapter<CharSequence> adapter_detail = ArrayAdapter.createFromResource(this, R.array.todo_subject_item_0_array, R.layout.default_spinner_item);
//        adapter_detail.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        spi_todo_subject_detail.setAdapter(adapter_detail);

        // 과목선택에 따라서 detail 항목을 변경해줘야 한다.
        // 일단 세부항목을 과목과 통합시켰다. 그래서 hidden.
//        spi_todo_subject.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                ArrayAdapter<CharSequence> adapter_detail;
//                switch (position) {
//                    case 0:
//                        adapter_detail = ArrayAdapter.createFromResource(mContext, R.array.todo_subject_item_0_array, R.layout.default_spinner_item);
//                        MHDLog.d(TAG, "spi_todo_subject >>> " + position);
//                        break;
//                    case 1:
//                        adapter_detail = ArrayAdapter.createFromResource(mContext, R.array.todo_subject_item_1_array, R.layout.default_spinner_item);
//                        MHDLog.d(TAG, "spi_todo_subject >>> " + position);
//                        break;
//                    case 2:
//                        adapter_detail = ArrayAdapter.createFromResource(mContext, R.array.todo_subject_item_2_array, R.layout.default_spinner_item);
//                        MHDLog.d(TAG, "spi_todo_subject >>> " + position);
//                        break;
//                    case 3:
//                        adapter_detail = ArrayAdapter.createFromResource(mContext, R.array.todo_subject_item_3_array, R.layout.default_spinner_item);
//                        MHDLog.d(TAG, "spi_todo_subject >>> " + position);
//                        break;
//                    default:
//                        adapter_detail = ArrayAdapter.createFromResource(mContext, R.array.todo_subject_item_0_array, R.layout.default_spinner_item);
//                        MHDLog.d(TAG, "spi_todo_subject >>> " + position);
//                }
//                adapter_detail.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//                spi_todo_subject_detail.setAdapter(adapter_detail);
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
////                textView.setText("선택 : ");
//            }
//        });

        AppCompatButton btn_sun = (AppCompatButton) findViewById(R.id.btn_sun);
        AppCompatButton btn_mon = (AppCompatButton) findViewById(R.id.btn_mon);
        AppCompatButton btn_tues = (AppCompatButton) findViewById(R.id.btn_tues);
        AppCompatButton btn_wed = (AppCompatButton) findViewById(R.id.btn_wed);
        AppCompatButton btn_thur = (AppCompatButton) findViewById(R.id.btn_thur);
        AppCompatButton btn_fri = (AppCompatButton) findViewById(R.id.btn_fri);
        AppCompatButton btn_sat = (AppCompatButton) findViewById(R.id.btn_sat);
        AppCompatButton btn_todo_cancel = (AppCompatButton) findViewById(R.id.btn_todo_cancel);
        AppCompatButton btn_todo_save = (AppCompatButton) findViewById(R.id.btn_todo_save);

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

        btn_todo_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { finish(); }
        });
        btn_todo_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 입력 값 서버로 전송.
                finish();
            }
        });

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
    }

    @Override
    public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
        if(i == EditorInfo.IME_ACTION_DONE) {
            switch (textView.getId()) {
                case R.id.et_daily_radio_1:
                    // 하루 분량. 여기서는 이동할 곳도, 계산할 것도 없다.
                    break;
                case R.id.et_daily_radio_2:
                    // 총페이지 다음 edittext로 이동
                    et_daily_radio_3.setFocusableInTouchMode(true);
                    et_daily_radio_3.requestFocus();
                    break;
                case R.id.et_daily_radio_3:
                    // 하루분량, 값이 비어있는지, 총페이지보다 크지 않은지 체크. 종료예정일 계산
                    String total = (et_daily_radio_2.getText() == null) ? "" : et_daily_radio_2.getText().toString();
                    if("".equals(total)){
                        Toast.makeText(mContext, "총페이지를 입력해주세요.", Toast.LENGTH_SHORT).show();
                        et_daily_radio_2.setFocusableInTouchMode(true);
                        et_daily_radio_2.requestFocus();
                        break;
                    }
                    String oneday = (et_daily_radio_3.getText() == null) ? "" : et_daily_radio_3.getText().toString();
                    if("".equals(oneday)){
                        Toast.makeText(mContext, "하루분량을 입력해주세요.", Toast.LENGTH_SHORT).show();
                        et_daily_radio_3.setFocusableInTouchMode(true);
                        et_daily_radio_3.requestFocus();
                        break;
                    }
                    if(Integer.parseInt(total) < Integer.parseInt(oneday)) {
                        Toast.makeText(mContext, "총페이지보다 하루분량이 작아야 합니다.", Toast.LENGTH_SHORT).show();
                        et_daily_radio_3.setFocusableInTouchMode(true);
                        et_daily_radio_3.requestFocus();
                    }
                    break;
                case R.id.et_daily_radio_4:
                    // 하루 할당량 계산. 목표일 설정 체크
                    if(targetDate == null){
                        Toast.makeText(mContext, "목표일을 설정해주세요.", Toast.LENGTH_SHORT).show();
                        break;
                    }
                    String total_1 = (et_daily_radio_4.getText() == null) ? "" : et_daily_radio_4.getText().toString();
                    if("".equals(total_1)){
                        Toast.makeText(mContext, "총페이지를 입력해주세요.", Toast.LENGTH_SHORT).show();
                        et_daily_radio_4.setFocusableInTouchMode(true);
                        et_daily_radio_4.requestFocus();
                        break;
                    }
                    // 오늘부터 목표일 까지의 날 수 계산(나중에는 시작일, 주말제외 옵셥을 넣어야 함)
                    long diffSec = (targetDate.getTimeInMillis() - baseDate.getTimeInMillis()) / 1000;
                    long diffDay = diffSec / (24*60*60);
                    int diffDayInt = (int)diffDay + 1;
                    int totoal_1_int = Integer.parseInt(total_1);
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
}
