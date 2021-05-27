package com.mhd.elemantary.webview.client;

import android.annotation.TargetApi;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.util.Log;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.SslErrorHandler;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.mhd.elemantary.constant.MHDConstants;
import com.mhd.elemantary.util.MHDLog;
import com.mhd.elemantary.webview.activity.BaseWebActivity;
import com.mhd.elemantary.webview.appinterface.BaseWebCallback;
import com.mhd.elemantary.webview.views.CustomProgressDialog;

import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;


/**
 * WebClient Custom ( web page in webview. link & move )
 * Created by MH.D on 2017-04-18.
 */
public class BaseWebClient extends WebViewClient {
	
	private final String TAG = BaseWebClient.class.getSimpleName();
	private final String INTENT_PROTOCOL_START = "intent:";	// 내부 scheme 정의
	private final String GOOGLE_PLAY_STORE_PREFIX = "market://details?id=";
	/**
	 * callback interface
	 */
	private BaseWebCallback mCallBack;
	private CustomProgressDialog dialog;
	/**
	 * callback setter
	 */
	public void setCallBack(BaseWebCallback callback) {
		this.mCallBack = callback;
	}
	/**
	 * v
	 * @Override
	 */
	@Override
	public void onPageFinished(WebView view, String url) {
		super.onPageFinished(view, url);

		MHDLog.d(TAG, "onPageFinished >> " + url);

		//웹의 특정 혹은 전체페이지(url로 판단)에서 상단, 하단 등에 공백을 준다던가 하는 식의 특정 처리가 필요한 경우 여기서 처리
//		if ((url != null) && (url.contains("특정 URL"))){
//			view.loadUrl("javascript:$('body').css('padding-top','73px')");
//		}

		//웹의 특정 혹은 전체페이지에서 앱 단의 특정 버튼을 hide 한다던가 하는 식의 특정 처리가 필요한 경우 여기서 처리
//		if ((url != null) && (url.contains("특정 URL"))){
//			if(mCallBack != null) mCallBack.visibleCloseButton(false);
//		} else {
//			if(mCallBack != null) mCallBack.visibleCloseButton(true);
//		}
		// 쿠키 강제 동기화.
		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
			CookieSyncManager.getInstance().sync();
		} else {
			CookieManager.getInstance().flush();
		}

		if (dialog != null) {
			dialog.dismiss();
			dialog = null;
		}
	}
	/**
	 * onPageStarted
	 * @Override
	 */
	@Override
	public void onPageStarted(WebView view, String url, Bitmap favicon) {
		super.onPageStarted(view, url, favicon);

		MHDLog.d(TAG, "onPageStarted >> " + url);

//		if(view.getContext() instanceof MainActivity) {
//			// 특정 액티비티인 경우 프로그레바를 보여주지 않기 위해서는 여기서 return
//			return;
//		} else {
			if (dialog == null) {
				dialog = new CustomProgressDialog(view.getContext());
				dialog.setCancelable(true);
			}
			dialog.show();
//		}

		// 페이지 로딩 완료시마다 bridge로 받은 javascript function 초기화
		if(mCallBack != null) {
			mCallBack.clrScriptFunc();
		}
	}
	/**
	 * onReceivedError
	 * @Override
	 */
	@SuppressWarnings("deprecation")
	@Override
	public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
		super.onReceivedError(view, errorCode, description, failingUrl);

		Intent intent = new Intent();
		intent.setAction(MHDConstants.WebView.BROAD_WEB_ERR);
		intent.putExtra(MHDConstants.WebView.BROAD_WEB_ERR_URL, failingUrl);
		view.getContext().sendBroadcast(intent);
	}
	/**
	 * onReceivedError
	 * @Override
	 */
	@TargetApi(Build.VERSION_CODES.M)
	@Override
	public void onReceivedError(WebView view, WebResourceRequest req, WebResourceError error) {
		onReceivedError(view, error.getErrorCode(), error. getDescription().toString(), req.getUrl().toString());
	}
	/**
	 * onReceivedSslError
	 * @Override
	 */
	@Override
	public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
		//handler.proceed();
		Log.e(TAG, "onReceivedSslError=============");
		if(handler != null) {
			handler.proceed();
		} else {
			super.onReceivedSslError(view, handler, error);
		}
	}
	/**
	 * shouldOverrideUrlLoading
	 * @Override
	 */
	@SuppressWarnings("deprecation")
	@Override
	public boolean shouldOverrideUrlLoading(final WebView view, final String url) {
		boolean ret = true;
		if(view.getContext() instanceof BaseWebActivity) {
			MHDLog.d(TAG, "instance of BaseWebActivity=============");
		}

		MHDLog.d(TAG, "shouldOverrideUrlLoading >> " + url);
		
		if (url.startsWith("tel:")) { 			//전화걸기
			try {
				Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
				view.getContext().startActivity(intent);
			} catch (Exception e) {
				MHDLog.printException(e);
			}
			return true;
		}else if(url.startsWith("sms:")) {		//sms
			try {
				Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse(url));
				view.getContext().startActivity(intent);
			} catch (Exception e) {
				MHDLog.printException(e);
			}
			return true;
		}else if(url.startsWith("mailto:")) {	//mail
			try {
				Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse(url));
				view.getContext().startActivity(intent);
			} catch (ActivityNotFoundException e) {
				MHDLog.printException(e);
			}
			return true;
		}else if( url.contains("http://market.android.com")
	    		|| url.contains("play.google.com")
	            || url.contains("기타 특수 scheme")) {
	        try{
	            Intent intent = null;
	            intent = Intent.parseUri(url, Intent.URI_INTENT_SCHEME);
	            if (url.startsWith("intent") && url.contains("com.ahnlab.v3mobileplus")) {
					view.getContext().startActivity(Intent.parseUri(url, 0));
					return true;
				}else if(url.startsWith("intent")){ // chrome 버젼 방식
	                // 앱설치 체크. 설치 안되어있다면 마켓 검색으로.
	                if (view.getContext().getPackageManager().resolveActivity(intent, 0) == null){
	                    String packagename = intent.getPackage();
	                    if (packagename != null){
	                        Uri uri = Uri.parse("market://search?q=pname:" + packagename);
	                        intent = new Intent(Intent.ACTION_VIEW, uri);
	                        view.getContext().startActivity(intent);
	                        return true;
	                    }
	                }
	                Uri uri = Uri.parse(intent.getDataString());
	                intent = new Intent(Intent.ACTION_VIEW, uri);
	                view.getContext().startActivity(intent);
	                return true;
	            }else{
	                Uri uri = Uri.parse(url);
	                intent = new Intent(Intent.ACTION_VIEW, uri);
	                view.getContext().startActivity(intent);
	                return true;
	            }
	        }catch(ActivityNotFoundException e){
				MHDLog.printException(e);
	            return false;
	        }catch (URISyntaxException e){
				MHDLog.printException(e);
	            return false;
	        }
	    }else if(url.startsWith(INTENT_PROTOCOL_START)) {		//start with intent://
			try{
	            Intent intent = null;
				intent = Intent.parseUri(url, Intent.URI_INTENT_SCHEME);
	            
	            if(url.contains("samsungcard")) {		//samsungcard 기반 app -> 마켓으로 연동
	            	String packagename = intent.getPackage();
                    if (packagename != null){
                    	view.getContext().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(GOOGLE_PLAY_STORE_PREFIX + packagename)));
                        return true;
                    }
	            }
	            else {		//기타앱 -> 연동
	            	if (view.getContext().getPackageManager().resolveActivity(intent, 0) == null){
	                    String packagename = intent.getPackage();
	                    if (packagename != null){
	                    	view.getContext().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(GOOGLE_PLAY_STORE_PREFIX + packagename)));
	                        return true;
	                    }
	                }
	                Uri uri = Uri.parse(intent.getDataString());
	                intent = new Intent(Intent.ACTION_VIEW, uri);
	                view.getContext().startActivity(intent);
	                return true;
	            }
	        }catch(ActivityNotFoundException e){
				MHDLog.printException(e);
	            return false;
	        }catch (URISyntaxException e){
				MHDLog.printException(e);
	            return false;
	        }
		}
		
		//referer 처리
		Map<String, String> headers = new HashMap<String, String>();
		headers.put("Referer", view.getUrl());
		view.loadUrl(url, headers);

		return ret;
	}
	/**
	 * shouldOverrideUrlLoading
	 * @Override
	 */
	@TargetApi(Build.VERSION_CODES.N)
	@Override
	public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request){
		return shouldOverrideUrlLoading(view, request.getUrl().toString());
	}
}
