 
/*
 * Remoteroid - A remote control solution for Android platform, including handy file transfer and notify-to-PC.
 * Copyright (C) 2012 Taeho Kim(jyte82@gmail.com), Hyomin Oh(ohmnia1112@gmail.com), Hongkyun Kim(godgjdgjd@nate.com), Yongwan Hwang(singerhwang@gmail.com)
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
*/

package com.example.remotedroid.service;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;


import com.example.remotedroid.IRemoteroid;
import com.example.remotedroid.MainActivity_Select_Thread;
import com.example.remotedroid.intent.RemoteroidIntent;
import com.example.remotedroid.natives.FrameHandler;
import com.example.remotedroid.natives.InputHandler;
import com.example.remotedroid.network.AddOptionListener;
import com.example.remotedroid.network.FileTransmissionListener;
import com.example.remotedroid.network.PacketHeader.OpCode;
import com.example.remotedroid.network.ScreenTransmissionListener;
import com.example.remotedroid.network.ServerConnectionListener;
import com.example.remotedroid.network.Tranceiver;
import com.example.remotedroid.network.VirtualEventListener;
import com.example.remotedroid.util.CommandLine;
import com.example.remotedroid.util.HongUtil;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.PendingIntent.CanceledException;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteException;
import android.telephony.PhoneStateListener;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.util.Log;

/**
 * A base service to communicate with PC.
 * @author Taeho Kim
 *
 */
public class RemoteroidService extends Service 
					implements ServerConnectionListener, FileTransmissionListener, VirtualEventListener, ScreenTransmissionListener, AddOptionListener{
	public enum ServiceState{IDLE, CONNECTING, CONNECTED};
	
	private Tranceiver mTransmitter;
	private InputHandler mInputHandler;
	private static ServiceState mState = ServiceState.IDLE;
	private FrameHandler frameHandler;
	
	private String DEBUG_STATE = "debug_state";
	 
	private Handler handler;
	
	private IBinder mBinder = new IRemoteroid.Stub() {
		
		@Override
		public void onNotificationCatched(final String notificationText, long when, final int type)
				throws RemoteException {
			if(mTransmitter!=null && mTransmitter.isConnected()){
				new AsyncTask<Void, Void, Void>(){
	
					@Override
					protected Void doInBackground(Void... params) {
						mTransmitter.sendNotification(notificationText,type);
						return null;
					}
					
				}.execute();
			}
			
		}

		@Override
		public String getConnectionStatus() throws RemoteException {
			return mState.name();
		}

		@Override
		public void connect(final String ipAddress)
				throws RemoteException {
			
			new AsyncTask<Void, Void, Void>(){

				@Override
				protected Void doInBackground(Void... params) {
						
						
						// Open input device
						mInputHandler.open();
						
						
						// Start connection and receive events from server
						mTransmitter.connect(ipAddress);
						//Send devices resolution to host for coordinate transformation;
						if(mTransmitter!=null && mTransmitter.isConnected()){
							mState = ServiceState.CONNECTING;
							mTransmitter.sendDeviceInfo(getApplicationContext().getResources().getDisplayMetrics());
						}
						
						return null;	
				}
				
			}.execute();
			
		}

		@Override
		public void disconnect() throws RemoteException {
			
			TelephonyManager telManager = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
			telManager.listen(mPhoneListener, PhoneStateListener.LISTEN_NONE);
			dismissNotification();
			//onScreenTransferStopRequested();
			if(ScreenStateReceiver.isOrderedBroadcast()){
				unregisterReceiver(ScreenStateReceiver);
			}
			// Do time-consuming (blocks UI thread, causes activity death) task on here
			new AsyncTask<Void, Void, Void>(){

				@Override
				protected Void doInBackground(Void... params) {					
					mInputHandler.close();
					mTransmitter.disconnect();
					Log.i(DEBUG_STATE,"disconnect()");
					mState = ServiceState.IDLE;
					return null;
				}

			}.execute();
		}


		@Override
		public boolean isConnected() throws RemoteException {
			return (mTransmitter!=null && mTransmitter.isConnected()) ? true : false;
		}

		@Override
		public void requestFragmentBeShown() throws RemoteException {			
			if(mTransmitter.isConnected()){
				sendBroadcast(new Intent(RemoteroidIntent.ACTION_SHOW_CONNECTED_FRAGMENT));
			}else{
				sendBroadcast(new Intent(RemoteroidIntent.ACTION_SHOW_CONNECT_FRAGMENT));
			}
		}
	};

	@Override
	public IBinder onBind(Intent intent) {
		return mBinder;
	}
	
	public ServiceState getConnectionState(){
		return mState;
	}
	
	@Override
	public void onCreate() {
		super.onCreate();
		mTransmitter = new Tranceiver(this);
		mTransmitter.setFileTransmissionListener(this);
		mTransmitter.setVirtualEventListener(this);
		mTransmitter.setFrameBufferListener(this);
		mTransmitter.setAddOptionListener(this);
		
		
		mInputHandler = new InputHandler();
		frameHandler = new FrameHandler(getApplicationContext());
		
		handler = new Handler();
	}
	
	private PhoneStateListener mPhoneListener = new PhoneStateListener(){

		@Override
		public void onCallStateChanged(int state, final String incomingNumber) {
			if(state==TelephonyManager.CALL_STATE_RINGING){
				// TODO Notify call catched!
				if(mTransmitter!=null && mTransmitter.isConnected()){
					new AsyncTask<Void, Void, Void>(){
		
						@Override
						protected Void doInBackground(Void... params) {
							mTransmitter.sendNotification(String.format(getString(com.example.remotedroid.R.string.hello_world), incomingNumber), OpCode.NOTIFICATION_SEND);
							return null;
						}
						
					}.execute();
				}
			}
		}
		
	};


	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
//		if(intent != null &&intent.getExtras()!=null && intent.getParcelableArrayListExtra(SmsReceiver.EXTRA_MSGS)!=null){
//			ArrayList<RDSmsMessage> list = intent.getParcelableArrayListExtra(SmsReceiver.EXTRA_MSGS);
//			for(RDSmsMessage msg : list){
//				System.out.println(msg.toString());
//				// TODO Message received..
//			}
//		}
		
		return super.onStartCommand(intent, flags, startId);
	}


	@Override
	public void onDestroy() {
		super.onDestroy();
		if(ScreenStateReceiver.isOrderedBroadcast()){
			unregisterReceiver(ScreenStateReceiver);
		}
	}


	/**
	 * Place a phone call.
	 * @param phoneNumber a Phone number
	 */
	@SuppressWarnings("unused")
	private void callPhone(String phoneNumber){
		startActivity(new Intent(Intent.ACTION_CALL).setData(Uri.parse("tel:"+phoneNumber)));
	}
	
	/**
	 * Send a SMS.
	 * @param phoneNumber a Phone number
	 * @param body SMS body text
	 */
	
	private static final int NOTIFICATION_ID = 2012;
	
	@SuppressWarnings("deprecation")
	private void showConnectionNotification(String ipAddress){
	

	}
	
	private void dismissNotification(){
		NotificationManager notifManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
		notifManager.cancelAll();
	}


	@Override
	public void onSetCoordinates(int xPosition, int yPosition) {
		if(mInputHandler.isDeviceOpened())
			mInputHandler.touchSetPtr(xPosition, yPosition);
		
	}


	@Override
	public void onTouchDown() {
		if(mInputHandler.isDeviceOpened())
			mInputHandler.touchDown();		
	}


	@Override
	public void onTouchUp() {
		if(mInputHandler.isDeviceOpened())
			mInputHandler.touchUp();		
	}


	@Override
	public void onKeyDown(int keyCode) {
		if(mInputHandler.isDeviceOpened())
			mInputHandler.keyDown(keyCode);
	}


	@Override
	public void onKeyUp(int keyCode) {
		if(mInputHandler.isDeviceOpened()){
			mInputHandler.keyUp(keyCode);
		}
	}
	
	@Override
	public void onKeyStroke(int keyCode) {
		if(mInputHandler.isDeviceOpened())
			mInputHandler.keyStroke(keyCode);
	}


	@Override
	public void onFileInfoReceived(String fileName, long size) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void onReadyToSend(ArrayList<File> filesToSend) {
		// TODO Auto-generated method stub
		
	}

	
	private boolean isTransmission = false; // Taeho : What is this variable means for?

	private Object synchronizedScreenTrans = new Object();
	@Override
	public void onScreenTransferRequested() {
		Log.i(DEBUG_STATE,"onScreenTransferRequested()");
		
		// prevent for multiple thread
		if(isTransmission)
			return;
		
		isTransmission = true;
		
		synchronized(synchronizedScreenTrans){
			CommandLine.execAsRoot("chmod 664 /dev/graphics/fb0");
			CommandLine.execAsRoot("chmod 664 /dev/graphics/fb1");
		}
		
		
		Thread mThread = new Thread(){
			@Override
			public void run() {
				
				while(isTransmission){		
					byte[] frameStream;
					synchronized(synchronizedScreenTrans){
						
						frameStream = frameHandler.getFrameStream();
						
					}
					int rotation = HongUtil.getRotation(getApplicationContext());				
					
					mTransmitter.screenTransmission(frameStream, rotation, frameHandler.getJpegSize());
				}
			}
		};
		mThread.setDaemon(true);
		mThread.start();
	}

	@Override
	public void onScreenTransferStopRequested() {
		Log.i(DEBUG_STATE,"onScreenTransferStopRequested()");
		
		if(!isTransmission)
			return;
		
		isTransmission = false;
		synchronized(synchronizedScreenTrans){
			CommandLine.execAsRoot("chmod 660 /dev/graphics/fb0");
			CommandLine.execAsRoot("chmod 660 /dev/graphics/fb1");
		}
	}
	

	@Override
	public void onServerConnected(String ipAddress) {
		// Listen incoming calls
		TelephonyManager telManager = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
		telManager.listen(mPhoneListener, PhoneStateListener.LISTEN_CALL_STATE);
		
		sendBroadcast(new Intent(RemoteroidIntent.ACTION_SHOW_CONNECTED_FRAGMENT));
		
		IntentFilter filter = new IntentFilter();
		filter.addAction(Intent.ACTION_SCREEN_OFF);
		filter.addAction(Intent.ACTION_SCREEN_ON);
		registerReceiver(ScreenStateReceiver, filter);
		
		showConnectionNotification(ipAddress);
		mState = ServiceState.CONNECTED;
	}
	
	@Override
	public void onServerConnectionFailed() {
		Log.i(DEBUG_STATE,"onServerConnectionFailed()");
		mState = ServiceState.IDLE;
		sendBroadcast(new Intent(RemoteroidIntent.ACTION_CONNECTION_FAILED));
		
		dismissNotification();
		
		new AsyncTask<Void, Void, Void>(){
			@Override
			protected Void doInBackground(Void... params) {				
				mInputHandler.close();				
				return null;
			}

		}.execute();
	}

	@Override
	public void onServerConnectionInterrupted() {
		Log.i(DEBUG_STATE,"onServerConnectionInterrupted()");
		mState = ServiceState.IDLE;
		//onScreenTransferStopRequested();
		sendBroadcast(new Intent(RemoteroidIntent.ACTION_INTERRUPTED));
		dismissNotification();
		
		new AsyncTask<Void, Void, Void>(){
			@Override
			protected Void doInBackground(Void... params) {				
				mInputHandler.close();				
				return null;
			}

		}.execute();
	}

	@Override
	public void onServerDisconnected() {
		Log.i(DEBUG_STATE,"onServerDisconnected()");
		mState = ServiceState.IDLE;		
		// Sending broadcast for disconnection..
		sendBroadcast(new Intent(RemoteroidIntent.ACTION_SHOW_CONNECT_FRAGMENT));
		dismissNotification();
	}

	@Override
	public void onScreenTransferInterrupted() {
		Log.i(DEBUG_STATE,"onScreenTransferInterrupted()");		
		onScreenTransferStopRequested();
	}


	
	BroadcastReceiver ScreenStateReceiver = new BroadcastReceiver() {
		
		private static final String SCREEN_OFF = "android.intent.action.SCREEN_OFF";
		private static final String SCREEN_ON = "android.intent.action.SCREEN_ON";
		private String DEBUG_SCREEN = "ScreenStateReceiver";
		
		@Override
		public void onReceive(Context context, Intent intent) {
		
			if(intent.getAction().equals(SCREEN_ON)){
				Log.i(DEBUG_SCREEN, "SCREEN_ON");
				mTransmitter.sendScreenOnOffState(true);
			}
			else if(intent.getAction().equals(SCREEN_OFF)){
				Log.i(DEBUG_SCREEN, "SCREEN_OFF");
				mTransmitter.sendScreenOnOffState(false);
			}
			
		}
	};

	@Override
	public void setClipBoard(final String message) {
		// TODO Auto-generated method stub

		HongUtil.setClipBoard(getApplicationContext(), message,handler);

	}

	@Override
	public void onStartFileExplorer() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onSendKakaotalkMessage(String msg) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onSendFileInfo(File file) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onFileSent(File file) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onFileTransferInterrupted() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onFileTransferSucceeded() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onAllFileTransferSucceeded() {
		// TODO Auto-generated method stub
		
	}
}
