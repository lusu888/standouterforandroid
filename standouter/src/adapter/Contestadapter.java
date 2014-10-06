package adapter;

import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.androidquery.AQuery;
import com.example.standouter.Json;
import com.example.standouter.MainActivity;
import com.example.standouter.Profile;
import com.example.standouter.R;

import com.krislq.sliding.beans.RowItem;
//import com.squareup.picasso.Picasso;
import com.squareup.picasso.Picasso;

public class Contestadapter extends BaseAdapter {
    public  Context context;
    RowItem rowItem;
    List<RowItem> rowItems;
    Bitmap bmap;
    int withz;
    String b;
    private String uid;
    boolean memCache = false;
    boolean fileCache = false;

    String name;
    LayoutInflater mInflater;
     View convertView2;
    Activity actvity;
	protected boolean ispup;
	protected ProgressDialog pd;
   
   
 
    public Contestadapter(Context context, List<RowItem> items,  Activity activity) {
        this.context = MainActivity.nowcontext;
        this.rowItems = items;
        this.actvity=activity;
    }
 
    /*private view holder class*/
    public class ViewHolder {
        ImageView imageView;
        ImageView shadowvd;
        ImageView shadowvdup;
        //TextView txtTitle;
        //TextView txtDesc;
        RelativeLayout  zhre;
        Button profilebtn;
        
        ImageView loading;
        TextView votetext;
        public JSONObject sharejson;
        LinearLayout buttonslv;
		public TextView titlenametext;
        
    }
    
    public  ViewHolder holder;
	public   boolean isup;
	
	
	private Handler handlerfrobtn = new Handler(MainActivity.nowcontext.getMainLooper()){         
        public void handleMessage(Message msg) {     
             switch (msg.what) {                 
                case 1:                  
                	
                	MainActivity.x=0;
					Intent y = new Intent();
    				Bundle bundle = new Bundle();
                    bundle.putString("uid",uid);
                    y.putExtras(bundle);
    	            y.setClass(MainActivity.nowcontext, Profile.class);
    	            
    	          
    	            MainActivity.nowcontext.startActivity(y);
    	           // ((Activity) context).finish();
    	            ((Activity) MainActivity.nowcontext).overridePendingTransition(R.anim.activity_open,R.anim.activity_close); 
    	            pd.dismiss();
                	 //Contestadapter. mPopupWindow.dismiss();             
                    break; 
               
                                }            
                    
               }         
   };
	private AQuery aq;
    
	public View getView( final int position, View convertView, ViewGroup parent) {
		
		//vm=MainActivity.vm;
		aq = new AQuery(context);
		final int positionTemp = position;  
       holder = null;
       convertView2=convertView;
        mInflater = (LayoutInflater)
            context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.listviewcontest, null);
            
            holder = new ViewHolder();
            
            
            //holder.txtDesc = (TextView) convertView.findViewById(R.id.votes);
            
            //holder.txtTitle = (TextView) convertView.findViewById(R.id.title);
           holder.profilebtn=(Button)convertView.findViewById(R.id.vdwriter);
           holder.shadowvd=(ImageView)convertView.findViewById(R.id.shadowvd);
           holder.shadowvdup=(ImageView)convertView.findViewById(R.id.shadowvd2);

           holder.imageView = (ImageView) convertView.findViewById(R.id.vdimage);
           holder.zhre=(RelativeLayout)convertView.findViewById(R.id.listviewcell);
         // holder.votebtn=(ImageView)convertView.findViewById(R.id.votebtn);
         // holder.sharebtn= (ImageView)convertView.findViewById(R.id.sharebtn);
          //holder.sharefb= (ImageView)convertView.findViewById(R.id.sharefb);
          //holder.sharewp= (ImageView)convertView.findViewById(R.id.sharewp);
          holder.votetext=(TextView)convertView.findViewById(R.id.votetext);
          holder.titlenametext=(TextView)convertView.findViewById(R.id.titlename);
          holder.loading=(ImageView)convertView.findViewById(R.id.loading);
         /* holder.votebtn.setLayoutParams ( new LinearLayout.LayoutParams( MainActivity.widthz/5,MainActivity.widthz/5
                  ));
          holder.sharebtn.setLayoutParams ( new LinearLayout.LayoutParams( MainActivity.widthz/5,MainActivity.widthz/5));
          holder.sharefb.setLayoutParams ( new LinearLayout.LayoutParams( MainActivity.widthz/5,MainActivity.widthz/5));
          holder.sharewp.setLayoutParams ( new LinearLayout.LayoutParams( MainActivity.widthz*2/5,MainActivity.widthz/5));
          */
            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder) convertView.getTag();
        }
        
 
        rowItem = (RowItem) getItem(positionTemp );
        
 
        //holder.txtDesc.setText(rowItem.getDesc());
        //holder.txtTitle.setText(rowItem.getTitle());
       // Picasso.with(MainActivity.nowcontext).load(R.drawable.loading). resize(320, 180). into(holder.loading);
        if(rowItem.getImageId().equals("novideo")){
        	 
        	 Picasso.with(MainActivity.nowcontext).load(R.drawable.novideo). resize(MainActivity.widthz, MainActivity.widthz*224/400). into(holder.imageView);
        	holder.imageView.setLayoutParams(new RelativeLayout.LayoutParams(MainActivity.widthz, MainActivity.widthz*224/400));
        	holder.imageView.setScaleType(ImageView.ScaleType.FIT_XY); 
        	holder.profilebtn.setVisibility(View.GONE);
        	 //holder.votebtn.setVisibility(View.GONE);
        	 //holder.sharebtn.setVisibility(View.GONE);
        	
        	 return convertView;
        }
        
        else
        	aq.id(holder.imageView).image(rowItem.getImageId(), memCache, fileCache,320,0);
           holder.imageView.setScaleType(ImageView.ScaleType.FIT_XY); 
   	       aq.id(holder.shadowvd).image(R.drawable.shadowvd);

            //Picasso.with(MainActivity.nowcontext).load(rowItem.getImageId()). resize(320, 180). into(holder.imageView);
        // Picasso.with(MainActivity.nowcontext).load(R.drawable.shadowvd). resize(320, 51). into(holder.shadowvd);
        // Picasso.with(MainActivity.nowcontext).load(R.drawable.shadowvd2). resize(320, 51). into(holder.shadowvdup);
         //Picasso.with(MainActivity.nowcontext).load(R.drawable.sharea). into(holder.sharebtn);
         //Picasso.with(convertView.getContext()).load(R.drawable.facebook). resize(320, 180). into(holder.sharefb);
         //Picasso.with(convertView.getContext()).load(R.drawable.whatapp). resize(320, 180). into(holder.sharewp);
         //Picasso.with(MainActivity.nowcontext).load(R.drawable.sa). into(holder.votebtn);
        // holder.votebtn.setScaleType(ImageView.ScaleType.FIT_XY);
         holder.votetext.setText(rowItem.getvotecont()+" ");
         holder.titlenametext.setText( rowItem.getTitlename());
        
        // holder.sharebtn.setTag(position);
       /*
        *   holder.sharebtn.setOnClickListener(new View.OnClickListener() {
			
			private boolean isup;

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				

				//Login.publishStory((Activity) convertView2.getContext(), "https://www.google.com.tw", "https://www.google.com.tw", "https://www.google.com.tw");
				if(Login.session!=null){
					
				int[] xy = new int[2];
				v.getLocationInWindow(xy);
				Log.i("shapo",""+xy[1] );
				
				
				if(isup==true){mPopupWindow.dismiss();isup=false;}
				else{
					
					 getPopupWindowInstance();
					mPopupWindow.showAtLocation(v, Gravity.CENTER, -MainActivity.widthz/10, xy[1]-MainActivity.heiz/2+MainActivity.widthz/10);
					 //mPopupWindow.showAsDropDown(v, MainActivity.widthz/8, xy[1]-MainActivity.heiz/2+MainActivity.widthz/8);
					isup=true;
				}
				holder.sharejson = Json.getJson(MainActivity.website+"/video/videoinfo?ri="+rowItems.get(position).getVID(),MainActivity.httpclient);
				
				
				
				
				
				
				}
				else{
					Intent intent = new Intent(MainActivity.nowcontext, Login.class);
					MainActivity.nowcontext.startActivity(intent);
					//MainActivity.this.finish();
					//((Activity) convertView2.getContext()).overridePendingTransition(R.anim.activity_open,R.anim.activity_close);
					
				}
				
			}
		});
         
         */
         /*
         holder.votebtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub				

				JSONObject votejson = Json.getJson(MainActivity.website+"/video/vote?ri="+rowItems.get(position).getVID()+"&vt=STANDOUT",MainActivity.httpclient);
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
							votejson.getString("message"),
					        Toast.LENGTH_SHORT);
					toast.setGravity(Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 0);
			        toast.show();
			        MainActivity.votejsonstring= MainActivity.votejsonstring+"{"
			                                           
			        		                                                +"\"imageUrl320\":\""+rowItems.get(position).getImageId()+"\""      
			        		                                                +",\"ownerNickname\":\""+rowItems.get(position).getTitle()+"\""
			        		                                                +",\"videoUrl480\":\""+rowItems.get(position).getDesc()+"\""
			        		                                                +",\"id\":\""+rowItems.get(position).getVID()+"\""
			        		                                                +",\"votesCount\":\""+rowItems.get(position).getvotecont()+"\""
			        		                                                +"},";
			        MainActivity.votecontno++;
			       
			        Log.i("playlist",MainActivity.votejsonstring);
			       //Login.writeFileSdcardFile("/sdcard/as.txt", rowItems.get(position).getVID());
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				}
		        
		        Log.i("vote",votejson.toString());
				
			}
		});  */
               
  
       
        //bmap=BitmapFactory.decodeFile (rowItem.getImageId());   
        //holder.imageView.setImageBitmap(bmap);
        
        
        holder.profilebtn.setText(rowItems.get(position).getTitle());
        Typeface type= Typeface.createFromAsset(context.getAssets(), "fonts/helvetica-neue-bold.ttf");  

    	holder.profilebtn.setTypeface(type);
        holder.shadowvd.setLayoutParams(new RelativeLayout.LayoutParams(MainActivity.widthz, MainActivity.widthz*115/720));
        holder.shadowvd.setY((MainActivity.widthz*224/400-MainActivity.widthz*115/720));
        holder.shadowvdup.setLayoutParams(new RelativeLayout.LayoutParams(MainActivity.widthz, MainActivity.widthz*115/720));
        holder.shadowvdup.setY(0);
        
       
        holder.zhre.setMinimumHeight(MainActivity.widthz*224/400);
        holder.zhre.setMinimumWidth(MainActivity.widthz);
        holder.imageView.setLayoutParams(new RelativeLayout.LayoutParams(MainActivity.widthz, MainActivity.widthz*224/400));
        holder.profilebtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				pd = ProgressDialog.show(MainActivity.nowcontext, "Processing", "Processing，please wait……");  
				// TODO Auto-generated method stub
				
				 new Thread(new Runnable(){

					

					

					@Override
					public void run() {
						// TODO Auto-generated method stub
						String fqlid=rowItems.get(position).getVID();
						
				          uid = null;
						try {
							uid = Json.getJson(MainActivity.website+"/video/videoinfo?ri="+fqlid,MainActivity.httpclient).getString("ownerId");
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						MainActivity.x=0;
						 Message msg= new Message();
			      	        msg.what=1;
			      	        handlerfrobtn.sendMessage(msg);
					}
					 
				 }).start();

				
          
				
				
			}
		});
        if(rowItems.get(position).getflag()){
        	holder.profilebtn.setVisibility(View.VISIBLE);
        }else{
        	holder.profilebtn.setVisibility(View.GONE);
        }
        
 
        return convertView;
    }
	
    
	
   

 @SuppressWarnings("deprecation")

    @Override
    public int getCount() {
        return rowItems.size();
    }
    public void addItem(RowItem item) {  
    	rowItems.add(item) ;
    }  
 
    @Override
    public Object getItem(int position) {
        return rowItems.get(position);
    }
    
    public String[] getnearpic(int position){
    	int a = 0;
		int b=1;
		int c=2;
		int d=3;
		int e=4;
		
		
    	if (position<6){
    		 a = 0;
    		 b=1;
    		c=2;
    		d=3;
    		 e=4;
      
    		 
    	}else {if (position>rowItems.size()-6){
    		 a = 0;
    		 b=1;
    		c=2;
    		d=3;
    		 e=4;
    		  
    		 
    	}else{
    		a =position-2;
   		 b=position-1;
   		c=position;
   		d=position+1;
   		 e=position+2;
   	  
   		 
    	}
    	}
		return new String[]{rowItems.get(a).getImageId(),
				            rowItems.get(b).getImageId(),
				            rowItems.get(c).getImageId()
				            ,rowItems.get(d).getImageId()
				            ,rowItems.get(e).getImageId()
				            } ;
    	
    }

    public String[] getneardec(int position){
    	int a = 0;
		int b=1;
		int c=2;
		int d=3;
		int e=4;
		
		
    	if (position<6){
    		 a = 0;
    		 b=1;
    		c=2;
    		d=3;
    		 e=4;
      
    		 
    	}else {if (position>rowItems.size()-6){
    		 a = 0;
    		 b=1;
    		c=2;
    		d=3;
    		 e=4;
    		 
    		 
    	}else{
    		a =position-2;
   		 b=position-1;
   		c=position;
   		d=position+1;
   		 e=position+2;
   	   
   		 
    	}
    	}
		return new String[]{rowItems.get(a).getDesc(),
				            rowItems.get(b).getDesc(),
				            rowItems.get(c).getDesc()
				            ,rowItems.get(d).getDesc()
				            ,rowItems.get(e).getDesc()
				            } ;
    	
    }
    
    public String getItemdesc(int position) {
        return rowItems.get(position).getDesc();
    }
 
    @Override
    public long getItemId(int position) {
        return rowItems.indexOf(getItem(position));
    }
}
