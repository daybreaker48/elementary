package com.mhd.boomerang.webview.appinterface;

import android.content.Context;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;

import com.mhd.boomerang.activity.BaseActivity;
import com.mhd.boomerang.MainActivity;
import com.mhd.boomerang.common.MHDApplication;
import com.mhd.boomerang.util.MHDLog;
import com.mhd.boomerang.util.Util;
import com.mhd.boomerang.webview.activity.BaseWebActivity;
import com.mhd.boomerang.webview.view.BaseWebView;

import org.json.JSONObject;

import java.net.URLEncoder;


/**
 * custom bridge interface
 * Created by MH.D on 2017-04-12.
 */
public class MHDAppInterface {

	private final String TAG = MHDAppInterface.class.getName();
	/**
	 * activity object
	 * 웹뷰를 사용하고 있는 activity가 여러 개일때 각각에 따라 다르게 처리하기 위한 용도
	 */
	private BaseActivity mBaseActivity;
	/**
	 * custom webview
	 */
	private BaseWebView mWebView;
	/**
	 * application context
	 */
	private Context mContext;
	/**
	 * 액션 후 웹뷰로 콜백 리턴시 필요 string 이 있다면 처리
	 */
	private String returnCallback = "";
	/**
	 * constructor
	 */
	public MHDAppInterface(Context ctxt, WebView webView) {
		if(ctxt instanceof MainActivity) {
			this.mBaseActivity = (BaseActivity)ctxt;
			this.mContext = ctxt;
//			this.mWebView = webView;
		}
		else if(ctxt instanceof BaseWebActivity) {
			this.mBaseActivity = (BaseWebActivity)ctxt;
			this.mContext = ctxt;
//			this.mWebView = webView;
		}
	}
	/**
	 * setter return callback
	 */
	public void setReturnCallback(String callback) {
		this.returnCallback = callback;
	}
	/**
	 * getterreturn callbcack
	 */
	public String getReturnCallback() {
		return this.returnCallback;
	}
	/**
	 * changeTitle
	 * 웹뷰가 나타내는 페이지에 따라 앱의 타이틀을 컨트롤
	 * @JavascriptInterface
	 */
	@JavascriptInterface
	public void changeTitle(String param) {
		MHDLog.d(TAG, "@JavascriptInterface changeTitle >> "+ param);
		
		if (!Util.getInstance().isEmpty(param)) {
			try {
				JSONObject obj = new JSONObject(param);
				if(obj.has("title")){
					final String title = obj.getString("title");
					if( mBaseActivity != null && title.length() > 0) {
						MHDLog.d(TAG, "title >> " + title);
						if(mBaseActivity instanceof BaseWebActivity) {
							((BaseWebActivity)mBaseActivity).changeTitle(title);
						}
						// BaseWebActivity 외 다른 activity가 있다면 그것에 대한 처리도 추가
					}
				}
			} catch (Exception e) {
				MHDLog.printException(e);
			}
		}
	}
	/**
	 * outLinkOpenBrowser
	 * @JavascriptInterface
	 */
	@JavascriptInterface
	 public void outLinkOpenBrowser(String param) {
		MHDLog.d(TAG, "@JavascriptInterface outLinkOpenBrowser >> "+ param);

		if (!Util.getInstance().isEmpty(param)) {
			try {
				JSONObject obj = new JSONObject(param);
				StringBuilder url = new StringBuilder(MHDApplication.getInstance().getMHDSvcManager().getMHDNetInfoCVO().getSvrIntroUrl())
						.append("/sso.jsp?"); // 예시. 필수 아님.

				if(obj.has("rtrnUrl")){ // 예시. 필수 아님.
					String rtrnUrl = obj.getString("rtrnUrl");
					MHDLog.d(TAG, "url >> "+ rtrnUrl);

					url.append("rtrnUrl=")
							.append(URLEncoder.encode(rtrnUrl, "UTF-8"))
							.append("&ssoTkn=")
							.append(MHDApplication.getInstance().getMHDSvcManager().getLoginVo().getLsiv().getUsrsubInf_1());

					if(mBaseActivity instanceof BaseWebActivity) {
						((BaseWebActivity)mBaseActivity).outLinkOpenBrowser(url.toString());
					}
					// BaseWebActivity 외 다른 activity가 있다면 그것에 대한 처리도 추가
				}
			} catch (Exception e) {
				MHDLog.printException(e);
			}
		}
	}
	/**
	 * Just close the webview
	 * @JavascriptInterface
	 */
	@JavascriptInterface
	public void closeWebview() {
		MHDLog.d(TAG, "@JavascriptInterface closeWebview >> ok");

		if( mBaseActivity != null && mBaseActivity instanceof BaseWebActivity) {
			((BaseWebActivity)mBaseActivity).closeWebview();
		}
	}
	/**
	 * work and close the webview
	 * @JavascriptInterface
	 */
	@JavascriptInterface
	public void closeWebview(String param) {
		MHDLog.d(TAG, "@JavascriptInterface closeWebview >> " + param);

		if( mBaseActivity != null && Util.isEmpty(param) == false) {
			if ( "{}".equals(param) ) { // 아무것도 없는 경우. 웹에서 json 생성하는 로직에 따라 이렇게 올수도.
				if( mBaseActivity != null && mBaseActivity instanceof BaseWebActivity) {
					((BaseWebActivity)mBaseActivity).closeWebview();
				}
				return;
			}

			try {
				JSONObject obj = new JSONObject(param);
				String tmpValue = "";
				if(obj.has("특정 필드")) {
					tmpValue = obj.getString("특정 필드");
				}
				if( mBaseActivity != null && mBaseActivity instanceof BaseWebActivity) {
					((BaseWebActivity)mBaseActivity).closeWebview(tmpValue);
				}
			} catch (Exception e) {
				MHDLog.printException(e);
			}
		} else {
			mBaseActivity.finish();
		}
	}
	/**
	 * activity call
	 * @JavascriptInterface
	 */
	@JavascriptInterface
	public void openActivity(String arg) {
		MHDLog.d(TAG, "@JavascriptInterface openActivity >> " + arg);
		if( mBaseActivity != null && Util.isEmpty(arg) == false) {
			try {
				JSONObject obj = new JSONObject(arg);
				if(mBaseActivity instanceof BaseWebActivity) {
					((BaseWebActivity)mBaseActivity).openActivity(obj);
				}
			} catch (Exception e) {
				MHDLog.printException(e);
			}
		}
	}
	/**
	 * new webview call
	 * @JavascriptInterface
	 */
	@JavascriptInterface
	public void openNewWebview(String param) {
		MHDLog.d(TAG, "@JavascriptInterface openNewWebview >> " + param);

		if( mBaseActivity != null && Util.isEmpty(param) == false) {
			try {
				JSONObject obj = new JSONObject(param);
				String title = "";
				String url = "";

				if(obj.has("title")) {
					title = obj.getString("title");
				}
				if(obj.has("url")) {
					url = obj.getString("url");
				}
//				// fragment 라면
//				if(mBaseActivity instanceof MainActivity) {
//					BenefitFragment fragment = ((MainActivity)mBaseActivity).mAdapter.mBenefitFragment;
//					fragment.openNewWebview(title, url);
//				}
				if(mBaseActivity instanceof BaseWebActivity) {
					((BaseWebActivity)mBaseActivity).openNewWebview(title, url);
				}
			} catch (Exception e) {
				MHDLog.printException(e);
			}
		}
	}
	/**
	 * 앱 로그아웃
	 */
	@JavascriptInterface
	public void toAppLogout() {
		MHDLog.d(TAG, "@JavascriptInterface toAppLogout");

		if(mBaseActivity instanceof BaseWebActivity) {
			((BaseWebActivity)mBaseActivity).toAppLogout();
		}
	}
	/**
	 * 앱 로그인
	 * @JavascriptInterface
	 */
	@JavascriptInterface
	public void loginProcess(String logininfo) {
		MHDLog.d(TAG, "@JavascriptInterface loginProcess");

		try {
			if(mBaseActivity != null && mBaseActivity instanceof BaseWebActivity) {
				((BaseWebActivity)mBaseActivity).loginProcess(logininfo);
			}
		} catch (Exception e) {
			MHDLog.printException(e);
		}
	}
}


