package com.zt.maximo.activity.runnable;

import com.zt.maximo.service.RepairService;
import com.zt.maximo.service.domain.AppProxyResultDo;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

public class LoadRepairListRunnable implements Runnable {
	
	public static final String LOAD_REPAIR_LIST = "loadRepairList";
	private Handler handler;
	
	public LoadRepairListRunnable(Handler handler){
		this.handler = handler;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		//获取工单列表结果集
		AppProxyResultDo aprd = RepairService.getInstance().uploadRepairList();
		Message msg = new Message();
		msg.what = 0;
		Bundle bundle = new Bundle();
		bundle.putSerializable(LOAD_REPAIR_LIST, aprd);
		msg.setData(bundle);
		handler.sendMessage(msg);
		
	}

}
