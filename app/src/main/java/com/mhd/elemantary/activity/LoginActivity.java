package com.mhd.elemantary.activity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
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

import com.mhd.elemantary.R;
import com.mhd.elemantary.common.MHDApplication;
import com.mhd.elemantary.constant.MHDConstants;
import com.mhd.elemantary.network.MHDNetworkInvoker;
import com.mhd.elemantary.util.MHDDialogUtil;
import com.mhd.elemantary.util.MHDLog;
import com.mhd.elemantary.util.Util;
import com.mhd.elemantary.webview.activity.HybridWebGuestActivity;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

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
            // Just show nvMsg
            MHDDialogUtil.sAlert(mContext, nvMsg);
        }else if("S".equals(nvResultCode)){
            // 로그인 성공. user vo 를 구성하고
            MHDLog.d(TAG, "networkResponseProcess nvMsg >>> " + nvMsg);

            MHDDialogUtil.sAlert(mContext, R.string.alert_join_success, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(mContext, LoginActivity.class);
                    mContext.startActivity(intent);
                    finish();
                }
            });
            // 가입되었다는 메세지 띄우고 로그인 창으로 이동.
            // 가입하고 나서 최초 한번 학생등록 컨펌 창을 띄운다.
//            // vo 및 각종 변수에 저장하고 메인으로 넘긴다. 레코드가 하나여도 jsonarray 로 보내니 gson에서 에러가 나드라.
//            Gson gson = new Gson();
//            UserVo userVo;
//            userVo = gson.fromJson(nvMsg, UserVo.class);
//            MHDApplication.getInstance().getMHDSvcManager().setUserVo(null);
//            MHDApplication.getInstance().getMHDSvcManager().setUserVo(userVo);
//
//            if(MHDApplication.getInstance().getMHDSvcManager().getUserVo() != null){
//                // MainActivity 로 이동
//                goMain();
//            }
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
}
