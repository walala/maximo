package com.zt.maximo.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.cookie.Cookie;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

public class HttpUtil {
	
	private static DefaultHttpClient simpleHttpClient = null;
	
	private final static String DEFAULT_CHARSET = HTTP.UTF_8;
	private final static int CONNECT_TIME_OUT = 5*1000;
	private final static int READ_TIME_OUT = 40*1000;
	
	private static void init(){
		if (null==simpleHttpClient) {
			HttpParams params = new BasicHttpParams();
			HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
			HttpProtocolParams.setContentCharset(params, DEFAULT_CHARSET);
			HttpProtocolParams.setUseExpectContinue(params, true);
			ConnManagerParams.setTimeout(params, CONNECT_TIME_OUT);
			//通信读超时时间
			HttpConnectionParams.setSoTimeout(params, READ_TIME_OUT);
			//通信连接超时时间
			HttpConnectionParams.setConnectionTimeout(params, CONNECT_TIME_OUT);
			SchemeRegistry schReg = new SchemeRegistry();
			schReg.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
//			schReg.register(new Scheme("https", SSLSocketFactory.getSocketFactory(), 443));
			ClientConnectionManager connectionManager = new ThreadSafeClientConnManager(params, schReg);
			simpleHttpClient = new DefaultHttpClient(connectionManager,params);
		}
	}
	
	public static String post(String url,Map<String, String> args){
		return post(url, args,HTTP.UTF_8);
	}
	
	public static String post(String url,Map<String, String> args,String charset){
		if (StringUtil.isBlank(url)) {
			return null;
		}
		if ((!url.startsWith("http://"))) {
			url = "http://"+url;
		}
		init();
		HttpPost postMethod = initPostMethod(url, args, charset);
		if(null==postMethod){
			return null;
		}
		try {
			HttpResponse response = simpleHttpClient.execute(postMethod);
			CookieStore cookie = simpleHttpClient.getCookieStore();
			List<Cookie> cookies = cookie.getCookies();
            if (cookies.isEmpty()) {
                Log.i("cookies", "empty");
            } else {
                for (int i = 0; i < cookies.size(); i++) {
                    Log.i("cookie"+i, cookies.get(i).toString());
                }
            }
			if (response.getStatusLine().getStatusCode()!=HttpStatus.SC_OK) {
				return null;
			}
			HttpEntity entity = response.getEntity();
			return entity==null?null:EntityUtils.toString(entity,DEFAULT_CHARSET);//获取返回
		} catch (ClientProtocolException e) {
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		} catch (Throwable e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static String post(String url, Map<String, String> param, File file){
		try{
			if (StringUtil.isBlank(url)) {
				return null;
			}
			if ((!url.startsWith("http://"))) {
				url = "http://"+url;
			}
			init();
			HttpPost post = new HttpPost(url);
			MultipartEntity entity = new MultipartEntity();
			if (param != null && !param.isEmpty()) {
				for (Map.Entry<String, String> entry : param.entrySet()) {
					entity.addPart(entry.getKey(), new StringBody(entry.getValue()));
				}
			}
			if (file != null && file.exists()) {
				entity.addPart("file", new FileBody(file));
			}
			post.setEntity(entity);
			HttpResponse response = simpleHttpClient.execute(post);
			int stateCode = response.getStatusLine().getStatusCode();
			if (stateCode != HttpStatus.SC_OK) {
				return null;
			}
			return entity==null?null:EntityUtils.toString(response.getEntity(),DEFAULT_CHARSET);
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}
	
	private static HttpPost initPostMethod(String url,Map<String, String> args,String charset){
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		if (null!=args) {
			Iterator<String> iter = args.keySet().iterator();
			while (iter.hasNext()) {
				String key = iter.next();
				params.add(new BasicNameValuePair(key, args.get(key)));
			}
		}
		try {
			UrlEncodedFormEntity entity = new UrlEncodedFormEntity(params,DEFAULT_CHARSET);
			HttpPost post = new HttpPost(url);
			post.setEntity(entity);
			return post;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static String downloadBinaryWithDir(String url,String dir){
		if (StringUtil.isBlank(url)||url.indexOf(".")<0) {
			return null;
		}
		String fileSuffix = url.substring(url.lastIndexOf("."), url.length());
		HttpURLConnection conn = null;
		InputStream is = null;
		OutputStream os = null;
		try {
			URL httpUrl = new URL(url);
			conn = (HttpURLConnection) httpUrl.openConnection();
			conn.setConnectTimeout(CONNECT_TIME_OUT);
			conn.setReadTimeout(READ_TIME_OUT);
			conn.setDoInput(true);
			conn.connect();
			is = conn.getInputStream();
			File dirFile = new File(dir);
			if (!dirFile.exists()) {
				dirFile.mkdirs();
			}
			String filePath = dir+System.currentTimeMillis()+fileSuffix;
			File file = new File(filePath);
			Log.d("HttpUtil", "DestFileUri:"+filePath);
			file.createNewFile();
			os = new FileOutputStream(file);
			byte[] bytes = new byte[1024];
			int len = 0;
			while((len=is.read(bytes))!=-1){
				os.write(bytes, 0, len);
			}
			return file.getAbsolutePath();
		}  catch (Throwable e) {
			e.printStackTrace();
			return null;
		} finally{
			if (null!=is) {
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (null!=conn) {
				conn.disconnect();
			}
			if (null!=os) {
				try {
					os.flush();
					os.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				
			}
		}
	}
	
	public static Bitmap loadBitmap(String url){
		HttpURLConnection conn = null;
		try {
			URL httpUrl = new URL(url);
			conn = (HttpURLConnection) httpUrl.openConnection();
			conn.setConnectTimeout(CONNECT_TIME_OUT);
			conn.setReadTimeout(READ_TIME_OUT);
			conn.setDoInput(true);
			conn.connect();
			InputStream input = conn.getInputStream();
			Bitmap bitmap = BitmapFactory.decodeStream(input);
			return bitmap;
		}  catch (Throwable e) {
			return null;
		} finally{
			if (null!=conn) {
				conn.disconnect();
			}
		}
	}
}
