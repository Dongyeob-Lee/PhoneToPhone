package com.example.remotedroid;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

public class PTPDialog {
	
	public static final int YES_BUTTON = 1;
	public static final int NO_BUTTON = 2;
	
	private static int RESULT = -1;
	
	//경고창을 보여주는 dialog
	public static int showAlertDialog(String title, String message, Context context){
		
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		
		builder.setMessage(message).setTitle(title).setCancelable(false).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int id) {
				dialog.cancel();
				RESULT = YES_BUTTON;
			}
		});
		
		AlertDialog alert = builder.create();
		alert.show();
		
		return RESULT;
	}
	
	//Yes NO 선택하는 dialog
	public static int showYesNoDialog(String title, String message, Context context){
		
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		AlertDialog alert = null;
		
		builder.setMessage(message).setTitle(title).setCancelable(false).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
				RESULT = YES_BUTTON;
			}
		}).setNegativeButton("No", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
				RESULT = NO_BUTTON;
			}
		});
		
		alert = builder.create();
		alert.show();
		
		return RESULT;
	}
}
