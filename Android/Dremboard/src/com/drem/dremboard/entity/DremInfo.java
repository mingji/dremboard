package com.drem.dremboard.entity;

import java.util.ArrayList;

public class DremInfo {
	
	public int id;
	public int activity_id;
	public String media_title;
	public String category;
	public String media_type;
	public String guid;
	public String favorite;
	public String like;
	public String author_name;
	public String author_avatar;
	public String last_modified;
	public String description;
	public boolean isMore = true;
	public ArrayList<CommentInfo> comment_list;
	
	public DremInfo() {
		super();
	}

	
}
