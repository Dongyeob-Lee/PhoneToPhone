package com.example.remotedroid.network;

public interface ScreenTransmissionListener {
	public void onScreenTransferRequested();
	public void onScreenTransferStopRequested();
	public void onScreenTransferInterrupted();
}
