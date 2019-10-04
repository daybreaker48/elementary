package com.mhd.boomerang.webview.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.webkit.CookieSyncManager;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.mhd.boomerang.R;
import com.mhd.boomerang.activity.BaseActivity;
import com.mhd.boomerang.common.MHDApplication;
import com.mhd.boomerang.constant.MHDConstants;
import com.mhd.boomerang.util.MHDDialogUtil;
import com.mhd.boomerang.util.MHDLog;
import com.mhd.boomerang.util.Util;
import com.mhd.boomerang.webview.appinterface.BaseWebCallback;
import com.mhd.boomerang.webview.client.BaseWebChromeClient;
import com.mhd.boomerang.webview.view.BaseWebView;

import org.json.JSONObject;


/**
 * base webview activity
 * Created by MH.D on 2017-04-07.
 */
public class BaseWebActivity extends BaseActivity implements BaseWebCallback {

	private final String TAG = BaseWebActivity.class.getName();
	/**
	 * parent view
	 * */
	protected RelativeLayout webview_parent;
	 /**
	  * webview
	  * */
	protected BaseWebView webview;
	/**
	 * title area view
	 * */
	protected FrameLayout layout_titlearea;
//	protected FrameLayout layout_bottomarea;
	/**
	 * custom webview chrome client
	 * */
	protected BaseWebChromeClient mWebChromeClient;
	/**
	 * network check broadcast receiver
	 */
	public BroadcastReceiver netErrBr = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent != null) {
				MHDLog.e(TAG, "NetErrorReceiver onReceive==================");
				String failurl = intent.getExtras().getString(MHDConstants.WebView.BROAD_WEB_ERR_URL);

				MHDDialogUtil.sAlert(BaseWebActivity.this, R.string.alert_network_disable, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						exitProcess();
					}
				});
			}
		}
	};
	/**
	 * onCreate
	 * @Override
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		MHDLog.d(TAG, "BaseWebActivity onCreate");

		initialize(R.layout.activity_hybridweb);
//		isVisibleHomeBtn(false); // ex) 웹뷰 화면에서 특정 ui 조정을 해야한다면 여기서 하면 되는 것.

		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
			//noinspection deprecation
			CookieSyncManager.createInstance(this);
		}
	}
	/**
	 * onResume
	 * @Override
	 */
	@Override
	protected void onResume() {
		super.onResume();
		MHDLog.d(TAG, "onResume==============================");

		registerReceiver(netErrBr, new IntentFilter(MHDConstants.WebView.BROAD_WEB_ERR));
		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
			//noinspection deprecation
			CookieSyncManager.getInstance().startSync();
		}

//		if(MHDApplication.getInstance().getMHDSvcManager().getLoginVo() != null) { // 로그인 상태에서만 웹뷰를 쓸 일이 있다면
			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					// 웹페이지의 pageshow 이벤트를 강제발생시킨다.
					if (webview_parent.getChildCount() > 1) {
						BaseWebView child = (BaseWebView) webview_parent.getChildAt(webview_parent.getChildCount() - 1);
						child.loadUrl(getJavaLoad("$(document).trigger('pageshow')"));        //call javascript window.pageshow
					} else {
						webview.loadUrl(getJavaLoad("$(document).trigger('pageshow')"));        //call javascript window.pageshow
					}
					MHDLog.d(TAG, "run >> " + getJavaLoad("$(document).trigger('pageshow')"));

				}
			});
//		}
	}
	/**
	 * onPause
	 * @Override
	 */
	@Override
	protected void onPause() {
		super.onPause();

		MHDLog.d(TAG, "onPause==============================");

		// 리시버 해제
		try {
			unregisterReceiver(netErrBr);
		}catch (Exception e) {
			MHDLog.printException(e);
		}
		// 쿠키 해제
		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
			//noinspection deprecation
			if(CookieSyncManager.getInstance() != null) {
				CookieSyncManager.getInstance().stopSync();
			}
		}

//		if(MHDApplication.getInstance().getMHDSvcManager().getLoginVo() != null) { // 로그인 상태에서만 웹뷰를 쓸 일이 있다면
			// 웹페이지의 pagehide 이벤트를 강제발생시킨다.
			if (webview_parent.getChildCount() > 1) {        //child webview history back
				BaseWebView child = (BaseWebView) webview_parent.getChildAt(webview_parent.getChildCount() - 1);
				child.loadUrl(getJavaLoad("$(document).trigger('pagehide')"));        //call javascript window.pagehide
			}
			else {
				webview.loadUrl(getJavaLoad("$(document).trigger('pagehide')"));        //call javascript window.pagehide
			}
			MHDLog.d(TAG, "run >> " + getJavaLoad("$(document).trigger('pagehide')"));
//		}
	}
	/**
	 * onDestroy
	 * @Override
	 */
	@Override
	protected void onDestroy() {
		if(webview_parent != null && webview_parent.getChildCount() > 0) {		//child 존재시 각 child destroy
			for(int i=0; i<webview_parent.getChildCount(); i++) {
				BaseWebView child = (BaseWebView)webview_parent.getChildAt(i);
				webview_parent.removeView(child);
				child.destroy();
			}
		}

//		try {
//			unregisterReceiver(netErrBr);
//		}catch (Exception e) {
//			MHDLog.printException(e);
//		}

		super.onDestroy();
	}
	/**
	 * init webview
	 */
	protected void initWebview() {
		webview_parent = (RelativeLayout)findViewById(R.id.webView_container);
		webview = (BaseWebView)findViewById(R.id.webView);
		layout_titlearea = (FrameLayout)findViewById(R.id.layout_title);
		layout_titlearea.setVisibility(View.VISIBLE);
		// 하단메뉴 hidden
//		layout_bottomarea = (FrameLayout)findViewById(R.id.layout_bottom);
//		layout_bottomarea.setVisibility(View.GONE);
	}
	/**
	 * webview url value check
	 */
	protected void getValues() {
		if(getIntent().getExtras() != null) {		//intent check
			String target_url = getIntent().getExtras().getString(MHDConstants.WebView.WEBVIEW_TARGET_URL, "");		//string value check

			if(target_url.length() > 0) {		//string url 존재시 로드
				MHDLog.d(TAG, "target_url is String=============================");
				String title = getIntent().getExtras().getString(MHDConstants.WebView.WEBVIEW_TARGET_TITLE);
				String url = getIntent().getExtras().getString(MHDConstants.WebView.WEBVIEW_TARGET_URL);
				String param = getIntent().getExtras().getString(MHDConstants.WebView.WEBVIEW_TARGET_PARAMS, "");

				if (!"".equals(param)) {
					webview.loadUrl(url + "?" + param);
				} else {
					webview.loadUrl(url);
				}
				setTitle(title);
			} else {		//int형 resource id 존재시 로드 (strings.xml 쪽에 도메인:포트를 제외한 하부 경로들)
				MHDLog.d(TAG, "target_url is Integer=============================");
				try {
					MHDLog.d(TAG, "get value >> " + getIntent().getExtras().getInt(MHDConstants.WebView.WEBVIEW_TARGET_URL));

					int resId = getIntent().getExtras().getInt(MHDConstants.WebView.WEBVIEW_TARGET_URL);		//resource id get
					String tagName = MHDApplication.getInstance().getResTagName(resId);		//resource id tag get

					MHDLog.d(TAG, "get value >> resName >> " + tagName);

					String url = getSubUrl(getString(resId));
					String param = getIntent().getExtras().getString(MHDConstants.WebView.WEBVIEW_TARGET_PARAMS, "");

					MHDLog.d(TAG, "get value >> url >> " + url);
					MHDLog.d(TAG, "get value >> param >> " + param);

					if (!"".equals(param)) {
						MHDLog.d(TAG, "param url >> " + url + "?" + param);
						webview.loadUrl(url + "?" + param);
					} else {
						webview.loadUrl(url);
					}
				} catch (Exception e) {
					MHDLog.printException(e);
				}
			}
		}
	}
	/**
	 * get sub full url
	 */
	public static String getSubUrl(String url) {
		return MHDApplication.getInstance().getMHDSvcManager().getMHDNetInfoCVO().getSvrIntroUrl() + url;
	}
	/**
	 * call. hybrid javascript function
	 */
	private String getJavaLoad(String name, JSONObject arg) {
		String ret = String.format("javascript:%s('%s')", name, String.valueOf(arg));
		return ret;
	}
	/**
	 * call. hybrid javascript function
	 */
	public String getJavaLoad(String name, String arg) {
		String ret = String.format("javascript:%s('%s')", name, arg);
		return ret;
	}
	/**
	 * call. hybrid javascript function
	 */
	private String getJavaLoad(String name) {
		String ret = String.format("javascript:%s()", name);
		return ret;
	}
	/**
	 * onClick
	 * @Override
	 */
	@Override
	public void onClick(View v) {
		MHDLog.d(TAG, "BaseWebActivity onClick()");

		if(v.getId() == R.id.title_home) {
			MHDLog.d(TAG, "onClick HomeButton====================");

			BaseWebView childview = (BaseWebView) webview_parent.getChildAt(webview_parent.getChildCount() - 1);
			if(childview.getScriptFn() != null && childview.getScriptFn().length() > 0) {		//브릿지 스크립트 펑션 존재시 웹뷰에 로드
				if(this instanceof HybridWebGuestActivity) {			//비로그인 webview activity > home
					childview.loadUrl(getJavaLoad(childview.getScriptFn()));
				}
			} else {		//브릿지 스크립트 펑션 미존재시 홈으로 이동 이벤트
				goMain();
			}
		}
		else if(v.getId() == R.id.title_back) {
			MHDLog.d(TAG, "onClick BackButton====================");
			onBackPressed();
		}
	}
	/**
	 * 팝업뷰 close button
	 */
	public void clickXButton () {
		MHDLog.d(TAG, "BaseWebActivity clickXButton()");

		BaseWebView childview = (BaseWebView) webview_parent.getChildAt(webview_parent.getChildCount() - 1);
		if(childview.getScriptFn() != null && childview.getScriptFn().length() > 0) {		//브릿지 펑션 존재시 펑션 호출
			childview.loadUrl(getJavaLoad(childview.getScriptFn()));
		}
		else {		//브릿지 펑션 미존재시 destroy 처리
			if(webview_parent.getChildCount() > 1) {		//child webview가 2개이상일 경우 > child webview destroy
				View child_title = layout_titlearea.getChildAt(layout_titlearea.getChildCount()-1);

				child_title.startAnimation(mWebChromeClient.getOutAnimation());
				childview.startAnimation(mWebChromeClient.getOutAnimation());

				layout_titlearea.removeView(child_title);
				webview_parent.removeView(childview);
				childview.destroy();
			} else {		//child webview가 1개일 경우 activity finish
				finish();
			}
		}
	}
	/**
	 * onBackPressed
	 * @Override
	 */
	@Override
	public void onBackPressed() {
		MHDLog.d(TAG, "BaseWebActivity onBackPressed()");

		BaseWebView childview = (BaseWebView) webview_parent.getChildAt(webview_parent.getChildCount() - 1);
		if(childview.isCloseOnBack()) {		//true : 종료처리
			clickXButton();
		} else {							//false : history back
			if (childview.canGoBack()) {        //webview history가 존재시
				childview.goBack();
			} else {        //webview history가 미존재시
				if (childview.getScriptFn() != null && childview.getScriptFn().length() > 0) {        //브릿지 펑션 존재시 펑션 호출
					childview.loadUrl(getJavaLoad(childview.getScriptFn()));
				} else {        //브릿지 펑션 미존재시 back 처리
					if (webview_parent.getChildCount() > 1) {        //child webview가 2개이상일 경우 > child webview destroy
						View child_title = layout_titlearea.getChildAt(layout_titlearea.getChildCount() - 1);
						child_title.startAnimation(mWebChromeClient.getOutAnimation());
						childview.startAnimation(mWebChromeClient.getOutAnimation());

						layout_titlearea.removeView(child_title);
						webview_parent.removeView(childview);
						childview.destroy();
					} else {        //child webview가 1개일 경우 activity finish
						finish();
					}
				}
			}
		}
	}
	/**
	 * 외부 브라우저로 새창
	 * callback interface match
	 */
	@Override
	public void outLinkOpenBrowser(String url) {
		MHDLog.d(TAG, "outLinkOpenBrowser()");

		Util.getInstance().goOutLink(url);
	}
	/**
	 * 현재 웹뷰 close
	 * callback interface match
	 */
	@Override
	public void closeWebview() {
		MHDLog.d(TAG, "closeWebview()");

		if(this instanceof HybridWebGuestActivity) {		// 비로그인 웹뷰 > 종료
			finish();
		} else {		// 로그인 웹뷰 > back finish
			finish();
		}
	}
	/**
	 *  현재 웹뷰 close
	 * callback interface match
	 */
	@Override
	public void closeWebview(String srnId) {
		MHDLog.d(TAG, "closeWebview() with params >> " + srnId);

		if(this instanceof HybridWebGuestActivity) {		// 비로그인 웹뷰 > 종료
			finish();
		} else {		// 로그인 웹뷰 > home button click > main 이동
			goMain();
		}
	}
	/**
	 * 특정 activity call
	 * callback interface match
	 */
	@Override
	public void openActivity(JSONObject param) {
		MHDLog.d(TAG, "openActivity() >> " + param);

		// TODO
//		Intent i = new Intent(BaseWebActivity.this, SomeActivity.class);
//		// 필요하면 param 처리 추가
//		startActivity(i);
	}
	/**
	 * 앱 로그아웃
	 * callback interface match
	 */
	@Override
	public void toAppLogout() {
		MHDLog.d(TAG, "toAppLogout()");

		// TODO Logout
	}
	/**
	 * app change Title
	 * callback interface match
	 */
	@Override
	public void changeTitle(final String title) {
		MHDLog.d(TAG, "changeTitle() >> " + title);

		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				setTitle(title);
			}
		});
//
	}
	/**
	 * app loginProcess
	 * callback interface match
	 */
	@Override
	public void loginProcess(String logininfo) {
		MHDLog.d(TAG, "loginProcess() >> " + logininfo);

		// TODO Login
	}
	/**
	 * openNewWebview
	 * callback interface match
	 */
	@Override
	public void openNewWebview(String title, String url) {
		MHDLog.d(TAG, "openNewWebview() >> " + title + "///" + url);

		// TODO openNewWebview
	}
    /**
     * visibleCloseButton
     * callback interface match
     */
	@Override
	public void visibleCloseButton(final boolean isVisible) {
		MHDLog.d(TAG, "visibleCloseButton()");

		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				isVisibleCloseBtn(isVisible);
			}
		});
	}
    /**
     * clrScriptFunc
     * callback interface match
     */
	@Override
	public void clrScriptFunc() {
		//저장된 script 펑션 초기화
		//호출시점 : onPageStarted() / onCloseWindow()
		MHDLog.d(TAG, "clrScriptFunc : clear=====================");

		BaseWebView child = (BaseWebView) webview_parent.getChildAt(webview_parent.getChildCount() - 1);
		child.setScriptFn(null);
		child.setCloseOnBack(false);
	}

}