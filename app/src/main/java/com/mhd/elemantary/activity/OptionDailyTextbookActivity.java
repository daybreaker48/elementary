package com.mhd.elemantary.activity;

import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.mhd.elemantary.R;
import com.mhd.elemantary.adapter.ReCyclerAdapter;

import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class OptionDailyTextbookActivity extends BaseActivity {

    TextView tv_selectday, vst_top_title;
    LinearLayout ll_daily_progress;
    private String[] day_array = new String[7];
    RadioGroup rg_daily_progress;
    RadioButton rb_daily_progress_1, rb_daily_progress_2, rb_daily_progress_3;
    EditText et_daily_radio_1, et_daily_radio_2, et_daily_radio_3;

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initialize(R.layout.activity_regist_todo_textbook_option);
        mContext = OptionDailyTextbookActivity.this;

        vst_top_title = (TextView) findViewById(R.id.vst_top_title);
        vst_top_title.setText(R.string.title_todo_regist_textbook);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recv_textbook);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(mContext);
        recyclerView.setLayoutManager(layoutManager);

        String[] textSet = {"111", "222", "333", "444", "111", "222", "333", "444", "111", "222", "333", "444"};
        int[] imgSet = {R.drawable.icon_link, R.drawable.ico_ofw_logout, R.drawable.icon_bullet_list, R.drawable.icon_numbered_list, R.drawable.icon_link, R.drawable.ico_ofw_logout, R.drawable.icon_bullet_list, R.drawable.icon_numbered_list, R.drawable.icon_link, R.drawable.ico_ofw_logout, R.drawable.icon_bullet_list, R.drawable.icon_numbered_list};

        adapter = new ReCyclerAdapter(textSet, imgSet);
        recyclerView.setAdapter(adapter);

        rg_daily_progress = (RadioGroup) findViewById(R.id.rg_daily_progress);
        rb_daily_progress_1 = (RadioButton) findViewById(R.id.rb_daily_progress_1);
        rb_daily_progress_2 = (RadioButton) findViewById(R.id.rb_daily_progress_2);
        rb_daily_progress_3 = (RadioButton) findViewById(R.id.rb_daily_progress_3);
        et_daily_radio_1 = (EditText) findViewById(R.id.et_daily_radio_1);
        et_daily_radio_2 = (EditText) findViewById(R.id.et_daily_radio_2);
        et_daily_radio_3 = (EditText) findViewById(R.id.et_daily_radio_3);
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

        et_daily_radio_1.post(new Runnable() {
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
                        et_daily_radio_1.setVisibility(View.VISIBLE);
                        et_daily_radio_2.setVisibility(View.GONE);
                        et_daily_radio_3.setVisibility(View.GONE);
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
                        et_daily_radio_1.setVisibility(View.GONE);
                        et_daily_radio_2.setVisibility(View.VISIBLE);
                        et_daily_radio_3.setVisibility(View.GONE);
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
                        et_daily_radio_1.setVisibility(View.GONE);
                        et_daily_radio_2.setVisibility(View.GONE);
                        et_daily_radio_3.setVisibility(View.VISIBLE);
                        et_daily_radio_3.post(new Runnable() {
                            @Override
                            public void run() {
                                et_daily_radio_3.setFocusableInTouchMode(true);
                                et_daily_radio_3.requestFocus();
                                InputMethodManager imm = (InputMethodManager)getSystemService(mContext.INPUT_METHOD_SERVICE);
                                imm.showSoftInput(et_daily_radio_3,0);
                            }
                        });
                        break;
                }
            }
        });
    }
}
