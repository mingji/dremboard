package com.drem.dremboard.google;

import java.io.InputStream;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender.SendIntentException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.drem.dremboard.Const;
import com.drem.dremboard.R;
import com.drem.dremboard.ui.ActivityMain;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;

//==============================================================================
public class ActivityLoginGoogle extends Activity
		implements	OnClickListener,
					ConnectionCallbacks,
					OnConnectionFailedListener {

	private static final int RC_SIGN_IN = 0;
	// Profile pic image size in pixels
	private static final int PROFILE_PIC_SIZE = 400;

	// Google client to interact with Google API
	private GoogleApiClient m_googleApiClient;

	private boolean	m_bIntentInProgress;
	private boolean	m_bSignInClicked;

	private ConnectionResult	m_connectionResult;

	private SignInButton	m_btnSignIn;
	private Button			m_btnSignOut;
	private Button			m_btnRevokeAccess;
	private ImageView		m_imgViewProfilePic;
	private TextView		m_txtViewName;
	private TextView		m_txtViewEmail;
	private LinearLayout	m_linLytProfile;

	private	String		m_strUserName;
	private	String		m_strAvatarUrl;

	//------------------------------------------------------------------------------
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login_google);

		m_strUserName	= null;
		m_strAvatarUrl	= null;
		m_btnSignIn				= (SignInButton)findViewById(R.id.btn_sign_in);
		m_btnSignOut			= (Button)		findViewById(R.id.btn_sign_out);
		m_btnRevokeAccess		= (Button)		findViewById(R.id.btn_revoke_access);
		m_imgViewProfilePic		= (ImageView)	findViewById(R.id.imgProfilePic);
		m_txtViewName			= (TextView)	findViewById(R.id.txtName);
		m_txtViewEmail			= (TextView)	findViewById(R.id.txtEmail);
		m_linLytProfile			= (LinearLayout)findViewById(R.id.llProfile);

		// Button click listeners
		m_btnSignIn.setOnClickListener(this);
		m_btnSignOut.setOnClickListener(this);
		m_btnRevokeAccess.setOnClickListener(this);

		m_googleApiClient = new GoogleApiClient
				.Builder(this)
				.addConnectionCallbacks(this)
				.addOnConnectionFailedListener(this)
				.addApi(Plus.API)
				.addScope(Plus.SCOPE_PLUS_LOGIN).build();
	}

	//------------------------------------------------------------------------------
	@Override
	protected void onStart() {
		super.onStart();
		m_googleApiClient.connect();
	}

	//------------------------------------------------------------------------------
	@Override
	protected void onStop() {
		super.onStop();
		if (m_googleApiClient.isConnected()) {
			m_googleApiClient.disconnect();
		}
	}

	//------------------------------------------------------------------------------
	private void ResolveSignInError() {
		if (m_connectionResult.hasResolution()) {
			try {
				m_bIntentInProgress = true;
				m_connectionResult.startResolutionForResult(this, RC_SIGN_IN);
			}
			catch (SendIntentException e) {
				m_bIntentInProgress = false;
				m_googleApiClient.connect();
			}
		}
	}

	//------------------------------------------------------------------------------
	@Override
	public void onConnectionFailed(ConnectionResult result) {
		if (!result.hasResolution()) {
			GooglePlayServicesUtil.getErrorDialog(result.getErrorCode(), this, 0).show();
			return;
		}

		if (!m_bIntentInProgress) {
			// Store the ConnectionResult for later usage
			m_connectionResult = result;

			if (m_bSignInClicked) {
				// The user has already clicked 'sign-in'
				// so we attempt to resolve all errors until the user is signed in, or they cancel.
				ResolveSignInError();
			}
		}

	}

	//------------------------------------------------------------------------------
	@Override
	protected void onActivityResult(int requestCode, int responseCode, Intent intent) {
		if (requestCode == RC_SIGN_IN) {
			if (responseCode != RESULT_OK) {
				m_bSignInClicked = false;
			}

			m_bIntentInProgress = false;

			if (!m_googleApiClient.isConnecting()) {
				m_googleApiClient.connect();
			}
		}
	}

	//------------------------------------------------------------------------------
	@Override
	public void onConnected(Bundle arg0) {
		m_bSignInClicked = false;
		Toast.makeText(this, "User is connected!", Toast.LENGTH_LONG).show();

		// Get user's information
		GetProfileInformation();

		// Update the UI after signin
		UpdateUi(true);

	}

	//------------------------------------------------------------------------------
	private void UpdateUi(boolean isSignedIn) {
		if (isSignedIn) {
//			btnSignIn.setVisibility(View.GONE);
//			btnSignOut.setVisibility(View.VISIBLE);
//			btnRevokeAccess.setVisibility(View.VISIBLE);
//			llProfileLayout.setVisibility(View.VISIBLE);
			/*
			Intent		intentGoogle2Main;
			Bundle		bundle;
			intentGoogle2Main = new Intent(ActivityLoginGoogle.this, ActivityMain.class);
			bundle = new Bundle();
			bundle.putInt(Const.KEY_LOGINTYPE, Const.TYPE_LOGIN_GOOGLE);
			bundle.putString(Const.KEY_USERID,		m_strUserName);
			bundle.putString(Const.KEY_USERNAME,	m_strUserName);
			bundle.putString(Const.KEY_AVATAR,		m_strAvatarUrl);
			intentGoogle2Main.putExtras(bundle);
			startActivity(intentGoogle2Main);
			finish();
			*/
		}
		else {
			m_btnSignIn.setVisibility(View.VISIBLE);
			m_btnSignOut.setVisibility(View.GONE);
			m_btnRevokeAccess.setVisibility(View.GONE);
			m_linLytProfile.setVisibility(View.GONE);
		}
	}

	//------------------------------------------------------------------------------
	private void GetProfileInformation() {
		try {
			if (Plus.PeopleApi.getCurrentPerson(m_googleApiClient) != null) {
				Person currentPerson = Plus.PeopleApi.getCurrentPerson(m_googleApiClient);
				m_strUserName	= currentPerson.getDisplayName();
				m_strAvatarUrl	= currentPerson.getImage().getUrl();
				String strPersonProfile = currentPerson.getUrl();
				String strEmail = Plus.AccountApi.getAccountName(m_googleApiClient);

				m_txtViewName.setText(m_strUserName);
				m_txtViewEmail.setText(strEmail);

				// by default the profile url gives 50x50 px image only
				// we can replace the value with whatever dimension we want by
				// replacing sz=X
				m_strAvatarUrl = m_strAvatarUrl.substring(0, m_strAvatarUrl.length() - 2) + PROFILE_PIC_SIZE;

				new LoadProfileImage(m_imgViewProfilePic).execute(m_strAvatarUrl);
			}
			else {
				Toast.makeText(
					getApplicationContext(),
					"Person information is null", Toast.LENGTH_LONG
				).show();
			}
		} catch (Exception e) {}
	}

	//------------------------------------------------------------------------------
	@Override
	public void onConnectionSuspended(int arg0) {
		m_googleApiClient.connect();
		UpdateUi(false);
	}

	//------------------------------------------------------------------------------
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
//		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	//------------------------------------------------------------------------------
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.btn_sign_in:
				// Signin button clicked
				SignInWithGplus();
				break;
			case R.id.btn_sign_out:
				// Signout button clicked
				SignOutFromGplus();
				break;
			case R.id.btn_revoke_access:
				// Revoke access button clicked
				RevokeGplusAccess();
				break;
		}
	}

	//------------------------------------------------------------------------------
	private void SignInWithGplus() {
		if (!m_googleApiClient.isConnecting()) {
			m_bSignInClicked = true;
			ResolveSignInError();
		}
	}

	//------------------------------------------------------------------------------
	private void SignOutFromGplus() {
		if (m_googleApiClient.isConnected()) {
			Plus.AccountApi.clearDefaultAccount(m_googleApiClient);
			m_googleApiClient.disconnect();
			m_googleApiClient.connect();
			UpdateUi(false);
		}
	}

	//------------------------------------------------------------------------------
	private void RevokeGplusAccess() {
		if (m_googleApiClient.isConnected()) {
			Plus.AccountApi.clearDefaultAccount(m_googleApiClient);
			Plus.AccountApi.revokeAccessAndDisconnect(m_googleApiClient).setResultCallback(
				new ResultCallback<Status>() {
					@Override
					public void onResult(Status arg0) {
						m_googleApiClient.connect();
						UpdateUi(false);
					}
				}
			);
		}
	}

	//==============================================================================
	private class LoadProfileImage extends AsyncTask<String, Void, Bitmap> {
		ImageView bmImage;

		public LoadProfileImage(ImageView bmImage) {
			this.bmImage = bmImage;
		}

		@Override
		protected Bitmap doInBackground(String... urls) {
			String	strUrlDisplay = urls[0];
			Bitmap	mIcon11 = null;
			try {
				InputStream in = new java.net.URL(strUrlDisplay).openStream();
				mIcon11 = BitmapFactory.decodeStream(in);
			}
			catch (Exception e) {}
			return mIcon11;
		}

		@Override
		protected void onPostExecute(Bitmap result) {
			bmImage.setImageBitmap(result);
		}
	}

	//==============================================================================

}

//==============================================================================
