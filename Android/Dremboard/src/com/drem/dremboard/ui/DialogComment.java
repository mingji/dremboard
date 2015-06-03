package com.drem.dremboard.ui;

import java.util.ArrayList;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.drem.dremboard.R;
import com.drem.dremboard.entity.Beans.SetCommentParam;
import com.drem.dremboard.entity.Beans.SetCommentResult;
import com.drem.dremboard.entity.CommentInfo;
import com.drem.dremboard.entity.DremerInfo;
import com.drem.dremboard.entity.GlobalValue;
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

public class DialogComment extends Dialog implements WebApiCallback {
	Activity activity;
	int activity_id;
	Button mBtnClose;
	Button btnPost, btnCancel;
	EditText txtComment;

	AppPreferences mPrefs;

	WaitDialog waitDialog;

	OnCommentResultCallback mResultCallback;
	int itemIndex;

	ArrayList<CommentInfo> mArrayComment = null;
	CommentAdapter mAdapterComment;
	ListView mListviewComment;


	public DialogComment(Context context, Activity activity, int activity_id, int index, ArrayList<CommentInfo> arrayComment, OnCommentResultCallback callback) {
		super(context);

		this.activity = activity;
		this.activity_id = activity_id;
		this.itemIndex = index;
		this.mResultCallback = callback;
		mArrayComment = new ArrayList<CommentInfo>();
		mArrayComment.addAll(arrayComment);
	}

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE | 
				WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);		

		setContentView(R.layout.dialog_comment);

		setCancelable(true);
		mPrefs = new AppPreferences(this.activity);
		waitDialog = new WaitDialog(this.activity);

		btnPost = (Button) findViewById(R.id.btnPost);
		btnCancel = (Button) findViewById(R.id.btnCancel);

		mBtnClose = (Button) findViewById(R.id.btnClose);
		mBtnClose.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dismiss();
			}
		});

		txtComment = (EditText) findViewById(R.id.txtComment);

		mAdapterComment = new CommentAdapter(this.activity, R.layout.item_diag_comment, mArrayComment);

		mListviewComment = (ListView) findViewById(R.id.lstComment);		
		mListviewComment.setAdapter(mAdapterComment);

		setListViewHeightBasedOnChildren(mListviewComment);

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

	public void setListViewHeightBasedOnChildren(ListView listView) {

		ListAdapter listAdapter = listView.getAdapter();
		if (listAdapter == null) {
			return;
		}

		int totalHeight = 0;
		for (int i = 0, len = listAdapter.getCount(); i < len; i++) {
			View listItem;

			listItem = ((CommentAdapter)listAdapter).getViewForSize(i, null, listView);
			listItem.measure(0, 0); 
			totalHeight += listItem.getMeasuredHeight();
		}

		ViewGroup.LayoutParams params = listView.getLayoutParams();
		params.height = totalHeight
				+ (listView.getDividerHeight() * (listAdapter.getCount() - 1));

		if(params.height > 800)
			params.height = 800;

		listView.setLayoutParams(params);
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
				if (this.mResultCallback != null){
					DremerInfo currentDremer = GlobalValue.getInstance().getCurrentDremer();
					resultBean.data.comment.author_name = currentDremer.display_name;
					mResultCallback.OnCommentResult(resultBean.data.comment, itemIndex);
				}
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
		void OnCommentResult(CommentInfo commentData, int index);
	}

	public class CommentAdapter extends ArrayAdapter<CommentInfo> {
		Activity activity;
		int layoutResourceId;
		ArrayList<CommentInfo> item = new ArrayList<CommentInfo>();

		public CommentAdapter(Activity activity, int layoutId, ArrayList<CommentInfo> items) {
			// TODO Auto-generated constructor stub
			super(activity, layoutId, items);
			item = items;
			this.activity = activity;
			this.layoutResourceId = layoutId;
		}

		public View getViewForSize(final int position, View convertView, ViewGroup parent) {
			CommentHolder holder = null;	

			CommentInfo comment = getItem(position);

			// inflate the view
			if (convertView == null) {
				LayoutInflater inflater = ((Activity) activity).getLayoutInflater();
				convertView = inflater.inflate(R.layout.item_diag_comment, null);
				holder = new CommentHolder();

				holder.imgUser = (WebCircularImgView) convertView.findViewById(R.id.imgCommentAuthor);

				holder.txtName = (TextView) convertView.findViewById(R.id.txtCommentAuthor);
				holder.txtComment = (TextView) convertView.findViewById(R.id.txtComment);

				holder.txtTime = (TextView) convertView.findViewById(R.id.txtTime);
				holder.imgComment = (WebImgView) convertView.findViewById(R.id.imgCommentPic);

				convertView.setTag(holder);
			} 
			else
				holder = (CommentHolder) convertView.getTag();

			holder.txtName.setTag(position);
			holder.txtName.setText(comment.author_name);

			holder.txtComment.setText(comment.description);

			holder.txtTime.setTag(position);
			holder.txtTime.setText(Utility.getRelativeDateStrFromTime(comment.last_modified));

			if (comment.author_avatar != null && !comment.author_avatar.isEmpty())
				ImageLoader.getInstance().displayImage(comment.author_avatar, holder.imgUser, 0, 0);
			else
				holder.imgUser.imageView.setImageResource(R.drawable.empty_man);
			holder.imgUser.setTag(position);

			if (comment.media_guid != null && !comment.media_guid.isEmpty()) {
				if (!comment.media_guid.contains("dremboard.com")) {
					comment.media_guid= "http://dremboard.com" + comment.media_guid;
				}
				ImageLoader.getInstance().displayImage(comment.media_guid, holder.imgComment, 0, 0);
			} else
				holder.imgComment.setVisibility(View.GONE);

			return convertView;
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			CommentHolder holder = null;	

			CommentInfo comment = getItem(position);

			// inflate the view
			if (convertView == null) {
				LayoutInflater inflater = ((Activity) activity).getLayoutInflater();
				convertView = inflater.inflate(R.layout.item_diag_comment, null);
				holder = new CommentHolder();

				holder.imgUser = (WebCircularImgView) convertView.findViewById(R.id.imgCommentAuthor);

				holder.txtName = (TextView) convertView.findViewById(R.id.txtCommentAuthor);
				holder.txtComment = (TextView) convertView.findViewById(R.id.txtComment);

				holder.txtTime = (TextView) convertView.findViewById(R.id.txtTime);
				holder.imgComment = (WebImgView) convertView.findViewById(R.id.imgCommentPic);

				convertView.setTag(holder);
			} 
			else
				holder = (CommentHolder) convertView.getTag();

			holder.txtName.setTag(position);
			holder.txtName.setText(comment.author_name);

			holder.txtComment.setText(comment.description);

			holder.txtTime.setTag(position);
			holder.txtTime.setText(Utility.getRelativeDateStrFromTime(comment.last_modified));

			if (comment.author_avatar != null && !comment.author_avatar.isEmpty())
				ImageLoader.getInstance().displayImage(comment.author_avatar, holder.imgUser, 0, 0);
			else
				holder.imgUser.imageView.setImageResource(R.drawable.empty_man);
			holder.imgUser.setTag(position);

			if (comment.media_guid != null && !comment.media_guid.isEmpty()) {
				if (!comment.media_guid.contains("dremboard.com")) {
					comment.media_guid= "http://dremboard.com" + comment.media_guid;
				}
				ImageLoader.getInstance().displayImage(comment.media_guid, holder.imgComment, 0, 0);
			} else
				holder.imgComment.setVisibility(View.GONE);

			return convertView;
		}

		public class CommentHolder {
			WebCircularImgView imgUser;
			TextView txtName;
			TextView txtComment;
			TextView txtTime;
			WebImgView imgComment;
		}

	}	
}

