package adapter;

import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.standouter.Json;
import com.example.standouter.Login;
import com.example.standouter.MainActivity;
import com.example.standouter.R;

import com.krislq.sliding.beans.RowItem;
import com.squareup.picasso.Picasso;

public class profilovideodaapter extends BaseAdapter {
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
   
   
 
    public profilovideodaapter(Context context, List<RowItem> items,  Activity activity) {
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
        
        TextView votetext;
        public JSONObject sharejson;
        LinearLayout buttonslv;
        
    }
    
    public  ViewHolder holder;
	public   boolean isup;
	
	
 
    
	public View getView( final int position, View convertView, ViewGroup parent) {
		//vm=MainActivity.vm;
		final int positionTemp = position;  
       holder = null;
       convertView2=convertView;
        mInflater = (LayoutInflater)
            context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.protfileadapter, null);
            
            holder = new ViewHolder();
            
            
            //holder.txtDesc = (TextView) convertView.findViewById(R.id.votes);
            
            //holder.txtTitle = (TextView) convertView.findViewById(R.id.title);
           holder.shadowvd=(ImageView)convertView.findViewById(R.id.shadowvd2);
           holder.imageView = (ImageView) convertView.findViewById(R.id.vdimage2);
           holder.zhre=(RelativeLayout)convertView.findViewById(R.id.listviewcell2);
          
          holder.votetext=(TextView)convertView.findViewById(R.id.votetext2);
          
            convertView.setTag(holder);

        }
        else {
            holder = (ViewHolder) convertView.getTag();
        }
 
        rowItem = (RowItem) getItem(positionTemp );
      
 
        //holder.txtDesc.setText(rowItem.getDesc());
        //holder.txtTitle.setText(rowItem.getTitle());
        
        if(rowItem.getImageId().equals("novideo")){
       	 Picasso.with(MainActivity.nowcontext).load(R.drawable.novideo). resize(MainActivity.widthz, MainActivity.widthz*224/400). into(holder.imageView);
         Picasso.with(convertView.getContext()).load(R.drawable.shadowvd). resize(320, 51). into(holder.shadowvd);

        }
       else
    	   
    	 
    	   
           Picasso.with(MainActivity.nowcontext).load(rowItem.getImageId()). resize(MainActivity.widthz, MainActivity.widthz*224/400). into(holder.imageView);
         Picasso.with(convertView.getContext()).load(R.drawable.shadowvd). resize(320, 51). into(holder.shadowvd);
         //Picasso.with(convertView.getContext()).load(R.drawable.sharea). resize(320, 180). into(holder.sharebtn);
        // //Picasso.with(convertView.getContext()).load(R.drawable.facebook). resize(320, 180). into(holder.sharefb);
         //Picasso.with(convertView.getContext()).load(R.drawable.whatapp). resize(320, 180). into(holder.sharewp);
         //Picasso.with(convertView.getContext()).load(R.drawable.plusfan). resize(320, 180). into(holder.votebtn);
        
         holder.votetext.setText("fans number: "+rowItem.getvotecont());
         
         
       
        //bmap=BitmapFactory.decodeFile (rowItem.getImageId());   
        //holder.imageView.setImageBitmap(bmap);
        
        
        holder.shadowvd.setLayoutParams(new RelativeLayout.LayoutParams(MainActivity.widthz, MainActivity.widthz*115/720));
        holder.shadowvd.setY((MainActivity.widthz*224/400-MainActivity.widthz*115/720));
        
       
        holder.zhre.setMinimumHeight(MainActivity.widthz*224/400);
        holder.zhre.setMinimumWidth(MainActivity.widthz);
        holder.imageView.setLayoutParams(new RelativeLayout.LayoutParams(MainActivity.widthz, MainActivity.widthz*224/400));
        
        
 
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
