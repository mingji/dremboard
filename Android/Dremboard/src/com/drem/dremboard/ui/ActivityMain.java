package com.drem.dremboard.ui;

import java.util.Timer;
import java.util.TimerTask;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.drem.dremboard.R;
import com.drem.dremboard.adapter.TabsAdapter;
import com.drem.dremboard.entity.DremerInfo;
import com.drem.dremboard.entity.GlobalValue;
import com.drem.dremboard.entity.Beans.GetNTCntParam;
import com.drem.dremboard.entity.Beans.GetNTCntResult;
import com.drem.dremboard.utils.AppPreferences;
import com.drem.dremboard.utils.DownloadImageTask;
import com.drem.dremboard.utils.ImageLoader;
import com.drem.dremboard.view.CustomToast;
import com.drem.dremboard.view.WebCircularImgView;
import com.drem.dremboard.view.WebImgView;
import com.drem.dremboard.webservice.Constants;
import com.drem.dremboard.webservice.WebApiCallback;
import com.drem.dremboard.webservice.WebApiInstance;
import com.drem.dremboard.webservice.WebApiInstance.Type;

/**
 * Demonstrates combining a TabHost with a ViewPager to implement a tab UI
 * that switches between tabs and also allows the user to perform horizontal
 * flicks to move between the tabs.
 */
public class ActivityMain extends SherlockFragmentActivity implements WebApiCallback, OnClickListener {
    TabHost mTabHost;
    ViewPager  mViewPager;
    TabsAdapter mTabsAdapter;
    
    int mCounter = 0;
    Button mBtnLogo;
    TextView mTxtNotificationIcon;
    WebCircularImgView mImgUserIcon;
    
    private AppPreferences mPrefs;
    
    public static ActivityMain instance;
    
    HorizontalScrollView mScrollTabs;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
    	setTheme(R.style.ActivityTheme);
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        mPrefs = new AppPreferences(this);
        
        instance = this;
        
        initView();
        
        mTabHost = (TabHost)findViewById(android.R.id.tabhost);
        mTabHost.setup();

        mViewPager = (ViewPager)findViewById(R.id.pager);

        mTabsAdapter = new TabsAdapter(this, mTabHost, mViewPager, mScrollTabs);

        mTabsAdapter.addTab(mTabHost.newTabSpec("home").setIndicator("Home"),
                FragmentHome.class, null);
        mTabsAdapter.addTab(mTabHost.newTabSpec("drems").setIndicator("Drēms"),
                FragmentDrems.class, null);
        mTabsAdapter.addTab(mTabHost.newTabSpec("dremers").setIndicator("Drēmers"),
                FragmentDremers.class, null);
        mTabsAdapter.addTab(mTabHost.newTabSpec("dremboards").setIndicator("Drēmboards"),
                FragmentDremboards.class, null);
        mTabsAdapter.addTab(mTabHost.newTabSpec("dremcast").setIndicator("Drēmcast"),
                FragmentDremcasts.class, null);
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
		mTxtNotificationIcon = (TextView) findViewById(R.id.txtNotificationIcon);
		mTxtNotificationIcon.setVisibility(View.INVISIBLE);

		mBtnLogo = (Button) findViewById(R.id.btnLogo);
		
		DremerInfo dremer = GlobalValue.getInstance().getCurrentDremer();
		if (dremer.user_avatar != null && !dremer.user_avatar.isEmpty())
			ImageLoader.getInstance().displayImage(dremer.user_avatar, mImgUserIcon, 0, 0);
		
    	mScrollTabs = (HorizontalScrollView) findViewById(R.id.scrollTabs);
    	
 	   	mImgUserIcon.setOnClickListener(this);
 	   	findViewById(R.id.iconNotification).setOnClickListener(this);
    	mBtnLogo.setOnClickListener(this);
    	
    	Timer timer = new Timer();
    	TimerTask myTimerTask = new TimerTask() {
    	    public void run() {
    	    	getNotificationCount();
    	    }
    	};
    	timer.scheduleAtFixedRate(myTimerTask, 0, 30000);
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

    public void getNotificationCount()
    {
    	GetNTCntParam param = new GetNTCntParam();

		param.user_id = mPrefs.getUserId();
		param.type = "unread";

		WebApiInstance.getInstance().executeAPI(Type.GET_NT_CNT, param, this);
    }
    
    private void onPostAvatar()
    {
		Intent intent = new Intent();
    	intent.setClass(this, ActivityAvatarPost.class);
		startActivity(intent);
		this.overridePendingTransition(R.anim.in_right_left, R.anim.out_right_left);
    }
    
    private void onNotificationScreen()
    {
    	Intent intent = new Intent();
    	intent.setClass(this, ActivityNotification.class);
		intent.putExtra("dremer_id", Integer.parseInt(mPrefs.getUserId()));

    	startActivity(intent);
		this.overridePendingTransition(R.anim.in_right_left, R.anim.out_right_left);
    }
    
    private void onPostDremScreen()
    {
    	Intent intent = new Intent();
		intent.setClass(this, ActivityDremPost.class);
		intent.putExtra("dremer_id", Integer.parseInt(mPrefs.getUserId()));

    	startActivity(intent);
		this.overridePendingTransition(R.anim.in_right_left, R.anim.out_right_left);
    }
    
	private void finishApp() {
		finish();
		Log.i("Dremboard", "close application");
		android.os.Process.killProcess(android.os.Process.myPid());
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		int id = v.getId();

		switch (id) {
		case R.id.imgUserIcon:
			onPostAvatar();
			break;
			
		case R.id.iconNotification:
			onNotificationScreen();
			 break;
			
		case R.id.btnLogo:
			onPostDremScreen();
			break;
		}
	}
	
	public void onChangeAvatar()
	{
		DremerInfo dremer = GlobalValue.getInstance().getCurrentDremer();
		if (dremer.user_avatar != null && !dremer.user_avatar.isEmpty())
			ImageLoader.getInstance().displayImage(dremer.user_avatar, mImgUserIcon, 0, 0);
	}

	private void getNotificationCountResult(Object obj) {

		if (obj == null) {
			CustomToast.makeCustomToastShort(this, Constants.NETWORK_ERR);
		}

		if (obj != null){
			GetNTCntResult resultBean = (GetNTCntResult)obj;

			if (resultBean.status.equals("ok")) {
				if (resultBean.count == 0)
				{
					mTxtNotificationIcon.setVisibility(View.INVISIBLE);
				}
				else
				{
					mTxtNotificationIcon.setVisibility(View.VISIBLE);
					mTxtNotificationIcon.setText(Integer.toString(resultBean.count));
				}
				
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
			case GET_NT_CNT:
				getNotificationCountResult(result);			
				break;
				
			default:
				break;
		}
	}
}

