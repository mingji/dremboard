package com.drem.dremboard.ui;

import com.drem.dremboard.R;
import com.drem.dremboard.entity.Beans.SendMessageParam;
import com.drem.dremboard.entity.Beans.SendMessageResult;
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

public class FragmentMessageCompose extends Fragment implements OnClickListener, WebApiCallback
{
	Button mBtnCompose;
	EditText mTxtReceivers, mTxtSubject, mTxtMessage;
	
	AppPreferences mPrefs;
	WaitDialog waitDialog;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View view = inflater.inflate(R.layout.fragment_message_compose, null);

		mPrefs = new AppPreferences(getActivity());
		waitDialog = new WaitDialog(getActivity());
		
		initView (view);

		return view;
	}

	private void initView (View view)
	{
		mTxtReceivers = (EditText) view.findViewById(R.id.txtReceivers);
		mTxtMessage = (EditText) view.findViewById(R.id.txtMessage);
		mTxtSubject = (EditText) view.findViewById(R.id.txtSubject);
		
		mBtnCompose = (Button) view.findViewById(R.id.btnCompose);
		mBtnCompose.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		int id = v.getId();
		
		switch (id)
		{
		case R.id.btnCompose:
			onClickCompose();
			break;
		default:
			break;
		}
	}
	
	private void onClickCompose()
	{
		if (!checkMessage())
			return;
		
		sendMessage();		
	}
	
	private boolean checkMessage()
	{
		String receivers = mTxtReceivers.getText().toString();
		String subject = mTxtSubject.getText().toString();
		String msg = mTxtMessage.getText().toString();
		
		if (receivers == null || receivers.isEmpty()) {
			CustomToast.makeCustomToastShort(getActivity(), "Please input receivers.");
			return false;
		}
		
		if (subject == null || subject.isEmpty()) {
			CustomToast.makeCustomToastShort(getActivity(), "Please input subject.");
			return false;
		}
		
		if (msg == null || msg.isEmpty()) {
			CustomToast.makeCustomToastShort(getActivity(), "Please input message.");
			return false;
		}
		
		return true;
	}
	
	private void afterSendMessage()
	{
		mTxtReceivers.setText("");
		mTxtSubject.setText("");
		mTxtMessage.setText("");	
		
		ActivityMessages parent = (ActivityMessages) getActivity();
		
		parent.updateMessages("inbox");
		parent.updateMessages("sent");
		
		parent.changeTab("sent");

	}	
	
	private void sendMessage()	{		
		
		String receivers = mTxtReceivers.getText().toString().trim();
		String subject = mTxtSubject.getText().toString().trim();
		String msg = mTxtMessage.getText().toString().trim();
		
		SendMessageParam param = new SendMessageParam();

		param.user_id = mPrefs.getUserId();
		param.recipients = receivers;		
		param.subject = subject;
		param.content = msg;
		param.is_notice = false;

		waitDialog.show();

		WebApiInstance.getInstance().executeAPI(Type.SEND_MESSAGE, param, this);
	}

	private void sendMessageResult(Object obj) {

		waitDialog.dismiss();

		if (obj == null) {
			CustomToast.makeCustomToastShort(getActivity(), Constants.NETWORK_ERR);
		}

		if (obj != null){
			SendMessageResult resultBean = (SendMessageResult)obj;

			if (resultBean.status.equals("ok")) {
				afterSendMessage();
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
		case SEND_MESSAGE:
			sendMessageResult(result);			
			break;
		default:
			break;
		}
	}
}
