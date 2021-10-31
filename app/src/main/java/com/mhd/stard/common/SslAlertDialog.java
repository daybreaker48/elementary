package com.mhd.stard.common;

import android.app.Activity;
import androidx.appcompat.app.AlertDialog;

import android.content.Context;
import android.content.DialogInterface;
import android.webkit.SslErrorHandler;

public class SslAlertDialog {
    private SslErrorHandler handler = null;
    private AlertDialog dialog = null;

    public SslAlertDialog(SslErrorHandler errorHandler, Context context){
        if(errorHandler == null || context == null) return;

        this.handler = errorHandler;
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage("ssl인증서가 올바르지 않습니다. 계속 진행하시겠습니까?");
        builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                handler.proceed();
            }
        });
        builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                handler.cancel();
            }
        });
        dialog = builder.create();
    }

    public void show(){
        dialog.show();
    }
}
