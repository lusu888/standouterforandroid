package com.example.standouter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;

import com.example.standouter.MainActivity.MySeekbar;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.krislq.sliding.beans.RowItem;
import com.krislq.sliding.beans.inforowitem;
import com.squareup.picasso.Picasso;

import adapter.Contestadapter;
import adapter.contextinfoadapter;
import adapter.infoitemadapter;
import adapter.profilovideodaapter;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.PixelFormat;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.SeekBar.OnSeekBarChangeListener;

public class Profile extends Activity {
	
	String uid;
	protected boolean isup;
	private PopupWindow msharePopupWindow;
	private PopupWindow mPopupsmallWindowleft;
	private PopupWindow mPopupsmallWindowright;
	private PopupWindow msharePopupsmallWindow;
	
	private PopupWindow mPopupWindowleft;

	

	private Button right;

	private PopupWindow mPopupWindowright;
	LayoutInflater layoutInflater ;
	protected JSONObject fimjson;

	public boolean isChanging;

	public SeekBar seekbar;
   private Contestadapter adapter;
   private String[] titlesname=new String[] { "506520975",
	        "07026746", "513736767", "516943231","524083761" };;
	
	        protected Timer mTimer;
	
	public  VideoView videoview;
	public  boolean isplay=false;
	public  boolean ispause=false;
	public  boolean  isheng;
	public  ProgressBar buffer;
	private String vdpath;
	private int vidtime;
	String jsoncode;
	 ImageView contest1;
	 public int listno;
	
	public  ListView listView;
	
	  FragmentTransaction fragmentTransaction;
    
      ImageView contestheader;
    
    
    /********************
     * 
     */
    protected Button play;
	private int isplayheng;

	protected PopupWindow mPopupWindow;
    private int visibleLastIndex = 0;   //最后的可视项索引  
    private int visibleItemCount;       // 当前窗口可见项总数 
    private View loadMoreView; 
   
 
    private Handler handler = new Handler();  
    
    private int[] location={-1,-1};
   
    private String[] titles =new String[5] ;
    private String[] VID = new String[5] ;

    private String[] images = new String[5] ;
    private  String[] vidpath=new String[5] ;
    

    private List<RowItem> rowItems;
    private int positionzhang;
    private ProgressBar loadMorepb;
    private TextView loadmoretext;
    private JSONObject jsonobject;
    List< inforowitem> InforItems;
	
	
	/************************************************/
	
	public  JSONObject uidjson;
	LinearLayout infolay;
	FrameLayout otherlay;
	SlidingMenu sm ;
	 Profile contest;

	private int[] votecont=new int[5];

	private boolean flag;

	protected ProgressDialog pd;

	private ProgressBar bufferct;

	private VideoView vvct;

	private ListView lvct;

	private profilovideodaapter adapter2;

	private int fanno;

	private View getcurrentview;

	private int heiz;

	private int widthz;

	private FrameLayout group;

	protected TimerTask mTimerTask;

	int mTimerflag=0;
	private JSONObject jsonmsg;
	private JSONObject jsonfan;
	private boolean isfan;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		/********************************************************************/
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		//getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);//for full screnn
		//etRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//
		getWindow().setFormat(PixelFormat.TRANSLUCENT);
		setContentView(R.layout.activity_profile);
		/***************************************************/

		/***********************************************************
		 * 
		 */
		mTimer=null;
		mTimerflag=0;
		positionzhang=-1;
		group=(FrameLayout)this.findViewById(R.id.proheadergroup);

		heiz=MainActivity.heiz;
		widthz=MainActivity.widthz;
		 getcurrentview =Profile.this.getCurrentFocus(); 
		if(MainActivity.gNotMgr!=null)
		MainActivity.gNotMgr.cancelAll();
		MainActivity.nowcontext=Profile.this;
		Bundle bData = this.getIntent().getExtras();
		uid=bData.getString("uid");
		//uid=Json.getJson(MainActivity.website+"/video/videoinfo?ri="+vid,MainActivity.httpclient).getString("ownerId");
		
		uidjson=Json.getJson(MainActivity.website+"/artist/artistinfo?ai="+uid,MainActivity.httpclient);
		jsonfan = Json.getJson(MainActivity.website+"/fansof?ui="+uid,MainActivity.httpclient);
		isfan=false;
		
		

		try {
			fanno=jsonfan.getInt("total");
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		
		// TODO Auto-generated method stub
		contest=Profile.this;
		
		ImageView plusfan=(ImageView)this.findViewById(R.id.plusfan);
		if(isfan==false){
	     Picasso.with(Profile.this).load(R.drawable.plusfan).into(plusfan);
		}else{
	        Picasso.with(Profile.this).load(R.drawable.removefan).into( plusfan);
		}
		plusfan.setOnClickListener(new View.OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
						Profile.this);

					alertDialogBuilder.setTitle("Be a fan");
					if (isfan==false)
					{

					try {
						alertDialogBuilder
							.setMessage("You will become a fan of "+uidjson.getString("name")+" "+uidjson.getString("surname"))
							.setCancelable(false)
							.setPositiveButton("Yes",new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,int id) {
									// if this button is clicked, close
									// current activity
									JSONObject fanresult = null;
									try {
										fanresult = Json.getJson(MainActivity.website+"/befan?ui="+MainActivity.jsonlateoginst.getString("registrationId")+"&ai="
										        +uid,MainActivity.httpclient);
									} catch (JSONException e1) {
										// TODO Auto-generated catch block
										e1.printStackTrace();
									}
									try {
										
										  if(fanresult.getString("status").equals("ok")){
										   Toast toast = Toast.makeText(MainActivity.nowcontext,
												 "Now you become a fan of "+uidjson.getString("name")+" "+uidjson.getString("surname"),
									                Toast.LENGTH_SHORT);
									        toast.setGravity(Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 0);
									        toast.show();
										  }else{
											  Toast toast = Toast.makeText(MainActivity.nowcontext,
													  fanresult.getString("message"),
											                Toast.LENGTH_SHORT);
											        toast.setGravity(Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 0);
											        toast.show();
										  }
									} catch (JSONException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}

								}
							  })
							.setNegativeButton("No",new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,int id) {
									// if this button is clicked, just close
									// the dialog box and do nothing
									dialog.cancel();
								}
							});
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					AlertDialog alertDialog = alertDialogBuilder.create();
					 
					// show it
					alertDialog.show();
					}else{
						try {
							alertDialogBuilder
								.setMessage("You will remove a fan of "+uidjson.getString("name")+" "+uidjson.getString("surname"))
								.setCancelable(false)
								.setPositiveButton("Yes",new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,int id) {
										// if this button is clicked, close
										// current activity
										try {
											
											Toast toast = Toast.makeText(MainActivity.nowcontext,
													Json.getJson(MainActivity.website+"/removefan?ui="+MainActivity.jsonlateoginst.getString("registrationId")+"&ai="
									                        +uid,MainActivity.httpclient).toString(),
										                Toast.LENGTH_SHORT);
										        toast.setGravity(Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 0);
										        toast.show();
										} catch (JSONException e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										}

									}
								  })
								.setNegativeButton("No",new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,int id) {
										// if this button is clicked, just close
										// the dialog box and do nothing
										dialog.cancel();
									}
								});
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						AlertDialog alertDialog = alertDialogBuilder.create();
						 
						// show it
						alertDialog.show();
					}
			}
			
		});

		
		buffer=(ProgressBar)findViewById(R.id.profilebuffer);
        // buffer.setX(widthz/2-80);
         buffer.setMax(10);
         buffer.setVisibility(View.GONE);
         bufferct=(ProgressBar)findViewById(R.id.bufferctpro);
         // buffer.setX(widthz/2-80);
         bufferct.setMax(10);
         bufferct.setVisibility(View.GONE);
		
		 videoview=(VideoView)Profile.this.findViewById(R.id.profilevideoView);
	       FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(MainActivity.widthz, MainActivity.widthz*9/16);
	       videoview.setLayoutParams(lp);
	       videoview.setMinimumHeight(MainActivity.widthz*9/16);
	       videoview.setMinimumWidth(MainActivity.widthz);
	        
	       
	       getPopupWindowInstance();  
			
           getPopupWindowInstanceleft() ;

           getPopupWindowInstanceright();
	       
	        videoview.setVisibility(View.INVISIBLE);
	        
videoview.setOnTouchListener(new View.OnTouchListener() {
				
				
				

				

				@Override
				public boolean onTouch(View v, MotionEvent event) {
					// TODO Auto-generated method stub
					finishflag=0;
					Log.i("","ssd");
					// TODO Auto-generated method stubp
					
						if(mTimerflag==0){ 
					 	  	//----------定时器记录播放进度---------//    
					 	        mTimer = new Timer();    
					 	        mTimerTask = new TimerTask() {    
					 	            @Override    
					 	            public void run() {     
					 	                if(isChanging==true) {   
					 	                    return;    
					 	                } 
					 	                if(isheng){
					 	                seekbar.setMax(videoview.getDuration());//设置进度条  
					 	               try{ seekbar.setProgress(vvct.getCurrentPosition());  
					 	                }
					 	                catch(Exception e)
					 	               {
					 	                  Log.i("currett","error") ;// error catch 1
					 	                 } 
					 	                }
					 	            }    
					 	        };   
					 	        mTimer.schedule(mTimerTask, 0, 100); 
					 	       mTimerflag=1;
					 	  	   
						}
					if(isheng){
						
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
							//play.setBackgroundResource(R.drawable.play);

							
							
						}else
						 {
							getPopupWindowInstance();  
				            getPopupWindowInstanceleft() ;
				            getPopupWindowInstanceright();
						    getsharePopupWindowInstance();  
							videoview.start();
							Log.i("","ssd"+isplayheng);
							isplayheng=1;
							//play.setBackgroundResource(R.drawable.pause);

							
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
			    		  isplay=false;
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
					Toast toast = Toast.makeText(Profile.this,
	    	                "ok",
	    	                Toast.LENGTH_SHORT);
					buffer.setVisibility(View.INVISIBLE);
	    	        toast.setGravity(Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 0);
	    	        toast.show();
				}
	        	
	        });
	        
	        vvct=(VideoView)this.findViewById(R.id.vvctpro);
		       FrameLayout.LayoutParams lp2 = new FrameLayout.LayoutParams(MainActivity.widthz, MainActivity.widthz*9/16);
		       vvct.setLayoutParams(lp2);
		       vvct.setMinimumHeight(MainActivity.widthz*9/16);
		       vvct.setMinimumWidth(MainActivity.widthz);
		        
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
			    		  isplay=false;
						}else{
							play.setBackgroundResource(R.drawable.play);

							vvct.seekTo(1500);
							isplayheng=0;
							getPopupWindowInstance();  
				            mPopupWindow.showAtLocation(getcurrentview, Gravity.BOTTOM, 0, 0);
				            

						}
						
						
						
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
					 	                if(isheng){
					 	                seekbar.setMax(vvct.getDuration());//设置进度条  
					 	               try{ seekbar.setProgress(vvct.getCurrentPosition());  
					 	                }
					 	                catch(Exception e)
					 	               {
					 	                  Log.i("currett","error") ;// error catch 1
					 	                 }
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
		

		       vvct.setOnPreparedListener(new OnPreparedListener(){

					@Override
					public void onPrepared(MediaPlayer arg0) {
						// TODO Auto-generated method stub
						Toast toast = Toast.makeText(Profile.this,
		    	                "ok",
		    	                Toast.LENGTH_SHORT);
						bufferct.setVisibility(View.INVISIBLE);
		    	        toast.setGravity(Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 0);
		    	        toast.show();
					}
		        	
		        });
				
		
		
				
			
		 listView = (ListView)Profile.this.findViewById(R.id.profilelistView);
		  loadMoreView = getLayoutInflater().inflate(R.layout.load_more, null);  
	     
	      loadMorepb=(ProgressBar)loadMoreView.findViewById(R.id.progressBarload);
	      loadmoretext=(TextView)loadMoreView.findViewById(R.id.loadmoretext);
	      loadMorepb.setVisibility(View.INVISIBLE);
	      listView.addFooterView(loadMoreView);  
	      
	     
	  
		
		
		/********************************************************************/
		
		
		
		
		
		infolay=(LinearLayout)Profile.this.findViewById(R.id.infolay);
		otherlay=(FrameLayout)Profile.this.findViewById(R.id.otherlay);
		
		initprofilesmint();
		
		
		
		
		
		/***********************************************************
		 * for header
		 */
		
		ImageView contestheader = (ImageView)Profile.this.findViewById(R.id.profileheader);
	     contestheader.setLayoutParams(new FrameLayout.LayoutParams( MainActivity.widthz,MainActivity.widthz/4
               ));
	    // Picasso.with(Profile.this).load(R.drawable.shadowvd). resize(MainActivity.widthz, MainActivity.widthz/4). into(contestheader);
	     
	     ImageView backheader=(ImageView)Profile.this.findViewById(R.id.profileback);
	     backheader.setLayoutParams(new FrameLayout.LayoutParams( MainActivity.widthz/4*124/180,MainActivity.widthz/4
               ));
	     backheader.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				
				// TODO Auto-generated method stub
				
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
			isplay=false;
	   	     
				finish();
				
			}
		});
	     
	     
	     Picasso.with(Profile.this).load(R.drawable.lw). resize( MainActivity.widthz/4*124/180, MainActivity.widthz/4). into( backheader);
	     
	     ImageView menuheader=(ImageView)Profile.this.findViewById(R.id.profilemenuheader);
	     menuheader.setLayoutParams(new FrameLayout.LayoutParams( MainActivity.widthz/4*124/180,MainActivity.widthz/4
               ));
	     menuheader.setX(MainActivity.widthz- MainActivity.widthz/4*124/180);
	     menuheader.setOnClickListener(new View.OnClickListener() {
			
			

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
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
			isplay=false;
				sm.toggle();
				
			}
		});
	     Picasso.with(Profile.this).load(R.drawable.menuwhite). resize( MainActivity.widthz/4*124/180, MainActivity.widthz/4). into(menuheader);
	    
	    
	     
	     TextView headertitle=(TextView)Profile.this.findViewById(R.id.headername);
	     try {
			headertitle.setText(uidjson.getString("name")+" "+uidjson.getString("surname"));
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
			
			
			
	     /***********************************************************
			 * 
			 */
	     
	     
	     /***********************************************************
			 * info
			 
		*/
		
		
		
		TextView nickname=(TextView)Profile.this.findViewById(R.id.nickname);
		TextView city=(TextView)Profile.this.findViewById(R.id.city);
		TextView bio=(TextView)Profile.this.findViewById(R.id.biography);
		
		try {
			
			nickname.setText("Name: "+uidjson.getString("name")+" "+uidjson.getString("surname"));
			city.setText("City: "+uidjson.getString("city"));
			bio.setText(uidjson.getString("biography"));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 /**********************************************************
		 * 
		 */
		 if(MainActivity.x==0);
	      else loadmsg();
		  Timer timer = new Timer(true);
	        
	        timer.schedule(new timerTask(), 1000, 1000);
     

	        new Thread(new MyThread()).start();
	        pd = ProgressDialog.show(Profile.this, "dowloading", "dowloading，please wait……");

			 new Thread(runnableproinfo).start();
		
		
		
		
		
	}
	
	
	
	public void initprofilesmint(){
		sm = new SlidingMenu(this);
		sm = new SlidingMenu(this);
		sm.setMode(SlidingMenu.RIGHT);
	    sm.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN); 
        sm.setShadowWidth(0);
        sm.setShadowDrawable(R.drawable.shadow);
        sm.setBehindOffset(MainActivity.widthz/4*3);
        sm.setFadeDegree(0.35f);
        //设置slding menu的几种手势模式
        //TOUCHMODE_FULLSCREEN 全屏模式，在content页面中，滑动，可以打开sliding menu
        //TOUCHMODE_MARGIN 边缘模式，在content页面中，如果想打开slding ,你需要在屏幕边缘滑动才可以打开slding menu
        //TOUCHMODE_NONE 自然是不能通过手势打开啦
        sm.attachToActivity(this, SlidingMenu.TOUCHMODE_FULLSCREEN);
        sm.setMenu(R.layout.profilemenu);
        Profile.this.infolay.setVisibility(View.VISIBLE);
		Profile.this.otherlay.setVisibility(View.INVISIBLE);
		
		contest1=(ImageView)this.findViewById(R.id.profilemenuphoto);
        contest1.setLayoutParams ( new LinearLayout.LayoutParams( MainActivity.widthz/4,MainActivity.widthz/4
                ));
       
        try {
			Picasso.with(this).load(MainActivity.website+""+uidjson.getString("profileImageUrl")).into(contest1);
		} catch (JSONException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
        contest1.setScaleType(ImageView.ScaleType.CENTER_CROP);
        contest1.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
					
						
						Profile.this.infolay.setVisibility(View.VISIBLE);
						Profile.this.otherlay.setVisibility(View.INVISIBLE);
						pd = ProgressDialog.show(Profile.this, "dowloading", "dowloading，please wait……");

						 new Thread(runnableproinfo).start();

						sm.toggle();
			}
		});
        
       
        
        ImageView portfoliomenu = (ImageView)this.findViewById(R.id.portfoliomenu);
        portfoliomenu.setLayoutParams ( new LinearLayout.LayoutParams( MainActivity.widthz/4,MainActivity.widthz/4
                ));
        portfoliomenu.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Profile.this.infolay.setVisibility(View.INVISIBLE);
				Profile.this.otherlay.setVisibility(View.VISIBLE);
				pd = ProgressDialog.show(Profile.this, "dowloading", "dowloading，please wait……");

				 new Thread(runnableloadportf).start();

				sm.toggle();
				
			}
		});
	   	Picasso.with(this).load(R.drawable.portfolio). resize(MainActivity.widthz/4, MainActivity.widthz/4). into(portfoliomenu);

        
        ImageView fanmenu = (ImageView)this.findViewById(R.id.fansmenu);
        fanmenu.setLayoutParams ( new LinearLayout.LayoutParams( MainActivity.widthz/4,MainActivity.widthz/4
                ));
        fanmenu.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Profile.this.infolay.setVisibility(View.INVISIBLE);
				Profile.this.otherlay.setVisibility(View.VISIBLE);
				
				int totlefanno = 0;
				try {
					totlefanno = jsonfan.getInt("total");
				} catch (JSONException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
				InforItems = new ArrayList<inforowitem>();   
				Log.i("zsdf",totlefanno+""); 
				rowItems = new ArrayList<RowItem>();   
				for(int i=0; i<totlefanno;i++){
					try {
						
						 
					String	imagesfan=MainActivity.website+""+jsonfan.getJSONArray("items").getJSONObject(i).getString("profileImageUrl");
					String	titlesfan=jsonfan.getJSONArray("items").getJSONObject(i).getString("name");
					String	desfan=jsonfan.getJSONArray("items").getJSONObject(i).getString("nickname");
					inforowitem Inforowitem=new inforowitem(imagesfan, titlesfan, desfan);
					InforItems.add(Inforowitem);
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				infoitemadapter infoadapter = new  infoitemadapter(Profile.this, InforItems,Profile.this);
		        listView.setAdapter(infoadapter);
		        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){

					@Override
					public void onItemClick(AdapterView<?> parent, View view, int position,
				            long id) {
						// TODO Auto-generated method stub
						MainActivity.x=0;
						Intent y = new Intent();
	    				Bundle bundle = new Bundle();
	                    try {
							bundle.putString("uid",jsonfan.getJSONArray("items").getJSONObject(position).getString("id"));
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
	                    y.putExtras(bundle);
	    	            y.setClass(MainActivity.nowcontext, Profile.class);
	    	            
	    	          
	    	            MainActivity.nowcontext.startActivity(y);
	                    finish();

	    	           // ((Activity) context).finish();
	    	            //((Activity) MainActivity.nowcontext).overridePendingTransition(R.anim.activity_open,R.anim.activity_close); 
	    	            pd.dismiss();
					}
		        	
		        });
		        
				
				
				Log.i("zsdf",jsonfan.toString()); 
				Log.i("zsdf",totlefanno+""); 
				 
				Log.i("zsdf",listView.getCount()+""); 
		            
		          
				//loadlistview(MainActivity.website+"/video/search?ss=artist&so=most_voted&sp="+uid);
				sm.toggle();
				
			}
		});
	   	Picasso.with(this).load(R.drawable.fans). resize(MainActivity.widthz/4, MainActivity.widthz/4). into(fanmenu);
	   	
	   	

        
        
        ImageView playlistmenu = (ImageView)this.findViewById(R.id.playlistmenu);
       playlistmenu.setLayoutParams ( new LinearLayout.LayoutParams( MainActivity.widthz/4,MainActivity.widthz/4
                ));
       playlistmenu.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Profile.this.infolay.setVisibility(View.INVISIBLE);
				Profile.this.otherlay.setVisibility(View.VISIBLE);
				pd = ProgressDialog.show(Profile.this, "dowloading", "dowloading，please wait……");

				 new Thread(runnableplaylist).start();

				
				sm.toggle();
				
			}
		});
	   	Picasso.with(this).load(R.drawable.playlist). resize(MainActivity.widthz/4, MainActivity.widthz/4). into(playlistmenu);

	   	ImageView msgmenu = (ImageView)this.findViewById(R.id.msgmenu);
	   	try {
	   		msgmenu.setVisibility(View.VISIBLE);
			if(MainActivity.jsonlateoginst.getString("registrationId").equals(uid)){
			
			msgmenu.setLayoutParams ( new LinearLayout.LayoutParams( MainActivity.widthz/4,MainActivity.widthz/4
			        ));
			msgmenu.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					loadmsg();
					sm.toggle();
					
				}
			});
			Picasso.with(this).load(R.drawable.news). resize(MainActivity.widthz/4, MainActivity.widthz/4). into(msgmenu);
			}
			else{
				msgmenu.setVisibility(View.GONE);
			}
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

       
        ImageView logoutmenu = (ImageView)this.findViewById(R.id.logoutmenu);
       logoutmenu.setLayoutParams ( new LinearLayout.LayoutParams( MainActivity.widthz/4,MainActivity.widthz/4
               ));
       
       
       try {
		if(MainActivity.jsonlateoginst.getString("authenticated").equals("true")&&MainActivity.jsonlateoginst.getString("registrationId").equals(uid)){
		   	//Log.i("zzzzzzzzzzzz",Json.getJson("https://graph.facebook.com/me?access_token="+Login.AT+"&method=GET&fields=picture.type(large)").getJSONObject("picture").getJSONObject("data").getString("url"));
		   	//Log.i("zz",Json.getJson(MainActivity.website+"/fbaccess?accessToken="+Login.AT,httpclient).toString());
			   MainActivity.LOGINSTATE=1;
			   	Picasso.with(this).load(R.drawable.logout). resize(MainActivity.widthz/4, MainActivity.widthz/4). into(logoutmenu);

		   	//Log.i("zz",jsonlateoginst.toString());
		   	//MainActivity.loginoperas();
		   }
		   else
		   {
			   logoutmenu.setVisibility(View.GONE);
		   	
		   }
	} catch (JSONException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}

       logoutmenu.setOnClickListener(new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			try {
				if(MainActivity.jsonlateoginst.getString("authenticated").equals("true")){
				   	//Log.i("zzzzzzzzzzzz",Json.getJson("https://graph.facebook.com/me?access_token="+Login.AT+"&method=GET&fields=picture.type(large)").getJSONObject("picture").getJSONObject("data").getString("url"));
				   	//Log.i("zz",Json.getJson(MainActivity.website+"/fbaccess?accessToken="+Login.AT,httpclient).toString());
					   MainActivity.LOGINSTATE=1;
					   Login.callFacebookLogout(Profile.this);
					   MainActivity. httpclient = new DefaultHttpClient(); // for port 80 requests!
					   MainActivity.LOGINSTATE=0;
					   sm.toggle();
					   finish();
						
				        overridePendingTransition(R.anim.activity_open_right,R.anim.activity_close_right);
				        


				   	//Log.i("zz",jsonlateoginst.toString());
				   }
				   
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	});
	}
	

	private void loadmsg(){
		Profile.this.infolay.setVisibility(View.INVISIBLE);
		Profile.this.otherlay.setVisibility(View.VISIBLE);
		String msgprofilo = null;
		try {
			msgprofilo = Login.getHtmlByGet(MainActivity.website+"/notifications?ui="+MainActivity.jsonlateoginst.getInt("registrationId"),MainActivity.httpclient);
		} catch (JSONException e3) {
			// TODO Auto-generated catch block
			e3.printStackTrace();
		}
		 jsonmsg = null;
		try {
			jsonmsg = new JSONObject(msgprofilo);
		} catch (JSONException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		int totlefanno = 0;
		try {
			totlefanno = jsonmsg.getInt("total");
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		InforItems = new ArrayList<inforowitem>();   
		Log.i("zsdf",totlefanno+""); 
		rowItems = new ArrayList<RowItem>();   
		for(int i=0; i<totlefanno;i++){
			try {
				
				 
			String	imagesfan=MainActivity.website+""+jsonmsg.getJSONArray("items").getJSONObject(i).getString("authorProfileImageUrl");
			String	titlesfan=jsonmsg.getJSONArray("items").getJSONObject(i).getString("authorName")+jsonmsg.getJSONArray("items").getJSONObject(i).getString("authorSurname");
			String	desfan=titlesfan+" "+jsonmsg.getJSONArray("items").getJSONObject(i).getString("customMessage");
			inforowitem Inforowitem=new inforowitem(imagesfan, titlesfan, desfan);
			InforItems.add(Inforowitem);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		infoitemadapter infoadapter = new  infoitemadapter(Profile.this, InforItems,Profile.this);
        listView.setAdapter(infoadapter);


       listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int position,
				long arg3) {
			 MainActivity.x=0;
				Intent y = new Intent();
				Bundle bundle = new Bundle();
		        try {
					bundle.putString("uid",jsonmsg.getJSONArray("items").getJSONObject(position).getString("authorId"));
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		        y.putExtras(bundle);
		        y.setClass(MainActivity.nowcontext, Profile.class);
		        
		      
		        MainActivity.nowcontext.startActivity(y);
		        finish();


			// TODO Auto-generated method stub
			
		}
	})	;	
		
		//Log.i("zsdf",jsonmsg.toString()); 
		//Log.i("zsdf",totlefanno+""); 
		 
		//Log.i("zsdf",listView.getCount()+""); 
            
          
		
		
	}
	public void loadprofiletinfo(String jsoncode2){
		mTimer=null;
		positionzhang=-1;

		 lvct=(ListView)this.findViewById(R.id.lvctpro);
			
			flag=false;
	    	//listView.destroyDrawingCache();
			jsoncode=jsoncode2;
			jsonobject=Json.getJson(jsoncode,MainActivity.httpclient);
			
			listno=20;

			

			
			try {
				listno=Json.getJson(jsoncode,MainActivity.httpclient).getInt("totalResults");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			//Log.i("","NO IS "+listno);
			
			int smalltno;
			if(listno<5) smalltno=listno;
			else smalltno=5;
			if(listno!=0){
				for(int i=0;i<1;i++){
		      		try {
							images[i]=jsonobject.getJSONArray("items").getJSONObject(i).getString("imageUrl320");
							titles[i]=jsonobject.getJSONArray("items").getJSONObject(i).getString("ownerNickname");
							vidpath[i]=jsonobject.getJSONArray("items").getJSONObject(i).getString("videoUrl480");
							VID[i]=jsonobject.getJSONArray("items").getJSONObject(i).getString("id");
							votecont[i]=fanno;
							
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
		      	}
	      	
			}else{
				for(int i=0;i<1;i++){
		      		try {
							images[i]="novideo";
							titles[i]=uidjson.getString("nickname");
							
							vidpath[i]="";
							VID[i]=uidjson.getString("id");
							votecont[i]=fanno;
							
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
		      	}
			}
			
	       rowItems = new ArrayList<RowItem>();
	        for (int i = 0; i < 1; i++) {
	            RowItem item = new RowItem(images[i], titles[i], "",vidpath[i],VID[i],votecont[i],flag);
	            rowItems.add(item);
	        }
	       
	        adapter2 = new  profilovideodaapter(this, rowItems,this);
	       
	       
	        
	        
		
	}
	public void lvinfoint(){
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
				        if(isplay==false){
				        	
				    		if(position!=positionzhang) {vidtime=0;
				    		Log.i("",""+position+","+positionzhang);
				    		isplay=true;
				    		
					        Toast toast = Toast.makeText(view.getContext(),
					                "Item " + (position + 1+a*5) + ": " + rowItems.get(position)+"Position:"+Pos[1]+"xx"+location[1],
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
							    isplay=true;

				    		} 
					        
					        
					        positionzhang=position;
					        
				    	}
				    	else{
				    		if(position==positionzhang)
				    		{
				    			vvct.pause();
				    			vidtime=vvct.getCurrentPosition();
							//videoview.setVisibility(View.INVISIBLE);
				    			isplay=false;
				    		}
				    		else
				    		{
				    			vidtime=0;
				        		Log.i("",""+position+","+positionzhang);
				        		isplay=true;
				        		
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
					fs=jsonobject.getJSONArray("items").getJSONObject(i).getString("title");
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
				
	        	RowItem item = new RowItem(as, bs,fs, cs,ds,es,flag);
	        	
	            adapter.addItem(item);
	        }
	        }
	    }


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.profile, menu);
		return true;
	}
	public void finish() {  
	    // TODO Auto-generated method stub  
	 
	   super.onDestroy();
	   MainActivity.nowcontext=MainActivity.context;
	   super.finish();
	   
	    //关闭窗体动画显示  
	    this.overridePendingTransition(R.anim.activity_open_right,R.anim.activity_close_right);  
	}
	
	 public void onConfigurationChanged(Configuration newConfig) {  
		 
	      super.onConfigurationChanged(newConfig);  
	      Log.i("zhang"," == onConfigurationChanged");  
	      if(this.getResources().getConfiguration().orientation==Configuration.ORIENTATION_LANDSCAPE)
	      {  isheng=true;
	      	        
	    	  if(isplay==true||ispause){
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
	    		  isplay=(isplayheng!=0);
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
	    	  message.what = isplay?1:0; 
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

	protected int finishflag;

	protected int ytemp;

	protected int ytemp2;

	protected int tempx;  
	    
	    
	    void loadlistview(String jsoncode2){
	    	mTimer=null;
			positionzhang=-1;

	    	//flag=false;
	    	listView.destroyDrawingCache();
			jsoncode=jsoncode2;
			jsonobject=Json.getJson(jsoncode,MainActivity.httpclient);
			
			listno=20;
			

			
			try {
				listno=Json.getJson(jsoncode,MainActivity.httpclient).getInt("totalResults");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			//Log.i("","NO IS "+listno);
			
			int smalltno;
			if(listno<5) smalltno=listno;
			else smalltno=5;
			for(int i=0;i<smalltno;i++){
	      		try {
						images[i]=jsonobject.getJSONArray("items").getJSONObject(i).getString("imageUrl320");
						titles[i]=jsonobject.getJSONArray("items").getJSONObject(i).getString("ownerName")+" "+jsonobject.getJSONArray("items").getJSONObject(i).getString("ownerSurname");
						titlesname[i]=jsonobject.getJSONArray("items").getJSONObject(i).getString("title");						vidpath[i]=jsonobject.getJSONArray("items").getJSONObject(i).getString("videoUrl480");
						VID[i]=jsonobject.getJSONArray("items").getJSONObject(i).getString("id");
						votecont[i]=jsonobject.getJSONArray("items").getJSONObject(i).getInt("votesCount");
						
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
	      	}
	      	
	     
	       rowItems = new ArrayList<RowItem>();
	        for (int i = 0; i < smalltno; i++) {
	            RowItem item = new RowItem(images[i], titles[i],titlesname[i], vidpath[i],VID[i],votecont[i],flag);
	            rowItems.add(item);
	        }
	       
	        adapter = new  Contestadapter(this, rowItems,this);
	        
	        
	      
		}
	    public void loadportfint(){
	    	 // listView.setAdapter(adapter);
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
				        if(isplay==false){
				        	getPopupsmallWindowInstanceleft();
				            getPopupsmallWindowInstanceright();
				   	        getsharePopupsmallWindowInstance() ;

				        	
				    		if(position!=positionzhang){ vidtime=0;
				    		Log.i("",""+position+","+positionzhang);
				    		isplay=true;
				    		
					        Toast toast = Toast.makeText(view.getContext(),
					                "Item " + (position + 1+a*5) + ": " + rowItems.get(position)+"Position:"+Pos[1]+"xx"+location[1],
					                Toast.LENGTH_SHORT);
					        toast.setGravity(Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 0);
					        toast.show();
					        
					        Uri uri = Uri.parse(adapter.getItemdesc(position));
					       videoview.setVideoURI(uri);
					       adapter.getItemdesc(position);
					        //MainActivity.vidpath=vidpath[position+a*5];
					       videoview.setMinimumHeight(MainActivity.widthz*9/16);
					        videoview.setMinimumWidth(MainActivity.widthz);
					        videoview.requestFocus();
					        videoview.setY(Pos[1]-location[1]);
					      buffer.setY(Pos[1]- widthz/10);
					       videoview.seekTo(vidtime);
					       videoview.start();
					         videoview.setVisibility(View.VISIBLE);
					       buffer.setVisibility(View.VISIBLE);
				    		}else{
				    			videoview.start();
				    		   videoview.setY(Pos[1]-location[1]);
				    			isplay=true;
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
				    			isplay=false;
				    		}
				    		else
				    		{
				    			getPopupsmallWindowInstanceleft();
					            getPopupsmallWindowInstanceright();
						   	     getsharePopupsmallWindowInstance() ;

					        	 
				    			vidtime=0;
				        		Log.i("",""+position+","+positionzhang);
				        		isplay=true;
				         		
				    	        Toast toast = Toast.makeText(view.getContext(),
				    	                "Item " + (position + 1+a*5) + ": " + rowItems.get(position)+"Position:"+Pos[1],
				    	                Toast.LENGTH_SHORT);
				    	        toast.setGravity(Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 0);
				    	        toast.show();
				    	        
				    	        Uri uri = Uri.parse(adapter.getItemdesc(position));
						        videoview.setVideoURI(uri);
						        adapter.getItemdesc(position);
				    	       //MainActivity.vidpath=vidpath[position+a*5];
				    	       videoview.setMinimumHeight(widthz*9/16);
				    	        videoview.setMinimumWidth(widthz);
				    	        videoview.requestFocus();
				    	       videoview.setY(Pos[1]-location[1]);
				    	        buffer.setY(Pos[1]- widthz/10);
				    	       videoview.seekTo(vidtime);
				    	       videoview.start();
				    	       videoview.setVisibility(View.VISIBLE);
				    	      buffer.setVisibility(View.VISIBLE);
				    	        
				    	        //setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
				    	        positionzhang=position;
				    		}
				    		//tempx=videoview.getDuration();
					}

				        
				
	        	
	        }});
	        listView.setOnScrollListener(new OnScrollListener(){

	        	 @Override  
	        	    public void onScroll(AbsListView view, int firstVisibleItem, final int visibleItemCount, int totalItemCount) {  
	        	        Profile.this.visibleItemCount = visibleItemCount;  
	        	        visibleLastIndex = firstVisibleItem + visibleItemCount - 1; 
	        	        //Contestadapter.mPopupWindow.dismiss();
	        	        //Contestadapter.isup=false;     
	        	        
	        	    }  

				@Override
				public void onScrollStateChanged(AbsListView view, int scrollState) {  
					
					
			        int itemsLastIndex = adapter.getCount() - 1;    //数据集最后一项的索引  
			        final int lastIndex = itemsLastIndex + 1;             //加上底部的loadMoreView项  
			        if (scrollState == OnScrollListener.SCROLL_STATE_IDLE && visibleLastIndex == lastIndex) {  
			            //如果是自动加载,可以在这里放置异步加载数据的代码  
			            Log.i("LOADMORE", "loading...");  
			            Log.i("",lastIndex+"");
			            Log.i("","gf"+listno);
			            loadmoretext.setText("Loadind...");
			            loadMorepb.setVisibility(View.VISIBLE);
			            Message msg =new Message();
			            msg.what=lastIndex;
			            handlersocll.sendMessageDelayed(msg, 1000);
						//handlersocll.sendMessage(msg);
			            
			           // loadMoreButton.setText("loading...");   //设置按钮文字loading  
			            
			                   
			                   // loadMoreButton.setText("load more");    //恢复按钮文字 
			                   
			                
			           
			        }  
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
		               isplay=false;
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
		            	isplay=false;
		                break; 
		            }
			        
			        
			    }  
	        	
	        });
	    }
	    private Handler handlersocll=new Handler(){
		 	   @Override
			public void handleMessage(final Message msg){
		 		  // super.handleMessage(msg2);
		 		   
						// TODO Auto-generated method stub
						 if(msg.what<listno){ 
			                   loadData();  
			                     
			                   adapter.notifyDataSetChanged(); //数据集变化后,通知adapter  
			                   listView.setSelection(visibleLastIndex - visibleItemCount + 1); //设置选中项  
			                   loadmoretext.setText("Load More");
			                   } else{
			                	   if(listno>3){
					                   	 loadmoretext.setText("All Loaded");
					                	 }else{
					                		 
							                	   loadmoretext.setText("");
							                   
					                	 }
			                   }
			                 
			                   loadMorepb.setVisibility(View.INVISIBLE);
					
		 	   }
		     };
	    public class MyThread implements Runnable{

	    	@Override
	    	public void run() {
	    		
	    	}
	    	
	    }
	    private Handler handlerlv=new Handler(){
		 	   @Override
			public void handleMessage(Message msglv){
		 		  // super.handleMessage(msg2);
		 		   if( msglv.what==1){
		 			  
		 			 
		 		     listView.setAdapter(adapter);
		 		    loadportfint();
		 		   if(listno>3){
	                   	 loadmoretext.setText("All Loaded");
	                	 }else{
	                		 
			                	   loadmoretext.setText("");
			                   
	                	 }
		 			pd.dismiss(); 
		 		   }
		 		   
		 		   
		 	   }
		     };
		     private Handler handlerlvpro=new Handler(){
			 	   @Override
				public void handleMessage(Message msglv){
			 		  // super.handleMessage(msg2);
			 		   if( msglv.what==1){
			 			  
			 			 
			 			  lvct.setAdapter(adapter2);
			 			 lvinfoint() ;
			 			pd.dismiss(); 
			 		   }
			 		   
			 		   
			 	   }
			     };
	    
	    Runnable runnableloadportf=new Runnable(){

     		@Override
     		public void run() {
     			// TODO Auto-generated method stub
     			flag=false;
     			loadlistview(MainActivity.website+"/video/search?ss=artist&so=most_voted&sp="+uid);
     			Message msglv= new Message();
     	        msglv.what=1;
     	        handlerlv.sendMessage(msglv);
         	   
      }
              
     };
     Runnable runnableplaylist=new Runnable(){

  		@Override
  		public void run() {
  			// TODO Auto-generated method stub
  			
  			//String jsonstring= "{\"totalResults\":"+MainActivity.votecontno+",\"items\":[" +MainActivity.votejsonstring+"]}";
			/*
			try {
				loadlistview2(new JSONObject(jsonstring));
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			*/
  			flag=true;
 			loadlistview(MainActivity.website+"/video/search?ss=global&so=most_voted");

  			Message msglv= new Message();
  	        msglv.what=1;
  	        handlerlv.sendMessage(msglv);
      	   
   }
           
  };
	    
  Runnable runnableproinfo=new Runnable(){

		@Override
		public void run() {
			// TODO Auto-generated method stub
			
			loadprofiletinfo(MainActivity.website+"/video/search?ss=artist&so=most_voted&sp="+uid);
			Message msglvpro= new Message();
	        msglvpro.what=1;
	        handlerlvpro.sendMessage(msglvpro);
    	   
 }
         
};

/********************
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 */
	protected String[] filmimg;


	
	
	
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
		   layoutInflater = LayoutInflater.from(Profile.this);
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
		   private Gallery filmshow;

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
		  	    ImageAdapter imageadapter = new ImageAdapter(Profile.this);
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
			        		isplay=true;

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
				        		isplay=true;

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
	
	
	
			  
	private void initPopuptWindowleft() {  
	       LayoutInflater layoutInflater = LayoutInflater.from(this);  
	       View popupWindow2 = layoutInflater.inflate(R.layout.leftb, null);  
	       Button left = (Button) popupWindow2.findViewById(R.id.left);
	       left.setOnClickListener(new View.OnClickListener() {
				
				private boolean isup;
	
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					
	
					//Login.publishStory((Activity) convertView2.getContext(), "https://www.google.com.tw", "https://www.google.com.tw", "https://www.google.com.tw");
					try {
						if(MainActivity.jsonlateoginst.getString("authenticated").equals("true")){
							
						int[] xy = new int[2];
						v.getLocationInWindow(xy);
						Log.i("shapo",""+xy[1] );
						
						
						if(isup==true){msharePopupWindow.dismiss();isup=false;}
						else{
							
							 getsharePopupWindowInstance();
							msharePopupWindow.showAtLocation(Profile.this.getcurrentview, Gravity.LEFT, heiz/6, 0);
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
							isplay=false;
							isplayheng=0;
							
							Intent intent = new Intent(MainActivity.nowcontext, Login.class);
							MainActivity.nowcontext.startActivity(intent);
							//Profile.this.finish();
							//((Activity) convertView2.getContext()).overridePendingTransition(R.anim.activity_open,R.anim.activity_close);
							
						}
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
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
								 MainActivity.convertNodeToText(Jsoup.parse(votejson.getString("message"))),
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
	       Button left = (Button) popupWindow2.findViewById(R.id.left);
	       left.setOnClickListener(new View.OnClickListener() {
				
				private boolean isupshu;
	
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					
	
					//Login.publishStory((Activity) convertView2.getContext(), "https://www.google.com.tw", "https://www.google.com.tw", "https://www.google.com.tw");
					try {
						if(MainActivity.jsonlateoginst.getString("authenticated").equals("true")){
							
						int[] xy = new int[2];
						v.getLocationInWindow(xy);
						Log.i("shapo",""+xy[1] );
						
						
						if(isupshu==true){msharePopupsmallWindow.dismiss();isupshu=false;}
						else{
							
							initsharePopuptsmallWindow() ;
							msharePopupsmallWindow.showAtLocation(Profile.this.getcurrentview, Gravity.LEFT, widthz/4, ytemp2-widthz/16*9-widthz/16);
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
							isplay=false;
							isplayheng=0;
							
							Intent intent = new Intent(MainActivity.nowcontext, Login.class);
							MainActivity.nowcontext.startActivity(intent);
							//Profile.this.finish();
							//((Activity) convertView2.getContext()).overridePendingTransition(R.anim.activity_open,R.anim.activity_close);
							
						}
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
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
								 MainActivity.convertNodeToText(Jsoup.parse(votejson.getString("message"))),
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
	       LayoutInflater layoutInflater = LayoutInflater.from(Profile.this);  
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
	       LayoutInflater layoutInflater = LayoutInflater.from(Profile.this);  
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
		        Profile.this.startActivity(Intent.createChooser(waIntent, "Share with"));
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
				MainActivity.LOGINSTATE=1;	
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
	  
	       

}

