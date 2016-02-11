package com.drem.dremboard.entity;

import android.text.Editable;
import android.text.TextWatcher;

public abstract class MyTextWatcher implements TextWatcher {
	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		onTextChanged();
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
	@Override
	public void afterTextChanged(Editable s) { }
	
	public abstract void onTextChanged();
}
