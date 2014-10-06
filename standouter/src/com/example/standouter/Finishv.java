package com.example.standouter;



import android.os.Bundle;
import android.provider.MediaStore.Images.Thumbnails;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;

import android.media.ThumbnailUtils;
import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;

public class Finishv extends Activity {
	private Button submit;
	private Button watch;
	private Button retake;
	private ImageView bitmapz;
	String result1;
	int tempstr;
	private int headerid;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		/********************************************************************/
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);//for full screnn
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);//
		getWindow().setFormat(PixelFormat.TRANSLUCENT);
		/********************************************************************/
		setContentView(R.layout.finishv);
		Bundle bData = this.getIntent().getExtras();
		headerid = bData.getInt("uid");
		
		Bitmap s=ThumbnailUtils.createVideoThumbnail("/sdcard/files/e.mp4", Thumbnails.MICRO_KIND);
       bitmapz=(ImageView)this.findViewById(R.id.bitmapz);
       bitmapz.setImageBitmap(s);
       bitmapz.setScaleType(ImageView.ScaleType.FIT_XY);
       
		submit=(Button)this.findViewById(R.id.submit);
		submit.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent y = new Intent();
				Bundle bundle = new Bundle();
			    bundle.putInt("uid", (Integer)  headerid);
			    bundle.putInt("x", 2);
			    y.putExtras(bundle);
			    y.setClass(Finishv.this, Upload.class);
			    Finishv.this.startActivity(y);
			    
			    finish();
			}
		});
		
		watch=(Button)this.findViewById(R.id.watch);
		watch.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent x = new Intent();
                x.setClass(Finishv.this, Playzhang.class);
                startActivity(x);
               
			}
		});
		
		retake=(Button)this.findViewById(R.id.retake);
		retake.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent y = new Intent();
				
				Bundle bundle = new Bundle();
			    bundle.putInt("uid", (Integer)  headerid);
			    bundle.putInt("x", 2);
			    y.putExtras(bundle);
                y.setClass(Finishv.this, Cameradiv.class);
               
                startActivity(y);
				
                finish();
			}
		});
		
	}
	
	
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}