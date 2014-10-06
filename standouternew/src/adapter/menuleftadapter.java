package adapter;

import java.util.List;

import com.androidquery.AQuery;
import com.standouter.standouternew.R;
import com.standouter.standouternew.Standouter;
import com.squareup.picasso.Picasso;

import rowierm.RowItem;
import rowierm.rowitemmenu;
import adapter.page1adapter.ViewHolder;
import android.app.Activity;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.TextView;

public class menuleftadapter  extends BaseAdapter {
	 public  Context context;
	   rowitemmenu rowItem;
	    Activity activity;
	    List<rowitemmenu> rowItems;
		private Standouter qapp;
		private LayoutInflater mInflater;
		private ViewHolder holder;
		//private AQuery aq;
	    public menuleftadapter(Context context, List<rowitemmenu> items,  Activity activity) {
	        this.context = context;
	        this.rowItems = items;
	        this.activity=activity;
	    }
	
	public void addItem(rowitemmenu item) {  
    	rowItems.add(item) ;
    }  
 
	@Override
	public int getCount() {
        return rowItems.size();
    }
	@Override
	public rowitemmenu getItem(int position) {
        return rowItems.get(position);
    }
	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}
	/*private view holder class*/
    public class ViewHolder {
        ImageView contestlego;
        ImageView imgisopen;
       
        
    }

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		
		qapp=(Standouter)activity.getApplication();
		//aq = new AQuery(context);

		mInflater = (LayoutInflater)
	            context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		if (convertView == null) {
            convertView = mInflater.inflate(R.layout.menuleftadapter, null);
            holder = new ViewHolder();
            holder.contestlego=(ImageView)convertView.findViewById(R.id.contestlego);
            holder.imgisopen=(ImageView)convertView.findViewById(R.id.imgisopen);
            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder) convertView.getTag();
        }
		FrameLayout.LayoutParams flp=new FrameLayout.LayoutParams( qapp.width/4,qapp.width/4);
        flp.gravity=Gravity.CENTER;
        holder.contestlego.setLayoutParams(flp);
		//aq.id(holder.contestlego).image(getItem(position).getlegoimageurl() , qapp.getmemCache(), qapp.getfileCache(),qapp.width/4,0);
       Picasso.with(this.context).load(getItem(position).getlegoimageurl()).into(holder.contestlego);
       holder.contestlego.setScaleType(ScaleType.FIT_XY);
        
        flp=new FrameLayout.LayoutParams( qapp.width/12,qapp.width/12);
        flp.gravity=Gravity.RIGHT;
        holder.imgisopen.setLayoutParams(flp);
        if(getItem(position).getuseopen()){
        	holder.imgisopen.setVisibility(View.VISIBLE);
        	if(getItem(position).getisopen()){
        		Picasso.with(this.context).load(R.drawable.contestopen).resize(qapp.width/12,qapp.width/12).into(holder.imgisopen);
        	}else{
            	Picasso.with(this.context).load(R.drawable.contestclose).resize(qapp.width/12,qapp.width/12).centerCrop().into(holder.imgisopen);
        	}
        }else{
        	holder.imgisopen.setVisibility(View.INVISIBLE);
        }
        
		return convertView;
	}

}
