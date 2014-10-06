package com.standouter.standouternew;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EncodingUtils;
import org.json.JSONException;
import org.json.JSONObject;

import cn.jpush.android.api.JPushInterface;

import com.facebook.Session;
import com.googlecode.javacv.FFmpegFrameRecorder;

import android.app.ActivityManager;
import android.app.Application;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.tencent.mm.sdk.openapi.WXTextObject;


public class Standouter extends Application {
	public String webaddress;
	public int width;
	public int height;
	public DefaultHttpClient httpclient;
	public final int loginactivitycode = 1;
	private String facebooktoken = null;
	public final String shareweb = "http://www.standouter.com";
	public final String pcweb = "http://www.standouter.com";

	private String selfid = null;
	private Boolean rerording;
	Boolean memCache;
	Boolean fileCache;
	private FFmpegFrameRecorder recorder;

	private String categoria;

	private JSONObject briefjson;

	public String contestname;
	private Boolean rerordingbegin;
	
	private Handler handler = null; 
	
	
	private static final String WECHET_ID="wx49bf6d07bcf652b6";
	private IWXAPI api;
	private void regToWx(){
		api=WXAPIFactory.createWXAPI(this, WECHET_ID,true);
		api.registerApp(WECHET_ID);
	}
	
	public IWXAPI getWechatApi(){
		return this.api;
	}
	
	// set方法  
	public void setHandler(Handler handler) {  
	this.handler = handler;  
	}  
	  
	// get方法  
	public Handler getHandler() {  
	return handler;  
	}  

	@Override
	public void onCreate() {
		super.onCreate();
		JPushInterface.setDebugMode(true);
		JPushInterface.init(this);
		regToWx();
		
	}

	public void setcategoria(String categoria) {
		this.categoria = categoria;
	}

	public String getcategoria() {
		return this.categoria;
	}

	public void setbriefjson(JSONObject briefstring) {
		this.briefjson = briefstring;
	}

	public JSONObject getbriefjson() {
		return this.briefjson;
	}

	public void setrecordnull() {
		recorder = null;

	}

	public FFmpegFrameRecorder getrecorder() {
		return this.recorder;
	}

	public void setrecoder(FFmpegFrameRecorder recorder,
			int sampleAudioRateInHz, int frameRate) {

		this.recorder = recorder;
		this.recorder.setFormat("mp4");
		this.recorder.setVideoQuality(1);
		Log.v("", "recorder.setFormat(\"mp4\")");

		this.recorder.setSampleRate(sampleAudioRateInHz);
		Log.v("", "recorder.setSampleRate(sampleAudioRateInHz)");

		// re-set in the surface changed method as well
		recorder.setFrameRate(frameRate);
	}

	public Boolean getrecording() {
		return this.rerording;
	}

	public void setrecording(Boolean recording) {
		this.rerording = recording;
	}

	public Boolean getrecordingbegin() {
		return this.rerordingbegin;
	}

	public void setrecordingbegin(Boolean recordingbegin) {
		this.rerordingbegin = recordingbegin;
	}

	public void callFacebookLogout(Context context) {
		SharedPreferences sharedPreference = context.getSharedPreferences(
				"userinfo", MODE_PRIVATE);
		// 获得一个Editor对象，进行数据保存
		Editor myed = sharedPreference.edit();
		// 设置相应保存的数据，（一般保存一下应用程序的配置信息）
		// 使用和Map方式一样的
		myed.clear();
		// 提交操作（将操作的数据进行保存到userinfo.xml中）
		myed.commit();
		Session session = Session.getActiveSession();

		if (session != null) {

			if (!session.isClosed()) {
				session.closeAndClearTokenInformation();
				// clear your preferences if saved
			}
		} else {

			session = new Session(context);
			Session.setActiveSession(session);

			session.closeAndClearTokenInformation();
			// clear your preferences if saved

		}

		setfacebooktoken(null);
	}

	public String getfacebooktoken() {
		return this.facebooktoken;
	}

	public void setfacebooktoken(String fbtoken) {
		this.facebooktoken = fbtoken;
	}

	public String getselfid() {
		return this.selfid;
	}

	public void setselfid(String selfid) {
		this.selfid = selfid;
	}

	public String getcontestname() {
		return contestname;
	}

	public void setcontestname(String contestname) {
		this.contestname = contestname;
	}

	public HttpClient gethttpclient() {
		return httpclient;
	}

	public void sethttpclient(DefaultHttpClient httpclient) {
		this.httpclient = httpclient;
	}

	public Boolean getfileCache() {
		return fileCache;
	}

	public void setfileCache(Boolean fileCache) {
		this.fileCache = fileCache;
	}

	public Boolean getmemCache() {
		return memCache;
	}

	public void setememCache(Boolean memCache) {
		this.memCache = memCache;
	}

	public String getwebaddress() {
		return webaddress;
	}

	public void setwebaddress(String webaddress) {
		this.webaddress = webaddress;
	}

	public int getwidth() {
		return width;
	}

	public void setvidth(int vidth) {
		this.width = vidth;
	}

	public int getheight() {
		return height;
	}

	public void setheight(int height) {
		this.height = height;
	}

	// 读SD中的文件
	public String readFileSdcardFile(String fileName) throws IOException {
		String res = "";
		try {
			FileInputStream fin = new FileInputStream(fileName);

			int length = fin.available();

			byte[] buffer = new byte[length];
			fin.read(buffer);

			res = EncodingUtils.getString(buffer, "UTF-8");

			fin.close();
		}

		catch (Exception e) {
			e.printStackTrace();
		}
		return res;
	}

	public void writeFileSdcardFile(String fileName, String write_str)
			throws IOException {
		try {

			FileOutputStream fout = new FileOutputStream(fileName);
			byte[] bytes = write_str.getBytes();

			fout.write(bytes);
			fout.close();
		}

		catch (Exception e) {
			e.printStackTrace();
		}
	}

	public JSONObject getJson(String url, HttpClient httpclient)
			throws IOException {

		InputStream is = null;
		String result = "";
		JSONObject jsonObject = null;
		HttpGet httpget = null;
		HttpEntity entity;
		// HTTP
		try {
			// HttpClient httpclient = new DefaultHttpClient(); // for port 80
			// requests!
			httpget = new HttpGet(url);
			if (new File("/sdcard/header9.txt").exists()
					&& new File("/sdcard/header8.txt").exists()) {
				String xz = null;
				String yz = null;

				try {
					xz = "JSESSIONID="
							+ readFileSdcardFile("/sdcard/header9.txt");
					httpget.addHeader("Cookie", xz);
					yz = "SPRING_SECURITY_REMEMBER_ME_COOKIE="
							+ readFileSdcardFile("/sdcard/header8.txt");
					httpget.addHeader("Cookie", yz);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}// 第二次访问拿着JSESSIONID就可以了

				Log.i("JSESSIONID", xz + "::2");
				Log.i("JSESSIONID", yz + "::2");
			}
			HttpResponse response = httpclient.execute(httpget);
			entity = response.getEntity();
			is = entity.getContent();

		} catch (Exception e) {
			e.printStackTrace();

			return null;
		}

		// Read response to string
		try {

			BufferedReader reader = new BufferedReader(new InputStreamReader(
					is, "utf-8"), 8);
			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
			is.close();
			result = sb.toString();
		} catch (Exception e) {
			e.printStackTrace();

			return null;
		}

		// Convert string to object
		try {
			jsonObject = new JSONObject(result);
		} catch (JSONException e) {
			e.printStackTrace();

			return null;
		}

		httpget.abort();

		return jsonObject;

	}
	
	public JSONObject getJsonpost(String url, HttpClient httpclient,
			List<NameValuePair> nameValuePairs) throws IOException {

		InputStream is = null;
		String result = "";
		JSONObject jsonObject = null;
		HttpPost httppost = null;
		Log.i("", url);
		// HTTP
		try {
			// HttpClient httpclient = new DefaultHttpClient(); // for port 80
			// requests!
			httppost = new HttpPost(url);

			if (new File("/sdcard/header9.txt").exists()
					&& new File("/sdcard/header8.txt").exists()) {
				String xz = null;
				String yz = null;

				try {
					xz = "JSESSIONID="
							+ readFileSdcardFile("/sdcard/header9.txt");
					httppost.addHeader("Cookie", xz);
					yz = "SPRING_SECURITY_REMEMBER_ME_COOKIE="
							+ readFileSdcardFile("/sdcard/header8.txt");
					httppost.addHeader("Cookie", yz);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}// 第二次访问拿着JSESSIONID就可以了

				Log.i("JSESSIONID", xz + "::2");
				Log.i("JSESSIONID", yz + "::2");
			}
			/*
			 * HttpParams params = new BasicHttpParams();// params = new
			 * BasicHttpParams();
			 * HttpConnectionParams.setConnectionTimeout(params, 8000); //连接超时
			 * HttpConnectionParams.setSoTimeout(params, 5000); //响应超时
			 * httppost.setParams(params);
			 */
			/**************/
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));// 加入数据

			HttpResponse response = httpclient.execute(httppost);
			HttpEntity entity = response.getEntity();
			is = entity.getContent();
			/*
			 * if (entity != null) { entity.consumeContent();
			 * 
			 * }
			 */
		} catch (Exception e) {
			Log.i("", e.toString());
			return null;
		}

		// Read response to string
		try {

			BufferedReader reader = new BufferedReader(new InputStreamReader(
					is, "utf-8"), 8);
			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
			is.close();
			result = sb.toString();
		} catch (Exception e) {
			return null;
		}

		// Convert string to object
		try {
			jsonObject = new JSONObject(result);
		} catch (JSONException e) {
			return null;
		}

		httppost.abort();
		return jsonObject;

	}

	void getcookiestandouter() {
		String TAG = "";
		List<Cookie> cookies = httpclient.getCookieStore().getCookies();
		Cookie cookie = null;
		if (cookies.isEmpty()) {
			Log.i(TAG, "-------Cookie NONE---------");
		} else {
			for (int i = 0; i < cookies.size(); i++) {
				// 保存cookie
				cookie = cookies.get(i);

				Log.i(TAG, cookies.get(i).getName() + "="
						+ cookies.get(i).getValue());
				if ("JSESSIONID".equals(cookies.get(i).getName())) {
					String JSESSIONID = cookies.get(i).getValue();
					SharedPreferences sp = this.getSharedPreferences(
							"app_settings", Context.MODE_PRIVATE);

					Editor editor = sp.edit();
					editor.putString("JSESSIONID", JSESSIONID);
					editor.commit();
					Log.i("jszz", "JSESSIONID=" + cookies.get(i).getValue());
					try {
						writeFileSdcardFile("/sdcard/header9.txt",
								cookies.get(i).getValue());
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				if ("SPRING_SECURITY_REMEMBER_ME_COOKIE".equals(cookies.get(i)
						.getName())) {
					String SPRING_SECURITY_REMEMBER_ME_COOKIE = cookies.get(i)
							.getValue();
					SharedPreferences sp2 = this.getSharedPreferences(
							"app_settings", Context.MODE_PRIVATE);

					Editor editor2 = sp2.edit();
					editor2.putString("SPRING_SECURITY_REMEMBER_ME_COOKIE",
							SPRING_SECURITY_REMEMBER_ME_COOKIE);
					editor2.commit();
					Log.i("jszz", "SPRING_SECURITY_REMEMBER_ME_COOKIE="
							+ cookies.get(i).getValue());
					try {
						writeFileSdcardFile("/sdcard/header8.txt",
								cookies.get(i).getValue());
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

			}
		}
	}

	public String getHtmlByGet(String url, HttpClient hc) {

		InputStream is = null;
		String result = "";
		HttpGet httpget = null;
		// HTTP
		try {
			// HttpClient httpclient = new DefaultHttpClient(); // for port 80
			// requests!
			httpget = new HttpGet(url);
			if (new File("/sdcard/header9.txt").exists()
					&& new File("/sdcard/header8.txt").exists()) {
				String xz = null;
				String yz = null;

				try {
					xz = "JSESSIONID="
							+ readFileSdcardFile("/sdcard/header9.txt");
					httpget.addHeader("Cookie", xz);
					yz = "SPRING_SECURITY_REMEMBER_ME_COOKIE="
							+ readFileSdcardFile("/sdcard/header8.txt");
					httpget.addHeader("Cookie", yz);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}// 第二次访问拿着JSESSIONID就可以了

				Log.i("JSESSIONID", xz + "::2");
				Log.i("JSESSIONID", yz + "::2");
			}
			HttpResponse response = hc.execute(httpget);
			HttpEntity entity = response.getEntity();
			is = entity.getContent();

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

		// Read response to string
		try {

			BufferedReader reader = new BufferedReader(new InputStreamReader(
					is, "utf-8"), 8);
			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
			is.close();
			result = sb.toString();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		httpget.abort();
		return result;
	}
	
	
	
}