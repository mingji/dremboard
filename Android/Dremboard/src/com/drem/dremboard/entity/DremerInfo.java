package com.drem.dremboard.entity;


public class DremerInfo {

	public int user_id;
	public String user_registered;
	public String user_login;
	public String user_niceman;
	public String display_name;
	public String fullname;
	public String user_email;
	public String friendship_status;
	public String familyship_status;
	public int is_following;
	public int block_type;
	public String user_avatar;
	public String last_activity;
	public ContentInfo latest_update;
	public int drem_count;

	public class ContentInfo {
		public int id;
		public String content;		
	}
	
	public DremerInfo () {
		super();
		
		this.user_id = -1; // -1 : invalid user id
	}
}
