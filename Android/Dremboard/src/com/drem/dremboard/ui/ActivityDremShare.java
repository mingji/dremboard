package com.drem.dremboard.ui;

import android.app.Activity;
import android.os.Bundle;

import com.actionbarsherlock.view.Window;
import com.drem.dremboard.R;


public class ActivityDremShare extends Activity {
	@Override
	  public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    requestWindowFeature((int) Window.FEATURE_NO_TITLE);
	    setContentView(R.layout.item_share_modal);

	  }
}
