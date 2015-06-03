package com.drem.dremboard.entity;

import java.util.ArrayList;

public class DremActivityInfo {

	public int activity_id;
	public int author_id;
	public String author_avatar;
	public String action;
	public String last_modified;
	public String description;
	public String like;
	public String favorite;

	public ArrayList<MediaInfo> media_list;
	public ArrayList<CommentInfo> comment_list;

	public DremActivityInfo() {
		super();
	}

	public class MediaInfo {
		public String media_type;
		public String media_guid;
	}

}
