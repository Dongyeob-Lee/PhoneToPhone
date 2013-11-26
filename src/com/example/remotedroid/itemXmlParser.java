package com.example.remotedroid;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import android.util.Log;

public class itemXmlParser {
	public static final String item = "item";
	public final String TAG = "Parser";
	public static final String ITEMS = "items";
	public static final String ITEM = "item";
	public static final String ITEMNUM = "itemnum";
	public static final String NAME = "name";
	public static final String phone = "phone";
	public static final String register = "register";
	InputStream inputStream;

	public itemXmlParser(InputStream inputStream) {
		this.inputStream = inputStream;
	}

	public List<Map<String, String>> parse() {

		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

		List<Map<String, String>> items = new ArrayList<Map<String, String>>();

		try {

			DocumentBuilder builder = factory.newDocumentBuilder();

			Document document = builder.parse(inputStream);

			Element root = document.getDocumentElement();

			NodeList itemList = root.getElementsByTagName(item);

			Map<String, String> memberMap = null;

			for (int i = 0; i < itemList.getLength(); i++) {

				Member member=null;

				Node item = itemList.item(i);

				member = getMemberFromXml(item);

				memberMap = new HashMap<String, String>();
				memberMap.put("title", member.getTitle());
				memberMap.put("result", member.getResult());
				items.add(memberMap);
			}

		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return items;

	}

	private Member getMemberFromXml(Node node) {

		Member member = new Member();

		NodeList item = node.getChildNodes();

		for (int i = 0; i < item.getLength(); i++) {

			Node property = item.item(i);

			String name = property.getNodeName();

			if (name.equalsIgnoreCase("title")) {
				Node firstChild = property.getFirstChild();
				String title = null;
				if (firstChild != null) {
					title = firstChild.getNodeValue();
				}
				member.setTitle(title);
			} else if (name.equalsIgnoreCase("result")) {
				Node firstChild = property.getFirstChild();
				String result = null;
				if (firstChild != null) {
					result = firstChild.getNodeValue();
				}
				member.setResult(result);
			}

		}

		return member;
	}
	public ArrayList<FriendParseItem> parseContact() {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		ArrayList<FriendParseItem> itemList = new ArrayList<FriendParseItem>();

		try {
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document dom = builder.parse(this.inputStream);
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