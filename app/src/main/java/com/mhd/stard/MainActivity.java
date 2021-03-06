package com.mhd.stard;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.core.content.ContextCompat;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.mhd.stard.activity.BaseActivity;
import com.mhd.stard.activity.ModifyKidsActivity;
import com.mhd.stard.activity.ModifyScheduleActivity;
import com.mhd.stard.activity.ModifySelfActivity;
import com.mhd.stard.activity.ModifyTodoActivity;
import com.mhd.stard.activity.RegistKidsActivity;
import com.mhd.stard.activity.KidsListActivity;
import com.mhd.stard.activity.RegistScheduleActivity;
import com.mhd.stard.activity.RegistSelfActivity;
import com.mhd.stard.activity.RegistTodoActivity;
import com.mhd.stard.adapter.MenuPagerAdapter;
import com.mhd.stard.common.MHDApplication;
import com.mhd.stard.common.vo.TodoVo;
import com.mhd.stard.constant.MHDConstants;
import com.mhd.stard.fragment.MenuDialogFragment;
import com.mhd.stard.fragment.ScheduleFragment;
import com.mhd.stard.fragment.SelfFragment;
import com.mhd.stard.fragment.SettingFragment;
import com.mhd.stard.fragment.SumFragment;
import com.mhd.stard.fragment.TodoFragment;
import com.mhd.stard.util.MHDDialogUtil;
import com.mhd.stard.util.MHDLog;
import com.mhd.stard.view.GlobalTabsView;

public class MainActivity extends BaseActivity implements MenuDialogFragment.MenuDialogListener {

    public ViewPager2 viewPager2;
    public MenuPagerAdapter adapter;
    public static Context context_main;

    @Override
    public void onDialogItemSelected(int which, int position) {
        ((TodoFragment) getSupportFragmentManager().findFragmentByTag("f0")).onDialogResult(which, position);
    }
    @Override
    public void onSelfDialogItemSelected(int which, int position) {
        ((SelfFragment) getSupportFragmentManager().findFragmentByTag("f2")).onDialogResult(which, position);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initialize(R.layout.activity_main);
        context_main = this;

        overridePendingTransition(R.anim.horizon_in, R.anim.none);

        // ?????? status bar ??? ????????? ??? ???????????? ???????????????.
//        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_FULLSCREEN);

        // ??????. ?????? ??????????????? ?????? ????????? ?????? ??????
        final View background = findViewById(R.id.am_background_view);

        // viewpager
        viewPager2 = findViewById(R.id.am_view_pager);
        adapter = new MenuPagerAdapter(this);
        viewPager2.setAdapter(adapter);

        // UI ??? ????????? ???????????? ???
        final int colorBlue = ContextCompat.getColor(this, R.color.light_blue);
        final int colorPurple = ContextCompat.getColor(this, R.color.light_purple);

        // viewpager ????????? ?????? ??????, ????????? ?????? ??????????????? ??????.
        ViewPager2.OnPageChangeCallback callback = new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position,
                                       float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);

                if(position == 0) {
//                    background.setBackgroundColor(colorBlue);
//                    background.setAlpha(1 - positionOffset);
                    MHDLog.i("dagian = 0", 1 - positionOffset);
                }
                else if(position == 1) {
//                    background.setBackgroundColor(colorPurple);
//                    background.setAlpha(positionOffset);
                    MHDLog.i("dagian = 1", positionOffset);
                }
            }
        };
        viewPager2.registerOnPageChangeCallback(callback);

        // viewpager ????????? ???????????? ??????.
        MHDApplication.getInstance().getMHDSvcManager().setGlobalTabsView((GlobalTabsView) findViewById(R.id.am_snap_tabs));
        MHDApplication.getInstance().getMHDSvcManager().getGlobalTabsView().setUpWithMenuViewPager(viewPager2, MainActivity.this);

        // viewpager ?????? ???????????? view ?????? ??????.
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
                // ????????? ???????????? ????????? ????????????
            }
        }
        super.onNewIntent(intent);
    }

    /**
     * volley listner ??? success response ??????.
     * ????????? ????????? override ?????? ??????.
     * ???????????? Fragment ????????? ??????????
     */
    @Override
    protected boolean networkResponseProcess(String result) {
        boolean resultFlag = super.networkResponseProcess(result);
        MHDLog.d(TAG, "networkResponseProcess resultFlag >>> " + resultFlag);

        if(!resultFlag) return resultFlag;

        // resultFlag ??? true ?????? ?????? ????????? ????????? data ?????? ????????? ????????? ?????? ??????.
        if("M".equals(nvResultCode)){
            // fragment?????? ?????????????????? ?????? ???????????? ????????? ????????? ??????.
            // ?????? ???????????? ????????? ????????? ????????? ?????? fragment ??? ?????? ???????????? ?????? ??????
            if(nvApi.equals(getApplicationContext().getString(R.string.restapi_query_todo))){ // ??????
                MHDApplication.getInstance().getMHDSvcManager().setTodoVo(null);
                ((TodoFragment) getSupportFragmentManager().findFragmentByTag("f0")).noData(nvApi);
            }else if(nvApi.equals(getApplicationContext().getString(R.string.restapi_query_schedule))){ // ?????????
                MHDApplication.getInstance().getMHDSvcManager().setScheduleVo(null);
                ((ScheduleFragment) getSupportFragmentManager().findFragmentByTag("f1")).noData(nvApi);
            }else if(nvApi.equals(getApplicationContext().getString(R.string.restapi_query_self))){ // ??????
                MHDApplication.getInstance().getMHDSvcManager().setSelfVo(null);
                ((SelfFragment) getSupportFragmentManager().findFragmentByTag("f2")).noData(nvApi);
            }else if(nvApi.equals(getApplicationContext().getString(R.string.restapi_update_self))){ // ?????? ??????
                ((SelfFragment) getSupportFragmentManager().findFragmentByTag("f2")).noData(nvApi);
            }else if(nvApi.equals(getApplicationContext().getString(R.string.restapi_update_todo_check))){ // ?????? ??????
                ((TodoFragment) getSupportFragmentManager().findFragmentByTag("f0")).noData(nvApi);
            }else if(nvApi.equals(getApplicationContext().getString(R.string.restapi_query_sum))){ // ????????? ??????
                ((SumFragment) getSupportFragmentManager().findFragmentByTag("f3")).noData(nvApi);
            }else if(nvApi.equals(getApplicationContext().getString(R.string.restapi_query_end))){ // ???????????? ??????
                ((SumFragment) getSupportFragmentManager().findFragmentByTag("f3")).noData(nvApi);
            }else {
                // ?????? ???????????? Just show nvMsg
                MHDDialogUtil.sAlert(mContext, nvMsg);
            }
            return true;
        }else if("S".equals(nvResultCode)){
            if(nvApi.equals(getApplicationContext().getString(R.string.restapi_regist_post))){
                // regist post
                MHDLog.d(TAG, "networkResponseProcess nvMsg >>> " + nvMsg);

            }else if(nvApi.equals(getApplicationContext().getString(R.string.restapi_query_todo))){
                // TodoFragment ?????? ??????.
//                callFragmentMethod(0);
                ((TodoFragment) getSupportFragmentManager().findFragmentByTag("f0")).networkResponseProcess(nvMsg, nvCnt, nvJsonDataString);
            }else if(nvApi.equals(getApplicationContext().getString(R.string.restapi_query_schedule))){
                // ScheduleFragment ?????? ??????.
//                callFragmentMethod(0);
                ((ScheduleFragment) getSupportFragmentManager().findFragmentByTag("f1")).networkResponseProcess(nvMsg, nvCnt, nvJsonDataString);
            }else if(nvApi.equals(getApplicationContext().getString(R.string.restapi_query_self))){
                // SelfFragment ?????? ??????.
//                callFragmentMethod(0);
                ((SelfFragment) getSupportFragmentManager().findFragmentByTag("f2")).networkResponseProcess(nvMsg, nvCnt, nvJsonDataString);
            }else if(nvApi.equals(getApplicationContext().getString(R.string.restapi_update_self))){
                // SelfFragment ?????? item update.
//                callFragmentMethod(0);
                ((SelfFragment) getSupportFragmentManager().findFragmentByTag("f2")).networkResponseProcess_update(nvMsg, nvCnt, nvJsonDataString);
            }else if(nvApi.equals(getApplicationContext().getString(R.string.restapi_update_todo_check))){
                // TodoFragment ?????? item update.
                // ?????? ?????? ???????????? ????????? ??????????????? ????????? ?????? ????????? ???????????? ?????? ???????????? ?????????, ??????????????? ????????? ????????? ??????
//                ((TodoFragment) getSupportFragmentManager().findFragmentByTag("f0")).queryTodo();
//                callFragmentMethod(0);
//                ((TodoFragment) getSupportFragmentManager().findFragmentByTag("f0")).networkResponseProcess_update(nvMsg, nvCnt, nvJsonDataString);
            }else if(nvApi.equals(getApplicationContext().getString(R.string.restapi_query_sum))){ // ?????????
                // ?????? ??????
//                callFragmentMethod(0);
                ((SumFragment) getSupportFragmentManager().findFragmentByTag("f3")).networkResponseProcess(nvMsg, nvCnt, nvMsg2, nvCnt2, nvJsonDataString);
            }else if(nvApi.equals(getApplicationContext().getString(R.string.restapi_query_end))) {
                // ????????? ??????
//                callFragmentMethod(0);
                ((SumFragment) getSupportFragmentManager().findFragmentByTag("f3")).networkResponseProcess_end(nvMsg, nvCnt, nvJsonDataString);
            }else if(nvApi.equals(getApplicationContext().getString(R.string.restapi_delete_todo))) {
                ((TodoFragment) getSupportFragmentManager().findFragmentByTag("f0")).queryTodo();
            }else if(nvApi.equals(getApplicationContext().getString(R.string.restapi_delete_self))) {
                ((SelfFragment) getSupportFragmentManager().findFragmentByTag("f2")).querySelf();
            }

        }

        return true;
    }

//    // viewpager?????? ?????? fragment??? ????????? ??? ?????? ?????? function??? ?????? ?????? ???
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
                        // preference ???????????? & ?????? ??????.
                        ((SettingFragment) getSupportFragmentManager().findFragmentByTag("f4")).batchFunction("");
                    }
                }
            });
    public void startKidsList(){
        Intent intent = new Intent(context_main, KidsListActivity.class);
        startActivityResultKidsList.launch(intent);
    }
    ActivityResultLauncher<Intent> startActivityResultKidsList = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        // preference ???????????? & ?????? ??????.
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

    public void startScheduleModify(int position){
        Intent intent = new Intent(context_main, ModifyScheduleActivity.class);
        intent.putExtra("position", position);
        startActivityResultScheduleModify.launch(intent);
    }
    ActivityResultLauncher<Intent> startActivityResultScheduleModify = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        ((ScheduleFragment) getSupportFragmentManager().findFragmentByTag("f1")).querySchedule();
                    }
                }
            });

    public void startSelfModify(int position){
        Intent intent = new Intent(context_main, ModifySelfActivity.class);
        intent.putExtra("position", position);
        startActivityResultSelfModify.launch(intent);
    }
    ActivityResultLauncher<Intent> startActivityResultSelfModify = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        ((SelfFragment) getSupportFragmentManager().findFragmentByTag("f2")).querySelf();
                    }
                }
            });

    public void startKidsModify(int position){
        Intent intent = new Intent(context_main, ModifyKidsActivity.class);
        intent.putExtra("position", position);
        startActivityResultKidsModify.launch(intent);
    }
    ActivityResultLauncher<Intent> startActivityResultKidsModify = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        ((SettingFragment) getSupportFragmentManager().findFragmentByTag("f4")).batchFunction("");
                    }
                }
            });

    public void logoutProcess(){
        confirmLogout();
    }

    public void showPMenu(int position){
        if(position == 0)
            ((TodoFragment) getSupportFragmentManager().findFragmentByTag("f0")).showPMenu();
        if(position == 1)
            ((ScheduleFragment) getSupportFragmentManager().findFragmentByTag("f1")).showPMenu();
        if(position == 2)
            ((SelfFragment) getSupportFragmentManager().findFragmentByTag("f2")).showPMenu();
        if(position == 3)
            ((SumFragment) getSupportFragmentManager().findFragmentByTag("f3")).showPMenu();
    }

}
