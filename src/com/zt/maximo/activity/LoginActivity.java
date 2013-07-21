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
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.zt.maximo.F;
import com.zt.maximo.R;
import com.zt.maximo.dao.BaseDao;
import com.zt.maximo.dao.UserDao;
import com.zt.maximo.dao.domain.UserDo;
import com.zt.maximo.service.UserService;
import com.zt.maximo.service.domain.AppProxyConfig;
import com.zt.maximo.service.domain.AppProxyResultDo;
import com.zt.maximo.service.domain.ProxyUserInfoDo;
import com.zt.maximo.util.DialogUtil;
import com.zt.maximo.util.JsonUtil;
import com.zt.maximo.util.PropertiesUtil;
import com.zt.maximo.util.PropertiesUtil.SpKey;

/**
 * 登陆Activity，为用户显示登陆界面
 */
public class LoginActivity extends BaseActivity implements OnClickListener,OnEditorActionListener{

	private static final String[] DUMMY_CREDENTIALS = new String[] {
			"foo@example.com:hello", "bar@example.com:world" };

	/**
	 * 默认的账号填充账号字段
	 */
	public static final String EXTRA_ACCOUNT = "com.zt.maximo.extra.EMAIL";

	/**
	 * 登陆任务(task)
	 */
	private UserLoginTask mAuthTask = null;

	// 账号、密码.
	private String mAccount;
	private String mPassword;
	
	//记住密码、自动登录
	private boolean isSavePassword = true;
	private boolean isAutoLogin = true;

	// UI控件.
	private EditText mAccountView;
	private EditText mPasswordView;
	private View mLoginFormView;
	private View mLoginStatusView;
	private TextView mLoginStatusMessageView;
	private CheckBox mSavePasswordView;
	private CheckBox mAutoLoginView;
	
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

		// 设置登陆表单
		mAccount = getIntent().getStringExtra(EXTRA_ACCOUNT);
		mAccountView = (EditText) findViewById(R.id.account);
		mAccountView.setText(mAccount);

		mPasswordView = (EditText) findViewById(R.id.password);
		
		mSavePasswordView = (CheckBox)findViewById(R.id.remember_password_checkbox);
		mAutoLoginView = (CheckBox)findViewById(R.id.auto_login_checkbox);

		mLoginFormView = findViewById(R.id.login_form);
		mLoginStatusView = findViewById(R.id.login_status);
		mLoginStatusMessageView = (TextView) findViewById(R.id.login_status_message);

		//注册事件
		mPasswordView.setOnEditorActionListener(this);
		findViewById(R.id.sign_in_button).setOnClickListener(this);
		mSavePasswordView.setOnClickListener(this);
		mAutoLoginView.setOnClickListener(this);
		
		//dao innit
		userDao = new UserDao(this);
		//properties innit
		prop = new PropertiesUtil(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		getMenuInflater().inflate(R.menu.login, menu);
		return true;
	}

	/**
	 * 尝试指定的账号、密码表单进行登陆.
	 * 如果有书写错误(如无效账户、空字段等)，则提示错误
	 */
	public void attemptLogin() {
		if (mAuthTask != null) {
			return;
		}

		// 重置错误信息.
		mAccountView.setError(null);
		mPasswordView.setError(null);

		// 存储当前的账号及密码.
		mAccount = mAccountView.getText().toString();
		mPassword = mPasswordView.getText().toString();

		boolean cancel = false;
		View focusView = null;

		// 无效密码验证(如为空、长度不足等)，目前默认密码长度必须大于4，可根据实际情况修改
		if (TextUtils.isEmpty(mPassword)) {
			mPasswordView.setError(getString(R.string.error_field_required));
			focusView = mPasswordView;
			cancel = true;
		} else if (mPassword.length() < 4) {
			mPasswordView.setError(getString(R.string.error_invalid_password));
			focusView = mPasswordView;
			cancel = true;
		}

		// 无效账号验证(如为空、长度不足等)，目前默认账号长度必须大于4，可根据实际情况修改.
		if (TextUtils.isEmpty(mAccount)) {
			mAccountView.setError(getString(R.string.error_field_required));
			focusView = mAccountView;
			cancel = true;
		} else if (mAccount.length() < 4) {
			mAccountView.setError(getString(R.string.error_invalid_account));
			focusView = mAccountView;
			cancel = true;
		}

		if (cancel) {
			// 有错误，不能登陆并且焦点返回第一个错误表单区域
			focusView.requestFocus();
		} else {
			// 显示进度条，并且开始进行后台登陆任务
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
		//默认记住密码、自动登录
		mSavePasswordView.setChecked(isSavePassword);
		mAutoLoginView.setChecked(isAutoLogin);
		
		if(prop.getBoolean(SpKey.isFirst, true)){
			new BaseDao(this).init();
			prop.setBoolean(SpKey.isFirst, false);
		}else{
			Log.i("LoginActivity", "not init base data");
		}
	}

	@Override
	protected void onStart() {
		super.onStart();
	}

	/**
	 * 显示进度UI，并隐藏登陆表单
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
	private void showProgress(final boolean show) {
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
			mLoginStatusView.setVisibility(show ? View.VISIBLE : View.GONE);
			mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
		}
	}

	/**
	 * 登陆验证的异步任务
	 */
	public class UserLoginTask extends AsyncTask<Void, Void, AppProxyResultDo> {
		@Override
		protected AppProxyResultDo doInBackground(Void... params) {

			try {
				Log.d("loginTask","currentThread:"+Thread.currentThread().getName());
				//登陆验证，返回：结果集包装类
				AppProxyResultDo aprd = UserService.getInstance().login(mAccount, mPassword);
				return aprd;
			} catch (Exception e) {
				return null;
			}
		}

		@Override
		protected void onPostExecute(final AppProxyResultDo aprd) {
			mAuthTask = null;
			showProgress(false);

			Log.i("loginResult", JsonUtil.Object2Json(aprd));
			
			if (aprd.isError()) {
				dismissDialog();
				DialogUtil.newAlertDialog(LoginActivity.this, "亲~", "网络异常,请稍后再试喔").show();
			} else {
				ProxyUserInfoDo result = JsonUtil.Json2T(aprd.getResut().toString(), ProxyUserInfoDo.class);
				switch (result.getRequestCode()) {
				case AppProxyConfig.USER_LOGIN_SUCCESS:
					UserDo user = new UserDo();
					user.setAccount(mAccount);
					user.setPassword(mPassword);
					user.setSavePassword(isSavePassword);
					user.setAutoLogin(isAutoLogin);
					user.setNick(result.getNick());
					user.setUid(result.getUid());
					user.setLastLogin(System.currentTimeMillis());
					UserService.getInstance().saveOrUpdateUser(user, userDao);
					
					prop.setString(SpKey.account, mAccount);
					prop.setString(SpKey.password, mPassword);
					prop.setString(SpKey.nick, result.getNick());
					prop.setInt(SpKey.uid, result.getUid());
					prop.setBoolean(SpKey.savePassword, isSavePassword);
					prop.setBoolean(SpKey.autoLogin, isAutoLogin);
					//将本地最新用户数据缓存到cache中，供全局使用
					F.user = user;
					
					Intent intent = new Intent();
					Bundle bundle = new Bundle();
					bundle.putString("nick", user.getNick());
					intent.putExtras(bundle);
					intent.setClass(LoginActivity.this, MainActivity.class);
					startActivity(intent);
					break;
				case AppProxyConfig.USER_LOGIN_FAIL:
					dismissDialog();
					DialogUtil.newAlertDialog(LoginActivity.this, "亲~", "请检查您的账号及密码是否正确").show();
					break;
				default:
					break;
				}
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

	@Override
	public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
		if (actionId == R.id.login || actionId == EditorInfo.IME_NULL) {
			attemptLogin();
			return true;
		}
		return false;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.sign_in_button:
			attemptLogin();
			break;
		case R.id.remember_password_checkbox:
			isSavePassword = !isSavePassword;
			mSavePasswordView.setChecked(isSavePassword);
			break;
		case R.id.auto_login_checkbox:
			isAutoLogin = !isAutoLogin;
			mAutoLoginView.setChecked(isAutoLogin);
			break;
		default:
			break;
		}
	}
}
