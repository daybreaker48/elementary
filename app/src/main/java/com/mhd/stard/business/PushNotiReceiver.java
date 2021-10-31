package com.mhd.stard.business;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;

import com.mhd.stard.MainActivity;
import com.mhd.stard.business.model.PushVo;
import com.mhd.stard.common.MHDApplication;
import com.mhd.stard.constant.MHDConstants;
import com.mhd.stard.util.MHDLog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;


/**
 * 푸시 Notibar Click 시 실행되는 class
 * Created by MH.D on 2017-04-04.
 */
public class PushNotiReceiver extends BroadcastReceiver {

	private static final String TAG = PushNotiReceiver.class.getName();

	/**
	 * onReceive
	 * @Override
	 */
	@Override
	public void onReceive (Context context, Intent intent) {
//		PushMsg msg = new PushMsg(intent.getExtras());
		//푸쉬 정보를 서버든 어디서든 받아와서 여기서 적절히 처리.
		String msg_data = "{\"url\":\"http:\\/\\/www.naver.com\"}";
		String msg_msgId = "00000";
		String msg_notiMsg = "notiMsg";
		String msg_message = "message";
		String msg_notiImg = "notiImg";
		JSONObject originalJson = null;

		//링크처리
		String link = "";
		try {
			originalJson = new JSONObject(msg_data); // msg.data [{"url":"http:\/\/www.naver.com"}]
			if(originalJson.has("url")){
				link = originalJson.getString("url");
			}
		} catch (JSONException e) {
			MHDLog.printException(e);
		}

		String workday = "";
		//Noti bar 를 통해서 들어왔으니 new push
		MHDApplication.getInstance().getMHDSvcManager().setIsNewPush(true);
		MHDLog.d(TAG, "msg_msgId.trim() >> " + msg_msgId.trim());

		PushVo pushVo = new PushVo();
		pushVo.setMsgId(msg_msgId.trim());
		pushVo.setWorkday(workday);
		pushVo.setNotiMsg(msg_notiMsg.trim());
		pushVo.setMessage(msg_message.trim());
		pushVo.setNotiImg(msg_notiImg.trim());
		pushVo.setLink(link);
		if(link != null && !"".equals(link)){
			pushVo.setIsLink(true);
		}
		else {
			pushVo.setIsLink(false);
		}

		MHDApplication.getInstance().getMHDSvcManager().setPushVo(pushVo);
		moveToPage(context);
	}
	/**
	 * 현재 App 상태에 따른 페이지 이동
	 */
	private void moveToPage(Context context) {
		//broadcast
		if(!getTopPackage(context).contains(context.getPackageName())) {		//app 상태 : background
			MHDLog.d(TAG, "App state >> BackGround");

			//start App. IntroActivity, StartActivity 가 있다면 해당 activity 설정
			Intent i = new Intent(context, MainActivity.class);
			if(MHDApplication.getInstance().getMHDSvcManager().getLoginVo()!= null && MHDApplication.getInstance().getMHDSvcManager().getLoginVo().getLsiv() != null ){		//로그인 중
				i = new Intent(context, MainActivity.class);
			}

			i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
			context.startActivity(i);
		}
		else {		//app 상태 : foreground
			MHDLog.d(TAG, "App state >> ForeGround");

			//send broadcast
			Intent i = new Intent(MHDConstants.IntentKey.PUSH_MSG_KEY);
			i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
			context.sendBroadcast(i);
		}
	}
	/**
	 * 최상위 패키지 체크
	 */
	private String getTopPackage(Context ctxt) {
		ActivityManager am = (ActivityManager) ctxt.getSystemService(Context.ACTIVITY_SERVICE);
		List<ActivityManager.RunningTaskInfo> taskInfo = am.getRunningTasks(1);
		ComponentName topActivity = taskInfo.get(0).topActivity;
		String topActivityName = topActivity.getPackageName();
		return topActivityName;
	}
}

/*
# Example

02-20 15:25:44.338: D/MSALog PushNotiReceiver(18208): reads : [{"msgId":"2868","workday":"20160220152544"}]
02-20 15:25:44.338: D/MSALog PushNotiReceiver(18208): msgId [2868], notiTitle [푸시팝업제목], notiMsg [푸시팝업메세지], notiImg [], message [<html>
02-20 15:25:44.338: D/MSALog PushNotiReceiver(18208): <head>
02-20 15:25:44.338: D/MSALog PushNotiReceiver(18208): <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
02-20 15:25:44.338: D/MSALog PushNotiReceiver(18208): <meta name="msgId" content="2868"/>
02-20 15:25:44.338: D/MSALog PushNotiReceiver(18208): <meta name="msgPushType" content="P"/>
02-20 15:25:44.338: D/MSALog PushNotiReceiver(18208): 	<title></title>
02-20 15:25:44.338: D/MSALog PushNotiReceiver(18208): </head>
02-20 15:25:44.338: D/MSALog PushNotiReceiver(18208): <body>
02-20 15:25:44.338: D/MSALog PushNotiReceiver(18208): <div>HTML 템플릿메세지</div>
02-20 15:25:44.338: D/MSALog PushNotiReceiver(18208): </body>
02-20 15:25:44.338: D/MSALog PushNotiReceiver(18208): </html>
02-20 15:25:44.338: D/MSALog PushNotiReceiver(18208): ], sound [null], msgType [H], data [{"url":"http:\/\/www.naver.com"}]
//		msg.msgId 		// 메세지 ID
//		msg.notiTitle 	// notification에 출력될 타이틀
//		msg.notiMsg 	// notification에 출력될 메시지 내용
//		msg.notiImg 	// notification에 출력될 이미지 URL
//		msg.message 	// (리치) 푸시 내용
//		msg.sound 		// 푸시 수신 시 출력될 사운드
//		msg.msgType 	// 메시지 타입 : H – html, T – Text, L – Link
//		msg.data 		// 추가 데이터 (채팅상담에서는 "l" 필드에 TCKT0000000001 와 같은 값을 담아준다 TCKT 고정)
 *
 *
 * 							data [{"url":"TCKT0000000027|NODE0000000003|SVTLK|NODE0000000001"}]  // old ver.
 * 							data [{"url":"TCKT0000000027,NODE0000000003,SVTLK,NODE0000000001"}]  // new ver.
 */
