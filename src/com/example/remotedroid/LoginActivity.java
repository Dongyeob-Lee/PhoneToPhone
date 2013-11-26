package com.example.remotedroid;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Map;

import com.example.remotedroid.R;
import com.example.remotedroid.RegisterActivity;


import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;

public class LoginActivity extends Activity {

	EditText etLogin;
	EditText etPW;
	Intent intent1; // 筌�옙占쏙옙�곤옙揶�옙占�占쎈�占쏙옙占쏙옙紐�옙占싼�옙占쏙옙	boolean autologin=true;
	boolean autologin=true;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        etLogin = (EditText)findViewById(R.id.etLogin);
        etPW = (EditText)findViewById(R.id.etPW);
        checkInitFile();
    }
    
    public void btnOnClick(View view){
    	switch (view.getId()) {
		case R.id.btn_login:
			String email_id = etLogin.getText().toString();
			String password = etPW.getText().toString();
			autologin=false;
			HttpThread joinThread = new HttpThread("login", email_id, password,handler);
			joinThread.start();
			//Intent intent1 = new Intent(LoginActivity.this, TabActivity.class);
			//startActivity(intent1);
			break;
		case R.id.btn_register:
			Intent intent2 = new Intent(LoginActivity.this, RegisterActivity.class);
			startActivity(intent2);
			break;
		default:
			break;
		}
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_login, menu);
        return true;
    }
    Handler handler = new Handler() {

		@SuppressWarnings("unchecked")
		@Override
		public void handleMessage(Message msg) {

			List<Map<String, String>> items = (List<Map<String, String>>) msg.obj;
			for(int i=0; i<items.size(); i++){
				
				System.out.println(items.get(i).get("title"));
				System.out.println(items.get(i));
			}
			if(items.get(0).get("title").equals("login"))
			{
				if(items.get(0).get("result").equals("true")){
					if(autologin){
						intent1 = new Intent();
						intent1.setClass(getApplicationContext(), TabActivity.class);
					
						startActivityForResult(intent1, 1);
						finish();
					}else{
						PTPDialog.showAlertDialog("로그인","성공적으로 로그인하였습니다.", LoginActivity.this);
						intent1 = new Intent();
						intent1.setClass(getApplicationContext(), TabActivity.class);
					
						startActivityForResult(intent1, 1);
						finish();
					}
				}else{
					PTPDialog.showAlertDialog("로그인","로그인 실패.", LoginActivity.this);
				}
			}
			
			System.out.println(items.toString());
		/*	SimpleAdapter adapter = new SimpleAdapter(this,
					items, R.layout.main, new String[] {
							JavamodelingMember.KEY_MEMBER_ID,
							JavamodelingMember.KEY_MEMBER_NAME },
					// ƒ√∑≥¿Ã ≥™≈∏≥æ View ¡ˆ¡§
					new int[] { R.id.text_id, R.id.text_name });

			this.setListAdapter(adapter);*/
		}
	};
	protected void checkInitFile() {
		// TODO Auto-generated method stub
		//Intent it = new Intent(this, LoginActivity.class);
		byte data[]=null;
		FileInputStream fis;
		String result; 
		
		try {
			fis = openFileInput("init.txt");
			data = new byte[fis.available()];
			while(fis.read(data)!=-1){
				;
			}
			fis.close();
			result=new String(data);
			String []b = result.split(",");
			HttpThread joinThread = new HttpThread("login",b[0],b[1],handler);
			joinThread.start();
		} catch (Exception e) {
			// TODO: handle exception
		}
		//startActivityForResult(it, 1);
		//finish();
	}
}
