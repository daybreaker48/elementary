package com.mhd.stard.common;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

/**
 * custom shared preference
 * Created by MH.D on 2017-03-31.
 */
public class MHDPreferences {

	private final static String TAG = MHDPreferences.class.getName();
	/**
	 * PreferenceKey class
	 */
	private interface PreferenceKey{
		/**
		 * App 첫 실행 SharedPreferences name
		 */
		String PREF_NAME_FIRST_EXEC_APP = "FIRST_EXEC_APP";
		/**
		 * App 첫 로그인 SharedPreferences name
		 */
		String PREF_NAME_FIRST_LOGIN = "FIRST_LOGIN";
		/**
		 * 최근 로그인 모드 SharedPreferences name
		 */
		String PREF_NAME_LAST_LOGIN_MODE = "LAST_LOGIN_MODE ";
		/**
		 * 디지털멤버ID번호(UUID) SharedPreferences name
		 */
		String PREF_NAME_DEVICE_MEMBER_NO = "DIGITAL_MEMBER_NO ";
		/**
		 * tutorial 실행 여부
		 */
		String PREF_NAME_TUTORIAL = "PREF_NAME_TUTORIAL";
		/**
		 * FCM token
		 */
		String PREF_NAME_FCM_TOKEN = "FCM_TOKEN";
		/**
		 * 로그인 유지 상태
		 */
		String PREF_NAME_LOGIN_KEEP = "LOGIN_KEEP";
		/**
		 * 아이디 저장 상태
		 */
		String PREF_NAME_ID_KEEP = "ID_KEEP";
		/**
		 * 계정정보
		 */
		String PREF_NAME_ID = "NAME_ID";
		String PREF_NAME_PWD = "NAME_PWD";
		String PREF_NAME_LTYPE = "NAME_LTYPE";
		String PREF_NAME_KAKAO = "NAME_KAKAO";
		String PREF_LOGOUT = "LOGOUT";
	}

	private SharedPreferences mSharedPreferences;
	private Editor mEditor;
	private Context mContext;

	private static class SingletonHolder {
		static final MHDPreferences INSTANCE = new MHDPreferences();
	}
	public static MHDPreferences getInstance() {
		return SingletonHolder.INSTANCE;
	}

	/**
	 * MHDPreferences
	 */
	private MHDPreferences() {
		mContext = MHDApplication.getInstance().getApplicationContext();
		mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
		mEditor = mSharedPreferences.edit();
	}
	/**
	 * App 첫 실행 여부 값 추출
	 * @return ( true : App 첫 실행, flase : App 첫 실행 아님)
	 */
	public boolean getPrefFirstExecApp() {
		return getBooleanValue(PreferenceKey.PREF_NAME_FIRST_EXEC_APP, true);
	}
	/**
	 * App 첫 실행 여부 값 저장
	 */
	public void savePrefFirstExecApp() {
		setValue(PreferenceKey.PREF_NAME_FIRST_EXEC_APP, false);
	}
	/**
	 * 첫 로그인 여부 값 추출
	 *
	 * @return ( true : 첫 로그인, flase : 첫 로그인 아님)
	 */
	public boolean getPrefFirstLogin() {
		return getBooleanValue(PreferenceKey.PREF_NAME_FIRST_LOGIN, true);
	}
	/**
	 * 첫 로그인 여부 값 저장
	 */
	public void savePrefFirstLogin(boolean flag) {
		setValue(PreferenceKey.PREF_NAME_FIRST_LOGIN, flag);
	}
	/**
	 * 최근 로그인 모드 추출
	 * @return ( ID : 아이디, FB : facebook, ETC : 기타)
	 */
	public String getPrefLastLoginMode() {
		return getStringValue(PreferenceKey.PREF_NAME_LAST_LOGIN_MODE, "ID");
	}
	/**
	 * 최근 로그인 모드 저장
	 */
	public void savePrefLastLoginMode(String mode) {
		setValue(PreferenceKey.PREF_NAME_LAST_LOGIN_MODE, mode);
	}
	/**
	 * 디지털멤버ID번호 추출
	 * @return (UUID)
	 */
	public String getPrefDvcMmbrIdNo() {
		return getStringValue(PreferenceKey.PREF_NAME_DEVICE_MEMBER_NO, "");
	}
	/**
	 * 디지털멤버ID번호 저장
	 */
	public void savePrefDvcMmbrIdNo(String no) {
		setValue(PreferenceKey.PREF_NAME_DEVICE_MEMBER_NO, no);
	}
	/**
	 * 튜토리얼
	 */
	public void setTutorialVisible(boolean vislble) {
		setValue(PreferenceKey.PREF_NAME_TUTORIAL, vislble);
	}
	/**
	 * 튜토리얼
	 */
	public boolean isTutorialVisible() {
		return getBooleanValue(PreferenceKey.PREF_NAME_TUTORIAL, true);
	}
	/**
	 * FCM Token 추출
	 */
	public String getPrefFcmToken() {
		return getStringValue(PreferenceKey.PREF_NAME_FCM_TOKEN, "");
	}
	/**
	 * FCM Token 저장
	 */
	public void savePrefFcmToken(String token) {
		setValue(PreferenceKey.PREF_NAME_FCM_TOKEN, token);
	}
	/**
	 * 로그인 유지 상태 저장
	 */
	public void savePrefLoginKeep(boolean keep) {
		setValue(PreferenceKey.PREF_NAME_LOGIN_KEEP, keep);
	}
	public boolean getPrefLoginKeep() {
		return getBooleanValue(PreferenceKey.PREF_NAME_LOGIN_KEEP, true);
	}
	/**
	 * 아이디 유지 상태 저장
	 */
	public void savePrefIDKeep(String keep) {
		setValue(PreferenceKey.PREF_NAME_ID_KEEP, keep);
	}
	public String getPrefIDKeep() {
		return getStringValue(PreferenceKey.PREF_NAME_ID_KEEP, "");
	}
	/**
	 * 로그인 유지 상태일때 계정정보 저장
	 */
	public void savePrefUserID(String keep) {
		setValue(PreferenceKey.PREF_NAME_ID, keep);
	}
	public String getPrefUserID() {
		return getStringValue(PreferenceKey.PREF_NAME_ID, "");
	}
	public void savePrefUserPWD(String keep) {
		setValue(PreferenceKey.PREF_NAME_PWD, keep);
	}
	public String getPrefUserPWD() {
		return getStringValue(PreferenceKey.PREF_NAME_PWD, "");
	}
	public void savePrefUserLtype(String keep) {
		setValue(PreferenceKey.PREF_NAME_LTYPE, keep);
	}
	public String getPrefUserLtype() {
		return getStringValue(PreferenceKey.PREF_NAME_LTYPE, "");
	}
	public void savePrefKakaoToken(String keep) {
		setValue(PreferenceKey.PREF_NAME_KAKAO, keep);
	}
	public String getPrefKakaoToken() {
		return getStringValue(PreferenceKey.PREF_NAME_KAKAO, "");
	}
	/**
	 * 로그아웃 여부. 이 경우는 자동로그인을 태우지 않는다.
	 */
	public void savePrefLogout(boolean keep) {
		setValue(PreferenceKey.PREF_LOGOUT, keep);
	}
	public boolean getPrefLogout() {
		return getBooleanValue(PreferenceKey.PREF_LOGOUT, false);
	}

	/**
	 * 특정 값 삭제하기
	 */
	public void removeAllPreferences(String prefName) {
		mEditor.remove(prefName);
		mEditor.commit();
	}
	/**
	 * 모든 값(ALL Data) 삭제하기
	 */
	public void removeAllPreferences() {
		mEditor.clear();
		mEditor.commit();
	}
	/**
	 * 특정 preference 키 값 저장
	 */
	private void setValue(String name, Object value) {
		if(value instanceof String){
			mEditor.putString(name, (String)value).commit();
		}
		else if(value instanceof Integer){
			mEditor.putInt(name, (Integer)value).commit();
		}
		else if(value instanceof Boolean){
			mEditor.putBoolean(name, (Boolean)value).commit();
		}
		else if(value instanceof Long){
			mEditor.putLong(name, (Long)value).commit();
		}
		else if(value instanceof Float){
			mEditor.putFloat(name, (Float)value).commit();
		}
	}
	/**
	 * String preference 키 값 조회
	 */
	private String getStringValue(String key, String defaultValue){
		return mSharedPreferences.getString(key, defaultValue);
	}
	/**
	 * long preference 키 값 조회
	 */
	private long getLongValue(String key, long defaultValue){
		return mSharedPreferences.getLong(key, defaultValue);
	}
	/**
	 * int preference 키 값 조회
	 */
	private int getIntValue(String key, int defaultValue){
		return mSharedPreferences.getInt(key, defaultValue);
	}
	/**
	 * boolean preference 키 값 조회
	 */
	private boolean getBooleanValue(String key, boolean defaultValue){
		return mSharedPreferences.getBoolean(key, defaultValue);
	}
	/**
	 * float preference 키 값 조회
	 */
	private float getFloatValue(String key, float defaultValue){
		return mSharedPreferences.getFloat(key, defaultValue);
	}
}






