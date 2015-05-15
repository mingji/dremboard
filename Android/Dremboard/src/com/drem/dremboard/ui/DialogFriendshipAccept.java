package com.drem.dremboard.ui;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.drem.dremboard.R;
import com.drem.dremboard.entity.Beans.SetFriendshipParam;
import com.drem.dremboard.entity.Beans.SetFriendshipResult;
import com.drem.dremboard.entity.DremerInfo;
import com.drem.dremboard.utils.AppPreferences;
import com.drem.dremboard.utils.ImageLoader;
import com.drem.dremboard.utils.Utility;
import com.drem.dremboard.view.CustomToast;
import com.drem.dremboard.view.HyIconView;
import com.drem.dremboard.view.WaitDialog;
import com.drem.dremboard.webservice.Constants;
import com.drem.dremboard.webservice.WebApiCallback;
import com.drem.dremboard.webservice.WebApiInstance;
import com.drem.dremboard.webservice.WebApiInstance.Type;

public class DialogFriendshipAccept extends Dialog implements WebApiCallback {
	Activity activity;
	DremerInfo mDremer;
	Button btnAccept, btnReject;
	HyIconView mImgDremer;
	TextView mTxtName, mTxtUptime;	

	AppPreferences mPrefs;

	WaitDialog waitDialog;

	OnFriendshipResultCallback mResultCallback;
	int itemIndex;

	public DialogFriendshipAccept(Context context, Activity activity, DremerInfo dremer, int index, OnFriendshipResultCallback callback) {
		super(context);

		this.activity = activity;
		this.mDremer = dremer;
		this.itemIndex = index;
		this.mResultCallback = callback;
	}

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.dialog_friendship_accept);

		setCancelable(true);

		mPrefs = new AppPreferences(this.activity);
		waitDialog = new WaitDialog(this.activity);

		mImgDremer = (HyIconView) findViewById(R.id.imgUser);
		mTxtName = (TextView) findViewById(R.id.txtUserName);
		mTxtUptime = (TextView) findViewById(R.id.txtUptime);
		
		if (mDremer.user_avatar != null && !mDremer.user_avatar.isEmpty())
			ImageLoader.getInstance().displayImage(mDremer.user_avatar, mImgDremer, 0, 0);
		else
			mImgDremer.imageView.setImageResource(R.drawable.empty_man);
		mTxtName.setText(mDremer.display_name);
		mTxtUptime.setText(mDremer.last_activity);
		
		btnAccept = (Button) findViewById(R.id.btnAccept);
		btnReject = (Button) findViewById(R.id.btnReject);

		btnReject.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				setDremerFriendship("reject");
			}
		});
		btnAccept.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				setDremerFriendship("accept");
			}
		});
	}

	private void setDremerFriendship(String action)	{


		waitDialog.show();

		SetFriendshipParam param = new SetFriendshipParam();

		param.user_id = mPrefs.getUserId();
		param.dremer_id = mDremer.user_id;
		param.action = action;

		WebApiInstance.getInstance().executeAPI(Type.SET_FRIENDSHIP, param, this);
	}

	private void setDremerFriendshipResult(Object obj) {

		waitDialog.dismiss();

		if (obj == null) {
			CustomToast.makeCustomToastShort(this.activity, Constants.NETWORK_ERR);
		}

		if (obj != null){
			SetFriendshipResult resultBean = (SetFriendshipResult)obj;

			if (resultBean.status.equals("ok")) {
				if (this.mResultCallback != null)
					mResultCallback.OnFriendshipResult(resultBean.data.friendship_status, itemIndex);
			} else {
				CustomToast.makeCustomToastShort(this.activity, resultBean.msg);
			}
		}

		dismiss();
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
		case SET_FRIENDSHIP:
			setDremerFriendshipResult(result);			
			break;
		default:
			break;
		}
	}

	public interface OnFriendshipResultCallback {
		void OnFriendshipResult(String friendshipStatus, int index);
	}
}

