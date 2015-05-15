package com.drem.dremboard.ui;


import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.URLSpan;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.drem.dremboard.R;
import com.drem.dremboard.utils.RestApi;
import com.drem.dremboard.webservice.Constants;

public class DialogShare extends Dialog implements View.OnClickListener {
	Activity activity;
	int activity_id;
	Button btnShareDremboard, btnShareFacebook, btnShareTwitter,
			btnShareGoogle, btnShareEmail;

	public DialogShare(Context context, Activity activity, int activity_id) {
		super(context);

		this.activity = activity;
		this.activity_id = activity_id;

	}

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.item_share_modal);

		setTitle("Share Drem");
		setCancelable(true);

		btnShareDremboard = (Button) findViewById(R.id.btnShareDremboard);
		btnShareFacebook = (Button) findViewById(R.id.btnShareFacebook);
		btnShareTwitter = (Button) findViewById(R.id.btnShareTwitter);
		btnShareGoogle = (Button) findViewById(R.id.btnShareGoogle);
		btnShareEmail = (Button) findViewById(R.id.btnShareEmail);

		btnShareDremboard.setOnClickListener(this);
		btnShareFacebook.setOnClickListener(this);
		btnShareTwitter.setOnClickListener(this);
		btnShareGoogle.setOnClickListener(this);
		btnShareEmail.setOnClickListener(this);
	}
	
	private void onClickShareDremboard()
	{
		Dialog dialog = new ShareDremboardDialog(activity, activity,
				activity_id);
		dialog.show();
		dismiss();
	}
	
	private void onClickShareFacebook()
	{
		String url = "http://www.facebook.com/sharer/sharer.php?u="
				+ Constants.HTTP_HOME + "activity/" + activity_id + "/";
		Intent i = new Intent(Intent.ACTION_VIEW);
		i.setData(Uri.parse(url));
		activity.startActivity(i);
		dismiss();
	}
	
	private void onClickShareTwitter()
	{
		String url = "http://twitter.com/share?url="
				+ Constants.HTTP_HOME + "activity/" + activity_id + "/";
		Intent i = new Intent(Intent.ACTION_VIEW);
		i.setData(Uri.parse(url));
		activity.startActivity(i);
		dismiss();
	}
	
	private void onClickShareGoogle()
	{
		String url = "https://plus.google.com/share?url="
				+ Constants.HTTP_HOME + "activity/" + activity_id + "/";
		Intent i = new Intent(Intent.ACTION_VIEW);
		i.setData(Uri.parse(url));
		activity.startActivity(i);
		dismiss();
	}
	
	private void onClickShareEmail()
	{
		String url = Constants.HTTP_HOME + "activity/" + activity_id
				+ "/";

		SpannableStringBuilder builder = new SpannableStringBuilder();
		int start = builder.length();
		builder.append(url);
		int end = builder.length();

		builder.setSpan(new URLSpan(url), start, end,
				Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

		Intent i = new Intent(Intent.ACTION_SEND);
		i.setType("message/rfc822");
		i.putExtra(Intent.EXTRA_EMAIL, new String[] { "" });
		i.putExtra(Intent.EXTRA_SUBJECT, "Share from Dremboard");
		i.putExtra(Intent.EXTRA_TEXT, builder);
		activity.startActivity(Intent.createChooser(i,
				"Select application"));
		dismiss();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		int viewId = v.getId();
		
		switch (viewId)
		{
		case R.id.btnShareDremboard:
			onClickShareDremboard();
			break;
		case R.id.btnShareFacebook:
			onClickShareFacebook();
			break;
		case R.id.btnShareTwitter:
			onClickShareTwitter();
			break;
		case R.id.btnShareGoogle:
			onClickShareGoogle();
			break;
		case R.id.btnShareEmail:
			onClickShareEmail();
			break;
		default:
			break;
		}
	}
}

class ShareDremboardDialog extends Dialog {
	Activity activity;
	int activity_id;
	Spinner spin_share_option;
	ArrayAdapter<CharSequence> objAdapter;
	LinearLayout lay_share_target;
	Button btn_share, btn_cancel;
	EditText txt_share_target, txt_share_desc;

	public ShareDremboardDialog(Context context, Activity activity,
			int activity_id) {
		super(context);

		this.activity = activity;
		this.activity_id = activity_id;
	}

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.item_share_dremboard_modal);

		setCancelable(true);
		spin_share_option = (Spinner) findViewById(R.id.spin_share_option);
		btn_share = (Button) findViewById(R.id.btn_share);
		btn_cancel = (Button) findViewById(R.id.btn_cancel);
		txt_share_target = (EditText) findViewById(R.id.txt_share_target);
		lay_share_target = (LinearLayout)findViewById(R.id.lay_share_target);
		txt_share_desc = (EditText) findViewById(R.id.txt_desc);
		
		objAdapter = ArrayAdapter.createFromResource(activity,
				R.array.ShareOption, android.R.layout.simple_spinner_item);
		objAdapter
				.setDropDownViewResource(R.layout.custom_spinner_dropdown_item);

		spin_share_option.setAdapter(objAdapter);

		btn_cancel.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dismiss();
			}
		});
		btn_share.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String desc = txt_share_desc.getText().toString();
				String share_user = txt_share_target.getText().toString();
				String share_mode = spin_share_option.getSelectedItem()
						.toString();
				share_drem_task(activity_id, desc, share_user, share_mode);
				dismiss();
			}
		});
		spin_share_option
				.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

					@Override
					public void onItemSelected(AdapterView<?> parent,
							View view, int position, long id) {
						// TODO Auto-generated method stub
						switch (position) {
						case 0: // On your own Timeline
							txt_share_target.setVisibility(View.GONE);
							break;
						case 1: // Share with friend // friend name
							txt_share_target.setHint("friend name");
							txt_share_target.setVisibility(View.VISIBLE);
							break;
						case 2: // In a group // group name
							txt_share_target.setHint("group name");
							txt_share_target.setVisibility(View.VISIBLE);
							break;
						case 3: // In a private message // user name
							txt_share_target.setHint("user name");
							txt_share_target.setVisibility(View.VISIBLE);
							break;
						}
					}

					@Override
					public void onNothingSelected(AdapterView<?> parent) {
						// TODO Auto-generated method stub

					}
				});
	}

	private void share_drem_task(final int activity_id, final String desc,
			final String share_user, final String share_mode) {
		String action = RestApi.ACT_SHARE_DREM;
		HashMap<String, String> params = new HashMap<String, String>();
		params.put(RestApi.PARAM_ACTIVITY_ID, String.valueOf(activity_id));
		params.put(RestApi.PARAM_DESCRIPTION, desc);
		params.put(RestApi.PARAM_SHARE_USER, share_user);
		params.put(RestApi.PARAM_SHARE_MODE, share_mode);
		new RestApi(activity, action, params) {
			public void process_result(JSONObject data) throws JSONException {

			}
		}.execute();
	}

}