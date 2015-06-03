package com.drem.dremboard.ui;

import java.util.ArrayList;

import com.drem.dremboard.R;
import com.drem.dremboard.entity.Beans.GetSingleMessageParam;
import com.drem.dremboard.entity.Beans.GetSingleMessageResult;
import com.drem.dremboard.entity.Beans.ReplyMessageParam;
import com.drem.dremboard.entity.Beans.ReplyMessageResult;
import com.drem.dremboard.entity.SingleViewMessageInfo;
import com.drem.dremboard.entity.SingleViewMessageInfo.MsgThread;
import com.drem.dremboard.utils.AppPreferences;
import com.drem.dremboard.utils.ImageLoader;
import com.drem.dremboard.view.CustomToast;
import com.drem.dremboard.view.WebCircularImgView;
import com.drem.dremboard.view.WaitDialog;
import com.drem.dremboard.webservice.Constants;
import com.drem.dremboard.webservice.WebApiCallback;
import com.drem.dremboard.webservice.WebApiInstance;
import com.drem.dremboard.webservice.WebApiInstance.Type;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

public class ActivitySingleMessage extends Activity implements OnClickListener, WebApiCallback{
	
	AppPreferences mPrefs;

	TextView mTxtTitle, mTxtPartner;
	EditText mTxtReply;
	Button mBtnBack, mBtnDel, mBtnReply;
	
	ScrollView mScroll;
	
	int mMessageId;
	String mMsgSubject;
	
	ArrayList<MsgThread> mArrayThread = null;
	MsgThreadAdapter mAdapterThread;
	ListView mListviewThread;
	
	WaitDialog waitDialog;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_single_message);
        
        mPrefs = new AppPreferences(this);
        waitDialog = new WaitDialog(this);
        
        mMessageId = getIntent().getIntExtra("message_id", -1);
        
        if (mMessageId == -1) {
        	finish();
        	return;
        }
        
        initView();
        
        getSingleMessage();
    }
    
    @Override
    protected void onResume()
    {
    	super.onResume();    	
    }

    @Override
	protected void onPause() {
		super.onPause();		
	}
    
    private void initView()
    {
    	mBtnBack = (Button) findViewById(R.id.btnBack);
		mBtnBack.setOnClickListener(this);
		
		mTxtTitle = (TextView) findViewById(R.id.txtTitle);
		mTxtTitle.setText("View Message");
		
		mScroll = (ScrollView) findViewById(R.id.viewScroll);
		
		mTxtPartner = (TextView) findViewById(R.id.txtPartner);
		mTxtReply = (EditText) findViewById(R.id.txtReply);
		
		mBtnDel = (Button) findViewById(R.id.btnDelete);
		mBtnReply = (Button) findViewById(R.id.btnReply);
		
		mBtnDel.setOnClickListener(this);
		mBtnReply.setOnClickListener(this);
		
		mArrayThread = new ArrayList<MsgThread>();
		mAdapterThread = new MsgThreadAdapter(this, R.layout.item_msg_thread, mArrayThread);

		mListviewThread = (ListView) findViewById(R.id.lstThread);		
		mListviewThread.setAdapter(mAdapterThread);
		mListviewThread.setOnTouchListener(new OnTouchListener() {
		    // Setting on Touch Listener for handling the touch inside ScrollView

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				mScroll.requestDisallowInterceptTouchEvent(true);
				return false;
			}
		});
    }
    
    @Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		int id = v.getId();

		switch(id)
		{
		case R.id.btnBack:
			onBackButton();
			break;
		case R.id.btnDelete:			
			break;
		case R.id.btnReply:
			replyMessage();
			break;
		default:
			break;
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
	
	private void updateMessage (SingleViewMessageInfo info)
	{
		if (info == null)
			return;
		
//		mTxtPartner.setText(info.recipients);
		mMessageId = info.id;
		mMsgSubject = info.subject;
		
		mArrayThread.clear();
		
		for (MsgThread thread : info.thread) {
			mArrayThread.add(thread);
		}
		mAdapterThread.notifyDataSetChanged();
	}
	
	private void getSingleMessage() {
		waitDialog.show();
		
		GetSingleMessageParam param = new GetSingleMessageParam();

		param.user_id = mPrefs.getUserId();
		param.message_id = mMessageId;

		WebApiInstance.getInstance().executeAPI(Type.GET_SINGLE_MESSAGE, param, this);
	}

	private void getSingleMessageResult(Object obj) {

		waitDialog.dismiss();

		if (obj == null) {
			CustomToast.makeCustomToastShort(this, Constants.NETWORK_ERR);
		}

		if (obj != null){
			GetSingleMessageResult resultBean = (GetSingleMessageResult)obj;

			if (resultBean.status.equals("ok")) {
				updateMessage(resultBean.data.messages);
			} else {
				CustomToast.makeCustomToastShort(this, resultBean.msg);
			}
		}
	}
	
	private void replyMessage() {
		String message = mTxtReply.getText().toString().trim();
		
		if (message == null || message.isEmpty()) {
			CustomToast.makeCustomToastShort(this, "Please input reply message.");
			return;
		}
		
		waitDialog.show();
		
		ReplyMessageParam param = new ReplyMessageParam();

		param.user_id = mPrefs.getUserId();
		param.message_id = mMessageId;
		param.content = message;
		param.subject = mMsgSubject;

		WebApiInstance.getInstance().executeAPI(Type.REPLY_MESSAGE, param, this);
	}

	private void replyMessageResult(Object obj) {

		waitDialog.dismiss();

		if (obj == null) {
			CustomToast.makeCustomToastShort(this, Constants.NETWORK_ERR);
		}

		if (obj != null){
			ReplyMessageResult resultBean = (ReplyMessageResult)obj;

			if (resultBean.status.equals("ok")) {
				getSingleMessage();
				mTxtReply.setText("");
			} else {
				CustomToast.makeCustomToastShort(this, resultBean.msg);
			}
		}
	}
	
	public class MsgThreadAdapter extends ArrayAdapter<MsgThread> {
		Activity activity;
		int layoutResourceId;
		ArrayList<MsgThread> item = new ArrayList<MsgThread>();

		// constructor
		public MsgThreadAdapter(Activity activity, int layoutId, ArrayList<MsgThread> items) {
			super(activity, layoutId, items);
			item = items;
			this.activity = activity;
			this.layoutResourceId = layoutId;
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			MsgThreadHolder holder = null;	

			MsgThread msgThread = getItem(position);

			// inflate the view
			if (convertView == null) {
				LayoutInflater inflater = ((Activity) activity).getLayoutInflater();
				convertView = inflater.inflate(R.layout.item_msg_thread, null);
				holder = new MsgThreadHolder();				
				
				holder.imgSender = (WebCircularImgView) convertView.findViewById(R.id.imgSender);
				
				holder.txtSender = (TextView) convertView.findViewById(R.id.txtSender);
				holder.txtTime = (TextView) convertView.findViewById(R.id.txtTimeSince);
				
				holder.txtContent = (TextView) convertView.findViewById(R.id.txtContent);

				convertView.setTag(holder);
			} 
			else
				holder = (MsgThreadHolder) convertView.getTag();
			
			if (msgThread.sender.avatar != null && !msgThread.sender.avatar.isEmpty())
				ImageLoader.getInstance().displayImage(msgThread.sender.avatar, holder.imgSender, 0, 0);
			else
				holder.imgSender.imageView.setImageResource(R.drawable.empty_man);
			
			holder.txtSender.setText(msgThread.sender.name);
			holder.txtTime.setText(msgThread.time_since);
			holder.txtContent.setText(msgThread.content);

			return convertView;
		}

		public class MsgThreadHolder {
			WebCircularImgView imgSender;
			TextView txtSender;
			TextView txtTime;
			
			TextView txtContent;
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
		case GET_SINGLE_MESSAGE:
			getSingleMessageResult(result);			
			break;
		case REPLY_MESSAGE:
			replyMessageResult(result);
			break;
		default:
			break;
		}
	}
}
