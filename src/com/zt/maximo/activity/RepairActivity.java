package com.zt.maximo.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.zt.maximo.R;

import android.os.Bundle;
import android.view.Menu;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class RepairActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_repair);
		/*SimpleAdapter adapter = new SimpleAdapter(this, getData(), R.layout.row4list_repair, new String[]{"title","info","img"}, new int[]{R.id.title,R.id.info,R.id.img});
		ListView listView = (ListView)findViewById(R.id.repairList);
		listView.setAdapter(adapter);*/
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.repair, menu);
		return true;
	}
}
