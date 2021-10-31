package com.mhd.stard.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.TypedValue;
import android.view.WindowManager.BadTokenException;

import com.mhd.stard.R;


/**
 * 커스텀 다이얼로그
 * Created by MH.D on 2017-03-31.
 */
public class MHDDialogUtil {
	
	public static DialogInterface mPopup;

	/**
	 * sAlert
	 */
	public static void sAlert(Context context, int msgId){
		sAlert(context, context.getString(msgId));
	}
	
	public static void sAlert(Context context, String msg){
		sAlert(context, msg, null);
	}
	
	public static void sAlert(Context context, int pId, String msg){
		sAlert(context, pId, -1, msg, null, null);
	}
	
	public static void sAlert(Context context, int msgId, DialogInterface.OnClickListener pListener){
		sAlert(context, R.string.alert_ok, -1, context.getString(msgId), pListener, null);
	}
	
	public static void sAlert(Context context, int pId, String msg, DialogInterface.OnClickListener pListener){
		sAlert(context, pId, -1, msg, pListener, null);
	}
	
	public static void sAlert(Context context, String msg, DialogInterface.OnClickListener pListener){
		sAlert(context, R.string.alert_ok, -1, msg, pListener, null);
	}

	public static void sAlert(Context context, int msg, DialogInterface.OnClickListener pListener, DialogInterface.OnClickListener nListener){
		sAlert(context, R.string.alert_ok, R.string.alert_cancel, context.getString(msg), pListener, nListener);
	}

	public static void sAlert(Context context, String msg, DialogInterface.OnClickListener pListener, DialogInterface.OnClickListener nListener){
		sAlert(context, R.string.alert_ok, R.string.alert_cancel, msg, pListener, nListener);
	}

	public static void sAlert(Context context, int msgId, DialogInterface.OnClickListener pListener, DialogInterface.OnClickListener cListener, DialogInterface.OnClickListener nListener){
		sAlert(context, R.string.alert_ok, R.string.alert_donotsee, R.string.alert_cancel, context.getString(msgId), pListener, cListener, nListener);
	}
	
	public static void sAlert(Context context, String msg, DialogInterface.OnClickListener pListener, DialogInterface.OnClickListener cListener, DialogInterface.OnClickListener nListener){
		sAlert(context, R.string.alert_ok, R.string.alert_donotsee, R.string.alert_cancel, msg, pListener, cListener, nListener);
	}
	
	public static void sAlert(Context context, int pId, int nId, String msg, DialogInterface.OnClickListener pListener, DialogInterface.OnClickListener nListener){
		sAlert(context, pId, -1, nId, msg, pListener, null, nListener);
	}
	
	public static void sAlert(Context context, int pId, int cId, int nId, String msg, DialogInterface.OnClickListener pListener, DialogInterface.OnClickListener cListener, DialogInterface.OnClickListener nListener){
		final AlertDialog.Builder dialog = new AlertDialog.Builder(context);
		if(pId >= 0){
			dialog.setNegativeButton(pId, pListener);	
		}
		
		if(cId >= 0){
			dialog.setNeutralButton(cId, cListener);
		}
		
		if(nId >= 0){
			dialog.setPositiveButton(nId, nListener);	
		}
		dialog.setMessage(msg);
		dialog.setCancelable(false);
		
		Activity activity = (Activity) context;
		try {
			activity.runOnUiThread(new Runnable() {

				@Override
				public void run() {
					mPopup = dialog.show();
				}
			});
		} 
		catch (BadTokenException e) {
			mPopup = dialog.show();
		}
		
	}
	/**
	 * dismiss
	 */
	public static void dissmis() {
		if(mPopup != null) {
			mPopup.dismiss();
		}
	}
	/**
	* 현재 디스플레이 화면에 비례한 DP단위를 픽셀 크기로 반환
	*/
	public static int PixelFromDP(Context context, float dp) {
		return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics());
	}
	
}
