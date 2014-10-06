package com.standouter.standouternew;

import java.io.IOException;

import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import com.standouter.standouternew.R;
import com.standouter.standouternew.R.anim;
import com.standouter.standouternew.R.layout;
import com.standouter.standouternew.R.menu;

import cn.jpush.android.api.InstrumentedActivity;
import cn.jpush.android.api.JPushInterface;


import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.PixelFormat;
import android.view.Menu;
import android.view.Window;

public class SplashActivity extends InstrumentedActivity {

	private Standouter qapp;
	private DefaultHttpClient httpclient;
	protected JSONObject channelJson;
	protected JSONObject contestJson;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		/********************************************************************/
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		//setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);//
		//getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);//for full screnn
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//
		getWindow().setFormat(PixelFormat.TRANSLUCENT);
		setContentView(R.layout.activity_splash);
		
		JPushInterface.setDebugMode(true);
        JPushInterface.init(this);
		qapp=(Standouter)getApplication();
		httpclient = new DefaultHttpClient(); // for port 80 requests!

		qapp.sethttpclient(httpclient);
		qapp.setwebaddress("http://apps.standouter.com");
		 Thread getjsonthread=new Thread(getjsonun);
		 getjsonthread.start();
	}
	Runnable getjsonun=new Runnable(){

		
		@Override
		public void run() {
			// TODO Auto-generated method stub
			try {
				contestJson=qapp.getJson(qapp.getwebaddress()+"/contest/contestlist?ps=25",qapp.httpclient );
			} catch (IOException e) {
				// TODO Auto-generated catch block
				 Message msg = new Message();
				  msg.what=1;
				  errorhandler.sendMessage(msg);
				  return;
			}
			if(contestJson==null){
				 Message msg = new Message();
				  msg.what=1;
				  errorhandler.sendMessage(msg);
				  return;

			}
			try {
				qapp.writeFileSdcardFile("/sdcard/contestjson.txt", contestJson.toString());

			} catch (IOException e) {
				// TODO Auto-generated catch block
				 Message msg = new Message();
				  msg.what=1;
				  errorhandler.sendMessage(msg);
				  return;

			}
			try {
				channelJson=qapp.getJson(qapp.getwebaddress()+"/contest/channellist?ps=25", qapp.httpclient);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				 Message msg = new Message();
				  msg.what=1;
				  errorhandler.sendMessage(msg);
				  return;
			}
			if(contestJson==null){
				 Message msg = new Message();
				  msg.what=1;
				  errorhandler.sendMessage(msg);
				  return;

			}
			try {
				qapp.writeFileSdcardFile("/sdcard/channeljson.txt", channelJson.toString());

			} catch (IOException e) {
				// TODO Auto-generated catch block
				 Message msg = new Message();
				  msg.what=1;
				  errorhandler.sendMessage(msg);
				  return;

			}
			
			  Message msg = new Message();
			  msg.what=1;
			  gotomainhandler.sendMessage(msg);
		}
		
		
		
	};
	
	private Handler errorhandler=new Handler(){
		 public void handleMessage(Message msg) {
	            super.handleMessage(msg);
	            appexit();
	            
		 }
	};

	private Handler gotomainhandler=new Handler(){
		 public void handleMessage(Message msg) {
	            super.handleMessage(msg);
	            Intent intent = new Intent(SplashActivity.this, MainActivity.class);
	            SplashActivity.this.startActivity(intent);
	            SplashActivity.this.finish();
	            overridePendingTransition(R.anim.activity_open,R.anim.activity_close);
	            
		 }
	};
	private void appexit(){
		String message="Sorry, you should connect the Internet to use this APP!";
		new AlertDialog.Builder(SplashActivity.this)
	    .setTitle("LOGIN")
	    .setMessage(message)
	    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
	        public void onClick(DialogInterface dialog, int which) { 
	            // continue with delete
	        	dialog.dismiss();
	        	  System.exit(0);
	        }
	     })
	    
	     .show();
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.splash, menu);
		return true;
	}
	
	@Override
    protected void onResume() {
        super.onResume();
        //JPushInterface.onResume(this);
    }
    @Override
    protected void onPause() {
        super.onPause();
       // JPushInterface.onPause(this);
    }

}