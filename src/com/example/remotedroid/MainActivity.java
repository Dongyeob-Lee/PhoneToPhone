package com.example.remotedroid;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.ContactsContract;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;

public class MainActivity extends Activity {
	public static final String REMOTE_REQUEST = "remote_request";
	public static final String REMOTE_ACCEPT = "remote_accept";
	public static final String REMOTE_REJECT = "remote_reject";
	public static final String SHARE_REQUEST = "share_request";
	public static final String SHARE_ACCEPT = "share_accept";
	public static final String SHARE_REJECT = "share_reject";
	private Vector<String> arrPhoneList = new Vector<String>();
	private Vector<String> arrNameList = new Vector<String>();
	private List<FriendParseItem> friendParseItems =null;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		Button btnStart = (Button) findViewById(R.id.btn_start);
		//ReadFromDB();
		//createContactsFile();
		HttpThread thread = new HttpThread("contacts", this, handler);
		thread.start();
		btnStart.setOnClickListener(new Button.OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				createContactsFile();
				Intent intent = new Intent(MainActivity.this,
						LoginActivity.class);
				startActivity(intent);
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	//string buffer...��遺�������源�. �쇰� 二쇱�濡��遺�� �⑥���� �몄�二쇰� 遺�� 援ы�.
		// /���踰��遺�� xml濡�留��湲���� 硫����
		private StringBuffer makeXmlContacts(List<FriendParseItem> item) {

			StringBuffer stringBuffer = new StringBuffer();
			stringBuffer.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
			stringBuffer.append("<items>\n");
			for(int i=0; i<item.size(); i++){
				stringBuffer.append("\t<item itemnum='"+item.get(i).getItemnum()+"'>\n");
		
				stringBuffer.append("\t\t<name>" + item.get(i).getName() + "</name>\n");
				stringBuffer.append("\t\t<phone>" + item.get(i).getPhone() + "</phone>\n");
				if(item.get(i).isRegister()){
					stringBuffer.append("\t\t<register>true</register>\n");
				}else{
					stringBuffer.append("\t\t<register>false</register>\n");
				}
				stringBuffer.append("\t</item>\n");
			}
			
			stringBuffer.append("</items>");

			return stringBuffer;
		}

	protected void createContactsFile() {
		// TODO Auto-generated method stub
		//Intent it = new Intent(this, LoginActivity.class);
		String dirPath = getFilesDir().getAbsolutePath();
		File file = new File(dirPath,"contacts.xml");
		
		try {
			file.createNewFile();
			
			FileOutputStream fos = openFileOutput("contacts.xml", Context.MODE_PRIVATE);
			//獄�옙占쏙옙占쏙옙�쏙옙筌�옙占쏙옙�곤옙 筌�옙占썸묾占�			
			fos.write(makeXmlContacts(friendParseItems).toString().getBytes());
			
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
	 Handler handler = new Handler() {

			@SuppressWarnings("unchecked")
			@Override
			public void handleMessage(Message msg) {
				if(msg.what==1){
					friendParseItems = (List<FriendParseItem>) msg.obj;
				}
			}
		};
	private void ReadFromDB() {
//		Vector<String> arrPhoneList = new Vector<String>();
//		Vector<String> arrNameList = new Vector<String>();
		String numtemp;//'-'��옙占쏙옙占썸묾怨�옙占쏙옙占쏙옙占썼�紐�옙
		String[] arrProjection = { ContactsContract.Contacts._ID,
				ContactsContract.Contacts.DISPLAY_NAME };
		String[] arrPhoneProjection = { ContactsContract.CommonDataKinds.Phone.NUMBER };

		Cursor clsCursor = this
				.getContentResolver()
				.query(ContactsContract.Contacts.CONTENT_URI, arrProjection,
						ContactsContract.Contacts.HAS_PHONE_NUMBER + "=1",
						null, null);

		while (clsCursor.moveToNext()) {
			String strContactId = clsCursor.getString(0);

			Cursor clsPhoneCursor = this
					.getContentResolver()
					.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
							arrPhoneProjection,
							ContactsContract.CommonDataKinds.Phone.CONTACT_ID
									+ " = " + strContactId, null, null);
			while (clsPhoneCursor.moveToNext()) {
				// 占쎈�占썸�占쏙옙占쏙옙 甕곤옙占썹�占썲�占쏙옙占쏙옙�귐�옙占쎈�占�占쏙옙占쏙옙占쏙옙.
				numtemp = clsPhoneCursor.getString(0).replace("-", "");
				System.out.println(numtemp);
				numtemp = numtemp.replace(" ", "");
				System.out.println(numtemp);
				arrNameList.add(clsCursor.getString(1));
				arrPhoneList.add(numtemp);
			}

			clsPhoneCursor.close();
		}
		clsCursor.close();
	}
}
