package com.drem.dremboard.ui;

import com.drem.dremboard.R;
import com.drem.dremboard.entity.DremerInfo;
import com.drem.dremboard.entity.GlobalValue;
import com.drem.dremboard.utils.ImageLoader;
import com.drem.dremboard.view.HyIconView;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;

public class ActivityTerms extends Activity implements OnClickListener{

	TextView mTxtTitle;
	Button mBtnBack;
	HyIconView mImgUserIcon;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_terms);
        
        initView();
        
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
    
    private void initView()
    {
    	mImgUserIcon = (HyIconView) findViewById(R.id.imgUserIcon);
		mImgUserIcon.imageView.setImageResource(R.drawable.empty_man);
		DremerInfo dremer = GlobalValue.getInstance().getCurrentDremer();
		if (dremer.user_avatar != null && !dremer.user_avatar.isEmpty())
			ImageLoader.getInstance().displayImage(dremer.user_avatar, mImgUserIcon, 0, 0);
		
    	mBtnBack = (Button) findViewById(R.id.btnBack);
		mBtnBack.setOnClickListener(this);
		
		mTxtTitle = (TextView) findViewById(R.id.txtTitle);
		mTxtTitle.setText("Terms");
		
		WebView webView = (WebView) findViewById(R.id.webView);
		webView.loadUrl("file:///android_asset/Terms.html");
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
}
