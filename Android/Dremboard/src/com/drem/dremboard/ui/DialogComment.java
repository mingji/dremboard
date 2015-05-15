package com.drem.dremboard.ui;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import com.drem.dremboard.R;
import com.drem.dremboard.entity.Beans.CommentData;
import com.drem.dremboard.entity.Beans.SetCommentParam;
import com.drem.dremboard.entity.Beans.SetCommentResult;
import com.drem.dremboard.utils.AppPreferences;
import com.drem.dremboard.view.CustomToast;
import com.drem.dremboard.view.WaitDialog;
import com.drem.dremboard.webservice.Constants;
import com.drem.dremboard.webservice.WebApiCallback;
import com.drem.dremboard.webservice.WebApiInstance;
import com.drem.dremboard.webservice.WebApiInstance.Type;

public class DialogComment extends Dialog implements WebApiCallback {
	Activity activity;
	int activity_id;
	Button btnPost, btnCancel;
	EditText txtComment;

	AppPreferences mPrefs;
	
	WaitDialog waitDialog;
	
	OnCommentResultCallback mResultCallback;
	int itemIndex;

	public DialogComment(Context context, Activity activity, int activity_id, int index, OnCommentResultCallback callback) {
		super(context);

		this.activity = activity;
		this.activity_id = activity_id;
		this.itemIndex = index;
		this.mResultCallback = callback;
	}

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.dialog_comment);

		setCancelable(true);

		mPrefs = new AppPreferences(this.activity);
		waitDialog = new WaitDialog(this.activity);

		btnPost = (Button) findViewById(R.id.btnPost);
		btnCancel = (Button) findViewById(R.id.btnCancel);
		txtComment = (EditText) findViewById(R.id.txtComment);

		btnCancel.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dismiss();
			}
		});
		btnPost.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String comment = txtComment.getText().toString();
				setComment(comment);				
			}
		});
	}

	private void setComment(String comment)	{
		
		waitDialog.show();
		
		SetCommentParam param = new SetCommentParam();

		param.user_id = mPrefs.getUserId();
		param.activity_id = this.activity_id;
		param.comment = comment;
		param.photo = "tmp.png";

		WebApiInstance.getInstance().executeAPI(Type.SET_COMMENT, param, this);
	}

	private void setCommentResult(Object obj) {

		waitDialog.dismiss();
		
		if (obj == null) {
			CustomToast.makeCustomToastShort(this.activity, Constants.NETWORK_ERR);
		}

		if (obj != null){
			SetCommentResult resultBean = (SetCommentResult)obj;

			if (resultBean.status.equals("ok")) {
				if (this.mResultCallback != null)
					mResultCallback.OnCommentResult(resultBean.data.comment, itemIndex);
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
		case SET_COMMENT:
			setCommentResult(result);			
			break;
		default:
			break;
		}
	}

	public interface OnCommentResultCallback {
		void OnCommentResult(CommentData commentData, int index);
	}
}

