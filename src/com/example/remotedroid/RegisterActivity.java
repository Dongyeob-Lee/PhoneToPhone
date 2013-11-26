package com.example.remotedroid;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import com.example.remotedroid.R;
import com.google.android.gcm.GCMRegistrar;


import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class RegisterActivity extends Activity {

	EditText userNumber;
	EditText userName;
	EditText userMail;
	EditText userPassword;
	Button btnRegister;
	Button btnClear;
	String regID;
	List<Map<String, String>> items = null;
	Intent it;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        userNumber = (EditText)findViewById(R.id.usernumber);
        userName = (EditText)findViewById(R.id.username);
        userMail = (EditText)findViewById(R.id.usermail);
        userPassword = (EditText)findViewById(R.id.userpw);
        btnRegister = (Button)findViewById(R.id.btnsendregister);
        btnClear = (Button)findViewById(R.id.btnclear);
        GCMRegistrar.checkDevice(this);
        GCMRegistrar.checkManifest(this);
        
        final String regID = GCMRegistrar.getRegistrationId(this);
        
        if(regID.equals("")){
        	GCMRegistrar.register(this, "1026769505608");
        	System.out.println("register!!!!!!!!!!!!!!!!!!!!!!!!");
        	Log.e("reg", regID);
        }else{
        	System.out.println("registered:"+regID);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_register, menu);
        return true;
    }
    
    
    //�깅���린 �담��담���� ���留�� �담�����쇈�
    public void mOnClick(View v){
    	switch (v.getId()) {
		case R.id.btnsendregister:
			
			String email_id = userMail.getText().toString();
			String username = userName.getText().toString();
			String password = userPassword.getText().toString();
			String phonenumber = userNumber.getText().toString();
			HttpThread joinThread = new HttpThread("join", email_id, password, username, phonenumber,regID, handler);
			joinThread.start();
			
			//finish();
			break;
		case R.id.btnclear:
			userMail.setText("");
			userName.setText("");
			userPassword.setText("");
			userNumber.setText("");
			break;
		default:
			break;
		}
    }
    Handler handler = new Handler() {

		@SuppressWarnings("unchecked")
		@Override
		public void handleMessage(Message msg) {

			items = (List<Map<String, String>>) msg.obj;
			for(int i=0; i<items.size(); i++){
				
				System.out.println(items.get(i).get("title"));
				System.out.println(items.get(i));
			}
			if(items.get(0).get("title").equals("join"))
			{
				if(items.get(0).get("result").equals("true")){
					it = new Intent();
					it.setClass(getApplicationContext(), LoginActivity.class);
					PTPDialog.showAlertDialog("등록","서버에 성공적으로 등록하였습니다.", RegisterActivity.this);
					//사용자 계정이랑 비밀번호 저장파일 생성!!
					createInitFile();
				
					startActivityForResult(it, 1);
					finish();
				}else{
					PTPDialog.showAlertDialog("등록","서버에 사용자등록을 실패하였습니다.", RegisterActivity.this);
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
	protected void createInitFile() {
		// TODO Auto-generated method stub
		//Intent it = new Intent(this, LoginActivity.class);
		String dirPath = getFilesDir().getAbsolutePath();
		File file = new File(dirPath,"init.txt");
		
		try {
			file.createNewFile();
			
			FileOutputStream fos = openFileOutput("init.txt", Context.MODE_PRIVATE);
			
			fos.write((userMail.getText().toString()+","+userPassword.getText().toString()).getBytes());
			
			fos.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//startActivityForResult(it, 1);
		//finish();
	}
	public void registerGcm(){
		GCMRegistrar.checkDevice(this);
		GCMRegistrar.checkManifest(this);
		
		final String Id = GCMRegistrar.getRegistrationId(this);
		
		if (Id.equals("")) {
			GCMRegistrar.register(this, "1026769505608");
			regID=GCMRegistrar.getRegistrationId(this);;
			System.out.println("unregister:"+regID);
		}else {
			regID=Id;
			System.out.println("register:"+regID);
			Log.e("reg_id", Id);
		}
	}

}
