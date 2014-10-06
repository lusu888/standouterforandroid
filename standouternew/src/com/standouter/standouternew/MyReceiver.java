package com.standouter.standouternew;

import java.util.List;

import org.json.JSONObject;

import cn.jpush.android.api.JPushInterface;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.WindowManager;

public class MyReceiver extends BroadcastReceiver {
    private static final String TAG = "MyReceiver";
     private Context context;
    private NotificationManager nm;
	private Standouter qapp;
    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        this.context=context;
        
        Log.d(TAG, "onReceive - " + intent.getAction());
        Boolean isapprun=isRunningForeground();
        if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
             
        } else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {
            System.out.println("收到了自定义消息。消息内容是：" + bundle.getString(JPushInterface.EXTRA_MESSAGE));
            // 自定义消息不会展示在通知栏，完全要开发者写代码去处理
        } else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {
            System.out.println("收到了通知");
            if(isapprun){
            	System.out.println("现在做点事情");
            	Intent i = new Intent(context, com.standouter.standouternew.AlertDialogActivity.class);  //自定义打开的界面
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(i);
            	/*
            	AlertDialog alertDialog=new AlertDialog.Builder(context)
            	.setTitle("Message")
				.setMessage("You have received a new message!")
				.setPositiveButton("OK",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								// continue with delete
								dialog.dismiss();
								
							}
						})
						.create();
            	alertDialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
            	alertDialog.show();
            	
            	new AlertDialog.Builder(qapp)
				.setTitle("Message")
				.setMessage("You have received a new message!")
				.setPositiveButton("OK",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								// continue with delete
								dialog.dismiss();
							}
						})

				.show();
				*/
            	
            	
            	
              }
            // 在这里可以做些统计，或者做些其他工作
        } else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {
            System.out.println("用户点击打开了通知");
            // 在这里可以自己写代码去定义用户点击后的行为
            
            if(!isapprun){
              Intent i = new Intent(context, com.standouter.standouternew.SplashActivity.class);  //自定义打开的界面
              i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
              context.startActivity(i);
            }
   
        } else {
            Log.d(TAG, "Unhandled intent - " + intent.getAction());
        }
    }
 
   private void receivingNotification(Context context, Bundle bundle){
        String title = bundle.getString(JPushInterface.EXTRA_NOTIFICATION_TITLE);
        Log.d(TAG, " title : " + title);
        String message = bundle.getString(JPushInterface.EXTRA_ALERT);
        Log.d(TAG, "message : " + message);
        String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);
        Log.d(TAG, "extras : " + extras);
    } 
 
   private void openNotification(Context context, Bundle bundle){
        String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);
        String myValue = ""; 
        try {
            JSONObject extrasJson = new JSONObject(extras);
            myValue = extrasJson.optString("myKey");
        } catch (Exception e) {
        	Log.w(TAG, "Unexpected: extras is not a valid json", e);
            return;
        }
       
    }
   
   public boolean isRunningForeground(){
       String packageName=getPackageName(this.context);
       String topActivityClassName=getTopActivityName(this.context);
       System.out.println("packageName="+packageName+",topActivityClassName="+topActivityClassName);
       if (packageName!=null&&topActivityClassName!=null&&topActivityClassName.startsWith(packageName)) {
           System.out.println("---> isRunningForeGround");
           return true;
       } else {
           System.out.println("---> isRunningBackGround");
           return false;
       }
   }
   public  String getTopActivityName(Context context){
       String topActivityClassName=null;
        ActivityManager activityManager =
       (ActivityManager)(context.getSystemService(android.content.Context.ACTIVITY_SERVICE )) ;
        List<RunningTaskInfo> runningTaskInfos = activityManager.getRunningTasks(1) ;
        if(runningTaskInfos != null){
            ComponentName f=runningTaskInfos.get(0).topActivity;
            topActivityClassName=f.getClassName();
        }
        return topActivityClassName;
   }
    
   public String getPackageName(Context context){
        String packageName = context.getPackageName();  
        return packageName;
   }
}
