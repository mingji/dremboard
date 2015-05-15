package com.drem.dremboard.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class AppPreferences {

	//private static final String CLASS_NAME = "AppPreferences";
	private static String APP_SHARED_PREFS;
	private final String IS_LOGIN = "IS_LOGIN";
	private final String FIRST_RUN = "FIRST_RUNNING";
	private final String USER_NAME = "USER_NAME";
	private final String USER_PWORD = "USER_PWORD";
	private final String USER_ID = "USER_ID";
	private final String USER_LOGIN = "USER_LOGIN";
	private final String USER_AVATAR = "USER_AVATAR";
	
	private final String TOKEN = "TOKEN";
	private final String GCM_REGID = "GCM_REGISTRATION_ID";
	private final String CATEGORY = "CATEGORY";
	private final String CATEGORY_LIST = "CATEGORY_LIST";
	private final String SEARCH_STR = "SEARCH_STR";
	
	private SharedPreferences mPrefs;
	private Editor mPrefsEditor;

	public AppPreferences(Context context) {

		APP_SHARED_PREFS = context.getApplicationContext().getPackageName();
		this.mPrefs = context.getSharedPreferences(APP_SHARED_PREFS, Activity.MODE_PRIVATE);
		this.mPrefsEditor = mPrefs.edit();
	}
	
	public static AppPreferences getInstance(Context context){
		AppPreferences mPrefs;
		mPrefs = new AppPreferences(context);
		return mPrefs;
	}
	
	public void setAccountname(String value) {
		mPrefsEditor.putString(USER_NAME, value);
		mPrefsEditor.commit();
	}

	public String getAccountname() {
		String value = mPrefs.getString(USER_NAME, "");
		return value;
	}

	public void setAccountPasswd(String value) {
		mPrefsEditor.putString(USER_PWORD, value);
		mPrefsEditor.commit();
	}

	public String getAccountPasswd() {
		String value = mPrefs.getString(USER_PWORD, "");
		return value;
	}

	public void setFirstRunning(boolean value) {
		mPrefsEditor.putBoolean(FIRST_RUN, value);
		mPrefsEditor.commit();
	}

	public boolean isFirstRunning() {
		boolean value = mPrefs.getBoolean(FIRST_RUN, true);
		return value;
	}
	
	public void setLogIn(boolean value) {
		mPrefsEditor.putBoolean(IS_LOGIN, value);
		mPrefsEditor.commit();
	}

	public boolean isLogIn() {
		boolean value = mPrefs.getBoolean(IS_LOGIN, false);
		return value;
	}
	
	public void setUserLogin(String value) {
		mPrefsEditor.putString(USER_LOGIN, value);
		mPrefsEditor.commit();
	}

	public String getUserLogin() {
		String value = mPrefs.getString(USER_LOGIN, "");
		return value;
	}
	
	public void setUserAvatar(String value) {
		mPrefsEditor.putString(USER_AVATAR, value);
		mPrefsEditor.commit();
	}

	public String getUserAvatar() {
		String value = mPrefs.getString(USER_AVATAR, "");
		return value;
	}
	
	public void setUserId(String value) {
		mPrefsEditor.putString(USER_ID, value);
		mPrefsEditor.commit();
	}

	public String getUserId() {
		String value = mPrefs.getString(USER_ID, "");
		return value;
	}
	
	
	public void setToken(String value) {
		mPrefsEditor.putString(TOKEN, value);
		mPrefsEditor.commit();
	}

	public String getToken() {
		String value = mPrefs.getString(TOKEN, "");
		return value;
	}
	
	public void setGcmRegId(String value) {
		mPrefsEditor.putString(GCM_REGID, value);
		mPrefsEditor.commit();
	}

	public String getGcmRegId() {
		String value = mPrefs.getString(GCM_REGID, "");
		return value;
	}
	
	public void setCategory(String category) {
		mPrefsEditor.putString(CATEGORY, category);
		mPrefsEditor.commit();
	}
	public String getCategory() {
		String value = mPrefs.getString(CATEGORY, "");
		return value;
	}
	
	public void setSearchStr(String search_str) {
		mPrefsEditor.putString(SEARCH_STR, search_str);
		mPrefsEditor.commit();
	}
	public String getSearchStr() {
		String value = mPrefs.getString(SEARCH_STR, "");
		return value;
	}
	
	public void setCategoryList(String categorylist) {
		mPrefsEditor.putString(CATEGORY_LIST, categorylist);
		mPrefsEditor.commit();
	}
	public String getCategoryList() {
		String value = mPrefs.getString(CATEGORY_LIST, "");
		return value;
	}
}
