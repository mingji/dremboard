package com.drem.dremboard.ui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.Spinner;

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

public class FragmentHome extends Fragment implements WebApiCallback
{
	private AppPreferences mPrefs;

	Spinner spin_ActivityScope;
	GridView mGridDremActivities;
	DremActivityAdapter mAdapterDremActivity;
	ArrayList<DremActivityInfo> mArrayDremActivities;

	ProgressBar mProgMore;

	boolean mFlagLoading = false;
	boolean mAddMoreFlag = true;
	int mLastDremActivityId = 0;
	int mPerPage = 5;
	String mDremActivityScope = "all";
	
	//Init activity scope array string & hashMap
	ArrayList<String> mArrayScopeStr = new ArrayList(Arrays.asList("ALL MEMBERS","FOLLOWING","MY FRIENDS","MENTIONS","NOTIFICATINOS"));
	ArrayList<String> mArrayScopeVal = new ArrayList(Arrays.asList("all","following","friends","mentions","notifications"));
	HashMap<String, String> mMapScope;
	
	WaitDialog waitDialog;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View view = inflater.inflate(R.layout.fragment_home, null);
		
		mPrefs = new AppPreferences(getActivity());
		waitDialog = new WaitDialog(getActivity());

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
		
		//Init spin_activityscope control <<
		spin_ActivityScope = (Spinner) view.findViewById(R.id.spinDremActivityOption);
		spin_ActivityScope.setPrompt("Select activity scope");
		ArrayAdapter<String> adapterActivityScope = new ArrayAdapter<String>(
				getActivity(), android.R.layout.simple_spinner_item,
				mArrayScopeStr);
		adapterActivityScope
				.setDropDownViewResource(R.layout.custom_spinner_dropdown_item);
		spin_ActivityScope.setAdapter(adapterActivityScope);

		mMapScope = new HashMap<String, String>();
		for (int i = 0; i < 5; i++) {
			mMapScope.put(mArrayScopeStr.get(i), mArrayScopeVal.get(i));
		}		
		//>>

		//Set spin_activityscope item select listener <<
		spin_ActivityScope.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				String scope_str = spin_ActivityScope.getSelectedItem().toString();
				String scope_val = mMapScope.get(scope_str);

				resetOptions();
				mDremActivityScope = scope_val;
				getDremActivityList(mLastDremActivityId, mPerPage);				
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub

			}
		});
		//>>
		
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
	
	private void resetOptions()
	{
		mFlagLoading = false;
		mAddMoreFlag = true;
		
		mDremActivityScope = "all";
		mLastDremActivityId = 0;
		mPerPage = 5;
		mArrayDremActivities.clear();
	}

	private void loadMoreDremActivities()
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
		param.disp_user_id = -1;//Integer.parseInt(mPrefs.getUserId());
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
