package com.example.remotedroid;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;

import org.apache.http.util.ByteArrayBuffer;

import com.example.remotedroid.natives.FrameHandler;
import com.example.remotedroid.network.Packet;
import com.example.remotedroid.network.PacketHeader;
import com.example.remotedroid.network.PacketListener;
import com.example.remotedroid.network.PacketReceiver;
import com.example.remotedroid.network.ServerConnectionListener;
import com.example.remotedroid.network.Tranceiver;
import com.example.remotedroid.network.PacketHeader.OpCode;
import com.example.remotedroid.util.HongUtil;

import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity_Select_Thread extends Activity {
/////////////////
	private static final int MAXDATASIZE = 4090;
	 int WIDTH = FrameHandler.WIDTH;
	    int HEIGHT = FrameHandler.HEIGHT;
	    int BYTE_PER_PIXEL = FrameHandler.BYTE_PER_PIXEL;
	    int SCREEN_NUM = FrameHandler.SCREEN_NUM; // 화면 갯수
	    int size = FrameHandler.size;
	    Bitmap bitmap1, bitmap2;
	    byte[] screenbuff= new byte[1000000];
	    int bufsize;
	    //////////////
	ImageView rView;
	TextView chatmessage;
	TextView tvAddress;
	EditText inputText;
	EditText etAddress;
	Button btnsend;
	Button btnconnect;
	String Serverip;
	String myIp;
	int port = 1111;
	int mode;
	
	Socket socket;
	DataInputStream input;
	DataOutputStream output;
	ServerSocket serverSocket;
	
	ServerThread serverThread;
	ClientThread clientThread;
	//////////////////////////////
//	PacketReceiver packetReceiver;
	Tranceiver tranceiver;
	ServerConnectionListener listener;
	PacketListener packetlistener;
	int receivedSize=0;
	int jpgTotalSize=0;
	//////////////////////////////
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_select_thread);
		Intent intent = getIntent();
		inputText = (EditText)findViewById(R.id.inputField);
		etAddress = (EditText)findViewById(R.id.ipfield);
		tvAddress = (TextView)findViewById(R.id.ip);
		btnconnect = (Button)findViewById(R.id.connectButton);
		btnsend = (Button)findViewById(R.id.sendButton);
		///////////////////
		rView = (ImageView)findViewById(R.id.imageView1);
		tranceiver = new Tranceiver(listener);
		////////////

		WifiManager wifiManager = (WifiManager) getSystemService(WIFI_SERVICE);
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        int ipAddress = wifiInfo.getIpAddress();
        myIp =
            ((ipAddress >> 0) & 0xFF) + "." +
            ((ipAddress >> 8) & 0xFF) + "." +
            ((ipAddress >> 16) & 0xFF) + "." +
            ((ipAddress >> 24) & 0xFF);
        
        
		tvAddress.setText(myIp);
		if(intent.getExtras().get("mode").toString().equals("server")){
			mode=1;
		}else{
			mode=2;
		}
		
		if(mode==1){

		}else if(mode==2){
		//	print("����� Ŭ���̾�Ʈ��");
			//connect();
//			SendThread sendThread = new SendThread(handler);
//			sendThread.start();
		}
		btnconnect.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(mode==1){
					
				}else if(mode==2){
					connectServer();
					clientThread.start();
				}
			}
		});
		btnsend.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String text = inputText.getText().toString();
				inputText.setText("");

				if(mode==1){
					serverThread.send(text);
				}else if(mode==2){
		//			clientThread.send(text);
				}
			}
		});
		// Kickoff the Client
		
		
	}
	public void connectServer(){
	//	clientThread = new ClientThread(handler, etAddress.getText().toString(), tranceiver,this);
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}
	
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if(mode==1){
			serverThread.interrupt();
			serverThread.destroySockect();
			//serverThread.st
		}else if(mode==2){
			clientThread.interrupt();
		//	clientThread.destroySockect();
			
		}
	}

	void printscreen(int length){
	        bitmap1 = Bitmap.createBitmap(WIDTH, HEIGHT, Bitmap.Config.ARGB_8888);
	        bitmap1 = BitmapFactory.decodeByteArray(screenbuff, 0,length);
	}
	Handler handler = new Handler() {

		public void handleMessage(Message message) {
			super.handleMessage(message);
			if(message.what==1){
				rView.setImageBitmap(bitmap1);
			}else if(message.what==0){
				
				setContentView(R.layout.remoteview);
				rView = (ImageView)findViewById(R.id.remote_view);
			}
		}
	};
}
