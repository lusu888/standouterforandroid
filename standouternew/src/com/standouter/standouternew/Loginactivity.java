package com.standouter.standouternew;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import cn.jpush.android.api.JPushInterface;

import com.standouter.standouternew.R;
import com.standouter.standouternew.R.anim;
import com.standouter.standouternew.R.drawable;
import com.standouter.standouternew.R.id;
import com.standouter.standouternew.R.layout;
import com.standouter.standouternew.R.menu;
import com.facebook.LoginActivity;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.model.GraphUser;
import com.facebook.widget.FacebookDialog;
import com.facebook.widget.LoginButton;
import com.squareup.picasso.Picasso;

import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.PixelFormat;
import android.graphics.Typeface;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class Loginactivity extends Activity {

	private Standouter qapp;
	private FrameLayout headerlayout;
	private RelativeLayout contentlayout;
	private ImageView headerimg;
	private TextView accedibtn;
	private Thread loginthread;
	private ProgressDialog pd;
	private EditText usernametext;
	private EditText passwordtext;
	private JSONObject Loginjson;
	private UiLifecycleHelper uiHelper;
	private LoginButton loginButton;
	protected GraphUser user;
	protected Thread fbloginthread;
	protected Thread regthread;
	private String emailtemp;
	private JSONObject regjson;

	private TextView regbtn;
	private TextView recuperabtn;
	private Thread recuperathread;
	private JSONObject recuperajson;

	/**
	 * Logout From Facebook
	 */

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		/********************************************************************/
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		// setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);//
		// getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
		// WindowManager.LayoutParams.FLAG_FULLSCREEN);//for full screnn
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//
		getWindow().setFormat(PixelFormat.TRANSLUCENT);

		/********************************************************************/

		uiHelper = new UiLifecycleHelper(this, callback);
		uiHelper.onCreate(savedInstanceState);

		setContentView(R.layout.activity_loginactivity);
		qapp = (Standouter) getApplication();

		loginButton = (LoginButton) findViewById(R.id.fbloginButton);

		loginButton
				.setUserInfoChangedCallback(new LoginButton.UserInfoChangedCallback() {
					@Override
					public void onUserInfoFetched(GraphUser user) {
						Loginactivity.this.user = user;

					}
				});

		headerlayout = (FrameLayout) this.findViewById(R.id.headerlayoutlogin);
		LinearLayout.LayoutParams flp = new LinearLayout.LayoutParams(
				qapp.width, qapp.width / 4);
		flp.gravity = Gravity.CENTER_HORIZONTAL;
		headerlayout.setLayoutParams(flp);

		contentlayout = (RelativeLayout) this
				.findViewById(R.id.contentlayoutlogin);

		headerimg = (ImageView) this.findViewById(R.id.headerimglogin);
		FrameLayout.LayoutParams flpimg = new FrameLayout.LayoutParams(
				qapp.width, qapp.width / 4);
		flpimg.gravity = Gravity.CENTER_HORIZONTAL;
		headerimg.setLayoutParams(flpimg);
		Picasso.with(this).load(R.drawable.freestyleheader)
				.resize(qapp.width, qapp.width / 4).into(headerimg);

		ImageView menuleft = (ImageView) this
				.findViewById(R.id.imgmenuleftlogin);
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

		accedibtn = (TextView) this.findViewById(R.id.accedibtn);
		Typeface type = Typeface.createFromAsset(this.getAssets(),
				"fonts/oswald.bold.ttf");

		accedibtn.setTypeface(type);
		accedibtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				pd = ProgressDialog.show(Loginactivity.this, "dowloading",
						"dowloading，please wait……");

				loginthread = new Thread(Loginrunable);
				loginthread.start();
			}

		});

		regbtn = (TextView) this.findViewById(R.id.regbtn);
		regbtn.setTypeface(type);
		regbtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				final AlertDialog.Builder alert = new AlertDialog.Builder(
						Loginactivity.this);
				final EditText input = new EditText(Loginactivity.this);
				input.setHint("Inseriscia la tua Email");
				alert.setTitle("Registrati");
				alert.setView(input);
				alert.setPositiveButton("Registrati",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {
								emailtemp = input.getText().toString().trim();
								// Toast.makeText(getApplicationContext(),
								// value, Toast.LENGTH_SHORT).show();
								pd = ProgressDialog.show(Loginactivity.this,
										"dowloading",
										"dowloading，please wait……");
								regthread = new Thread(regrunnable);
								regthread.start();
							}
						});

				alert.setNegativeButton("Cancel",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {
								dialog.cancel();
							}
						});
				alert.show();

			}
		});

		recuperabtn = (TextView) this.findViewById(R.id.recuperabtn);
		recuperabtn.setTypeface(type);
		recuperabtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				final AlertDialog.Builder alert = new AlertDialog.Builder(
						Loginactivity.this);
				final EditText input = new EditText(Loginactivity.this);
				input.setHint("Inseriscia la tua Email");
				alert.setTitle("Recupera Password");
				alert.setView(input);
				alert.setPositiveButton("Recupera",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {
								emailtemp = input.getText().toString().trim();
								// Toast.makeText(getApplicationContext(),
								// value, Toast.LENGTH_SHORT).show();
								pd = ProgressDialog.show(Loginactivity.this,
										"dowloading",
										"dowloading，please wait……");
								recuperathread = new Thread(recuperarunnable);
								recuperathread.start();
							}
						});

				alert.setNegativeButton("Cancel",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {
								dialog.cancel();
							}
						});
				alert.show();
			}
		});

		usernametext = (EditText) this.findViewById(R.id.editTextusername);
		passwordtext = (EditText) this.findViewById(R.id.editTextpassword);
		type = Typeface.createFromAsset(this.getAssets(),
				"fonts/oswald.regular.ttf");
		usernametext.setTypeface(type);
		passwordtext.setTypeface(type);

		if (qapp.getselfid() != null) {
			usernametext.setVisibility(View.GONE);
			passwordtext.setVisibility(View.GONE);
			accedibtn.setVisibility(View.GONE);
		}

	}

	private Runnable recuperarunnable = new Runnable() {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			String url = qapp.pcweb + "/pwd?t=" + emailtemp; // web
			try {
				Log.i("", url); // address

				recuperajson = qapp.getJson(url, qapp.httpclient);

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			pd.dismiss();
			Message msg = new Message();
			recuperahandle.sendMessage(msg);

		}

	};
	private Handler recuperahandle = new Handler() {
		public void handleMessage(final Message msg) {
			String message;
			try {
				message = recuperajson.getString("message");
				TextView myTextView = new TextView(Loginactivity.this);

				myTextView.setText(message);
				myTextView.setTextSize(17);

				new AlertDialog.Builder(Loginactivity.this)
						.setTitle("Recupera Password")
						.setView(myTextView)
						.setPositiveButton("OK",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int which) {
										// continue with delete
										dialog.dismiss();
									}
								})

						.show();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			pd.dismiss();
		}
	};

	private Runnable regrunnable = new Runnable() {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			String url = qapp.pcweb + "/regreq?email=" + emailtemp; // web
			try {
				Log.i("", url); // address

				regjson = qapp.getJson(url, qapp.httpclient);

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			pd.dismiss();
			Message msg = new Message();
			reghandle.sendMessage(msg);

		}

	};

	private Handler reghandle = new Handler() {
		public void handleMessage(final Message msg) {
			String message;
			try {
				message = regjson.getString("message");
				TextView myTextView = new TextView(Loginactivity.this);

				myTextView.setText(message);
				myTextView.setTextSize(17);

				new AlertDialog.Builder(Loginactivity.this)
						.setTitle("Registrati")
						.setView(myTextView)
						.setPositiveButton("OK",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int which) {
										// continue with delete
										dialog.dismiss();
									}
								})

						.show();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			pd.dismiss();
		}
	};

	private Session.StatusCallback callback = new Session.StatusCallback() {
		@Override
		public void call(final Session session, final SessionState state,
				final Exception exception) {
			onSessionStateChange(session, state, exception);
			if (state.isOpened()) {
				Log.i("", "Logged in...");
				AT = session.getAccessToken();
				Log.i("zzzssszzzzzzzz", AT);
				qapp.setfacebooktoken(AT);
				pd = ProgressDialog.show(Loginactivity.this, "dowloading",
						"dowloading，please wait……");

				if (qapp.getselfid() == null) {
					fbloginthread = new Thread(fbloginrunable);
					fbloginthread.start();
				} else {
					pd.dismiss();

					Loginactivity.this.finish();
				}

			} else if (state.isClosed()) {
				Log.i("", "Logged out...");
			}
		}
	};
	private Session session;
	private String AT;

	@Override
	public void onResume() {
		super.onResume();

		// For scenarios where the main activity is launched and user
		// session is not null, the session state change notification
		// may not be triggered. Trigger it if it's open/closed.
		session = Session.getActiveSession();

		if (session != null && (session.isOpened() || session.isClosed())) {
			onSessionStateChange(session, session.getState(), null);
		}
		if (session.isOpened()) {
			Log.i("", "hi.isOpened");

		}
		if (session.isClosed()) {
			Log.i("", "hi.");
		}

		uiHelper.onResume();
	}

	Runnable fbloginrunable = new Runnable() {

		@Override
		public void run() {
			// TODO Auto-generated method stub

			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(3);
			nameValuePairs.add(new BasicNameValuePair("json_response", "true"));

			try {

				Loginjson = qapp.getJsonpost(qapp.webaddress
						+ "/fbaccess?accessToken=" + qapp.getfacebooktoken(),
						qapp.httpclient, nameValuePairs);
				Log.i("", Loginjson.toString());

				if (!Loginjson.getBoolean("logged")) {
					pd.dismiss();
					Intent ie2 = new Intent();

					String url = qapp.pcweb + "/fbregistration?accessToken="
							+ qapp.getfacebooktoken(); // web address
					ComponentName comp = new ComponentName(
							"com.android.browser",
							"com.android.browser.BrowserActivity");
					ie2.setComponent(comp);
					ie2.setAction(Intent.ACTION_VIEW);
					ie2.setData(Uri.parse(url));
					startActivity(ie2);
				} else {
					String stringtemp = Loginjson.getJSONObject("loginStatus")
							.getString("authenticated");
					if (stringtemp.equals("true")) {
						qapp.setselfid(Loginjson.getJSONObject("loginStatus")
								.getString("registrationId"));
						Log.i("", qapp.getselfid());
						Message msg = new Message();
						loginsuccess.sendMessage(msg);
					} else {

						Message msg = new Message();
						loginfaild.sendMessage(msg);
					}
				}

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	};

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		uiHelper.onActivityResult(requestCode, resultCode, data,
				new FacebookDialog.Callback() {
					@Override
					public void onError(FacebookDialog.PendingCall pendingCall,
							Exception error, Bundle data) {
						Log.e("Activity",
								String.format("Error: %s", error.toString()));
					}

					@Override
					public void onComplete(
							FacebookDialog.PendingCall pendingCall, Bundle data) {
						Log.i("Activity", "Success!");

					}
				});
	}

	@Override
	public void onPause() {
		super.onPause();
		uiHelper.onPause();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		uiHelper.onDestroy();
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		uiHelper.onSaveInstanceState(outState);
	}

	private void onSessionStateChange(Session session, SessionState state,
			Exception exception) {
		if (state.isOpened()) {
			Log.i("", "Logged in...");

		} else if (state.isClosed()) {
			Log.i("", "Logged out...");
		}
	}

	/************************/

	private Runnable Loginrunable = new Runnable() {

		@Override
		public void run() {
			// TODO Auto-generated method stub

			String username = usernametext.getText().toString();
			String password = passwordtext.getText().toString();

			// TODO Auto-generated method stub
			login(username, password);

		}

	};

	private void login(String username, String password) {

		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(3);
		nameValuePairs.add(new BasicNameValuePair("json_response", "true"));
		nameValuePairs.add(new BasicNameValuePair("j_username", username));
		nameValuePairs.add(new BasicNameValuePair("j_password", password));
		try {
			Loginjson = qapp.getJsonpost(qapp.webaddress
					+ "/j_spring_security_check", qapp.httpclient,
					nameValuePairs);
			Log.i("", Loginjson.toString());

			String stringtemp = Loginjson.getString("authenticated");
			if (stringtemp.equals("true")) {
				qapp.setselfid(Loginjson.getString("registrationId"));
				Log.i("", qapp.getselfid());
				Message msg = new Message();
				loginsuccess.sendMessage(msg);
			} else {

				Message msg = new Message();
				loginfaild.sendMessage(msg);
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	Handler loginfaild = new Handler() {
		public void handleMessage(final Message msg) {
			// super.handleMessage(msg2);

			JPushInterface.setAlias(qapp, "", null);
			qapp.setselfid(null);
			String message = null;
			try {
				message = Loginjson.getString("message");

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			new AlertDialog.Builder(Loginactivity.this)
					.setTitle("LOGIN")
					.setMessage(message)
					.setPositiveButton("OK",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int which) {
									// continue with delete
									dialog.dismiss();
								}
							})

					.show();
			pd.dismiss();

		}
	};
	Handler loginsuccess = new Handler() {
		public void handleMessage(final Message msg) {
			// super.handleMessage(msg2);

			JPushInterface.setAlias(qapp, qapp.getselfid(), null);
			qapp.getcookiestandouter();
			pd.dismiss();

			Loginactivity.this.finish();

		}
	};

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

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.loginactivity, menu);
		return true;
	}

}
