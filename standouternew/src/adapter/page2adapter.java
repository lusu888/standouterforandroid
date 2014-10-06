package adapter;

import java.util.List;

import rowierm.RowItem;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Typeface;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.standouter.standouternew.R;
import com.standouter.standouternew.Standouter;

public class page2adapter extends BaseAdapter {
	public Context context;
	RowItem rowItem;
	Activity activity;
	List<RowItem> rowItems;
	Thread gotoprofilethread;
	String chooseuserid;

	public page2adapter(Context context, List<RowItem> items, Activity activity) {
		this.context = context;
		this.rowItems = items;
		this.activity = activity;
	}

	/* private view holder class */
	public class ViewHolder {
		ImageView videoimg;
		TextView videowriter;
		TextView videoname;
		TextView votecount;

	}

	public ViewHolder holder;
	private AQuery aq;
	private LayoutInflater mInflater;
	private Standouter qapp;
	protected ProgressDialog pd;

	public void addItem(RowItem item) {
		rowItems.add(item);
	}

	@Override
	public int getCount() {
		return rowItems.size();
	}

	@Override
	public RowItem getItem(int position) {
		return rowItems.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		qapp = (Standouter) activity.getApplication();
		aq = new AQuery(context);
		mInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.page1adapter, null);

			holder = new ViewHolder();
			holder.videoimg = (ImageView) convertView
					.findViewById(R.id.videoimg);
			holder.videoname = (TextView) convertView
					.findViewById(R.id.videotitle);
			holder.videowriter = (TextView) convertView
					.findViewById(R.id.videowriter);
			holder.votecount = (TextView) convertView
					.findViewById(R.id.votecount);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		FrameLayout.LayoutParams flp = new FrameLayout.LayoutParams(qapp.width,
				qapp.width / 16 * 9);
		flp.gravity = Gravity.CENTER_HORIZONTAL;
		holder.videoimg.setLayoutParams(flp);
		flp = null;
		aq.id(holder.videoimg).image(getItem(position).getimageurl(),
				qapp.getmemCache(), qapp.getfileCache(), qapp.getwidth(), 0);
		holder.videoimg.setScaleType(ImageView.ScaleType.FIT_XY);
		holder.videoname.setText(getItem(position).getvideoname());
		holder.videowriter.setText(getItem(position).getvideowriterl());
		Typeface type = Typeface.createFromAsset(context.getAssets(),
				"fonts/oswald.regular.ttf");
		holder.videowriter.setTypeface(type);
		type = Typeface.createFromAsset(context.getAssets(),
				"fonts/oswald.light.ttf");

		holder.videoname.setTypeface(type);
		/*
		 * holder.videowriter.setOnClickListener(new View.OnClickListener() {
		 * 
		 * @Override public void onClick(View v) { // TODO Auto-generated method
		 * stub pd = ProgressDialog.show(context, "Processing",
		 * "Processing，please wait……");
		 * 
		 * 
		 * 
		 * 
		 * gotoprofilethread= new Thread(new Runnable(){
		 * 
		 * @Override public void run() { // TODO Auto-generated method stub
		 * page1adapter.this.chooseuserid=getItem(position).getvideowiterid();
		 * Log.i("",page1adapter.this.chooseuserid); Message msg= new Message();
		 * msg.what=1; handlerfrobtn.sendMessage(msg);
		 * 
		 * }
		 * 
		 * }); gotoprofilethread.start();
		 * 
		 * 
		 * 
		 * } });
		 */
		holder.votecount.setText("" + getItem(position).getvotecount());

		if (getItem(position).getvotecount() != 0) {
			type = Typeface.createFromAsset(context.getAssets(),
					"fonts/oswald.bold.ttf");

			holder.votecount.setText("" + getItem(position).getvotecount());
			holder.votecount.setTypeface(type);
		} else {
			holder.votecount.setVisibility(View.GONE);

		}

		return convertView;

	}

}
