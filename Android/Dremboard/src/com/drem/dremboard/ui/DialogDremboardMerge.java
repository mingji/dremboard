package com.drem.dremboard.ui;

import java.util.ArrayList;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.drem.dremboard.R;
import com.drem.dremboard.entity.DremboardInfo;
import com.drem.dremboard.entity.GlobalValue;
import com.drem.dremboard.entity.Beans.MergeDremboardData;
import com.drem.dremboard.entity.Beans.MergeDremboardResult;
import com.drem.dremboard.entity.Beans.GetDremboardsParam;
import com.drem.dremboard.entity.Beans.GetDremboardsResult;
import com.drem.dremboard.utils.AppPreferences;
import com.drem.dremboard.view.CustomToast;
import com.drem.dremboard.view.WaitDialog;
import com.drem.dremboard.webservice.Constants;
import com.drem.dremboard.webservice.WebApiCallback;
import com.drem.dremboard.webservice.WebApiInstance;
import com.drem.dremboard.webservice.WebApiInstance.Type;

public class DialogDremboardMerge extends Dialog implements OnClickListener, WebApiCallback  {

	Activity activity;

	Button mBtnClose, mBtnMerge;
	Spinner mSpinGender;
	ArrayList<String> mArrayGender;
	ArrayList<DremboardInfo> mArrayDremboards;
	
	boolean mFlagLoading = false;

	WaitDialog waitDialog;
	DremboardInfo mDremboard;
	private AppPreferences mPrefs;

	public DialogDremboardMerge(Context context, Activity activity) {
		super(context);
		// TODO Auto-generated constructor stub
		this.activity = activity;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE | 
				WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

		setContentView(R.layout.dialog_dremboard_merge);

		setCancelable(true);		
				
		mPrefs = new AppPreferences(this.activity);
		waitDialog = new WaitDialog(this.activity);
		mArrayDremboards = new ArrayList<DremboardInfo>();

		mBtnClose = (Button) findViewById(R.id.btnClose);
		mBtnClose.setOnClickListener(this);
		
		mBtnMerge = (Button) findViewById(R.id.btnDremboardMerge);
		mBtnMerge.setOnClickListener(this);
		
		mSpinGender = (Spinner) findViewById(R.id.spinDremboardMergeCategory);
		mSpinGender.setPrompt("Select Gender");

		mArrayGender = new ArrayList<String>();
		mArrayGender.add("----");

		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this.activity,
				android.R.layout.simple_spinner_item, mArrayGender);
		dataAdapter.setDropDownViewResource(R.layout.custom_spinner_dropdown_item);
		mSpinGender.setAdapter(dataAdapter);
	
		mDremboard = GlobalValue.getInstance().getCurrentDremboard();
	}
	
	@Override
	public void onAttachedToWindow() {
		// TODO Auto-generated method stub
		super.onAttachedToWindow();
		getDremboardList(0, 30);
	}

	private void getDremboardList(int lastId, int count)	{

		if (mFlagLoading)
			return;

		mFlagLoading = true;
		_getDremboardList(lastId, count);
	}

	private void _getDremboardList(int lastId, int count)	{
		GetDremboardsParam param = new GetDremboardsParam();

		param.user_id = mPrefs.getUserId();
		param.author_id = mPrefs.getUserId();
		param.category = -1;		
		param.per_page = count;
		param.last_media_id = lastId;
		param.search_str = "";

		waitDialog.show();
		WebApiInstance.getInstance().executeAPI(Type.GET_DREMBOARD, param, this);
	}
	
	private void addDremboards(ArrayList<DremboardInfo> arrayBoards) {

		if (arrayBoards == null)
			return;
		for (DremboardInfo board : arrayBoards) {
			mArrayDremboards.add(board);
			
			if (Integer.parseInt(mPrefs.getUserId()) == board.media_author_id)
				mArrayGender.add(board.media_title);
		}
		
		ArrayAdapter<String> dataDremboardTitle = new ArrayAdapter<String>(this.activity,
				android.R.layout.simple_spinner_item, mArrayGender);
		dataDremboardTitle.setDropDownViewResource(R.layout.custom_spinner_dropdown_item);
		mSpinGender.setAdapter(dataDremboardTitle);
	}
	
	private void getDremboardListResult(Object obj) {
		mFlagLoading = false;
		waitDialog.dismiss();

		if (obj == null) {
			CustomToast.makeCustomToastShort(this.activity, Constants.NETWORK_ERR);
		}

		if (obj != null){
			GetDremboardsResult resultBean = (GetDremboardsResult)obj;

			if (resultBean.status.equals("ok")) {
				addDremboards(resultBean.data.media);
			} else {
				CustomToast.makeCustomToastShort(this.activity, resultBean.msg);
			}
		}
	}
	
	private int _getDremboardId (String boardTitle)
	{
	    int retId = -1;
	    
	    if (boardTitle.isEmpty())
	        return retId;
	    
	    for (DremboardInfo board : mArrayDremboards) {
	    	if (board.media_title.equals(boardTitle))
	    		return board.id;
	    }
	    
	    return retId;
	}
	
	private void mergeDremboard()
	{
		String spinnertext = mSpinGender.getSelectedItem().toString();
		
		if (spinnertext.isEmpty() || spinnertext.equals("----"))
		{
			CustomToast.makeCustomToastShort(this.activity, "Please select the dremboard.");
			return;
		}
		
		MergeDremboardData param = new MergeDremboardData();
		
		param.user_id = mPrefs.getUserId();
		param.source_id = mDremboard.id;
		param.target_id = _getDremboardId(spinnertext);

		WebApiInstance.getInstance().executeAPI(Type.MERGE_DREMBOARD, param, this);

		waitDialog.show();	
	}
	
	private void mergeDremboardResult(Object obj) {
		waitDialog.dismiss();

		if (obj == null) {
			CustomToast.makeCustomToastShort(this.activity, Constants.NETWORK_ERR);
		}

		if (obj != null){
			MergeDremboardResult resultBean = (MergeDremboardResult)obj;
			CustomToast.makeCustomToastShort(this.activity, resultBean.msg);
		}

		this.cancel();
		Intent intent = new Intent(this.activity, FragmentDremboards.class);
		this.activity.setResult(this.activity.RESULT_OK, intent);
		this.activity.finish();
		this.activity.overridePendingTransition(R.anim.in_left_right, R.anim.out_left_right);
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
			case R.id.btnClose:
				this.cancel();
				this.activity.overridePendingTransition(R.anim.in_left_right, R.anim.out_left_right);
				break;
				
			case R.id.btnDremboardMerge:
				mergeDremboard();
				break;
		
			default:
				break;
		}
	}

	@Override
	public void onPreProcessing(Type type, Object parameter) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onResultProcessing(Type type, Object parameter, Object result) {
		switch (type)
		{
			case GET_DREMBOARD:
				getDremboardListResult(result);			
				break;
				
			case MERGE_DREMBOARD:
				mergeDremboardResult(result);			
				break;
			default:
				break;
		}
	}
}
