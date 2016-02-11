package com.drem.dremboard.ui;

import java.util.ArrayList;

import com.drem.dremboard.R;
import com.drem.dremboard.entity.FunctionInfo;
import com.drem.dremboard.utils.AppPreferences;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
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
		mArrayFunction.add(new FunctionInfo("Post"));
		mArrayFunction.add(new FunctionInfo("Log Out"));

		
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

		case 11:
			intent.setClass(getActivity(), ActivityDremPost.class);
			intent.putExtra("dremer_id", user_id);
			break;

		case 12:
			actionLogOut();
			return;
		}
		
		startActivity(intent);
		getActivity().overridePendingTransition(R.anim.in_right_left, R.anim.out_right_left);
	}
	
	private void actionLogOut() {
		TextView title = new TextView(getActivity());
		title.setText(R.string.text_title_logout);
		title.setTextSize(18);
		title.setPadding(0, 40, 0, 40);
		title.setGravity(Gravity.CENTER);
						
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
			getActivity());
		alertDialogBuilder.setCustomTitle(title);
		alertDialogBuilder
			.setMessage("Are you sure you want to" + "\n" + "log out?")
			.setCancelable(false)
			.setPositiveButton("Log Out",new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog,int id) {
					getActivity().finish();
					Intent intent = new Intent(getActivity(), ActivityLogin.class);
					intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); 
					intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					startActivity(intent);
					getActivity().overridePendingTransition(R.anim.in_right_left, R.anim.out_right_left);
				}
			  })
			.setNegativeButton("Cancel",new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog,int id) {
					dialog.cancel();
				}
			});

		// create alert dialog
		AlertDialog alertDialog = alertDialogBuilder.create();
		// show it
		alertDialog.show();
		TextView messageText = (TextView)alertDialog.findViewById(android.R.id.message);
        messageText.setGravity(Gravity.CENTER);
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
