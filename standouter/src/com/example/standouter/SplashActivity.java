package com.example.standouter;

import java.io.File;
import java.io.IOException;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import com.squareup.picasso.Picasso;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;

/**
 * 
 * @{#} SplashActivity.java Create on 2013-5-2 下午9:10:01    
 *    
 * class desc:   启动画面
 *
 * <p>Copyright: Copyright(c) 2013 </p> 
 * @Version 1.0
 * @Author <a href="mailto:gaolei_xj@163.com">Leo</a>   
 *  
 *
 */


public class SplashActivity extends Activity {

    //延迟3秒 
    private  long SPLASH_DELAY_MILLIS = 1000;
	static DefaultHttpClient httpclient;
	public static JSONObject jsonobject;
	
	public String website;
	public static String  result;  
	@Override
    protected void onCreate(Bundle savedInstanceState) {
		/********************************************************************/
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		//setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);//
		//getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);//for full screnn
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//
		getWindow().setFormat(PixelFormat.TRANSLUCENT);
		/********************************************************************/
		 
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.splash);
       // System.gc();

        int widthz = this.getWindowManager().getDefaultDisplay().getWidth();
	     int heiz = this.getWindowManager().getDefaultDisplay().getHeight();
	     
        website= "http://demostandouter.zerouno.it";
        ImageView loginiv=(ImageView)this.findViewById(R.id.splash);
        RelativeLayout.LayoutParams a=new RelativeLayout.LayoutParams( widthz,widthz/4
                );
        a.topMargin=heiz/3;
        loginiv.setLayoutParams(a);
        
		Picasso.with(this).load(R.drawable.standouterheader). into(loginiv);

        if (httpclient==null){
    		httpclient = new DefaultHttpClient(); // for port 80 requests!
    		
    		}
        
        //Picasso.with(this).load("http://i.imgur.com/DvpvklR.png").into(loginiv);
      
       // jsonobject = Json.getJson(MainActivity.website+"/video/search?ss=global&so=most_voted",httpclient);

        // 使用Handler的postDelayed方法，3秒后执行跳转到MainActivity 
        new Handler().postDelayed(new Runnable() {
            public void run() {
                goHome();
            }
        }, SPLASH_DELAY_MILLIS);
    }

    private void goHome() {
    	String path="/sdcard/json.txt";
        File f = new File(path);
        
            try {
            	if(!f.exists()){
               jsonobject = Json.getJson(website+"/video/search?ss=global&so=most_voted",httpclient);
				Login.writeFileSdcardFile(path, jsonobject.toString());
            	}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
           path="/sdcard/json2.txt";
            f = new File(path);
            
                try {
                	if(!f.exists()){
                		  jsonobject = Json.getJson(website+"/contest/contestinfo?cc=oldwildwest",httpclient);
          				Login.writeFileSdcardFile(path, jsonobject.toString());
                	}
    			} catch (IOException e) {
    				// TODO Auto-generated catch block
    				e.printStackTrace();
    			}
                
                
                
                
                JSONObject jsonlateoginst = Json.getJson(website+"/login_status.json",httpclient);
                result =jsonlateoginst.toString();
					//Log.i("zzzzzzzzzzzz",Json.getJson("https://graph.facebook.com/me?access_token="+Login.AT+"&method=GET&fields=picture.type(large)").getJSONObject("picture").getJSONObject("data").getString("url"));
					//Log.i("zz",Json.getJson(website+"/fbaccess?accessToken="+Login.AT,httpclient).toString());
					Log.i("","no login");
                
					Intent i_getvalue = getIntent();  
					String action = i_getvalue.getAction();  
					  
					if(Intent.ACTION_VIEW.equals(action)){  
					    Uri uri = i_getvalue.getData();  
					    if(uri != null){  
					        String name = uri.getQueryParameter("name");  
					        String age= uri.getQueryParameter("age"); 
					        Log.i("name",name);
					        Log.i("age",age);

					    }  
					}
      	        
      	        
      	        
      	        
      	        
            
        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
        SplashActivity.this.startActivity(intent);
       // System.gc();
        SplashActivity.this.finish();
        overridePendingTransition(R.anim.activity_open,R.anim.activity_close);
    }
}
