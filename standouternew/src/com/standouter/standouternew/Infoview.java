package com.standouter.standouternew;

import org.json.JSONException;
import org.json.JSONObject;

import com.androidquery.AQuery;
import com.standouter.standouternew.R;
import com.standouter.standouternew.R.id;
import com.standouter.standouternew.R.layout;
import com.squareup.picasso.Picasso;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class Infoview extends RelativeLayout  {
	
	public ImageView profilevideoimg;
	private TextView city;
	private TextView zhiye;
	private TextView biography;
	private AQuery aq;

	
	private Standouter qapp;

	public Infoview(Context context,Standouter qapp,JSONObject infojson,String profilevideocode) {
		super(context);
		// TODO Auto-generated constructor stub
		this.setDrawingCacheEnabled(true);
		this.qapp=qapp;


		aq=new AQuery(this);
		//profilevideocode="KEG7pXQz";
		LayoutInflater mInflater = (LayoutInflater)
				 context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view=mInflater.inflate(R.layout.inforviewlayout, null);
		profilevideoimg=(ImageView)view.findViewById(R.id.profilevideoimg);
		RelativeLayout.LayoutParams flp=new RelativeLayout.LayoutParams( qapp.width,qapp.width/16*9);
		
		String temp="https://content.bitsontherun.com/thumbs/"+profilevideocode+"-320.jpg";
		profilevideoimg.setLayoutParams(flp);
		
		
       // Picasso.with(context).load(temp).resize(qapp.width,qapp.width/16*9).into(profilevideoimg);
		aq.id(profilevideoimg).image(temp , qapp.getmemCache(), qapp.getfileCache(),qapp.width,0);
		profilevideoimg.setScaleType(ScaleType.FIT_XY); 
		Typeface type= Typeface.createFromAsset(context.getAssets(), "fonts/oswald.regular.ttf"); 
		city=(TextView)view.findViewById(R.id.city);
		zhiye=(TextView)view.findViewById(R.id.zhiye);
		biography=(TextView)view.findViewById(R.id.biography);
		city.setTypeface(type);
		zhiye.setTypeface(type);
		type= Typeface.createFromAsset(context.getAssets(), "fonts/oswald.light.ttf"); 
		biography.setTypeface(type);
		
		try {
			temp=infojson.getString("city");
			city.setText(temp);
			temp=infojson.getString("category");
			zhiye.setText(temp);
			temp=infojson.getString("biography");
			biography.setText(temp);

			

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		

		
		this.addView(view);
	}

	public void destroytheview(){
		aq=null;
		profilevideoimg=null;
		this.zhiye=null;
		this.city=null;
		this.biography=null;
		this.destroyDrawingCache();
		
		
	}
	

}
