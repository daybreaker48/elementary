package com.mhd.stard.common;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.os.Build;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;
import com.kakao.sdk.user.UserApiClient;
import com.mhd.stard.MainActivity;
import com.mhd.stard.R;
import com.mhd.stard.business.model.PushVo;
import com.mhd.stard.common.vo.KidsVo;
import com.mhd.stard.common.vo.LoginVo;
import com.mhd.stard.common.vo.MenuVo;
import com.mhd.stard.common.vo.PagingBannerVo;
import com.mhd.stard.common.vo.ScheduleVo;
import com.mhd.stard.common.vo.SelfVo;
import com.mhd.stard.common.vo.SubjectVo;
import com.mhd.stard.common.vo.TodoVo;
import com.mhd.stard.common.vo.UserVo;
import com.mhd.stard.constant.MHDConstants;
import com.mhd.stard.util.MHDLog;
import com.mhd.stard.util.Util;
import com.mhd.stard.view.GlobalTabsView;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.preference.PreferenceManager;


/**
 * App Service Manager
 * Created by MH.D on 2017-04-04.
 */
public class MHDSvcManager {

	private final String TAG = MHDSvcManager.class.getName();
	/**
	 * Context
	 */
	private Context mContext;
	/**
	 * 어플리케이션 서비스(배포) 구분(개발,품질,운영 등, ALPHA, BETA, OMEGA, RELEASE)
	 */
	private String srvEnvFlag;
	/**
	 * 디바이스 ID. READ_PHONE_STATE 권한 없이 가져올 수 있다. permission 상관없이 최초 셋팅할 것.
	 */
	private String deviceNewUuid;
	/**
	 * 현재 FCM Token
	 */
	private String fcmToken;
	/**
	 * 네트워크정보 vo
	 */
	private MHDNetInfoCVO mhdNetInfoCVO;
	/**
	 * 로그인 정보 vo
	 */
	private LoginVo loginVo;
	/**
	 * 푸시 정보 vo
	 */
	private PushVo pushVo;
	/**
	 * 유저 정보 vo
	 */
	private UserVo userVo;
	/**
	 * 아이 정보 vo
	 */
	private KidsVo kidsVo;
	/**
	 * 메뉴 정보 vo
	 */
	private MenuVo menuVo;
	/**
	 * 과목 정보 vo
	 */
	private SubjectVo subjectVo;
    /**
     * 할일 정보 vo
     */
    private TodoVo todoVo;
    /**
     * 스케쥴 정보 vo
     */
    private ScheduleVo scheduleVo;
	/**
	 * 습관 정보 vo
	 */
	private SelfVo selfVo;
	/**
	 * 새로 들어온 푸시 여부(알림함 운영에 필요할 수 있다)
	 */
	private boolean isNewPush = false;
	/**
	 * 전체화면 페이징배너 최초 이미지(튜토리얼 혹은 기타 이미지가 포함된 페이징 처리에서 각 페이지의 이미지)
	 */
	private Bitmap lBitmap = null;
	/**
	 * 전체화면 페이징배너 vo
	 */
	private PagingBannerVo pgBannerVo;
	/**
	 * 앱 실행여부
	 */
	private boolean isFirstStart;
	/**
	 * Global Menu
	 */
	private GlobalTabsView globalTabsView;
	/**
	 * Current Write ViewPager Index
	 */
	private int currentWriteIndex = 1;
	/**
	 * Current Stats ViewPager Index
	 */
	private int currentStatIndex = 1;
	/**
	 * Current Read ViewPager Index
	 */
	private int currentReadIndex = 1;
	/**
	 * Current Subject
	 */
	private String currentSubject= "";
	/**
	 * Current Detail
	 */
	private String currentDetail = "";

	public SharedPreferences appPref;
	public boolean execFirst;

	/**
	 * constructor
	 */
	public MHDSvcManager(Context ctx) {
		this.mContext = ctx;
		
		init();
		
		if (this.srvEnvFlag == null) {
			throw new RuntimeException("Initialize MHDSvcManager RuntimeException");
		}
		
		load(this.srvEnvFlag);
	}
	/**
	 * init
	 */
	public void init() {
		this.srvEnvFlag = this.mContext.getString(R.string.SERVICE_ENVIRONMENT);
		this.mhdNetInfoCVO = new MHDNetInfoCVO();

		appPref = PreferenceManager.getDefaultSharedPreferences(mContext);
		this.isFirstStart = appPref.getBoolean("execFirst", true);

		// app 이 새로 init 되는 시점에 항상 uuid 값이 svcmanager에 setting 된다. 빈값은 없다.
		// preference 에는 넣지 않는다. 나중에 비교하고 갱신한다.
		setDeviceNewUuid(Util.getInstance().getDeviceUUID(this.mContext));
		MHDLog.d(TAG, "deviceNewUuid init >>> " + this.deviceNewUuid);

		// app 이 새로 init 되는 시점에서 항상 fcm token 값을 호출해서 null 이 아닌 경우 svcmanager에 setting 된다.
		FirebaseMessaging.getInstance().getToken()
				.addOnCompleteListener(new OnCompleteListener<String>() {
					@Override
					public void onComplete(@NonNull Task<String> task) {
						if (!task.isSuccessful()) {
							MHDLog.d(TAG, "Fetching FCM registration token failed", task.getException());
							return;
						}

						// Get new FCM registration token
						String currentToken = task.getResult();

						// Log and toast
						setFcmToken(currentToken == null ? "" : currentToken);
					}
				});
	}
	/**
	 * network connection info load
	 */
	public void load(String envFlag) {
		try {
			String mProtocol = ((MHDApplication) mContext).getResStrFromEntryName(MHDConstants.NetworkFlag.PROTOCOL + envFlag);
			String mDomain = ((MHDApplication) mContext).getResStrFromEntryName(MHDConstants.NetworkFlag.DOMAIN + envFlag);
			String mPort = ((MHDApplication) mContext).getResStrFromEntryName(MHDConstants.NetworkFlag.PORT + envFlag);
			String mPath = ((MHDApplication) mContext).getResStrFromEntryName(MHDConstants.NetworkFlag.PATH + envFlag);

			setFullUrl(mProtocol, mDomain, mPort, mPath);
			
		} catch (Exception e) {
			MHDLog.printException(e);
		}
	}
	/**
	 * network connection info load
	 */
	public void load(String cd, String protocol, String domain, String port, String path) {
		String mProtocol = protocol;
		String mDomain = domain;
		String mPort = port;
		String mPath = path;
		
		setFullUrl(mProtocol, mDomain, mPort, mPath);
	}
	/**
	 * full url 생성
	 */
	private void setFullUrl(String protocol, String domain, String port, String path) {
		StringBuilder urlsb = new StringBuilder(protocol)
		.append("://")
		.append(domain);
		
		if(port != null && port.length() > 0 && !"80".equals(port)) {
			urlsb.append(":")
			.append(port);
		}
		if(path != null && path.length() > 0) {
			urlsb.append(path);
		}

		this.mhdNetInfoCVO.setSvrIntroUrl(urlsb.toString());
		MHDLog.d(TAG, "webview intro url : setFullUrl() >> " + this.mhdNetInfoCVO.getSvrIntroUrl());
	}
	/**
	 * icon badge count setting
	 */
	public void updateIconBadgeCount(Context context, int count) {
		Intent intent = new Intent("android.intent.action.BADGE_COUNT_UPDATE");
		// Component
		intent.putExtra("badge_count_package_name", MHDConstants.APP_PACKAGE_NAME);
		intent.putExtra("badge_count_class_name", getLauncherClassName(context));

		// badge count
		intent.putExtra("badge_count", count);

		if( Build.VERSION.SDK_INT > Build.VERSION_CODES.GINGERBREAD_MR1 ) {
			intent.setFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES); //FLAG_INCLUDE_STOPPED_PACKAGES, 0x00000020
			//ref URL : http://arabiannight.tistory.com/entry/%EC%95%88%EB%93%9C%EB%A1%9C%EC%9D%B4%EB%93%9CAndroid-ICS-%EB%B6%80%ED%84%B0-Broadcast-%EC%8B%9C-%EC%A3%BC%EC%9D%98%ED%95%B4%EC%95%BC-%ED%95%A0-%EC%A0%90-Stopped-Process
		}

		// send
		context.sendBroadcast(intent);
	}
	private String getLauncherClassName(Context context) {
		Intent intent = new Intent(Intent.ACTION_MAIN);
		intent.addCategory(Intent.CATEGORY_LAUNCHER);
		intent.setPackage(context.getPackageName());

		List<ResolveInfo> resolveInfoList = context.getPackageManager().queryIntentActivities(intent, 0);
		if(resolveInfoList != null && resolveInfoList.size() > 0) {
			return resolveInfoList.get(0).activityInfo.name;
		} else {
			return MHDConstants.CommonCode.BADGE_COUNT_CLASS_NAME;
		}
	}
	/**
	 * 로그인 세션 작업 여부 등과 같이 대부분(혹은 일부)의 activity( 화면 ) 단에서 check가 필요한 것들
	 */
	public boolean isLoginTaskRunning() {
		boolean isLogin = MHDApplication.getInstance().getMHDSvcManager().getUserVo() != null;
		return isLogin;
	}
	/**
	 * logout. 모든 vo를 비우고 로그인화면으로
	 */
	public boolean userLogout(Context context) {
		try{
			setPushVo(null);
			setUserVo(null);
			setKidsVo(null);
			setMenuVo(null);
			setSubjectVo(null);
			setTodoVo(null);
			setScheduleVo(null);
			setSelfVo(null);

			MHDPreferences.getInstance().savePrefUserID("");
			MHDPreferences.getInstance().savePrefUserPWD("");

			//////////////// 구글 기존 로그인정보 조회
			// DEFAULT_SIGN_IN parameter는 유저의 ID와 기본적인 프로필 정보를 요청하는데 사용된다.
			GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
					.requestIdToken("790798364026-gp83cj2jgn84d9u0hjh17jfcjjdtq7i4.apps.googleusercontent.com")
					.requestEmail() // email addresses 요청
					.build();
			// 위에서 만든 GoogleSignInOptions을 사용해 GoogleSignInClient 객체를 만듬
			GoogleSignInClient mGoogleSignInClient = GoogleSignIn.getClient((MainActivity)context, gso);
			mGoogleSignInClient.signOut()
					.addOnCompleteListener((MainActivity)context, new OnCompleteListener<Void>() {
						@Override
						public void onComplete(@NonNull Task<Void> task) {
							MHDLog.d(TAG, "google 로그아웃 성공");
						}
					});
			UserApiClient.getInstance().logout (error -> {
				if (error != null) {
					MHDLog.d(TAG, "kakao 로그아웃 실패, SDK에서 토큰 삭제됨");
				}else{
					MHDLog.d(TAG, "kakao 로그아웃 성공, SDK에서 토큰 삭제됨");
				}
				return null;
			});

			MHDPreferences.getInstance().savePrefLogout(true);
		} catch (Exception e) {
			MHDLog.printException(e);

			return false;
		}
		return true;
	}
	/**
	 * get network info cvo
	 */
	public MHDNetInfoCVO getMHDNetInfoCVO() {
		return this.mhdNetInfoCVO;
	}
	/**
	 * set network info cvo
	 */
	public void setMHDNetInfoCVO(MHDNetInfoCVO infoCvo) {
		this.mhdNetInfoCVO = infoCvo;
	}
	/**
	 * get deviceid
	 */
	public String getDeviceNewUuid() {
		return this.deviceNewUuid;
	}
	/**
	 * set deviceid
	 * MHDPreferences 도 비교하여 갱신
	 */
	public void setDeviceNewUuid(String did) {
		this.deviceNewUuid = did;
	}
	/**
	 * get fcm token
	 */
	public String getFcmToken() {
		return this.fcmToken;
	}
	/**
	 * set fcm token
	 * * MHDPreferences 도 비교하여 갱신
	 */
	public void setFcmToken(String tok) {
		this.fcmToken = tok;
	}
	/**
	 * get login info vo
	 */
	public LoginVo getLoginVo() {
		return loginVo;
	}
	/**
	 * set login info vo
	 */
	public void setLoginVo(LoginVo loginVo) {
		this.loginVo = loginVo;
	}
	/**
	 * 푸시 vo getter
	 */
	public PushVo getPushVo() {
		return pushVo;
	}
	/**
	 * 푸시 vo setter
	 */
	public void setPushVo(PushVo pushVo) {
		this.pushVo = pushVo;
	}
	/**
	 * get User info vo
	 */
	public UserVo getUserVo() {
		return userVo;
	}
	/*
	 * set User info vo
	 */
	public void setUserVo(UserVo userVo) {
		this.userVo = userVo;
	}
	/**
	 * get menu info vo
	 */
	public MenuVo getMenuVo() {
		return menuVo;
	}
	/*
	 * set menu info vo
	 */
	public void setMenuVo(MenuVo menuVo) {
		this.menuVo = menuVo;
	}
	/**
	 * get Kids info vo
	 */
	public KidsVo getKidsVo() {
		return kidsVo;
	}
	/*
	 * set Kids info vo
	 */
	public void setKidsVo(KidsVo kidsVo) {
		this.kidsVo = kidsVo;
	}
	/**
	 * get Subject Info
	 */
	public SubjectVo getSubjectVo() {
		return subjectVo;
	}
	/**
	 * set Subject Info
	 */
	public void setSubjectVo(SubjectVo subjectVo) {
		this.subjectVo = subjectVo;
	}
    /**
     * get self Info
     */
    public SelfVo getSelfVo() {
        return selfVo;
    }
    /**
     * set self Info
     */
    public void setSelfVo(SelfVo selfVo) {
        this.selfVo = selfVo;
    }
	/**
	 * get schedule Info
	 */
	public ScheduleVo getScheduleVo() {
		return scheduleVo;
	}
	/**
	 * set schedule Info
	 */
	public void setScheduleVo(ScheduleVo scheduleVo) {
		this.scheduleVo = scheduleVo;
	}
	/**
	 * get todo Info
	 */
	public TodoVo getTodoVo() {
		return todoVo;
	}
	/**
	 * set todo Info
	 */
	public void setTodoVo(TodoVo todoVo) {
		this.todoVo = todoVo;
	}
	/**
	 * get new push boolean
	 */
	public boolean isNewPush() {
		return isNewPush;
	}
	/**
	 * set new push boolean
	 */
	public void setIsNewPush(boolean isNewPush) {
		this.isNewPush = isNewPush;
	}
	/**
	 * get paging banner bitmap
	 */
	public Bitmap getlBitmap() {
		return lBitmap;
	}
	/**
	 * set paging banner bitmap
	 */
	public void setlBitmap(Bitmap lBitmap) {
		this.lBitmap = lBitmap;
	}
	/**
	 * get paging banner list vo
	 */
	public PagingBannerVo getPgBannerVo() {
		return pgBannerVo;
	}
	/**
	 * set paging banner list vo
	 */
	public void setPgBannerVo(PagingBannerVo pgBannerVo) {
		this.pgBannerVo = pgBannerVo;
	}
	/**
	 * get GlobalTabsView
	 */
	public GlobalTabsView getGlobalTabsView() {
		return globalTabsView;
	}
	/**
	 * set GlobalTabsView
	 */
	public void setGlobalTabsView(GlobalTabsView globalTabsView) {
		this.globalTabsView = globalTabsView;
	}
	/**
	 * get Current Write ViewPager Index
	 */
	public int getCurrentWriteIndex() {
		return currentWriteIndex;
	}
	/**
	 * set Current Write ViewPager Index
	 */
	public void setCurrentWriteIndex(int mCurrentWriteIndex) {
		currentWriteIndex = mCurrentWriteIndex;
	}
	/**
	 * get Current Stats ViewPager Index
	 */
	public int getCurrentStatIndex() {
		return currentStatIndex;
	}
	/**
	 * set Current Write ViewPager Index
	 */
	public void setCurrentStatIndex(int mCurrentStatIndex) {
		currentStatIndex = mCurrentStatIndex;
	}
	/**
	 * get Current Read ViewPager Index
	 */
	public int getCurrentReadIndex() {
		return currentReadIndex;
	}
	/**
	 * set Current Write ViewPager Index
	 */
	public void setCurrentReadIndex(int mCurrentReadIndex) {
		currentReadIndex = mCurrentReadIndex;
	}
	/**
	 * get first start
	 */
	public boolean getIsFirstStart() {
		return isFirstStart;
	}
	/**
	 * set first start
	 */
	public void setIsFirstStart(boolean firstStart) {
		isFirstStart = firstStart;
	}
	/**
	 * get current subject
	 */
	public String getCurrentSubject() {
		return currentSubject;
	}
	/**
	 * set current subject
	 */
	public void setCurrentSubject(String currentSubject) {
		currentSubject = currentSubject;
	}
	/**
	 * get current detail
	 */
	public String getCurrentDetail() {
		return currentDetail;
	}
	/**
	 * set current detail
	 */
	public void setCurrentDetail(String currentDetail) {
		currentDetail = currentDetail;
	}
}
