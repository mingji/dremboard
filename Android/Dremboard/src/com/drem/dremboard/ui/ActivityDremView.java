package com.drem.dremboard.ui;


import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.drem.dremboard.R;
import com.drem.dremboard.entity.DremActivityInfo;
import com.drem.dremboard.entity.DremInfo;
import com.drem.dremboard.entity.CommentInfo;
import com.drem.dremboard.entity.DremerInfo;
import com.drem.dremboard.entity.GlobalValue;
import com.drem.dremboard.entity.Beans.SetLikeParam;
import com.drem.dremboard.entity.Beans.SetLikeResult;
import com.drem.dremboard.ui.DialogFlagDrem.OnFlagResultCallback;
import com.drem.dremboard.utils.AppPreferences;
import com.drem.dremboard.utils.ImageLoader;
import com.drem.dremboard.view.CustomToast;
import com.drem.dremboard.view.WaitDialog;
import com.drem.dremboard.view.WebImgView;
import com.drem.dremboard.webservice.Constants;
import com.drem.dremboard.webservice.WebApiCallback;
import com.drem.dremboard.webservice.WebApiInstance;
import com.drem.dremboard.webservice.WebApiInstance.Type;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerFragment;

public class ActivityDremView extends Activity implements OnClickListener, WebApiCallback, OnFlagResultCallback {

	Button mBtnClose;
	Button mBtnLike;
	Button mBtnFlag;
	Button mBtnComment;
	Button mBtnShare;
	DremInfo mDrem;

	WebImgView mImgDrem;
	
	AppPreferences mPrefs;
	WaitDialog waitDialog;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setTheme(R.style.DialogHoloLightTheme);
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_drem_view);
		
		mPrefs = new AppPreferences(this);		
		waitDialog = new WaitDialog(this);
		
		mDrem = GlobalValue.getInstance().getCurrentDrem();

		initView();
	}

	@Override
	public void onResume(){
		super.onResume();
		// put your code here...
	}

	private void initView()
	{
		mBtnLike = (Button) findViewById(R.id.btnLike);
		mBtnLike.setText(mDrem.like);
		mBtnLike.setOnClickListener(this);

		mBtnFlag = (Button) findViewById(R.id.btnFlag);
		mBtnFlag.setOnClickListener(this);

		mBtnComment = (Button) findViewById(R.id.btnComment);
		mBtnComment.setOnClickListener(this);

		mBtnShare = (Button) findViewById(R.id.btnShare);
		mBtnShare.setOnClickListener(this);

		mImgDrem = (WebImgView) findViewById(R.id.imgPic);
		if (mDrem.guid != null && !mDrem.guid.isEmpty())
			ImageLoader.getInstance().displayImage(mDrem.guid, mImgDrem, 0, 0);
		else
			mImgDrem.imageView.setImageResource(R.drawable.sample_drem);

		mBtnClose = (Button) findViewById(R.id.btnClose);
		mBtnClose.setOnClickListener(this);

		if(mDrem.comment_list.size() > 0) {
			mBtnComment.setText("Comment ("+String.valueOf(mDrem.comment_list.size())+")");
		} else {
			mBtnComment.setText("Comment");
		}
		
	}
	
	private void showFlagDialog(int activity_id, int index)
	{
		Dialog dialog = new DialogFlagDrem(this, activity_id, index, this);
		dialog.show();
	}

	private void setLike(DremInfo dremItem)	{
		if (dremItem == null)
			return;
		
		waitDialog.show();
		
		SetLikeParam param = new SetLikeParam();
		
		String strLike = "Like";
		if (dremItem.like.contains("Unlike"))
			strLike = "Unlike";
		
		param.user_id = mPrefs.getUserId();
		param.activity_id = dremItem.activity_id;	
		param.like_str = strLike;
		
		WebApiInstance.getInstance().executeAPI(Type.SET_LIKE, param, this);
	}

	private void setLikeResult(Object param, Object obj) {

		waitDialog.dismiss();
		
		if (obj == null) {
			CustomToast.makeCustomToastShort(this, Constants.NETWORK_ERR);
		}

		if (obj != null){
			SetLikeResult resultBean = (SetLikeResult)obj;

			if (resultBean.status.equals("ok")) {
				
				if (resultBean.data.like_str != null) {
					mBtnLike.setText(resultBean.data.like_str);
				}
			} else {
				CustomToast.makeCustomToastShort(this, resultBean.msg);
			}
		}
	}

	private void setComment(DremInfo dremItem)	{
		if (dremItem == null)
			return;
		
		DialogComment commentDiag = new DialogComment(this, this, dremItem.activity_id, -1, dremItem.comment_list, null);
		commentDiag.show();
	}
	
	private void showShareDialog(int activity_id)
	{
		Dialog dialog = new DialogShare(this, this, activity_id);
		dialog.show();
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		int id = v.getId();

		switch (id)
		{
		case R.id.btnClose:
			finish();
			overridePendingTransition(R.anim.in_left_right, R.anim.out_left_right);
			break;
		case R.id.btnLike:
			setLike(mDrem);
			break;
		case R.id.btnFlag:
			showFlagDialog (mDrem.activity_id, -1);
			break;
		case R.id.btnComment:
			setComment(mDrem);
			break;
		case R.id.btnShare:
			showShareDialog(mDrem.activity_id);
			break;
		default:
			break;
		}		
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		boolean bool;
		if (keyCode != KeyEvent.KEYCODE_BACK) {
			bool = super.onKeyDown(keyCode, event);
		} else {
			finish();
			overridePendingTransition(R.anim.in_left_right, R.anim.out_left_right);			
			bool = true;
		}
		return bool;
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
		case SET_LIKE:
			setLikeResult(parameter, result);
			break;
		default:
			break;
		}		
	}
	
	@Override
	public void onFinishSetFlag(String strAlert, int index) {
		// TODO Auto-generated method stub
		CustomToast.makeCustomToastLong(this, strAlert);
	}
}
