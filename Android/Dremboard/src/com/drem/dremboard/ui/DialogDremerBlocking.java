package com.drem.dremboard.ui;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

import com.drem.dremboard.R;
import com.drem.dremboard.entity.Beans.SetBlockingParam;
import com.drem.dremboard.entity.Beans.SetBlockingResult;
import com.drem.dremboard.utils.AppPreferences;
import com.drem.dremboard.view.CustomToast;
import com.drem.dremboard.view.WaitDialog;
import com.drem.dremboard.webservice.Constants;
import com.drem.dremboard.webservice.WebApiCallback;
import com.drem.dremboard.webservice.WebApiInstance;
import com.drem.dremboard.webservice.WebApiInstance.Type;

public class DialogDremerBlocking extends Dialog implements WebApiCallback {
	Activity activity;

	int mDremerId;

	static int BLOCK_TYPE1 = 2;
	static int BLOCK_TYPE2 = 3;
	static int BLOCK_TYPE3 = 5;

	Button btnBlock, btnCancel;	
	CheckBox mChkType1, mChkType2, mChkType3, mChkAll;

	AppPreferences mPrefs;

	WaitDialog waitDialog;

	OnSetBlockResultCallback mResultCallback;
	int itemIndex;

	public DialogDremerBlocking(Context context, Activity activity, int dremer_id, int index, OnSetBlockResultCallback callback) {
		super(context);

		this.activity = activity;
		this.mDremerId = dremer_id;
		this.itemIndex = index;
		this.mResultCallback = callback;
	}

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.dialog_dremer_blocking);

		setCancelable(true);

		mPrefs = new AppPreferences(this.activity);
		waitDialog = new WaitDialog(this.activity);

		mChkType1 = (CheckBox) findViewById(R.id.chkType1);
		mChkType2 = (CheckBox) findViewById(R.id.chkType2);
		mChkType3 = (CheckBox) findViewById(R.id.chkType3);
		
		mChkAll = (CheckBox) findViewById(R.id.chkAll);
		mChkAll.setOnCheckedChangeListener(new OnCheckedChangeListener () {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				// TODO Auto-generated method stub
				
				mChkType1.setChecked(isChecked);
				mChkType2.setChecked(isChecked);
				mChkType3.setChecked(isChecked);
			}});

		btnBlock = (Button) findViewById(R.id.btnBlock);
		btnCancel = (Button) findViewById(R.id.btnCancel);
		btnCancel.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dismiss();
			}
		});
		btnBlock.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				int type = getBlocktype();
				if (type > 1)
					setBlock(type);
				else
					CustomToast.makeCustomToastShort(activity, "Please select a block type.");					
			}
		});
	}

	private int getBlocktype()
	{
		int ret = 1;

		if (mChkType1.isChecked())
			ret = ret * BLOCK_TYPE1;
		if (mChkType2.isChecked())
			ret = ret * BLOCK_TYPE2;
		if (mChkType3.isChecked())
			ret = ret * BLOCK_TYPE3;

		return ret;
	}

	private void setBlock(int block_type)	{

		waitDialog.show();

		SetBlockingParam param = new SetBlockingParam();

		param.user_id = mPrefs.getUserId();
		param.dremer_id = this.mDremerId;
		param.action = "block";
		param.block_type = block_type;

		WebApiInstance.getInstance().executeAPI(Type.SET_BLOCK, param, this);
	}

	private void setBlockResult(Object obj) {

		waitDialog.dismiss();

		if (obj == null) {
			CustomToast.makeCustomToastShort(this.activity, Constants.NETWORK_ERR);
		}

		if (obj != null){
			SetBlockingResult resultBean = (SetBlockingResult)obj;

			if (resultBean.status.equals("ok")) {
				if (this.mResultCallback != null)
					mResultCallback.OnSetBlockResult(resultBean.data.block_type, itemIndex);
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
		case SET_BLOCK:
			setBlockResult(result);			
			break;
		default:
			break;
		}
	}

	public interface OnSetBlockResultCallback {
		void OnSetBlockResult(int block_type, int index);
	}
}

