package com.example.remotedroid;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;

import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import android.content.Context;
import android.database.Cursor;
import android.os.Handler;
import android.os.Message;
import android.provider.ContactsContract;


public class HttpThread extends Thread{
	String action;
	String email_id;
	String password;
	String username;
	String phonenumber;
	String mynum;
	String friendName;
	String regID;
	String ip;
	StringBuffer ContactsXml;
	InputStream inputStream;
	Handler handler;
	Context mContext;
	String tagName="";
	
	//회원가입 스레드 생성자
	public HttpThread(String action, String email_id, String password, String username, String phonenumber,String regID, Handler handler){
		this.action = action;
		this.email_id = email_id;
		this.password = password;
		this.username = username;
		this.phonenumber = phonenumber;
		this.regID = regID;
		this.handler = handler;
	}
	//로그인 스레드 생성자
	public HttpThread(String action, String email_id, String password , Handler handler){
		this.action = action;
		this.email_id = email_id;
		this.password = password;
		this.handler = handler;
	}
	///이건 보류... 주소록 보내기위한 생성자.
	public HttpThread(String action, Context context, Handler handler)
	{
		this.action = action;
		this.mContext = context;
		this.handler = handler;
	}
	
	//원격제어 요청 보내기 위한 스레드 생성자
	public HttpThread(String action, String friendName, String mynum, String ip){
		this.action = action;
		this.friendName = friendName;
		this.mynum = mynum;
		this.ip = ip;
	}
	
	//요청에 대한 수락 또는 거절을 보내줄 때..
	public HttpThread(String action, String sender) {
		// TODO Auto-generated constructor stub
		this.action = action;
		this.username = sender;
	}
	@Override
	public void run() {
		
		// TODO Auto-generated method stub
		//HttpConnectAdapter.executeJoin(url,email_id,password,username,gender);
		//멀티파트로 문자열 보내기
		
		HttpClient httpClient = new DefaultHttpClient();
		String url = "http://192.168.0.41:8080/ptpServer/ptp.servlet";

		HttpPost httpPost = new HttpPost(url);
		System.out.println("action:"+action);
		if(action.equals("contacts")){
			//여기도 보류 주소록..보내는 곳..
			try {
				List<NameValuePair> postParameters = new ArrayList<NameValuePair>();
				
				NameValuePair actionValuePair = new BasicNameValuePair("action", "contacts");
				postParameters.add(actionValuePair);
				UrlEncodedFormEntity urlEncodedFormEntity = new UrlEncodedFormEntity(
						postParameters, "EUC-KR");

				httpPost.setEntity(urlEncodedFormEntity);
				
				//////////////////response
				HttpResponse response;
				XmlPullParserFactory factory = XmlPullParserFactory
						.newInstance();
				factory.setNamespaceAware(true);

				response =  httpClient.execute(httpPost);
				InputStream inputStream = response.getEntity().getContent();
				System.out.println(inputStream.toString());
				itemXmlParser parser = new itemXmlParser(inputStream);

				List<FriendParseItem> javamodelingMembers = parser.parseContact();

				Message message = handler.obtainMessage(1);

				message.obj = javamodelingMembers;

				handler.sendMessage(message);
				
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (XmlPullParserException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else if(action.equals(MainActivity.REMOTE_REQUEST)){
			try {
				List<NameValuePair> postParameters = new ArrayList<NameValuePair>();
				System.out.println("request action:"+mynum+","+friendName+","+ip);
				NameValuePair actionNaValuePair = new BasicNameValuePair("action", MainActivity.REMOTE_REQUEST);
				postParameters.add(actionNaValuePair);
				NameValuePair email_idNameValuePair = new BasicNameValuePair("mynum",
						mynum);
				postParameters.add(email_idNameValuePair);
				NameValuePair passwordNameValuePair = new BasicNameValuePair("friendname",
						friendName);
				postParameters.add(passwordNameValuePair);
				NameValuePair ipAddressNameValuePair = new BasicNameValuePair("ip",
						ip);
				postParameters.add(ipAddressNameValuePair);
				UrlEncodedFormEntity urlEncodedFormEntity = new UrlEncodedFormEntity(
						postParameters, "EUC-KR");

				httpPost.setEntity(urlEncodedFormEntity);
				System.out.println("send http post");
				///응답받는 부분.
				HttpResponse response;
				XmlPullParserFactory factory = XmlPullParserFactory
						.newInstance();
				factory.setNamespaceAware(true);

				response =  httpClient.execute(httpPost);
//				InputStream inputStream = response.getEntity().getContent();
//
//				itemXmlParser parser = new itemXmlParser(inputStream);
//
//				List<Map<String, String>> javamodelingMembers = parser.parse();
//
//				Message message = handler.obtainMessage(1);


		//		handler.sendMessage(message);
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (XmlPullParserException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else if(action.equals(MainActivity.REMOTE_ACCEPT) || action.equals(MainActivity.REMOTE_REJECT)){
			try {
				List<NameValuePair> postParameters = new ArrayList<NameValuePair>();

				NameValuePair actionValuePair = new BasicNameValuePair("action",
						action);
				postParameters.add(actionValuePair);
				NameValuePair nameValuePair = new BasicNameValuePair("name",
						username);
				postParameters.add(nameValuePair);
				UrlEncodedFormEntity urlEncodedFormEntity = new UrlEncodedFormEntity(
						postParameters, "EUC-KR");

				httpPost.setEntity(urlEncodedFormEntity);
				System.out.println("send remote accept or reject http post");
				///응답받는 부분.
				httpClient.execute(httpPost);
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else if(action.equals("join")){
			try {
				List<NameValuePair> postParameters = new ArrayList<NameValuePair>();
				
				NameValuePair actionNaValuePair = new BasicNameValuePair("action", "join");
				postParameters.add(actionNaValuePair);
				NameValuePair email_idNameValuePair = new BasicNameValuePair("email_id",
						email_id);
				postParameters.add(email_idNameValuePair);
				NameValuePair passwordNameValuePair = new BasicNameValuePair("password",
						password);
				postParameters.add(passwordNameValuePair);
				NameValuePair usernameNameValuePair = new BasicNameValuePair("username",
						username);
				postParameters.add(usernameNameValuePair);
				NameValuePair genderNameValuePair = new BasicNameValuePair("phonenumber",
						phonenumber);
				postParameters.add(genderNameValuePair);
				NameValuePair regIdNameValuePair = new BasicNameValuePair("reg_id", regID);
				postParameters.add(regIdNameValuePair);

				UrlEncodedFormEntity urlEncodedFormEntity = new UrlEncodedFormEntity(
						postParameters, "EUC-KR");

				httpPost.setEntity(urlEncodedFormEntity);

				/////////////////
				HttpResponse response;
				XmlPullParserFactory factory = XmlPullParserFactory
						.newInstance();
				factory.setNamespaceAware(true);

				response =  httpClient.execute(httpPost);
				InputStream inputStream = response.getEntity().getContent();

				itemXmlParser parser = new itemXmlParser(inputStream);

				List<Map<String, String>> javamodelingMembers = parser.parse();

				Message message = handler.obtainMessage(1);

				message.obj = javamodelingMembers;

				handler.sendMessage(message);
				/////////////////////
				
			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalStateException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}catch (XmlPullParserException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else if(action.equals("login")){
			//HttpConnectAdapter.executeJoin(url, email_id, password, username, phonenumber);
			try {
				List<NameValuePair> postParameters = new ArrayList<NameValuePair>();
				
				NameValuePair actionNaValuePair = new BasicNameValuePair("action", "login");
				postParameters.add(actionNaValuePair);
				NameValuePair email_idNameValuePair = new BasicNameValuePair("email_id",
						email_id);
				postParameters.add(email_idNameValuePair);
				NameValuePair passwordNameValuePair = new BasicNameValuePair("password",
						password);
				postParameters.add(passwordNameValuePair);

				UrlEncodedFormEntity urlEncodedFormEntity = new UrlEncodedFormEntity(
						postParameters, "EUC-KR");

				httpPost.setEntity(urlEncodedFormEntity);

				/////////////////
				HttpResponse response;
				XmlPullParserFactory factory = XmlPullParserFactory
						.newInstance();
				factory.setNamespaceAware(true);

				response =  httpClient.execute(httpPost);
				InputStream inputStream = response.getEntity().getContent();
				System.out.println(inputStream.toString());
				itemXmlParser parser = new itemXmlParser(inputStream);

				List<Map<String, String>> javamodelingMembers = parser.parse();

				Message message = handler.obtainMessage(1);

				message.obj = javamodelingMembers;

				handler.sendMessage(message);
				/////////////////////
				
			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalStateException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}catch (XmlPullParserException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}
}
