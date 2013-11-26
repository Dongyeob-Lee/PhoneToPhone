package com.example.remotedroid;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;

import android.content.Context;
import android.provider.OpenableColumns;

public abstract class BaseParser {
	public final String TAG = "Parser";
	public static final String ITEMS = "items";
	public static final String ITEM = "item";
	public static final String ITEMNUM = "itemnum";
	public static final String NAME = "name";
	public static final String phone = "phone";
	public static final String register = "register";

	private String xmlFileName;
	private Context mContext;

	protected ArrayList<FriendParseItem> itemList;

	protected BaseParser(Context context, String xmlFileName) {
		try {
			this.mContext = context;
			this.xmlFileName = xmlFileName;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	protected FileInputStream getInputStream() {
		try {
			//return new FileInputStream(xmlFile);
			return mContext.openFileInput(xmlFileName);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	public ArrayList<FriendParseItem> parse() {
		return itemList;
	}
}
