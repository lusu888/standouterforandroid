package com.standouter.standouternew;

import java.lang.Exception;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.ImageFormat;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.hardware.Camera;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.nio.Buffer;
import java.nio.ShortBuffer;
import java.text.DecimalFormat;
import java.text.NumberFormat;

import com.standouter.standouternew.R;
import com.standouter.standouternew.R.drawable;
import com.standouter.standouternew.R.id;
import com.standouter.standouternew.R.layout;
import com.googlecode.javacv.FFmpegFrameRecorder;
import com.googlecode.javacv.cpp.opencv_core.IplImage;
import com.squareup.picasso.Picasso;

import static com.googlecode.javacv.cpp.opencv_core.*;

public class Cameraactivity extends Activity {
	public static final String LOGTAG = "MJPEG_FFMPEG";
	private ProgressBar pb;
	private final static String LOG_TAG = "MainActivity";
	private Camera camera;
	private PowerManager.WakeLock mWakeLock;

	// private String ffmpeg_link =
	// "rtmp://username:password@xxx.xxx.xxx.xxx:1935/live/test.flv";
	private String ffmpeg_link = "/mnt/sdcard/Standouter/new_stream.mp4";

	long startTime = 0;

	private int sampleAudioRateInHz = 44100;
	private int imageWidth = 640;
	private int imageHeight = 480;
	private int frameRate = 30;

	private Thread audioThread;
	private AudioRecord audioRecord;
	private AudioRecordRunnable audioRecordRunnable;

	private CameraView cameraView;
	private IplImage yuvIplimage = null;

	private NumberFormat fileCountFormatter = new DecimalFormat("00000");

	private FrameLayout mainLayout;

	private CamcorderProfile camcorderProfile;
	int fileCount = 0;
	private String formattedFileCount;
	long[] timearray = new long[3000];

	private ProcessVideo processVideo;

	private ImageView pausebtn;

	private long pausetime = 0;

	private long pausetimestart = 0;

	private RelativeLayout controllayout;

	private int widthz;

	private int heiz;

	private ImageView btn15s;
	private ImageView btn6s;

	protected int maxtime;
	private Standouter qapp;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		qapp = (Standouter) getApplication();

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		camera = null;
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		setContentView(R.layout.activity_cameraactivity);
		widthz = this.getWindowManager().getDefaultDisplay().getWidth();
		heiz = this.getWindowManager().getDefaultDisplay().getHeight();
		camcorderProfile = CamcorderProfile.get(1);
		imageWidth = camcorderProfile.videoFrameWidth;
		imageHeight = camcorderProfile.videoFrameHeight;
		frameRate = camcorderProfile.videoFrameRate;
		initLayout();
		initRecorder();
		startRecording();

	}

	@Override
	protected void onResume() {
		super.onResume();

		if (mWakeLock == null) {
			PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
			mWakeLock = pm.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK,
					LOG_TAG);
			mWakeLock.acquire();
		}
	}

	@Override
	protected void onPause() {
		super.onPause();

		if (mWakeLock != null) {
			mWakeLock.release();
			mWakeLock = null;
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();

		qapp.setrecording(false);
	}

	private void initLayout() {

		mainLayout = (FrameLayout) this.findViewById(R.id.record_layout);
		controllayout = (RelativeLayout) this.findViewById(R.id.controllayout);
		// recordButton.setOnClickListener(this);
		pb = (ProgressBar) this.findViewById(R.id.progressBar);
		pb.setMax(600);

		RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
				heiz / 4, heiz / 4);
		lp.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
		pausebtn = (ImageView) this.findViewById(R.id.pausebtn);

		pausebtn.setLayoutParams(lp);

		// pausebtn.setVisibility(View.GONE);
		pausebtn.setOnTouchListener(new View.OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub

				if (event.getAction() == MotionEvent.ACTION_DOWN) {

					try {

						Cameraactivity.this.resumeRecording();

					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();

					}
					return true;

				}
				if (event.getAction() == MotionEvent.ACTION_UP) {
					try {

						Cameraactivity.this.pauseRecording();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				return false;
			}
		});
		/*
		 * pausebtn.setOnClickListener(new View.OnClickListener() {
		 * 
		 * @Override public void onClick(View v) { // TODO Auto-generated method
		 * stub
		 * 
		 * if(recording){ try { MainActivity.this.pauseRecording(); } catch
		 * (Exception e) { // TODO Auto-generated catch block
		 * e.printStackTrace(); } } else{ try {
		 * MainActivity.this.resumeRecording(); } catch (Exception e) { // TODO
		 * Auto-generated catch block e.printStackTrace(); } }
		 * 
		 * } });
		 */
		FrameLayout.LayoutParams layoutParam;
		if (cameraView == null) {
			cameraView = new CameraView(this);

			layoutParam = new FrameLayout.LayoutParams(widthz, heiz);
			layoutParam.gravity = Gravity.CENTER;
			mainLayout.addView(cameraView, layoutParam);
			Log.v(LOG_TAG, "added cameraView to mainLayout");
		}
		layoutParam = null;
		btn6s = (ImageView) this.findViewById(R.id.btn6s);
		layoutParam = new FrameLayout.LayoutParams(heiz / 4, heiz / 4);
		layoutParam.gravity = Gravity.CENTER_VERTICAL;
		btn6s.setLayoutParams(layoutParam);
		Picasso.with(this).load(R.drawable.z6s).resize(heiz / 4, heiz / 4)
				.into(btn6s);
		mainLayout.bringChildToFront(btn6s);
		btn6s.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				btn6s.setVisibility(View.GONE);
				btn15s.setVisibility(View.GONE);
				pausebtn.setVisibility(View.VISIBLE);
				maxtime = 600;
				pb.setMax(maxtime);

			}
		});
		layoutParam = null;

		btn15s = (ImageView) this.findViewById(R.id.btn15s);
		layoutParam = new FrameLayout.LayoutParams(heiz / 4, heiz / 4);
		layoutParam.gravity = Gravity.CENTER;
		btn15s.setLayoutParams(layoutParam);
		Picasso.with(this).load(R.drawable.z15s).resize(heiz / 4, heiz / 4)
				.into(btn15s);
		mainLayout.bringChildToFront(btn15s);
		btn15s.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				btn6s.setVisibility(View.GONE);
				btn15s.setVisibility(View.GONE);
				pausebtn.setVisibility(View.VISIBLE);
				maxtime = 1500;
				pb.setMax(maxtime);

			}
		});
		btn6s.setVisibility(View.VISIBLE);
		btn15s.setVisibility(View.VISIBLE);
		layoutParam = null;

		pausebtn.setVisibility(View.GONE);
		mainLayout.bringChildToFront(pb);
		mainLayout.bringChildToFront(controllayout);

		lp = null;
		layoutParam = null;
	}

	private void initRecorder() {
		// pb.setMax(1500);
		File sdFile = android.os.Environment.getExternalStorageDirectory();
		String path = sdFile.getPath() + File.separator + "Standouter";

		File dirFile = new File(path);

		if (!dirFile.exists()) {// 如果資料夾不存在

			dirFile.mkdir();// 建立資料夾
		}

		Log.w(LOG_TAG, "initRecorder");

		if (yuvIplimage == null) {
			// Recreated after frame size is set in surface change method
			imageWidth = 640;
			imageHeight = 480;
			yuvIplimage = IplImage.create(imageWidth, imageHeight,
					IPL_DEPTH_8U, 2);
			// yuvIplimage = IplImage.create(imageWidth, imageHeight,
			// IPL_DEPTH_32S, 2);

			Log.v(LOG_TAG, "IplImage.create");
		}
		imageWidth = 640;
		imageHeight = 480;
		qapp.setrecoder(new FFmpegFrameRecorder(ffmpeg_link, imageWidth,
				imageHeight, 1), sampleAudioRateInHz, frameRate);
		Log.v(LOG_TAG, "FFmpegFrameRecorder: " + ffmpeg_link + " imageWidth: "
				+ imageWidth + " imageHeight " + imageHeight);

		// Create audio recording thread
		audioRecordRunnable = new AudioRecordRunnable();
		audioThread = new Thread(audioRecordRunnable);
		// this.sampleAudioRateInHz=initRecorderParameters(mSampleRates );
	}

	// Start the capture
	public void startRecording() {
		try {

			qapp.setrecordingbegin(true);
			qapp.getrecorder().start();
			startTime = System.currentTimeMillis();
			qapp.setrecording(true);
			audioThread.start();
			// pausebtn.setVisibility(View.VISIBLE);
			pauseRecording();
		} catch (FFmpegFrameRecorder.Exception e) {
			e.printStackTrace();
		}
	}

	public void pauseRecording() throws FFmpegFrameRecorder.Exception {
		// recorder.start();
		// runAudioThread = false;

		// runAudioThread = true;
		pausetimestart = System.currentTimeMillis();
		qapp.setrecording(false);

	}

	public void resumeRecording() throws FFmpegFrameRecorder.Exception {
		// recorder.start();

		// audioThread = new Thread(audioRecordRunnable);
		// audioThread.start();
		qapp.setrecording(true);
		pausetime = System.currentTimeMillis() - pausetimestart + pausetime;

	}

	public void stopRecording() {
		// This should stop the audio thread from running
		qapp.setrecordingbegin(false);
		pausebtn.setEnabled(false);
		if (qapp.getrecorder() != null) {
			qapp.setrecording(false);
			// audioThread=null;
			processVideo = new ProcessVideo();
			processVideo.execute();
			// recorder = null;
		}
	}

	// ---------------------------------------------
	// audio thread, gets and encodes audio data
	// ---------------------------------------------
	class AudioRecordRunnable implements Runnable {

		private static final int TIMER_INTERVAL = 120;
		private int framePeriod;
		private short bSamples = 16;

		@Override
		public void run() {
			// Set the thread priority

			android.os.Process
					.setThreadPriority(android.os.Process.THREAD_PRIORITY_URGENT_AUDIO);

			// Audio
			int bufferSize;
			short[] audioData;
			int bufferReadResult;
			bufferSize = AudioRecord
					.getMinBufferSize(sampleAudioRateInHz,
							AudioFormat.CHANNEL_IN_MONO,
							AudioFormat.ENCODING_PCM_16BIT);
			audioRecord = new AudioRecord(MediaRecorder.AudioSource.MIC,
					sampleAudioRateInHz, AudioFormat.CHANNEL_IN_MONO,
					AudioFormat.ENCODING_PCM_16BIT, bufferSize);

			audioData = new short[bufferSize];

			Log.d(LOG_TAG, "audioRecord.startRecording()");
			audioRecord.startRecording();

			// Audio Capture/Encoding Loop
			while (qapp.getrecordingbegin()) {
				// Read from audioRecord
				bufferReadResult = audioRecord.read(audioData, 0,
						audioData.length);
				if (bufferReadResult > 0) {

					// Changes in this variable may not be picked up despite it
					// being "volatile"
					Boolean s = qapp.getrecording();
					// Log.v(LOG_TAG,"audioRecord bufferReadResult: " +
					// bufferReadResult+s);

					if (s) {

						try {
							// Write to FFmpegFrameRecorder
							Buffer[] buffer = { ShortBuffer.wrap(audioData, 0,
									bufferReadResult) };
							qapp.getrecorder().record(buffer);
						} catch (FFmpegFrameRecorder.Exception e) {
							// Log.v(LOG_TAG,e.getMessage());
							e.printStackTrace();
							System.exit(0);
						}
					}
				}
			}
			Log.v(LOG_TAG, "AudioThread Finished");
			// runAudioThread=false;
			Log.v(LOG_TAG,
					"audioRecord bufferReadResult: " + qapp.getrecordingbegin());

			//

			Message msg = new Message();
			closeaudiohandler.sendMessage(msg);

		}
	}

	Handler closeaudiohandler = new Handler() {

		public void handleMessage(Message msg) {
			super.handleMessage(msg);

			if (audioRecord != null) {
				audioRecord.stop();
				audioRecord.release();
				audioRecord = null;
				// audioData=null;
				Log.v(LOG_TAG, "audioRecord released");
			}

		}

	};

	class CameraView extends SurfaceView implements SurfaceHolder.Callback,
			Camera.PreviewCallback { // ???
		long videoTimestamp = 0;

		Method mAcb; // method for adding a pre-allocated buffer
		Object[] mArglist; // list of arguments

		int framecounter;
		float fps;
		SurfaceHolder mHolder;
		Camera mCamera;
		long startTime;

		CameraView(Context context) {
			super(context);

			mHolder = getHolder();
			mHolder.addCallback(this);
			mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

			// initialize the timer
			startTime = System.currentTimeMillis();
			framecounter = 0;
			fps = 0.0f;

		}

		// Preview is started or resumed
		public void surfaceCreated(SurfaceHolder holder) {
			// The Surface has been created, acquire the camera and tell it
			// where
			// to draw.

			mCamera = Camera.open();
			camera = mCamera;
			Camera.Parameters currentParams = mCamera.getParameters();
			// Log.v(LOG_TAG,"Preview Framerate: " +
			// currentParams.getPreviewFrameRate());
			// Log.v(LOG_TAG,"Preview imageWidth: " +
			// currentParams.getPreviewSize().width + " imageHeight: " +
			// currentParams.getPreviewSize().height);

			// Use these values
			Log.i("zhangggggggggggggggg",
					"" + currentParams.getSupportedPreviewFrameRates());
			Log.v(LOG_TAG,
					"Preview imageWidth: "
							+ currentParams.getPreviewSize().width
							+ " imageHeight: "
							+ currentParams.getPreviewSize().height);

			currentParams.setPreviewFpsRange(15000, 25000);
			imageWidth = 640;
			imageHeight = 480;
			currentParams.setPreviewSize(imageWidth, imageHeight);
			// currentParams.setPreviewFrameRate(20);
			// currentParams.setWhiteBalance(value)
			// currentParams.setSceneMode(Camera.Parameters.SCENE_MODE_SPORTS);
			/*
			 * if ( currentParams.isAutoExposureLockSupported() )
			 * currentParams.setAutoExposureLock( true );
			 */
			currentParams
					.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO);

			// Camera.Size sizea=getBestPreviewSize(currentParams);
			imageWidth = 640;
			imageHeight = 480;
			frameRate = currentParams.getPreviewFrameRate();
			// int
			// widthz=this.getWindowManager().getDefaultDisplay().getWidth();
			int heiz = Cameraactivity.this.getWindowManager()
					.getDefaultDisplay().getHeight();
			FrameLayout.LayoutParams layoutParam = new FrameLayout.LayoutParams(
					heiz * imageWidth / imageHeight, heiz);
			cameraView.setLayoutParams(layoutParam);
			camera.setParameters(currentParams);

			try {
				mCamera.setPreviewDisplay(mHolder);
				// mCamera.setPreviewDisplay(null);

			} catch (IOException exception) {
				mCamera.release();
				mCamera = null;
			}

		}

		public void surfaceDestroyed(SurfaceHolder holder) {
			mCamera.stopPreview();
			mCamera.release();
			mCamera = null;

		}

		public void surfaceChanged(SurfaceHolder holder, int format, int w,
				int h) {

			Camera.Parameters parameters = mCamera.getParameters();
			parameters.setPreviewSize(w, h); // 800, 442
			mCamera.setParameters(parameters);
			Camera.Parameters currentParams = mCamera.getParameters();
			// Log.v(LOG_TAG,"Preview Framerate: " +
			// currentParams.getPreviewFrameRate());
			Log.v(LOG_TAG,
					"Preview imageWidth: "
							+ currentParams.getPreviewSize().width
							+ " imageHeight: "
							+ currentParams.getPreviewSize().height);

			// Use these values

			currentParams.setPreviewFpsRange(15000, 25000);
			currentParams.setPreviewSize(imageWidth, imageHeight);
			// currentParams.setPreviewFrameRate(20);
			// currentParams.setWhiteBalance(value)
			currentParams.setSceneMode(Camera.Parameters.SCENE_MODE_SPORTS);
			/*
			 * if ( currentParams.isAutoExposureLockSupported() )
			 * currentParams.setAutoExposureLock( true );
			 */
			currentParams
					.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO);

			camera.setParameters(currentParams);
			// Camera.Size sizea=getBestPreviewSize(currentParams);
			imageWidth = 640;
			imageHeight = 480;
			// int
			// widthz=this.getWindowManager().getDefaultDisplay().getWidth();
			int heiz = Cameraactivity.this.getWindowManager()
					.getDefaultDisplay().getHeight();
			FrameLayout.LayoutParams layoutParam = new FrameLayout.LayoutParams(
					heiz * imageWidth / imageHeight, heiz);
			cameraView.setLayoutParams(layoutParam);
			// newstuff
			PixelFormat p = new PixelFormat();
			PixelFormat.getPixelFormatInfo(parameters.getPreviewFormat(), p);
			int bufSize = (w * h * p.bitsPerPixel) / 8;
			// Must call this before calling addCallbackBuffer to get all the
			// reflection variables setup
			initForACB();

			// Add three buffers to the buffer queue. I re-queue them once they
			// are used in
			// onPreviewFrame, so we should not need many of them.
			byte[] buffer = new byte[bufSize];
			addCallbackBuffer_Android2p2(buffer);

			setPreviewCallbackWithBuffer();

			// mCamera.setPreviewCallback(previewCallback);
			mCamera.startPreview();

		}

		/**
		 * This method will list all methods of the android.hardware.Camera
		 * class, even the hidden ones. With the information it provides, you
		 * can use the same approach I took below to expose methods that were
		 * written but hidden in eclair
		 */
		private void listAllCameraMethods() {
			try {
				Class c = Class.forName("android.hardware.Camera");
				Method[] m = c.getMethods();
				for (int i = 0; i < m.length; i++) {
					Log.i("AR", "  method:" + m[i].toString());
				}
			} catch (Exception e) {
				Log.i("AR", e.toString());
			}
		}

		/*
		 * newstuff: addCallbackBuffer();
		 */
		private void initForACB() {
			try {
				Class mC = Class.forName("android.hardware.Camera");

				Class[] mPartypes = new Class[1];
				// variable that will hold parameters for a function call
				mPartypes[0] = (new byte[1]).getClass(); // There is probably a
															// better way to do
															// this.
				mAcb = mC.getMethod("addCallbackBuffer", mPartypes);

				mArglist = new Object[1];
			} catch (Exception e) {
				Log.e("AR",
						"Problem setting up for addCallbackBuffer: "
								+ e.toString());
			}
		}

		/**
		 * This method allows you to add a byte buffer to the queue of buffers
		 * to be used by preview. See:
		 * http://android.git.kernel.org/?p=platform/
		 * frameworks/base.git;a=blob;f
		 * =core/java/android/hardware/Camera.java;hb
		 * =9db3d07b9620b4269ab33f78604a36327e536ce1
		 * 
		 * @param b
		 *            The buffer to register. Size should be width * height *
		 *            bitsPerPixel / 8.
		 */
		private void addCallbackBuffer_Android2p2(byte[] b) { // this function
																// is native in
																// Android 2.2
																// ?!
			// Check to be sure initForACB has been called to setup
			// mAcb and mArglist
			if (mArglist == null) {
				initForACB();
			}

			mArglist[0] = b;
			try {
				mAcb.invoke(mCamera, mArglist);
			} catch (Exception e) {
				Log.e("AR",
						"invoking addCallbackBuffer failed: " + e.toString());
			}
		}

		/**
		 * Use this method instead of setPreviewCallback if you want to use
		 * manually allocated buffers. Assumes that "this" implements
		 * Camera.PreviewCallback
		 */
		private void setPreviewCallbackWithBuffer() {
			try {
				Class c = Class.forName("android.hardware.Camera");
				Method spcwb = null; // sets a preview with buffers
				// This way of finding our method is a bit inefficient, but I am
				// a reflection novice,
				// and didn't want to waste the time figuring out the right way
				// to do it.
				// since this method is only called once, this should not cause
				// performance issues

				Method[] m = c.getMethods(); // get all methods of camera
				for (int i = 0; i < m.length; i++) {
					if (m[i].getName()
							.compareTo("setPreviewCallbackWithBuffer") == 0) {
						spcwb = m[i];
						break;
					}
				}

				/*
				 * Class[] mPartypes = new Class[1]; mPartypes[0] = (new
				 * byte[1]).getClass(); //There is probably a better way to do
				 * this. spcwb = c.getMethod("setPreviewCallbackWithBuffer",
				 * mPartypes);
				 */

				// If we were able to find the setPreviewCallbackWithBuffer
				// method of Camera,
				// we can now invoke it on our Camera instance, setting 'this'
				// to be the
				// callback handler
				if (spcwb != null) {
					Object[] arglist = new Object[1];
					arglist[0] = this; // receives a copy of a preview frame
					spcwb.invoke(mCamera, this);
					// Log.i("AR","setPreviewCallbackWithBuffer: Called method");
				} else {
					Log.i("AR",
							"setPreviewCallbackWithBuffer: Did not find method");
				}

			} catch (Exception e) {
				Log.i("AR", e.toString());
			}
		}

		@Override
		public void onPreviewFrame(byte[] _data, Camera camera) {
			FileOutputStream outStream = null;

			if (qapp.getrecording()) {
				videoTimestamp = 1000 * (System.currentTimeMillis() - startTime - pausetime);

				pb.setProgress((int) (videoTimestamp / 10000));
				if (videoTimestamp / 10000 >= maxtime) {
					stopRecording();
					return;
				}
				formattedFileCount = fileCountFormatter.format(fileCount);
				// jpegFile = new
				// File(Environment.getExternalStorageDirectory().getPath() +
				// "/Standouter/frame_" + formattedFileCount + ".jpg");
				timearray[fileCount] = videoTimestamp;
				fileCount++;
				try {
					YuvImage yuvimage = new YuvImage(_data, ImageFormat.NV21,
							imageWidth, imageHeight, null);
					ByteArrayOutputStream baos = new ByteArrayOutputStream();
					yuvimage.compressToJpeg(new Rect(0, 0, imageWidth,
							imageHeight), 80, baos);

					outStream = new FileOutputStream(Environment
							.getExternalStorageDirectory().getPath()
							+ "/Standouter/frame_"
							+ formattedFileCount
							+ ".jpg");
					outStream.write(baos.toByteArray());
					outStream.close();

					// Log.d(TAG, "onPreviewFrame - wrote bytes: " +
					// data.length);
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				} finally {
				}

			}

			addCallbackBuffer_Android2p2(_data);

			// did camera finished initialization?

		}

	} // surface view

	private class ProcessVideo extends AsyncTask<Void, Integer, Boolean> {

		private ProgressDialog progressDialog;

		@Override
		protected void onPreExecute() {
			progressDialog = ProgressDialog.show(Cameraactivity.this,
					"MAKE VIDEO", "processing", true);
			progressDialog.show();
		}

		@Override
		protected Boolean doInBackground(Void... params) {

			for (int i = 0; i < fileCount; i++) {

				int downloadPercent = i * 100 / fileCount;
				publishProgress(downloadPercent);

				try {

					formattedFileCount = fileCountFormatter.format(i);
					imageWidth = 640;
					imageHeight = 480;
					yuvIplimage = IplImage.create(imageWidth, imageHeight,
							IPL_DEPTH_8U, 2);
					yuvIplimage = com.googlecode.javacv.cpp.opencv_highgui
							.cvLoadImage(Environment
									.getExternalStorageDirectory().getPath()
									+ "/Standouter/frame_"
									+ formattedFileCount
									+ ".jpg");
					qapp.getrecorder().setTimestamp(timearray[i]);
					qapp.getrecorder().record(yuvIplimage);
					// cvReleaseImage(yuvIplimage);
					yuvIplimage = null;

				} catch (FFmpegFrameRecorder.Exception e) {
					Log.i(LOG_TAG, e.getMessage());
					e.printStackTrace();
					// finish();
					// return false;
				}

			}
			Log.v(LOG_TAG,
					"Finishing recording, calling stop and release on recorder");
			try {
				qapp.getrecorder().stop();
				qapp.getrecorder().release();
				qapp.setrecordnull();

			} catch (FFmpegFrameRecorder.Exception e) {
				return false;
			}
			fileCount = 0;
			pausetime = 0;
			startTime = 0;
			// recorder = null;
			/*
			 * try {
			 * 
			 * //ffmpeg -r 10 -b 1800 -i %03d.jpg test1800.mp4 // 00000 //
			 * /data/data/com.mobvcasting.ffmpegcommandlinetest/ffmpeg -r
			 * p.getPreviewFrameRate() -b 1000 -i frame_%05d.jpg video.mov
			 * 
			 * //String[] args2 =
			 * {"/data/data/com.mobvcasting.ffmpegcommandlinetest/ffmpeg", "-y",
			 * "-i", "/data/data/com.mobvcasting.ffmpegcommandlinetest/",
			 * "-vcodec", "copy", "-acodec", "copy", "-f", "flv",
			 * "rtmp://192.168.43.176/live/thestream"};
			 * 
			 * String[] ffmpegCommand = {"/data/data/Standouter/ffmpeg", "-r",
			 * ""+9, "-b", "1000000", "-vcodec", "mjpeg", "-i",
			 * Environment.getExternalStorageDirectory().getPath() +
			 * "/Standouter/frame_%05d.jpg",
			 * Environment.getExternalStorageDirectory().getPath() +
			 * "/Standouter/video.mov"};
			 * 
			 * ffmpegProcess = new
			 * ProcessBuilder(ffmpegCommand).redirectErrorStream(true).start();
			 * 
			 * OutputStream ffmpegOutStream = ffmpegProcess.getOutputStream();
			 * BufferedReader reader = new BufferedReader(new
			 * InputStreamReader(ffmpegProcess.getInputStream()));
			 * 
			 * String line;
			 * 
			 * Log.v(LOGTAG,"***Starting FFMPEG***"); while ((line =
			 * reader.readLine()) != null) { Log.v(LOGTAG,"***"+line+"***"); }
			 * 
			 * System.exit(1);
			 * 
			 * 
			 * } catch (IOException e) { e.printStackTrace(); }
			 * 
			 * if (ffmpegProcess != null) { ffmpegProcess.destroy(); }
			 */
			return true;
		}

		@Override
		protected void onProgressUpdate(Integer... values) {
			progressDialog.setMessage("Processing：" + values[0] + "%");
		}

		protected void onPostExecute(Boolean result) {

			// audioThread=null;
			// audioRecordRunnable=null;
			pb.setProgress(0);
			progressDialog.dismiss();
			if (result) {
				// camera.stopPreview();

				Toast.makeText(Cameraactivity.this, "成功", Toast.LENGTH_SHORT)
						.show();

				Intent y = new Intent();
				y.setClass(Cameraactivity.this, Playrecord.class);

				Cameraactivity.this.startActivityForResult(y, 1);

				mainLayout.removeView(cameraView);

				cameraView = null;

				// MainActivity.this.finish();
				// MainActivity.this.recreate();

				// processVideo=null;
				// MainActivity.this.finish();
				// MainActivity.this.onRestart();
			} else {
				Toast.makeText(Cameraactivity.this, "失败", Toast.LENGTH_SHORT)
						.show();
				camera.stopPreview();
				mainLayout.removeView(cameraView);
				Cameraactivity.this.onDestroy();
				Cameraactivity.this.recreate();
				processVideo = null;
			}

		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		super.onActivityResult(requestCode, resultCode, data);
		Log.i("ssssssss", "ssssssssss");
		switch (requestCode) {
		case 1:
			if (resultCode == RESULT_OK) {
				Bundle bundleResult = data.getExtras();
				if (bundleResult.getInt("state") == 0) {
					Cameraactivity.this.onDestroy();
					Cameraactivity.this.recreate();
					processVideo = null;
				}
				if (bundleResult.getInt("state") == 1) {
					Intent intent = new Intent(); // 創建一個Intent，聯繫Activity之用
					Bundle bundleBack = new Bundle(); // 創建一個Bundle，傳值之用
					bundleBack.putInt("state", 1);
					intent.putExtras(bundleBack);
					setResult(RESULT_OK, intent);
					Cameraactivity.this.finish();
				}
				if (bundleResult.getInt("state") == 2) {
					Intent intent = new Intent(); // 創建一個Intent，聯繫Activity之用
					Bundle bundleBack = new Bundle(); // 創建一個Bundle，傳值之用
					bundleBack.putInt("state", 1);
					intent.putExtras(bundleBack);
					setResult(0, intent);
					Cameraactivity.this.finish();
				}
			}

			break;

		default:
			Cameraactivity.this.finish();
			break;

		}

	}

	public void finish() {
		// TODO Auto-generated method stub
		qapp.setrecordingbegin(false);
		if (audioRecord != null) {
			// audioRecord.stop();
			audioRecord.release();
			audioRecord = null;
			// audioData=null;
			Log.v(LOG_TAG, "audioRecord released");
		}
		if (qapp.getrecorder() != null) {
			// stopRecording();
			try {
				qapp.getrecorder().stop();
				qapp.getrecorder().release();
			} catch (com.googlecode.javacv.FrameRecorder.Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			qapp.setrecordnull();
		}

		super.onDestroy();

		super.finish();

		// 关闭窗体动画显示
		// this.overridePendingTransition(R.anim.activity_open_right,R.anim.activity_close_right);
	}
}
