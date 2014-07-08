package com.android.smartmobile.cz.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
public class BitmapUtils {



	// 遍历SD卡中某一路径下指定类型的图片
	public static ArrayList<String> getImgsFrom(String path) {
		ArrayList<String> it = new ArrayList<String>();

		File f = new File(path);
		if (f.exists()) {
			File[] files = f.listFiles();
			// System.out.print(f.getPath());
			for (int i = 0; i < files.length; i++) {
				File file = files[i];
				// System.out.print(file.getPath());

				if (getImageFile(file.getPath()))
					it.add(file.getPath());
				Log.d("PATH------------", file.getPath());

			}
		} else {

		}
		return it;
	}

	// 指定遍历文件类型
	private static boolean getImageFile(String fName) {
		boolean re;

		String end = fName
				.substring(fName.lastIndexOf(".") + 1, fName.length())
				.toLowerCase();

		if (end.equals("jpg") || end.equals("gif") || end.equals("png")
				|| end.equals("jpeg") || end.equals("bmp")) {
			re = true;
		} else {
			re = false;
		}
		return re;
	}

	// 缩略图
	// decodes image and scales it to reduce memory consumption
	public static Bitmap decodeFile_100(File f) {
		try {
			// Decode image size
			BitmapFactory.Options o = new BitmapFactory.Options();
			o.inJustDecodeBounds = true;
			BitmapFactory.decodeStream(new FileInputStream(f), null, o);

			// The new size we want to scale to
			final int REQUIRED_SIZE = 100;

			// Find the correct scale value. It should be the power of 2.
			int scale = 1;
			while (o.outWidth / scale / 2 >= REQUIRED_SIZE
					&& o.outHeight / scale / 2 >= REQUIRED_SIZE)
				scale *= 2;

			// Decode with inSampleSize
			BitmapFactory.Options o2 = new BitmapFactory.Options();
			o2.inSampleSize = scale;
			return BitmapFactory.decodeStream(new FileInputStream(f), null, o2);
		} catch (FileNotFoundException e) {
		}
		return null;
	}
	
	
    /**
     * 以最省内存的方式读取本地资源的图片
     *
     * @param file
     * @return
     */
    public static Bitmap readBitMapFromFile(File file) throws FileNotFoundException {
        BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inPreferredConfig = Bitmap.Config.RGB_565;
        opt.inPurgeable = true;
        opt.inInputShareable = true;
//获取资源图片
        InputStream is = new FileInputStream(file);
        return BitmapFactory.decodeStream(is, null, opt);
    }


}