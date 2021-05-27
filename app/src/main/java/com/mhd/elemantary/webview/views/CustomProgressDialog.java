package com.mhd.elemantary.webview.views;

import android.app.Dialog;
import android.content.Context;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.mhd.elemantary.R;
import com.mhd.elemantary.activity.BaseActivity;


/**
 * custom progress dialog
 */
public class CustomProgressDialog extends Dialog {

	private ImageView iv;
	private Context mContext;

	public CustomProgressDialog(Context context) {
//		super(context);
		super(context, R.style.TransparentProgressDialog);
		mContext = context;
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.setContentView(R.layout.layout_progress_dialog);
		this.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
		setCancelable(false);
		setTitle(null);
		// setCancelable(false);
		setOnCancelListener(null);
	}

	@Override
	public void show() {
		super.show();
	}

	@Override
	public void dismiss() {
		super.dismiss();
	}

	@Override
	public void onBackPressed() {
		if(mContext != null && mContext instanceof BaseActivity){
			dismiss();
			((BaseActivity)mContext).onBackPressed();
		}
	}


}