package com.mhd.elemantary.activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.gson.Gson;
import com.kakao.sdk.auth.AuthApiClient;
import com.kakao.sdk.auth.model.OAuthToken;
import com.kakao.sdk.user.UserApiClient;
import com.kakao.sdk.user.model.Account;
import com.mhd.elemantary.R;
import com.mhd.elemantary.common.MHDApplication;
import com.mhd.elemantary.common.MHDPreferences;
import com.mhd.elemantary.common.vo.KidsVo;
import com.mhd.elemantary.common.vo.MenuVo;
import com.mhd.elemantary.common.vo.UserVo;
import com.mhd.elemantary.network.MHDNetworkInvoker;
import com.mhd.elemantary.util.MHDDialogUtil;
import com.mhd.elemantary.util.MHDLog;

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

        //////////////// 해쉬 키 찍어보기
//        try {
//            PackageInfo info = getPackageManager().getPackageInfo("com.mhd.elementary", PackageManager.GET_SIGNATURES);
//            for (Signature signature : info.signatures) {
//                MessageDigest md = MessageDigest.getInstance("SHA");
//                md.update(signature.toByteArray());
//                MHDLog.d("dagian key", Base64.encodeToString(md.digest(), Base64.DEFAULT));
//            }
//        } catch (PackageManager.NameNotFoundException e) {
//            e.printStackTrace();
//        } catch (NoSuchAlgorithmException e) {
//            e.printStackTrace();
//        }

        //////////////// 로그인 상태 유지 버튼 초기화
        tv_login_keep = (TextView) findViewById(R.id.tv_autologin);
        btn_login_keep = (AppCompatButton) findViewById(R.id.btn_login_keep);
        boolean tmpKeep = MHDPreferences.getInstance().getPrefLoginKeep();
        btn_login_keep.setSelected(tmpKeep);
        if(tmpKeep){
            btn_login_keep.setBackgroundResource(R.drawable.circle_check_ok);
        }else{
            btn_login_keep.setBackgroundResource(R.drawable.circle_check_not);
        }

        //////////////// 로그인 상태 유지 클릭이벤트
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
        //////////////// 아이디 저장 버튼 초기화
        tv_id_keep = (TextView) findViewById(R.id.tv_save_id);
        btn_id_keep = (AppCompatButton) findViewById(R.id.btn_id_keep);
        btn_id_keep.setSelected(!"".equals(MHDPreferences.getInstance().getPrefIDKeep()));
        //////////////// 아이디 저장 버튼 클릭이벤트
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
        //////////////// ID입력 EditText 초기화
        et_login_id = (EditText) findViewById(R.id.et_login_id);
        if(!"".equals(MHDPreferences.getInstance().getPrefIDKeep()) && "E".equals(MHDPreferences.getInstance().getPrefUserLtype())){
            // 아이디저장 상태이고 마지막 로그인이 이메일일때 저장된 ID 표시
            et_login_id.setText(MHDPreferences.getInstance().getPrefIDKeep());
            // 아이디저장 버튼 select 처리
            btn_id_keep.setSelected(true);
            btn_id_keep.setBackgroundResource(R.drawable.circle_check_ok);
        }
        //////////////// 비번입력 EditText 초기화
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
        //////////////// back 버튼 초기화, GONE
        btn_move_stat_left = (AppCompatButton) findViewById(R.id.btn_move_stat_left);
        btn_move_stat_left.setVisibility(View.GONE);
        //////////////// 각 로그인 버튼 초기화
        btn_google = (AppCompatButton) findViewById(R.id.btn_google);
        btn_kakao = (AppCompatButton) findViewById(R.id.btn_kakao);
        btn_login = (Button) findViewById(R.id.btn_login);
        //////////////// 각 로그인 버튼 클릭이벤트
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
        //////////////// 화면 타이틀 초기화
        vst_top_title = (TextView) findViewById(R.id.vst_top_title);
        vst_top_title.setText(R.string.title_login);
        //////////////// 회원가입 버튼 초기화
        tv_join = (TextView) findViewById(R.id.tv_join);
        //////////////// 회원가입 버튼 클릭이벤트
        tv_join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, JoinActivity.class);
                mContext.startActivity(intent);
            }
        });

        //////////////// 구글 기존 로그인정보 조회
        // DEFAULT_SIGN_IN parameter는 유저의 ID와 기본적인 프로필 정보를 요청하는데 사용된다.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("790798364026-gp83cj2jgn84d9u0hjh17jfcjjdtq7i4.apps.googleusercontent.com")
                .requestEmail() // email addresses 요청
                .build();
        // 위에서 만든 GoogleSignInOptions을 사용해 GoogleSignInClient 객체를 만듬
        mGoogleSignInClient = GoogleSignIn.getClient(LoginActivity.this, gso);

        String lastLtype = MHDPreferences.getInstance().getPrefUserLtype();
        String lastID = MHDPreferences.getInstance().getPrefUserID();
        String lastPWD = MHDPreferences.getInstance().getPrefUserPWD();
        if (MHDPreferences.getInstance().getPrefLoginKeep()) {
            // 로그인상태유지
            if (!"".equals(lastLtype) || !MHDPreferences.getInstance().getPrefLogout()) {
                // 로그인한 데이타가 있다면 태운다.
                // 로그인한 데이타가 없거나 로그아웃 버튼을 눌러서 로그아웃 한거면 로그인상태유지라 하더라도 자동로그인을 태우지 않는다.그냥 stay
                // 다시 로그인했을 때 로그아웃 여부를 초기화 한다.
                autoSignIn(lastLtype);
            }
        } else {
            // 로그인상태유지 아니다. 구글, 카카오 기존 연결정보 해제
            autoSignOut(lastLtype);
        }
    }

    private void signIn(String type){
        if("".equals(type)) {
            // 마지막 로그인 수단이 없다면 새로 설치, 기타 데이터 유실된 상태.
            // 아무것도 하지 않는다.
        }else if("G".equals(type)) {
            Intent signInIntent = mGoogleSignInClient.getSignInIntent();
            startActivityResultGoogle.launch(signInIntent);
        }else if("K".equals(type)){
            if(UserApiClient.getInstance().isKakaoTalkLoginAvailable(LoginActivity.this)){
                UserApiClient.getInstance().loginWithKakaoTalk(LoginActivity.this, (oAuthToken, error) -> {
                    if (error != null) {
                        MHDLog.d(TAG, "kakaotalk 로그인 실패");
                    } else if (oAuthToken != null) {
                        tmp = oAuthToken;
                        MHDLog.d(TAG, "kakaotalk 로그인 성공(토큰)" + oAuthToken.getAccessToken());
                        MHDPreferences.getInstance().savePrefKakaoToken(oAuthToken.getAccessToken());
                        token = oAuthToken.getAccessToken();

                        UserApiClient.getInstance().me((user, meError) -> {
                            if (meError != null) {
                                MHDLog.d(TAG, "kakaotalk 사용자 정보 요청 실패");
                            } else {
                                MHDLog.d(TAG, "kakaotalk 로그인 완료");
                                MHDLog.d(TAG, "kakaotalk 로그인 완료" + user.toString());
                                MHDLog.d(TAG, "사용자 정보 요청 성공" +
                                        "\n회원번호: " + user.getId() +
                                        "\nnick: " + user.getProperties().get("nickname"));
                                Account user1 = user.getKakaoAccount();
                                MHDLog.d(TAG, "kakaotalk 사용자 계정" + user1);

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
                        MHDLog.d(TAG, "kakao 로그인 실패");
                    } else if (oAuthToken != null) {
                        tmp = oAuthToken;
                        MHDLog.d(TAG, "kakao 로그인 성공(토큰)" + oAuthToken.getAccessToken());
                        MHDPreferences.getInstance().savePrefKakaoToken(oAuthToken.getAccessToken());
                        token = oAuthToken.getAccessToken();

                        UserApiClient.getInstance().me((user, meError) -> {
                            if (meError != null) {
                                MHDLog.d(TAG, "kakao 사용자 정보 요청 실패");
                            } else {
                                MHDLog.d(TAG, "kakao 로그인 완료");
                                MHDLog.d(TAG, "kakao 로그인 완료" + user.toString());
                                MHDLog.d(TAG, "사용자 정보 요청 성공" +
                                        "\n회원번호: " + user.getId() +
                                        "\nnick: " + user.getProperties().get("nickname"));
                                Account user1 = user.getKakaoAccount();
                                MHDLog.d(TAG, "kakao 사용자 계정" + user1);

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

            if(!"".equals(lastID) && !"".equals(lastPWD)) { // 계정정보 있다.
                loginService("E", lastID, lastPWD, "");
            }else{// 계정정보 없다. stay
            }
        }else if("G".equals(type)){
            // 기존에 로그인 했던 계정정보 확인한다.
            GoogleSignInAccount gsa = GoogleSignIn.getLastSignedInAccount(LoginActivity.this);
            // 계정정보 유무에 따른 처리
            if (gsa != null) {
                // 구글 계정정보 있는 경우.
                MHDLog.d(TAG, "google 자동로그인 정보 있음");

                // 로그인상태 유지 상태라면 서버로그인 바로 태운다
                String personName = gsa.getDisplayName();
                String personEmail = gsa.getEmail();
                String personId = gsa.getId();
                // 서버에서 아이 정보만 가져온다.
                loginService("G", personEmail, personId, personName);
            } else {
                // 구글 계정정보 없다. stay
                MHDLog.d(TAG, "google 자동로그인 정보 없음");
            }
        }else if("K".equals(type)){
            // 카카오 계정정보 확인
//            String tokenInfo = MHDPreferences.getInstance().getPrefKakaoToken();
//            UserApiClient.getInstance().accessTokenInfo (tmp.getAccessToken(), error -> {
//                if (error != null) {
//                    MHDLog.d(TAG, "kakao 로그아웃 실패, SDK에서 토큰 삭제됨");
//                }else if(tmp.getAccessToken() != null){
//                    MHDLog.d(TAG, "kakao 로그아웃 성공, SDK에서 토큰 삭제됨");
//                }
//                return null;
//            });
//            if (AuthApiClient.getInstance().hasToken()) {
//                signIn("K");
//                UserApiClient.getInstance().accessTokenInfo { _, error ->
//                    if (error != null) {
//                        if (error is KakaoSdkError && error.isInvalidTokenError() == true) {
//                            //로그인 필요
//                        }
//                        else {
//                            //기타 에러
//                        }
//                    }
//                    else {
//                        //토큰 유효성 체크 성공(필요 시 토큰 갱신됨)
//                    }
//                }
//            }
//            else {
//                //로그인 필요
//            }

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
                // 로그인 성공
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

                // 구글로그인 자체는 정상. 서버에서는 저장만 한다.
                loginService("G", personEmail, personId, personName);
            }
        } catch (ApiException e) {
            MHDDialogUtil.sAlert(mContext, R.string.alert_not_login);
        }
    }

    private void loginService(String ltype, String uuid, String uupwd, String uuname){
        //필터링. agree terms 해야 함.
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
            // Map 방식 0
            Map<String, String> params = new HashMap<String, String>();
            params.put("UUAPP", MHDApplication.getInstance().getAppVersion());
            params.put("UUMAIL", userID);
            params.put("UUPWD", userPWD);
            params.put("UULOGIN", ltype);
            params.put("UUNAME", uuname);
            MHDNetworkInvoker.getInstance().sendVolleyRequest(mContext, R.string.url_restapi_login_member, params, responseListener);

            if(btn_id_keep.isSelected()){ // 아이디 저장
                MHDPreferences.getInstance().savePrefIDKeep(userID);
            }else{ // 아이디 저장 안함
                MHDPreferences.getInstance().savePrefIDKeep("");
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            MHDLog.printException(e);
        }
        //Main 이동
    }

    @Override
    protected boolean networkResponseProcess(String result) {
        boolean resultFlag = super.networkResponseProcess(result);
        MHDLog.d(TAG, "networkResponseProcess resultFlag >>> " + resultFlag);

        if(!resultFlag) return resultFlag;

        // resultFlag 이 true 라면 현재 여기에 필요한 data 들이 전역에 들어가 있는 상태.

        if("M".equals(nvResultCode)){
            // nvMsg = "NK" 라면 회원이지만 아이 정보가 없다는 것.
            if("NK".equals(nvMsg)){
                if(MHDPreferences.getInstance().getPrefLoginKeep()){
                    // 로그인상태유지 상태라면 이메일 로그인할때 계정정보를 저장.
                    if("E".equals(loginType)) {
                        MHDPreferences.getInstance().savePrefUserID(userID);
                        MHDPreferences.getInstance().savePrefUserPWD(userPWD);
                    }
                    MHDPreferences.getInstance().savePrefUserLtype(loginType); // 마지막 로그인 수단
                }
                MHDPreferences.getInstance().savePrefLogout(false);
                // 이제 Tutorial 이 뜨지 않는다.
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
                // 로그인상태유지 상태라면 이메일 로그인할때 계정정보를 저장.
                if("E".equals(loginType)) {
                    MHDPreferences.getInstance().savePrefUserID(userID);
                    MHDPreferences.getInstance().savePrefUserPWD(userPWD);
                }
                MHDPreferences.getInstance().savePrefUserLtype(loginType); // 마지막 로그인 수단
            }
            MHDPreferences.getInstance().savePrefLogout(false);
            // 이제 Tutorial 이 뜨지 않는다.
            MHDApplication.getInstance().getMHDSvcManager().setIsFirstStart(false);
            SharedPreferences appPref = PreferenceManager.getDefaultSharedPreferences(mContext);
            SharedPreferences.Editor editor = appPref.edit();
            editor.putBoolean("execFirst", false);
            editor.commit();

            // 로그인 성공. user vo 를 구성하고
            MHDLog.d(TAG, "networkResponseProcess nvMsg >>> " + nvMsg);

            // 가입되었다는 메세지 띄우고 로그인 창으로 이동.
            // vo 및 각종 변수에 저장하고 메인으로 넘긴다. 레코드가 하나여도 jsonarray 로 보내니 gson에서 에러가 나드라.
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

            // 메뉴별로 보여주는 아이가 다를 수 있기 때문에. MenuVo에 저장해서 관리
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
                // MainActivity 로 이동
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
                        // 아이 정보가 등록이 됐다면 다시 로그인 태운다?
                        loginService(loginType, userID, userPWD, "");
                    } else {
                        // Login 단에서 아이 정보가 하나도 등록이 안된다면 앱 종료
                        exitApplication();
                    }
                }
            });

    private void signOut(String channel) {
        if("G".equals(channel)){ // 구글이라면
            mGoogleSignInClient.signOut()
                    .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            MHDLog.d(TAG, "google 로그아웃 성공");
                        }
                    });
        }
        if("K".equals(channel)){ // 카카오라면
            UserApiClient.getInstance().logout (error -> {
                if (error != null) {
                    MHDLog.d(TAG, "kakao 로그아웃 실패, SDK에서 토큰 삭제됨");
                }else{
                    MHDLog.d(TAG, "kakao 로그아웃 성공, SDK에서 토큰 삭제됨");
                }
                return null;
            });
        }
    }

    // 구글 회원 삭제. 로그인 정보 초기화
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

        if(btn_login_keep.isSelected()){ // 로그인 유지
            MHDPreferences.getInstance().savePrefLoginKeep(true);
            btn_login_keep.setBackgroundResource(R.drawable.circle_check_ok);
        }else{ // 로그인 유지 안함.
            MHDPreferences.getInstance().savePrefLoginKeep(false);
            btn_login_keep.setBackgroundResource(R.drawable.circle_check_not);
        }
    }
    private void idKeepStateChange(){
        btn_id_keep.setSelected(!btn_id_keep.isSelected());

        if(btn_id_keep.isSelected()){ // loginService 태울 때 아이디 최종 저장. 여기서는 버튼 모양만 변경
            btn_id_keep.setBackgroundResource(R.drawable.circle_check_ok);
        }else{ // loginService 태울 때 아이디 최종 삭제. 여기서는 버튼 모양만 변경
            MHDPreferences.getInstance().savePrefIDKeep("");
            btn_id_keep.setBackgroundResource(R.drawable.circle_check_not);
        }
    }
}
