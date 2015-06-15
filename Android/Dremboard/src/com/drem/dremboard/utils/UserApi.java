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

//==============================================================================
public abstract class UserApi {
	public Activity		mActivity;
	public String		mStrAction;
	public HashMap<String, String>	mMapParams;
	public String		mStrUserId;
	
	private AppPreferences	mPrefs;
	public WaitDialog		mWaitDialog;

	public static final String SERVER_USER_API_BASE		= Constants.HTTP_HOME + "api/user/";

	public static final String ACT_FB_CONNECT 	= "fb_connect";
	public static final String ACT_GET_AVATAR	= "get_avatar";

	public static final String PARAM_ACCESS_TOKEN	= "access_token";
	public static final String PARAM_USER_ID 		= "user_id";
	public static final String PARAM_TYPE			= "type";

	//------------------------------------------------------------------------------
	public abstract void process_result(JSONObject aJsonData) throws JSONException;

	//------------------------------------------------------------------------------
	public UserApi(Activity aActivity, String aStrAction, HashMap<String, String> aMapParams) {
		this.mActivity	= aActivity;
		this.mStrAction	= aStrAction;
		this.mMapParams	= aMapParams;
		this.mPrefs		= new AppPreferences(aActivity);
		mStrUserId	= this.mPrefs.getUserId();
		mWaitDialog	= new WaitDialog(this.mActivity);
	}
	
	//------------------------------------------------------------------------------
	private String getRestApi(String aStrAction, HashMap<String, String> aMapParams) {
		HttpParams	httpParam = new HttpParams();
		String		strResponse = new String();

		for (Map.Entry<String, String> entry : aMapParams.entrySet()) {
			httpParam.addParam(entry.getKey(), entry.getValue());
		}

		strResponse = HttpApi.sendRequest(UserApi.SERVER_USER_API_BASE + aStrAction, httpParam, null);

		return strResponse;
	}

	//------------------------------------------------------------------------------
	// User Api
	public void execute() {
		BaseTask.run(
			new TaskListener() {
				@Override
				public Object onTaskRunning(int taskId, Object data) {
					return getRestApi(mStrAction, mMapParams);
				}
	
				@Override
				public void onTaskResult(int taskId, Object result) {
					try {
						mWaitDialog.dismiss();
						String		jsonResult	= (String)result;
						JSONObject	jsonData	= new JSONObject(jsonResult);
						String		strStatus	= new String();
						String		strMsg		= new String();

						if (jsonData.has("status"))
							strStatus = jsonData.getString("status");
						if (jsonData.has("msg"))
							strMsg = jsonData.getString("msg");

						if (strStatus.equals("ok") && (jsonData != null))
							process_result(jsonData);
	
						if (!strMsg.equals(""))
							CustomToast.makeCustomToastShort(mActivity, strMsg);
					}
					catch (Exception e) {
						CustomToast.makeCustomToastShort(mActivity, "Couldn't connect to Dremboard.com");
					}
				}
	
				@Override
				public void onTaskPrepare(int taskId, Object data) {
					// TODO Auto-generated method stub
					mWaitDialog.show();
				}
	
				@Override
				public void onTaskProgress(int taskId, Object... values) {
					// TODO Auto-generated method stub
				}
	
				@Override
				public void onTaskCancelled(int taskId) {
					// TODO Auto-generated method stub
				}
			}
		);
	}

}
//==============================================================================

