package com.drem.dremboard.ui;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.drem.dremboard.Const;
import com.drem.dremboard.R;
import com.drem.dremboard.entity.DremerInfo;
import com.drem.dremboard.entity.GlobalValue;
import com.drem.dremboard.entity.Beans.GetSingleDremerParam;
import com.drem.dremboard.entity.Beans.GetSingleDremerResult;
import com.drem.dremboard.entity.ProfileItem;
import com.drem.dremboard.facebook.FacebookLoginUtil;
import com.drem.dremboard.facebook.FacebookLoginUtil.FacebookLoginFinishedListener;
import com.drem.dremboard.google.ActivityLoginGoogle;
import com.drem.dremboard.twitter.TwitterLoginUtil;
import com.drem.dremboard.twitter.TwitterLoginUtil.TwitterLoginFinishedListener;
import com.drem.dremboard.utils.AppPreferences;
import com.drem.dremboard.utils.RestApi;
import com.drem.dremboard.utils.UserApi;
import com.drem.dremboard.view.CustomToast;
import com.drem.dremboard.webservice.Constants;
import com.drem.dremboard.webservice.WebApiCallback;
import com.drem.dremboard.webservice.WebApiInstance;
import com.drem.dremboard.webservice.WebApiInstance.Type;

public class ActivityLogin extends Activity implements OnClickListener, WebApiCallback {

	private AppPreferences mPrefs;

	TextView mTxtHomeUrl;

	Button mBtnLogin, mBtnRegister, mBtnLostPwd;
	Button mBtnFacebook, mBtnGoogle, mBtnTwitter, mBtnWindows, mBtnLkdin;
	EditText mTxtUserName, mTxtPassWord;
	CheckBox mChkRemember;

	// Facebook
	FacebookLoginUtil mFacebookLoginUtil;
	String mStrFacebookId;
	String mStrFacebookName;
	String mStrFacebookAvatar;

	// Twitter
	TwitterLoginUtil mTwitterLoginUtil;
	String mStrTwitterName;
	
	// Google
	String mStrGoogleToken;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_login);
		mPrefs = new AppPreferences(this);
		initView();
	}

	private void initView() {
		mTxtHomeUrl = (TextView) findViewById(R.id.txtCopyright);
		mTxtHomeUrl.setMovementMethod(LinkMovementMethod.getInstance());

		mBtnLogin = (Button) findViewById(R.id.btnLogin);
		mBtnRegister = (Button) findViewById(R.id.btnRegister);
		mBtnLostPwd = (Button) findViewById(R.id.btnLostPwd);

		mBtnFacebook = (Button) findViewById(R.id.btnFacebook);
		mBtnGoogle = (Button) findViewById(R.id.btnGoogle);
		mBtnTwitter = (Button) findViewById(R.id.btnTwitter);
		mBtnWindows = (Button) findViewById(R.id.btnWindows);
		mBtnLkdin = (Button) findViewById(R.id.btnLinkedin);

		mTxtUserName = (EditText) findViewById(R.id.txtLoginUserName);
		mTxtPassWord = (EditText) findViewById(R.id.txtUserPassword);

		mChkRemember = (CheckBox) findViewById(R.id.chkRemember);

		String username, password;
		username = mPrefs.getAccountname();
		password = mPrefs.getAccountPasswd();
		mPrefs.setUserId("");

		mTxtUserName.setText(username);
		mTxtPassWord.setText(password);

		mBtnLogin.setOnClickListener(this);
		mBtnRegister.setOnClickListener(this);
		mBtnLostPwd.setOnClickListener(this);

		mBtnFacebook.setOnClickListener(this);
		mBtnGoogle.setOnClickListener(this);
		mBtnTwitter.setOnClickListener(this);
		mBtnWindows.setOnClickListener(this);
		mBtnLkdin.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		int id = v.getId();

		switch (id) {
		case R.id.btnLogin:
			onClickLogin();
			break;
		case R.id.btnRegister:
			onClickRegister();
			break;
		case R.id.btnLostPwd:
			onClickLostPwd();
			break;

		case R.id.btnFacebook:
			if (mFacebookLoginUtil == null)
				mFacebookLoginUtil = new FacebookLoginUtil(ActivityLogin.this);

			mFacebookLoginUtil.SetLoginFinishedListener(
				new FacebookLoginFinishedListener() {
					@Override
					public void OnFacebookLoginFinished(boolean result) {
						if (result) {
							mStrFacebookId = mFacebookLoginUtil.GetFacebookId();
							mStrFacebookName = mFacebookLoginUtil.GetFacebookName();
							ActivityLogin.this.onFacebookLoginResult();
						}
					}
				}
			);

			try {
				mFacebookLoginUtil.SetFacebookConnection();

				if (mFacebookLoginUtil.IsFacebookSessionValid()) {
					mStrFacebookId = mFacebookLoginUtil.GetFacebookId();
					mStrFacebookName = mFacebookLoginUtil.GetFacebookName();

					if (mStrFacebookName != null) {
						onFacebookLoginResult();
						break;
					}
				}
				mFacebookLoginUtil.StartAuthorizeFacebook();
			} catch (Exception e) { e.printStackTrace(); }
			
			break;
		case R.id.btnGoogle:
			Intent intentLogin2Google;
			intentLogin2Google = new Intent(ActivityLogin.this, ActivityLoginGoogle.class);
			startActivityForResult(intentLogin2Google, Const.REQUESTCODE_LOGIN_GOOGLE);
			break;
		case R.id.btnTwitter:
			if (mTwitterLoginUtil == null)
				mTwitterLoginUtil = new TwitterLoginUtil(ActivityLogin.this);
			mTwitterLoginUtil.SetLoginFinishedListener(
				new TwitterLoginFinishedListener() {
					@Override
					public void OnTwitterLoginFinished() {
						mStrTwitterName	= mTwitterLoginUtil.GetUserName();
						// Treat Result
						if (mStrTwitterName == null)	return;
						if (mStrTwitterName.isEmpty())	return;

						onTwitterLoginResult();
					}
				}
			);
			mTwitterLoginUtil.LoginTwitter();
			break;
		case R.id.btnWindows:
			break;
		case R.id.btnLinkedin:
			break;
		default:
			break;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
			case Const.REQUESTCODE_LOGIN_GOOGLE :
				try {
					mStrGoogleToken = data.getStringExtra(Const.KEY_TOKEN);
					onGoogleLoginResult();
				} catch (Exception e) {}
				break;
			case Const.REQUESTCODE_LOGIN_TWITTER :
				if (mTwitterLoginUtil != null)
					mTwitterLoginUtil.StartGetAccessToken(data.getStringExtra(Const.KEY_TWITTER_CALLBACK_URL));
				break;
			case Const.REQUESTCODE_LOGIN_FACEBOOK :
				if (mFacebookLoginUtil != null)
					mFacebookLoginUtil.AuthorizeCallback(requestCode, resultCode, data);
				break;
			default:
				break;
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	private void onFacebookLoginResult() {
		HashMap<String, String> mapParams = new HashMap<String, String>();
		
		mapParams.put(UserApi.PARAM_ACCESS_TOKEN, mFacebookLoginUtil.GetFacebookToken());

		new UserApi(ActivityLogin.this, UserApi.ACT_FB_CONNECT, mapParams) {
			public void process_result(JSONObject aJsonData) throws JSONException {
				String strUserId	= aJsonData.getString("wp_user_id");
				String strUserLogin	= aJsonData.getString("user_login");
				
				mPrefs.setUserId(strUserId);
				mPrefs.setUserLogin(strUserLogin);
				mPrefs.setCategory("-1");
				
				String strCateList = mPrefs.getCategoryList();
				if (strCateList.equals(""))
					get_init_params_task();
				else
					getSingleDremer();

			}
		}.execute();
	}

	private void onGoogleLoginResult() {
		HashMap<String, String> mapParams = new HashMap<String, String>();
		
		mapParams.put(UserApi.PARAM_ACCESS_TOKEN, mStrGoogleToken);

		new UserApi(ActivityLogin.this, UserApi.ACT_GG_CONNECT, mapParams) {
			public void process_result(JSONObject aJsonData) throws JSONException {
				String strUserId	= aJsonData.getString("wp_user_id");
				String strUserLogin	= aJsonData.getString("user_login");
				
				mPrefs.setUserId(strUserId);
				mPrefs.setUserLogin(strUserLogin);
				mPrefs.setCategory("-1");
				
				String strCateList = mPrefs.getCategoryList();
				if (strCateList.equals(""))
					get_init_params_task();
				else
					getSingleDremer();
			}
		}.execute();
	}
	
	private void onTwitterLoginResult() {
		HashMap<String, String> mapParams = new HashMap<String, String>();
		
		mapParams.put(UserApi.PARAM_ACCESS_TOKEN,			mTwitterLoginUtil.GetAccessToken());
		mapParams.put(UserApi.PARAM_ACCESS_TOKEN_SECRET,	mTwitterLoginUtil.GetAccessTokenSecret());
		mapParams.put(UserApi.PARAM_CONSUMER_KEY,			Const.TWITTER_CONSUMER_KEY);
		mapParams.put(UserApi.PARAM_CONSUMER_SECRET,		Const.TWITTER_CONSUMER_SECRET);

		new UserApi(ActivityLogin.this, UserApi.ACT_TW_CONNECT, mapParams) {
			public void process_result(JSONObject aJsonData) throws JSONException {
				String strUserId	= aJsonData.getString("wp_user_id");
				String strUserLogin	= aJsonData.getString("user_login");
				
				mPrefs.setUserId(strUserId);
				mPrefs.setUserLogin(strUserLogin);
				mPrefs.setCategory("-1");
				
				String strCateList = mPrefs.getCategoryList();
				if (strCateList.equals(""))
					get_init_params_task();
				else
					getSingleDremer();
			}
		}.execute();
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		boolean bool;
		if (keyCode != KeyEvent.KEYCODE_BACK) {
			bool = super.onKeyDown(keyCode, event);
		} else {
			showCloseDialog();
			bool = true;
		}
		return bool;
	}

	private void onClickLostPwd() {
		DialogResetPassword resetDiag = new DialogResetPassword(this, this);
		resetDiag.show();
	}

	private void onClickRegister() {
		// Run next activity
		Intent intent = new Intent();
		intent.setClass(this, ActivityRegister.class);
		startActivity(intent);
		overridePendingTransition(R.anim.in_right_left, R.anim.out_right_left);
	}
	
	private void startMainActivity()
	{
		Intent intent = new Intent();
		intent.setClass(this, ActivityMain.class);
		startActivity(intent);
		finish();
		overridePendingTransition(R.anim.in_right_left, R.anim.out_right_left);
	}

	private void onClickLogin() {
		String username = mTxtUserName.getText().toString();
		final String password = mTxtPassWord.getText().toString();

		if (mChkRemember.isChecked()) {
			mPrefs.setAccountname(username);
			mPrefs.setAccountPasswd(password);
		} else {
			mPrefs.setAccountname("");
			mPrefs.setAccountPasswd("");
		}

		String action = RestApi.ACT_LOG_IN;
		HashMap<String, String> params = new HashMap<String, String>();
		params.put(RestApi.PARAM_USERNAME, username);
		params.put(RestApi.PARAM_PASSWORD, password);

		new RestApi(this, action, params) {
			public void process_result(JSONObject data) throws JSONException {

				String user_id = data.getString("id");// resultBean.data.ID;
				String user_login = data.getString("user_login");
				String user_avatar = data.getString("avatar");
				mPrefs.setUserId(user_id);
				mPrefs.setUserLogin(user_login);
				mPrefs.setUserAvatar(user_avatar);
				mPrefs.setCategory("-1");
				GlobalValue.getInstance().setCurrentPassword(password);

				String categorylist = mPrefs.getCategoryList();
				if (categorylist.equals("")) {
					get_init_params_task();
				} else {
					getSingleDremer();
				}
			}
		}.execute();
	}	
	
	private void setCurrentDremer(DremerInfo dremer)
	{
		GlobalValue.getInstance().setCurrentDremer(dremer);
	}
	
	private void setCurrentProfiles(ArrayList<ProfileItem> profiles)
	{
		GlobalValue.getInstance().setCurrentProfiles(profiles);
	}

	private void getSingleDremer() {
		
		GetSingleDremerParam param = new GetSingleDremerParam();

		param.user_id = mPrefs.getUserId();
		param.disp_user_id = Integer.parseInt(mPrefs.getUserId());

		WebApiInstance.getInstance().executeAPI(Type.GET_SINGLE_DREMER, param, this);
	}

	private void getSingleDremerResult(Object param, Object obj) {

		if (obj == null) {
			CustomToast.makeCustomToastShort(this, Constants.NETWORK_ERR);
		}

		if (obj != null){
			GetSingleDremerResult resultBean = (GetSingleDremerResult)obj;

			if (resultBean.status.equals("ok")) {
				setCurrentDremer(resultBean.data.member);
				setCurrentProfiles(resultBean.data.profiles);
				startMainActivity();
			} else {
				CustomToast.makeCustomToastShort(this, resultBean.msg);
			}
		}
	}

	private void get_init_params_task() {
		String action = RestApi.ACT_GET_INIT_PARAMS;
		HashMap<String, String> params = new HashMap<String, String>();

		new RestApi(this, action, params) {
			public void process_result(JSONObject data) throws JSONException {
				JSONObject category_obj = data.getJSONObject("category");
				String category_json = category_obj.toString();
				mPrefs.setCategoryList(category_json);

				getSingleDremer();
			}
		}.execute();
	}

	private void showCloseDialog() {
		new AlertDialog.Builder(this)
				.setTitle("Exit App")
				.setMessage("Sure to exit?")
				.setPositiveButton("OK", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						finishApp();
					}
				})
				.setNegativeButton("Cancel",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {
							}
						})
				.setOnKeyListener(new DialogInterface.OnKeyListener() {
					@Override
					public boolean onKey(DialogInterface dialog, int keyCode,
							KeyEvent event) {
						if (keyCode == KeyEvent.KEYCODE_BACK) {
							return false;
						}
						return false;
					}
				}).show();
	}

	private void finishApp() {
		finish();
		Log.i("Dremboard", "close application");
		android.os.Process.killProcess(android.os.Process.myPid());
	}
	
	@Override
	public void onPreProcessing(Type type, Object parameter) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onResultProcessing(Type type, Object parameter, Object result) {
		// TODO Auto-generated method stub
		
		switch (type)
		{
		case GET_SINGLE_DREMER:
			getSingleDremerResult(parameter, result);			
			break;
		default:
			break;
		}
	}
}
