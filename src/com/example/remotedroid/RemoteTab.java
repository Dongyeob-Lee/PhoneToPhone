package com.example.remotedroid;

import java.util.ArrayList;

import com.example.remotedroid.R;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;


public class RemoteTab extends Fragment {
	String myNumber;
	ListView friendList;
	EditText etSearchFriend;
	ImageButton btSearch;
	Context mContext;
	Button btRequest;
	
	///
	String myIp;
	String friendName;
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.activity_remote_tab, container,
				false);
		etSearchFriend = (EditText) v.findViewById(R.id.etSearchFriend);
		friendList = (ListView) v.findViewById(R.id.friendlist2);
		btSearch = (ImageButton)v.findViewById(R.id.friendSearchIcon);
		btRequest = (Button)v.findViewById(R.id.btRequestRemote);
		mContext = v.getContext();
		////ip address
		WifiManager wifiManager = (WifiManager) mContext.getSystemService(mContext.WIFI_SERVICE);
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        int ipAddress = wifiInfo.getIpAddress();
        myIp =
            ((ipAddress >> 0) & 0xFF) + "." +
            ((ipAddress >> 8) & 0xFF) + "." +
            ((ipAddress >> 16) & 0xFF) + "." +
            ((ipAddress >> 24) & 0xFF);
        ///////////////
		//get my number
		TelephonyManager mTelephonyManager = (TelephonyManager)mContext.getSystemService(Context.TELEPHONY_SERVICE);
		myNumber = mTelephonyManager.getLine1Number();
		
		Friendlist friendlist = new Friendlist(mContext,1);
		friendList.setAdapter(friendlist.getFriendItemsAdapter());
		//friendList.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		friendList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long l_position) {
				// TODO Auto-generated method stub
				 Friend friend = (Friend)parent.getAdapter().getItem(position);
				// showDialog(friend.getName(), myNumber, mContext);
				 friendName = friend.getName();
				 Toast.makeText(mContext, friend.getName(), Toast.LENGTH_SHORT).show();
				 showDialog(friendName, myNumber, mContext);
			}
		});
		
		
		return v;
	}	
	
	public void showDialog(final String friendname, final String mynumber, Context c){
		AlertDialog.Builder builder = new AlertDialog.Builder(c);
		AlertDialog alert = null;
		
		builder.setMessage(friendname + "님에게 원격요청을 하시겠습니까?")
				.setTitle("원격제어요청")
				.setCancelable(false)
				.setPositiveButton("Yes",
						new DialogInterface.OnClickListener() {
							
							//yes button click 
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								HttpThread httpThread = new HttpThread(MainActivity.REMOTE_REQUEST, friendname, mynumber,myIp);
								System.out.println("send http:"+MainActivity.REMOTE_REQUEST+","+friendname+","+mynumber);
								httpThread.start();
								System.out.println("Thread start");
								Toast.makeText(mContext, mynumber, Toast.LENGTH_SHORT).show();
								dialog.cancel();
								
								//serverThread star
								Intent remoteServerIntent = new Intent(mContext, RemoteServer.class);
								startActivity(remoteServerIntent);
							}
						})
				.setNegativeButton("No", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();

					}
				});
		
		alert = builder.create();
		alert.show();
	}
}
