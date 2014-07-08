package com.android.smartmobile.cz.view;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.SoftReference;
import java.util.HashMap;

import com.android.smartmobile.cz.util.BitmapUtils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.ImageView;

/**
 * @author oneRain
 */
public class AsyncBitmapLoader {
	/**
	 * 内存图片软引用缓冲
	 */
	private HashMap<String, SoftReference<Bitmap>> imageCache = null;

	public AsyncBitmapLoader() {
		imageCache = new HashMap<String, SoftReference<Bitmap>>();
	}

	public Bitmap loadBitmap(final ImageView imageView, final String imageURL,
			final ImageCallBack imageCallBack) {
		// 在内存缓存中，则返回Bitmap对象
		if (imageCache.containsKey(imageURL)) {
			SoftReference<Bitmap> reference = imageCache.get(imageURL);
			Bitmap bitmap = reference.get();
			if (bitmap != null) {
				return bitmap;
			}
		} /*
		 * else {
		 */
		/**
		 * 加上一个对本地缓存的查找
		 */
		/*
		 * String bitmapName = imageURL .substring(imageURL.lastIndexOf("/") +
		 * 1); if (bitmapName != null) {
		 * System.out.println("========================" + bitmapName); } else {
		 * System.out.println("========================bitmapName不存在"); } File
		 * cacheDir = new File(Utils.get_SD_PATH + "/oldtree/images/"); //
		 * 若存在本地文件中则直接读取 if (cacheDir.exists()) { File[] cacheFiles =
		 * cacheDir.listFiles(); int i = 0; for (; i < cacheFiles.length; i++) {
		 * if (bitmapName.equals(cacheFiles[i].getName())) { break; } }
		 * 
		 * if (i < cacheFiles.length) { return Utils.decodeFile_50(new
		 * File(Utils.get_SD_PATH + "/oldtree/images/" + bitmapName)); } }
		 * 
		 * }
		 */

		final Handler handler = new Handler() {
			/*
			 * (non-Javadoc)
			 * 
			 * @see android.os.Handler#handleMessage(android.os.Message)
			 */
			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				imageCallBack.imageLoad(imageView, (Bitmap) msg.obj);
			}
		};

		new Thread() {

			public void run() {
				Bitmap bitmap = BitmapUtils.decodeFile_100(new File(imageURL));
				imageCache.put(imageURL, new SoftReference<Bitmap>(bitmap));
				Message msg = handler.obtainMessage(0, bitmap);
				handler.sendMessage(msg);
			}
		}.start();

		return null;
	}

	/**
	 * 回调接口
	 * 
	 * @author onerain
	 */
	public interface ImageCallBack {
		public void imageLoad(ImageView imageView, Bitmap bitmap);
	}
}
