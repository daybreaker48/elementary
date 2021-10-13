package com.mhd.elemantary.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import com.mhd.elemantary.R;
import com.mhd.elemantary.network.manager.ResponseListener;
import com.mhd.elemantary.util.MHDDialogUtil;
import com.mhd.elemantary.util.MHDLog;

import org.json.JSONObject;

import androidx.annotation.Nullable;
import androidx.preference.PreferenceFragmentCompat;

public class PreferenceCustomFragment extends PreferenceFragmentCompat {
    public final String TAG = getClass().getName();
    protected Context mContext;
    /**
     * Fragment network Listener
     * */
    public ResponseListener fmresponseListener;
    /**
     * fragment network variables
     * */
    public String nvFmResultCode = "";
    public String nvFmApi = "";
    public int nvFmCnt = 0;
    public String nvFmMsg = "";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mContext = getActivity();

        // Activity volley 통신 ResponseListner 등록
        // 리턴하는 데이터에서 request 정보를 담아와서 처리 분기한다.
        fmresponseListener = new ResponseListener() {
            @Override
            public void onError(String message) {
                MHDLog.e(TAG, "ResponseListener fragment onError >>>>>>>>>> " + message.toString());
                MHDDialogUtil.sAlert(mContext, R.string.alert_networkRequestError, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) { return; }
                });
                return;
            }
            @Override
            public void onResponse(String response) {
                MHDLog.d(TAG, "ResponseListener fragment onResponse >>>>>>>>>> " + response.toString());
//                MHDDialogUtil.sAlert(mContext, R.string.alert_networkRequestSuccess, new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) { return; }
//                });

                networkResponseProcessForFragment(response);
                return;
            }
        };
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.setting_preference, rootKey);
    }

    /**
     * Fragment volley listner 의 success response 처리.
     * 필요한 곳에서 override 해서 구현.
     * 기본적인, 주요 Error 처리들은 MHDNetworkInvoker 에서 처리하고
     * 이 곳에서는 result "S" 인 성공 처리. "S" 라는 것은 서버에서 응답을 정상적으로 했다라는 의미.
     */
    public boolean networkResponseProcessForFragment(String result) {
        MHDLog.d(TAG, "networkResponseProcessm fragment >>> " + result);

        boolean resultFlag = false;
        try{
            JSONObject nvJsonObject = new JSONObject(result);
            // 결과코드. 여기로 왔다는 건 M, S(Success) 라는 것.
            nvFmResultCode = nvJsonObject.getString("result");
            String nvData = nvJsonObject.getString("data");
            JSONObject nvJsonDataObject = new JSONObject(nvData);

            nvFmApi = nvJsonDataObject.getString("api");
            nvFmMsg = nvJsonDataObject.getString("msg");
            if("S".equals(nvFmResultCode)){  // Success. data 필드가 있고 그 안에 api, (cnt), msg 필드가 있다.
                nvFmCnt = nvJsonDataObject.getInt("cnt");
            }
            resultFlag = true;
        } catch (Exception e) {
            MHDLog.printException(e);
            resultFlag = false;
        } finally {
            return resultFlag;
        }
    }

//    @Override
//    public int getLayoutResId() {
//        return R.layout.fragment_setting;
//    }
//
//    @Override
//    public void inOnCreateView(View root, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        //Statusbar 아래로 내리기
////        final TextView mTitle = (TextView) root.findViewById(R.id.vst_top_title);
////        RelativeLayout.LayoutParams mLayoutParams = (RelativeLayout.LayoutParams) mTitle.getLayoutParams();
////        mLayoutParams.topMargin = Util.getInstance().getStatusBarHeight(root.getContext());
////        mTitle.setLayoutParams(mLayoutParams);
//    }
//
//    @Override
//    public void batchFunction(String api) {
////        if(api.equals(getString(R.string.api_editor_clear))) {
////            // editor 내용 초기화.
////            editor.clearAllContents();
////        }
//    }
}

