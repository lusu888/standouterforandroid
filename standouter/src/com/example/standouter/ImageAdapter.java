package com.example.standouter;

import com.squareup.picasso.Picasso;

import android.content.Context;
import android.database.DataSetObserver;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.SpinnerAdapter;

public class ImageAdapter implements SpinnerAdapter {
	private Context mContext; 
	//定义Context 
	public  String[] mImageIds = new String[5]; ;
	public ImageAdapter(Context c) { 
	//声明 ImageAdapter  
	mContext = c;  
	}  
	public int getCount() { //获取图片的个数  
	return mImageIds.length;  
	}  
	public Object getItem(int position) {
	//获取图片在库中的位置  
	return position;  
	}  
	public long getItemId(int position) {
	//获取图片在库中的位置  
	return position;  
	}  
	public View getView(int position, View convertView,
	 ViewGroup parent) {  
	ImageView i = new ImageView(mContext); 
	Picasso.with(mContext).load(mImageIds[position]).into(i);
	
	//给ImageView设置资源  
	
	i.setLayoutParams(new Gallery.LayoutParams((MainActivity.widthz/4-10)/9*16, MainActivity.widthz/4-10));
	//设置布局 图片200×200显示  
	
	i.setScaleType(ImageView.ScaleType.FIT_XY);
	//设置比例类型  
	return i;  
	}
	@Override
	public int getItemViewType(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}
	public void setItem(String[] pic) {
		// TODO Auto-generated method stub
		mImageIds=pic;
		
	}
	@Override
	public int getViewTypeCount() {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public boolean hasStableIds() {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public boolean isEmpty() {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public void registerDataSetObserver(DataSetObserver arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void unregisterDataSetObserver(DataSetObserver observer) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public View getDropDownView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		return null;
	} 

}
