package com.mhd.stard.activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatButton;

import com.google.gson.Gson;
import com.mhd.stard.R;
import com.mhd.stard.common.MHDApplication;
import com.mhd.stard.common.vo.KidsVo;
import com.mhd.stard.network.MHDNetworkInvoker;
import com.mhd.stard.util.MHDDialogUtil;
import com.mhd.stard.util.MHDLog;

import java.util.HashMap;
import java.util.Map;

public class RegistKidsActivity extends BaseActivity {
    TextView vst_top_title;
    EditText et_kids_name_1, et_kids_name_2, et_kids_name_3;
    EditText et_kids_age_1, et_kids_age_2, et_kids_age_3;
    LinearLayout ll_regist_kids_2, ll_regist_kids_3;
    ImageView iv_kids_add_button, iv_kids_del_button_1, iv_kids_del_button_2;
    AppCompatButton btn_move_stat_left;
    int kidsCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initialize(R.layout.activity_kids_regist);
        mContext = RegistKidsActivity.this;

        overridePendingTransition(R.anim.vertical_in, R.anim.none);

        if(MHDApplication.getInstance().getMHDSvcManager().getKidsVo() != null) {
            KidsVo kidsVo = MHDApplication.getInstance().getMHDSvcManager().getKidsVo();
            kidsCount = kidsVo == null ? 0 : kidsVo.getCnt();
        }

        btn_move_stat_left = (AppCompatButton) findViewById(R.id.btn_move_stat_left);
        btn_move_stat_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        ll_regist_kids_2 = (LinearLayout) findViewById(R.id.ll_regist_kids_2);
        ll_regist_kids_3 = (LinearLayout) findViewById(R.id.ll_regist_kids_3);
        iv_kids_add_button = (ImageView) findViewById(R.id.iv_kids_add_button);
        iv_kids_del_button_1 = (ImageView) findViewById(R.id.iv_kids_del_button_1);
        iv_kids_del_button_2 = (ImageView) findViewById(R.id.iv_kids_del_button_2);

        et_kids_name_1 = (EditText) findViewById(R.id.et_kids_name_1);
        et_kids_name_2 = (EditText) findViewById(R.id.et_kids_name_2);
        et_kids_name_3 = (EditText) findViewById(R.id.et_kids_name_3);
        et_kids_age_1 = (EditText) findViewById(R.id.et_kids_age_1);
        et_kids_age_2 = (EditText) findViewById(R.id.et_kids_age_2);
        et_kids_age_3 = (EditText) findViewById(R.id.et_kids_age_3);

        iv_kids_add_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(kidsCount < 5 && ll_regist_kids_2.getVisibility() == View.GONE){
                    ll_regist_kids_2.setVisibility(View.VISIBLE);
                }else if(kidsCount < 4 && ll_regist_kids_3.getVisibility() == View.GONE){
                    ll_regist_kids_3.setVisibility(View.VISIBLE);
                }else{
                    Toast.makeText(mContext, getString(R.string.content_kids_limit), Toast.LENGTH_SHORT).show();
                }
            }
        });
        iv_kids_del_button_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                et_kids_name_2.setText("");
                et_kids_age_2.setText("");
                ll_regist_kids_2.setVisibility(View.GONE);
            }
        });
        iv_kids_del_button_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                et_kids_name_3.setText("");
                et_kids_age_3.setText("");
                ll_regist_kids_3.setVisibility(View.GONE);
            }
        });

        AppCompatButton btn_todo_cancel = (AppCompatButton) findViewById(R.id.btn_todo_cancel);
        btn_todo_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(Activity.RESULT_CANCELED);
                finish();
            }
        });
        AppCompatButton btn_todo_save = (AppCompatButton) findViewById(R.id.btn_todo_save);
        btn_todo_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveProcess();
            }
        });

        vst_top_title = (TextView) findViewById(R.id.vst_top_title);
        vst_top_title.setText(R.string.title_kids_regist);
    }

    private void saveProcess() {
        String et_kids_name_1_value = et_kids_name_1.getText().toString();
        String et_kids_name_2_value = et_kids_name_2.getText().toString();
        String et_kids_name_3_value = et_kids_name_3.getText().toString();
        String et_kids_age_1_value = et_kids_age_1.getText().toString();
        String et_kids_age_2_value = et_kids_age_2.getText().toString();
        String et_kids_age_3_value = et_kids_age_3.getText().toString();

        et_kids_name_1_value = (et_kids_name_1_value==null || "".equals(et_kids_name_1_value)) ? "" : et_kids_name_1_value;
        et_kids_name_2_value = (et_kids_name_2_value==null || "".equals(et_kids_name_2_value)) ? "" : et_kids_name_2_value;
        et_kids_name_3_value = (et_kids_name_3_value==null || "".equals(et_kids_name_3_value)) ? "" : et_kids_name_3_value;
        et_kids_age_1_value = (et_kids_age_1_value==null || "".equals(et_kids_age_1_value)) ? "" : et_kids_age_1_value;
        et_kids_age_2_value = (et_kids_age_2_value==null || "".equals(et_kids_age_2_value)) ? "" : et_kids_age_2_value;
        et_kids_age_3_value = (et_kids_age_3_value==null || "".equals(et_kids_age_3_value)) ? "" : et_kids_age_3_value;

        if ("".equals(et_kids_name_1_value) && "".equals(et_kids_name_2_value) && "".equals(et_kids_name_3_value)) {
            Toast.makeText(mContext, getString(R.string.content_kids_name), Toast.LENGTH_SHORT).show();
        }else if("".equals(et_kids_name_1_value) && !"".equals(et_kids_age_1_value)){
            Toast.makeText(mContext, "??? ?????? ?????? ????????? ??????????????????.", Toast.LENGTH_SHORT).show();
        }else if(!"".equals(et_kids_name_1_value) && "".equals(et_kids_age_1_value)){
            Toast.makeText(mContext, "??? ?????? ?????? ????????? ??????????????????.", Toast.LENGTH_SHORT).show();
        }else if(ll_regist_kids_2.getVisibility() == View.VISIBLE && "".equals(et_kids_name_2_value) && !"".equals(et_kids_age_1_value)){
            Toast.makeText(mContext, "??? ?????? ?????? ????????? ??????????????????.", Toast.LENGTH_SHORT).show();
        }else if(ll_regist_kids_2.getVisibility() == View.VISIBLE && !"".equals(et_kids_name_2_value) && "".equals(et_kids_age_1_value)){
            Toast.makeText(mContext, "??? ?????? ?????? ????????? ??????????????????.", Toast.LENGTH_SHORT).show();
        }else if(ll_regist_kids_3.getVisibility() == View.VISIBLE && "".equals(et_kids_name_3_value) && !"".equals(et_kids_age_1_value)){
            Toast.makeText(mContext, "??? ?????? ?????? ????????? ??????????????????.", Toast.LENGTH_SHORT).show();
        }else if(ll_regist_kids_3.getVisibility() == View.VISIBLE && !"".equals(et_kids_name_3_value) && "".equals(et_kids_age_1_value)){
            Toast.makeText(mContext, "??? ?????? ?????? ????????? ??????????????????.", Toast.LENGTH_SHORT).show();
        }else{
            try {
                // Map ?????? 0
                Map<String, String> params = new HashMap<String, String>();
                params.put("UUMAIL", MHDApplication.getInstance().getMHDSvcManager().getUserVo().getUuMail());
                params.put("KIDNAME1", et_kids_name_1_value);
                params.put("KIDAGE1", et_kids_age_1_value);
                params.put("KIDNAME2", et_kids_name_2_value);
                params.put("KIDAGE2", et_kids_age_2_value);
                params.put("KIDNAME3", et_kids_name_3_value);
                params.put("KIDAGE3", et_kids_age_3_value);
                MHDNetworkInvoker.getInstance().sendVolleyRequest(mContext, R.string.url_restapi_regist_kids, params, responseListener);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                MHDLog.printException(e);
            }
        }
    }

    @Override
    protected boolean networkResponseProcess(String result) {
        boolean resultFlag = super.networkResponseProcess(result);
        MHDLog.d(TAG, "networkResponseProcess resultFlag >>> " + resultFlag);
        if(!resultFlag) return resultFlag;

        // resultFlag ??? true ?????? ?????? ????????? ????????? data ?????? ????????? ????????? ?????? ??????.
        if("M".equals(nvResultCode)){
            // Just show nvMsg
            MHDDialogUtil.sAlert(mContext, nvMsg);
            return true;
        }else if("S".equals(nvResultCode)){
            if(nvApi.equals(getString(R.string.restapi_regist_kids))){
                if (nvCnt == 0) {
                    // ????????? ????????? ?????????
                    // ?????? toast??? ?????? ???.
                    Toast.makeText(mContext, nvMsg, Toast.LENGTH_SHORT).show();
                } else {
                    Gson gson = new Gson();
                    KidsVo kidsVo;
                    kidsVo = gson.fromJson(nvJsonDataString, KidsVo.class);
                    MHDApplication.getInstance().getMHDSvcManager().setKidsVo(null);
                    MHDApplication.getInstance().getMHDSvcManager().setKidsVo(kidsVo);

                    // Self ???????????? ????????? ??????
                    MHDDialogUtil.sAlert(mContext, R.string.alert_regist_server, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            setResult(Activity.RESULT_OK);
                            finish();
                            return;
                        }
                    });
                }

                return true;
            }
        }

        return true;
    }
}
