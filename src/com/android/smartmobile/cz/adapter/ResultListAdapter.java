/**
 * 
 */
package com.android.smartmobile.cz.adapter;

import java.util.List;

import com.android.smartmobile.cz.R;
import com.android.smartmobile.cz.bean.AddressBean;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * 
 * @ClassName: ListAdapter
 * @Description:
 * @author Administrator
 * @date 2013年8月5日 下午2:01:36
 * @Company: www.shdci.com
 * @Copyright: Copyright (c) 2013 All rights reserved.
 * @version
 */
public class ResultListAdapter extends BaseAdapter {
	private Context mContext;
	private List<AddressBean> mList;

	/**
	 * 
	 */
	public ResultListAdapter(Context context, List<AddressBean> list) {
		// TODO Auto-generated constructor stub
		this.mContext = context;
		this.mList = list;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.Adapter#getCount()
	 */
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mList.size();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.Adapter#getItem(int)
	 */
	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return mList.get(position);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.Adapter#getItemId(int)
	 */
	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.Adapter#getView(int, android.view.View,
	 * android.view.ViewGroup)
	 */
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder;
		if (convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.item_list_info, null);

			holder = new ViewHolder();
			holder.txt_item_list_name = (TextView) convertView
					.findViewById(R.id.txt_item_list_name);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		holder.txt_item_list_name.setText(mList.get(position).getName());
		if (position % 2 == 0) {
			convertView.setBackgroundResource(R.drawable.lv_bg_ou);
		} else {
			convertView.setBackgroundResource(R.drawable.lv_bg);
		}
		return convertView;
	}

	static class ViewHolder {
		TextView txt_item_list_name;
	}

}
