package com.mhd.boomerang.service;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.mhd.boomerang.R;
import com.mhd.boomerang.common.MHDApplication;
import com.mhd.boomerang.common.MHDPreferences;
import com.mhd.boomerang.common.vo.UserVo;
import com.mhd.boomerang.network.MHDNetworkInvoker;
import com.mhd.boomerang.util.MHDLog;
import com.mhd.boomerang.util.Util;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by MH D on 2017-12-15.
 */

public class FCMInstanceIDService extends FirebaseInstanceIdService {
    private final String TAG = getClass().getName();

    @Override
    public void onTokenRefresh() {
//        super.onTokenRefresh();
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        MHDLog.d(TAG, "dagian token >>> " + refreshedToken);

        String token = MHDPreferences.getInstance().getPrefFcmToken();

        MHDApplication.getInstance().getMHDSvcManager().setFcmToken(refreshedToken);
        MHDPreferences.getInstance().savePrefFcmToken(refreshedToken);
        MHDApplication.getInstance().getMHDSvcManager().getUserVo().setUuToken(refreshedToken);

        sendTokenToServer(token, refreshedToken);
    }

    private void sendTokenToServer(String token, String newToken){
        try{
            // call service intro check
            // String 방식
//            StringBuilder fullParams = new StringBuilder("{");
//            fullParams.append("\"UUID\":\""+MHDPreferences.getInstance().getPrefDvcMmbrIdNo()+"\"")
//                    .append(",\"PAST\":\""+token+"\"")
//                    .append(",\"UUTOKEN\":\""+newToken+"\"")
//                    .append("}");

            // Map 방식 0
            Map<String, String> params = new HashMap<String, String>();
            params.put("UUID", MHDPreferences.getInstance().getPrefDvcMmbrIdNo());
            params.put("PAST", token);
            params.put("UUTOKEN", newToken);

            MHDNetworkInvoker.getInstance().sendVolleyRequest(this, R.string.url_restapi_fcmtoken, params, null);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            MHDLog.printException(e);
        }

    }
}
