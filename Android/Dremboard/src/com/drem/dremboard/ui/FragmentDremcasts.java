package com.drem.dremboard.ui;

import java.util.ArrayList;

import com.drem.dremboard.R;
import com.drem.dremboard.entity.Beans.GetDremcastsResult;
import com.drem.dremboard.entity.DremInfo;
import com.drem.dremboard.entity.DremerInfo;
import com.drem.dremboard.entity.DremcastInfo;
import com.drem.dremboard.entity.GlobalValue;
import com.drem.dremboard.entity.Beans.GetDremcastsParam;
import com.drem.dremboard.entity.Beans.GetDremsResult;
import com.drem.dremboard.entity.Beans.SetFavoriteParam;
import com.drem.dremboard.entity.Beans.SetFavoriteResult;
import com.drem.dremboard.entity.Beans.SetLikeParam;
import com.drem.dremboard.entity.Beans.SetLikeResult;
import com.drem.dremboard.ui.DialogDremerBlocking.OnSetBlockResultCallback;
import com.drem.dremboard.ui.DialogFlagDremcast.OnFlagResultCallback;
import com.drem.dremboard.ui.DialogFriendshipAccept.OnFriendshipResultCallback;
import com.drem.dremboard.ui.FragmentDremers.DremerAdapter;
import com.drem.dremboard.ui.FragmentDremers.DremerGridAdapter;
import com.drem.dremboard.utils.AppPreferences;
import com.drem.dremboard.utils.ImageLoader;
import com.drem.dremboard.view.CustomToast;
import com.drem.dremboard.view.CustomSearchView;
import com.drem.dremboard.view.WaitDialog;
import com.drem.dremboard.view.WebCircularImgView;
import com.drem.dremboard.view.WebImgView;
import com.drem.dremboard.webservice.Constants;
import com.drem.dremboard.webservice.WebApiCallback;
import com.drem.dremboard.webservice.WebApiInstance;
import com.drem.dremboard.webservice.WebApiInstance.Type;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.VideoView;
import android.widget.AbsListView.OnScrollListener;

public class FragmentDremcasts extends Fragment implements 
		CustomSearchView.OnCustomSearchListener, WebApiCallback, OnFlagResultCallback
{
	private AppPreferences mPrefs;

	ProgressBar mProgMore;

	String mSearchStr = "";
	int mPerPage = 10;
	int mLastDremcastId = 0;
	boolean mAddMoreFlag = true;


	boolean mFlagLoading = false;
	boolean mShowType = false; // false-Grid, true-List
	
	TextView mTxtGrid, mTxtList;

	CustomSearchView mSearchView;

	ArrayList<DremcastInfo> mArrayDremcast = null;
	DremcastAdapter mAdapterDremcast;
	
	GridView mGridDremcast;
	DremerGridAdapter mAdapterGridDremer;
	
	int mDremerId;
	
	WaitDialog waitDialog;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View view = inflater.inflate(R.layout.fragment_dremcasts, null);

		mPrefs = new AppPreferences(getActivity());
		waitDialog = new WaitDialog(getActivity());

		Bundle bundle = getArguments();
		if (bundle != null) {
			mDremerId = getArguments().getInt("dremer_id", -1);
		} else {
			mDremerId = -1;
		}
		
		initView (view);
		
		resetOptions();
		getDremcastList(mLastDremcastId, mPerPage);

		return view;
	}

	private void initView (View view)
	{
		
		mProgMore = (ProgressBar) view.findViewById(R.id.progMore);

		if (mFlagLoading)
			mProgMore.setVisibility(View.VISIBLE);
		else
			mProgMore.setVisibility(View.GONE);

		
		mArrayDremcast = new ArrayList<DremcastInfo>();
		mAdapterDremcast = new DremcastAdapter(getActivity(), R.layout.item_dremcast, mArrayDremcast);

		mGridDremcast = (GridView)view.findViewById(R.id.gridDremcasts);
		mGridDremcast.setAdapter(mAdapterDremcast);

		mGridDremcast.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				// TODO Auto-generated method stub
				if (mAddMoreFlag == false) {
					return;
				}

				int lastInScreen = firstVisibleItem + visibleItemCount;
				if (totalItemCount != 0 && (lastInScreen == totalItemCount)
						&& !mFlagLoading) {
					loadMoreDremcast();
				}
			}
		});
		mSearchView = (CustomSearchView) view
				.findViewById(R.id.lay_drem_search);
		mSearchView.setOnCustomSearchListener(this);
	}

	private void resetOptions()
	{
		mFlagLoading = false;
		mAddMoreFlag = true;
		
		mSearchStr = "";
		mLastDremcastId = 0;
		mPerPage = 10;
	}
	
	private void updateActionView (String type, Object view, int index, String value)
	{
		DremcastInfo dremcastItem = mArrayDremcast.get(index);
		Button button = (Button) view;
		
		if (dremcastItem == null || button == null)
			return;
		
		button.setText(value);
		
		if (type.equalsIgnoreCase("like"))
			dremcastItem.like = value;
		else if (type.equalsIgnoreCase("favorite"))
			dremcastItem.favorite = value;
		
		mAdapterDremcast.notifyDataSetChanged();
	}

	public void loadMoreDremcasts()
	{
		getDremcastList(mLastDremcastId, mPerPage);
	}

	public void removeAllDremcasts() {
		mArrayDremcast.removeAll(mArrayDremcast);
	}
	
	private void addDremcasts(ArrayList<DremcastInfo> arrayDremcasts) {

		if (arrayDremcasts == null)
			return;
		
		for (DremcastInfo item : arrayDremcasts) {
			mArrayDremcast.add(item);
			mLastDremcastId = item.id;
		}

		mAdapterDremcast.notifyDataSetChanged();
	}
	
	private void getDremcastList(int lastId, int count)	{

		if (mFlagLoading)
			return;

		mFlagLoading = true;
		mProgMore.setVisibility(View.VISIBLE);

		_getDremcastList(lastId, count);
	}
	
	private void _getDremcastList(int lastId, int count)	{
		GetDremcastsParam param = new GetDremcastsParam();
		
		param.user_id = mPrefs.getUserId();
		param.per_page = count;
		param.last_media_id = lastId;
		param.search_str = mSearchStr;
		
		param.author_id = mDremerId;
		
		waitDialog.show();

		WebApiInstance.getInstance().executeAPI(Type.GET_DREMCAST, param, this);
	}

	private void getDremcastListResult(Object obj) {

		waitDialog.dismiss();
		mFlagLoading = false;
		mProgMore.setVisibility(View.GONE);
		
		if (obj == null) {
			CustomToast.makeCustomToastShort(getActivity(), Constants.NETWORK_ERR);
		}

		if (obj != null){
			GetDremcastsResult resultBean = (GetDremcastsResult)obj;

			if (resultBean.status.equals("ok")) {
				
				if (resultBean.data.media.size() == 0) {
					mAddMoreFlag = false;
					return;
				}
				
				addDremcasts(resultBean.data.media);
			} else {
				CustomToast.makeCustomToastShort(getActivity(), resultBean.msg);
			}
		}
	}	

	
	private void setLike(DremcastInfo dremcastItem, Object exParam, int index)	{
		if (dremcastItem == null)
			return;
		
		waitDialog.show();
		
		SetLikeParam param = new SetLikeParam();
		
		String strLike = "Like";
		if (dremcastItem.like.contains("Unlike"))
			strLike = "Unlike";
		
		param.user_id = mPrefs.getUserId();
		param.activity_id = dremcastItem.id;	
		param.like_str = strLike;
		
		param.exParam = exParam;
		param.index = index;

		WebApiInstance.getInstance().executeAPI(Type.SET_LIKE, param, this);
	}

	private void setLikeResult(Object param, Object obj) {

		waitDialog.dismiss();
		
		if (obj == null) {
			CustomToast.makeCustomToastShort(getActivity(), Constants.NETWORK_ERR);
		}

		if (obj != null){
			SetLikeResult resultBean = (SetLikeResult)obj;

			if (resultBean.status.equals("ok")) {
				
				if (resultBean.data.like_str != null) {
					SetLikeParam likeParam = (SetLikeParam) param;
					
					updateActionView("like", likeParam.exParam, likeParam.index, resultBean.data.like_str);
				}
			} else {
				CustomToast.makeCustomToastShort(getActivity(), resultBean.msg);
			}
		}
	}
	
	private void setFavorite(DremcastInfo dremcastItem, Object exParam, int index)	{
		if (dremcastItem == null)
			return;
		
		waitDialog.show();
		
		SetFavoriteParam param = new SetFavoriteParam();
		
		String strFavorite = "Favorite";
		if (dremcastItem.favorite.contains("Unfavorite"))
			strFavorite = "Unfavorite";
		
		param.user_id = mPrefs.getUserId();
		param.activity_id = dremcastItem.id;	
		param.favorite_str = strFavorite;
		
		param.exParam = exParam;
		param.index = index;

		WebApiInstance.getInstance().executeAPI(Type.SET_FAVORITE, param, this);
	}

	private void setFavoriteResult(Object param, Object obj) {

		waitDialog.dismiss();
		
		if (obj == null) {
			CustomToast.makeCustomToastShort(getActivity(), Constants.NETWORK_ERR);
		}

		if (obj != null){
			SetFavoriteResult resultBean = (SetFavoriteResult)obj;

			if (resultBean.status.equals("ok")) {
				
				if (resultBean.data.favorite_str != null) {
					SetFavoriteParam likeParam = (SetFavoriteParam) param;
					
					updateActionView("favorite", likeParam.exParam, likeParam.index, resultBean.data.favorite_str);
				}
			} else {
				CustomToast.makeCustomToastShort(getActivity(), resultBean.msg);
			}
		}
	}
	
	private void showShareDialog(int activity_id, String guid)
	{
		Dialog dialog = new DialogShare(getActivity(), getActivity(), activity_id, guid);
		dialog.show();
	}
	
	private void showFlagDialog(int activity_id, int index)
	{
		Dialog dialog = new DialogFlagDremcast(getActivity(), activity_id, index, this);
		dialog.show();
	}

	@Override
	public void onCustomSearchBtnClicked(String search_str) {
		
		mSearchStr = search_str;
		
		mAddMoreFlag = true;
		mLastDremcastId = 0;
		
		removeAllDremcasts();
		getDremcastList(mLastDremcastId, mPerPage);		
	}

	public void loadMoreDremcast()
	{
		getDremcastList(mLastDremcastId, mPerPage);
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
		case GET_DREMCAST:
			getDremcastListResult(result);			
			break;
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
		mArrayDremcast.remove(index);
		mAdapterDremcast.notifyDataSetChanged();
		
		CustomToast.makeCustomToastLong(getActivity(), strAlert);
	}
	

	private void ViewDremcast(DremcastInfo dremcastItem)
	{		
		Intent intent = new Intent();
		intent.setClass(getActivity(), ActivityMediaPlayer.class);
//		intent.setClass(getActivity(), ActivityContentView.class);
		intent.putExtra("type", "video");
		intent.putExtra("content_id", dremcastItem.guid);
//		GlobalValue.getInstance().setCurrentDremcast(dremcastItem);
		startActivity(intent);
		getActivity().overridePendingTransition(R.anim.in_right_left, R.anim.out_right_left);
	}

	public class DremcastAdapter extends ArrayAdapter<DremcastInfo> implements OnClickListener{
		Activity activity;
		int layoutResourceId;
		ArrayList<DremcastInfo> item = new ArrayList<DremcastInfo>();

		public DremcastAdapter(Activity activity, int layoutId, ArrayList<DremcastInfo> items) {
			super(activity, layoutId, items);
			item = items;
			this.activity = activity;
			this.layoutResourceId = layoutId;
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			VideoHolder holder = null;	

			final DremcastInfo dremcastItem = getItem(position);			

			// inflate the view
			if (convertView == null) {
				LayoutInflater inflater = ((Activity) activity).getLayoutInflater();
				convertView = inflater.inflate(R.layout.item_dremcast, null);
				holder = new VideoHolder();
				holder.txtVideoName = (TextView) convertView.findViewById(R.id.txtVideoName);				
				holder.txtUserName = (TextView) convertView.findViewById(R.id.txtUserName);
				holder.imgVideoPic = (WebImgView) convertView.findViewById(R.id.imgDremcastPic);
				holder.videoUser = (WebCircularImgView) convertView.findViewById(R.id.videoUser);
				holder.btnFavorite = (Button) convertView.findViewById(R.id.btnFavorite);
				holder.btnLike = (Button) convertView.findViewById(R.id.btnLike);
				holder.btnFlag = (Button) convertView.findViewById(R.id.btnFlag);
				holder.btnShare = (Button) convertView.findViewById(R.id.btnShare);
//				holder.btnMore = (Button) convertView.findViewById(R.id.btnMore);
//				holder.btnLess = (Button) convertView.findViewById(R.id.btnLess);

				holder.imgVideoPic.setOnClickListener(this);
				holder.btnFavorite.setOnClickListener(this);
				holder.btnLike.setOnClickListener(this);
				holder.btnFlag.setOnClickListener(this);
				holder.btnShare.setOnClickListener(this);
//				holder.btnMore.setOnClickListener(this);
//				holder.btnLess.setOnClickListener(this);
				
				convertView.setTag(holder);
			} 
			else
				holder = (VideoHolder) convertView.getTag();

			holder.imgVideoPic.imageView.setImageResource(R.drawable.sample_video);

			holder.imgVideoPic.setTag(position);
			
			holder.txtVideoName.setText(dremcastItem.media_title);			
			holder.txtUserName.setText(dremcastItem.media_author_name);

			if (dremcastItem.media_author_avatar != null && !dremcastItem.media_author_avatar.isEmpty())
				ImageLoader.getInstance().displayImage(dremcastItem.media_author_avatar, holder.videoUser, 0, 0);
			else
				holder.videoUser.imageView.setImageResource(R.drawable.empty_man);
			
			holder.videoUser.setTag(position);

			holder.btnFavorite.setText(dremcastItem.favorite);
			holder.btnFavorite.setTag(position);
			
			holder.btnLike.setText(dremcastItem.like);
			holder.btnLike.setTag(position);
			
			holder.btnFlag.setTag(position);
			holder.btnShare.setTag(position);			

//			holder.btnMore.setTag(position);
//			holder.btnLess.setTag(position);
			
//			if(dremcastItem.isMore == true){
//				holder.btnFavorite.setVisibility(View.GONE);
//				holder.btnFlag.setVisibility(View.INVISIBLE);
//				holder.btnMore.setVisibility(View.VISIBLE);
//				holder.btnLess.setVisibility(View.INVISIBLE);
//			} else {
//				holder.btnFavorite.setVisibility(View.VISIBLE);
//				holder.btnFlag.setVisibility(View.VISIBLE);
//				holder.btnMore.setVisibility(View.INVISIBLE);
//				holder.btnLess.setVisibility(View.VISIBLE);
//			}
			
			return convertView;
		}

		public class VideoHolder {
			WebImgView imgVideoPic;
			TextView txtVideoName;
			TextView txtUserName;
			WebCircularImgView videoUser;
			Button btnFavorite;
			Button btnLike;
			Button btnFlag;
			Button btnShare;
			Button btnMore;
			Button btnLess;
		}
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			int viewId = v.getId();
			int position = (Integer) v.getTag();
			
			DremcastInfo dremcastItem = getItem(position);
			
			if (dremcastItem == null)
				return;
			
			switch (viewId) {
			case R.id.imgDremcastPic:
				ViewDremcast(dremcastItem);
				break;
			case R.id.btnFavorite:
				setFavorite(dremcastItem, v, position);
				break;
			case R.id.btnLike:
				setLike(dremcastItem, v, position);
				break;
			case R.id.btnShare:
				showShareDialog(dremcastItem.id, dremcastItem.guid);
				break;
			case R.id.btnFlag:
				showFlagDialog(dremcastItem.id, position);
				break;
//			case R.id.btnMore:
//				dremcastItem.isMore = false;
//				notifyDataSetChanged();				
//				break;
//			case R.id.btnLess:
//				dremcastItem.isMore = true;
//				notifyDataSetChanged();				
//				break;
			default:
				break;
			}
		}
	}
}
