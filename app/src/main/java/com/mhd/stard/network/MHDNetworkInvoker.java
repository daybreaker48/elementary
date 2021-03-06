package com.mhd.stard.network;

import android.content.Context;
import android.content.DialogInterface;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.mhd.stard.R;
import com.mhd.stard.activity.BaseActivity;
import com.mhd.stard.common.CustomLoading;
import com.mhd.stard.common.MHDApplication;
import com.mhd.stard.network.manager.ResponseListener;
import com.mhd.stard.util.MHDDialogUtil;
import com.mhd.stard.util.MHDLog;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;


/**
 * Volley Network
 * Created by MH.D on 2017-04-21.
 */
public class MHDNetworkInvoker {

    private static final String TAG = MHDNetworkInvoker.class.getName();
    private static MHDNetworkInvoker INSTANCE = null;
    private RequestQueue queue;
    public static MHDNetworkInvoker getInstance() throws Exception {
        if(INSTANCE == null) {
            INSTANCE = new MHDNetworkInvoker();
            INSTANCE.initialize();
        }
        return INSTANCE;
    }
    private MHDNetworkInvoker() { }

    /**
     * initialize
     */
    public void initialize() throws Exception {

//        SSLConnect ssl = new SSLConnect();
//        ssl.postHttps("https://gslpis.gscaltext.com", 1000, 1000);
//        //queue = Volley.newRequestQueue(MHDApplication.getInstance().getApplicationContext(), new SslHttpStack(new SslHttpClient(MHDApplication.getInstance().getApplicationContext(), 443)));
//        //queue = Volley.newRequestQueue(MHDApplication.getInstance().getApplicationContext(), new HurlStack(null, newSslSocketFactory()));
        queue = Volley.newRequestQueue(MHDApplication.getInstance().getApplicationContext());
    }

//    private SSLSocketFactory newSslSocketFactory() {
//        try {
//            CertificateFactory cf;
//            cf = CertificateFactory.getInstance("X.509");
//            InputStream in = MHDApplication.getInstance().getApplicationContext().getResources().openRawResource(R.raw.gsc_cert);
//            Certificate ca = cf.generateCertificate(in);
//
////            in.close();
////            URL url = new URL("https://gslpis.gscaltex.com");
////            HttpsURLConnection urlConnection =
////                    (HttpsURLConnection)url.openConnection();
////            in = urlConnection.getInputStream();
////            in.close();
////
//            String keyStoreType = KeyStore.getDefaultType();
//            KeyStore keyStore = KeyStore.getInstance(keyStoreType);
//            keyStore.load(null, null);
//            keyStore.setCertificateEntry("ca", ca);// my question shows how to get 'ca'
//            TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
////// Initialise the TMF as you normally would, for example:
//            tmf.init(keyStore);
////
////            TrustManager[] trustManagers = tmf.getTrustManagers();
////            final X509TrustManager origTrustmanager = (X509TrustManager)trustManagers[0];
////
////            TrustManager[] wrappedTrustManagers = new TrustManager[]{
////                    new X509TrustManager() {
////                        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
////                            return origTrustmanager.getAcceptedIssuers();
////                        }
////
////                        public void checkClientTrusted(X509Certificate[] certs, String authType) {
////                            try {
////                                origTrustmanager.checkClientTrusted(certs, authType);
////                            } catch (CertificateException e) {
////                                e.printStackTrace();
////                            }
////                        }
////
////                        public void checkServerTrusted(X509Certificate[] certs, String authType) {
////                            try {
////                                origTrustmanager.checkServerTrusted(certs, authType);
////                            } catch (CertificateException e) {
////                                e.printStackTrace();
////                            }
////                        }
////                    }
////            };
////
//            //TrustManager[] wrappedTrustManagers = getWrappedTrustManagers(tmf.getTrustManagers());
//
//            SSLContext context = SSLContext.getInstance("TLS");
//            context.init(null, tmf.getTrustManagers(), null);
////            //HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
////            SSLSocketFactory sf = sc.getSocketFactory();
////            return sf;
//
////
////            // Load CAs from an InputStream
////            CertificateFactory cf = CertificateFactory.getInstance("X.509");
////// From https://www.washington.edu/itconnect/security/ca/load-der.crt
////            InputStream caInput = new BufferedInputStream(new FileInputStream("load-der.crt"));
////            Certificate ca;
////            try {
////                ca = cf.generateCertificate(caInput);
////                System.out.println("ca=" + ((X509Certificate) ca).getSubjectDN());
////            } finally {
////                caInput.close();
////            }
////
////// Create a KeyStore containing our trusted CAs
////            String keyStoreType = KeyStore.getDefaultType();
////            KeyStore keyStore = KeyStore.getInstance(keyStoreType);
////            keyStore.load(null, null);
////            keyStore.setCertificateEntry("ca", ca);
////
////// Create a TrustManager that trusts the CAs in our KeyStore
////            String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
////            TrustManagerFactory tmf = TrustManagerFactory.getInstance(tmfAlgorithm);
////            tmf.init(keyStore);
////
////// Create an SSLContext that uses our TrustManager
////            SSLContext context = SSLContext.getInstance("TLS");
////            context.init(null, tmf.getTrustManagers(), null);
////
////// Tell the URLConnection to use a SocketFactory from our SSLContext
////            URL url = new URL("https://certs.cac.washington.edu/CAtest/");
////            HttpsURLConnection urlConnection =
////                    (HttpsURLConnection)url.openConnection();
////            urlConnection.setSSLSocketFactory(context.getSocketFactory());
////            InputStream in = urlConnection.getInputStream();
////            copyInputStreamToOutputStream(in, System.out);
//
//
//
//
///*
//            // Get an instance of the Bouncy Castle KeyStore format
//            KeyStore trusted = KeyStore.getInstance("BKS");
//            // Get the raw resource, which contains the keystore with
//            // your trusted certificates (root and any intermediate certs)
//            InputStream in = MHDApplication.getInstance().getApplicationContext().getResources().openRawResource(R.raw.gsc_cert);
//            try {
//                // Initialize the keystore with the provided trusted certificates
//                // Provide the password of the keystore
//                trusted.load(null, null);
//            } finally {
//                in.close();
//            }
//
//            String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
//            TrustManagerFactory tmf = TrustManagerFactory.getInstance(tmfAlgorithm);
//            tmf.init(trusted);
//
//            SSLContext context = SSLContext.getInstance("TLS");
//            context.init(null, tmf.getTrustManagers(), null);*/
//
//            SSLSocketFactory sf = context.getSocketFactory();
//            return sf;
//        } catch (Exception e) {
//            throw new AssertionError(e);
//        }
//    }

//    private SSLSocketFactory newSslSocketFactory() {
//        try {
//            // Get an instance of the Bouncy Castle KeyStore format
//            KeyStore trusted = KeyStore.getInstance("BKS");
//            // Get the raw resource, which contains the keystore with
//            // your trusted certificates (root and any intermediate certs)
//            InputStream in = MHDApplication.getInstance().getApplicationContext().getResources().openRawResource(R.raw.gsc_cert);
//            try {
//                // Initialize the keystore with the provided trusted certificates
//                // Provide the password of the keystore
//                trusted.load(in, "gslpis".toCharArray());
//            } finally {
//                in.close();
//            }
//
//            String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
//            TrustManagerFactory tmf = TrustManagerFactory.getInstance(tmfAlgorithm);
//            tmf.init(trusted);
//
//            SSLContext context = SSLContext.getInstance("TLS");
//            context.init(null, tmf.getTrustManagers(), null);
//
//            SSLSocketFactory sf = context.getSocketFactory();
//            return sf;
//        } catch (Exception e) {
//            throw new AssertionError(e);
//        }
//    }

    public void sendVolleyRequest(Context context, final int ifID, final Map<String, String> paramsMap, final ResponseListener listener) throws JSONException {

        final Context mContext = context;
        final BaseActivity mBaseActivity = (BaseActivity)context;
        // uuid, ufcm ??? ???????????? ???????????? ???????????? ???. ???????????? ????????? ????????? ?????????????????? ?????????.
        // uuid ??? ????????? ufcm ????????? ????????? ??????.
        // uuid, ufcm ?????? ????????? ???????????? ?????? ??????????????? ????????? ????????? ??? ?????? ??????.
        // ?????? ????????? ?????? ????????? ??????. ??????????????? ???????????? ?????? ??????.
        final String userUuid = MHDApplication.getInstance().getMHDSvcManager().getDeviceNewUuid();
        final String userUfcm = MHDApplication.getInstance().getMHDSvcManager().getFcmToken();

        // ?????? ?????? ?????? ????????? ?????? ??????
        if((userUuid==null || "".equals(userUuid)) && (userUfcm==null || "".equals(userUfcm))){
            MHDDialogUtil.sAlert(context, R.string.alert_first_no_uuid, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    mBaseActivity.exitProcess();
                    return;
                }
            });
            return;
        }
        // ????????? ?????? api??? ?????????(????????? ???????????? ???????????? ?????????????????? api ??????) && LoginVo??? ???????????????(???????????? ??????) ?????? ????????? ????????? ????????????.
        if( ((ifID != R.string.url_restapi_introcheck) && (ifID != R.string.url_restapi_regist_member) && (ifID != R.string.url_restapi_login_member)) && !MHDApplication.getInstance().getMHDSvcManager().isLoginTaskRunning() ) {
            // ?????????????????? login vo??? null ??????
            MHDDialogUtil.sAlert(context, R.string.alert_security, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    mBaseActivity.goLogin();
                    return;
                }
            });
            return;
        }

        // api full url
        String url = MHDApplication.getInstance().getMHDSvcManager().getMHDNetInfoCVO().getSvrIntroUrl() + context.getString(ifID);
        MHDLog.d(TAG, "sendVolleyRequest(listener) url >>>>>>>>>>>> " + url);

        // Request ??????
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
            new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    String Response = response.trim();
                    MHDLog.d(TAG, "onResponse >>>>>>>>>>>>>>>>>>>>> "+ Response);
                    String result = "";  // ??????????????? ????????? ????????? ??????.
                    String data = "";   // ???????????? ?????? ??????
                    JSONObject jsonDataObject = null;   // data ?????? ???
                    String msg = "";    // ????????? ???????????? ??????????????? ????????? ?????????

                    try {
                        // parsing
                        JSONObject jsonObject = new JSONObject(Response);
                        // result:????????????. -1:Fail(??????), 0:Fail(????????????), 1:Fail,Info(alert), etc:Success
                        result = jsonObject.getString("result");
                        if(!jsonObject.isNull("data")){
                            data = jsonObject.getString("data");
                            jsonDataObject = new JSONObject(data);
                            msg = jsonDataObject.getString("msg");
                        }

                        // loading close
                        CustomLoading.hideLoading();

                        // result process
                        switch (result){
                            case "-1": // Fail. ?????? ??????
                                MHDDialogUtil.sAlert(mContext, msg, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        mBaseActivity.exitProcess();
                                        return;
                                    }
                                });
                                break;
                            case "0": // Fail. ?????? ??????
                                MHDDialogUtil.sAlert(mContext, msg, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        if(ifID == R.string.url_restapi_introcheck) // ???. ????????? ????????????????????? ?????? ??????.
                                            mBaseActivity.exitProcess();
                                        else
                                            mBaseActivity.goLogin();

                                        return;
                                    }
                                });
                                break;
                            case "1": // Fail. msg ????????? ?????? ???.
                                MHDDialogUtil.sAlert(mContext, msg, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        return;
                                    }
                                });
                                break;
                            case "M": // Response Success. msg ??????
                                if(listener !=null) listener.onResponse(Response);

                                break;
                            case "S": // Response Success. json ??????
                                if(listener !=null) listener.onResponse(Response);

                                break;
                            default:
                                if(listener !=null) listener.onResponse(Response);
                        }

                    } catch (Exception e) {
                        MHDLog.printException(e);

                        CustomLoading.hideLoading();

                        MHDDialogUtil.sAlert(mContext, R.string.alert_network_error, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // ?????? ?????????????????? ??????????????? ????????? ?????? ??????.
                                if(ifID == R.string.url_restapi_introcheck){
                                    mBaseActivity.exitProcess();
                                }
                                return;
                            }
                        });
                        return;
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    MHDLog.d(TAG, "onErrorResponse >>>>>>>>>>>>>>>>>>>>> "+ error.toString());

                    NetworkResponse response = error.networkResponse;
                    if (error instanceof ServerError && response != null) {
                        try {
                            String res = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
                            // Now you can use any deserializer to make sense of data
                            MHDLog.d(TAG, "onErrorResponse ServerError <><><><><><><><><> "+ res);
                        } catch (UnsupportedEncodingException e) {
                            MHDLog.printException(e);
                            // Couldn't properly decode data to string
                            MHDLog.d(TAG, "NetworkResponse UnsupportedEncodingException <><><><><><><><><> ");
                        }
                    }

                    CustomLoading.hideLoading();

                    // ?????? ?????????????????? ??????????????? ????????? ?????? ??????.
                    if(ifID == R.string.url_restapi_introcheck){
                        MHDDialogUtil.sAlert(mContext, R.string.alert_networkRequestError, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mBaseActivity.exitProcess();
                                return;
                            }
                        });
                        return;
                    }else{
                        if(listener !=null) listener.onError(error.toString());
                    }


                }
            }) {

//                // body??? data??? json?????? ??????????????? ?????? ??????????????????.
//                @Override
//                public byte[] getBody() throws AuthFailureError {
//                    MHDLog.d(TAG, "paramsMap >>>>>>>>>>>> "+paramsMap);
//                    return paramsMap.getBytes();
//                };
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    MHDLog.d(TAG, "paramsMap >>>>>>>>>>>> "+paramsMap.toString());
                    return paramsMap;
                }
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> headers = new HashMap<String, String>();

                    // ?????? ??? ???????????? ????????? ?????? ???( ?????? ????????? ????????? ?????? ??? )??? ????????? ?????? ?????? ??? ??????.
                    // ????????? ????????????. ????????? ????????? ???????????? ???????????? ????????? ??????.
                    //headers.put("Content-Type", "charset=utf-8;application/json;");
                    MHDLog.d(TAG, "paramsMap UUID >>>>>>>>>>>> "+userUuid);
                    MHDLog.d(TAG, "paramsMap UFCM >>>>>>>>>>>> "+userUfcm);
                    headers.put("UUID", userUuid);
                    headers.put("UFCM", userUfcm);
                    headers.put("USER_AGENT", mContext.getString(R.string.user_agent));
                    //headers.put("param_3", MHDApplication.getInstance().getMHDSvcManager().getLoginVo().getUsrInf_3());

                    return headers;
                }
//                // body??? data??? json?????? ??????????????? ??????. getBodyContentType??? ??????????????????.
//                public String getBodyContentType() {
//                    return "application/json;";
//                    //return "charset=UTF-8;";
//                }

            };

        postRequest.setRetryPolicy(new DefaultRetryPolicy(10000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)); // 20 secs

        queue.add(postRequest);
//        CustomLoading.showLoading(context);
    }

    public void cancelRequest(Context context){
        queue.cancelAll(context);
    }
}