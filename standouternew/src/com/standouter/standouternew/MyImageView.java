package com.standouter.standouternew;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

public class MyImageView extends ImageView {

    
   public MyImageView(Context context) {
       super(context);
       // TODO Auto-generated constructor stub
   }

   public MyImageView(Context context, AttributeSet attrs) {
       super(context, attrs, 0);
       
   }

   protected void onDraw(Canvas canvas) {
       // TODO Auto-generated method stub
        
	   getParent().requestDisallowInterceptTouchEvent(true);
           Path clipPath = new Path();  
           int w = this.getWidth();  
           int h = this.getHeight();  
           clipPath.addRoundRect(new RectF(0, 0, w, h), w/2, h/2, Path.Direction.CW);  
           canvas.clipPath(clipPath);  
           super.onDraw(canvas);
   }
   public void setImageDrawable(Drawable drawable, int pixels) {
       // TODO Auto-generated method stub
        Bitmap bitmap =toRoundCorner(drawableToBitmap(drawable), pixels);  
        Drawable drawable1 = new BitmapDrawable(bitmap);   
       super.setImageDrawable(drawable1);      
   }
    

   public void setImageBitmap(Drawable drawable, int pixels) {     
       // TODO Auto-generated method stub
       super.setImageBitmap(toRoundCorner(drawableToBitmap(drawable), pixels));
   }

   public static Bitmap toRoundCorner(Bitmap bitmap, int pixels) {
       //创建一个和原始图片一样大小位图
       Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
               bitmap.getHeight(), Config.ARGB_8888);
       //创建带有位图bitmap的画布
       Canvas canvas = new Canvas(output);

       final int color = 0xff424242;
       //创建画笔
       final Paint paint = new Paint();
       //创建一个和原始图片一样大小的矩形
       final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
       final RectF rectF = new RectF(rect);
       final float roundPx = pixels;
        // 去锯齿
       paint.setAntiAlias(true);
       canvas.drawARGB(0, 0, 0, 0);
       paint.setColor(color);
        //画一个和原始图片一样大小的圆角矩形
       canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
         //设置相交模式
       paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
       //把图片画到矩形去
       canvas.drawBitmap(bitmap, rect, rect, paint);
       return output;
   }

    
   public static Bitmap drawableToBitmap(Drawable drawable){
         
       int width = drawable.getIntrinsicWidth();
        int height = drawable.getIntrinsicHeight();
        //创建一个和原始图片一样大小位图
        Bitmap bitmap = Bitmap.createBitmap(width, height,
        drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
        : Bitmap.Config.RGB_565);//创建一个指定高、宽的可变的Bitmap图像
       //创建带有位图bitmap的画布
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0,0,width,height);
        drawable.draw(canvas);
        return bitmap;
        }
         

}



//该代码片段来自于: http://www.sharejs.com/codes/java/6855