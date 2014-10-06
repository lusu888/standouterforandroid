package com.example.standouter;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import com.squareup.picasso.Picasso;

import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.graphics.PixelFormat;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class Upload extends Activity {
	

	private TextView editadress;
	private JSONObject linkjson;
	public Timer timer;
	private ProgressDialog pd;
	protected DefaultHttpClient hc;
	private EditText editname;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		/********************************************************************/
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		//getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);//for full screnn
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//
		getWindow().setFormat(PixelFormat.TRANSLUCENT);
		/********************************************************************/
		Bundle bData = this.getIntent().getExtras();
		int headerid = bData.getInt("uid");
		int x = bData.getInt("x");
		
		setContentView(R.layout.activity_upload);
		
		ImageView uploadLego=(ImageView)this.findViewById(R.id.uploadlego);
		Picasso.with(this).load(headerid). into(uploadLego);
		uploadLego.setLayoutParams ( new FrameLayout.LayoutParams( MainActivity.widthz,(MainActivity.widthz)/4));
		uploadLego.setScaleType(ImageView.ScaleType.FIT_XY);
		
		
		ImageView uploadgoback=(ImageView)this.findViewById(R.id.uploadgoback);
		Picasso.with(this).load(R.drawable.lw). into(uploadgoback);
		uploadgoback.setLayoutParams ( new FrameLayout.LayoutParams( MainActivity.widthz*124/4/180,(MainActivity.widthz)/4));
		uploadgoback.setScaleType(ImageView.ScaleType.FIT_XY);
		uploadgoback.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
				
			}
		});
		
		
		ImageView choosefile=(ImageView)this.findViewById(R.id.choosefilebtn);
		choosefile.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				showFileChooser();
			}
		});
		TextView textfilepath = (TextView)this.findViewById(R.id.fileadresstextview);
		 editadress=(TextView)this.findViewById(R.id.fileadressedit);
		 editname=(EditText)this.findViewById(R.id.filenameedit);
		
		Button uploadbtn=(Button)this.findViewById(R.id.uploadbtn);
		uploadbtn.setOnClickListener(new View.OnClickListener() {
			
			

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Log.i("leg",""+editname.length());
				if(editname.length()==0){
					
					AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
							Upload.this);
			 
						// set title
					     alertDialogBuilder.setTitle("upload");
			 
						// set dialog message
						
						alertDialogBuilder
							.setMessage("Please input the video name!")
							.setCancelable(false)
							.setPositiveButton("OK",new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,int id) {
									// if this button is clicked, close
									// current activity
									dialog.cancel();
								}
							  });
							
			 
							// create alert dialog
							AlertDialog alertDialog = alertDialogBuilder.create();
			 
							// show it
							alertDialog.show();
				}else{
		        new Thread(new MyThread()).start();
		        pd = ProgressDialog.show(Upload.this, "Uploading", "Uploading，please wait……");  
				}

				
			}
			
		});
		 if(x==2) {
			 editadress.setText("/storage/sdcard0/files/e.mp4");
			 editadress.setVisibility(View.GONE);
			 textfilepath.setVisibility(View.GONE);
			 
		 }
		 else{
				showFileChooser();

		 }
		 
		
	}
	public void onBackPressed() {
		  onBackPressed_local(this);
		}
 public void onBackPressed_local(final Activity context) {
	   finish(); 
	    Intent y = new Intent();
		
     y.setClass(Upload.this,MainActivity.class);
     Upload.this.startActivity(y);
     Upload.this.overridePendingTransition(R.anim.activity_open_right,R.anim.activity_close_right); 
}
	/******************************************************************************************************
	 * choose the file
	 */
	private  final int FILE_SELECT_CODE = 0;
	private  final String TAG = "hi";

    private void showFileChooser() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT); 
        intent.setType("video/*"); 
        intent.addCategory(Intent.CATEGORY_OPENABLE);

        try {
            startActivityForResult(
                    Intent.createChooser(intent, "Select a File to Upload"),
                    FILE_SELECT_CODE);
        } catch (android.content.ActivityNotFoundException ex) {
            // Potentially direct the user to the Market with a Dialog
            Toast.makeText(this, "Please install a File Manager.", 
                    Toast.LENGTH_SHORT).show();
        }
    }
	
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case FILE_SELECT_CODE:      
            if (resultCode == RESULT_OK) {  
                // Get the Uri of the selected file 
                Uri uri = data.getData();
                Log.d(TAG, "File Uri: " + uri.toString());
                // Get the path
                String path = null;
				try {
					path = getPath(this, uri);
				} catch (URISyntaxException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				editadress.setText(path);
                Log.d(TAG, "File Path: " + path);
                // Get the file instance
                // File file = new File(path);
                // Initiate the upload
            }           
            break;
        }
    super.onActivityResult(requestCode, resultCode, data);
    }
	
    public static String getPath(Context context, Uri uri) throws URISyntaxException {
        if ("content".equalsIgnoreCase(uri.getScheme())) {
            String[] projection = { "_data" };
            Cursor cursor = null;

            try {
                cursor = context.getContentResolver().query(uri, projection, null, null, null);
                int column_index = cursor
                .getColumnIndexOrThrow("_data");
                if (cursor.moveToFirst()) {
                    return cursor.getString(column_index);
                }
            } catch (Exception e) {
                // Eat it
            }
        }

        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }
    
    
	
	/******************************************************************************************************
	 * 
	 */
	
	
	
	
	
	
	
	
	/******************************************************************************************************
	 * 
	 */
	/**
	 * Bits on the Run JavaScript Uploader version 1.01
	 * Written by Tom Boshoven
	 * 
	 * Inspired by Resumable.js and Valums AJAX Upload (version 1).
	 */

	/**
	 * Construct a BotrUpload object.
	 * 
	 * Only the link parameter is required.
	 * 
	 * link:             The link object as returned by the API
	 * resumableSession: The resumable session id if the upload should be
	 *                   resumable. False otherwise.
	 * redirect:         The object describing the redirect location.
	 *                   Should be of the form {url: '...', params: ['...', ...]} .
	 *                   The params property is optional.
	 * id:               A unique identifier to use for the iframe (if used)
	 *                   and for the JSON-P calls (if used).
	 *                   If left blank, it is randomly generated.
	 */
	
	public String prepareupload(String title){
		String severurl = null;
		String Link=null;
		 linkjson=null;
		 HttpPost httppost = new HttpPost(MainActivity.website+"/contest/"+MainActivity.cc+"/prepare_upload?"+System.currentTimeMillis());
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
		    try {
		        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
		        nameValuePairs.add(new BasicNameValuePair("resumable","false"));
		        nameValuePairs.add(new BasicNameValuePair("title",title));
		       
		        httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

		        HttpResponse response = MainActivity.httpclient.execute(httppost);
		         Link = EntityUtils.toString(response.getEntity());
		        Log.i("zz", Link);
		        


		    } catch (ClientProtocolException e) {

		    } catch (IOException e) {

		    }
		    try {
		    	
				linkjson=new JSONObject(Link);
				severurl=linkjson.getJSONObject("link").getString("protocol")
						  +"://"
						  +linkjson.getJSONObject("link").getString("address")
						  +linkjson.getJSONObject("link").getString("path")
						  +"?api_format=json&key="
						  +linkjson.getJSONObject("link").getJSONObject("query").getString("key")
						 
						  ;
				if(linkjson.getString("session_id").equals("null")){
					severurl =severurl+"&token="
							 +linkjson.getJSONObject("link").getJSONObject("query").getString("token");
				}
				return severurl;
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		    
			return null;
		    
		    
		    
	}
	

	public String post(String pathToOurFile,String urlServer) throws ClientProtocolException, IOException, JSONException {
		   HttpClient httpclient = MainActivity.httpclient;
		   //设置通信协议版本
		   httpclient.getParams().setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);
		   
		   //File path= Environment.getExternalStorageDirectory(); //取得SD卡的路径

		//String pathToOurFile = path.getPath()+File.separator+"ak.txt"; //uploadfile
		//String urlServer = "http://192.168.1.88/test/upload.php"; 

		   HttpPost httppost = new HttpPost(urlServer);
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
		   File file = new File(pathToOurFile);
		   httppost.setHeader("enctype", "multipart/form-data");
		   httppost.setHeader("path", "");
		  
		   
		   


		   MultipartEntity mpEntity = new MultipartEntity(); //文件传输
		   ContentBody cbFile = new FileBody(file);
		   mpEntity.addPart("file", cbFile); // <input type="file" name="userfile" />  对应的


		  httppost.setEntity(mpEntity);
		  
		   
		   System.out.println("executing request " + httppost.getRequestLine());
		   
		   HttpResponse response = httpclient.execute(httppost);
		   HttpEntity resEntity = response.getEntity();

		   System.out.println(response.getStatusLine());//通信Ok
		   String json="";
		   String path="";
		   if (resEntity != null) {
		     //System.out.println(EntityUtils.toString(resEntity,"utf-8"));
		     json=EntityUtils.toString(resEntity,"utf-8");
		     JSONObject p=null;
		     try{
		   	  p=new JSONObject(json);
		   	  Log.i("p",p.toString());
		   	  path=p.toString();
		     }catch(Exception e){
		   	  e.printStackTrace();
		     }
		   }
		   if (resEntity != null) {
		     resEntity.consumeContent();
		   }
		   timer = new Timer(true);
	        
	         timer.schedule(new timerTask(),2000,2000);

		   //httpclient.getConnectionManager().shutdown();
		   return path;
		 }
	
	
	/******************************************************************************************************
	 * 
	 */
	
	 public class MyThread implements Runnable{

	    	@Override
	    	public void run() {
	    		
	    		String path=prepareupload(editname.getText().toString());
	    		Log.i("",path);
	    		 
				try {
					Log.i("hi2",post(editadress.getText().toString(),path));
				} catch (ClientProtocolException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				

			     

	    	}
	    	
	    }
	
	 public class timerTask extends TimerTask
	    {
	      public void run()
	      {
	    	  
	    	 
	    	  
	    	  Message message = new Message(); 
	    	  
	    	  message.what = 1; 
	    	  handlertimetask1.sendMessage(message);
	    	  
	    	  
	      }
	    }; 
	    Handler handlertimetask1 = new Handler(){         
        

		public void handleMessage(Message msg) {
			if( hc!=null){
        	  hc =new DefaultHttpClient();
			}
              switch (msg.what) {                 
                 case 1:  
				try {
					String prosessurl=linkjson.getJSONObject("link").getString("protocol")
					  +"://"
					  +linkjson.getJSONObject("link").getString("address")
					  
					  +"/progress?token="
					  +linkjson.getJSONObject("link").getJSONObject("query").getString("token")
					  +"&callback=callback"
					  ;
					String callbackupload=null;
					
					callbackupload=Login.getHtmlByGet(prosessurl,MainActivity.httpclient);
					Log.i("progress",callbackupload);
					
					 JSONObject callbackjson = new JSONObject(callbackupload.substring(callbackupload.indexOf("(") + 1, callbackupload.lastIndexOf(")")));
					Log.i("progress",callbackjson.getString("state"));
					
					if(callbackjson.getString("state").equals("done")){
						Log.i("","pk");
						timer.cancel();
						 pd.dismiss();
						 Toast toast = Toast.makeText(Upload.this,
					                "Upload Success!",
					                Toast.LENGTH_LONG);
					        toast.setGravity(Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 0);
					        toast.show();
					}else{
						 pd.dismiss();
						AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
								Upload.this);
				 
							// set title
							alertDialogBuilder.setTitle("Upload");
				 
							// set dialog message
							alertDialogBuilder
								.setMessage("Upload does not Success, please try again!")
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
						Log.i("","no");
					}
					
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		
				//Log.i("",);

                     break; 
                 case 0:
                 	
  					break;
                                 }   
              
                super.handleMessage(msg);      
                } 
		
    };
	
	
	
	
	
	
	
	
	
	/******************************************************************************************************
	 * 
	 */
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

	public void finish() {  
	    // TODO Auto-generated method stub  
	    super.finish(); 
	    Intent y = new Intent();
		
        y.setClass(this,SplashActivity.class);
        
        
      
        this.startActivity(y);
	    
	    
	    this.overridePendingTransition(R.anim.activity_open_right,R.anim.activity_close_right);  
	} 

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.upload, menu);
		return true;
	}

}
