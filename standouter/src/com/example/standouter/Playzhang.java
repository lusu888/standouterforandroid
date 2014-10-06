package com.example.standouter;

import java.io.File;






import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.graphics.PixelFormat;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

public class Playzhang extends Activity {

	private VideoView videoView;
	String result2;
	int tempstr;
	
	
	MediaController mediaController;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		/********************************************************************/
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);//for full screnn
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);//
		getWindow().setFormat(PixelFormat.TRANSLUCENT);
		/********************************************************************/
		setContentView(R.layout.playzhang);
		
		

		videoView = (VideoView) this.findViewById(R.id.video);
		

		// 使用这种方式创建MediaController将不会显示“快进”和“快退”两个按钮
		// mediaController = new MediaController(this,false);

		mediaController = new MediaController(this);

		File videoFile = new File("/sdcard/files/e.mp4");

		// 先判断这个文件是否存在
		if (videoFile.exists()) {
			System.out.println("文件存在");

			videoView.setVideoPath(videoFile.getAbsolutePath());
			System.out.println(videoFile.getAbsolutePath());
			// 设置VideView与MediaController建立关联
			videoView.setMediaController(mediaController);
			// 设置MediaController与VideView建立关联
			mediaController.setMediaPlayer(videoView);
			

			// 让VideoView获取焦点
			videoView.requestFocus();
			// 开始播放
			videoView.start();
			mediaController.show(100);
			videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
				
				@Override
				public void onCompletion(MediaPlayer mp) {
					// TODO Auto-generated method stub
					
	                finish();
					
				}
			});
			
		} else {
			Toast.makeText(this, "文件不存在", Toast.LENGTH_LONG).show();
		}

	}
	
}