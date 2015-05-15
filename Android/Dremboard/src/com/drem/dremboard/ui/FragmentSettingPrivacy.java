package com.drem.dremboard.ui;


import com.drem.dremboard.R;
import com.drem.dremboard.entity.Beans.GetSettingNoteParam;
import com.drem.dremboard.entity.Beans.GetSettingPrivacyParam;
import com.drem.dremboard.entity.Beans.GetSettingPrivacyResult;
import com.drem.dremboard.entity.Beans.SetSettingPrivacyParam;
import com.drem.dremboard.entity.Beans.SetSettingPrivacyResult;
import com.drem.dremboard.utils.AppPreferences;
import com.drem.dremboard.view.CustomToast;
import com.drem.dremboard.view.WaitDialog;
import com.drem.dremboard.webservice.Constants;
import com.drem.dremboard.webservice.WebApiCallback;
import com.drem.dremboard.webservice.WebApiInstance;
import com.drem.dremboard.webservice.WebApiInstance.Type;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;

public class FragmentSettingPrivacy extends Fragment implements OnClickListener, WebApiCallback
{
	Button mBtnSave;
	
	AppPreferences mPrefs;
	WaitDialog waitDialog;
	
	RadioButton mRbPrivate, mRbFamily, mRbFriend, mRbPublic;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View view = inflater.inflate(R.layout.fragment_setting_privacy, null);

		mPrefs = new AppPreferences(getActivity());
		waitDialog = new WaitDialog(getActivity());
		
		initView (view);

		getDefaultPrivacy();
		return view;
	}

	private void initView (View view)
	{
		mBtnSave = (Button) view.findViewById(R.id.btnSave);
		mBtnSave.setOnClickListener(this);
		
		mRbPrivate = (RadioButton) view.findViewById(R.id.radioPrivate);
		mRbFamily = (RadioButton) view.findViewById(R.id.radioFamily);
		mRbFriend = (RadioButton) view.findViewById(R.id.radioFriend);
		mRbPublic = (RadioButton) view.findViewById(R.id.radioPublic);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		int id = v.getId();
		
		switch (id)
		{
		case R.id.btnSave:
			onClickSave();
			break;
		default:
			break;
		}
	}
	
	private void onClickSave()
	{
		String privacy = getPrivacy();
		
		setDefaultPrivacy(privacy);
	}
	
	private String getPrivacy()
	{
		String ret = "";
		
		if (mRbPrivate.isChecked())
			ret = "60";
		else if (mRbFamily.isChecked())
			ret = "50";
		else if (mRbFriend.isChecked())
			ret = "40";
		else
			ret = "";
		
		return ret;
	}
	
	private void updatePrivacyConf(String privacy)
	{
		if (privacy.equals("60"))
			mRbPrivate.setChecked(true);
		else if (privacy.equals("50"))
			mRbFamily.setChecked(true);
		else if (privacy.equals("50"))
			mRbFriend.setChecked(true);
		else
			mRbPublic.setChecked(true);
	}
	
	private void getDefaultPrivacy() {
		
		waitDialog.show();

		GetSettingPrivacyParam param = new GetSettingPrivacyParam();

		param.user_id = mPrefs.getUserId();
		param.disp_user_id = mPrefs.getUserId();

		WebApiInstance.getInstance().executeAPI(Type.GET_SETTING_PRIVACY, param, this);
	}
	
	private void getDefaultPrivacyResult(Object obj) {
		
		waitDialog.dismiss();

		if (obj == null) {
			CustomToast.makeCustomToastShort(getActivity(), Constants.NETWORK_ERR);
		}

		if (obj != null){
			GetSettingPrivacyResult resultBean = (GetSettingPrivacyResult)obj;

			if (resultBean.status.equals("ok")) {
				updatePrivacyConf(resultBean.data.privacy);
			} else {
				CustomToast.makeCustomToastShort(getActivity(), resultBean.msg);
			}
		}
	}
	
	private void setDefaultPrivacy(String privacy) {
		
		waitDialog.show();

		SetSettingPrivacyParam param = new SetSettingPrivacyParam();

		param.user_id = mPrefs.getUserId();
		param.disp_user_id = mPrefs.getUserId();
		param.privacy = privacy;		

		WebApiInstance.getInstance().executeAPI(Type.SET_SETTING_PRIVACY, param, this);
	}
	
	private void setDefaultPrivacyResult(Object obj) {
		
		waitDialog.dismiss();

		if (obj == null) {
			CustomToast.makeCustomToastShort(getActivity(), Constants.NETWORK_ERR);
		}

		if (obj != null){
			SetSettingPrivacyResult resultBean = (SetSettingPrivacyResult)obj;

			if (resultBean.status.equals("ok")) {
			} else {
				CustomToast.makeCustomToastShort(getActivity(), resultBean.msg);
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
		case GET_SETTING_PRIVACY:
			getDefaultPrivacyResult(result);			
			break;
			
		case SET_SETTING_PRIVACY:
			setDefaultPrivacyResult(result);			
			break;
		default:
			break;
		}
	}
}
