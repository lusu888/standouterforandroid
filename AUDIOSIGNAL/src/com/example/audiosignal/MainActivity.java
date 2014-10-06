package com.example.audiosignal;

import java.io.IOException;
import java.net.URISyntaxException;

import com.example.audiosignal.R.id;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Toast;

public class MainActivity extends Activity {
    // originally from http://marblemice.blogspot.com/2010/04/generate-and-play-tone-in-android.html
    // and modified by Steve Pomeroy <steve@staticfree.info>
    private  int duration = 20; // seconds
    private  int sampleRate = 8000;
    private int numSamples = duration * sampleRate;
    private double sample[] = new double[numSamples];
    private double freqOfTone1 ; // hz
    private double freqOfTone0; 
    private double signalbits[]=new double[20];
    private  byte generatedSnd[] = new byte[2 * numSamples];
     AudioTrack audioTrack ;
     private Boolean isplay;
     private Button choosebtn;

    Handler handler = new Handler();
	private EditText f1text;
	private EditText f2text;
	private SeekBar volumbar;
	
	

	private EditText signaltext;
	private MediaPlayer mp;
	private Button  playsongbtn;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        isplay=false;
        f1text= (EditText)this.findViewById(R.id.f1);
        f2text= (EditText)this.findViewById(R.id.f2);
        volumbar=(SeekBar)this.findViewById(R.id.seekBarvolum);
        choosebtn=(Button)this.findViewById(R.id.choosebtn);
        signaltext= (EditText)this.findViewById(R.id.editText2);
        Button playbtn=(Button)this.findViewById(R.id.playbtn);
         audioTrack = new AudioTrack(AudioManager.STREAM_SYSTEM,
                sampleRate, AudioFormat.CHANNEL_CONFIGURATION_MONO,
                AudioFormat.ENCODING_PCM_16BIT, generatedSnd.length,
                AudioTrack.MODE_STREAM);
        new MediaPlayer();
        
		mp= MediaPlayer.create(this, R.raw.zhang);
		playsongbtn=(Button)this.findViewById(R.id.playsongbtn);
        playsongbtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				if(!isplay){
				 mp.setVolume(0, 1);   
			     mp.start();
			     isplay=true;
			     playsongbtn.setText("暂停播放");
				}else{
					 mp.pause(); 
				     isplay=false;
				     playsongbtn.setText("播放歌曲");

				     
				}
			}
		});
        
        mp.setOnCompletionListener(new OnCompletionListener(){
            @Override
            public void onCompletion(MediaPlayer mp) {
            	isplay=false;
			     playsongbtn.setText("播放歌曲");
            }
         });
        
        playbtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				 // Use a new tread as this can take a while
		        final Thread thread = new Thread(new Runnable() {
		            public void run() {
		            	freqOfTone1=Integer.valueOf(MainActivity.this.f1text.getText().toString());
		            	freqOfTone0=Integer.valueOf(MainActivity.this.f2text.getText().toString());
		                int tempsignal=Integer.valueOf(MainActivity.this.signaltext.getText().toString());
		                for(int i=0;i<20;i++){
		                	signalbits[19-i]=tempsignal&0x01;
		                	tempsignal=tempsignal>>1;
		                   Log.i(""+i,"i "+signalbits[i]);
		                }

		            	
		                genTone();
		                handler.post(new Runnable() {

		                    public void run() {
		                        playSound();
		                    }
		                });
		            }
		        });
		        thread.start();
			}
		});
        choosebtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				showFileChooser();
			}
		});
        
        
        this.volumbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
			
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				// TODO Auto-generated method stub
				Float v=progress/100.f;
				mp.setVolume(0, v);
				Log.i("",""+v);
				
			}
		});
        
        
        
    }
    

    @Override
    protected void onResume() {
        super.onResume();

       
    }

    void genTone(){
        // fill out the array
    	
    	
    	int k=0;
    	int j=0;

    	
        for (int i = 0; i < numSamples; ++i) {
        	
        	//int j=i/(sampleRate/bitrate);
        	if(j<20){
	        	if (signalbits[j]==0x01){
	        		sample[i] = Math.sin(2 * Math.PI * i / (sampleRate/freqOfTone1));
	        		if (k<(3*sampleRate/freqOfTone1)){
	        			k++;
	        		}else{
	        			j++;
	        			k=0;
	        		}
	        	}
	        	else{
	        		sample[i] = Math.sin(2 * Math.PI * i / (sampleRate/freqOfTone0));
	        		if (k<(2*sampleRate/freqOfTone0)){
	        			k++;
	        		}else{
	        			j++;
	        			k=0;
	        		}
	
	        	}
	        	
        	}else{
        		sample[i]=0;
        	}
        	
        }

        // convert to 16 bit pcm sound array
        // assumes the sample buffer is normalised.
        int idx = 0;
        for (final double dVal : sample) {
            // scale to maximum amplitude
            final short val = (short) ((dVal * 32767));
            // in 16 bit wav PCM, first byte is the low order byte
            generatedSnd[idx++] = (byte) (val & 0x00ff);
            generatedSnd[idx++] = (byte) ((val & 0xff00) >>> 8);

        }
    }

    void playSound(){
    	audioTrack.stop();
    	audioTrack.release();
    	
         audioTrack = new AudioTrack(AudioManager.STREAM_SYSTEM,
                sampleRate, AudioFormat.CHANNEL_CONFIGURATION_MONO,
                AudioFormat.ENCODING_PCM_16BIT, generatedSnd.length,
                AudioTrack.MODE_STREAM);
        audioTrack.setStereoVolume(1.0f, 0.0f);
        audioTrack.write(generatedSnd, 0, generatedSnd.length);
        audioTrack.play();
        
       
    }
    
    
    /******************************************************************************************************
	 * choose the file
	 */
	private  final int FILE_SELECT_CODE = 0;
	private  final String TAG = "hi";

    private void showFileChooser() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT); 
        intent.setType("audio/mp3*"); 
        intent.addCategory(Intent.CATEGORY_OPENABLE);

        try {
            startActivityForResult(
                    Intent.createChooser(intent, "Select a File to Upload"),
                    FILE_SELECT_CODE);
        } catch (android.content.ActivityNotFoundException ex) {
            // Potentially direct the user to the Market with a Dialog
            Toast.makeText(this, "Please install a File Manager.", 
                    Toast.LENGTH_SHORT).show();
        }
    }
	
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case FILE_SELECT_CODE:      
            if (resultCode == RESULT_OK) {  
                // Get the Uri of the selected file 
                Uri uri = data.getData();
                Log.d(TAG, "File Uri: " + uri.toString());
                // Get the path
                String path = null;
				try {
					path = getPath(this, uri);
				} catch (URISyntaxException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				mp.release();
				mp=new MediaPlayer();
				
				try {
                    mp.setDataSource(path);
                    mp.prepare();
                    mp.start();
                 } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                 } catch (IllegalStateException e) {
                    e.printStackTrace();
                 } catch (IOException e) {
                    e.printStackTrace();
                 }				
				mp.setVolume(0, 1);   
			     isplay=true;
			     playsongbtn.setText("暂停播放");
				//editadress.setText(path);
                Log.d(TAG, "File Path: " + path);
                // Get the file instance
                // File file = new File(path);
                // Initiate the upload
                mp.setOnCompletionListener(new OnCompletionListener(){
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                    	isplay=false;
        			     playsongbtn.setText("播放歌曲");
                    }
                 });
            }           
            break;
        }
    super.onActivityResult(requestCode, resultCode, data);
    }
	
    public static String getPath(Context context, Uri uri) throws URISyntaxException {
        if ("content".equalsIgnoreCase(uri.getScheme())) {
            String[] projection = { "_data" };
            Cursor cursor = null;

            try {
                cursor = context.getContentResolver().query(uri, projection, null, null, null);
                int column_index = cursor
                .getColumnIndexOrThrow("_data");
                if (cursor.moveToFirst()) {
                    return cursor.getString(column_index);
                }
            } catch (Exception e) {
                // Eat it
            }
        }

        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }
    
    
	
	/******************************************************************************************************
	 * 
	 */
}
