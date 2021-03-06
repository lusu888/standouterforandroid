package com.standouter.standouternew;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import com.standouter.standouternew.R;
import com.standouter.standouternew.R.anim;
import com.standouter.standouternew.R.drawable;
import com.standouter.standouternew.R.id;
import com.standouter.standouternew.R.layout;
import com.standouter.standouternew.R.menu;
import com.squareup.picasso.Picasso;

import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.graphics.PixelFormat;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class Upload extends Activity {

	private Standouter qapp;
	private FrameLayout headerlayout;
	private RelativeLayout contentlayout;
	private ImageView headerimg;
	private upload1view viewforchoose;
	private upload2View viewforupload;
	private String videoaddress;
	private Thread gootherthread;

	private final int FILE_SELECT_CODE_camera = 2;
	protected ProgressDialog pd;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		/********************************************************************/
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		// setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);//
		// getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
		// WindowManager.LayoutParams.FLAG_FULLSCREEN);//for full screnn
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//
		getWindow().setFormat(PixelFormat.TRANSLUCENT);
		videoaddress = null;

		/********************************************************************/
		setContentView(R.layout.activity_upload);
		qapp = (Standouter) getApplication();
		headerlayout = (FrameLayout) this.findViewById(R.id.headerlayoutupload);
		LinearLayout.LayoutParams flp = new LinearLayout.LayoutParams(
				qapp.width, qapp.width / 4);
		flp.gravity = Gravity.CENTER_HORIZONTAL;
		headerlayout.setLayoutParams(flp);

		contentlayout = (RelativeLayout) this
				.findViewById(R.id.contentlayoutuplaod);

		headerimg = (ImageView) this.findViewById(R.id.headerimgupload);
		FrameLayout.LayoutParams flpimg = new FrameLayout.LayoutParams(
				qapp.width, qapp.width / 4);
		flpimg.gravity = Gravity.CENTER_HORIZONTAL;
		headerimg.setLayoutParams(flpimg);

		if (qapp.contestname.equals("freestyle")) {
			Picasso.with(this)
					.load("file:///android_asset/image/freestyle"
							+ "header.png").resize(qapp.width, qapp.width / 4)
					.into(headerimg);

		} else {
			try {
				Picasso.with(this)
						.load(qapp.webaddress
								+ qapp.getbriefjson().getString(
										"backgroundImageUrlMobile"))
						.resize(qapp.width, qapp.width / 4).into(headerimg);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		// Picasso.with(this).load("file:///android_asset/image/"+qapp.getcontestname()+"header.png").resize(qapp.width,
		// qapp.width/4).into(headerimg);

		ImageView menuleft = (ImageView) this
				.findViewById(R.id.imgmenuleftupload);
		flpimg = new FrameLayout.LayoutParams(qapp.width / 16 * 3,
				qapp.width / 4);
		flpimg.gravity = Gravity.LEFT;
		menuleft.setLayoutParams(flpimg);
		Picasso.with(this).load(R.drawable.lw)
				.resize(qapp.width / 16 * 3, qapp.width / 4).into(menuleft);
		menuleft.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		viewforchoose = new upload1view(this, qapp);
		this.contentlayout.addView(viewforchoose);

		viewforchoose.gallerychoosebtn
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						pd = ProgressDialog.show(Upload.this, "Processing",
								"Processing，please wait……");
						gootherthread = new Thread(new Runnable() {

							@Override
							public void run() {
								// TODO Auto-generated method stub
								showFileChooser();
								pd.dismiss();
							}
						});
						gootherthread.start();
					}

				});
		this.viewforchoose.camerachoosebtn
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						pd = ProgressDialog.show(Upload.this, "Processing",
								"Processing，please wait……");
						gootherthread = new Thread(gotocamerarunable);
						gootherthread.start();
						/*
						 * Intent intent = new
						 * Intent(MediaStore.ACTION_VIDEO_CAPTURE);
						 * intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
						 * File videofile = new File("/sdcard/zhang.mp4"); Uri
						 * originalUri = Uri.fromFile(videofile);//
						 * 这是个实例变量，方便下面获取视频的时候用
						 * intent.putExtra(MediaStore.EXTRA_OUTPUT,
						 * originalUri); startActivityForResult(intent,
						 * FILE_SELECT_CODE_camera);
						 */
					}
				});

	}

	Runnable gotocamerarunable = new Runnable() {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			Intent y = new Intent();
			y.setClass(Upload.this, Cameraactivity.class);
			Upload.this.startActivityForResult(y, FILE_SELECT_CODE_camera);
			pd.dismiss();
		}

	};

	/**************** choosefile **********************/
	private void showFileChooser() {
		Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
		intent.setType("video/*");
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

	private final int FILE_SELECT_CODE = 0;
	private final String TAG = "hi";

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
				// editadress.setText(path);
				Log.d(TAG, "File Path: " + path);
				videoaddress = path;
				Message msg = new Message();
				handlerupload2view.sendMessage(msg);
				// Get the file instance
				// File file = new File(path);
				// Initiate the upload
			}
			break;

		case FILE_SELECT_CODE_camera:
			// Get the Uri of the selected file
			if (resultCode == RESULT_OK) {
				Uri uri = data.getData();

				String path = "/sdcard/Standouter/new_stream.mp4";

				// editadress.setText(path);
				Log.d(TAG, "File Path: " + path);
				videoaddress = path;
				Message msg = new Message();
				handlerupload2view.sendMessage(msg);
			}
			break;

		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	public static String getPath(Context context, Uri uri)
			throws URISyntaxException {
		if ("content".equalsIgnoreCase(uri.getScheme())) {
			String[] projection = { "_data" };
			Cursor cursor = null;

			try {
				cursor = context.getContentResolver().query(uri, projection,
						null, null, null);
				int column_index = cursor.getColumnIndexOrThrow("_data");
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

	/**************** choosefile end **********************/

	private Handler handlerupload2view = new Handler() {

		@Override
		public void handleMessage(final Message msg) {
			// super.handleMessage(msg2);
			// TODO Auto-generated method stub

			Upload.this.clearallviews();
			viewforupload = new upload2View(Upload.this, qapp, videoaddress);
			Upload.this.contentlayout.addView(viewforupload);

			viewforupload.uploadbtn.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					if (viewforupload.titletext.length() == 0) {
						AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
								Upload.this);

						// set title
						alertDialogBuilder.setTitle("upload");

						// set dialog message

						alertDialogBuilder
								.setMessage("Please input the video name!")
								.setCancelable(false)
								.setPositiveButton("OK",
										new DialogInterface.OnClickListener() {
											public void onClick(
													DialogInterface dialog,
													int id) {
												// if this button is clicked,
												// close
												// current activity
												dialog.cancel();
											}
										});

						// create alert dialog
						AlertDialog alertDialog = alertDialogBuilder.create();

						// show it
						alertDialog.show();
					} else {
						new Thread(new MyThread()).start();
						pd = ProgressDialog.show(Upload.this, "Uploading",
								"Uploading，please wait……");
					}
				}

			});

			viewforupload.cancelbtn.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Upload.this.clearallviews();
					viewforchoose = new upload1view(Upload.this, qapp);
					Upload.this.contentlayout.addView(viewforchoose);
					viewforchoose.gallerychoosebtn
							.setOnClickListener(new OnClickListener() {

								@Override
								public void onClick(View v) {
									// TODO Auto-generated method stub
									pd = ProgressDialog.show(Upload.this,
											"Uploading",
											"Uploading，please wait……");
									gootherthread = new Thread(new Runnable() {

										@Override
										public void run() {
											// TODO Auto-generated method stub
											showFileChooser();
											pd.dismiss();
										}
									});
									gootherthread.start();
								}

							});
					viewforchoose.camerachoosebtn
							.setOnClickListener(new OnClickListener() {

								@Override
								public void onClick(View v) {
									// TODO Auto-generated method stub
									pd = ProgressDialog.show(Upload.this,
											"Uploading",
											"Uploading，please wait……");
									gootherthread = new Thread(new Runnable() {

										@Override
										public void run() {
											// TODO Auto-generated method stub
											Intent y = new Intent();
											y.setClass(Upload.this,
													Cameraactivity.class);
											Upload.this.startActivityForResult(
													y, FILE_SELECT_CODE_camera);
											pd.dismiss();
										}
									});
									gootherthread.start();
									/*
									 * Intent intent = new
									 * Intent(MediaStore.ACTION_VIDEO_CAPTURE);
									 * intent
									 * .putExtra(MediaStore.EXTRA_VIDEO_QUALITY,
									 * 1); File videofile = new
									 * File("/sdcard/zhang.mp4"); Uri
									 * originalUri = Uri.fromFile(videofile);//
									 * 这是个实例变量，方便下面获取视频的时候用
									 * intent.putExtra(MediaStore.EXTRA_OUTPUT,
									 * originalUri);
									 * startActivityForResult(intent,
									 * FILE_SELECT_CODE_camera);
									 */
								}
							});

				}

			});
		}

	};
	private Timer timer;
	private JSONObject linkjson;

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.upload, menu);
		return true;
	}

	public void clearallviews() {
		if (viewforchoose != null) {
			this.contentlayout.removeView(viewforchoose);
			viewforchoose.destroyDrawingCache();
			viewforchoose = null;
		}
		if (viewforupload != null) {
			this.contentlayout.removeView(viewforupload);
			viewforupload.destroytheview();
			viewforupload.destroyDrawingCache();
			viewforupload = null;
		}
	}

	public void finish() {
		// TODO Auto-generated method stub

		qapp = null;

		headerimg = null;

		super.onDestroy();

		super.finish();

		// 关闭窗体动画显示
		this.overridePendingTransition(R.anim.alphaappear,
				R.anim.alphadisappear);
	}

	public class MyThread implements Runnable {

		@Override
		public void run() {

			String title = prepareupload(viewforupload.titletext.getText()
					.toString());
			// Log.i("",path);
			if (title != null) {
				try {

					Log.i("hi2", post(videoaddress, title));

				} catch (ClientProtocolException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}

		}

	}

	/******************************************************************************************************
		 * 
		 */
	/**
	 * Bits on the Run JavaScript Uploader version 1.01 Written by Tom Boshoven
	 * 
	 * Inspired by Resumable.js and Valums AJAX Upload (version 1).
	 */

	/**
	 * Construct a BotrUpload object.
	 * 
	 * Only the link parameter is required.
	 * 
	 * link: The link object as returned by the API resumableSession: The
	 * resumable session id if the upload should be resumable. False otherwise.
	 * redirect: The object describing the redirect location. Should be of the
	 * form {url: '...', params: ['...', ...]} . The params property is
	 * optional. id: A unique identifier to use for the iframe (if used) and for
	 * the JSON-P calls (if used). If left blank, it is randomly generated.
	 */

	public String prepareupload(String title) {
		String severurl = null;
		String Link = null;
		linkjson = null;
		HttpPost httppost = new HttpPost(qapp.webaddress + "/contest/"
				+ qapp.contestname + "/prepare_upload?"
				+ System.currentTimeMillis());
		if (new File("/sdcard/header9.txt").exists()
				&& new File("/sdcard/header8.txt").exists()) {
			String xz = null;
			String yz = null;

			try {
				xz = "JSESSIONID="
						+ qapp.readFileSdcardFile("/sdcard/header9.txt");
				httppost.addHeader("Cookie", xz);
				yz = "SPRING_SECURITY_REMEMBER_ME_COOKIE"
						+ qapp.readFileSdcardFile("/sdcard/header8.txt");
				httppost.addHeader("Cookie", yz);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}// 第二次访问拿着JSESSIONID就可以了

			Log.i("JSESSIONID", xz + "::2");
			Log.i("JSESSIONID", yz + "::2");
		}
		try {
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
			nameValuePairs.add(new BasicNameValuePair("resumable", "false"));
			nameValuePairs.add(new BasicNameValuePair("title", title));
			if (qapp.getcontestname().equals("tuborg")) {
				nameValuePairs.add(new BasicNameValuePair("category", qapp
						.getcategoria()));
			}

			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

			HttpResponse response = qapp.httpclient.execute(httppost);
			Link = EntityUtils.toString(response.getEntity());
			Log.i("zz", Link);

		} catch (ClientProtocolException e) {

		} catch (IOException e) {

		}
		try {

			linkjson = new JSONObject(Link);
			if (!(linkjson.getString("status").equals("error"))) {
				severurl = linkjson.getJSONObject("link").getString("protocol")
						+ "://"
						+ linkjson.getJSONObject("link").getString("address")
						+ linkjson.getJSONObject("link").getString("path")
						+ "?api_format=json&key="
						+ linkjson.getJSONObject("link").getJSONObject("query")
								.getString("key")

				;
				if (linkjson.getString("session_id").equals("null")) {
					severurl = severurl
							+ "&token="
							+ linkjson.getJSONObject("link")
									.getJSONObject("query").getString("token");
				}
				return severurl;
			} else {
				pd.dismiss();
				Message msg = new Message();
				faildupload.sendMessage(msg);
				return null;
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;

	}

	Handler uploadokhandle = new Handler() {
		public void handleMessage(final Message msg) {
			// super.handleMessage(msg2);

			AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
					Upload.this);

			// set title
			alertDialogBuilder.setTitle("upload");

			// set dialog message

			alertDialogBuilder
					.setMessage("upload  ok")
					.setCancelable(false)
					.setPositiveButton("OK",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
									// if this button is clicked, close
									// current activity
									dialog.cancel();
									finish();
								}
							});
			// create alert dialog
			AlertDialog alertDialog = alertDialogBuilder.create();

			// show it
			alertDialog.show();
			// finish();

		}
	};
	Handler faildupload = new Handler() {
		public void handleMessage(final Message msg) {
			// super.handleMessage(msg2);
			AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
					Upload.this);

			// set title
			alertDialogBuilder.setTitle("upload");

			// set dialog message

			try {
				alertDialogBuilder
						.setMessage(linkjson.getString("message"))
						.setCancelable(false)
						.setPositiveButton("OK",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int id) {
										// if this button is clicked, close
										// current activity
										dialog.cancel();
										finish();
									}
								});
				// create alert dialog
				AlertDialog alertDialog = alertDialogBuilder.create();

				// show it
				alertDialog.show();
				// finish();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	};
	private JSONObject s;

	public String post(String pathToOurFile, String urlServer)
			throws ClientProtocolException, IOException, JSONException {
		// 设置通信协议版本
		qapp.httpclient.getParams().setParameter(
				CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);

		// File path= Environment.getExternalStorageDirectory(); //取得SD卡的路径

		// String pathToOurFile = path.getPath()+File.separator+"ak.txt";
		// //uploadfile
		// String urlServer = "http://192.168.1.88/test/upload.php";

		HttpPost httppost = new HttpPost(urlServer);
		if (new File("/sdcard/header9.txt").exists()
				&& new File("/sdcard/header8.txt").exists()) {
			String xz = null;
			String yz = null;

			try {
				xz = "JSESSIONID="
						+ qapp.readFileSdcardFile("/sdcard/header9.txt");
				httppost.addHeader("Cookie", xz);
				yz = "SPRING_SECURITY_REMEMBER_ME_COOKIE"
						+ qapp.readFileSdcardFile("/sdcard/header8.txt");
				httppost.addHeader("Cookie", yz);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}// 第二次访问拿着JSESSIONID就可以了

			Log.i("JSESSIONID", xz + "::2");
			Log.i("JSESSIONID", yz + "::2");
		}
		File file = new File(pathToOurFile);
		httppost.setHeader("enctype", "multipart/form-data");
		httppost.setHeader("path", "");

		MultipartEntity mpEntity = new MultipartEntity(); // 文件传输
		ContentBody cbFile = new FileBody(file);
		mpEntity.addPart("file", cbFile); // <input type="file" name="userfile"
											// /> 对应的

		httppost.setEntity(mpEntity);

		System.out.println("executing request " + httppost.getRequestLine());

		HttpResponse response = qapp.httpclient.execute(httppost);
		HttpEntity resEntity = response.getEntity();

		System.out.println(response.getStatusLine());// 通信Ok
		String json = "";
		String path = "";
		if (resEntity != null) {
			// System.out.println(EntityUtils.toString(resEntity,"utf-8"));
			json = EntityUtils.toString(resEntity, "utf-8");
			JSONObject p = null;
			try {
				p = new JSONObject(json);
				Log.i("p", p.toString());
				path = p.toString();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if (resEntity != null) {
			resEntity.consumeContent();

		}
		String url = qapp.webaddress
				+ "/contest/"
				+ qapp.contestname
				+ "/end_upload.json?video_key="
				+ linkjson.getJSONObject("link").getJSONObject("query")
						.getString("key");
		s = qapp.getJson(url, qapp.httpclient);
		Log.i("", s.toString());
		pd.dismiss();
		Message msg = new Message();
		uploadokhandle.sendMessage(msg);
		// httpclient.getConnectionManager().shutdown();
		return path;
	}

	/******************************************************************************************************
		 * 
		 */

}
