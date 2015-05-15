package com.drem.dremboard.ui;

import com.drem.dremboard.R;
import com.drem.dremboard.entity.Beans.SetSettingGeneralParam;
import com.drem.dremboard.entity.DremerInfo;
import com.drem.dremboard.entity.GlobalValue;
import com.drem.dremboard.entity.Beans.GetDremersParam;
import com.drem.dremboard.entity.Beans.GetDremersResult;
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
import android.widget.EditText;

public class FragmentSettingGeneral extends Fragment implements OnClickListener, WebApiCallback
{
	Button mBtnSave;

	EditText mTxtCurPwd;
	EditText mTxtMail, mTxtNewPass, mTxtConfPass;
	
	AppPreferences mPrefs;
	WaitDialog waitDialog;	

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View view = inflater.inflate(R.layout.fragment_setting_general, null);

		mPrefs = new AppPreferences(getActivity());
		waitDialog = new WaitDialog(getActivity());
		
		initView (view);

		return view;
	}

	private void initView (View view)
	{
		mBtnSave = (Button) view.findViewById(R.id.btnSave);
		mBtnSave.setOnClickListener(this);
		
		mTxtCurPwd = (EditText) view.findViewById(R.id.txtCurPwd);
		mTxtMail = (EditText) view.findViewById(R.id.txtEmail);
		mTxtNewPass = (EditText) view.findViewById(R.id.txtNewPass);
		mTxtConfPass = (EditText) view.findViewById(R.id.txtConfirmPass);
		
		DremerInfo dremer = GlobalValue.getInstance().getCurrentDremer();
		mTxtMail.setText(dremer.user_email);
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
	
	private void onClickSave ()
	{
		String email, password;
		if (!checkCurrentPassword()) {
			CustomToast.makeCustomToastShort(getActivity(), "Your current password is invalid. No changes were made to your account.");
			return;
		}
		
		if (!checkPassword()) {
			CustomToast.makeCustomToastShort(getActivity(), "Please input a correct password.");
			return;
		}
		
		if (!checkEmail()) {
			CustomToast.makeCustomToastShort(getActivity(), "Please input Email address.");
			return;
		}
		
		email = mTxtMail.getText().toString();
		password = mTxtNewPass.getText().toString();
		
		CustomToast.makeCustomToastShort(getActivity(), "The email address and password are not changed.");
//		setGenearl(email, password);
	}
	
	private boolean checkCurrentPassword()
	{
		String curPwd = mTxtCurPwd.getText().toString();
		String password = GlobalValue.getInstance().getCurrentPassword();		
		
		if (!curPwd.equalsIgnoreCase(password))
			return false;
		
		return true;
	}
	
	private boolean checkPassword()
	{
		String orgPwd = mTxtNewPass.getText().toString();
		String confirmPwd = mTxtConfPass.getText().toString();
		
		if (orgPwd.isEmpty() && confirmPwd.isEmpty())
			return true;
		
		if (!orgPwd.equalsIgnoreCase(confirmPwd))
			return false;
		
		return true;
	}
	
	private boolean checkEmail()
	{
		String emailAddr = mTxtMail.getText().toString();
	
		if (emailAddr.isEmpty())
			return false;
		
		return true;
	}
	
	private void setGenearl(String email, String password)	{
		
		waitDialog.show();

		SetSettingGeneralParam param = new SetSettingGeneralParam();

		param.user_id = mPrefs.getUserId();
		param.disp_user_id = mPrefs.getUserId();
		param.email = email;
		param.password = password;

		WebApiInstance.getInstance().executeAPI(Type.SET_SETTING_GENERAL, param, this);
	}
	
	private void setSettingGeneralResult(Object obj) {
		
		waitDialog.dismiss();

		if (obj == null) {
			CustomToast.makeCustomToastShort(getActivity(), Constants.NETWORK_ERR);
		}

		if (obj != null){
			GetDremersResult resultBean = (GetDremersResult)obj;

			if (resultBean.status.equals("ok")) {
				DremerInfo dremer = GlobalValue.getInstance().getCurrentDremer();
				dremer.user_email = mTxtMail.getText().toString();
				
				GlobalValue.getInstance().setCurrentPassword(mTxtNewPass.getText().toString());
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
		case SET_SETTING_GENERAL:
			setSettingGeneralResult(result);			
			break;
		default:
			break;
		}
	}
}
