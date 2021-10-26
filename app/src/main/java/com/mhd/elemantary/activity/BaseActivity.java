package com.mhd.elemantary.activity;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.customview.widget.ViewDragHelper;
import androidx.appcompat.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.webkit.CookieManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.mhd.elemantary.MainActivity;
import com.mhd.elemantary.R;
import com.mhd.elemantary.business.ExternalLibraryManager;
import com.mhd.elemantary.business.model.PushVo;
import com.mhd.elemantary.common.MHDApplication;
import com.mhd.elemantary.constant.MHDConstants;
import com.mhd.elemantary.network.MHDNetworkInvoker;
import com.mhd.elemantary.network.manager.ResponseListener;
import com.mhd.elemantary.util.MHDDialogUtil;
import com.mhd.elemantary.util.MHDLog;
import com.mhd.elemantary.util.Util;
import com.mhd.elemantary.webview.activity.HybridWebActivity;
import com.mhd.elemantary.webview.activity.HybridWebGuestActivity;

import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

//import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;
import io.github.inflationx.viewpump.ViewPumpContextWrapper;


/**
 * Activity Common Setting
 * Created by MH.D on 2017-03-22.
 */
public class BaseActivity extends AppCompatActivity implements View.OnClickListener {
    public final String TAG = getClass().getName();
    /**
     * 공통 UI( home, back, logo, menu ... ) 설정 및 이벤트를 처리
     */
    private ImageView btn_back;
    private ImageView btn_home;
    /**
     * 외부라이브러리 controller
     */
    private ExternalLibraryManager mExternalLibraryManager;
    /**
     * 메뉴 영역
     * */
    private DrawerLayout drawerlayout;
    private LinearLayout ll_menu_1, ll_menu_2, ll_menu_3, ll_menu_4, ll_menu_5;
    /**
     * Activity network Listener
     * */
    public ResponseListener responseListener;
    /**
     * 현재 activity context
     */
    public Context mContext;
    /**
     * photo file path
     */
    public String mCurrentPhotoPath;
    /**
     * image uri
     */
    public Uri photoUri, galleryUri = null;
    /**
     * get image channel flag
     * */
    public Boolean album = false;
    /**
     * network variables
     * */
    public String nvJsonDataString;
    public String nvResultCode = "";
    public String nvApi = "";
    public int nvCnt = 0;
    public String nvMsg = "";

    /**
     * onCreate
     * @Override
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        MHDLog.d(TAG, this.getClass().getSimpleName() + " activity created=========" + MHDApplication.getInstance().isAnyActivityInvokedOnce());
        mExternalLibraryManager = new ExternalLibraryManager(this, isEssentialCheckNeeded());

        // Application 컴포넌트 최초 호출이라면.
        if(MHDApplication.getInstance().isAnyActivityInvokedOnce() == false) {
            MHDApplication.getInstance().setAnyActivityInvokedOnce(true);

            boolean isInitializeStarted = mExternalLibraryManager.initializeExternalLibrary();
            if(isInitializeStarted == false) {
                // network 사용불가능
                MHDDialogUtil.sAlert(this, R.string.alert_network_disable, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        exitProcess();
                    }
                }, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //네트워크 연결 alert - 취소하면 그냥 패스.
                        //exitProcess();
                    }
                });
                return;
            }
        }

        // 외부라이브러리 Main Thread 단 프로세스 호출.
        mExternalLibraryManager.onActivityCreateProcess();

        // Activity volley 통신 ResponseListner 등록
        // 리턴하는 데이터에서 request 정보를 담아와서 처리 분기한다.
        responseListener = new ResponseListener() {
            @Override
            public void onError(String message) {
                MHDLog.e(TAG, "ResponseListener onError >>>>>>>>>> " + message.toString());
                MHDDialogUtil.sAlert(mContext, "요청을 수행하는 중에 오류가 발생하였습니다.", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) { return; }
                });
                return;
            }
            @Override
            public void onResponse(String response) {
                MHDLog.d(TAG, "ResponseListener onResponse >>>>>>>>>> " + response.toString());
//                MHDDialogUtil.sAlert(mContext, R.string.alert_networkRequestSuccess, new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) { return; }
//                });

                networkResponseProcess(response);
                return;
            }
        };
    }
    /**
     * volley listner 의 success response 처리.
     * 필요한 곳에서 override 해서 구현.
     * 기본적인, 주요 Error 처리들은 MHDNetworkInvoker 에서 처리하고
     * 이 곳에서는 result "S" 인 성공 처리. "S" 라는 것은 서버에서 응답을 정상적으로 했다라는 의미.
     */
    protected boolean networkResponseProcess(String result) {
        MHDLog.d(TAG, "networkResponseProcess >>> " + result);

        boolean resultFlag = false;
        try{
            JSONObject nvJsonObject = new JSONObject(result);
            // 결과코드. 여기로 왔다는 건 M, S(Success) 라는 것.
            nvResultCode = nvJsonObject.getString("result");
            String nvData = nvJsonObject.getString("data");
            JSONObject nvJsonDataObject = new JSONObject(nvData);
            nvJsonDataString = nvJsonDataObject.toString().trim();

            nvApi = nvJsonDataObject.getString("api");
            nvMsg = nvJsonDataObject.getString("msg");
            if("S".equals(nvResultCode)){  // Success. data 필드가 있고 그 안에 api, (cnt), msg 필드가 있다.
                nvCnt = nvJsonDataObject.getInt("cnt");
            }
            resultFlag = true;
        } catch (Exception e) {
            MHDLog.printException(e);
            resultFlag = false;
        } finally {
            return resultFlag;
        }
    }
    /**
     * 외부 라이브러리 초기 체크를 할것인지 여부 default true
     * 필요없을시 override 해서 false 를 return 할것.
     */
    protected boolean isEssentialCheckNeeded() {
        return true;
    }
    /**
     * 어플리케이션 종료
     */
    public void exitProcess() {
        // Application 종료 시 네트워크 인트턴스 종료 처리
        try {
            MHDNetworkInvoker.getInstance().cancelRequest(this);
            //TODO 기타 필요한 세션처리와 같은 종료처리

        } catch (Exception e) {
            MHDLog.printException(e);
        }

        // 세션 초기화
        clearSession();

        mExternalLibraryManager.destroyExternalLibrary();

        //if(Build.VERSION.SDK_INT >= 16) finishAffinity();
        ActivityCompat.finishAffinity(this);
        System.runFinalizersOnExit(true);
        System.exit(0);
    }
    /**
     * webview cookie clear
     */
    public void clearSession() {
        try {
            CookieManager cookieManager = CookieManager.getInstance();
            cookieManager.removeSessionCookie();
            cookieManager.removeAllCookie();
        }catch(Exception e){
            MHDLog.printException(e);
        }
    }
    /**
     * 초기설정(initialize) : setContentView 를 여기서 처리한다. (위 아래 공통영역을 한번에 컨트롤하기 위해)
     */
    protected void initialize(int layoutID) {
        setContentView(layoutID);
//        initTitleViews();
//        initMenuViews();
    }
    /**
     * 초기설정(initialize)
     */
    protected void initialize(int layoutID, String title) {
        setContentView(layoutID);
        setTitle(title);
        initTitleViews();
        initMenuViews();
    }
    /**
     * 초기설정(initialize)
     */
    protected void initialize(int layoutID, int title) {
        setContentView(layoutID);
        setTitle(getString(title));
        initTitleViews();
        initMenuViews();
    }
    /**
     * 초기설정(initialize - noTitle)
     */
    protected void initializeNoTitle(int layoutID) {
        setContentView(layoutID);
        initMenuViews();
    }
    /**
     * title area init
     */
    private void initTitleViews() {
        // title area 가 필요없는 activity는 return.
//        if (this instanceof MainActivity)
//            return;

        btn_back = (ImageView) findViewById(R.id.title_back);   // back, menu, home 응용
        btn_home = (ImageView) findViewById(R.id.title_home);   // back, menu, home 응용

        btn_back.setOnClickListener(this);
        btn_home.setOnClickListener(this);
    }
    /**
     * side menu area init
     */
    private void initMenuViews() {
        // side menu 가 필요없는 activity는 return.
        // 현재는 side menu 를 안만들었기에 모두 리턴.
        if (true) return;

        drawerlayout = (DrawerLayout)findViewById(R.id.drawerlayout);
        drawerlayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
//        if (this instanceof "특정 activity")
        drawerlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        drawerlayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
        drawerlayout.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerOpened(View view) {
                drawerlayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
            }
            @Override
            public void onDrawerClosed(View view) {
                drawerlayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
            }
            @Override public void onDrawerStateChanged(int i) {}
            @Override public void onDrawerSlide(View view, float v) {}
        });

        //drawerlayout 터치영역 늘림(센터정도)
        try {
            Field mDragger = drawerlayout.getClass().getDeclaredField("mRightDragger");
            mDragger.setAccessible(true);
            ViewDragHelper draggerObj = (ViewDragHelper)mDragger.get(drawerlayout);
            Field mEdgeSize = draggerObj.getClass().getDeclaredField("mEdgeSize");
            mEdgeSize.setAccessible(true);
            int edge = mEdgeSize.getInt(draggerObj);
            mEdgeSize.setInt(draggerObj, edge * 10);

        } catch (Exception e) {
            MHDLog.printException(e);
        }
        ll_menu_1 = (LinearLayout) findViewById(R.id.ll_menu_1);
        ll_menu_2 = (LinearLayout) findViewById(R.id.ll_menu_2);
        ll_menu_3 = (LinearLayout) findViewById(R.id.ll_menu_3);
        ll_menu_4 = (LinearLayout) findViewById(R.id.ll_menu_4);
        ll_menu_5 = (LinearLayout) findViewById(R.id.ll_menu_5);
        ll_menu_1.setOnClickListener(this);
        ll_menu_2.setOnClickListener(this);
        ll_menu_3.setOnClickListener(this);
        ll_menu_4.setOnClickListener(this);
        ll_menu_5.setOnClickListener(this);

        findViewById(R.id.iv_logout).setOnClickListener(this);
        findViewById(R.id.ll_close).setOnClickListener(this);
    }
    /**
     * 타이틀 세팅
     */
    protected void setTitle(String title) {
        MHDLog.d(TAG, "screen title >> " + title);
        ((TextView) findViewById(R.id.title_txt)).setText(title);
    }
    /**
     * 타이틀 홈버튼 show / hide
     * screen 에 따라 버튼 display 여부 결정
     */
    protected void isVisibleHomeBtn(boolean isVisible) {
        if (isVisible)
            btn_home.setVisibility(View.VISIBLE);
        else
            btn_home.setVisibility(View.GONE);
    }
    /**
     * 타이틀 닫기버튼 show / hide
     * home 버튼을 close 버튼으로 사용할 경우 예시
     */
    protected void isVisibleCloseBtn(boolean isVisible) {
        if (isVisible) {
//            btn_home.setImageResource(R.drawable.btn_notice_close); // 닫기버튼 이미지
            btn_home.setVisibility(View.VISIBLE);
        }else {
            btn_home.setVisibility(View.GONE);
        }
    }
    /**
     * 타이틀 백버튼 show / hide
     */
    protected void isVisibleBackBtn(boolean isVisible) {
        if (isVisible)
            btn_back.setVisibility(View.VISIBLE);
        else
            btn_back.setVisibility(View.GONE);
    }
    /**
     * onResume
     * @Override
     */
    @Override
    protected void onResume() {
        super.onResume();

        mExternalLibraryManager.onActivityResumeProcess();
        // custom broadcast receiver 등록. 소스상에서 receiver 를 등록하는 예제.
        registerReceiver(customBr, new IntentFilter(MHDConstants.IntentKey.PUSH_MSG_KEY));

        MHDLog.d(TAG, "is NetworkEnabled >> " + Util.getInstance().isNetWorkEnabled(this));

        // 로그인 되어있을시만 세션체크(일정 시간이 지나면 서버에서 세션을 종료시키도록 구현한 경우 사용)
        if (MHDApplication.getInstance().getMHDSvcManager().getLoginVo() != null && MHDApplication.getInstance().getMHDSvcManager().getLoginVo().getLsiv() != null) {
//            if(this instanceof MainActivity) {	// 특정 screen 에서는 pass 시킬 경우. 보안에 상관없는 마케팅, 튜토리얼 화면 같은 경우
//                return;
//            }
//            else if(!Util.getInstance().isNetWorkEnabled(BaseActivity.this) && this instanceof BaseWebActivity) {		//webview && network disabled return
//                //웹뷰화면에서는 웹자체에서 체크 & 처리하기 때문에 return
//                return;
//            }

            requestSessionCheck();
        }
    }
    /**
     * onClick
     * @Override
     */
    @Override
    public void onClick(View v) {
        MHDLog.d(TAG, "BaseActivity onClick====================");

        if(v.getId() == R.id.title_back) {
            //onBackPressed();
            drawerlayout.openDrawer(Gravity.END);
        } else if(v.getId() == R.id.title_home) {
            drawerlayout.closeDrawer(Gravity.END);
            if(this instanceof HybridWebGuestActivity) { // 특정 액티비티에서는 해당 액티비티를 종료
                finish();
            }else{
                goMain();
            }
        } else if(v.getId() == R.id.iv_logout) {
            confirmLogout();
        } else if(v.getId() == R.id.ll_close) {
            drawerlayout.closeDrawer(Gravity.END);
        }
    }

    /**
     * onBackPressed
     * @Override
     */
    @Override
    public void onBackPressed() {
        // 메인 & 그 외 특정 화면에서는 종료처리(팝업을 띄운다)
        if (this instanceof StartActivity || this instanceof TutorialActivity || this instanceof MainActivity || this instanceof LoginActivity) {
            exitApplication();
        } else if (this instanceof RegistTodoActivity || this instanceof RegistScheduleActivity || this instanceof ModifyKidsActivity || this instanceof ModifySelfActivity || this instanceof ModifyTodoActivity || this instanceof ModifyScheduleActivity
                || this instanceof LoginActivity || this instanceof JoinActivity || this instanceof OptionAlarmActivity || this instanceof RegistKidsActivity || this instanceof KidsListActivity || this instanceof RegistSelfActivity){
            super.onBackPressed();
        } else {
            if (drawerlayout.isDrawerOpen(Gravity.END)) {
                drawerlayout.closeDrawer(Gravity.END);
            } else {
                super.onBackPressed();
            }
        }
    }
    /**
     * onPause
     * @Override
     */
    @Override
    protected void onPause() {
        mExternalLibraryManager.onActivityPauseProcess();
        super.onPause();

        try{
            unregisterReceiver(customBr);
        } catch (Exception e) {
            MHDLog.printException(e);
        }
    }
    /**
     * onDestroy
     * @Override
     */
    @Override
    protected void onDestroy() {
        try {
            // 액티비티 종료시 해당 액티비티에 묶여 있는 요청에 대한 취소 처리
            MHDNetworkInvoker.getInstance().cancelRequest(this);

            mExternalLibraryManager.onActivityDestroyProcess();
            mExternalLibraryManager =  null;
        } catch (Exception e) {
            MHDLog.printException(e);
        }

        super.onDestroy();
    }
    /**
     * application 종료 confirm 노출
     */
    public void exitApplication() {
        MHDDialogUtil.sAlert(this, R.string.confirm_exit, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(Util.getInstance().isNetWorkEnabled(BaseActivity.this)) {        // 네트워크 연결시 로그아웃
                    if(MHDApplication.getInstance().getMHDSvcManager().getLoginVo() != null && MHDApplication.getInstance().getMHDSvcManager().getLoginVo().getLsiv() != null) {
                        logout(true);
                    } else {
                        exitProcess();
                    }
                } else {        // 네트워크 미연결시 바로 종료
                    exitProcess();
                }
            }
        }, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) { }
        });
    }
    /**
     * 로그아웃 alert 노출
     */
    protected void confirmLogout() {
        MHDDialogUtil.sAlert(this, getString(R.string.confirm_logout), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(MHDApplication.getInstance().getMHDSvcManager().userLogout()){
                    Intent i = new Intent(BaseActivity.this, LoginActivity.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    startActivity(i);
                    finish();
                }else{
                    // logout 오류
                }
            }
        }, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) { }
        });
    }
    /**
     * 메뉴 > 로그아웃 버튼 처리
     */
    public void logout() {
        logout(false);
    }
    /**
     * 로그아웃(or 앱 종료)
     */
    private void logout(boolean isExit) {
        reqLogout(isExit);
    }
    /**
     * request Logout(true : 앱 종료 / false : 로그아웃 후 메인)
     */
    private void reqLogout(final boolean exit){
        try{
            // 서버 통신.
            // 입을 종료시킬지 로그아웃을 시킬지,
            // 종료든 로그아웃이든 사전조치 할 사항이 있으면 해당 사항 조치하고 종료.
        }catch(Exception e){
            MHDLog.printException(e);
        }
    }
    /**
     * 로그인화면으로 이동(로그인 화면이 있는 경우)
     * 현재는 별도의 로그인 화면이 없기 때문에 StartActivity 로 이동
     */
    public void goLogin() {
        MHDApplication.getInstance().getMHDSvcManager().setLoginVo(null);

        try {
            // cookie clear
            clearSession();
        }catch (Exception e) {
            MHDLog.printException(e);
        }

        Intent i = new Intent(BaseActivity.this, StartActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
        finish();
    }
    /**
     * Tutorial 이동
     */
    public void goTutorial() {
        Intent i = new Intent(BaseActivity.this, TutorialActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
        finish();
    }
    /**
     * go Main Activity
     */
    public void goMain() {
        if (this instanceof MainActivity ) {
            MHDDialogUtil.sAlert(this, R.string.alert_already_main, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    return;
                }
            });
            return;
        } else {
            Intent i = new Intent(BaseActivity.this, MainActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            i.putExtra(MHDConstants.KEY_IS_MAIN_ACTIVITY_REPRESH_NEEDED, true);
            startActivity(i);
            finish();
        }
    }
    /**
     * 키보드 감추기
     */
    protected void hideSoftKeyboard(View v) {
        if (v != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
        }
    }
    /**
     * 로그인 세션 체크
     */
    public void requestSessionCheck(){
        try{
            // 서버와 통신.
            // 현재 세션이 유지되어있는지 체크해서 리턴.
            // 중복로그인 체크할거면 여기서 체크 가능.
        }catch(Exception e){
            MHDLog.printException(e);
        }
    }
    /**
     * Marshmallow 권한 체크
     */
    public void checkPermissionMM(Context context, String permission) {
        if (permission.equals(Manifest.permission.READ_PHONE_STATE)) {
            // 서비스 1.0 에서는 이 권한을 요청하지 않기로 결정. 벤치마킹 앱들이 그러하다.
            TedPermission.with(context)
                    .setPermissionListener(RPSPermissionListner)
                    .setRationaleMessage(getString(R.string.alert_permission_request_RPS))
                    .setDeniedMessage(getString(R.string.alert_permission_denied))
                    .setPermissions(permission)
                    .check();
        } else if (permission.equals(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            TedPermission.with(context)
                    .setPermissionListener(WESPermissionListner)
                    .setRationaleMessage(getString(R.string.alert_permission_request_WES))
                    .setDeniedMessage(getString(R.string.alert_permission_denied))
                    .setPermissions(permission)
                    .check();
//        } else if (permission.equals(Manifest.permission.CAMERA)) {
//            TedPermission.with(context)
//                    .setPermissionListener(CAMERAPermissionListner)
//                    .setRationaleMessage(getString(R.string.alert_permission_request_CAMERA))
//                    .setDeniedMessage(getString(R.string.alert_permission_denied))
//                    .setPermissions(permission)
//                    .check();
        }
    }
    /**
     * Marshmallow 권한설정 리스너
     */
    PermissionListener RPSPermissionListner = new PermissionListener() {
        @Override
        public void onPermissionGranted() {
            MHDLog.d(TAG, "RPSPermissionListner Granted");

//            String uuid = MHDPreferences.getInstance().getPrefDvcMmbrIdNo();
//            if(uuid.length() <= 0){
//                uuid = MHDApplication.getInstance().getMHDSvcManager().getDeviceNewUuid();
//                MHDApplication.getInstance().getMHDSvcManager().setDeviceNewUuid(uuid);
//                MHDPreferences.getInstance().savePrefDvcMmbrIdNo(uuid);
//            }
            if(mContext instanceof StartActivity){
                ((StartActivity) mContext).authUser();
            }
        }

        @Override
        public void onPermissionDenied(ArrayList<String> deniedPermissions) {
            MHDLog.d(TAG, "RPSPermissionListner Denied");
        }
    };
    /**
     * Marshmallow 권한설정 리스너
     */
    PermissionListener WESPermissionListner = new PermissionListener() {
        @Override
        public void onPermissionGranted() {
            MHDLog.d(TAG, "WESPermissionListner Granted");

            if(album == true){
                getGallery();
            }else{
                takePhoto();
            }
        }

        @Override
        public void onPermissionDenied(ArrayList<String> deniedPermissions) {
            MHDLog.d(TAG, "WESPermissionListner Denied");
        }
    };
    /**
     * create temp image file
     */
    private void takePhoto() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File photoFile = null;
        try{
            photoFile = createImageFile();
        }catch(IOException e){
            MHDLog.printException(e);
        }

        // Nougat 기준으로 경로 설정의 차이. 저장소 접근방식의 변화
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            String state = Environment.getExternalStorageState();
            if(Environment.MEDIA_MOUNTED.equals(state)){
                if(photoFile != null){
                    // getUriFile의 두번째 인자는 Manifest provider의 authorites와 일치해야 한다.
                    // photoURI는 file://로 시작, FileProvider(Content Provider 하위)는 content:// 로 시작
                    // 누가 부터는 file://로 시작되는 Uri의 갑슬 다른 앱과 주고받기(Content Provider)가 불가능하다.
                    photoUri = FileProvider.getUriForFile(mContext, "com.mhd.elementary", photoFile);

                    MHDLog.i(TAG, "photoFIle >>>>>> " + photoFile.toString());
                    MHDLog.i(TAG, "photoUri >>>>>> " + photoUri.toString());

                    intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                    startActivityForResult(intent, MHDConstants.StartActivityForResult.REQUEST_TAKE_PHOTO);
                }
            }else{
                MHDLog.e(TAG, "외장 메모리 미 지원");
            }
        }else{
            if(intent.resolveActivity(getPackageManager()) != null){
                if(photoFile != null){
                    photoUri = Uri.fromFile(photoFile);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                    startActivityForResult(intent, MHDConstants.StartActivityForResult.REQUEST_TAKE_PHOTO);
                }
            }
        }
    }
    /**
     * Marshmallow 권한설정 리스너
     */
//    PermissionListener CAMERAPermissionListner = new PermissionListener() {
//        @Override
//        public void onPermissionGranted() {
//            MHDLog.d(TAG, "CAMERAPermissionListner Granted");
//
//            new IntentIntegrator((Activity)mContext).initiateScan();
//        }
//
//        @Override
//        public void onPermissionDenied(ArrayList<String> deniedPermissions) {
//            MHDLog.d(TAG, "CAMERAPermissionListner Denied");
//        }
//    };
    /**
     * create temp image file
     */
    public File createImageFile() throws IOException {
        // create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = timeStamp + ".jpg";
        File storageDir = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/return/" + imageFileName);

        // Save a file : path for use with ACTION_VIEW intents
        mCurrentPhotoPath = storageDir.getAbsolutePath();
        MHDLog.i(TAG, "mCurrentPhotoPath >>>>>>" + mCurrentPhotoPath);
        return storageDir;
    }
    /**
     * gallery
     */
    public  void getGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
        startActivityForResult(intent, MHDConstants.StartActivityForResult.REQUEST_TAKE_GALLERY);
    }
    /**
     * image crop
     */
    public void cropImage() {
        Intent cropIntent = new Intent("com.android.camera.action.CROP");
        cropIntent.setDataAndType(photoUri, "image/*");
        cropIntent.putExtra("outputX", 200);
        cropIntent.putExtra("outputY", 200);
        cropIntent.putExtra("aspectX", 1);
        cropIntent.putExtra("aspectY", 1);
        cropIntent.putExtra("scale", true);

        if(album){
            cropIntent.putExtra("output", galleryUri);
        }else{
            cropIntent.putExtra("output", photoUri);
        }

        cropIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        cropIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        startActivityForResult(cropIntent, MHDConstants.StartActivityForResult.REQUEST_IMAGE_GROP);
    }
    /**
     * image crop
     */
    public void galleryAddPhoto() {
        Intent scanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(mCurrentPhotoPath);
        Uri contentUri = Uri.fromFile(f);
        scanIntent.setData(contentUri);
        this.sendBroadcast(scanIntent);
    }
    /**
     * custom broadcast receiver( ex. 푸쉬를 받았을 경우. url 등을 체크해서 webview or native 중 적합한 화면을 띄우는 프로세스.
     */
    public BroadcastReceiver customBr = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null) {
                if ( MHDApplication.getInstance().getMHDSvcManager().isNewPush() ) {

                    PushVo pushVo = MHDApplication.getInstance().getMHDSvcManager().getPushVo();
//					MHDLog.d(TAG, "pushVo msgId >>>>>>>>>> " + pushVo.getMsgId());
//					MHDLog.d(TAG, "pushVo link >>>>>>>>>> "+pushVo.getLink());

                    String url = pushVo.getLink();
                    if(pushVo.isLink() && url != null && url.length() > 0) {		// link
                        MHDApplication.getInstance().getMHDSvcManager().setIsNewPush(false);

                        if(url.indexOf("return/") > 0) {		// 내부 링크(내부링크임을 알 수 있는 기준을 정해야한다. 이 경우는 웹뷰에서 띄우는 것)
                            Intent innerIntent = new Intent(BaseActivity.this, HybridWebActivity.class);
                            innerIntent.putExtra(MHDConstants.WebView.WEBVIEW_TARGET_URL, url);
                            startActivity(innerIntent);
                            // 현재 보는 화면에 올라오는 식으로 구현하는거라 finish 하지 않는다.
                        } else {		//외부 링크
                            Util.getInstance().goOutLink(url);
                        }
                    } else {		//일반적으로 로그인 or 푸시상세화면으로 이동
                        //start App
//                        Intent pushIntent = new Intent(context, LoginActivity.class);
//                        if(MHDApplication.getInstance().getMHDSvcManager().getLoginVo()!= null && MHDApplication.getInstance().getMHDSvcManager().getLoginVo().getLsiv() != null ){		//로그인 중
//                            pushIntent = new Intent(context, PushDetailActivity.class);
//                            pushIntent.putExtra(MHDConstants.IntentKey.PUSH_ID_KEY, pushVo.getMsgId());
//                        }
//                        pushIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
//                        startActivity(pushIntent);
                    }
                }
            }
        }
    };


    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
    }

}
