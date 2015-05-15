package com.drem.dremboard.ui;

import java.util.ArrayList;

import com.drem.dremboard.R;
import com.drem.dremboard.entity.Beans.*;
import com.drem.dremboard.entity.MessageInfo;
import com.drem.dremboard.entity.MessageInfo.FromToInfo;
import com.drem.dremboard.utils.AppPreferences;
import com.drem.dremboard.utils.ImageLoader;
import com.drem.dremboard.view.CustomToast;
import com.drem.dremboard.view.HyIconView;
import com.drem.dremboard.view.WaitDialog;
import com.drem.dremboard.webservice.Constants;
import com.drem.dremboard.webservice.WebApiCallback;
import com.drem.dremboard.webservice.WebApiInstance;
import com.drem.dremboard.webservice.WebApiInstance.Type;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.AbsListView.OnScrollListener;

public class FragmentMessages extends Fragment
implements OnClickListener, WebApiCallback
{
	private AppPreferences mPrefs;

	ProgressBar mProgMore;

	int mLastPageNum = 1;
	int mPerPage = 10;
	int mLastCount = 1;

	boolean mFlagLoading = false;

	ArrayList<MessageInfo> mArrayMsg = null;
	MessageAdapter mAdapterMsg;
	ListView mListviewMsg;

	String mType;

	WaitDialog waitDialog;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View view = inflater.inflate(R.layout.fragment_messages, null);

		mPrefs = new AppPreferences(getActivity());
		waitDialog = new WaitDialog(getActivity());

		Bundle bundle = getArguments();
		if (bundle != null) {
			mType = getArguments().getString("type", "inbox");
		} else {
			mType = "inbox";
		}

		initView (view);

		resetOptions();

		getMessages(mLastPageNum, mPerPage);

		return view;
	}

	private void initView (View view)
	{
		mProgMore = (ProgressBar) view.findViewById(R.id.progMore);
		if (mFlagLoading)
			mProgMore.setVisibility(View.VISIBLE);
		else
			mProgMore.setVisibility(View.GONE);

		mArrayMsg = new ArrayList<MessageInfo>();
		mAdapterMsg = new MessageAdapter(getActivity(), R.layout.item_message, mArrayMsg);

		mListviewMsg = (ListView) view.findViewById(R.id.lstMessage);		
		mListviewMsg.setAdapter(mAdapterMsg);
		mListviewMsg.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				// TODO Auto-generated method stub

				int lastInScreen = firstVisibleItem + visibleItemCount;
				if (totalItemCount != 0 && (lastInScreen == totalItemCount) &&
						!mFlagLoading && mLastCount > 0) {					
					loadMoreMessage();
				}				
			}
		});
	}

	private void resetOptions()
	{
		mLastPageNum = 1;
		mPerPage = 10;
		mLastCount = 1;
	}

	void reloadMessages()
	{
		resetOptions();
		mArrayMsg.clear();
		getMessages(mLastPageNum, mPerPage);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

	}

	private void startSingleMessageView(int messageId) {
		// Run next activity
		Intent intent = new Intent();
		intent.setClass(getActivity(), ActivitySingleMessage.class);
		intent.putExtra("message_id", messageId);
		startActivity(intent);
		getActivity().overridePendingTransition(R.anim.in_right_left, R.anim.out_right_left);
	}

	private void addMessages(ArrayList<MessageInfo> arrayMsg) {

		if (arrayMsg == null)
			return;

		for (MessageInfo message : arrayMsg) {
			mArrayMsg.add(message);
		}

		mAdapterMsg.notifyDataSetChanged();
	}

	private void loadMoreMessage()
	{
		mFlagLoading = false;
		mProgMore.setVisibility(View.GONE);
		mLastPageNum++;
		getMessages(mLastPageNum, mPerPage);
	}

	private void getMessages(int pagenum, int count)	{

		if (mFlagLoading)
			return;

		mFlagLoading = true;
		mProgMore.setVisibility(View.VISIBLE);

		_getMessages(pagenum, count);
	}

	private void _getMessages(int pagenum, int count)	{
		GetMessagesParam param = new GetMessagesParam();

		param.user_id = mPrefs.getUserId();
		param.page = pagenum;
		param.per_page = count;
		param.type = mType;

		WebApiInstance.getInstance().executeAPI(Type.GET_MESSAGES, param, this);
	}

	private void getMessagesResult(Object obj) {

		mFlagLoading = false;
		mProgMore.setVisibility(View.GONE);

		if (obj == null) {
			CustomToast.makeCustomToastShort(getActivity(), Constants.NETWORK_ERR);
		}

		if (obj != null){
			GetMessagesResult resultBean = (GetMessagesResult)obj;

			if (resultBean.status.equals("ok")) {
				if (resultBean.data.messages != null) {
					mLastCount = resultBean.data.messages.size();
					addMessages(resultBean.data.messages);
				} else
					mLastCount = 0;
			} else {
				CustomToast.makeCustomToastShort(getActivity(), resultBean.msg);
			}
		}
	}

	public class MessageAdapter extends ArrayAdapter<MessageInfo> implements OnClickListener {
		Activity activity;
		int layoutResourceId;
		ArrayList<MessageInfo> item = new ArrayList<MessageInfo>();

		// constructor
		public MessageAdapter(Activity activity, int layoutId, ArrayList<MessageInfo> items) {
			super(activity, layoutId, items);
			item = items;
			this.activity = activity;
			this.layoutResourceId = layoutId;
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			MessageHolder holder = null;	

			MessageInfo message = getItem(position);

			// inflate the view
			if (convertView == null) {
				LayoutInflater inflater = ((Activity) activity).getLayoutInflater();
				convertView = inflater.inflate(R.layout.item_message, null);
				holder = new MessageHolder();

				holder.imgSender = (HyIconView) convertView.findViewById(R.id.imgSender);

				holder.txtFromTo = (TextView) convertView.findViewById(R.id.txtFromTo);
				holder.txtPartner = (TextView) convertView.findViewById(R.id.txtPartner);
				holder.txtDate = (TextView) convertView.findViewById(R.id.txtDate);

				holder.txtTitle = (TextView) convertView.findViewById(R.id.txtSubject);
				holder.txtExcerpt = (TextView) convertView.findViewById(R.id.txtExcerpt);

				holder.imgSender.setOnClickListener(this);
				holder.txtPartner.setOnClickListener(this);
				holder.txtTitle.setOnClickListener(this);
				holder.txtExcerpt.setOnClickListener(this);

				convertView.setTag(holder);
			} 
			else
				holder = (MessageHolder) convertView.getTag();

			holder.imgSender.setTag(position);
			holder.txtPartner.setTag(position);
			holder.txtTitle.setTag(position);
			holder.txtExcerpt.setTag(position);

			holder.txtDate.setText(message.post_date);
			holder.txtTitle.setText(message.title);
			holder.txtExcerpt.setText(message.excerpt);

			if (message.type.equalsIgnoreCase("inbox")) {
				holder.txtFromTo.setText("From: ");

				if (message.from.avatar != null && !message.from.avatar.isEmpty())
					ImageLoader.getInstance().displayImage(message.from.avatar, holder.imgSender, 0, 0);
				else
					holder.imgSender.imageView.setImageResource(R.drawable.empty_man);
				holder.txtPartner.setText(message.from.name);
			}
			else {
				holder.txtFromTo.setText("To: ");
				String partner = "";
				for (FromToInfo user : message.to) {
					partner += user.name + ", ";
				}
				holder.txtPartner.setText(partner);
				String myAvatar = mPrefs.getUserAvatar();
				if (myAvatar != null && !myAvatar.isEmpty())
					ImageLoader.getInstance().displayImage(myAvatar, holder.imgSender, 0, 0);
				else
					holder.imgSender.imageView.setImageResource(R.drawable.empty_man);
			}

			return convertView;
		}

		public class MessageHolder {
			HyIconView imgSender;

			TextView txtFromTo;
			TextView txtPartner;
			TextView txtDate;

			TextView txtTitle;
			TextView txtExcerpt;
		}

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			int viewId = v.getId();
			int position = (Integer) v.getTag();

			MessageInfo message = getItem(position);

			if (message == null)
				return;

			switch (viewId) {
			case R.id.imgSender:
				break;
			case R.id.txtSubject:
				startSingleMessageView(message.id);
				break;
			default:
				break;
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
		case GET_MESSAGES:
			getMessagesResult(result);			
			break;
			//		case SET_NT:
			//			setNotificationResult(parameter, result);			
			//			break;
		default:
			break;
		}
	}

}
