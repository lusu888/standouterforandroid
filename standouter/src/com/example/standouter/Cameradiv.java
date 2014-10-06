package com.example.standouter;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.util.LinkedList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import com.coremedia.iso.IsoFile;
import com.coremedia.iso.boxes.Container;
import com.googlecode.mp4parser.authoring.Movie;
import com.googlecode.mp4parser.authoring.Track;
import com.googlecode.mp4parser.authoring.builder.DefaultMp4Builder;
import com.googlecode.mp4parser.authoring.container.mp4.MovieCreator;
import com.googlecode.mp4parser.authoring.tracks.AppendTrack;
import com.squareup.picasso.Picasso;





import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.hardware.Camera.Size;
import android.media.MediaRecorder;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.SurfaceHolder.Callback;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

@SuppressLint("HandlerLeak")
public class Cameradiv extends Activity {
	private SurfaceView sfvShow; 
    
    private MediaRecorder mediaRecorder; 

    private Camera camera = null;         
    private int cameraCount = 0; 
    boolean isPreview=false;
    SurfaceHolder mSurfaceHolder;
    private ProgressBar pro1;
    private int timeno;
    private int timeno2;
	private String state="ready";
	int i=0;
	int j=0;
	int N=0;
	int k=0;
	int contv=0;
	private Timer tim;
	private int dt;
	private double[] stt=new double[300];
	private double[] spt=new double[300];
	private TextView text;
	private ImageView s6;
	private ImageView s15;
	private ImageView s30;
	private Thread t1;
	double errt=0;


	


	private Thread t2;

	protected ProgressDialog pd;

	private int headerid;
	
	

	@SuppressLint("NewApi")
	@Override
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		/********************************************************************/
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);//for full screnn
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);//
		getWindow().setFormat(PixelFormat.TRANSLUCENT);
		/********************************************************************/
		Bundle bData = this.getIntent().getExtras();
		headerid = bData.getInt("uid");
        setContentView(R.layout.activity_cameradiv);
		pro1 = (ProgressBar) findViewById(R.id.progressBar1); 
		pro1.setIndeterminate(false); 
		
		text=(TextView)this.findViewById(R.id.text);
		s6=(ImageView)this.findViewById(R.id.s6);
		s15=(ImageView)this.findViewById(R.id.s15);
		s30=(ImageView)this.findViewById(R.id.s30);
    	Picasso.with(this).load(R.drawable.z6s). resize(MainActivity.widthz/4,MainActivity.widthz/4). into(s6);
    	Picasso.with(this).load(R.drawable.z15s). resize(MainActivity.widthz/4,MainActivity.widthz/4). into(s15);
    	Picasso.with(this).load(R.drawable.z30s). resize(MainActivity.widthz/4,MainActivity.widthz/4). into(s30);

		//camfr=(Button)this.findViewById(R.id.fr);
		sfvShow = (SurfaceView)this.findViewById(R.id.sfvShow); 
		sfvShow.setLayoutParams(new FrameLayout.LayoutParams( MainActivity.heiz,MainActivity.widthz
                ));
		//LayoutParams Ip= sfvShow.getLayoutParams();
		
		//Ip.width=  MainActivity.heiz;
		//Ip.height=  MainActivity.widthz;
		//sfvShow.setLayoutParams(Ip);
		//sfvShow.setActivated(false);
		
        timeno=6;
        N=25;
        
       
       
        initView();
       
        
      
        
       
		
		
		
		
	
		s6.setOnClickListener(new View.OnClickListener() {
			
			@SuppressLint("NewApi")
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				 pd = ProgressDialog.show(Cameradiv.this, "Waiting", "Processing，please wait……");  
				timeno=4;
				timeno2=timeno*N;
				pro1.setMax(timeno2); 
				s6.setVisibility(View.GONE);
  				s15.setVisibility(View.GONE);
  				s30.setVisibility(View.GONE);
				t2.start();
			}
		});

		s15.setOnClickListener(new View.OnClickListener() {
			
			@SuppressLint("NewApi")
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				 pd = ProgressDialog.show(Cameradiv.this, "Waiting", "Processing，please wait……");  
				timeno=15;
				timeno2=timeno*N;
				pro1.setMax(timeno2); 
				s6.setVisibility(View.GONE);
  				s15.setVisibility(View.GONE);
  				s30.setVisibility(View.GONE);
				t2.start();
				
				
			}
		});
		

		s30.setOnClickListener(new View.OnClickListener() {
			
			@SuppressLint("NewApi")
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				 pd = ProgressDialog.show(Cameradiv.this, "Waiting", "Processing，please wait……");  
				timeno=30;
				timeno2=timeno*N;
				pro1.setMax(timeno2); 
				s6.setVisibility(View.GONE);
  				s15.setVisibility(View.GONE);
  				s30.setVisibility(View.GONE);
				t2.start();
				
			}
		});
		t2= new Thread(runnable3);
		t1= new Thread(runnable1);
		Log.i("","sxs");
		statt();
		Log.i("","sxs");
		
	
		
		
	}
	
	 private Handler handler3=new Handler(){
    	 @Override
		@SuppressLint("NewApi") public void handleMessage(Message msg3){
   		  // super.handleMessage(msg2);
    		
    		 if(msg3.what==0){
 				
    			
 				sfvShow.setActivated(true);
 				sfvShow.setOnTouchListener(new View.OnTouchListener() {
 					
 					@Override
 					public boolean onTouch(View v, MotionEvent event) {
 						// TODO Auto-generated method stub
 						switch (event.getAction()){
 						case MotionEvent.ACTION_DOWN:
 							try {
 								starv();
 							} catch (IOException e) {
 								// TODO Auto-generated catch block
 								e.printStackTrace();
 								i=i;
 							}
 							break;
 							

 							
 							 
 						case MotionEvent.ACTION_UP:
 							
 							 stopv();
 							 //String TAG = null;
 							//Log.i(TAG,"ZHANG");
 							
 							
 							break;
 							
 						
 						}
 						
 						return true;
 					}
 				});
 				 pd.dismiss();
 				
    		 }
    	 }
    		 
    	
    };
    private Runnable runnable3=new Runnable(){
    	

		@SuppressLint("NewApi") @Override
		public void run() {
			// TODO Auto-generated method stub
			sfvShow.setActivated(false);
			Message msg3= new Message();
			//msg3.what=2;
			
			//handler3.sendMessage(msg3);
			if(j==0){
				
				camera.unlock();
				
				
			 //调用前置摄像头--注意，要在MediaRecorder设置参数之前就调用unlock来获得camera的控制权。camera是单例的嘛。如果不调用，程序就挂 
	        //File videofile = new File(Environment.getExternalStorageDirectory(), "ZHANG.3gp"); 
	        mediaRecorder = new MediaRecorder(); 
	        mediaRecorder.setCamera(camera); //如果需要前置摄像头，则加上，反之，这句话不需要存在 
	        //设置声音从哪个设备获取 
	        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC); 
	        //设置画面从哪个设备获取 
	        mediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA); 
	        //设置输入的格式 
	        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4); 
	        //设置视频大小 
	        mediaRecorder.setVideoSize(1280,720); 
	        //设置每秒获取几帧，5帧比较清晰 
	        mediaRecorder.setVideoFrameRate(5); 
	        //设置音频编码格式 
	        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB); 
	        //设置视频编码格式 
	        mediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.MPEG_4_SP); 
	        //设置文件存放位置 
	        mediaRecorder.setOutputFile("/sdcard/files/a.mp4"); 
	        //显示画面 
	        mediaRecorder.setPreviewDisplay(sfvShow.getHolder().getSurface()); //预览输入 
	        //缓冲 
	        try {
				mediaRecorder.prepare();
			} catch (IllegalStateException e) {
				// TODO Auto-generated catch block
				
				Log.i("","do wroing");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				Log.i("","do wroing");
			} 
	        //开始刻录 
	       
	       
	        mediaRecorder.start(); 
	        
	        if(j==0)
	    		
	           state="delay";
	           
	           while(state=="delay");
	          
	         
			}
			
			msg3.what=0;
			handler3.sendMessage(msg3);
			
			
		}
	};
	

    private Handler handler2=new Handler(){
 	   @Override
	public void handleMessage(Message msg2){
 		  // super.handleMessage(msg2);
 		   
 		   text.setText("xx"+ msg2.what);
 		  delfileall();
 		   if(msg2.what==0){
 			  pd.dismiss();
					j=0;
					i=0;
					i=0;
					j=0;
					k=0;
					
					Intent x = new Intent();
					/************************************
					 * 
					 */
					Bundle bundle = new Bundle();
				    bundle.putInt("uid", (Integer)  headerid);
				    x.putExtras(bundle);
					x.setClass(Cameradiv.this, Finishv.class);
	               
	               
	               
	                startActivity(x);
					
						finish();
						
 			   
 		   }
 	   }
     };
     
     
	Runnable runnable1=new Runnable(){

		@Override
		public void run() {
			// TODO Auto-generated method stub
			Message msg2= new Message();
			msg2.what=2;
			handler2.sendMessage(msg2);
			if(mediaRecorder != null){ 
	             //停止记录 
	             mediaRecorder.stop(); 

	             //释放该对象 
	             mediaRecorder.release();
	             camera.lock();
	             mediaRecorder = null;
	             } 
			try {
				errt=k/N-GetDuration.getdu("/sdcard/files/a.mp4");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			    
			int n=1;
			for(n=1;n<=j;n++){
				try {
					divz.div(stt[n]-errt,spt[n]-errt,"/sdcard/files/a"+(n-1)+".mp4");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			mergev();
			contv=j;
			if(j==1){
				File fzhang = new File("/sdcard/files/a0.mp4");
				File newFile = new File("/sdcard/files/e.mp4");  
				fzhang.renameTo(newFile);
				}
			
			if(j>=2){
			File fzhang = new File("/sdcard/files/b"+(contv-1)+".mp4");
			File newFile = new File("/sdcard/files/e.mp4");  
			fzhang.renameTo(newFile);
			}
			msg2= new Message();
			msg2.what=0;
			handler2.sendMessage(msg2);
			
		}
		
	};
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	
	public void statt(){
		tim= new Timer();
		k=0;
		tim.schedule(new TimerTask(){
			int flagz=0;
			@Override
			public void run(){
				
				Message msg= new Message();
				k++;
				
				
				if(state=="begin") {if(flagz==0){j++;flagz=1;stt[j]=k/N;}i++;msg.what=i;handler.sendMessage(msg);}
				if(state=="finish") {flagz=0;spt[j]=k/N;tim.cancel();}
				if(state=="delay")  {dt++;if(dt>150){dt=0;flagz=0;state="ready";}}
				if(state=="pause") {if(flagz==1){flagz=0;spt[j]=k/N;}msg.what=i;handler.sendMessage(msg);}
				
				
			}
			
			
		}, 0,1000/N);
	}
	private Handler handler=new Handler(){
		@Override
		public void handleMessage(Message msg3){
			super.handleMessage(msg3);
			
			
			
			
			
			pro1.setProgress(msg3.what);
			if(j!=0)text.setText("      "+msg3.what);
			if (msg3.what==timeno2) {
				 pd = ProgressDialog.show(Cameradiv.this, "Waiting", "Processing，please wait……");  
				 state="finish";	
				 t1.start();	
			}
			
		}
	};
	private void initView(){ 
        
	    //initCamera();
        

       // sfvShow.getHolder().setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS); 
      //  sfvShow.getHolder().setFixedSize(1280, 720); 
       // sfvShow.getHolder().setKeepScreenOn(true); //使摄像头一直保持高亮 
        preview();

		
		if (isPreview) {
			camera.stopPreview();
			camera.release();
			camera = null;
		}
		
		
        
    	
        
        
     }
	private void stopv(){
		 
         state="pause";
         
       
         
	}
	@SuppressLint("NewApi")
	private void starv() throws IOException{
		
		
		
		
		state="begin";
	}
	@SuppressWarnings("deprecation")
	private void preview(){
		// 绑定预览视图
				SurfaceHolder holder = sfvShow.getHolder();
				holder.addCallback(new Callback() {

					@Override
					public void surfaceDestroyed(SurfaceHolder holder) {
						if (camera != null) {
							if (isPreview) {
								camera.stopPreview();
								isPreview = false;
							}
							camera.release();
							camera = null; // 记得释放
						}
						sfvShow = null;
						mSurfaceHolder = null;
						
					}

					@Override
					public void surfaceCreated(SurfaceHolder holder) {
						try {
							initCamera();
							

							Camera.Parameters parameters = camera.getParameters();
							//parameters.setPictureSize(720,480);
							Size s = parameters.getPictureSize();
							  double w = s.width;
							  
							  double h = s.height;
							  
							  Log.i("w",""+w);
							  Log.i("h",""+h);
							  
							  Log.i("w",""+(int) (MainActivity.widthz*(w/h)));
							  Log.i("h",""+MainActivity.widthz);
							  /*
							List<Camera.Size> sizeList = parameters.getSupportedPreviewSizes();
							 parameters.getSupportedPictureSizes(); 
							 
							
							
							int PreviewWidth = 0;
							int PreviewHeight = 0;
							if (sizeList.size() > 1) {
								Iterator<Camera.Size> itor = sizeList.iterator();
								while (itor.hasNext()) {
								Camera.Size cur = itor.next();
								if (cur.width >= PreviewWidth
								&& cur.height >= PreviewHeight) {
								PreviewWidth = cur.width;
								PreviewHeight = cur.height;
								break;
								}
								}
								}
								*/
							  FrameLayout.LayoutParams sfv=	  new FrameLayout.LayoutParams( (int) (MainActivity.widthz*(w/h)),MainActivity.widthz);
							  sfv.gravity=Gravity.CENTER;
					          sfvShow.setLayoutParams( sfv);

								//设置Preview(预览)的尺寸
						  parameters.setPreviewSize(MainActivity.heiz,MainActivity.widthz);
								parameters.setPictureSize((int) (MainActivity.widthz*(w/h)),MainActivity.widthz);
							camera.setPreviewDisplay(holder);
							camera.startPreview();
							isPreview = true;
						} catch (Exception e) {
							e.printStackTrace();
						}
						mSurfaceHolder = holder;
					}

					@Override
					public void surfaceChanged(SurfaceHolder holder, int format,
							int width, int height) {
						 mSurfaceHolder = holder;
					}
				});
				holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
	}




     @TargetApi(Build.VERSION_CODES.GINGERBREAD) @SuppressLint("NewApi") private void initCamera(){ 
        Camera.CameraInfo cameraInfo = new Camera.CameraInfo(); 
       
        cameraCount = Camera.getNumberOfCameras(); // get cameras number   

        for ( int camIdx = 0; camIdx < cameraCount;camIdx++ ) {   
                Camera.getCameraInfo( camIdx, cameraInfo ); // get camerainfo   
                if ( cameraInfo.facing ==Camera.CameraInfo.CAMERA_FACING_BACK) {  
                        // 代表摄像头的方位，目前有定义值两个分别为CAMERA_FACING_FRONT前置和CAMERA_FACING_BACK后置   
                        try {               
                                camera = Camera.open(camIdx);   
                        } catch (RuntimeException e) {   
                                e.printStackTrace();   
                        }   
                } 
        } 
       // camera.unlock(); 
     } 
     public void mergev(){
 		int n=2;
 		
 		
 		
 		 if(j==2)
 		try {
 			merge("/sdcard/files/a0.mp4","/sdcard/files/a1.mp4","/sdcard/files/b1.mp4");

 			 //delFile("/sdcard/files/a"+0+".mp4");
 			//delFile("/sdcard/files/a"+1+".mp4");
 		} catch (IOException e) {
 			// TODO Auto-generated catch block
 			e.printStackTrace();
 		}
 		 if(j>2){
 			 
 			 try {
 					merge("/sdcard/files/a0.mp4","/sdcard/files/a1.mp4","/sdcard/files/b1.mp4");
 					 //delFile("/sdcard/files/a"+0+".mp4");
 					// delFile("/sdcard/files/a"+1+".mp4");
 					
 				} catch (IOException e) {
 					// TODO Auto-generated catch block
 					e.printStackTrace();
 				}
 			 for(n=2;n<=j-1;n++)
 				try {
 					merge("/sdcard/files/b"+(n-1)+".mp4","/sdcard/files/a"+n+".mp4","/sdcard/files/b"+n+".mp4");
 					
 					//delFile("/sdcard/files/a"+n+".mp4");
 					//delFile("/sdcard/files/b"+(n-1)+".mp4");
 				} catch (IOException e) {
 					// TODO Auto-generated catch block
 					e.printStackTrace();
 				}
 				
 		 }
 	}
     public void delfileall(){
    	 int fileno=0;
    	 for(fileno=0;fileno<=j-2;fileno++){
    	 delFile("/sdcard/files/a"+fileno+".mp4");
    	 if(fileno>0)delFile("/sdcard/files/b"+(fileno-1)+".mp4");
    	 }
     }
 	public void delFile(String filePathAndName) {
 		 try {
 		 String filePath = filePathAndName;
 		 filePath = filePath.toString();
 		 java.io.File myDelFile = new java.io.File(filePath);
 		 myDelFile.delete();
 		 }
 		 catch (Exception e) {
 		 System.out.println("������������������������������������������������������������������������");
 		 e.printStackTrace();
 		 }
 		 }
 	 public  void merge(String f1,String f2,String f3) throws IOException {

 	    	

 		
         Movie[] inMovies = new Movie[]{
                 MovieCreator.build(f1),
                 MovieCreator.build(f2)};

         List<Track> videoTracks = new LinkedList<Track>();
         List<Track> audioTracks = new LinkedList<Track>();

         for (Movie m : inMovies) {
             for (Track t : m.getTracks()) {
                 if (t.getHandler().equals("soun")) {
                     audioTracks.add(t);
                 }
                 if (t.getHandler().equals("vide")) {
                     videoTracks.add(t);
                 }
             }
         }

         Movie result = new Movie();

         if (audioTracks.size() > 0) {
             result.addTrack(new AppendTrack(audioTracks.toArray(new Track[audioTracks.size()])));
         }
         if (videoTracks.size() > 0) {
             result.addTrack(new AppendTrack(videoTracks.toArray(new Track[videoTracks.size()])));
         }

         Container out = new DefaultMp4Builder().build(result);

         @SuppressWarnings("resource")
		FileChannel fc = new RandomAccessFile(String.format(f3), "rw").getChannel();
         out.writeContainer(fc);
         fc.close();
      Log.i("","zza");

 	    }
 	public static class GetDuration {
	    public static double getdu(String filename ) throws IOException {
	       
	        @SuppressWarnings("resource")
			IsoFile isoFile = new IsoFile(filename);
	        double lengthInSeconds = (double)
	                isoFile.getMovieBox().getMovieHeaderBox().getDuration() /
	                isoFile.getMovieBox().getMovieHeaderBox().getTimescale();
	        System.err.println(lengthInSeconds);
	        return lengthInSeconds;

	    }

	}
 	public void onBackPressed() {
		  onBackPressed_local(this);
		}
 public void onBackPressed_local(final Activity context) {
	   finish(); 
	    Intent y = new Intent();
		
     y.setClass(Cameradiv.this,MainActivity.class);
     Cameradiv.this.startActivity(y);
     Cameradiv.this.overridePendingTransition(R.anim.activity_open_right,R.anim.activity_close_right); 
}
	
	

}

