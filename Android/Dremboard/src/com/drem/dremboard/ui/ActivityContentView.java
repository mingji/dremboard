package com.drem.dremboard.ui;


import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.drem.dremboard.R;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerFragment;

public class ActivityContentView extends ActivityYouTubeFailureRecovery implements OnClickListener{

	Button mBtnClose;

	YouTubePlayerFragment mYouTubeFragment;

	RelativeLayout mLayVideo, mLayRef, mLayNote;

	ContentType mContentType;

	private enum ContentType {
		VIDEO, NONE
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_content_view);

		String type = getIntent().getStringExtra("type");
		if (type == null) {
			finish();
			return; 
		} else if (type.equalsIgnoreCase("video"))
			mContentType = ContentType.VIDEO;
		else {
			finish();
			return;
		}			

		String contentId = getIntent().getStringExtra("content_id");

		initView();

		showContent(contentId);
	}

	@Override
	public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer player,
			boolean wasRestored) {
		if (!wasRestored) {
			player.cueVideo("avP5d16wEp0");
		}
	}

	@Override
	protected YouTubePlayer.Provider getYouTubePlayerProvider() {
		return (YouTubePlayerFragment) getFragmentManager().findFragmentById(R.id.youtube_fragment);
	}

	@Override
	public void onResume(){
		super.onResume();
		// put your code here...
	}

	private void initView()
	{
		mYouTubeFragment =
				(YouTubePlayerFragment) getFragmentManager().findFragmentById(R.id.youtube_fragment);
		mYouTubeFragment.initialize("Develop Key", this);

		mLayVideo = (RelativeLayout) findViewById(R.id.layVideo);
		mLayRef = (RelativeLayout) findViewById(R.id.layReference);
		mLayNote = (RelativeLayout) findViewById(R.id.layNote);

		mBtnClose = (Button) findViewById(R.id.btnClose);
		mBtnClose.setOnClickListener(this);

	}

	private void showContent(String contentId)
	{
		mLayVideo.setVisibility(View.GONE);
		mLayNote.setVisibility(View.GONE);
		mLayRef.setVisibility(View.GONE);

		switch (mContentType)
		{
		case VIDEO:
			mLayVideo.setVisibility(View.VISIBLE);
			break;
		default:
			break;
		}

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		int id = v.getId();


		switch (id)
		{
		case R.id.btnClose:
			finish();
			overridePendingTransition(R.anim.in_left_right, R.anim.out_left_right);
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
			finish();
			overridePendingTransition(R.anim.in_left_right, R.anim.out_left_right);			
			bool = true;
		}
		return bool;
	}
}
