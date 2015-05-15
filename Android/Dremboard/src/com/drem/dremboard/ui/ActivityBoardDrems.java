package com.drem.dremboard.ui;

import java.util.ArrayList;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.AbsListView.OnScrollListener;

import com.drem.dremboard.R;
import com.drem.dremboard.entity.DremInfo;
import com.drem.dremboard.entity.DremboardInfo;
import com.drem.dremboard.entity.DremerInfo;
import com.drem.dremboard.entity.GlobalValue;
import com.drem.dremboard.entity.Beans.GetDremsParam;
import com.drem.dremboard.entity.Beans.GetDremsResult;
import com.drem.dremboard.entity.Beans.SetFavoriteParam;
import com.drem.dremboard.entity.Beans.SetFavoriteResult;
import com.drem.dremboard.entity.Beans.SetLikeParam;
import com.drem.dremboard.entity.Beans.SetLikeResult;
import com.drem.dremboard.ui.FragmentDrems.DremAdapter;
import com.drem.dremboard.ui.FragmentDrems.DremAdapter.DremHolder;
import com.drem.dremboard.utils.AppPreferences;
import com.drem.dremboard.utils.ImageLoader;
import com.drem.dremboard.view.AdminSearchView;
import com.drem.dremboard.view.CustomToast;
import com.drem.dremboard.view.HyIconView;
import com.drem.dremboard.view.WaitDialog;
import com.drem.dremboard.webservice.Constants;
import com.drem.dremboard.webservice.WebApiCallback;
import com.drem.dremboard.webservice.WebApiInstance;
import com.drem.dremboard.webservice.WebApiInstance.Type;

public class ActivityBoardDrems extends Activity
		implements OnClickListener, WebApiCallback {

	AppPreferences mPrefs;
	
	HyIconView mImgDremer;
	TextView mTxtTitle, mTxtName;	
	
	HyIconView mImgUserIcon;

	Button mBtnBack;
	GridView mGridDrems;
	DremAdapter mAdapterDrem;
	ArrayList<DremInfo> mArrayDrems;
	AdminSearchView mAdminSearchView;

	DremboardInfo mDremboard;
	
	ProgressBar mProgMore;

	boolean mFlagLoading = false;
	boolean mAddMoreFlag = true;
	
	String mSearchStr = "";
	int mCategory = -1;
	int mLastDremId = 0;
	int mPerPage = 10;
	
	WaitDialog waitDialog;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		setTheme(R.style.ActivityTheme);
		super.onCreate(savedInstanceState);
		//requestWindowFeature(Window.FEATURE_NO_TITLE);		
		setContentView(R.layout.activity_dremboard_drems);
		mPrefs = new AppPreferences(this);
		waitDialog = new WaitDialog(this);
		
		mDremboard = GlobalValue.getInstance().getCurrentDremboard();
		if (mDremboard.id == -1) { // check valid
			finish();
			return;
		}

		initView();
		updateView(mDremboard);
		
		resetOptions();
		
		getDremList(mLastDremId, mPerPage);
	}

	private void initView() {
		
		mImgDremer = (HyIconView) findViewById(R.id.imgDremer);
		mImgDremer.imageView.setImageResource(R.drawable.empty_man);
		
		mTxtTitle = (TextView) findViewById(R.id.txtBoardTitle);
		
		mTxtName = (TextView) findViewById(R.id.txtName);
		
		mBtnBack = (Button) findViewById(R.id.btnBack);
		mBtnBack.setOnClickListener(this);
		
		mImgUserIcon = (HyIconView) findViewById(R.id.imgUserIcon);
		mImgUserIcon.imageView.setImageResource(R.drawable.empty_man);
		DremerInfo dremer = GlobalValue.getInstance().getCurrentDremer();
		if (dremer.user_avatar != null && !dremer.user_avatar.isEmpty())
			ImageLoader.getInstance().displayImage(dremer.user_avatar, mImgUserIcon, 0, 0);
		
		mProgMore = (ProgressBar) findViewById(R.id.progMore);

		if (mFlagLoading)
			mProgMore.setVisibility(View.VISIBLE);
		else
			mProgMore.setVisibility(View.GONE);

		mArrayDrems = new ArrayList<DremInfo>();
		mAdapterDrem = new DremAdapter(this, R.layout.item_drem,
				mArrayDrems);

		mGridDrems = (GridView) findViewById(R.id.gridDrems);
		mGridDrems.setAdapter(mAdapterDrem);
		mGridDrems.setOnScrollListener(new OnScrollListener() {

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
					loadMoreDrems();
				}
			}
		});
	}
	
	private void updateView (DremboardInfo board)
	{
		if (board == null || board.id == -1)
			return;
		
		if (board.media_author_avatar != null && !board.media_author_avatar.isEmpty())
			ImageLoader.getInstance().displayImage(board.media_author_avatar, mImgDremer, 0, 0);
		else
			mImgDremer.imageView.setImageResource(R.drawable.empty_man);
		
		mTxtTitle.setText(board.media_title);
		mTxtName.setText(board.media_author_name);
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		int id = v.getId();

		switch (id) {
		case R.id.btnBack:
			onBackButton();
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
			onBackButton();			
			bool = true;
		}
		return bool;
	}
	
	private void onBackButton()
	{
		finish();
		overridePendingTransition(R.anim.in_left_right, R.anim.out_left_right);		
	}
	
	private void resetOptions()
	{
		mFlagLoading = false;
		mAddMoreFlag = true;
		
		mSearchStr = "";
		mCategory = -1;
		mLastDremId = 0;
		mPerPage = 10;
	}	
	
	private void updateActionView (String type, Object view, int index, String value)
	{
		DremInfo dremItem = mArrayDrems.get(index);
		Button button = (Button) view;
		
		if (dremItem == null || button == null)
			return;
		
		button.setText(value);
		
		if (type.equalsIgnoreCase("like"))
			dremItem.like = value;
		else if (type.equalsIgnoreCase("favorite"))
			dremItem.favorite = value;
	}
	
	private void loadMoreDrems()
	{
		getDremList(mLastDremId, mPerPage);
	}

	private void removeAllDrems() {
		mArrayDrems.removeAll(mArrayDrems);
	}
	
	private void addDrems(ArrayList<DremInfo> arrayDrems) {

		if (arrayDrems == null)
			return;
		
		for (DremInfo item : arrayDrems) {
			mArrayDrems.add(item);
			mLastDremId = item.id;
		}

		mAdapterDrem.notifyDataSetChanged();
	}
	
	private void getDremList(int lastId, int count)	{

		if (mFlagLoading)
			return;

		mFlagLoading = true;
		mProgMore.setVisibility(View.VISIBLE);

		_getDremList(lastId, count);
	}
	
	private void _getDremList(int lastId, int count)	{
		GetDremsParam param = new GetDremsParam();
		
		param.user_id = mPrefs.getUserId();
		param.category = mCategory;
		param.album_id = mDremboard.id;
		param.per_page = count;
		param.last_media_id = lastId;
		param.search_str = mSearchStr;
		
		param.author_id = -1; // unused id

		waitDialog.show();

		WebApiInstance.getInstance().executeAPI(Type.GET_DREM, param, this);
	}

	private void getDremListResult(Object obj) {

		waitDialog.dismiss();
		mFlagLoading = false;
		mProgMore.setVisibility(View.GONE);
		
		if (obj == null) {
			CustomToast.makeCustomToastShort(this, Constants.NETWORK_ERR);
		}

		if (obj != null){
			GetDremsResult resultBean = (GetDremsResult)obj;

			if (resultBean.status.equals("ok")) {
				
				if (resultBean.data.media.size() == 0) {
					mAddMoreFlag = false;
					return;
				}
				
				addDrems(resultBean.data.media);
			} else {
				CustomToast.makeCustomToastShort(this, resultBean.msg);
			}
		}
	}	
	
	private void setLike(DremInfo dremItem, Object exParam, int index)	{
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
		
		param.exParam = exParam;
		param.index = index;

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
					SetLikeParam likeParam = (SetLikeParam) param;
					
					updateActionView("like", likeParam.exParam, likeParam.index, resultBean.data.like_str);
				}
			} else {
				CustomToast.makeCustomToastShort(this, resultBean.msg);
			}
		}
	}
	
	private void setFavorite(DremInfo dremItem, Object exParam, int index)	{
		if (dremItem == null)
			return;
		
		waitDialog.show();
		
		SetFavoriteParam param = new SetFavoriteParam();
		
		String strFavorite = "Favorite";
		if (dremItem.favorite.contains("Unfavorite"))
			strFavorite = "Unfavorite";
		
		param.user_id = mPrefs.getUserId();
		param.activity_id = dremItem.activity_id;	
		param.favorite_str = strFavorite;
		
		param.exParam = exParam;
		param.index = index;

		WebApiInstance.getInstance().executeAPI(Type.SET_FAVORITE, param, this);
	}

	private void setFavoriteResult(Object param, Object obj) {

		waitDialog.dismiss();
		
		if (obj == null) {
			CustomToast.makeCustomToastShort(this, Constants.NETWORK_ERR);
		}

		if (obj != null){
			SetFavoriteResult resultBean = (SetFavoriteResult)obj;

			if (resultBean.status.equals("ok")) {
				
				if (resultBean.data.favorite_str != null) {
					SetFavoriteParam likeParam = (SetFavoriteParam) param;
					
					updateActionView("favorite", likeParam.exParam, likeParam.index, resultBean.data.favorite_str);
				}
			} else {
				CustomToast.makeCustomToastShort(this, resultBean.msg);
			}
		}
	}
	
	private void showShareDialog(int activity_id)
	{
		Dialog dialog = new DialogShare(this, this, activity_id);
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
		case GET_DREM:
			getDremListResult(result);			
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
	
	public class DremAdapter extends ArrayAdapter<DremInfo> implements OnClickListener{
		Activity activity;
		int layoutResourceId;
		ArrayList<DremInfo> item = new ArrayList<DremInfo>();

		public DremAdapter(Activity activity, int layoutId,
				ArrayList<DremInfo> items) {
			super(activity, layoutId, items);
			item = items;
			this.activity = activity;
			this.layoutResourceId = layoutId;
		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			DremHolder holder = null;

			final DremInfo dremItem = getItem(position);

			// inflate the view
			if (convertView == null) {
				LayoutInflater inflater = ((Activity) activity).getLayoutInflater();
				convertView = inflater.inflate(R.layout.item_drem, null);
				
				holder = new DremHolder();
				
				holder.txtCategory = (TextView) convertView.findViewById(R.id.txtCategory);
				holder.imgPic = (HyIconView) convertView.findViewById(R.id.imgPic);
				
				holder.btnFavorite = (Button) convertView.findViewById(R.id.btnFavorite);
				holder.btnLike = (Button) convertView.findViewById(R.id.btnLike);
				holder.btnFlag = (Button) convertView.findViewById(R.id.btnFlag);
				holder.btnShare = (Button) convertView.findViewById(R.id.btnShare);

				holder.imgPic.setOnClickListener(this);
				holder.btnFavorite.setOnClickListener(this);
				holder.btnLike.setOnClickListener(this);
//				holder.btnFlag.setOnClickListener(this);
				holder.btnShare.setOnClickListener(this);
				
				convertView.setTag(holder);
			} else
				holder = (DremHolder) convertView.getTag();

			holder.imgPic.setTag(position);
			if (dremItem.guid != null && !dremItem.guid.isEmpty())
				ImageLoader.getInstance().displayImage(dremItem.guid, holder.imgPic, 0, 0);
			else
				holder.imgPic.imageView.setImageResource(R.drawable.sample_drem);

			holder.txtCategory.setText(dremItem.category);
			
			holder.btnFavorite.setText(dremItem.favorite);
			holder.btnFavorite.setTag(position);
			
			holder.btnLike.setText(dremItem.like);
			holder.btnLike.setTag(position);
			
			holder.btnFlag.setTag(position);
			holder.btnShare.setTag(position);

			holder.btnFlag.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					DialogFlagDrem flag_drem_diag = new DialogFlagDrem(activity, dremItem.activity_id) {
						public void drem_flaged(int activity_id, String flag_slug) {
							item.remove(dremItem);
							notifyDataSetChanged();
						}
					};
					flag_drem_diag.show();
				}
			});

			return convertView;

		}

		public class DremHolder {
			HyIconView imgPic;
			TextView txtCategory;
			Button btnFavorite;
			Button btnLike;
			Button btnFlag;
			Button btnShare;
		}

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			int viewId = v.getId();
			int position = (Integer) v.getTag();
			
			DremInfo dremItem = getItem(position);
			
			if (dremItem == null)
				return;
			
			switch (viewId) {
			case R.id.imgPic:				
				break;
			case R.id.btnFavorite:
				setFavorite(dremItem, v, position);
				break;
			case R.id.btnLike:
				setLike(dremItem, v, position);
				break;
			case R.id.btnShare:
				showShareDialog(dremItem.activity_id);
				break;
			default:
				break;
			}
		}
	}
}
