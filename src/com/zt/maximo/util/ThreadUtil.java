package com.zt.maximo.util;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.util.Log;

public class ThreadUtil {
	private static ExecutorService threadCache = null;
	private static final int MAX_THREAD_COUNT = 3;
	
	private synchronized static void init(){
		if(null == threadCache){
			threadCache = Executors.newFixedThreadPool(MAX_THREAD_COUNT); 
		}
	}
	
	public static void execute(Runnable runnable){
		Log.d("ThreadUtil", "add runnable:" + runnable.getClass().getName());
		if(null == threadCache){
			init();
		}
		threadCache.submit(runnable);
	}

}
