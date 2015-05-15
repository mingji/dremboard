package com.drem.dremboard.ui;

import com.drem.dremboard.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

public class FragmentBlank extends Fragment implements OnClickListener
{
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View view = inflater.inflate(R.layout.fragment_blank, null);

		initView (view);

		return view;
	}

	private void initView (View view)
	{
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

	}
}
