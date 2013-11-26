package com.example.remotedroid;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicBoolean;

import com.example.remotedroid.network.Packet;
import com.example.remotedroid.network.PacketListener;
import com.example.remotedroid.network.PacketReceiver;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class ServerThread extends Thread {
	private AtomicBoolean isRunning; 
	Handler handler;
	ServerSocket serverSocket;
	static int port=1111;
	Socket sock;
	PacketReceiver pakcetReceiver;
	PacketListener listener;
	Context mContext;
	private OutputStream sendStream;
	private InputStream recvStream;
	
	String recieved;
	public ServerThread(Handler handler, PacketListener listener, Context context) {
		super();
		this.handler = handler;
		this.listener = listener;
		this.mContext = context;
	}

	public void run() {
		// TODO Auto-generated method stub
		createSocket();
		System.out.println("create server socket");
		try {
			sock = serverSocket.accept();
			Message message = handler.obtainMessage(0);
			handler.sendMessage(message);
			
			sendStream = sock.getOutputStream();
			recvStream = sock.getInputStream();
			message = handler.obtainMessage(1, sendStream);
			handler.sendMessage(message);
			pakcetReceiver = new PacketReceiver(recvStream, listener);
			pakcetReceiver.start();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		while(true){
			if(sock.isConnected()){

				
			}else{
				Message message = handler.obtainMessage(1, "연결이 끊어졌습니다.");
				
				handler.sendMessage(message);
				try {
					sendStream.close();
					recvStream.close();
					sock.close();
					serverSocket.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				break;
			}
		}
	}
	public void createSocket(){
		try {
			serverSocket = new ServerSocket(port);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void send(String message){
		if(sock.isConnected()){

		}else{
			Message msg = handler.obtainMessage(1, "연결이 끊어졌습니다.");
			
			handler.sendMessage(msg);
		}
	}
	public void destroySockect(){
		try {
			if(sock!=null){
				sock.close();
			}
			serverSocket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void setRunningState(boolean state){
		
	}
}
