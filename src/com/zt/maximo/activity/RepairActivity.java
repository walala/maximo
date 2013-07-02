package com.zt.maximo.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.zt.maximo.R;
import com.zt.maximo.R.layout;
import com.zt.maximo.R.menu;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class RepairActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_repair);
		SimpleAdapter adapter = new SimpleAdapter(this, getData(), R.layout.row4list_repair, new String[]{"title","info","img"}, new int[]{R.id.title,R.id.info,R.id.img});
		ListView listView = (ListView)findViewById(R.id.repairList);
		listView.setAdapter(adapter);
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.repair, menu);
		return true;
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
        list.add(map);
         
        return list;
    }

}
