package com.drem.dremboard.webservice;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.drem.dremboard.entity.Beans.*;
import android.content.Context;
import android.os.Handler;


public class WebApiInstance {

	private ExecutorService executorService;
	private Handler handler = new Handler();// handler to display images in UI thread
	
	public enum Type {
		
		/* User Actions */		
		SIGN_UP, SIGN_IN, SIGN_OUT,
		
		GET_PROFILE, SET_PROFILE, SET_AVATAR,
		
		CHANGE_PWD, RESET_PWD,
		
		/* Message */
		GET_MESSAGES, GET_SINGLE_MESSAGE, SEND_MESSAGE, REPLY_MESSAGE, SET_MESSAGE,
		
		/* Settings */
		SET_SETTING_GENERAL, GET_SETTING_NOTE, SET_SETTING_NOTE, GET_SETTING_PRIVACY, SET_SETTING_PRIVACY,
		
		/* Invite */
		SEND_INVITE,
		
		/* Notification */
		GET_NT, SET_NT,	GET_NT_CNT,
		
		/* GET Content */
		GET_DREM_ACTIVITY, GET_DREM, GET_DREMER, GET_DREMBOARD, GET_DREMCAST, GET_SINGLE_DREMER, GET_ONE_ACTIVITY, 
		
		/* Contents Actions */
		SET_COMMENT, EDIT_COMMENT, SET_FAVORITE, SET_LIKE, SET_FLAG, SET_SINGLE_DREMER_IMAGE,
		
		/* Dremer Actions */
		SET_FRIENDSHIP, SET_FAMILYSHIP, SET_BLOCK, SET_FOLLOW,
		
		/* Manage Dremboard */
		ADD_DREM_TO_DREMBOARD, CREATE_DREMBOARD, DELETE_DREMBOARD, EDIT_DREMBOARD, MERGE_DREMBOARD, REMOVE_DREMS_FROM_DREMBOARD, MOVE_DREMS_TO_DREMBOARD, SET_ACTIVITY, DELETE_ACTIVITY,
		
	};

	private WebApiInstance() { }
	 
    private static class SingletonHolder { 
            public static final WebApiInstance instance = new WebApiInstance();
    }

    public static WebApiInstance getInstance() {
            return SingletonHolder.instance;
    }

    public void init(Context context) {
//		executorService = Executors.newFixedThreadPool(5);
		executorService = Executors.newCachedThreadPool();
	}
    
    public void init(Context context, int numberOfThreads) {
		executorService = Executors.newFixedThreadPool(numberOfThreads);
	}
    
	public void init(Context context, String directory) {
//		executorService = Executors.newFixedThreadPool(5);
		executorService = Executors.newCachedThreadPool();
	}
	
	
	public void init(Context context, String directory, int numberOfThreads) {
		executorService = Executors.newFixedThreadPool(numberOfThreads);
	}
	
	public void executeAPI(Type type, Object param, WebApiCallback callback)
	{
		callback.onPreProcessing(type, param);
		executorService.submit(new ApiRunner(type, param, callback));
	}    
	
	class ApiRunner implements Runnable {
		WebApiCallback webApiCallback;
		Object parameter;
		Type type;

		ApiRunner(Type type, Object param, WebApiCallback callback) {
			webApiCallback = callback;
			parameter = param;
			this.type = type; 
		}

		@Override
		public void run() {
			try {
				
				Object result = callApi(type, parameter);
				
				ApiResult apiResult = new ApiResult(type, parameter, result, webApiCallback);
				handler.post(apiResult);
			} catch (Throwable th) {
				th.printStackTrace();
			}
		}
	}

	class ApiResult implements Runnable {
		WebApiCallback webApiCallback;
		Object parameter, result;
		Type type;

		ApiResult(Type type, Object param, Object obj, WebApiCallback callback) {
			webApiCallback = callback;
			parameter = param;
			result = obj;
			this.type = type;
		}

		@Override
		public void run() {
			try {
				if (webApiCallback != null)
					webApiCallback.onResultProcessing(type, parameter, result);
			} catch (Throwable th) {
				th.printStackTrace();
			}
		}
	}
	
	Object callApi(Type type, Object param)
	{
		Object result = null;
		
		switch (type)
		{
		case GET_DREM_ACTIVITY:
			result = getActivities(param);
			break;
			
		case GET_DREM:
			result = getDrems(param);
			break;

		case GET_DREMER:
			result = getDremers(param);
			break;
			
		case GET_DREMBOARD:
			result = getDremboards(param); 
			break;
			
		case GET_DREMCAST:
			result = getDremcasts(param);
			break;
			
		case GET_SINGLE_DREMER:
			result = getSingleDremer(param);
			break;
			
		case SET_SINGLE_DREMER_IMAGE:
			result = setSingleDremerImage(param);
			break;
			
		case GET_NT:
			result = getNotifications(param);
			break;
			
		case SET_NT:
			result = setNotification(param);
			break;
			
		case GET_NT_CNT:
			result = getNotificationsCount(param);
			break;
			
		case SET_COMMENT:
			result = setComment(param);
			break;
			
		case EDIT_COMMENT:
			result = editComment(param);
			break;

		case SET_FAVORITE:
			result = setFavorite(param);
			break;
			
		case SET_LIKE:
			result = setLike(param);
			break;
			
		case SET_FLAG:
			result = setFlag(param);
			break;
			
		case SET_FRIENDSHIP:
			result = setDremerFriendship(param);
			break;
			
		case SET_FAMILYSHIP:
			result = setDremerFamilyship(param);
			break;
			
		case SET_FOLLOW:
			result = setDremerFollowing(param);
			break;
			
		case SET_BLOCK:
			result = setDremerBlocking(param);
			break;
			
		case GET_MESSAGES:
			result = getMessages(param);
			break;
		case GET_SINGLE_MESSAGE:
			result = getSingleMessage(param);
			break;
		case SEND_MESSAGE:
			result = sendMessage(param);
			break;
		case REPLY_MESSAGE:
			result = replyMessage(param);
			break;
		case SET_MESSAGE:
			break;
			
		case SET_SETTING_GENERAL:
			result = setSettingGeneral(param);
			break;
		case GET_SETTING_NOTE:
			result = getSettingEmailNote(param);			
			break;
		case SET_SETTING_NOTE:
			result = setSettingEmailNote(param);			
			break;
		case GET_SETTING_PRIVACY:
			result = getSettingDefaultPrivacy(param);
			break;
		case SET_SETTING_PRIVACY:
			result = setSettingDefaultPrivacy(param);
			break;
			
		case ADD_DREM_TO_DREMBOARD:
			result = addDremToDremboard(param);
			break;
			
		case CREATE_DREMBOARD:
			result = createDremboard(param);
			break;
			
		case DELETE_DREMBOARD:
			result = deleteDremboard(param);
			break;
			
		case EDIT_DREMBOARD:
			result = editDremboard(param);
			break;
			
		case MERGE_DREMBOARD:
			result = mergeDremboard(param);
			break;
			
		case REMOVE_DREMS_FROM_DREMBOARD:
			result = removeDremsFromDremboard(param);
			break;

		case MOVE_DREMS_TO_DREMBOARD:
			result = moveDremsToDremboard(param);
			break;

		case SET_ACTIVITY:
			result = setActivityDrem(param);
			break;
			
		case DELETE_ACTIVITY:
			result = deleteActivityDrem(param);
			break;
		
		case GET_ONE_ACTIVITY:
			result = getActivity(param);
			break;
		default:
			break;
		}
		
		return result;
	}
	
	/* **************************************************************************
	 *                           Get Contents
	 * *************************************************************************/
	Object getActivities(Object obj)
	{
		Object result = null;
		GetActivitiesParam param = (GetActivitiesParam)obj;
		
		if (param == null)
			return null;		
		
		result = Server.GetDremActivities(param);
		return result;
	}
	
	Object getDrems(Object obj)
	{
		Object result = null;
		GetDremsParam param = (GetDremsParam)obj;
		
		if (param == null)
			return null;		
		
		result = Server.GetDrems(param);
		return result;
	}
	
	Object getDremers(Object obj)
	{
		Object result = null;
		GetDremersParam param = (GetDremersParam)obj;
		
		if (param == null)
			return null;		
		
		result = Server.GetDremers(param);
		return result;
	}
	
	Object getSingleDremer(Object obj)
	{
		Object result = null;
		GetSingleDremerParam param = (GetSingleDremerParam)obj;
		
		if (param == null)
			return null;		
		
		result = Server.GetSingleDremer(param);
		return result;
	}
	
	Object setSingleDremerImage(Object obj)
	{
		Object result = null;
		SetSingleDremerImageParam param = (SetSingleDremerImageParam)obj;
		
		if (param == null)
			return null;		
		
		result = Server.SetSingleDremerImage(param);
		return result;
	}
	
	Object getDremboards(Object obj)
	{
		Object result = null;
		GetDremboardsParam param = (GetDremboardsParam)obj;
		
		if (param == null)
			return null;		
		
		result = Server.GetDremboards(param);
		return result;
	}
	
	Object getDremcasts(Object obj)
	{
		Object result = null;
		GetDremcastsParam param = (GetDremcastsParam)obj;
		
		if (param == null)
			return null;		
		
		result = Server.GetDremcasts(param);
		return result;
	}
	
	/* **************************************************************************
	 *                           Contents Actions
	 * *************************************************************************/
	Object setFavorite(Object obj)
	{
		Object result = null;
		SetFavoriteParam param = (SetFavoriteParam)obj;
		
		if (param == null)
			return null;		
		
		result = Server.SetFavorite(param);
		return result;
	}
	
	Object setLike(Object obj)
	{
		Object result = null;
		SetLikeParam param = (SetLikeParam)obj;
		
		if (param == null)
			return null;		
		
		result = Server.SetLike(param);
		return result;
	}
	
	Object setFlag(Object obj)
	{
		Object result = null;
		SetFlagParam param = (SetFlagParam)obj;
		
		if (param == null)
			return null;		
		
		result = Server.SetFlag(param);
		return result;
	}
	
	Object setComment(Object obj)
	{
		Object result = null;
		SetCommentParam param = (SetCommentParam)obj;
		
		if (param == null)
			return null;		
		
		result = Server.SetActivityComment(param);
		return result;
	}
	
	Object editComment(Object obj)
	{
		Object result = null;
		EditCommentParam param = (EditCommentParam)obj;
		
		if (param == null)
			return null;		
		
		result = Server.EditActivityComment(param);
		return result;
	}

	/* **************************************************************************
	 *                           Dremer Actions
	 * *************************************************************************/
	Object setDremerFriendship(Object obj)
	{
		Object result = null;
		SetFriendshipParam param = (SetFriendshipParam)obj;
		
		if (param == null)
			return null;		
		
		result = Server.SetDremerFriendship(param);
		return result;
	}
	
	Object setDremerFamilyship(Object obj)
	{
		Object result = null;
		SetFamilyshipParam param = (SetFamilyshipParam)obj;
		
		if (param == null)
			return null;		
		
		result = Server.SetDremerFamilyship(param);
		return result;
	}
	
	Object setDremerFollowing(Object obj)
	{
		Object result = null;
		SetFollowingParam param = (SetFollowingParam)obj;
		
		if (param == null)
			return null;		
		
		result = Server.SetDremerFollowing(param);
		return result;
	}

	Object setDremerBlocking(Object obj)
	{
		Object result = null;
		SetBlockingParam param = (SetBlockingParam)obj;
		
		if (param == null)
			return null;		
		
		result = Server.SetDremerBlocking(param);
		return result;
	}
	
	/* **************************************************************************
	 *                           Notification
	 * *************************************************************************/
	Object getNotifications(Object obj)
	{
		Object result = null;
		GetNTsParam param = (GetNTsParam)obj;
		
		if (param == null)
			return null;		
		
		result = Server.GetNotifications(param);
		return result;
	}
	
	Object setNotification(Object obj)
	{
		Object result = null;
		SetNTParam param = (SetNTParam)obj;
		
		if (param == null)
			return null;		
		
		result = Server.SetNotification(param);
		return result;
	}
	
	Object getNotificationsCount(Object obj)
	{
		Object result = null;
		GetNTCntParam param = (GetNTCntParam)obj;
		
		if (param == null)
			return null;		
		
		result = Server.GetNotificationCount(param);
		return result;
	}
	
	/* **************************************************************************
	 *                           Message
	 * *************************************************************************/
	Object getMessages(Object obj)
	{
		Object result = null;
		GetMessagesParam param = (GetMessagesParam)obj;
		
		if (param == null)
			return null;		
		
		result = Server.GetMessages(param);
		return result;
	}
	
	Object getSingleMessage(Object obj)
	{
		Object result = null;
		GetSingleMessageParam param = (GetSingleMessageParam)obj;
		
		if (param == null)
			return null;		
		
		result = Server.GetSingleMessage(param);
		return result;
	}
	
	Object sendMessage(Object obj)
	{
		Object result = null;
		SendMessageParam param = (SendMessageParam)obj;
		
		if (param == null)
			return null;		
		
		result = Server.SendMessage(param);
		return result;
	}
	
	Object replyMessage(Object obj)
	{
		Object result = null;
		ReplyMessageParam param = (ReplyMessageParam)obj;
		
		if (param == null)
			return null;		
		
		result = Server.ReplyMessage(param);
		return result;
	}
	
	/* **************************************************************************
	 *                           Settings
	 * *************************************************************************/
	Object setSettingGeneral(Object obj)
	{
		Object result = null;
		SetSettingGeneralParam param = (SetSettingGeneralParam)obj;
		
		if (param == null)
			return null;		
		
		result = Server.SetGeneral(param);
		return result;
	}
	
	Object getSettingEmailNote(Object obj)
	{
		Object result = null;
		GetSettingNoteParam param = (GetSettingNoteParam)obj;
		
		if (param == null)
			return null;		
		
		result = Server.GetEmailNote(param);
		return result;
	}

	
	Object setSettingEmailNote(Object obj)
	{
		Object result = null;
		SetSettingNoteParam param = (SetSettingNoteParam)obj;
		
		if (param == null)
			return null;		
		
		result = Server.SetEmailNote(param);
		return result;
	}
	
	Object getSettingDefaultPrivacy(Object obj)
	{
		Object result = null;
		GetSettingPrivacyParam param = (GetSettingPrivacyParam)obj;
		
		if (param == null)
			return null;		
		
		result = Server.GetDefaultPrivacy(param);
		return result;
	}
	
	Object setSettingDefaultPrivacy(Object obj)
	{
		Object result = null;
		SetSettingPrivacyParam param = (SetSettingPrivacyParam)obj;
		
		if (param == null)
			return null;		
		
		result = Server.SetDefaultPrivacy(param);
		return result;
	}
	
	Object addDremToDremboard(Object obj)
	{
		Object result = null;
		AddDremToDremboardData param = (AddDremToDremboardData)obj;
		
		if (param == null)
			return null;		
		
		result = Server.AddDremToDremboard(param);
		return result;
	}
	
	Object createDremboard(Object obj)
	{
		Object result = null;
		CreateDremboardData param = (CreateDremboardData)obj;
		
		if (param == null)
			return null;		
		
		result = Server.CreateDremboard(param);
		return result;
	}
	
	Object deleteDremboard(Object obj)
	{
		Object result = null;
		DeleteDremboardData param = (DeleteDremboardData)obj;
		
		if (param == null)
			return null;		
		
		result = Server.DeleteDremboard(param);
		return result;
	}
	
	Object editDremboard(Object obj)
	{
		Object result = null;
		EditDremboardData param = (EditDremboardData)obj;
		
		if (param == null)
			return null;		
		
		result = Server.EditDremboard(param);
		return result;
	}
	
	Object mergeDremboard(Object obj)
	{
		Object result = null;
		MergeDremboardData param = (MergeDremboardData)obj;
		
		if (param == null)
			return null;		
		
		result = Server.MergeDremboard(param);
		return result;
	}
	
	Object removeDremsFromDremboard(Object obj)
	{
		Object result = null;
		RemoveDremsFromDremboardData param = (RemoveDremsFromDremboardData)obj;
		
		if (param == null)
			return null;		
		
		result = Server.RemoveDremsFromDremboard(param);
		return result;
	}
	
	Object moveDremsToDremboard(Object obj)
	{
		Object result = null;
		MoveDremsToDremboardData param = (MoveDremsToDremboardData)obj;
		
		if (param == null)
			return null;		
		
		result = Server.MoveDremsToDremboard(param);
		return result;
	}
	
	Object setActivityDrem(Object obj)
	{
		Object result = null;
		EditActivityDremData param = (EditActivityDremData)obj;
		
		if (param == null)
			return null;		
		
		result = Server.SetActivityDrem(param);
		return result;
	}
	
	Object deleteActivityDrem(Object obj)
	{
		Object result = null;
		DeleteActivityDremData param = (DeleteActivityDremData)obj;
		
		if (param == null)
			return null;		
		
		result = Server.DeleteActivityDrem(param);
		return result;
	}
	
	Object getActivity(Object obj)
	{
		Object result = null;
		GetActivityParam param = (GetActivityParam)obj;
		
		if (param == null)
			return null;		
		
		result = Server.getActivity(param);
		return result;
	}
}
