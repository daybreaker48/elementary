package com.mhd.boomerang.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mhd.boomerang.R;
import com.mhd.boomerang.network.manager.ResponseListener;
import com.mhd.boomerang.util.MHDDialogUtil;
import com.mhd.boomerang.util.MHDLog;

import org.json.JSONObject;


public abstract class BaseFragment extends Fragment {

    public final String TAG = getClass().getName();
    private View mRoot;
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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRoot = inflater.inflate(getLayoutResId(), container, false);
        inOnCreateView(mRoot, container, savedInstanceState);
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

        return mRoot;
    }

    @LayoutRes
    public abstract int getLayoutResId();

    public abstract void inOnCreateView(View root, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState);

    public abstract void batchFunction(String api);

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
}
