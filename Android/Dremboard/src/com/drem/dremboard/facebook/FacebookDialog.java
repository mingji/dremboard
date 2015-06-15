/*
 * Copyright 2010 Facebook, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *	http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.drem.dremboard.facebook;

import com.drem.dremboard.facebook.Facebook.FacebookDialogListener;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

//==============================================================================
public class FacebookDialog extends Dialog {

	static final int FB_BLUE = 0xFF6D84B4;
//	static final float[] DIMENSIONS_DIFF_LANDSCAPE	= {20, 60};
//	static final float[] DIMENSIONS_DIFF_PORTRAIT	= {40, 60};
	static final float[] DIMENSIONS_DIFF_LANDSCAPE	= {0, 0};
	static final float[] DIMENSIONS_DIFF_PORTRAIT	= {0, 0};
	static final FrameLayout.LayoutParams LYTPARAM_FILL =
		new FrameLayout.LayoutParams(
			ViewGroup.LayoutParams.FILL_PARENT,
			ViewGroup.LayoutParams.FILL_PARENT
		);
	static final int	MARGIN	= 4;
	static final int	PADDING	= 2;
	static final String	DISPLAY_STRING = "touch";
	static final String	FB_ICON = "icon.png";

	private String					m_strUrl;
	private FacebookDialogListener	m_listener;
	private ProgressDialog			m_progressDlg;
//	private ImageView				m_imgViewCrossImage;
	private WebView					m_webView;
	private FrameLayout				m_frmLytContent;

	//------------------------------------------------------------------------------
	public FacebookDialog(Context context, String url, FacebookDialogListener listener) {
		super(context, android.R.style.Theme_Translucent_NoTitleBar);
		m_strUrl = url;
		m_listener = listener;
	}

	//------------------------------------------------------------------------------
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		m_progressDlg = new ProgressDialog(getContext());
		m_progressDlg.requestWindowFeature(Window.FEATURE_NO_TITLE);
		m_progressDlg.setMessage("Loading...");
		m_progressDlg.setCanceledOnTouchOutside(false);
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		m_frmLytContent = new FrameLayout(getContext());

		/* Create the 'x' image, but don't add to the mContent layout yet
		 * at this point, we only need to know its drawable width and height 
		 * to place the webview
		 */
		createCrossImage();
		
		/* Now we know 'x' drawable width and height, 
		 * layout the webivew and add it the mContent layout
		 */
//		int crossWidth = m_imgViewCrossImage.getDrawable().getIntrinsicWidth();
		int crossWidth = 0;
		setUpWebView(crossWidth / 2);
		
		/* Finally add the 'x' image to the mContent layout and
		 * add mContent to the Dialog view
		 */
//		mContent.addView(m_imgViewCrossImage, new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		addContentView(m_frmLytContent, new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
	}
	
	//------------------------------------------------------------------------------
	private void createCrossImage() {
		/*
		mCrossImage = new ImageView(getContext());
		// Dismiss the dialog when user click on the 'x'
		mCrossImage.setOnClickListener(
			new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					mListener.OnCancel();
					FacebookDialog.this.dismiss();
				}
			}
		);
		int id = getContext().getResources().getIdentifier("img_btn_close", "drawable", getContext().getPackageName());
		Drawable crossDrawable = getContext().getResources().getDrawable(id);
		mCrossImage.setImageDrawable(crossDrawable);
		*/
		/* 'x' should not be visible while webview is loading
		 * make it visible only after webview has fully loaded
		*/
//		mCrossImage.setVisibility(View.INVISIBLE);
		
	}

	//------------------------------------------------------------------------------
	private void setUpWebView(int margin) {
		LinearLayout webViewContainer = new LinearLayout(getContext());
		m_webView = new WebView(getContext());
		m_webView.setVerticalScrollBarEnabled(false);
		m_webView.setHorizontalScrollBarEnabled(false);
		m_webView.setWebViewClient(new FacebookDialog.FbWebViewClient());
		m_webView.getSettings().setJavaScriptEnabled(true);
		m_webView.loadUrl(m_strUrl);
		m_webView.setLayoutParams(LYTPARAM_FILL);
		m_webView.setVisibility(View.INVISIBLE);
		m_webView.getSettings().setSavePassword(false);
		
		webViewContainer.setPadding(margin, margin, margin, margin);
		webViewContainer.addView(m_webView);
		m_frmLytContent.addView(webViewContainer);
		
	}

	//==============================================================================
	private class FbWebViewClient extends WebViewClient {
		//------------------------------------------------------------------------------
		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			FacebookMisc.Logd("Facebook-WebView", "Redirect URL: " + url);
			if (url.startsWith(Facebook.STR_URI_REDIRECT)) {
				Bundle values = FacebookMisc.parseUrl(url);

				String error = values.getString("error");
				if (error == null) {
					error = values.getString("error_type");
				}

				if (error == null) {
					m_listener.OnComplete(values);
				}
				else if (error.equals("access_denied") || error.equals("OAuthAccessDeniedException")) {
					m_listener.OnCancel();
				}
				else {
					m_listener.OnFacebookError(new FacebookError(error));
				}

				FacebookDialog.this.dismiss();
				return true;
			}
			else if (url.startsWith(Facebook.STR_URI_CANCEL)) {
				m_listener.OnCancel();
				FacebookDialog.this.dismiss();
				return true;
			}
			else if (url.contains(DISPLAY_STRING)) {
				return false;
			}
			// launch non-dialog URLs in a full browser
			getContext().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
			return true;
		}

		//------------------------------------------------------------------------------
		@Override
		public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
			super.onReceivedError(view, errorCode, description, failingUrl);
			m_listener.OnError(new DialogError(description, errorCode, failingUrl));
			FacebookDialog.this.dismiss();
		}

		//------------------------------------------------------------------------------
		@Override
		public void onPageStarted(WebView view, String url, Bitmap favicon) {
			FacebookMisc.Logd("Facebook-WebView", "Webview loading URL: " + url);
			super.onPageStarted(view, url, favicon);
			m_progressDlg.show();
		}

		//------------------------------------------------------------------------------
		@Override
		public void onPageFinished(WebView view, String url) {
			super.onPageFinished(view, url);
			m_progressDlg.dismiss();
			/* 
			 * Once webview is fully loaded, set the mContent background to be transparent
			 * and make visible the 'x' image. 
			 */
			m_frmLytContent.setBackgroundColor(Color.TRANSPARENT);
			m_webView.setVisibility(View.VISIBLE);
//			mCrossImage.setVisibility(View.VISIBLE);
		}
		//------------------------------------------------------------------------------
	}
	//==============================================================================
}

//==============================================================================
