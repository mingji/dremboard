package com.drem.dremboard.view;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
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

public class CustomSearchView extends LinearLayout implements OnClickListener {

	Spinner spin_cate;
	EditText txt_search;
	Button btn_search;
	ArrayList<String> mArrayCategory;
	HashMap<String, String> mMapCategory;
	private AppPreferences mPrefs;
	public OnCustomSearchListener mSearchListener;

	public CustomSearchView(Context context) {
		super(context);
		init();
	}

	public CustomSearchView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}
	
	public void setOnCustomSearchListener(OnCustomSearchListener searchListener){
		this.mSearchListener = searchListener;
	}

	private void init() {
		mPrefs = new AppPreferences(getContext());
		
		String infService = Context.LAYOUT_INFLATER_SERVICE;
		LayoutInflater li;
		li = (LayoutInflater) getContext().getSystemService(infService);
		li.inflate(R.layout.dremers_search, this, true);

		txt_search = (EditText) findViewById(R.id.txt_search);
		btn_search = (Button) findViewById(R.id.btn_search);
		btn_search.setOnClickListener(this);
	}

	public static interface OnCustomSearchListener{
		public void onCustomSearchBtnClicked(String search_str);
	}
	@Override
	public void onClick(View v) {
		int id = v.getId();
		if (id == R.id.btn_search && !mSearchListener.equals(null) ){
			String search_str = txt_search.getText().toString();

			mSearchListener.onCustomSearchBtnClicked(search_str);
			txt_search.setText("");
		}
	}
}
