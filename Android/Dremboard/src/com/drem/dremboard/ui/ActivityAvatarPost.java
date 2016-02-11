package com.drem.dremboard.ui;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import com.drem.dremboard.Const;
import com.drem.dremboard.R;
import com.drem.dremboard.entity.DremerInfo;
import com.drem.dremboard.entity.GlobalValue;
import com.drem.dremboard.entity.ProfileItem;
import com.drem.dremboard.entity.Beans.GetDremboardsResult;
import com.drem.dremboard.entity.Beans.GetSingleDremerParam;
import com.drem.dremboard.entity.Beans.GetSingleDremerResult;
import com.drem.dremboard.entity.Beans.SetSingleDremerImageParam;
import com.drem.dremboard.entity.Beans.SetSingleDremerImageResult;
import com.drem.dremboard.utils.AppPreferences;
import com.drem.dremboard.utils.MyDialog;
import com.drem.dremboard.utils.ResourceUtil;
import com.drem.dremboard.view.CustomToast;
import com.drem.dremboard.view.WaitDialog;
import com.drem.dremboard.webservice.Constants;
import com.drem.dremboard.webservice.WebApiCallback;
import com.drem.dremboard.webservice.WebApiInstance;
import com.drem.dremboard.webservice.WebApiInstance.Type;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;


@SuppressLint("NewApi")
public class ActivityAvatarPost extends Activity implements OnClickListener, WebApiCallback {
	public static ActivityAvatarPost instance = null;

	View mBtnDelete;
	ImageView mImgAvatar;
	private AppPreferences mPrefs;
	WaitDialog waitDialog;

	private Bitmap mOrgBmp = null;
	private String tempFilePath = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		instance = this;

		setContentView(R.layout.activity_avatar_post);

		mPrefs = new AppPreferences(this);
		waitDialog = new WaitDialog(this);
		
		mImgAvatar = (ImageView) findViewById(R.id.imgAvatar);
		mBtnDelete = findViewById(R.id.btnDelete);
		mBtnDelete.setVisibility(View.INVISIBLE);
		
		findViewById(R.id.img_camera).setOnClickListener(this);
		findViewById(R.id.btnPost).setOnClickListener(this);
		findViewById(R.id.btnBack).setOnClickListener(this);
		findViewById(R.id.btnDelete).setOnClickListener(this);
	}

	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		int id = view.getId();
		
		switch (id) {
		case R.id.img_camera: {
			String [] items = getResources().getStringArray(R.array.avatar_kinds);
			new AlertDialog.Builder(instance)
			.setSingleChoiceItems(items, -1, new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					dialog.dismiss();
					switch (which) {
					case 0:
						ImportImageFromCamera();
						break;

					case 1:
						ImportImageFromGallery();
						break;
					}
				}
			})
			.show();
		} break;

		case R.id.btnBack:
			finish();
			overridePendingTransition(R.anim.in_left_right, R.anim.out_left_right);		
			break;
			
		case R.id.btnDelete:
			doDeleteAvatar();
			break;
			
		case R.id.btnPost:
			if (isValid())
				doPost();
			break;
		}
	}

	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
		super.onActivityResult(requestCode, resultCode, intent);

		if (resultCode == Activity.RESULT_OK) {
			if (requestCode == Const.REQ_IMAGE_FROM_CAMERA_CROP) {
				// select camera for picture file
				String cameraFileName = ResourceUtil.getCameraFilePath(this);
				tempFilePath = ResourceUtil.getNewImageFilePath(this);

				Intent data = new Intent("com.android.camera.action.CROP");
				Uri imageCaptureUri = Uri.fromFile(new File(cameraFileName));
				Uri imageOutputUri = Uri.fromFile(new File(tempFilePath));
				data.setDataAndType(imageCaptureUri, "image/*");
				data.putExtra("aspectX", 1);
				data.putExtra("aspectY", 1);
				data.putExtra("outputX", 150);
				data.putExtra("outputY", 150);
				data.putExtra("scale", true);
				data.putExtra("return-data", true);		
				data.putExtra(MediaStore.EXTRA_OUTPUT, imageOutputUri);
				startActivityForResult(data, Const.REQ_IMAGE_FROM_GALLERY);

			} else if (requestCode == Const.REQ_IMAGE_FROM_GALLERY_CROP) {
				Bitmap bm = BitmapFactory.decodeFile(tempFilePath);
				if (bm == null) {
					// This is the case when using Photos app and it is not cropped.
					Intent data = new Intent("com.android.camera.action.CROP");
					Uri imageOutputUri = Uri.fromFile(new File(tempFilePath));
					Uri photoUri = intent.getData();
					data.putExtra("aspectX", 1);
					data.putExtra("aspectY", 1);
					data.putExtra("outputX", 150);
					data.putExtra("outputY", 150);
					data.putExtra("scale", true);
					data.putExtra("return-data", true);
					data.putExtra(MediaStore.EXTRA_OUTPUT, imageOutputUri);
					startActivityForResult(data, Const.REQ_IMAGE_FROM_GALLERY);

				} else {
					// this is the case when using Gallery app and it is already cropped. 
					if (mOrgBmp != null)
						mOrgBmp.recycle();
					
					mOrgBmp = bm;
					mImgAvatar.setImageDrawable(new BitmapDrawable(mOrgBmp));
					mBtnDelete.setVisibility(View.VISIBLE);
				}

			} else if (requestCode == Const.REQ_IMAGE_FROM_GALLERY) {
				// select gallery for picture
				Bitmap bm = BitmapFactory.decodeFile(tempFilePath);

				if (bm != null) {
					if (mOrgBmp != null)
						mOrgBmp.recycle();
					mOrgBmp = bm;
					mImgAvatar.setImageDrawable(new BitmapDrawable(mOrgBmp));
					mBtnDelete.setVisibility(View.VISIBLE);
				} else {
					Log.i(getString(R.string.app_name), "Bitmap is null");
				}
			}
		}
	}

	private boolean isValid() {
		if (mBtnDelete.getVisibility() == View.INVISIBLE) {
			CustomToast.makeCustomToastShort(this, "No post avatar");
			return false;
		}
		return true;
	}

	private void doDeleteAvatar() {
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
				mImgAvatar.invalidate();
				mImgAvatar.setImageBitmap(null);
				mBtnDelete.setVisibility(View.INVISIBLE);
			}
		});
	}
	
	private File savebitmap(Bitmap bmp) {
	    OutputStream outStream = null;

	    File file = new File(bmp + ".png");
	    if (file.exists()) {
	        file.delete();
	        file = new File(tempFilePath);
	        Log.e("file exist", "" + file + ",Bitmap= " + bmp);
	    }
	    try {
	        outStream = new FileOutputStream(file);
	        bmp.compress(Bitmap.CompressFormat.PNG, 100, outStream);
	        outStream.flush();
	        outStream.close();
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    Log.e("file", "" + file);
	    return file;

	}
	
	private void doPost() {
		SetSingleDremerImageParam param = new SetSingleDremerImageParam();
		
		param.user_id = mPrefs.getUserId();
		param.disp_user_id = Integer.parseInt(mPrefs.getUserId());
		param.avatar = mOrgBmp;
		param.crop_x = 0;
		param.crop_y = 0;
		param.crop_w = 150;
		param.crop_h = 150;
		
		WebApiInstance.getInstance().executeAPI(Type.SET_SINGLE_DREMER_IMAGE, param, this);

		waitDialog.show();

	}

	private void ImportImageFromCamera() {
		try {
			File file = new File(ResourceUtil.getCameraFilePath(instance));
			if (file.exists()) {
				file.delete();
			}
			file.createNewFile();

			Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
			startActivityForResult(intent, Const.REQ_IMAGE_FROM_CAMERA_CROP);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void ImportImageFromGallery() {
		try {
			tempFilePath = ResourceUtil.getNewImageFilePath(instance);
			File file = new File(tempFilePath);
			if (file.exists()) {
				file.delete();
			}
			file.createNewFile();

			Intent photoPickerIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
			photoPickerIntent.setType("image/*");
			photoPickerIntent.putExtra("crop", "true");
			photoPickerIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
			photoPickerIntent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
			startActivityForResult(photoPickerIntent, Const.REQ_IMAGE_FROM_GALLERY_CROP);

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	private void ImportVideoFromCamera() {
		try {
			File file = new File(ResourceUtil.getVideoFilePath(instance));
			if (file.exists()) {
				file.delete();
			}
			file.createNewFile();

			Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
			intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
			if (intent.resolveActivity(instance.getPackageManager()) != null)
				startActivityForResult(intent, Const.REQ_VIDEO_FROM_CAMERA);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void ImportVideoFromGallery() {
		Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT);
		getIntent.setType("video/*");

		Intent pickIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
		pickIntent.setType("video/*");

		Intent chooserIntent = Intent.createChooser(getIntent, "Select Video");
		chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[] {pickIntent});

		startActivityForResult(chooserIntent, Const.REQ_VIDEO_FROM_GALLERY);
	}

	private void setCurrentDremer(DremerInfo dremer)
	{
		GlobalValue.getInstance().setCurrentDremer(dremer);
	}
	
	private void setCurrentProfiles(ArrayList<ProfileItem> profiles)
	{
		GlobalValue.getInstance().setCurrentProfiles(profiles);
	}

	private void changeAvatar()
	{
		if (ActivityMain.instance != null)
			ActivityMain.instance.onChangeAvatar();
		
		if (ActivityDremer.instance != null)
			ActivityDremer.instance.onChangeAvatar();
	}
	
	private void getSingleDremer() {
		
		GetSingleDremerParam param = new GetSingleDremerParam();

		param.user_id = mPrefs.getUserId();
		param.disp_user_id = Integer.parseInt(mPrefs.getUserId());

		WebApiInstance.getInstance().executeAPI(Type.GET_SINGLE_DREMER, param, this);
	}

	private void getSingleDremerResult(Object param, Object obj) {

		if (obj == null) {
			CustomToast.makeCustomToastShort(this, Constants.NETWORK_ERR);
		}

		if (obj != null){
			GetSingleDremerResult resultBean = (GetSingleDremerResult)obj;

			if (resultBean.status.equals("ok")) {
				setCurrentDremer(resultBean.data.member);
				setCurrentProfiles(resultBean.data.profiles);
				changeAvatar();
			} else {
				CustomToast.makeCustomToastShort(this, resultBean.msg);
			}
		}
	}
	
	private void setSingleDremerImageResult(Object obj) {

		waitDialog.dismiss();

		if (obj == null) {
			CustomToast.makeCustomToastShort(this, Constants.NETWORK_ERR);
		}

		if (obj != null){
			SetSingleDremerImageResult resultBean = (SetSingleDremerImageResult)obj;

			if (resultBean.status.equals("ok")) {
				getSingleDremer();
				finish();
			} else {
				CustomToast.makeCustomToastShort(this, resultBean.msg);
				finish();
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
			case SET_SINGLE_DREMER_IMAGE:
				setSingleDremerImageResult(result);			
				break;
				
			case GET_SINGLE_DREMER:
				getSingleDremerResult(parameter, result);			
				break;
				
			default:
				break;
		}
	}

}
