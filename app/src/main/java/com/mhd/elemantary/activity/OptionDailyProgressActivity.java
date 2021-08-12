package com.mhd.elemantary.activity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
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

import com.mhd.elemantary.R;
import com.mhd.elemantary.util.MHDLog;

import java.util.Arrays;
import java.util.Calendar;

import androidx.appcompat.widget.AppCompatButton;

public class OptionDailyProgressActivity extends BaseActivity {

    TextView tv_selectday, vst_top_title;
    LinearLayout ll_daily_progress;
    private String[] day_array = new String[7];
    String sendDay;
    RadioGroup rg_daily_progress;
    RadioButton rb_daily_progress_1, rb_daily_progress_2, rb_daily_progress_3;
    LinearLayout ll_daily_radio_1, ll_daily_radio_2, ll_daily_radio_3;
    EditText et_daily_radio_1, et_daily_radio_2;
    AppCompatButton btn_todo_goal;
    private DatePickerDialog.OnDateSetListener callbackMethod;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initialize(R.layout.activity_regist_todo_option);
        mContext = OptionDailyProgressActivity.this;

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        sendDay = (extras == null) ? "" : extras.getString("sendDay");


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
        AppCompatButton btn_todo_cancel = (AppCompatButton) findViewById(R.id.btn_todo_cancel);
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
                // 선택한 날짜가 오늘보다 이전인지 체크. 이전이면 오류류
               btn_todo_goal.setText(year + "년 " + monthOfYear + "월 " + dayOfMonth + "일");
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
}
