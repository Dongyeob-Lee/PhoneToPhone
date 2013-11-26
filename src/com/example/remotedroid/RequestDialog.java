package com.example.remotedroid;

import com.example.remotedroid.network.ServerConnectionListener;
import com.example.remotedroid.network.Tranceiver;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class RequestDialog extends Activity {

	TextView textMsg;
	Button btYes;
	Button btNo;
	String sender;
	String ip;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_request_dialog);
		textMsg = (TextView)findViewById(R.id.text_msg);
		btYes = (Button)findViewById(R.id.bt_yes);
		btNo = (Button)findViewById(R.id.bt_no);
		Intent intent = getIntent();
		textMsg.setText(intent.getStringExtra("msg"));
		sender = intent.getStringExtra("sender");
		ip = intent.getStringExtra("ip");
		
		
		btYes.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				HttpThread httpThread = new HttpThread(MainActivity.REMOTE_ACCEPT, sender);
				httpThread.start();
				//요청을 수락하였으므로.. 여기서 바로 서버로 접속하는 클라이언트 생성.
				Tranceiver tranceiver;
				ServerConnectionListener listener = null;
				tranceiver = new Tranceiver(listener);
				ClientThread clientThread;
				clientThread = new ClientThread(ip, tranceiver, getApplicationContext());
				clientThread.start();
			}
		});
		btNo.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				HttpThread httpThread = new HttpThread(MainActivity.REMOTE_REJECT, sender);
				httpThread.start();
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_request_dialog, menu);
		return true;
	}

}
