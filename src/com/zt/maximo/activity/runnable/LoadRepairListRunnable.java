package com.zt.maximo.activity.runnable;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

public class LoadRepairListRunnable implements Runnable {
	
	private Handler handler;
	
	public LoadRepairListRunnable(Handler handler){
		this.handler = handler;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		//do something...
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Message msg = new Message();
		msg.what = 0;
		Bundle bundle = new Bundle();
		bundle.putSerializable("aaa", "1111");
		bundle.putSerializable("bbb", "2222");
		msg.setData(bundle);
		handler.sendMessage(msg);
		
	}

}
