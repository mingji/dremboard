package com.drem.dremboard.ui;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.MovementMethod;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.Toast;

import com.drem.dremboard.R;
import com.drem.dremboard.entity.Beans.RemoveDremsFromDremboardData;
import com.drem.dremboard.entity.Beans.RemoveDremsFromDremboardResult;
import com.drem.dremboard.entity.DremInfo;
import com.drem.dremboard.entity.DremboardInfo;
import com.drem.dremboard.entity.DremerInfo;
import com.drem.dremboard.entity.GlobalValue;
import com.drem.dremboard.entity.Beans.DeleteDremboardData;
import com.drem.dremboard.entity.Beans.GetDremsParam;
import com.drem.dremboard.entity.Beans.GetDremsResult;
import com.drem.dremboard.entity.Beans.MoveDremsToDremboardResult;
import com.drem.dremboard.entity.Beans.SetFavoriteParam;
import com.drem.dremboard.entity.Beans.SetFavoriteResult;
import com.drem.dremboard.entity.Beans.SetLikeParam;
import com.drem.dremboard.entity.Beans.SetLikeResult;
import com.drem.dremboard.ui.ActivityBoardManageDrems.DremAdapter.DremManageHolder;
import com.drem.dremboard.ui.DialogFlagDrem.OnFlagResultCallback;
import com.drem.dremboard.ui.FragmentDremboards.DremboardAdapter.DremboardHolder;
import com.drem.dremboard.ui.FragmentDrems.DremAdapter;
import com.drem.dremboard.ui.FragmentDrems.DremAdapter.DremHolder;
import com.drem.dremboard.utils.AppPreferences;
import com.drem.dremboard.utils.ImageLoader;
import com.drem.dremboard.view.AdminSearchView;
import com.drem.dremboard.view.CustomToast;
import com.drem.dremboard.view.WebCircularImgView;
import com.drem.dremboard.view.WebImgView;
import com.drem.dremboard.view.WaitDialog;
import com.drem.dremboard.webservice.Constants;
import com.drem.dremboard.webservice.WebApiCallback;
import com.drem.dremboard.webservice.WebApiInstance;
import com.drem.dremboard.webservice.WebApiInstance.Type;

public class ActivityBoardManageDrems extends Activity
		implements OnClickListener, WebApiCallback, OnFlagResultCallback {

	AppPreferences mPrefs;
	
	Button mBtnBack;
	CheckBox mChkSelectAll;
	GridView mGridDrems;
	TextView mTxtMove, mTxtDelete;
	
	DremAdapter mAdapterDrem;
	ArrayList<DremInfo> mArrayDrems;
	ArrayList<DremManageHolder> mArrayDremsHolder;

	DremboardInfo mDremboard;
	
	WaitDialog waitDialog;

	public static ActivityBoardManageDrems instance;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		setTheme(R.style.ActivityTheme);
		super.onCreate(savedInstanceState);
		//requestWindowFeature(Window.FEATURE_NO_TITLE);		
		setContentView(R.layout.activity_dremboard_manage);
		mPrefs = new AppPreferences(this);
		waitDialog = new WaitDialog(this);

		instance = this;
		
		mArrayDrems = DialogDremboardOptions.instance.mArrayDrems;
		mArrayDremsHolder = new ArrayList<DremManageHolder>();

		mDremboard = GlobalValue.getInstance().getCurrentDremboard();
		if (mDremboard.id == -1) { // check valid
			finish();
			return;
		}

		initView();
	
	}

	private void initView() {
		mBtnBack = (Button) findViewById(R.id.btnBack);
		mBtnBack.setOnClickListener(this);

		mChkSelectAll = (CheckBox) findViewById(R.id.chkManageOption);
		mTxtMove = (TextView) findViewById(R.id.txtManageMove);
		mTxtDelete = (TextView) findViewById(R.id.txtManageDelete);
		
		mGridDrems = (GridView) findViewById(R.id.gridDrems);
		
		mChkSelectAll.setOnCheckedChangeListener(new OnCheckedChangeListener()
		{
		    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
		    {
				mAdapterDrem.notifyDataSetChanged();
		    }
		});
		
		mTxtMove.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View viewIn) {
            	int flag = 0;
            	for (int i = 0; i < mArrayDremsHolder.size() - 1; i ++)
            	{
            		if (mArrayDremsHolder.get(i).chkCategory.isChecked())
            		{
            			flag = 1;
            		}
            	}
            	
            	if (flag == 0)
            	{
            	     Toast.makeText(ActivityBoardManageDrems.this, "Please select some media.", Toast.LENGTH_LONG).show();
            	}
            	else
            	{
            		moveManageMedia();
            	}
            }
        });
		
		mTxtDelete.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View viewIn) {
            	int flag = 0;
            	for (int i = 0; i < mArrayDremsHolder.size() -1; i ++)
            	{
            		if (mArrayDremsHolder.get(i).chkCategory.isChecked())
            		{
            			flag = 1;
            		}
            	}
            	
            	if (flag == 0)
            	{
            	     Toast.makeText(ActivityBoardManageDrems.this, "Please select some media.", Toast.LENGTH_LONG).show();
            	}
            	else
            	{
            		deleteManageMedia();
            	}
            }
        });
		
		mAdapterDrem = new DremAdapter(this, R.layout.item_drem_manage,
				mArrayDrems);

		mGridDrems = (GridView) findViewById(R.id.gridDrems);
		mGridDrems.setAdapter(mAdapterDrem);

	}
	
	private void moveManageMedia()
	{
		DialogMoveDremsToDremboard moveDremsToDremboardDiag = new DialogMoveDremsToDremboard(this, this, mArrayDremsHolder);
		moveDremsToDremboardDiag.show();
		this.overridePendingTransition(R.anim.in_left_right, R.anim.out_left_right);
	}
	
	private void deleteManageMedia() {
		TextView title = new TextView(this);
		title.setText(R.string.text_deleteManageMedia);
		title.setTextSize(18);
		title.setPadding(0, 40, 0, 40);
		title.setGravity(Gravity.CENTER);
						
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
				this);
		alertDialogBuilder.setCustomTitle(title);
		alertDialogBuilder
			.setMessage("Are you sure want to delete" + "\n" + "selected medias?")
			.setCancelable(false)
			.setPositiveButton("Delete",new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog,int id) {
					int requestFlag = 0;
					RemoveDremsFromDremboardData param = new RemoveDremsFromDremboardData();
					param.drem_ids = "";
					for (int i = 0; i < mArrayDremsHolder.size() - 1; i ++)
	            	{
	            		if (mArrayDremsHolder.get(i).chkCategory.isChecked())
	            		{
	            			requestFlag = 1;
	            			
	            			if (param.drem_ids.equals(""))
	            			{
		            			param.drem_ids = String.format("%d",  mArrayDremsHolder.get(i).drem_id);
	            			}
	            			else
	            			{
		            			param.drem_ids = String.format("%s, %d",  param.drem_ids, mArrayDremsHolder.get(i).drem_id);	            				
	            			}
	            		}
	            	}

					if (requestFlag == 1)
					{
						WebApiInstance.getInstance().executeAPI(Type.REMOVE_DREMS_FROM_DREMBOARD, param, ActivityBoardManageDrems.this);
						waitDialog.show();
					}
				}
			  })
			.setNegativeButton("Cancel",new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog,int id) {
					dialog.cancel();
				}
			});

		// create alert dialog
		AlertDialog alertDialog = alertDialogBuilder.create();
		// show it
		alertDialog.show();
		TextView messageText = (TextView)alertDialog.findViewById(android.R.id.message);
        messageText.setGravity(Gravity.CENTER);
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		int id = v.getId();

		switch (id) {
		case R.id.btnBack:
			onBackButton();
			break;
		default:
			break;
		}
	}
	
	private void removeDremboardResult(Object obj) {
		waitDialog.dismiss();

		if (obj == null) {
			CustomToast.makeCustomToastShort(this, Constants.NETWORK_ERR);
		}

		if (obj != null){
			RemoveDremsFromDremboardResult resultBean = (RemoveDremsFromDremboardResult)obj;
			CustomToast.makeCustomToastShort(this, resultBean.msg);
			finish();
			this.overridePendingTransition(R.anim.in_left_right, R.anim.out_left_right);
			
			ActivityBoardDrems.instance.onRefreshDrems();

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
	
	@Override
	public void onPreProcessing(Type type, Object parameter) {
		// TODO Auto-generated method stub
		
	}

	
	@Override
	public void onResultProcessing(Type type, Object parameter, Object result) {
		switch (type)
		{
			case REMOVE_DREMS_FROM_DREMBOARD:
				removeDremboardResult(result);			
				break;
			default:
				break;
		}
	}

	@Override
	public void onFinishSetFlag(String strAlert, int index) {
		// TODO Auto-generated method stub

	}
	
	public class DremAdapter extends ArrayAdapter<DremInfo> implements OnClickListener{
		Activity activity;
		int layoutResourceId;
		ArrayList<DremInfo> item = new ArrayList<DremInfo>();

		public DremAdapter(Activity activity, int layoutId,
				ArrayList<DremInfo> items) {
			super(activity, layoutId, items);
			item = items;
			this.activity = activity;
			this.layoutResourceId = layoutId;
		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			DremManageHolder holder = null;

			final DremInfo dremItem = getItem(position);

			// inflate the view
			if (convertView == null) {
				LayoutInflater inflater = ((Activity) activity).getLayoutInflater();
				convertView = inflater.inflate(R.layout.item_drem_manage, null);
				
				holder = new DremManageHolder();

				holder.imgPic = (WebImgView) convertView.findViewById(R.id.imgPic);
				holder.chkCategory = (CheckBox) convertView.findViewById(R.id.chkCategory);
				holder.chkCategory.setText(dremItem.category);
				
				mArrayDremsHolder.add(holder);

				convertView.setTag(holder);
			} else
				holder = (DremManageHolder) convertView.getTag();

			holder.drem_id = dremItem.id;
			holder.imgPic.setTag(position);
			if (dremItem.guid != null && !dremItem.guid.isEmpty())
				ImageLoader.getInstance().displayImage(dremItem.guid, holder.imgPic, 0, 0);
			else
				holder.imgPic.imageView.setImageResource(R.drawable.sample_drem);

			if (mChkSelectAll.isChecked())
				holder.chkCategory.setChecked(true);
			else
				holder.chkCategory.setChecked(false);

			return convertView;

		}

		public class DremManageHolder {
			CheckBox chkCategory;
			WebImgView imgPic;
			int drem_id;
		}

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			
		}
	}
}
