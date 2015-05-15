package com.drem.dremboard.ui;

import java.util.ArrayList;

import com.drem.dremboard.R;
import com.drem.dremboard.entity.Beans.*;
import com.drem.dremboard.entity.DremerInfo;
import com.drem.dremboard.entity.GlobalValue;
import com.drem.dremboard.ui.DialogDremerBlocking.OnSetBlockResultCallback;
import com.drem.dremboard.ui.DialogFriendshipAccept.OnFriendshipResultCallback;
import com.drem.dremboard.utils.AppPreferences;
import com.drem.dremboard.utils.ImageLoader;
import com.drem.dremboard.utils.Utility;
import com.drem.dremboard.view.CustomToast;
import com.drem.dremboard.view.HyIconView;
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
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.AbsListView.OnScrollListener;

public class FragmentDremers extends Fragment
		implements OnClickListener, WebApiCallback, OnFriendshipResultCallback, OnSetBlockResultCallback
{
	private AppPreferences mPrefs;

	ProgressBar mProgMore;

	String mSearchStr = "";
	int mLastPageNum = 1;
	int mPerPage = 10;
	int mLastCount = 1;

	boolean mFlagLoading = false;

	ArrayList<DremerInfo> mArrayDremer = null;
	DremerAdapter mAdapterDremer;
	ListView mListviewDremer;
	
	int mDremerId;
	String mType;
	
	WaitDialog waitDialog;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View view = inflater.inflate(R.layout.fragment_dremers, null);
		
		mPrefs = new AppPreferences(getActivity());
		waitDialog = new WaitDialog(getActivity());
		
		Bundle bundle = getArguments();
		if (bundle != null) {
			mType = getArguments().getString("type", "active");
			mDremerId = getArguments().getInt("dremer_id", -1);
		} else {
			mType = "active";
			mDremerId = -1;
		}		

		initView (view);
		
		resetOptions();
		
		getDremerList(mLastPageNum, mPerPage);

		return view;
	}

	private void initView (View view)
	{
		mProgMore = (ProgressBar) view.findViewById(R.id.progMore);
		if (mFlagLoading)
			mProgMore.setVisibility(View.VISIBLE);
		else
			mProgMore.setVisibility(View.GONE);
		
		mArrayDremer = new ArrayList<DremerInfo>();
		mAdapterDremer = new DremerAdapter(getActivity(), R.layout.item_dremer, mArrayDremer);

		mListviewDremer = (ListView) view.findViewById(R.id.lstDremer);		
		mListviewDremer.setAdapter(mAdapterDremer);
		mListviewDremer.setOnScrollListener(new OnScrollListener() {

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
					loadMoreDremers();
				}				
			}
		});
	}
	
	private void resetOptions()
	{
		mSearchStr = "";
		mLastPageNum = 1;
		mPerPage = 10;
		mLastCount = 1;
	}
	
	private void startActivityDremer (int dremerId)
	{
		Intent intent = new Intent();
		intent.setClass(getActivity(), ActivityDremer.class);
		intent.putExtra("dremer_id", dremerId);
		startActivity(intent);
		getActivity().overridePendingTransition(R.anim.in_right_left, R.anim.out_right_left);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

	}
	
	private void updateActionView (String type, Object view, int index, Object value)
	{
		DremerInfo dremerItem = mArrayDremer.get(index);
		Button button = (Button) view;
		
		if (dremerItem == null || button == null)
			return;		
		
		if (type.equalsIgnoreCase("friendship")) {
			String status = (String) value;
			dremerItem.friendship_status = status;
			button.setText(Utility.getFriendshipStr(status));
			if (status.equals("not_friends"))
				button.setBackgroundResource(R.drawable.btn_register);
			else
				button.setBackgroundResource(R.drawable.btn_unblock);
		} else if (type.equalsIgnoreCase("following")) {
			int is_following = (Integer) value;
			dremerItem.is_following = is_following;
			if (is_following == 0) {
				button.setText("Follow");
			} else {
				button.setText("Stop Following");
			}
		} else if (type.equalsIgnoreCase("blocking")) {
			int block_type = (Integer) value;
			dremerItem.block_type = block_type;
			if (block_type == 0) {
				button.setText("Block");
				button.setBackgroundResource(R.drawable.btn_register);
			} else {
				button.setText("Unblock");
				button.setBackgroundResource(R.drawable.btn_unblock);
			}
		}
	}
	
	private void loadMoreDremers()
	{
		mFlagLoading = false;
		mProgMore.setVisibility(View.GONE);
		mLastPageNum++;
		getDremerList(mLastPageNum, mPerPage);
	}
	
	private void addDremers(ArrayList<DremerInfo> arrayDremers) {

		if (arrayDremers == null)
			return;
		
		for (DremerInfo dremer : arrayDremers) {
			mArrayDremer.add(dremer);
		}

		mAdapterDremer.notifyDataSetChanged();
	}

	private void getDremerList(int pagenum, int count)	{

		if (mFlagLoading)
			return;

		mFlagLoading = true;
		mProgMore.setVisibility(View.VISIBLE);

		_getDremerList(pagenum, count);
	}
	
	private void _getDremerList(int pagenum, int count)	{
		GetDremersParam param = new GetDremersParam();

		param.user_id = mPrefs.getUserId();
		param.disp_user_id = mDremerId;
		param.page = pagenum;
		param.per_page = count;
		param.search_str = mSearchStr;
		param.type = mType;

		WebApiInstance.getInstance().executeAPI(Type.GET_DREMER, param, this);
	}

	private void showAcceptDialog (DremerInfo dremer, int index)
	{
		DialogFriendshipAccept acceptDiag = new DialogFriendshipAccept(
				getActivity(), getActivity(), dremer, index, this);
		acceptDiag.show();
	}
	
	private void showBlockingDialog (DremerInfo dremer, int index)
	{
		DialogDremerBlocking acceptDiag = new DialogDremerBlocking(
				getActivity(), getActivity(), dremer.user_id, index, this);
		acceptDiag.show();
	}
	
	private void getDremerListResult(Object obj) {

		mFlagLoading = false;
		mProgMore.setVisibility(View.GONE);

		if (obj == null) {
			CustomToast.makeCustomToastShort(getActivity(), Constants.NETWORK_ERR);
		}

		if (obj != null){
			GetDremersResult resultBean = (GetDremersResult)obj;

			if (resultBean.status.equals("ok")) {
				mLastCount = resultBean.data.member.size();
				addDremers(resultBean.data.member);
			} else {
				CustomToast.makeCustomToastShort(getActivity(), resultBean.msg);
			}
		}
	}
	
	private void setDremerFriendship(int dremerId, String action, Object exParam, int index) {
		waitDialog.show();
		
		SetFriendshipParam param = new SetFriendshipParam();

		param.user_id = mPrefs.getUserId();
		param.dremer_id = dremerId;
		param.action = action;
		
		param.exParam = exParam;
		param.index = index;

		WebApiInstance.getInstance().executeAPI(Type.SET_FRIENDSHIP, param, this);
	}

	private void setDremerFriendshipResult(Object param, Object obj) {

		waitDialog.dismiss();

		if (obj == null) {
			CustomToast.makeCustomToastShort(getActivity(), Constants.NETWORK_ERR);
		}

		if (obj != null){
			SetFriendshipResult resultBean = (SetFriendshipResult)obj;

			if (resultBean.status.equals("ok")) {
				SetFriendshipParam friendshipParam = (SetFriendshipParam) param;
				updateActionView ("friendship",
						friendshipParam.exParam, friendshipParam.index,
						resultBean.data.friendship_status);
			} else {
				CustomToast.makeCustomToastShort(getActivity(), resultBean.msg);
			}
		}
	}
	
	private void setDremerFollowing(int dremerId, String action, Object exParam, int index) {
		waitDialog.show();
		
		SetFollowingParam param = new SetFollowingParam();

		param.user_id = mPrefs.getUserId();
		param.dremer_id = dremerId;
		param.action = action;
		
		param.exParam = exParam;
		param.index = index;

		WebApiInstance.getInstance().executeAPI(Type.SET_FOLLOW, param, this);
	}

	private void setDremerFollowingResult(Object param, Object obj) {

		waitDialog.dismiss();

		if (obj == null) {
			CustomToast.makeCustomToastShort(getActivity(), Constants.NETWORK_ERR);
		}

		if (obj != null){
			SetFollowingResult resultBean = (SetFollowingResult)obj;

			if (resultBean.status.equals("ok")) {
				SetFollowingParam followParam = (SetFollowingParam) param;
				updateActionView ("following",
						followParam.exParam, followParam.index,
						resultBean.data.is_follow);
			} else {
				CustomToast.makeCustomToastShort(getActivity(), resultBean.msg);
			}
		}
	}
	
	private void setDremerBlocking(int dremerId, String action, Object exParam, int index) {
		waitDialog.show();
		
		SetBlockingParam param = new SetBlockingParam();

		param.user_id = mPrefs.getUserId();
		param.dremer_id = dremerId;
		param.action = action;
		param.block_type = 0;
		
		param.exParam = exParam;
		param.index = index;

		WebApiInstance.getInstance().executeAPI(Type.SET_BLOCK, param, this);
	}

	private void setDremerBlockingResult(Object param, Object obj) {

		waitDialog.dismiss();

		if (obj == null) {
			CustomToast.makeCustomToastShort(getActivity(), Constants.NETWORK_ERR);
		}

		if (obj != null){
			SetBlockingResult resultBean = (SetBlockingResult)obj;

			if (resultBean.status.equals("ok")) {
				SetBlockingParam blockParam = (SetBlockingParam) param;
				updateActionView ("blocking",
						blockParam.exParam, blockParam.index,
						resultBean.data.block_type);
			} else {
				CustomToast.makeCustomToastShort(getActivity(), resultBean.msg);
			}
		}
	}
	
	public class DremerAdapter extends ArrayAdapter<DremerInfo> implements OnClickListener {
		Activity activity;
		int layoutResourceId;
		ArrayList<DremerInfo> item = new ArrayList<DremerInfo>();

		// constructor
		public DremerAdapter(Activity activity, int layoutId, ArrayList<DremerInfo> items) {
			super(activity, layoutId, items);
			item = items;
			this.activity = activity;
			this.layoutResourceId = layoutId;
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			DremerHolder holder = null;	

			DremerInfo dremer = getItem(position);

			// inflate the view
			if (convertView == null) {
				LayoutInflater inflater = ((Activity) activity).getLayoutInflater();
				convertView = inflater.inflate(R.layout.item_dremer, null);
				holder = new DremerHolder();
				
				holder.btnFriendship = (Button) convertView.findViewById(R.id.btnAdd);
				holder.btnBlock = (Button) convertView.findViewById(R.id.btnBlock);
				holder.btnFollow = (Button) convertView.findViewById(R.id.btnFollow);				
								
				holder.txtName = (TextView) convertView.findViewById(R.id.txtUserName);
				holder.txtUptime = (TextView) convertView.findViewById(R.id.txtUptime);
				
				holder.layLastContent = (LinearLayout) convertView.findViewById(R.id.layLastContent);
				holder.txtLastContent = (TextView) convertView.findViewById(R.id.txtLastContent);
				holder.txtViewContent = (TextView) convertView.findViewById(R.id.txtViewContent);
				
				holder.imgUser = (HyIconView) convertView.findViewById(R.id.imgUser);
				
				holder.btnFriendship.setOnClickListener(this);
				holder.txtName.setOnClickListener(this);
				holder.btnBlock.setOnClickListener(this);
				holder.btnFollow.setOnClickListener(this);
				holder.imgUser.setOnClickListener(this);

				convertView.setTag(holder);
			} 
			else
				holder = (DremerHolder) convertView.getTag();

			holder.txtName.setTag(position);
			holder.txtName.setText(dremer.display_name);
			
			holder.txtUptime.setText(dremer.last_activity);
			
			if (dremer.latest_update != null && dremer.latest_update.id != -1) {
				holder.layLastContent.setVisibility(View.VISIBLE);
				holder.txtLastContent.setText("\"" + dremer.latest_update.content + "\"" );
			} else
				holder.layLastContent.setVisibility(View.GONE);
			
			if (dremer.user_avatar != null && !dremer.user_avatar.isEmpty())
				ImageLoader.getInstance().displayImage(dremer.user_avatar, holder.imgUser, 0, 0);
			else
				holder.imgUser.imageView.setImageResource(R.drawable.empty_man);
			holder.imgUser.setTag(position);
			
			holder.btnFriendship.setTag(position);
			holder.btnFriendship.setText(Utility.getFriendshipStr(dremer.friendship_status));
			if (dremer.friendship_status.equals("not_friends"))
				holder.btnFriendship.setBackgroundResource(R.drawable.btn_register);
			else
				holder.btnFriendship.setBackgroundResource(R.drawable.btn_unblock);
			
			holder.btnBlock.setTag(position);
			if (dremer.block_type == 0) {
				holder.btnBlock.setText("Block");
				holder.btnBlock.setBackgroundResource(R.drawable.btn_register);
			} else {
				holder.btnBlock.setText("Unblock");
				holder.btnBlock.setBackgroundResource(R.drawable.btn_unblock);
			}
			
			holder.btnFollow.setTag(position);
			if (dremer.is_following == 0)
				holder.btnFollow.setText("Follow");
			else
				holder.btnFollow.setText("Stop Following");
			
			String my_id = mPrefs.getUserId();
			if (dremer.user_id == Integer.parseInt(my_id)) {
				holder.btnFriendship.setVisibility(View.GONE);
				holder.btnBlock.setVisibility(View.GONE);
				holder.btnFollow.setVisibility(View.GONE);
			} else {
				holder.btnFriendship.setVisibility(View.VISIBLE);
				holder.btnBlock.setVisibility(View.VISIBLE);
				holder.btnFollow.setVisibility(View.VISIBLE);
			}

			return convertView;
		}

		public class DremerHolder {
			TextView txtName;
			TextView txtUptime;
			HyIconView imgUser;
			
			LinearLayout layLastContent;
			TextView txtLastContent;
			TextView txtViewContent;
			
			Button btnFriendship;
			Button btnBlock;
			Button btnFollow;
		}

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			int viewId = v.getId();
			int position = (Integer) v.getTag();
			
			DremerInfo dremerItem = getItem(position);
			
			if (dremerItem == null)
				return;
			
			switch (viewId) {
			case R.id.imgUser:
			case R.id.txtUserName:
				startActivityDremer(dremerItem.user_id);
				break;
			case R.id.btnAdd:
				if (dremerItem.friendship_status.equals("awaiting_response")) {
					showAcceptDialog(dremerItem, position);
				} else {
					setDremerFriendship(dremerItem.user_id,
							Utility.getFriendshipAction(dremerItem.friendship_status),
							v, position);
				}
				break;
			case R.id.btnBlock:
				if (dremerItem.block_type == 0) {
					showBlockingDialog(dremerItem, position);
				} else {
					setDremerBlocking(dremerItem.user_id, "unblock", v, position);
				}
				break;
			case R.id.btnFollow:
				String action;
				if (dremerItem.is_following == 0)
					action = "start";
				else
					action = "stop";
				setDremerFollowing(dremerItem.user_id, action, v, position); 
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
		case GET_DREMER:
			getDremerListResult(result);			
			break;
		case SET_FRIENDSHIP:
			setDremerFriendshipResult(parameter, result);			
			break;
		case SET_FOLLOW:
			setDremerFollowingResult(parameter, result);			
			break;
		case SET_BLOCK:
			setDremerBlockingResult(parameter, result);			
			break;
		default:
			break;
		}
	}

	@Override
	public void OnFriendshipResult(String friendshipStatus, int index) {
		// TODO Auto-generated method stub
		if (index > mArrayDremer.size() - 1)
			return;
		
		DremerInfo dremer = mArrayDremer.get(index);
		if (dremer == null)
			return;
		
		dremer.friendship_status = friendshipStatus;
		mAdapterDremer.notifyDataSetChanged();
	}

	@Override
	public void OnSetBlockResult(int block_type, int index) {
		// TODO Auto-generated method stub
		if (index > mArrayDremer.size() - 1)
			return;
		
		DremerInfo dremer = mArrayDremer.get(index);
		if (dremer == null)
			return;
		
		dremer.block_type = block_type;
		mAdapterDremer.notifyDataSetChanged();
	}
}
