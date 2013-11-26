package com.example.remotedroid;

import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.example.remotedroid.MainActivity;
import com.google.android.gcm.GCMBaseIntentService;




public class GCMIntentService extends GCMBaseIntentService{

	private static void generateNotification(Context context, String message){
		int icon = com.example.remotedroid.R.drawable.ic_action_search;
		long when = System.currentTimeMillis();
		NotificationManager notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
		Notification.Builder notification = new Notification.Builder(context);
		//Notification notification = new Notification(icon,message,when);
		
		String title = context.getString(com.example.remotedroid.R.string.app_name);
		Intent notificationIntent = new Intent(context, MainActivity.class);
		
		notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
		PendingIntent intent = PendingIntent.getActivity(context, 0, notificationIntent, 0);
		
		notification.setTicker(message);
		notification.setAutoCancel(true);
		notification.setSmallIcon(icon);
		notification.setContentTitle(message);
		notification.setContentText(message);
		notification.setContentIntent(intent);
		notificationManager.notify(0, notification.getNotification()); 
	}
	@Override
	protected void onError(Context context, String intent) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void onMessage(Context context, Intent intent) {
		// TODO Auto-generated method stub
//		String msg = intent.getStringExtra("msg");
//		Log.e("getmessage", "getmessage:"+msg);
		String msg = null;
		String action = intent.getStringExtra("action");
		if(action.equals(MainActivity.REMOTE_REQUEST)){
//			msg = intent.getStringExtra("sender");
//			generateNotification(context, msg);
			Intent popupIntent = new Intent(context, RequestDialog.class);
			popupIntent.putExtra("ip",intent.getStringExtra("ip"));
			popupIntent.putExtra("msg", intent.getStringExtra("sender")+"님께서 원격제어 요청을 하셨습니다. 수락하시겠습니까?");
			PendingIntent pi = PendingIntent.getActivity(context, 0, popupIntent, PendingIntent.FLAG_ONE_SHOT);
			     
			try{
			 pi.send();
			}
			catch(Exception e){
			 Toast.makeText(context, e.toString(), Toast.LENGTH_LONG);
			}
			
		}else if(action.equals(MainActivity.REMOTE_ACCEPT)){
			
		}else if(action.equals(MainActivity.REMOTE_REJECT)){
			
		}else if(action.equals(MainActivity.SHARE_REQUEST)){
			Intent popupIntent = new Intent(context, RequestDialog.class);
			popupIntent.putExtra("msg", intent.getStringExtra("sender")+"님께서 화면공유 요청을 하셨습니다. 수락하시겠습니까?");
			PendingIntent pi = PendingIntent.getActivity(context, 0, popupIntent, PendingIntent.FLAG_ONE_SHOT);
			     
			try{
			 pi.send();
			}
			catch(Exception e){
			 Toast.makeText(context, e.toString(), Toast.LENGTH_LONG);
			}
		}else if(action.equals(MainActivity.SHARE_ACCEPT)){
			
		}else if(action.equals(MainActivity.SHARE_REJECT)){
			
		}
		
	}

	@Override
	protected void onRegistered(Context context, String reg_id) {
		// TODO Auto-generated method stub
		Log.e("키를 등록합니다.(GCM INTENTSERVICE)", reg_id);
	}

	@Override
	protected void onUnregistered(Context context, String arg1) {
		// TODO Auto-generated method stub
		Log.e("키를 제거합니다.(GCM INTENTSERVICE)", "제거되었습니다.");
	}
}
