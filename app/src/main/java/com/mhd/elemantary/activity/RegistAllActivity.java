package com.mhd.elemantary.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.mhd.elemantary.R;
import com.mhd.elemantary.util.MHDLog;

import java.util.Arrays;

import androidx.appcompat.widget.AppCompatButton;

public class RegistAllActivity extends BaseActivity {

    TextView tv_selectday;
    LinearLayout ll_daily_progress;
    private String[] day_array = new String[7];
    String sendDay = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initialize(R.layout.activity_regist);
        mContext = RegistAllActivity.this;

        tv_selectday = (TextView) findViewById(R.id.tv_selectday);
        tv_selectday.setText(getString(R.string.content_dailyprogress));
        ll_daily_progress = (LinearLayout) findViewById(R.id.ll_daily_progress);

        Spinner spi_todo_subject = (Spinner) findViewById(R.id.spi_todo_subject);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.todo_subject_array, R.layout.default_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spi_todo_subject.setAdapter(adapter);
        final Spinner spi_todo_subject_detail = (Spinner) findViewById(R.id.spi_todo_subject_detail);
        ArrayAdapter<CharSequence> adapter_detail = ArrayAdapter.createFromResource(this, R.array.todo_subject_item_0_array, R.layout.default_spinner_item);
        adapter_detail.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spi_todo_subject_detail.setAdapter(adapter_detail);
        Spinner spi_todo_textbook = (Spinner) findViewById(R.id.spi_todo_textbook);
        ArrayAdapter<CharSequence> adapter_textbook = ArrayAdapter.createFromResource(this, R.array.todo_textbook_array, R.layout.default_spinner_item);
        adapter_textbook.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spi_todo_textbook.setAdapter(adapter_textbook);

        // 과목선택에 따라서 detail 항목을 변경해줘야 한다.
        spi_todo_subject.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ArrayAdapter<CharSequence> adapter_detail;
                switch (position) {
                    case 0:
                        adapter_detail = ArrayAdapter.createFromResource(mContext, R.array.todo_subject_item_0_array, R.layout.default_spinner_item);
                        MHDLog.d(TAG, "spi_todo_subject >>> " + position);
                        break;
                    case 1:
                        adapter_detail = ArrayAdapter.createFromResource(mContext, R.array.todo_subject_item_1_array, R.layout.default_spinner_item);
                        MHDLog.d(TAG, "spi_todo_subject >>> " + position);
                        break;
                    case 2:
                        adapter_detail = ArrayAdapter.createFromResource(mContext, R.array.todo_subject_item_2_array, R.layout.default_spinner_item);
                        MHDLog.d(TAG, "spi_todo_subject >>> " + position);
                        break;
                    case 3:
                        adapter_detail = ArrayAdapter.createFromResource(mContext, R.array.todo_subject_item_3_array, R.layout.default_spinner_item);
                        MHDLog.d(TAG, "spi_todo_subject >>> " + position);
                        break;
                    default:
                        adapter_detail = ArrayAdapter.createFromResource(mContext, R.array.todo_subject_item_0_array, R.layout.default_spinner_item);
                        MHDLog.d(TAG, "spi_todo_subject >>> " + position);
                }
                adapter_detail.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spi_todo_subject_detail.setAdapter(adapter_detail);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
//                textView.setText("선택 : ");
            }
        });

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
        ll_daily_progress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { startDailyPregressActivity(); }
        });
        btn_todo_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { finish(); }
        });
        btn_todo_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 입력 값 서버로 전송.
            }
        });
    }
    public void startDailyPregressActivity() {
        // 할일 요일 정보를 보내야 한다. 오늘/매주 어떤 요일
        MHDLog.d(TAG, "sendDay: " + sendDay);
        Intent i = new Intent(mContext, OptionDailyProgressActivity.class);
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
        }else {
            tv_selectday.setText("매주 " + displayStrings);
            sendDay = displayStrings;
        }
    }
}
