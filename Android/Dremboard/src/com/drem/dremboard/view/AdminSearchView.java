package com.drem.dremboard.view;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.drem.dremboard.R;
import com.drem.dremboard.utils.AppPreferences;

public class AdminSearchView extends LinearLayout implements OnClickListener {

	Spinner spin_cate;
	EditText txt_search;
	Button btn_search;
	ArrayList<String> mArrayCategory;
	HashMap<String, String> mMapCategory;
	private AppPreferences mPrefs;
	public OnSearchListener mSearchListener;

	public AdminSearchView(Context context) {
		super(context);
		init();
	}

	public AdminSearchView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}
	
	public void setOnSearchListener(OnSearchListener searchListener){
		this.mSearchListener = searchListener;
	}

	private void init() {
		mPrefs = new AppPreferences(getContext());
		
		String infService = Context.LAYOUT_INFLATER_SERVICE;
		LayoutInflater li;
		li = (LayoutInflater) getContext().getSystemService(infService);
		li.inflate(R.layout.admin_search, this, true);

		spin_cate = (Spinner) findViewById(R.id.spin_cate);
		txt_search = (EditText) findViewById(R.id.txt_search);
		btn_search = (Button) findViewById(R.id.btn_search);

		spin_cate.setPrompt("Select Category");
		mArrayCategory = new ArrayList<String>();
		mMapCategory = new HashMap<String, String>();
		
		try {
			setCategories();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		ArrayAdapter<String> adapterCategory = new ArrayAdapter<String>(
				getContext(), android.R.layout.simple_spinner_item,
				mArrayCategory);
		adapterCategory
				.setDropDownViewResource(R.layout.custom_spinner_dropdown_item);
		spin_cate.setAdapter(adapterCategory);
		spin_cate.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				
				// same logic when click search button <<
				String cate_val = spin_cate.getSelectedItem().toString();
				String category = mMapCategory.get(cate_val);
				String search_str = "";

				mSearchListener.onSearchBtnClicked(category, search_str);
			}
				// >>

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub

			}
		});
		btn_search.setOnClickListener(this);
	}

	private void setCategories() throws JSONException {
		String category_json = mPrefs.getCategoryList();
		JSONObject category_obj = new JSONObject(category_json);
		JSONArray cate_key_obj = category_obj.getJSONArray("keys");
		JSONArray cate_val_obj = category_obj.getJSONArray("values");

		for (int i = 0; i < cate_key_obj.length(); i++) {
			mArrayCategory.add(cate_val_obj.getString(i));
			mMapCategory.put(cate_val_obj.getString(i),
					cate_key_obj.getString(i));
		}
	}
	public static interface OnSearchListener{
		public void onSearchBtnClicked(String category, String search_str);
	}
	@Override
	public void onClick(View v) {
		int id = v.getId();
		if (id == R.id.btn_search && !mSearchListener.equals(null) ){
			String cate_val = spin_cate.getSelectedItem().toString();
			String category = mMapCategory.get(cate_val);
			String search_str = txt_search.getText().toString();

			mSearchListener.onSearchBtnClicked(category, search_str);
			
			spin_cate.setSelection(0);
			txt_search.setText("");
		}
	}
}
