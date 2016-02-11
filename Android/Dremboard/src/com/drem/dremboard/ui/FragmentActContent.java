package com.drem.dremboard.ui;

import java.util.ArrayList;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.GridView;
import android.widget.ProgressBar;

import com.drem.dremboard.R;
import com.drem.dremboard.adapter.DremActivityAdapter;
import com.drem.dremboard.entity.Beans.GetActivitiesParam;
import com.drem.dremboard.entity.Beans.GetActivitiesResult;
import com.drem.dremboard.entity.DremActivityInfo;
import com.drem.dremboard.utils.AppPreferences;
import com.drem.dremboard.view.CustomToast;
import com.drem.dremboard.view.WaitDialog;
import com.drem.dremboard.webservice.Constants;
import com.drem.dremboard.webservice.WebApiCallback;
import com.drem.dremboard.webservice.WebApiInstance;
import com.drem.dremboard.webservice.WebApiInstance.Type;

public class FragmentActContent extends Fragment implements WebApiCallback
{
	public static FragmentActContent instance;
	private AppPreferences mPrefs;

	GridView mGridDremActivities;
	DremActivityAdapter mAdapterDremActivity;
	ArrayList<DremActivityInfo> mArrayDremActivities;

	ProgressBar mProgMore;

	boolean mFlagLoading = false;
	boolean mAddMoreFlag = true;
	int mLastDremActivityId = 0;
	int mPerPage = 5;
	String mDremActivityScope;
	
	int mDremerId;
	
	WaitDialog waitDialog;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View view = inflater.inflate(R.layout.fragment_act_content, null);
		
		instance = this;
		mPrefs = new AppPreferences(getActivity());
		waitDialog = new WaitDialog(getActivity());
		
		Bundle bundle = getArguments();
		if (bundle != null) {
			mDremActivityScope = bundle.getString("scope", "just-me");
			mDremerId = bundle.getInt("dremer_id", -1);
		} else {
			mDremActivityScope = "just-me";
			mDremerId = Integer.parseInt(mPrefs.getUserId());
		}
		
		if (mDremerId == -1) {
			mDremerId = Integer.parseInt(mPrefs.getUserId());
		}

		initView(view);
		
		resetOptions();

		getDremActivityList(mLastDremActivityId, mPerPage);

		return view;
	}

	private void initView (View view)
	{
		mProgMore = (ProgressBar) view.findViewById(R.id.progMore);

		if (mFlagLoading)
			mProgMore.setVisibility(View.VISIBLE);
		else
			mProgMore.setVisibility(View.GONE);
		
		mArrayDremActivities = new ArrayList<DremActivityInfo>();
		mAdapterDremActivity = new DremActivityAdapter(getActivity(), R.layout.item_drem_activity,
				mArrayDremActivities);

		mGridDremActivities = (GridView) view.findViewById(R.id.gridDremActivities);
		mGridDremActivities.setAdapter(mAdapterDremActivity);
		
		mGridDremActivities.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				// TODO Auto-generated method stub
				
				if (!mAddMoreFlag)
					return;
				
				int lastInScreen = firstVisibleItem + visibleItemCount;
				if (totalItemCount != 0 && (lastInScreen == totalItemCount)
						&& !mFlagLoading) {
					loadMoreDremActivities();
				}
			}
		});
	}
	
	public void resetOptions()
	{
		mFlagLoading = false;
		mAddMoreFlag = true;
		
		mLastDremActivityId = 0;
		mPerPage = 5;
	}

	public void loadMoreDremActivities()
	{
		getDremActivityList(mLastDremActivityId, mPerPage);
	}

	private void addDremActivities(ArrayList<DremActivityInfo> arrayDremActivities) {

		if (arrayDremActivities == null)
			return;
		
		for (DremActivityInfo item : arrayDremActivities) {
			mArrayDremActivities.add(item);
			mLastDremActivityId = item.activity_id;
		}

		mAdapterDremActivity.notifyDataSetChanged();
	}
	
	private void getDremActivityList(int lastId, int count)	{

		if (mFlagLoading)
			return;

		mFlagLoading = true;
		mProgMore.setVisibility(View.VISIBLE);

		_getDremActivityList(lastId, count);
	}
	
	private void _getDremActivityList(int lastId, int count)	{
		GetActivitiesParam param = new GetActivitiesParam();
		
		param.user_id = mPrefs.getUserId();
		param.disp_user_id = mDremerId;
		param.activity_scope = mDremActivityScope;		
		param.per_page = count;
		param.last_activity_id = lastId;

		waitDialog.show();

		WebApiInstance.getInstance().executeAPI(Type.GET_DREM_ACTIVITY, param, this);
	}

	private void getDremActivityListResult(Object obj) {

		waitDialog.dismiss();
		mFlagLoading = false;
		mProgMore.setVisibility(View.GONE);
		
		if (obj == null) {
			CustomToast.makeCustomToastShort(getActivity(), Constants.NETWORK_ERR);
		}

		if (obj != null){
			GetActivitiesResult resultBean = (GetActivitiesResult)obj;

			if (resultBean.status.equals("ok")) {
				
				if (resultBean.data.activity.size() == 0) {
					mAddMoreFlag = false;
					return;
				}
				
				addDremActivities(resultBean.data.activity);
			} else {
				CustomToast.makeCustomToastShort(getActivity(), resultBean.msg);
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
		case GET_DREM_ACTIVITY:
			getDremActivityListResult(result);			
			break;
		default:
			break;
		}
	}
}
