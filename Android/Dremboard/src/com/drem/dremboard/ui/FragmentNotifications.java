package com.drem.dremboard.ui;

import java.util.ArrayList;

import com.drem.dremboard.R;
import com.drem.dremboard.entity.Beans.*;
import com.drem.dremboard.entity.NotificationInfo;
import com.drem.dremboard.utils.AppPreferences;
import com.drem.dremboard.view.CustomToast;
import com.drem.dremboard.view.WaitDialog;
import com.drem.dremboard.webservice.Constants;
import com.drem.dremboard.webservice.WebApiCallback;
import com.drem.dremboard.webservice.WebApiInstance;
import com.drem.dremboard.webservice.WebApiInstance.Type;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.AbsListView.OnScrollListener;

public class FragmentNotifications extends Fragment
		implements OnClickListener, WebApiCallback
{
	private AppPreferences mPrefs;

	ProgressBar mProgMore;

	int mLastPageNum = 1;
	int mPerPage = 10;
	int mLastCount = 1;

	boolean mFlagLoading = false;

	ArrayList<NotificationInfo> mArrayNT = null;
	NotificationAdapter mAdapterNT;
	ListView mListviewNT;
	
	String mType;
	
	WaitDialog waitDialog;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View view = inflater.inflate(R.layout.fragment_notifications, null);
		
		mPrefs = new AppPreferences(getActivity());
		waitDialog = new WaitDialog(getActivity());
		
		Bundle bundle = getArguments();
		if (bundle != null) {
			mType = getArguments().getString("type", "read");
		} else {
			mType = "read";
		}
		
		initView (view);
		
		resetOptions();
		
		getNotifications(mLastPageNum, mPerPage);

		return view;
	}

	private void initView (View view)
	{
		mProgMore = (ProgressBar) view.findViewById(R.id.progMore);
		if (mFlagLoading)
			mProgMore.setVisibility(View.VISIBLE);
		else
			mProgMore.setVisibility(View.GONE);
		
		mArrayNT = new ArrayList<NotificationInfo>();
		mAdapterNT = new NotificationAdapter(getActivity(), R.layout.item_notification, mArrayNT);

		mListviewNT = (ListView) view.findViewById(R.id.lstNotification);		
		mListviewNT.setAdapter(mAdapterNT);
		mListviewNT.setOnScrollListener(new OnScrollListener() {

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
					loadMoreNotifications();
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
	
	void reloadNotifications()
	{
		resetOptions();
		mArrayNT.clear();
		getNotifications(mLastPageNum, mPerPage);
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

	}
	
	private void afterSetNotification(String action)
	{
		ActivityNotification parent = (ActivityNotification) getActivity();
		
		parent.updateNotifications(action);
	}
	
	private void updateListView (String action, int index)
	{
		NotificationInfo notification = mArrayNT.get(index);
		
		if (notification == null)
			return;
		
		mArrayNT.remove(index);
		mAdapterNT.notifyDataSetChanged();
		
		if (action.equalsIgnoreCase("read")) {
			afterSetNotification("read");			
		} else if (action.equalsIgnoreCase("unread")) {
			afterSetNotification("unread");
		} else if (action.equalsIgnoreCase("delete")) {
			
		}
	}
	
	private void addNotifications(ArrayList<NotificationInfo> arrayNotification) {

		if (arrayNotification == null)
			return;
		
		for (NotificationInfo notification : arrayNotification) {
			mArrayNT.add(notification);
		}

		mAdapterNT.notifyDataSetChanged();
	}

	private void loadMoreNotifications()
	{
		mFlagLoading = false;
		mProgMore.setVisibility(View.GONE);
		mLastPageNum++;
		getNotifications(mLastPageNum, mPerPage);
	}
	
	private void getNotifications(int pagenum, int count)	{

		if (mFlagLoading)
			return;

		mFlagLoading = true;
		mProgMore.setVisibility(View.VISIBLE);

		_getNotifications(pagenum, count);
	}
	
	private void _getNotifications(int pagenum, int count)	{
		GetNTsParam param = new GetNTsParam();

		param.user_id = mPrefs.getUserId();
		param.page = pagenum;
		param.per_page = count;
		param.type = mType;

		WebApiInstance.getInstance().executeAPI(Type.GET_NT, param, this);
	}

	private void getNotificationsResult(Object obj) {

		mFlagLoading = false;
		mProgMore.setVisibility(View.GONE);

		if (obj == null) {
			CustomToast.makeCustomToastShort(getActivity(), Constants.NETWORK_ERR);
		}

		if (obj != null){
			GetNTsResult resultBean = (GetNTsResult)obj;

			if (resultBean.status.equals("ok")) {
				if (resultBean.data.notification != null) {
					mLastCount = resultBean.data.notification.size();
					addNotifications(resultBean.data.notification);
				} else
					mLastCount = 0;
			} else {
				CustomToast.makeCustomToastShort(getActivity(), resultBean.msg);
			}
		}
	}
	
	private void setNotification(int notificationId, String action, int index) {
		waitDialog.show();
		
		SetNTParam param = new SetNTParam();

		param.user_id = mPrefs.getUserId();
		param.notification_id = notificationId;
		param.action = action;
		
		param.index = index;

		WebApiInstance.getInstance().executeAPI(Type.SET_NT, param, this);
	}

	private void setNotificationResult(Object param, Object obj) {

		waitDialog.dismiss();

		if (obj == null) {
			CustomToast.makeCustomToastShort(getActivity(), Constants.NETWORK_ERR);
		}

		if (obj != null){
			SetNTResult resultBean = (SetNTResult)obj;

			if (resultBean.status.equals("ok")) {
				SetNTParam ntParam = (SetNTParam) param;
				updateListView (ntParam.action, ntParam.index);
			} else {
				CustomToast.makeCustomToastShort(getActivity(), resultBean.msg);
			}
		}
	}
	
	public class NotificationAdapter extends ArrayAdapter<NotificationInfo> implements OnClickListener {
		Activity activity;
		int layoutResourceId;
		ArrayList<NotificationInfo> item = new ArrayList<NotificationInfo>();

		// constructor
		public NotificationAdapter(Activity activity, int layoutId, ArrayList<NotificationInfo> items) {
			super(activity, layoutId, items);
			item = items;
			this.activity = activity;
			this.layoutResourceId = layoutId;
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			NTHolder holder = null;	

			NotificationInfo notification = getItem(position);

			// inflate the view
			if (convertView == null) {
				LayoutInflater inflater = ((Activity) activity).getLayoutInflater();
				convertView = inflater.inflate(R.layout.item_notification, null);
				holder = new NTHolder();
				
				holder.btnRead = (Button) convertView.findViewById(R.id.btnRead);
				holder.btnDelete = (Button) convertView.findViewById(R.id.btnDelete);		
								
				holder.txtDesc = (TextView) convertView.findViewById(R.id.txtDesc);
				holder.txtSince = (TextView) convertView.findViewById(R.id.txtSince);
				
				holder.btnRead.setOnClickListener(this);
				holder.btnDelete.setOnClickListener(this);

				convertView.setTag(holder);
			} 
			else
				holder = (NTHolder) convertView.getTag();

			holder.txtDesc.setText(notification.desc);			
			holder.txtSince.setText(notification.since);			
	
			holder.btnRead.setTag(position);
			if (notification.type.equals("read"))
				holder.btnRead.setText("Unread");
			else
				holder.btnRead.setText("Read");
			
			holder.btnDelete.setTag(position);

			return convertView;
		}

		public class NTHolder {
			TextView txtDesc;
			TextView txtSince;
			
			Button btnRead;
			Button btnDelete;
		}

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			int viewId = v.getId();
			int position = (Integer) v.getTag();
			
			NotificationInfo notification = getItem(position);
			
			if (notification == null)
				return;
			
			switch (viewId) {
			case R.id.btnRead:
				if (notification.type.equals("read")) {
					setNotification(notification.id, "unread", position);
				} else {
					setNotification(notification.id, "read", position);
				}
				break;
			case R.id.btnDelete:
				setNotification(notification.id, "delete", position);
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
		case GET_NT:
			getNotificationsResult(result);			
			break;
		case SET_NT:
			setNotificationResult(parameter, result);			
			break;
		default:
			break;
		}
	}

}
