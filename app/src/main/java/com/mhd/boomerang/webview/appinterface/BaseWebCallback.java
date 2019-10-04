package com.mhd.boomerang.webview.appinterface;

import org.json.JSONObject;

/**
 * custom Webview Activity Bridge Callback
 * Created by MH.D on 2017-04-06.
 */
public interface BaseWebCallback {

	public void outLinkOpenBrowser(String url);
	public void closeWebview();
	public void closeWebview(String srnId);
	public void openActivity(JSONObject jobj);
	public void toAppLogout();
	public void changeTitle(String title);
	public void loginProcess(String logininfo);
	public void openNewWebview(String title, String url);
	public void visibleCloseButton(boolean isVisible);
	public void clrScriptFunc();
}
