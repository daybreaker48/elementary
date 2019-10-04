package com.mhd.boomerang.util;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.provider.Settings.Secure;
import android.telephony.TelephonyManager;
import android.util.TypedValue;

import com.mhd.boomerang.R;
import com.mhd.boomerang.common.MHDApplication;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

/**
 * Util
 * Created by MH.D on 2017-03-27.
 */
public class Util {
	
	private static final String TAG = Util.class.getName();
	public static Util instance = null;
	public static Util getInstance(){
		if(instance == null) instance = new Util();
		return instance;
	}

	/**
	 * check network
	 */
	public boolean isNetWorkEnabled(Context context) {
		ConnectivityManager mConnectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		try {
			if (mConnectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).isConnectedOrConnecting())
				return true;
		} catch (Exception e) {}
		try {
			if (mConnectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).isConnectedOrConnecting())
				return true;
		} catch (Exception e) {}
		try {
			if (mConnectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIMAX).isConnectedOrConnecting()) // wibro
				return true;
		} catch (Exception e) {}

		return false;
	}
	/**
	 * 숫자만 골라내서 파싱
	 */
	public int getSafeParseInt(String str) {
		int result = 0;
		str = str.replaceAll("[^0-9]", "");
		try {
			result = Integer.parseInt(str);
		} catch (Exception e) {}

		return result;
	}
	/**
	 * 금액 콤마 표기
	 */
	public static String changeMoneyPattern(String amount) {
		DecimalFormat formatter1 = new DecimalFormat("#,###");
		BigInteger bi = new BigInteger(amount);
		return formatter1.format(bi);
	}
	/**
	 * 날짜 cast
	 * string 으로 201603으로 입력값이 들어오면 16년 03월로 리턴한다.
	 */
	public static String changeDateYMShort(Context context, String date){
		if(date == null || "".equals(date) || " ".equals(date)){
			return date;
		}
		String year = date.substring(2, 4);
		String month = date.substring(4);
		String year2 = context.getResources().getString(R.string.common_year);
		String month2 = context.getResources().getString(R.string.common_month);
		return year + year2 + " " + month + month2;
	}
	/**
	 * 날짜 cast
	 * 20160316으로 입력값이 들어오면 03월 14일로 리턴한다.
	 */
	public static String changeDateMD(Context context, String date){
		if(date == null || "".equals(date) || " ".equals(date)){
			return date;
		}
		String month = date.substring(4, 6);
		String day = date.substring(6);
		String month2 = context.getResources().getString(R.string.common_month);
		String day2 = context.getResources().getString(R.string.common_day);
		return month + month2 + " " + day + day2;
	}
	/**
	 * 날짜 cast
	 * 20160316으로 입력값이 들어오면 2016.03.16로 리턴한다.
	 */
	public static String changeDateYMDcomma(String date){
		if(date == null || "".equals(date) || " ".equals(date)){
			return date;
		}
		String year  = date.substring(0, 4);
		String month = date.substring(4, 6);
		String day = date.substring(6);
		return year + "." + month + "." + day;
	}
	/**
	 * 날짜 cast
	 * 20160316으로 입력값이 들어오면 16.03.16로 리턴한다.
	 */
	public static String changeFullDateFormat(String date, String pattern){
		String ret = "";
		String tmpdate = date.trim();
		if(tmpdate == null || tmpdate.length() == 0 || tmpdate.length() < 8 ){
			return date;
		}
		SimpleDateFormat fromdt = new SimpleDateFormat("yyyyMMdd");
		SimpleDateFormat todt = new SimpleDateFormat(pattern);
		try {
			ret = todt.format(fromdt.parse(tmpdate));
		}catch (Exception e) {
			MHDLog.printException(e);
		}

		return ret;
	}
	/**
	 * 숫자를 받아 정해진 패턴(여기서는 카드번호 형태)으로 반환.
	 * 이것을 샘플로 두고 참고해서 응용한다.
	 * 1234456712344567을 받아
	 * 1234-1234-1234-1234를 리턴한다.
	 */
	public static String changeNumberPatern(String date){
		if(date == null || "".equals(date) || " ".equals(date)){
			return date;
		}
		try{
			String card1 = date.substring(0,  4);
			String card2 = date.substring(4,  8);
			String card3 = date.substring(8,  12);
			String card4 = date.substring(12);
			return card1 + "-" + card2 + "-" + card3 + "-" + card4;
		}catch(Exception e){
			MHDLog.printException(e);
			return date;
		}
	}
	/**
	 * 소수점 아래 숫자를 받아 원하는 자리수만큼 잘라낸다.
	 * 이것을 샘플로 두고 참고해서 응용한다.
	 * 9.90을 받아
	 * 9.9를 리턴한다.(소수점 첫째 자리까지)
	 */
	public static String changeNumberPatern2(String data){
		if(data == null || "".equals(data)){
			return data;
		}
		double number = Double.parseDouble(data);
		return  String.format("%.2f", number);
	}
	/**
	 * dp 값을 받아 Pixel 로 변환한다.
	 */
	public static float dp2pixel(Context ctx, int dp){
		float pixel = 0f;
		Resources r = ctx.getResources();
		pixel = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics());
		return pixel;
	}
	/**
	 * 데이터마스크
	 */
	public String maskData(int dataLength) {
		return maskData('*', dataLength);
	}
	/**
	 * 데이터마스크
	 */
	public String maskData(Character maskChar, int dataLength) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < dataLength; i++) {
			sb.append(maskChar);
		}
		return sb.toString();
	}
	/**
	 * 에뮬레이터 실행여부 체크
	 * @return
	*/
	public static boolean isEmulator(){
		String build = Build.FINGERPRINT;
		String brand = Build.BRAND;
		String prodt = Build.PRODUCT;
		String manuf = Build.MANUFACTURER;
		String model = Build.MODEL;
		String hardw = Build.HARDWARE;

		try{
			if (build.startsWith("generic") ||
				brand.startsWith("generic") ||
				prodt.equals("google_sdk")  || prodt.equals("sdk") ||
				manuf.contains("Genymotion")||
				model.equals("google_sdk")  || model.equals("sdk") || model.equals("SDK") ||
				hardw.contains("goldfish")  ||
				prodt.equals("nox")  		 || manuf.equals("bignox") || hardw.equals("nox")){

			 return true;
			}
		}catch(Exception e){
			MHDLog.printException(e);
		}
		return false;
	}
	/**
	 * out link 호출
	 */
	public void goOutLink(String url) {
		MHDLog.d(TAG, "outLink url >> " + url);
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setData(Uri.parse(url));
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		MHDApplication.getInstance().startActivity(intent);
	}
	/**
	 * get phone number
	 */
	public String getPhoneNumber(Context context) {
		String mPhoneNum = "";
		// READ_PHONE_STATE 권한을 획득하지 않는다.
//		TelephonyManager manager = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
//		mPhoneNum = manager.getLine1Number();
//		if(isEmpty(mPhoneNum) ) {
//			mPhoneNum = manager.getDeviceId();
//		} else if(mPhoneNum.startsWith("+")){
//			mPhoneNum = mPhoneNum.replace("+82", "0");
//		}
		
		if (isEmpty(mPhoneNum))
			mPhoneNum = ((WifiManager)context.getSystemService(Context.WIFI_SERVICE)).getConnectionInfo().getMacAddress();			

		mPhoneNum = isEmpty(mPhoneNum) ? "null" : mPhoneNum;
		return mPhoneNum;
	}
	/**
	 * check string value is null, ,"null", "", empty. len=0
	 */
	public static boolean isEmpty(String str){
		if(str == null || "".equals(str) || str.isEmpty() || "null".equals(str) || (str.length()==0)){
			return true;
		}
		return false;
	}
	/**
	 * 비로그인시 common random userId 취득.
	 * 현재 접속에서만 유효한 unique value 로 간주해도 될듯.
	 * 활용할만한 곳이 있을 수 있음.
	 */
	public String getRandomUserId() {
		StringBuilder randomstr = new StringBuilder("");
		for(int i=0; i<7; i++) {
			randomstr.append((char)((Math.random()*26) + 97));
		}
		return randomstr.toString();
	}
	/**
	 * 비로그인시 common random userId 취득.
	 * 현재 접속에서만 유효한 unique value 로 간주해도 될듯.
	 * 활용할만한 곳이 있을 수 있음.
	 */
	public static void goMarket(Context context, String appName, String marketUrl){
		if(marketUrl == null || marketUrl.length() == 0) {		//주소 없을경우 플레이스토어 주소 하드코딩
			Uri marketUri = Uri.parse("market://details?id=" + appName);
			Intent marketIntent = new Intent(Intent.ACTION_VIEW).setData(marketUri);
			marketIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			context.startActivity(marketIntent);
		}else{		//update 주소, market 주소가 있을경우 호출
			Util.getInstance().goOutLink(marketUrl);
		}
	}
	/**
	 * get device UUID
	 */
	public static String getDeviceUUID(final Context context) {
		UUID uuid = null; 
		final String androidId = Secure.getString(context.getContentResolver(), Secure.ANDROID_ID);

		try {
			if (!"9774d56d682e549c".equals(androidId)) {
				uuid = UUID.nameUUIDFromBytes(androidId.getBytes("utf8"));
			} else {
//				final String deviceId = ((TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();
//				uuid = deviceId != null ? UUID.nameUUIDFromBytes(deviceId.getBytes("utf8")) : UUID.randomUUID();
				uuid = UUID.randomUUID();
			}
		} catch (UnsupportedEncodingException e) {
			MHDLog.printException(e);
		}
		return uuid.toString(); 
	}
	/**
	 * get Device ID
	 */
	public String getDeviceID(Context ctxt){
		String deviceID = "";
		final TelephonyManager tm = (TelephonyManager) ctxt.getSystemService(Context.TELEPHONY_SERVICE);
		final String tmDevice = "" + tm.getDeviceId();
		final String tmSerial = "" + tm.getSimSerialNumber();
		final String androidId = ""+ Secure.getString(ctxt.getContentResolver(), Secure.ANDROID_ID);
		
		UUID uuid = new UUID(androidId.hashCode(), ((long) tmDevice.hashCode() << 32)|tmSerial.hashCode());
		deviceID = uuid.toString();
		return deviceID;
	}
    /**
     * decodeXss
     */
	public String decodeXss(String origStr){
		origStr = origStr.replace("&#x27;", "'");
		origStr = origStr.replace("&amp;", "&");
		origStr = origStr.replace("&quot;", "\"");
		origStr = origStr.replace("&lt;", "<");
		origStr = origStr.replace("&gt;", ">");
		origStr = origStr.replace("&#x2F;", "/");
		return origStr;
	}
	/**
	 * App install 여부 확인
	 * 설치 되어 있으면 마켓이동, 없으면 앱 실행
	 */
	public static void runInstallApp(Context context, String packageName, String referrer){
		boolean isInstall = false;
		try {
			context.getPackageManager().getPackageInfo(packageName, PackageManager.GET_ACTIVITIES);
			isInstall = true;
		} catch (NameNotFoundException e) {
			MHDLog.printException(e);
			isInstall = false;
		}

		if( isInstall ){
			Intent intent = context.getPackageManager().getLaunchIntentForPackage(packageName);
			intent.putExtra("needField", "needValue");
			context.startActivity(intent);
		}else{
			Intent intent = null;
			if( referrer == null || "".equals(referrer)){
				intent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id="+packageName));
			}else{
				intent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id="+packageName + "&referrer=" + referrer));
			}
			context.startActivity(intent);
		}
	}
	/**
	 * 입력된 날짜에 .을 찍어 리턴
	 **/
	public static String getDateFormat(String date){
		String ret = "";
		try {
			String year = date.substring(0, 4);
			String monty = date.substring(4, 6);
			String day	= date.substring(6, 8);
			ret = year + "." + monty + "." + day;
		} catch (Exception e) {
			MHDLog.printException(e);
		}
		return ret;
	}
	/**
	 * 입력된 날짜와 시간에 구분값을 찍어 리턴
	 **/
	public static String getDateTimeFormat(String date){
		String ret = "";
		try{
			String year = date.substring(0, 4);
			String monty = date.substring(4, 6);
			String day	= date.substring(6, 8);
			String hour	= date.substring(8, 10);
			String min	= date.substring(10, 12);
			String sec	= date.substring(12, 14);
			ret = year + "." + monty + "." + day + " " + hour + ":" + min + ":" + sec;
		} catch (Exception e) {
			MHDLog.printException(e);
		}
		return ret;
	}
	/**
	 * 현재 날짜 리턴
	 */
	public String getCurrentDate(String format){
		SimpleDateFormat dateFormat = new SimpleDateFormat(format);
		long now = System.currentTimeMillis();
		return dateFormat.format(new Date(now));
	}
	/**
	 * 현재 날짜,시간 리턴
	 */
	public String getCurrentDateAndTime(long milliseconds){
		Date currentTime = new Date(milliseconds);
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return format.format(currentTime);
	}
	/**
	 * 상단 StatusBar 의 높이를 구한다.
	 */
	public int getStatusBarHeight(Context context) {
		int statusbarHeight = 0;
		int screenSizeType = (context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK);
		if(screenSizeType != Configuration.SCREENLAYOUT_SIZE_XLARGE){
			int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
			if(resourceId > 0){
				statusbarHeight = context.getResources().getDimensionPixelSize(resourceId);
			}
		}

		return statusbarHeight;
	}
}
