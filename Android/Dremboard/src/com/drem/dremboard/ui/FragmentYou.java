package com.drem.dremboard.ui;

import java.util.ArrayList;

import com.drem.dremboard.R;
import com.drem.dremboard.entity.FunctionInfo;
import com.drem.dremboard.utils.AppPreferences;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class FragmentYou extends Fragment implements OnClickListener
{
	ArrayList<FunctionInfo> mArrayFunction = null;
	FunctionAdapter mAdapterFunction;
	ListView mListviewFunction;
	
	AppPreferences mPrefs;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View view = inflater.inflate(R.layout.fragment_you, null);
		
		mPrefs = new AppPreferences(getActivity());

		initView (view);

		return view;
	}

	private void initView (View view)
	{
		mArrayFunction = new ArrayList<FunctionInfo>();
		mArrayFunction.add(new FunctionInfo("Activity"));
		mArrayFunction.add(new FunctionInfo("Profile"));
		mArrayFunction.add(new FunctionInfo("Notifications"));
		mArrayFunction.add(new FunctionInfo("Messages"));
		mArrayFunction.add(new FunctionInfo("Friends"));
		mArrayFunction.add(new FunctionInfo("Family"));
		mArrayFunction.add(new FunctionInfo("Groups"));
		mArrayFunction.add(new FunctionInfo("Following"));
		mArrayFunction.add(new FunctionInfo("Followers"));
		mArrayFunction.add(new FunctionInfo("Setting"));
		mArrayFunction.add(new FunctionInfo("Media"));
		
		mAdapterFunction = new FunctionAdapter(getActivity(), R.layout.item_more, mArrayFunction);

		mListviewFunction = (ListView) view.findViewById(R.id.lstMore);		
		mListviewFunction.setAdapter(mAdapterFunction);
		mListviewFunction.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View view,
					int position, long id) {
				onClickYouFunction(position);
			}
		});
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

	}
	
	private void onClickYouFunction (int position)
	{
		int user_id = Integer.parseInt(mPrefs.getUserId());
		Intent intent = new Intent();
		
		switch (position)
		{
		case 0:
			intent.setClass(getActivity(), ActivityActContent.class);
			intent.putExtra("dremer_id", user_id);
			break;
		case 1:
			intent.setClass(getActivity(), ActivityProfile.class);
			intent.putExtra("dremer_id", user_id);
			break;
		case 2:
			intent.setClass(getActivity(), ActivityNotification.class);
			intent.putExtra("dremer_id", user_id);
			break;
		case 3:
			intent.setClass(getActivity(), ActivityMessages.class);
			intent.putExtra("dremer_id", user_id);
			break;
		case 4:
			intent.setClass(getActivity(), ActivityFriends.class);
			intent.putExtra("dremer_id", user_id);
			break;
		case 5:
			intent.setClass(getActivity(), ActivityFamily.class);
			intent.putExtra("dremer_id", user_id);
			break;
		case 6:
			intent.setClass(getActivity(), ActivityGroup.class);
			intent.putExtra("dremer_id", user_id);
			break;
		case 7:
			intent.setClass(getActivity(), ActivityFollow.class);
			intent.putExtra("dremer_id", user_id);
			break;
		case 8:
			intent.setClass(getActivity(), ActivityFollowers.class);
			intent.putExtra("dremer_id", user_id);
			break;
		case 9:
			intent.setClass(getActivity(), ActivitySetting.class);
			intent.putExtra("dremer_id", user_id);
			break;
		case 10:
			intent.setClass(getActivity(), ActivityMedia.class);
			intent.putExtra("dremer_id", user_id);
			break;
		}
		
		startActivity(intent);
		getActivity().overridePendingTransition(R.anim.in_right_left, R.anim.out_right_left);
	}
	
	public class FunctionAdapter extends ArrayAdapter<FunctionInfo> {
		Activity activity;
		int layoutResourceId;
		ArrayList<FunctionInfo> item = new ArrayList<FunctionInfo>();

		// constuctor
		public FunctionAdapter(Activity activity, int layoutId, ArrayList<FunctionInfo> items) {
			super(activity, layoutId, items);
			item = items;
			this.activity = activity;
			this.layoutResourceId = layoutId;
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			FunctionHolder holder = null;	

			FunctionInfo function = getItem(position);

			// inflate the view
			if (convertView == null) {
				LayoutInflater inflater = ((Activity) activity).getLayoutInflater();
				convertView = inflater.inflate(R.layout.item_more, null);
				holder = new FunctionHolder();
				holder.txtName = (TextView) convertView.findViewById(R.id.txtFunction);

				convertView.setTag(holder);
			} 
			else
				holder = (FunctionHolder) convertView.getTag();

			holder.txtName.setText(function.mName);		

			return convertView;
		}

		public class FunctionHolder {
			TextView txtName;
		}
	}
}
