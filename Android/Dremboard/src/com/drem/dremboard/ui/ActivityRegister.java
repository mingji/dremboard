package com.drem.dremboard.ui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.drem.dremboard.R;
import com.drem.dremboard.utils.RestApi;

public class ActivityRegister extends Activity implements OnClickListener {

	Button mBtnSignup, mBtnBack;

	Spinner mSpinContentView, mSpinContentField;
	ArrayList<String> mArrayContentView, mArrayViewPermission;

	Spinner mSpinGender, mSpinGenderField;
	ArrayList<String> mArrayGender;

	Spinner mSpinLang, mSpinLangField;
	ArrayList<String> mArrayLang;

	Spinner mSpinCountry, mSpinCountryField;
	ArrayList<String> mArrayCountry;

	Spinner mSpinFacebookField, mSpinGoogleField, mSpinTwitterField;

	EditText mTxtUsername, mTxtEmail, mTxtOrgPwd, mTxtConfirmPwd, mTxtName;
	EditText mTxtFacebook, mTxtGoogle, mTxtTwitter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_register);

		initView();
	}

	private void initView() {
		setSpinners();

		mBtnBack = (Button) findViewById(R.id.btnBack);
		mBtnBack.setOnClickListener(this);

		mBtnSignup = (Button) findViewById(R.id.btnSignup);
		mBtnSignup.setOnClickListener(this);

		mTxtUsername = (EditText) findViewById(R.id.txtUsername);
		mTxtEmail = (EditText) findViewById(R.id.txtEmail);
		mTxtOrgPwd = (EditText) findViewById(R.id.txtOrgPwd);
		mTxtConfirmPwd = (EditText) findViewById(R.id.txtConfirmPwd);
		mTxtName = (EditText) findViewById(R.id.txtName);
		mTxtFacebook = (EditText) findViewById(R.id.txtFacebook);
		mTxtGoogle = (EditText) findViewById(R.id.txtGoogle);
		mTxtTwitter = (EditText) findViewById(R.id.txtTwitter);

	}

	private void setSpinners() {
		mArrayViewPermission = new ArrayList<String>();
		mArrayViewPermission.add("Everyone");
		mArrayViewPermission.add("Only Me");
		mArrayViewPermission.add("All Members");
		mArrayViewPermission.add("My Friends");
		mArrayViewPermission.add("Nobody");

		// Who can view my content
		mSpinContentView = (Spinner) findViewById(R.id.spinContentViewPermission);
		mSpinContentView.setPrompt("Who can view my content");

		mArrayContentView = new ArrayList<String>();
		mArrayContentView.add("----");
		mArrayContentView.add("public");
		mArrayContentView.add("family and friends");
		mArrayContentView.add("only myself");

		ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, mArrayContentView);
		adapter1.setDropDownViewResource(R.layout.custom_spinner_dropdown_item);
		mSpinContentView.setAdapter(adapter1);

		// Gender
		mSpinGender = (Spinner) findViewById(R.id.spinGender);
		mSpinGender.setPrompt("Select Gender");

		mArrayGender = new ArrayList<String>();
		mArrayGender.add("----");
		mArrayGender.add("male");
		mArrayGender.add("female");

		ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, mArrayContentView);
		adapter2.setDropDownViewResource(R.layout.custom_spinner_dropdown_item);
		mSpinGender.setAdapter(adapter2);

		Locale[] locale = Locale.getAvailableLocales();

		// Language
		mSpinLang = (Spinner) findViewById(R.id.spinLang);
		mSpinLang.setPrompt("Select Language");

		mArrayLang = new ArrayList<String>();
		mArrayLang.add("----");
		String language;
		for (Locale loc : locale) {
			language = loc.getDisplayLanguage();
			if (language.length() > 0 && !mArrayLang.contains(language)) {
				mArrayLang.add(language);
			}
		}
		Collections.sort(mArrayLang, String.CASE_INSENSITIVE_ORDER);

		ArrayAdapter<String> adapterLang = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, mArrayLang);
		adapterLang
				.setDropDownViewResource(R.layout.custom_spinner_dropdown_item);
		mSpinLang.setAdapter(adapterLang);

		// Country
		mSpinCountry = (Spinner) findViewById(R.id.spinCountry);
		mSpinCountry.setPrompt("Select Country");

		mArrayCountry = new ArrayList<String>();
		mArrayCountry.add("----");
		String country;
		for (Locale loc : locale) {
			country = loc.getDisplayCountry();
			if (country.length() > 0 && !mArrayCountry.contains(country)) {
				mArrayCountry.add(country);
			}
		}
		Collections.sort(mArrayCountry, String.CASE_INSENSITIVE_ORDER);

		ArrayAdapter<String> adapterCountry = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, mArrayCountry);
		adapterCountry
				.setDropDownViewResource(R.layout.custom_spinner_dropdown_item);
		mSpinCountry.setAdapter(adapterCountry);

		// Who can view my content
		mSpinContentField = (Spinner) findViewById(R.id.spinContentFieldPermission);
		mSpinContentField.setPrompt("Who can see this field?");

		ArrayAdapter<String> adapterContentField = new ArrayAdapter<String>(
				this, android.R.layout.simple_spinner_item,
				mArrayViewPermission);
		adapterContentField
				.setDropDownViewResource(R.layout.custom_spinner_dropdown_item);
		mSpinContentField.setAdapter(adapterContentField);

		// Gender Permission
		mSpinGenderField = (Spinner) findViewById(R.id.spinGenderFieldPermission);
		mSpinGenderField.setPrompt("Who can see this field?");

		ArrayAdapter<String> adapterGenderField = new ArrayAdapter<String>(
				this, android.R.layout.simple_spinner_item,
				mArrayViewPermission);
		adapterGenderField
				.setDropDownViewResource(R.layout.custom_spinner_dropdown_item);
		mSpinGenderField.setAdapter(adapterGenderField);

		// Language Permission
		mSpinLangField = (Spinner) findViewById(R.id.spinLangFieldPermission);
		mSpinLangField.setPrompt("Who can see this field?");

		ArrayAdapter<String> adapterLangField = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, mArrayViewPermission);
		adapterLangField
				.setDropDownViewResource(R.layout.custom_spinner_dropdown_item);
		mSpinLangField.setAdapter(adapterLangField);

		// Country Permission
		mSpinCountryField = (Spinner) findViewById(R.id.spinCountryFieldPermission);
		mSpinCountryField.setPrompt("Who can see this field?");

		ArrayAdapter<String> adapterCountryField = new ArrayAdapter<String>(
				this, android.R.layout.simple_spinner_item,
				mArrayViewPermission);
		adapterCountryField
				.setDropDownViewResource(R.layout.custom_spinner_dropdown_item);
		mSpinCountryField.setAdapter(adapterCountryField);

		// Facebook Permission
		mSpinFacebookField = (Spinner) findViewById(R.id.spinFacebookFieldPermission);
		mSpinFacebookField.setPrompt("Who can see this field?");

		ArrayAdapter<String> adapterFacebookField = new ArrayAdapter<String>(
				this, android.R.layout.simple_spinner_item,
				mArrayViewPermission);
		adapterFacebookField
				.setDropDownViewResource(R.layout.custom_spinner_dropdown_item);
		mSpinFacebookField.setAdapter(adapterFacebookField);

		// Google Permission
		mSpinGoogleField = (Spinner) findViewById(R.id.spinGoogleFieldPermission);
		mSpinGoogleField.setPrompt("Who can see this field?");

		ArrayAdapter<String> adapterGoogleField = new ArrayAdapter<String>(
				this, android.R.layout.simple_spinner_item,
				mArrayViewPermission);
		adapterGoogleField
				.setDropDownViewResource(R.layout.custom_spinner_dropdown_item);
		mSpinGoogleField.setAdapter(adapterGoogleField);

		// Twitter Permission
		mSpinTwitterField = (Spinner) findViewById(R.id.spinTwitterFieldPermission);
		mSpinTwitterField.setPrompt("Who can see this field?");

		ArrayAdapter<String> adapterTwitterField = new ArrayAdapter<String>(
				this, android.R.layout.simple_spinner_item,
				mArrayViewPermission);
		adapterTwitterField
				.setDropDownViewResource(R.layout.custom_spinner_dropdown_item);
		mSpinTwitterField.setAdapter(adapterTwitterField);

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		int id = v.getId();

		switch (id) {
		case R.id.btnBack:
			onBackButton();
			break;
		case R.id.btnSignup:
			RegisterTask();
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

	private void onBackButton() {
		finish();
		overridePendingTransition(R.anim.in_left_right, R.anim.out_left_right);
	}

	private void RegisterTask() {
		String action = RestApi.ACT_USER_REGISTER;
		String signup_profile_field_ids = "1,2,12,15,94,96,97,98";

		String signup_username = mTxtUsername.getText().toString();
		String signup_email = mTxtEmail.getText().toString();
		String signup_password = mTxtOrgPwd.getText().toString();
		String signup_password_confirm = mTxtConfirmPwd.getText().toString();

		String field_1 = mTxtName.getText().toString();
		String field_96 = mTxtFacebook.getText().toString();
		String field_97 = mTxtGoogle.getText().toString();
		String field_98 = mTxtTwitter.getText().toString();
		String field_2 = mSpinContentView.getSelectedItem().toString();
		String field_2_visibility = mSpinContentField.getSelectedItem()
				.toString();
		String field_12 = mSpinGender.getSelectedItem().toString();
		String field_12_visibility = mSpinGenderField.getSelectedItem()
				.toString();
		String field_15 = mSpinLang.getSelectedItem().toString();
		String field_15_visibility = mSpinLangField.getSelectedItem()
				.toString();
		String field_94 = mSpinCountry.getSelectedItem().toString();
		String field_94_visibility = mSpinCountryField.getSelectedItem()
				.toString();

		String field_96_visibility = mSpinFacebookField.getSelectedItem()
				.toString();
		String field_97_visibility = mSpinGoogleField.getSelectedItem()
				.toString();
		String field_98_visibility = mSpinTwitterField.getSelectedItem()
				.toString();

		HashMap<String, String> params = new HashMap<String, String>();
		params.put("signup_profile_field_ids", signup_profile_field_ids);
		params.put("signup_username", signup_username);
		params.put("signup_email", signup_email);
		params.put("signup_password", signup_password);
		params.put("signup_password_confirm", signup_password_confirm);
		params.put("field_1", field_1);
		params.put("field_96", field_96);
		params.put("field_96_visibility", field_96_visibility);
		params.put("field_97", field_97);
		params.put("field_97_visibility", field_97_visibility);
		params.put("field_98", field_98);
		params.put("field_2", field_2);
		params.put("field_2_visibility", field_2_visibility);
		params.put("field_12", field_12);
		params.put("field_12_visibility", field_12_visibility);
		params.put("field_15", field_15);
		params.put("field_15_visibility", field_15_visibility);
		params.put("field_94", field_94);
		params.put("field_94_visibility", field_94_visibility);

		new RestApi(this, action, params) {
			public void process_result(JSONObject data) throws JSONException {
				onBackButton();
			}
		}.execute();

	}
}
