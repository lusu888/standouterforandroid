package com.standouter.standouternew;

import java.io.IOException;
import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.NodeTraversor;
import org.jsoup.select.NodeVisitor;

import rowierm.galleryiterms;

import com.standouter.standouternew.R;
import com.standouter.standouternew.R.anim;
import com.standouter.standouternew.R.drawable;
import com.standouter.standouternew.R.id;
import com.standouter.standouternew.R.layout;
import com.squareup.picasso.Picasso;


import adapter.galleryadapter;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.widget.VideoView;

import com.tencent.mm.sdk.openapi.SendMessageToWX;
import com.tencent.mm.sdk.openapi.WXMediaMessage;
import com.tencent.mm.sdk.openapi.WXTextObject;


public class Playerlayout extends FrameLayout {
	private com.meetme.android.horizontallistview.HorizontalListView horlistview;
	JSONObject videojson;
	private VideoView videoView;
	private Context context;
	private ProgressBar pb;
	private ImageView sharebtn;
	private ImageView votebtn;
	private ImageView sharefbbtn;
	private ImageView sharewpbtn;
	private ImageView sharewcbtn;
	private Standouter qapp;
	private Boolean isshareshow;
	private Boolean ismain;
	private String code;
	

	View view;
	private ArrayList<galleryiterms> rowItems;
	private galleryadapter galleryadapterplayer;
	private boolean isheng;
	private int position;
	String vidurl = null;
	private boolean iscontrolshow;
	private int vid;
	private Thread btnthread;
	protected Object Votestate;
	protected JSONObject votejson;

	private ProgressDialog pd;

	public Playerlayout(Context context, Standouter qapp, JSONObject videojson,
			int postion, Boolean ismain, String code) {
		super(context);
		// TODO Auto-generated constructor stub

		iscontrolshow = false;

		isheng = false;
		this.position = postion - 1;
		this.qapp = qapp;
		this.context = context;
		this.code = code;
		this.ismain = ismain;
		isshareshow = false;
		this.videojson = videojson;
		this.setDrawingCacheEnabled(true);
		LayoutInflater mInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		view = mInflater.inflate(R.layout.palyer_layout, null);

		FrameLayout.LayoutParams flp = new FrameLayout.LayoutParams(qapp.width,
				qapp.width / 16 * 9);
		view.setFocusable(true);
		view.forceLayout();
		videoView = (VideoView) view.findViewById(R.id.videoView);
		pb = (ProgressBar) view.findViewById(R.id.progressBar1);

		sharebtn = (ImageView) view.findViewById(R.id.sharebtn);

		FrameLayout.LayoutParams flp2 = new FrameLayout.LayoutParams(
				qapp.width / 4, qapp.width / 4);
		flp2.gravity = Gravity.CENTER_VERTICAL;
		flp2.leftMargin = 0;
		sharebtn.setLayoutParams(flp2);
		flp2 = null;
		Picasso.with(context).load(R.drawable.sharea)
				.resize(qapp.width / 4, qapp.width / 4).into(sharebtn);
		sharebtn.setVisibility(View.GONE);
		sharebtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (isshareshow) {
					hidesharecontrol();
				} else {
					if (Playerlayout.this.qapp.getselfid() == null) {

						Toast toast = Toast.makeText(Playerlayout.this.context,
								"Please Login first", Toast.LENGTH_SHORT);
						toast.setGravity(Gravity.BOTTOM
								| Gravity.CENTER_HORIZONTAL, 0, 0);

						toast.show();
						Intent intent = new Intent(Playerlayout.this.context,
								Loginactivity.class);
						Playerlayout.this.context.startActivity(intent);

					} else {

						showsharecontrol();

					}
				}

			}
		});

		votebtn = (ImageView) view.findViewById(R.id.votebtn);
		flp2 = new FrameLayout.LayoutParams(qapp.width / 4, qapp.width / 4);
		flp2.gravity = Gravity.CENTER_VERTICAL;
		flp2.leftMargin = qapp.width * 3 / 4;
		votebtn.setLayoutParams(flp2);
		flp2 = null;
		Picasso.with(context).load(R.drawable.sa)
				.resize(qapp.width / 4, qapp.width / 4).into(votebtn);
		votebtn.setVisibility(View.GONE);
		votebtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				pd = ProgressDialog.show(Playerlayout.this.context,
						"dowloading", "dowloading，please wait……");

				btnthread = new Thread(voterunnable);
				btnthread.start();

			}
		});

		sharefbbtn = (ImageView) view.findViewById(R.id.sharefbbtn);
		flp2 = new FrameLayout.LayoutParams(qapp.width / 6, qapp.width / 4);
		flp2.gravity = Gravity.CENTER_VERTICAL;
		flp2.leftMargin = qapp.width / 4;
		sharefbbtn.setLayoutParams(flp2);
		flp2 = null;
		sharefbbtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (Playerlayout.this.qapp.getfacebooktoken() == null) {
					Toast toast = Toast.makeText(Playerlayout.this.context,
							"Please Login facebook first", Toast.LENGTH_SHORT);
					toast.setGravity(
							Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);

					toast.show();
					Intent intent = new Intent(Playerlayout.this.context,
							Loginactivity.class);
					Playerlayout.this.context.startActivity(intent);
				} else {
					AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
							Playerlayout.this.context);

					// set title
					alertDialogBuilder.setTitle("Share on FaceBook");

					// set dialog message
					alertDialogBuilder
							.setMessage(
									"You will share this video on FaceBook!")
							.setCancelable(false)
							.setPositiveButton("Yes",
									new DialogInterface.OnClickListener() {
										public void onClick(
												DialogInterface dialog, int id) {
											// if this button is clicked, close
											// current activity
											// Contestadapter .fbsharecode();

											dialog.cancel();
											pd = ProgressDialog.show(
													Playerlayout.this.context,
													"dowloading",
													"dowloading，please wait……");

											Thread sharethread = new Thread(
													new Runnable() {

														@Override
														public void run() {
															// TODO
															// Auto-generated
															// method stub

															fbsharecode();
														}

													});
											sharethread.start();

										}
									})
							.setNegativeButton("No",
									new DialogInterface.OnClickListener() {
										public void onClick(
												DialogInterface dialog, int id) {
											// if this button is clicked, just
											// close
											// the dialog box and do nothing
											dialog.cancel();
										}
									});

					// create alert dialog
					AlertDialog alertDialog = alertDialogBuilder.create();

					// show it
					alertDialog.show();
				}
			}
		});
		Picasso.with(context).load(R.drawable.facebook)
				.resize(qapp.width / 6, qapp.width / 4).into(sharefbbtn);
		sharefbbtn.setVisibility(View.GONE);

		sharewpbtn = (ImageView) view.findViewById(R.id.sharewpbtn);
		flp2 = new FrameLayout.LayoutParams(qapp.width / 6, qapp.width / 4);
		flp2.gravity = Gravity.CENTER_VERTICAL;
		flp2.leftMargin = qapp.width / 12 * 5;
		sharewpbtn.setLayoutParams(flp2);
		sharewpbtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				onClickWhatsApp(v);
			}
		});
		flp2 = null;
		Picasso.with(context).load(R.drawable.whatsapp)
				.resize(qapp.width / 6, qapp.width / 4).into(sharewpbtn);
		sharewpbtn.setVisibility(View.GONE);
		
		sharewcbtn = (ImageView) view.findViewById(R.id.sharewcbtn);
		flp2 = new FrameLayout.LayoutParams(qapp.width / 6, qapp.width / 4);
		flp2.gravity = Gravity.CENTER_VERTICAL;
		flp2.leftMargin = qapp.width / 12 * 7;
		sharewcbtn.setLayoutParams(flp2);
		sharewcbtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//onClickWhatsApp(v);
				String text=null;
				try {
					text = " I like this video in STANDOUTER! \n"
							+ Playerlayout.this.qapp.shareweb
							+ "/video?ri="
							+ Playerlayout.this.videojson.getJSONArray("items")
									.getJSONObject(Playerlayout.this.position)
									.getString("id");
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				WXTextObject textObj=new WXTextObject();
				textObj.text="Standoter";
				
				WXMediaMessage msg=new WXMediaMessage();
				msg.mediaObject=textObj;
				msg.description=text;
				SendMessageToWX.Req req=new SendMessageToWX.Req();
				req.transaction=String.valueOf(System.currentTimeMillis());
				req.message=msg;
				
				boolean a= Playerlayout.this.qapp.getWechatApi().sendReq(req);
				Log.i("send","a"+a);
				
			}
		});
		flp2 = null;
		Picasso.with(context).load(R.drawable.wechat)
				.resize(qapp.width / 6, qapp.width / 4).into(sharewcbtn);
		sharewcbtn.setVisibility(View.GONE);

		horlistview = (com.meetme.android.horizontallistview.HorizontalListView) view
				.findViewById(R.id.HorizontalListView);
		int height = (qapp.width - 20) / 2 - qapp.width / 8 - 20;
		int width = height / 9 * 16;
		flp2 = new FrameLayout.LayoutParams(qapp.height + 20, height);
		flp2.bottomMargin = 10;
		flp2.gravity = Gravity.BOTTOM;
		horlistview.setLayoutParams(flp2);
		// horlistview.setDividerHeight(10);
		horlistview.setDividerWidth(10);

		if (ismain) {
			loadgallery();
			horlistview.setAdapter(galleryadapterplayer);
		}
		this.horlistview.setVisibility(View.GONE);

		/*
		 * 
		 * 
		 * galleryview=(Gallery) view.findViewById(R.id.galleryplayer);
		 * galleryview.setPadding(0, 10, 0, 10); galleryview.setSpacing(0);
		 * galleryview.setUnselectedAlpha(1);
		 * galleryview.setVisibility(View.GONE);
		 * 
		 * if(ismain){ loadgallery() ;
		 * galleryview.setAdapter(galleryadapterplayer);
		 * if(galleryadapterplayer.getCount()>=2){ galleryview.setSelection(1);
		 * } }
		 */
		/************************ initial *************************************/

		if (this.ismain == true) {
			try {
				vidurl = videojson.getJSONArray("items")
						.getJSONObject(postion - 1).getString("videoUrl480");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			vidurl = qapp.webaddress + "/videos/" + this.code + "-480.mp4";
		}

		videoView.setVideoURI(Uri.parse(vidurl));
		videoView.requestFocus();
		videoView.start();
		videoView.setOnPreparedListener(new OnPreparedListener() {

			@Override
			public void onPrepared(MediaPlayer mp) {
				// TODO Auto-generated method stub
				pb.setVisibility(View.GONE);
				Playerlayout.this.hidecontrol();
			}
		});

		videoView.setOnTouchListener(new View.OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				int e = event.getAction();
				if (e == MotionEvent.ACTION_DOWN) {
					Log.i("", "yes");
					if (videoView.isPlaying()) {
						videoView.pause();
						if (Playerlayout.this.ismain)
							showcontrol();

					} else {
						videoView.start();
						if (Playerlayout.this.ismain)
							hidecontrol();

					}
					return true;
				}

				return true;
			}

		});
		videoView.setOnCompletionListener(new OnCompletionListener() {

			@Override
			public void onCompletion(MediaPlayer arg0) {
				// TODO Auto-generated method stub
				if (Playerlayout.this.ismain)
					showcontrol();

			}

		});
		/************************ initialend *************************************/

		view.setBackgroundColor(Color.BLACK);

		this.horlistview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int pos,
					long arg3) {
				// TODO Auto-generated method stub
				Playerlayout.this.position = pos;
				try {
					Playerlayout.this.vidurl = Playerlayout.this.videojson
							.getJSONArray("items")
							.getJSONObject(Playerlayout.this.position)
							.getString("videoUrl480");
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				videoView.stopPlayback();

				videoView.setVideoURI(Uri.parse(vidurl));
				videoView.requestFocus();
				videoView.start();
				Playerlayout.this.hidecontrol();
				pb.setVisibility(View.VISIBLE);

			}

		});
		/*
		 * galleryview.setOnItemClickListener(new OnItemClickListener(){
		 * 
		 * @Override public void onItemClick(AdapterView<?> arg0, View arg1, int
		 * pos, long arg3) { // TODO Auto-generated method stub
		 * Playerlayout.this.position=pos; try { Playerlayout.this.vidurl =
		 * Playerlayout
		 * .this.videojson.getJSONArray("items").getJSONObject(Playerlayout
		 * .this.position).getString("videoUrl480"); } catch (JSONException e) {
		 * // TODO Auto-generated catch block e.printStackTrace(); }
		 * videoView.stopPlayback();
		 * 
		 * videoView.setVideoURI(Uri.parse(vidurl)); videoView.requestFocus();
		 * videoView.start(); Playerlayout.this.hidecontrol();
		 * pb.setVisibility(View.VISIBLE);
		 * 
		 * 
		 * }
		 * 
		 * });
		 */

		this.addView(view, flp);

	}

	private Runnable voterunnable = new Runnable() {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			votejson = null;
			try {
				votejson = Playerlayout.this.qapp.getJson(
						Playerlayout.this.qapp.webaddress
								+ "/video/vote?ri="
								+ Playerlayout.this.videojson
										.getJSONArray("items")
										.getJSONObject(
												Playerlayout.this.position)
										.getString("id") + "&vt=STANDOUT",
						Playerlayout.this.qapp.httpclient);
			} catch (IOException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			} catch (JSONException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}
			Votestate = null;
			try {
				Votestate = votejson.getString("result");
			} catch (JSONException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			Message msg = new Message();
			msg.what = 1;
			votehandler.sendMessage(msg);

		}

	};
	private Handler votehandler = new Handler() {
		public void handleMessage(final Message msg) {
			Toast toast;

			if (Playerlayout.this.Votestate.equals("unknown")) {

				toast = Toast.makeText(Playerlayout.this.context,
						"Please Login first", Toast.LENGTH_SHORT);
				toast.setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0,
						0);
				Playerlayout.this.pd.dismiss();

				toast.show();
				Intent intent = new Intent(Playerlayout.this.context,
						Loginactivity.class);
				Playerlayout.this.context.startActivity(intent);

			} else {
				try {

					toast = Toast
							.makeText(Playerlayout.this.context,
									convertNodeToText(Jsoup.parse(votejson
											.getString("message"))),
									Toast.LENGTH_SHORT);
					toast.setGravity(
							Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
					Playerlayout.this.pd.dismiss();
					toast.show();

					// Login.writeFileSdcardFile("/sdcard/as.txt",
					// rowItems.get(position).getVID());
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			Log.i("vote", votejson.toString());
		}
	};
	private JSONObject fbsharestate;

	private void loadgallery() {

		rowItems = new ArrayList<galleryiterms>();

		int showrowsno = 0;
		try {
			showrowsno = videojson.getInt("totalResults");
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		if (showrowsno > 10)
			showrowsno = 10;

		for (int i = 0; i < showrowsno; i++) {
			String imageurl = null;

			boolean showflag = false;
			try {
				imageurl = videojson.getJSONArray("items").getJSONObject(i)
						.getString("imageUrl320");

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			;

			galleryiterms item = new galleryiterms(imageurl);

			rowItems.add(item);

		}

		galleryadapterplayer = new galleryadapter(Playerlayout.this.context,
				rowItems, (Activity) (Playerlayout.this.context));

	}

	private void showsharecontrol() {
		sharefbbtn.setVisibility(View.VISIBLE);
		sharewpbtn.setVisibility(View.VISIBLE);
		sharewcbtn.setVisibility(View.VISIBLE);

		Animation sharebtnappear = (Animation) AnimationUtils.loadAnimation(
				Playerlayout.this.context, R.anim.backfromright2);
		Animation votebtnappear = (Animation) AnimationUtils.loadAnimation(
				Playerlayout.this.context, R.anim.backfromright3);
		Animation sharewcbtnappear = (Animation) AnimationUtils.loadAnimation(
				Playerlayout.this.context, R.anim.backfromright4);

		sharefbbtn.startAnimation(sharebtnappear);
		sharewpbtn.startAnimation(votebtnappear);
		sharewcbtn.startAnimation(sharewcbtnappear);
		isshareshow = true;
	}

	private void hidesharecontrol() {

		Animation sharebtndisappear = (Animation) AnimationUtils.loadAnimation(
				Playerlayout.this.context, R.anim.gotoright2);
		Animation votebtndisappear = (Animation) AnimationUtils.loadAnimation(
				Playerlayout.this.context, R.anim.gotoright3);
		Animation sharewcbtndisappear = (Animation) AnimationUtils.loadAnimation(
				Playerlayout.this.context, R.anim.gotorignt4);
		votebtndisappear.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationEnd(Animation arg0) {
				// TODO Auto-generated method stub
				sharefbbtn.setVisibility(View.GONE);
				sharewpbtn.setVisibility(View.GONE);
				sharewcbtn.setVisibility(View.GONE);

			}

			@Override
			public void onAnimationRepeat(Animation arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onAnimationStart(Animation arg0) {
				// TODO Auto-generated method stub

			}

		});

		sharefbbtn.startAnimation(sharebtndisappear);
		sharewpbtn.startAnimation(votebtndisappear);
		sharewcbtn.startAnimation(sharewcbtndisappear);

		isshareshow = false;
	}

	private void showcontrol() {

		if (!iscontrolshow) {
			sharebtn.setVisibility(View.VISIBLE);
			votebtn.setVisibility(View.VISIBLE);

			Animation sharebtnappear = (Animation) AnimationUtils
					.loadAnimation(Playerlayout.this.context,
							R.anim.backfromleft);
			Animation votebtnappear = (Animation) AnimationUtils.loadAnimation(
					Playerlayout.this.context, R.anim.backfromright);

			sharebtn.startAnimation(sharebtnappear);
			votebtn.startAnimation(votebtnappear);

			if (isheng) {
				this.horlistview.setVisibility(View.VISIBLE);
				// this.galleryview.setVisibility(View.VISIBLE);
				Animation gaappear = (Animation) AnimationUtils.loadAnimation(
						Playerlayout.this.context, R.anim.backfromdown);
				this.horlistview.startAnimation(gaappear);
				// galleryview.startAnimation(gaappear);

			}
		}
		iscontrolshow = true;
	}

	private void hidecontrol() {

		if (iscontrolshow) {
			if (isshareshow)
				hidesharecontrol();
			Animation sharebtnappear = (Animation) AnimationUtils
					.loadAnimation(Playerlayout.this.context, R.anim.goleftone);
			Animation votebtndisappear = (Animation) AnimationUtils
					.loadAnimation(Playerlayout.this.context, R.anim.goright1);

			sharebtnappear.setAnimationListener(new AnimationListener() {

				@Override
				public void onAnimationEnd(Animation arg0) {
					// TODO Auto-generated method stub
					sharebtn.setVisibility(View.GONE);
					votebtn.setVisibility(View.GONE);
					horlistview.setVisibility(View.GONE);
					// galleryview.setVisibility(View.GONE);

				}

				@Override
				public void onAnimationRepeat(Animation animation) {
					// TODO Auto-generated method stub

				}

				@Override
				public void onAnimationStart(Animation animation) {
					// TODO Auto-generated method stub

				}

			});
			sharebtn.startAnimation(sharebtnappear);
			votebtn.startAnimation(votebtndisappear);

			if (isheng) {
				Animation gadisappear = (Animation) AnimationUtils
						.loadAnimation(Playerlayout.this.context, R.anim.godown);
				this.horlistview.startAnimation(gadisappear);
				// galleryview.startAnimation(gadisappear);
			}
		}
		iscontrolshow = false;

	}

	public Boolean isplaying() {
		if (videoView != null)
			return videoView.isPlaying();
		else
			return false;
	}

	public void play() {
		if (videoView != null)
			videoView.resume();
	}

	public void pause() {
		if (videoView != null)
			videoView.pause();
	}

	public Playerlayout(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public Playerlayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public void destroytheview() {

		this.destroyDrawingCache();

		videoView.stopPlayback();
		videoView.destroyDrawingCache();
		videoView = null;
		videojson = null;
		// galleryview.destroyDrawingCache();
		// galleryview=null;

		sharebtn.destroyDrawingCache();
		votebtn.destroyDrawingCache();
		sharefbbtn.destroyDrawingCache();
		sharewpbtn.destroyDrawingCache();
		sharebtn = null;
		votebtn = null;
		sharefbbtn = null;
		sharewpbtn = null;

	}

	public void setviewlayout(FrameLayout.LayoutParams flp) {
		this.setLayoutParams(flp);
		view.setLayoutParams(flp);

	}

	public void setheng() {

		isheng = true;
		FrameLayout.LayoutParams flp = new FrameLayout.LayoutParams(
				qapp.height + 20, qapp.width - 20);
		flp.topMargin = -30;
		view.setLayoutParams(flp);
		FrameLayout.LayoutParams flp2 = new FrameLayout.LayoutParams(
				qapp.height + 20, qapp.width - 20);
		flp2.topMargin = 0;
		this.setLayoutParams(flp2);

		this.hidecontrol();

		flp2 = new FrameLayout.LayoutParams(qapp.width / 4, qapp.width / 4);
		flp2.gravity = Gravity.CENTER_VERTICAL;
		flp2.leftMargin = 20;
		sharebtn.setLayoutParams(flp2);
		flp2 = null;

		flp2 = new FrameLayout.LayoutParams(qapp.width / 4, qapp.width / 4);
		flp2.gravity = Gravity.CENTER_VERTICAL;
		flp2.leftMargin = qapp.height - qapp.width / 4;
		votebtn.setLayoutParams(flp2);
		flp2 = null;
		votebtn.setVisibility(View.GONE);

		flp2 = new FrameLayout.LayoutParams(qapp.width / 6, qapp.width / 4);
		flp2.gravity = Gravity.CENTER_VERTICAL;
		flp2.leftMargin = qapp.width / 4 + 20 * 2;
		sharefbbtn.setLayoutParams(flp2);
		flp2 = null;
		sharefbbtn.setVisibility(View.GONE);

		flp2 = new FrameLayout.LayoutParams(qapp.width / 6, qapp.width / 4);
		flp2.gravity = Gravity.CENTER_VERTICAL;
		flp2.leftMargin = qapp.width / 12 * 5 + 20 * 3;
		sharewpbtn.setLayoutParams(flp2);
		flp2 = null;
		sharewpbtn.setVisibility(View.GONE);

		if (!videoView.isPlaying() && ismain)
			this.showcontrol();

	}

	public void setshu(int topm) {

		isheng = false;

		// this.galleryview.setVisibility(View.GONE);
		this.horlistview.setVisibility(View.GONE);
		FrameLayout.LayoutParams flp = new FrameLayout.LayoutParams(qapp.width,
				qapp.width / 16 * 9);
		flp.topMargin = 0;
		view.setLayoutParams(flp);
		FrameLayout.LayoutParams flp2 = new FrameLayout.LayoutParams(
				qapp.width, qapp.width / 16 * 9);
		flp2.topMargin = topm;
		this.setLayoutParams(flp2);

		this.hidecontrol();

		flp2 = new FrameLayout.LayoutParams(qapp.width / 4, qapp.width / 4);
		flp2.gravity = Gravity.CENTER_VERTICAL;
		flp2.leftMargin = 0;
		sharebtn.setLayoutParams(flp2);
		flp2 = null;

		flp2 = new FrameLayout.LayoutParams(qapp.width / 4, qapp.width / 4);
		flp2.gravity = Gravity.CENTER_VERTICAL;
		flp2.leftMargin = qapp.width * 3 / 4;
		votebtn.setLayoutParams(flp2);
		flp2 = null;
		votebtn.setVisibility(View.GONE);

		flp2 = new FrameLayout.LayoutParams(qapp.width / 6, qapp.width / 4);
		flp2.gravity = Gravity.CENTER_VERTICAL;
		flp2.leftMargin = qapp.width / 4;
		sharefbbtn.setLayoutParams(flp2);
		flp2 = null;
		sharefbbtn.setVisibility(View.GONE);

		flp2 = new FrameLayout.LayoutParams(qapp.width / 6, qapp.width / 4);
		flp2.gravity = Gravity.CENTER_VERTICAL;
		flp2.leftMargin = qapp.width / 12 * 5;
		sharewpbtn.setLayoutParams(flp2);
		flp2 = null;
		sharewpbtn.setVisibility(View.GONE);
		if (!videoView.isPlaying() && ismain)
			this.showcontrol();

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

	public void fbsharecode() {

		String link;
		fbsharestate = null;
		try {
			link = qapp.shareweb
					+ "/video?ri="
					+ videojson.getJSONArray("items")
							.getJSONObject(Playerlayout.this.position)
							.getString("id");
			;
			;
			String picture = videojson.getJSONArray("items")
					.getJSONObject(Playerlayout.this.position)
					.getString("imageUrl320");
			String des = "I%20like%20the%20video%20"
					+ videojson.getJSONArray("items")
							.getJSONObject(Playerlayout.this.position)
							.getString("ownerName") + "%20in%20Standouter!";

			fbsharestate = qapp.getJson(
					"https://graph.facebook.com/me/feed?method=POST&picture="
							+ picture + "&link=" + link + "&message=" + des
							+ "&access_token=" + qapp.getfacebooktoken(),
					qapp.httpclient);
			Log.i("", fbsharestate.toString());

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		pd.dismiss();

		Message msg = new Message();
		msg.what = 1;
		fbsharehandler.sendMessage(msg);

	}

	private Handler fbsharehandler = new Handler() {
		public void handleMessage(final Message msg) {
			try {
				if (fbsharestate.get("id").toString() != null) {

					Toast toast;
					toast = Toast.makeText(Playerlayout.this.context,
							"You have shared it!", Toast.LENGTH_SHORT);
					toast.setGravity(
							Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
					toast.show();
				} else {
					Toast toast;
					toast = Toast.makeText(Playerlayout.this.context,
							"share facebook does not sucecss!",
							Toast.LENGTH_SHORT);
					toast.setGravity(
							Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
					toast.show();
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block

				Toast toast;
				toast = Toast.makeText(Playerlayout.this.context,
						"share facebook does not sucecss!", Toast.LENGTH_SHORT);
				toast.setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0,
						0);
				toast.show();
				// MainActivity.this.finish();
				// convertView2.getContext().overridePendingTransition(R.anim.activity_open,R.anim.activity_close);

			}
			Playerlayout.this.hidesharecontrol();

		}
	};

	public void onClickWhatsApp(View view) {
		Playerlayout.this.hidesharecontrol();

		Intent waIntent = new Intent(Intent.ACTION_SEND);
		waIntent.setType("text/plain");
		String text = null;
		try {
			text = " I like this video in STANDOUTER! \n"
					+ qapp.shareweb
					+ "/video?ri="
					+ videojson.getJSONArray("items")
							.getJSONObject(Playerlayout.this.position)
							.getString("id");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		waIntent.setPackage("com.whatsapp");
		if (waIntent != null) {
			waIntent.putExtra(Intent.EXTRA_TEXT, text);//
			context.startActivity(Intent.createChooser(waIntent, "Share with"));
		} else {
			Toast.makeText(Playerlayout.this.context, "WhatsApp not Installed",
					Toast.LENGTH_SHORT).show();
		}

	}
}
