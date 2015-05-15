package com.drem.dremboard.entity;

import java.util.ArrayList;


public class SingleViewMessageInfo {

	public int id;
	public String subject;
	public ArrayList<UserInfo> recipients;
	
	public ArrayList<MsgThread> thread;

	public SingleViewMessageInfo () {
		super();
		
		this.id = -1; // -1 : invalid user id
	}
	
	public class MsgThread {
		public UserInfo sender;
		public String time_since;
		public String content;
	}
	
	public class UserInfo {
		public int id;
		public String name;
		public String avatar;
	}
}
