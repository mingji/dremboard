package com.drem.dremboard.ui;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.drem.dremboard.R;
import com.drem.dremboard.adapter.TabsAdapter;
import com.drem.dremboard.entity.Beans.GetActivityResult;
import com.drem.dremboard.entity.DremActivityInfo;
import com.drem.dremboard.entity.DremInfo;
import com.drem.dremboard.entity.DremerInfo;
import com.drem.dremboard.entity.GlobalValue;
import com.drem.dremboard.entity.Beans.GetActivitiesResult;
import com.drem.dremboard.entity.Beans.GetActivityParam;
import com.drem.dremboard.entity.DremActivityInfo.MediaInfo;
import com.drem.dremboard.utils.AppPreferences;
import com.drem.dremboard.utils.ImageLoader;
import com.drem.dremboard.view.CustomToast;
import com.drem.dremboard.view.WaitDialog;
import com.drem.dremboard.view.WebImgView;
import com.drem.dremboard.webservice.Constants;
import com.drem.dremboard.webservice.WebApiCallback;
import com.drem.dremboard.webservice.WebApiInstance;
import com.drem.dremboard.webservice.WebApiInstance.Type;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.TabHost;

public class ActivityNotification extends SherlockFragmentActivity implements OnClickListener, WebApiCallback{
	TabHost mTabHost;
	ViewPager  mViewPager;
	TabsAdapter mTabsAdapter;

	HorizontalScrollView mScrollTabs;
	int mDremerId;
	Button mBtnBack;
	WebImgView mImgUserIcon;
	private AppPreferences mPrefs;
	WaitDialog waitDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setTheme(R.style.ActivityTheme);
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_notification);

		int tabIndex = getIntent().getIntExtra("tab_index", 0);
		mDremerId = getIntent().getIntExtra("dremer_id", -1);
		mPrefs = new AppPreferences(this);
		waitDialog = new WaitDialog(this);

		initView();

		setupTabs();

		mViewPager.setCurrentItem(tabIndex);

		if (savedInstanceState != null) {
			mTabHost.setCurrentTabByTag(savedInstanceState.getString("tab"));
		}
	}

	@Override
	protected void onResume()
	{
		super.onResume();    	
	}

	@Override
	protected void onPause() {
		super.onPause();		
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putString("tab", mTabHost.getCurrentTabTag());
	}

	private void initView()
	{
		mImgUserIcon = (WebImgView) findViewById(R.id.imgUserIcon);
		mImgUserIcon.imageView.setImageResource(R.drawable.empty_man);
		DremerInfo dremer = GlobalValue.getInstance().getCurrentDremer();
		if (dremer.user_avatar != null && !dremer.user_avatar.isEmpty())
			ImageLoader.getInstance().displayImage(dremer.user_avatar, mImgUserIcon, 0, 0);

		mBtnBack = (Button) findViewById(R.id.btnBack);
		mBtnBack.setOnClickListener(this);

		mTabHost = (TabHost)findViewById(android.R.id.tabhost);
		mTabHost.setup();

		mViewPager = (ViewPager)findViewById(R.id.pager);

		mScrollTabs = (HorizontalScrollView) findViewById(R.id.scrollTabs);
	}

	private void setupTabs()
	{
		mTabsAdapter = new TabsAdapter(this, mTabHost, mViewPager, mScrollTabs);

		Bundle read = new Bundle();
		read.putString("type", "read");

		Bundle undread = new Bundle();
		undread.putString("type", "unread");       

		mTabsAdapter.addTab(mTabHost.newTabSpec("unread").setIndicator("Unread"),
				FragmentNotifications.class, undread);
		mTabsAdapter.addTab(mTabHost.newTabSpec("read").setIndicator("Read"),
				FragmentNotifications.class, read);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		int id = v.getId();

		switch(id)
		{
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
		ActivityMain.instance.getNotificationCount();

		finish();
		overridePendingTransition(R.anim.in_left_right, R.anim.out_left_right);		
	}

	void getActivity(int activity_id)
	{
		GetActivityParam param = new GetActivityParam();

		param.user_id = mPrefs.getUserId();
		param.activity_id = activity_id;		

		waitDialog.show();

		WebApiInstance.getInstance().executeAPI(Type.GET_ONE_ACTIVITY, param, this);	
	}

	void updateNotifications(String action, int activity_id)
	{
		int position = 0;

		if (action.equalsIgnoreCase("read"))
			position = 1;

		String tag = "android:switcher:" + mViewPager.getId() + ":" +  position;
		FragmentNotifications fragmentNt = (FragmentNotifications) getSupportFragmentManager().findFragmentByTag(tag);


		if (fragmentNt == null)
			return;

		fragmentNt.reloadNotifications();

		if (position == 1)
		{
			getActivity(activity_id);
		}
	}

	private void displayDrem (DremActivityInfo activityItem)
	{
		DremInfo dremItem = new DremInfo();
		
		dremItem.activity_id = activityItem.activity_id;
		dremItem.category = activityItem.category;
		dremItem.favorite = activityItem.favorite;
		dremItem.like = activityItem.like;
		dremItem.author_name = activityItem.author_name;
		dremItem.author_avatar = activityItem.author_avatar;;
		dremItem.comment_list = activityItem.comment_list; 
		dremItem.description = activityItem.description;
		dremItem.isMore = activityItem.isMore;
		dremItem.last_modified = activityItem.last_modified;
		
		for (int i = 0; i < activityItem.media_list.size(); i++){
			MediaInfo mediaInfo = activityItem.media_list.get(i);
			
			if (mediaInfo.media_type.equals("photo")) {
				
				String photo_guid = mediaInfo.media_guid;
				if (photo_guid != null){
					dremItem.guid = photo_guid;
				}
			}
			else if (mediaInfo.media_type.equals("video")) {
				;
			}
		}
		
		Intent intent = new Intent();
		intent.setClass(this, ActivityDremView.class);
		GlobalValue.getInstance().setCurrentDrem(dremItem);
		startActivity(intent);
		this.overridePendingTransition(R.anim.in_right_left, R.anim.out_right_left);		
	}

	private void getDremActivityResult(Object obj) {

		waitDialog.dismiss();

		if (obj == null) {
			CustomToast.makeCustomToastShort(this, Constants.NETWORK_ERR);
		}

		if (obj != null){
			GetActivityResult resultBean = (GetActivityResult)obj;

			if (resultBean.status.equals("ok")) {
				displayDrem(resultBean.data.activity);
			} else {
				CustomToast.makeCustomToastShort(this, resultBean.msg);
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
		case GET_ONE_ACTIVITY:
			getDremActivityResult(result);			
			break;
		}
	}
}
