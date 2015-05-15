package com.drem.dremboard.entity;


public class DremboardInfo {

	public int id;
	public String media_type;
	public String media_title;
	public int media_author_id;
	public String media_author_avatar;
	public String media_author_name;
	public String guid;
	public String album_count;

	public DremboardInfo() {
		super();
		
		this.id = -1; // invalid id
	}

}
