package com.zt.maximo.activity.handler;

import javax.net.ssl.HandshakeCompletedListener;

import com.zt.maximo.R;
import com.zt.maximo.activity.BaseActivity;

import android.content.Context;
import android.os.Handler;
import android.os.Message;


public class RepairHandler extends Handler{
	
	private BaseActivity activity;
	
	public RepairHandler(BaseActivity activity){
		this.activity =  activity;
	}

	@Override
	public void handleMessage(Message msg) {
		// TODO Auto-generated method stub
		super.handleMessage(msg);
		if(msg.what == 0){
			activity.setContentView(R.layout.activity_repair);
		}
	}

}
