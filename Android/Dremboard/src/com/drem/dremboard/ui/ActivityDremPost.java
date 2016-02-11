package com.drem.dremboard.ui;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.drem.dremboard.Const;
import com.drem.dremboard.R;
import com.drem.dremboard.entity.DremerInfo;
import com.drem.dremboard.entity.GlobalValue;
import com.drem.dremboard.entity.MyTextWatcher;
import com.drem.dremboard.utils.AppPreferences;
import com.drem.dremboard.utils.ImageLoader;
import com.drem.dremboard.utils.MyDialog;
import com.drem.dremboard.utils.ResourceUtil;
import com.drem.dremboard.view.CustomToast;
import com.drem.dremboard.view.MyImageView;
import com.drem.dremboard.view.WebCircularImgView;
import com.learnncode.mediachooser.MediaChooser;
import com.learnncode.mediachooser.activity.HomeFragmentActivity;
import com.learnncode.mediachooser.async.ImageLoadAsync;
import com.learnncode.mediachooser.async.MediaAsync;
import com.learnncode.mediachooser.async.VideoLoadAsync;

import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.RelativeLayout.LayoutParams;

import com.learnncode.mediachooser.fragment.VideoFragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;


@SuppressLint("NewApi")
public class ActivityDremPost extends Activity implements OnClickListener {
	public static ActivityDremPost instance = null;

	private enum DremType {
		NONE, DREM, DREMCAST
	};
	
	private Context mContext;

	private int mWidth, mHeight;
	ListView lst_media;
	MediaListAdapter mediaAdapter;
	EditText edt_title, edt_post;
	Button btn_back;
	Spinner spin_cate;
	ArrayList<String> mArrayCategory;
	HashMap<String, String> mMapCategory;
	// Data
	boolean isSelected[] = new boolean[1];

	private List<String> mMediaList;
	private Bitmap mOrgBmp = null;
	private String tempFilePath = null;

	public VideoFragment videoFragment;  
	private AppPreferences mPrefs;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		instance = this;

		setContentView(R.layout.activity_drem_post);

		mPrefs = new AppPreferences(this);

//		edt_title = (EditText) findViewById(R.id.edt_title);
		lst_media = (ListView) findViewById(R.id.lst_media);
		edt_post = (EditText) findViewById(R.id.edt_post);
		btn_back = (Button) findViewById(R.id.btn_back);
		spin_cate = (Spinner) findViewById(R.id.spin_category);

		WebCircularImgView img_avatar = (WebCircularImgView) findViewById(R.id.img_userIcon);
		img_avatar.imageView.setImageResource(R.drawable.empty_man);
		DremerInfo dremer = GlobalValue.getInstance().getCurrentDremer();
		if (dremer.user_avatar != null && !dremer.user_avatar.isEmpty())
			ImageLoader.getInstance().displayImage(dremer.user_avatar, img_avatar, 0, 0);

		edt_post.addTextChangedListener(new MyTextWatcher() {
			@Override
			public void onTextChanged() {
				// TODO Auto-generated method stub
			}
		});

		spin_cate.setPrompt("Select Category");
		mArrayCategory = new ArrayList<String>();
		mMapCategory = new HashMap<String, String>();
		
		try {
			setCategories();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		ArrayAdapter<String> adapterCategory = new ArrayAdapter<String>(
				this, android.R.layout.simple_spinner_item,
				mArrayCategory);
		adapterCategory
				.setDropDownViewResource(R.layout.custom_spinner_dropdown_item);
		spin_cate.setAdapter(adapterCategory);
		spin_cate.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				
				// same logic when click search button <<
				String cate_val = spin_cate.getSelectedItem().toString();
				String category = mMapCategory.get(cate_val);
				String search_str = "";
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		
		findViewById(R.id.img_camera).setOnClickListener(this);
		findViewById(R.id.btn_post).setOnClickListener(this);
		btn_back.setOnClickListener(this);
		
		IntentFilter videoIntentFilter = new IntentFilter(MediaChooser.VIDEO_SELECTED_ACTION_FROM_MEDIA_CHOOSER);
		registerReceiver(videoBroadcastReceiver, videoIntentFilter);

		IntentFilter imageIntentFilter = new IntentFilter(MediaChooser.IMAGE_SELECTED_ACTION_FROM_MEDIA_CHOOSER);
		registerReceiver(imageBroadcastReceiver, imageIntentFilter);
	}

	private void setCategories() throws JSONException {
		String category_json = mPrefs.getCategoryList();
		JSONObject category_obj = new JSONObject(category_json);
		JSONArray cate_key_obj = category_obj.getJSONArray("keys");
		JSONArray cate_val_obj = category_obj.getJSONArray("values");

		for (int i = 0; i < cate_key_obj.length(); i++) {
			mArrayCategory.add(cate_val_obj.getString(i));
			mMapCategory.put(cate_val_obj.getString(i),
					cate_key_obj.getString(i));
		}
	}
	
	private void setAdapter( List<String> filePathList) {
		if(mediaAdapter == null){
			mediaAdapter = new MediaListAdapter(this, 0, filePathList);
			lst_media.setAdapter(mediaAdapter);
		}else{
			mediaAdapter.addAll(filePathList);
			mediaAdapter.notifyDataSetChanged();
		}
	}
	
	BroadcastReceiver videoBroadcastReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {

//			Toast.makeText(MainActivity.this, "yippiee Video ", Toast.LENGTH_SHORT).show();
//			Toast.makeText(MainActivity.this, "Video SIZE :" + intent.getStringArrayListExtra("list").size(), Toast.LENGTH_SHORT).show();
			setAdapter(intent.getStringArrayListExtra("list"));
		}
	};


	BroadcastReceiver imageBroadcastReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
//			Toast.makeText(MainActivity.this, "yippiee Image ", Toast.LENGTH_SHORT).show();
//			Toast.makeText(MainActivity.this, "Image SIZE :" + intent.getStringArrayListExtra("list").size(), Toast.LENGTH_SHORT).show();
			setAdapter(intent.getStringArrayListExtra("list"));
		}
	};
	
	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		int id = view.getId();
		DremType kind;
		
		switch (id) {
		case R.id.img_camera: 
			Intent intent = new Intent(this, HomeFragmentActivity.class);
			startActivity(intent);
			break;

		case R.id.btn_post:
			kind = isValid();

			if (kind == DremType.DREM)
				doPost(1);
			else if (kind == DremType.DREMCAST)
				doPost(2);
			break;
			
		case R.id.btn_back:
			finish();
			overridePendingTransition(R.anim.in_left_right, R.anim.out_left_right);		
			break;
		}
	}
	
	private DremType isValid() {
		DremType flag = DremType.NONE;
		String postStr = edt_post.getText().toString().trim();
		if (TextUtils.isEmpty(postStr)) {
			CustomToast.makeCustomToastShort(this, "No post message");
			return DremType.NONE;
		}
		
		if (mMediaList == null)
			return DremType.DREM;
		
		for (int i = 0; i < mMediaList.size(); i++)
		{
			File mediaFile = new File(mMediaList.get(i));
			if(mediaFile.getPath().contains("mp4") || mediaFile.getPath().contains("wmv") ||
						mediaFile.getPath().contains("avi") || mediaFile.getPath().contains("3gp")) {
				
				if (flag == DremType.DREM)
				{
					CustomToast.makeCustomToastShort(this, "You can't to post video and image together");
					return DremType.NONE;
				}
				
				flag = DremType.DREMCAST;
			}
			else
			{
				if (flag == DremType.DREMCAST)
				{
					CustomToast.makeCustomToastShort(this, "You can't to post video and image together");
					return DremType.NONE;
				}
				flag = DremType.DREM;
			}
		}
		
		return DremType.DREM;
	}

	private void doPost(int kind) {
		

	}

	@Override
	protected void onDestroy() {
		unregisterReceiver(imageBroadcastReceiver);
		unregisterReceiver(videoBroadcastReceiver);
		super.onDestroy();
	}
	
	private class MediaListAdapter extends ArrayAdapter<String> {

		private LayoutInflater mInflater;

		public MediaListAdapter(Context context, int resource, List<String> filePathList) {
			super (context, resource, filePathList);
			mContext = context;
			mMediaList  = filePathList;
			mInflater = LayoutInflater.from(context);
		}

		public int getCount() {
			return mMediaList.size();
		}

		@Override
		public String getItem(int position) {
			return mMediaList.get(position);
		}
		
		public void addAll( List<String> mediaFile) {
			if(mediaFile != null){
				int count = mediaFile.size();
				for(int i = 0; i < count; i++){
					if(mMediaList.contains(mediaFile.get(i))){

					}else{
						mMediaList.add(mediaFile.get(i));
					}
				}
			}
		}

		public long getItemId(int position) {
			return position;
		}

		class ViewHolder {
			ImageView img_post;
			View btn_delete;
			TextView txt_path;
		}

		public View getView(final int position, View convertView, ViewGroup parent) {
			final ViewHolder holder;
			if (convertView == null) {
				
				mWidth = mContext.getResources().getDisplayMetrics().widthPixels - 80;  

				convertView = mInflater.inflate(R.layout.item_post_media, parent, false);
				holder = new ViewHolder();
				holder.img_post = (ImageView)convertView.findViewById(R.id.img_post);
				holder.btn_delete = convertView.findViewById(R.id.btn_delete);
				holder.txt_path = (TextView)convertView.findViewById(R.id.txt_path);
				convertView.setTag(holder);

			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			
			LayoutParams imageParams = (LayoutParams) holder.img_post.getLayoutParams();
			imageParams.width  = mWidth;
			imageParams.height = mWidth;
			
			holder.img_post.setLayoutParams(imageParams);

			File mediaFile = new File(mMediaList.get(position));
			
			if(mediaFile.exists()){
				if(mediaFile.getPath().contains("mp4") || mediaFile.getPath().contains("wmv") ||
						mediaFile.getPath().contains("avi") || mediaFile.getPath().contains("3gp") ){
//					new VideoLoadAsync(videoFragment, holder.img_post, false, mWidth/2).executeOnExecutor(MediaAsync.THREAD_POOL_EXECUTOR, mediaFile.getAbsolutePath());
					holder.img_post.setImageBitmap(null);
					int sdk = android.os.Build.VERSION.SDK_INT;
					if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
						holder.img_post.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.ic_video));
					} else {
						holder.img_post.setBackground(mContext.getResources().getDrawable(R.drawable.ic_video));
					}
				}
				else
				{
					ImageLoadAsync loadAsync = new ImageLoadAsync(mContext, holder.img_post, mWidth);
					loadAsync.executeOnExecutor(MediaAsync.THREAD_POOL_EXECUTOR, mediaFile.getAbsolutePath());
				}
				
				holder.txt_path.setText(mediaFile.getName());
			}
			
//			if(mediaFile.exists()){
//				if(mediaFile.getPath().contains("mp4") || mediaFile.getPath().contains("wmv") ||
//						mediaFile.getPath().contains("avi") || mediaFile.getPath().contains("3gp") ){
//					holder.img_post.setImageBitmap(null);
//
//					int sdk = android.os.Build.VERSION.SDK_INT;
//					if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
//						holder.img_post.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.ic_video));
//					} else {
//						holder.img_post.setBackground(mContext.getResources().getDrawable(R.drawable.ic_video));
//					}
//				}else{
//					Options options = new Options();
//					options.inPurgeable = true;
//					options.inSampleSize = 2;
//					
//					Bitmap myBitmap = BitmapFactory.decodeFile(mediaFile.getAbsolutePath(), options);
//					holder.img_post.setImageBitmap(myBitmap);
//				}
//				
//				holder.txt_path.setText(mediaFile.getName());
//
//			}

			holder.btn_delete.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					final MyDialog dialog = new MyDialog(instance);
					dialog.setContentView(R.layout.dialog_delete);
					dialog.show();

					dialog.findViewById(R.id.btn_no).setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							dialog.dismiss();
						}
					});
					dialog.findViewById(R.id.btn_yes).setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							dialog.dismiss();
							mMediaList.remove(position);
							mediaAdapter.notifyDataSetChanged();
						}
					});
				}
			});

			return convertView;
		}
	}

}
