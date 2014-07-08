package com.android.smartmobile.cz.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import com.android.smartmobile.cz.R;
import com.android.smartmobile.cz.bean.LegendBean;

/**
 * Gridview适配器<br/>
 * <br/>
 * 
 * @Author zhoul
 * @Date 2013-1-3
 */
public class GridviewAdapter extends BaseAdapter {
	private Context context;
	private List<LegendBean> imageURL;

	private int index;

	public GridviewAdapter(Context c, List<LegendBean> imageURL) {
		this.context = c;
		this.imageURL = imageURL;

	}

	@Override
	public int getCount() {
		return imageURL.size();
	}

	@Override
	public Object getItem(int position) {
		return imageURL.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = LayoutInflater.from(context).inflate(
					R.layout.item_legend_info, null);
			holder.txt_gv_item_image = (ImageView) convertView
					.findViewById(R.id.txt_gv_item_image);
			holder.txt_gv_item_name = (TextView) convertView
					.findViewById(R.id.txt_gv_item_name);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		holder.txt_gv_item_image.setBackgroundColor(imageURL.get(position).getInitColor());
		holder.txt_gv_item_name.setText(imageURL.get(position).getName());

		return convertView;
	}

	static class ViewHolder {
		private ImageView txt_gv_item_image;
		private TextView txt_gv_item_name;

	}

}