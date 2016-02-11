package com.drem.dremboard.ui;

import java.util.ArrayList;

import com.drem.dremboard.R;
import com.drem.dremboard.entity.GlobalValue;
import com.drem.dremboard.entity.ProfileItem;
import com.drem.dremboard.entity.Beans.GetSingleDremerParam;
import com.drem.dremboard.entity.Beans.GetSingleDremerResult;
import com.drem.dremboard.utils.AppPreferences;
import com.drem.dremboard.view.CustomToast;
import com.drem.dremboard.view.WaitDialog;
import com.drem.dremboard.webservice.Constants;
import com.drem.dremboard.webservice.WebApiCallback;
import com.drem.dremboard.webservice.WebApiInstance;
import com.drem.dremboard.webservice.WebApiInstance.Type;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class FragmentProfileView extends Fragment implements OnClickListener, WebApiCallback
{
	ArrayList<ProfileItem> mArrayProfile = null;
	ProfileAdapter mAdapterProfile;
	ListView mListviewProfile;
	
	ArrayList<ProfileItem> mCurrentProfiles;
	private AppPreferences mPrefs;
	WaitDialog waitDialog;
	
	int mDremerId;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View view = inflater.inflate(R.layout.fragment_profile_view, null);
		
		mCurrentProfiles = GlobalValue.getInstance().getCurrentProfiles();
		mPrefs = new AppPreferences(getActivity());
		waitDialog = new WaitDialog(getActivity());
		
		Bundle bundle = getArguments();
		if (bundle != null) {
			mDremerId = getArguments().getInt("dremer_id", -1);
		} else {
			mDremerId = -1;
		}		

		initView (view);
		return view;
	}

	private void initView (View view)
	{		
		mArrayProfile = new ArrayList<ProfileItem>();
		mAdapterProfile = new ProfileAdapter(getActivity(), R.layout.item_profile, mArrayProfile);

		mListviewProfile = (ListView) view.findViewById(R.id.lstProfile);		
		mListviewProfile.setAdapter(mAdapterProfile);
		
		getSingleDremer();
	}
	
	private void updateView()
	{
		mArrayProfile.clear();
		
		for (ProfileItem item : mCurrentProfiles)
		{
			mArrayProfile.add(item);
		}
		
		mAdapterProfile.notifyDataSetChanged();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

	}
	
	private void getSingleDremer() {
		waitDialog.show();
		
		GetSingleDremerParam param = new GetSingleDremerParam();

		param.user_id = mPrefs.getUserId();
		param.disp_user_id = mDremerId;

		WebApiInstance.getInstance().executeAPI(Type.GET_SINGLE_DREMER, param, this);
	}

	private void getSingleDremerResult(Object param, Object obj) {

		waitDialog.dismiss();

		if (obj == null) {
			CustomToast.makeCustomToastShort(getActivity(), Constants.NETWORK_ERR);
		}

		if (obj != null){
			GetSingleDremerResult resultBean = (GetSingleDremerResult)obj;

			if (resultBean.status.equals("ok")) {
				GlobalValue.getInstance().setCurrentDremer(resultBean.data.member);
				GlobalValue.getInstance().setCurrentProfiles(resultBean.data.profiles);
			} else {
				CustomToast.makeCustomToastShort(getActivity(), resultBean.msg);
			}
		}
		
		updateView();
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
		case GET_SINGLE_DREMER:
			getSingleDremerResult(parameter, result);			
			break;
		default:
			break;
		}
	}
	
	public class ProfileAdapter extends ArrayAdapter<ProfileItem> {
		Activity activity;
		int layoutResourceId;
		ArrayList<ProfileItem> item = new ArrayList<ProfileItem>();

		// constructor
		public ProfileAdapter(Activity activity, int layoutId, ArrayList<ProfileItem> items) {
			super(activity, layoutId, items);
			item = items;
			this.activity = activity;
			this.layoutResourceId = layoutId;
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			ProfileHolder holder = null;	

			ProfileItem profileItem = getItem(position);

			// inflate the view
			if (convertView == null) {
				LayoutInflater inflater = ((Activity) activity).getLayoutInflater();
				convertView = inflater.inflate(R.layout.item_profile, null);
				holder = new ProfileHolder();
								
				holder.txtFieldName = (TextView) convertView.findViewById(R.id.txtFieldName);
				holder.txtFieldValue = (TextView) convertView.findViewById(R.id.txtFieldValue);

				convertView.setTag(holder);
			} 
			else
				holder = (ProfileHolder) convertView.getTag();

			holder.txtFieldName.setText(profileItem.name);
			holder.txtFieldValue.setText(profileItem.value);
			
			return convertView;
		}

		public class ProfileHolder {
			TextView txtFieldName;
			TextView txtFieldValue;
		}
	}
	
}
