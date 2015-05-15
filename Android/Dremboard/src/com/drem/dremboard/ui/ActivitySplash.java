package com.drem.dremboard.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;

import com.drem.dremboard.R;
import com.drem.dremboard.utils.AppPreferences;
import com.drem.dremboard.utils.ImageLoader;
import com.drem.dremboard.webservice.WebApiInstance;

public class ActivitySplash extends Activity{

	int[] seconds = {3};
	final Handler handler = new Handler();
	Runnable runnable;

	private AppPreferences mPrefs;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);		

		setContentView(R.layout.activity_splash);
		ImageLoader.getInstance().init(getApplicationContext());
		WebApiInstance.getInstance().init(getApplicationContext(), 8);

		mPrefs = new AppPreferences(this);
		
		runnable = new Runnable() {
			@Override
			public void run() {
				/* do what you need to do */
				seconds[0]--;
				if (seconds[0] > 0) {
					/* and here comes the "trick" */
					handler.postDelayed(this, 1000);
				} else {
					startNextActivity();
				}

			}
		};

		handler.postDelayed(runnable, 100);
	}

	private void startNextActivity() {
		
		// Run next activity
		if (mPrefs.isLogIn()) {
			startMainActivity();
		} else {
			startLoginActivity();
		}
	}

	private void startMainActivity() {
		// Run next activity
		Intent intent = new Intent();
		intent.setClass(this, ActivityLogin.class);
		startActivity(intent);
		overridePendingTransition(R.anim.in_right_left, R.anim.out_right_left);
		finish();
	}

	private void startLoginActivity() {
		// Run next activity
		Intent intent = new Intent();
		intent.setClass(this, ActivityLogin.class);
		startActivity(intent);
		overridePendingTransition(R.anim.in_right_left, R.anim.out_right_left);
		finish();
	}
}
