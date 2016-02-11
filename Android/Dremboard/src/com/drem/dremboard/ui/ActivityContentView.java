package com.drem.dremboard.ui;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.webkit.URLUtil;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.VideoView;

import com.drem.dremboard.R;


public class ActivityContentView extends Activity implements OnClickListener{

	Button mBtnClose;

	RelativeLayout mLayRef, mLayNote;

	ContentType mContentType;
	String mContentId, mCurrentId;
	ImageButton mPlay, mPause, mReset, mStop;

	private VideoView mVideoView;

	private enum ContentType {
		VIDEO, NONE
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
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

		mContentId = getIntent().getStringExtra("content_id");

		initView();

		showContent();
	}

	private void initView()
	{
		mVideoView = (VideoView) findViewById(R.id.surface_view);
		mLayRef = (RelativeLayout) findViewById(R.id.layReference);
		mLayNote = (RelativeLayout) findViewById(R.id.layNote);
		mPlay = (ImageButton) findViewById(R.id.play);
		mPause = (ImageButton) findViewById(R.id.pause);
		mReset = (ImageButton) findViewById(R.id.reset);
		mStop = (ImageButton) findViewById(R.id.stop);
		mBtnClose = (Button) findViewById(R.id.btnClose);
		
		mPlay.setOnClickListener(this);
		mPause.setOnClickListener(this);
		mReset.setOnClickListener(this);
		mStop.setOnClickListener(this);
		mBtnClose.setOnClickListener(this);
		
//		runOnUiThread(new Runnable(){
//			public void run() {
//				playVideo();
//				
//			}
//		});
	}

//	private void showContent(String contentId)
//	{
//		switch (mContentType)
//		{
//		case VIDEO:
//			mVideoView.setVideoPath(mContentId);
//			mVideoView.start();
//			mVideoView.requestFocus();
//			break;
//		default:
//			break;
//		}
//	}

	private void showContent() {
		if (mContentId.equals(mCurrentId) && mVideoView != null) {
			mVideoView.start();
			mVideoView.requestFocus();
			return;
		}
		mCurrentId = mContentId;
		mVideoView.setVideoPath(mCurrentId);
		mVideoView.start();
		mVideoView.requestFocus();
	}
	
//	private void playVideo() {
//		
//		try {
//			// If the path has not changed, just start the media player
//			if (mContentId.equals(mCurrentId) && mVideoView != null) {
//				mVideoView.start();
//				mVideoView.requestFocus();
//				return;
//			}
//			mCurrentId = mContentId;
//			mVideoView.setVideoPath(getDataSource(mCurrentId));
//			mVideoView.start();
//			mVideoView.requestFocus();
//		} catch (Exception e) {
//			if (mVideoView != null) {
//				mVideoView.stopPlayback();
//			}
//		}
//	}
	
	private String getDataSource(String path) throws IOException {
		if (!URLUtil.isNetworkUrl(path)) {
			return path;
		} else {
			URL url = new URL(path);
			URLConnection cn = url.openConnection();
			cn.connect();
			InputStream stream = cn.getInputStream();
			if (stream == null)
				throw new RuntimeException("stream is null");
			File temp = File.createTempFile("mediaplayertmp", "dat");
			temp.deleteOnExit();
			String tempPath = temp.getAbsolutePath();
			FileOutputStream out = new FileOutputStream(temp);
			byte buf[] = new byte[128];
			do {
				int numread = stream.read(buf);
				if (numread <= 0)
					break;
				out.write(buf, 0, numread);
			} while (true);
			try {
				stream.close();
			} catch (IOException ex) {
				Log.e("Dremcast", "error: " + ex.getMessage(), ex);
			}
			return tempPath;
		}
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		int id = v.getId();

		switch (id)
			{
			case R.id.play:
				showContent();
				break;
				
			case R.id.pause:
				if (mVideoView != null) {
					mVideoView.pause();
				}
				break;
				
			case R.id.reset:
				if (mVideoView != null) {
					mVideoView.seekTo(0);
				}
				break;
				
			case R.id.stop:
				if (mVideoView != null) {
					mCurrentId = null;
					mVideoView.stopPlayback();
				}
				break;
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
