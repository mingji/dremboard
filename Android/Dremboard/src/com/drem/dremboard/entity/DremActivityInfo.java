package com.drem.dremboard.entity;

import java.util.ArrayList;
import java.util.HashMap;

import com.drem.dremboard.entity.Beans.CommentData;
import com.drem.dremboard.entity.Beans.SetCommentData;

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

	public static class CommentInfo {

		public int activity_id;
		public int author_id;
		public String author_name;
		public String author_avatar;
		public String description;
		public String media_guid;
		public String last_modified;
		
		public CommentInfo(CommentData data) {
			super();
			
			this.activity_id = data.activity_id;
			this.author_id = data.author_id;
			this.author_name = data.author_name;
			this.author_avatar = data.author_avatar;
			this.description = data.description;
			this.media_guid = data.media_id;
			this.last_modified = data.last_modified;
		}
	}
}
