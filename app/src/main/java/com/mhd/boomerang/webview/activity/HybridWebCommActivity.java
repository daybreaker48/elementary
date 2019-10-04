package com.mhd.boomerang.webview.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.mhd.boomerang.constant.MHDConstants;
import com.mhd.boomerang.webview.appinterface.MHDAppInterface;
import com.mhd.boomerang.webview.client.BaseWebChromeClient;
import com.mhd.boomerang.webview.client.BaseWebClient;


/**
 * webview activity(비로그인 공용)
 * Created by MH.D on 2017-04-03.
 */
public class HybridWebCommActivity extends BaseWebActivity {
	
	private final String TAG = HybridWebCommActivity.class.getName();
	/**
	 * hybrid app : web interface
	 */
	private MHDAppInterface mAppInterface;
	/**
	 * self startactivity
	 */
	public static void invokeActivity(Context context, int res) {
		Intent intent = new Intent(context, HybridWebCommActivity.class);
		intent.putExtra(MHDConstants.WebView.WEBVIEW_TARGET_URL, res);
		context.startActivity(intent);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		initWebview();
		initViews();
		getValues();
	}
	
	private void initViews() {
		// webView setting
		mWebChromeClient = new BaseWebChromeClient(this, webview);
		mWebChromeClient.setTitleAreaLayout(layout_titlearea);

		BaseWebClient webClient = new BaseWebClient();
		webClient.setCallBack(this);

		mAppInterface = new MHDAppInterface(this, webview);
		webview.addJavascriptInterface(mAppInterface, MHDConstants.WebView.WEBVIEW_INTERFACE);
		webview.setWebViewClient(webClient);
		webview.setWebChromeClient(mWebChromeClient);
	}
}