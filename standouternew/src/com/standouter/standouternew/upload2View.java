package com.standouter.standouternew;

import com.standouter.standouternew.R;
import com.standouter.standouternew.R.id;
import com.standouter.standouternew.R.layout;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.media.ThumbnailUtils;
import android.provider.MediaStore.Images.Thumbnails;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

public class upload2View extends RelativeLayout{
	private Standouter qapp;
	public EditText titletext;
	private TextView youwilluploadtext;
	private ImageView imgvideo;
	public TextView uploadbtn;
	public TextView cancelbtn;
	private Bitmap bitmap;
	private  Bitmap bitmap1;
	public Spinner spinner; 
    private static final String[] m={"#SHARETUBORG - amazing skills","#PLAYTUBORG - urban art","#LOVETUBORG - beatbox","#LIVETUBORG - parkour","#ENJOYTUBORG - hiphop crew"}; 
    private static final String[] m2={"SHARETUBORG","PLAYTUBORG","LOVETUBORG","LIVETUBORG","ENJOYTUBORG"};

    private ArrayAdapter<String> adapter;  

	View view;
	public upload2View(Context context,Standouter qapp,String vidpath) {
		super(context);
		// TODO Auto-generated constructor stub
       this.qapp=qapp;
       
		LayoutInflater mInflater = (LayoutInflater)
				 context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		view=mInflater.inflate(R.layout.upload2view, null);
		Typeface type= Typeface.createFromAsset(context.getAssets(), "fonts/Helvetica Neue Condensed Bold.ttf"); 
		titletext=(EditText)view.findViewById(R.id.TITLETEXT);
		titletext.setTypeface(type);
		titletext.setSingleLine(true);
		youwilluploadtext=(TextView)view.findViewById(R.id.textView1);
		youwilluploadtext.setTypeface(type);
		RelativeLayout.LayoutParams flp=new RelativeLayout.LayoutParams( qapp.width-80,(qapp.width-80)/16*9);
		imgvideo=(ImageView)view.findViewById(R.id.imguploadvid);
		flp.addRule(RelativeLayout.BELOW, R.id.textView1);
		flp.addRule(RelativeLayout.ALIGN_LEFT,R.id.TITLETEXT);
		
		imgvideo.setLayoutParams(flp);
	
		this.uploadbtn=(TextView)view.findViewById(R.id.Uploadbtn);
		uploadbtn.setTypeface(type);
		this.cancelbtn=(TextView)view.findViewById(R.id.Canceluploadbtn);
		cancelbtn.setTypeface(type);
		
		
		
		  bitmap=ThumbnailUtils.createVideoThumbnail(vidpath,Thumbnails.MINI_KIND);  

		 

         bitmap1=ThumbnailUtils.extractThumbnail(bitmap,qapp.width-80,(qapp.width-80)/16*9);  
         BitmapDrawable   d=new BitmapDrawable (bitmap1);  
         imgvideo.setBackgroundDrawable(d);
         
         spinner = (Spinner) view.findViewById(R.id.spinner1);  

         spinner.setBackgroundColor(Color.WHITE);
         adapter = new ArrayAdapter<String>(context,android.R.layout.simple_spinner_item,m);  
         
         //设置下拉列表的风格  
         adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);  
           
         //将adapter 添加到spinner中  
         spinner.setAdapter(adapter);  
           
         //添加事件Spinner事件监听    
         spinner.setOnItemSelectedListener(new SpinnerSelectedListener());  
           
         //设置默认值  
         qapp.setcategoria(m2[0]);
         if(qapp.getcontestname().equals("tuborg")){
             spinner.setVisibility(View.VISIBLE); 

         }
         else{
             spinner.setVisibility(View.GONE); 

         }
		
		this.addView(view);
	}
	class SpinnerSelectedListener implements OnItemSelectedListener{  
		  
        public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,  
                long arg3) {  
           // view.setText("你的血型是："+m[arg2]);  
        	qapp.setcategoria(m2[arg2]);
        }  
  
        public void onNothingSelected(AdapterView<?> arg0) {  
        }  
    }  
	public void destroytheview(){
		this.removeView(view);
		imgvideo.destroyDrawingCache();
		bitmap1.recycle();
		bitmap1=null;
		bitmap.recycle();
		bitmap=null;
		cancelbtn=null;
		uploadbtn=null;
		youwilluploadtext=null;
		titletext=null;
		qapp=null;
	}

}
