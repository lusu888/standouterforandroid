package com.example.standouter;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;  
import android.content.res.Configuration;
import android.graphics.PixelFormat;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;  
import android.util.Log;
import android.view.Gravity;  
import android.view.LayoutInflater;  
import android.view.MotionEvent;
import android.view.View;  
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;  
import android.widget.Gallery;
import android.widget.PopupWindow;   
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView; 
import android.widget.SeekBar.OnSeekBarChangeListener;

  
@SuppressWarnings("deprecation")
public class Play extends Activity  {  
  
    private PopupWindow mPopupWindow=null; 
    private PopupWindow mPopupWindowleft=null; 
    private PopupWindow mPopupWindowright=null; 
   
    public  String[] vidpath= new String[5];;
    private VideoView videoView2;
    private Button play;
	protected boolean isplay=false;
	private SeekBar seekbar;
	private Timer mTimer;    
    private TimerTask mTimerTask;   
    private boolean isChanging=false;//互斥变量，防止定时器与SeekBar拖动时进度冲突   
    protected boolean ispup=false;
    private Button left;
    private Button right;
    private Gallery filmshow;
	public  int heigz;
	public  int widthz;
	public  int vidtime;
	public  String[] picid=new String[5];;
	int vid2time;
  
    @Override  
    public void onCreate(Bundle savedInstanceState) {  
        super.onCreate(savedInstanceState);  
       
        /********************************************************************/
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);//for full screnn
		//setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);//
		getWindow().setFormat(PixelFormat.TRANSLUCENT);
		/********************************************************************/
		heigz=this.getWindowManager().getDefaultDisplay().getHeight();
		widthz=this.getWindowManager().getDefaultDisplay().getWidth();
		setContentView(R.layout.play);
		 Bundle bData = this.getIntent().getExtras();
		 
		 String vpath=bData.getString("result1");
		 vidtime=Integer.valueOf(bData.getString("time"));
		  picid=bData.getStringArray("pic");
		  vidpath=bData.getStringArray("des");
		 Log.i("", vpath);
	     
	      
	      // 顯示結果
	   

		videoView2 = (VideoView) this.findViewById(R.id.video2);
		 
	    
       // File videoFile = new File(vpath);
        
        //initPopuptWindow() ;

		// 先判断这个文件是否存在
		if (true) {
			//System.out.println("文件存在");
			Uri uri = Uri.parse(vpath);
			videoView2.setVideoURI(uri);

			//videoView2.setVideoPath(videoFile.getAbsolutePath());
			//System.out.println(videoFile.getAbsolutePath());
			
			// 让VideoView获取焦点
			initPopuptWindow() ;
			videoView2.seekTo(vidtime);
			videoView2.requestFocus();
			
			playv();
			
			TextView text=(TextView) this.findViewById(R.id.text2);
			
			text.setText(""+videoView2.getDuration());
			
			
			
            videoView2.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
				
				@Override
				public void onCompletion(MediaPlayer mp) {
					// TODO Auto-generated method stub
					//play.setText("Play");
				
					
					

					
					//ImageTextListBaseAdapterActivity.videoview.setVisibility(View.INVISIBLE);
					//setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//
	               //Play.this.finish();
					
				}
			});
            ispup=false;
			
			
			videoView2.setOnTouchListener(new View.OnTouchListener() {
				
				@Override
				public boolean onTouch(View v, MotionEvent event) {
					// TODO Auto-generated method stub
					
					
					if(ispup){
						 mPopupWindow.dismiss();
						 mPopupWindowleft.dismiss();
						 mPopupWindowright.dismiss();
						 ispup=false;
					}else{
						
						getPopupWindowInstance();  
			            mPopupWindow.showAtLocation(v, Gravity.BOTTOM, 0, 0); 
			            getPopupWindowInstanceleft() ;
			            mPopupWindowleft.showAtLocation(v, Gravity.LEFT, 100, -30);
			            getPopupWindowInstanceright();
			            mPopupWindowright.showAtLocation(v, Gravity.RIGHT, 100, -30);
			            ispup=true;
					}
					
					
					return false;
				}
			});
			
			
			
			
		} else {
			Toast.makeText(this, "文件不存在", Toast.LENGTH_LONG).show();
		}

	}
  
          
      
  
    /* 
     * 获取PopupWindow实例 
     */  
    private void getPopupWindowInstance() {  
        if (null != mPopupWindow) {  
            mPopupWindow.dismiss();  
            return;  
        } else {  
        	
            initPopuptWindow();  
        }  
    }  
    private void getPopupWindowInstanceleft() {  
        if (null != mPopupWindowleft) {  
            mPopupWindowleft.dismiss();  
            return;  
        } else {  
            initPopuptWindowleft();  
        }  
    }  
    private void getPopupWindowInstanceright() {  
        if (null != mPopupWindowright) {  
            mPopupWindowright.dismiss();  
            return;  
        } else {  
            initPopuptWindowright();  
        }  
    }  
  
    /* 
     * 创建PopupWindow 
     */  
   
    private void initPopuptWindow() {  
    	
        LayoutInflater layoutInflater = LayoutInflater.from(this);  
        View popupWindow = layoutInflater.inflate(R.layout.contr, null);  
        play=(Button) popupWindow.findViewById(R.id.play2);
        
        play.setHeight(heigz/12);
        seekbar = (SeekBar)  popupWindow.findViewById(R.id.seekbar2);
	    seekbar.setOnSeekBarChangeListener(new MySeekbar());
	    
	    filmshow= (Gallery) popupWindow.findViewById(R.id.filmlist2);
	    ImageAdapter imageadapter = new ImageAdapter(this);
	    imageadapter.setItem(picid);
	    filmshow.setAdapter(imageadapter );
	    
	    filmshow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
	    	
			
			public void onItemClick(AdapterView parent, View v,  int position, long id) {
				// TODO Auto-generated method stub
				
				 
				Toast.makeText(Play.this, "" + position,   
				    	Toast.LENGTH_SHORT).show(); //Toast显示图片位置  
				    	
				       videoView2.destroyDrawingCache();
				       Uri uri = Uri.parse(vidpath[position]);
						videoView2.setVideoURI(uri);
				       //File videoFile=new File( vidpath[position]);
				       //videoView2.setVideoPath(videoFile.getAbsolutePath());
			    	   
			    		videoView2.requestFocus();// 让VideoView获取焦点
				/*
				    	if(position==3){
				    		//videoView.pause();
				    	
				    		
				    		
				            File videoFile = new File("/sdcard/files/c.mp4");
				    	    videoView2.setVideoPath(videoFile.getAbsolutePath());
				    	    
				    		videoView2.requestFocus();// 让VideoView获取焦点
				    		
							
				    	
				    	}    
				    	if(position==0){
				    		
				    		
				    		
				            File videoFile = new File("/sdcard/files/e.mp4");
				    	    videoView2.setVideoPath(videoFile.getAbsolutePath());
				    	   
				    		videoView2.requestFocus();// 让VideoView获取焦点
				    		
				    	
				    	}   
				    	if(position==1){
				    		
				    		
				    		
				    		Uri uri = Uri.parse("http://www.boisestatefootball.com/sites/default/files/videos/original/01%20-%20coach%20pete%20bio_4.mp4");
				    	   videoView2.setVideoURI(uri);
				    	  

				    	   
				    		videoView2.requestFocus();// 让VideoView获取焦点
				    		//videoView.seekTo(100); 
				    		
				    		
				    	
				    	} 
				    	 
				    	if(position==2){
				    		
				    		
				    		
				    		 File videoFile = new File("/sdcard/files/e.mp4");
					    	    videoView2.setVideoPath(videoFile.getAbsolutePath());
					    	   
					    		videoView2.requestFocus();// 让VideoView获取焦点
				    		
				    		
				    	
				    	} 
				    	*/
				    	playv();
				    	
				    	
				
			}
		});
	   
	    
        play.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				if(isplay){
					//play.setText("Play");
					pausev();
					
					
				}else{
					playv();
				}
			}
		});
        
  
        // 创建一个PopupWindow  
        // 参数1：contentView 指定PopupWindow的内容  
        // 参数2：width 指定PopupWindow的width  
        // 参数3：height 指定PopupWindow的height  
        mPopupWindow = new PopupWindow(popupWindow,this.getWindowManager().getDefaultDisplay().getWidth(), heigz/3); 
        mPopupWindow.setAnimationStyle(R.style.AnimationFade2); 
       
        //mPopupWindow.getBackground().setAlpha(100)
        
        
  
        getWindowManager().getDefaultDisplay().getWidth();  
        getWindowManager().getDefaultDisplay().getHeight();  
        mPopupWindow.getWidth();  
        mPopupWindow.getHeight();  
        
    }  
    public void playv(){
    	
    	play.setBackgroundResource(R.drawable.pause);
		play.getBackground().setAlpha(100);
		videoView2.start();
		
    	
		
        //----------定时器记录播放进度---------//    
        mTimer = new Timer();    
        mTimerTask = new TimerTask() {    
            @Override    
            public void run() {     
                if(isChanging==true) {   
                    return;    
                }  
                seekbar.setMax(videoView2.getDuration());//设置进度条  
                seekbar.setProgress(videoView2.getCurrentPosition());  
            }    
        };   
        mTimer.schedule(mTimerTask, 0, 10);  
		isplay=true;
    	
    	
		
    }
    public void pausev(){
    	videoView2.pause();
		isplay=false;
		play.setBackgroundResource(R.drawable.play);
		play.getBackground().setAlpha(100);
    }
    
    private void initPopuptWindowleft() {  
        LayoutInflater layoutInflater = LayoutInflater.from(this);  
        View popupWindow2 = layoutInflater.inflate(R.layout.leftb, null);  
        left=(Button) popupWindow2.findViewById(R.id.left);
        
        
  
        // 创建一个PopupWindow  
        // 参数1：contentView 指定PopupWindow的内容  
        // 参数2：width 指定PopupWindow的width  
        // 参数3：height 指定PopupWindow的height  
        mPopupWindowleft = new PopupWindow(popupWindow2,widthz/6, widthz/6); 
        mPopupWindowleft.setAnimationStyle(R.style.AnimationFade); 
        
        //mPopupWindow.getBackground().setAlpha(100)
        
        
  
        
        
    }  
    class MySeekbar implements OnSeekBarChangeListener {  
        public void onProgressChanged(SeekBar seekBar, int progress,  
                boolean fromUser) {  
        }  
  
        public void onStartTrackingTouch(SeekBar seekBar) {  
            isChanging=true;    
        }  
  
        public void onStopTrackingTouch(SeekBar seekBar) {  
        	videoView2.seekTo(seekbar.getProgress());  
            isChanging=false;    
        }  
  
    }  
    private void initPopuptWindowright() {  
        LayoutInflater layoutInflater = LayoutInflater.from(this);  
        View popupWindowright = layoutInflater.inflate(R.layout.right, null);  
        right=(Button) popupWindowright.findViewById(R.id.right);
        
        
  
        // 创建一个PopupWindow  
        // 参数1：contentView 指定PopupWindow的内容  
        // 参数2：width 指定PopupWindow的width  
        // 参数3：height 指定PopupWindow的height  
        mPopupWindowright = new PopupWindow(popupWindowright,widthz/6, widthz/6); 
        mPopupWindowright.setAnimationStyle(R.style.AnimationFaderight); 
        
        //mPopupWindow.getBackground().setAlpha(100)
        
        
  
        // 获取屏幕和PopupWindow的width和height  
         
        
    }  
    
    @Override 
    public void onConfigurationChanged(Configuration newConfig) {  
      super.onConfigurationChanged(newConfig);  
      Log.i("zhang"," == onConfigurationChanged");  
      
      if(this.getResources().getConfiguration().orientation==Configuration.ORIENTATION_PORTRAIT)
      { 
    	  vidtime=videoView2.getCurrentPosition();
    	  finish();
      }
        
    }  
    
    
    
    
  
    
}  
