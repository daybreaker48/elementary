package com.mhd.elemantary.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatButton;

import com.mhd.elemantary.R;
import com.mhd.elemantary.util.MHDLog;

import java.util.Arrays;

public class RegistSelfActivity extends BaseActivity {

    TextView tv_selectday, vst_top_title;
    LinearLayout ll_schedule_time, ll_daily_textbook;
    private String[] day_array = new String[7];
    String sendDay = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initialize(R.layout.activity_self_regist);
        mContext = RegistSelfActivity.this;

        vst_top_title = (TextView) findViewById(R.id.vst_top_title);
        vst_top_title.setText(R.string.title_self_regist);
    }
    public void startSchduleTimeActivity() {
        // 할일 요일 정보를 보내야 한다. 오늘/매주 어떤 요일
        MHDLog.d(TAG, "sendDay: " + sendDay);
        Intent i = new Intent(mContext, OptionDailyProgressActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(i);
    }
    public void startDailyTextbookActivity() {
        // 교재 선택 창을 띄운다.
        MHDLog.d(TAG, "sendDay: " + sendDay);
        Intent i = new Intent(mContext, OptionDailyTextbookActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(i);
    }

}
