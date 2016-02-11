package com.drem.dremboard.webservice;

public class Constants {

	/* Published server */
//	public static final String HTTP_HOME = "http://10.70.126.4:8080/";
	public static final String HTTP_HOME = "http://dremboard.com/";
	
	public static final String SERVER_REST_API_BASE = Constants.HTTP_HOME
			+ "api/rest/";
	
	public static final String ACT_LOG_IN 			= "user_login";
	public static final String ACT_GET_INIT_PARAMS 	= "get_init_params";
	public static final String ACT_GET_DREMS 		= "get_drems";
	public static final String ACT_SET_COMMENT 		= "set_activity_comment";
	public static final String ACT_EDIT_COMMENT 		= "edit_activity_comment";
	public static final String ACT_SET_FAVORITE 	= "set_favorite";
	public static final String ACT_SET_LIKE 		= "set_like"; 
	public static final String ACT_SHARE_DREM 		= "share_drem"; 
	public static final String ACT_USER_REGISTER	= "user_register";
	public static final String ACT_RETRIEVE_PASSWORD= "retrieve_password";
	public static final String ACT_FLAG_DREM		= "flag_drem";
	public static final String ACT_GET_ACTIVITIES	= "get_activities";
	public static final String ACT_GET_DREMERS		= "get_dremers";
	public static final String ACT_GET_SINGLE_DREMER		= "get_single_dremer";
	public static final String ACT_GET_DREMBOARDS	= "get_dremboards";
	public static final String ACT_GET_MEMORIES		= "get_memories";
	public static final String ACT_SET_FRIENDSHIP	= "change_dremer_friendship";
	public static final String ACT_SET_FAMILYSHIP	= "change_dremer_familyship";
	public static final String ACT_SET_FOLLOWING	= "change_dremer_following";
	public static final String ACT_SET_BLOCKING		= "change_dremer_blocking";
	public static final String ACT_GET_NT			= "get_notifications";
	public static final String ACT_SET_NT			= "set_notification_action";
	public static final String ACT_GET_NT_CNT			= "get_notification_count";
	public static final String ACT_GET_MESSAGES		= "get_messages";
	public static final String ACT_GET_SINGLE_MESSAGE		= "get_message_single_view";
	public static final String ACT_SEND_MESSAGE		= "message_compose";
	public static final String ACT_REPLY_MESSAGE	= "message_reply";
	public static final String ACT_SET_SINGLE_DREMER_IMAGE		= "set_single_dremer_image";
	public static final String ACT_SET_GENERAL		= "set_single_dremer_general";
	public static final String ACT_GET_MAIL_NOTE	= "get_single_dremer_email_note";
	public static final String ACT_SET_MAIL_NOTE	= "set_single_dremer_email_note";
	public static final String ACT_GET_DEF_PRIVACY	= "get_single_dremer_default_privacy";
	public static final String ACT_SET_DEF_PRIVACY	= "set_single_dremer_default_privacy";
	public static final String ACT_ADD_DREM_TO_DREMBOARD = "add_drem_to_dremboard";
	public static final String CREATE_DREMBOARD = 	"create_dremboard";
	public static final String DELETE_DREMBOARD = 	"delete_dremboard";
	public static final String EDIT_DREMBOARD = 	"edit_dremboard";
	public static final String MERGE_DREMBOARD = 	"merge_dremboard";
	public static final String REMOVE_DREMS_FROM_DREMBOARD = 	"remove_drems_from_dremboard";
	public static final String MOVE_DREMS_TO_DREMBOARD = 	"move_drems_to_dremboard";
	public static final String SET_ACTIVITY = 		"set_activity";
	public static final String DELETE_ACTIVITY = 	"delete_activity";
	public static final String GET_ACTIVITY = 	"get_activity";

	public static final String PARAM_USERNAME 		= "username";
	public static final String PARAM_PASSWORD 		= "password";
	public static final String PARAM_USER_LOGIN 	= "user_login";
	public static final String PARAM_USER_ID 		= "user_id";
	public static final String PARAM_DISP_USER_ID 	= "disp_user_id";
	public static final String PARAM_DREMER_ID 		= "dremer_id";
	public static final String PARAM_ALBUM_ID 		= "album_id";
	public static final String PARAM_AUTHOR_ID 		= "author_id";
	public static final String PARAM_CATEGORY 		= "category"; 
	public static final String PARAM_COMMENT 		= "comment";
	public static final String PARAM_SEARCH_STR 	= "search_str"; 
	public static final String PARAM_LAST_MEDIA_ID 	= "last_media_id"; 
	public static final String PARAM_LAST_DREMER_ID	= "last_dremer_id";
	public static final String PARAM_PAGE 			= "page";
	public static final String PARAM_PER_PAGE 		= "per_page";
	public static final String PARAM_ACTIVITY_ID 	= "activity_id"; 
	public static final String PARAM_RTMEDIA_ID 	= "rtmedia_id";
	public static final String PARAM_FAVORITE_STR 	= "favorite_str";
	public static final String PARAM_LIKE_STR 		= "like_str"; 
	public static final String PARAM_DESCRIPTION 	= "description"; 
	public static final String PARAM_SHARE_USER 	= "share_user"; 
	public static final String PARAM_SHARE_MODE 	= "share_mode"; 
	public static final String PARAM_FLAG_SLUG		= "flag_slug";
	public static final String PARAM_ACTIVITY_SCOPE	= "activity_scope";
	public static final String PARAM_LAST_ACTIVITY_ID = "last_activity_id";
	public static final String PARAM_PHOTO			= "file";
	public static final String PARAM_ACTION			= "action";
	public static final String PARAM_BLOCK_TYPE		= "block_type";
	public static final String PARAM_TYPE			= "type";
	public static final String PARAM_NOTIFICATION_ID	= "notification_id";
	public static final String PARAM_SUBJECT		= "subject";
	public static final String PARAM_CONTENT		= "content";
	public static final String PARAM_RECIPIENTS		= "recipients";
	public static final String PARAM_IS_NOTICE		= "is_notice";
	public static final String PARAM_MESSAGE_ID		= "message_id";
	public static final String PARAM_EMAIL			= "email";
	public static final String PARAM_PRIVACY	 	= "privacy";
	public static final String PARAM_NOTE_JSON	 	= "notifications_json";
	public static final String PARAM_DREM_ID	 	= "drem_id";
	public static final String PARAM_DREM_IDS	 	= "drem_ids";
	public static final String PARAM_DREMBOARD_ID	= "dremboard_id";
	public static final String PARAM_TITLE	 		= "title";
	public static final String PARAM_CATEGORY_ID 	= "category_id";
	public static final String PARAM_SOURCE_ID	 	= "source_id";
	public static final String PARAM_TARGET_ID	 	= "target_id";
	public static final String PARAM_FILE		 	= "file";
	public static final String PARAM_CROP_X			= "crop_x";
	public static final String PARAM_CROP_Y			= "crop_y";
	public static final String PARAM_CROP_WIDTH		= "crop_w";
	public static final String PARAM_CROP_HEIGHT	= "crop_h";




	public static final String NETWORK_ERR = "Couldn't connect to Dremboard.com.";
	
}
// admin/buildabettersite123
