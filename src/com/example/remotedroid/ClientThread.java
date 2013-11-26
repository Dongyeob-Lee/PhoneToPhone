package com.example.remotedroid;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import com.example.remotedroid.natives.FrameHandler;
import com.example.remotedroid.network.Packet;
import com.example.remotedroid.network.Tranceiver;
import com.example.remotedroid.network.PacketHeader.OpCode;
import com.example.remotedroid.service.RemoteroidService;
import com.example.remotedroid.util.CommandLine;
import com.example.remotedroid.util.HongUtil;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class ClientThread extends Thread {

	Socket socket;
	DataInputStream input;
	DataOutputStream output;
	Handler handler;
	String ipaddr;
	static int port = 1111;
	////////////
	int Xposition;
	int Yposition;
	int EventCode;
	////////
	Tranceiver tranceiver;
	private FrameHandler frameHandler;
	//RemoteroidService remoteroidService;
	private Context context;
	
	public ClientThread(Handler handler, String ipaddr) {
		super();
		this.handler = handler;
		this.ipaddr = ipaddr;
	}


	public ClientThread(String ip, Tranceiver tranceiver, Context context) {
		// TODO Auto-generated constructor stub
		this.ipaddr = ip;
		this.tranceiver = tranceiver;
		this.context = context;
	}


	public void run() {

		frameHandler = new FrameHandler(context);
		tranceiver.connect(ipaddr);
		
		while(true){
			if(tranceiver.isConnected()){
				send();
				break;
			}
		}

	}
	public void send(){
		onScreenTransferRequested();
	}
	public void destroySockect(){
		try {
			input.close();
			output.close();
			socket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	private boolean isTransmission = false; // Taeho : What is this variable means for?

	private Object synchronizedScreenTrans = new Object();
	
	public void onScreenTransferRequested() {
		Log.i("screentransfer","onScreenTransferRequested()");
		
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
						//System.out.println(frameStream.length);
					}
					//Log.e("frame", frameStream.toString());
					int rotation = HongUtil.getRotation(context);				
					
					tranceiver.screenTransmission(frameStream, rotation, frameHandler.getJpegSize());
					//isTransmission = false;
				}
			}
		};
		mThread.setDaemon(true);
		mThread.start();
	}
	public void onScreenTransferStopRequested() {
		Log.i("screen","onScreenTransferStopRequested()");
		
		if(!isTransmission)
			return;
		
		isTransmission = false;
		synchronized(synchronizedScreenTrans){
			CommandLine.execAsRoot("chmod 660 /dev/graphics/fb0");
			CommandLine.execAsRoot("chmod 660 /dev/graphics/fb1");
		}
	}
}
