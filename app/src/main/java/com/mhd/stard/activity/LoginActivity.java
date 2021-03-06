package com.mhd.stard.activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.gson.Gson;
import com.kakao.sdk.auth.AuthApiClient;
import com.kakao.sdk.auth.model.OAuthToken;
import com.kakao.sdk.user.UserApiClient;
import com.kakao.sdk.user.model.Account;
import com.mhd.stard.R;
import com.mhd.stard.common.MHDApplication;
import com.mhd.stard.common.MHDPreferences;
import com.mhd.stard.common.vo.KidsVo;
import com.mhd.stard.common.vo.MenuVo;
import com.mhd.stard.common.vo.UserVo;
import com.mhd.stard.network.MHDNetworkInvoker;
import com.mhd.stard.util.MHDDialogUtil;
import com.mhd.stard.util.MHDLog;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.preference.PreferenceManager;

public class LoginActivity extends BaseActivity {
    TextView vst_top_title;
    private Button btn_login;
    private EditText et_login_id, et_login_pwd;
    private TextView tv_join, tv_login_keep, tv_id_keep;
    AppCompatButton btn_move_stat_left, btn_google, btn_kakao, btn_id_keep, btn_login_keep, logout_kakao, logout_google;
    GoogleSignInClient mGoogleSignInClient;
    String userID = "";
    String userPWD = "";
    String loginType = "";
    String token = "";
    OAuthToken tmp;
    String inputID = "";
    String inputPWD = "";
    Pattern pattern;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initialize(R.layout.activity_login);
        mContext = LoginActivity.this;

        pattern = Patterns.EMAIL_ADDRESS;

        //////////////// ?????? ??? ????????????
        try {
            PackageInfo info = getPackageManager().getPackageInfo("com.mhd.stard", PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                MHDLog.d("dagian key", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        //////////////// ????????? ?????? ?????? ?????? ?????????
        tv_login_keep = (TextView) findViewById(R.id.tv_autologin);
        btn_login_keep = (AppCompatButton) findViewById(R.id.btn_login_keep);
        boolean tmpKeep = MHDPreferences.getInstance().getPrefLoginKeep();
        btn_login_keep.setSelected(tmpKeep);
        if(tmpKeep){
            btn_login_keep.setBackgroundResource(R.drawable.circle_check_ok);
        }else{
            btn_login_keep.setBackgroundResource(R.drawable.circle_check_not);
        }

        //////////////// ????????? ?????? ?????? ???????????????
        tv_login_keep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginKeepStateChange();
            }
        });
        btn_login_keep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginKeepStateChange();
            }
        });
        //////////////// ????????? ?????? ?????? ?????????
        tv_id_keep = (TextView) findViewById(R.id.tv_save_id);
        btn_id_keep = (AppCompatButton) findViewById(R.id.btn_id_keep);
        btn_id_keep.setSelected(!"".equals(MHDPreferences.getInstance().getPrefIDKeep()));
        //////////////// ????????? ?????? ?????? ???????????????
        tv_id_keep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                idKeepStateChange();
            }
        });
        btn_id_keep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                idKeepStateChange();
            }
        });
        //////////////// ID?????? EditText ?????????
        et_login_id = (EditText) findViewById(R.id.et_login_id);
        if(!"".equals(MHDPreferences.getInstance().getPrefIDKeep()) && "E".equals(MHDPreferences.getInstance().getPrefUserLtype())){
            // ??????????????? ???????????? ????????? ???????????? ??????????????? ????????? ID ??????
            et_login_id.setText(MHDPreferences.getInstance().getPrefIDKeep());
            // ??????????????? ?????? select ??????
            btn_id_keep.setSelected(true);
            btn_id_keep.setBackgroundResource(R.drawable.circle_check_ok);
        }
        //////////////// ???????????? EditText ?????????
        et_login_pwd = (EditText) findViewById(R.id.et_login_pwd);
        et_login_id.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                inputID = et_login_id.getText().toString();
                inputPWD = et_login_pwd.getText().toString();
                if(inputID == null) inputID = "";
                if(inputPWD == null) inputID = "";

                if(pattern.matcher(inputID).matches()){
                    if(inputPWD.length() <= 10 && inputPWD.length() >= 4){
                        btn_login.setEnabled(true);
                        btn_login.setBackgroundResource(R.drawable.btn_login);
                    }else{
                        btn_login.setEnabled(false);
                        btn_login.setBackgroundResource(R.drawable.btn_login_false);
                    }
                }else{
                    btn_login.setEnabled(false);
                    btn_login.setBackgroundResource(R.drawable.btn_login_false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        et_login_pwd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                inputID = et_login_id.getText().toString();
                inputPWD = et_login_pwd.getText().toString();
                if(inputID == null) inputID = "";
                if(inputPWD == null) inputID = "";

                if(inputPWD.length() <= 10 && inputPWD.length() >= 4){
                    if(pattern.matcher(inputID).matches()) {
                        btn_login.setEnabled(true);
                        btn_login.setBackgroundResource(R.drawable.btn_login);
                    }else{
                        btn_login.setEnabled(false);
                        btn_login.setBackgroundResource(R.drawable.btn_login_false);
                    }
                }else{
                    btn_login.setEnabled(false);
                    btn_login.setBackgroundResource(R.drawable.btn_login_false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        //////////////// back ?????? ?????????, GONE
        btn_move_stat_left = (AppCompatButton) findViewById(R.id.btn_move_stat_left);
        btn_move_stat_left.setVisibility(View.GONE);
        //////////////// ??? ????????? ?????? ?????????
        btn_google = (AppCompatButton) findViewById(R.id.btn_google);
        btn_kakao = (AppCompatButton) findViewById(R.id.btn_kakao);
        btn_login = (Button) findViewById(R.id.btn_login);
        btn_login.setEnabled(false);
        //////////////// ??? ????????? ?????? ???????????????
        btn_google.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn("G");
            }
        });
        btn_kakao.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                signIn("K");
            }
        });
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn("E");
            }
        });
        //////////////// ?????? ????????? ?????????
        vst_top_title = (TextView) findViewById(R.id.vst_top_title);
        vst_top_title.setText(R.string.title_login);
        //////////////// ???????????? ?????? ?????????
        tv_join = (TextView) findViewById(R.id.tv_join);
        //////////////// ???????????? ?????? ???????????????
        tv_join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, JoinActivity.class);
                mContext.startActivity(intent);
            }
        });

        //////////////// ?????? ?????? ??????????????? ??????
        // DEFAULT_SIGN_IN parameter??? ????????? ID??? ???????????? ????????? ????????? ??????????????? ????????????.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("790798364026-gp83cj2jgn84d9u0hjh17jfcjjdtq7i4.apps.googleusercontent.com")
                .requestEmail() // email addresses ??????
                .build();
        // ????????? ?????? GoogleSignInOptions??? ????????? GoogleSignInClient ????????? ??????
        mGoogleSignInClient = GoogleSignIn.getClient(LoginActivity.this, gso);

        String lastLtype = MHDPreferences.getInstance().getPrefUserLtype();
        String lastID = MHDPreferences.getInstance().getPrefUserID();
        String lastPWD = MHDPreferences.getInstance().getPrefUserPWD();
        if (MHDPreferences.getInstance().getPrefLoginKeep()) {
            // ?????????????????????
            if (!"".equals(lastLtype) || !MHDPreferences.getInstance().getPrefLogout()) {
                // ???????????? ???????????? ????????? ?????????.
                // ???????????? ???????????? ????????? ???????????? ????????? ????????? ???????????? ????????? ???????????????????????? ???????????? ?????????????????? ????????? ?????????.?????? stay
                // ?????? ??????????????? ??? ???????????? ????????? ????????? ??????.
                autoSignIn(lastLtype);
            }
        } else {
            // ????????????????????? ?????????. ??????, ????????? ?????? ???????????? ??????
            autoSignOut(lastLtype);
        }
    }

    private void signIn(String type){
        if("".equals(type)) {
            // ????????? ????????? ????????? ????????? ?????? ??????, ?????? ????????? ????????? ??????.
            // ???????????? ?????? ?????????.
        }else if("G".equals(type)) {
            Intent signInIntent = mGoogleSignInClient.getSignInIntent();
            startActivityResultGoogle.launch(signInIntent);
        }else if("K".equals(type)){
            if(UserApiClient.getInstance().isKakaoTalkLoginAvailable(LoginActivity.this)){
                UserApiClient.getInstance().loginWithKakaoTalk(LoginActivity.this, (oAuthToken, error) -> {
                    if (error != null) {
                        MHDLog.d(TAG, "kakaotalk ????????? ??????");
                    } else if (oAuthToken != null) {
                        tmp = oAuthToken;
                        MHDLog.d(TAG, "kakaotalk ????????? ??????(??????)" + oAuthToken.getAccessToken());
                        MHDPreferences.getInstance().savePrefKakaoToken(oAuthToken.getAccessToken());
                        token = oAuthToken.getAccessToken();

                        UserApiClient.getInstance().me((user, meError) -> {
                            if (meError != null) {
                                MHDLog.d(TAG, "kakaotalk ????????? ?????? ?????? ??????");
                            } else {
                                MHDLog.d(TAG, "kakaotalk ????????? ??????");
                                MHDLog.d(TAG, "kakaotalk ????????? ??????" + user.toString());
                                MHDLog.d(TAG, "????????? ?????? ?????? ??????" +
                                        "\n????????????: " + user.getId() +
                                        "\nnick: " + user.getProperties().get("nickname"));
                                Account user1 = user.getKakaoAccount();
                                MHDLog.d(TAG, "kakaotalk ????????? ??????" + user1);

                                loginService("K", String.valueOf(user.getId()), "", user.getProperties().get("nickname"));
                            }
                            return null;
                        });
                    }
                    return null;
                });
            }else {
                UserApiClient.getInstance().loginWithKakaoAccount(LoginActivity.this, (oAuthToken, error) -> {
                    if (error != null) {
                        MHDLog.d(TAG, "kakao ????????? ??????");
                    } else if (oAuthToken != null) {
                        tmp = oAuthToken;
                        MHDLog.d(TAG, "kakao ????????? ??????(??????)" + oAuthToken.getAccessToken());
                        MHDPreferences.getInstance().savePrefKakaoToken(oAuthToken.getAccessToken());
                        token = oAuthToken.getAccessToken();

                        UserApiClient.getInstance().me((user, meError) -> {
                            if (meError != null) {
                                MHDLog.d(TAG, "kakao ????????? ?????? ?????? ??????");
                            } else {
                                MHDLog.d(TAG, "kakao ????????? ??????");
                                MHDLog.d(TAG, "kakao ????????? ??????" + user.toString());
                                MHDLog.d(TAG, "????????? ?????? ?????? ??????" +
                                        "\n????????????: " + user.getId() +
                                        "\nnick: " + user.getProperties().get("nickname"));
                                Account user1 = user.getKakaoAccount();
                                MHDLog.d(TAG, "kakao ????????? ??????" + user1);

                                loginService("K", String.valueOf(user.getId()), "", user.getProperties().get("nickname"));
                            }
                            return null;
                        });
                    }
                    return null;
                });
            }
        }else if("E".equals(type)){
            loginService("E", et_login_id.getText().toString(), et_login_pwd.getText().toString(), "");
        }
    }
    private void autoSignIn(String type){
        if("E".equals(type)){
            String lastID = MHDPreferences.getInstance().getPrefUserID();
            String lastPWD = MHDPreferences.getInstance().getPrefUserPWD();

            if(!"".equals(lastID) && !"".equals(lastPWD)) { // ???????????? ??????.
                loginService("E", lastID, lastPWD, "");
            }else{// ???????????? ??????. stay
            }
        }else if("G".equals(type)){
            // ????????? ????????? ?????? ???????????? ????????????.
            GoogleSignInAccount gsa = GoogleSignIn.getLastSignedInAccount(LoginActivity.this);
            // ???????????? ????????? ?????? ??????
            if (gsa != null) {
                // ?????? ???????????? ?????? ??????.
                MHDLog.d(TAG, "google ??????????????? ?????? ??????");

                // ??????????????? ?????? ???????????? ??????????????? ?????? ?????????
                String personName = gsa.getDisplayName();
                String personEmail = gsa.getEmail();
                String personId = gsa.getId();
                // ???????????? ?????? ????????? ????????????.
                loginService("G", personEmail, personId, personName);
            } else {
                // ?????? ???????????? ??????. stay
                MHDLog.d(TAG, "google ??????????????? ?????? ??????");
            }
        }else if("K".equals(type)){
            // ????????? ???????????? ??????
//            String tokenInfo = MHDPreferences.getInstance().getPrefKakaoToken();
//            UserApiClient.getInstance().accessTokenInfo (tmp.getAccessToken(), error -> {
//                if (error != null) {
//                    MHDLog.d(TAG, "kakao ???????????? ??????, SDK?????? ?????? ?????????");
//                }else if(tmp.getAccessToken() != null){
//                    MHDLog.d(TAG, "kakao ???????????? ??????, SDK?????? ?????? ?????????");
//                }
//                return null;
//            });
            if (AuthApiClient.getInstance().hasToken()) {
                signIn("K");
//                UserApiClient.getInstance().accessTokenInfo { _, error ->
//                    if (error != null) {
//                        if (error is KakaoSdkError && error.isInvalidTokenError() == true) {
//                            //????????? ??????
//                        }
//                        else {
//                            //?????? ??????
//                        }
//                    }
//                    else {
//                        //?????? ????????? ?????? ??????(?????? ??? ?????? ?????????)
//                    }
//                }
            }
            else {
                //????????? ??????
            }

        }
    }
    private void autoSignOut(String type){
        if("E".equals(type)){
            MHDPreferences.getInstance().savePrefUserID("");
            MHDPreferences.getInstance().savePrefUserPWD("");
        }else if("G".equals(type)){
            signOut("G");
        }else if("K".equals(type)){
            signOut("K");
        }
    }

    ActivityResultLauncher<Intent> startActivityResultGoogle = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(result.getData());
                        handleSignInResult(task);
                    }
                }
            });

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount acct = completedTask.getResult(ApiException.class);

            if (acct != null) {
                // ????????? ??????
                String personName = acct.getDisplayName();
                String personGivenName = acct.getGivenName();
                String personFamilyName = acct.getFamilyName();
                String personEmail = acct.getEmail();
                String personId = acct.getId();
                Uri personPhoto = acct.getPhotoUrl();

                MHDLog.d(TAG, "handleSignInResult:personName "+personName);
                MHDLog.d(TAG, "handleSignInResult:personGivenName "+personGivenName);
                MHDLog.d(TAG, "handleSignInResult:personEmail "+personEmail);
                MHDLog.d(TAG, "handleSignInResult:personId "+personId);
                MHDLog.d(TAG, "handleSignInResult:personFamilyName "+personFamilyName);
                MHDLog.d(TAG, "handleSignInResult:personPhoto "+personPhoto);

                // ??????????????? ????????? ??????. ??????????????? ????????? ??????.
                loginService("G", personEmail, personId, personName);
            }
        } catch (ApiException e) {
            MHDDialogUtil.sAlert(mContext, R.string.alert_not_login);
        }
    }

    private void loginService(String ltype, String uuid, String uupwd, String uuname){
        //?????????. agree terms ?????? ???.
        if("E".equals(ltype)) {
            if (uuid == null || "".equals(uuid)) {
                MHDDialogUtil.sAlert(mContext, R.string.alert_not_id);
                return;
            }
            if (uupwd == null || "".equals(uupwd)) {
                MHDDialogUtil.sAlert(mContext, R.string.alert_not_pwd);
                return;
            }
        }
        userID = uuid;
        userPWD = uupwd;
        loginType = ltype;
        try {
            // Map ?????? 0
            Map<String, String> params = new HashMap<String, String>();
            params.put("UUAPP", MHDApplication.getInstance().getAppVersion());
            params.put("UUMAIL", userID);
            params.put("UUPWD", userPWD);
            params.put("UULOGIN", ltype);
            params.put("UUNAME", uuname);
            MHDNetworkInvoker.getInstance().sendVolleyRequest(mContext, R.string.url_restapi_login_member, params, responseListener);

            if(btn_id_keep.isSelected()){ // ????????? ??????
                MHDPreferences.getInstance().savePrefIDKeep(userID);
            }else{ // ????????? ?????? ??????
                MHDPreferences.getInstance().savePrefIDKeep("");
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            MHDLog.printException(e);
        }
        //Main ??????
    }

    @Override
    protected boolean networkResponseProcess(String result) {
        boolean resultFlag = super.networkResponseProcess(result);
        MHDLog.d(TAG, "networkResponseProcess resultFlag >>> " + resultFlag);

        if(!resultFlag) return resultFlag;

        // resultFlag ??? true ?????? ?????? ????????? ????????? data ?????? ????????? ????????? ?????? ??????.

        if("M".equals(nvResultCode)){
            // nvMsg = "NK" ?????? ??????????????? ?????? ????????? ????????? ???.
            if("NK".equals(nvMsg)){
                if(MHDPreferences.getInstance().getPrefLoginKeep()){
                    // ????????????????????? ???????????? ????????? ??????????????? ??????????????? ??????.
                    if("E".equals(loginType)) {
                        MHDPreferences.getInstance().savePrefUserID(userID);
                        MHDPreferences.getInstance().savePrefUserPWD(userPWD);
                    }
                    MHDPreferences.getInstance().savePrefUserLtype(loginType); // ????????? ????????? ??????
                }
                MHDPreferences.getInstance().savePrefLogout(false);
                // ?????? Tutorial ??? ?????? ?????????.
                MHDApplication.getInstance().getMHDSvcManager().setIsFirstStart(false);
                SharedPreferences appPref = PreferenceManager.getDefaultSharedPreferences(mContext);
                SharedPreferences.Editor editor = appPref.edit();
                editor.putBoolean("execFirst", false);
                editor.commit();

                UserVo userVo = new UserVo();
                userVo.setUuID(MHDApplication.getInstance().getMHDSvcManager().getDeviceNewUuid());
                userVo.setUuToken(MHDApplication.getInstance().getMHDSvcManager().getFcmToken());
                userVo.setUuAppVer(MHDApplication.getInstance().getAppVersion());
                userVo.setUuMail(userID);
                userVo.setUuLogin(loginType);
                MHDApplication.getInstance().getMHDSvcManager().setUserVo(null);
                MHDApplication.getInstance().getMHDSvcManager().setUserVo(userVo);

                MHDDialogUtil.sAlert(mContext, R.string.content_not_kids, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startKidsRegistMain();
                        return;
                    }
                });
            }else {
                MHDDialogUtil.sAlert(mContext, nvMsg);
            }
        }else if("S".equals(nvResultCode)){
            if(MHDPreferences.getInstance().getPrefLoginKeep()){
                // ????????????????????? ???????????? ????????? ??????????????? ??????????????? ??????.
                if("E".equals(loginType)) {
                    MHDPreferences.getInstance().savePrefUserID(userID);
                    MHDPreferences.getInstance().savePrefUserPWD(userPWD);
                }
                MHDPreferences.getInstance().savePrefUserLtype(loginType); // ????????? ????????? ??????
            }
            MHDPreferences.getInstance().savePrefLogout(false);
            // ?????? Tutorial ??? ?????? ?????????.
            MHDApplication.getInstance().getMHDSvcManager().setIsFirstStart(false);
            SharedPreferences appPref = PreferenceManager.getDefaultSharedPreferences(mContext);
            SharedPreferences.Editor editor = appPref.edit();
            editor.putBoolean("execFirst", false);
            editor.commit();

            // ????????? ??????. user vo ??? ????????????
            MHDLog.d(TAG, "networkResponseProcess nvMsg >>> " + nvMsg);

            // ?????????????????? ????????? ????????? ????????? ????????? ??????.
            // vo ??? ?????? ????????? ???????????? ???????????? ?????????. ???????????? ???????????? jsonarray ??? ????????? gson?????? ????????? ?????????.
            UserVo userVo = new UserVo();
            userVo.setUuID(MHDApplication.getInstance().getMHDSvcManager().getDeviceNewUuid());
            userVo.setUuToken(MHDApplication.getInstance().getMHDSvcManager().getFcmToken());
            userVo.setUuAppVer(MHDApplication.getInstance().getAppVersion());
            userVo.setUuMail(userID);
            userVo.setUuLogin(loginType);
            MHDApplication.getInstance().getMHDSvcManager().setUserVo(null);
            MHDApplication.getInstance().getMHDSvcManager().setUserVo(userVo);

            Gson gson = new Gson();
            KidsVo kidsVo;
            kidsVo = gson.fromJson(nvJsonDataString, KidsVo.class);
            MHDApplication.getInstance().getMHDSvcManager().setKidsVo(null);
            MHDApplication.getInstance().getMHDSvcManager().setKidsVo(kidsVo);

            // ???????????? ???????????? ????????? ?????? ??? ?????? ?????????. MenuVo??? ???????????? ??????
            String jsonString = "{" +
                                    "\"msg\":[{" +
                                        "\"menuname\":\"TO\"," +
                                        "\"kidname\":\""+ kidsVo.getMsg().get(0).getName() +"\"},{" +
                                        "\"menuname\":\"SC\"," +
                                        "\"kidname\":\""+ kidsVo.getMsg().get(0).getName() +"\"},{" +
                                        "\"menuname\":\"SE\"," +
                                        "\"kidname\":\""+ kidsVo.getMsg().get(0).getName() +"\"},{" +
                                        "\"menuname\":\"SU\"," +
                                        "\"kidname\":\""+ kidsVo.getMsg().get(0).getName() +"\"}]}";
            MenuVo menuVo;
            menuVo = gson.fromJson(jsonString, MenuVo.class);
            MHDApplication.getInstance().getMHDSvcManager().setMenuVo(null);
            MHDApplication.getInstance().getMHDSvcManager().setMenuVo(menuVo);

            if(MHDApplication.getInstance().getMHDSvcManager().getUserVo() != null){
                // MainActivity ??? ??????
                goMain();
            }
        }

        return true;
    }

    public void startKidsRegistMain(){
        Intent intent = new Intent(mContext, RegistKidsActivity.class);
        startActivityResultKids.launch(intent);
    }
    ActivityResultLauncher<Intent> startActivityResultKids = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        // ?????? ????????? ????????? ????????? ?????? ????????? ??????????
                        loginService(loginType, userID, userPWD, "");
                    } else {
                        // Login ????????? ?????? ????????? ????????? ????????? ???????????? ??? ??????
                        exitApplication();
                    }
                }
            });

    private void signOut(String channel) {
        if("G".equals(channel)){ // ???????????????
            mGoogleSignInClient.signOut()
                    .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            MHDLog.d(TAG, "google ???????????? ??????");
                        }
                    });
        }
        if("K".equals(channel)){ // ???????????????
            UserApiClient.getInstance().logout (error -> {
                if (error != null) {
                    MHDLog.d(TAG, "kakao ???????????? ??????, SDK?????? ?????? ?????????");
                }else{
                    MHDLog.d(TAG, "kakao ???????????? ??????, SDK?????? ?????? ?????????");
                }
                return null;
            });
        }
    }

    // ?????? ?????? ??????. ????????? ?????? ?????????
    private void revokeAccess() {
        mGoogleSignInClient.revokeAccess()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        // ...
                    }
                });
    }

    private void loginKeepStateChange() {
        btn_login_keep.setSelected(!btn_login_keep.isSelected());

        if(btn_login_keep.isSelected()){ // ????????? ??????
            MHDPreferences.getInstance().savePrefLoginKeep(true);
            btn_login_keep.setBackgroundResource(R.drawable.circle_check_ok);
        }else{ // ????????? ?????? ??????.
            MHDPreferences.getInstance().savePrefLoginKeep(false);
            btn_login_keep.setBackgroundResource(R.drawable.circle_check_not);
        }
    }
    private void idKeepStateChange(){
        btn_id_keep.setSelected(!btn_id_keep.isSelected());

        if(btn_id_keep.isSelected()){ // loginService ?????? ??? ????????? ?????? ??????. ???????????? ?????? ????????? ??????
            btn_id_keep.setBackgroundResource(R.drawable.circle_check_ok);
        }else{ // loginService ?????? ??? ????????? ?????? ??????. ???????????? ?????? ????????? ??????
            MHDPreferences.getInstance().savePrefIDKeep("");
            btn_id_keep.setBackgroundResource(R.drawable.circle_check_not);
        }
    }
}
