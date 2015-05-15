package com.drem.dremboard.entity;


public class VideoInfo {

	public String mVideoUrl, mVideoName, mUserName;
	
	public int id;
	public String media_title;
	public int media_author_id;
	public String media_author_avatar;
	public String media_author_name;
	public String media_type;
	public String guid;
	public int view_count;	

	public VideoInfo(String url, String videoname, String username) {
		super();
		
		this.mVideoName = videoname;
		this.mVideoUrl = url;
		this.mUserName = username;
	}

}
