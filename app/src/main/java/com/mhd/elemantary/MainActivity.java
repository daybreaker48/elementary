package com.mhd.elemantary;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;
import android.view.View;

import com.mhd.elemantary.activity.BaseActivity;
import com.mhd.elemantary.activity.LoginActivity;
import com.mhd.elemantary.activity.ModifyTodoActivity;
import com.mhd.elemantary.activity.RegistKidsActivity;
import com.mhd.elemantary.activity.RegistScheduleActivity;
import com.mhd.elemantary.activity.RegistSelfActivity;
import com.mhd.elemantary.activity.RegistTodoActivity;
import com.mhd.elemantary.adapter.MenuPagerAdapter;
import com.mhd.elemantary.common.MHDApplication;
import com.mhd.elemantary.constant.MHDConstants;
import com.mhd.elemantary.fragment.ScheduleFragment;
import com.mhd.elemantary.fragment.SelfFragment;
import com.mhd.elemantary.fragment.SettingFragment;
import com.mhd.elemantary.fragment.TodoFragment;
import com.mhd.elemantary.util.MHDDialogUtil;
import com.mhd.elemantary.util.MHDLog;
import com.mhd.elemantary.view.GlobalTabsView;

import static androidx.core.app.ActivityCompat.startActivityForResult;

public class MainActivity extends BaseActivity {

    public ViewPager2 viewPager2;
    public MenuPagerAdapter adapter;
    public static Context context_main;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initialize(R.layout.activity_main);
        context_main = this;
//        setContentView(R.layout.activity_main);

        // 상단 status bar 를 감추고 그 영역까지 확대시킨다.
//        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_FULLSCREEN);

        // 배경. 컬러 애니메이션 등의 효과를 주기 위해
        final View background = findViewById(R.id.am_background_view);

        // viewpager
        viewPager2 = findViewById(R.id.am_view_pager);
        adapter = new MenuPagerAdapter(this);
        viewPager2.setAdapter(adapter);

        // UI 에 필요한 컬러코드 값
        final int colorBlue = ContextCompat.getColor(this, R.color.light_blue);
        final int colorPurple = ContextCompat.getColor(this, R.color.light_purple);

        // viewpager 이동에 따른 컬러, 투명도 변경 애니메이션 처리.
        ViewPager2.OnPageChangeCallback callback = new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position,
                                       float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);

                if(position == 0) {
//                    background.setBackgroundColor(colorBlue);
//                    background.setAlpha(1 - positionOffset);
                    MHDLog.e("dagian = 0", 1 - positionOffset);
                }
                else if(position == 1) {
//                    background.setBackgroundColor(colorPurple);
//                    background.setAlpha(positionOffset);
                    MHDLog.e("dagian = 1", positionOffset);
                }
            }
        };
        viewPager2.registerOnPageChangeCallback(callback);

        // viewpager 위에서 돌아가는 메뉴.
        MHDApplication.getInstance().getMHDSvcManager().setGlobalTabsView((GlobalTabsView) findViewById(R.id.am_snap_tabs));
        MHDApplication.getInstance().getMHDSvcManager().getGlobalTabsView().setUpWithMenuViewPager(viewPager2, MainActivity.this);

        // viewpager 에서 특정위치 view 초기 지정.
        viewPager2.setCurrentItem(0);
    }
    /**
     * onNewIntent
     * @Override
     */
    @Override
    protected void onNewIntent(Intent intent) {
        if(intent != null) {
            boolean isRefreshNeeded = intent.getBooleanExtra(MHDConstants.KEY_IS_MAIN_ACTIVITY_REPRESH_NEEDED, false);

            if(isRefreshNeeded) {
                // 서비스 메인화면 초기화 프로세스
            }
        }
        super.onNewIntent(intent);
    }

    /**
     * volley listner 의 success response 처리.
     * 필요한 곳에서 override 해서 구현.
     * 대부분의 Fragment 처리를 여기서?
     */
    @Override
    protected boolean networkResponseProcess(String result) {
        boolean resultFlag = super.networkResponseProcess(result);
        MHDLog.d(TAG, "networkResponseProcess resultFlag >>> " + resultFlag);

        if(!resultFlag) return resultFlag;

        // resultFlag 이 true 라면 현재 여기에 필요한 data 들이 전역에 들어가 있는 상태.
        if("M".equals(nvResultCode)){
            // fragment에서 호출된거라면 우선 데이타가 없다는 것으로 간주.
            // 그런 경우라면 없다고 화면에 띄우기 위해 fragment 내 함수 호출해서 화면 표시
            if(nvApi.equals(getApplicationContext().getString(R.string.restapi_query_todo))){ // 학습
                ((TodoFragment) getSupportFragmentManager().findFragmentByTag("f0")).noData(nvApi);
            }else if(nvApi.equals(getApplicationContext().getString(R.string.restapi_query_schedule))){ // 스케쥴
                ((ScheduleFragment) getSupportFragmentManager().findFragmentByTag("f1")).noData(nvApi);
            }else if(nvApi.equals(getApplicationContext().getString(R.string.restapi_query_self))){ // 습관
                ((SelfFragment) getSupportFragmentManager().findFragmentByTag("f2")).noData(nvApi);
            }else if(nvApi.equals(getApplicationContext().getString(R.string.restapi_update_self))){ // 습관 완료
                ((SelfFragment) getSupportFragmentManager().findFragmentByTag("f2")).noData(nvApi);
            }else {
                // 그게 아니라면 Just show nvMsg
                MHDDialogUtil.sAlert(mContext, nvMsg);
            }
            return true;
        }else if("S".equals(nvResultCode)){
            if(nvApi.equals(getApplicationContext().getString(R.string.restapi_regist_post))){
                // regist post
                MHDLog.d(TAG, "networkResponseProcess nvMsg >>> " + nvMsg);

            }else if(nvApi.equals(getApplicationContext().getString(R.string.restapi_query_todo))){
                // TodoFragment 화면 구성.
//                callFragmentMethod(0);
                ((TodoFragment) getSupportFragmentManager().findFragmentByTag("f0")).networkResponseProcess(nvMsg, nvCnt, nvJsonDataString);
            }else if(nvApi.equals(getApplicationContext().getString(R.string.restapi_query_schedule))){
                // ScheduleFragment 화면 구성.
//                callFragmentMethod(0);
                ((ScheduleFragment) getSupportFragmentManager().findFragmentByTag("f1")).networkResponseProcess(nvMsg, nvCnt, nvJsonDataString);
            }else if(nvApi.equals(getApplicationContext().getString(R.string.restapi_query_self))){
                // SelfFragment 화면 구성.
//                callFragmentMethod(0);
                ((SelfFragment) getSupportFragmentManager().findFragmentByTag("f2")).networkResponseProcess(nvMsg, nvCnt, nvJsonDataString);
            }else if(nvApi.equals(getApplicationContext().getString(R.string.restapi_update_self))){
                // SelfFragment 개별 item update.
//                callFragmentMethod(0);
                ((SelfFragment) getSupportFragmentManager().findFragmentByTag("f2")).networkResponseProcess_update(nvMsg, nvCnt, nvJsonDataString);
            }

        }

        return true;
    }

//    // viewpager내에 있는 fragment를 찾아서 그 안에 있는 function의 호출 처리 법
//    private Fragment findFragmentByPosition(int position){
//        return getSupportFragmentManager().findFragmentByTag("f"+position);
//    }
//    private void callFragmentMethod(int position){
//        ((TodoFragment)findFragmentByPosition(position)).getData();
//    }

    public void startTodoRegist(){
        Intent intent = new Intent(context_main, RegistTodoActivity.class);
        startActivityResult.launch(intent);
    }
    ActivityResultLauncher<Intent> startActivityResult = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        ((TodoFragment) getSupportFragmentManager().findFragmentByTag("f0")).queryTodo();
                    }
                }
            });

    public void startScheduleRegist(){
        Intent intent = new Intent(context_main, RegistScheduleActivity.class);
        startActivityResultSchedule.launch(intent);
    }
    ActivityResultLauncher<Intent> startActivityResultSchedule = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        ((ScheduleFragment) getSupportFragmentManager().findFragmentByTag("f1")).querySchedule();
                    }
                }
            });

    public void startSelfRegist(){
        Intent intent = new Intent(context_main, RegistSelfActivity.class);
        startActivityResultSelf.launch(intent);
    }
    ActivityResultLauncher<Intent> startActivityResultSelf = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        ((SelfFragment) getSupportFragmentManager().findFragmentByTag("f2")).querySelf();
                    }
                }
            });

    public void startKidsRegist(){
        Intent intent = new Intent(context_main, RegistKidsActivity.class);
        startActivityResultKids.launch(intent);
    }
    ActivityResultLauncher<Intent> startActivityResultKids = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        // preference 업데이트 & 화면 갱신.
                        ((SettingFragment) getSupportFragmentManager().findFragmentByTag("f4")).batchFunction("");
                    }
                }
            });

    public void startTodoModify(int position){
        Intent intent = new Intent(context_main, ModifyTodoActivity.class);
        intent.putExtra("position", position);
        startActivityResultTodoModify.launch(intent);
    }
    ActivityResultLauncher<Intent> startActivityResultTodoModify = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        ((TodoFragment) getSupportFragmentManager().findFragmentByTag("f0")).queryTodo();
                    }
                }
            });

    public void logoutProcess(){
        confirmLogout();
    }
}
