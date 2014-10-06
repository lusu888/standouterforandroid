package com.standouter.standouternew;

import com.standouter.standouternew.R;
import com.standouter.standouternew.R.drawable;
import com.standouter.standouternew.R.id;
import com.standouter.standouternew.R.layout;
import com.standouter.standouternew.R.menu;
import com.squareup.picasso.Picasso;

import android.media.MediaPlayer;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.VideoView;

public class Playrecord extends Activity {

	private int widthz;
	private int heiz;
	private ImageView btnretake;
	private ImageView btnsubmit;
	private ImageView btncancel;
	private int stateno;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		 requestWindowFeature(Window.FEATURE_NO_TITLE);
			getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
	        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		setContentView(R.layout.activity_playrecord);
		widthz=this.getWindowManager().getDefaultDisplay().getWidth();
	    heiz=this.getWindowManager().getDefaultDisplay().getHeight();
	    
		VideoView videoView = (VideoView) this.findViewById(R.id.videoView);
		  MediaController mc = new MediaController(this);
		  videoView.setMediaController(mc);
		  videoView.setVideoPath("/mnt/sdcard/Standouter/new_stream.mp4");
		  videoView.requestFocus();
		  videoView.start();
		  videoView.setOnPreparedListener(new OnPreparedListener() {

              public void onPrepared(MediaPlayer mp) {
                      // TODO Auto-generated method stub
                      mp.start();
                      mp.setLooping(true);
              }
          });
		  
		  btnretake=(ImageView)this.findViewById(R.id.btnretake);
		  RelativeLayout.LayoutParams lp=new  RelativeLayout.LayoutParams(heiz/4,heiz/4);
	      lp.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
	      btnretake.setLayoutParams(lp);
		  Picasso.with(this).load(R.drawable.retake).resize(heiz/4, heiz/4).into(btnretake);
		  btnretake.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
	            
	             stateno=0;
	            // Playrecord.this.startActivity(y);
	             Playrecord.this.finish();
			}
		});

	      lp=null;
	      
	      btnsubmit=(ImageView)this.findViewById(R.id.btnsubmit);
	      lp=new  RelativeLayout.LayoutParams(heiz/4,heiz/4);
	      lp.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
	      btnsubmit.setLayoutParams(lp);
		  Picasso.with(this).load(R.drawable.submit).resize(heiz/4, heiz/4).into(btnsubmit);
	      lp=null;
	      btnsubmit.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					
		            
		             stateno=1;
		            // Playrecord.this.startActivity(y);
		             Playrecord.this.finish();
				}
			});
	      
	      btncancel=(ImageView)this.findViewById(R.id.btncancel);
	      lp=new  RelativeLayout.LayoutParams(heiz/4,heiz/4);
	      lp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
	      btncancel.setLayoutParams(lp);

		  Picasso.with(this).load(R.drawable.deletev).resize(heiz/4, heiz/4).into(btncancel);
		  btncancel.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Intent y = new Intent();
		             y.setClass(Playrecord.this, MainActivity.class);
		            
		             stateno=2;
		            // Playrecord.this.startActivity(y);
		             Playrecord.this.finish();
				}
			});

	      lp=null;

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		
		getMenuInflater().inflate(R.menu.playrecord, menu);
		
		
		return true;
	}
	
	public void finish() {  
	    // TODO Auto-generated method stub  
	   
		
		
		
		Intent intent = new Intent();       //創建一個Intent，聯繫Activity之用	
		Bundle bundleBack = new Bundle();   //創建一個Bundle，傳值之用	
		bundleBack.putInt("state", stateno);
		intent.putExtras(bundleBack);       
		setResult(RESULT_OK, intent);
		
	   super.onDestroy();
	   
	   super.finish();
	   
	    //关闭窗体动画显示  
	  //  this.overridePendingTransition(R.anim.activity_open_right,R.anim.activity_close_right);  
	}

}
