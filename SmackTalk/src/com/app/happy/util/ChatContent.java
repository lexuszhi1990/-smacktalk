package com.app.happy.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import android.util.Log;

public class ChatContent {
	
	static private HashMap<String, ArrayList<ChatMsg>> chatItem = new HashMap<String, ArrayList<ChatMsg>>();
	private HashMap<String, ArrayList<ChatMsg>> map;
	private ArrayList<ChatMsg> aList;
	private ChatMsg cMsg;
		
	
	public void creatChatLog (String str) {	
		ArrayList<ChatMsg> list = new ArrayList<ChatMsg>();
		chatItem.put(str, list);
		
	}

	
	public void addChat (String key, ChatMsg msg) {
		aList = chatItem.get(key);
		if(aList == null){
			creatChatLog(key);
		}
		chatItem.get(key).add(msg);
		
	}
	
	public ArrayList<ChatMsg> getList(String key){
		return chatItem.get(key);
	}
	
	public void putChat(String key){
		aList = chatItem.get(key);
		if(aList == null){
			System.out.println("alist is null");
			return;
		}
		
		int i = aList.size();
			System.out.println("the size is "  + aList.size() );
		
	}
	
	public void putMap(){
		System.out.println("map" + chatItem.toString());
	}

}
