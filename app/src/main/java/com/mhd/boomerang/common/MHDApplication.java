package com.mhd.boomerang.common;

import com.mhd.boomerang.util.MHDLog;

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
