package com.example.standouter;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;



public class Json {
	
	public static  JSONObject getJson(String url,HttpClient httpclient){
		
		InputStream is = null;
		String result = "";
		JSONObject jsonObject = null;
		
		// HTTP
		try {	    	
			//HttpClient httpclient = new DefaultHttpClient(); // for port 80 requests!
			HttpPost httppost = new HttpPost(url);
			if(new File("/sdcard/header9.txt").exists()&&new File("/sdcard/header8.txt").exists()){
			String xz = null;
			String yz=null;
			
			try {
            	xz="JSESSIONID="+ Login.readFileSdcardFile("/sdcard/header9.txt");
				httppost.addHeader("Cookie", xz);
				yz = "SPRING_SECURITY_REMEMBER_ME_COOKIE="+ Login.readFileSdcardFile("/sdcard/header8.txt");
				httppost.addHeader("Cookie", yz);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}//第二次访问拿着JSESSIONID就可以了
			
			Log.i("JSESSIONID",  xz+ "::2"); 
			Log.i("JSESSIONID",  yz+ "::2"); 
			}
			HttpResponse response = httpclient.execute(httppost);
			HttpEntity entity = response.getEntity();
			is = entity.getContent();
		} catch(Exception e) {
			return null;
		}
	    
		// Read response to string
		try {	 
			
            
			BufferedReader reader = new BufferedReader(new InputStreamReader(is,"utf-8"),8);
			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
			is.close();
			result = sb.toString();	            
		} catch(Exception e) {
			return null;
		}
 
		// Convert string to object
		try {
			jsonObject = new JSONObject(result);            
		} catch(JSONException e) {
			return null;
		}
    
		return jsonObject;
 
	}
	
}