package adapter;

import java.util.List;

import com.androidquery.AQuery;
import com.standouter.standouternew.R;
import com.standouter.standouternew.Standouter;

import rowierm.galleryiterms;

import adapter.fansadapter.ViewHolder;
import android.app.Activity;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

public class galleryadapter extends BaseAdapter {

	private Context context;
	private List<galleryiterms> iterms;
	private Activity activity;
	private LayoutInflater mInflater;
	private ViewHolder holder;

	public galleryadapter(Context context, List<galleryiterms> iterms,
			Activity activity) {
		this.context = context;
		this.iterms = iterms;
		this.activity = activity;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return iterms.size();
	}

	@Override
	public galleryiterms getItem(int position) {
		// TODO Auto-generated method stub
		return iterms.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	public class ViewHolder {
		ImageView galleryimg;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub

		AQuery aq = new AQuery(context);

		Standouter qapp = (Standouter) activity.getApplication();
		// aq = new AQuery(context);

		mInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.galleryadapter, null);
			holder = new ViewHolder();
			holder.galleryimg = (ImageView) convertView
					.findViewById(R.id.imggallery);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		int height = (qapp.width - 20) / 2 - qapp.width / 8 - 20;
		int width = height / 9 * 16;
		FrameLayout.LayoutParams flp = new FrameLayout.LayoutParams(width,
				height);
		flp.gravity = Gravity.CENTER;
		holder.galleryimg.setLayoutParams(flp);
		aq.id(holder.galleryimg).image(getItem(position).getimgurl(),
				qapp.getmemCache(), qapp.getfileCache(), width, 0);

		holder.galleryimg.setScaleType(ScaleType.FIT_XY);
		return convertView;
	}

}
