package com.mhd.stard.activity;

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

import androidx.appcompat.widget.AppCompatButton;

import com.mhd.stard.R;
import com.mhd.stard.common.MHDApplication;
import com.mhd.stard.common.vo.MenuVo;
import com.mhd.stard.common.vo.SelfVo;
import com.mhd.stard.network.MHDNetworkInvoker;
import com.mhd.stard.util.MHDDialogUtil;
import com.mhd.stard.util.MHDLog;

import java.util.HashMap;
import java.util.Map;

public class ModifySelfActivity extends BaseActivity {
    TextView vst_top_title;
    EditText et_self_title_1, et_self_title_2, et_self_title_3;
    AppCompatButton btn_move_stat_left;
    /* 수정 처리 */
    String dataIndex = "";
    LinearLayout ll_self_comment;
    ImageView vst_right_image;
    /* 수정 처리 */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initialize(R.layout.activity_self_regist);
        mContext = ModifySelfActivity.this;

        /* 수정 처리 */
        Intent intent = getIntent();
        int itemPosition = intent.getIntExtra("position", -1);
        SelfVo mSelfVo = MHDApplication.getInstance().getMHDSvcManager().getSelfVo();
        dataIndex = mSelfVo.getMsg().get(itemPosition).getIdx();

        if(itemPosition == -1){
            //비정상적인 접근
            Toast.makeText(mContext, R.string.text_never, Toast.LENGTH_SHORT).show();
            finish();
        }

        ll_self_comment = (LinearLayout) findViewById(R.id.ll_self_comment);
        ll_self_comment.setVisibility(View.GONE);
        /* 수정 처리 */

        et_self_title_1 = (EditText) findViewById(R.id.et_self_title_1);
        et_self_title_1.setText(mSelfVo.getMsg().get(itemPosition).getTbtitle());
        et_self_title_2 = (EditText) findViewById(R.id.et_self_title_2);
        et_self_title_2.setVisibility(View.GONE);
        et_self_title_3 = (EditText) findViewById(R.id.et_self_title_3);
        et_self_title_3.setVisibility(View.GONE);
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
        vst_top_title.setText("["+displayKid+"] "+ getString(R.string.title_self_modify));

        /* 수정 처리 */
        vst_right_image = (ImageView) findViewById(R.id.vst_right_image);
        vst_right_image.setVisibility(View.VISIBLE);
        vst_right_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MHDDialogUtil.sAlert(ModifySelfActivity.this, R.string.confirm_delete, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteSelf(mSelfVo.getMsg().get(itemPosition).getIdx());
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
        String tmpSelf_1 = et_self_title_1.getText().toString();
        String tmpSelf_2 = et_self_title_2.getText().toString();
        String tmpSelf_3 = et_self_title_3.getText().toString();
        tmpSelf_1 = (tmpSelf_1==null || "".equals(tmpSelf_1)) ? "" : tmpSelf_1;
        tmpSelf_2 = (tmpSelf_2==null || "".equals(tmpSelf_2)) ? "" : tmpSelf_2;
        tmpSelf_3 = (tmpSelf_3==null || "".equals(tmpSelf_3)) ? "" : tmpSelf_3;

        if ((tmpSelf_1==null || "".equals(tmpSelf_1))) {
            Toast.makeText(mContext, getString(R.string.content_self_modify), Toast.LENGTH_SHORT).show();
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
                params.put("IDX", dataIndex);
                MHDNetworkInvoker.getInstance().sendVolleyRequest(mContext, R.string.url_restapi_modify_self, params, responseListener);
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
            if(nvApi.equals(getString(R.string.restapi_modify_self)) || nvApi.equals(getString(R.string.restapi_delete_self))){
                if (nvCnt == 0) {
                    // 정보가 없으면 비정상
                    // 우선 toast를 띄울 것.
                    Toast.makeText(mContext, nvMsg, Toast.LENGTH_SHORT).show();
                } else {
                    // Self 정상등록 여부를 알림
                    MHDDialogUtil.sAlert(mContext, R.string.alert_modify_server, new DialogInterface.OnClickListener() {
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

    private void deleteSelf(String sIndex){
        // db index 값 받아서 넘기면서 바로 삭제 처리
        try {
            Map<String, String> params = new HashMap<String, String>();
            //params.put("UUID", MHDApplication.getInstance().getMHDSvcManager().getDeviceNewUuid());
            params.put("UUMAIL", MHDApplication.getInstance().getMHDSvcManager().getUserVo().getUuMail());
            params.put("IDX", sIndex);

            MHDNetworkInvoker.getInstance().sendVolleyRequest(mContext, R.string.url_restapi_delete_self, params, responseListener);
        } catch (Exception e) {
            MHDLog.printException(e);
        }
    }
}
