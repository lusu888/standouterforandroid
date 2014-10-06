package com.example.standouter;






import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import com.androidquery.util.AQUtility;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.NodeTraversor;
import org.jsoup.select.NodeVisitor;




import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.krislq.sliding.beans.RowItem;
import com.squareup.picasso.Picasso;

import adapter.Contestadapter;
import adapter.contextinfoadapter;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.PixelFormat;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;


public class MainActivity extends Activity implements com.example.standouter.XListView.IXListViewListener {
	private int lsy;
	public  int STATE_NORMAL = 0;
	public int STATE_READY = 1;
	public int STATE_REFRESHING = 2;
	public   SlidingMenu sm ;
	public  Contestadapter adapter;
	public static int LOGINSTATE=0;
	int mTimerflag;
	protected String[] filmimg;

	//public static PopupWindow mPopupWindow=null; 
	public static JSONObject jsonlateoginst;
	public static DefaultHttpClient httpclient;
	public  VideoView videoview;
	public  int isplay=0;
	public  boolean ispause=false;
	public  boolean  isheng;
	private Button play;
	private SeekBar seekbar;
	@SuppressWarnings("deprecation")
	private Gallery filmshow;
	private PopupWindow mPopupWindow;
	public  ProgressBar buffer;
	
	private int vidtime;
    private boolean isChanging=false;//互斥变量，防止定时器与SeekBar拖动时进度冲突   

	static public  int widthz;
	static public  int heiz;
	 static ImageView contest1;
	String jsoncode;
	 public int listno;
	private  final int MAX_SCROLL = 200;  
	private  final float SCROLL_RATIO = 0.5f;// 阻尼系数  
	
	public  XListView listView;
	
	 public FragmentTransaction fragmentTransaction;
    
     public ImageView contestheader;
     public static int totalmessageno;
    RelativeLayout contestinfo;
    static public Context nowcontext;
    private Button left;
	private Button right;
	private PopupWindow mPopupWindowleft;
	private PopupWindow mPopupWindowright;
	
	protected int ytemp2;
   
    /********************
     * 
     */
    
    private int visibleLastIndex = 0;   //最后的可视项索引  
    private int visibleItemCount;       // 当前窗口可见项总数 
    private View loadMoreView; 
   
	private float ytemp;
   
    
    private int[] location={-1,-1};
   
    private String[] titles = new String[5] ;
    private String[] VID = new String[5];

    private String[] images =new String[5];
    private  String[] vidpath=new String[5];
    
    public static  NotificationManager gNotMgr = null;
    private List<RowItem> rowItems;
    private int positionzhang;
private ProgressBar loadMorepb;
private TextView loadmoretext;
private JSONObject jsonobject;
private int[] votecont=new int[5];
private ImageView menuvideo;
private ImageView menubref;
private ImageView photomenu;
private FrameLayout listlayout;
protected int page;
public static  JSONObject jsonmessage;
private ListView lvct;
private VideoView vvct;
private ProgressBar bufferct;
private contextinfoadapter adapter2;
private ArrayList<RowItem> rowItems2;
private  ProgressDialog pd;
private Timer timer2;
private int smalltno;

private String contestdes;
private TextView contesttext;
private JSONObject jsontemp1;
private JSONObject jsontemp2;
private FrameLayout group;
private String[] titlesname=new String[] { "506520975",
        "07026746", "513736767", "516943231","524083761" };
protected int finishflag;

public static  String cc;
public  WindowManager vm;
public   MainActivity contest;
public  boolean ispup;
 static Context context;
View getcurrentview ;
private Timer mTimer=null;
private TimerTask mTimerTask;
private boolean uploadok;
private View loadMoreViewtop;
private ProgressBar loadMorepbtop;

private TextView loadmoretexttop;
private  String contestvdurl;
public static String website="http://demostandouter.zerouno.it";
protected  int firstVisibleItem;

private boolean memCache = false;

private boolean fileCache = true;

	/*************/
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		/********************************************************************/
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		//setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);//
		//getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);//for full screnn
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//
		getWindow().setFormat(PixelFormat.TRANSLUCENT);
		/********************************************************************/
		//setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);//
		
		widthz=this.getWindowManager().getDefaultDisplay().getWidth();
	     heiz=this.getWindowManager().getDefaultDisplay().getHeight(); 
	     
	     
		
		
		
		
	     
	    
	     
		positionzhang=-1;
		mTimerflag=0;
		vm=this.getWindowManager();
		
		contestinfo=(RelativeLayout)this.findViewById(R.id.CONTESTINFO);
		context=this;
		setContentView(R.layout.activity_main);
		
		//getWindow().setBackgroundDrawableResource(R.drawable.splash);
		
		httpclient = SplashActivity.httpclient; // for port 80 requests!
		LOGINSTATE=0;
		
		
		String result=SplashActivity.result;
		Log.i("",result);
        try {
			jsonlateoginst = new JSONObject(result);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		nowcontext=MainActivity.this;
		contest=this;
		 
	   
	     getcurrentview =this.getCurrentFocus(); 
	     
	 
  //  mQueue = VolleyLog.newRequestQueue(this);	
			
	// File file = new File("drawable://" + R.drawable.splash); 
	 //aq.id(mainback).image(file, 300);	
		//Picasso.with(this).load(R.drawable.splash). resize(widthz/4,heiz/4). into(mainback);
  	
	     
	     
		buffer=(ProgressBar)findViewById(R.id.buffer);
        // buffer.setX(widthz/2-80);
		          buffer.setMax(10);
		          FrameLayout.LayoutParams flp=new FrameLayout.LayoutParams( widthz/10,widthz/10);
		               flp.gravity=Gravity.CENTER_HORIZONTAL;
         buffer.setLayoutParams(flp);
        

         buffer.setVisibility(View.GONE);
         //vm=getWindowManager();
         //Contestadapter.initPopuptWindow(context) ;
         bufferct=(ProgressBar)findViewById(R.id.bufferct);
         // buffer.setX(widthz/2-80);
         bufferct.setMax(10);
         bufferct.setVisibility(View.GONE);


         
		
		 videoview=(VideoView)this.findViewById(R.id.videoView);
	       FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(widthz, widthz*9/16);
	       videoview.setLayoutParams(lp);
	       videoview.setMinimumHeight(widthz*9/16);
	       videoview.setMinimumWidth(widthz);
	        
	        videoview.setVisibility(View.INVISIBLE);
	       videoview.setOnTouchListener(new View.OnTouchListener() {
				
				
				

				@Override
				public boolean onTouch(View v, MotionEvent event) {
					// TODO Auto-generated method stub
					finishflag=0;
					Log.i("","ssd");
					// TODO Auto-generated method stubp
					if(isheng){
						if(mTimer==null&&mTimerflag==0){ 
					 	  	//----------定时器记录播放进度---------//    
					 	        mTimer = new Timer();    
					 	        mTimerTask = new TimerTask() {    
					 	            @Override    
					 	            public void run() {     
					 	                if(isChanging==true) {   
					 	                    return;    
					 	                }  
					 	                seekbar.setMax(videoview.getDuration());//设置进度条  
					 	               try{ seekbar.setProgress(videoview.getCurrentPosition());  
					 	                }
					 	                catch(Exception e)
					 	               {
					 	                  Log.i("currett","error") ;// error catch 1
					 	                 }
					 	            }    
					 	        };   
					 	        mTimer.schedule(mTimerTask, 0, 10);
					 	       mTimerflag=1;
					 	  	   }

						
						if(isplayheng==1) {

							getPopupWindowInstance();  
				            mPopupWindow.showAtLocation(getcurrentview, Gravity.BOTTOM, 0, 0);
				            getPopupWindowInstanceleft() ;
				            mPopupWindowleft.showAtLocation(getcurrentview, Gravity.LEFT, 0, 0);
				            getPopupWindowInstanceright();
				            mPopupWindowright.showAtLocation(getcurrentview, Gravity.RIGHT, 0, 0);
							videoview.pause();
							Log.i("","ssd"+isplayheng);
							isplayheng=0;
							play.setBackgroundResource(R.drawable.play);

							
							
						}else
						 {
							mPopupWindow.dismiss();
							mPopupWindowleft.dismiss();
							 mPopupWindowright.dismiss();
						    getsharePopupWindowInstance();  
							videoview.start();
							Log.i("","ssd"+isplayheng);
							isplayheng=1;
							play.setBackgroundResource(R.drawable.pause);

							
						}
						return false;
					
				}
					return false;
				}
			});
	        
	        videoview.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
				
				

				@Override
				public void onCompletion(MediaPlayer mp) {
					
					finishflag=1;
					vidtime=0;
					
					vvct.stopPlayback();
					vvct.setVisibility(View.INVISIBLE);
					bufferct.setVisibility(View.INVISIBLE);
					if(isheng==false){
						getPopupsmallWindowInstanceleft();
			            mPopupsmallWindowleft.showAtLocation(getcurrentview, Gravity.LEFT, 0,ytemp2- widthz/16*9-widthz/16);
			            getPopupsmallWindowInstanceright();
			            mPopupsmallWindowright.showAtLocation(getcurrentview, Gravity.RIGHT, 0,ytemp2- widthz/16*9-widthz/16);
						// TODO Auto-generated method stub
						 lvct.setVisibility(View.VISIBLE);
						group.setVisibility(View.VISIBLE);
			    		  videoview.setY(ytemp);
			    		  videoview.setLayoutParams(new FrameLayout.LayoutParams( widthz,widthz/16*9));
						// TODO Auto-generated method stub
			    		videoview.stopPlayback();
						videoview.setVisibility(View.INVISIBLE);
						 vvct.setY(ytemp);
			    		  vvct.setLayoutParams(new FrameLayout.LayoutParams( widthz,widthz/16*9));
			    		  isplay=0;
						}else{
							getPopupWindowInstance();  
						
				            mPopupWindow.showAtLocation(getcurrentview, Gravity.BOTTOM, 0, 0);
				            getPopupWindowInstanceleft() ;
				            mPopupWindowleft.showAtLocation(getcurrentview, Gravity.LEFT, 0, 0);
				            getPopupWindowInstanceright();
				            mPopupWindowright.showAtLocation(getcurrentview, Gravity.RIGHT, 0, 0);
							videoview.seekTo(100);
							isplayheng=0;
							play.setBackgroundResource(R.drawable.play);

						}
						
						

					}
			});
	        videoview.setOnPreparedListener(new OnPreparedListener(){

				@Override
				public void onPrepared(MediaPlayer arg0) {
					// TODO Auto-generated method stub
					Toast toast = Toast.makeText(MainActivity.this,
	    	                "ok",
	    	                Toast.LENGTH_SHORT);
					buffer.setVisibility(View.INVISIBLE);
	    	        toast.setGravity(Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 0);
	    	        toast.show();
				}
	        	
	        });
          
	        vvct=(VideoView)this.findViewById(R.id.vvct);
		       FrameLayout.LayoutParams lp2 = new FrameLayout.LayoutParams(widthz, widthz*9/16);
		       vvct.setLayoutParams(lp2);
		       vvct.setMinimumHeight(widthz*9/16);
		       vvct.setMinimumWidth(widthz);
		        
		       vvct.setVisibility(View.INVISIBLE);
		        
		       vvct.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
					
					

					@Override
					public void onCompletion(MediaPlayer mp) {
						
						finishflag=1;
						vidtime=0;
						videoview.stopPlayback();
						videoview.setVisibility(View.INVISIBLE);
						bufferct.setVisibility(View.INVISIBLE);
						if(isheng==false){
						// TODO Auto-generated method stub
					 vvct.stopPlayback();
					vvct.setVisibility(View.INVISIBLE);
						 lvct.setVisibility(View.VISIBLE);
						group.setVisibility(View.VISIBLE);
			    		  videoview.setY(ytemp);
			    		  videoview.setLayoutParams(new FrameLayout.LayoutParams( widthz,widthz/16*9));
						// TODO Auto-generated method stub
						
						 vvct.setY(ytemp);
			    		  vvct.setLayoutParams(new FrameLayout.LayoutParams( widthz,widthz/16*9));
			    		  isplay=0;
						}else{
							play.setBackgroundResource(R.drawable.play);

							vvct.seekTo(1500);
							isplayheng=0;
							getPopupWindowInstance();  
				            mPopupWindow.showAtLocation(getcurrentview, Gravity.BOTTOM, 0, 0);
				            

						}
						
						
						
					}
				});
		       vvct.setOnPreparedListener(new OnPreparedListener(){

					@Override
					public void onPrepared(MediaPlayer arg0) {
						// TODO Auto-generated method stub
						Toast toast = Toast.makeText(MainActivity.this,
		    	                "ok",
		    	                Toast.LENGTH_SHORT);
						bufferct.setVisibility(View.INVISIBLE);
		    	        toast.setGravity(Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 0);
		    	        toast.show();
					}
		        	
		        });
			
		       vvct.setOnTouchListener(new View.OnTouchListener() {
					
					
					

					@Override
					public boolean onTouch(View v, MotionEvent event) {
						// TODO Auto-generated method stub
						
						if(mTimer==null&&mTimerflag==0){ 
					 	  	//----------定时器记录播放进度---------//    
					 	        mTimer = new Timer();    
					 	        mTimerTask = new TimerTask() {    
					 	            @Override    
					 	            public void run() {     
					 	                if(isChanging==true) {   
					 	                    return;    
					 	                }  
					 	                seekbar.setMax(vvct.getDuration());//设置进度条  
					 	                try{ seekbar.setProgress(vvct.getCurrentPosition());  
					 	                }
					 	                catch(Exception e)
					 	               {
					 	                  Log.i("currett","error") ;// error catch 1
					 	                 }
					 	            }    
					 	        };   
					 	        mTimer.schedule(mTimerTask, 0, 10);
					 	       mTimerflag=1;
					 	  	   }

						finishflag=0;
						Log.i("","ssd");
						// TODO Auto-generated method stubp
						if(isheng){
							if(isplayheng==1) {
								getPopupWindowInstance();  
					            mPopupWindow.showAtLocation(v, Gravity.BOTTOM, 0, 0);
					            getPopupWindowInstanceleft() ;
					            
								vvct.pause();
								Log.i("","ssd"+isplayheng);
								isplayheng=0;
								play.setBackgroundResource(R.drawable.play);

								
							}else
							 {
								 mPopupWindow.dismiss();
								 

								vvct.start();
								Log.i("","ssd"+isplayheng);
								isplayheng=1;
								play.setBackgroundResource(R.drawable.pause);

							}
							return false;
						
					}
						return false;
					}
				});
		
		
				
			listlayout=(FrameLayout)this.findViewById(R.id.listlayout);
		 listView = (XListView)this.findViewById(R.id.listView);
		 listView.setPullLoadEnable(true);
		 listView.setXListViewListener(this);
		 listView.setDivider(null); 
		 
		 
		  loadMoreView = getLayoutInflater().inflate(R.layout.load_more, null);  
		   loadMoreViewtop = getLayoutInflater().inflate(R.layout.load_more, null); 
		   
		   
	     
	      loadMorepb=(ProgressBar)loadMoreView.findViewById(R.id.progressBarload);
	      loadmoretext=(TextView)loadMoreView.findViewById(R.id.loadmoretext);
	      
	      loadMorepbtop=(ProgressBar)loadMoreViewtop.findViewById(R.id.progressBarload);
		  loadmoretexttop=(TextView)loadMoreViewtop.findViewById(R.id.loadmoretext);
	      
	      loadMorepb.setVisibility(View.INVISIBLE);
	      //listView.addHeaderView(loadMoreViewtop);
	      //listView.addFooterView(loadMoreView);  
	  
	     
	      
	      
		 
		group=(FrameLayout)this.findViewById(R.id.headergroup);
	     contestheader=(ImageView)this.findViewById(R.id.header);
	     contestheader.setLayoutParams(new FrameLayout.LayoutParams( widthz,widthz/4
                 ));

	     //Picasso.with(this).load(R.drawable.cococolaheader). resize(widthz, widthz/4). into(contestheader);
	     
	     ImageView menuheader=(ImageView)this.findViewById(R.id.menuheader);
	     menuheader.setLayoutParams(new FrameLayout.LayoutParams( widthz/4*124/180,widthz/4
                 ));
	     menuheader.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				sm.toggle();
				getPopupsmallWindowInstanceleft();
	            getPopupsmallWindowInstanceright();
	   	     getsharePopupsmallWindowInstance() ;
	   	  lvct.setVisibility(View.VISIBLE);
			group.setVisibility(View.VISIBLE);
			// TODO Auto-generated method stub
  		videoview.stopPlayback();
			videoview.setVisibility(View.INVISIBLE);
			vvct.stopPlayback();
			vvct.setVisibility(View.INVISIBLE);
			isplay=0;

			}
		});

	    Picasso.with(this).load(R.drawable.menuwhite). resize( widthz/4*124/180, widthz/4). into(menuheader);
	     
	     
	     ImageView menuheaderright=(ImageView)this.findViewById(R.id.menuheaderright);
	     menuheaderright.setLayoutParams(new FrameLayout.LayoutParams( widthz/4*124/180,widthz/4
                 ));
	     menuheaderright.setX(widthz-widthz/4*124/180);
	     menuheaderright.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				sm.showSecondaryMenu();
				getPopupsmallWindowInstanceleft();
	            getPopupsmallWindowInstanceright();
	   	     getsharePopupsmallWindowInstance() ;
	   	  lvct.setVisibility(View.VISIBLE);
			group.setVisibility(View.VISIBLE);
			// TODO Auto-generated method stub
		videoview.stopPlayback();
			videoview.setVisibility(View.INVISIBLE);
			vvct.stopPlayback();
			vvct.setVisibility(View.INVISIBLE);
			isplay=0;
			}
		});


	     Picasso.with(this).load(R.drawable.menuwhite). resize( widthz/4*124/180, widthz/4). into(menuheaderright);
	     Log.i("SS","SD");
	    
	     initslidingmenu();
	     Log.i("SS","SD");
	     
	     listlayout.setVisibility(View.VISIBLE);	

		   
			//contestinfo.setVisibility(View.GONE);
			pd = ProgressDialog.show(MainActivity.this, "dowloading", "dowloading，please wait……");
			cc="freestyle";

	    	
			menurightc.setBackgroundColor(0xFFff3200);
			


			Picasso.with(MainActivity.this).load(R.drawable.standouterheader). resize(widthz, widthz/4). into(contestheader);
			// loadlistview(website+"/video/search?ss=contest&so=most_voted&sp=22");
		     //loadcontestinfo(website+"/contest/contestinfo?cc=oldwildwest");
			 new Thread(runnable0).start();
			
		     
		     contestheader.setTag(R.drawable.standouterheader);	
		    
	     //loadlistview(website+"/video/search?ss=contest&so=most_voted&sp=22");
	     
	     Timer timer = new Timer(true);
	        
         timer.schedule(new timerTask(), 1000, 1000);
         
        timer2 =new Timer(true);
        
        
     	//images =images2;
        
		
	
	}
	public void loadcontestinfo(String url){
		positionzhang=-1;
		
		
		 lvct=(ListView)this.findViewById(R.id.lvct);
			String contestphotourl = null;
	        contestdes= null;
	       contestvdurl=null;
	       JSONObject jsonobject2 = null ;
	       contesttext=(TextView)this.findViewById(R.id.contesttext);
		if(url.equals("first")){
			try {
				String s=Login.readFileSdcardFile("/sdcard/json2.txt");
				//Log.i("",s);
				 jsonobject2 = new JSONObject(s);
				contestphotourl=website+"/"+jsonobject2.getString("playerOverlayUrl");
				 contestdes=jsonobject2.getString("description");
	            contestvdurl =jsonobject2.getString("spotVideoUrl");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else{
			String jsoncodecontest = url;
			try {
				
				 jsonobject2 =Json.getJson(jsoncodecontest,httpclient);
				 contestphotourl=website+"/"+jsonobject2.getString("playerOverlayUrl");
				 contestdes=jsonobject2.getString("description");
	            contestvdurl =jsonobject2.getString("spotVideoUrl");
	            
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
        if(cc.equals("freestyle"))
		{
        	jsontemp2=jsonobject2;
            contestvdurl="RAIL0cx5";
		}
        
        
        try {
			uploadok=jsontemp2.getBoolean("uploadEnabled");
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
        
		
		
		for(int i=0;i<1;i++){
      		try {
					images[i]="http://a.jwpcdn.com/thumbs/"+contestvdurl +"-320.jpg";
					titles[i]=jsonobject2.getString("name");
					titles[i]=jsonobject2.getString("name");
					vidpath[i]="http://content.bitsontherun.com/videos/"+contestvdurl+"-480.mp4";
					VID[i]=jsonobject2.getString("contestId");
					votecont[i]=0;
					
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
      	}
		
		 rowItems2 = new ArrayList<RowItem>();
	       for(int i=0;i<1;i++){ 
	            RowItem item = new RowItem(images[i], titles[i],titlesname[i], vidpath[i],VID[i],votecont[i],false);
	            rowItems2.add(item);
	        }
	        
	        adapter2 = new  contextinfoadapter(this, rowItems2,this);
	       
	       
	        
	        
		
	}
	public void lvcontestint(){
		 lvct.setOnItemClickListener(new AdapterView.OnItemClickListener(){

				
		     

				
				@Override
				public void onItemClick(AdapterView<?> parent, View view, int position,
			            long id) {
					
					finishflag=0;
					lvct.getLocationInWindow(location);
					int Pos[]={-1,-1};
			    	view.getLocationOnScreen(Pos);
			    	ytemp=Pos[1]-location[1];
					// TODO Auto-generated method stub
					
			    		if(position!=positionzhang) vidtime=0;
			    		Log.i("",""+position+","+positionzhang);
			    		
				    	int a = 0;			        
				        if(isplay==0){
				        	
				    		if(position!=positionzhang) {vidtime=0;
				    		Log.i("",""+position+","+positionzhang);
				    		isplay=1;
				    		
					        Toast toast = Toast.makeText(view.getContext(),
					                "Item " + (position + 1+a*5) + ": " + rowItems2.get(position)+"Position:"+Pos[1]+"xx"+location[1],
					                Toast.LENGTH_SHORT);
					        toast.setGravity(Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 0);
					        toast.show();
					        
					        Uri uri = Uri.parse(adapter2.getItemdesc(position));
					       vvct.setVideoURI(uri);
					       adapter2.getItemdesc(position);
					        //MainActivity.vidpath=vidpath[position+a*5];
					       vvct.setMinimumHeight(MainActivity.widthz*9/16);
					       vvct.setMinimumWidth(MainActivity.widthz);
					       vvct.requestFocus();
					       vvct.setY(Pos[1]-location[1]);
					      bufferct.setY(Pos[1]- widthz/10);
					      vvct.seekTo(vidtime);
					      vvct.start();
					      vvct.setVisibility(View.VISIBLE);
					       bufferct.setVisibility(View.VISIBLE);
				    		}else{
				    			vvct.setY(Pos[1]-location[1]);
				    			vvct.start();
							    vvct.setVisibility(View.VISIBLE);
							    isplay=1;

				    		} 
					        
					        
					        positionzhang=position;
					        
				    	}
				    	else{
				    		if(position==positionzhang)
				    		{
				    			vvct.pause();
				    			vidtime=vvct.getCurrentPosition();
							//videoview.setVisibility(View.INVISIBLE);
				    			isplay=0;
				    		}
				    		else
				    		{
				    			vidtime=0;
				        		Log.i("",""+position+","+positionzhang);
				        		isplay=1;
				        		
				    	        Toast toast = Toast.makeText(view.getContext(),
				    	                "Item " + (position + 1+a*5) + ": " + rowItems.get(position)+"Position:"+Pos[1],
				    	                Toast.LENGTH_SHORT);
				    	        toast.setGravity(Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 0);
				    	        toast.show();
				    	        
				    	        Uri uri = Uri.parse(adapter2.getItemdesc(position));
				    	        vvct.setVideoURI(uri);
						        adapter2.getItemdesc(position);
				    	       //MainActivity.vidpath=vidpath[position+a*5];
						        vvct.setMinimumHeight(widthz*9/16);
						        vvct.setMinimumWidth(widthz);
						        vvct.requestFocus();
						        vvct.setY(Pos[1]-location[1]);
				    	        bufferct.setY(Pos[1]- widthz/10);
				    	        vvct.seekTo(vidtime);
				    	        vvct.start();
				    	        vvct.setVisibility(View.VISIBLE);
				    	      bufferct.setVisibility(View.VISIBLE);
				    	        
				    	        //setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
				    	        positionzhang=position;
				    		}
					}

				        
				
	        	
	        }});
	}
	/*
	 public static  void getPopupWindowInstance(Context contextnow) {  
	        if (null != mPopupWindow) {  
	            mPopupWindow.dismiss();  
	            return;  
	        } else {  
	        	
	            initPopuptWindow(contextnow);  
	        }  
	    }  
	
	 public static  void initPopuptWindow(final Context contextnow){
		 LayoutInflater layoutInflater = LayoutInflater.from(contextnow);  
	     View popupWindow = layoutInflater.inflate(R.layout.sharepoup, null);  
	     mPopupWindow = new PopupWindow(popupWindow,widthz*2/5, widthz/5); 
	        mPopupWindow.setAnimationStyle(R.style.AnimationFade); 
	        facebookshare=(ImageButton)popupWindow.findViewById(R.id.sharefb);
	       facebookshare.setLayoutParams(new LinearLayout.LayoutParams( widthz/5,widthz/5
              ));
	       facebookshare.setScaleType(ImageView.ScaleType.FIT_XY);
	       facebookshare.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
						contextnow);
		 
					// set title
					alertDialogBuilder.setTitle("Share on FaceBook");
		 
					// set dialog message
					alertDialogBuilder
						.setMessage("You will share it on FaceBook!")
						.setCancelable(false)
						.setPositiveButton("Yes",new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,int id) {
								// if this button is clicked, close
								// current activity
								Contestadapter .fbsharecode();
							}
						  })
						.setNegativeButton("No",new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,int id) {
								// if this button is clicked, just close
								// the dialog box and do nothing
								dialog.cancel();
							}
						});
		 
						// create alert dialog
						AlertDialog alertDialog = alertDialogBuilder.create();
		 
						// show it
						alertDialog.show();
				
			}
		});
	       
	      wpshare=(ImageButton)popupWindow.findViewById(R.id.sharewp);
	       wpshare.setLayoutParams(new LinearLayout.LayoutParams( widthz/5,widthz/5
	                 ));
	       wpshare.setScaleType(ImageView.ScaleType.FIT_XY);
	        //mPopupWindow.getBackground().setAlpha(100)
	       wpshare.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				onClickWhatsApp(v);
			}
		});
	        
	        
	  
	        vm.getDefaultDisplay().getWidth();  
	        vm.getDefaultDisplay().getHeight();  
	        mPopupWindow.getWidth();  
	        mPopupWindow.getHeight();  
	 }
	 
 	/*********************************************************************************
	 * 
	 * @param jsoncode2
	 */
	
	
	
	
	
	
	
	void loadlistview(String jsoncode2){
		

		if(jsoncode2.equals("first")){
			try {
				String s=Login.readFileSdcardFile("/sdcard/json.txt");
				//Log.i("",s);
				jsonobject=new JSONObject(s);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else{
		jsoncode=jsoncode2;
		jsonobject=Json.getJson(jsoncode,httpclient);
		
		}
		listno=20;
		if(cc.equals("freestyle"))
		jsontemp1=jsonobject;

		try {
			listno=jsonobject.getInt("totalResults");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Log.i("","NO IS "+listno);
		
     
		if(listno<5) smalltno=listno;
		else smalltno=5;
		if(smalltno!=0){
		for(int i=0;i<smalltno;i++){
      		try {
					images[i]=jsonobject.getJSONArray("items").getJSONObject(i).getString("imageUrl320");
					titles[i]=jsonobject.getJSONArray("items").getJSONObject(i).getString("ownerName")+" "+jsonobject.getJSONArray("items").getJSONObject(i).getString("ownerSurname");
					titlesname[i]=jsonobject.getJSONArray("items").getJSONObject(i).getString("title");
					vidpath[i]=jsonobject.getJSONArray("items").getJSONObject(i).getString("videoUrl320");
					VID[i]=jsonobject.getJSONArray("items").getJSONObject(i).getString("id");
					votecont[i]=jsonobject.getJSONArray("items").getJSONObject(i).getInt("votesCount");
					
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
      	}
      	
     
       rowItems = new ArrayList<RowItem>();
       for(int i=0;i<smalltno;i++){ 
            RowItem item = new RowItem(images[i], titles[i],titlesname[i], vidpath[i],VID[i],votecont[i],true);
            rowItems.add(item);
        }
       }
		else{
		rowItems = new ArrayList<RowItem>();
       RowItem item = new RowItem("novideo", "novideo", "novideo","novideo","novideo",0,true);
       rowItems.add(item);
		}
        
        adapter = new  Contestadapter(this, rowItems,this);
        //listView.setAdapter(adapter);
       
        
	}
	
	 private Handler handlerlv=new Handler(){
	 	   @Override
		public void handleMessage(Message msglv){
	 		  // super.handleMessage(msg2);
	 		   if( msglv.what==1){
	 			   
	 			  if(listno>3){
	 	          	 loadmoretext.setText("load more");
	 	       	 }else{
	 	       		 
	 	             loadmoretext.setText("");
	 	                  
	 	       	 }
	 			  //listView.setAdapter(null);
	 		     listView.setAdapter(adapter);
	 		    contesttext.setText("\n"+convertNodeToText(Jsoup.parse(contestdes))); 
	 			  lvct.setAdapter(adapter2);
	 			 lvcontestint();
	 		    listviewinit();
	 		   getPopupWindowInstance();
	 		  SimpleDateFormat   sDateFormat   =   new   SimpleDateFormat("dd-mm-yyyy   hh:mm:ss");     
	 	        date   =   sDateFormat.format(new   java.util.Date()); 
	 		  //listView.setSelection(1);
	 	       
	 		  
	 		  //listView.setSelection(1);
	 			pd.dismiss(); 
	 			onLoad();
	 		   }
	 		   
	 		   
	 		   
	 	   }
	     };
	     
	     public static String convertNodeToText(Element element)
	     {
	         final StringBuilder buffer = new StringBuilder();

	         new NodeTraversor(new NodeVisitor() {
	             boolean isNewline = true;

	             public void head(Node node, int depth) {
	                 if (node instanceof TextNode) {
	                     TextNode textNode = (TextNode) node;
	                     String text = textNode.text().replace('\u00A0', ' ').trim();                    
	                     if(!text.isEmpty())
	                     {                        
	                         buffer.append(text);
	                         isNewline = false;
	                     }
	                 } else if (node instanceof Element) {
	                     Element element = (Element) node;
	                     if (!isNewline)
	                     {
	                         if((element.isBlock() || element.tagName().equals("br")))
	                         {
	                             buffer.append("\n\n\n");
	                             isNewline = true;
	                         }
	                     }
	                 }                
	             }

	             @Override
	             public void tail(Node node, int depth) {                
	             }                        
	         }).traverse(element);        

	         return buffer.toString();               
	     }

	protected View handler;
	private Handler handlersocll=new Handler(){
	 	   @Override
		public void handleMessage(final Message msg){
	 		  // super.handleMessage(msg2);
	 		 
	 		   
					// TODO Auto-generated method stub
					 if(msg.what<listno){ 
		                   loadData();  
		                     
		                   adapter.notifyDataSetChanged(); //数据集变化后,通知adapter  
		                   listView.setSelection(visibleLastIndex - visibleItemCount + 1); //设置选中项  
		                   //loadmoretext.setText("Load More");
		                   //listView.setfootstate(STATE_READY);
		                   } else{
		                	 if(listno>3){
		                		 //listView.setfootstate(STATE_NORMAL);
		                   	 //loadmoretext.setText("All Loaded");
		                	 }else{
		                		 
		                		 //listView.setfootstate(STATE_NORMAL);
				                   
		                	 }
		                   }
					    onLoad();
		                 //  loadMorepb.setVisibility(View.INVISIBLE);
				
	 			   
	 		   
	 	   }
	     };

	private int isplayheng;
	private LinearLayout menurightc;
	private String date;
	
	
	public void onRefresh() {
		Log.i("LOADMORE", "loading...");  
        loadmoretexttop.setText("Loadind...");
        loadMorepbtop.setVisibility(View.VISIBLE);
        
        Message msg =new Message();
        msg.what=1;
        handlersoclltop.sendMessageDelayed(msg, 200);
	}

	private void onLoad() {
		listView.stopRefresh();
		listView.stopLoadMore();
		listView.setRefreshTime(date);
	}
	public void onLoadMore() {
		int itemsLastIndex = adapter.getCount() ;    //数据集最后一项的索引  
        final int lastIndex = itemsLastIndex + 1;
		
		 Log.i("LOADMORE", "loading...");  
         Log.i("",lastIndex+"");
         Log.i("","gf"+listno);
        
       // listView.setfootstate(STATE_REFRESHING );
         //loadmoretext.setText("Loadind...");
        // loadMorepb.setVisibility(View.VISIBLE);
         Message msg =new Message();
         msg.what=lastIndex;
         handlersocll.sendMessageDelayed(msg, 1000);
	}
	
	public void listviewinit(){
		 if(cc.equals("universal")){
	        	Picasso.with(this).load(R.drawable.briefb). resize(widthz/4, widthz/4). into(menubref);
	        	 Picasso.with(this).load(R.drawable.uploadb). resize(widthz/4, widthz/4). into(photomenu);
	        }
	        else{
	        	Picasso.with(this).load(R.drawable.brief). resize(widthz/4, widthz/4). into(menubref);
	        	 Picasso.with(this).load(R.drawable.upload). resize(widthz/4, widthz/4). into(photomenu);
	        }
listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){

			
     

			
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
		            long id) {
				
				
				finishflag=0;
				listView.getLocationInWindow(location);
				// TODO Auto-generated method stub

		    		if(position!=positionzhang) vidtime=0;
		    		Log.i("",""+position+","+positionzhang);
		    		int Pos[]={-1,-1};
			    	view.getLocationOnScreen(Pos);
			    	ytemp=Pos[1]-location[1];
			    	ytemp2=Pos[1];
			    	int a = 0;			        
			        if(isplay==0){
			        	getPopupsmallWindowInstanceleft();
			            getPopupsmallWindowInstanceright();
			   	     getsharePopupsmallWindowInstance() ;

			        	
			    		if(position!=positionzhang){ vidtime=0;
			    		Log.i("",""+position+","+positionzhang);
			    		isplay=1;
			    		
				        Toast toast = Toast.makeText(view.getContext(),
				                "Item " + (position + 1+a*5) + ": " + rowItems.get(position-1)+"Position:"+Pos[1]+"xx"+location[1],
				                Toast.LENGTH_SHORT);
				        toast.setGravity(Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 0);
				        toast.show();
				        
				        Uri uri = Uri.parse(adapter.getItemdesc(position-1));
				       videoview.setVideoURI(uri);
				       adapter.getItemdesc(position-1);
				        //MainActivity.vidpath=vidpath[position+a*5];
				       videoview.setMinimumHeight(MainActivity.widthz*9/16);
				        videoview.setMinimumWidth(MainActivity.widthz);
				        videoview.requestFocus();
				        videoview.setY(Pos[1]-location[1]);
				      buffer.setY(ytemp2- widthz/10);
				       videoview.seekTo(vidtime);
				       videoview.start();
				         videoview.setVisibility(View.VISIBLE);
				       buffer.setVisibility(View.VISIBLE);
			    		}else{
			    			videoview.start();
			    			
			    		   videoview.setY(Pos[1]-location[1]);
			    			isplay=1;
			    			 videoview.setVisibility(View.VISIBLE);
			    		}
				        
				        
				        positionzhang=position;
			    	}
			    	else{
			    		if(position==positionzhang)
			    		{
			    			getPopupsmallWindowInstanceleft();
				            mPopupsmallWindowleft.showAtLocation(getcurrentview, Gravity.LEFT, 0,Pos[1]- widthz/16*9-widthz/16);
				            getPopupsmallWindowInstanceright();
				            mPopupsmallWindowright.showAtLocation(getcurrentview, Gravity.RIGHT, 0,Pos[1]- widthz/16*9-widthz/16);
			    			videoview.pause();
			    			vidtime=videoview.getCurrentPosition();
						//videoview.setVisibility(View.INVISIBLE);
			    			isplay=0;
			    		}
			    		else
			    		{
			    			getPopupsmallWindowInstanceleft();
				            getPopupsmallWindowInstanceright();
					   	     getsharePopupsmallWindowInstance() ;

				        	 
			    			vidtime=0;
			        		Log.i("",""+position+","+positionzhang);
			        		isplay=1;
			         		
			    	        Toast toast = Toast.makeText(view.getContext(),
			    	                "Item " + (position + 1+a*5) + ": " + rowItems.get(position)+"Position:"+Pos[1],
			    	                Toast.LENGTH_SHORT);
			    	        toast.setGravity(Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 0);
			    	        toast.show();
			    	        
			    	        Uri uri = Uri.parse(adapter.getItemdesc(position-1));
					        videoview.setVideoURI(uri);
					        adapter.getItemdesc(position-1);
			    	       //MainActivity.vidpath=vidpath[position+a*5];
			    	       videoview.setMinimumHeight(widthz*9/16);
			    	        videoview.setMinimumWidth(widthz);
			    	        videoview.requestFocus();
			    	       videoview.setY(Pos[1]-location[1]);
			    	        buffer.setY(ytemp2-widthz/10);
			    	       videoview.seekTo(vidtime);
			    	       videoview.start();
			    	       videoview.setVisibility(View.VISIBLE);
			    	      buffer.setVisibility(View.VISIBLE);
			    	        
			    	        //setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
			    	        positionzhang=position;
			    		}
				}

			        
			
        	
        }});

        listView.setOnScrollListener(new OnScrollListener(){

        	 @Override  
        	    public void onScroll(AbsListView view, int firstVisibleItem, final int visibleItemCount, int totalItemCount) {  
        	        MainActivity.this.visibleItemCount = visibleItemCount;  
        	        visibleLastIndex = firstVisibleItem + visibleItemCount - 1; 
        	        //Contestadapter.mPopupWindow.dismiss();
        	        //Contestadapter.isup=false; 
        	        MainActivity.this.firstVisibleItem=firstVisibleItem;
        	        
        	    }  

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {  


		        int itemsLastIndex = adapter.getCount() ;    //数据集最后一项的索引  
		        final int lastIndex = itemsLastIndex + 1;             //加上底部的loadMoreView项  
		        /*
		        if (scrollState == OnScrollListener.SCROLL_STATE_IDLE && visibleLastIndex == lastIndex) {  
		            //如果是自动加载,可以在这里放置异步加载数据的代码  
		            Log.i("LOADMORE", "loading...");  
		            Log.i("",lastIndex+"");
		            Log.i("","gf"+listno);
		           
		           listView.setfootstate(STATE_REFRESHING );
		            //loadmoretext.setText("Loadind...");
		           // loadMorepb.setVisibility(View.VISIBLE);
		            Message msg =new Message();
		            msg.what=lastIndex;
		            handlersocll.sendMessageDelayed(msg, 1000);
					//handlersocll.sendMessage(msg);
		            
		           // loadMoreButton.setText("loading...");   //设置按钮文字loading  
		            
		                   
		                   // loadMoreButton.setText("load more");    //恢复按钮文字 
		                   
		                
		           
		        }
		        */
		        /*
		        if (scrollState == OnScrollListener.SCROLL_STATE_IDLE && MainActivity.firstVisibleItem == 0&&listView.getheanderstate()==2) {  
		            //如果是自动加载,可以在这里放置异步加载数据的代码  
		            
		        	Log.i("LOADMORE", "loading...");  
		            loadmoretexttop.setText("Loadind...");
		            loadMorepbtop.setVisibility(View.VISIBLE);
		            Message msg =new Message();
		            msg.what=1;
		            handlersoclltop.sendMessageDelayed(msg, 1000);
		            
					//handlersocll.sendMessage(msg);
		            
		           // loadMoreButton.setText("loading...");   //设置按钮文字loading  
		            
		                   
		                   // loadMoreButton.setText("load more");    //恢复按钮文字 
		                   
		                
		           
		        }
		        */
		        switch(scrollState) { 
	            case SCROLL_STATE_FLING: 
	                //Log.i("Scroll State","滾動中...");
	            	getPopupsmallWindowInstanceleft();
		            getPopupsmallWindowInstanceright();
			   	     getsharePopupsmallWindowInstance() ;

	                videoview.stopPlayback();
	                videoview.setVisibility(View.INVISIBLE);
	                buffer.setVisibility(View.INVISIBLE);
					//setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//
					//setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);
	               isplay=0;
	                break; 
	            case SCROLL_STATE_IDLE: 
	               // Log.i("Scroll State","滾動停止...");
	            	
	                break; 
	            case SCROLL_STATE_TOUCH_SCROLL: 
	               // Log.i("Scroll State","手指滾動...");
	            	getPopupsmallWindowInstanceleft();
		            getPopupsmallWindowInstanceright();
			   	     getsharePopupsmallWindowInstance() ;

	            	videoview.stopPlayback();
	            	 videoview.setVisibility(View.INVISIBLE);
	            	buffer.setVisibility(View.INVISIBLE);
					//setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//
					//setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);
	            	isplay=0;
	                break; 
	            }
		        
		        
		    }  
        	
        });
        
	}
  /**
 * @throws JSONException *****************************************************
   * 
   */
	private Handler handlersoclltop=new Handler(){
	 	   @Override
		public void handleMessage(final Message msg){
	 		  // super.handleMessage(msg2);
	 		   
					// TODO Auto-generated method stub
					 if(msg.what==1){ 
							pd = ProgressDialog.show(MainActivity.this, "dowloading", "dowloading，please wait……");

						 if(cc.equals("freestyle")){
				            	new Thread(runnable1).start();
				            	 Log.i("",cc);
				            }
						 if(cc.equals("universal")){
				            	new Thread(runnable2).start();
				            	 Log.i("",cc);
				            }
				            if(cc.equals("goandfun")){
				            	new Thread(runnable3).start();
				            	 Log.i("",cc);
				            }
				            if(cc.equals("whoodbrooklyn")){
				            	new Thread(runnable4).start();
				            	 Log.i("",cc);
				            }
				            if(cc.equals("metro")){
				            	new Thread(runnable5).start();
				            	 Log.i("",cc);
				            }
			            	 Log.i("",cc);

		                   } 
		 			 

					 
		                 //  onLoad();
	 			   
	 		   
	 	   }
	     };
	
	void initslidingmenu() {
		sm = new SlidingMenu(this);
		sm.setMode(SlidingMenu.LEFT_RIGHT);
	    sm.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN); 
	    
        sm.setShadowWidth(30);
        sm.setShadowDrawable(R.drawable.shadow);
        sm.setBehindOffset(widthz/4*3);
        sm.setFadeDegree(0.35f);
        //设置slding menu的几种手势模式
        //TOUCHMODE_FULLSCREEN 全屏模式，在content页面中，滑动，可以打开sliding menu
        //TOUCHMODE_MARGIN 边缘模式，在content页面中，如果想打开slding ,你需要在屏幕边缘滑动才可以打开slding menu
        //TOUCHMODE_NONE 自然是不能通过手势打开啦
        sm.attachToActivity(this, SlidingMenu.TOUCHMODE_FULLSCREEN);
        sm.setMenu(R.layout.menu);
        sm.setSecondaryMenu(R.layout.rightmenumain);
        
        menurightc=(LinearLayout)this.findViewById(R.id.menurightc);
        //menurightc.setBackgroundColor(0xffffff);
        
        menuvideo=(ImageView)this.findViewById(R.id.videomenu);
        
        menuvideo.setLayoutParams ( new RelativeLayout.LayoutParams( widthz/4,widthz/4
                ));
        menuvideo.setOnClickListener(new View.OnClickListener() {
			
			

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				listlayout.setVisibility(View.VISIBLE);
				//contestinfo.setVisibility(View.GONE);
				videoview.stopPlayback();
				videoview.destroyDrawingCache();
				videoview.setVisibility(View.GONE);
				vvct.stopPlayback();
				vvct.destroyDrawingCache();
				vvct.setVisibility(View.GONE);
				isplay=0;
				sm.toggle();
				page=1;
				positionzhang=-1;
				
			}
		});


    	Picasso.with(this).load(R.drawable.freestyle). resize(widthz/4, widthz/4). into(menuvideo);

        
        menubref=(ImageView)this.findViewById(R.id.mrbrief);
        menubref.setLayoutParams ( new RelativeLayout.LayoutParams( widthz/4,widthz/4
                ));
        menubref.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				page=2;
				// TODO Auto-generated method stub
				videoview.stopPlayback();
				videoview.destroyDrawingCache();
				videoview.setVisibility(View.GONE);
				vvct.stopPlayback();
				vvct.destroyDrawingCache();
				vvct.setVisibility(View.GONE);
				listlayout.setVisibility(View.GONE);
				//contestinfo.setVisibility(View.VISIBLE);
				
				//Uri uri = Uri.parse("http://content.bitsontherun.com/videos/"+contestvdurl+".mp4");
				positionzhang=-1;
    	        isplay=0;
				sm.toggle();
				
				
			}
		});
        
    	Log.i("sadfffffdfffffffffffff",""+menubref.getX());

        
        photomenu=(ImageView)this.findViewById(R.id.mrphoto);
        photomenu.setLayoutParams ( new RelativeLayout.LayoutParams( widthz/4,widthz/4
                ));
        photomenu.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				positionzhang=-1;
				// TODO Auto-generated method stub
				pd = ProgressDialog.show(MainActivity.this, "dowloading", "dowloading，please wait……");
				

				//sm.toggle();
				try {
					if(jsonlateoginst.getString("authenticated").equals("true")){
						if(uploadok==true){
							Intent y = new Intent();
							pd.dismiss();
							 Bundle bundle = new Bundle();
							    bundle.putInt("uid", (Integer) contestheader.getTag());
							    y.putExtras(bundle);
							    y.setClass(MainActivity.this, Law.class);
							    MainActivity.this.startActivity(y);
							    MainActivity.this.overridePendingTransition(R.anim.jiatihuan2,R.anim.sleep); 
							    finish();
						}else{
							pd.dismiss();
							AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
					 				context);
					  
					 			// set title
					 		     alertDialogBuilder.setTitle("Upload");
					  
					 			// set dialog message
					 			
					 			alertDialogBuilder
					 				.setMessage("Sorry, you cannot upload video in this context.")
					 				.setCancelable(false)
					 				.setPositiveButton("OK",new DialogInterface.OnClickListener() {
					 					public void onClick(DialogInterface dialog,int id) {
					 						// if this button is clicked, close
					 						// current activity
					 						 fbsharecode();
					 					}
					 				  });
					 				
					  
					 				// create alert dialog
					 				AlertDialog alertDialog = alertDialogBuilder.create();
					  
					 				// show it
					 				alertDialog.show();
							
						}
						
					}else{
						pd.dismiss();

						 Toast toast = Toast.makeText(context,
					                "Please Login first",
					                Toast.LENGTH_SHORT);
					        toast.setGravity(Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 0);
					        toast.show();
					        Intent intent = new Intent(context, Login.class);
					        context.startActivity(intent);
					        overridePendingTransition(R.anim.jiatihuan2,R.anim.sleep);
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				
			}
		});

       
        
        	Picasso.with(this).load(R.drawable.brief). resize(widthz/4, widthz/4). into(menubref);
        	 Picasso.with(this).load(R.drawable.upload). resize(widthz/4, widthz/4). into(photomenu);
        
       
       
        contest1=(ImageView)this.findViewById(R.id.contest1);
        contest1.setLayoutParams ( new LinearLayout.LayoutParams( widthz/4,widthz/4
                ));
        

        try {
			if(jsonlateoginst.getString("authenticated").equals("true")){
				//Log.i("zzzzzzzzzzzz",Json.getJson("https://graph.facebook.com/me?access_token="+Login.AT+"&method=GET&fields=picture.type(large)").getJSONObject("picture").getJSONObject("data").getString("url"));
				//Log.i("zz",Json.getJson(website+"/fbaccess?accessToken="+Login.AT,httpclient).toString());
				LOGINSTATE=1;
				Log.i("zz",jsonlateoginst.toString());
				loginoperas();
			}
			else
			{


				Picasso.with(this).load(R.drawable.defaultavatar). resize(widthz/4, widthz/4). into(contest1);
			}
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
		}
        contest1.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				pd = ProgressDialog.show(MainActivity.this, "Processing", "Processing，please wait……");  
				 new Thread(runnablelogin).start();

				
				
			}
		});
        contest1.setScaleType(ImageView.ScaleType.CENTER_CROP);
        
        ImageView contest2 = (ImageView)this.findViewById(R.id.contest2);
        contest2.setLayoutParams ( new LinearLayout.LayoutParams( widthz/4,widthz/4
                ));
        contest2.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(cc.equals("freestyle"))sm.toggle();
				else{
				//contestinfo.setVisibility(View.GONE);
				// TODO Auto-generated method stub
					menurightc.setBackgroundColor(0xFFff3200);
					menurightc.invalidate();

					

					//Picasso.with(MainActivity.this).load(R.drawable.splash). resize(widthz/4,heiz/4). into(mainback);
				    listlayout.setVisibility(View.VISIBLE);
					pd = ProgressDialog.show(MainActivity.this, "dowloading", "dowloading，please wait……"); 
					


			    	Picasso.with(context).load(R.drawable.freestyle).resize(widthz/4, widthz/4). into(menuvideo);
			    	//menuvideo.setScaleType(ImageView.ScaleType.FIT_XY);
					


					Picasso.with(MainActivity.this).load(R.drawable.standouterheader). resize(widthz, widthz/4). into(contestheader);
					// loadlistview(website+"/video/search?ss=contest&so=most_voted&sp=22");
				     //loadcontestinfo(website+"/contest/contestinfo?cc=oldwildwest");
					 new Thread(runnable1).start();
					
				     
				     contestheader.setTag(R.drawable.standouterheader);
				//Log.i("",Json.getJson(website+"/count_notifications?ui=481&t="+System.currentTimeMillis(),MainActivity.httpclient).toString());
				sm.toggle();
				}
		       
				
				
			}
		});

		Picasso.with(context).load(R.drawable.freestyle). into(contest2);
    	contest2.setScaleType(ImageView.ScaleType.FIT_XY);
    	
    	 ImageView contest3 = (ImageView)this.findViewById(R.id.contest3);
         contest3.setLayoutParams ( new LinearLayout.LayoutParams( widthz/4,widthz/4
                 ));
         contest3.setOnClickListener(new View.OnClickListener() {
 			
 			@Override
 			public void onClick(View v) {
 				//contestinfo.setVisibility(View.GONE);
 				// TODO Auto-generated method stub
 				if(cc.equals("universal"))sm.toggle();
 				else{
 					menurightc.setBackgroundColor(0xFFFFFFFF);
 					menurightc.invalidate();
 					
 					//Picasso.with(MainActivity.this).load(R.drawable.goandfunback). resize(widthz/4,heiz/4). into(mainback);
 					listlayout.setVisibility(View.VISIBLE);
 					pd = ProgressDialog.show(MainActivity.this, "dowloading", "dowloading，please wait……");  

 			    	Picasso.with(context).load(R.drawable.universal). resize(widthz/4, widthz/4). into(menuvideo);
 			    	Picasso.with(MainActivity.this).load(R.drawable.universalheader). resize(widthz, widthz/4). into(contestheader);


 					//.with(MainActivity.this).load(R.drawable.goandfunheader). resize(widthz, widthz/4). into(MainActivity.contestheader);
 					// loadlistview(website+"/video/search?ss=contest&so=most_voted&sp=22");
 				     //loadcontestinfo(website+"/contest/contestinfo?cc=oldwildwest");
 					 new Thread(runnable2).start();
 					
 				     contestheader.setTag(R.drawable.universalheader);
 				//Log.i("",Json.getJson(website+"/count_notifications?ui=481&t="+System.currentTimeMillis(),MainActivity.httpclient).toString());
 				sm.toggle();
 				}
 				
 				
 			}
 		});
         Picasso.with(context).load(R.drawable.universal). into(contest3);
     	contest3.setScaleType(ImageView.ScaleType.FIT_XY);
        
        ImageView contest4 = (ImageView)this.findViewById(R.id.contest4);
        contest4.setLayoutParams ( new LinearLayout.LayoutParams( widthz/4,widthz/4
                ));
        contest4.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//contestinfo.setVisibility(View.GONE);
				// TODO Auto-generated method stub
				if(cc.equals("goandfun"))sm.toggle();
				else{
					menurightc.setBackgroundColor(0xFFB4C960);
					menurightc.invalidate();
					
					//Picasso.with(MainActivity.this).load(R.drawable.goandfunback). resize(widthz/4,heiz/4). into(mainback);
					listlayout.setVisibility(View.VISIBLE);
					pd = ProgressDialog.show(MainActivity.this, "dowloading", "dowloading，please wait……");  

			    	Picasso.with(context).load(R.drawable.goandfun). resize(widthz/4, widthz/4). into(menuvideo);
			    	Picasso.with(MainActivity.this).load(R.drawable.goandfunheader). resize(widthz, widthz/4). into(contestheader);


					//.with(MainActivity.this).load(R.drawable.goandfunheader). resize(widthz, widthz/4). into(MainActivity.contestheader);
					// loadlistview(website+"/video/search?ss=contest&so=most_voted&sp=22");
				     //loadcontestinfo(website+"/contest/contestinfo?cc=oldwildwest");
					 new Thread(runnable3).start();
					
				     contestheader.setTag(R.drawable.goandfunheader);
				//Log.i("",Json.getJson(website+"/count_notifications?ui=481&t="+System.currentTimeMillis(),MainActivity.httpclient).toString());
				sm.toggle();
				}
				
				
			}
		});


    	Picasso.with(context).load(R.drawable.goandfun). resize(widthz/4, widthz/4). into(contest4);

        
        ImageView contest5 = (ImageView)this.findViewById(R.id.contest5);
        contest5.setLayoutParams ( new LinearLayout.LayoutParams( widthz/4,widthz/4
                ));
        contest5.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(cc.equals("whoodbrooklyn"))sm.toggle();
				else{
					menurightc.setBackgroundColor(0xFF83111E);
					menurightc.invalidate();
					
					//Picasso.with(MainActivity.this).load(R.drawable.whoodbrooklyback). resize(widthz/4,heiz/4). into(mainback);

				listlayout.setVisibility(View.VISIBLE);
				pd = ProgressDialog.show(MainActivity.this, "dowloading", "dowloading，please wait……");  
				//contestinfo.setVisibility(View.GONE);
		    	Picasso.with(context).load(R.drawable.whoodbrooklyn). resize(widthz/4, widthz/4). into(menuvideo);
		    	Picasso.with(MainActivity.this).load(R.drawable.whoodrooklynheader). resize(widthz, widthz/4). into(contestheader);
				//loadlistview(website+"/video/search?ss=contest&so=most_voted&sp=goandfun");
			     //loadcontestinfo(website+"/contest/contestinfo?cc=goandfun");
			     contestheader.setTag(R.drawable.whoodrooklynheader);
			     new Thread(runnable4).start();

				sm.toggle();
				}
			}
		});

    	Picasso.with(context).load(R.drawable.whoodbrooklyn). resize(widthz/4, widthz/4). into(contest5);

        
        ImageView contest6 = (ImageView)this.findViewById(R.id.contest6);
        contest6.setLayoutParams ( new LinearLayout.LayoutParams( widthz/4,widthz/4
                ));
        contest6.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(cc.equals("metro"))sm.toggle();
				else{
					menurightc.setBackgroundColor(0xFF007736);
					menurightc.invalidate();
					
					//Picasso.with(MainActivity.this).load(R.drawable.metroback). resize(widthz/4,heiz/4). into(mainback);

				listlayout.setVisibility(View.VISIBLE);
				pd = ProgressDialog.show(MainActivity.this, "dowloading", "dowloading，please wait……");  
				//contestinfo.setVisibility(View.GONE);
		    	Picasso.with(context).load(R.drawable.metro). resize(widthz/4, widthz/4). into(menuvideo);
		    	Picasso.with(MainActivity.this).load(R.drawable.metroheader). resize(widthz, widthz/4). into(contestheader);
				//loadlistview(website+"/video/search?ss=contest&so=most_voted&sp=goandfun");
			     //loadcontestinfo(website+"/contest/contestinfo?cc=goandfun");
			     contestheader.setTag(R.drawable.metroheader);
			     new Thread(runnable5).start();

				sm.toggle();
				}

        	
			}
		});

    	Picasso.with(context).load(R.drawable.metro). resize(widthz/4, widthz/4). into(contest6);

        
        ImageView contest7 = (ImageView)this.findViewById(R.id.contest7);
        contest7.setLayoutParams ( new LinearLayout.LayoutParams( widthz/4,widthz/4
                ));
        contest7.setOnClickListener(new View.OnClickListener() {
			
        	public void onClick(View v) {
				// TODO Auto-generated method stub
				listlayout.setVisibility(View.VISIBLE);
				//contestinfo.setVisibility(View.GONE);
				sm.toggle();
        	}
			
			
		});

    	Picasso.with(context).load(R.drawable.comingsoon). resize(widthz/4, widthz/4). into(contest7);

        
        ImageView contest8 = (ImageView)this.findViewById(R.id.contest8);
        contest8.setLayoutParams ( new LinearLayout.LayoutParams( widthz/4,widthz/4
                ));
        contest8.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				listlayout.setVisibility(View.VISIBLE);
				//contestinfo.setVisibility(View.GONE);
				sm.toggle();
				
				
			}
		});
    	Picasso.with(context).load(R.drawable.comingsoon). resize(widthz/4, widthz/4). into(contest8);

        
       

        
        /*
        LayoutInflater inflator = (LayoutInflater)this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflator.inflate(R.layout.title, null);
        Button cam=(Button)v.findViewById(R.id.PHOTO);
        ImageView titlelego = (ImageView)v.findViewById(R.id.logotitle);
        titlelego.setImageResource(R.drawable.logo3);
        cam.setOnClickListener(new View.OnClickListener(){

 		@Override
 		public void onClick(View v) {
 			// TODO Auto-generated method stub
 			
 			
             
 		}});
        
        ImageView menubtn=(ImageView)v.findViewById(R.id.menubtn);
        menubtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				sm.toggle();
			}
		});
        
       
        
        getActionBar().setCustomView(v);
        getActionBar().setDisplayShowCustomEnabled(true);
        */
        
	}
	
	
	
	  /** ++++++++++++********************************
     * 点击按钮事件 
     * @param view 
     */  
    public void loadMore(View view) {  
       // loadMoreButton.setText("loading...");   //设置按钮文字loading  
    	loadMorepb.setVisibility(View.VISIBLE);
        handler.postDelayed(new Runnable() {  
            @Override  
            public void run() {  
                  
                loadData();  
                  
                adapter.notifyDataSetChanged(); //数据集变化后,通知adapter  
                listView.setSelection(visibleLastIndex - visibleItemCount + 1); //设置选中项  
                loadMorepb.setVisibility(View.INVISIBLE) ;
                //loadMoreButton.setText("load more");    //恢复按钮文字  
            }  
        }, 2000);  
    }  
    
      
    /** 
     * 模拟加载数据 
     */  
   
    private void loadData() {  
        int count = adapter.getCount();  
        String as = null;
        String bs = null;
        String cs = null;
        String ds = null;
        String fs = null;
        int es=0;
        
        for (int i = count; i < count + 5; i++) { 
        	if(i<listno){
        	try {
				as=jsonobject.getJSONArray("items").getJSONObject(i).getString("imageUrl320");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				bs=jsonobject.getJSONArray("items").getJSONObject(i).getString("ownerName")+" "+jsonobject.getJSONArray("items").getJSONObject(i).getString("ownerSurname");
				 fs = jsonobject.getJSONArray("items").getJSONObject(i).getString("title");
				
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			try {
				cs=jsonobject.getJSONArray("items").getJSONObject(i).getString("videoUrl480");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				ds=jsonobject.getJSONArray("items").getJSONObject(i).getString("id");
				es=jsonobject.getJSONArray("items").getJSONObject(i).getInt("votesCount");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
        	RowItem item = new RowItem(as, bs, fs,cs,ds,es,true);
        	
            adapter.addItem(item);
        } 
        }
    }
    

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	public void finish() {  
	    // TODO Auto-generated method stub  
		pd = ProgressDialog.show(MainActivity.this, "dowloading", "dowloading，please wait……");

	    timer2.cancel();
	   //Login.callFacebookLogout(this);
	    savedata() ;
	    pd.dismiss();
	    super.finish(); 
	    //关闭窗体动画显示  
	    
	   // this.overridePendingTransition(R.anim.activity_open_right,R.anim.activity_close_right);  
	} 
	private long mExitTime;
	public boolean onKeyDown(int keyCode, KeyEvent event) {
	                if (keyCode == KeyEvent.KEYCODE_BACK) {
	                        if ((System.currentTimeMillis() - mExitTime) > 2000) {
	                                Toast.makeText(this, "Press again to exit the program", Toast.LENGTH_SHORT).show();
	                                mExitTime = System.currentTimeMillis();

	                        } else {
	                                finish();
	                                if(MainActivity.gNotMgr!=null)
	                        			MainActivity.gNotMgr.cancelAll();
	                        	    super.finish(); 
	                        	    System.exit(0);

	                        }
	                        return true;
	                }
	                return super.onKeyDown(keyCode, event);
	        }
	

	 public static  void loginoperas(){
	    	try {
	    		jsonlateoginst = Json.getJson(website+"/login_status.json",MainActivity.httpclient);
	    		SplashActivity.result=jsonlateoginst.toString();


	        	Picasso.with(context).load(website+"/avatar/"+Json.getJson(website+"/login_status.json",MainActivity.httpclient).getString("registrationId")+"/picture"). into(MainActivity.contest1);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    	 Message message = new Message();                  
			  message.what = 1;  
			  handlertimetask2.sendMessageDelayed(message, 1000);
			 //handlertimetask2.sendMessage(message);  
	    }
	 public static  void logoutoperas(){

	    	Picasso.with(context).load(R.drawable.defaultavatar). into(MainActivity.contest1);
	        jsonlateoginst = Json.getJson(website+"/login_status.json",MainActivity.httpclient);

	 }
	 public void onConfigurationChanged(Configuration newConfig) {  
		 
	      super.onConfigurationChanged(newConfig);  
	      Log.i("zhang"," == onConfigurationChanged");  
	      if(this.getResources().getConfiguration().orientation==Configuration.ORIENTATION_LANDSCAPE)
	      {  isheng=true;
	      	        
	    	  if(isplay==1||ispause){
	          Log.i("info", "landscape"); // 横屏 
	          //videoview.pause();
	          group.setVisibility(View.GONE);
	          videoview.setLayoutParams(new FrameLayout.LayoutParams( widthz/9*16,widthz
                 ));
	          videoview.setY(-0);
	          listView.setVisibility(View.GONE);
	          vvct.setLayoutParams(new FrameLayout.LayoutParams( widthz/9*16,widthz
	                  ));
	 	          vvct.setY(-0);
	 	         lvct.setVisibility(View.GONE);
	          //videoview.setClickable(true);
	          //vidtime=videoview.getCurrentPosition();
	          //videoview.stopPlayback();
	          isplayheng=1;
				
	         /*
	          Intent x = new Intent();
	          x.setClass(MainActivity.this, Play.class);
	    		Bundle bundle=new Bundle();  
	    	  bundle.putStringArray("pic", adapter.getnearpic(positionzhang));
	    	  bundle.putStringArray("des", adapter.getneardec(positionzhang));
	          bundle.putString("result1",vdpath); 
	          bundle.putString("time", Integer.toString(vidtime));
	          x.putExtras(bundle); 
	          startActivity(x);
	          */
	        //----------定时器记录播放进度---------//    
	       
			   
	          //自定义函数处理配置改变事件
	    	  }
	      } 
	      if(this.getResources().getConfiguration().orientation==Configuration.ORIENTATION_PORTRAIT)
	      { 
	    	  if(mTimer!=null){
	    	    mTimer.cancel();
	    	    mTimerflag=0;
	    	    mTimer=null;
	           }
	    	  Log.i("info", "landscape2");
	    	  isheng=false;
	    	  getPopupWindowInstance();  
	          
	            getPopupWindowInstanceleft() ;
	          
	            getPopupWindowInstanceright();
	            getsharePopupWindowInstance();
	          

	    		  //videoview.setClickable(false);
	    		  listView.setVisibility(View.VISIBLE);
	    		  lvct.setVisibility(View.VISIBLE);
	    		  group.setVisibility(View.VISIBLE);
	    		  videoview.setY(ytemp);
	    		  videoview.setLayoutParams(new FrameLayout.LayoutParams( widthz,widthz/16*9
	    	                 ));
	    		  vvct.setY(ytemp);
	    		  vvct.setLayoutParams(new FrameLayout.LayoutParams( widthz,widthz/16*9 ));
	    		  //videoview.seekTo(vidtime);
	    		  //videoview.start(); 
	    		  isplay=isplayheng;
	    		  isplayheng=0;
	    		  if(finishflag==1){
	    			  videoview.stopPlayback();
	  				videoview.destroyDrawingCache();
	  				videoview.setVisibility(View.GONE);
	  				vvct.stopPlayback();
	  				vvct.destroyDrawingCache();
	  				vvct.setVisibility(View.GONE);
	    		  }
	    	  
	      }
	    }
	 public class timerTask extends TimerTask
	    {
	      public void run()
	      {
	    	  
	    	 
	    	  
	    	  Message message = new Message(); 
	    	  message.what = isplay; 
	    	  handlertimetask1.sendMessage(message);
	      }
	    };
	    Handler handlertimetask1 = new Handler(){         
            public void handleMessage(Message msg) {     
                 switch (msg.what) {                 
                    case 1:  
                    

                    	setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
                    	
                    	 //Contestadapter. mPopupWindow.dismiss();             
                        break; 
                    case 0:
                    	 setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//
     					//setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);
     					if(isheng==true)
                        	isheng=false;
     					
     					break;
                                    }            
                   super.handleMessage(msg);      
                   }         
       };
		public static int x;  
	    
	  
	    static Handler handlertimetask2 = new Handler(){         
            public void handleMessage(Message msg) {     
                 int getmessageno = 0;
                 Log.i("saaaaaaaaaaa","sa");
				switch (msg.what) {                 
                    case 1: 
                    	try {
        					jsonmessage=new JSONObject(Login.getHtmlByGet(website+"/notifications?ui="+MainActivity.jsonlateoginst.getInt("registrationId"),httpclient));
        				} catch (JSONException e1) {
        					// TODO Auto-generated catch block
        					e1.printStackTrace();
        				}

        				//Log.i("",jsonmessage.toString());
        				    
        				 gNotMgr = (NotificationManager)context. getSystemService(NOTIFICATION_SERVICE);
        			        
        				 try {
        					    getmessageno=jsonmessage.getInt("totalCount");
        				 } catch (JSONException e) {
     						// TODO Auto-generated catch block
     						e.printStackTrace();
     					}
					try {
						if(getmessageno!=totalmessageno&&getmessageno!=0&&jsonlateoginst.getInt("registrationId")!=-1){
							totalmessageno=getmessageno;
							
						
								//����Notification�����A���]�w��������
								Notification tBNot = new Notification(R.drawable.sa, "You have "+getmessageno+" message", System.currentTimeMillis());

								//�]�w�_�����W�v
								long[] tVibrate = {0,100,200,300};
								tBNot.vibrate = tVibrate;
								
								//�]�wLED�O�G�P�t�������P�C���A���]�wflags�q��
								tBNot.ledARGB = 0xff00ff00;
								tBNot.ledOnMS = 300; 
								tBNot.ledOffMS = 1000;
								tBNot.flags |= Notification.FLAG_SHOW_LIGHTS;
								
								
								


								Intent notificationIntent = new Intent(context, Profile.class);
								
								Bundle bundle = new Bundle();
				                bundle.putString("uid",jsonlateoginst.getString("registrationId"));
				                notificationIntent.putExtras(bundle);
				                notificationIntent.setAction(String.valueOf(System.currentTimeMillis()));
								
								x=1;
								
								
								
								PendingIntent contentIntent = PendingIntent.getActivity(context, 0, notificationIntent, 0);
								tBNot.setLatestEventInfo(context, "Standouter", jsonmessage.getJSONArray("items").getJSONObject(0).getString("authorName")
										                                        +jsonmessage.getJSONArray("items").getJSONObject(0).getString("authorSurname")
										                                        +" "+jsonmessage.getJSONArray("items").getJSONObject(0).getString("customMessage"), contentIntent);
								gNotMgr.notify(1, tBNot);
								
						}
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
                        break; 
                    
                                    }            
                   super.handleMessage(msg);      
                   }         
       };  
       
       Runnable runnable0=new Runnable(){

     		@Override
     		public void run() {
     			// TODO Auto-generated method stub
     			cc="freestyle";
     			 loadlistview("first");
     			loadcontestinfo("first");
     			 Message msglv= new Message();
     	        msglv.what=1;
     	        handlerlv.sendMessage(msglv);
     		}
         	   
      };
      
       Runnable runnable1=new Runnable(){

		@Override
		public void run() {
			// TODO Auto-generated method stub
			cc="freestyle";
 			 loadlistview(website+"/video/search?ss=contest&so=most_recent&sp=freestyle");
 			 loadcontestinfo(website+"/contest/contestinfo?cc=freestyle");
 			 Message msglv= new Message();
 		     msglv.what=1;
 		     handlerlv.sendMessage(msglv);
		}
    	   
       };
       Runnable runnable2=new Runnable(){

   		@Override
   		public void run() {
   			// TODO Auto-generated method stub
   			cc="universal";
    			 loadlistview(website+"/video/search?ss=contest&so=most_recent&sp=universal");
    			 loadcontestinfo(website+"/contest/contestinfo?cc=universal");
    			 Message msglv= new Message();
    		     msglv.what=1;
    		     handlerlv.sendMessage(msglv);
   		}
       	   
          };
       Runnable runnable3=new Runnable(){

      		@Override
      		public void run() {
      			// TODO Auto-generated method stub
      			cc="goandfun";
      			 loadlistview(website+"/video/search?ss=contest&so=most_recent&sp=goandfun");
      			loadcontestinfo(website+"/contest/contestinfo?cc=goandfun");
      		 Message msglv= new Message();
            msglv.what=1;
            handlerlv.sendMessage(msglv);
      		}
          	   
             };
        Runnable runnable4=new Runnable(){

           		@Override
           		public void run() {
           			// TODO Auto-generated method stub
           			cc="whoodbrooklyn";
           			 loadlistview(website+"/video/search?ss=contest&so=most_recent&sp=whoodbrooklyn");
           			loadcontestinfo(website+"/contest/contestinfo?cc=whoodbrooklyn");
           			 Message msglv= new Message();
           	        msglv.what=1;
           	        handlerlv.sendMessage(msglv);
           		}
               	   
            };
       Runnable runnable5=new Runnable(){

   		@Override
   		public void run() {
   			// TODO Auto-generated method stub
   			cc="metro";
 			 loadlistview(website+"/video/search?ss=contest&so=most_recent&sp=metro");
 			loadcontestinfo(website+"/contest/contestinfo?cc=metro");
 			 Message msglv= new Message();
 	        msglv.what=1;
 	        handlerlv.sendMessage(msglv);
   		}
       	   
       };
       
       Runnable runnable6=new Runnable(){

     		@Override
     		public void run() {
     			// TODO Auto-generated method stub
     			cc="metro";
     			 loadlistview(website+"/video/search?ss=contest&so=most_recent&sp=metro");
     			loadcontestinfo(website+"/contest/contestinfo?cc=metro");
     			 Message msglv= new Message();
     	        msglv.what=1;
     	        handlerlv.sendMessage(msglv);
     		}
         	   
      };
       Runnable runnablelogin=new Runnable(){

     		@Override
     		public void run() {
     			// TODO Auto-generated method stub
     			jsonlateoginst=Json.getJson(website+"/login_status.json",MainActivity.httpclient);
				Log.i("sd",jsonlateoginst.toString());
				
				Message msglogin= new Message();
      	        try {
      	        	msglogin.what=jsonlateoginst.getString("authenticated").equals("true")?1:2;
				} catch (JSONException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
      	      handlerlogin.sendMessage(msglogin);
				
         	   
      }
              
     };
     Handler handlerlogin = new Handler(){         
         public void handleMessage(Message msg) {     
       	  try {
					if(msg.what==1){
						String uid=jsonlateoginst.getString("registrationId");
						  
							
							
							Intent y = new Intent();
							Bundle bundle = new Bundle();
			                bundle.putString("uid",uid);
			                y.putExtras(bundle);
				            y.setClass(context, Profile.class);
				            x=0;
				            
				          
				            context.startActivity(y);
				           // ((Activity) context).finish();
				            ((Activity) context).overridePendingTransition(R.anim.jiatihuan,R.anim.sleep); 
						//sm.toggle();
						
					}
					else
					{   LOGINSTATE=0;
						Intent intent = new Intent(MainActivity.this, Login.class);
						MainActivity.this.startActivity(intent);
						//MainActivity.this.finish();
				        overridePendingTransition(R.anim.jiatihuan,R.anim.sleep);
						//sm.toggle();
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
       	  pd.dismiss();
       	 //sm.toggle();
  		} 
         
     };
      
    void savedata() {
     // TODO Auto-generated method stub
     			String path="/sdcard/json.txt";
     	        
                try {
                	
                  // jsonobject = Json.getJson(website+"/video/search?ss=contest&so=most_voted&sp=22",httpclient);
    				Login.writeFileSdcardFile(path, jsontemp1.toString());
                	
    			} catch (IOException e) {
    				// TODO Auto-generated catch block
    				e.printStackTrace();
    			}
               path="/sdcard/json2.txt";
                
                    try {
                    	
                    		  //jsonobject = Json.getJson(website+"/contest/contestinfo?cc=oldwildwest",httpclient);
              				Login.writeFileSdcardFile(path, jsontemp2.toString());
                    	
        			} catch (IOException e) {
        				// TODO Auto-generated catch block
        				e.printStackTrace();
        			}
               Log.i("","finish");
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
	  LayoutInflater layoutInflater ;
	protected JSONObject fimjson;
      private void initPopuptWindow() {  
    	   layoutInflater = LayoutInflater.from(MainActivity.this);
    	  new Thread(runnableinitPopuptWindow).start();
    	 
           
          
          
      }  
      class MySeekbar implements OnSeekBarChangeListener {  

		public void onProgressChanged(SeekBar seekBar, int progress,  
                  boolean fromUser) {  
          }  
    
          public void onStartTrackingTouch(SeekBar seekBar) {  
              isChanging=true;    
          }  
    
          public void onStopTrackingTouch(SeekBar seekBar) {  
          	videoview.seekTo(seekbar.getProgress());  
              isChanging=false;    
          }  
    
      }  
      Runnable runnableinitPopuptWindow=new Runnable(){

    		

			@Override
    		public void run() {
    			// TODO Auto-generated method stub
    			 fimjson = null;
    	    	  try {
    					String s=Login.readFileSdcardFile("/sdcard/json.txt");
    					//Log.i("",s);
    					fimjson=new JSONObject(s);
    				} catch (JSONException e) {
    					// TODO Auto-generated catch block
    					e.printStackTrace();
    				} catch (IOException e) {
    					// TODO Auto-generated catch block
    					e.printStackTrace();
    				}
    	    	 
    	    	  int a = 0;
    			try {
    				a = fimjson.getInt("totalResults");
    			} catch (JSONException e) {
    				// TODO Auto-generated catch block
    				e.printStackTrace();
    			}
    	    	  if (a>20) a=20;
    	    	  filmimg=new String[a];
    	    	  for(int i=0;i<a;i++){
    				try {
    					filmimg[i]=fimjson.getJSONArray("items").getJSONObject(i).getString("imageUrl120");
    				} catch (JSONException e) {
    					// TODO Auto-generated catch block
    					e.printStackTrace();
    				}
    	    	  
    	    	  }
    	    	  
    			 Message msglv= new Message();
    	        msglv.what=1;
    	        handlerpupwindow.sendMessage(msglv);
    		}
        	   
     };
     private Handler  handlerpupwindow=new Handler(){
	 	   @Override
		public void handleMessage(Message msglv){
	 		  // super.handleMessage(msg2);
	 		   if( msglv.what==1){
	 			  
	 			  //listView.setAdapter(null);
	 			   
	 	          View popupWindow = layoutInflater.inflate(R.layout.contr, null);  
	 	          play=(Button) popupWindow.findViewById(R.id.play2);
	 	          play.setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						
						if(isplayheng==1){
							play.setBackgroundResource(R.drawable.play);
							isplayheng=0;
						if(vvct.isShown()){
							   vvct.pause();
							}
						else{
							videoview.pause();
						}
						   
						}
						else{
							play.setBackgroundResource(R.drawable.pause);
							isplayheng=1;
							if(vvct.isShown()){
								   vvct.start();
								}
							else{
								videoview.start();
							}
						}
					}
				});
	 	          
	 	          play.setHeight(heiz/12);
	 	          seekbar = (SeekBar)  popupWindow.findViewById(R.id.seekbar2);
	 	  	    seekbar.setOnSeekBarChangeListener(new MySeekbar());
	 	  	    
	 	  	    filmshow= (Gallery) popupWindow.findViewById(R.id.filmlist2);
	 	  	    ImageAdapter imageadapter = new ImageAdapter(MainActivity.this);
	 	  	    imageadapter.setItem(filmimg);
	 	  	    filmshow.setAdapter(imageadapter );
	 	  	  if (filmimg.length > 5) {
	 	  		filmshow.setSelection(3);
	 	  	} else  { 
	 	  		filmshow.setSelection(filmimg.length/2 - 1);
	 	  	}
	 	  	  filmshow.setOnItemClickListener(new AdapterView.OnItemClickListener(){
	 	  		public void onItemClick(AdapterView parent, View view, int position, long id) {
	 	  			       if(videoview.isShown()){
	 	  			    	getPopupsmallWindowInstanceleft();
				            getPopupsmallWindowInstanceright();
					   	     getsharePopupsmallWindowInstance() ;

				        	 
			    			vidtime=0;
			        		Log.i("",""+position+","+positionzhang);
			        		isplay=1;

			    	        Uri uri = null;
							try {
								uri = Uri.parse(fimjson.getJSONArray("items").getJSONObject(position).getString("videoUrl480"));
							} catch (JSONException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
					        videoview.setVideoURI(uri);
			    	       //MainActivity.vidpath=vidpath[position+a*5];
			    	        videoview.requestFocus();
			    	       videoview.seekTo(0);
			    	       videoview.start();
			    	       videoview.setVisibility(View.VISIBLE);
			    	      buffer.setVisibility(View.VISIBLE);
			    	        
			    	        //setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
			    	        positionzhang=position;
	 	  			       }else if(vvct.isShown()){
		 	  			    	getPopupsmallWindowInstanceleft();
					            getPopupsmallWindowInstanceright();
						   	     getsharePopupsmallWindowInstance() ;

					        	 
				    			vidtime=0;
				        		Log.i("",""+position+","+positionzhang);
				        		isplay=1;

				    	        Uri uri = null;
								try {
									uri = Uri.parse(fimjson.getJSONArray("items").getJSONObject(position).getString("videoUrl480"));
								} catch (JSONException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
						        vvct.setVideoURI(uri);
				    	       //MainActivity.vidpath=vidpath[position+a*5];
				    	        vvct.requestFocus();
				    	       vvct.seekTo(0);
				    	       vvct.start();
				    	       vvct.setVisibility(View.VISIBLE);
				    	      buffer.setVisibility(View.VISIBLE);
				    	        
				    	        //setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
				    	        positionzhang=position;
	 	  			       }
	 	  			  }
	 	  	  });
	 	  	 
	 	  	    
	 	  	  
	 	  	   
	 	  	   
	 	          // 创建一个PopupWindow  
	 	          // 参数1：contentView 指定PopupWindow的内容  
	 	          // 参数2：width 指定PopupWindow的width  
	 	          // 参数3：height 指定PopupWindow的height  
	 	          mPopupWindow = new PopupWindow(popupWindow,heiz, widthz/3); 
	 	          mPopupWindow.setAnimationStyle(R.style.AnimationFade2); 
	 	         
	 	          //mPopupWindow.getBackground().setAlpha(100)
	 	          
	 	          
	 	    
	 	          getWindowManager().getDefaultDisplay().getWidth();  
	 	          getWindowManager().getDefaultDisplay().getHeight();  
	 	          mPopupWindow.getWidth();  
	 	          mPopupWindow.getHeight();  
	 		   }
	 		   
	 		   
	 	   }
	 	   
	     };
	

	protected boolean isup;
	private PopupWindow msharePopupWindow;
	private PopupWindow mPopupsmallWindowleft;
	private PopupWindow mPopupsmallWindowright;
	private PopupWindow msharePopupsmallWindow;
			  
	private void initPopuptWindowleft() {  
	         LayoutInflater layoutInflater = LayoutInflater.from(this);  
	         View popupWindow2 = layoutInflater.inflate(R.layout.leftb, null);  
	         left=(Button) popupWindow2.findViewById(R.id.left);
	         left.setOnClickListener(new View.OnClickListener() {
	 			
	 			private boolean isup;

	 			@Override
	 			public void onClick(View v) {
	 				// TODO Auto-generated method stub
	 				

	 				if(Login.session!=null){
						
					int[] xy = new int[2];
					v.getLocationInWindow(xy);
					Log.i("shapo",""+xy[1] );
					
					
					if(isup==true){msharePopupWindow.dismiss();isup=false;}
					else{
						
						 getsharePopupWindowInstance();
						msharePopupWindow.showAtLocation(MainActivity.this.getcurrentview, Gravity.LEFT, heiz/6, 0);
						 //mPopupWindow.showAsDropDown(v, MainActivity.widthz/8, xy[1]-MainActivity.heiz/2+MainActivity.widthz/8);
						isup=true;
					}
					
					}
					else{
						mTimer.cancel();
						mTimerflag=0;
						videoview.stopPlayback();
						videoview.destroyDrawingCache();
						videoview.setVisibility(View.GONE);
						vvct.stopPlayback();
						vvct.destroyDrawingCache();
						vvct.setVisibility(View.GONE);
						isplay=0;
						isplayheng=0;
						
						Intent intent = new Intent(MainActivity.nowcontext, Login.class);
						MainActivity.nowcontext.startActivity(intent);
						//MainActivity.this.finish();
						//((Activity) convertView2.getContext()).overridePendingTransition(R.anim.activity_open,R.anim.activity_close);
						
					}
	 				
	 			}
	 		});
	         
	   
	         // 创建一个PopupWindow  
	         // 参数1：contentView 指定PopupWindow的内容  
	         // 参数2：width 指定PopupWindow的width  
	         // 参数3：height 指定PopupWindow的height  
	         mPopupWindowleft = new PopupWindow(popupWindow2,heiz/6, heiz/6); 
	         mPopupWindowleft.setAnimationStyle(R.style.AnimationFade); 
	         
	         //mPopupWindow.getBackground().setAlpha(100)
	         
	     } 
	     
	     private void initPopuptWindowright() {  
	         LayoutInflater layoutInflater = LayoutInflater.from(this);  
	         View popupWindowright = layoutInflater.inflate(R.layout.right, null);  
	         right=(Button) popupWindowright.findViewById(R.id.right);
	         right.setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub

						JSONObject votejson = Json.getJson(MainActivity.website+"/video/vote?ri="+rowItems.get(positionzhang).getVID()+"&vt=STANDOUT",MainActivity.httpclient);
						Toast toast;
						String Votestate = null;
						try {
							Votestate = votejson.getString("result");
						} catch (JSONException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						
						if (Votestate.equals("unknown")){
							  toast = Toast.makeText(MainActivity.nowcontext,
						                "Please Login first",
						                Toast.LENGTH_SHORT);
						        toast.setGravity(Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 0);
						        toast.show();
						        Intent intent = new Intent(MainActivity.nowcontext, Login.class);
						        MainActivity.nowcontext.startActivity(intent);
								
						        
						}else{
						try {
							
							toast = Toast.makeText(MainActivity.nowcontext,
									 convertNodeToText(Jsoup.parse(votejson.getString("message"))),
							        Toast.LENGTH_SHORT);
							toast.setGravity(Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 0);
					        toast.show();
					      
					       //Login.writeFileSdcardFile("/sdcard/as.txt", rowItems.get(position).getVID());
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						}
				        
				        Log.i("vote",votejson.toString());
						
					}
				});
	         
	   
	         // 创建一个PopupWindow  
	         // 参数1：contentView 指定PopupWindow的内容  
	         // 参数2：width 指定PopupWindow的width  
	         // 参数3：height 指定PopupWindow的height  
	         mPopupWindowright = new PopupWindow(popupWindowright,heiz/6, heiz/6); 
	         mPopupWindowright.setAnimationStyle(R.style.AnimationFaderight); 
	         
	         //mPopupWindow.getBackground().setAlpha(100)
	         
	         
	   
	         // 获取屏幕和PopupWindow的width和height  
	          
	         
	     } 
		  

 	private void getPopupsmallWindowInstanceleft() {  
         if (null != mPopupsmallWindowleft) {  
             mPopupsmallWindowleft.dismiss();  
             return;  
         } else {  
             initPopuptsmallWindowleft();  
         }  
     }  
     private void getPopupsmallWindowInstanceright() {  
         if (null != mPopupsmallWindowright) {  
             mPopupsmallWindowright.dismiss();  
             return;  
         } else {  
             initPopuptsmallWindowright();  
         }  
     } 
	 	
	private void initPopuptsmallWindowleft() {  
	         LayoutInflater layoutInflater = LayoutInflater.from(this);  
	         View popupWindow2 = layoutInflater.inflate(R.layout.leftb, null);  
	         left=(Button) popupWindow2.findViewById(R.id.left);
	         left.setOnClickListener(new View.OnClickListener() {
	 			
	 			private boolean isupshu;

	 			@Override
	 			public void onClick(View v) {
	 				// TODO Auto-generated method stub
	 				

	 				if(Login.session!=null){
						
					int[] xy = new int[2];
					v.getLocationInWindow(xy);
					Log.i("shapo",""+xy[1] );
					
					
					if(isupshu==true){msharePopupsmallWindow.dismiss();isupshu=false;}
					else{
						
						initsharePopuptsmallWindow() ;
						msharePopupsmallWindow.showAtLocation(MainActivity.this.getcurrentview, Gravity.LEFT, widthz/4, ytemp2-widthz/16*9-widthz/16);
						 //mPopupWindow.showAsDropDown(v, MainActivity.widthz/8, xy[1]-MainActivity.heiz/2+MainActivity.widthz/8);
						isupshu=true;
					}
					
					}
					else{
						//mTimer.cancel();
						videoview.stopPlayback();
						videoview.destroyDrawingCache();
						videoview.setVisibility(View.GONE);
						vvct.stopPlayback();
						vvct.destroyDrawingCache();
						vvct.setVisibility(View.GONE);
						isplay=0;
						isplayheng=0;
						
						Intent intent = new Intent(MainActivity.nowcontext, Login.class);
						MainActivity.nowcontext.startActivity(intent);
						//MainActivity.this.finish();
						//((Activity) convertView2.getContext()).overridePendingTransition(R.anim.activity_open,R.anim.activity_close);
						
					}
	 				
	 			}
	 		});
	         
	   
	         // 创建一个PopupWindow  
	         // 参数1：contentView 指定PopupWindow的内容  
	         // 参数2：width 指定PopupWindow的width  
	         // 参数3：height 指定PopupWindow的height  
	         mPopupsmallWindowleft = new PopupWindow(popupWindow2,widthz/4, widthz/4); 
	         mPopupsmallWindowleft.setAnimationStyle(R.style.AnimationFade); 
	         
	         //mPopupWindow.getBackground().setAlpha(100)
	         
	     } 
	     
	     private void initPopuptsmallWindowright() {  
	         LayoutInflater layoutInflater = LayoutInflater.from(this);  
	         View popupWindowright = layoutInflater.inflate(R.layout.right, null);  
	         right=(Button) popupWindowright.findViewById(R.id.right);
	         right.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub

					JSONObject votejson = Json.getJson(MainActivity.website+"/video/vote?ri="+rowItems.get(positionzhang).getVID()+"&vt=STANDOUT",MainActivity.httpclient);
					Toast toast;
					String Votestate = null;
					try {
						Votestate = votejson.getString("result");
					} catch (JSONException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					
					if (Votestate.equals("unknown")){
						  toast = Toast.makeText(MainActivity.nowcontext,
					                "Please Login first",
					                Toast.LENGTH_SHORT);
					        toast.setGravity(Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 0);
					        toast.show();
					        Intent intent = new Intent(MainActivity.nowcontext, Login.class);
					        MainActivity.nowcontext.startActivity(intent);
							
					        
					}else{
					try {
						
						toast = Toast.makeText(MainActivity.nowcontext,
								 convertNodeToText(Jsoup.parse(votejson.getString("message"))),
						        Toast.LENGTH_SHORT);
						toast.setGravity(Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 0);
				        toast.show();
				        /*
				       votejsonstring= votejsonstring+"{"
				                                           
				        		                                                +"\"imageUrl320\":\""+rowItems.get(positionzhang).getImageId()+"\""      
				        		                                                +",\"ownerNickname\":\""+rowItems.get(positionzhang).getTitle()+"\""
				        		                                                +",\"videoUrl480\":\""+rowItems.get(positionzhang).getDesc()+"\""
				        		                                                +",\"id\":\""+rowItems.get(positionzhang).getVID()+"\""
				        		                                                +",\"votesCount\":\""+rowItems.get(positionzhang).getvotecont()+"\""
				        		                                                +"},";
				       votecontno++;
				       
				        Log.i("playlist",votejsonstring);
				        */
				       //Login.writeFileSdcardFile("/sdcard/as.txt", rowItems.get(position).getVID());
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					}
			        
			        Log.i("vote",votejson.toString());
					
				}
			});
	         
	   
	         // 创建一个PopupWindow  
	         // 参数1：contentView 指定PopupWindow的内容  
	         // 参数2：width 指定PopupWindow的width  
	         // 参数3：height 指定PopupWindow的height  
	         mPopupsmallWindowright = new PopupWindow(popupWindowright,widthz/4, widthz/4); 
	         mPopupsmallWindowright.setAnimationStyle(R.style.AnimationFaderight); 
	         
	         //mPopupWindow.getBackground().setAlpha(100)
	         
	         
	   
	         // 获取屏幕和PopupWindow的width和height  
	          
	         
	     } 
	    
	     public void getsharePopupsmallWindowInstance() {  
	         if (null != msharePopupsmallWindow) {  
	             msharePopupsmallWindow.dismiss();  
	             return;  
	         } else {  
	             initsharePopuptsmallWindow();  
	         }  
	     }  
	     private void initsharePopuptsmallWindow() {  
	         LayoutInflater layoutInflater = LayoutInflater.from(MainActivity.this);  
	         View popupshareWindow = layoutInflater.inflate(R.layout.shareplay, null);  
	         
	      // 创建一个PopupWindow  
	         // 参数1：contentView 指定PopupWindow的内容  
	         // 参数2：width 指定PopupWindow的width  
	         // 参数3：height 指定PopupWindow的height  
	         
	   
	         // 获取屏幕和PopupWindow的width和height  
	          msharePopupsmallWindow = new PopupWindow(popupshareWindow,widthz/2, widthz/4); 
	          msharePopupsmallWindow.setAnimationStyle(R.style.AnimationFade); 

	         
	         
	         ImageView sharefbp=(ImageView)popupshareWindow.findViewById(R.id.sharefbpplay);
	         ImageView sharewpp=(ImageView)popupshareWindow.findViewById(R.id.sharewppplay);
	         sharefbp.setLayoutParams ( new LinearLayout.LayoutParams( MainActivity.widthz/4,MainActivity.widthz/4));
	         sharewpp.setLayoutParams ( new LinearLayout.LayoutParams( MainActivity.widthz/4,MainActivity.widthz/4));

	         Picasso.with(MainActivity.nowcontext).load(R.drawable.facebook). resize(MainActivity.widthz/4,MainActivity.widthz/4). into(sharefbp);
	         Picasso.with(MainActivity.nowcontext).load(R.drawable.whatapp). resize(MainActivity.widthz/4,MainActivity.widthz/4). into(sharewpp);
	         sharefbp.setScaleType(ImageView.ScaleType.FIT_XY);
	         sharewpp.setScaleType(ImageView.ScaleType.FIT_XY);
	         
	         sharefbp.setOnClickListener(new View.OnClickListener() {
	 			
	 			@Override
	 			public void onClick(View v) {
	 				
	 				// TODO Auto-generated method stub
	 				AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
	 				MainActivity.nowcontext);
	  
	 			// set title
	 		     alertDialogBuilder.setTitle("Share on FaceBook");
	  
	 			// set dialog message
	 			
	 			alertDialogBuilder
	 				.setMessage("You will share it on FaceBook!")
	 				.setCancelable(false)
	 				.setPositiveButton("Yes",new DialogInterface.OnClickListener() {
	 					public void onClick(DialogInterface dialog,int id) {
	 						// if this button is clicked, close
	 						// current activity
	 						 fbsharecode();
	 					}
	 				  })
	 				.setNegativeButton("No",new DialogInterface.OnClickListener() {
	 					public void onClick(DialogInterface dialog,int id) {
	 						// if this button is clicked, just close
	 						// the dialog box and do nothing
	 						dialog.cancel();
	 					}
	 				});
	  
	 				// create alert dialog
	 				AlertDialog alertDialog = alertDialogBuilder.create();
	  
	 				// show it
	 				alertDialog.show();
	 				msharePopupsmallWindow.dismiss(); 
	 				isup=false;
	 		
	 			}
	 		});
	         sharewpp.setOnClickListener(new View.OnClickListener() {
	 			
	 			@Override
	 			public void onClick(View v) {
	 				// TODO Auto-generated method stub
	 				onClickWhatsApp(v);
	 				msharePopupsmallWindow.dismiss(); 
	 				isup=false;
	 			}
	 		});
	     }

	     public void getsharePopupWindowInstance() {  
	         if (null != msharePopupWindow) {  
	             msharePopupWindow.dismiss();  
	             return;  
	         } else {  
	             initsharePopuptWindow();  
	         }  
	     }  
	     private void initsharePopuptWindow() {  
	         LayoutInflater layoutInflater = LayoutInflater.from(MainActivity.this);  
	         View popupshareWindow = layoutInflater.inflate(R.layout.shareplay, null);  
	         
	      // 创建一个PopupWindow  
	         // 参数1：contentView 指定PopupWindow的内容  
	         // 参数2：width 指定PopupWindow的width  
	         // 参数3：height 指定PopupWindow的height  
	         
	   
	         // 获取屏幕和PopupWindow的width和height  
	          msharePopupWindow = new PopupWindow(popupshareWindow,heiz/3, heiz/6); 
	          msharePopupWindow.setAnimationStyle(R.style.AnimationFade); 

	         
	         
	         ImageView sharefbp=(ImageView)popupshareWindow.findViewById(R.id.sharefbpplay);
	         ImageView sharewpp=(ImageView)popupshareWindow.findViewById(R.id.sharewppplay);
	         sharefbp.setLayoutParams ( new LinearLayout.LayoutParams( MainActivity.heiz/6,MainActivity.heiz/6));
	         sharewpp.setLayoutParams ( new LinearLayout.LayoutParams( MainActivity.heiz/6,MainActivity.heiz/6));
	        Picasso.with(MainActivity.nowcontext).load(R.drawable.facebook). resize(MainActivity.heiz/6,MainActivity.heiz/6). into(sharefbp);
	        Picasso.with(MainActivity.nowcontext).load(R.drawable.whatapp). resize(MainActivity.heiz/6,MainActivity.heiz/6). into(sharewpp);
	         sharefbp.setScaleType(ImageView.ScaleType.FIT_XY);
	         sharewpp.setScaleType(ImageView.ScaleType.FIT_XY);
	         
	         sharefbp.setOnClickListener(new View.OnClickListener() {
	 			
	 			@Override
	 			public void onClick(View v) {
	 				
	 				// TODO Auto-generated method stub
	 				AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
	 				MainActivity.nowcontext);
	  
	 			// set title
	 		     alertDialogBuilder.setTitle("Share on FaceBook");
	  
	 			// set dialog message
	 			
	 			alertDialogBuilder
	 				.setMessage("You will share it on FaceBook!")
	 				.setCancelable(false)
	 				.setPositiveButton("Yes",new DialogInterface.OnClickListener() {
	 					public void onClick(DialogInterface dialog,int id) {
	 						// if this button is clicked, close
	 						// current activity
	 						 fbsharecode();
	 					}
	 				  })
	 				.setNegativeButton("No",new DialogInterface.OnClickListener() {
	 					public void onClick(DialogInterface dialog,int id) {
	 						// if this button is clicked, just close
	 						// the dialog box and do nothing
	 						dialog.cancel();
	 					}
	 				});
	  
	 				// create alert dialog
	 				AlertDialog alertDialog = alertDialogBuilder.create();
	  
	 				// show it
	 				alertDialog.show();
	 				msharePopupWindow.dismiss(); 
	 				isup=false;
	 		
	 			}
	 		});
	         sharewpp.setOnClickListener(new View.OnClickListener() {
	 			
	 			@Override
	 			public void onClick(View v) {
	 				// TODO Auto-generated method stub
	 				onClickWhatsApp(v);
	 				msharePopupWindow.dismiss(); 
	 				isup=false;
	 			}
	 		});
	     }
	     public  void onClickWhatsApp(View view) {

	 	    Intent waIntent = new Intent(Intent.ACTION_SEND);
	 	    waIntent.setType("text/plain");
	 	            String text = null;
	 				try {
	 					text =" I like this video in STANDOUTER! \n"+MainActivity.website+"/video?ri="+fimjson.getJSONArray("items").getJSONObject(positionzhang).getString("id");
	 				} catch (JSONException e) {
	 					// TODO Auto-generated catch block
	 					e.printStackTrace();
	 				}
	 	    waIntent.setPackage("com.whatsapp");
	 	    if (waIntent != null) {
	 	        waIntent.putExtra(Intent.EXTRA_TEXT, text);//
	 	        context.startActivity(Intent.createChooser(waIntent, "Share with"));
	 	    } else {
	 	        Toast.makeText(MainActivity.nowcontext, "WhatsApp not Installed", Toast.LENGTH_SHORT)
	 	                .show();
	 	    }

	 	}
	     public void fbsharecode(){
	     	
	 		
	 			String link;
	 		JSONObject fbsharestate = null;
			try {
	 			link = MainActivity.website+"/video?ri="+jsonobject.getJSONArray("items").getJSONObject(positionzhang).getString("id");;;
	 			String picture=jsonobject.getJSONArray("items").getJSONObject(positionzhang).getString("imageUrl320");
	 			String des="I%20like%20the%20video%20"+jsonobject.getJSONArray("items").getJSONObject(positionzhang).getString("ownerName")+"%20in%20Standouter!";
	 			
	 			fbsharestate=Json.getJson("https://graph.facebook.com/me/feed?method=POST&picture="+picture+"&link="+link+
	 					"&message="+des+"&access_token="
	 					+Login.session.getAccessToken(),MainActivity.httpclient);
	            Log.i("",fbsharestate.toString());
	 			
	 		} catch (JSONException e) {
	 			// TODO Auto-generated catch block
	 			e.printStackTrace();
	 		}
	 		try {
	 			if(fbsharestate.get("id").toString() != null) {
	 				
	 			Toast toast;
	 			toast = Toast.makeText(MainActivity.nowcontext,
	 					"You have shared it!",
	 			        Toast.LENGTH_SHORT);
	 			toast.setGravity(Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 0);
	 			toast.show();
	 			}
	 			else{
	 				
	 			}
	 		} catch (JSONException e) {
	 			// TODO Auto-generated catch block
	 			LOGINSTATE=1;	
	 			Toast toast;
	 			toast = Toast.makeText(MainActivity.nowcontext,
	 					"Please login Facebook first",
	 			        Toast.LENGTH_SHORT);
	 			toast.setGravity(Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 0);
	 			toast.show();
	 			e.printStackTrace();
	 			Intent intent = new Intent(MainActivity.nowcontext, Login.class);
	 			MainActivity.nowcontext.startActivity(intent);
	 			//MainActivity.this.finish();
	 			//convertView2.getContext().overridePendingTransition(R.anim.activity_open,R.anim.activity_close);
	 		}
	     }
		
	     protected void onDestroy(){
	         
	         super.onDestroy();
	         //clean the file cache when root activity exit
	         //the resulting total cache size will be less than 3M   
	         if(isTaskRoot()){
	        	 Log.i("cash","cashclean");
	                 AQUtility.cleanCacheAsync(this);
	         }
	 } 
	         
    

}
