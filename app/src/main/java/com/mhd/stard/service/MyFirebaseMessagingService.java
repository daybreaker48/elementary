package com.mhd.stard.service;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.widget.RemoteViews;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.mhd.stard.R;
import com.mhd.stard.activity.StartActivity;
import com.mhd.stard.common.MHDApplication;
import com.mhd.stard.common.MHDPreferences;
import com.mhd.stard.network.MHDNetworkInvoker;
import com.mhd.stard.util.MHDLog;

import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

/**
 * Created by MH D on 2017-12-15.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private final String TAG = getClass().getName();
    String newToken = "";
    String pastToken = "";

    public MyFirebaseMessagingService() {
        super();

        Task<String> token = FirebaseMessaging.getInstance().getToken();
        token.addOnCompleteListener(new OnCompleteListener<String>() {
            @Override
            public void onComplete(@NonNull Task<String> task) {
                if(task.isSuccessful()){
                    newToken = task.getResult();
                    pastToken = MHDPreferences.getInstance().getPrefFcmToken();

                    MHDApplication.getInstance().getMHDSvcManager().setFcmToken(newToken);
                    MHDPreferences.getInstance().savePrefFcmToken(newToken);
                    if(MHDApplication.getInstance().getMHDSvcManager().getUserVo() != null)
                        MHDApplication.getInstance().getMHDSvcManager().getUserVo().setUuToken(newToken);

                    // 저장된 토큰이 있었을 때 새로운 토큰과 서로 다르다면 서버전송
                    // 회원테이블에 지난 토큰값이 있었다면 update, 없다면 update 안될거고.
                    if(!"".equals(pastToken) && !pastToken.equals(newToken))
                        sendTokenToServer(pastToken, newToken);

                    MHDLog.d(TAG, "dagian MyFirebaseMessagingService >>> " + newToken);
                }
            }
        });
    }

    @Override
    public void onNewToken(@NonNull String stoken) {
        super.onNewToken(stoken);

        String refreshedToken = stoken;
        MHDLog.d(TAG, "dagian onNewToken >>> " + refreshedToken);

        pastToken = MHDPreferences.getInstance().getPrefFcmToken();
        MHDApplication.getInstance().getMHDSvcManager().setFcmToken(refreshedToken);
        MHDPreferences.getInstance().savePrefFcmToken(refreshedToken);
        MHDApplication.getInstance().getMHDSvcManager().getUserVo().setUuToken(refreshedToken);

        // 회원테이블에 지난 토큰값이 있었다면 update, 없다면 update 안될거고.
        sendTokenToServer(pastToken, refreshedToken);
    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
//        super.onMessageReceived(remoteMessage);

        if (remoteMessage.getData().size() > 0) {
            showNotification(remoteMessage.getData().get("title"), remoteMessage.getData().get("body"));
        }
    }

    private void sendTokenToServer(String token, String newToken){
        try{
            MHDLog.d(TAG, "dagian onNewToken > sendTokenToServer token >>> " + token);
            MHDLog.d(TAG, "dagian onNewToken > sendTokenToServer newToken >>> " + newToken);

            // Map 방식 0
            Map<String, String> params = new HashMap<String, String>();
            params.put("UUID", MHDPreferences.getInstance().getPrefDvcMmbrIdNo());
            params.put("PAST", token);
            params.put("UUTOKEN", newToken);

            MHDNetworkInvoker.getInstance().sendVolleyRequest(this, R.string.url_restapi_fcmtoken, params, null);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            MHDLog.printException(e);
        }

    }

    private RemoteViews getCustomDesign(String title, String message) {
        MHDLog.d(TAG, "dagian getCustomDesign");
        RemoteViews remoteViews = new RemoteViews(getApplicationContext().getPackageName(), R.layout.noti);
        remoteViews.setTextViewText(R.id.noti_title, title);
        remoteViews.setTextViewText(R.id.noti_message, message);
        remoteViews.setImageViewResource(R.id.logo, R.drawable.kid_select);
        return remoteViews;
    }

    public void showNotification(String title, String message) {
        //팝업 터치시 이동할 액티비티를 지정합니다.
        MHDLog.d(TAG, "dagian showNotification");
        Intent intent = new Intent(this, StartActivity.class);
        //알림 채널 아이디 : 본인 하고싶으신대로...
        String channel_id = "starD_ID";
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        //기본 사운드로 알림음 설정. 커스텀하려면 소리 파일의 uri 입력
        Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), channel_id)
                .setSmallIcon(R.drawable.kid_select)
                .setSound(uri)
                .setAutoCancel(true)
                .setVibrate(new long[]{1000, 1000, 1000}) //알림시 진동 설정 : 1초 진동, 1초 쉬고, 1초 진동
                .setOnlyAlertOnce(true) //동일한 알림은 한번만.. : 확인 하면 다시 울림
                .setContentIntent(pendingIntent);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) { //안드로이드 버전이 커스텀 알림을 불러올 수 있는 버전이면
            //커스텀 레이아웃 호출
            builder = builder.setContent(getCustomDesign(title, message));
        } else { //아니면 기본 레이아웃 호출
            builder = builder.setContentTitle(title)
                    .setContentText(message)
                    .setSmallIcon(R.drawable.kid_select); //커스텀 레이아웃에 사용된 로고 파일과 동일하게..
        }

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        //알림 채널이 필요한 안드로이드 버전을 위한 코드
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(channel_id, "CHN_NAME", NotificationManager.IMPORTANCE_HIGH);
            notificationChannel.setSound(uri, null);
            notificationManager.createNotificationChannel(notificationChannel);
        }
        //알림 표시 !
        notificationManager.notify(0, builder.build());
    }
}
