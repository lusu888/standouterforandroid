package com.standouter.standouternew;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.NodeTraversor;
import org.jsoup.select.NodeVisitor;

import com.standouter.standouternew.R;

import rowierm.RowItem;
import rowierm.rowitemmenu;

import adapter.page1adapter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import cn.jpush.android.api.JPushInterface;

import com.androidquery.AQuery;
import com.standouter.standouternew.Standouter;//全局变量
import com.standouter.standouternew.R.anim;
import com.standouter.standouternew.R.drawable;
import com.standouter.standouternew.R.id;
import com.standouter.standouternew.R.layout;
import com.standouter.standouternew.R.menu;
import com.standouter.standouternew.R.raw;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.squareup.picasso.Picasso;
import com.standouter.standouternew.MyImageView;

public class MainActivity extends Activity implements
		com.standouter.standouternew.XListView.IXListViewListener {
	private Standouter qapp;
	private XListView listView;
	private View loadMoreView;
	private View loadMoreViewtop;
	private ProgressBar loadMorepb;
	private TextView loadmoretext;
	private ProgressBar loadMorepbtop;
	private TextView loadmoretexttop;
	private List<RowItem> rowItems;
	private List<rowitemmenu> rowmenuutems;
	private String date;
	private SlidingMenu sm;

	private JSONObject mainJson;
	private JSONObject contestJson;
	private JSONObject channelJson;

	private int showrowsno;
	private int totalResults;
	private page1adapter mainadapter;
	private adapter.menuleftadapter menuleftadapter;

	private ProgressDialog pd;

	private Thread threadbegin;
	private Thread threadgetcontest;
	private int totalcontestno;
	private int totalchannelno;

	private ListView listviewmenuleft;
	int contesttheno;

	private ImageView menuvideo;
	private ImageView menubrief;
	private ImageView menuupload;
	private LinearLayout menulayout;
	private ImageView headerimg;
	protected View view;
	private FrameLayout contentlayout;
	protected ImageView breifimage;
	protected String videocode;
	protected TextView brieftext;
	private AQuery aq;
	private Playerlayout player;
	private int posy;
	private FrameLayout headerlayout;
	protected int postion;
	protected boolean islistviewshow;
	protected JSONObject loginstatejson;
	private Animation appear;
	private Thread threadgetnews;
	private TranslateAnimation gotonewsanimation;
	private MyImageView notipic;
	private android.widget.RelativeLayout.LayoutParams flpnoti;
	protected JSONObject newsjson;
	private ImageView shadowwhate;
	private android.widget.RelativeLayout.LayoutParams flpshadow;

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
		aq = new AQuery(this);

		contesttheno = 0;
		videocode = "RAIL0cx5";
		view = null;
		islistviewshow = true;

		/********************************************************************/
		setContentView(R.layout.activity_main);

		qapp = (Standouter) getApplication();

		// httpclient=null;

		int widthz = this.getWindowManager().getDefaultDisplay().getWidth();
		int heiz = this.getWindowManager().getDefaultDisplay().getHeight() - 30;
		qapp.setvidth(widthz);
		qapp.setheight(heiz);

		qapp.setememCache(false);
		qapp.setfileCache(true);

		appear = (Animation) AnimationUtils.loadAnimation(MainActivity.this,
				R.anim.alphaappear);

		headerlayout = (FrameLayout) this.findViewById(R.id.headerlayout);
		LinearLayout.LayoutParams flp = new LinearLayout.LayoutParams(
				qapp.width, qapp.width / 4);
		flp.gravity = Gravity.CENTER_HORIZONTAL;
		headerlayout.setLayoutParams(flp);

		contentlayout = (FrameLayout) this.findViewById(R.id.contentlayout);

		headerimg = (ImageView) this.findViewById(R.id.headerimg);
		FrameLayout.LayoutParams flpimg = new FrameLayout.LayoutParams(
				qapp.width, qapp.width / 4);
		flpimg.gravity = Gravity.CENTER_HORIZONTAL;
		headerimg.setLayoutParams(flpimg);

		ImageView menuleft = (ImageView) this
				.findViewById(R.id.imgmenuleftmain);
		flpimg = new FrameLayout.LayoutParams(qapp.width / 6,
				qapp.width / 4);
		flpimg.gravity = Gravity.LEFT;
		menuleft.setLayoutParams(flpimg);
		Picasso.with(this).load(R.drawable.menu)
				.resize(qapp.width / 6, qapp.width / 4).into(menuleft);

		ImageView menuright = (ImageView) this
				.findViewById(R.id.imgmenurightmain);
		flpimg = new FrameLayout.LayoutParams(qapp.width / 6, qapp.width /4 );
		flpimg.gravity = Gravity.RIGHT;
		menuright.setLayoutParams(flpimg);
		Picasso.with(this).load(R.drawable.camera)
				.resize(qapp.width / 6, qapp.width / 4).into(menuright);

		flpshadow = new RelativeLayout.LayoutParams(qapp.width, qapp.width / 6);
		flpshadow.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM,
				RelativeLayout.TRUE);
		shadowwhate = (ImageView) this.findViewById(R.id.shawdowwhite);
		shadowwhate.setLayoutParams(flpshadow);

		shadowwhate.bringToFront();
		Picasso.with(this).load(R.drawable.shadoww)
				.resize(qapp.width, qapp.width / 6).into(shadowwhate);
		shadowwhate.setVisibility(View.INVISIBLE);

		/*********************** menuint ***************/
		sm = new SlidingMenu(this);
		sm.setMode(SlidingMenu.LEFT_RIGHT);
		sm.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);

		sm.setShadowWidth(0);
		sm.setShadowDrawable(R.drawable.shadow);
		sm.setBehindOffset(widthz / 4 * 3);
		sm.setFadeDegree(0.35f);
		// 设置slding menu的几种手势模式
		// TOUCHMODE_FULLSCREEN 全屏模式，在content页面中，滑动，可以打开sliding menu
		// TOUCHMODE_MARGIN 边缘模式，在content页面中，如果想打开slding ,你需要在屏幕边缘滑动才可以打开slding
		// menu
		// TOUCHMODE_NONE 自然是不能通过手势打开啦
		// sm.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
		sm.attachToActivity(this, SlidingMenu.TOUCHMODE_FULLSCREEN);

		sm.setMenu(R.layout.menuleftmain);
		sm.setSecondaryMenu(R.layout.menurightmain);

		menuleft.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				sm.showMenu(true);
			}
		});
		menuright.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				sm.showSecondaryMenu(true);

			}
		});

		listviewmenuleft = (ListView) this.findViewById(R.id.listViewmenuleft);

		menulayout = (LinearLayout) this.findViewById(R.id.menurightlayout);

		LayoutParams flp2 = new LinearLayout.LayoutParams(qapp.width / 4,
				qapp.width / 4);
		flp2.gravity = Gravity.RIGHT;

		menuvideo = (ImageView) this.findViewById(R.id.menuvideo);
		menuvideo.setLayoutParams(flp2);
		menuvideo.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				if (sm.isMenuShowing())
					sm.toggle();
				clearallviews();
				islistviewshow = true;
				listView.setVisibility(View.VISIBLE);
				listView.startAnimation(appear);

			}
		});

		menubrief = (ImageView) this.findViewById(R.id.menubrief);
		menubrief.setLayoutParams(flp2);

		menubrief.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				if (view == null) {
					/*********************
					 * 
					 * 
					 * 
					 * 
					 * 
					 * 
					 * 
					 * 
					 */
					islistviewshow = false;
					clearallviews();

					Thread loadbried = new Thread(runnableloadbrifview);
					loadbried.start();
				} else {
					if (sm.isMenuShowing())
						sm.toggle();

				}

			}
		});

		menuupload = (ImageView) this.findViewById(R.id.menuupload);
		menuupload.setLayoutParams(flp2);
		menuupload.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				MainActivity.this.clearallviews();
				islistviewshow = true;
				listView.setVisibility(View.VISIBLE);
				sm.toggle();
				// 延迟两秒跳转
				new Handler().postDelayed(new Runnable() {

					@Override
					public void run() {

						if (qapp.getselfid() == null) {
							Intent y = new Intent();
							y.setClass(MainActivity.this, Loginactivity.class);

							MainActivity.this.startActivityForResult(y,
									qapp.loginactivitycode);

							// ((Activity) context).finish();
							(MainActivity.this).overridePendingTransition(
									R.anim.alphaappear, R.anim.alphadisappear);
						} else {

							Intent y = new Intent();
							y.setClass(MainActivity.this, Upload.class);
							MainActivity.this.startActivity(y);
							// finish();
							(MainActivity.this).overridePendingTransition(
									R.anim.alphaappear, R.anim.alphadisappear);

						}
					}
				}, 400);

			}

		});

		threadgetcontest = new Thread(runnablegetcontest);
		threadgetcontest.start();

		listviewmenuleft.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int i,
					long arg3) {
				// TODO Auto-generated method stub
				clearallviews();
				listView.setVisibility(View.VISIBLE);
				islistviewshow = true;
				String contestname = null;
				// contesttheno=i;
				if (i == 0) {
					sm.toggle();
					// 延迟两秒跳转
					new Handler().postDelayed(new Runnable() {

						@Override
						public void run() {

							if (qapp.getselfid() == null) {
								Intent y = new Intent();
								y.setClass(MainActivity.this,
										Loginactivity.class);

								MainActivity.this.startActivityForResult(y,
										qapp.loginactivitycode);

								// ((Activity) context).finish();
								(MainActivity.this).overridePendingTransition(
										R.anim.alphaappear,
										R.anim.alphadisappear);
							} else {
								pd = ProgressDialog.show(MainActivity.this,
										"dowloading",
										"dowloading，please wait……");

								Intent y = new Intent();
								
								
								Bundle bundle = new Bundle();
								bundle.putString("uid", qapp.getselfid());
								bundle.putInt("news", 0);

								y.putExtras(bundle);
								y.setClass(MainActivity.this,
										com.standouter.standouternew.Profile.class);

								MainActivity.this.startActivityForResult(y, 2);

								// ((Activity) context).finish();
								MainActivity.this.overridePendingTransition(
										R.anim.alphaappear,
										R.anim.alphadisappear);

								pd.dismiss();
							}
						}
					}, 400);

					return;
				}

				contesttheno = i;
				if (i == 1) {
					contestname = "freestyle";

				}

				if (i >= 2 && i < totalcontestno + 2) {
					try {
						int j = i - 2;
						contestname = contestJson.getJSONArray("items")
								.getJSONObject(j).getString("code");
						qapp.setbriefjson(contestJson.getJSONArray("items")
								.getJSONObject(j));

					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				if (i >= totalcontestno + 2
						&& i < totalcontestno + totalchannelno + 2) {
					try {
						int j = i - 2 - totalcontestno;
						contestname = channelJson.getJSONArray("items")
								.getJSONObject(j).getString("code");
						qapp.setbriefjson(channelJson.getJSONArray("items")
								.getJSONObject(j));

					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				if (i == totalcontestno + totalchannelno + 2) {
					pd = ProgressDialog.show(MainActivity.this, "dowloading",
							"dowloading，please wait……");

					Thread logoutthread = new Thread(logoutrunnable);
					logoutthread.start();

					return;

				}
				qapp.setcontestname(contestname);

				pd = ProgressDialog.show(MainActivity.this, "dowloading",
						"dowloading，please wait……");

				threadbegin = new Thread(runnableload);
				threadbegin.start();

			}

		});

		/*********************** menuint ***************/

		listView = (XListView) findViewById(R.id.listViewmain);

		listView.setPullLoadEnable(true);

		listView.setXListViewListener(this);
		listView.setDivider(null);
		loadMoreView = getLayoutInflater().inflate(R.layout.load_more, null);
		loadMoreViewtop = getLayoutInflater().inflate(R.layout.load_more, null);

		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View viewitem,
					int position, long arg3) {
				// TODO Auto-generated method stub
				Log.i("", "" + position);
				if (position >= 1 && position <= mainadapter.getCount()) {
					if (player != null) {
						contentlayout.removeView(player);
						setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//

						player.destroyDrawingCache();
						player.destroytheview();
						player = null;
					}

					int location[] = { -1, -1 };
					int pos[] = { -1, -1 };
					listView.getLocationInWindow(location);
					viewitem.getLocationInWindow(pos);
					Log.i("", "" + location[1]);
					Log.i("", "" + pos[1]);

					if (pos[1] < location[1]) {
						listView.setSelection(position);
						Log.i("", "" + pos[1]);
						pos[1] = location[1];
					}

					player = new Playerlayout(MainActivity.this, qapp,
							mainJson, position, true, "");

					sm.addIgnoredView(player);

					FrameLayout.LayoutParams flp = new FrameLayout.LayoutParams(
							qapp.width, qapp.width / 16 * 9);
					flp.topMargin = pos[1] - location[1];
					posy = flp.topMargin;
					contentlayout.addView(player, flp);
					setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
					contentlayout.bringChildToFront(player);
					MainActivity.this.postion = position;
				}
			}

		});

		listView.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				// TODO Auto-generated method stub
				if (player != null) {
					contentlayout.removeView(player);
					setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//

					player.destroyDrawingCache();
					player.destroytheview();

					player = null;
				}
			}

		});

		loadMorepb = (ProgressBar) loadMoreView
				.findViewById(R.id.progressBarload);
		loadmoretext = (TextView) loadMoreView.findViewById(R.id.loadmoretext);

		loadMorepbtop = (ProgressBar) loadMoreViewtop
				.findViewById(R.id.progressBarload);
		loadmoretexttop = (TextView) loadMoreViewtop
				.findViewById(R.id.loadmoretext);

		loadMorepb.setVisibility(View.INVISIBLE);

		qapp.setcontestname("freestyle");
		pd = ProgressDialog.show(MainActivity.this, "dowloading",
				"dowloading，please wait……");

		Handler handler = new Handler() {
			public void handleMessage(final Message msg) {

				if ((qapp.getselfid() != null)) {

					if (notipic != null && notipic.isShown()) {
						notipic.setVisibility(View.GONE);
						notipic = null;
					}

					notipic = (MyImageView) MainActivity.this
							.findViewById(R.id.notipic);
					notipic.setVisibility(View.VISIBLE);
					threadgetnews = new Thread(getnewsrunnable);
					threadgetnews.start();

				}
			}
		};
		qapp.setHandler(handler);

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		super.onActivityResult(requestCode, resultCode, data);
		Log.i("ssssssss", "ssssssssss");
		switch (requestCode) {
		case 1:

			/* 回传错误时以Dialog显示 */

			if (qapp.getselfid() != null) {
				pd = ProgressDialog.show(MainActivity.this, "dowloading",
						"dowloading，please wait……");
				qapp.contestname = "freestyle";
				contesttheno=0;
				threadgetcontest = new Thread(runnablegetcontest);
				threadgetcontest.start();
				Log.i("id", qapp.getselfid());
			}
			break;
		case 2:
			if (resultCode == RESULT_OK) {
				Bundle bundleResult = data.getExtras();
				if (bundleResult.getBoolean("reloadmenu")) {
					pd = ProgressDialog.show(MainActivity.this, "dowloading",
							"dowloading，please wait……");

					Thread logoutthread = new Thread(logoutrunnable);
					logoutthread.start();
				}
			}

		default:

			break;

		}

	}

	Runnable getnewsrunnable = new Runnable() {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			try {
				newsjson = qapp.getJson(qapp.webaddress + "/notifications?ui="
						+ qapp.getselfid(), qapp.httpclient);
				Log.i("", newsjson.toString());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Message msg = new Message();
			getnewshandle.sendMessage(msg);

		}

	};

	Handler getnewshandle = new Handler() {
		@SuppressLint("ClickableViewAccessibility")
		public void handleMessage(final Message msg) {
			// super.handleMessage(msg2);

			flpnoti = new RelativeLayout.LayoutParams(qapp.width / 6,
					qapp.width / 6);
			// flpnoti.gravity=Gravity.RIGHT;
			flpnoti.topMargin = qapp.width / 4 + 30;
			flpnoti.leftMargin = 30;
			notipic.setLayoutParams(flpnoti);

			int newsno = 0;
			try {
				newsno = newsjson.getInt("totalCount");
				Log.i("", "no:" + newsno);

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (newsno == 0) {
				notipic.setVisibility(View.GONE);
			} else {

				Log.i("", "2no:" + newsno);

				notipic.setVisibility(View.VISIBLE);
				String legourl = null;
				try {
					legourl = qapp.webaddress
							+ newsjson.getJSONArray("items").getJSONObject(0)
									.getString("authorProfileImageUrl");
					Log.i("", "2no:" + legourl);

					Picasso.with(MainActivity.this).load(legourl)
							.resize(qapp.width / 6, qapp.width / 6)
							.into(notipic);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				notipic.bringToFront();
				notipic.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						pd = ProgressDialog.show(MainActivity.this,
								"dowloading", "dowloading，please wait……");

						Intent y = new Intent();
						Bundle bundle = new Bundle();
						bundle.putString("uid", qapp.getselfid());
						bundle.putInt("news", 1);
						y.putExtras(bundle);
						y.setClass(MainActivity.this,
								com.standouter.standouternew.Profile.class);

						MainActivity.this.startActivityForResult(y, 2);

						// ((Activity) context).finish();
						MainActivity.this.overridePendingTransition(
								R.anim.alphaappear, R.anim.alphadisappear);

						pd.dismiss();
					}
				});
				notipic.setOnTouchListener(new View.OnTouchListener() {
					int lastX, lastY;
					int x = 0;
					private Animation newsgotoleft;
					int lastea = MotionEvent.ACTION_DOWN;

					@Override
					public boolean onTouch(View v, MotionEvent event) {
						// TODO Auto-generated method stub
						int ea = event.getAction();

						Log.i("TAG", "Touch:" + ea);
						switch (ea) {
						case MotionEvent.ACTION_DOWN: // 按下
							lastX = (int) event.getRawX();
							lastY = (int) event.getRawY();
							lastea = MotionEvent.ACTION_DOWN;

							TranslateAnimation shawdowappear = new TranslateAnimation(
									0, 0, 100, 0);

							shawdowappear.setDuration(400);
							shawdowappear
									.setAnimationListener(new AnimationListener() {

										@Override
										public void onAnimationStart(
												Animation animation) {
											// TODO Auto-generated method stub
											shadowwhate
													.setVisibility(View.VISIBLE);
											shadowwhate.bringToFront();
										}

										@Override
										public void onAnimationEnd(
												Animation animation) {
											// TODO Auto-generated method stub

										}

										@Override
										public void onAnimationRepeat(
												Animation animation) {
											// TODO Auto-generated method stub

										}
									});
							MainActivity.this.shadowwhate
									.startAnimation(shawdowappear);

							break;
						/**
						 * layout(l,t,r,b) l Left position, relative to parent t
						 * Top position, relative to parent r Right position,
						 * relative to parent b Bottom position, relative to
						 * parent
						 * */
						case MotionEvent.ACTION_MOVE: // 移动
							// 移动中动态设置位置
							x++;
							int dx = (int) event.getRawX() - lastX;
							int dy = (int) event.getRawY() - lastY;
							int left = v.getLeft() + dx;
							int top = v.getTop() + dy;
							int right = v.getRight() + dx;
							int bottom = v.getBottom() + dy;
							if (left < 0) {
								left = 0;
								right = left + v.getWidth();
							}
							if (right > qapp.width) {
								right = qapp.width;
								left = right - v.getWidth();
							}
							if (top < 0) {
								top = 0;
								bottom = top + v.getHeight();
							}
							if (bottom > qapp.height) {
								bottom = qapp.height;
								top = bottom - v.getHeight();
							}
							flpnoti.leftMargin = left;
							flpnoti.topMargin = top;
							// flpnoti.setMargins(left, top, right, bottom);
							notipic.setLayoutParams(flpnoti);
							// v.layout(left, top, right, bottom);
							Log.i("aaa", "position：" + left + ", " + top + ", "
									+ right + ", " + bottom);
							// 将当前的位置再次设置
							lastX = (int) event.getRawX();
							lastY = (int) event.getRawY();
							lastea = MotionEvent.ACTION_MOVE;
							break;

						default: // 脱离

							if (ea == MotionEvent.ACTION_UP && x < 3)
								v.performClick();
							x = 0;
							Log.i("TAG", "Touch:" + ea);
							TranslateAnimation shawdowappear2 = new TranslateAnimation(
									0, 0, 0, 1000);

							shawdowappear2.setDuration(400);
							shawdowappear2
									.setAnimationListener(new AnimationListener() {

										@Override
										public void onAnimationStart(
												Animation animation) {
											// TODO Auto-generated method stub

										}

										@Override
										public void onAnimationEnd(
												Animation animation) {
											// TODO Auto-generated method stub

											shadowwhate
													.setVisibility(View.INVISIBLE);
										}

										@Override
										public void onAnimationRepeat(
												Animation animation) {
											// TODO Auto-generated method stub

										}
									});
							int leftx = notipic.getLeft();
							int downy = notipic.getBottom();
							if (downy > qapp.height - 100) {
								notipic.setVisibility(View.GONE);
								notipic = null;
								shadowwhate.startAnimation(shawdowappear2);


							} else {

								if (leftx > qapp.width / 2 - qapp.width / 12) {
									gotonewsanimation = new TranslateAnimation(
											0, qapp.width - leftx - qapp.width
													/ 6, 0, 0);
									gotonewsanimation.setDuration(400);
									// gotonewsanimation.setFillAfter(true);
									// notipic.setAnimation(gotonewsanimation);
									gotonewsanimation
											.setAnimationListener(new AnimationListener() {

												@Override
												public void onAnimationStart(
														Animation animation) {
													// TODO Auto-generated
													// method stub

												}

												@Override
												public void onAnimationEnd(
														Animation animation) {
													// TODO Auto-generated
													// method stub
													notipic.clearAnimation();
													flpnoti.leftMargin = qapp.width * 5 / 6;
													// flpnoti.setMargins(left,
													// top, right, bottom);
													notipic.setLayoutParams(flpnoti);
												}

												@Override
												public void onAnimationRepeat(
														Animation animation) {
													// TODO Auto-generated
													// method stub

												}
											});
								} else {

									gotonewsanimation = new TranslateAnimation(
											0, -leftx, 0, 0);
									gotonewsanimation.setDuration(400);
									// gotonewsanimation.setFillAfter(true);
									// notipic.setAnimation(gotonewsanimation);
									gotonewsanimation
											.setAnimationListener(new AnimationListener() {

												@Override
												public void onAnimationStart(
														Animation animation) {
													// TODO Auto-generated
													// method stub

												}

												@Override
												public void onAnimationEnd(
														Animation animation) {
													// TODO Auto-generated
													// method stub
													notipic.clearAnimation();
													flpnoti.leftMargin = 0;
													// flpnoti.setMargins(left,
													// top, right, bottom);
													notipic.setLayoutParams(flpnoti);
												}

												@Override
												public void onAnimationRepeat(
														Animation animation) {
													// TODO Auto-generated
													// method stub

												}
											});

								}
								shadowwhate.startAnimation(shawdowappear2);

								notipic.startAnimation(gotonewsanimation);
							}

							break;
						}
						return true;
					}

				});
			}
		}
	};

	Runnable logoutrunnable = new Runnable() {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			try {
				List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(
						3);
				nameValuePairs.add(new BasicNameValuePair("json_response",
						"true"));

				JSONObject logoutjson = qapp.getJsonpost(qapp.webaddress
						+ "/logout", qapp.httpclient, nameValuePairs);
				Log.i("", "logut:" + logoutjson.toString());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			qapp.callFacebookLogout(MainActivity.this);
			qapp.contestname = "freestyle";
			contesttheno=0;

			qapp.setselfid(null);
			File f1 = new File("/sdcard/header9.txt");
			if (f1.exists()) {
				f1.delete();

			}
			f1 = new File("/sdcard/header8.txt");
			if (f1.exists()) {
				f1.delete();
			}
			f1 = null;
			Message msg = new Message();
			logouthandle.sendMessage(msg);

		}

	};
	Handler logouthandle = new Handler() {
		public void handleMessage(final Message msg) {
			// super.handleMessage(msg2);

			threadgetcontest = new Thread(runnablegetcontest);
			threadgetcontest.start();

		}
	};

	Runnable runnablegetcontest = new Runnable() {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			/***************** loginstate *******************/
			try {
				loginstatejson = qapp.getJson(qapp.webaddress
						+ "/login_status.json", qapp.httpclient);

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			String stringtemp = null;
			try {
				stringtemp = loginstatejson.getString("authenticated");
				if (stringtemp.equals("true")) {
					qapp.setselfid(loginstatejson.getString("registrationId"));
					JPushInterface.setAlias(qapp, qapp.getselfid(), null);
					Log.i("", qapp.getselfid());
				} else {
					JPushInterface.setAlias(qapp, "", null);
					qapp.setselfid(null);
					try {
						List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(
								3);
						nameValuePairs.add(new BasicNameValuePair(
								"json_response", "true"));

						JSONObject logoutjson = qapp.getJsonpost(
								qapp.webaddress + "/logout", qapp.httpclient,
								nameValuePairs);

					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					qapp.callFacebookLogout(MainActivity.this);
					qapp.contestname = "freestyle";
					qapp.setselfid(null);
					File f1 = new File("/sdcard/header9.txt");
					if (f1.exists()) {
						f1.delete();

					}
					f1 = new File("/sdcard/header8.txt");
					if (f1.exists()) {
						f1.delete();
					}
					f1 = null;

				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			Log.i("", "stateis::::" + loginstatejson.toString());

			/***************** loginstateend *******************/
			String s1 = null;
			String s2 = null;
			try {
				s1 = qapp.readFileSdcardFile("/sdcard/contestjson.txt");
				s2 = qapp.readFileSdcardFile("/sdcard/channeljson.txt");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			// Log.i("",s);
			try {
				contestJson = null;
				channelJson = null;
				contestJson = new JSONObject(s1);
				channelJson = new JSONObject(s2);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				totalcontestno = contestJson.getInt("total");
				totalchannelno = channelJson.getInt("total");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			rowmenuutems = new ArrayList<rowitemmenu>();

			for (int i = 0; i <= totalcontestno + totalchannelno + 2; i++) {

				if (i == 0) {
					String contestname = "freestyle";
					String legourl = "";
					if (qapp.getselfid() == null) {

						legourl = "file:///android_asset/image/defaultavatar.png";
					} else {
						legourl = qapp.webaddress + "/avatar/"
								+ qapp.getselfid() + "/picture";
						Log.i("logossssssss", legourl);
					}

					Boolean isopen = false;
					rowitemmenu item2 = new rowitemmenu(contestname, legourl,
							isopen, false);
					rowmenuutems.add(item2);
				}
				if (i == 1) {
					String contestname = "freestyle";
					String legourl = "file:///android_asset/image/"
							+ contestname + ".png";
					Boolean isopen = true;
					rowitemmenu item2 = new rowitemmenu(contestname, legourl,
							isopen, true);
					rowmenuutems.add(item2);
				}

				if (i >= 2 && i < totalcontestno + 2) {
					try {
						int j = i - 2;
						String contestname = contestJson.getJSONArray("items")
								.getJSONObject(j).getString("code");
						String legourl = qapp.webaddress
								+ contestJson.getJSONArray("items")
										.getJSONObject(j).getString("logoUrl");
						Boolean isopen = contestJson.getJSONArray("items")
								.getJSONObject(j).getString("stateCode")
								.endsWith("OPEN");
						rowitemmenu item2 = new rowitemmenu(contestname,
								legourl, isopen, true);
						rowmenuutems.add(item2);

					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				if (i >= totalcontestno + 2
						&& i < totalcontestno + totalchannelno + 2) {
					try {
						int j = i - 2 - totalcontestno;
						String contestname = channelJson.getJSONArray("items")
								.getJSONObject(j).getString("code");
						String legourl = qapp.webaddress
								+ channelJson.getJSONArray("items")
										.getJSONObject(j).getString("logoUrl");
						Boolean isopen = channelJson.getJSONArray("items")
								.getJSONObject(j).getString("stateCode")
								.endsWith("OPEN");
						rowitemmenu item2 = new rowitemmenu(contestname,
								legourl, isopen, true);
						rowmenuutems.add(item2);

					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

				if (qapp.getselfid() != null
						&& i == totalcontestno + totalchannelno + 2) {
					String contestname = "freestyle";
					String legourl = "";
					legourl = "file:///android_asset/image/logout.png";

					Boolean isopen = false;
					rowitemmenu item2 = new rowitemmenu(contestname, legourl,
							isopen, false);
					rowmenuutems.add(item2);
				}

			}

			/*********************** fromhere *****/

			menuleftadapter = new adapter.menuleftadapter(MainActivity.this,
					rowmenuutems, MainActivity.this);

			Message msg = new Message();
			showmenu.sendMessage(msg);

		}

	};
	private String conteststring;
	private String tempurl;

	Runnable runnableloadbrifview = new Runnable() {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			LayoutInflater mInflater = (LayoutInflater) MainActivity.this
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = mInflater.inflate(R.layout.briefview, null);

			view.setDrawingCacheEnabled(true);

			breifimage = (ImageView) view.findViewById(R.id.briefvideo);
			RelativeLayout.LayoutParams flp3 = new RelativeLayout.LayoutParams(
					qapp.width, qapp.width / 16 * 9);

			breifimage.setLayoutParams(flp3);

			brieftext = (TextView) view.findViewById(R.id.contesttext);
			Typeface type = Typeface.createFromAsset(
					MainActivity.this.getAssets(), "fonts/oswald.light.ttf");
			// brieftext.setTypeface(type);

			//conteststring = null;

			tempurl = "";
			videocode = "";
			Log.i("contesttheno", "" + contesttheno);
			if (contesttheno == 0) {
				videocode = "RAIL0cx5";
				InputStream inputStream = getResources().openRawResource(
						R.raw.freestylebrief);
				conteststring = (MainActivity.this.getString(inputStream));

			}
			if (contesttheno == 1) {
				videocode = "RAIL0cx5";
				InputStream inputStream = getResources().openRawResource(
						R.raw.freestylebrief);

				conteststring = (MainActivity.this.getString(inputStream));

			}

			if (contesttheno >= 2 && contesttheno < totalcontestno + 2) {
				try {
					int j = contesttheno - 2;
					videocode = contestJson.getJSONArray("items")
							.getJSONObject(j).getString("spotVideoUrl");
					conteststring = contestJson.getJSONArray("items")
							.getJSONObject(j).getString("description");

				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (contesttheno >= totalcontestno + 2
					&& contesttheno < totalcontestno + totalchannelno + 2) {
				try {
					int j = contesttheno - 2 - totalcontestno;
					videocode = channelJson.getJSONArray("items")
							.getJSONObject(j).getString("spotVideoUrl");
					conteststring = channelJson.getJSONArray("items")
							.getJSONObject(j).getString("description");

				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			tempurl = "https://content.bitsontherun.com/thumbs/" + videocode
					+ "-320.jpg";

			Log.i("", tempurl);

			Message msg = new Message();
			showbreifmesaage.sendMessage(msg);

		}

	};

	private Handler showbreifmesaage = new Handler() {
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			LinearLayout.LayoutParams flp = new LinearLayout.LayoutParams(
					qapp.width, qapp.height - qapp.width / 4);
			flp.gravity = Gravity.CENTER_HORIZONTAL;
			aq.id(breifimage).image(tempurl, qapp.getmemCache(),
					qapp.getfileCache(), qapp.getwidth(), 0);
			// brieftext.setText("\n"+convertNodeToText(Jsoup.parse(conteststring)));
			brieftext.setText(Html.fromHtml(conteststring, new ImageGetter(),
					null));
			breifimage.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					if (player != null) {
						contentlayout.removeView(player);
						setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//

						player.destroyDrawingCache();
						player.destroytheview();
						player = null;
					}

					int pos[] = { -1, -1 };
					breifimage.getLocationInWindow(pos);
					Log.i("", "" + pos[1]);

					player = new Playerlayout(MainActivity.this, qapp, null, 0,
							false, videocode);
					sm.addIgnoredView(player);
					FrameLayout.LayoutParams flp = new FrameLayout.LayoutParams(
							qapp.width, qapp.width / 16 * 9);
					flp.topMargin = 0;
					posy = 0;
					contentlayout.addView(player, flp);
					setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);

					contentlayout.bringChildToFront(player);
				}

			});

			if (sm.isMenuShowing())
				sm.toggle();
			listView.setDrawingCacheEnabled(true);
			listView.destroyDrawingCache();
			if (runnableloadbrifview != null) {
				this.removeCallbacks(runnableloadbrifview);
			}
			contentlayout.addView(view, flp);
			view.startAnimation(appear);

		}
	};
	private Handler showmenu = new Handler() {
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			listView.setVisibility(View.VISIBLE);
			islistviewshow = true;
			listviewmenuleft.setAdapter(menuleftadapter);
			if (runnablegetcontest != null) {
				this.removeCallbacks(runnablegetcontest);
			}

			String path = "/sdcard/json.txt";
			File f = new File(path);
			if (f.exists()) {
				threadbegin = new Thread(runnablebegin);
				threadbegin.start();
			} else {
				threadbegin = new Thread(runnableload);
				threadbegin.start();

			}

		}
	};

	Runnable runnablebegin = new Runnable() {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			String s = null;
			try {
				s = qapp.readFileSdcardFile("/sdcard/json.txt");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			// Log.i("",s);
			try {
				mainJson = null;
				mainJson = new JSONObject(s);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			showrowsno = 5;
			try {
				totalResults = mainJson.getInt("totalResults");
				if (totalResults <= showrowsno) {
					showrowsno = totalResults;
				}

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			Message msg = new Message();
			showmainpage.sendMessage(msg);

		}

	};

	Runnable runnableload = new Runnable() {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			try {
				mainJson = null;
				mainJson = qapp.getJson(qapp.webaddress
						+ "/video/search?ss=contest&so=most_recent&sp="
						+ qapp.contestname, qapp.httpclient);
				if (qapp.contestname.equals("freestyle")) {
					qapp.writeFileSdcardFile("/sdcard/json.txt",
							mainJson.toString());
				}
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			showrowsno = 5;
			try {
				totalResults = mainJson.getInt("totalResults");
				if (totalResults <= showrowsno) {
					showrowsno = totalResults;
				}

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			Message msg = new Message();
			showmainpage.sendMessage(msg);

		}

	};

	private void loadlistview() {

		rowItems = new ArrayList<RowItem>();

		for (int i = 0; i < showrowsno; i++) {
			String imageurl = null;
			String videowriter = "";
			String videoname = "";
			int votecount = 0;
			String videowiterid = "";
			String videoid = "";
			String videourl = "";
			boolean showflag = false;
			try {
				imageurl = mainJson.getJSONArray("items").getJSONObject(i)
						.getString("imageUrl320");
				videowriter = mainJson.getJSONArray("items").getJSONObject(i)
						.getString("ownerName")
						+ " "
						+ mainJson.getJSONArray("items").getJSONObject(i)
								.getString("ownerSurname");
				videoname = mainJson.getJSONArray("items").getJSONObject(i)
						.getString("title");

				votecount = mainJson.getJSONArray("items").getJSONObject(i)
						.getInt("votesCount");
				videowiterid = mainJson.getJSONArray("items").getJSONObject(i)
						.getString("ownerId");
				videoid = mainJson.getJSONArray("items").getJSONObject(i)
						.getString("id");
				videourl = mainJson.getJSONArray("items").getJSONObject(i)
						.getString("videoUrl480");
				;

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			;

			RowItem item = new RowItem(imageurl, videowriter, videoname,
					votecount, videowiterid, videoid, videourl, showflag);

			rowItems.add(item);
			imageurl = null;
			videowriter = null;
			videoname = null;
			votecount = 0;
			videowiterid = null;
			videoid = null;
			videourl = null;
		}

		mainadapter = new page1adapter(this, rowItems, this);

	}

	private Handler showmainpage = new Handler() {
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			loadlistview();

			if (notipic == null && (qapp.getselfid() != null)) {

				notipic = (MyImageView) MainActivity.this
						.findViewById(R.id.notipic);
				notipic.setVisibility(View.VISIBLE);
				threadgetnews = new Thread(getnewsrunnable);
				threadgetnews.start();

			}

			menulayout.setBackgroundColor(Color.parseColor("#CD5223"));
			if (qapp.contestname.equals("freestyle")) {
				menulayout.setBackgroundColor(Color.parseColor("#FFCD5223"));
			}
			if (qapp.contestname.equals("volagratis")) {
				menulayout.setBackgroundColor(Color.WHITE);
			}
			if (qapp.contestname.equals("universal")) {
				menulayout.setBackgroundColor(Color.WHITE);
			}
			if (qapp.contestname.equals("cityselfie")) {
				menulayout.setBackgroundColor(Color.parseColor("#02782B"));
			}
			if (qapp.contestname.equals("tuborg")) {
				menulayout.setBackgroundColor(Color.parseColor("#003D22"));
			}
			if (qapp.contestname.equals("grock")) {
				menulayout.setBackgroundColor(Color.parseColor("#D94410"));
			}
			if (qapp.contestname.equals("goandfun")) {
				menulayout.setBackgroundColor(Color.parseColor("#ACCA58"));
			}
			if (qapp.contestname.equals("metro")) {
				menulayout.setBackgroundColor(Color.parseColor("#02782B"));
			}
			if (qapp.contestname.equals("whoodbrooklyn")) {
				menulayout.setBackgroundColor(Color.parseColor("#6B0916"));
			}

			// menulayout.setBackgroundColor(Color.WHITE);

			// Picasso.with(MainActivity.this).load("file:///android_asset/image/"+qapp.getcontestname()+"header.png").resize(qapp.width,
			// qapp.width/4).into(headerimg);
			// Picasso.with(MainActivity.this).load("file:///android_asset/image/"+qapp.getcontestname()+".png").resize(qapp.width/4,qapp.width/4).into(menuvideo);

			if (qapp.contestname.equals("freestyle")) {
				Picasso.with(MainActivity.this)
						.load("file:///android_asset/image/freestyle"
								+ "header.png")
						.resize(qapp.width, qapp.width / 4).into(headerimg);
				Picasso.with(MainActivity.this)
						.load("file:///android_asset/image/freestyle" + ".png")
						.resize(qapp.width / 4, qapp.width / 4).into(menuvideo);

			} else {
				try {
					Picasso.with(MainActivity.this)
							.load(qapp.webaddress
									+ qapp.getbriefjson().getString(
											"backgroundImageUrlMobile"))
							.resize(qapp.width, qapp.width / 4).into(headerimg);
					Picasso.with(MainActivity.this)
							.load(qapp.webaddress
									+ qapp.getbriefjson().getString("logoUrl"))
							.resize(qapp.width / 4, qapp.width / 4)
							.into(menuvideo);

				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}

			if (((ColorDrawable) menulayout.getBackground()).getColor() == Color.WHITE) {
				Picasso.with(MainActivity.this).load(R.drawable.briefb)
						.resize(qapp.width / 4, qapp.width / 4).into(menubrief);
				Picasso.with(MainActivity.this).load(R.drawable.uploadb)
						.resize(qapp.width / 4, qapp.width / 4)
						.into(menuupload);
			} else {
				Picasso.with(MainActivity.this).load(R.drawable.brief)
						.resize(qapp.width / 4, qapp.width / 4).into(menubrief);
				Picasso.with(MainActivity.this).load(R.drawable.upload)
						.resize(qapp.width / 4, qapp.width / 4)
						.into(menuupload);

			}
			listView.setVisibility(View.VISIBLE);
			islistviewshow = true;
			if (view != null) {
				contentlayout.removeViewInLayout(view);
				view.destroyDrawingCache();
				view = null;
			}
			view = null;
			listView.setAdapter(mainadapter);
			Log.i("", "zzzzzzzzzzzzzzzzz" + mainadapter.getCount());
			pd.dismiss();
			onLoad();

			if (runnablebegin != null) {
				this.removeCallbacks(runnablebegin);
			}
			if (runnableload != null) {
				this.removeCallbacks(runnableload);
			}
			if (threadbegin != null) {
				threadbegin.interrupt();
				threadbegin = null;
			}

			if (sm.isMenuShowing())
				sm.toggle();

			// threadbegin.destroy();

			// txtCount.setText(Integer.toString(msg.getData().getInt("count",
			// 0)));
		}
	};

	/**************************************** listview loadmore ************/

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	public void onRefresh() {
		pd = ProgressDialog.show(MainActivity.this, "dowloading",
				"dowloading，please wait……");

		Log.i("LOADMORE", "loading...");
		loadmoretexttop.setText("Loadind...");
		loadMorepbtop.setVisibility(View.VISIBLE);

		Message msg = new Message();
		msg.what = 1;
		handlersoclltop.sendMessageDelayed(msg, 200);

	}

	private void onLoad() {
		listView.stopRefresh();
		listView.stopLoadMore();
		listView.setRefreshTime(date);

	}

	public void onLoadMore() {
		int itemsLastIndex = mainadapter.getCount(); // 数据集最后一项的索引
		final int lastIndex = itemsLastIndex + 1;

		Log.i("LOADMORE", "loading...");
		Log.i("", lastIndex + "");
		Log.i("", "gf" + showrowsno);

		// listView.setfootstate(STATE_REFRESHING );
		// loadmoretext.setText("Loadind...");
		// loadMorepb.setVisibility(View.VISIBLE);
		Message msg = new Message();
		msg.what = lastIndex;
		handlersocll.sendMessageDelayed(msg, 1000);
	}

	private Handler handlersocll = new Handler() {
		@Override
		public void handleMessage(final Message msg) {
			// super.handleMessage(msg2);

			// TODO Auto-generated method stub
			if (msg.what < totalResults) {
				loadData();

				mainadapter.notifyDataSetChanged(); // 数据集变化后,通知adapter
				// listView.setSelection(visibleLastIndex - visibleItemCount +
				// 1); //设置选中项
				// loadmoretext.setText("Load More");
				// listView.setfootstate(STATE_READY);
			} else {
				if (totalResults > 3) {
					// listView.setfootstate(STATE_NORMAL);
					// loadmoretext.setText("All Loaded");
				} else {

					// listView.setfootstate(STATE_NORMAL);

				}
			}
			onLoad();
			// loadMorepb.setVisibility(View.INVISIBLE);

		}
	};
	private Handler handlersoclltop = new Handler() {

		@Override
		public void handleMessage(final Message msg) {
			// super.handleMessage(msg2);

			// TODO Auto-generated method stub
			if (msg.what == 1) {

				SimpleDateFormat sDateFormat = new SimpleDateFormat(
						"dd-mm-yyyy   hh:mm:ss");
				date = sDateFormat.format(new java.util.Date());
				threadbegin = new Thread(runnableload);
				threadbegin.start();
			}

			// onLoad();

		}
	};

	/**************************************** listview loadmore end ************/

	private void loadData() {
		int count = mainadapter.getCount();
		for (int i = count; i < count + 5; i++) {
			if (i < totalResults) {

				String imageurl = null;
				String videowriter = "";
				String videoname = "";
				int votecount = 0;
				String videowiterid = "";
				String videoid = "";
				String videourl = "";
				boolean showflag = false;
				try {
					imageurl = mainJson.getJSONArray("items").getJSONObject(i)
							.getString("imageUrl480");
					videowriter = mainJson.getJSONArray("items")
							.getJSONObject(i).getString("ownerName")
							+ " "
							+ mainJson.getJSONArray("items").getJSONObject(i)
									.getString("ownerSurname");
					videoname = mainJson.getJSONArray("items").getJSONObject(i)
							.getString("title");

					votecount = mainJson.getJSONArray("items").getJSONObject(i)
							.getInt("votesCount");
					videowiterid = mainJson.getJSONArray("items")
							.getJSONObject(i).getString("ownerId");
					videoid = mainJson.getJSONArray("items").getJSONObject(i)
							.getString("id");
					videourl = mainJson.getJSONArray("items").getJSONObject(i)
							.getString("videoUrl480");
					;

				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				;

				RowItem item = new RowItem(imageurl, videowriter, videoname,
						votecount, videowiterid, videoid, videourl, showflag);

				mainadapter.addItem(item);
				imageurl = null;
				videowriter = null;
				videoname = null;
				votecount = 0;
				videowiterid = null;
				videoid = null;
				videourl = null;

			} else {
				showrowsno = totalResults;
			}
		}
	}

	public static String getString(InputStream inputStream) {
		InputStreamReader inputStreamReader = null;
		inputStreamReader = new InputStreamReader(inputStream);
		BufferedReader reader = new BufferedReader(inputStreamReader);
		StringBuffer sb = new StringBuffer("");
		String line;
		try {
			while ((line = reader.readLine()) != null) {
				sb.append(line);
				sb.append("\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return sb.toString();
	}

	private long mExitTime;
	private int x;

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if ((System.currentTimeMillis() - mExitTime) > 2000) {
				Toast.makeText(this, "Press again to exit the program",
						Toast.LENGTH_SHORT).show();
				mExitTime = System.currentTimeMillis();

			} else {

				System.exit(0);

			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	Boolean notipicisshow = false;

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		// TODO Auto-generated method stub
		super.onConfigurationChanged(newConfig);
		if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
			if (player != null) {
				player.setheng();
				listView.setVisibility(View.GONE);
				this.headerlayout.setVisibility(View.GONE);

				if (notipic != null) {
					if (notipic.isShown()) {
						notipic.setVisibility(View.INVISIBLE);
						notipicisshow = true;
					} else {
						notipicisshow = false;

					}
				}

			}
			sm.setEnabled(false);

		}
		if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
			if (player != null) {

				player.setshu(posy);

				if (notipicisshow && notipic != null) {
					notipic.setVisibility(View.VISIBLE);
				}
				this.headerlayout.setVisibility(View.VISIBLE);
				if (islistviewshow)
					listView.setVisibility(View.VISIBLE);

			}
			sm.setEnabled(true);
			Log.i("info", "landscape2");

		}
	}

	public void clearallviews() {
		if (player != null) {
			contentlayout.removeView(player);
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//

			player.destroyDrawingCache();
			player.destroytheview();

			player = null;
		}
		listView.setVisibility(View.GONE);
		if (view != null) {
			contentlayout.removeViewInLayout(view);
			view.destroyDrawingCache();
			view = null;
		}
	}

	public String convertNodeToText(Element element) {
		final StringBuilder buffer = new StringBuilder();

		new NodeTraversor(new NodeVisitor() {
			boolean isNewline = true;

			public void head(Node node, int depth) {
				if (node instanceof TextNode) {
					TextNode textNode = (TextNode) node;
					String text = textNode.text().replace('\u00A0', ' ').trim();
					if (!text.isEmpty()) {
						buffer.append(text);
						isNewline = false;
					}
				} else if (node instanceof Element) {
					Element element = (Element) node;
					if (!isNewline) {
						if ((element.isBlock() || element.tagName()
								.equals("br"))) {
							buffer.append("\n\n\n");
							isNewline = true;
						}
					}
				}
			}

			@Override
			public void tail(Node node, int depth) {
			}
		}).traverse(element);

		return buffer.toString();
	}

	private class ImageGetter implements Html.ImageGetter {

		public Drawable getDrawable(String source) {
			int id;
			if (source.equals("hughjackman.jpg")) {
				id = R.drawable.freestyle;
			} else {
				return null;
			}

			Drawable d = getResources().getDrawable(id);
			d.setBounds(0, 0, d.getIntrinsicWidth(), d.getIntrinsicHeight());
			return d;
		}
	};

	@Override
	protected void onResume() {
		super.onResume();
		JPushInterface.onResume(this);
	}

	@Override
	protected void onPause() {
		super.onPause();
		JPushInterface.onPause(this);
	}

}
