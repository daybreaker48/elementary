package com.mhd.elemantary.activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.widget.AppCompatButton;

import com.github.dhaval2404.colorpicker.MaterialColorPickerDialog;
import com.github.dhaval2404.colorpicker.listener.ColorListener;
import com.github.dhaval2404.colorpicker.model.ColorShape;
import com.github.dhaval2404.colorpicker.model.ColorSwatch;
import com.github.dhaval2404.colorpicker.util.ColorUtil;
import com.mhd.elemantary.R;
import com.mhd.elemantary.common.MHDApplication;
import com.mhd.elemantary.network.MHDNetworkInvoker;
import com.mhd.elemantary.util.MHDDialogUtil;
import com.mhd.elemantary.util.MHDLog;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import org.jetbrains.annotations.NotNull;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


public class RegistScheduleActivity extends BaseActivity implements TimePickerDialog.OnTimeSetListener {

    TextView tv_selectday, vst_top_title, tv_schedule_time, tv_schedule_color, tv_schedule_alarm;
    LinearLayout ll_schedule_time, ll_daily_textbook, ll_schedule_color, ll_schedule_alarm;
    EditText et_schedule_subject;
    private String[] day_array = new String[7];
    String sendDay = "";
    String mMaterialColorSquare = ""; // 설정하지 않으면 white
    String displayStrings, innerStrings = "";
    int startHour = 0, endHour = 0;

    @Override
    public void onTimeSet(TimePickerDialog view, int hourOfDay, int minute, int second) {
        if(view.getTitle().equals("학습 시작")){
            startHour = hourOfDay;
            tv_schedule_time.setText("시 간 : " + String.valueOf(hourOfDay) + ":00 ~ ");
            TimePickerDialog mTimePickerDialog = TimePickerDialog.newInstance(RegistScheduleActivity.this, true);
            mTimePickerDialog.enableMinutes(false);
            mTimePickerDialog.setMinTime(8,0,0);
            mTimePickerDialog.setMaxTime(22,0,0);
            mTimePickerDialog.setTitle("학습 종료");
            mTimePickerDialog.show(getSupportFragmentManager(), "TimePickerDialog");
        }
        if(view.getTitle().equals("학습 종료")){
            if(startHour == 0)
                Toast.makeText(mContext, getString(R.string.content_alarm_start_none), Toast.LENGTH_SHORT).show();
            if(endHour == 0)
                Toast.makeText(mContext, getString(R.string.content_alarm_end_none), Toast.LENGTH_SHORT).show();
            if(startHour >= hourOfDay)
                Toast.makeText(mContext, getString(R.string.content_alarm_time_error), Toast.LENGTH_SHORT).show();

            endHour = hourOfDay;
            String cuString = tv_schedule_time.getText().toString();
            tv_schedule_time.setText(cuString + String.valueOf(hourOfDay) + ":00");
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initialize(R.layout.activity_schedule_regist);
        mContext = RegistScheduleActivity.this;

        et_schedule_subject = (EditText) findViewById(R.id.et_schedule_subject);
        vst_top_title = (TextView) findViewById(R.id.vst_top_title);
        vst_top_title.setText(R.string.title_schedule_regist);
        tv_selectday = (TextView) findViewById(R.id.tv_selectday);
        tv_selectday.setText(getString(R.string.content_dailyprogress));
        tv_schedule_time = (TextView) findViewById(R.id.tv_schedule_time);
        tv_schedule_color = (TextView) findViewById(R.id.tv_schedule_color);
        tv_schedule_alarm = (TextView) findViewById(R.id.tv_schedule_alarm);
        ll_schedule_time = (LinearLayout) findViewById(R.id.ll_schedule_time);
        ll_daily_textbook = (LinearLayout) findViewById(R.id.ll_daily_textbook);
        ll_schedule_color = (LinearLayout) findViewById(R.id.ll_schedule_color);
        ll_schedule_alarm = (LinearLayout) findViewById(R.id.ll_schedule_alarm);

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
        ll_schedule_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { startSchduleTimeActivity(); }
        });
        ll_schedule_color.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { startColorPicker(); }
        });
        ll_schedule_alarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { startAlarmActivity(); }
        });

        btn_todo_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { finish(); }
        });
        btn_todo_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 입력 값 서버로 전송.
                saveProcess();
            }
        });
    }
    public void startSchduleTimeActivity() {
        // 시간 선택
//        TimePickerFragment mTimePickerFragment = new TimePickerFragment();
//        mTimePickerFragment.show(getSupportFragmentManager(), "timepicker");
        TimePickerDialog mTimePickerDialog = TimePickerDialog.newInstance(RegistScheduleActivity.this, true);
        mTimePickerDialog.enableMinutes(false);
        mTimePickerDialog.setMinTime(8,0,0);
        mTimePickerDialog.setMaxTime(22,0,0);
        mTimePickerDialog.setTitle("학습 시작");
        mTimePickerDialog.show(getSupportFragmentManager(), "TimePickerDialog");
    }
    public void startColorPicker() {
        // 컬러 선택
        new MaterialColorPickerDialog
                .Builder(mContext)
                .setTitle("색상")
                .setColorShape(ColorShape.SQAURE)
                .setColorSwatch(ColorSwatch._300)
                .setDefaultColor(mMaterialColorSquare)
                .setColorListener(new ColorListener() {
                    @Override
                    public void onColorSelected(int color, @NotNull String colorHex) {
                        // Handle Color Selection
                        mMaterialColorSquare = colorHex;
                        setButtonBackground(color);
                    }
                })
                .setPositiveButton("확인")
                .show();
    }
    private void setButtonBackground(int color) {
        if (ColorUtil.isDarkColor(color)) {
            tv_schedule_color.setTextColor(Color.WHITE);
        } else {
            tv_schedule_color.setTextColor(Color.BLACK);
        }
        tv_schedule_color.setBackgroundTintList(ColorStateList.valueOf(color));
    }

//    public void startAlarmActivity() {
//        // 교재 선택 창을 띄운다.
//        MHDLog.d(TAG, "sendDay: " + sendDay);
//        Intent i = new Intent(mContext, OptionAlarmActivity.class);
//        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
//        startActivity(i);
//    }

    public void startAlarmActivity(){
        Intent intent = new Intent(mContext, OptionAlarmActivity.class);
        startActivityResult.launch(intent);
    }

    ActivityResultLauncher<Intent> startActivityResult = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {

                    }
                }
            });



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
        }else{
            tv_selectday.setText("매주 " + displayStrings);
        }
    }

    private void saveProcess() {
        String tmpTitle = et_schedule_subject.getText().toString();

        if (tmpTitle == null || "".equals(tmpTitle)) {
            // 학습 날짜 정보가 없다.
            Toast.makeText(mContext, getString(R.string.content_todo_subject), Toast.LENGTH_SHORT).show();
        } else if ("".equals(innerStrings)) {
            // 학습 날짜 정보가 없다.
            Toast.makeText(mContext, getString(R.string.content_dailyprogress), Toast.LENGTH_SHORT).show();
        } else if(startHour == 0) {
            Toast.makeText(mContext, getString(R.string.content_alarm_start_none), Toast.LENGTH_SHORT).show();
        } else if(endHour == 0) {
            Toast.makeText(mContext, getString(R.string.content_alarm_end_none), Toast.LENGTH_SHORT).show();
        } else if(startHour >= endHour) {
            Toast.makeText(mContext, getString(R.string.content_alarm_time_error), Toast.LENGTH_SHORT).show();
        } else {
            if("".equals(mMaterialColorSquare))
                mMaterialColorSquare = "#ffffff";

            sendScheduleData(tmpTitle, String.valueOf(startHour), String.valueOf(endHour), mMaterialColorSquare);
        }
    }

    private void sendScheduleData(String subject, String start, String end, String color){
        try {
            // Map 방식 0
            Map<String, String> params = new HashMap<String, String>();
            params.put("UUMAIL", MHDApplication.getInstance().getMHDSvcManager().getUserVo().getUuMail());
            params.put("TBSUBJECT", subject);
            params.put("SCSUN", innerStrings.contains("1") ? "Y" : "N");
            params.put("SCMON", innerStrings.contains("2") ? "Y" : "N");
            params.put("SCTUE", innerStrings.contains("3") ? "Y" : "N");
            params.put("SCWED", innerStrings.contains("4") ? "Y" : "N");
            params.put("SCTHU", innerStrings.contains("5") ? "Y" : "N");
            params.put("SCFRI", innerStrings.contains("6") ? "Y" : "N");
            params.put("SCSAT", innerStrings.contains("7") ? "Y" : "N");
            params.put("SCSTART", start);
            params.put("SCEND", end);
            params.put("SCCOLOR", color);
            // 알람 추가해야함.
            MHDNetworkInvoker.getInstance().sendVolleyRequest(mContext, R.string.url_restapi_regist_schedule, params, responseListener);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            MHDLog.printException(e);
        }
    }

    @Override
    protected boolean networkResponseProcess(String result) {
        boolean resultFlag = super.networkResponseProcess(result);
        MHDLog.d(TAG, "networkResponseProcess resultFlag >>> " + resultFlag);

        if(!resultFlag) return resultFlag;

        // resultFlag 이 true 라면 현재 여기에 필요한 data 들이 전역에 들어가 있는 상태.

        if("M".equals(nvResultCode)){
            // Just show nvMsg
            MHDDialogUtil.sAlert(mContext, nvMsg);
            return true;
        }else if("S".equals(nvResultCode)){
            if(nvApi.equals(getString(R.string.restapi_regist_schedule))){
                if (nvCnt == 0) {
                    // 정보가 없으면 비정상
                    // 우선 toast를 띄울 것.
                    Toast.makeText(mContext, nvMsg, Toast.LENGTH_SHORT).show();
                } else {
                    // 스케쥴 정상등록 여부를 알림
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
}
