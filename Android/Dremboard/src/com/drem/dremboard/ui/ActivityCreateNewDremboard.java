package com.drem.dremboard.ui;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.AdapterView.OnItemSelectedListener;

import com.drem.dremboard.R;
import com.drem.dremboard.entity.DremInfo;
import com.drem.dremboard.entity.GlobalValue;
import com.drem.dremboard.entity.Beans.AddDremToDremboardData;
import com.drem.dremboard.entity.Beans.AddDremToDremboardResult;
import com.drem.dremboard.entity.Beans.CreateDremboardData;
import com.drem.dremboard.entity.Beans.CreateDremboardResult;
import com.drem.dremboard.ui.DialogFlagDrem.OnFlagResultCallback;
import com.drem.dremboard.utils.AppPreferences;
import com.drem.dremboard.view.CustomToast;
import com.drem.dremboard.view.WaitDialog;
import com.drem.dremboard.webservice.Constants;
import com.drem.dremboard.webservice.WebApiCallback;
import com.drem.dremboard.webservice.WebApiInstance;
import com.drem.dremboard.webservice.WebApiInstance.Type;


public class ActivityCreateNewDremboard extends Activity implements OnClickListener, WebApiCallback, OnFlagResultCallback {
	Activity activity;
	Button mBtnClose;
	Button btnCreateDremboard;
	String mCategoryId;
	Spinner mSpinCategory;
	ArrayList<String> mArrayCategory;
	HashMap<String, String> mMapCategory;
	EditText mTxtTitle, mTxtDescription;
	RadioButton radioPersonal, radioPublic, radioFamily, radioFriends;
	WaitDialog waitDialog;
	private AppPreferences mPrefs;


	@Override
	public void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setTheme(R.style.DialogHoloLightTheme);
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_create_new_dremboard);
		waitDialog = new WaitDialog(this);
		mPrefs = new AppPreferences(this);

		initView();
	}

	@Override
	public void onResume(){
		super.onResume();
		// put your code here...
	}
	
	private void initView()
	{
		mTxtTitle = (EditText) findViewById(R.id.txtDremboardTitle);
		mTxtDescription = (EditText) findViewById(R.id.txtDremboardDescription);
		radioPublic = (RadioButton) findViewById(R.id.radioPublic);
		radioPersonal = (RadioButton) findViewById(R.id.radioPersonal);
		radioFamily = (RadioButton) findViewById(R.id.radioFamily);
		radioFriends = (RadioButton) findViewById(R.id.radioFriends);
		
		mBtnClose = (Button) findViewById(R.id.btnClose);
		mBtnClose.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
				overridePendingTransition(R.anim.in_left_right, R.anim.out_left_right);			}
		});
		
		btnCreateDremboard = (Button) findViewById(R.id.btnCreateDremboard);
		btnCreateDremboard.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				_createNewDremboard();
			}
		});
		
		mSpinCategory = (Spinner) findViewById(R.id.spinDremboardCategory);
		mSpinCategory.setPrompt("Select Category");
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
		mSpinCategory.setAdapter(adapterCategory);
		mSpinCategory.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				
				// same logic when click search button <<
				String cate_val = mSpinCategory.getSelectedItem().toString();
				mCategoryId = mMapCategory.get(cate_val);
			}
				// >>

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub

			}
		});
	}

	private void setCategories() throws JSONException {
		String category_json = mPrefs.getCategoryList();
		JSONObject category_obj = new JSONObject(category_json);
		JSONArray cate_key_obj = category_obj.getJSONArray("keys");
		JSONArray cate_val_obj = category_obj.getJSONArray("values");

		for (int i = 0; i < cate_key_obj.length(); i++) {
			if (cate_val_obj.getString(i).equals("All categories") || 
					cate_val_obj.getString(i).equals("Uncategorized"))
				continue;
			
			mArrayCategory.add(cate_val_obj.getString(i));
			mMapCategory.put(cate_val_obj.getString(i),
					cate_key_obj.getString(i));
		}
	}
	
	private void _createNewDremboard()
	{
		if (mTxtTitle.getText().toString().isEmpty())
		{
			CustomToast.makeCustomToastShort(this, "Please input a title.");
			return;
		}
	
		CreateDremboardData param = new CreateDremboardData();
		
		param.user_id = mPrefs.getUserId();
		param.title = mTxtTitle.getText().toString();
		param.description = mTxtDescription.getText().toString();
		param.category_id = Integer.parseInt(mCategoryId);
		
		if(radioPublic.isChecked())
			param.privacy = 0;
		else if (radioPersonal.isChecked())
			param.privacy = 1;
		else if (radioFamily.isChecked())
			param.privacy = 2;
		else if (radioFriends.isChecked())
			param.privacy = 3;
		else
			param.privacy = 0;

		WebApiInstance.getInstance().executeAPI(Type.CREATE_DREMBOARD, param, this);

		waitDialog.show();
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

	private void createNewDremboardResult(Object obj) {
		waitDialog.dismiss();

		if (obj == null) {
			CustomToast.makeCustomToastShort(this, Constants.NETWORK_ERR);
		}

		if (obj != null){
			CreateDremboardResult resultBean = (CreateDremboardResult)obj;
			if (resultBean.status.equals("ok")) {
				TextView title = new TextView(ActivityCreateNewDremboard.this);
				title.setText(R.string.text_new_dremboard_title);
				title.setTextSize(16);
				title.setPadding(0, 40, 0, 40);
				title.setGravity(Gravity.CENTER);
				
				AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
					this);

				alertDialogBuilder.setCustomTitle(title);
				alertDialogBuilder
					.setMessage(mTxtTitle.getText().toString() + " album created" + "\n" + "successfully.")
					.setCancelable(false)
					.setPositiveButton(Html.fromHtml("<b>" + "Ok" + "</b>"), new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,int id) {
							Intent intent = new Intent();
							intent.setClass(ActivityCreateNewDremboard.this, ActivityAddDremToDremboard.class);
							ActivityCreateNewDremboard.this.setResult(ActivityCreateNewDremboard.this.RESULT_OK, intent);
							ActivityCreateNewDremboard.this.finish();
						}
					  });

				// create alert dialog
				AlertDialog alertDialog = alertDialogBuilder.create();

				// show it
				alertDialog.show();
				TextView messageText = (TextView)alertDialog.findViewById(android.R.id.message);
		        messageText.setGravity(Gravity.CENTER);
			}
			else {
				CustomToast.makeCustomToastShort(this, resultBean.msg);
			}
		}
	}
	
	@Override
	public void onResultProcessing(Type type, Object parameter, Object result) {
		switch (type)
		{
			case CREATE_DREMBOARD:
				createNewDremboardResult(result);
				break;
				
			default:
				break;
		}		
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
	}
}
