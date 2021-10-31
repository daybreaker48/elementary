package com.mhd.stard.common;

import android.content.pm.PackageInfo;
import androidx.multidex.MultiDexApplication;

import com.mhd.stard.util.MHDLog;

/**
 * MultiDexApplication
 * Created by MH.D on 2017-03-27.
 */
public abstract class AbstractApplication extends MultiDexApplication {
	
	protected final String TAG = AbstractApplication.class.getName();
	/**
	 * application manager
	 */
	private MHDSvcManager mhdSvcManager;
	/**
	 * service manager getter
	 */
	public MHDSvcManager getMHDSvcManager() {
		return mhdSvcManager;
	}


	@Override
	public void onCreate() {
		super.onCreate();
		init();
	}
	/**
	 * init
	 */
	public void init() {
		mhdSvcManager = new MHDSvcManager(getApplicationContext());
	}
	/**
	 * get app version
	 * @return
	 */
	public String getAppVersion() {
		String version = "";
		try {
			PackageInfo i = this.getPackageManager().getPackageInfo(this.getPackageName(), 0);
			version = i.versionName;
		} catch (Exception e) {
			MHDLog.printException(e);
		}
		return version;
	}
	/**
	 * resource id를 통해 tag entry name get
	 */
	public String getResTagName(int resId) {
		return getResources().getResourceEntryName(resId);
	}
	/**
	 * tag engry name을 통해 해당 value get
	 */
	public String getResStrFromEntryName(String entryName) {
		int resId = getResources().getIdentifier(entryName, "string", getPackageName());
		return getString(resId);
	}
}