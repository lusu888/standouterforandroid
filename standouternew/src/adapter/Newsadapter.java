package adapter;

import java.util.List;

import com.androidquery.AQuery;
import com.standouter.standouternew.R;
import com.standouter.standouternew.Standouter;
import com.squareup.picasso.Picasso;

import rowierm.newsiterm;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class Newsadapter extends BaseAdapter {

	private Context context;
	private List<newsiterm> items;
	private Activity activity;

	private Standouter qapp;
	private LayoutInflater mInflater;
	private ViewHolder holder;
	private AQuery aq;

	public Newsadapter(Context context, List<newsiterm> items, Activity activity) {
		this.context = context;
		this.items = items;
		this.activity = activity;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return items.size();
	}

	public void addIterm(newsiterm iterm) {
		items.add(iterm);
	}

	@Override
	public newsiterm getItem(int position) {
		// TODO Auto-generated method stub
		return items.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	public class ViewHolder {
		ImageView imgphoto;
		TextView nametext;
		TextView messagetext;

	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub

		qapp = (Standouter) activity.getApplication();
		aq = new AQuery(context);

		mInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.newsadapter, null);
			holder = new ViewHolder();
			holder.imgphoto = (ImageView) convertView
					.findViewById(R.id.imgnewsphoto);
			holder.nametext = (TextView) convertView
					.findViewById(R.id.newsname);
			holder.messagetext = (TextView) convertView
					.findViewById(R.id.newsmessage);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		RelativeLayout.LayoutParams flp = new RelativeLayout.LayoutParams(
				qapp.width / 4, qapp.width / 4);
		flp.leftMargin = 20;
		// Picasso.with(this.context).load(getItem(position).getphotourl()).resize(qapp.width/4,qapp.width/4).into(holder.imgphoto);

		aq.id(holder.imgphoto)
				.image(getItem(position).getphotourl(), qapp.getmemCache(),
						qapp.getfileCache(), qapp.getwidth() / 4, 0);

		Typeface type = Typeface.createFromAsset(context.getAssets(),
				"fonts/oswald.bold.ttf");

		holder.nametext.setText(getItem(position).getsendername());
		holder.nametext.setTypeface(type);
		holder.messagetext.setText(getItem(position).getmessage());
		type = Typeface.createFromAsset(context.getAssets(),
				"fonts/oswald.light.ttf");

		holder.messagetext.setTypeface(type);
		return convertView;
	}

}
