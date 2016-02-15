package com.drem.dremboard.ui;
import java.util.ArrayList;

import com.drem.dremboard.utils.ImageLoader;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.app.Dialog;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.Window;
import android.view.Gravity;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.drem.dremboard.R;
import com.drem.dremboard.entity.DremActivityInfo;
import com.drem.dremboard.entity.DremInfo;
import com.drem.dremboard.entity.CommentInfo;
import com.drem.dremboard.entity.DremerInfo;
import com.drem.dremboard.entity.GlobalValue;
import com.drem.dremboard.entity.Beans.DeleteActivityDremData;
import com.drem.dremboard.entity.Beans.DeleteActivityDremResult;
import com.drem.dremboard.entity.Beans.GetActivitiesParam;
import com.drem.dremboard.entity.Beans.SetLikeParam;
import com.drem.dremboard.entity.Beans.SetLikeResult;
import com.drem.dremboard.ui.DialogFlagDrem.OnFlagResultCallback;
import com.drem.dremboard.ui.ActivityComment.OnCommentResultCallback;
import com.drem.dremboard.ui.ActivityComment.OnDelCommentResultCallback;
import com.drem.dremboard.ui.ActivityComment.OnEditCommentResultCallback;
import com.drem.dremboard.utils.AppPreferences;
import com.drem.dremboard.utils.ImageLoader;
import com.drem.dremboard.utils.Utility;
import com.drem.dremboard.view.CustomToast;
import com.drem.dremboard.view.WaitDialog;
import com.drem.dremboard.view.WebCircularImgView;
import com.drem.dremboard.view.WebImgView;
import com.drem.dremboard.webservice.Constants;
import com.drem.dremboard.webservice.WebApiCallback;
import com.drem.dremboard.webservice.WebApiInstance;
import com.drem.dremboard.webservice.WebApiInstance.Type;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerFragment;

public class ActivityDremView extends Activity implements OnClickListener, WebApiCallback, 
	OnCommentResultCallback, OnDelCommentResultCallback, OnEditCommentResultCallback, OnFlagResultCallback{

	Button mBtnClose;
	Button mBtnLike;
	Button mBtnFlag;
	Button mBtnComment;
	Button mBtnShare;
	Button mBtnEdit;
	Button mBtnDelete;
	
	DremInfo mDrem;

	WebImgView mImgDrem;

	WebCircularImgView imgAuthor;
	TextView txtAuthor;
	TextView txtAuthorDate;
	TextView txtDescription;

	AppPreferences mPrefs;
	WaitDialog waitDialog;
	
	public static ActivityDremView instance;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setTheme(R.style.DialogHoloLightTheme);
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_drem_view);
		
		instance = this;
		
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
		final Context context = this;
		
		imgAuthor = (WebCircularImgView) findViewById(R.id.img_drem_author);
		txtAuthor = (TextView) findViewById(R.id.txtDremAuthor);
		txtAuthorDate = (TextView) findViewById(R.id.txtDremDate);
		txtDescription = (TextView) findViewById(R.id.txtDremDescription);
		txtDescription.setMovementMethod(LinkMovementMethod.getInstance());

		if (mDrem.author_avatar != null && !mDrem.author_avatar.isEmpty())
			ImageLoader.getInstance().displayImage(mDrem.author_avatar, imgAuthor, 0, 0);
		else
			imgAuthor.imageView.setImageResource(R.drawable.empty_pic);
		
		txtAuthor.setText(mDrem.author_name);
		txtAuthorDate.setText(Utility.getRelativeDateStrFromTime(mDrem.last_modified));
		txtDescription.setText(mDrem.description);
		
		mBtnLike = (Button) findViewById(R.id.btnLike);
		mBtnLike.setText(mDrem.like);
		mBtnLike.setOnClickListener(this);

		mBtnFlag = (Button) findViewById(R.id.btnFlag);
		mBtnFlag.setOnClickListener(this);

		mBtnComment = (Button) findViewById(R.id.btnComment);
		mBtnComment.setOnClickListener(this);

		mBtnShare = (Button) findViewById(R.id.btnShare);
		mBtnShare.setOnClickListener(this);
		
		mBtnEdit = (Button) findViewById(R.id.btnEdit);
		mBtnEdit.setOnClickListener(this);
		
		mBtnDelete = (Button) findViewById(R.id.btnDelete);
		mBtnDelete.setOnClickListener(this);

		mImgDrem = (WebImgView) findViewById(R.id.imgPic);
		if (mDrem.guid != null && !mDrem.guid.isEmpty())
			ImageLoader.getInstance().displayImage(mDrem.guid, mImgDrem, 0, 0);
		else
			mImgDrem.setVisibility(View.INVISIBLE);

		mImgDrem.setLongClickable(true);
		mImgDrem.setOnLongClickListener(new OnLongClickListener() {
			@Override
			public boolean onLongClick(View v) {
				TextView title = new TextView(ActivityDremView.this);
				title.setText(R.string.text_addtodremboard);
				title.setTextSize(18);
				title.setPadding(0, 40, 0, 40);
				title.setGravity(Gravity.CENTER);
								
				AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
					context);
				alertDialogBuilder.setCustomTitle(title);
				alertDialogBuilder
					.setMessage("Are you sure to add a drēm to your drēmboard?")
					.setCancelable(false)
					.setPositiveButton("Ok",new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,int id) {
							Intent intent = new Intent();
							intent.setClass(ActivityDremView.this, ActivityAddDremToDremboard.class);
							startActivity(intent);
							overridePendingTransition(R.anim.in_right_left, R.anim.out_right_left);
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
				return true;
			}
		});

		mBtnClose = (Button) findViewById(R.id.btnClose);
		mBtnClose.setOnClickListener(this);

		if(mDrem.comment_list != null && mDrem.comment_list.size() > 0) {
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

	private void deleteActivityDremResult(Object param, Object obj) {

		waitDialog.dismiss();
		
		if (obj == null) {
			CustomToast.makeCustomToastShort(this, Constants.NETWORK_ERR);
		}

		if (obj != null){
			DeleteActivityDremResult resultBean = (DeleteActivityDremResult)obj;
			CustomToast.makeCustomToastShort(this, resultBean.msg);

			this.finish();
			
			if (FragmentDrems.instance != null)
			{
				FragmentDrems.instance.resetOptions();
				FragmentDrems.instance.removeAllDrems();
				FragmentDrems.instance.loadMoreDrems();
			}
		}
	}
	
	private void setComment(DremInfo dremItem)	{
		if (dremItem == null)
			return;
		
		if (dremItem.comment_list == null)
			dremItem.comment_list = new ArrayList<CommentInfo>();
		
		ActivityComment.mResultCallback = null;
		ActivityComment.mEditCommentCallback = null;
		ActivityComment.mDelCommentCallback = null;
		ActivityComment.mCommentList = null;

		ActivityComment.mResultCallback = this;
		ActivityComment.mEditCommentCallback = this;
		ActivityComment.mDelCommentCallback = this;
		ActivityComment.mCommentList = dremItem.comment_list;
				
		Intent intent = new Intent(this, ActivityComment.class);
		intent.putExtra("activity_id", dremItem.activity_id);
		intent.putExtra("index", 0);
		
		startActivity(intent);
	}
	
	private void showShareDialog(int activity_id, String guid)
	{
		Dialog dialog = new DialogShare(this, this, activity_id, guid);
		dialog.show();
	}
	
	private void showEditDialog (int activity_id)
	{
		DremActivityInfo activityItem = new DremActivityInfo();
		
		activityItem.activity_id = activity_id;
		activityItem.author_avatar = mDrem.author_avatar;
		activityItem.description = mDrem.description;
		activityItem.category = mDrem.category;
		
		Dialog dialog = new DialogActivityDremEdit(this, this, activityItem);
		
		dialog.show();

	}
	
	private void DeleteActivityDream(DremActivityInfo activityItem)
	{
		DeleteActivityDremData param = new DeleteActivityDremData();

		param.user_id = mPrefs.getUserId();
		param.activity_id = activityItem.activity_id;
		
		WebApiInstance.getInstance().executeAPI(Type.DELETE_ACTIVITY, param, this);

		waitDialog.show();

	}
	
	private void showDeleteDialog (int activity_id)
	{
		final DremActivityInfo activityItem = new DremActivityInfo();
		
		activityItem.activity_id = activity_id;
		activityItem.author_avatar = mDrem.author_avatar;
		activityItem.description = mDrem.description;
		activityItem.category = mDrem.category;
		
		TextView title = new TextView(this);
		title.setText("Delete Drēm");
		title.setTextSize(18);
		title.setPadding(0, 40, 0, 40);
		title.setGravity(Gravity.CENTER);

		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
				this);
			alertDialogBuilder.setCustomTitle(title);
			alertDialogBuilder
				.setMessage("Are you sure to delete" + "\n" + "drēm?")
				.setCancelable(false)
				.setPositiveButton("Ok",new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,int id) {
						DeleteActivityDream(activityItem);
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
			showShareDialog(mDrem.activity_id, mDrem.guid);
			break;
			
		case R.id.btnEdit:
			showEditDialog(mDrem.activity_id);
			break;
			
		case R.id.btnDelete:
			showDeleteDialog(mDrem.activity_id);
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
			
		case DELETE_ACTIVITY:
			deleteActivityDremResult(parameter, result);
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
	
	private void setCommentResult (CommentInfo commentData, int index)
	{
		if (mDrem == null || commentData == null)
			return;
		
		if (mDrem.comment_list == null)
			mDrem.comment_list = new ArrayList<CommentInfo>();
		
		mDrem.comment_list.add(commentData);
		
		if(mDrem.comment_list.size() > 0) {
			mBtnComment.setText("Comment ("+String.valueOf(mDrem.comment_list.size())+")");
		} else {
			mBtnComment.setText("Comment");
		}
	}
	
	private void delCommentResult (int activity_index, int index)
	{
		if (mDrem == null || mDrem.comment_list == null)
			return;
		
		mDrem.comment_list.remove(index);
		
		if(mDrem.comment_list.size() > 0) {
			mBtnComment.setText("Comment ("+String.valueOf(mDrem.comment_list.size())+")");
		} else {
			mBtnComment.setText("Comment");
		}
	}
	
	private void editCommentResult (int activity_index, CommentInfo commentData, int index)
	{
		if (mDrem == null || commentData == null)
			return;
		
		if (mDrem.comment_list == null)
			mDrem.comment_list = new ArrayList<CommentInfo>();
		
		CommentInfo changeData = mDrem.comment_list.get(index);
		changeData.description = commentData.description;
		
		if(mDrem.comment_list.size() > 0) {
			mBtnComment.setText("Comment ("+String.valueOf(mDrem.comment_list.size())+")");
		} else {
			mBtnComment.setText("Comment");
		}
	}
	
	@Override
	public void OnCommentResult(
			CommentInfo commentData, int index) {
		// TODO Auto-generated method stub
		setCommentResult (commentData, index);
	}
	
	@Override
	public void OnDelCommentResult(int activity_index, int index) {
		// TODO Auto-generated method stub
		delCommentResult (activity_index, index);
	}
	
	@Override
	public void OnEditCommentResult(int activity_index, CommentInfo commentData, int index) {
		// TODO Auto-generated method stub
		editCommentResult (activity_index, commentData, index);
	}
	
}
