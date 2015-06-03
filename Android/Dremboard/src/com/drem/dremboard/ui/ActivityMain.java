package com.drem.dremboard.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.TabHost;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.drem.dremboard.R;
import com.drem.dremboard.adapter.TabsAdapter;
import com.drem.dremboard.entity.DremerInfo;
import com.drem.dremboard.entity.GlobalValue;
import com.drem.dremboard.utils.AppPreferences;
import com.drem.dremboard.utils.DownloadImageTask;
import com.drem.dremboard.utils.ImageLoader;
import com.drem.dremboard.view.WebCircularImgView;
import com.drem.dremboard.view.WebImgView;

/**
 * Demonstrates combining a TabHost with a ViewPager to implement a tab UI
 * that switches between tabs and also allows the user to perform horizontal
 * flicks to move between the tabs.
 */
public class ActivityMain extends SherlockFragmentActivity {
    TabHost mTabHost;
    ViewPager  mViewPager;
    TabsAdapter mTabsAdapter;
    
    WebCircularImgView mImgUserIcon;
    
    private AppPreferences mPrefs;
    
    HorizontalScrollView mScrollTabs;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
    	setTheme(R.style.ActivityTheme);
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        mPrefs = new AppPreferences(this);
        
        initView();
        
        mTabHost = (TabHost)findViewById(android.R.id.tabhost);
        mTabHost.setup();

        mViewPager = (ViewPager)findViewById(R.id.pager);

        mTabsAdapter = new TabsAdapter(this, mTabHost, mViewPager, mScrollTabs);

        mTabsAdapter.addTab(mTabHost.newTabSpec("home").setIndicator("Home"),
                FragmentHome.class, null);
        mTabsAdapter.addTab(mTabHost.newTabSpec("drems").setIndicator("Drems"),
                FragmentDrems.class, null);
        mTabsAdapter.addTab(mTabHost.newTabSpec("dremers").setIndicator("Dremers"),
                FragmentDremers.class, null);
        mTabsAdapter.addTab(mTabHost.newTabSpec("dremboards").setIndicator("Dremboards"),
                FragmentDremboards.class, null);
        mTabsAdapter.addTab(mTabHost.newTabSpec("dremcast").setIndicator("Dremcast"),
                FragmentDremcast.class, null);
        mTabsAdapter.addTab(mTabHost.newTabSpec("you").setIndicator("You"),
                FragmentYou.class, null);
        mTabsAdapter.addTab(mTabHost.newTabSpec("more").setIndicator("More"),
                FragmentMore.class, null);        
        
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
    	mImgUserIcon = (WebCircularImgView) findViewById(R.id.imgUserIcon);
		mImgUserIcon.imageView.setImageResource(R.drawable.empty_man);
		DremerInfo dremer = GlobalValue.getInstance().getCurrentDremer();
		if (dremer.user_avatar != null && !dremer.user_avatar.isEmpty())
			ImageLoader.getInstance().displayImage(dremer.user_avatar, mImgUserIcon, 0, 0);
		
    	mScrollTabs = (HorizontalScrollView) findViewById(R.id.scrollTabs);
    }
    
    @Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		boolean bool;
		if (keyCode != KeyEvent.KEYCODE_BACK) {
			bool = super.onKeyDown(keyCode, event);
		} else {
			showCloseDialog();
			bool = true;
		}
		return bool;
	}
    
    private void showCloseDialog() {
		new AlertDialog.Builder(this)
				.setTitle("Exit App")
				.setMessage("Sure to exit?")
				.setPositiveButton("OK", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						finishApp();
					}
				})
				.setNegativeButton("Cancel",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {
							}
						})
				.setOnKeyListener(new DialogInterface.OnKeyListener() {
					@Override
					public boolean onKey(DialogInterface dialog, int keyCode,
							KeyEvent event) {
						if (keyCode == KeyEvent.KEYCODE_BACK) {
							return false;
						}
						return false;
					}
				}).show();
	}

	private void finishApp() {
		finish();
		Log.i("Dremboard", "close application");
		android.os.Process.killProcess(android.os.Process.myPid());
	}
    
}

