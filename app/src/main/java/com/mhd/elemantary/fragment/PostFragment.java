package com.mhd.elemantary.fragment;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ScrollView;

import com.mhd.elemantary.MainActivity;
import com.mhd.elemantary.R;
import com.mhd.elemantary.network.MHDNetworkInvoker;
import com.mhd.elemantary.util.MHDDialogUtil;
import com.mhd.elemantary.util.MHDLog;

import java.util.HashMap;
import java.util.Map;


public class PostFragment extends BaseFragment {

    private Button btnSave;
    public com.github.irshulx.Editor editor;

    public static PostFragment create() {
        return new PostFragment();
    }

    @Override
    public int getLayoutResId() {
        return R.layout.fragment_post;
    }

    @Override
    public void inOnCreateView(View root, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        MHDLog.d("dagian", "PostFragment inOnCreateView");
//        //Statusbar 아래로 내리기
//        final TextView mTitle = (TextView) root.findViewById(R.id.vst_top_title);
//        RelativeLayout.LayoutParams mLayoutParams = (RelativeLayout.LayoutParams) mTitle.getLayoutParams();
//        mLayoutParams.topMargin = Util.getInstance().getStatusBarHeight(root.getContext());
//        mTitle.setLayoutParams(mLayoutParams);

//        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN | WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        ScrollView scrollView = (ScrollView) root.findViewById(R.id.svEditor);
        btnSave = (Button) root.findViewById(R.id.btn_save_post);
        editor = (com.github.irshulx.Editor) root.findViewById(R.id.editor);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MHDDialogUtil.sAlert(mContext, R.string.confirm_post, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        savePost();
                    }
                }, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) { }
                });
            }
        });

        scrollView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                v.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });
    }

    @Override
    public void onDestroyView() {
        MHDLog.d("dagian","PostFragment onDestroyView");
        super.onDestroyView();
    }

    private void savePost(){
        //입력된 Text(우선)를 취합해서 서버 전송

        //필터링. (gender, year, agree terms)
        String postText = editor.getContentAsHTML();
        if(postText.length() == 0){ MHDDialogUtil.sAlert(mContext, R.string.alert_empty_post); return; }

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
            params.put("UUPOST", editor.getContentAsHTML());
            MHDNetworkInvoker.getInstance().sendVolleyRequest(mContext, R.string.url_restapi_regist_post, params, fmresponseListener);
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
    public void batchFunction(String api) {
//        if(api.equals(getString(R.string.api_editor_clear))) {
//            // editor 내용 초기화.
//            editor.clearAllContents();
//        }
    }

    /**
     * Fragment volley listner 의 success response 처리.
     * 필요한 곳에서 override 해서 구현.
     * 대부분의 Fragment 처리를 여기서?
     */
    @Override
    public boolean networkResponseProcessForFragment(String result) {
        boolean resultFlag = super.networkResponseProcessForFragment(result);

        MHDLog.d(TAG, "networkResponseProcess fragment resultFlag >>> " + resultFlag);

        if(!resultFlag) return resultFlag;

        // resultFlag 이 true 라면 현재 여기에 필요한 data 들이 전역에 들어가 있는 상태.

        if("M".equals(nvFmResultCode)){
            // Just show nvMsg
            MHDDialogUtil.sAlert(mContext, nvFmMsg);
            return true;
        }else if("S".equals(nvFmResultCode)){
            if(nvFmApi.equals(mContext.getString(R.string.restapi_regist_post))){
                editor.clearAllContents();

                // regist post
                MHDLog.d(TAG, "networkResponseProcess fragment nvMsg >>> " + nvFmMsg);

                // 목록 화면으로 이동.
                ((MainActivity)getActivity()).viewPager.setCurrentItem(1);
            }else if(nvFmApi.equals(mContext.getString(R.string.restapi_regist_post))){

            }
        }

        return true;
    }
}
