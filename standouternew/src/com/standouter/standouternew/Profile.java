package com.standouter.standouternew;

import java.io.IOException;
import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import rowierm.RowItem;
import rowierm.fansiterm;
import rowierm.newsiterm;

import com.androidquery.AQuery;
import com.standouter.standouternew.R;
import com.standouter.standouternew.R.anim;
import com.standouter.standouternew.R.drawable;
import com.standouter.standouternew.R.id;
import com.standouter.standouternew.R.layout;
import com.standouter.standouternew.R.menu;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.squareup.picasso.Picasso;

import adapter.Newsadapter;
import adapter.fansadapter;
import adapter.page2adapter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.Typeface;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.FrameLayout.LayoutParams;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class Profile extends Activity {

	private Standouter qapp;
	private ImageView backleftbtn;
	private ImageView menuprofilebtn;
	private TextView profiletitle;
	private String uid;

	private Thread threadinfoview;
	private Thread threadprofileview;
	private Thread threadfansview;

	protected JSONObject uidjson;
	protected JSONObject videojson;
	protected JSONObject fansjson;

	protected ProgressDialog pd;
	private FrameLayout contentview;
	private Infoview infoview;
	private GridView fansview;

	private SlidingMenu sm;
	private ImageView infomenu;
	private ImageView profilemenu;
	private ImageView fansmenu;
	private ImageView newsmenu;
	private ImageView logooutmenu;
	private String tempstring;
	private AQuery aq;
	private ListView lvprofile;
	private Playerlayout player;

	private ArrayList<RowItem> rowItems;
	private page2adapter mainadapter;

	private ArrayList<fansiterm> fansItems;
	private fansadapter fansadapter1;
	private FrameLayout headerlayout;
	private int posy;
	private Boolean willreload;

	private ListView newslv;
	private ArrayList<newsiterm> newsitems;
	private Newsadapter mewsadapter;
	protected Thread threadnewsview;
	protected JSONObject newsjson;
	private int isgotonews=0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		/********************************************************************/
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		// setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);//
		// getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
		// WindowManager.LayoutParams.FLAG_FULLSCREEN);//for full screnn
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//
		/********************************************************************/
		setContentView(R.layout.activity_profile);
		aq = new AQuery(this);

		Bundle bData = this.getIntent().getExtras();
		uid = bData.getString("uid");

		getWindow().setFormat(PixelFormat.TRANSLUCENT);
		qapp = (Standouter) getApplication();
		willreload = false;
		uidjson = null;
		videojson = null;
		infoview = null;
		lvprofile = null;
		newslv = null;
		newsjson = null;

		headerlayout = (FrameLayout) this
				.findViewById(R.id.headerlayoutprofile);
		LinearLayout.LayoutParams flp = new LinearLayout.LayoutParams(
				qapp.width, qapp.width / 4);
		flp.gravity = Gravity.CENTER_HORIZONTAL;
		headerlayout.setLayoutParams(flp);
		headerlayout.setBackgroundColor(Color.BLACK);

		backleftbtn = (ImageView) this.findViewById(R.id.imgmenuleftprofile);
		LayoutParams flpimg = new FrameLayout.LayoutParams(qapp.width / 6, qapp.width /  4);
		flpimg.gravity = Gravity.LEFT;
		backleftbtn.setLayoutParams(flpimg);
		Picasso.with(this).load(R.drawable.lw)
				.resize(qapp.width / 6, qapp.width / 4).into(backleftbtn);
		backleftbtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});

		menuprofilebtn = (ImageView) this
				.findViewById(R.id.imgmenurightprofile);
		flpimg = new FrameLayout.LayoutParams(qapp.width / 6, qapp.width / 4);
		flpimg.gravity = Gravity.RIGHT;
		menuprofilebtn.setLayoutParams(flpimg);
		Picasso.with(this).load(R.drawable.menu)
				.resize(qapp.width / 6, qapp.width / 4)
				.into(menuprofilebtn);
		profiletitle = (TextView) this.findViewById(R.id.profiletile);
		Typeface type = Typeface.createFromAsset(this.getAssets(),
				"fonts/oswald.bold.ttf");
		profiletitle.setTypeface(type);

		contentview = (FrameLayout) this
				.findViewById(R.id.contentlayoutprofile);

		/***************************************************************/
		sm = new SlidingMenu(this);
		sm.setMode(SlidingMenu.RIGHT);
		sm.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);

		sm.setShadowWidth(0);
		sm.setShadowDrawable(R.drawable.shadow);
		sm.setBehindOffset(qapp.width * 3 / 4);
		sm.setFadeDegree(0.35f);
		// 设置slding menu的几种手势模式
		// TOUCHMODE_FULLSCREEN 全屏模式，在content页面中，滑动，可以打开sliding menu
		// TOUCHMODE_MARGIN 边缘模式，在content页面中，如果想打开slding ,你需要在屏幕边缘滑动才可以打开slding
		// menu
		// TOUCHMODE_NONE 自然是不能通过手势打开啦
		sm.attachToActivity(this, SlidingMenu.TOUCHMODE_FULLSCREEN);
		sm.setMenu(R.layout.profilemenu);

		menuprofilebtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				sm.showMenu();
			}
		});
		flp = new LinearLayout.LayoutParams(qapp.width / 4, qapp.width / 4);
		flp.gravity = Gravity.CENTER_HORIZONTAL;

		infomenu = (ImageView) this.findViewById(R.id.infomenu);
		infomenu.setLayoutParams(flp);
		infomenu.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				if (infoview == null) {
					clearallview();
					pd = ProgressDialog.show(Profile.this, "dowloading",
							"dowloading，please wait……");
					Profile.this.threadinfoview = new Thread(inforun);
					threadinfoview.start();
				} else {
					sm.toggle();
				}
			}
		});

		profilemenu = (ImageView) this.findViewById(R.id.profilemenu);
		profilemenu.setLayoutParams(flp);
		Picasso.with(this).load(R.drawable.portfolio)
				.resize(qapp.width / 4, qapp.width / 4).into(profilemenu);
		profilemenu.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				if (lvprofile == null) {

					clearallview();
					pd = ProgressDialog.show(Profile.this, "dowloading",
							"dowloading，please wait……");
					Profile.this.threadprofileview = new Thread(profileviewrun);
					Profile.this.threadprofileview.start();

				} else {
					sm.toggle();
				}

			}
		});

		fansmenu = (ImageView) this.findViewById(R.id.fansmenu);
		fansmenu.setLayoutParams(flp);
		Picasso.with(this).load(R.drawable.fans)
				.resize(qapp.width / 4, qapp.width / 4).into(fansmenu);
		fansmenu.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (fansview == null) {
					clearallview();
					pd = ProgressDialog.show(Profile.this, "dowloading",
							"dowloading，please wait……");
					Profile.this.threadfansview = new Thread(fansviewrun);
					Profile.this.threadfansview.start();

				} else {
					sm.toggle();
				}
			}
		});

		newsmenu = (ImageView) this.findViewById(R.id.newsmenu);
		logooutmenu = (ImageView) this.findViewById(R.id.logoutmenu);

		// aq.id(infomenu).image(R.drawable.portfolio, qapp.getmemCache(),
		// qapp.getfileCache(),qapp.width/4,0);
		if (qapp.getselfid() != null) {
			if (qapp.getselfid().equals(uid)) {
				newsmenu.setLayoutParams(flp);
				Picasso.with(this).load(R.drawable.news)
						.resize(qapp.width / 4, qapp.width / 4).into(newsmenu);
				newsmenu.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						if (newslv == null) {
							clearallview();
							pd = ProgressDialog.show(Profile.this,
									"dowloading", "dowloading，please wait……");
							Profile.this.threadnewsview = new Thread(
									newsviewrun);
							Profile.this.threadnewsview.start();

						} else {
							sm.toggle();
						}

					}
				});

				logooutmenu.setLayoutParams(flp);
				Picasso.with(this).load(R.drawable.logout)
						.resize(qapp.width / 4, qapp.width / 4)
						.into(logooutmenu);

				logooutmenu.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						sm.toggle();
						willreload = true;
						finish();
					}
				});
			} else {
				newsmenu.setVisibility(View.GONE);
				logooutmenu.setVisibility(View.GONE);
			}
		} else {
			newsmenu.setVisibility(View.GONE);
			logooutmenu.setVisibility(View.GONE);
		}
		pd = ProgressDialog
				.show(this, "dowloading", "dowloading，please wait……");
		isgotonews=bData.getInt("news");
		
		this.threadinfoview = new Thread(inforun);
		threadinfoview.start();
		

		// profiletitle.setTextScaleX(qapp.width/8);

	}

	Runnable newsviewrun = new Runnable() {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			if (newsjson == null) {
				try {
					newsjson = qapp.getJson(qapp.webaddress
							+ "/notifications?ui=" + qapp.getselfid(),
							qapp.httpclient);
					Log.i("", newsjson.toString());
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			newslv = new ListView(Profile.this);

			FrameLayout.LayoutParams flpimg = new FrameLayout.LayoutParams(
					qapp.width, qapp.height - qapp.width / 4 - 20);
			flpimg.gravity = Gravity.TOP;
			newslv.setLayoutParams(flpimg);
			newslv.setBackgroundColor(Color.BLACK);
			loadnewslistview();

			Message msg = new Message();
			msg.what = 1;
			handlenewsview.sendMessage(msg);

		}

	};
	Handler handlenewsview = new Handler() {
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			newslv.setAdapter(mewsadapter);
			Profile.this.contentview.addView(newslv);

			pd.dismiss();
			if (sm.isMenuShowing())
				sm.toggle();

			newslv.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int position, long arg3) {
					// TODO Auto-generated method stub
					Profile.this.uid = newsitems.get(position).getsenderid();
					uidjson = null;
					videojson = null;
					fansjson = null;
					clearallview();
					if (qapp.getselfid() != null) {
						if (qapp.getselfid().equals(uid)) {
							LinearLayout.LayoutParams flp = new LinearLayout.LayoutParams(
									qapp.width / 4, qapp.width / 4);

							newsmenu.setLayoutParams(flp);
							Picasso.with(Profile.this).load(R.drawable.news)
									.resize(qapp.width / 4, qapp.width / 4)
									.into(newsmenu);
							newsmenu.setOnClickListener(new View.OnClickListener() {

								@Override
								public void onClick(View v) {
									// TODO Auto-generated method stub
									if (newslv == null) {
										clearallview();
										pd = ProgressDialog.show(Profile.this,
												"dowloading",
												"dowloading，please wait……");
										Profile.this.threadnewsview = new Thread(
												newsviewrun);
										Profile.this.threadnewsview.start();

									} else {
										sm.toggle();
									}

								}
							});

							logooutmenu.setLayoutParams(flp);
							Picasso.with(Profile.this).load(R.drawable.logout)
									.resize(qapp.width / 4, qapp.width / 4)
									.into(logooutmenu);

							logooutmenu
									.setOnClickListener(new View.OnClickListener() {

										@Override
										public void onClick(View v) {
											// TODO Auto-generated method stub
											sm.toggle();
											willreload = true;
											finish();
										}
									});
						} else {
							newsmenu.setVisibility(View.GONE);
							logooutmenu.setVisibility(View.GONE);
						}
					} else {
						newsmenu.setVisibility(View.GONE);
						logooutmenu.setVisibility(View.GONE);
					}
					pd = ProgressDialog.show(Profile.this, "dowloading",
							"dowloading，please wait……");
					Profile.this.threadinfoview = new Thread(inforun);
					threadinfoview.start();
				}

			});
		}

	};

	void loadnewslistview() {
		newsitems = new ArrayList<newsiterm>();

		int showrowsno = 0;
		try {
			showrowsno = newsjson.getInt("total");
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		for (int i = 0; i < showrowsno; i++) {

			String photourl = null;
			String sendername = null;
			String message = null;
			String senderid = null;
			String msgtype = null;

			// String imageurl = null;
			// String fansid = null;

			try {
				photourl = qapp.webaddress
						+ newsjson.getJSONArray("items").getJSONObject(i)
								.getString("authorProfileImageUrl");
				sendername = newsjson.getJSONArray("items").getJSONObject(i)
						.getString("authorName")
						+ newsjson.getJSONArray("items").getJSONObject(i)
								.getString("authorSurname");
				message = newsjson.getJSONArray("items").getJSONObject(i)
						.getString("customMessage");
				senderid = newsjson.getJSONArray("items").getJSONObject(i)
						.getString("authorId");
				msgtype = newsjson.getJSONArray("items").getJSONObject(i)
						.getString("type");

				newsiterm item = new newsiterm(photourl, sendername, message,
						senderid, msgtype);

				newsitems.add(item);

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			;

			photourl = null;
			sendername = null;
			;
			message = null;
			senderid = null;
			msgtype = null;

		}

		mewsadapter = new Newsadapter(this, newsitems, this);
	}

	Runnable fansviewrun = new Runnable() {

		@Override
		public void run() {
			// TODO Auto-generated method stub

			if (fansjson == null) {
				try {
					fansjson = qapp.getJson(qapp.webaddress + "/fansof?ui="
							+ uid, qapp.httpclient);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			fansview = new GridView(Profile.this);

			FrameLayout.LayoutParams flpimg = new FrameLayout.LayoutParams(
					qapp.width / 4 * 3 + 20, qapp.height - qapp.width / 4 - 20);
			flpimg.gravity = Gravity.CENTER_HORIZONTAL;
			fansview.setLayoutParams(flpimg);

			loadfansgridview();

			Message msg = new Message();
			msg.what = 1;
			handlefansview.sendMessage(msg);
		}

	};

	private Handler handlefansview = new Handler() {
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			// fansview.setColumnWidth(qapp.width/3);
			fansview.setNumColumns(3);
			fansview.setVerticalSpacing(10);
			fansview.setHorizontalSpacing(10);
			fansview.setAdapter(fansadapter1);
			/****** hi ******/
			Log.i("", "sdfhi11111111111111111");
			Profile.this.contentview.addView(fansview);
			pd.dismiss();
			if (sm.isMenuShowing())
				sm.toggle();
			fansview.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int position, long arg3) {
					// TODO Auto-generated method stub
					Profile.this.uid = fansItems.get(position).getfansid();
					uidjson = null;
					videojson = null;
					fansjson = null;
					clearallview();
					if (qapp.getselfid() != null) {
						if (qapp.getselfid().equals(uid)) {
							LinearLayout.LayoutParams flp = new LinearLayout.LayoutParams(
									qapp.width / 4, qapp.width / 4);

							newsmenu.setLayoutParams(flp);
							Picasso.with(Profile.this).load(R.drawable.news)
									.resize(qapp.width / 4, qapp.width / 4)
									.into(newsmenu);
							newsmenu.setOnClickListener(new View.OnClickListener() {

								@Override
								public void onClick(View v) {
									// TODO Auto-generated method stub
									if (newslv == null) {
										clearallview();
										pd = ProgressDialog.show(Profile.this,
												"dowloading",
												"dowloading，please wait……");
										Profile.this.threadnewsview = new Thread(
												newsviewrun);
										Profile.this.threadnewsview.start();

									} else {
										sm.toggle();
									}

								}
							});

							logooutmenu.setLayoutParams(flp);
							Picasso.with(Profile.this).load(R.drawable.logout)
									.resize(qapp.width / 4, qapp.width / 4)
									.into(logooutmenu);

							logooutmenu
									.setOnClickListener(new View.OnClickListener() {

										@Override
										public void onClick(View v) {
											// TODO Auto-generated method stub
											sm.toggle();
											willreload = true;
											finish();
										}
									});
						} else {
							newsmenu.setVisibility(View.GONE);
							logooutmenu.setVisibility(View.GONE);
						}
					} else {
						newsmenu.setVisibility(View.GONE);
						logooutmenu.setVisibility(View.GONE);
					}
					pd = ProgressDialog.show(Profile.this, "dowloading",
							"dowloading，please wait……");
					Profile.this.threadinfoview = new Thread(inforun);
					threadinfoview.start();
				}

			});

		}
	};

	private void loadfansgridview() {

		fansItems = new ArrayList<fansiterm>();

		int showrowsno = 0;
		try {
			showrowsno = fansjson.getInt("total");
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		for (int i = 0; i < showrowsno; i++) {
			String imageurl = null;
			String fansid = null;

			try {
				imageurl = qapp.webaddress
						+ fansjson.getJSONArray("items").getJSONObject(i)
								.getString("profileImageUrl");
				fansid = fansjson.getJSONArray("items").getJSONObject(i)
						.getString("id");

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			;

			fansiterm item = new fansiterm(imageurl, fansid);

			fansItems.add(item);
			imageurl = null;
			fansid = null;

		}

		fansadapter1 = new fansadapter(this, fansItems, this);

	}

	Runnable profileviewrun = new Runnable() {

		@Override
		public void run() {
			// TODO Auto-generated method stub

			lvprofile = new ListView(Profile.this);
			FrameLayout.LayoutParams flpimg = new FrameLayout.LayoutParams(
					qapp.width, qapp.height - qapp.width / 4 - 20);
			flpimg.gravity = Gravity.TOP;
			lvprofile.setLayoutParams(flpimg);
			lvprofile.setBackgroundColor(Color.BLACK);
			loadlistview();

			Message msg = new Message();
			msg.what = 1;
			handleprofileview.sendMessage(msg);
		}

	};
	private Handler handleprofileview = new Handler() {
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			lvprofile.setAdapter(mainadapter);
			Profile.this.contentview.addView(lvprofile);
			lvprofile.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View viewitem,
						int position, long arg3) {
					// TODO Auto-generated method stub
					Log.i("", "" + position);
					if (player != null) {
						contentview.removeView(player);
						player.destroyDrawingCache();
						player.destroytheview();
						player = null;
					}

					int location[] = { -1, -1 };
					int pos[] = { -1, -1 };
					lvprofile.getLocationOnScreen(location);
					viewitem.getLocationOnScreen(pos);
					Log.i("", "" + location[1]);
					Log.i("", "" + pos[1]);

					if (pos[1] < location[1]) {
						lvprofile.setSelection(position);
						Log.i("pos", "poss" + pos[1]);
						pos[1] = location[1];
					}
					Log.i("pos", "poss2" + pos[1]);

					player = new Playerlayout(Profile.this, qapp, videojson,
							position + 1, true, "");
					FrameLayout.LayoutParams flp = new FrameLayout.LayoutParams(
							qapp.width, qapp.width / 16 * 9);
					flp.topMargin = pos[1] - location[1];
					posy = flp.topMargin;
					sm.addIgnoredView(player);

					setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);

					contentview.addView(player, flp);
					contentview.bringChildToFront(player);

				}

			});

			lvprofile.setOnScrollListener(new OnScrollListener() {

				@Override
				public void onScroll(AbsListView view, int firstVisibleItem,
						int visibleItemCount, int totalItemCount) {
					// TODO Auto-generated method stub

				}

				@Override
				public void onScrollStateChanged(AbsListView view,
						int scrollState) {
					// TODO Auto-generated method stub
					if (player != null) {
						contentview.removeView(player);
						player.destroyDrawingCache();
						player.destroytheview();

						player = null;
					}
				}

			});
			pd.dismiss();
			if (sm.isMenuShowing())
				sm.toggle();

		}
	};

	private void loadlistview() {

		rowItems = new ArrayList<RowItem>();

		int showrowsno = 0;
		try {
			showrowsno = videojson.getInt("totalResults");
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
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
				imageurl = videojson.getJSONArray("items").getJSONObject(i)
						.getString("imageUrl320");
				videowriter = videojson.getJSONArray("items").getJSONObject(i)
						.getString("ownerName")
						+ " "
						+ videojson.getJSONArray("items").getJSONObject(i)
								.getString("ownerSurname");
				videoname = videojson.getJSONArray("items").getJSONObject(i)
						.getString("title");

				votecount = videojson.getJSONArray("items").getJSONObject(i)
						.getInt("votesCount");
				videowiterid = videojson.getJSONArray("items").getJSONObject(i)
						.getString("ownerId");
				videoid = videojson.getJSONArray("items").getJSONObject(i)
						.getString("id");
				videourl = videojson.getJSONArray("items").getJSONObject(i)
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

		mainadapter = new page2adapter(this, rowItems, this);

	}

	Runnable inforun = new Runnable() {

		@Override
		public void run() {
			// TODO Auto-generated method stub

			if (uidjson == null || videojson == null) {
				try {
					uidjson = qapp
							.getJson(qapp.webaddress
									+ "/profile/profileinfo?id=" + uid,
									qapp.httpclient);
					videojson = qapp
							.getJson(
									qapp.webaddress
											+ "/video/search?ss=artist&so=most_voted&sp="
											+ uid, qapp.httpclient);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			Message msg = new Message();
			msg.what = 1;
			handlerinfoview.sendMessage(msg);
		}

	};

	private Handler handlerinfoview = new Handler() {
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			String writername = null;

			aq.id(infomenu).image(
					qapp.webaddress + "/avatar/" + uid + "/picture",
					qapp.getmemCache(), qapp.getfileCache(), qapp.width / 4, 0);

			try {
				writername = uidjson.getString("name") + " "
						+ uidjson.getString("surname");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Profile.this.profiletitle.setText(writername);
			String temp = "RAIL0cx5";
			try {
				temp = videojson.getJSONArray("items").getJSONObject(0)
						.getString("videoKey");

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				temp = "RAIL0cx5";
				// e.printStackTrace();
			}
			infoview = new Infoview(Profile.this, qapp, uidjson, temp);
			Profile.this.contentview.addView(infoview);
			infoview.profilevideoimg
					.setOnClickListener(new View.OnClickListener() {

						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							if (player != null) {
								contentview.removeView(player);
								player.destroyDrawingCache();
								player.destroytheview();
								player = null;
							}

							String videocode = "RAIL0cx5";
							try {
								videocode = videojson.getJSONArray("items")
										.getJSONObject(0).getString("videoKey");
							} catch (JSONException e) {
								// TODO Auto-generated catch block
								videocode = "RAIL0cx5";
							}

							player = new Playerlayout(Profile.this, qapp, null,
									0, false, videocode);
							setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
							sm.addIgnoredView(player);

							FrameLayout.LayoutParams flp = new FrameLayout.LayoutParams(
									qapp.width, qapp.width / 16 * 9);
							flp.topMargin = 0;
							posy = flp.topMargin;

							contentview.addView(player, flp);
							contentview.bringChildToFront(player);
						}
					});
			pd.dismiss();
			if (sm.isMenuShowing())
				sm.toggle();
			if(isgotonews==1){
				isgotonews=0;
				newsmenu.performClick();
			}


		}
		
			};

	public void finish() {
		// TODO Auto-generated method stub

		sm.destroyDrawingCache();
		clearallview();
		sm = null;
		infoview = null;

		Intent intent = new Intent(); // 創建一個Intent，聯繫Activity之用
		Bundle bundleBack = new Bundle(); // 創建一個Bundle，傳值之用
		bundleBack.putBoolean("reloadmenu", willreload);
		intent.putExtras(bundleBack);
		setResult(RESULT_OK, intent);

		super.onDestroy();

		super.finish();

		// 关闭窗体动画显示
		this.overridePendingTransition(R.anim.activity_open_right,
				R.anim.activity_close_right);
	}

	public void clearallview() {

		if (infoview != null) {
			this.contentview.removeView(infoview);
			infoview.destroytheview();
			infoview.destroyDrawingCache();
			infoview = null;
		}
		if (lvprofile != null) {
			this.contentview.removeView(lvprofile);
			this.mainadapter = null;
			this.rowItems.clear();
			this.rowItems = null;
			lvprofile.destroyDrawingCache();
			lvprofile = null;
		}
		if (fansview != null) {
			this.contentview.removeView(fansview);
			this.fansadapter1 = null;
			this.fansItems.clear();
			this.fansItems = null;
			fansview.destroyDrawingCache();
			fansview = null;
		}
		if (player != null) {
			contentview.removeView(player);
			player.destroyDrawingCache();
			player.destroytheview();
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
			player = null;
		}
		if (newslv != null) {
			this.contentview.removeView(newslv);
			this.mewsadapter = null;
			this.newsitems.clear();
			this.newsitems = null;
			newslv.destroyDrawingCache();
			newslv = null;
		}

	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		// TODO Auto-generated method stub
		super.onConfigurationChanged(newConfig);
		if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
			if (player != null) {
				player.setheng();
				if (this.lvprofile != null)
					this.lvprofile.setVisibility(View.GONE);
				if (this.headerlayout != null)
					this.headerlayout.setVisibility(View.GONE);

			}
			sm.setEnabled(false);

		}
		if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
			if (player != null) {
				player.setshu(posy);
				this.headerlayout.setVisibility(View.VISIBLE);
				if (lvprofile != null)
					this.lvprofile.setVisibility(View.VISIBLE);

			}
			sm.setEnabled(true);
			Log.i("info", "landscape2");

		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.profile, menu);
		return true;
	}

}
