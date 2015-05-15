package com.drem.dremboard.ui;

import java.util.ArrayList;

import com.drem.dremboard.R;
import com.drem.dremboard.entity.VideoInfo;
import com.drem.dremboard.utils.ImageLoader;
import com.drem.dremboard.view.HyIconView;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

public class FragmentDremcast extends Fragment implements OnClickListener
{
	GridView mGridMemory;
	MemoryAdapter mAdapterMemory;
	ArrayList<VideoInfo> mArrayMemory;

//	VideoView mVideoView;
//	MediaController mediaControls;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View view = inflater.inflate(R.layout.fragment_memories, null);

		initView (view);
		updateDrems();

		return view;
	}

	private void initView (View view)
	{
		mArrayMemory = new ArrayList<VideoInfo>();
		mAdapterMemory = new MemoryAdapter(getActivity(), R.layout.item_memory, mArrayMemory);

		mGridMemory = (GridView)view.findViewById(R.id.gridMemory);
		mGridMemory.setAdapter(mAdapterMemory);

//		mVideoView = (VideoView)view.findViewById(R.id.videoView);
//
//			mediaControls = new MediaController(getActivity());
//		
//		try {
//			mediaControls.setAnchorView(mVideoView);
//			mediaControls.setMediaPlayer(mVideoView);
//			mVideoView.setMediaController(mediaControls);
//			mVideoView.setVideoURI(Uri.parse("http://dremboard.com/wp-content/uploads/rtMedia/users/9/2014/10/M2U00126_x264.mp4"));
//		} catch (Exception e) {
//			Log.e("Error", e.getMessage());
//			e.printStackTrace();
//		}
//		
//		mVideoView.requestFocus();
//		mVideoView.start();
//		mVideoView.setOnPreparedListener(new OnPreparedListener() {
//			// Close the progress bar and play the video
//			public void onPrepared(MediaPlayer mp) {
//				mVideoView.start();
//			}
//		});

	}

	private void updateDrems()
	{
		mArrayMemory.add(new VideoInfo(null, "Test Video", "User1"));
		mArrayMemory.add(new VideoInfo(null, "Greate Race", "User2"));
		mArrayMemory.add(new VideoInfo(null, "Dog Race", "User1"));
		mArrayMemory.add(new VideoInfo(null, "RACE", "User3"));
		mArrayMemory.add(new VideoInfo(null, "TRACK", "User3"));
		mArrayMemory.add(new VideoInfo(null, "Just Another Race", "User1"));
		mArrayMemory.add(new VideoInfo(null, "EATING", "User2"));
		mArrayMemory.add(new VideoInfo(null, "BIG RAY CURLS", "User3"));

		mAdapterMemory.notifyDataSetChanged();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

	}

	private void startContentViewActivity()
	{		
		Intent intent = new Intent();
		intent.setClass(getActivity(), ActivityContentView.class);
		intent.putExtra("type", "video");
		intent.putExtra("content_id", "test");
		startActivity(intent);
		getActivity().overridePendingTransition(R.anim.in_right_left, R.anim.out_right_left);
	}

	public class MemoryAdapter extends ArrayAdapter<VideoInfo> {
		Activity activity;
		int layoutResourceId;
		ArrayList<VideoInfo> item = new ArrayList<VideoInfo>();

		public MemoryAdapter(Activity activity, int layoutId, ArrayList<VideoInfo> items) {
			super(activity, layoutId, items);
			item = items;
			this.activity = activity;
			this.layoutResourceId = layoutId;
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			VideoHolder holder = null;	

			final VideoInfo video = getItem(position);			

			// inflate the view
			if (convertView == null) {
				LayoutInflater inflater = ((Activity) activity).getLayoutInflater();
				convertView = inflater.inflate(R.layout.item_memory, null);
				holder = new VideoHolder();
				holder.txtVideoName = (TextView) convertView.findViewById(R.id.txtVideoName);				
				holder.txtUserName = (TextView) convertView.findViewById(R.id.txtUserName);
				holder.imgVideoPic = (HyIconView) convertView.findViewById(R.id.imgPic);
				holder.imgVideoPic.setOnClickListener(new View.OnClickListener() {
					public void onClick(View v) {

						startContentViewActivity();
					}
				});

				convertView.setTag(holder);
			} 
			else
				holder = (VideoHolder) convertView.getTag();

			if (video.mVideoUrl != null && !video.mVideoUrl.isEmpty())
				ImageLoader.getInstance().displayImage(video.mVideoUrl, holder.imgVideoPic, 0, 0);
			else
				holder.imgVideoPic.imageView.setImageResource(R.drawable.sample_video);

			holder.txtVideoName.setText(video.mVideoName);			
			holder.txtUserName.setText(video.mUserName);

			return convertView;
		}

		public class VideoHolder {
			HyIconView imgVideoPic;
			TextView txtVideoName;
			TextView txtUserName;
		}
	}
}
