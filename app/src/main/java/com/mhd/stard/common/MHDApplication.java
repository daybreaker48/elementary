package com.mhd.stard.common;

//import com.kakao.sdk.common.KakaoSdk;
import com.kakao.sdk.common.KakaoSdk;
import com.mhd.stard.util.MHDLog;

/**
 * Created by MH.D on 2017-03-27.
 * 컴포넌트 어플리케이션
 */
public class MHDApplication extends AbstractApplication {
	
	private static MHDApplication MHD_APPLICATION = null;
	public static MHDApplication getInstance() { return MHD_APPLICATION; }
    /**
     * 어플리케이션 컴포넌트가 최초 호출인지 아닌지. false:최초 호출, true:이미 호출
     */
    private boolean mIsAnyActivityInvokedOnce = false;

	@Override
	public void onCreate() {
		MHDLog.d(TAG, "MHDApplication onCreated");
		super.onCreate();
		
		MHD_APPLICATION = this;

		// Kakao SDK 초기화
		KakaoSdk.init(this, "c88e112c6bceda98920c8db73b3e2ac4");
	}
    /**
     * getter mIsAnyActivityInvokedOnce
     */
	public boolean isAnyActivityInvokedOnce(){
		return mIsAnyActivityInvokedOnce;
	}
    /**
     * setter mIsAnyActivityInvokedOnce
     */
	public void setAnyActivityInvokedOnce(boolean isInvoked){
		mIsAnyActivityInvokedOnce = isInvoked;
	}
}
