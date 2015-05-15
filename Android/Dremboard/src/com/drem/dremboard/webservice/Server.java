package com.drem.dremboard.webservice;

import android.util.Log;

import com.drem.dremboard.entity.Beans.*;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;


public class Server {
	
	public static GetActivitiesResult GetDremActivities(GetActivitiesParam param)
	{
		HttpParams httpParam = new HttpParams();
		httpParam.addParam(Constants.PARAM_USER_ID, param.user_id);
		if (param.disp_user_id != -1)
			httpParam.addParam(Constants.PARAM_DISP_USER_ID, Integer.toString(param.disp_user_id));
		httpParam.addParam(Constants.PARAM_ACTIVITY_SCOPE, param.activity_scope);
		httpParam.addParam(Constants.PARAM_LAST_ACTIVITY_ID, Integer.toString(param.last_activity_id));
		httpParam.addParam(Constants.PARAM_PER_PAGE, Integer.toString(param.per_page));

		String response = HttpApi.sendRequest(Constants.SERVER_REST_API_BASE + Constants.ACT_GET_ACTIVITIES,
				httpParam, null);

		GetActivitiesResult result = null;
		if (response != null) {
			try {
				Gson gson = new Gson();
				result = gson.fromJson(response, GetActivitiesResult.class);
			} catch (JsonSyntaxException e) {
				Log.e("GetActivities", e.toString());
			}
			return result;
		} else
			return null;
	}
	
	public static GetDremsResult GetDrems(GetDremsParam param)
	{
		HttpParams httpParam = new HttpParams();
		httpParam.addParam(Constants.PARAM_USER_ID, param.user_id);
		httpParam.addParam(Constants.PARAM_SEARCH_STR, param.search_str);
		httpParam.addParam(Constants.PARAM_CATEGORY, Integer.toString(param.category));
		if (param.album_id != -1)
			httpParam.addParam(Constants.PARAM_ALBUM_ID, Integer.toString(param.album_id));
		if (param.author_id != -1)
			httpParam.addParam(Constants.PARAM_AUTHOR_ID, Integer.toString(param.author_id));
		httpParam.addParam(Constants.PARAM_LAST_MEDIA_ID, Integer.toString(param.last_media_id));
		httpParam.addParam(Constants.PARAM_PER_PAGE, Integer.toString(param.per_page));

		String response = HttpApi.sendRequest(Constants.SERVER_REST_API_BASE + Constants.ACT_GET_DREMS,
				httpParam, null);

		GetDremsResult result = null;
		if (response != null) {
			try {
				Gson gson = new Gson();
				result = gson.fromJson(response, GetDremsResult.class);
			} catch (JsonSyntaxException e) {
				Log.e("GetDrems", e.toString());
			}
			return result;
		} else
			return null;
	}
	
	public static GetDremersResult GetDremers(GetDremersParam param)
	{
		HttpParams httpParam = new HttpParams();
		httpParam.addParam(Constants.PARAM_USER_ID, param.user_id);
		httpParam.addParam(Constants.PARAM_SEARCH_STR, param.search_str);
		if (param.disp_user_id != -1)
			httpParam.addParam(Constants.PARAM_DISP_USER_ID, Integer.toString(param.disp_user_id));
		httpParam.addParam(Constants.PARAM_TYPE, param.type);
		httpParam.addParam(Constants.PARAM_PAGE, Integer.toString(param.page));
		httpParam.addParam(Constants.PARAM_PER_PAGE, Integer.toString(param.per_page));

		String response = HttpApi.sendRequest(Constants.SERVER_REST_API_BASE + Constants.ACT_GET_DREMERS,
				httpParam, null);

		GetDremersResult result = null;
		if (response != null) {
			try {
				Gson gson = new Gson();
				result = gson.fromJson(response, GetDremersResult.class);
			} catch (JsonSyntaxException e) {
				Log.e("GetDremers", e.toString());
			}
			return result;
		} else
			return null;
	}
	
	public static GetSingleDremerResult GetSingleDremer(GetSingleDremerParam param)
	{
		HttpParams httpParam = new HttpParams();
		httpParam.addParam(Constants.PARAM_USER_ID, param.user_id);
		if (param.disp_user_id != -1)
			httpParam.addParam(Constants.PARAM_DISP_USER_ID, Integer.toString(param.disp_user_id));

		String response = HttpApi.sendRequest(Constants.SERVER_REST_API_BASE + Constants.ACT_GET_SINGLE_DREMER,
				httpParam, null);

		GetSingleDremerResult result = null;
		if (response != null) {
			try {
				Gson gson = new Gson();
				result = gson.fromJson(response, GetSingleDremerResult.class);
			} catch (JsonSyntaxException e) {
				Log.e("GetSingleDremer", e.toString());
			}
			return result;
		} else
			return null;
	}

	public static GetDremboardsResult GetDremboards(GetDremboardsParam param)
	{

		HttpParams httpParam = new HttpParams();
		httpParam.addParam(Constants.PARAM_USER_ID, param.user_id);
		httpParam.addParam(Constants.PARAM_CATEGORY, Integer.toString(param.category));
		httpParam.addParam(Constants.PARAM_SEARCH_STR, param.search_str);
		httpParam.addParam(Constants.PARAM_LAST_MEDIA_ID, Integer.toString(param.last_media_id));
		httpParam.addParam(Constants.PARAM_PER_PAGE, Integer.toString(param.per_page));

		String response = HttpApi.sendRequest(Constants.SERVER_REST_API_BASE + Constants.ACT_GET_DREMBOARDS,
				httpParam, null);

		GetDremboardsResult result = null;
		if (response != null) {
			try {
				Gson gson = new Gson();
				result = gson.fromJson(response, GetDremboardsResult.class);
			} catch (JsonSyntaxException e) {
				Log.e("GetDremboard", e.toString());
			}
			return result;
		} else
			return null;
	}
	
	public static GetVideosResult GetVideos(GetVideosParam param)
	{

		HttpParams httpParam = new HttpParams();
		httpParam.addParam(Constants.PARAM_USER_ID, param.user_id);
		httpParam.addParam(Constants.PARAM_AUTHOR_ID, Integer.toString(param.author_id));
		httpParam.addParam(Constants.PARAM_SEARCH_STR, param.search_str);
		httpParam.addParam(Constants.PARAM_LAST_MEDIA_ID, Integer.toString(param.last_media_id));
		httpParam.addParam(Constants.PARAM_PER_PAGE, Integer.toString(param.per_page));

		String response = HttpApi.sendRequest(Constants.SERVER_REST_API_BASE + Constants.ACT_GET_MEMORIES,
				httpParam, null);

		GetVideosResult result = null;
		if (response != null) {
			try {
				Gson gson = new Gson();
				result = gson.fromJson(response, GetVideosResult.class);
			} catch (JsonSyntaxException e) {
				Log.e("GetVideos", e.toString());
			}
			return result;
		} else
			return null;
	}
	
	public static SetFavoriteResult SetFavorite(SetFavoriteParam param)
	{
		HttpParams httpParam = new HttpParams();
		httpParam.addParam(Constants.PARAM_USER_ID, param.user_id);
		httpParam.addParam(Constants.PARAM_ACTIVITY_ID, Integer.toString(param.activity_id));
		httpParam.addParam(Constants.PARAM_FAVORITE_STR, param.favorite_str);

		String response = HttpApi.sendRequest(Constants.SERVER_REST_API_BASE + Constants.ACT_SET_FAVORITE,
				httpParam, null);

		SetFavoriteResult result = null;
		if (response != null) {
			try {
				Gson gson = new Gson();
				result = gson.fromJson(response, SetFavoriteResult.class);
			} catch (JsonSyntaxException e) {
				Log.e("SetFavorite", e.toString());
			}
			return result;
		} else
			return null;
	}
	
	public static SetLikeResult SetLike(SetLikeParam param)
	{

		HttpParams httpParam = new HttpParams();
		httpParam.addParam(Constants.PARAM_USER_ID, param.user_id);
		httpParam.addParam(Constants.PARAM_ACTIVITY_ID, Integer.toString(param.activity_id));
		httpParam.addParam(Constants.PARAM_LIKE_STR, param.like_str);

		String response = HttpApi.sendRequest(Constants.SERVER_REST_API_BASE + Constants.ACT_SET_LIKE,
				httpParam, null);

		SetLikeResult result = null;
		if (response != null) {
			try {
				Gson gson = new Gson();
				result = gson.fromJson(response, SetLikeResult.class);
			} catch (JsonSyntaxException e) {
				Log.e("SetLike", e.toString());
			}
			return result;
		} else
			return null;
	}
	
	public static SetCommentResult SetActivityComment(SetCommentParam param)
	{

		HttpParams httpParam = new HttpParams();
		httpParam.addParam(Constants.PARAM_USER_ID, param.user_id);
		httpParam.addParam(Constants.PARAM_ACTIVITY_ID, Integer.toString(param.activity_id));
		httpParam.addParam(Constants.PARAM_COMMENT, param.comment);
		httpParam.addParam(Constants.PARAM_PHOTO, param.photo);

		String response = HttpApi.sendRequest(Constants.SERVER_REST_API_BASE + Constants.ACT_SET_COMMENT,
				httpParam, null);

		SetCommentResult result = null;
		if (response != null) {
			try {
				Gson gson = new Gson();
				result = gson.fromJson(response, SetCommentResult.class);
			} catch (JsonSyntaxException e) {
				Log.e("SetActivityComment", e.toString());
			}
			return result;
		} else
			return null;
	}
	
	public static SetFriendshipResult SetDremerFriendship(SetFriendshipParam param)
	{

		HttpParams httpParam = new HttpParams();
		httpParam.addParam(Constants.PARAM_USER_ID, param.user_id);
		httpParam.addParam(Constants.PARAM_DREMER_ID, Integer.toString(param.dremer_id));
		httpParam.addParam(Constants.PARAM_ACTION, param.action);

		String response = HttpApi.sendRequest(Constants.SERVER_REST_API_BASE + Constants.ACT_SET_FRIENDSHIP,
				httpParam, null);

		SetFriendshipResult result = null;
		if (response != null) {
			try {
				Gson gson = new Gson();
				result = gson.fromJson(response, SetFriendshipResult.class);
			} catch (JsonSyntaxException e) {
				Log.e("SetDremerFriendship", e.toString());
			}
			return result;
		} else
			return null;
	}
	
	public static SetFamilyshipResult SetDremerFamilyship(SetFamilyshipParam param)
	{

		HttpParams httpParam = new HttpParams();
		httpParam.addParam(Constants.PARAM_USER_ID, param.user_id);
		httpParam.addParam(Constants.PARAM_DREMER_ID, Integer.toString(param.dremer_id));
		httpParam.addParam(Constants.PARAM_ACTION, param.action);

		String response = HttpApi.sendRequest(Constants.SERVER_REST_API_BASE + Constants.ACT_SET_FAMILYSHIP,
				httpParam, null);

		SetFamilyshipResult result = null;
		if (response != null) {
			try {
				Gson gson = new Gson();
				result = gson.fromJson(response, SetFamilyshipResult.class);
			} catch (JsonSyntaxException e) {
				Log.e("SetDremerFamilyship", e.toString());
			}
			return result;
		} else
			return null;
	}
	
	public static SetFollowingResult SetDremerFollowing(SetFollowingParam param)
	{

		HttpParams httpParam = new HttpParams();
		httpParam.addParam(Constants.PARAM_USER_ID, param.user_id);
		httpParam.addParam(Constants.PARAM_DREMER_ID, Integer.toString(param.dremer_id));
		httpParam.addParam(Constants.PARAM_ACTION, param.action);

		String response = HttpApi.sendRequest(Constants.SERVER_REST_API_BASE + Constants.ACT_SET_FOLLOWING,
				httpParam, null);

		SetFollowingResult result = null;
		if (response != null) {
			try {
				Gson gson = new Gson();
				result = gson.fromJson(response, SetFollowingResult.class);
			} catch (JsonSyntaxException e) {
				Log.e("SetDremerFriendship", e.toString());
			}
			return result;
		} else
			return null;
	}
	
	public static SetBlockingResult SetDremerBlocking(SetBlockingParam param)
	{

		HttpParams httpParam = new HttpParams();
		httpParam.addParam(Constants.PARAM_USER_ID, param.user_id);
		httpParam.addParam(Constants.PARAM_DREMER_ID, Integer.toString(param.dremer_id));
		httpParam.addParam(Constants.PARAM_ACTION, param.action);
		httpParam.addParam(Constants.PARAM_BLOCK_TYPE, Integer.toString(param.block_type));

		String response = HttpApi.sendRequest(Constants.SERVER_REST_API_BASE + Constants.ACT_SET_BLOCKING,
				httpParam, null);

		SetBlockingResult result = null;
		if (response != null) {
			try {
				Gson gson = new Gson();
				result = gson.fromJson(response, SetBlockingResult.class);
			} catch (JsonSyntaxException e) {
				Log.e("SetDremerBlocking", e.toString());
			}
			return result;
		} else
			return null;
	}
	
	public static GetNTsResult GetNotifications(GetNTsParam param)
	{
		HttpParams httpParam = new HttpParams();
		httpParam.addParam(Constants.PARAM_USER_ID, param.user_id);
		httpParam.addParam(Constants.PARAM_TYPE, param.type);
		httpParam.addParam(Constants.PARAM_PAGE, Integer.toString(param.page));
		httpParam.addParam(Constants.PARAM_PER_PAGE, Integer.toString(param.per_page));

		String response = HttpApi.sendRequest(Constants.SERVER_REST_API_BASE + Constants.ACT_GET_NT,
				httpParam, null);

		GetNTsResult result = null;
		if (response != null) {
			try {
				Gson gson = new Gson();
				result = gson.fromJson(response, GetNTsResult.class);
			} catch (JsonSyntaxException e) {
				Log.e("GetNotifications", e.toString());
			}
			return result;
		} else
			return null;
	}
	
	public static SetNTResult SetNotification(SetNTParam param)
	{
		HttpParams httpParam = new HttpParams();
		httpParam.addParam(Constants.PARAM_USER_ID, param.user_id);
		httpParam.addParam(Constants.PARAM_ACTION, param.action);
		httpParam.addParam(Constants.PARAM_NOTIFICATION_ID, Integer.toString(param.notification_id));

		String response = HttpApi.sendRequest(Constants.SERVER_REST_API_BASE + Constants.ACT_SET_NT,
				httpParam, null);

		SetNTResult result = null;
		if (response != null) {
			try {
				Gson gson = new Gson();
				result = gson.fromJson(response, SetNTResult.class);
			} catch (JsonSyntaxException e) {
				Log.e("SetNotification", e.toString());
			}
			return result;
		} else
			return null;
	}
	
	public static GetMessagesResult GetMessages(GetMessagesParam param)
	{
		HttpParams httpParam = new HttpParams();
		httpParam.addParam(Constants.PARAM_USER_ID, param.user_id);
		httpParam.addParam(Constants.PARAM_TYPE, param.type);
		httpParam.addParam(Constants.PARAM_PAGE, Integer.toString(param.page));
		httpParam.addParam(Constants.PARAM_PER_PAGE, Integer.toString(param.per_page));

		String response = HttpApi.sendRequest(Constants.SERVER_REST_API_BASE + Constants.ACT_GET_MESSAGES,
				httpParam, null);

		GetMessagesResult result = null;
		if (response != null) {
			try {
				Gson gson = new Gson();
				result = gson.fromJson(response, GetMessagesResult.class);
			} catch (JsonSyntaxException e) {
				Log.e("GetMessages", e.toString());
			}
			return result;
		} else
			return null;
	}
	
	public static GetSingleMessageResult GetSingleMessage(GetSingleMessageParam param)
	{
		HttpParams httpParam = new HttpParams();
		httpParam.addParam(Constants.PARAM_USER_ID, param.user_id);
		httpParam.addParam(Constants.PARAM_MESSAGE_ID, Integer.toString(param.message_id));

		String response = HttpApi.sendRequest(Constants.SERVER_REST_API_BASE + Constants.ACT_GET_SINGLE_MESSAGE,
				httpParam, null);

		GetSingleMessageResult result = null;
		if (response != null) {
			try {
				Gson gson = new Gson();
				result = gson.fromJson(response, GetSingleMessageResult.class);
			} catch (JsonSyntaxException e) {
				Log.e("GetSingleMessage", e.toString());
			}
			return result;
		} else
			return null;
	}
	
	public static SendMessageResult SendMessage(SendMessageParam param)
	{
		HttpParams httpParam = new HttpParams();
		httpParam.addParam(Constants.PARAM_USER_ID, param.user_id);
		httpParam.addParam(Constants.PARAM_RECIPIENTS, param.recipients);
		httpParam.addParam(Constants.PARAM_SUBJECT, param.subject);
		httpParam.addParam(Constants.PARAM_CONTENT, param.content);
		httpParam.addParam(Constants.PARAM_IS_NOTICE, Boolean.toString(param.is_notice));

		String response = HttpApi.sendRequest(Constants.SERVER_REST_API_BASE + Constants.ACT_SEND_MESSAGE,
				httpParam, null);

		SendMessageResult result = null;
		if (response != null) {
			try {
				Gson gson = new Gson();
				result = gson.fromJson(response, SendMessageResult.class);
			} catch (JsonSyntaxException e) {
				Log.e("SendMessage", e.toString());
			}
			return result;
		} else
			return null;
	}
	
	public static ReplyMessageResult ReplyMessage(ReplyMessageParam param)
	{
		HttpParams httpParam = new HttpParams();
		httpParam.addParam(Constants.PARAM_USER_ID, param.user_id);
		httpParam.addParam(Constants.PARAM_MESSAGE_ID, Integer.toString(param.message_id));
		httpParam.addParam(Constants.PARAM_SUBJECT, param.subject);
		httpParam.addParam(Constants.PARAM_CONTENT, param.content);

		String response = HttpApi.sendRequest(Constants.SERVER_REST_API_BASE + Constants.ACT_REPLY_MESSAGE,
				httpParam, null);

		ReplyMessageResult result = null;
		if (response != null) {
			try {
				Gson gson = new Gson();
				result = gson.fromJson(response, ReplyMessageResult.class);
			} catch (JsonSyntaxException e) {
				Log.e("ReplyMessage", e.toString());
			}
			return result;
		} else
			return null;
	}
	
	public static SetSettingGeneralResult SetGeneral(SetSettingGeneralParam param)
	{
		HttpParams httpParam = new HttpParams();
		httpParam.addParam(Constants.PARAM_USER_ID, param.user_id);
		httpParam.addParam(Constants.PARAM_DISP_USER_ID, param.disp_user_id);
		httpParam.addParam(Constants.PARAM_EMAIL, param.email);
		httpParam.addParam(Constants.PARAM_PASSWORD, param.password);

		String response = HttpApi.sendRequest(Constants.SERVER_REST_API_BASE + Constants.ACT_SET_GENERAL,
				httpParam, null);

		SetSettingGeneralResult result = null;
		if (response != null) {
			try {
				Gson gson = new Gson();
				result = gson.fromJson(response, SetSettingGeneralResult.class);
			} catch (JsonSyntaxException e) {
				Log.e("SetGeneral", e.toString());
			}
			return result;
		} else
			return null;
	}
	
	public static GetSettingNoteResult GetEmailNote(GetSettingNoteParam param)
	{
		HttpParams httpParam = new HttpParams();
		httpParam.addParam(Constants.PARAM_USER_ID, param.user_id);
		httpParam.addParam(Constants.PARAM_DISP_USER_ID, param.disp_user_id);

		String response = HttpApi.sendRequest(Constants.SERVER_REST_API_BASE + Constants.ACT_GET_MAIL_NOTE,
				httpParam, null);

		GetSettingNoteResult result = null;
		if (response != null) {
			try {
				Gson gson = new Gson();
				result = gson.fromJson(response, GetSettingNoteResult.class);
			} catch (JsonSyntaxException e) {
				Log.e("GetEmailNote", e.toString());
			}
			return result;
		} else
			return null;
	}
	
	public static SetSettingNoteResult SetEmailNote(SetSettingNoteParam param)
	{
		HttpParams httpParam = new HttpParams();
		httpParam.addParam(Constants.PARAM_USER_ID, param.user_id);
		httpParam.addParam(Constants.PARAM_DISP_USER_ID, param.disp_user_id);
		httpParam.addParam(Constants.PARAM_NOTE_JSON, param.notifications);

		String response = HttpApi.sendRequest(Constants.SERVER_REST_API_BASE + Constants.ACT_SET_MAIL_NOTE,
				httpParam, null);

		SetSettingNoteResult result = null;
		if (response != null) {
			try {
				Gson gson = new Gson();
				result = gson.fromJson(response, SetSettingNoteResult.class);
			} catch (JsonSyntaxException e) {
				Log.e("SetEmailNote", e.toString());
			}
			return result;
		} else
			return null;
	}
	
	public static GetSettingPrivacyResult GetDefaultPrivacy(GetSettingPrivacyParam param)
	{
		HttpParams httpParam = new HttpParams();
		httpParam.addParam(Constants.PARAM_USER_ID, param.user_id);
		httpParam.addParam(Constants.PARAM_DISP_USER_ID, param.disp_user_id);

		String response = HttpApi.sendRequest(Constants.SERVER_REST_API_BASE + Constants.ACT_GET_DEF_PRIVACY,
				httpParam, null);

		GetSettingPrivacyResult result = null;
		if (response != null) {
			try {
				Gson gson = new Gson();
				result = gson.fromJson(response, GetSettingPrivacyResult.class);
			} catch (JsonSyntaxException e) {
				Log.e("GetDefaultPrivacy", e.toString());
			}
			return result;
		} else
			return null;
	}
	
	public static SetSettingPrivacyResult SetDefaultPrivacy(SetSettingPrivacyParam param)
	{
		HttpParams httpParam = new HttpParams();
		httpParam.addParam(Constants.PARAM_USER_ID, param.user_id);
		httpParam.addParam(Constants.PARAM_DISP_USER_ID, param.disp_user_id);
		httpParam.addParam(Constants.PARAM_PRIVACY, param.privacy);

		String response = HttpApi.sendRequest(Constants.SERVER_REST_API_BASE + Constants.ACT_SET_DEF_PRIVACY,
				httpParam, null);

		SetSettingPrivacyResult result = null;
		if (response != null) {
			try {
				Gson gson = new Gson();
				result = gson.fromJson(response, SetSettingPrivacyResult.class);
			} catch (JsonSyntaxException e) {
				Log.e("SetDefaultPrivacy", e.toString());
			}
			return result;
		} else
			return null;
	}
}
