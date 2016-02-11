package com.drem.dremboard.ui;

import java.util.ArrayList;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.drem.dremboard.R;
import com.drem.dremboard.entity.DremInfo;
import com.drem.dremboard.entity.DremboardInfo;
import com.drem.dremboard.entity.GlobalValue;
import com.drem.dremboard.entity.Beans.GetDremboardsParam;
import com.drem.dremboard.entity.Beans.GetDremboardsResult;
import com.drem.dremboard.entity.Beans.AddDremToDremboardResult;
import com.drem.dremboard.entity.Beans.AddDremToDremboardData;
import com.drem.dremboard.entity.Beans.AddDremToDremboardResult;
import com.drem.dremboard.ui.DialogFlagDrem.OnFlagResultCallback;
import com.drem.dremboard.utils.AppPreferences;
import com.drem.dremboard.view.CustomToast;
import com.drem.dremboard.view.WaitDialog;
import com.drem.dremboard.webservice.Constants;
import com.drem.dremboard.webservice.WebApiCallback;
import com.drem.dremboard.webservice.WebApiInstance;
import com.drem.dremboard.webservice.WebApiInstance.Type;


public class ActivityAddDremToDremboard extends Activity implements OnClickListener, WebApiCallback, OnFlagResultCallback {
	Activity activity;
	Button mBtnClose;
	Button btnAddDremboard, btnNewDremboard;
	Spinner mSpinGender;
	boolean mFlagLoading = false;
	
	ArrayList<String> mArrayGender;
	ArrayList<DremboardInfo> mArrayDremboards;

	private AppPreferences mPrefs;
	WaitDialog waitDialog;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setTheme(R.style.DialogHoloLightTheme);
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_add_drem_to_dremboard);
		mPrefs = new AppPreferences(this);
		waitDialog = new WaitDialog(this);
		initView();
	}

	@Override
	public void onResume(){
		super.onResume();
		// put your code here...
	}
	
	private void initView()
	{
		
		getDremboardList(0, 30);
		mArrayDremboards = new ArrayList<DremboardInfo>();

		btnAddDremboard = (Button) findViewById(R.id.btnAddDremboard);
		btnNewDremboard = (Button) findViewById(R.id.btnNewDremboard);

		mSpinGender = (Spinner) findViewById(R.id.spinCategory);
		mSpinGender.setPrompt("Select Gender");

		mArrayGender = new ArrayList<String>();
		mArrayGender.add("----");

		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, mArrayGender);
		dataAdapter.setDropDownViewResource(R.layout.custom_spinner_dropdown_item);
		mSpinGender.setAdapter(dataAdapter);
		
		mBtnClose = (Button) findViewById(R.id.btnClose);
		mBtnClose.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
				overridePendingTransition(R.anim.in_left_right, R.anim.out_left_right);			}
		});

		btnAddDremboard.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				_addDremToDremboard();
			}
		});
		
		btnNewDremboard.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				ViewCreateNewDremboard();
			}
		});		
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

	private void _addDremToDremboard()
	{
		String spinnertext = mSpinGender.getSelectedItem().toString();
		
		if (spinnertext.isEmpty() || spinnertext.equals("----"))
		{
			CustomToast.makeCustomToastShort(this, "Please select the dremboard.");
			return;
		}
		
		DremInfo mDrem = GlobalValue.getInstance().getCurrentDrem();
		
		AddDremToDremboardData param = new AddDremToDremboardData();
		
		param.user_id = mPrefs.getUserId();
		param.drem_id = mDrem.id;
		param.dremboard_id = _getDremboardId(spinnertext);

		WebApiInstance.getInstance().executeAPI(Type.ADD_DREM_TO_DREMBOARD, param, this);

		waitDialog.show();
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

		WebApiInstance.getInstance().executeAPI(Type.GET_DREMBOARD, param, this);

		waitDialog.show();

	}
	
	private void addDremboards(ArrayList<DremboardInfo> arrayBoards) {

		if (arrayBoards == null)
			return;
		for (DremboardInfo board : arrayBoards) {
			mArrayDremboards.add(board);
			
			if (Integer.parseInt(mPrefs.getUserId()) == board.media_author_id)
				mArrayGender.add(board.media_title);
		}
		
		ArrayAdapter<String> dataDremboardTitle = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, mArrayGender);
		dataDremboardTitle.setDropDownViewResource(R.layout.custom_spinner_dropdown_item);
		mSpinGender.setAdapter(dataDremboardTitle);
	}
	
	private void getDremboardListResult(Object obj) {

		waitDialog.dismiss();
		mFlagLoading = false;

		if (obj == null) {
			CustomToast.makeCustomToastShort(this, Constants.NETWORK_ERR);
		}

		if (obj != null){
			GetDremboardsResult resultBean = (GetDremboardsResult)obj;

			if (resultBean.status.equals("ok")) {
				addDremboards(resultBean.data.media);
			} else {
				CustomToast.makeCustomToastShort(this, resultBean.msg);
			}
		}
	}
	
	private void addDremToDremboardResult(Object obj) {
		waitDialog.dismiss();
		mFlagLoading = false;

		if (obj == null) {
			CustomToast.makeCustomToastShort(this, Constants.NETWORK_ERR);
		}

		if (obj != null){
			AddDremToDremboardResult resultBean = (AddDremToDremboardResult)obj;
			CustomToast.makeCustomToastShort(this, resultBean.msg);
		}
	}

	public void ViewCreateNewDremboard() {
		Intent intent = new Intent();
		intent.setClass(this, ActivityCreateNewDremboard.class);
		startActivityForResult(intent, 1);
		overridePendingTransition(R.anim.in_right_left, R.anim.out_right_left);
	}	
	
	@Override
	public void onFinishSetFlag(String strAlert, int index) {
		// TODO Auto-generated method stub
		CustomToast.makeCustomToastLong(this, strAlert);
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
			case GET_DREMBOARD:
				getDremboardListResult(result);			
				break;
				
			case ADD_DREM_TO_DREMBOARD:
				addDremToDremboardResult(result);
				break;
				
			default:
				break;
		}
	}
	

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
	    if (requestCode == 1) {
	        if(resultCode == Activity.RESULT_OK){
	        	mArrayGender.clear();
	    		mArrayGender.add("----");
	    		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
	    				android.R.layout.simple_spinner_item, mArrayGender);
	    		dataAdapter.setDropDownViewResource(R.layout.custom_spinner_dropdown_item);
	    		mSpinGender.setAdapter(dataAdapter);
	    		getDremboardList(0, 30);
	        }
	        if (resultCode == Activity.RESULT_CANCELED) {
	            //Write your code if there's no result
	        }
	    }
	}//onActivityResult
}

