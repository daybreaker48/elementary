package com.mhd.elemantary.activity;

import android.app.Dialog;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.NumberPicker;
import android.widget.TextView;

import com.mhd.elemantary.R;
import com.mhd.elemantary.common.MHDApplication;
import com.mhd.elemantary.constant.MHDConstants;
import com.mhd.elemantary.fragment.MemberInputFragment;
import com.mhd.elemantary.network.MHDNetworkInvoker;
import com.mhd.elemantary.util.MHDDialogUtil;
import com.mhd.elemantary.util.MHDLog;
import com.mhd.elemantary.util.Util;
import com.mhd.elemantary.webview.activity.HybridWebGuestActivity;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import androidx.annotation.Nullable;

public class LoginActivity extends BaseActivity {

    private Button btnStart;
    private CheckBox btnCheckTerms, btnCheckPrivacy, btnCheckGps;
    private int selectedYear = 0;
    private TextView btnTerms, btnPrivacy, btnGps;

    private String selectedSex = "M";

    public static MemberInputFragment create() {
        return new MemberInputFragment();
    }

    @Override
    public int getLayoutResId() {
        return R.layout.fragment_memberinput;
    }

    @Override
    public void inOnCreateView(View root, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        btnStart = (Button) root.findViewById(R.id.btn_start);
        btnCheckTerms = (CheckBox) root.findViewById(R.id.checkTerms);
        btnCheckPrivacy = (CheckBox) root.findViewById(R.id.checkPrivacy);
        btnCheckGps = (CheckBox) root.findViewById(R.id.checkGps);
        btnTerms = (TextView) root.findViewById(R.id.tv_terms_all_view);
        btnPrivacy = (TextView) root.findViewById(R.id.tv_privacy_all_view);
        btnGps = (TextView) root.findViewById(R.id.tv_gps_all_view);

        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                joinService();
            }
        });
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
    }

    private void showPicker(){
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);

        final Dialog yearDialog = new Dialog(getActivity());
        yearDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        yearDialog.setContentView(R.layout.picker_year);

        final NumberPicker numberPicker = (NumberPicker) yearDialog.findViewById(R.id.np_year_picker);
        final Button confirmBtn = (Button) yearDialog.findViewById(R.id.bt_confirm);
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

        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedYear = numberPicker.getValue();
                btnYear.setText(String.valueOf(selectedYear + " 년"));

                yearDialog.dismiss();
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
        if(selectedYear == 0){ MHDDialogUtil.sAlert(mContext, R.string.alert_not_select_year); return; }
        if(!btnCheckTerms.isChecked()){ MHDDialogUtil.sAlert(mContext, R.string.alert_not_agree_terms); return; }
        if(!btnCheckPrivacy.isChecked()){ MHDDialogUtil.sAlert(mContext, R.string.alert_not_agree_privacy); return; }
        if(!btnCheckGps.isChecked()){ MHDDialogUtil.sAlert(mContext, R.string.alert_not_agree_gps); return; }

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
            params.put("UUSEX", selectedSex);
            params.put("UUYEAR", Integer.toString(selectedYear));
            MHDNetworkInvoker.getInstance().sendVolleyRequest(mContext, R.string.url_restapi_regist_member, params, ((TutorialActivity)getActivity()).responseListener);

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
}
