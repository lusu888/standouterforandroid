package adapter;

import java.io.IOException;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.standouter.Json;
import com.example.standouter.Login;
import com.example.standouter.MainActivity;
import com.example.standouter.Profile;
import com.example.standouter.R;

import com.krislq.sliding.beans.RowItem;
import com.squareup.picasso.Picasso;

public class nonameadapter extends BaseAdapter {
    public  Context context;
    RowItem rowItem;
    List<RowItem> rowItems;
    Bitmap bmap;
    int withz;
    String b;

    String name;
    LayoutInflater mInflater;
     View convertView2;
    Activity actvity;
	protected boolean ispup;
	private int mScreenWidth;  
    // 屏幕的height  
    private int mScreenHeight;  
    // PopupWindow的width  
    private int mPopupWindowWidth;  
    // PopupWindow的height  
    private int mPopupWindowHeight;
	public  PopupWindow mPopupWindow;
   
   
 
    public nonameadapter(Context context, List<RowItem> items,  Activity activity) {
        this.context = MainActivity.nowcontext;
        this.rowItems = items;
        this.actvity=activity;
    }
 
    /*private view holder class*/
    public class ViewHolder {
        ImageView imageView;
        ImageView shadowvd;
        //TextView txtTitle;
        //TextView txtDesc;
        RelativeLayout  zhre;
        ImageView votebtn;
        ImageView sharebtn;
        ImageView sharefb;
        ImageView sharewp;
        TextView votetext;
        public JSONObject sharejson;
        LinearLayout buttonslv;
        
    }
    
    public  ViewHolder holder;
	private  JSONObject fbsharestate;
	public   boolean isup;
	
	
 
    
	public View getView( final int position, View convertView, ViewGroup parent) {
		//vm=MainActivity.vm;
		getPopupWindowInstance();
		final int positionTemp = position;  
       holder = null;
       convertView2=convertView;
        mInflater = (LayoutInflater)
            context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.nonaleadapter, null);
            
            holder = new ViewHolder();
            
            
            //holder.txtDesc = (TextView) convertView.findViewById(R.id.votes);
            
            //holder.txtTitle = (TextView) convertView.findViewById(R.id.title);
           holder.shadowvd=(ImageView)convertView.findViewById(R.id.shadowvd2);
           holder.imageView = (ImageView) convertView.findViewById(R.id.vdimage2);
           holder.zhre=(RelativeLayout)convertView.findViewById(R.id.listviewcell2);
          holder.votebtn=(ImageView)convertView.findViewById(R.id.votebtn2);
          holder.sharebtn= (ImageView)convertView.findViewById(R.id.sharebtn2);
          holder.sharefb= (ImageView)convertView.findViewById(R.id.sharefb2);
          holder.sharewp= (ImageView)convertView.findViewById(R.id.sharewp2);
          holder.votetext=(TextView)convertView.findViewById(R.id.votetext2);
          holder.votebtn.setLayoutParams ( new LinearLayout.LayoutParams( MainActivity.widthz/5,MainActivity.widthz/5
                  ));
          holder.sharebtn.setLayoutParams ( new LinearLayout.LayoutParams( MainActivity.widthz/5,MainActivity.widthz/5));
          holder.sharefb.setLayoutParams ( new LinearLayout.LayoutParams( MainActivity.widthz*2/5,MainActivity.widthz/5));
          holder.sharewp.setLayoutParams ( new LinearLayout.LayoutParams( MainActivity.widthz/5,MainActivity.widthz/5));
           
            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder) convertView.getTag();
        }
 
        rowItem = (RowItem) getItem(positionTemp );
      
 
        //holder.txtDesc.setText(rowItem.getDesc());
        //holder.txtTitle.setText(rowItem.getTitle());
        
         Picasso.with(convertView.getContext()).load(rowItem.getImageId()). resize(320, 180). into(holder.imageView);
         Picasso.with(convertView.getContext()).load(R.drawable.shadowvd). resize(320, 51). into(holder.shadowvd);
         Picasso.with(convertView.getContext()).load(R.drawable.sharea). resize(320, 180). into(holder.sharebtn);
         //Picasso.with(convertView.getContext()).load(R.drawable.facebook). resize(320, 180). into(holder.sharefb);
         //Picasso.with(convertView.getContext()).load(R.drawable.whatapp). resize(320, 180). into(holder.sharewp);
         Picasso.with(convertView.getContext()).load(R.drawable.sa). resize(320, 180). into(holder.votebtn);
         holder.votebtn.setScaleType(ImageView.ScaleType.FIT_XY);
         holder.votetext.setText("vote: "+rowItem.getvotecont());
         holder.sharebtn.setTag(position);
         holder.sharebtn.setOnClickListener(new View.OnClickListener() {
			
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
					 mPopupWindow.showAtLocation(v, Gravity.CENTER, -MainActivity.widthz/10, xy[1]-MainActivity.heiz/2+MainActivity.widthz/10);					 //mPopupWindow.showAsDropDown(v, MainActivity.widthz/8, xy[1]-MainActivity.heiz/2+MainActivity.widthz/8);
					isup=true;
				}
				holder.sharejson = Json.getJson(MainActivity.website+"/video/videoinfo?ri="+rowItems.get(position).getVID(),MainActivity.httpclient);
				
				
				
				
				
				
				}
				else{
					Intent intent = new Intent(convertView2.getContext(), Login.class);
					convertView2.getContext().startActivity(intent);
					//MainActivity.this.finish();
					//((Activity) convertView2.getContext()).overridePendingTransition(R.anim.activity_open,R.anim.activity_close);
					
				}
			}
		});
         
         
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
					  toast = Toast.makeText(convertView2.getContext(),
				                "Please Login first",
				                Toast.LENGTH_SHORT);
				        toast.setGravity(Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 0);
				        toast.show();
				        Intent intent = new Intent(convertView2.getContext(), Login.class);
						convertView2.getContext().startActivity(intent);
						
				        
				}else{
				try {
					
					toast = Toast.makeText(convertView2.getContext(),
							votejson.getString("message"),
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
				/*
				 try {
					String Votestate=votejson.getString("result");
					if (Votestate.equals("unknown")){
						 Toast toast = Toast.makeText(convertView2.getContext(),
					                "Please Login first",
					                Toast.LENGTH_SHORT);
					        toast.setGravity(Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 0);
					        toast.show();
					}
					if (Votestate.equals("accepted")){
						 Toast toast = Toast.makeText(convertView2.getContext(),
					                "OK, YOU HAVE VOTED",
					                Toast.LENGTH_SHORT);
					        toast.setGravity(Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 0);
					        toast.show();
					}
					else{
						 Toast toast = Toast.makeText(convertView2.getContext(),
					                "sorry you state is"+votejson.getString("result"),
					                Toast.LENGTH_SHORT);
					        toast.setGravity(Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 0);
					        toast.show();
					}
					
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}  
				*/
			}
		});
               
  
       
        //bmap=BitmapFactory.decodeFile (rowItem.getImageId());   
        //holder.imageView.setImageBitmap(bmap);
        
        
        holder.shadowvd.setLayoutParams(new RelativeLayout.LayoutParams(MainActivity.widthz, MainActivity.widthz*115/720));
        holder.shadowvd.setY((MainActivity.widthz*224/400-MainActivity.widthz*115/720));
        
       
        holder.zhre.setMinimumHeight(MainActivity.widthz*224/400);
        holder.zhre.setMinimumWidth(MainActivity.widthz);
        holder.imageView.setLayoutParams(new RelativeLayout.LayoutParams(MainActivity.widthz, MainActivity.widthz*224/400));
        
        
 
        return convertView;
    }
    
	/* 
     * 创建PopupWindow 
     */ 
	public void getPopupWindowInstance() {  
        if (null != mPopupWindow) {  
            mPopupWindow.dismiss();  
            return;  
        } else {  
            initPopuptWindow();  
        }  
    }  
    private void initPopuptWindow() {  
        LayoutInflater layoutInflater = LayoutInflater.from(MainActivity.nowcontext);  
        View popupWindow = layoutInflater.inflate(R.layout.sharepoup, null);  
        
        ImageView sharefbp=(ImageView)popupWindow.findViewById(R.id.sharefbp);
        ImageView sharewpp=(ImageView)popupWindow.findViewById(R.id.sharewpp);
        sharefbp.setLayoutParams ( new LinearLayout.LayoutParams( MainActivity.widthz/5,MainActivity.widthz/5));
        sharewpp.setLayoutParams ( new LinearLayout.LayoutParams( MainActivity.widthz/5,MainActivity.widthz/5));
        Picasso.with(MainActivity.nowcontext).load(R.drawable.facebook). resize(MainActivity.widthz/5,MainActivity.widthz/5). into(sharefbp);
        Picasso.with(MainActivity.nowcontext).load(R.drawable.whatapp). resize(MainActivity.widthz/5,MainActivity.widthz/5). into(sharewpp);
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
				mPopupWindow.dismiss(); 
				isup=false;
		
			}
		});
        sharewpp.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				onClickWhatsApp(v);
				mPopupWindow.dismiss(); 
				isup=false;
			}
		});
        
  
        // 创建一个PopupWindow  
        // 参数1：contentView 指定PopupWindow的内容  
        // 参数2：width 指定PopupWindow的width  
        // 参数3：height 指定PopupWindow的height  
         mPopupWindow = new PopupWindow(popupWindow, MainActivity.widthz*2/5, MainActivity.widthz/5);  
  
        // 获取屏幕和PopupWindow的width和height  
        mScreenWidth = ((Activity) MainActivity.nowcontext).getWindowManager().getDefaultDisplay().getWidth();  
        mScreenWidth = ((Activity) MainActivity.nowcontext).getWindowManager().getDefaultDisplay().getHeight();  
        mPopupWindowWidth = mPopupWindow.getWidth();  
        mPopupWindowHeight = mPopupWindow.getHeight();
        mPopupWindow.setAnimationStyle(R.style.AnimationFade);
    } 
    
    public  void onClickWhatsApp(View view) {

	    Intent waIntent = new Intent(Intent.ACTION_SEND);
	    waIntent.setType("text/plain");
	            String text = null;
				try {
					text =" I like this video in STANDOUTER! \n"+MainActivity.website+"/video?ri="+holder.sharejson.getString("id");
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
    public  void fbsharecode(){
    	
		Log.i("",holder.sharejson.toString());
			String link;
		try {
			link = MainActivity.website+"/video?ri="+holder.sharejson.getString("id");
			String picture=holder.sharejson.getString("imageUrl320");
			String des="I%20like%20the%20video%20"+holder.sharejson.getString("ownerName")+"%20in%20Standouter!";
			
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
			Intent intent = new Intent(convertView2.getContext(), Login.class);
			convertView2.getContext().startActivity(intent);
			//MainActivity.this.finish();
			//convertView2.getContext().overridePendingTransition(R.anim.activity_open,R.anim.activity_close);
		}
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
