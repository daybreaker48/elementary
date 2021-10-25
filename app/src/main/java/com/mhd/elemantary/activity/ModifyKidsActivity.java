package com.mhd.elemantary.activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.mhd.elemantary.R;
import com.mhd.elemantary.common.MHDApplication;
import com.mhd.elemantary.common.vo.KidsVo;
import com.mhd.elemantary.network.MHDNetworkInvoker;
import com.mhd.elemantary.util.MHDDialogUtil;
import com.mhd.elemantary.util.MHDLog;

import java.util.HashMap;
import java.util.Map;

import androidx.appcompat.widget.AppCompatButton;

public class ModifyKidsActivity extends BaseActivity {
    TextView vst_top_title;
    EditText et_kids_name_1, et_kids_name_2, et_kids_name_3;
    EditText et_kids_age_1, et_kids_age_2, et_kids_age_3;
    LinearLayout ll_regist_kids_2, ll_regist_kids_3, ll_kids_comment;
    ImageView iv_kids_add_button, iv_kids_del_button_1, iv_kids_del_button_2;
    AppCompatButton btn_move_stat_left;
    /* 수정 처리 */
    String dataIndex = "";
    ImageView vst_right_image;
    /* 수정 처리 */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initialize(R.layout.activity_kids_regist);
        mContext = ModifyKidsActivity.this;

        /* 수정 처리 */
        Intent intent = getIntent();
        int itemPosition = intent.getIntExtra("position", -1);
        KidsVo mKidsVo = MHDApplication.getInstance().getMHDSvcManager().getKidsVo();
        dataIndex = mKidsVo.getMsg().get(itemPosition).getIdx();

        if(itemPosition == -1){
            //비정상적인 접근
            Toast.makeText(mContext, R.string.text_never, Toast.LENGTH_SHORT).show();
            finish();
        }
        /* 수정 처리 */

        btn_move_stat_left = (AppCompatButton) findViewById(R.id.btn_move_stat_left);
        btn_move_stat_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        ll_regist_kids_2 = (LinearLayout) findViewById(R.id.ll_regist_kids_2);
        ll_regist_kids_3 = (LinearLayout) findViewById(R.id.ll_regist_kids_3);
        ll_kids_comment = (LinearLayout) findViewById(R.id.ll_kids_comment);
        iv_kids_add_button = (ImageView) findViewById(R.id.iv_kids_add_button);
        /* 수정 처리 */
        ll_kids_comment.setVisibility(View.GONE);
        iv_kids_add_button.setVisibility(View.GONE);
        /* 수정 처리 */
        iv_kids_del_button_1 = (ImageView) findViewById(R.id.iv_kids_del_button_1);
        iv_kids_del_button_2 = (ImageView) findViewById(R.id.iv_kids_del_button_2);

        et_kids_name_1 = (EditText) findViewById(R.id.et_kids_name_1);
        /* 수정 처리 */
        et_kids_name_1.setText(mKidsVo.getMsg().get(itemPosition).getName());
        /* 수정 처리 */
        et_kids_name_2 = (EditText) findViewById(R.id.et_kids_name_2);
        et_kids_name_3 = (EditText) findViewById(R.id.et_kids_name_3);
        et_kids_age_1 = (EditText) findViewById(R.id.et_kids_age_1);
        /* 수정 처리 */
        et_kids_age_1.setText(mKidsVo.getMsg().get(itemPosition).getAge());
        /* 수정 처리 */
        et_kids_age_2 = (EditText) findViewById(R.id.et_kids_age_2);
        et_kids_age_3 = (EditText) findViewById(R.id.et_kids_age_3);

        iv_kids_add_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ll_regist_kids_2.getVisibility() == View.GONE){
                    ll_regist_kids_2.setVisibility(View.VISIBLE);
                }else if(ll_regist_kids_3.getVisibility() == View.GONE){
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
        vst_top_title.setText(R.string.title_kids_modify);

        /* 수정 처리 */
        vst_right_image = (ImageView) findViewById(R.id.vst_right_image);
        vst_right_image.setVisibility(View.VISIBLE);
        vst_right_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MHDDialogUtil.sAlert(ModifyKidsActivity.this, R.string.confirm_delete, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteKids(mKidsVo.getMsg().get(itemPosition).getIdx());
                    }
                }, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
            }
        });
        /* 수정 처리 */
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

        /* 수정 처리 */
        if ("".equals(et_kids_name_1_value) && "".equals(et_kids_name_2_value) && "".equals(et_kids_name_3_value)) {
            Toast.makeText(mContext, getString(R.string.content_kids_name), Toast.LENGTH_SHORT).show();
        }else if("".equals(et_kids_name_1_value) && !"".equals(et_kids_age_1_value)){
            Toast.makeText(mContext, "수정할 아이 이름을 입력해주세요.", Toast.LENGTH_SHORT).show();
        }else if(!"".equals(et_kids_name_1_value) && "".equals(et_kids_age_1_value)){
            Toast.makeText(mContext, "수정할 아이 나이를 입력해주세요.", Toast.LENGTH_SHORT).show();
        }else{
            try {
                // Map 방식 0
                Map<String, String> params = new HashMap<String, String>();
                params.put("UUMAIL", MHDApplication.getInstance().getMHDSvcManager().getUserVo().getUuMail());
                params.put("KIDNAME1", et_kids_name_1_value);
                params.put("KIDAGE1", et_kids_age_1_value);
                params.put("IDX", dataIndex);
                MHDNetworkInvoker.getInstance().sendVolleyRequest(mContext, R.string.url_restapi_modify_kids, params, responseListener);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                MHDLog.printException(e);
            }
        }
        /* 수정 처리 */
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
            if(nvApi.equals(getString(R.string.restapi_modify_kids)) || nvApi.equals(getString(R.string.restapi_delete_kids))){
                if (nvCnt == 0) {
                    // 정보가 없으면 비정상
                    // 우선 toast를 띄울 것.
                    Toast.makeText(mContext, nvMsg, Toast.LENGTH_SHORT).show();
                } else {
                    Gson gson = new Gson();
                    KidsVo kidsVo;
                    kidsVo = gson.fromJson(nvJsonDataString, KidsVo.class);
                    MHDApplication.getInstance().getMHDSvcManager().setKidsVo(null);
                    MHDApplication.getInstance().getMHDSvcManager().setKidsVo(kidsVo);

                    // Self 정상등록 여부를 알림
                    MHDDialogUtil.sAlert(mContext, R.string.alert_networkRequestSuccess, new DialogInterface.OnClickListener() {
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

    private void deleteKids(String sIndex){
        // db index 값 받아서 넘기면서 바로 삭제 처리
        try {
            Map<String, String> params = new HashMap<String, String>();
            //params.put("UUID", MHDApplication.getInstance().getMHDSvcManager().getDeviceNewUuid());
            params.put("UUMAIL", MHDApplication.getInstance().getMHDSvcManager().getUserVo().getUuMail());
            params.put("IDX", sIndex);

            MHDNetworkInvoker.getInstance().sendVolleyRequest(mContext, R.string.url_restapi_delete_kids, params, responseListener);
        } catch (Exception e) {
            MHDLog.printException(e);
        }
    }
}
