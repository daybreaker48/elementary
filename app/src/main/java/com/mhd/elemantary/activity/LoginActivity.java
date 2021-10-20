package com.mhd.elemantary.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.TextView;

import com.google.gson.Gson;
import com.mhd.elemantary.R;
import com.mhd.elemantary.common.MHDApplication;
import com.mhd.elemantary.common.vo.KidsVo;
import com.mhd.elemantary.common.vo.MenuVo;
import com.mhd.elemantary.common.vo.UserVo;
import com.mhd.elemantary.constant.MHDConstants;
import com.mhd.elemantary.network.MHDNetworkInvoker;
import com.mhd.elemantary.util.MHDDialogUtil;
import com.mhd.elemantary.util.MHDLog;
import com.mhd.elemantary.util.Util;
import com.mhd.elemantary.webview.activity.HybridWebGuestActivity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.preference.PreferenceManager;

public class LoginActivity extends BaseActivity {

    private Button btn_login;
    private EditText et_login_id, et_login_pwd;
    private TextView tv_join;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initialize(R.layout.activity_login);

        mContext = LoginActivity.this;
        btn_login = (Button) findViewById(R.id.btn_login);
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginService();
            }
        });
        et_login_id = (EditText) findViewById(R.id.et_login_id);
        et_login_pwd = (EditText) findViewById(R.id.et_login_pwd);
        tv_join = (TextView) findViewById(R.id.tv_join);
        tv_join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, JoinActivity.class);
                mContext.startActivity(intent);
            }
        });
    }
    private void loginService(){
        //입력된 정보를 취합해서 서버 전송

        //필터링. (gender, year, agree terms)
        if(et_login_id.getText().toString().length() == 0) { MHDDialogUtil.sAlert(mContext, R.string.alert_not_id); return; }
        if(et_login_pwd.getText().toString().length() == 0) { MHDDialogUtil.sAlert(mContext, R.string.alert_not_pwd); return; }

        try {
            // call service intro check
            // String 방식
//            StringBuilder fullParams = new StringBuilder("{");
//            fullParams.append("\"UUID\":\""+userVo.getUuID()+"\"")
//                    .append(",\"UUPN\":\""+userVo.getUuPN()+"\"")
//                    .append(",\"UUOS\":\""+userVo.getUuOs()+"\"")
//                    .append(",\"UUDEVICE\":\""+userVo.getUuDevice()+"\"")
//                    .append(",\"UUTOKEN\":\""+userVo.getUuToken()+"\"")
//                    .append(",\"UUAPP\":\""+userVo.getUuAppVer()+"\"")
//                    .append("}");
            // Map 방식 0
            Map<String, String> params = new HashMap<String, String>();
            params.put("UUAPP", MHDApplication.getInstance().getAppVersion());
            params.put("UUMAIL", et_login_id.getText().toString());
            params.put("UUPWD", et_login_pwd.getText().toString());
            params.put("UULOGIN", "E");
            MHDNetworkInvoker.getInstance().sendVolleyRequest(mContext, R.string.url_restapi_login_member, params, responseListener);

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
        //Main 이동
    }

    @Override
    protected boolean networkResponseProcess(String result) {
        boolean resultFlag = super.networkResponseProcess(result);
        MHDLog.d(TAG, "networkResponseProcess resultFlag >>> " + resultFlag);

        if(!resultFlag) return resultFlag;

        // resultFlag 이 true 라면 현재 여기에 필요한 data 들이 전역에 들어가 있는 상태.

        if("M".equals(nvResultCode)){
            // nvMsg = "NK" 라면 회원이지만 아이 정보가 없다는 것.
            if("NK".equals(nvMsg)){
                // 이제 Tutorial 이 뜨지 않는다.
                MHDApplication.getInstance().getMHDSvcManager().setIsFirstStart(false);
                SharedPreferences appPref = PreferenceManager.getDefaultSharedPreferences(mContext);
                SharedPreferences.Editor editor = appPref.edit();
                editor.putBoolean("execFirst", false);
                editor.commit();

                UserVo userVo = new UserVo();
                userVo.setUuID(MHDApplication.getInstance().getMHDSvcManager().getDeviceNewUuid());
                userVo.setUuToken(MHDApplication.getInstance().getMHDSvcManager().getFcmToken());
                userVo.setUuAppVer(MHDApplication.getInstance().getAppVersion());
                userVo.setUuMail(et_login_id.getText().toString());
                userVo.setUuLogin("E");
                MHDApplication.getInstance().getMHDSvcManager().setUserVo(null);
                MHDApplication.getInstance().getMHDSvcManager().setUserVo(userVo);

                MHDDialogUtil.sAlert(mContext, R.string.content_not_kids, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startKidsRegistMain();
                        return;
                    }
                });
            }else {
                MHDDialogUtil.sAlert(mContext, nvMsg);
            }
        }else if("S".equals(nvResultCode)){
            // 이제 Tutorial 이 뜨지 않는다.
            MHDApplication.getInstance().getMHDSvcManager().setIsFirstStart(false);
            SharedPreferences appPref = PreferenceManager.getDefaultSharedPreferences(mContext);
            SharedPreferences.Editor editor = appPref.edit();
            editor.putBoolean("execFirst", false);
            editor.commit();

            // 로그인 성공. user vo 를 구성하고
            MHDLog.d(TAG, "networkResponseProcess nvMsg >>> " + nvMsg);

            // 가입되었다는 메세지 띄우고 로그인 창으로 이동.
            // vo 및 각종 변수에 저장하고 메인으로 넘긴다. 레코드가 하나여도 jsonarray 로 보내니 gson에서 에러가 나드라.
            UserVo userVo = new UserVo();
            userVo.setUuID(MHDApplication.getInstance().getMHDSvcManager().getDeviceNewUuid());
            userVo.setUuToken(MHDApplication.getInstance().getMHDSvcManager().getFcmToken());
            userVo.setUuAppVer(MHDApplication.getInstance().getAppVersion());
            userVo.setUuMail(et_login_id.getText().toString());
            userVo.setUuLogin("E");
            MHDApplication.getInstance().getMHDSvcManager().setUserVo(null);
            MHDApplication.getInstance().getMHDSvcManager().setUserVo(userVo);

            Gson gson = new Gson();
            KidsVo kidsVo;
            kidsVo = gson.fromJson(nvJsonDataString, KidsVo.class);
            MHDApplication.getInstance().getMHDSvcManager().setKidsVo(null);
            MHDApplication.getInstance().getMHDSvcManager().setKidsVo(kidsVo);

            // 메뉴별로 보여주는 아이가 다를 수 있기 때문에. MenuVo에 저장해서 관리
            String jsonString = "{" +
                                    "\"msg\":[{" +
                                        "\"menuname\":\"TO\"," +
                                        "\"kidname\":\""+ kidsVo.getMsg().get(0).getName() +"\"},{" +
                                        "\"menuname\":\"SC\"," +
                                        "\"kidname\":\""+ kidsVo.getMsg().get(0).getName() +"\"},{" +
                                        "\"menuname\":\"SE\"," +
                                        "\"kidname\":\""+ kidsVo.getMsg().get(0).getName() +"\"},{" +
                                        "\"menuname\":\"SU\"," +
                                        "\"kidname\":\""+ kidsVo.getMsg().get(0).getName() +"\"}]}";
            MenuVo menuVo;
            menuVo = gson.fromJson(jsonString, MenuVo.class);
            MHDApplication.getInstance().getMHDSvcManager().setMenuVo(null);
            MHDApplication.getInstance().getMHDSvcManager().setMenuVo(menuVo);

            if(MHDApplication.getInstance().getMHDSvcManager().getUserVo() != null){
                // MainActivity 로 이동
                goMain();
            }
//            // 학생을 등록하겠느냐는 컨펌창을 띄우고 확인 -> 등록창, 취소 -> 메인
//            MHDDialogUtil.sAlert(this, R.string.alert_join_success, new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialog, int which) {
//                    // 학생 등록창으로 이동.
//                }
//            }, new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialog, int which) {
//                    // 메인으로 이동. 아무 내용 안나올 것.
//                }
//            });
        }

        return true;
    }

    public void startKidsRegistMain(){
        Intent intent = new Intent(mContext, RegistKidsActivity.class);
        startActivityResultKids.launch(intent);
    }
    ActivityResultLauncher<Intent> startActivityResultKids = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        // 아이 정보가 등록이 됐다면 다시 로그인 태운다?
                        loginService();
                    } else {
                        // Login 단에서 아이 정보가 하나도 등록이 안된다면 앱 종료
                        exitApplication();
                    }
                }
            });
}
