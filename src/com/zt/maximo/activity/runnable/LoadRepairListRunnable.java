package com.zt.maximo.activity.runnable;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

public class LoadRepairListRunnable implements Runnable {
	
	private Handler handler;
	private Context context;
	
	public LoadRepairListRunnable(Handler handler, Context context){
		this.handler = handler;
		this.context = context;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		//do something...
		Message msg = new Message();
		msg.what = 0;
		Bundle bundle = new Bundle();
		bundle.putSerializable("aaa", "1111");
		bundle.putSerializable("bbb", "2222");
		msg.setData(bundle);
		handler.sendMessage(msg);
		
	}

}
