package com.mhd.stard.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.mhd.stard.R;

import java.util.Calendar;

import androidx.appcompat.widget.AppCompatButton;

public class OptionAlarmActivity extends BaseActivity {

    TextView tv_selectday, vst_top_title;
    int currentNumber = 10;
    int currentMeasure = 0;
    RadioGroup  rg_schedule_alarm;
    RadioButton rb_schedule_alarm_1, rb_schedule_alarm_2;
    LinearLayout ll_schedule_alarm_2;
    Calendar baseDate, targetDate = null;
    NumberPicker picker1, picker2;
    String[] measure = new String[]{"분", "시간"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initialize(R.layout.activity_schedule_alarm);
        mContext = OptionAlarmActivity.this;

        vst_top_title = (TextView) findViewById(R.id.vst_top_title);
        vst_top_title.setText(R.string.title_schedule_regist_alarm);

        picker1 = (NumberPicker) findViewById(R.id.picker1);
        picker2 = (NumberPicker) findViewById(R.id.picker2);
        picker1.setMinValue(1);
        picker1.setMaxValue(99);
        picker1.setValue(10);
        picker2.setMinValue(0);
        picker2.setMaxValue(1);
        picker2.setDisplayedValues(measure);
        picker2.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        picker1.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                currentNumber = newVal;
                rb_schedule_alarm_2.setText(currentSettingTime(currentNumber, currentMeasure));
            }
        });
        picker2.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                currentMeasure = newVal;
                rb_schedule_alarm_2.setText(currentSettingTime(currentNumber, currentMeasure));
            }
        });

        rg_schedule_alarm = (RadioGroup) findViewById(R.id.rg_schedule_alarm);
        rb_schedule_alarm_1 = (RadioButton) findViewById(R.id.rb_schedule_alarm_1);
        rb_schedule_alarm_2 = (RadioButton) findViewById(R.id.rb_schedule_alarm_2);
        ll_schedule_alarm_2 = (LinearLayout) findViewById(R.id.ll_schedule_alarm_2);
        AppCompatButton btn_alarm_cancel = (AppCompatButton) findViewById(R.id.btn_alarm_cancel);
        btn_alarm_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { finish(); }
        });
        AppCompatButton btn_alarm_save = (AppCompatButton) findViewById(R.id.btn_alarm_save);
        btn_alarm_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 입력 값 아래 화면으로 전달하고 요일에 따른 페이지 등을 계산해서 입력.
                // 그 후에 저장하면 모두 동시에 서버로 전송. 그 후에 결과값 리턴
                setResult(Activity.RESULT_OK);
                finish();
            }
        });

        rg_schedule_alarm.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.rb_schedule_alarm_1:
                        ll_schedule_alarm_2.setVisibility(View.GONE);
                        break;
                    case R.id.rb_schedule_alarm_2:
                        ll_schedule_alarm_2.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    public String currentSettingTime(int cutime, int cumea) {
        String currenttime = "";
        if(cumea == 0){
            currenttime = String.valueOf(cutime) + "분 전";
        }else if(cumea == 1){
            currenttime = String.valueOf(cutime) + "시간 전";
        }else if(cumea == 2){
            currenttime = String.valueOf(cutime) + "일 전";
        }

        return currenttime;
    }
}
