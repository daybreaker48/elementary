package com.mhd.stard.webview.client;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.webkit.ConsoleMessage;
import android.webkit.CookieManager;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.mhd.stard.R;
import com.mhd.stard.MainActivity;
import com.mhd.stard.constant.MHDConstants;
import com.mhd.stard.util.MHDLog;
import com.mhd.stard.webview.activity.BaseWebActivity;
import com.mhd.stard.webview.appinterface.MHDAppInterface;
import com.mhd.stard.webview.view.BaseWebView;

import java.util.HashMap;
import java.util.Map;

/**
 * WebChromeClient Custom ( script )
 * Created by MH.D on 2017-04-11.
 */
public class BaseWebChromeClient extends WebChromeClient {

	private final String TAG = BaseWebChromeClient.class.getName();
	/**
	 * application context
	 */
	private Context mContext = null;
	/**
	 * base web activity
	 */
	private BaseWebActivity mBaseWebActivity = null;
	/**
	 * main activity
	 */
	private MainActivity mMainActivity = null;
	/**
	 * base webview
	 */
	private BaseWebView mWebview = null;
	/**
	 * content area layout
	 */
	private RelativeLayout mWebview_Parent = null;
	/**
	 * title area layout
	 */
	private FrameLayout mTitle_Layout = null;
	/**
	 * subwebview in animation
	 */
	private Animation inAnimation = null;
	/**
	 * subwebview out animation
	 */
	private Animation outAnimation = null;
	/**
	 * constructor
	 */
	public BaseWebChromeClient(Context context){
		mContext = context;
		if(context instanceof BaseWebActivity){
			mBaseWebActivity = (BaseWebActivity)context;
		}else if(context instanceof MainActivity){
			mMainActivity = (MainActivity)context;
		}
	}
	/**
	 * constructor
     */
	public BaseWebChromeClient(Context context, BaseWebView webview){
		mContext = context;
		if(context instanceof BaseWebActivity){
			mBaseWebActivity = (BaseWebActivity)context;
		}else if(context instanceof MainActivity){
			mMainActivity = (MainActivity)context;
		}
		this.mWebview = webview;
		if(context instanceof BaseWebActivity){
			this.mWebview_Parent = (RelativeLayout)mWebview.getParent();
		}
	}
	/**
	 * ?????? title parent layout set
	 */
	public void setTitleAreaLayout(FrameLayout titlearea) {
		this.mTitle_Layout = titlearea;
	}
	/**
	 * onConsoleMessage
	 * @Override
	 */
	@Override
	public boolean onConsoleMessage(ConsoleMessage cm) {
		MHDLog.d(TAG, "javascript >> " + cm.message() + "/////" + cm.messageLevel() + "/////" + cm.sourceId());
		return true;
	}
	/**
	 * onJsAlert
	 * @Override
	 */
	@Override
	public boolean onJsAlert(WebView view, String url, String message, final JsResult result) {
		MHDLog.d(TAG, "onJsAlert : " + message);
		
		new AlertDialog.Builder(view.getContext()).setMessage(message)
				.setPositiveButton(android.R.string.ok, new AlertDialog.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						result.confirm();
					}
				}).setCancelable(false).create().show();
		return true;
	}
	/**
	 * onJsConfirm
	 * @Override
	 */
	@Override
	public boolean onJsConfirm(WebView view, String url, String message, final JsResult result) {
		MHDLog.d(TAG, "onJsConfirm : "+ message);
		
		new AlertDialog.Builder(view.getContext()).setMessage(message)
			.setPositiveButton(R.string.alert_cancel, new AlertDialog.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					result.cancel();
				}
			}).setNegativeButton(R.string.alert_ok, new AlertDialog.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				result.confirm();
			}
		}).setCancelable(false).create().show();
		return true;
	}
	/**
	 * onCreateWindow
	 * @Override
	 */
	@Override
	public boolean onCreateWindow(final WebView view, boolean isDialog, boolean isUserGesture, Message resultMsg) {
		MHDLog.d(TAG, "onCreateWindow : open=====================");

		WebView.HitTestResult result = view.getHitTestResult();
		String url = result.getExtra();
		MHDLog.d(TAG, "onCreateWindow >> url >> " + url);
		MHDLog.d(TAG, "isDialog >> " + isDialog);
		MHDLog.d(TAG, "isUserGesture >> " + isUserGesture);
		MHDLog.d(TAG, "mWebview url >> " + view.getUrl());

		if(view != null) {
			MHDLog.d(TAG, "case new window============");

			//???????????? title layout & content layout view ??????
			final View subweb_titleview = ((BaseWebActivity)mContext).getLayoutInflater().inflate(R.layout.common_subwebview_title, null, false);
			final BaseWebView popupWebView = new BaseWebView(mContext);

			popupWebView.getSettings().setJavaScriptEnabled(true);
			popupWebView.getSettings().setSupportMultipleWindows(true);
			popupWebView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);

			//?????? ?????????
			CookieManager cookieManager = CookieManager.getInstance();
			cookieManager.setAcceptCookie(true);

			//subtitle view??? ???????????? ?????????
			ImageView btn_close = (ImageView)subweb_titleview.findViewById(R.id.btn_title_close);
			btn_close.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					((BaseWebActivity) mContext).clickXButton();
				}
			});

			MHDAppInterface mAppInterface = new MHDAppInterface((BaseWebActivity)mContext, popupWebView);
			popupWebView.addJavascriptInterface(mAppInterface, MHDConstants.WebView.WEBVIEW_INTERFACE);
			popupWebView.setWebChromeClient(this);
			popupWebView.setWebViewClient(new WebViewClient() {
					@Override
					public boolean shouldOverrideUrlLoading (WebView view, String url){
						//referer ??????
						Map<String, String> headers = new HashMap<String, String>();
						headers.put("Referer", view.getUrl());
						view.loadUrl(url, headers);

						return true;
					}
				}
			);
			popupWebView.getSettings().setTextZoom(100);

			// ??? ????????? ????????? ?????? ????????? ??????
			subweb_titleview.startAnimation(getInAnimation());
			mTitle_Layout.addView(subweb_titleview, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
			// ?????? ????????? ????????? ?????? ??????
			popupWebView.startAnimation(getInAnimation());
			mWebview_Parent.addView(popupWebView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

			WebView.WebViewTransport transport = (WebView.WebViewTransport) resultMsg.obj;
			transport.setWebView(popupWebView);
			resultMsg.sendToTarget();

			return true;
		}

		return true;
		// false ???????????? ?????? ????????????.
	}
	/**
	 * onCloseWindow
	 * @Override
	 */
	@Override
	public void onCloseWindow(WebView window) {
		MHDLog.d(TAG, "onCloseWindow : close=====================");
		super.onCloseWindow(window);

		//????????? ??????.????????? bridge??? ?????? javascript function ?????????
		((BaseWebActivity)mContext).clrScriptFunc();

		//????????? ????????? ?????? ??????
		View subweb_title = mTitle_Layout.getChildAt(mTitle_Layout.getChildCount()-1);
		subweb_title.startAnimation(getOutAnimation());
		window.startAnimation(getOutAnimation());

		//????????? ????????? ??????
		mTitle_Layout.removeView(subweb_title);
		mWebview_Parent.removeView(window);

		window.destroy();
	}
	/**
	 * popup in animation
	 */
	public Animation getInAnimation() {
		if(inAnimation == null) {
			inAnimation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 1.0f,
					Animation.RELATIVE_TO_SELF, 0.0f,
					Animation.RELATIVE_TO_SELF, 0.0f,
					Animation.RELATIVE_TO_SELF, 0.0f);
		}
		inAnimation.setDuration(300);
		inAnimation.setFillAfter(true);
		return inAnimation;
	}
	/**
	 * popup out animation
	 */
	public Animation getOutAnimation() {
		if(outAnimation == null) {
			outAnimation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
					Animation.RELATIVE_TO_SELF, 1.0f,
					Animation.RELATIVE_TO_SELF, 0.0f,
					Animation.RELATIVE_TO_SELF, 0.0f);
		}
		outAnimation.setDuration(300);
		outAnimation.setFillAfter(true);
		return outAnimation;
	}
}
