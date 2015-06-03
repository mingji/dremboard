package com.drem.dremboard.adapter;

import java.util.ArrayList;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.text.Html;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.drem.dremboard.R;
import com.drem.dremboard.entity.DremActivityInfo;
import com.drem.dremboard.entity.Beans.SetFavoriteParam;
import com.drem.dremboard.entity.Beans.SetFavoriteResult;
import com.drem.dremboard.entity.Beans.SetLikeParam;
import com.drem.dremboard.entity.Beans.SetLikeResult;
import com.drem.dremboard.entity.CommentInfo;
import com.drem.dremboard.entity.DremActivityInfo.MediaInfo;
import com.drem.dremboard.ui.DialogComment;
import com.drem.dremboard.ui.DialogComment.OnCommentResultCallback;
import com.drem.dremboard.ui.ActivityDremer;
import com.drem.dremboard.ui.DialogFlagDrem;
import com.drem.dremboard.ui.DialogFlagDrem.OnFlagResultCallback;
import com.drem.dremboard.ui.DialogShare;
import com.drem.dremboard.utils.AppPreferences;
import com.drem.dremboard.utils.ImageLoader;
import com.drem.dremboard.utils.TextViewUtils;
import com.drem.dremboard.utils.Utility;
import com.drem.dremboard.view.CustomToast;
import com.drem.dremboard.view.WebCircularImgView;
import com.drem.dremboard.view.WebImgView;
import com.drem.dremboard.view.WaitDialog;
import com.drem.dremboard.webservice.Constants;
import com.drem.dremboard.webservice.WebApiCallback;
import com.drem.dremboard.webservice.WebApiInstance;
import com.drem.dremboard.webservice.WebApiInstance.Type;

public class DremActivityAdapter extends ArrayAdapter<DremActivityInfo>
	implements OnClickListener, WebApiCallback, OnCommentResultCallback, OnFlagResultCallback{
	Activity activity;
	int layoutResourceId;
	ArrayList<DremActivityInfo> item = new ArrayList<DremActivityInfo>();
	WaitDialog waitDialog;
	private AppPreferences mPrefs;

	public DremActivityAdapter(Activity activity, int layoutId,
			ArrayList<DremActivityInfo> items) {
		super(activity, layoutId, items);
		item = items;
		this.activity = activity;
		this.layoutResourceId = layoutId;
		
		mPrefs = new AppPreferences(this.activity);
		waitDialog = new WaitDialog(this.activity);
	}
	
	private void showFlagDialog(int activity_id, int index)
	{
		Dialog dialog = new DialogFlagDrem(this.activity, activity_id, index, this);
		dialog.show();
	}

	@Override
	public View getView(final int position, View convertView,
			ViewGroup parent) {

		DremActivityHolder holder = null;

		final DremActivityInfo dremActivityItem = getItem(position);
		
		// inflate the view
		if (convertView == null) {
			LayoutInflater inflater = ((Activity) activity)
					.getLayoutInflater();
			convertView = inflater.inflate(R.layout.item_drem_activity, null);
			holder = new DremActivityHolder();
			
			holder.imgAuthor = (WebCircularImgView) convertView.findViewById(R.id.img_author);
			holder.txtAction = (TextView) convertView.findViewById(R.id.txtAction);
			holder.txtDate = (TextView) convertView.findViewById(R.id.txtDate);
			holder.layMediaList = (LinearLayout) convertView.findViewById(R.id.layMediaList);
			holder.txtDescription = (TextView) convertView.findViewById(R.id.txtDescription);
			holder.btnComment = (Button) convertView.findViewById(R.id.btnComment);
			holder.btnFavorite = (Button) convertView.findViewById(R.id.btnFavorite);
			holder.btnLike = (Button) convertView.findViewById(R.id.btnLike);
			holder.btnFlag = (Button) convertView.findViewById(R.id.btnFlag);
			holder.btnShare = (Button) convertView.findViewById(R.id.btnShare);
			holder.btnEdit = (Button) convertView.findViewById(R.id.btnEdit);
			holder.btnDelete = (Button) convertView.findViewById(R.id.btnDelete);
//			holder.layCommentList = (LinearLayout) convertView.findViewById(R.id.layCommentList);
			
			holder.imgAuthor.setOnClickListener(this);
			holder.btnComment.setOnClickListener(this);
			holder.btnFavorite.setOnClickListener(this);
			holder.btnLike.setOnClickListener(this);
			holder.btnFlag.setOnClickListener(this);
			holder.btnShare.setOnClickListener(this);
			holder.btnEdit.setOnClickListener(this);
			holder.btnDelete.setOnClickListener(this);

			convertView.setTag(holder);
		} else
			holder = (DremActivityHolder) convertView.getTag();		
		
		holder.imgAuthor.setTag(position);
		if (dremActivityItem.author_avatar != null && !dremActivityItem.author_avatar.isEmpty())
			ImageLoader.getInstance().displayImage(dremActivityItem.author_avatar, holder.imgAuthor, 0, 0);
		else
			holder.imgAuthor.imageView.setImageResource(R.drawable.empty_pic);
		
		String actionTxt = dremActivityItem.action;
		Spanned actionSpan = Html.fromHtml(actionTxt);
		holder.txtAction.setText(actionSpan);
		holder.txtAction.setMovementMethod(LinkMovementMethod.getInstance());
		TextViewUtils.stripUnderlines(holder.txtAction);
		
		holder.txtDate.setText(Utility.getRelativeDateStrFromTime(dremActivityItem.last_modified));
		
		String descTxt = dremActivityItem.description;
		Spanned descSpan = Html.fromHtml(descTxt);
		holder.txtDescription.setText(descSpan);
		holder.txtDescription.setMovementMethod(LinkMovementMethod.getInstance());
		TextViewUtils.stripUnderlines(holder.txtDescription);
		
//		holder.btnFavorite.setText(dremActivityItem.favorite);
		holder.btnFavorite.setTag(position);
		
		holder.btnLike.setText(dremActivityItem.like);
		holder.btnLike.setTag(position);		
		
		holder.btnComment.setTag(position);
		holder.btnShare.setTag(position);
		holder.btnFlag.setTag(position);
		holder.btnEdit.setTag(position);
		holder.btnDelete.setTag(position);
		
		holder.layMediaList.removeAllViews();
		for (int i = 0; i < dremActivityItem.media_list.size(); i++){
			MediaInfo mediaInfo = dremActivityItem.media_list.get(i);
			
			if (mediaInfo.media_type.equals("photo")) {
				
				String photo_guid = mediaInfo.media_guid;
				if (photo_guid != null){
					WebImgView imgPic = new WebImgView(activity);
					holder.layMediaList.addView(imgPic);
					ImageLoader.getInstance().displayImage(photo_guid, imgPic, 0, 0);
				}
			}
			else if (mediaInfo.media_type.equals("video")) {
				;
			}
		}
		
//		holder.layCommentList.removeAllViews();
//		for (int i = 0; i < dremActivityItem.comment_list.size(); i++){			
//			CommentInfo commentInfo = dremActivityItem.comment_list.get(i);
//			
//			if (commentInfo == null)
//				continue;
//			
//			View commentView = getCommentView(commentInfo);
//			
//			holder.layCommentList.addView(commentView);
//			
//		}
			
		holder.btnLike.setText(dremActivityItem.like);
		if(dremActivityItem.comment_list.size() > 0) {
			holder.btnComment.setText("Comment ("+String.valueOf(dremActivityItem.comment_list.size())+")");
		} else {
			holder.btnComment.setText("Comment");
		}
		return convertView;
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		int viewId = v.getId();
		int position = (Integer) v.getTag();
		
		DremActivityInfo item = getItem(position);
		
		if (item == null)
			return;
		
		switch (viewId) {
		case R.id.img_author:
			startActivityDremer(item.author_id);
			break;
		case R.id.btnComment:
			setComment(item, position);
			break;
		case R.id.btnFavorite:
			setFavorite(item, v, position);
			break;
		case R.id.btnLike:
			setLike(item, v, position);
			break;
		case R.id.btnFlag:
			showFlagDialog(item.activity_id, position);
			break;
		case R.id.btnShare:
			showShareDialog(item.activity_id);
			break;
		default:
			break;
		}
	}

	class DremActivityHolder {
		WebCircularImgView imgAuthor;
		TextView txtAction;
		TextView txtDate;
		TextView txtDescription;
		Button btnComment;
		Button btnFavorite;
		Button btnLike;
		Button btnFlag;
		Button btnShare;
		Button btnEdit;
		Button btnDelete;
		LinearLayout layMediaList;
//		LinearLayout layCommentList;
	}
	
	private View getCommentView (CommentInfo commentInfo)
	{
		LayoutInflater inflater = ((Activity) this.activity).getLayoutInflater();
		View commentView = inflater.inflate(R.layout.item_comment, null);
		
		WebCircularImgView imgAuthor = (WebCircularImgView) commentView.findViewById(R.id.imgCommentAuthor);
		TextView txtAuthor = (TextView) commentView.findViewById(R.id.txtCommentAuthor);
		TextView txtComment = (TextView) commentView.findViewById(R.id.txtComment);
		WebImgView imgComment = (WebImgView) commentView.findViewById(R.id.imgCommentPic);
		Button btnCommentLike = (Button) commentView.findViewById(R.id.btnCommentLike);
		TextView txtCommentDate = (TextView) commentView.findViewById(R.id.txtCommentDate);
		
		if (commentInfo.author_avatar != null && !commentInfo.author_avatar.isEmpty())
			ImageLoader.getInstance().displayImage(commentInfo.author_avatar, imgAuthor, 0, 0);
		else
			imgAuthor.imageView.setImageResource(R.drawable.empty_pic);
		
		txtAuthor.setText(commentInfo.author_name);
		txtComment.setText(commentInfo.description);
		txtCommentDate.setText(Utility.getRelativeDateStrFromTime(commentInfo.last_modified));
		
		if (commentInfo.media_guid != null && !commentInfo.media_guid.isEmpty()) {
			if (!commentInfo.media_guid.contains("dremboard.com")) {
				commentInfo.media_guid= "http://dremboard.com" + commentInfo.media_guid;
			}
				ImageLoader.getInstance().displayImage(commentInfo.media_guid, imgComment, 0, 0);
		} else
			imgComment.setVisibility(View.GONE);
		
		return commentView;
	}
	
	private void startActivityDremer (int dremerId)
	{
		Intent intent = new Intent();
		intent.setClass(this.activity, ActivityDremer.class);
		intent.putExtra("dremer_id", dremerId);
		this.activity.startActivity(intent);
		this.activity.overridePendingTransition(R.anim.in_right_left, R.anim.out_right_left);
	}
	
	private void updateActionView (String type, Object view, int index, String value)
	{
		DremActivityInfo activityItem = getItem(index);
		Button button = (Button) view;
		
		if (activityItem == null || button == null)
			return;
		
		button.setText(value);
		
		if (type.equalsIgnoreCase("like"))
			activityItem.like = value;
		else if (type.equalsIgnoreCase("favorite"))
			activityItem.favorite = value;
	}
	
	private void setLike(DremActivityInfo activityItem, Object exParam, int index)	{
		if (activityItem == null)
			return;
		
		waitDialog.show();
		
		SetLikeParam param = new SetLikeParam();
		
		String strLike = "Like";
		if (activityItem.like.contains("Unlike"))
			strLike = "Unlike";
		
		param.user_id = mPrefs.getUserId();
		param.activity_id = activityItem.activity_id;	
		param.like_str = strLike;
		
		param.exParam = exParam;
		param.index = index;

		WebApiInstance.getInstance().executeAPI(Type.SET_LIKE, param, this);
	}

	private void setLikeResult(Object param, Object obj) {

		waitDialog.dismiss();
		
		if (obj == null) {
			CustomToast.makeCustomToastShort(this.activity, Constants.NETWORK_ERR);
		}

		if (obj != null){
			SetLikeResult resultBean = (SetLikeResult)obj;

			if (resultBean.status.equals("ok")) {
				
				if (resultBean.data.like_str != null) {
					SetLikeParam likeParam = (SetLikeParam) param;
					
					updateActionView("like", likeParam.exParam, likeParam.index, resultBean.data.like_str);
				}
			} else {
				CustomToast.makeCustomToastShort(this.activity, resultBean.msg);
			}
		}
	}
	
	private void setComment(DremActivityInfo activityItem, int index)	{
		if (activityItem == null)
			return;
		
		DialogComment commentDiag = new DialogComment(activity, activity, activityItem.activity_id, 
				index, activityItem.comment_list, this);
		commentDiag.show();
	}
	
	private void setCommentResult (CommentInfo commentData, int index)
	{
		DremActivityInfo activityItem = getItem(index);
		
		if (activityItem == null || commentData == null)
			return;
		
		if (activityItem.comment_list == null)
			activityItem.comment_list = new ArrayList<CommentInfo>();
		
		activityItem.comment_list.add(commentData);
		
		notifyDataSetChanged();
	}
	
	private void setFavorite(DremActivityInfo activityItem, Object exParam, int index)	{
		if (activityItem == null)
			return;
		
		waitDialog.show();
		
		SetFavoriteParam param = new SetFavoriteParam();
		
		String strFavorite = "Favorite";
		if (activityItem.favorite != null && activityItem.favorite.contains("Unfavorite"))
			strFavorite = "Unfavorite";
		
		param.user_id = mPrefs.getUserId();
		param.activity_id = activityItem.activity_id;	
		param.favorite_str = strFavorite;
		
		param.exParam = exParam;
		param.index = index;

		WebApiInstance.getInstance().executeAPI(Type.SET_FAVORITE, param, this);
	}

	private void setFavoriteResult(Object param, Object obj) {

		waitDialog.dismiss();
		
		if (obj == null) {
			CustomToast.makeCustomToastShort(this.activity, Constants.NETWORK_ERR);
		}

		if (obj != null){
			SetFavoriteResult resultBean = (SetFavoriteResult)obj;

			if (resultBean.status.equals("ok")) {
				
				if (resultBean.data.favorite_str != null) {
					SetFavoriteParam likeParam = (SetFavoriteParam) param;
					
					updateActionView("favorite", likeParam.exParam, likeParam.index, resultBean.data.favorite_str);
				}
			} else {
				CustomToast.makeCustomToastShort(this.activity, resultBean.msg);
			}
		}
	}
	
	private void showShareDialog(int activity_id)
	{
		Dialog dialog = new DialogShare(this.activity, this.activity, activity_id);
		dialog.show();
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
		case SET_FAVORITE:
			setFavoriteResult(parameter, result);			
			break;
		default:
			break;
		}
	}

	@Override
	public void onFinishSetFlag(String strAlert, int index) {
		// TODO Auto-generated method stub
		item.remove(index);
		notifyDataSetChanged();
		
		CustomToast.makeCustomToastLong(this.activity, strAlert);
	}

	@Override
	public void OnCommentResult(
			CommentInfo commentData, int index) {
		// TODO Auto-generated method stub
		setCommentResult (commentData, index);
	}
}
