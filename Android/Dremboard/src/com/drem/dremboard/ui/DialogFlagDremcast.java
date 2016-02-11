package com.drem.dremboard.ui;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.drem.dremboard.R;
import com.drem.dremboard.entity.Beans.SetFlagParam;
import com.drem.dremboard.entity.Beans.SetFlagResult;
import com.drem.dremboard.entity.DremInfo;
import com.drem.dremboard.entity.Beans.SetFavoriteParam;
import com.drem.dremboard.entity.Beans.SetFavoriteResult;
import com.drem.dremboard.utils.AppPreferences;
import com.drem.dremboard.utils.RestApi;
import com.drem.dremboard.view.CustomToast;
import com.drem.dremboard.view.WaitDialog;
import com.drem.dremboard.webservice.Constants;
import com.drem.dremboard.webservice.WebApiCallback;
import com.drem.dremboard.webservice.WebApiInstance;
import com.drem.dremboard.webservice.WebApiInstance.Type;

public class DialogFlagDremcast extends Dialog implements WebApiCallback {
	Activity activity;
	int activity_id;
	Button btnCancel, btnFlag;
	RadioGroup radio_flag_group;
	TextView text_complaint;
	ArrayList<String> flag_value;
	
	int itemIndex;
	
	OnFlagResultCallback callback;
	
	AppPreferences mPrefs;
	WaitDialog waitDialog;

	public interface OnFlagResultCallback {
		void onFinishSetFlag(String strAlert, int index);
	}
	
	public DialogFlagDremcast(Context context, int activity_id, int itemIndex, OnFlagResultCallback callback) {
		super(context);
		this.activity = (Activity) context;
		this.activity_id = activity_id;
		this.itemIndex = itemIndex;
		this.callback = callback;
	}
	
//	public abstract void drem_flaged(int activity_id, String flag_slug);

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.item_flag_drem_modal);
		
		mPrefs = new AppPreferences(this.activity);		
		waitDialog = new WaitDialog(this.activity);

		setCancelable(true);
		btnCancel = (Button) findViewById(R.id.btn_cancel);
		btnFlag = (Button) findViewById(R.id.btn_flag_register);
		radio_flag_group = (RadioGroup) findViewById(R.id.radio_report_group);
		text_complaint = (TextView) findViewById(R.id.text_complaint);
		text_complaint.setMovementMethod(LinkMovementMethod.getInstance());

		flag_value = new ArrayList<String>();

		flag_value.add("annoy");
		flag_value.add("nudity");
		flag_value.add("graphic");
		flag_value.add("attack");
		flag_value.add("improper");
		flag_value.add("spam");

		btnCancel.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				dismiss();
			}
		});
		btnFlag.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				int radioButtonId = radio_flag_group.getCheckedRadioButtonId();
				View radioButton = radio_flag_group.findViewById(radioButtonId);
				int idx = radio_flag_group.indexOfChild(radioButton);
				String flag_slug = flag_value.get(idx);
				setFlag (activity_id, flag_slug);				
			}
		});
	}
	
	private void setFlag(int activityId, String flag_slug)	{
		waitDialog.show();
		
		SetFlagParam param = new SetFlagParam();
		
		param.user_id = mPrefs.getUserId();
		param.activity_id = activityId;	
		param.flag_slug = flag_slug;
		
		WebApiInstance.getInstance().executeAPI(Type.SET_FLAG, param, this);
	}

	private void setFlagResult(Object param, Object obj) {

		waitDialog.dismiss();
		
		if (obj == null) {
			CustomToast.makeCustomToastShort(this.activity, Constants.NETWORK_ERR);
		}

		if (obj != null){
			SetFlagResult resultBean = (SetFlagResult)obj;

			if (resultBean.status.equals("ok")) {				
				if (this.callback != null)
						callback.onFinishSetFlag(resultBean.msg, itemIndex);
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
		case SET_FLAG:
			setFlagResult(parameter, result);			
			break;
		default:
			break;
		}		
	}
}
