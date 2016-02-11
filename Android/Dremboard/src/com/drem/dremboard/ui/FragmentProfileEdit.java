package com.drem.dremboard.ui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;

import org.json.JSONException;
import org.json.JSONObject;

import com.drem.dremboard.R;
import com.drem.dremboard.entity.GlobalValue;
import com.drem.dremboard.entity.ProfileItem;
import com.drem.dremboard.ui.FragmentProfileView.ProfileAdapter;
import com.drem.dremboard.ui.FragmentProfileView.ProfileAdapter.ProfileHolder;
import com.drem.dremboard.utils.RestApi;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Spinner;

public class FragmentProfileEdit extends Fragment implements OnClickListener
{
	ArrayList<ProfileItem> mArrayProfile = null;
	ArrayList<ProfileItem> mCurrentProfiles;

	Button mBtnSaveChanges;

	Spinner mSpinGender, mSpinGenderField;
	ArrayList<String> mArrayGender;

	Spinner mSpinLang, mSpinLangField;
	ArrayList<String> mArrayLang;

	Spinner mSpinCountry, mSpinCountryField;
	ArrayList<String> mArrayCountry;

	Spinner mSpinContentView, mSpinContentField;
	ArrayList<String> mArrayContentView, mArrayViewPermission;
	
	Spinner mSpinBirthdayField, mSpinAddressField, mSpinTelephoneField, mSpinFacebookField, mSpinGoogleField, mSpinTwitterField, mSpinBioField;

	EditText mTxtName, mTxtBirthday, mTxtAddress, mTxtTelephone, mTxtFacebook, mTxtGoogle, mTxtTwitter, mTxtBio;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View view = inflater.inflate(R.layout.fragment_profile_edit, null);
		mCurrentProfiles = GlobalValue.getInstance().getCurrentProfiles();
		
		initView (view);
		updateView();

		return view;
	}

	private void initView (View view)
	{
		mArrayProfile = new ArrayList<ProfileItem>();

		mBtnSaveChanges = (Button)view.findViewById(R.id.btnSaveChanges);
		mBtnSaveChanges.setOnClickListener(this);
		
		setSpinners(view);
		
		mTxtName = (EditText)view.findViewById(R.id.txtName);
		mTxtBirthday = (EditText)view.findViewById(R.id.txtBirthday);
		mTxtAddress = (EditText)view.findViewById(R.id.txtAddress);
		mTxtTelephone = (EditText)view.findViewById(R.id.txtTelephoneNum);
		mTxtFacebook = (EditText)view.findViewById(R.id.txtFacebook);
		mTxtGoogle = (EditText)view.findViewById(R.id.txtGoogle);
		mTxtTwitter = (EditText)view.findViewById(R.id.txtTwitter);
		mTxtBio = (EditText)view.findViewById(R.id.txtBio);
	}

	private void updateView()
	{
		mArrayProfile.clear();
		
		for (ProfileItem item : mCurrentProfiles)
		{
			mArrayProfile.add(item);
			
			switch (Integer.parseInt(item.id)) {
		        case 1: // Name
		            mTxtName.setText(item.value);
		            break;
		            
		        case 12: // Gender
		        	selectSpinnerItem(mSpinGender, item.value);
		            break;
		            
		        case 101: // Birthday
		        	mTxtBirthday.setText(item.value);
		            break;
		        
		        case 15: // Language
		        	selectSpinnerItem(mSpinLang, item.value);
		            break;
		            
		        case 94: // Country
		        	selectSpinnerItem(mSpinCountry, item.value);
		            break;
		            
		        case 100: // Address
		            mTxtAddress.setText(item.value);
		            break;
		            
		        case 102: // Phone Number
		        	mTxtTelephone.setText(item.value);
		            break;
		            
		        case 96: // Facebook
		        	mTxtFacebook.setText(item.value);
		            break;
		            
		        case 97: // Google +
		        	mTxtGoogle.setText(item.value);
		            break;
		            
		        case 98: // Twitter
		        	mTxtTwitter.setText(item.value);
		            break;
		            
		        case 99: // Bio
		        	mTxtBio.setText(item.value);
		            break;
		            
		        case 2: // Who can view my content
		        	selectSpinnerItem(mSpinContentView, item.value);
		            break;
		        default:
		        	break;
			}
		}
	}
	
	private void selectSpinnerItem(Spinner spinItem, String value) {
		int index = 0;
		
		for (int i = 0; i < spinItem.getCount(); i ++)
		{
			if (spinItem.getItemAtPosition(i).toString().equalsIgnoreCase(value))
			{
				spinItem.setSelection(i);
				return;
			}
		}
	}

	private void setSpinners(View view) {
		mArrayViewPermission = new ArrayList<String>();
		mArrayViewPermission.add("Everyone");
		mArrayViewPermission.add("Only Me");
		mArrayViewPermission.add("All Members");
		mArrayViewPermission.add("My Friends");
		mArrayViewPermission.add("Nobody");

		// Who can view my content
		mSpinContentView = (Spinner) view.findViewById(R.id.spinContent);
		mSpinContentView.setPrompt("Who can view my content");

		mArrayContentView = new ArrayList<String>();
		mArrayContentView.add("----");
		mArrayContentView.add("public can view my content");
		mArrayContentView.add("family and friends view my content");
		mArrayContentView.add("only myself can view my content");

		ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(getActivity(),
				android.R.layout.simple_spinner_item, mArrayContentView);
		adapter1.setDropDownViewResource(R.layout.custom_spinner_dropdown_item);
		mSpinContentView.setAdapter(adapter1);

		// Gender
		mSpinGender = (Spinner) view.findViewById(R.id.spinGender);
		mSpinGender.setPrompt("Select Gender");

		mArrayGender = new ArrayList<String>();
		mArrayGender.add("----");
		mArrayGender.add("male");
		mArrayGender.add("female");

		ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(getActivity(),
				android.R.layout.simple_spinner_item, mArrayGender);
		adapter2.setDropDownViewResource(R.layout.custom_spinner_dropdown_item);
		mSpinGender.setAdapter(adapter2);

		Locale[] locale = Locale.getAvailableLocales();

		// Language
		mSpinLang = (Spinner) view.findViewById(R.id.spinLang);
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

		ArrayAdapter<String> adapterLang = new ArrayAdapter<String>(getActivity(),
				android.R.layout.simple_spinner_item, mArrayLang);
		adapterLang
				.setDropDownViewResource(R.layout.custom_spinner_dropdown_item);
		mSpinLang.setAdapter(adapterLang);

		// Country
		mSpinCountry = (Spinner) view.findViewById(R.id.spinCountry);
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
		
		mArrayCountry.add("USA");
		mArrayCountry.add("USA Minor Outlying Islands");
		mArrayCountry.add("U.K.");
		
		Collections.sort(mArrayCountry, String.CASE_INSENSITIVE_ORDER);

		ArrayAdapter<String> adapterCountry = new ArrayAdapter<String>(getActivity(),
				android.R.layout.simple_spinner_item, mArrayCountry);
		adapterCountry
				.setDropDownViewResource(R.layout.custom_spinner_dropdown_item);
		mSpinCountry.setAdapter(adapterCountry);

		// Who can view my content
		mSpinContentField = (Spinner) view.findViewById(R.id.spinContentFieldPermission);
		mSpinContentField.setPrompt("Who can see this field?");

		ArrayAdapter<String> adapterContentField = new ArrayAdapter<String>(
				getActivity(), android.R.layout.simple_spinner_item,
				mArrayViewPermission);
		adapterContentField
				.setDropDownViewResource(R.layout.custom_spinner_dropdown_item);
		mSpinContentField.setAdapter(adapterContentField);

		// Gender Permission
		mSpinGenderField = (Spinner) view.findViewById(R.id.spinGenderFieldPermission);
		mSpinGenderField.setPrompt("Who can see this field?");

		ArrayAdapter<String> adapterGenderField = new ArrayAdapter<String>(
				getActivity(), android.R.layout.simple_spinner_item,
				mArrayViewPermission);
		adapterGenderField
				.setDropDownViewResource(R.layout.custom_spinner_dropdown_item);
		mSpinGenderField.setAdapter(adapterGenderField);

		// Language Permission
		mSpinLangField = (Spinner) view.findViewById(R.id.spinLangFieldPermission);
		mSpinLangField.setPrompt("Who can see this field?");

		ArrayAdapter<String> adapterLangField = new ArrayAdapter<String>(getActivity(),
				android.R.layout.simple_spinner_item, mArrayViewPermission);
		adapterLangField
				.setDropDownViewResource(R.layout.custom_spinner_dropdown_item);
		mSpinLangField.setAdapter(adapterLangField);

		// Country Permission
		mSpinCountryField = (Spinner) view.findViewById(R.id.spinCountryFieldPermission);
		mSpinCountryField.setPrompt("Who can see this field?");

		ArrayAdapter<String> adapterCountryField = new ArrayAdapter<String>(
				getActivity(), android.R.layout.simple_spinner_item,
				mArrayViewPermission);
		adapterCountryField
				.setDropDownViewResource(R.layout.custom_spinner_dropdown_item);
		mSpinCountryField.setAdapter(adapterCountryField);

		// Facebook Permission
		mSpinFacebookField = (Spinner) view.findViewById(R.id.spinFacebookFieldPermission);
		mSpinFacebookField.setPrompt("Who can see this field?");

		ArrayAdapter<String> adapterFacebookField = new ArrayAdapter<String>(
				getActivity(), android.R.layout.simple_spinner_item,
				mArrayViewPermission);
		adapterFacebookField
				.setDropDownViewResource(R.layout.custom_spinner_dropdown_item);
		mSpinFacebookField.setAdapter(adapterFacebookField);

		// Google Permission
		mSpinGoogleField = (Spinner) view.findViewById(R.id.spinGoogleFieldPermission);
		mSpinGoogleField.setPrompt("Who can see this field?");

		ArrayAdapter<String> adapterGoogleField = new ArrayAdapter<String>(
				getActivity(), android.R.layout.simple_spinner_item,
				mArrayViewPermission);
		adapterGoogleField
				.setDropDownViewResource(R.layout.custom_spinner_dropdown_item);
		mSpinGoogleField.setAdapter(adapterGoogleField);

		// Twitter Permission
		mSpinTwitterField = (Spinner) view.findViewById(R.id.spinTwitterFieldPermission);
		mSpinTwitterField.setPrompt("Who can see this field?");

		ArrayAdapter<String> adapterTwitterField = new ArrayAdapter<String>(
				getActivity(), android.R.layout.simple_spinner_item,
				mArrayViewPermission);
		adapterTwitterField
				.setDropDownViewResource(R.layout.custom_spinner_dropdown_item);
		mSpinTwitterField.setAdapter(adapterTwitterField);
		
		// Birthday Permission
		mSpinBirthdayField = (Spinner) view.findViewById(R.id.spinBirthdayFieldPermission);
		mSpinBirthdayField.setPrompt("Who can see this field?");

		ArrayAdapter<String> adapterBirthdayField = new ArrayAdapter<String>(
				getActivity(), android.R.layout.simple_spinner_item,
				mArrayViewPermission);
		adapterBirthdayField
				.setDropDownViewResource(R.layout.custom_spinner_dropdown_item);
		mSpinBirthdayField.setAdapter(adapterBirthdayField);
			
			
		// Address Permission
		mSpinAddressField = (Spinner) view.findViewById(R.id.spinAddressFieldPermission);
		mSpinAddressField.setPrompt("Who can see this field?");

		ArrayAdapter<String> adapterAddressField = new ArrayAdapter<String>(
				getActivity(), android.R.layout.simple_spinner_item,
				mArrayViewPermission);
		adapterAddressField
				.setDropDownViewResource(R.layout.custom_spinner_dropdown_item);
		mSpinAddressField.setAdapter(adapterAddressField);
			
			
		// Telephone Number Permission
		mSpinTelephoneField = (Spinner) view.findViewById(R.id.spinTelephoneNumFieldPermission);
		mSpinTelephoneField.setPrompt("Who can see this field?");

		ArrayAdapter<String> adapterTelephone = new ArrayAdapter<String>(
				getActivity(), android.R.layout.simple_spinner_item,
				mArrayViewPermission);
		adapterTelephone
				.setDropDownViewResource(R.layout.custom_spinner_dropdown_item);
		mSpinTelephoneField.setAdapter(adapterTelephone);
		
		// Bis Permission
		mSpinBioField = (Spinner) view.findViewById(R.id.spinFieldBioPermission);
		mSpinBioField.setPrompt("Who can see this field?");

		ArrayAdapter<String> adapterBio = new ArrayAdapter<String>(
				getActivity(), android.R.layout.simple_spinner_item,
				mArrayViewPermission);
		adapterBio
				.setDropDownViewResource(R.layout.custom_spinner_dropdown_item);
		mSpinBioField.setAdapter(adapterBio);
	}

	@Override
	public void onClick(View v) {
		int id = v.getId();

		switch (id) {
		case R.id.btnSaveChanges:
			onSaveChanges();
			break;
		default:
			break;
		}
	}
	
	private void onSaveChanges() {
		String action = RestApi.ACT_USER_REGISTER;
		String signup_profile_field_ids = "1,2,12,15,94,96,97,98,99,100,101,102";

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

		String field_99 = mTxtBio.getText().toString();
		String field_99_visibility = mSpinBioField.getSelectedItem().toString();

		String field_100 = mTxtAddress.getText().toString();
		String field_100_visibility = mSpinAddressField.getSelectedItem().toString();
		
		String field_101 = mTxtBirthday.getText().toString();
		String field_101_visibility = mSpinBirthdayField.getSelectedItem().toString();

		String field_102 = mTxtTelephone.getText().toString();
		String field_102_visibility = mSpinTelephoneField.getSelectedItem().toString();

		HashMap<String, String> params = new HashMap<String, String>();
		params.put("signup_profile_field_ids", signup_profile_field_ids);
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

		params.put("field_99", field_99);
		params.put("field_99_visibility", field_99_visibility);
		params.put("field_100", field_100);
		params.put("field_100_visibility", field_100_visibility);
		params.put("field_101", field_101);
		params.put("field_101_visibility", field_101_visibility);
		params.put("field_102", field_102);
		params.put("field_102_visibility", field_102_visibility);

		new RestApi(getActivity(), action, params) {
			public void process_result(JSONObject data) throws JSONException {
				getActivity().finish();
			}
		}.execute();

	}

}
