package com.standouter.standouternew;

import com.standouter.standouternew.R;
import com.standouter.standouternew.R.drawable;
import com.standouter.standouternew.R.id;
import com.standouter.standouternew.R.layout;
import com.squareup.picasso.Picasso;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class upload1view extends RelativeLayout {
	public ImageView gallerychoosebtn;
	public ImageView camerachoosebtn;
	private Standouter qapp;


	public upload1view(Context context,Standouter qapp) {
		super(context);
		// TODO Auto-generated constructor stub
		this.qapp=qapp;
		
		LayoutInflater mInflater = (LayoutInflater)
				 context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view=mInflater.inflate(R.layout.upload1view, null);
		Typeface type= Typeface.createFromAsset(context.getAssets(), "fonts/oswald.bold.ttf"); 
		

		
		RelativeLayout.LayoutParams flp=new RelativeLayout.LayoutParams( qapp.width/3,qapp.width/3);
		
		TextView gallerytext=(TextView)view.findViewById(R.id.textGALLERY);
		gallerytext.setTypeface(type);
		TextView cameratext=(TextView)view.findViewById(R.id.textCAMERA);
		cameratext.setTypeface(type);
		gallerychoosebtn=(ImageView)view.findViewById(R.id.gallerychoose);
		gallerychoosebtn.setClickable(true);
        Picasso.with(context).load(R.drawable.iphone).resize(qapp.width/3,qapp.width/3).into(gallerychoosebtn);
        
        camerachoosebtn=(ImageView)view.findViewById(R.id.camerachoose);
        camerachoosebtn.setClickable(true);
        Picasso.with(context).load(R.drawable.camera2).resize(qapp.width/3,qapp.width/3).into(camerachoosebtn);
 
        this.addView(view);

	}
	

}
