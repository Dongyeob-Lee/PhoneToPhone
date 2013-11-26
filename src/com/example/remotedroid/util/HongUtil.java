 
/*
 * Remoteroid - A remote control solution for Android platform, including handy file transfer and notify-to-PC.
 * Copyright (C) 2012 Taeho Kim(jyte82@gmail.com), Hyomin Oh(ohmnia1112@gmail.com), Hongkyun Kim(godgjdgjd@nate.com), Yongwan Hwang(singerhwang@gmail.com)
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
*/

package com.example.remotedroid.util;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.regex.Pattern;


import com.example.remotedroid.data.ExplorerType;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.PendingIntent.CanceledException;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Handler;
import android.provider.MediaStore;
import android.telephony.TelephonyManager;
import android.text.InputFilter;
import android.text.Spanned;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

public class HongUtil {
	
	public static String TYPE_PICTURE = "image";
	public static String TYPE_VIDEO = "video";
	public static String TYPE_MUSIC = "audio";
	public static String TYPE_APK = "application";
	
	
	public static NormalComparater com = new NormalComparater();
	
	public static void makeToast(Context c, String str){
		Toast.makeText(c, str, Toast.LENGTH_SHORT).show();
	}
	
	public static Comparator<ExplorerType> nameComparator = new Comparator<ExplorerType>() {

		public int compare(ExplorerType lhs, ExplorerType rhs) {
			// TODO Auto-generated method stub
			return com.compare(lhs.getName(), rhs.getName());
		}
	};
	
	public static String getMimeType(File file){				// �뚯씪�� ���(鍮꾨뵒��,�ㅻ뵒��,�ъ쭊 �깅벑)�� 泥댄겕
		String result="";
		if(file.exists()){
			MimeTypeMap mtm = MimeTypeMap.getSingleton();
			String fileExtension = file.getName().substring(file.getName().lastIndexOf(".") + 1 , file.getName().length()).toLowerCase();
			String mimeType = mtm.getMimeTypeFromExtension(fileExtension);
			if(mimeType!=null){
				result = (mimeType.split("/", 0))[0];
			}
//			result = mimeType;
		}
		
		
		return result;
	}
	
	public static int getFileIcon(String path, String fileName){				// �뚯씪�� ��낆뿉 ��� �꾩씠肄� 異붿텧
		int result=0;
		String type = getMimeType(new File(path+fileName));
		
		if(type.equals(TYPE_PICTURE)){
			
		}
		else if(type.equals(TYPE_VIDEO)){
			
		}
		else if(type.equals(TYPE_MUSIC)){
			
		}
		else{
			
		}
		return result;
	}
	
	public static Bitmap getApkBitmap(File f, Context c){				//  apk �꾩씠肄� 鍮꾪듃留� 異붿텧
		Bitmap result = null;
		String filePath = f.getPath();
		PackageInfo packageInfo = c.getPackageManager().getPackageArchiveInfo(filePath, PackageManager.GET_ACTIVITIES);
		if(packageInfo !=null){
			
			ApplicationInfo appInfo = packageInfo.applicationInfo;
			if(Build.VERSION.SDK_INT >= 8){
				appInfo.sourceDir = filePath;
				appInfo.publicSourceDir = filePath;
			}
			Drawable icon = appInfo.loadIcon(c.getPackageManager());
//			result = (((BitmapDrawable)icon).getBitmap()).createScaledBitmap(result, 72, 72, true);
			result = result.createScaledBitmap(((BitmapDrawable)icon).getBitmap(),72,72,true);
						
			
		}
		
		return result;
	}
	
	public static InputFilter filterAlpha = new InputFilter() {						// �곸뼱留� �낅젰 媛�ν븯寃� �꾪꽣留�
		
		@Override
		public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
			
			Pattern ps = Pattern.compile("^[a-zA-Z]+$");
			if(!ps.matcher(source).matches()){
				return "";
			}
			return null;
		}
	};
	
	public static File getRootPath(){													// �붾컮�댁뒪 �덈�寃쎈줈 由ы꽩
		String sdcard = android.os.Environment.getExternalStorageDirectory().getAbsolutePath();
		File root = new File(sdcard);
		return root;
	}
	
	//searchIndex 삭제
	//addFile 삭제 
	
	
	
	//getPhoto 삭
	//getVideo 삭제.
	
	//getMusic 삭제.
	
	public static void showExitDialog(Context context){
		final Context c = context;
		
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle("醫낅즺�섏떆寃좎뒿�덇퉴?");
		builder.setPositiveButton("�뺤씤", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				((Activity)c).finish();
			}
		});
		builder.setNegativeButton("痍⑥냼", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
			}
		}).show();
	}
	
	
	
	
	
	
	
	
	//�명똿�쒗궗 Calendar 留뚮뱾�댁쨲
		public static Calendar setCal(long date){
			Calendar result=new GregorianCalendar();
			result.setTimeInMillis(date);
			
		    Date d = result.getTime();
		    String day = (d.getYear()+1900)+"��    "+(d.getMonth()+1)+"��"+d.getDate()+"��  "+d.getHours() +"��"+d.getMinutes()+" 遺�"+d.getSeconds()+"珥�";
		    
		    return result;
		}
		
		public static String getTime(Calendar day) {
			String result="";
			
			Date d = day.getTime();
//			result = (d.getYear()+1900)+"�� "+(d.getMonth()+1)+"�� "+d.getDate()+"�� "+d.getHours() +":"+d.getMinutes()+":"+d.getSeconds();
			result = (d.getYear()+1900)+"-"+(d.getMonth()+1)+"-"+d.getDate();
			
			
			return result;
		}
		
		public static String setHashValue(String path){
			File f = new File(path);
			String result=String.valueOf(f.hashCode());
			
			return result;
		}
		
		public static long setFileSize(String path){
			File f = new File(path);
			return f.length();
		}
	
	
	
	
	
	public static int setMusicType(String msg){
		int result=0;
		if(msg.indexOf(".mp3")!=-1){
			
		}
		
		else if(msg.indexOf(".mid")!=-1){
			
		}
		
		else if(msg.indexOf(".mid")!=-1){
			
		}
		
		else if(msg.indexOf(".mid")!=-1){
	
		}
		
		return result;
	}
	
	public static String setPhoneNumber(String num){
		return num.replace("+82", "0");
	}
	
	
	public static String getDuration(int time){
		String result = "";
		
		int hour = 0;
		int min = time/60000;
		
		if(min>=60){
			hour = min/60;
			min = min%60;
		}
		
		String sec = String.valueOf((time%60000));
		if(sec.length()==0){
			sec = "00";
		}
		else if(sec.length()<2)
			sec = "0"+sec.substring(0,1);
		else{
			sec = sec.substring(0, 2);
		}
		
		if(hour!=0){
			result = String.valueOf(hour) + String.valueOf(min)+":"+sec;
		}
		else{
			result = String.valueOf(min)+":"+sec;
		}
		return result;
	}
	
	public static String getMegabyte(long size){
		String result ="";
		double re = (double)(((double)size/(double)1024)/(double)1024);
		
		result = String.format("%.1f", re);
		
		return result;
	}
	
	public static String getMegabyte(double size){
		String result ="";
		double re = size/1024/1024;
		
		result = String.format("%.1f", re);
		
		return result;
	}
	
	public static String getMegaSpeed(long size){
		String result ="";
		double re = (double)size/(double)1000;
		
		result = String.format("%.1f", re);
		
		
		return result;
	}
	
	public static String getDeviceId(Context context){
		TelephonyManager tm = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
		String uuid = tm.getDeviceId();
		if(uuid==null){
			WifiManager wifiManager = (WifiManager)context.getSystemService(Context.WIFI_SERVICE);
			String mac = wifiManager.getConnectionInfo().getMacAddress();
			if(mac==null)
				throw new IllegalStateException();
			return mac;
		}
		return uuid;
	}
	
	//exec 머시기 삭제..
	
	//카카오 ㅅ??
	
	
	public static int getPositionalNumber(int iSrc){
		int positionnalNumber=1;
		while(true){
			if(iSrc/(int)Math.pow(10, positionnalNumber) == 0)
				break;
			positionnalNumber++;
		}
		return positionnalNumber;
	}
	
		
	public static int itoa(int iSrc, byte[] buffer){
		int positionalNumber = getPositionalNumber(iSrc);
		int length = positionalNumber;
				
		int i=0;
		while(positionalNumber > 0){
			int jesu = (int)Math.pow(10, positionalNumber-1);
			int quotiont = iSrc / jesu;
			
			buffer[i] = (byte) (quotiont+'0');
			
			int remainder = iSrc % jesu;		
			
			positionalNumber--;
			i++;
			iSrc = remainder;
		}
		return length;
	}

	public static int itoa(int iSrc, byte[] buffer, int offset){
		int positionalNumber = getPositionalNumber(iSrc);
		int length = positionalNumber;
				
		int i=offset;
		while(positionalNumber > 0){
			int jesu = (int)Math.pow(10, positionalNumber-1);
			int quotiont = iSrc / jesu;
			
			buffer[i] = (byte) (quotiont+'0');
			
			int remainder = iSrc % jesu;		
			
			positionalNumber--;
			i++;
			iSrc = remainder;
		}
		return length;
	}
	public static int atoi(byte[] buffer, int length){
		int size=0;
		for(int i=length; i>0; i--){
			int jesu = (int)Math.pow(10, i-1);
			size+=((int)(buffer[length-i+1]-'0'))*jesu;
		}
		return size;
	}
	
	public static int itoa(int iSrc, byte[] buffer, int positionalNumber, int offset){
						
		int i=offset;
		while(positionalNumber > 0){
			int jesu = (int)Math.pow(10, positionalNumber-1);
			int quotiont = iSrc / jesu;
			
			buffer[i] = (byte) (quotiont+'0');
			
			int remainder = iSrc % jesu;		
			
			positionalNumber--;
			i++;
			iSrc = remainder;
		}
		return positionalNumber;
	}
	

	
	public static int getOrientation(Context context){		
		return context.getResources().getConfiguration().orientation-1;
	}
	
	public static int getRotation(Context context){
		WindowManager wm = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
		Display display = wm.getDefaultDisplay();
		
		
		return  display.getRotation();
	}
	

	public static void setClipBoard(final Context context, final String msg, Handler handler){
		
		
		Runnable updater = new Runnable() {
	         public void run() {
	        	
	        	ClipboardManager clip = (ClipboardManager)context.getSystemService(Context.CLIPBOARD_SERVICE);
		     	ClipData data = ClipData.newPlainText("Remoteroid_Clip", msg);
		     	clip.setPrimaryClip(data);					
				 		     		
	         }             
	     };
	     handler.post(updater);
		
	}

	public static String getClipData(Context context){
		ClipboardManager clip = (ClipboardManager)context.getSystemService(Context.CLIPBOARD_SERVICE);
		return clip.getPrimaryClip().getItemAt(0).getText().toString();
	}
	

	
}
