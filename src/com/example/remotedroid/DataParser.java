package com.example.remotedroid;

import java.io.FileNotFoundException;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import android.content.Context;
import android.util.Log;

public class DataParser extends BaseParser {

	//private Context mContext;
	private String fileVersion = "";

	public DataParser(Context context, String xmlFileName) {
		super(context, xmlFileName);
		// TODO Auto-generated constructor stub
		//this.mContext = context;
	}

	public String getVersion() {
		return fileVersion;
	}

	public ArrayList<FriendParseItem> parse() {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		itemList = new ArrayList<FriendParseItem>();

		try {
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document dom = builder.parse(this.getInputStream());
			Element root = dom.getDocumentElement();
			NodeList items = root.getElementsByTagName(ITEM);

			for (int i = 0; i < items.getLength(); i++) {
				FriendParseItem parseItem = new FriendParseItem();

				Node item = items.item(i);
				NodeList itemDatas = item.getChildNodes();
				
				String itemnum = ((Element) item).getAttribute("itemnum");
				Log.d(TAG, "groupId = " + itemnum);
				parseItem.setItemnum(itemnum);

				for (int j = 0; j < itemDatas.getLength(); j++) {
					Node data = itemDatas.item(j);
					String nodename = data.getNodeName();

					if (nodename.equalsIgnoreCase(NAME)) {
						String name = data.getFirstChild().getNodeValue();
						parseItem.setName(name);
					} else if (nodename.equalsIgnoreCase(phone)) {
						String phone = data.getFirstChild().getNodeValue();
						parseItem.setPhone(phone);
					} else if (nodename.equalsIgnoreCase(register)) {
						boolean register = false;
						if(data.getFirstChild().getNodeValue().equals("false")){
							
						}else{
							register = true;
						}
						parseItem.setRegister(register);
					}
				}// groupDatas Loop end
				itemList.add(parseItem);
			}
		} catch (NullPointerException npe) {
			npe.printStackTrace();
		} catch (FileNotFoundException fnfe) {
			fnfe.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return itemList;
	}
}
