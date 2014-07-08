package com.android.smartmobile.cz.adapter;

import java.util.ArrayList;

import com.android.smartmobile.cz.R;
import com.android.smartmobile.cz.bean.MediaBean;
import com.android.smartmobile.cz.view.AsyncBitmapLoader;

import android.R.integer;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

/**
 * 适配器<br/>
 * <br/>
 * 
 * @Author zhoul
 * @Date 2013-1-3
 */
public class MediaAdapter extends BaseAdapter {
	private Context mContext;
	private int mGalleryItemBackground;
	private ArrayList<MediaBean> list;
	private AsyncBitmapLoader asyncBitmapLoader;

	public MediaAdapter(Context c, ArrayList<MediaBean> list) {
		this.mContext = c;
		this.list = list;
		// 获得Gallery组件的属性
		asyncBitmapLoader = new AsyncBitmapLoader();

	}

	public int getCount() {
		return list.size();
	}

	public Object getItem(int position) {
		return list.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		viewHolder holder;
		if (convertView == null) {
			holder = new viewHolder();
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.media_item_info, null);
			holder.item_image = (ImageView) convertView
					.findViewById(R.id.media_item_image);
			
			convertView.setTag(holder);
		} else {
			holder = (viewHolder) convertView.getTag();
		}

		holder.item_image.setScaleType(ImageView.ScaleType.FIT_CENTER);
		// 设置Gallery组件的背景风格
		// holder.item_image.setBackgroundResource(mGalleryItemBackground);

		Bitmap bitmap = asyncBitmapLoader.loadBitmap(holder.item_image,
				list.get(position).getFILE_PATH(), new AsyncBitmapLoader.ImageCallBack() {
					@Override
					public void imageLoad(ImageView imageView, Bitmap bitmap) {
						// TODO Auto-generated method stub
						if (bitmap == null) {
							// 默认图片
							imageView.setImageResource(R.drawable.ic_launcher);
						} else {
							Bitmap thumb = ThumbnailUtils.extractThumbnail(
									bitmap, 240, 180);
							imageView.setImageBitmap(thumb);
						}

					}
				});

		if (bitmap == null) {
			// 默认图片
			holder.item_image.setImageResource(R.drawable.ic_launcher);
		} else {
			Bitmap thumb = ThumbnailUtils.extractThumbnail(bitmap, 240, 180);
			holder.item_image.setImageBitmap(thumb);
		}

		return convertView;
	}

	static class viewHolder {
		private ImageView item_image;
	}

}
