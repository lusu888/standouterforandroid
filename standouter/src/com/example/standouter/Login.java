package com.example.standouter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.cookie.Cookie;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EncodingUtils;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.model.GraphUser;
import com.facebook.widget.FacebookDialog;
import com.facebook.widget.LoginButton;
import com.squareup.picasso.Picasso;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.ActivityInfo;
import android.graphics.PixelFormat;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

public class Login extends Activity {

	
	private  final String TAG = "MainFragment";
	public static  Session session;
	public  GraphUser user;
	public  String username;
	private Runnable runnablelogins;

	public  boolean x=false;
	private  UiLifecycleHelper uiHelper;
    private Session.StatusCallback callback = new Session.StatusCallback() {
        @Override
        public void call(final Session session, final SessionState state, final Exception exception) {
            onSessionStateChange(session, state, exception);
        }
    };

	 static String AT;
	public  LoginButton loginButton;
	protected ProgressDialog pd;
	private String result;
	private String ps;
	private String un;
	
	//写数据到SD中的文件
	public static void writeFileSdcardFile(String fileName,String write_str) throws IOException{ 
	 try{ 

	       FileOutputStream fout = new FileOutputStream(fileName); 
	       byte [] bytes = write_str.getBytes(); 

	       fout.write(bytes); 
	       fout.close(); 
	     }

	      catch(Exception e){ 
	        e.printStackTrace(); 
	       } 
	   } 

	public void postData(String username,String password) {
	   
	    HttpPost httppost = new HttpPost(MainActivity.website+"/j_spring_security_check");
	    
	    if(new File("/sdcard/header9.txt").exists()&&new File("/sdcard/header8.txt").exists()){
			String xz = null;
			String yz=null;
			
			try {
            	xz="JSESSIONID="+ Login.readFileSdcardFile("/sdcard/header9.txt");
				httppost.addHeader("Cookie", xz);
				yz = "SPRING_SECURITY_REMEMBER_ME_COOKIE"+ Login.readFileSdcardFile("/sdcard/header8.txt");
				httppost.addHeader("Cookie", yz);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}//第二次访问拿着JSESSIONID就可以了
			
			Log.i("JSESSIONID",  xz+ "::2"); 
			Log.i("JSESSIONID",  yz+ "::2"); 
			}
	  
	  HttpParams params = new BasicHttpParams();// 
	  params = new BasicHttpParams();   
	      HttpConnectionParams.setConnectionTimeout(params, 8000);   //连接超时
	      HttpConnectionParams.setSoTimeout(params, 5000);   //响应超时
	  httppost.setParams(params);

	  
		  


	    try {
	        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(3);
	        nameValuePairs.add(new BasicNameValuePair("json_response","true"));
	        nameValuePairs.add(new BasicNameValuePair("j_username",  username));
	        nameValuePairs.add(new BasicNameValuePair("j_password", password));
	        httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

	        HttpResponse response = MainActivity.httpclient.execute(httppost);
	        HttpEntity resEntity = response.getEntity();
	         result=null;
	        if (resEntity != null) {    
                 result = EntityUtils.toString(resEntity);
            }
	        
	        Log.i("zz", "Response from server: " + result);
	        
	        
	        
	        
	        
	        
	        List<Cookie> cookies = MainActivity.httpclient.getCookieStore().getCookies();  
	        Cookie cookie = null;
	        if(cookies.isEmpty()) {     
	        	Log.i(TAG, "-------Cookie NONE---------");     
	        	} else {                    
	        		for (int i = 0; i < cookies.size(); i++ ) {     
	        			//保存cookie      
	        			cookie = cookies.get(i);   
	        			
	        			
	        			
	        			Log.i(TAG, cookies.get(i).getName() + "=" + cookies.get(i).getValue());  
	        			if ("JSESSIONID".equals(cookies.get(i).getName())) {
	        			String JSESSIONID = cookies.get(i).getValue();
	        			SharedPreferences sp=this.getSharedPreferences("app_settings", Context.MODE_PRIVATE);

	        			Editor editor = sp.edit();
	        			editor.putString("JSESSIONID", JSESSIONID);
	        			editor.commit();
	        			Log.i("jszz", "JSESSIONID=" + cookies.get(i).getValue());
	        			Login.writeFileSdcardFile("/sdcard/header9.txt", cookies.get(i).getValue());
	        			}
	        			if ("SPRING_SECURITY_REMEMBER_ME_COOKIE".equals(cookies.get(i).getName())) {
		        			String SPRING_SECURITY_REMEMBER_ME_COOKIE = cookies.get(i).getValue();
		        			SharedPreferences sp2=this.getSharedPreferences("app_settings", Context.MODE_PRIVATE);

		        			Editor editor2 = sp2.edit();
		        			editor2.putString("SPRING_SECURITY_REMEMBER_ME_COOKIE", SPRING_SECURITY_REMEMBER_ME_COOKIE);
		        			editor2.commit();
		        			Log.i("jszz", "SPRING_SECURITY_REMEMBER_ME_COOKIE=" + cookies.get(i).getValue());
		        			Login.writeFileSdcardFile("/sdcard/header8.txt", cookies.get(i).getValue());}
	        			
	        			
	        			
	        			}
	        	}
           


	        
	        Message message = new Message(); 
	    	  
	    	  message.what = 1; 
	    	  
	    	  loginhandler.sendMessage(message);
	        
			//Log.i("sd",Json.getJson(MainActivity.website+"/login_status.json",MainActivity.httpclient).toString());


	    } catch (ClientProtocolException e) {

	    } catch (IOException e) {

	    }
	    
	    
	} 
	Handler loginhandler = new Handler(){         
        public void handleMessage(Message msg) {  
        	pd.dismiss();
        	//Toast.makeText(Login.this, "已经保存您的数据！", Toast.LENGTH_LONG).show();
        	try {
				JSONObject jsonresult = new JSONObject(result);
				if(jsonresult.getString("authenticated").equals("true")){
					MainActivity.loginoperas();
				    finish();
				   overridePendingTransition(R.anim.activity_open,R.anim.activity_close);
				   x=true;
				   Toast toast = Toast.makeText(Login.this,
			                "Login Success!",
			                Toast.LENGTH_LONG);
			        toast.setGravity(Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 0);
			        toast.show();
			        
			       // final Account account = new Account(un, "com.google");
			       // AccountManager mAccountManager =  AccountManager.get(Login.this);;
					//mAccountManager.addAccountExplicitly(account, ps, null);
			        
				}
				else{
					AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
							Login.this);
			 
						// set title
						alertDialogBuilder.setTitle("Login");
			 
						// set dialog message
						alertDialogBuilder
							.setMessage("The username or the password is wrong, please try again!")
							.setCancelable(false)
							
							.setPositiveButton("OK",new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,int id) {
									// if this button is clicked, close
									// current activity
									dialog.cancel();
								}
							  })
							;
			 
							// create alert dialog
							AlertDialog alertDialog = alertDialogBuilder.create();
			 
							// show it
							alertDialog.show();
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
               }         
   };
	
public static String getHtmlByGet(String _url, HttpClient hc){    
       
        String result = "";
        
        try {
            
                        
            HttpGet get = new HttpGet(_url);
            if(new File("/sdcard/header9.txt").exists()&&new File("/sdcard/header8.txt").exists()){
    			String xz = null;
    			String yz=null;
    			
    			try {
                	xz="JSESSIONID="+ Login.readFileSdcardFile("/sdcard/header9.txt");
    				get.addHeader("Cookie", xz);
    				yz = "SPRING_SECURITY_REMEMBER_ME_COOKIE="+ Login.readFileSdcardFile("/sdcard/header8.txt");
    				get.addHeader("Cookie", yz);
    			} catch (IOException e1) {
    				// TODO Auto-generated catch block
    				e1.printStackTrace();
    			}//第二次访问拿着JSESSIONID就可以了
    			
    			Log.i("JSESSIONID",  xz+ "::2"); 
    			Log.i("JSESSIONID",  yz+ "::2"); 
    			}
            
            HttpResponse response =  hc.execute(get); 

            HttpEntity resEntity = response.getEntity();
            
            if (resEntity != null) {    
                result = EntityUtils.toString(resEntity);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } 
        return result;
 }
//读SD中的文件
	public static String readFileSdcardFile(String fileName) throws IOException{ 
	  String res=""; 
	  try{ 
	         FileInputStream fin = new FileInputStream(fileName); 

	         int length = fin.available(); 

	         byte [] buffer = new byte[length]; 
	         fin.read(buffer);     

	         res = EncodingUtils.getString(buffer, "UTF-8"); 

	         fin.close();     
	        } 

	        catch(Exception e){ 
	         e.printStackTrace(); 
	        } 
	        return res; 
	} 

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		/********************************************************************/
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		//getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);//for full screnn
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//
		getWindow().setFormat(PixelFormat.TRANSLUCENT);
		/********************************************************************/
		uiHelper = new UiLifecycleHelper(this, callback);
        uiHelper.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		loginButton = (LoginButton) findViewById(R.id.authButton);
	      
        loginButton.setUserInfoChangedCallback(new LoginButton.UserInfoChangedCallback() {
            @Override
            public void onUserInfoFetched(GraphUser user) {
            	Login.this.user = user;
           	
            	

            	
                
                
            }
        });
        
       
        final EditText usernameedit=(EditText)this.findViewById(R.id.usernameedit);
        final EditText passwordedit=(EditText)this.findViewById(R.id.Passwordedit);
        usernameedit.setLayoutParams( new LinearLayout.LayoutParams( (MainActivity.widthz-32)*3/4, (MainActivity.widthz-32)/8));
        passwordedit.setLayoutParams( new LinearLayout.LayoutParams(  (MainActivity.widthz-32)*3/4, (MainActivity.widthz-32)/8));
        runnablelogins=new Runnable(){

       		@Override
       		public void run() {
       			
       			// TODO Auto-generated method stub
       			postData(usernameedit.getText().toString(),passwordedit.getText().toString());
       			
       		}
           	   
        };
        ImageView logins=(ImageView)this.findViewById(R.id.logins);
        logins.setLayoutParams( new LinearLayout.LayoutParams(  (MainActivity.widthz-34)/4, (MainActivity.widthz-34)/4));
        Picasso.with(this).load(R.drawable.loginbutton). resize( (MainActivity.widthz-34)/4,  (MainActivity.widthz-34)/4). into(logins);
        logins.setScaleType(ImageView.ScaleType.FIT_XY);
        logins.setOnClickListener(new View.OnClickListener() {


			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				pd = ProgressDialog.show(Login.this, "Logining", "Logining，please wait……");  

				 new Thread(runnablelogins).start();

				
			}
		});
        LinearLayout loingstlayout=(LinearLayout)this.findViewById(R.id.loginslayout);
        Session session=Session.getActiveSession();
        if(MainActivity.LOGINSTATE==1){
        	loingstlayout.setVisibility(View.GONE);
        }else{
            loingstlayout.setVisibility(View.VISIBLE);
        }
         
	}
	 
	
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	    super.onActivityResult(requestCode, resultCode, data);

	    uiHelper.onActivityResult(requestCode, resultCode, data, new FacebookDialog.Callback() {
	        @Override
	        public void onError(FacebookDialog.PendingCall pendingCall, Exception error, Bundle data) {
	            Log.e("Activity", String.format("Error: %s", error.toString()));
	        }

	        @Override
	        public void onComplete(FacebookDialog.PendingCall pendingCall, Bundle data) {
	            Log.i("Activity", "Success!");
	        }
	    });
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.login, menu);
		return true;
	}
	
	public static void callFacebookLogout(Context context) {
		 SharedPreferences sharedPreference =  context.getSharedPreferences("userinfo", MODE_PRIVATE);
         //获得一个Editor对象，进行数据保存
         Editor myed = sharedPreference.edit();
         //设置相应保存的数据，（一般保存一下应用程序的配置信息）
         //使用和Map方式一样的
         myed.clear();
         //提交操作（将操作的数据进行保存到userinfo.xml中）
         myed.commit();
        Session session = Session.getActiveSession();
       
        
        if (session != null) {

            if (!session.isClosed()) {
                session.closeAndClearTokenInformation();
                Json.getJson(MainActivity.website+"/logout",MainActivity.httpclient);
                AT=null;
                //clear your preferences if saved
            }
        } else {

            session = new Session(context);
            Session.setActiveSession(session);
            Json.getJson(MainActivity.website+"/logout",MainActivity.httpclient);
            AT=null;

            session.closeAndClearTokenInformation();
                //clear your preferences if saved
            

        }
        MainActivity.logoutoperas();
        

    }
	 
    @Override
    public void onResume() {
        super.onResume();
        
        // For scenarios where the main activity is launched and user
		// session is not null, the session state change notification
		// may not be triggered. Trigger it if it's open/closed.
		session = Session.getActiveSession();
		if (session != null &&
				(session.isOpened() || session.isClosed()) ) {
			    onSessionStateChange(session, session.getState(), null);
		}
		if(session.isOpened()){
			Log.i(TAG, "hi.isOpened");
			
			AT=session.getAccessToken();
			Log.i("zzzssszzzzzzzz",AT);
			//Log.i("zz",Json.getJson(MainActivity.website+"/fbaccess?accessToken="+Login.AT,MainActivity.httpclient).toString());
			// postData();
			//Log.i("zzzssszzzzzzzz",Json.getJson("https://graph.facebook.com/me?access_token="+AT+"&method=GET",MainActivity.httpclient).getString("username"));
			if(MainActivity.LOGINSTATE==1){
	        	finish();
	        }else{
	        	loginsfb();
	        }
		}
		if(session.isClosed()){
			Log.i(TAG, "hi.");
			x=false;
		}
		
        uiHelper.onResume();
    }
    
    public void loginsfb(){
		Log.i("zz",Json.getJson(MainActivity.website+"/fbaccess?accessToken="+Login.AT,MainActivity.httpclient).toString());

    	MainActivity.loginoperas();
		
		
	//	Log.i("id",Json.getJson("http://www.standouter.com/login_status.json").toString());
		
		//Intent y = new Intent();
        //y.setClass(Login.this,  MainActivity.class);
        
        //startActivity(y);
        
         finish();
         overridePendingTransition(R.anim.activity_open,R.anim.activity_close);
		x=true;
    }
    
    
    
   
    
    @Override
    public void onPause() {
        super.onPause();
        uiHelper.onPause();
    }
    
    @Override
    public void onDestroy() {
        super.onDestroy();
        uiHelper.onDestroy();
    }
    
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        uiHelper.onSaveInstanceState(outState);
    }
    
    private void onSessionStateChange(Session session, SessionState state, Exception exception) {
    	if (state.isOpened()) {
    		Log.i(TAG, "Logged in...");
    		
    		

        } else if (state.isClosed()) {
        	Log.i(TAG, "Logged out...");
        }
    }
    
    
    

}
