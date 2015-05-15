package com.drem.dremboard.ui;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.drem.dremboard.R;
import com.drem.dremboard.utils.RestApi;

public abstract class DialogFlagDrem extends Dialog {
	Activity activity;
	int activity_id;
	Button btnCancel, btnFlag;
	RadioGroup radio_flag_group;
	TextView text_complaint;
	ArrayList<String> flag_value;

	public DialogFlagDrem(Context context, int activity_id) {
		super(context);
		this.activity = (Activity) context;
		this.activity_id = activity_id;
	}
	
	public abstract void drem_flaged(int activity_id, String flag_slug);

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.item_flag_drem_modal);

		setCancelable(true);
		btnCancel = (Button) findViewById(R.id.btn_cancel);
		btnFlag = (Button) findViewById(R.id.btn_flag_register);
		radio_flag_group = (RadioGroup) findViewById(R.id.radio_report_group);
		text_complaint = (TextView) findViewById(R.id.text_complaint);
		text_complaint.setMovementMethod(LinkMovementMethod.getInstance());

		flag_value = new ArrayList<String>();

		flag_value.add("annoy");
		flag_value.add("nudity");
		flag_value.add("graphic");
		flag_value.add("attack");
		flag_value.add("improper");
		flag_value.add("spam");

		btnCancel.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				dismiss();
			}
		});
		btnFlag.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				int radioButtonId = radio_flag_group.getCheckedRadioButtonId();
				View radioButton = radio_flag_group.findViewById(radioButtonId);
				int idx = radio_flag_group.indexOfChild(radioButton);
				String flag_slug = flag_value.get(idx);
				flag_drem_task(activity_id, flag_slug);
				dismiss();
			}
		});
	}

	public void flag_drem_task(final int activity_id, final String flag_slug) {
		String action = RestApi.ACT_FLAG_DREM;
		HashMap<String, String> params = new HashMap<String, String>();
		params.put(RestApi.PARAM_ACTIVITY_ID, String.valueOf(activity_id));
		params.put(RestApi.PARAM_FLAG_SLUG, flag_slug);

		new RestApi(activity, action, params) {
			public void process_result(JSONObject data) throws JSONException {
				drem_flaged(activity_id, flag_slug);
			}
		}.execute();
	}
}
