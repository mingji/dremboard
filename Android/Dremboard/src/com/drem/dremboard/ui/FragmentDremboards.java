package com.drem.dremboard.ui;

import java.util.ArrayList;

import com.drem.dremboard.R;
import com.drem.dremboard.entity.Beans.GetDremboardsParam;
import com.drem.dremboard.entity.Beans.GetDremboardsResult;
import com.drem.dremboard.entity.DremboardInfo;
import com.drem.dremboard.entity.DremerInfo;
import com.drem.dremboard.entity.GlobalValue;
import com.drem.dremboard.utils.AppPreferences;
import com.drem.dremboard.utils.ImageLoader;
import com.drem.dremboard.utils.Utility;
import com.drem.dremboard.view.AdminSearchView;
import com.drem.dremboard.view.CustomToast;
import com.drem.dremboard.view.WebCircularImgView;
import com.drem.dremboard.view.WebImgView;
import com.drem.dremboard.view.WaitDialog;
import com.drem.dremboard.webservice.Constants;
import com.drem.dremboard.webservice.WebApiCallback;
import com.drem.dremboard.webservice.WebApiInstance;
import com.drem.dremboard.webservice.WebApiInstance.Type;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.AbsListView.OnScrollListener;

public class FragmentDremboards extends Fragment implements AdminSearchView.OnSearchListener, WebApiCallback
{
	private AppPreferences mPrefs;

	AdminSearchView mAdminSearchView;
	
	GridView mGridDrems;
	DremboardAdapter mAdapterDremboard;
	ArrayList<DremboardInfo> mArrayDremboards;

	ProgressBar mProgMore;

	String mSearchStr = "";
	int mCategory = -1;	
	int mLastMediaId = 0;
	int mPerPage = 10;
	int mLastCount = 1;
	
	int mDremerId;

	boolean mFlagLoading = false;

	WaitDialog waitDialog;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View view = inflater.inflate(R.layout.fragment_dremboards, null);

		mPrefs = new AppPreferences(getActivity());
		waitDialog = new WaitDialog(getActivity());

		initView (view);
		
		Bundle bundle = getArguments();
		if (bundle != null) {
			mDremerId = getArguments().getInt("dremer_id", -1);
		} else {
			mDremerId = -1;
		}

		resetOptions();

		getDremboardList(mLastMediaId, mPerPage);

		return view;
	}

	private void initView (View view)
	{
		mProgMore = (ProgressBar) view.findViewById(R.id.progMore);
		if (mFlagLoading)
			mProgMore.setVisibility(View.VISIBLE);
		else
			mProgMore.setVisibility(View.GONE);

		mArrayDremboards = new ArrayList<DremboardInfo>();
		mAdapterDremboard = new DremboardAdapter(getActivity(), R.layout.item_drem, mArrayDremboards);

		mGridDrems = (GridView)view.findViewById(R.id.gridDremboards);
		mGridDrems.setAdapter(mAdapterDremboard);
		mGridDrems.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				// TODO Auto-generated method stub

				int lastInScreen = firstVisibleItem + visibleItemCount;
				if (totalItemCount != 0 && (lastInScreen == totalItemCount) &&
						!mFlagLoading && mLastCount > 0) {					
					loadMoreDrembodrds();
				}				
			}
		});
		
		mAdminSearchView = (AdminSearchView) view
				.findViewById(R.id.lay_drem_search);
		mAdminSearchView.setOnSearchListener(this);
	}
	
	private void setCurrentDremboard(DremboardInfo board)
	{
		GlobalValue.getInstance().setCurrentDremboard(board);
	}
	
	private void startActivityDremer (int dremerId)
	{
		Intent intent = new Intent();
		intent.setClass(getActivity(), ActivityDremer.class);
		intent.putExtra("dremer_id", dremerId);
		startActivity(intent);
		getActivity().overridePendingTransition(R.anim.in_right_left, R.anim.out_right_left);
	}
	
	private void startActivityBoardDrems ()
	{
		Intent intent = new Intent();
		intent.setClass(getActivity(), ActivityBoardDrems.class);
		startActivityForResult(intent, 1);
		getActivity().overridePendingTransition(R.anim.in_right_left, R.anim.out_right_left);
	}
	
	private void resetOptions()
	{
		mSearchStr = "";
		mCategory = -1;	
		mLastMediaId = 0;
		mPerPage = 10;
		mLastCount = 1;

		mFlagLoading = false;
	}

	private void loadMoreDrembodrds()
	{
		getDremboardList(mLastMediaId, mPerPage);
	}

	private void removeAllDremboards() {
		mArrayDremboards.removeAll(mArrayDremboards);
	}
	
	private void addDremboards(ArrayList<DremboardInfo> arrayBoards) {

		if (arrayBoards == null)
			return;
		for (DremboardInfo board : arrayBoards) {
			
			if (mDremerId == -1)
				mArrayDremboards.add(board);
			else if (mDremerId == board.media_author_id)
				mArrayDremboards.add(board);

			mLastMediaId = board.id;
		}

		mAdapterDremboard.notifyDataSetChanged();
	}

	private void getDremboardList(int lastId, int count)	{

		if (mFlagLoading)
			return;

		mFlagLoading = true;
		mProgMore.setVisibility(View.VISIBLE);

		_getDremboardList(lastId, count);
	}

	private void _getDremboardList(int lastId, int count)	{
		GetDremboardsParam param = new GetDremboardsParam();

		param.user_id = mPrefs.getUserId();
		param.category = mCategory;		
		param.per_page = count;
		param.last_media_id = lastId;
		param.search_str = mSearchStr;

//		waitDialog.show();

		WebApiInstance.getInstance().executeAPI(Type.GET_DREMBOARD, param, this);
	}

	private void getDremboardListResult(Object obj) {

//		waitDialog.dismiss();
		mFlagLoading = false;
		mProgMore.setVisibility(View.GONE);

		if (obj == null) {
			CustomToast.makeCustomToastShort(getActivity(), Constants.NETWORK_ERR);
		}

		if (obj != null){
			GetDremboardsResult resultBean = (GetDremboardsResult)obj;

			if (resultBean.status.equals("ok")) {
				mLastCount = resultBean.data.count;
				addDremboards(resultBean.data.media);
			} else {
				CustomToast.makeCustomToastShort(getActivity(), resultBean.msg);
			}
		}
	}

	public class DremboardAdapter extends ArrayAdapter<DremboardInfo> implements OnClickListener {
		Activity activity;
		int layoutResourceId;
		ArrayList<DremboardInfo> item = new ArrayList<DremboardInfo>();

		public DremboardAdapter(Activity activity, int layoutId, ArrayList<DremboardInfo> items) {
			super(activity, layoutId, items);
			item = items;
			this.activity = activity;
			this.layoutResourceId = layoutId;
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			DremboardHolder holder = null;	

			final DremboardInfo dremboardItem = getItem(position);			

			// inflate the view
			if (convertView == null) {
				LayoutInflater inflater = ((Activity) activity).getLayoutInflater();
				convertView = inflater.inflate(R.layout.item_dremboard, null);
				holder = new DremboardHolder();
				holder.imgAuthor = (WebCircularImgView) convertView.findViewById(R.id.imgAuthor);
				holder.txtAuthor = (TextView) convertView.findViewById(R.id.txtAuthor);	
				holder.imgBoardPic = (WebImgView) convertView.findViewById(R.id.imgPic);
				holder.txtBoardName = (TextView) convertView.findViewById(R.id.txtName);				
				holder.txtDremCount = (TextView) convertView.findViewById(R.id.txtDremCount);
				
				holder.imgAuthor.setOnClickListener(this);
				holder.txtAuthor.setOnClickListener(this);
				holder.imgBoardPic.setOnClickListener(this);
				holder.txtBoardName.setOnClickListener(this);
				holder.txtDremCount.setOnClickListener(this);				
				
				convertView.setTag(holder);
			} 
			else
				holder = (DremboardHolder) convertView.getTag();
			
			holder.imgAuthor.setTag(position);
			holder.txtAuthor.setTag(position);
			holder.imgBoardPic.setTag(position);
			holder.txtBoardName.setTag(position);
			holder.txtDremCount.setTag(position);

			if (dremboardItem.media_author_avatar != null && !dremboardItem.media_author_avatar.isEmpty())
				ImageLoader.getInstance().displayImage(dremboardItem.media_author_avatar, holder.imgAuthor, 0, 0);
			else
				holder.imgAuthor.imageView.setImageResource(R.drawable.empty_pic);

			holder.txtAuthor.setText(dremboardItem.media_author_name);		

			if (dremboardItem.guid != null && !dremboardItem.guid.isEmpty())
				ImageLoader.getInstance().displayImage(dremboardItem.guid, holder.imgBoardPic, 0, 0);
			else
				holder.imgBoardPic.imageView.setImageResource(R.drawable.sample_dremboard);

			holder.txtBoardName.setText(dremboardItem.media_title);			
			holder.txtDremCount.setText(dremboardItem.album_count + " Drēms");

			return convertView;
		}

		public class DremboardHolder {
			WebCircularImgView imgAuthor;
			TextView txtAuthor;
			WebImgView imgBoardPic;
			TextView txtBoardName;
			TextView txtDremCount;
		}

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			int viewId = v.getId();
			int position = (Integer) v.getTag();
			
			DremboardInfo dremboard = getItem(position);
			
			if (dremboard == null)
				return;
			
			switch (viewId) {
			case R.id.imgAuthor:
			case R.id.txtAuthor:
				startActivityDremer(dremboard.media_author_id);
				break;
			case R.id.imgPic:
			case R.id.txtName:
			case R.id.txtDremCount:
				setCurrentDremboard(dremboard);
				startActivityBoardDrems();
				break;
			default:
				break;
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
		case GET_DREMBOARD:
			getDremboardListResult(result);			
			break;
		default:
			break;
		}
	}

	@Override
	public void onSearchBtnClicked(String category, String search_str) {
		// TODO Auto-generated method stub
		mCategory = Integer.parseInt(category);
		mSearchStr = search_str;
		
		mLastCount = 0;
		mLastMediaId = 0;
		
		removeAllDremboards();
		getDremboardList(mLastMediaId, mPerPage);
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
	    if (requestCode == 1) {
	        if(resultCode == Activity.RESULT_OK){
	    		resetOptions();
	    		removeAllDremboards();
	    		loadMoreDrembodrds();
	    		this.getView();
	        }
	        if (resultCode == Activity.RESULT_CANCELED) {
	            //Write your code if there's no result
	        }
	    }
	}//onActivityResult
}
