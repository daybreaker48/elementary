package com.mhd.elemantary.activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.gson.Gson;
import com.kakao.sdk.user.UserApiClient;
import com.kakao.sdk.user.model.Account;
import com.mhd.elemantary.R;
import com.mhd.elemantary.common.MHDApplication;
import com.mhd.elemantary.common.vo.KidsVo;
import com.mhd.elemantary.common.vo.MenuVo;
import com.mhd.elemantary.common.vo.UserVo;
import com.mhd.elemantary.network.MHDNetworkInvoker;
import com.mhd.elemantary.util.MHDDialogUtil;
import com.mhd.elemantary.util.MHDLog;
import com.mhd.elemantary.webview.activity.HybridWebGuestActivity;

import java.util.HashMap;
import java.util.Map;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.widget.AppCompatButton;
import androidx.preference.PreferenceManager;

public class LoginActivity extends BaseActivity {
    TextView vst_top_title;
    private Button btn_login;
    private EditText et_login_id, et_login_pwd;
    private TextView tv_join;
    AppCompatButton btn_move_stat_left, btn_google, btn_kakao;
    GoogleSignInClient mGoogleSignInClient;
    SignInButton sign_in_button;
    String userID = "";
    String userPWD = "";
    String loginType = "E";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initialize(R.layout.activity_login);
        mContext = LoginActivity.this;

        // 해쉬 키 찍어보기
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

        btn_move_stat_left = (AppCompatButton) findViewById(R.id.btn_move_stat_left);
        btn_google = (AppCompatButton) findViewById(R.id.btn_google);
        btn_kakao = (AppCompatButton) findViewById(R.id.btn_kakao);

        btn_google.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });
        btn_kakao.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                UserApiClient.getInstance().loginWithKakaoAccount(LoginActivity.this,(oAuthToken, error) -> {
                    if (error != null) {
                        MHDLog.d(TAG, "kakao 로그인 실패");
                    } else if (oAuthToken != null) {
                        MHDLog.d(TAG, "kakao 로그인 성공(토큰)" + oAuthToken.getAccessToken());

                        UserApiClient.getInstance().me((user, meError) -> {
                            if (meError != null) {
                                MHDLog.d(TAG, "kakao 사용자 정보 요청 실패");
                            } else {
                                MHDLog.d(TAG, "kakao 로그인 완료");
                                MHDLog.d(TAG, "kakao 로그인 완료" + user.toString());
                                MHDLog.d(TAG, "사용자 정보 요청 성공" +
                                        "\n회원번호: "+user.getId() +
                                        "\nnick: "+user.getProperties().get("nickname"));
                                Account user1 = user.getKakaoAccount();
                                MHDLog.d(TAG, "kakao 사용자 계정" + user1);

                                loginService("K", String.valueOf(user.getId()), user.getProperties().get("nickname"));
                            }
                            return null;
                        });
                    }
                    return null;
                });

            }
        });
//        btn_login_out.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                UserApiClient.getInstance().logout(error -> {
//                    if (error != null) {
//                        Log.e(TAG, "로그아웃 실패, SDK에서 토큰 삭제됨", error);
//                    }else{
//                        Log.e(TAG, "로그아웃 성공, SDK에서 토큰 삭제됨");
//                    }
//                    return null;
//                });
//            }
//        });
        sign_in_button = (SignInButton) findViewById(R.id.sign_in_button);
        sign_in_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });
        btn_move_stat_left.setVisibility(View.GONE);

        vst_top_title = (TextView) findViewById(R.id.vst_top_title);
        vst_top_title.setText(R.string.title_login);

//        MHDLog.d("dagian", Utility.getKeyHash(this));

        btn_login = (Button) findViewById(R.id.btn_login);
        et_login_id = (EditText) findViewById(R.id.et_login_id);
        et_login_pwd = (EditText) findViewById(R.id.et_login_pwd);
        tv_join = (TextView) findViewById(R.id.tv_join);
        tv_join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, JoinActivity.class);
                mContext.startActivity(intent);
            }
        });
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginService("E", et_login_id.getText().toString(), et_login_pwd.getText().toString());
            }
        });

        // 앱에 필요한 사용자 데이터를 요청하도록 로그인 옵션을 설정한다.
        // DEFAULT_SIGN_IN parameter는 유저의 ID와 기본적인 프로필 정보를 요청하는데 사용된다.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("790798364026-gp83cj2jgn84d9u0hjh17jfcjjdtq7i4.apps.googleusercontent.com")
                .requestEmail() // email addresses도 요청함
                .build();

        // 위에서 만든 GoogleSignInOptions을 사용해 GoogleSignInClient 객체를 만듬
        mGoogleSignInClient = GoogleSignIn.getClient(LoginActivity.this, gso);

        // 기존에 로그인 했던 계정을 확인한다.
        GoogleSignInAccount gsa = GoogleSignIn.getLastSignedInAccount(LoginActivity.this);

        // 로그인 되있는 경우
        if (gsa != null) {
            // 로그인 성공. 자동로그인
            Toast.makeText(mContext, "자동로그인 성공", Toast.LENGTH_SHORT).show();
        } else {
            // 로그인되어있지 않은 경우.
            Toast.makeText(mContext, "자동로그인 해지", Toast.LENGTH_SHORT).show();
        }
    }

    private void signIn(){
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityResultGoogle.launch(signInIntent);
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
                loginService("G", personEmail, personId);
            }
        } catch (ApiException e) {
            MHDDialogUtil.sAlert(mContext, R.string.alert_not_login);
        }
    }

    private void loginService(String ltype, String uuid, String uupwd){
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
            MHDNetworkInvoker.getInstance().sendVolleyRequest(mContext, R.string.url_restapi_login_member, params, responseListener);

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
    protected boolean networkResponseProcess(String result) {
        boolean resultFlag = super.networkResponseProcess(result);
        MHDLog.d(TAG, "networkResponseProcess resultFlag >>> " + resultFlag);

        if(!resultFlag) return resultFlag;

        // resultFlag 이 true 라면 현재 여기에 필요한 data 들이 전역에 들어가 있는 상태.

        if("M".equals(nvResultCode)){
            // nvMsg = "NK" 라면 회원이지만 아이 정보가 없다는 것.
            if("NK".equals(nvMsg)){
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
            userVo.setUuLogin("E");
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
                        loginService(loginType, userID, userPWD);
                    } else {
                        // Login 단에서 아이 정보가 하나도 등록이 안된다면 앱 종료
                        exitApplication();
                    }
                }
            });
}
