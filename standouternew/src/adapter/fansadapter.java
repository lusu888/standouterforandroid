package adapter;

import java.util.List;

import com.androidquery.AQuery;
import com.standouter.standouternew.R;
import com.standouter.standouternew.Standouter;

import rowierm.fansiterm;
import android.app.Activity;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;

public class fansadapter extends BaseAdapter {
	private List<fansiterm> rowItems;
	private Standouter qapp;
	private Context context;
	private Activity activity;
	private LayoutInflater mInflater;
	private ViewHolder holder;
	private AQuery aq;

	public class ViewHolder {
		ImageView fansphoto;

	}

	public fansadapter(Context context, List<fansiterm> rowItems,
			Activity activity) {

		this.context = context;
		this.rowItems = rowItems;
		this.activity = activity;
	}

	public void addItem(fansiterm item) {
		rowItems.add(item);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return rowItems.size();
	}

	@Override
	public fansiterm getItem(int position) {
		// TODO Auto-generated method stub
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
		aq = new AQuery(context);

		qapp = (Standouter) activity.getApplication();
		// aq = new AQuery(context);

		mInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.fansadpater, null);
			holder = new ViewHolder();
			holder.fansphoto = (ImageView) convertView
					.findViewById(R.id.fansphoto);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		FrameLayout.LayoutParams flp = new FrameLayout.LayoutParams(
				qapp.width / 4, qapp.width / 4);
		flp.gravity = Gravity.CENTER;
		holder.fansphoto.setLayoutParams(flp);
		aq.id(holder.fansphoto)
				.image(getItem(position).getfansphotourl(), qapp.getmemCache(),
						qapp.getfileCache(), qapp.getwidth() / 4, 0);

		return convertView;
	}

}
