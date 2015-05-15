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
import com.drem.dremboard.entity.Beans.SetFamilyshipParam;
import com.drem.dremboard.entity.Beans.SetFamilyshipResult;
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

public class DialogFamilyshipAccept extends Dialog implements WebApiCallback {
	Activity activity;
	DremerInfo mDremer;
	Button btnAccept, btnReject;
	HyIconView mImgDremer;
	TextView mTxtName, mTxtUptime;	

	AppPreferences mPrefs;

	WaitDialog waitDialog;

	OnFamilyshipResultCallback mResultCallback;
	int itemIndex;

	public DialogFamilyshipAccept(Context context, Activity activity, DremerInfo dremer, int index, OnFamilyshipResultCallback callback) {
		super(context);

		this.activity = activity;
		this.mDremer = dremer;
		this.itemIndex = index;
		this.mResultCallback = callback;
	}

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.dialog_familyship_accept);

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
				setDremerFamilyship("reject");
			}
		});
		btnAccept.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				setDremerFamilyship("accept");
			}
		});
	}

	private void setDremerFamilyship(String action)	{


		waitDialog.show();

		SetFamilyshipParam param = new SetFamilyshipParam();

		param.user_id = mPrefs.getUserId();
		param.dremer_id = mDremer.user_id;
		param.action = action;

		WebApiInstance.getInstance().executeAPI(Type.SET_FAMILYSHIP, param, this);
	}

	private void setDremerFamilyshipResult(Object obj) {

		waitDialog.dismiss();

		if (obj == null) {
			CustomToast.makeCustomToastShort(this.activity, Constants.NETWORK_ERR);
		}

		if (obj != null){
			SetFamilyshipResult resultBean = (SetFamilyshipResult)obj;

			if (resultBean.status.equals("ok")) {
				if (this.mResultCallback != null)
					mResultCallback.OnFamilyshipResult(resultBean.data.familyship_status, itemIndex);
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
		case SET_FAMILYSHIP:
			setDremerFamilyshipResult(result);			
			break;
		default:
			break;
		}
	}

	public interface OnFamilyshipResultCallback {
		void OnFamilyshipResult(String familyshipStatus, int index);
	}
}

