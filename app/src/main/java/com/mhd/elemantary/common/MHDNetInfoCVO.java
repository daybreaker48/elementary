package com.mhd.elemantary.common;

import android.os.Build;

import com.mhd.elemantary.constant.MHDConstants;
import com.mhd.elemantary.util.MHDLog;

import java.util.HashMap;
import java.util.Map;


/**
 * 네트워크 정보 cvo
 * 어플:인증키, 웹뷰:Agent
 * Created by MH.D on 2017-03-31.
 */
public class MHDNetInfoCVO {
	
	private static final String TAG = MHDNetInfoCVO.class.getName();
	/**
	 * 최초 full url
	 */
	private String svrIntroUrl;
	/**
	 * 인증 보안키(사용할거면 쓰고, 아니면 예비용으로 두고)
	 */
	private String svrCert;
	/**
	 * user-agent string
	 */
	private String userAgent;
	/**
	 * user-agent setting map
	 */
	private Map<String, String> userAgentMap;

	/**
	 * get full url
	 */
	public String getSvrIntroUrl() {
		return svrIntroUrl;
	}
	/**
	 * set full url
	 */
	public void setSvrIntroUrl(String svrIntroUrl) {
		this.svrIntroUrl = svrIntroUrl;
	}
	/**
	 * get 보안키
	 */
	public String getSvrCert() {
		return svrCert;
	}
	/**
	 * set 보안키
	 */
	public void setSvrCert(String svrCert) {
		this.svrCert = svrCert;
	}
	/**
	 * get global user-agent string
	 */
	public String getUserAgent() {
		if(userAgent == null || userAgent.length() == 0) {
			StringBuilder userSb = new StringBuilder(MHDConstants.UserAgent.AGENT_CHANNEL)
				.append(" ")
				.append(MHDConstants.UserAgent.AGENT_CHANNEL_OS)
				.append(" ")
				.append(MHDConstants.UserAgent.AGENT_CHANNEL_OS_VER)
				.append("=")
				.append(Build.VERSION.RELEASE)
				.append(" ")
				.append(MHDConstants.UserAgent.AGENT_NAME)
				.append(" ")
				.append(MHDConstants.UserAgent.AGENT_APP_VER)
				.append("=")
				.append(MHDApplication.getInstance().getAppVersion())
				.append(" ")
				.append(MHDConstants.UserAgent.AGENT_APP_MARKET)
				.append(" ")
				.append(MHDConstants.UserAgent.AGENT_DEVICE_UID)
				.append("=")
				//.append(Util.getInstance().getDeviceID(MHDApplication.getInstance()))
				.append(MHDApplication.getInstance().getMHDSvcManager().getDeviceNewUuid())
				.append(" ")
				.append(MHDConstants.UserAgent.AGENT_CERTIFICATE_KEY)
				.append("=")
				.append("");
			
			userAgent = userSb.toString();
		}
		MHDLog.d(TAG, "userAgent >> " + userAgent);
		return userAgent;
	}
	/**
	 * get global user-agent map
	 */
	public Map<String, String> getUserAgentMap() {
		if(userAgentMap == null) {
			userAgentMap = new HashMap<String, String>();
			userAgentMap.put("User-Agent", getUserAgent());
		}
		return userAgentMap;
	}

}
