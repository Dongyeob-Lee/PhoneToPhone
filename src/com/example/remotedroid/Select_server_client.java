package com.example.remotedroid;

import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

public class Select_server_client extends Activity {

	TextView ipaddress;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       
        setContentView(R.layout.activity_select_server_client);
        ipaddress = (TextView)findViewById(R.id.myIpaddress);
        String myIp;
        WifiManager wifiManager = (WifiManager) getSystemService(WIFI_SERVICE);
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        int ipAddress = wifiInfo.getIpAddress();
        myIp =
            ((ipAddress >> 0) & 0xFF) + "." +
            ((ipAddress >> 8) & 0xFF) + "." +
            ((ipAddress >> 16) & 0xFF) + "." +
            ((ipAddress >> 24) & 0xFF);
        ipaddress.setText(myIp);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_select_server_client, menu);
        return true;
    }
    public void mOnClick(View v){
    	switch (v.getId()) {
		case R.id.select_server:
			Intent intent = new Intent(this, MainActivity_Select_Thread.class);
			intent.putExtra("mode", "server");
			startActivity(intent);
			break;
		case R.id.select_client:
			Intent intent2 = new Intent(this, MainActivity_Select_Thread.class);
			intent2.putExtra("mode", "client");
			startActivity(intent2);
			break;
		default:
			break;
		}
    }
}
