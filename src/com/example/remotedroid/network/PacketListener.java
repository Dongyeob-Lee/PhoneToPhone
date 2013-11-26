package com.example.remotedroid.network;

public interface PacketListener {
	public void onPacketReceived(Packet packet);
	public void onInterrupt();
}
