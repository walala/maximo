package com.zt.maximo.activity.handler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HandshakeCompletedListener;

import com.zt.maximo.R;
import com.zt.maximo.activity.BaseActivity;
import com.zt.maximo.activity.LoginActivity;
import com.zt.maximo.activity.RepairActivity;
import com.zt.maximo.activity.runnable.LoadRepairListRunnable;
import com.zt.maximo.service.domain.AppProxyResultDo;
import com.zt.maximo.service.domain.ProxyRepairDo;
import com.zt.maximo.util.DialogUtil;
import com.zt.maximo.util.JsonUtil;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.widget.ListView;
import android.widget.SimpleAdapter;


public class RepairHandler extends Handler{
	
	private RepairActivity activity;
	
	public RepairHandler(RepairActivity activity){
		this.activity =  activity;
	}

	@Override
	public void handleMessage(Message msg) {
		// TODO Auto-generated method stub
		super.handleMessage(msg);
		if(msg.what == 0){
			AppProxyResultDo aprd = (AppProxyResultDo)msg.getData().getSerializable(LoadRepairListRunnable.LOAD_REPAIR_LIST);
			if(aprd.isError()){
				activity.showAlertDialog("亲~", "网络异常,请稍后再试喔");
			}else{
				List<ProxyRepairDo> result = JsonUtil.Json2List(aprd.getResut().toString(), ProxyRepairDo.class);
				if(null == result || result.size() == 0){
					//提示
				}else{
					//更新数据库
					
					//更新UI
					SimpleAdapter adapter = new SimpleAdapter(activity, getData(result), R.layout.row4list_repair, new String[]{"title","info","img"}, new int[]{R.id.title,R.id.info,R.id.img});
					ListView listView = (ListView)activity.findViewById(R.id.repairList);
					listView.setAdapter(adapter);
				}
			}
		}
	}
	
	private List<Map<String, Object>> getData(List<ProxyRepairDo> result) {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
 
        for(int i = 0; i < result.size(); i++){
        	Map<String, Object> map = new HashMap<String, Object>();
        	map.put("title", result.get(i).getTitle());
        	map.put("info", result.get(i).getInfo());
        	map.put("img", R.drawable.ic_launcher);
        	list.add(map);
        }
         
        return list;
    }

}
