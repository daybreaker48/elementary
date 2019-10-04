package com.mhd.boomerang.constant;

import com.mhd.boomerang.common.MHDApplication;

/**
 * 상수 정의
 * Created by MH.D on 2017-03-31.
 */
public class MHDConstants {
	
	/**
	 * 앱 패키지 네임(유니크 코드로 활용)
	 * */
	public static final String APP_PACKAGE_NAME = MHDApplication.getInstance().getApplicationContext().getPackageName();
	/**
	 * 서비스 메인으로 이동시 메인 리프레쉬(초기화) 여부
	 * */
	public static final String KEY_IS_MAIN_ACTIVITY_REPRESH_NEEDED = "key_main_activity_refresh_needed";
	/**
	 * user agent key
	 * */
	public class UserAgent {
		/** 앱 & 모바일웹 여부(A or W) */
		public static final String AGENT_CHANNEL = "mSrvChnl=A";
		/** 모바일 OS 구분코드 : 01 : 안드로이드 */
		public static final String AGENT_CHANNEL_OS = "mSrvChnlOs=AND";
		/** 모바일 OS 버전 */
		public static final String AGENT_CHANNEL_OS_VER = "mSrvChnlOsVer";
		/** 모바일앱명 : RETURN */
		public static final String AGENT_NAME = "mSrvNm=RETURN";
		/** 모바일앱버전명 */
		public static final String AGENT_APP_VER = "mSrvAppVer";
		/** 앱스토어구분코드 : Google Play Store:1 */
		public static final String AGENT_APP_MARKET = "mSrvAppMkt=1";
		/** 모바일 디바이스식별번호 */
		public static final String AGENT_DEVICE_UID = "mDvcUID";
		/** 보안키값(옵션) */
		public static final String AGENT_CERTIFICATE_KEY = "mCertKey";
	}
	/**
	 * 웹뷰 관련
	 * */
	public class WebView {
		/** 웹뷰 전달용 url 키 */
		public static final String WEBVIEW_TARGET_URL = "webview_target_url";
		/** 웹뷰 전달용 title 키 */
		public static final String WEBVIEW_TARGET_TITLE = "webview_target_title";
		/** 웹뷰 전달용 params 키 */
		public static final String WEBVIEW_TARGET_PARAMS = "webview_target_params";
		/** 브릿지 인터페이스명 */
		public static final String WEBVIEW_INTERFACE = "AppWebBridge";
		/** network error broadcast action */
		public static final String BROAD_WEB_ERR = "action.network.error";
		/** network error broadcast url */
		public static final String BROAD_WEB_ERR_URL = "webview_network_error_url";
	}
	/**
	 * 서버 Request 시 사용하는 서버-클라이언트 합의된 Flag을 모두 지정.
	 * */
	public class ReqSend {
		public static final String MULTI_REQUEST = "LR";	// 다수 조회
		public static final String SINGLE_REQUEST = "DR";	// 단일 조회
		public static final String MULTI_UPDATE = "MU";		// 다수 갱신
		public static final String SINGLE_UPDATE = "SU";	// 단일 갱신
	}
	/**
	 * activity 이동시에 넘겨받는 intent key
	 * */
	public class IntentKey {
		public static final String UNIQUE_CODE = "unique_code";
		public static final String RECEIVE_CODE = "receive_code";
		public static final String PUSH_ID_KEY = "PUSH_ID_KEY";
		public static final String PUSH_MSG_KEY = "PUSH_MSG_KEY";
	}
	/**
	 * 네트워크 정보
	 * */
	public class NetworkFlag {
		public static final String PROTOCOL = "PROTOCOL_";
		public static final String DOMAIN = "DOMAIN_";
		public static final String PORT = "PORT_";
		public static final String PATH = "PATH_";
	}
	/**
	 * 공통코드 조회
	 * */
	public class CommonCode {
		public static final String NEED_FIELD = "need_field";
		public static final String BADGE_COUNT_CLASS_NAME = "com.mhd.boomerang.MainActivity";
	}
	/**
	 * startActivityForResult
	 * */
	public class StartActivityForResult {
		public static final int REQUEST_TAKE_PHOTO = 10001;
		public static final int REQUEST_TAKE_GALLERY = 10002;
		public static final int REQUEST_IMAGE_GROP = 10003;
	}
}
