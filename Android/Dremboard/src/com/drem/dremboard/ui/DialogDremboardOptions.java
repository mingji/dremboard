package com.drem.dremboard.ui;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.CalendarContract.Instances;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.drem.dremboard.R;
import com.drem.dremboard.entity.DremInfo;
import com.drem.dremboard.entity.DremboardInfo;
import com.drem.dremboard.entity.GlobalValue;
import com.drem.dremboard.entity.Beans.DeleteDremboardData;
import com.drem.dremboard.entity.Beans.DeleteDremboardResult;
import com.drem.dremboard.utils.AppPreferences;
import com.drem.dremboard.view.CustomToast;
import com.drem.dremboard.view.WaitDialog;
import com.drem.dremboard.webservice.Constants;
import com.drem.dremboard.webservice.WebApiCallback;
import com.drem.dremboard.webservice.WebApiInstance;
import com.drem.dremboard.webservice.WebApiInstance.Type;

public class DialogDremboardOptions extends Dialog implements OnClickListener, WebApiCallback  {

	Activity activity;
	DremboardInfo mDremboard;
	WaitDialog waitDialog;
	private AppPreferences mPrefs;
	ArrayList<DremInfo> mArrayDrems;
	public static DialogDremboardOptions instance;
	
	public DialogDremboardOptions(Context context, Activity activity, ArrayList<DremInfo> ArrayDrems) {
		super(context);
		// TODO Auto-generated constructor stub
		this.activity = activity;
		mArrayDrems = ArrayDrems;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE | 
				WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

		setContentView(R.layout.dialog_dremboard_options);

		instance = this;
		setCancelable(true);	
		
		waitDialog = new WaitDialog(this.activity);
		mPrefs = new AppPreferences(this.activity);

		Button mBtnClose = (Button)findViewById(R.id.btnClose);
		TextView mTxtDremboardEdit = (TextView) findViewById(R.id.txt_dremboard_edit);
		TextView mTxtDremboardManage = (TextView) findViewById(R.id.txt_dremboard_manage);
		TextView mTxtDremboardDelete = (TextView) findViewById(R.id.txt_dremboard_delete);
		TextView mTxtDremboardMerge = (TextView) findViewById(R.id.txt_dremboard_merge);

		mBtnClose.setOnClickListener(this);
		mTxtDremboardEdit.setOnClickListener(this);
		mTxtDremboardManage.setOnClickListener(this);
		mTxtDremboardDelete.setOnClickListener(this);
		mTxtDremboardMerge.setOnClickListener(this);
		
		mDremboard = GlobalValue.getInstance().getCurrentDremboard();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
			case R.id.btnClose:
				this.cancel();
				this.activity.overridePendingTransition(R.anim.in_left_right, R.anim.out_left_right);
				break;
				
			case R.id.txt_dremboard_edit:
				onViewDremboardEdit();
				break;
				
			case R.id.txt_dremboard_manage:
				this.cancel();
				onViewDremboardManage();
				break;
				
			case R.id.txt_dremboard_delete:
				deleteDremboard();
				break;
				
			case R.id.txt_dremboard_merge:
				onViewDremboardMerge();
				break;
				
			default:
				break;
		}
	}

	private void onViewDremboardEdit() {
		DialogDremboardEdit dremboardEditDiag = new DialogDremboardEdit(this.activity, this.activity);
		dremboardEditDiag.show();
		this.activity.overridePendingTransition(R.anim.in_left_right, R.anim.out_left_right);

	}
	
	private void onViewDremboardManage() {
		Intent intent = new Intent(activity, ActivityBoardManageDrems.class);
		activity.startActivity(intent);
		activity.overridePendingTransition(R.anim.in_right_left, R.anim.out_right_left);
	}
	
	private void deleteDremboard() {
		TextView title = new TextView(DialogDremboardOptions.this.activity);
		title.setText(R.string.text_deletedremboard);
		title.setTextSize(18);
		title.setPadding(0, 40, 0, 40);
		title.setGravity(Gravity.CENTER);
						
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
				this.activity);
		alertDialogBuilder.setCustomTitle(title);
		alertDialogBuilder
			.setMessage("Are you sure to delete" + "\n" + "a dremboard?")
			.setCancelable(false)
			.setPositiveButton("Delete",new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog,int id) {
					DeleteDremboardData param = new DeleteDremboardData();
					param.user_id = mPrefs.getUserId();
					param.dremboard_id = mDremboard.id;

					WebApiInstance.getInstance().executeAPI(Type.DELETE_DREMBOARD, param, DialogDremboardOptions.this);

					waitDialog.show();
				}
			  })
			.setNegativeButton("Cancel",new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog,int id) {
					dialog.cancel();
				}
			});

		// create alert dialog
		AlertDialog alertDialog = alertDialogBuilder.create();
		// show it
		alertDialog.show();
		TextView messageText = (TextView)alertDialog.findViewById(android.R.id.message);
        messageText.setGravity(Gravity.CENTER);
	}
	
	private void onViewDremboardMerge() {
		DialogDremboardMerge dremboardMergeDiag = new DialogDremboardMerge(this.activity, this.activity);
		dremboardMergeDiag.show();
		this.activity.overridePendingTransition(R.anim.in_left_right, R.anim.out_left_right);

	}

	private void deleteDremboardResult(Object obj) {
		waitDialog.dismiss();

		if (obj == null) {
			CustomToast.makeCustomToastShort(this.activity, Constants.NETWORK_ERR);
		}

		if (obj != null){
			DeleteDremboardResult resultBean = (DeleteDremboardResult)obj;
			CustomToast.makeCustomToastShort(this.activity, resultBean.msg);
		}
		
		this.cancel();
		Intent intent = new Intent(this.activity, FragmentDremboards.class);
		this.activity.setResult(this.activity.RESULT_OK, intent);
		this.activity.finish();
	}
	
	@Override
	public void onResultProcessing(Type type, Object parameter, Object result) {
		switch (type)
		{
			case DELETE_DREMBOARD:
				deleteDremboardResult(result);
				break;
				
			default:
				break;
		}
	}

	@Override
	public void onPreProcessing(Type type, Object parameter) {
		// TODO Auto-generated method stub
		
	}
}
