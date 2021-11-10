package com.mhd.stard.activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatButton;

import com.mhd.stard.R;
import com.mhd.stard.common.MHDApplication;
import com.mhd.stard.common.vo.MenuVo;
import com.mhd.stard.network.MHDNetworkInvoker;
import com.mhd.stard.util.MHDDialogUtil;
import com.mhd.stard.util.MHDLog;

import java.util.HashMap;
import java.util.Map;

public class RegistSelfActivity extends BaseActivity {
    TextView vst_top_title;
    EditText et_self_title_1, et_self_title_2, et_self_title_3;
    AppCompatButton btn_move_stat_left;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initialize(R.layout.activity_self_regist);
        mContext = RegistSelfActivity.this;

        overridePendingTransition(R.anim.vertical_in, R.anim.none);

        et_self_title_1 = (EditText) findViewById(R.id.et_self_title_1);
        et_self_title_2 = (EditText) findViewById(R.id.et_self_title_2);
        et_self_title_3 = (EditText) findViewById(R.id.et_self_title_3);
        btn_move_stat_left = (AppCompatButton) findViewById(R.id.btn_move_stat_left);

        AppCompatButton btn_todo_cancel = (AppCompatButton) findViewById(R.id.btn_todo_cancel);
        btn_todo_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { finish(); }
        });
        AppCompatButton btn_todo_save = (AppCompatButton) findViewById(R.id.btn_todo_save);
        btn_todo_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // currentRadio(F, P, G)에 따라 필수입력값 체크, 문제가 없다면 서버 전송.
                saveProcess();
            }
        });
        btn_move_stat_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { finish(); }
        });

        vst_top_title = (TextView) findViewById(R.id.vst_top_title);
        MenuVo menuVo = MHDApplication.getInstance().getMHDSvcManager().getMenuVo();
        String displayKid = "";
        for(int k=0; k<menuVo.getMsg().size(); k++){
            if("SE".equals(menuVo.getMsg().get(k).getMenuname())){
                // 해당메뉴에 설정된 아이정보
                displayKid = menuVo.getMsg().get(k).getKidname();
            }
        }
        vst_top_title.setText("[ "+displayKid+" ] "+ getString(R.string.title_self_regist));
    }

    private void saveProcess() {
        String tmpSelf_1 = et_self_title_1.getText().toString();
        String tmpSelf_2 = et_self_title_2.getText().toString();
        String tmpSelf_3 = et_self_title_3.getText().toString();
        tmpSelf_1 = (tmpSelf_1==null || "".equals(tmpSelf_1)) ? "" : tmpSelf_1;
        tmpSelf_2 = (tmpSelf_2==null || "".equals(tmpSelf_2)) ? "" : tmpSelf_2;
        tmpSelf_3 = (tmpSelf_3==null || "".equals(tmpSelf_3)) ? "" : tmpSelf_3;

        if ((tmpSelf_1==null || "".equals(tmpSelf_1)) && (tmpSelf_2==null || "".equals(tmpSelf_2)) && (tmpSelf_3==null || "".equals(tmpSelf_3))) {
            Toast.makeText(mContext, getString(R.string.content_self_hint_1), Toast.LENGTH_SHORT).show();
        }else{
            try {
                MenuVo menuVo = MHDApplication.getInstance().getMHDSvcManager().getMenuVo();
                String dKid = "";
                for(int k=0; k<menuVo.getMsg().size(); k++){
                    if("SE".equals(menuVo.getMsg().get(k).getMenuname())){
                        // 해당메뉴에 설정된 아이정보
                        dKid = menuVo.getMsg().get(k).getKidname();
                    }
                }
                // Map 방식 0
                Map<String, String> params = new HashMap<String, String>();
                params.put("UUMAIL", MHDApplication.getInstance().getMHDSvcManager().getUserVo().getUuMail());
                params.put("TKNAME", dKid);
                params.put("SFTITLE_1", tmpSelf_1);
                params.put("SFTITLE_2", tmpSelf_2);
                params.put("SFTITLE_3", tmpSelf_3);
                MHDNetworkInvoker.getInstance().sendVolleyRequest(mContext, R.string.url_restapi_regist_self, params, responseListener);
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

        // resultFlag 이 true 라면 현재 여기에 필요한 data 들이 전역에 들어가 있는 상태.

        if("M".equals(nvResultCode)){
            // Just show nvMsg
            MHDDialogUtil.sAlert(mContext, nvMsg);
            return true;
        }else if("S".equals(nvResultCode)){
            if(nvApi.equals(getString(R.string.restapi_regist_self))){
                if (nvCnt == 0) {
                    // 정보가 없으면 비정상
                    // 우선 toast를 띄울 것.
                    Toast.makeText(mContext, nvMsg, Toast.LENGTH_SHORT).show();
                } else {
                    // Self 정상등록 여부를 알림
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
