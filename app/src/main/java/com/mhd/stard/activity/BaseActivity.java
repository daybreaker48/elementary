package com.mhd.stard.activity;

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
import com.mhd.stard.MainActivity;
import com.mhd.stard.R;
import com.mhd.stard.business.ExternalLibraryManager;
import com.mhd.stard.business.model.PushVo;
import com.mhd.stard.common.MHDApplication;
import com.mhd.stard.constant.MHDConstants;
import com.mhd.stard.network.MHDNetworkInvoker;
import com.mhd.stard.network.manager.ResponseListener;
import com.mhd.stard.util.MHDDialogUtil;
import com.mhd.stard.util.MHDLog;
import com.mhd.stard.util.Util;
import com.mhd.stard.webview.activity.HybridWebActivity;
import com.mhd.stard.webview.activity.HybridWebGuestActivity;

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
     * ?????? UI( home, back, logo, menu ... ) ?????? ??? ???????????? ??????
     */
    private ImageView btn_back;
    private ImageView btn_home;
    /**
     * ????????????????????? controller
     */
    private ExternalLibraryManager mExternalLibraryManager;
    /**
     * ?????? ??????
     * */
    private DrawerLayout drawerlayout;
    private LinearLayout ll_menu_1, ll_menu_2, ll_menu_3, ll_menu_4, ll_menu_5;
    /**
     * Activity network Listener
     * */
    public ResponseListener responseListener;
    /**
     * ?????? activity context
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
    public int nvCnt2 = 0;
    public String nvMsg2 = "";

    /**
     * onCreate
     * @Override
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        MHDLog.d(TAG, this.getClass().getSimpleName() + " activity created=========" + MHDApplication.getInstance().isAnyActivityInvokedOnce());
        mExternalLibraryManager = new ExternalLibraryManager(this, isEssentialCheckNeeded());

        // Application ???????????? ?????? ???????????????.
        if(MHDApplication.getInstance().isAnyActivityInvokedOnce() == false) {
            MHDApplication.getInstance().setAnyActivityInvokedOnce(true);

            boolean isInitializeStarted = mExternalLibraryManager.initializeExternalLibrary();
            if(isInitializeStarted == false) {
                // network ???????????????
                MHDDialogUtil.sAlert(this, R.string.alert_network_disable, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        exitProcess();
                    }
                }, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //???????????? ?????? alert - ???????????? ?????? ??????.
                        //exitProcess();
                    }
                });
                return;
            }
        }

        // ????????????????????? Main Thread ??? ???????????? ??????.
        mExternalLibraryManager.onActivityCreateProcess();

        // Activity volley ?????? ResponseListner ??????
        // ???????????? ??????????????? request ????????? ???????????? ?????? ????????????.
        responseListener = new ResponseListener() {
            @Override
            public void onError(String message) {
                MHDLog.e(TAG, "ResponseListener onError >>>>>>>>>> " + message.toString());
                MHDDialogUtil.sAlert(mContext, "????????? ???????????? ?????? ????????? ?????????????????????.", new DialogInterface.OnClickListener() {
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
     * volley listner ??? success response ??????.
     * ????????? ????????? override ?????? ??????.
     * ????????????, ?????? Error ???????????? MHDNetworkInvoker ?????? ????????????
     * ??? ???????????? result "S" ??? ?????? ??????. "S" ?????? ?????? ???????????? ????????? ??????????????? ???????????? ??????.
     */
    protected boolean networkResponseProcess(String result) {
        MHDLog.d(TAG, "networkResponseProcess >>> " + result);

        boolean resultFlag = false;
        try{
            JSONObject nvJsonObject = new JSONObject(result);
            // ????????????. ????????? ????????? ??? M, S(Success) ?????? ???.
            nvResultCode = nvJsonObject.getString("result");
            String nvData = nvJsonObject.getString("data");
            JSONObject nvJsonDataObject = new JSONObject(nvData);
            nvJsonDataString = nvJsonDataObject.toString().trim();

            nvApi = nvJsonDataObject.getString("api");
            nvMsg = nvJsonDataObject.getString("msg");
            if("S".equals(nvResultCode)){  // Success. data ????????? ?????? ??? ?????? api, (cnt), msg ????????? ??????.
                nvCnt = nvJsonDataObject.getInt("cnt");

                //// ????????? ?????? ????????? json??? ????????? ?????? ?????????.
                //// ?????? ?????? null????????? ????????? ?????? ????????????.
                if(!nvJsonDataObject.isNull("cn2")){
                    nvCnt2 = nvJsonDataObject.getInt("cn2");
                    nvMsg2 = nvJsonDataObject.getString("sub");
                }
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
     * ?????? ??????????????? ?????? ????????? ???????????? ?????? default TRUE
     * ??????????????? override ?????? false ??? return ??????.
     */
    protected boolean isEssentialCheckNeeded() {
        return true;
    }
    /**
     * ?????????????????? ??????
     */
    public void exitProcess() {
        // Application ?????? ??? ???????????? ???????????? ?????? ??????
        try {
            MHDNetworkInvoker.getInstance().cancelRequest(this);
            //TODO ?????? ????????? ??????????????? ?????? ????????????

        } catch (Exception e) {
            MHDLog.printException(e);
        }

        // ?????? ?????????
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
     * ????????????(initialize) : setContentView ??? ????????? ????????????. (??? ?????? ??????????????? ????????? ??????????????? ??????)
     */
    protected void initialize(int layoutID) {
        setContentView(layoutID);
//        initTitleViews();
//        initMenuViews();
    }
    /**
     * ????????????(initialize)
     */
    protected void initialize(int layoutID, String title) {
        setContentView(layoutID);
        setTitle(title);
        initTitleViews();
        initMenuViews();
    }
    /**
     * ????????????(initialize)
     */
    protected void initialize(int layoutID, int title) {
        setContentView(layoutID);
        setTitle(getString(title));
        initTitleViews();
        initMenuViews();
    }
    /**
     * ????????????(initialize - noTitle)
     */
    protected void initializeNoTitle(int layoutID) {
        setContentView(layoutID);
        initMenuViews();
    }
    /**
     * title area init
     */
    private void initTitleViews() {
        // title area ??? ???????????? activity??? return.
//        if (this instanceof MainActivity)
//            return;

        btn_back = (ImageView) findViewById(R.id.title_back);   // back, menu, home ??????
        btn_home = (ImageView) findViewById(R.id.title_home);   // back, menu, home ??????

        btn_back.setOnClickListener(this);
        btn_home.setOnClickListener(this);
    }
    /**
     * side menu area init
     */
    private void initMenuViews() {
        // side menu ??? ???????????? activity??? return.
        // ????????? side menu ??? ?????????????????? ?????? ??????.
        if (true) return;

        drawerlayout = (DrawerLayout)findViewById(R.id.drawerlayout);
        drawerlayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
//        if (this instanceof "?????? activity")
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

        //drawerlayout ???????????? ??????(????????????)
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
     * ????????? ??????
     */
    protected void setTitle(String title) {
        MHDLog.d(TAG, "screen title >> " + title);
        ((TextView) findViewById(R.id.title_txt)).setText(title);
    }
    /**
     * ????????? ????????? show / hide
     * screen ??? ?????? ?????? display ?????? ??????
     */
    protected void isVisibleHomeBtn(boolean isVisible) {
        if (isVisible)
            btn_home.setVisibility(View.VISIBLE);
        else
            btn_home.setVisibility(View.GONE);
    }
    /**
     * ????????? ???????????? show / hide
     * home ????????? close ???????????? ????????? ?????? ??????
     */
    protected void isVisibleCloseBtn(boolean isVisible) {
        if (isVisible) {
//            btn_home.setImageResource(R.drawable.btn_notice_close); // ???????????? ?????????
            btn_home.setVisibility(View.VISIBLE);
        }else {
            btn_home.setVisibility(View.GONE);
        }
    }
    /**
     * ????????? ????????? show / hide
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
        // custom broadcast receiver ??????. ??????????????? receiver ??? ???????????? ??????.
        registerReceiver(customBr, new IntentFilter(MHDConstants.IntentKey.PUSH_MSG_KEY));

        MHDLog.d(TAG, "is NetworkEnabled >> " + Util.getInstance().isNetWorkEnabled(this));

        // ????????? ?????????????????? ????????????(?????? ????????? ????????? ???????????? ????????? ?????????????????? ????????? ?????? ??????)
        if (MHDApplication.getInstance().getMHDSvcManager().getLoginVo() != null && MHDApplication.getInstance().getMHDSvcManager().getLoginVo().getLsiv() != null) {
//            if(this instanceof MainActivity) {	// ?????? screen ????????? pass ?????? ??????. ????????? ???????????? ?????????, ???????????? ?????? ?????? ??????
//                return;
//            }
//            else if(!Util.getInstance().isNetWorkEnabled(BaseActivity.this) && this instanceof BaseWebActivity) {		//webview && network disabled return
//                //????????????????????? ??????????????? ?????? & ???????????? ????????? return
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
            if(this instanceof HybridWebGuestActivity) { // ?????? ????????????????????? ?????? ??????????????? ??????
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
        // ?????? & ??? ??? ?????? ??????????????? ????????????(????????? ?????????)
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
            // ???????????? ????????? ?????? ??????????????? ?????? ?????? ????????? ?????? ?????? ??????
            MHDNetworkInvoker.getInstance().cancelRequest(this);

            mExternalLibraryManager.onActivityDestroyProcess();
            mExternalLibraryManager =  null;
        } catch (Exception e) {
            MHDLog.printException(e);
        }

        super.onDestroy();
    }
    /**
     * application ?????? confirm ??????
     */
    public void exitApplication() {
        MHDDialogUtil.sAlert(this, R.string.confirm_exit, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(Util.getInstance().isNetWorkEnabled(BaseActivity.this)) {        // ???????????? ????????? ????????????
                    if(MHDApplication.getInstance().getMHDSvcManager().getLoginVo() != null && MHDApplication.getInstance().getMHDSvcManager().getLoginVo().getLsiv() != null) {
                        logout(true);
                    } else {
                        exitProcess();
                    }
                } else {        // ???????????? ???????????? ?????? ??????
                    exitProcess();
                }
            }
        }, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) { }
        });
    }
    /**
     * ???????????? alert ??????
     */
    protected void confirmLogout() {
        MHDDialogUtil.sAlert(this, getString(R.string.confirm_logout), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(MHDApplication.getInstance().getMHDSvcManager().userLogout(BaseActivity.this)){
                    Intent i = new Intent(BaseActivity.this, LoginActivity.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    startActivity(i);
                    finish();
                }else{
                    // logout ??????
                }
            }
        }, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) { }
        });
    }
    /**
     * ?????? > ???????????? ?????? ??????
     */
    public void logout() {
        logout(false);
    }
    /**
     * ????????????(or ??? ??????)
     */
    private void logout(boolean isExit) {
        reqLogout(isExit);
    }
    /**
     * request Logout(true : ??? ?????? / false : ???????????? ??? ??????)
     */
    private void reqLogout(final boolean exit){
        try{
            // ?????? ??????.
            // ?????? ??????????????? ??????????????? ?????????,
            // ????????? ?????????????????? ???????????? ??? ????????? ????????? ?????? ?????? ???????????? ??????.
        }catch(Exception e){
            MHDLog.printException(e);
        }
    }
    /**
     * ????????????????????? ??????(????????? ????????? ?????? ??????)
     * ????????? ????????? ????????? ????????? ?????? ????????? StartActivity ??? ??????
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
     * Tutorial ??????
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
     * ????????? ?????????
     */
    protected void hideSoftKeyboard(View v) {
        if (v != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
        }
    }
    /**
     * ????????? ?????? ??????
     */
    public void requestSessionCheck(){
        try{
            // ????????? ??????.
            // ?????? ????????? ????????????????????? ???????????? ??????.
            // ??????????????? ??????????????? ????????? ?????? ??????.
        }catch(Exception e){
            MHDLog.printException(e);
        }
    }
    /**
     * Marshmallow ?????? ??????
     */
    public void checkPermissionMM(Context context, String permission) {
        if (permission.equals(Manifest.permission.READ_PHONE_STATE)) {
            // ????????? 1.0 ????????? ??? ????????? ???????????? ????????? ??????. ???????????? ????????? ????????????.
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
     * Marshmallow ???????????? ?????????
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
     * Marshmallow ???????????? ?????????
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

        // Nougat ???????????? ?????? ????????? ??????. ????????? ??????????????? ??????
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            String state = Environment.getExternalStorageState();
            if(Environment.MEDIA_MOUNTED.equals(state)){
                if(photoFile != null){
                    // getUriFile??? ????????? ????????? Manifest provider??? authorites??? ???????????? ??????.
                    // photoURI??? file://??? ??????, FileProvider(Content Provider ??????)??? content:// ??? ??????
                    // ?????? ????????? file://??? ???????????? Uri??? ?????? ?????? ?????? ????????????(Content Provider)??? ???????????????.
                    photoUri = FileProvider.getUriForFile(mContext, "com.mhd.stard", photoFile);

                    MHDLog.i(TAG, "photoFIle >>>>>> " + photoFile.toString());
                    MHDLog.i(TAG, "photoUri >>>>>> " + photoUri.toString());

                    intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                    startActivityForResult(intent, MHDConstants.StartActivityForResult.REQUEST_TAKE_PHOTO);
                }
            }else{
                MHDLog.e(TAG, "?????? ????????? ??? ??????");
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
     * Marshmallow ???????????? ?????????
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
     * custom broadcast receiver( ex. ????????? ????????? ??????. url ?????? ???????????? webview or native ??? ????????? ????????? ????????? ????????????.
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

                        if(url.indexOf("return/") > 0) {		// ?????? ??????(?????????????????? ??? ??? ?????? ????????? ???????????????. ??? ????????? ???????????? ????????? ???)
                            Intent innerIntent = new Intent(BaseActivity.this, HybridWebActivity.class);
                            innerIntent.putExtra(MHDConstants.WebView.WEBVIEW_TARGET_URL, url);
                            startActivity(innerIntent);
                            // ?????? ?????? ????????? ???????????? ????????? ?????????????????? finish ?????? ?????????.
                        } else {		//?????? ??????
                            Util.getInstance().goOutLink(url);
                        }
                    } else {		//??????????????? ????????? or ???????????????????????? ??????
                        //start App
//                        Intent pushIntent = new Intent(context, LoginActivity.class);
//                        if(MHDApplication.getInstance().getMHDSvcManager().getLoginVo()!= null && MHDApplication.getInstance().getMHDSvcManager().getLoginVo().getLsiv() != null ){		//????????? ???
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
