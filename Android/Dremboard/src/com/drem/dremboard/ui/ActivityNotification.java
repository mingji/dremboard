package com.drem.dremboard.ui;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.drem.dremboard.R;
import com.drem.dremboard.adapter.TabsAdapter;
import com.drem.dremboard.entity.DremerInfo;
import com.drem.dremboard.entity.GlobalValue;
import com.drem.dremboard.utils.ImageLoader;
import com.drem.dremboard.view.HyIconView;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.TabHost;

public class ActivityNotification extends SherlockFragmentActivity implements OnClickListener{
    TabHost mTabHost;
    ViewPager  mViewPager;
    TabsAdapter mTabsAdapter;
    
    HorizontalScrollView mScrollTabs;
    int mDremerId;
    Button mBtnBack;
    HyIconView mImgUserIcon;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
    	setTheme(R.style.ActivityTheme);
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_notification);
        
        int tabIndex = getIntent().getIntExtra("tab_index", 0);
        mDremerId = getIntent().getIntExtra("dremer_id", -1);
        
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
    	mImgUserIcon = (HyIconView) findViewById(R.id.imgUserIcon);
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
		finish();
		overridePendingTransition(R.anim.in_left_right, R.anim.out_left_right);		
	}
	
	void updateNotifications(String action)
	{
		int position = 0;
		
		if (action.equalsIgnoreCase("read"))
			position = 1;
				
		String tag = "android:switcher:" + mViewPager.getId() + ":" +  position;
		FragmentNotifications fragmentNt = (FragmentNotifications) getSupportFragmentManager().findFragmentByTag(tag);
		
		if (fragmentNt == null)
			return;
		
		fragmentNt.reloadNotifications();
	}
}
