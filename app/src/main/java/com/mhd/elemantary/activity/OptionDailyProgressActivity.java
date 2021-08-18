package com.mhd.elemantary.activity;

import android.app.DatePickerDialog;
import android.content.Intent;
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

import com.mhd.elemantary.R;
import com.mhd.elemantary.util.MHDLog;

import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;

import androidx.appcompat.widget.AppCompatButton;

public class OptionDailyProgressActivity extends BaseActivity implements TextView.OnEditorActionListener {

    TextView tv_selectday, vst_top_title;
    LinearLayout ll_daily_progress;
    private String[] day_array = new String[7];
    String sendDay;
    int currentRadio = 1;
    RadioGroup rg_daily_progress;
    RadioButton rb_daily_progress_1, rb_daily_progress_2, rb_daily_progress_3;
    LinearLayout ll_daily_radio_1, ll_daily_radio_2, ll_daily_radio_3;
    EditText et_daily_radio_1, et_daily_radio_2, et_daily_radio_3, et_daily_radio_4, et_daily_radio_5;
    AppCompatButton btn_todo_goal;
    private DatePickerDialog.OnDateSetListener callbackMethod;
    Calendar baseDate, targetDate = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initialize(R.layout.activity_regist_todo_option);
        mContext = OptionDailyProgressActivity.this;

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        sendDay = (extras == null) ? "" : extras.getString("sendDay");
        MHDLog.d(TAG, "next sendDay: " + sendDay);

        vst_top_title = (TextView) findViewById(R.id.vst_top_title);
        vst_top_title.setText(R.string.title_todo_regist_daily);

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

        ll_daily_radio_1.post(new Runnable() {
            @Override
            public void run() {
                et_daily_radio_1.setFocusableInTouchMode(true);
                et_daily_radio_1.requestFocus();
                InputMethodManager imm = (InputMethodManager)getSystemService(mContext.INPUT_METHOD_SERVICE);
                imm.showSoftInput(et_daily_radio_1,0);
            }
        });
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

    private void enableSave(int checkedRadio) {
        if(checkedRadio == 1){

        } else if(checkedRadio == 2){

        }
    }
}
