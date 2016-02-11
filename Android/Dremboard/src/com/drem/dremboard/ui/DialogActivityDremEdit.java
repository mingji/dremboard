package com.drem.dremboard.ui;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.AdapterView.OnItemSelectedListener;

import com.drem.dremboard.R;
import com.drem.dremboard.entity.Beans.EditActivityDremData;
import com.drem.dremboard.entity.Beans.EditActivityDremResult;
import com.drem.dremboard.entity.DremActivityInfo;
import com.drem.dremboard.entity.DremInfo;
import com.drem.dremboard.entity.DremboardInfo;
import com.drem.dremboard.entity.GlobalValue;
import com.drem.dremboard.entity.Beans.DeleteDremboardResult;
import com.drem.dremboard.entity.Beans.EditDremboardData;
import com.drem.dremboard.entity.Beans.EditDremboardResult;
import com.drem.dremboard.utils.AppPreferences;
import com.drem.dremboard.utils.ImageLoader;
import com.drem.dremboard.utils.RestApi;
import com.drem.dremboard.view.CustomToast;
import com.drem.dremboard.view.WaitDialog;
import com.drem.dremboard.view.WebCircularImgView;
import com.drem.dremboard.webservice.Constants;
import com.drem.dremboard.webservice.WebApiCallback;
import com.drem.dremboard.webservice.WebApiInstance;
import com.drem.dremboard.webservice.WebApiInstance.Type;

public class DialogActivityDremEdit extends Dialog implements OnClickListener, WebApiCallback  {

	Activity activity;
	DremActivityInfo mActItem;
	EditText mEdtStatus;
	Button mBtnClose, mBtnSave;
	WebCircularImgView imgAuthor;
	
	String mCategoryId;
	Spinner mSpinCategory;
	ArrayList<String> mArrayCategory;
	HashMap<String, String> mMapCategory;
	
	private AppPreferences mPrefs;

	WaitDialog waitDialog;

	public DialogActivityDremEdit(Context context, Activity activity, DremActivityInfo Item) {
		super(context);
		// TODO Auto-generated constructor stub
		this.activity = activity;
		mActItem = Item;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		waitDialog = new WaitDialog(this.activity);
		mPrefs = new AppPreferences(this.activity);
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE | 
				WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

		setContentView(R.layout.dialog_act_edit);

		setCancelable(true);

		initView();
	}
	
	private void initView()
	{
		mEdtStatus = (EditText) findViewById(R.id.edtDremStatus);
		
		mBtnClose = (Button) findViewById(R.id.btnClose);
		mBtnClose.setOnClickListener(this);
		
		mBtnSave = (Button) findViewById(R.id.btnSaveChanges);
		mBtnSave.setOnClickListener(this);
		
		imgAuthor = (WebCircularImgView) findViewById(R.id.img_drem_author);
		
		if (mActItem.author_avatar != null && !mActItem.author_avatar.isEmpty())
			ImageLoader.getInstance().displayImage(mActItem.author_avatar, imgAuthor, 0, 0);
		else
			imgAuthor.imageView.setImageResource(R.drawable.empty_pic);

		mEdtStatus.setText(mActItem.description);
		
		mSpinCategory = (Spinner) findViewById(R.id.spinActEditCategory);
		mArrayCategory = new ArrayList<String>();
		mMapCategory = new HashMap<String, String>();
		
		try {
			setCategories();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		ArrayAdapter<String> adapterCategory = new ArrayAdapter<String>(
				this.activity, android.R.layout.simple_spinner_item,
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

		int pos=adapterCategory.getPosition(mActItem.category);
		mSpinCategory.setSelection(pos);
        
		waitDialog = new WaitDialog(this.activity);
	}

	private void setCategories() throws JSONException {
		String category_json = mPrefs.getCategoryList();
		JSONObject category_obj = new JSONObject(category_json);
		JSONArray cate_key_obj = category_obj.getJSONArray("keys");
		JSONArray cate_val_obj = category_obj.getJSONArray("values");

		for (int i = 0; i < cate_key_obj.length(); i++) {
			if (cate_val_obj.getString(i).equals("All categories")) 
				continue;
			
			mArrayCategory.add(cate_val_obj.getString(i));
			mMapCategory.put(cate_val_obj.getString(i),
					cate_key_obj.getString(i));
		}
	}
	
	private void saveChangesActivityDrem()
	{
		EditActivityDremData param = new EditActivityDremData();

		param.user_id = mPrefs.getUserId();
		param.content = mEdtStatus.getText().toString();
		param.activity_id = mActItem.activity_id;
		param.category = mSpinCategory.getSelectedItem().toString();
		
		WebApiInstance.getInstance().executeAPI(Type.SET_ACTIVITY, param, this);

		waitDialog.show();
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
			case R.id.btnClose:
				this.cancel();
				this.activity.overridePendingTransition(R.anim.in_left_right, R.anim.out_left_right);
				break;
				
			case R.id.btnSaveChanges:
				saveChangesActivityDrem();
				break;
			
			default:
				break;
		}
	}

	private void editActivityResult(Object obj) {
		waitDialog.dismiss();

		if (obj == null) {
			CustomToast.makeCustomToastShort(this.activity, Constants.NETWORK_ERR);
		}

		if (obj != null){
			EditActivityDremResult resultBean = (EditActivityDremResult)obj;
			CustomToast.makeCustomToastShort(this.activity, resultBean.msg);
		}
		
		this.cancel();
		this.activity.overridePendingTransition(R.anim.in_left_right, R.anim.out_left_right);
		
		if (FragmentHome.instance != null)
		{
			FragmentHome.instance.resetOptions();
			FragmentHome.instance.loadMoreDremActivities();
		}
		
		if (FragmentActContent.instance != null)
		{
			FragmentHome.instance.resetOptions();
			FragmentActContent.instance.loadMoreDremActivities();
		}
		
		if (FragmentDrems.instance != null)
		{
			FragmentDrems.instance.resetOptions();
			FragmentDrems.instance.removeAllDrems();
			FragmentDrems.instance.loadMoreDrems();
		}
		
		if (ActivityDremView.instance != null)
			ActivityDremView.instance.finish();
	}
	
	@Override
	public void onPreProcessing(Type type, Object parameter) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onResultProcessing(Type type, Object parameter, Object result) {
		switch (type)
		{
			case SET_ACTIVITY:
				editActivityResult(result);
				break;
				
			default:
				break;
		}
	}
}
