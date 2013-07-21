package com.zt.maximo.service;

import java.util.HashMap;
import java.util.Map;

import com.zt.maximo.F;
import com.zt.maximo.service.domain.AppProxyResultDo;

public class RepairService extends BaseService {

	private static RepairService instance;

	public static RepairService getInstance() {
		if (null == instance) {
			instance = new RepairService();
		}
		return instance;
	}
	
	public AppProxyResultDo uploadRepairList(){
		Map<String, String> map = new HashMap<String, String>();
		map.put("uid", F.user.getUid() + "");
		return execute(uploadRepairList, map);
	}

}
