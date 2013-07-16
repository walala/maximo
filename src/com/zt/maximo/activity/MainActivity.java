package com.zt.maximo.activity;

import com.zt.maximo.R;

import android.os.Bundle;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		String nick = getIntent().getStringExtra("nick");
		Log.i("nick",nick);
		setTitle(getTitle() + " -- " + nick);
		
		//注册按钮监听事件
		onClickListenter();
		
	}
	
	public void onClickListenter(){
		
		View.OnClickListener handler = new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				switch (v.getId()) {
				case R.id.repairButton:
					intent.setClass(MainActivity.this, RepairActivity.class);
					startActivity(intent);
					break;
				case R.id.toDoButton:
					intent.setClass(MainActivity.this, RepairActivity.class);
					startActivity(intent);
					break;

				default:
					break;
				}
			}
		};
		
		Button repairButton = (Button)findViewById(R.id.repairButton);
		Button toDoButton = (Button)findViewById(R.id.toDoButton);
		repairButton.setOnClickListener(handler);
		toDoButton.setOnClickListener(handler);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	

}
