package com.example.remotedroid;
import java.util.List;
 
interface IRemoteroid{
	String getConnectionStatus(); 
	boolean isConnected();
	void connect(String ipAddress);
	void disconnect();
	void onNotificationCatched(String notificationText, long when,int type);
	void requestFragmentBeShown();
}
 