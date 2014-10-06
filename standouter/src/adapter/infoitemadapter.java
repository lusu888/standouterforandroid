package adapter;

import java.io.IOException;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.standouter.Json;
import com.example.standouter.Login;
import com.example.standouter.MainActivity;
import com.example.standouter.Profile;
import com.example.standouter.R;

import com.krislq.sliding.beans.RowItem;
import com.krislq.sliding.beans.inforowitem;
import com.squareup.picasso.Picasso;

public class infoitemadapter extends BaseAdapter {
    Context context;
    inforowitem Inforowitem;
    List< inforowitem> InforItems;
    Bitmap bmap;
    int withz;
    String b;
    String name;
    LayoutInflater mInflater;
     View convertView2;
    Activity actvity;
   
   
 
    public infoitemadapter(Context context, List<inforowitem> items,  Activity actvity) {
        this.context = context;
        this.InforItems = items;
        this.actvity=actvity;
    }
 
    /*private view holder class*/
    public class ViewHolder {
        ImageView imageView;
       
        TextView txtTitle;
        TextView txtDesc;
        
       
        public JSONObject sharejson;
    }
    
    public  ViewHolder holder;
 
    @SuppressLint("NewApi")
	public View getView( final int position, View convertView, ViewGroup parent) {
       holder = null;
       convertView2=convertView;
        mInflater = (LayoutInflater)
            context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.vlist, null);
            holder = new ViewHolder();
            holder.imageView=(ImageView) convertView.findViewById(R.id.imgfan);
            holder.txtDesc = (TextView) convertView.findViewById(R.id.desfan);
            
            holder.txtTitle = (TextView) convertView.findViewById(R.id.titlefan);
          
          
            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder) convertView.getTag();
        }
 
        Inforowitem = (inforowitem) getItem(position);
        
 
        holder.txtDesc.setText(Inforowitem.getDesc());
        holder.txtTitle.setText(Inforowitem.getTitle());
        
         Picasso.with(convertView.getContext()).load(Inforowitem.getImageId()). resize(200, 200). into(holder.imageView);
         
         
        
 
        return convertView;
    }
    
    
    
    @Override
    public int getCount() {
        return InforItems.size();
    }
    public void addItem(inforowitem item) {  
    	InforItems.add(item) ;
    }  
 
    @Override
    public Object getItem(int position) {
        return InforItems.get(position);
    }
    
    

    
    public String getItemdesc(int position) {
        return InforItems.get(position).getDesc();
    }
 
    @Override
    public long getItemId(int position) {
        return InforItems.indexOf(getItem(position));
    }
}
