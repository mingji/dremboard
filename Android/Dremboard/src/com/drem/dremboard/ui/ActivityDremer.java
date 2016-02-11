package com.drem.dremboard.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.TextView;

import com.drem.dremboard.R;
import com.drem.dremboard.entity.Beans.GetSingleDremerParam;
import com.drem.dremboard.entity.Beans.GetSingleDremerResult;
import com.drem.dremboard.entity.Beans.SetFamilyshipParam;
import com.drem.dremboard.entity.Beans.SetFamilyshipResult;
import com.drem.dremboard.entity.DremerInfo;
import com.drem.dremboard.entity.GlobalValue;
import com.drem.dremboard.entity.Beans.SetFollowingParam;
import com.drem.dremboard.entity.Beans.SetFollowingResult;
import com.drem.dremboard.entity.Beans.SetFriendshipParam;
import com.drem.dremboard.entity.Beans.SetFriendshipResult;
import com.drem.dremboard.ui.DialogFamilyshipAccept.OnFamilyshipResultCallback;
import com.drem.dremboard.ui.DialogFriendshipAccept.OnFriendshipResultCallback;
import com.drem.dremboard.utils.AppPreferences;
import com.drem.dremboard.utils.ImageLoader;
import com.drem.dremboard.utils.Utility;
import com.drem.dremboard.view.CustomToast;
import com.drem.dremboard.view.WebImgView;
import com.drem.dremboard.view.WaitDialog;
import com.drem.dremboard.webservice.Constants;
import com.drem.dremboard.webservice.WebApiCallback;
import com.drem.dremboard.webservice.WebApiInstance;
import com.drem.dremboard.webservice.WebApiInstance.Type;

public class ActivityDremer extends Activity
		implements OnClickListener, WebApiCallback, OnFriendshipResultCallback, OnFamilyshipResultCallback {

	private AppPreferences mPrefs;
	
	WebImgView mImgDremer;
	WebImgView mImgUserIcon;
	TextView mTxtTitle;	
	TextView mTxtName, mTxtBio, mTxtUptime;
	LinearLayout mLayLastContent;
	TextView mTxtLastContent, mTxtViewContent;

	Button mBtnBack;
	Button mBtnFriend, mBtnFamily, mBtnFollow;
	Button mBtnPubMsg, mBtnPrivMsg;

	ExpandableListAdapter mMenuAdapter;
	ExpandableListView mMenuLstView;

	List<String> mListMenuHeader;
	HashMap<String, List<String>> mListMenuChild;
	
	int mDremerId;
	
	DremerInfo mDremer;
	
	WaitDialog waitDialog;

	public static ActivityDremer instance;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_dremer);
		mPrefs = new AppPreferences(this);
		waitDialog = new WaitDialog(this);
		
		instance = this;
		
		mDremerId = getIntent().getIntExtra("dremer_id", -1);
		if (mDremerId == -1) { // check valid dremer
			finish();
			return;
		}

		initMenuData();
		initView();
		getSingleDremer(mDremerId);
//		updateView(mDremer);
	}

	private void initMenuData()
	{
		mListMenuHeader = new ArrayList<String>();
		mListMenuChild = new HashMap<String, List<String>>();

		// Adding child data
		mListMenuHeader.add("ACTIVITY");
		mListMenuHeader.add("PROFILE");
		mListMenuHeader.add("FRIENDS");
		mListMenuHeader.add("FAMILY");
		mListMenuHeader.add("FOLLOWING");
		mListMenuHeader.add("FOLLOWERS");
		mListMenuHeader.add("GROUPS");
		mListMenuHeader.add("MEDIA");

		// Adding child data
		List<String> menu_activity = new ArrayList<String>();
		menu_activity.add("Personal");
		menu_activity.add("Mentions");
		menu_activity.add("Followings");
		menu_activity.add("Favorites");
		menu_activity.add("Friends");
		menu_activity.add("Groups");

		List<String> menu_profile = new ArrayList<String>();
		menu_profile.add("View");
		
		List<String> menu_friends = new ArrayList<String>();
		menu_friends.add("Friends");
		
		List<String> menu_family = new ArrayList<String>();
		menu_family.add("Family");
		
		List<String> menu_following = new ArrayList<String>();
		menu_following.add("Following");
		
		List<String> menu_follower = new ArrayList<String>();
		menu_follower.add("Followers");
		
		List<String> menu_group = new ArrayList<String>();
		menu_group.add("Memberships");
		
		List<String> menu_media = new ArrayList<String>();
		menu_media.add("Drems");
		menu_media.add("Dremboards");
		menu_media.add("Dremcast");
		
		mListMenuChild.put(mListMenuHeader.get(0), menu_activity); // Header, Child data
		mListMenuChild.put(mListMenuHeader.get(1), menu_profile);
		mListMenuChild.put(mListMenuHeader.get(2), menu_friends);
		mListMenuChild.put(mListMenuHeader.get(3), menu_family);
		mListMenuChild.put(mListMenuHeader.get(4), menu_following);
		mListMenuChild.put(mListMenuHeader.get(5), menu_follower);
		mListMenuChild.put(mListMenuHeader.get(6), menu_group);
		mListMenuChild.put(mListMenuHeader.get(7), menu_media);
		
	}

	private void initView() {
		
		mImgUserIcon = (WebImgView) findViewById(R.id.imgUserIcon);
		mImgUserIcon.imageView.setImageResource(R.drawable.empty_man);
		DremerInfo dremer = GlobalValue.getInstance().getCurrentDremer();
		if (dremer.user_avatar != null && !dremer.user_avatar.isEmpty())
			ImageLoader.getInstance().displayImage(dremer.user_avatar, mImgUserIcon, 0, 0);
		
		mImgUserIcon.setOnClickListener(this);
		
		mImgDremer = (WebImgView) findViewById(R.id.imgDremer);
		mImgDremer.imageView.setImageResource(R.drawable.empty_man);
		mImgDremer.setOnClickListener(this);
		
		mTxtTitle = (TextView) findViewById(R.id.txtTitle);
		
		mTxtName = (TextView) findViewById(R.id.txtName);
		mTxtBio = (TextView) findViewById(R.id.txtBio);
		mTxtUptime = (TextView) findViewById(R.id.txtUptime);
		
		mLayLastContent = (LinearLayout) findViewById(R.id.layLastContent);
		mTxtLastContent = (TextView) findViewById(R.id.txtLastContent);
		mTxtViewContent = (TextView) findViewById(R.id.txtViewContent);

		mBtnBack = (Button) findViewById(R.id.btnBack);
		mBtnBack.setOnClickListener(this);

		mBtnFriend = (Button) findViewById(R.id.btnFriend);
		mBtnFamily = (Button) findViewById(R.id.btnFamily);
		mBtnFollow = (Button) findViewById(R.id.btnFollow);

		mBtnPubMsg = (Button) findViewById(R.id.btnPubMsg);
		mBtnPrivMsg = (Button) findViewById(R.id.btnPrivMsg);

		mBtnFriend.setOnClickListener(this);
		mBtnFamily.setOnClickListener(this);
		mBtnFollow.setOnClickListener(this);

		mBtnPubMsg.setOnClickListener(this);
		mBtnPrivMsg.setOnClickListener(this);

		mMenuLstView = (ExpandableListView) findViewById(R.id.lstMenu);
		mMenuAdapter = new ExpandableListAdapter(this, mListMenuHeader, mListMenuChild);
		mMenuLstView.setAdapter(mMenuAdapter);
		
		mMenuLstView.setOnGroupClickListener(new OnGroupClickListener() {

			@Override
			public boolean onGroupClick(ExpandableListView parent, View v,
					int groupPosition, long id) {
				// TODO Auto-generated method stub
				if (mListMenuChild.get(mListMenuHeader.get(groupPosition)).size() <= 0)
					onClickMenuHeader(groupPosition, 0);
				
				return false;
			}
		});
		mMenuLstView.setOnChildClickListener(new OnChildClickListener() {

			@Override
			public boolean onChildClick(ExpandableListView parent, View v,
					int groupPosition, int childPosition, long id) {
				// TODO Auto-generated method stub
				onClickMenuHeader(groupPosition, childPosition);
				return false;
			}
			
		});
	}
	
	private void updateView (DremerInfo dremer)
	{
		if (dremer == null || dremer.user_id == -1)
			return;
		
		if (dremer.user_avatar != null && !dremer.user_avatar.isEmpty())
			ImageLoader.getInstance().displayImage(dremer.user_avatar, mImgDremer, 0, 0);
		else
			mImgDremer.imageView.setImageResource(R.drawable.empty_man);
		
		mTxtTitle.setText(dremer.display_name);
		mTxtName.setText(dremer.display_name);
		mTxtBio.setText(dremer.fullname);
		mTxtUptime.setText(dremer.last_activity);

		if (dremer.latest_update != null && dremer.latest_update.id != -1) {
			mLayLastContent.setVisibility(View.VISIBLE);
			mTxtLastContent.setText("\"" + dremer.latest_update.content + "\"" );
		} else
			mLayLastContent.setVisibility(View.GONE);
		
		mBtnFriend.setText(Utility.getFriendshipStr(dremer.friendship_status));
		if (dremer.friendship_status.equals("not_friends"))
			mBtnFriend.setBackgroundResource(R.drawable.btn_register);
		else
			mBtnFriend.setBackgroundResource(R.drawable.btn_unblock);
		
		mBtnFamily.setText(Utility.getFamilyshipStr(dremer.familyship_status));
		if (dremer.familyship_status.equals("not_familys"))
			mBtnFamily.setBackgroundResource(R.drawable.btn_register);
		else
			mBtnFamily.setBackgroundResource(R.drawable.btn_unblock);
		
		if (dremer.is_following == 0)
			mBtnFollow.setText("Follow");
		else
			mBtnFollow.setText("Stop Following");
	}
	
	private void updateActionView (String type, Object view, Object value)
	{
		Button button = (Button) view;
		
		if (mDremer == null || button == null)
			return;		
		
		if (type.equalsIgnoreCase("friendship")) {
			String status = (String) value;
			mDremer.friendship_status = status;
			button.setText(Utility.getFriendshipStr(status));
			if (status.equals("not_friends"))
				button.setBackgroundResource(R.drawable.btn_register);
			else
				button.setBackgroundResource(R.drawable.btn_unblock);
		} else if (type.equalsIgnoreCase("following")) {
			int is_following = (Integer) value;
			mDremer.is_following = is_following;
			if (is_following == 0) {
				button.setText("Follow");
			} else {
				button.setText("Stop Following");
			}
		} else if (type.equalsIgnoreCase("familyship")) {
			String status = (String) value;
			mDremer.familyship_status = status;
			button.setText(Utility.getFamilyshipStr(status));
			if (status.equals("not_familys"))
				button.setBackgroundResource(R.drawable.btn_register);
			else
				button.setBackgroundResource(R.drawable.btn_unblock);
		}
	}

	private void onPostAvatar()
    {
		Intent intent = new Intent();
    	intent.setClass(this, ActivityAvatarPost.class);
		startActivity(intent);
		this.overridePendingTransition(R.anim.in_right_left, R.anim.out_right_left);
    }
	
	public void onChangeAvatar()
	{
		DremerInfo dremer = GlobalValue.getInstance().getCurrentDremer();
		if (dremer.user_avatar != null && !dremer.user_avatar.isEmpty())
		{
			ImageLoader.getInstance().displayImage(dremer.user_avatar, mImgDremer, 0, 0);
			ImageLoader.getInstance().displayImage(dremer.user_avatar, mImgUserIcon, 0, 0);
		}
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		int id = v.getId();

		switch (id) {
		case R.id.imgUserIcon:
		case R.id.imgDremer:
			if (mDremer.user_id == Integer.parseInt(mPrefs.getUserId()))
				onPostAvatar();
			break;

		case R.id.btnBack:
			onBackButton();
			break;
		case R.id.btnFriend:
			if (mDremer.friendship_status.equals("awaiting_response")) {
				showFriendAcceptDialog(mDremer);
			} else {
				setDremerFriendship(mDremer.user_id,
						Utility.getFriendshipAction(mDremer.friendship_status), v);
			}
			break;
		case R.id.btnFamily:
			if (mDremer.familyship_status.equals("awaiting_response")) {
				showFamilyAcceptDialog(mDremer);
			} else {
				setDremerFamilyship(mDremer.user_id,
						Utility.getFamilyshipAction(mDremer.familyship_status), v);
			}
			break;
		case R.id.btnFollow:
			String action;
			if (mDremer.is_following == 0)
				action = "start";
			else
				action = "stop";
			setDremerFollowing(mDremer.user_id, action, v); 
			break;

		case R.id.btnPubMsg:
			break;
		case R.id.btnPrivMsg:
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
	
	private void onClickMenuHeader (int position, int sub_position)
	{
		Intent intent = new Intent();
		
		switch (position)
		{
		case 0:
			intent.setClass(this, ActivityActContent.class);
			intent.putExtra("tab_index", sub_position);
			intent.putExtra("dremer_id", mDremer.user_id);
			break;
		case 1:
			return;
		case 2:
			intent.setClass(this, ActivityFriends.class);
			intent.putExtra("tab_index", sub_position);
			intent.putExtra("dremer_id", mDremer.user_id);
			break;
		case 3:
			intent.setClass(this, ActivityFamily.class);
			intent.putExtra("tab_index", sub_position);
			intent.putExtra("dremer_id", mDremer.user_id);
			break;
		case 4:
			intent.setClass(this, ActivityFollow.class);
			intent.putExtra("tab_index", sub_position);
			intent.putExtra("dremer_id", mDremer.user_id);
			break;
		case 5:
			intent.setClass(this, ActivityFollowers.class);
			intent.putExtra("tab_index", sub_position);
			intent.putExtra("dremer_id", mDremer.user_id);
			break;
		case 6:
			intent.setClass(this, ActivityGroup.class);
			intent.putExtra("tab_index", sub_position);
			intent.putExtra("dremer_id", mDremer.user_id);
//			break;
			return;
		case 7:
			intent.setClass(this, ActivityMedia.class);
			intent.putExtra("tab_index", sub_position);
			intent.putExtra("dremer_id", mDremer.user_id);
			break;
		default:
			return;
		}
		
		startActivity(intent);
		overridePendingTransition(R.anim.in_right_left, R.anim.out_right_left);
	}
	
	private void showFriendAcceptDialog (DremerInfo dremer)
	{
		DialogFriendshipAccept acceptDiag = new DialogFriendshipAccept(this, this, dremer, -1/* invalid value */, this);
		acceptDiag.show();
	}
	
	private void showFamilyAcceptDialog (DremerInfo dremer)
	{
		DialogFamilyshipAccept acceptDiag = new DialogFamilyshipAccept(this, this, dremer, -1/* invalid value */, this);
		acceptDiag.show();
	}
	
	private void getSingleDremer(int dremerId) {
		waitDialog.show();
		
		GetSingleDremerParam param = new GetSingleDremerParam();

		param.user_id = mPrefs.getUserId();
		param.disp_user_id = dremerId;

		WebApiInstance.getInstance().executeAPI(Type.GET_SINGLE_DREMER, param, this);
	}

	private void getSingleDremerResult(Object param, Object obj) {

		waitDialog.dismiss();

		if (obj == null) {
			CustomToast.makeCustomToastShort(this, Constants.NETWORK_ERR);
		}

		if (obj != null){
			GetSingleDremerResult resultBean = (GetSingleDremerResult)obj;

			if (resultBean.status.equals("ok")) {
				mDremerId = resultBean.data.member.user_id;
				mDremer = resultBean.data.member;
				updateView(mDremer);
			} else {
				CustomToast.makeCustomToastShort(this, resultBean.msg);
			}
		}
	}
	
	private void setDremerFriendship(int dremerId, String action, Object exParam) {
		waitDialog.show();
		
		SetFriendshipParam param = new SetFriendshipParam();

		param.user_id = mPrefs.getUserId();
		param.dremer_id = dremerId;
		param.action = action;
		
		param.exParam = exParam;

		WebApiInstance.getInstance().executeAPI(Type.SET_FRIENDSHIP, param, this);
	}

	private void setDremerFriendshipResult(Object param, Object obj) {

		waitDialog.dismiss();

		if (obj == null) {
			CustomToast.makeCustomToastShort(this, Constants.NETWORK_ERR);
		}

		if (obj != null){
			SetFriendshipResult resultBean = (SetFriendshipResult)obj;

			if (resultBean.status.equals("ok")) {
				SetFriendshipParam friendshipParam = (SetFriendshipParam) param;
				updateActionView ("friendship",
						friendshipParam.exParam, resultBean.data.friendship_status);
			} else {
				CustomToast.makeCustomToastShort(this, resultBean.msg);
			}
		}
	}
	
	private void setDremerFamilyship(int dremerId, String action, Object exParam) {
		waitDialog.show();
		
		SetFamilyshipParam param = new SetFamilyshipParam();

		param.user_id = mPrefs.getUserId();
		param.dremer_id = dremerId;
		param.action = action;
		
		param.exParam = exParam;

		WebApiInstance.getInstance().executeAPI(Type.SET_FAMILYSHIP, param, this);
	}

	private void setDremerFamilyshipResult(Object param, Object obj) {

		waitDialog.dismiss();

		if (obj == null) {
			CustomToast.makeCustomToastShort(this, Constants.NETWORK_ERR);
		}

		if (obj != null){
			SetFamilyshipResult resultBean = (SetFamilyshipResult)obj;

			if (resultBean.status.equals("ok")) {
				SetFamilyshipParam familyshipParam = (SetFamilyshipParam) param;
				updateActionView ("familyship",
						familyshipParam.exParam, resultBean.data.familyship_status);
			} else {
				CustomToast.makeCustomToastShort(this, resultBean.msg);
			}
		}
	}
	
	private void setDremerFollowing(int dremerId, String action, Object exParam) {
		waitDialog.show();
		
		SetFollowingParam param = new SetFollowingParam();

		param.user_id = mPrefs.getUserId();
		param.dremer_id = dremerId;
		param.action = action;
		
		param.exParam = exParam;

		WebApiInstance.getInstance().executeAPI(Type.SET_FOLLOW, param, this);
	}

	private void setDremerFollowingResult(Object param, Object obj) {

		waitDialog.dismiss();

		if (obj == null) {
			CustomToast.makeCustomToastShort(this, Constants.NETWORK_ERR);
		}

		if (obj != null){
			SetFollowingResult resultBean = (SetFollowingResult)obj;

			if (resultBean.status.equals("ok")) {
				SetFollowingParam followParam = (SetFollowingParam) param;
				updateActionView ("following",
						followParam.exParam, resultBean.data.is_follow);
			} else {
				CustomToast.makeCustomToastShort(this, resultBean.msg);
			}
		}
	}

	public class ExpandableListAdapter extends BaseExpandableListAdapter {

		private Context _context;
		private List<String> _listDataHeader; // header titles
		// child data in format of header title, child title
		private HashMap<String, List<String>> _listDataChild;

		public ExpandableListAdapter(Context context, List<String> listDataHeader,
				HashMap<String, List<String>> listChildData) {
			this._context = context;
			this._listDataHeader = listDataHeader;
			this._listDataChild = listChildData;
		}

		@Override
		public Object getChild(int groupPosition, int childPosititon) {
			return this._listDataChild.get(this._listDataHeader.get(groupPosition))
					.get(childPosititon);
		}

		@Override
		public long getChildId(int groupPosition, int childPosition) {
			return childPosition;
		}

		@Override
		public View getChildView(int groupPosition, final int childPosition,
				boolean isLastChild, View convertView, ViewGroup parent) {

			final String childText = (String) getChild(groupPosition, childPosition);

			if (convertView == null) {
				LayoutInflater infalInflater = (LayoutInflater) this._context
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				convertView = infalInflater.inflate(R.layout.item_menu, null);
			}

			TextView txtListChild = (TextView) convertView
					.findViewById(R.id.txtMenu);

			txtListChild.setText(childText);
			return convertView;
		}

		@Override
		public int getChildrenCount(int groupPosition) {
			return this._listDataChild.get(this._listDataHeader.get(groupPosition))
					.size();
		}

		@Override
		public Object getGroup(int groupPosition) {
			return this._listDataHeader.get(groupPosition);
		}

		@Override
		public int getGroupCount() {
			return this._listDataHeader.size();
		}

		@Override
		public long getGroupId(int groupPosition) {
			return groupPosition;
		}

		@Override
		public View getGroupView(int groupPosition, boolean isExpanded,
				View convertView, ViewGroup parent) {
			String headerTitle = (String) getGroup(groupPosition);
			if (convertView == null) {
				LayoutInflater infalInflater = (LayoutInflater) this._context
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				convertView = infalInflater.inflate(R.layout.item_menu_header, null);
			}

			TextView lblListHeader = (TextView) convertView
					.findViewById(R.id.txtHeader);
			lblListHeader.setText(headerTitle);

			return convertView;
		}

		@Override
		public boolean hasStableIds() {
			return false;
		}

		@Override
		public boolean isChildSelectable(int groupPosition, int childPosition) {
			return true;
		}
	}

	@Override
	public void OnFriendshipResult(String friendshipStatus, int index) {
		// TODO Auto-generated method stub
		
		mDremer.friendship_status = friendshipStatus;
		mBtnFriend.setText(Utility.getFriendshipStr(mDremer.friendship_status));
		if (mDremer.friendship_status.equals("not_friends"))
			mBtnFriend.setBackgroundResource(R.drawable.btn_register);
		else
			mBtnFriend.setBackgroundResource(R.drawable.btn_unblock);
	}
	
	@Override
	public void OnFamilyshipResult(String familyshipStatus, int index) {
		// TODO Auto-generated method stub
		mDremer.familyship_status = familyshipStatus;
		mBtnFamily.setText(Utility.getFamilyshipStr(mDremer.familyship_status));
		if (mDremer.familyship_status.equals("not_familys"))
			mBtnFamily.setBackgroundResource(R.drawable.btn_register);
		else
			mBtnFamily.setBackgroundResource(R.drawable.btn_unblock);
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
		case SET_FRIENDSHIP:
			setDremerFriendshipResult(parameter, result);			
			break;
		case SET_FAMILYSHIP:
			setDremerFamilyshipResult(parameter, result);			
			break;
		case SET_FOLLOW:
			setDremerFollowingResult(parameter, result);			
			break;
		default:
			break;
		}
	}	
}
