package com.example.standouter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import com.squareup.picasso.Picasso;

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
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

public class Law extends Activity {
	private TextView lawtext;
	private Button nobtn;
	private Button okbtn;
	private int headerid;
	protected String texttemp;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		/********************************************************************/
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		//getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);//for full screnn
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//
		getWindow().setFormat(PixelFormat.TRANSLUCENT);
		/********************************************************************/
		setContentView(R.layout.activity_law);
		
		Bundle bData = this.getIntent().getExtras();
		headerid = bData.getInt("uid");
		
		ImageView uploadLego=(ImageView)this.findViewById(R.id.lawlego);
		Picasso.with(this).load(headerid). into(uploadLego);
		uploadLego.setLayoutParams ( new FrameLayout.LayoutParams( MainActivity.widthz,(MainActivity.widthz)/4));
		uploadLego.setScaleType(ImageView.ScaleType.FIT_XY);
		
		
		ImageView uploadgoback=(ImageView)this.findViewById(R.id.lawgoback);
		Picasso.with(this).load(R.drawable.lw). into(uploadgoback);
		uploadgoback.setLayoutParams ( new FrameLayout.LayoutParams( MainActivity.widthz*124/4/180,(MainActivity.widthz)/4));
		uploadgoback.setScaleType(ImageView.ScaleType.FIT_XY);
		uploadgoback.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
				  Intent y = new Intent();
				 y.setClass(Law.this,MainActivity.class);
			        Law.this.startActivity(y);
			        Law.this.overridePendingTransition(R.anim.activity_open_right,R.anim.activity_close_right); 
				
			}
		});
		
		
		
		
		lawtext=(TextView)this.findViewById(R.id.textlaw);
		nobtn=(Button)this.findViewById(R.id.canclebtn);
		okbtn=(Button)this.findViewById(R.id.okbtn);
		
		
		
		
		//lawtext.setText(getString( inputStream));
		
		nobtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish(); 
			    Intent y = new Intent();
				
		        y.setClass(Law.this,MainActivity.class);
		        Law.this.startActivity(y);
		        Law.this.overridePendingTransition(R.anim.activity_open_right,R.anim.activity_close_right); 
			}
		});
		okbtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
						Law.this);
		 
					// set title
				     alertDialogBuilder.setTitle("Upload");
		 
					// set dialog message
					
					alertDialogBuilder
						.setMessage("You want to upload from the......")
						.setCancelable(false)
						.setPositiveButton("Camera",new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,int id) {
								// if this button is clicked, close
								// current activity
								dialog.cancel();
								Intent y = new Intent();
								
								 Bundle bundle = new Bundle();
								    bundle.putInt("uid", (Integer)  headerid);
								    y.putExtras(bundle);
								    y.setClass(Law.this, Cameradiv.class);
								    Law.this.startActivity(y);
								    Law.this.overridePendingTransition(R.anim.jiatihuan2,R.anim.sleep); 
								    finish();
							}
						  })
						.setNegativeButton("Cellphone",new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,int id) {
								// if this button is clicked, just close
								// the dialog box and do nothing
								dialog.cancel();
								Intent y = new Intent();
								Bundle bundle = new Bundle();
							    bundle.putInt("uid", (Integer)  headerid);
							    bundle.putInt("x", 1);
							    y.putExtras(bundle);
							    y.setClass(Law.this, Upload.class);
							    Law.this.startActivity(y);
							    Law.this.overridePendingTransition(R.anim.jiatihuan2,R.anim.sleep); 
							    finish();
							}
						});
		 
						// create alert dialog
						AlertDialog alertDialog = alertDialogBuilder.create();
		 
						// show it
						alertDialog.show();
				
				
			}
		});
		new Thread(runnable).start();
		

		
		
	}
	
	public void onBackPressed() {
		  onBackPressed_local(this);
		}
   public void onBackPressed_local(final Activity context) {
	   finish(); 
	    Intent y = new Intent();
		
       y.setClass(Law.this,MainActivity.class);
       Law.this.startActivity(y);
       Law.this.overridePendingTransition(R.anim.activity_open_right,R.anim.activity_close_right); 
  }
		    
		    
		    
	 Runnable runnable=new Runnable(){

   		@Override
   		public void run() {
   			// TODO Auto-generated method stub
   			InputStream inputStream = getResources().openRawResource(R.raw.law);
   			texttemp=getString( inputStream);
   			 Message msglv= new Message();
   	        msglv.what=1;
   	        handler.sendMessage(msglv);
   		}
       	   
    };
    private Handler handler=new Handler(){
	 	   @Override
		public void handleMessage(Message msglv){
	 		  // super.handleMessage(msg2);
	 		   if( msglv.what==1){
	 			  lawtext.setText(texttemp);
	 			 
	 		    
	 		   }
	 		   
	 		   
	 	   }
	     };

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.law, menu);
		return true;
	}
	public static String getString(InputStream inputStream) {
		InputStreamReader inputStreamReader = null;
		inputStreamReader = new InputStreamReader(inputStream);
		BufferedReader reader = new BufferedReader(inputStreamReader);
		StringBuffer sb = new StringBuffer("");
		String line;
		try {
			while ((line = reader.readLine()) != null) {
				sb.append(line);
				sb.append("\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return sb.toString();
	}

}
