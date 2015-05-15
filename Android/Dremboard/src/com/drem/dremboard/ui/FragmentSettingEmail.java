package com.drem.dremboard.ui;

import java.util.ArrayList;
import java.util.HashMap;

import com.drem.dremboard.R;
import com.drem.dremboard.entity.Beans.GetSettingNoteParam;
import com.drem.dremboard.entity.Beans.GetSettingNoteResult;
import com.drem.dremboard.entity.Beans.SetSettingNoteParam;
import com.drem.dremboard.entity.Beans.SetSettingNoteResult;
import com.drem.dremboard.entity.EmailNoteConf;
import com.drem.dremboard.utils.AppPreferences;
import com.drem.dremboard.view.CustomToast;
import com.drem.dremboard.view.WaitDialog;
import com.drem.dremboard.webservice.Constants;
import com.drem.dremboard.webservice.WebApiCallback;
import com.drem.dremboard.webservice.WebApiInstance;
import com.drem.dremboard.webservice.WebApiInstance.Type;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;

public class FragmentSettingEmail extends Fragment implements OnClickListener, WebApiCallback
{
	AppPreferences mPrefs;
	WaitDialog waitDialog;

	Button mBtnSave;
	
	CheckBox mChkActMention, mChkActReply, mChkMsgSend, mChkFriendReq, mChkFriendAccept;
	CheckBox mChkGroupInvite, mChkGroupUpdate, mChkGroupPromote, mChkGroupReq;
	CheckBox mChkFamilyReq, mChkFamilyAccept, mChkFollow;
	
	HashMap<String, CheckBox> mapIdChk;
	String[] arrayId = {
			
			"notification_activity_new_mention", "notification_activity_new_reply",
			
			"notification_messages_new_message", 
			
			"notification_friends_friendship_request", "notification_friends_friendship_accepted",
			
			"notification_groups_invite", "notification_groups_group_updated",
			"notification_groups_admin_promotion", "notification_groups_membership_request",
			
			"notification_familys_familyship_request", "notification_familys_familyship_accepted",
			
			"notification_starts_following"
	};
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View view = inflater.inflate(R.layout.fragment_setting_email, null);

		mPrefs = new AppPreferences(getActivity());
		waitDialog = new WaitDialog(getActivity());
		
		mapIdChk = new HashMap<String, CheckBox>();
		
		initView (view);

		getEmailNote();
		
		return view;
	}

	private void initView (View view)
	{
		mBtnSave = (Button) view.findViewById(R.id.btnSave);
		mBtnSave.setOnClickListener(this);
		
		mChkActMention = (CheckBox) view.findViewById(R.id.chkActivityMention);
		mChkActReply = (CheckBox) view.findViewById(R.id.chkActivityReply);		
		mapIdChk.put(arrayId[0], mChkActMention);
		mapIdChk.put(arrayId[1], mChkActReply);
		
		mChkMsgSend = (CheckBox) view.findViewById(R.id.chkMessageSend);		
		mapIdChk.put(arrayId[2], mChkMsgSend);
		
		mChkFriendReq = (CheckBox) view.findViewById(R.id.chkFriendRequest);
		mChkFriendAccept = (CheckBox) view.findViewById(R.id.chkFriendAccept);
		mapIdChk.put(arrayId[3], mChkFriendReq);
		mapIdChk.put(arrayId[4], mChkFriendAccept);
		
		mChkGroupInvite = (CheckBox) view.findViewById(R.id.chkGroupInvite);
		mChkGroupUpdate = (CheckBox) view.findViewById(R.id.chkGroupUpdate);
		mChkGroupPromote = (CheckBox) view.findViewById(R.id.chkGroupPromote);
		mChkGroupReq = (CheckBox) view.findViewById(R.id.chkGroupRequest);
		mapIdChk.put(arrayId[5], mChkGroupInvite);
		mapIdChk.put(arrayId[6], mChkGroupUpdate);
		mapIdChk.put(arrayId[7], mChkGroupPromote);
		mapIdChk.put(arrayId[8], mChkGroupReq);
		
		mChkFamilyReq = (CheckBox) view.findViewById(R.id.chkFamilyRequest);
		mChkFamilyAccept = (CheckBox) view.findViewById(R.id.chkFamilyAccept);
		mapIdChk.put(arrayId[9], mChkFamilyReq);
		mapIdChk.put(arrayId[10], mChkFamilyAccept);
		
		mChkFollow = (CheckBox) view.findViewById(R.id.chkFollow);
		mapIdChk.put(arrayId[11], mChkFollow);		
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
		String confJson = makeNoteConfString();
		setEmailNote(confJson);
	}	
	
	private void updateNoteConf(ArrayList<EmailNoteConf> notifications)
	{
		for (EmailNoteConf conf : notifications)
		{
			CheckBox chk = mapIdChk.get(conf.id);
			if (conf.value.equals("yes"))
				chk.setChecked(true);
			else
				chk.setChecked(false);
		}		
	}
	
	private String makeNoteConfString()
	{
		ArrayList<EmailNoteConf> arrayConf = new ArrayList<EmailNoteConf> ();
		
		for (String id : arrayId)
		{
			EmailNoteConf conf = new EmailNoteConf();
			CheckBox chk = mapIdChk.get(id);
			if (chk == null)
				continue;
			
			conf.id = id;
			if (chk.isChecked())
				conf.value = "yes";
			else
				conf.value = "no";
			
			arrayConf.add(conf);
		}
		
		Gson gson = new Gson();
		String strConf = gson.toJson(arrayConf, new TypeToken<ArrayList<EmailNoteConf>>(){}.getType());
		
		return strConf;
	}

	
	private void getEmailNote()	{
		
		waitDialog.show();

		GetSettingNoteParam param = new GetSettingNoteParam();

		param.user_id = mPrefs.getUserId();
		param.disp_user_id = mPrefs.getUserId();

		WebApiInstance.getInstance().executeAPI(Type.GET_SETTING_NOTE, param, this);
	}
	
	private void getEmailNoteResult(Object obj) {
		
		waitDialog.dismiss();

		if (obj == null) {
			CustomToast.makeCustomToastShort(getActivity(), Constants.NETWORK_ERR);
		}

		if (obj != null){
			GetSettingNoteResult resultBean = (GetSettingNoteResult)obj;

			if (resultBean.status.equals("ok")) {
				updateNoteConf(resultBean.data.notifications);
			} else {
				CustomToast.makeCustomToastShort(getActivity(), resultBean.msg);
			}
		}
	}
	
	
	private void setEmailNote(String noteJson)	{
		
		waitDialog.show();

		SetSettingNoteParam param = new SetSettingNoteParam();

		param.user_id = mPrefs.getUserId();
		param.disp_user_id = mPrefs.getUserId();
		param.notifications = noteJson;

		WebApiInstance.getInstance().executeAPI(Type.SET_SETTING_NOTE, param, this);
	}
	
	private void setEmailNoteResult(Object obj) {
		
		waitDialog.dismiss();

		if (obj == null) {
			CustomToast.makeCustomToastShort(getActivity(), Constants.NETWORK_ERR);
		}

		if (obj != null){
			SetSettingNoteResult resultBean = (SetSettingNoteResult)obj;

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
		case GET_SETTING_NOTE:
			getEmailNoteResult(result);			
			break;
		case SET_SETTING_NOTE:
			setEmailNoteResult(result);			
			break;
		default:
			break;
		}
	}
}
