package com.zt.maximo.activity.handler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HandshakeCompletedListener;

import com.zt.maximo.R;
import com.zt.maximo.activity.BaseActivity;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.widget.ListView;
import android.widget.SimpleAdapter;


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
			SimpleAdapter adapter = new SimpleAdapter(activity, getData(), R.layout.row4list_repair, new String[]{"title","info","img"}, new int[]{R.id.title,R.id.info,R.id.img});
			ListView listView = (ListView)activity.findViewById(R.id.repairList);
			listView.setAdapter(adapter);
		}
	}
	
	private List<Map<String, Object>> getData() {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
 
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("title", "G1");
        map.put("info", "google 1");
        map.put("img", R.drawable.ic_launcher);
        list.add(map);
 
        map = new HashMap<String, Object>();
        map.put("title", "G2");
        map.put("info", "google 2");
        map.put("img", R.drawable.ic_launcher);
        list.add(map);
 
        map = new HashMap<String, Object>();
        map.put("title", "G3");
        map.put("info", "google 3");
        map.put("img", R.drawable.ic_launcher);
        
        map.put("title", "G4");
        map.put("info", "google 4");
        map.put("img", R.drawable.ic_launcher);
        list.add(map);
 
        map = new HashMap<String, Object>();
        map.put("title", "G5");
        map.put("info", "google 5");
        map.put("img", R.drawable.ic_launcher);
        list.add(map);
 
        map = new HashMap<String, Object>();
        map.put("title", "G6");
        map.put("info", "google 6");
        map.put("img", R.drawable.ic_launcher);
        list.add(map);
        
        map.put("title", "G7");
        map.put("info", "google 7");
        map.put("img", R.drawable.ic_launcher);
        list.add(map);
 
        map = new HashMap<String, Object>();
        map.put("title", "G8");
        map.put("info", "google 8");
        map.put("img", R.drawable.ic_launcher);
        list.add(map);
 
        map = new HashMap<String, Object>();
        map.put("title", "G9");
        map.put("info", "google 9");
        map.put("img", R.drawable.ic_launcher);
        list.add(map);
         
        return list;
    }

}
