package com.drem.dremboard.entity;

import java.util.ArrayList;

public class Beans {
	
	public static class LoginData {
		public String ID;
		public String user_login;
		public String avatar;
	}
	
	public static class SignInResult {
		public String status;
		public String msg;
		public LoginData data;
	}
	
	public static class RetrievePasswordResult {
		public String status;
		public String msg;
	}
	
	public static class RegisterResult {
		public String status;
		public String msg;
		public RegErrData data;
	}
	
	public static class RegErrData {
		public String signup_username;
		public String signup_email;
		public String signup_password;
		public String field_1;
	}
	
	public static class RestApiResult {
		public String status;
		public String msg;
		public String data;
	}
	
	/* Get Drem Activities */
	public static class GetActivitiesParam {
		public String user_id;
		public int disp_user_id;
		public String type;
		public String activity_scope;
		public int last_activity_id;
		public int per_page;
	}

	public static class GetActivitiesData {
		public int count;
		public ArrayList<DremActivityInfo> activity;
	}
	
	public static class GetActivitiesResult {
		public String status;
		public String msg;
		public GetActivitiesData data;
	}
	
	/* Get Drems */
	public static class GetDremsParam {
		public String user_id;
		public int album_id;
		public int author_id;
		public String search_str;		
		public int category;		
		public int last_media_id;
		public int per_page;
	}

	public static class GetDremsData {
		public int count;
		public ArrayList<DremInfo> media;
	}
	
	public static class GetDremsResult {
		public String status;
		public String msg;
		public GetDremsData data;
	}
	
	/* Get Dremers */
	public static class GetDremersParam {
		public String user_id;
		public int disp_user_id;
		public String search_str;
		public String type;
		public int page;
		public int per_page;
	}

	public static class GetDremersData {
		public int count;
		public ArrayList<DremerInfo> member;
	}
	
	public static class GetDremersResult {
		public String status;
		public String msg;
		public GetDremersData data;
	}
	
	/* Get Single Dremer */
	public static class GetSingleDremerParam {
		public String user_id;
		public int disp_user_id;
	}

	public static class GetSingleDremerData {
		public DremerInfo member;
		public ArrayList<ProfileItem> profiles;
	}
	
	public static class GetSingleDremerResult {
		public String status;
		public String msg;
		public GetSingleDremerData data;
	}

	/* Get Dremboards */
	public static class GetDremboardsParam {
		public String user_id;
		public int category;
		public String search_str;
		public int last_media_id;
		public int per_page;
	}

	public static class GetDremboardsData {
		public int count;
		public ArrayList<DremboardInfo> media;
	}
	
	public static class GetDremboardsResult {
		public String status;
		public String msg;
		public GetDremboardsData data;
	}
	
	/* Get Videos */
	public static class GetVideosParam {
		public String user_id;
		public int author_id;
		public String search_str;
		public int last_media_id;
		public int per_page;
	}

	public static class GetVideosData {
		public int count;
		public ArrayList<VideoInfo> media;
	}
	
	public static class GetVideosResult {
		public String status;
		public String msg;
		public GetVideosData data;
	}
	
	/* Set Favorite */
	public static class SetFavoriteParam {
		public String user_id;		
		public int activity_id;
		public String favorite_str;
		
		public Object exParam;
		public int index;
	}

	public static class SetFavoriteData {
		public String favorite_str;
	}
	
	public static class SetFavoriteResult {
		public String status;
		public String msg;
		public SetFavoriteData data;
	}
	
	/* Set Like */
	public static class SetLikeParam {
		public String user_id;		
		public int activity_id;
		public String like_str;
		
		public Object exParam;
		public int index;
	}

	public static class SetLikeData {
		public String like_str;
	}
	
	public static class SetLikeResult {
		public String status;
		public String msg;
		public SetLikeData data;
	}
	
	/* Set Flag */
	public static class SetFlagParam {
		public String user_id;		
		public int activity_id;
		public String flag_slug;
		
		public Object exParam;
		public int index;
	}

	public static class SetFlagData {
		public String flag_slug;
	}
	
	public static class SetFlagResult {
		public String status;
		public String msg;
		public SetFlagData data;
	}
	
	/* Set Comment */
	public static class SetCommentParam {
		public String user_id;		
		public int activity_id;
		public String comment;
		public String photo;		
	}

	public static class SetCommentData {
		public CommentInfo comment;
	}
	
	public static class SetCommentResult {
		public String status;
		public String msg;
		public SetCommentData data;
	}
	
	/* Set Dremer Friendship */
	public static class SetFriendshipParam {
		public String user_id;		
		public int dremer_id;
		public String action;
		
		public Object exParam;
		public int index;
	}

	public static class SetFriendshipData {
		public String friendship_status;
	}
	
	public static class SetFriendshipResult {
		public String status;
		public String msg;
		public SetFriendshipData data;
	}
	
	/* Set Dremer Familyship */
	public static class SetFamilyshipParam {
		public String user_id;		
		public int dremer_id;
		public String action;
		
		public Object exParam;
		public int index;
	}

	public static class SetFamilyshipData {
		public String familyship_status;
	}
	
	public static class SetFamilyshipResult {
		public String status;
		public String msg;
		public SetFamilyshipData data;
	}
	
	/* Set Dremer Following */
	public static class SetFollowingParam {
		public String user_id;		
		public int dremer_id;
		public String action;
		
		public Object exParam;
		public int index;
	}

	public static class SetFollowingData {
		public int is_follow;
	}
	
	public static class SetFollowingResult {
		public String status;
		public String msg;
		public SetFollowingData data;
	}
	
	/* Set Dremer Blocking */
	public static class SetBlockingParam {
		public String user_id;		
		public int dremer_id;
		public String action;
		public int block_type;
		
		public Object exParam;
		public int index;
	}

	public static class SetBlockingData {
		public int block_type;
	}
	
	public static class SetBlockingResult {
		public String status;
		public String msg;
		public SetBlockingData data;
	}
	
	/* Get Notifications */
	public static class GetNTsParam {
		public String user_id;
		public String type;
		public int page;
		public int per_page;
	}

	public static class GetNTsData {
		public ArrayList<NotificationInfo> notification;
	}
	
	public static class GetNTsResult {
		public String status;
		public String msg;
		public GetNTsData data;
	}
	
	/* Set Notification */
	public static class SetNTParam {
		public String user_id;		
		public int notification_id;
		public String action;
		
		public Object exParam;
		public int index;
	}
	
	public static class SetNTResult {
		public String status;
		public String msg;
	}
	
	/* Get Messages */
	public static class GetMessagesParam {
		public String user_id;
		public String type;
		public int page;
		public int per_page;
	}

	public static class GetMessagesData {
		public ArrayList<MessageInfo> messages;
	}
	
	public static class GetMessagesResult {
		public String status;
		public String msg;
		public GetMessagesData data;
	}
	
	/* Send Messages */
	public static class SendMessageParam {
		public String user_id;
		public String recipients;
		public String subject;
		public String content;
		public boolean is_notice;
	}
	
	public static class SendMessageResult {
		public String status;
		public String msg;
	}
	
	/* Reply Messages */
	public static class ReplyMessageParam {
		public String user_id;
		public int message_id;
		public String subject;
		public String content;
	}
	
	public static class ReplyMessageResult {
		public String status;
		public String msg;
	}
	
	/* Get Message Single View */
	public static class GetSingleMessageParam {
		public String user_id;
		public int message_id;
	}	
	
	public static class GetSingleMessageData {
		public SingleViewMessageInfo messages;
	}
	
	public static class GetSingleMessageResult {
		public String status;
		public String msg;
		public GetSingleMessageData data;
	}
	
	/* Set Settings General */
	public static class SetSettingGeneralParam {
		public String user_id;
		public String disp_user_id;
		public String email;
		public String password;		
	}	
	
	public static class SetSettingGeneralResult {
		public String status;
		public String msg;
	}
	
	/* Get Settings Email Note */
	public static class GetSettingNoteParam {
		public String user_id;
		public String disp_user_id;
	}	
	
	public static class GetSettingNoteData {
		public ArrayList<EmailNoteConf> notifications;
	}
	
	public static class GetSettingNoteResult {
		public String status;
		public String msg;
		public GetSettingNoteData data;
	}
	
	/* Get Settings Email Note */
	public static class SetSettingNoteParam {
		public String user_id;
		public String disp_user_id;
		public String notifications;
	}	
	
	public static class SetSettingNoteResult {
		public String status;
		public String msg;
	}
	
	/* Get Settings Default Privacy */
	public static class GetSettingPrivacyParam {
		public String user_id;
		public String disp_user_id;
	}	
	
	public static class GetSettingPrivacyData {
		public String privacy;
	}
	
	public static class GetSettingPrivacyResult {
		public String status;
		public String msg;
		public GetSettingPrivacyData data;
	}
	
	/* Set Settings Default Privacy */
	public static class SetSettingPrivacyParam {
		public String user_id;
		public String disp_user_id;
		public String privacy;
	}	
	
	public static class SetSettingPrivacyResult {
		public String status;
		public String msg;
	}
}
