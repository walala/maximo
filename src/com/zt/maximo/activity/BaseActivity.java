package com.zt.maximo.activity;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;

import com.zt.maximo.util.DialogUtil;

public class BaseActivity extends Activity {

	private Dialog alertDialog;
	
	public void showAlertDialog(String title,String content){
		if (null != alertDialog) {
			alertDialog.dismiss();
			alertDialog = null;
		}
		alertDialog = DialogUtil.newAlertDialog(this, title, content);
		alertDialog.show();
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}
	
	

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}

	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
	}

}
