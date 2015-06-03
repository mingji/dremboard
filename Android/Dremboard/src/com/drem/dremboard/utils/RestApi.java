package com.drem.dremboard.utils;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;

import com.drem.dremboard.view.CustomToast;
import com.drem.dremboard.view.WaitDialog;
import com.drem.dremboard.webservice.BaseTask;
import com.drem.dremboard.webservice.BaseTask.TaskListener;
import com.drem.dremboard.webservice.Constants;
import com.drem.dremboard.webservice.HttpApi;
import com.drem.dremboard.webservice.HttpParams;

public abstract class RestApi {
	public Activity activity;
	public String action;
	public HashMap<String, String> params;
	public String user_id;
	
	private AppPreferences mPrefs;
	public WaitDialog waitDialog;

	public static final String SERVER_REST_API_BASE = Constants.HTTP_HOME
			+ "api/rest/";

	public static final String ACT_LOG_IN 			= "user_login";
	public static final String ACT_GET_INIT_PARAMS 	= "get_init_params";
	public static final String ACT_GET_DREMS 		= "get_drems";
	public static final String ACT_SET_FAVORITE 	= "set_favorite";
	public static final String ACT_SET_LIKE 		= "set_like"; 
	public static final String ACT_SHARE_DREM 		= "share_drem"; 
	public static final String ACT_USER_REGISTER	= "user_register";
	public static final String ACT_RETRIEVE_PASSWORD= "retrieve_password";
	public static final String ACT_FLAG_DREM		= "flag_drem";
	public static final String ACT_GET_ACTIVITIES	= "get_activities";

	public static final String PARAM_USERNAME 		= "username";
	public static final String PARAM_PASSWORD 		= "password";
	public static final String PARAM_USER_LOGIN 	= "user_login";
	public static final String PARAM_USER_ID 		= "user_id"; 
	public static final String PARAM_CATEGORY 		= "category"; 
	public static final String PARAM_SEARCH_STR 	= "search_str"; 
	public static final String PARAM_LAST_MEDIA_ID 	= "last_media_id"; 
	public static final String PARAM_PER_PAGE 		= "per_page";
	public static final String PARAM_ACTIVITY_ID 	= "activity_id"; 
	public static final String PARAM_RTMEDIA_ID 	= "rtmedia_id";
	public static final String PARAM_FAVORIT_STR 	= "favorite_str";
	public static final String PARAM_LIKE_STR 		= "like_str"; 
	public static final String PARAM_DESCRIPTION 	= "description"; 
	public static final String PARAM_SHARE_USER 	= "share_user"; 
	public static final String PARAM_SHARE_MODE 	= "share_mode"; 
	public static final String PARAM_FLAG_SLUG		= "flag_slug";
	public static final String PARAM_ACTIVITY_SCOPE	= "activity_scope";
	public static final String PARAM_LAST_ACTIVITY_ID = "last_activity_id";
	

	public RestApi(Activity activity, String action,
			HashMap<String, String> params) {
		this.activity = activity;
		this.action = action;
		this.params = params;
		this.mPrefs = new AppPreferences(activity);
		user_id = this.mPrefs.getUserId();
		waitDialog = new WaitDialog(this.activity);
	}
	
	private String getRestApi(String action,
			HashMap<String, String> params) {
		HttpParams param = new HttpParams();
		
		if (!user_id.equals("")){
			param.addParam(PARAM_USER_ID, user_id);
		}

		for (Map.Entry<String, String> param_data : params.entrySet()) {
			param.addParam(param_data.getKey(), param_data.getValue());
		}

		String response = HttpApi.sendRequest(RestApi.SERVER_REST_API_BASE
				+ action, param, null);
		return response;
	}

	public abstract void process_result(JSONObject data) throws JSONException;

	// ////////////////Rest Api/////////////////////
	public void execute() {
		BaseTask.run(new TaskListener() {
			@Override
			public Object onTaskRunning(int taskId, Object data) {
				
				return getRestApi(action, params);
			}

			@Override
			public void onTaskResult(int taskId, Object result) {
				try {
					waitDialog.dismiss();
					String json_result = (String) result;
					JSONObject c = new JSONObject(json_result);
					String status = c.getString("status");
					String msg = c.getString("msg");
					JSONObject data = c.getJSONObject("data");
					if (status.equals("ok"))
						process_result(data);

					if (!msg.equals(""))
						CustomToast.makeCustomToastShort(activity, msg);

				} catch (Exception e) {
					CustomToast.makeCustomToastShort(activity,
							"Couldn't connect to Dremboard.com");
				}

			}

			@Override
			public void onTaskPrepare(int taskId, Object data) {
				// TODO Auto-generated method stub
				waitDialog.show();
			}

			@Override
			public void onTaskProgress(int taskId, Object... values) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onTaskCancelled(int taskId) {
				// TODO Auto-generated method stub
				
			}
		});
	}

}
