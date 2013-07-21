package com.zt.maximo.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;

import com.zt.maximo.F;
import com.zt.maximo.R;
import com.zt.maximo.dao.BaseDao;
import com.zt.maximo.dao.domain.UserDo;
import com.zt.maximo.service.UserService;
import com.zt.maximo.service.domain.AppProxyConfig;
import com.zt.maximo.service.domain.AppProxyResultDo;
import com.zt.maximo.service.domain.ProxyUserInfoDo;
import com.zt.maximo.util.DialogUtil;
import com.zt.maximo.util.JsonUtil;
import com.zt.maximo.util.PropertiesUtil;
import com.zt.maximo.util.PropertiesUtil.SpKey;

public class StartActivity extends Activity {

	private PropertiesUtil prop;
	private AutoLoginTask mAuthTask;
	private Dialog dialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		prop = new PropertiesUtil(this);
		setContentView(R.layout.activity_start);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.start, menu);
		return true;
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

		if (prop.getBoolean(SpKey.isFirst, true)) {
			new BaseDao(this).init();
			prop.setBoolean(SpKey.isFirst, false);
		} else {
			Log.i("StartActivity", "not init base data");
		}

		// 是否设置为自动登录
		if (prop.getBoolean(SpKey.autoLogin, false)) {
			autoLogin();
		} else {
			// 如果用户没有设置自动登录,则进入登录页面
			new Handler().postDelayed(new Runnable() {
				@Override
				public void run() {
					startActivity(new Intent(StartActivity.this,
							LoginActivity.class));
					finish();
				}
			}, 1000);
		}
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
	}
	
	public void dismissDialog() {
		if (null!=dialog) {
			dialog.dismiss();
			dialog = null;
		}
	}
	
	private void autoLogin(){
		mAuthTask = new AutoLoginTask();
		mAuthTask.execute((Void)null);
	}

	public class AutoLoginTask extends AsyncTask<Void, Void, AppProxyResultDo> {

		@Override
		protected AppProxyResultDo doInBackground(Void... params) {
			// TODO Auto-generated method stub
			Log.d("autoLoginTask", "currentThread:"
					+ Thread.currentThread().getName());
			String account = prop.getString(SpKey.account, null);
			String password = prop.getString(SpKey.password, null);
			try {
				AppProxyResultDo apro = UserService.getInstance().login(
						account, password);
				return apro;
			} catch (Exception ex) {
				ex.printStackTrace();
				return null;
			}
		}

		@Override
		protected void onCancelled() {
			// TODO Auto-generated method stub
			super.onCancelled();
		}

		@Override
		protected void onPostExecute(AppProxyResultDo aprd) {
			if(aprd.isError()){
				dismissDialog();
				DialogUtil.newAlertDialog(StartActivity.this, "亲~", "网络异常,请稍后再试喔").show();
			}else{
				ProxyUserInfoDo result = JsonUtil.Json2T(aprd.getResut().toString(), ProxyUserInfoDo.class);
				switch (result.getRequestCode()) {
				case AppProxyConfig.USER_LOGIN_SUCCESS:
					UserDo user = new UserDo();
					user.setAccount(prop.getString(SpKey.account, ""));
					user.setPassword(prop.getString(SpKey.password, ""));
					user.setSavePassword(prop.getBoolean(SpKey.savePassword, false));
					user.setAutoLogin(prop.getBoolean(SpKey.autoLogin, false));
					user.setNick(result.getNick());
					user.setUid(result.getUid());
					user.setLastLogin(System.currentTimeMillis());
					//将本地最新用户数据缓存到cache中，供全局使用
					F.user = user;
					
					Intent intent = new Intent();
					Bundle bundle = new Bundle();
					bundle.putString("nick", user.getNick());
					intent.putExtras(bundle);
					intent.setClass(StartActivity.this, MainActivity.class);
					startActivity(intent);
					break;
				case AppProxyConfig.USER_LOGIN_FAIL:
					startActivity(new Intent().setClass(StartActivity.this, LoginActivity.class));
					
					break;
				default:
					break;
				}
				finish();
			}
		}

		@Override
		protected void onProgressUpdate(Void... values) {
			// TODO Auto-generated method stub
			super.onProgressUpdate(values);
		}

	}

}
