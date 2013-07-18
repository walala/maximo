package com.zt.maximo.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.zt.maximo.R;
import com.zt.maximo.dao.BaseDao;
import com.zt.maximo.dao.UserDao;
import com.zt.maximo.service.UserService;
import com.zt.maximo.service.domain.AppProxyConfig;
import com.zt.maximo.service.domain.AppProxyResultDo;
import com.zt.maximo.service.domain.ProxyUserInfoDo;
import com.zt.maximo.util.DialogUtil;
import com.zt.maximo.util.JsonUtil;
import com.zt.maximo.util.PropertiesUtil;
import com.zt.maximo.util.PropertiesUtil.SpKey;

/**
 * Activity which displays a login screen to the user, offering registration as
 * well.
 */
public class LoginActivity extends BaseActivity {
	/**
	 * A dummy authentication store containing known user names and passwords.
	 * TODO: remove after connecting to a real authentication system.
	 */
	private static final String[] DUMMY_CREDENTIALS = new String[] {
			"foo@example.com:hello", "bar@example.com:world" };

	/**
	 * The default email to populate the email field with.
	 */
	public static final String EXTRA_EMAIL = "com.example.android.authenticatordemo.extra.EMAIL";

	/**
	 * Keep track of the login task to ensure we can cancel it if requested.
	 */
	private UserLoginTask mAuthTask = null;

	// Values for email and password at the time of the login attempt.
	private String mAccount;
	private String mPassword;

	// UI references.
	private EditText mAccountView;
	private EditText mPasswordView;
	private View mLoginFormView;
	private View mLoginStatusView;
	private TextView mLoginStatusMessageView;
	
	//dao
	private UserDao userDao = null;
	// properties
	private PropertiesUtil prop;
	// dialog
	private Dialog dialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_login);

		// Set up the login form.
		mAccount = getIntent().getStringExtra(EXTRA_EMAIL);
		mAccountView = (EditText) findViewById(R.id.account);
		mAccountView.setText(mAccount);

		mPasswordView = (EditText) findViewById(R.id.password);
		mPasswordView
				.setOnEditorActionListener(new TextView.OnEditorActionListener() {
					@Override
					public boolean onEditorAction(TextView textView, int id,
							KeyEvent keyEvent) {
						if (id == R.id.login || id == EditorInfo.IME_NULL) {
							attemptLogin();
							return true;
						}
						return false;
					}
				});

		mLoginFormView = findViewById(R.id.login_form);
		mLoginStatusView = findViewById(R.id.login_status);
		mLoginStatusMessageView = (TextView) findViewById(R.id.login_status_message);

		findViewById(R.id.sign_in_button).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						attemptLogin();
					}
				});
		
		//dao innit
		userDao = new UserDao(this);
		
		prop = new PropertiesUtil(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		getMenuInflater().inflate(R.menu.login, menu);
		return true;
	}

	/**
	 * Attempts to sign in or register the account specified by the login form.
	 * If there are form errors (invalid email, missing fields, etc.), the
	 * errors are presented and no actual login attempt is made.
	 */
	public void attemptLogin() {
		if (mAuthTask != null) {
			return;
		}

		// Reset errors.
		mAccountView.setError(null);
		mPasswordView.setError(null);

		// Store values at the time of the login attempt.
		mAccount = mAccountView.getText().toString();
		mPassword = mPasswordView.getText().toString();

		boolean cancel = false;
		View focusView = null;

		// Check for a valid password.
		if (TextUtils.isEmpty(mPassword)) {
			mPasswordView.setError(getString(R.string.error_field_required));
			focusView = mPasswordView;
			cancel = true;
		} else if (mPassword.length() < 4) {
			mPasswordView.setError(getString(R.string.error_invalid_password));
			focusView = mPasswordView;
			cancel = true;
		}

		// Check for a valid email address.
		if (TextUtils.isEmpty(mAccount)) {
			mAccountView.setError(getString(R.string.error_field_required));
			focusView = mAccountView;
			cancel = true;
		} else if (!mAccount.contains("@")) {
			mAccountView.setError(getString(R.string.error_invalid_account));
			focusView = mAccountView;
			cancel = true;
		}

		if (cancel) {
			// There was an error; don't attempt login and focus the first
			// form field with an error.
			focusView.requestFocus();
		} else {
			// Show a progress spinner, and kick off a background task to
			// perform the user login attempt.
			mLoginStatusMessageView.setText(R.string.login_progress_signing_in);
			showProgress(true);
			mAuthTask = new UserLoginTask();
			mAuthTask.execute((Void) null);
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		Log.i("LoginActivity", "onResume...");
		if(prop.getBoolean(SpKey.isFirst, true)){
			new BaseDao(this).init();
			prop.setBoolean(SpKey.isFirst, false);
		}else{
			Log.i("LoginActivity", "not init base data");
		}
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
	}

	/**
	 * Shows the progress UI and hides the login form.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
	private void showProgress(final boolean show) {
		// On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
		// for very easy animations. If available, use these APIs to fade-in
		// the progress spinner.
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
			int shortAnimTime = getResources().getInteger(
					android.R.integer.config_shortAnimTime);

			mLoginStatusView.setVisibility(View.VISIBLE);
			mLoginStatusView.animate().setDuration(shortAnimTime)
					.alpha(show ? 1 : 0)
					.setListener(new AnimatorListenerAdapter() {
						@Override
						public void onAnimationEnd(Animator animation) {
							mLoginStatusView.setVisibility(show ? View.VISIBLE
									: View.GONE);
						}
					});

			mLoginFormView.setVisibility(View.VISIBLE);
			mLoginFormView.animate().setDuration(shortAnimTime)
					.alpha(show ? 0 : 1)
					.setListener(new AnimatorListenerAdapter() {
						@Override
						public void onAnimationEnd(Animator animation) {
							mLoginFormView.setVisibility(show ? View.GONE
									: View.VISIBLE);
						}
					});
		} else {
			// The ViewPropertyAnimator APIs are not available, so simply show
			// and hide the relevant UI components.
			mLoginStatusView.setVisibility(show ? View.VISIBLE : View.GONE);
			mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
		}
	}

	/**
	 * Represents an asynchronous login/registration task used to authenticate
	 * the user.
	 */
	public class UserLoginTask extends AsyncTask<Void, Void, AppProxyResultDo> {
		@Override
		protected AppProxyResultDo doInBackground(Void... params) {
			// TODO: attempt authentication against a network service.

			try {
				// Simulate network access.
				Log.d("loginTask","currentThread:"+Thread.currentThread().getName());
				AppProxyResultDo aprd = UserService.getInstance().login(mAccount, mPassword);
				
				//-----模拟AppProxyResultDo数据------
/*				aprd.setError(false);
				aprd.setErrorMessage(null);
				
				ProxyUserInfoDo puiDo = new ProxyUserInfoDo();
				puiDo.setRequestCode(AppProxyConfig.USER_LOGIN_SUCCESS);
				puiDo.setNick("张三");
				puiDo.setUid(111111);
				aprd.setResut(puiDo);*/
				//-----模拟AppProxyResultDo数据------
				
				return aprd;
			} catch (Exception e) {
				return null;
			}

/*			for (String credential : DUMMY_CREDENTIALS) {
				String[] pieces = credential.split(":");
				if (pieces[0].equals(mAccount)) {
					// Account exists, return true if the password matches.
					return new AppProxyResultDo();
				}
			}*/
		}

		@Override
		protected void onPostExecute(final AppProxyResultDo result) {
			mAuthTask = null;
			showProgress(false);

			Log.i("loginResult", JsonUtil.Object2Json(result));
			
			if (result.isError()) {
//				mPasswordView.setError(getString(R.string.error_incorrect_password));
//				mPasswordView.requestFocus();
				
				dismissDialog();
				DialogUtil.newAlertDialog(LoginActivity.this, "亲~", "网络异常,请稍后再试喔").show();
				
			} else {
				Intent intent = new Intent();
				Bundle bundle = new Bundle();
				bundle.putString("nick", "张三");
				intent.putExtras(bundle);
				intent.setClass(LoginActivity.this, MainActivity.class);
				startActivity(intent);
			}
		}

		@Override
		protected void onCancelled() {
			mAuthTask = null;
			showProgress(false);
		}
	}
	
	public void dismissDialog() {
		if (null!=dialog) {
			dialog.dismiss();
			dialog = null;
		}
	}
}
