package com.drem.dremboard.ui;

import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import com.drem.dremboard.R;
import com.drem.dremboard.utils.RestApi;
import com.drem.dremboard.view.CustomToast;

public class DialogResetPassword extends Dialog implements
		android.view.View.OnClickListener {

	Activity activity;

	Button mBtnCancel, mBtnSubmit;
	EditText mTxtUsername;

	public DialogResetPassword(Context context, Activity activity) {
		super(context);
		// TODO Auto-generated constructor stub
		this.activity = activity;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.dialog_reset_password);

		getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL,
				WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL);

		mTxtUsername = (EditText) findViewById(R.id.txtEmail);
		mBtnCancel = (Button) findViewById(R.id.btnCancel);
		mBtnSubmit = (Button) findViewById(R.id.btnSubmit);

		mBtnCancel.setOnClickListener(this);
		mBtnSubmit.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btnCancel:
			dismiss();
			break;
		case R.id.btnSubmit:
			if (!mTxtUsername.getText().toString().isEmpty())
				resetPasswordTask(mTxtUsername.getText().toString());
			else {
				String errStr = this.activity.getResources().getString(
						R.string.error_enter_username);
				CustomToast.makeCustomToastLong(this.activity, errStr);
			}
			break;
		default:
			break;
		}
	}

	private void resetPasswordTask(final String user_login) {
		String action = RestApi.ACT_RETRIEVE_PASSWORD;
		HashMap<String, String> params = new HashMap<String, String>();
		params.put(RestApi.PARAM_USER_LOGIN, user_login);
		new RestApi(activity, action, params) {
			public void process_result(JSONObject data) throws JSONException {
				CustomToast.makeCustomToastLong(this.activity,
						"Please check your mail.");
				dismiss();
			}
		}.execute();
	}
}
