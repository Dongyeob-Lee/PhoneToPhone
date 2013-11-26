package com.example.remotedroid.network;

public interface ServerConnectionListener {
	public void onServerConnected(String ipAddress);
	public void onServerConnectionFailed();
	public void onServerConnectionInterrupted();
	public void onServerDisconnected();
}
