package com.drem.dremboard.ui;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.drem.dremboard.R;
import com.drem.dremboard.entity.DremboardInfo;
import com.drem.dremboard.entity.GlobalValue;
import com.drem.dremboard.entity.Beans.DeleteDremboardResult;
import com.drem.dremboard.entity.Beans.EditDremboardData;
import com.drem.dremboard.entity.Beans.EditDremboardResult;
import com.drem.dremboard.utils.AppPreferences;
import com.drem.dremboard.utils.RestApi;
import com.drem.dremboard.view.CustomToast;
import com.drem.dremboard.view.WaitDialog;
import com.drem.dremboard.webservice.Constants;
import com.drem.dremboard.webservice.WebApiCallback;
import com.drem.dremboard.webservice.WebApiInstance;
import com.drem.dremboard.webservice.WebApiInstance.Type;

public class DialogDremboardEdit extends Dialog implements OnClickListener, WebApiCallback  {

	Activity activity;
	EditText mTxtTitle, mTxtDescription;
	Button mBtnClose, mBtnSave;

	WaitDialog waitDialog;
	DremboardInfo mDremboard;
	private AppPreferences mPrefs;

	public DialogDremboardEdit(Context context, Activity activity) {
		super(context);
		// TODO Auto-generated constructor stub
		this.activity = activity;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE | 
				WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

		setContentView(R.layout.dialog_dremboard_edit);

		setCancelable(true);
					
		mTxtTitle = (EditText) findViewById(R.id.txtDremboardEditTitle);
		mTxtDescription = (EditText) findViewById(R.id.txtDremboardEditDescription);
		
		mBtnClose = (Button) findViewById(R.id.btnClose);
		mBtnClose.setOnClickListener(this);
		
		mBtnSave = (Button) findViewById(R.id.btnSaveChanges);
		mBtnSave.setOnClickListener(this);
		
		waitDialog = new WaitDialog(this.activity);
		mPrefs = new AppPreferences(this.activity);

		mDremboard = GlobalValue.getInstance().getCurrentDremboard();
		
		mTxtTitle.setText(mDremboard.media_title);
		mTxtDescription.setText(mDremboard.media_description);
	}

	private void saveChangesDremboard()
	{
		if (mTxtTitle.getText().toString().isEmpty())
		{
			CustomToast.makeCustomToastShort(this.activity, "Please input a title.");
			return;
		}
		
		EditDremboardData param = new EditDremboardData();
		
		param.user_id = mPrefs.getUserId();
		param.title = mTxtTitle.getText().toString();
		param.description = mTxtDescription.getText().toString();
		param.dremboard_id = mDremboard.id;

		WebApiInstance.getInstance().executeAPI(Type.EDIT_DREMBOARD, param, this);

		waitDialog.show();
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
			case R.id.btnClose:
				this.cancel();
				this.activity.overridePendingTransition(R.anim.in_left_right, R.anim.out_left_right);
				break;
				
			case R.id.btnSaveChanges:
				saveChangesDremboard();
				break;
			
			default:
				break;
		}
	}

	private void editDremboardResult(Object obj) {
		waitDialog.dismiss();

		if (obj == null) {
			CustomToast.makeCustomToastShort(this.activity, Constants.NETWORK_ERR);
		}

		if (obj != null){
			EditDremboardResult resultBean = (EditDremboardResult)obj;
			CustomToast.makeCustomToastShort(this.activity, resultBean.msg);
			
			if (resultBean.status.equals("ok")) {
				mDremboard.media_title = mTxtTitle.getText().toString();
				mDremboard.media_description = mTxtDescription.getText().toString();
				
				ActivityBoardDrems.instance.mTxtTitle.setText(mDremboard.media_title);
				
				GlobalValue.getInstance().setCurrentDremboard(mDremboard);
			}
		}
		
		this.cancel();
		this.activity.overridePendingTransition(R.anim.in_left_right, R.anim.out_left_right);
	}
	
	@Override
	public void onPreProcessing(Type type, Object parameter) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onResultProcessing(Type type, Object parameter, Object result) {
		switch (type)
		{
			case EDIT_DREMBOARD:
				editDremboardResult(result);
				break;
				
			default:
				break;
		}
	}
}
