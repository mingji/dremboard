package com.drem.dremboard.entity;

import java.util.ArrayList;


public class MessageInfo {

	public int id;
	public String type;
	public FromToInfo from;
	public ArrayList<FromToInfo> to;
	public int unread_count;
	public String post_date;
	public String title;
	public String excerpt;

	public MessageInfo () {
		super();
		
		this.id = -1; // -1 : invalid user id
		this.to = new ArrayList<FromToInfo>();
	}
	
	public class FromToInfo {
		public int id;
		public String name;
		public String avatar;
	}
}
