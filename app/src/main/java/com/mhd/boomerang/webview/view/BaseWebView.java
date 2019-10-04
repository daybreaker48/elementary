package com.mhd.boomerang.webview.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.mhd.boomerang.common.MHDApplication;
import com.mhd.boomerang.util.MHDLog;

import java.io.File;


/**
 * WebView Custom
 * Created by MH.D on 2017-04-20.
 */
@SuppressLint("NewApi")
public class BaseWebView extends WebView {

	private final String TAG = BaseWebView.class.getName();
	/**
	 * webview context
	 */
	private Context contextEx;
	/**
	 * base webview
	 */
	private BaseWebView webview;
	/**
	 * script function string
	 */
	private String scriptFn = null;
	/**
	 * back 처리 boolean
	 */
	private boolean closeOnBack = false;

	/**
	 * set Context
	 */
	public void setContextEx(Context context) {
		this.contextEx = context;
	}
	/**
	 * get Context
	 */
	public Context getContextEx(){
		return contextEx;
	}
	/**
	 * constructor
     */
	public BaseWebView(Context context) {
		super(context);

		if(!isInEditMode()) init(context);
	}
	/**
	 * constructor
     */
	public BaseWebView(Context context, AttributeSet attrs) {
		super(context, attrs);

		if(!isInEditMode()) init(context);
	}
	/**
	 * constructor
     */
	public BaseWebView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);

			if(!isInEditMode()) init(context);
	}
	/**
	 * default webview setting
	 */
	//@SuppressWarnings("deprecation")
	public void setDefaultWebSettings() {
		WebSettings settings = getSettings();

		settings.setJavaScriptEnabled(true);
		settings.setBuiltInZoomControls(true);
		settings.setSupportZoom(true);
		if (Build.VERSION.SDK_INT >= 11) {
			settings.setDisplayZoomControls(false);
		}
		settings.setUseWideViewPort(true);
		settings.setLoadWithOverviewMode(true);
//		settings.setSavePassword(false); //deprecated
		settings.setLoadsImagesAutomatically(true);
//		if ((Build.VERSION.SDK_INT > 7) && (Build.VERSION.SDK_INT < 18)) {
//			settings.setPluginState(WebSettings.PluginState.ON);
//		} //deprecated
		if ( Build.VERSION.SDK_INT >= 16 ) {
			// API 16 이상부터는 default 가 false
			settings.setAllowUniversalAccessFromFileURLs(true);
		}
		settings.setAllowFileAccess(true);
		settings.setGeolocationEnabled(true);
//		settings.setRenderPriority(WebSettings.RenderPriority.HIGH); //deprecated
//		settings.setGeolocationDatabasePath(this.contextEx.getFilesDir().getPath()); //deprecated
		settings.setSupportMultipleWindows(true);
		settings.setJavaScriptCanOpenWindowsAutomatically(true);
//		settings.setEnableSmoothTransition(true); //deprecated
		//WebView inside Browser doesn't want initial focus to be set.
		settings.setNeedInitialFocus(false);
		//disable content url access
		settings.setAllowContentAccess(false);
		//HTML5 API flags
		settings.setAppCacheEnabled(true);
		settings.setDatabaseEnabled(true);
		settings.setDomStorageEnabled(true);
		//localStorage 사용
		File dir = this.contextEx.getCacheDir();
		if(!dir.exists()) {
			dir.mkdirs();
		}
		settings.setAppCachePath(dir.getPath());

		settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
		if(Build.VERSION.SDK_INT > Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
			settings.setTextZoom(100);
		}

		MHDLog.d(TAG, "BaseWebView init");
	}
	/**
	 * user agent set
	 */
	public void setDefaultUserAgent(String param) {
		getSettings().setUserAgentString(getSettings().getUserAgentString() + " " + param);
		MHDLog.d(TAG, "BaseWebView setDefaultUserAgent >> " + getSettings().getUserAgentString());
	}
	/**
	 * init
	 */
	public void init(Context context) {
		webview = this;

		setContextEx(context);
		setDefaultWebSettings();
		setDefaultUserAgent(MHDApplication.getInstance().getMHDSvcManager().getMHDNetInfoCVO().getUserAgent());

		if (Build.VERSION.SDK_INT >= 19) {
			this.setLayerType(View.LAYER_TYPE_HARDWARE, null);
		} else {
			this.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
		}

		this.setHorizontalScrollBarEnabled(true);
//		this.setHorizontalScrollbarOverlay(true); //deprecated
		this.setVerticalScrollBarEnabled(true);
//		this.setVerticalScrollbarOverlay(true); //deprecated
		this.setScrollbarFadingEnabled(true);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			WebView.setWebContentsDebuggingEnabled(true);
		}
	}
	/**
	 * get webview content height
	 */
	public int getContentHeight() {
		return computeVerticalScrollRange();
	}
	/**
	 * get webview max scroll
	 */
	public int getMaxScroll() {
		return super.computeVerticalScrollRange() - getMeasuredHeight();
	}
	/**
	 * get webview scroll X
	 */
	public int getNativeScrollX() {
		return getScrollX();
	}
	/**
	 * get webview scroll Y
	 */
	public int getNativeScrollY() {
		return getScrollY();
	}
	/**
	 * call go back
	 */
	public void goBack() {
		super.goBack();
	}
	/**
	 * get webview position top
	 */
	public void goTop() {
		scrollTo(0, 0);
	}
	/**
	 * remove this view
	 */
	public void removeFromParent() {
		ViewGroup viewGroup = (ViewGroup)getParent();
		if(viewGroup != null) {
			viewGroup.removeView(this);
		}
	}
	/**
	 * getter script function
	 */
	public String getScriptFn() {
		return scriptFn;
	}
	/**
	 * setter script function
	 */
	public void setScriptFn(String scriptFn) {
		this.scriptFn = scriptFn;
	}
	/**
	 * getter is webview close(이 창을 닫을 것인지 여부 - true : close)
	 */
	public boolean isCloseOnBack() {
		return closeOnBack;
	}
	/**
	 * setter is webview close
	 */
	public void setCloseOnBack(boolean closeOnBack) {
		this.closeOnBack = closeOnBack;
	}

}
