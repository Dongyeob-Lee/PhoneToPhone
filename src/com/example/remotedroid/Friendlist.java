package com.example.remotedroid;

import java.util.ArrayList;

import com.example.remotedroid.R;

import android.content.Context;
import android.widget.ListView;

public class Friendlist {
	ListView friendList;
	ArrayList<String> list;
	private static final String xmlFileName = "contacts.xml";
	private Context mContext;
	private ArrayList<FriendParseItem> dataItems;
	private FriendItemsAdapter friendItemsAdapter;
	int type=0;
    public FriendItemsAdapter getFriendItemsAdapter() {
		return friendItemsAdapter;
	}


	public void setFriendItemsAdapter(FriendItemsAdapter friendItemsAdapter) {
		this.friendItemsAdapter = friendItemsAdapter;
	}

	public Friendlist(Context mContext) {
		super();
		this.mContext = mContext;
		initParseData();
		makeFriendList();
		
	}
	public Friendlist(Context mContext, int type){
		super();
		this.mContext = mContext;
		this.type = type;
		initParseData();
		makeFriendList();
	}
  
    private void initParseData() {
    	//	File xmlFile = new File(xmlFilePath, xmlFileName);
    	//	if (xmlFile.exists()) {
    			dataItems = parseXmlFile();
    	//	}
    	}
    	private void makeFriendList() {
//    		list = new ArrayList<String>();
//    		for (int i = 0; i < dataItems.size(); i++) {
//    			list.add(dataItems.get(i).getName());
//    		}
    		Friend[] friends=null;
    		friends = new Friend[dataItems.size()];
    			for( int i=0; i<dataItems.size(); i++){
    				friends[i] = new Friend();
    				friends[i].setName(dataItems.get(i).getName());
    				friends[i].setPhonenumber(dataItems.get(i).getPhone());
    				friends[i].setRegistered(dataItems.get(i).isRegister());
    			}
    		
    	
    	friendItemsAdapter = new FriendItemsAdapter(mContext, R.layout.layout_friend_item, R.id.friendName, friends);
    	
    	
    
    	
    	//	friendList.setAdapter(friendItemsAdapter);
    	}
    	private ArrayList<FriendParseItem> parseXmlFile() {
    		BaseParser parser = new DataParser(mContext, xmlFileName);

    		ArrayList<FriendParseItem> itemsList = parser.parse();
    		//fileVersion = parser.getVersion();
    		return itemsList;
    	}
    

}
