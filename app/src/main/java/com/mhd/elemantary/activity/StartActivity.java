package com.mhd.elemantary.activity;

import android.app.AlertDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.widget.ArrayAdapter;

import com.google.gson.Gson;
import com.mhd.elemantary.R;
import com.mhd.elemantary.common.MHDApplication;
import com.mhd.elemantary.common.vo.UserVo;
import com.mhd.elemantary.network.MHDNetworkInvoker;
import com.mhd.elemantary.util.MHDDialogUtil;
import com.mhd.elemantary.util.MHDLog;
import com.mhd.elemantary.util.Util;

import java.util.HashMap;
import java.util.Map;


public class StartActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initialize(R.layout.activity_intro);
        mContext = StartActivity.this;

        // 상단 status bar 를 감추고 그 영역까지 확대시킨다.
//        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_FULLSCREEN);

        //===S=== badge count 를 사용할거라면 활성화
//        if( MHDPreferences.getInstance().getPrefFirstExecApp() ) {
//            MHDApplication.getInstance().getMHDSvcManager().updateIconBadgeCount(StartActivity.this, 0);
//        }
        //===E=== badge count 를 사용할거라면 활성화

        //===S=== 타이틀 영역에 대한 컨트롤이 필요.백버튼 등
        // 이 형식의 개별 화면들의 xml 에서 공통적으로 include 해서 사용하는 방식이다.
        // boom 1.0.x 에서는 메뉴를 전체 레이어 top 에서 개별적으로 움직이도록 구현한다. 그래서 이 방식은 해당사항 없다.
        //isVisibleBackBtn(false);
        //===E=== 타이틀 영역에 대한 컨트롤이 필요.백버튼 등

        //===S=== FCM 탭 클릭 진입처리
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            String channelId = getString(R.string.default_notification_channel_id);
            String channelName = getString(R.string.default_notification_channel_name);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(new NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_LOW));
        }
        if(getIntent().getExtras() != null){
            for(String key : getIntent().getExtras().keySet()){
                Object value = getIntent().getExtras().get(key);
                MHDLog.d(TAG, "Key: " + key + " Value: " + value);
            }
        }
        //===E=== FCM 탭 클릭 진입처리

        String deploy = getString(R.string.SERVICE_ENVIRONMENT);
        //===S=== 현재 플랫폼 접속 정보를 가져와서 상용이 아니면 서버선택 창 호출
        // 혼자하는 작업. 현재는 의미없음
//        if (!"RELEASE".equals(deploy)) { // dev mode
//            setServerDialog();
//        } else {
//            if(checkedNetwork()) {
//                checkRunEmul(deploy);
//            }
//        }
        //===S=== 현재 플랫폼 접속 정보를 가져와서 상용이 아니면 서버선택 창 호출

        checkRunEmul(deploy);
    }
    /**
     * onBackPressed
     * @Override
     */
    @Override
    public void onBackPressed() {
        exitProcess();
    }

    /**
     * test server set
     */
    private void setServerDialog() {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(StartActivity.this, android.R.layout.select_dialog_singlechoice);
        adapter.add(getString(R.string.SERVICE_ALPHA));
        adapter.add(getString(R.string.SERVICE_BETA));
        adapter.add(getString(R.string.SERVICE_OMEGA));
        adapter.add(getString(R.string.SERVICE_RELEASE));

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(StartActivity.this);
        alertDialogBuilder.setTitle("Server");
        alertDialogBuilder.setAdapter(adapter, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface arg0, int position) {

                if(position == 0) { // ALPHA
                    MHDApplication.getInstance().getMHDSvcManager().load(getString(R.string.SERVICE_ALPHA));
                    checkRunEmul(getString(R.string.SERVICE_ALPHA));
                } else if(position == 1) { // BETA
                    MHDApplication.getInstance().getMHDSvcManager().load(getString(R.string.SERVICE_BETA));
                    checkRunEmul(getString(R.string.SERVICE_BETA));
                } else if(position == 2) { // OMEGA
                    MHDApplication.getInstance().getMHDSvcManager().load(getString(R.string.SERVICE_OMEGA));
                    checkRunEmul(getString(R.string.SERVICE_OMEGA));
                } else if(position == 3) { // RELEASE
                    MHDApplication.getInstance().getMHDSvcManager().load(getString(R.string.SERVICE_RELEASE));
                    checkRunEmul(getString(R.string.SERVICE_RELEASE));
                }
            }
        });
        alertDialogBuilder.show();
    }
    /**
     * run emulator check
     */
    private void checkRunEmul(String env) {
        if (Util.getInstance().isEmulator()) {
            // 루팅 -> 종료
            MHDDialogUtil.sAlert(StartActivity.this, R.string.alert_checkDevice, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    exitProcess();
                }
            });
        } else {
            // permission 획득.
            // 일단 이 권한을 요청하지 않는 것으로 했다. 다른 참고 어플들은 이 권한을 요청하지 않는다.
            // 폰번호도 가져오지 못한다.
            //checkPermissionMM(mContext, Manifest.permission.READ_PHONE_STATE);
            authUser();
        }
    }
    /**
     * user base auth
     */
    public void authUser(){
        try {
            // 획득한 uuid 로 서버조회 후 처리.

            // call service intro check
//                // String 방식
//                StringBuilder fullParams = new StringBuilder("{");
//                fullParams.append("\"UUID\":\""+userVo.getUuID()+"\"")
//                        .append(",\"UUPN\":\""+userVo.getUuPN()+"\"")
//                        .append(",\"UUOS\":\""+userVo.getUuOs()+"\"")
//                        .append(",\"UUDEVICE\":\""+userVo.getUuDevice()+"\"")
//                        .append(",\"UUTOKEN\":\""+userVo.getUuToken()+"\"")
//                        .append(",\"UUAPP\":\""+userVo.getUuAppVer()+"\"")
//                      v  .append("}");
            // Map 방식 0
            Map<String, String> params = new HashMap<String, String>();
            //params.put("UUID", MHDApplication.getInstance().getMHDSvcManager().getDeviceNewUuid());
            params.put("UUPN", Util.getInstance().getPhoneNumber(mContext));
            params.put("UUOS", Integer.toString(Build.VERSION.SDK_INT));
            params.put("UUDEVICE", Build.MODEL);
            //params.put("UUTOKEN", MHDApplication.getInstance().getMHDSvcManager().getFcmToken());
            params.put("UUAPP", MHDApplication.getInstance().getAppVersion());

            MHDNetworkInvoker.getInstance().sendVolleyRequest(StartActivity.this, R.string.url_restapi_introcheck, params, responseListener);

//            new Handler().postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    Intent intent = new Intent(mContext, MainActivity.class);
//                    intent.putExtra("field", "value");
//                    startActivity(intent);
//                    overridePendingTransition(0, 0);
//                    finish();
//                }
//            }, 500);
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
            if(nvCnt == 0){
                // tb_user 테이블에 사용자가 없는 경우. 회원이 아닌 경우.
                // 튜토리얼은 앱 최초 실행때만 보여주는 것이 맞으나, 일단은 가입하지 않은 경우는 튜토리얼을 계속 보여주는거로. 나중에 수정할 수도 있다.
                // 슬라이드 형식 말고 자연스럽게 나타나게.
                Intent i = new Intent(mContext, TutorialActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(i);
                overridePendingTransition(0, 0);
                finish();
            }else{
                MHDLog.d(TAG, "networkResponseProcess nvMsg >>> " + nvMsg);

                // 회원인 경우.
                // 로그인 화면을 띄운다. 튜토리얼 마지막 화면으로 바로 이동시킨다.
                // 자동로그인이라면, vo 및 각종 변수에 저장하고 메인으로 넘긴다.
                Gson gson = new Gson();
                UserVo userVo;
                userVo = gson.fromJson(nvMsg, UserVo.class);
                MHDApplication.getInstance().getMHDSvcManager().setUserVo(null);
                MHDApplication.getInstance().getMHDSvcManager().setUserVo(userVo);

                if(MHDApplication.getInstance().getMHDSvcManager().getUserVo() != null){
                    // MainActivity 로 이동
                    goMain();
                }
            }
        }

//        try {
//            if("S".equals(resultCode)){  // Success
//                // 기존 사용자라면.
//                // 서버 저장. 단말 기본 정보 : VO 방식
//                // 이것은 로그인이 되었을때만 넣는게 맞다.
//                Gson gson = new Gson();
//                UserVo userVo;
//                userVo = gson.fromJson(jsonDataObject.toString(), UserVo.class);
//                MHDApplication.getInstance().getMHDSvcManager().setUserVo(null);
//                MHDApplication.getInstance().getMHDSvcManager().setUserVo(userVo);
//
//                if(MHDApplication.getInstance().getMHDSvcManager().getUserVo() != null){
//                    // MainActivity 로 이동
//                    goMain();
//                }
//            }else{  // maybe -1
//                // 신규 사용자라면.
//
//                // 튜토리얼 화면을 띄운다.
//                // 슬라이드 형식 말고 자연스럽게 나타나게.
//                Intent i = new Intent(mContext, TutorialActivity.class);
//                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
//                startActivity(i);
//                overridePendingTransition(0, 0);
//                finish();
//            }
//        } catch (Exception e) {
//            MHDLog.printException(e);
//        }
        return true;
    }
}
