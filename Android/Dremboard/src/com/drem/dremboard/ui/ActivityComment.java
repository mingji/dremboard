package com.drem.dremboard.ui;

import java.util.ArrayList;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Html;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.drem.dremboard.R;
import com.drem.dremboard.entity.Beans.DeleteActivityDremData;
import com.drem.dremboard.entity.Beans.DeleteActivityDremResult;
import com.drem.dremboard.entity.Beans.EditCommentParam;
import com.drem.dremboard.entity.Beans.EditCommentResult;
import com.drem.dremboard.entity.Beans.SetCommentParam;
import com.drem.dremboard.entity.Beans.SetCommentResult;
import com.drem.dremboard.entity.CommentInfo;
import com.drem.dremboard.entity.DremerInfo;
import com.drem.dremboard.entity.GlobalValue;
import com.drem.dremboard.utils.AppPreferences;
import com.drem.dremboard.utils.ImageLoader;
import com.drem.dremboard.utils.MyDialog;
import com.drem.dremboard.utils.StringUtil;
import com.drem.dremboard.utils.Utility;
import com.drem.dremboard.view.CustomToast;
import com.drem.dremboard.view.WaitDialog;
import com.drem.dremboard.view.WebCircularImgView;
import com.drem.dremboard.view.WebImgView;
import com.drem.dremboard.webservice.Constants;
import com.drem.dremboard.webservice.WebApiCallback;
import com.drem.dremboard.webservice.WebApiInstance;
import com.drem.dremboard.webservice.WebApiInstance.Type;

public class ActivityComment extends Activity implements WebApiCallback {
	int activity_id;
	ImageView mImgPost;
	Button mBtnClose, btnPost;
	EditText txtComment;

	AppPreferences mPrefs;

	WaitDialog waitDialog;
	
	public static ActivityComment instance = null;

	public static OnCommentResultCallback mResultCallback;
	public static OnDelCommentResultCallback mDelCommentCallback;
	public static OnEditCommentResultCallback mEditCommentCallback;
	public static ArrayList<CommentInfo> mCommentList;
	
	int itemIndex;
	int mDeleteIndex, mEditIndex;
	CommentAdapter mAdapterComment;
	ListView mListviewComment;
	CommentInfo mEdtCommentItem;
	ArrayList<CommentInfo> mArrayComment = null;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		setContentView(R.layout.activity_comment);

		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE | 
				WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);		

		getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

		instance = this;
		mPrefs = new AppPreferences(this);
		waitDialog = new WaitDialog(this);

		mArrayComment = new ArrayList<CommentInfo>();
		mArrayComment.addAll(mCommentList);
		activity_id = getIntent().getExtras().getInt("activity_id");
		itemIndex = getIntent().getExtras().getInt("index");
		
		btnPost = (Button) findViewById(R.id.btnPost);
		btnPost.setText("Post");

		mBtnClose = (Button) findViewById(R.id.btnClose);
		mBtnClose.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});

		txtComment = (EditText) findViewById(R.id.txtComment);

		mImgPost = (ImageView) findViewById(R.id.imgPost);
		mImgPost.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				txtComment.setText("");
				btnPost.setText("Post");
			}
		});
		
		mAdapterComment = new CommentAdapter(this, R.layout.item_diag_comment, mArrayComment);

		mListviewComment = (ListView) findViewById(R.id.lstComment);		
		mListviewComment.setAdapter(mAdapterComment);
		
		setListViewHeightBasedOnChildren(mListviewComment);

		btnPost.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String comment = txtComment.getText().toString();
				
				if (btnPost.getText().equals("Post"))
					setComment(comment);
				else if (btnPost.getText().equals("Edit"))
				{
					if (mEdtCommentItem.author_id != Integer.parseInt(mPrefs.getUserId()))
					{
						CustomToast.makeCustomToastShort(ActivityComment.this, "You can't edit the comment.");
					}
					else 
					{
						mEdtCommentItem.description = comment;
						editComment(comment, mEdtCommentItem.activity_id);
					}
				}
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
//		params.height = totalHeight
//				+ (listView.getDividerHeight() * (listAdapter.getCount() - 1));
//
//		int height = this.activity.getWindowManager().getDefaultDisplay().getHeight();
//		
//		if (params.height > height / 7 * 4)
//			params.height = height / 7 * 4;
			
//		if(params.height > 800)
//			params.height = 800;

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
	
	private void editComment(String comment, int activity_id)	{
		waitDialog.show();
		
		EditCommentParam param = new EditCommentParam();

		param.user_id = mPrefs.getUserId();
		param.activity_id = activity_id;
		param.comment = comment;
		param.photo = "tmp.png";
		
		WebApiInstance.getInstance().executeAPI(Type.EDIT_COMMENT, param, this);
	}
	
	private void deleteComment(CommentInfo comment)	{
		DeleteActivityDremData param = new DeleteActivityDremData();

		param.user_id = mPrefs.getUserId();
		param.activity_id = comment.activity_id;
		
		WebApiInstance.getInstance().executeAPI(Type.DELETE_ACTIVITY, param, this);

		waitDialog.show();
	}
	

	private void setCommentResult(Object obj) {

		waitDialog.dismiss();

		if (obj == null) {
			CustomToast.makeCustomToastShort(this, Constants.NETWORK_ERR);
		}

		if (obj != null){
			SetCommentResult resultBean = (SetCommentResult)obj;

			if (resultBean.status.equals("ok")) {
				if (this.mResultCallback != null){
					DremerInfo currentDremer = GlobalValue.getInstance().getCurrentDremer();
					resultBean.data.comment.author_name = currentDremer.display_name;
					mResultCallback.OnCommentResult(resultBean.data.comment, itemIndex);

					mArrayComment.add(resultBean.data.comment);
					mAdapterComment = new CommentAdapter(this, R.layout.item_diag_comment, mArrayComment);

					mListviewComment = (ListView) findViewById(R.id.lstComment);		
					mListviewComment.setAdapter(mAdapterComment);

					setListViewHeightBasedOnChildren(mListviewComment);
					
					mAdapterComment.notifyDataSetChanged();
				}
			} else {
				CustomToast.makeCustomToastShort(this, resultBean.msg);
			}
		}
		
		txtComment.setText("");
	}

	private void deleteCommentResult(Object obj) {

		waitDialog.dismiss();
		
		if (obj == null) {
			CustomToast.makeCustomToastShort(this, Constants.NETWORK_ERR);
		}

		if (obj != null){
			DeleteActivityDremResult resultBean = (DeleteActivityDremResult)obj;
			if (resultBean.status.equals("ok"))
			{
				mArrayComment.remove(mDeleteIndex);
				mAdapterComment.notifyDataSetChanged();

				if (this.mDelCommentCallback != null){
					mDelCommentCallback.OnDelCommentResult(itemIndex, mDeleteIndex);
				}
			} else {
				CustomToast.makeCustomToastShort(this, resultBean.msg);
			}
		}
	}
	
	private void editCommentResult(Object obj) {

		waitDialog.dismiss();
		
		if (obj == null) {
			CustomToast.makeCustomToastShort(this, Constants.NETWORK_ERR);
		}

		if (obj != null){
			EditCommentResult resultBean = (EditCommentResult)obj;
			if (resultBean.status.equals("ok"))
			{
				mAdapterComment.notifyDataSetChanged();
				
				if (this.mEditCommentCallback != null){
					mEditCommentCallback.OnEditCommentResult(itemIndex, mEdtCommentItem, mEditIndex);
				} else {
					CustomToast.makeCustomToastShort(this, resultBean.msg);
				}
			}
		}
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
			
		case DELETE_ACTIVITY:
			deleteCommentResult(result);
			
		case EDIT_COMMENT:
			editCommentResult(result);
		default:
			break;
		}
	}

	public interface OnCommentResultCallback {
		void OnCommentResult(CommentInfo commentData, int index);
	}
	
	public interface OnDelCommentResultCallback {
		void OnDelCommentResult(int activity_index, int index);
	}
	
	public interface OnEditCommentResultCallback {
		void OnEditCommentResult(int activity_index, CommentInfo commentData, int index);
	}
	
	public class CommentAdapter extends ArrayAdapter<CommentInfo> {
		Activity activity;
		int layoutResourceId;
		ArrayList<CommentInfo> item = new ArrayList<CommentInfo>();

		public CommentAdapter(Activity activity, int layoutId, ArrayList<CommentInfo> items) {
			// TODO Auto-generated constructor stub
			super(activity, layoutId, items);
			this.activity = activity;
			item = items;
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
				holder.txtComment.setMovementMethod(LinkMovementMethod.getInstance());

				holder.txtTime = (TextView) convertView.findViewById(R.id.txtTime);
				holder.imgComment = (WebImgView) convertView.findViewById(R.id.imgCommentPic);

				holder.btn_edit = convertView.findViewById(R.id.btn_delete);
				holder.btn_delete = convertView.findViewById(R.id.btn_delete);

				convertView.setTag(holder);
			} 
			else
				holder = (CommentHolder) convertView.getTag();

			holder.txtName.setTag(position);
			holder.txtName.setText(comment.author_name);

//			Spanned spanned = Html.fromHtml(comment.description);
//            SpannableString spannableString = SpannableString.valueOf(spanned);
//			holder.txtComment.setText(escapeJavaString(comment.description));
			
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

			holder.btn_delete.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					final MyDialog dialog = new MyDialog(getContext());
					dialog.setContentView(R.layout.dialog_delete);
					dialog.show();

					dialog.findViewById(R.id.btn_no).setOnClickListener(new View.OnClickListener() {

						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							dialog.dismiss();
						}
					});
					dialog.findViewById(R.id.btn_yes).setOnClickListener(new View.OnClickListener() {

						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							dialog.dismiss();
							mDeleteIndex = position;
							deleteComment(getItem(position));
						}
					});
				}
			});
			
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

				holder.btn_edit = convertView.findViewById(R.id.btn_edit);
				holder.btn_delete = convertView.findViewById(R.id.btn_delete);

				convertView.setTag(holder);
			} 
			else
				holder = (CommentHolder) convertView.getTag();

			holder.txtName.setTag(position);
			holder.txtName.setText(comment.author_name);

//			holder.txtComment.setText(escapeJavaString(comment.description));
			holder.txtComment.setText(comment.description);
			holder.txtComment.setMovementMethod(LinkMovementMethod.getInstance());

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

			holder.btn_edit.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					mEdtCommentItem = new CommentInfo();
					mEdtCommentItem = mArrayComment.get(position);
					txtComment.setText(mEdtCommentItem.description);
					mEditIndex = position;
					btnPost.setText("Edit");
				}
			});
			
			holder.btn_delete.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					final MyDialog dialog = new MyDialog(getContext());
					dialog.setContentView(R.layout.dialog_delete);
					dialog.show();

					dialog.findViewById(R.id.btn_no).setOnClickListener(new View.OnClickListener() {

						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							dialog.dismiss();
						}
					});
					dialog.findViewById(R.id.btn_yes).setOnClickListener(new View.OnClickListener() {

						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							mDeleteIndex = position;
							deleteComment(getItem(position));
							dialog.dismiss();
						}
					});
				}
			});

			return convertView;
		}

		public class CommentHolder {
			WebCircularImgView imgUser;
			TextView txtName;
			TextView txtComment;
			TextView txtTime;
			WebImgView imgComment;
			View btn_delete;
			View btn_edit;
		}

		public String escapeJavaString(String st){
	        StringBuilder builder = new StringBuilder();
	        try {
	            for (int i = 0; i < st.length(); i++) {
	                 char c = st.charAt(i);
	                 if(!Character.isLetterOrDigit(c) && !Character.isSpaceChar(c)&& !Character.isWhitespace(c) ){
	                     String unicode = String.valueOf(c);
	                     int code = (int)c;
	                     if(!(code >= 0 && code <= 255)){
	                         unicode = "\\\\u"+Integer.toHexString(c);
	                     }
	                     builder.append(unicode);
	                 }
	                 else{
	                     builder.append(c);
	                 }
	            }
	        } catch (Exception e) {
	            // TODO Auto-generated catch block
	            e.printStackTrace();
	        }
	        return builder.toString();
	    }
		
		 public String getUnicodeValue(String line) {

			    String hexCodeWithLeadingZeros = "";
			    try {
			      for (int index = 0; index < line.length(); index++) {
			        String hexCode = Integer.toHexString(line.codePointAt(index)).toUpperCase();
			        String hexCodeWithAllLeadingZeros = "0000" + hexCode;
			        String temp = hexCodeWithAllLeadingZeros.substring(hexCodeWithAllLeadingZeros.length() - 4);
			        hexCodeWithLeadingZeros += ("\\u" + temp);
			         }
			          return hexCodeWithLeadingZeros;
			       } catch (NullPointerException nlpException) {
			         return hexCodeWithLeadingZeros;
			          }
			    }
	}	
}

