package com.mhd.stard.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.mhd.stard.R;

public class IntroActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initialize(R.layout.activity_intro);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(getApplicationContext(),StartActivity.class);
                startActivity(intent);
                finish();
            }
        },1000);
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }
}
