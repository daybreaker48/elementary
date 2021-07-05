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
import android.widget.Toast;

import com.google.gson.Gson;
import com.mhd.elemantary.R;
import com.mhd.elemantary.common.MHDApplication;
import com.mhd.elemantary.common.vo.UserVo;
import com.mhd.elemantary.constant.MHDConstants;
import com.mhd.elemantary.network.MHDNetworkInvoker;
import com.mhd.elemantary.util.MHDDialogUtil;
import com.mhd.elemantary.util.MHDLog;
import com.mhd.elemantary.util.Util;
import com.mhd.elemantary.webview.activity.HybridWebGuestActivity;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class JoinActivity extends BaseActivity {

    private CheckBox btnCheckTerms, btnCheckPrivacy, btnCheckGps;
    private TextView btnTerms, btnPrivacy, btnGps;
    private EditText et_join_name, et_join_id, et_join_pwd;
    private Button btn_join;
    private String selectedSex = "M";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initialize(R.layout.activity_join);

        btnCheckTerms = (CheckBox) findViewById(R.id.checkTerms);
        btnCheckPrivacy = (CheckBox) findViewById(R.id.checkPrivacy);
        btnCheckGps = (CheckBox) findViewById(R.id.checkGps);
        btnTerms = (TextView) findViewById(R.id.tv_terms_all_view);
        btnPrivacy = (TextView) findViewById(R.id.tv_privacy_all_view);
        btnGps = (TextView) findViewById(R.id.tv_gps_all_view);
        btn_join = (Button) findViewById(R.id.btn_join);
        et_join_name = (EditText) findViewById(R.id.et_join_name);
        et_join_id = (EditText) findViewById(R.id.et_join_id);
        et_join_pwd = (EditText) findViewById(R.id.et_join_pwd);

        btnTerms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTerms("T");
            }
        });
        btnPrivacy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTerms("P");
            }
        });
        btnGps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTerms("G");
            }
        });
        btn_join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                joinService();
            }
        });
    }

    private void showPicker(){
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);

        final Dialog yearDialog = new Dialog(mContext);
        yearDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        yearDialog.setContentView(R.layout.picker_year);

        final NumberPicker numberPicker = (NumberPicker) yearDialog.findViewById(R.id.np_year_picker);
        numberPicker.setMinValue(year - 100);
        numberPicker.setMaxValue(year - 15);
        numberPicker.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        setDividerColor(numberPicker, R.color.white);
        numberPicker.setWrapSelectorWheel(false);
        numberPicker.setValue(year - 20);
        numberPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
            }
        });

        yearDialog.show();
    }

    private void setDividerColor(NumberPicker picker, int color){
        java.lang.reflect.Field[] pickerFields = NumberPicker.class.getDeclaredFields();
        for(java.lang.reflect.Field pf : pickerFields){
            if(pf.getName().equals("mSelectionDivider")){
                pf.setAccessible(true);
                try{
                    ColorDrawable colorDrawable = new ColorDrawable(color);
                    pf.set(picker, colorDrawable);
                }catch(IllegalArgumentException e){
                    e.printStackTrace();
                }catch(Resources.NotFoundException e){
                    e.printStackTrace();
                }catch(IllegalAccessException e){
                    e.printStackTrace();
                }
                break;
            }
        }
    }

    private void showTerms(String flag){
        //flag 에 따라 적절한 약관 선택하여 웹으로 표시(모두 웹에서 처리)
        Intent intent = new Intent(mContext, HybridWebGuestActivity.class);
        intent.putExtra(MHDConstants.WebView.WEBVIEW_TARGET_URL, getResources().getString(R.string.url_restapi_terms, flag));
        intent.putExtra(MHDConstants.WebView.WEBVIEW_TARGET_TITLE, MHDApplication.getInstance().getResStrFromEntryName(getString(R.string.text_terms_title_header) + flag));
        mContext.startActivity(intent);
    }

    private void joinService(){
        //입력된 정보를 취합해서 서버 전송

        //필터링. (gender, year, agree terms)
        if(!btnCheckTerms.isChecked()){ MHDDialogUtil.sAlert(mContext, R.string.alert_not_agree_terms); return; }
        if(!btnCheckPrivacy.isChecked()){ MHDDialogUtil.sAlert(mContext, R.string.alert_not_agree_privacy); return; }
        if(!btnCheckGps.isChecked()){ MHDDialogUtil.sAlert(mContext, R.string.alert_not_agree_gps); return; }
        if(et_join_name.getText().toString().length() == 0) { MHDDialogUtil.sAlert(mContext, R.string.alert_not_name); return; }
        if(et_join_id.getText().toString().length() == 0) { MHDDialogUtil.sAlert(mContext, R.string.alert_not_id); return; }
        if(et_join_pwd.getText().toString().length() == 0) { MHDDialogUtil.sAlert(mContext, R.string.alert_not_pwd); return; }

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
            //params.put("UUID", MHDApplication.getInstance().getMHDSvcManager().getDeviceNewUuid());
            params.put("UUPN", Util.getInstance().getPhoneNumber(mContext));
            params.put("UUOS", Integer.toString(Build.VERSION.SDK_INT));
            params.put("UUDEVICE", Build.MODEL);
            //params.put("UUTOKEN", MHDApplication.getInstance().getMHDSvcManager().getFcmToken());
            params.put("UUAPP", MHDApplication.getInstance().getAppVersion());
            params.put("UUNAME", et_join_name.getText().toString());
            params.put("UUID", et_join_id.getText().toString());
            params.put("UUPWD", et_join_pwd.getText().toString());
            MHDNetworkInvoker.getInstance().sendVolleyRequest(mContext, R.string.url_restapi_regist_member, params, responseListener);

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
            // 회원가입 성공. user vo 를 구성하고
            MHDLog.d(TAG, "networkResponseProcess nvMsg >>> " + nvMsg);

            // 기존 사용자인 경우.
            // vo 및 각종 변수에 저장하고 메인으로 넘긴다. 레코드가 하나여도 jsonarray 로 보내니 gson에서 에러가 나드라.
            Gson gson = new Gson();
            UserVo userVo;
            userVo = gson.fromJson(nvMsg, UserVo.class);
            MHDApplication.getInstance().getMHDSvcManager().setUserVo(null);
            MHDApplication.getInstance().getMHDSvcManager().setUserVo(userVo);

            if(MHDApplication.getInstance().getMHDSvcManager().getUserVo() != null){
                // MainActivity 로 이동
                goMain();
            }
            // 학생을 등록하겠느냐는 컨펌창을 띄우고 확인 -> 등록창, 취소 -> 메인
            MHDDialogUtil.sAlert(this, R.string.alert_join_success, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // 학생 등록창으로 이동.
                }
            }, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // 메인으로 이동. 아무 내용 안나올 것.
                }
            });
        }

        return true;
    }
}
